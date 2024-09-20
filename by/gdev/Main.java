/*     */ package by.gdev;
/*     */ 
/*     */ import by.gdev.component.Starter;
/*     */ import by.gdev.handler.Localise;
/*     */ import by.gdev.handler.UpdateCore;
/*     */ import by.gdev.model.AppLocalConfig;
/*     */ import by.gdev.model.ExceptionMessage;
/*     */ import by.gdev.model.StarterAppConfig;
/*     */ import by.gdev.subscruber.ConsoleSubscriber;
/*     */ import by.gdev.ui.subscriber.ViewSubscriber;
/*     */ import by.gdev.updater.ProgressFrame;
/*     */ import by.gdev.util.CoreUtil;
/*     */ import by.gdev.util.OSInfo;
/*     */ import by.gdev.utils.service.FileMapperService;
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.joran.JoranConfigurator;
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.joran.spi.JoranException;
/*     */ import com.beust.jcommander.JCommander;
/*     */ import com.google.common.eventbus.EventBus;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.FileSystemException;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.ResourceBundle;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class Main
/*     */ {
/*  38 */   private static final Logger log = LoggerFactory.getLogger(Main.class);
/*     */   
/*  40 */   public static Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
/*  41 */   public static Charset charset = StandardCharsets.UTF_8;
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/*  44 */     boolean flag = true;
/*  45 */     log.info("starter was run");
/*  46 */     System.setProperty("java.net.preferIPv4Stack", String.valueOf(flag));
/*  47 */     EventBus eventBus = new EventBus();
/*  48 */     StarterAppConfig starterConfig = StarterAppConfig.DEFAULT_CONFIG;
/*  49 */     if (starterConfig.isProd() && !Paths.get("", new String[0]).toAbsolutePath().endsWith("minecraft")) {
/*  50 */       starterConfig.setWorkDirectory(CoreUtil.getDefaultWorkingDirectory().toPath().toAbsolutePath().toString());
/*  51 */       starterConfig.setCacheDirectory(Paths.get(starterConfig.getWorkDirectory(), new String[] { "cache" }));
/*     */     } 
/*  53 */     loadLogbackConfig(starterConfig);
/*     */     
/*  55 */     JCommander.newBuilder().addObject(starterConfig).build().parse(args);
/*  56 */     ResourceBundle bundle = null;
/*     */     try {
/*  58 */       bundle = ResourceBundle.getBundle("application", (new Localise())
/*  59 */           .getLocal(Locale.getDefault().getLanguage()));
/*  60 */       ProgressFrame starterStatusFrame = null;
/*     */       
/*  62 */       FileMapperService fileMapperService = new FileMapperService(GSON, charset, starterConfig.getWorkDirectory());
/*  63 */       AppLocalConfig appLocalConfig = (AppLocalConfig)fileMapperService.read("starter.json", AppLocalConfig.class);
/*     */       
/*  65 */       if (Objects.nonNull(appLocalConfig) && Objects.nonNull(appLocalConfig.getLang()))
/*  66 */         bundle = ResourceBundle.getBundle("application", (new Localise()).getLocal(appLocalConfig.getLang())); 
/*  67 */       if (!GraphicsEnvironment.isHeadless()) {
/*     */         
/*  69 */         starterStatusFrame = new ProgressFrame(Objects.isNull(appLocalConfig) ? "" : appLocalConfig.getCurrentAppVersion(), true, bundle);
/*  70 */         starterStatusFrame.setVisible(true);
/*  71 */         eventBus.register(starterStatusFrame);
/*  72 */         eventBus.register(new ViewSubscriber(starterStatusFrame, bundle, OSInfo.getOSType(), starterConfig));
/*     */       } 
/*  74 */       if (starterConfig.isProd() && !starterConfig.getServerFile().equals(StarterAppConfig.URI_APP_CONFIG)) {
/*  75 */         String errorMessage = String.format("The prod parameter is true. You don't need to change the value of the field. Current: %s, should be: %s", new Object[] { starterConfig
/*     */               
/*  77 */               .getServerFile(), StarterAppConfig.URI_APP_CONFIG });
/*  78 */         throw new RuntimeException(errorMessage);
/*     */       } 
/*  80 */       Starter s = new Starter(eventBus, starterConfig, bundle, starterStatusFrame, appLocalConfig, fileMapperService);
/*     */       
/*  82 */       eventBus.register(new ConsoleSubscriber(bundle, s.getFileMapperService(), starterConfig));
/*  83 */       s.updateApplication();
/*  84 */       s.validateEnvironmentAndAppRequirements();
/*  85 */       s.prepareResources();
/*  86 */       s.runApp();
/*  87 */       UpdateCore.deleteTmpFileIfExist();
/*     */     }
/*  89 */     catch (FileSystemException ex) {
/*  90 */       log.error("error", ex);
/*  91 */       if (Objects.nonNull(bundle)) {
/*  92 */         eventBus.post(new ExceptionMessage(
/*  93 */               String.format(bundle.getString("file.delete.problem"), new Object[] { ex.getLocalizedMessage()
/*  94 */                 }), String.format("https://tlauncher.org/%s/check-disk.html", new Object[] { bundle.getLocale().getLanguage() })));
/*     */       }
/*  96 */     } catch (Throwable t) {
/*  97 */       log.error("error", t);
/*  98 */       String message = t.getMessage();
/*  99 */       if ("file doesn't exist".equals(message)) {
/* 100 */         eventBus.post(new ExceptionMessage(bundle.getString("download.error")));
/* 101 */       } else if (Objects.nonNull(message) && message.contains("GetIpAddrTable")) {
/* 102 */         eventBus.post(new ExceptionMessage(bundle.getString("get.ip.addr.table")));
/*     */       } else {
/* 104 */         eventBus.post(new ExceptionMessage(bundle.getString("unidentified.error"), t));
/*     */       } 
/* 106 */       System.exit(-1);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static void loadLogbackConfig(StarterAppConfig starterConfig) throws JoranException, IOException {
/* 111 */     System.setProperty("logs_dir", 
/* 112 */         CoreUtil.getDefaultWorkingDirectory().toPath().toAbsolutePath().getParent().toString());
/* 113 */     LoggerContext loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();
/* 114 */     loggerContext.reset();
/* 115 */     JoranConfigurator configurator = new JoranConfigurator();
/* 116 */     ClassLoader classloader = Thread.currentThread().getContextClassLoader();
/* 117 */     InputStream configStream = classloader.getResourceAsStream("logbackFull.xml");
/* 118 */     configurator.setContext((Context)loggerContext);
/* 119 */     configurator.doConfigure(configStream);
/* 120 */     configStream.close();
/* 121 */     log.info("logs directory {}", System.getProperty("logs_dir") + "/logs/starter/");
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */