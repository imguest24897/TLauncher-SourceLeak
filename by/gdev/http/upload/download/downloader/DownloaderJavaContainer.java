/*     */ package by.gdev.http.upload.download.downloader;
/*     */ 
/*     */ import by.gdev.util.DesktopUtil;
/*     */ import by.gdev.util.model.download.JvmRepo;
/*     */ import by.gdev.util.model.download.Metadata;
/*     */ import by.gdev.util.model.download.Repo;
/*     */ import by.gdev.utils.service.FileMapperService;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class DownloaderJavaContainer
/*     */   extends DownloaderContainer {
/*  23 */   private static final Logger log = LoggerFactory.getLogger(DownloaderJavaContainer.class);
/*     */   
/*     */   private FileMapperService fileMapperService;
/*     */   
/*     */   private String workDir;
/*     */   
/*     */   private String jreConfig;
/*     */   private Path runnableJreDir;
/*  31 */   public static String JRE_DEFAULT = "jre_default";
/*  32 */   public static String JRE_CONFIG = "jreConfig.json";
/*     */   
/*     */   public DownloaderJavaContainer(FileMapperService fileMapperService, String workDir, String jreConfig) {
/*  35 */     this.fileMapperService = fileMapperService;
/*  36 */     this.workDir = workDir;
/*  37 */     this.jreConfig = jreConfig;
/*     */   }
/*     */   
/*     */   public Path getJreDir() {
/*  41 */     return this.runnableJreDir;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void filterNotExistResoursesAndSetRepo(Repo repo, String workDirectory) throws NoSuchAlgorithmException, IOException {
/*  47 */     JvmRepo jvm = new JvmRepo();
/*  48 */     jvm.setJreDirectoryName(((JvmRepo)repo).getJreDirectoryName());
/*  49 */     this.repo = (Repo)jvm;
/*     */     
/*  51 */     this.repo.setRepositories(repo.getRepositories());
/*  52 */     this.repo.setResources(Lists.newArrayList());
/*  53 */     for (Metadata meta : repo.getResources()) {
/*  54 */       Path jrePath = Paths.get(workDirectory, new String[] { JRE_DEFAULT, ((JvmRepo)repo).getJreDirectoryName() });
/*  55 */       this.runnableJreDir = jrePath;
/*  56 */       if (Files.exists(jrePath, new java.nio.file.LinkOption[0])) {
/*  57 */         validateJre((JvmRepo)repo); continue;
/*     */       } 
/*  59 */       this.repo.getResources().add(meta);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void validateJre(JvmRepo repo) throws IOException, NoSuchAlgorithmException {
/*  65 */     boolean notExistJre = true;
/*  66 */     if (Files.exists(Paths.get(this.workDir, new String[] { JRE_DEFAULT, repo.getJreDirectoryName(), JRE_CONFIG }), new java.nio.file.LinkOption[0])) {
/*  67 */       notExistJre = false;
/*     */     }
/*  69 */     List<Metadata> configMetadata = new ArrayList<>();
/*  70 */     if (!notExistJre) {
/*     */       
/*  72 */       Repo repo1 = (Repo)this.fileMapperService.read(Paths.get(JRE_DEFAULT, new String[] { repo.getJreDirectoryName(), this.jreConfig }).toString(), Repo.class);
/*  73 */       if (Objects.nonNull(repo1)) {
/*  74 */         configMetadata = repo1.getResources();
/*  75 */         List<Metadata> localeJreMetadata = DesktopUtil.generateMetadataForJre(this.workDir, 
/*  76 */             Paths.get(JRE_DEFAULT, new String[] { repo.getJreDirectoryName() }).toString());
/*  77 */         configMetadata.removeAll(localeJreMetadata);
/*     */       } else {
/*  79 */         notExistJre = true;
/*     */       } 
/*     */     } 
/*     */     
/*  83 */     if (notExistJre || configMetadata.size() != 0) {
/*  84 */       Metadata m = repo.getResources().get(0);
/*  85 */       removeJava(Paths.get(this.workDir, new String[] { m.getPath() }));
/*  86 */       this.repo = (Repo)repo;
/*  87 */       log.warn("problem with jre");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void removeJava(Path path) throws IOException {
/*  92 */     FileUtils.deleteDirectory(Paths.get(this.workDir, new String[] { JRE_DEFAULT }).toFile());
/*  93 */     FileUtils.deleteQuietly(path.toAbsolutePath().toFile());
/*     */   }
/*     */ 
/*     */   
/*     */   public void containerAllSize(Repo repo) {
/*  98 */     this.containerSize = ((Long)repo.getResources().stream().map(Metadata::getSize).reduce(Long::sum).orElse(Long.valueOf(0L))).longValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void downloadSize(Repo repo, String workDirectory) {
/* 103 */     this
/*     */       
/* 105 */       .readyDownloadSize = ((Long)repo.getResources().stream().map(e -> Long.valueOf(Paths.get(workDirectory, new String[] { e.getPath() }).toFile().length())).reduce(Long::sum).orElse(Long.valueOf(0L))).longValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\htt\\upload\download\downloader\DownloaderJavaContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */