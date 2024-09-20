/*     */ package by.gdev.component;
/*     */ 
/*     */ import by.gdev.Main;
/*     */ import by.gdev.handler.UpdateCore;
/*     */ import by.gdev.handler.ValidateEnvironment;
/*     */ import by.gdev.handler.ValidateFont;
/*     */ import by.gdev.handler.ValidateTempDir;
/*     */ import by.gdev.handler.ValidateTempNull;
/*     */ import by.gdev.handler.ValidateUpdate;
/*     */ import by.gdev.handler.ValidateWorkDir;
/*     */ import by.gdev.handler.ValidatedPartionSize;
/*     */ import by.gdev.http.download.config.HttpClientConfig;
/*     */ import by.gdev.http.download.handler.ArchiveHandler;
/*     */ import by.gdev.http.download.handler.PostHandler;
/*     */ import by.gdev.http.download.handler.PostHandlerImpl;
/*     */ import by.gdev.http.download.impl.DownloaderImpl;
/*     */ import by.gdev.http.download.impl.FileCacheServiceImpl;
/*     */ import by.gdev.http.download.impl.GsonServiceImpl;
/*     */ import by.gdev.http.download.impl.HttpServiceImpl;
/*     */ import by.gdev.http.download.service.FileCacheService;
/*     */ import by.gdev.http.download.service.GsonService;
/*     */ import by.gdev.http.download.service.HttpService;
/*     */ import by.gdev.http.upload.download.downloader.DownloaderContainer;
/*     */ import by.gdev.http.upload.download.downloader.DownloaderJavaContainer;
/*     */ import by.gdev.model.AppConfig;
/*     */ import by.gdev.model.AppLocalConfig;
/*     */ import by.gdev.model.JVMConfig;
/*     */ import by.gdev.model.StarterAppConfig;
/*     */ import by.gdev.process.JavaProcessHelper;
/*     */ import by.gdev.updater.ProgressFrame;
/*     */ import by.gdev.updater.Update;
/*     */ import by.gdev.updater.UpdaterFormController;
/*     */ import by.gdev.util.DesktopUtil;
/*     */ import by.gdev.util.OSInfo;
/*     */ import by.gdev.util.StringVersionComparator;
/*     */ import by.gdev.util.model.download.JvmRepo;
/*     */ import by.gdev.util.model.download.Repo;
/*     */ import by.gdev.utils.service.FileMapperService;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.eventbus.EventBus;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.commons.text.StringSubstitutor;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Starter
/*     */ {
/*  76 */   private static final Logger log = LoggerFactory.getLogger(Starter.class);
/*     */   
/*     */   private EventBus eventBus;
/*     */   
/*     */   private StarterAppConfig starterConfig;
/*     */   
/*     */   private OSInfo.OSType osType;
/*     */   private OSInfo.Arch osArc;
/*     */   private AppConfig remoteAppConfig;
/*     */   private AppConfig remoteAppConfigDefault;
/*     */   private JVMConfig jvm;
/*     */   private Repo dependencis;
/*     */   private ProgressFrame starterStatusFrame;
/*     */   
/*     */   public FileMapperService getFileMapperService() {
/*  91 */     return this.fileMapperService;
/*     */   }
/*     */ 
/*     */   
/*     */   private ResourceBundle bundle;
/*     */   
/*     */   private GsonService gsonService;
/*     */   private RequestConfig requestConfig;
/*     */   private FileMapperService fileMapperService;
/*     */   
/*     */   public Starter(EventBus eventBus, StarterAppConfig starterConfig, ResourceBundle bundle, ProgressFrame starterStatusFrame, AppLocalConfig appLocalConfig, FileMapperService fileMapperService) throws UnsupportedOperationException, IOException, InterruptedException {
/* 102 */     this.osType = OSInfo.getOSType();
/* 103 */     this.osArc = OSInfo.getJavaBit();
/* 104 */     this.eventBus = eventBus;
/* 105 */     this.appLocalConfig = appLocalConfig;
/* 106 */     this.starterStatusFrame = starterStatusFrame;
/* 107 */     this.bundle = bundle;
/* 108 */     this.starterConfig = starterConfig;
/* 109 */     this.fileMapperService = fileMapperService;
/* 110 */     this
/* 111 */       .requestConfig = RequestConfig.custom().setConnectTimeout(starterConfig.getConnectTimeout()).setSocketTimeout(starterConfig.getSocketTimeout()).build();
/*     */     
/* 113 */     int maxAttepmts = DesktopUtil.numberOfAttempts(starterConfig.getUrlConnection(), starterConfig.getMaxAttempts(), this.requestConfig, 
/* 114 */         HttpClientConfig.getInstanceHttpClient());
/* 115 */     this.hasInternet = !(maxAttepmts == 1);
/* 116 */     log.trace("Max attempts from download = " + maxAttepmts);
/* 117 */     HttpServiceImpl httpServiceImpl = new HttpServiceImpl(null, HttpClientConfig.getInstanceHttpClient(), this.requestConfig, maxAttepmts);
/*     */ 
/*     */     
/* 120 */     FileCacheServiceImpl fileCacheServiceImpl = new FileCacheServiceImpl((HttpService)httpServiceImpl, Main.GSON, Main.charset, starterConfig.getCacheDirectory(), starterConfig.getTimeToLife());
/* 121 */     this.gsonService = (GsonService)new GsonServiceImpl(Main.GSON, (FileCacheService)fileCacheServiceImpl, (HttpService)httpServiceImpl);
/* 122 */     this.updateCore = new UpdateCore(bundle, this.gsonService, HttpClientConfig.getInstanceHttpClient(), this.requestConfig);
/*     */   }
/*     */   private String workDir; private boolean hasInternet; private UpdateCore updateCore;
/*     */   private AppLocalConfig appLocalConfig;
/*     */   private JvmRepo java;
/*     */   
/*     */   public void validateEnvironmentAndAppRequirements() throws Exception {
/* 129 */     this.workDir = this.starterConfig.workDir(this.starterConfig.getWorkDirectory(), this.osType).concat("/");
/* 130 */     List<ValidateEnvironment> validateEnvironment = new ArrayList<>();
/* 131 */     validateEnvironment.add(new ValidatedPartionSize(this.starterConfig.getMinMemorySize(), new File(this.workDir), this.bundle));
/* 132 */     validateEnvironment.add(new ValidateWorkDir(this.workDir, this.bundle));
/* 133 */     validateEnvironment.add(new ValidateTempNull(this.bundle));
/* 134 */     validateEnvironment.add(new ValidateTempDir(this.bundle));
/* 135 */     validateEnvironment.add(new ValidateFont(this.bundle));
/* 136 */     validateEnvironment.add(new ValidateUpdate(this.bundle));
/* 137 */     for (ValidateEnvironment val : validateEnvironment) {
/* 138 */       if (!val.validate()) {
/* 139 */         log.error(this.bundle.getString("validate.error") + " " + val.getClass().getName());
/* 140 */         this.eventBus.post(val.getExceptionMessage()); continue;
/*     */       } 
/* 142 */       log.debug(this.bundle.getString("validate.successful") + " " + val.getClass().getName());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepareResources() throws Exception {
/*     */     Repo resources;
/* 152 */     log.info("Start loading");
/* 153 */     log.info(String.valueOf(this.osType));
/* 154 */     log.info(String.valueOf(this.osArc));
/* 155 */     DesktopUtil.activeDoubleDownloadingResourcesLock(this.workDir);
/* 156 */     DownloaderImpl downloaderImpl = new DownloaderImpl(this.eventBus, HttpClientConfig.getInstanceHttpClient(), this.requestConfig);
/* 157 */     DownloaderContainer container = new DownloaderContainer();
/* 158 */     List<String> serverFile = this.starterConfig.getServerFileConfig(this.starterConfig, this.starterConfig.getVersion());
/*     */     
/* 160 */     if (this.hasInternet) {
/* 161 */       log.info("app remote config: {}", serverFile.toString());
/* 162 */       this.remoteAppConfig = (AppConfig)this.gsonService.getObjectByUrls(serverFile, AppConfig.class, false);
/* 163 */       updateApp(this.gsonService, this.fileMapperService);
/* 164 */       this.dependencis = (Repo)this.gsonService.getObjectByUrls(this.remoteAppConfig.getAppDependencies().getRepositories(), this.remoteAppConfig
/* 165 */           .getAppDependencies().getResources(), Repo.class, false);
/* 166 */       resources = (Repo)this.gsonService.getObjectByUrls(this.remoteAppConfig.getAppResources().getRepositories(), this.remoteAppConfig
/* 167 */           .getAppResources().getResources(), Repo.class, false);
/* 168 */       this.jvm = (JVMConfig)this.gsonService.getObjectByUrls(this.remoteAppConfig.getJavaRepo().getRepositories(), this.remoteAppConfig
/* 169 */           .getJavaRepo().getResources(), JVMConfig.class, false);
/*     */     } else {
/* 171 */       log.info("No Internet connection");
/* 172 */       this.remoteAppConfig = (AppConfig)this.gsonService.getLocalObject(Lists.newArrayList(serverFile), AppConfig.class);
/* 173 */       Repo dep = this.remoteAppConfig.getAppDependencies();
/* 174 */       List<String> d = DesktopUtil.generatePath(dep.getRepositories(), dep.getResources());
/* 175 */       this.dependencis = (Repo)this.gsonService.getLocalObject(d, Repo.class);
/* 176 */       Repo res = this.remoteAppConfig.getAppResources();
/* 177 */       List<String> r = DesktopUtil.generatePath(res.getRepositories(), res.getResources());
/* 178 */       resources = (Repo)this.gsonService.getLocalObject(r, Repo.class);
/* 179 */       Repo javaRepo = this.remoteAppConfig.getJavaRepo();
/* 180 */       List<String> j = DesktopUtil.generatePath(javaRepo.getRepositories(), javaRepo.getResources());
/* 181 */       this.jvm = (JVMConfig)this.gsonService.getLocalObject(j, JVMConfig.class);
/*     */     } 
/* 183 */     this.remoteAppConfigDefault = this.remoteAppConfig;
/*     */     
/*     */     try {
/* 186 */       this.appLocalConfig = (AppLocalConfig)this.fileMapperService.read("starter.json", AppLocalConfig.class);
/* 187 */     } catch (Exception e) {
/* 188 */       log.error("can't read default config {}", "starter.json", e);
/*     */     } 
/* 190 */     if (this.appLocalConfig == null) {
/* 191 */       this.appLocalConfig = new AppLocalConfig();
/* 192 */       this.appLocalConfig.setCurrentAppVersion(this.remoteAppConfig.getAppVersion());
/* 193 */       this.starterStatusFrame.setNewVersionText(this.appLocalConfig.getCurrentAppVersion());
/* 194 */       this.fileMapperService.write(this.appLocalConfig, "starter.json");
/*     */     } 
/* 196 */     if (Objects.nonNull(this.starterConfig.getVersion())) {
/* 197 */       this.appLocalConfig.setCurrentAppVersion(this.starterConfig.getVersion());
/* 198 */       this.fileMapperService.write(this.appLocalConfig, "starter.json");
/*     */     } 
/*     */     
/* 201 */     Repo fileRepo = this.remoteAppConfig.getAppFileRepo();
/* 202 */     this.java = (JvmRepo)((Map)((Map)this.jvm.getJvms().get(this.osType)).get(this.osArc)).get(DownloaderJavaContainer.JRE_DEFAULT);
/* 203 */     List<Repo> list = Lists.newArrayList((Object[])new Repo[] { fileRepo, this.dependencis, resources });
/* 204 */     PostHandlerImpl postHandler = new PostHandlerImpl();
/* 205 */     for (Repo repo : list) {
/* 206 */       container.containerAllSize(repo);
/* 207 */       container.filterNotExistResoursesAndSetRepo(repo, this.workDir);
/* 208 */       container.downloadSize(repo, this.workDir);
/* 209 */       container.setDestinationRepositories(this.workDir);
/* 210 */       container.setHandlers(Arrays.asList(new PostHandler[] { (PostHandler)postHandler }));
/* 211 */       downloaderImpl.addContainer(container);
/*     */     } 
/* 213 */     DownloaderJavaContainer jreContainer = new DownloaderJavaContainer(this.fileMapperService, this.workDir, DownloaderJavaContainer.JRE_CONFIG);
/*     */     
/* 215 */     ArchiveHandler archiveHandler = new ArchiveHandler(this.fileMapperService, DownloaderJavaContainer.JRE_CONFIG);
/* 216 */     jreContainer.containerAllSize((Repo)this.java);
/* 217 */     jreContainer.filterNotExistResoursesAndSetRepo((Repo)this.java, this.workDir);
/* 218 */     jreContainer.downloadSize((Repo)this.java, this.workDir);
/* 219 */     jreContainer.setDestinationRepositories(this.workDir);
/* 220 */     jreContainer.setHandlers(Arrays.asList(new PostHandler[] { (PostHandler)postHandler, (PostHandler)archiveHandler }));
/* 221 */     downloaderImpl.addContainer((DownloaderContainer)jreContainer);
/* 222 */     downloaderImpl.startDownload(true);
/* 223 */     DesktopUtil.diactivateDoubleDownloadingResourcesLock();
/* 224 */     log.info("loading is complete");
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateApp(GsonService gsonService, FileMapperService fileMapperService) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
/* 229 */     this.eventBus.register(this.starterStatusFrame);
/* 230 */     StringVersionComparator versionComparator = new StringVersionComparator();
/* 231 */     if (Objects.nonNull(this.appLocalConfig) && versionComparator.compare(this.appLocalConfig.getCurrentAppVersion(), this.remoteAppConfig
/* 232 */         .getAppVersion()) == -1) {
/* 233 */       if (!GraphicsEnvironment.isHeadless())
/*     */       {
/* 235 */         if (this.appLocalConfig.isSkippedVersion(this.remoteAppConfig.getAppVersion())) {
/* 236 */           this.remoteAppConfig = (AppConfig)gsonService.getObjectByUrls(this.starterConfig
/* 237 */               .getServerFileConfig(this.starterConfig, this.appLocalConfig.getCurrentAppVersion()), AppConfig.class, false);
/*     */         } else {
/*     */           
/*     */           try {
/* 241 */             Update update = (Update)gsonService.getObjectByUrls(this.starterConfig.getServerFile(), "/additionalUpdateConfiguration.json", Update.class, false);
/*     */             
/* 243 */             UpdaterFormController view = new UpdaterFormController(update, this.appLocalConfig, this.remoteAppConfig, fileMapperService, gsonService, this.bundle);
/*     */             
/* 245 */             boolean updated = view.getResult(update);
/* 246 */             if (view.getChoose() == -1) {
/* 247 */               System.exit(-1);
/* 248 */             } else if (view.getChoose() == 0 || !updated) {
/* 249 */               this.remoteAppConfig = (AppConfig)gsonService.getObjectByUrls(this.starterConfig.getServerFileConfig(this.starterConfig, this.appLocalConfig
/* 250 */                     .getCurrentAppVersion()), AppConfig.class, true);
/* 251 */               fileMapperService.write(this.appLocalConfig, "starter.json");
/* 252 */             } else if (view.getChoose() == 1) {
/* 253 */               this.appLocalConfig.setCurrentAppVersion(this.remoteAppConfig.getAppVersion());
/* 254 */               fileMapperService.write(this.appLocalConfig, "starter.json");
/*     */             } 
/* 256 */           } catch (Exception e) {
/* 257 */             log.error("error", e);
/*     */           } 
/*     */         } 
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void runApp() throws IOException, InterruptedException {
/* 273 */     log.info("Start application");
/*     */     
/* 275 */     Path jre = DesktopUtil.getJavaRun(Paths.get(this.workDir, new String[] { DownloaderJavaContainer.JRE_DEFAULT, this.java.getJreDirectoryName() }));
/* 276 */     JavaProcessHelper javaProcess = new JavaProcessHelper(String.valueOf(jre), new File(this.workDir), this.eventBus);
/* 277 */     String classPath = DesktopUtil.convertListToString(File.pathSeparator, javaProcess
/* 278 */         .librariesForRunning(this.workDir, this.remoteAppConfig.getAppFileRepo(), this.dependencis));
/* 279 */     javaProcess.addCommands(this.remoteAppConfig.getJvmArguments());
/* 280 */     javaProcess.addCommand("-cp", classPath);
/* 281 */     javaProcess.addCommand(this.remoteAppConfig.getMainClass());
/* 282 */     Map<String, String> map = new HashMap<>();
/* 283 */     map.put("starterConfig", 
/* 284 */         Paths.get(this.workDir, new String[] { "starter.json" }).toAbsolutePath().toString());
/* 285 */     map.put("requireUpdate", "" + (
/* 286 */         !this.appLocalConfig.getCurrentAppVersion().equals(this.remoteAppConfigDefault.getAppVersion()) ? 1 : 0));
/* 287 */     map.put("currentAppVersion", this.appLocalConfig.getCurrentAppVersion());
/* 288 */     StringSubstitutor substitutor = new StringSubstitutor(map);
/* 289 */     javaProcess.addCommands((List)this.remoteAppConfig.getAppArguments().stream().map(s -> substitutor.replace(s))
/* 290 */         .collect(Collectors.toList()));
/* 291 */     javaProcess.start();
/* 292 */     if (this.starterConfig.isStop()) {
/* 293 */       javaProcess.destroyProcess();
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateApplication() {
/*     */     try {
/* 299 */       this.updateCore.checkUpdates(this.osType, this.starterConfig.getStarterUpdateConfig());
/* 300 */     } catch (Exception e) {
/* 301 */       log.error("promlem with update application ", e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\component\Starter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */