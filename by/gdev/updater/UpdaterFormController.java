/*     */ package by.gdev.updater;
/*     */ 
/*     */ import by.gdev.http.download.service.GsonService;
/*     */ import by.gdev.model.AppConfig;
/*     */ import by.gdev.model.AppLocalConfig;
/*     */ import by.gdev.util.OSInfo;
/*     */ import by.gdev.utils.service.FileMapperService;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Properties;
/*     */ import java.util.ResourceBundle;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.lang3.time.DateUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UpdaterFormController
/*     */ {
/*  30 */   private static final Logger log = LoggerFactory.getLogger(UpdaterFormController.class);
/*     */   
/*     */   private int messageType;
/*  33 */   public static Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
/*  34 */   private static final SimpleDateFormat FORMAT1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/*     */   
/*     */   Properties property;
/*     */   private Update update;
/*     */   private UpdaterMessageView view;
/*     */   AppLocalConfig settings;
/*     */   FileMapperService fileMapperService;
/*     */   GsonService gsonService;
/*     */   private int user;
/*     */   
/*     */   public UpdaterFormController(Update update, AppLocalConfig settings, AppConfig remoteAppConfig, FileMapperService fileMapperService, GsonService gsonService, ResourceBundle bundle) throws IOException {
/*  45 */     this.update = update;
/*  46 */     this.settings = settings;
/*  47 */     this.gsonService = gsonService;
/*  48 */     this.fileMapperService = fileMapperService;
/*  49 */     this.property = new Properties();
/*  50 */     ClassLoader classloader = Thread.currentThread().getContextClassLoader();
/*  51 */     InputStream inputStream = classloader.getResourceAsStream("application.properties");
/*  52 */     this.property.load(inputStream);
/*  53 */     int force = update.getUpdaterView();
/*  54 */     boolean hashOfferDelay = false;
/*     */     
/*     */     try {
/*  57 */       if (settings.getUpdaterOfferInstallerEmptyCheckboxDelay1() != null) {
/*  58 */         Date offerDelay = DateUtils.addDays(FORMAT1
/*  59 */             .parse(settings.getUpdaterOfferInstallerEmptyCheckboxDelay1()), update
/*  60 */             .getOfferEmptyCheckboxDelay());
/*  61 */         if (offerDelay.after(new Date()))
/*  62 */           hashOfferDelay = true; 
/*     */       } 
/*  64 */       if (settings.getUpdaterOfferInstallerDelay1() != null) {
/*  65 */         Date offerDelay = DateUtils.addDays(FORMAT1.parse(settings.getUpdaterOfferInstallerDelay1()), update
/*  66 */             .getOfferDelay());
/*  67 */         if (offerDelay.after(new Date()))
/*  68 */           hashOfferDelay = true; 
/*     */       } 
/*  70 */     } catch (ParseException parseException) {}
/*     */     
/*  72 */     String langCase = bundle.getLocale().toString();
/*     */     
/*  74 */     if (!hashOfferDelay && force == 2 && OSInfo.getOSType().equals(OSInfo.OSType.WINDOWS) && update
/*  75 */       .getOfferByLang(langCase).isPresent()) {
/*  76 */       update.setSelectedOffer(update.getOfferByLang(langCase).get());
/*  77 */       this.messageType = 2;
/*  78 */     } else if (!update.getBanners().isEmpty() && update.getBanners().get(langCase) != null && force != 0) {
/*  79 */       this.messageType = 1;
/*     */     } else {
/*  81 */       this.messageType = 0;
/*     */     } 
/*  83 */     this.view = new UpdaterMessageView(update, this.messageType, langCase, true, settings, bundle, remoteAppConfig);
/*     */   }
/*     */   
/*     */   public boolean getResult(Update updates) throws IOException {
/*  87 */     long delay = Integer.parseInt(this.property.getProperty("updater.chooser.delay"));
/*  88 */     if ((new Date()).getTime() < (new Date(this.settings.getUpdaterDelay())).getTime() + delay * 3600L * 1000L) {
/*  89 */       return false;
/*     */     }
/*  91 */     return processUpdating(updates);
/*     */   }
/*     */   
/*     */   private boolean processUpdating(Update updates) throws UnsupportedOperationException, IOException {
/*     */     long time;
/*  96 */     UserResult res = this.view.showMessage(updates, this.settings);
/*  97 */     this.user = res.userChooser;
/*  98 */     switch (this.user) {
/*     */       case 1:
/* 100 */         if (this.messageType == 2 && 
/* 101 */           isExecutedOffer(res)) {
/* 102 */           return processUpdating(updates);
/*     */         }
/*     */         
/* 105 */         return true;
/*     */       case 0:
/* 107 */         if (this.messageType == 2 && 
/* 108 */           isExecutedOffer(res)) {
/* 109 */           return processUpdating(updates);
/*     */         }
/*     */         
/* 112 */         time = (new Date()).getTime();
/* 113 */         this.settings.setUpdaterDelay(time);
/* 114 */         this.fileMapperService.write(this.settings, "starter.json");
/* 115 */         return false;
/*     */     } 
/* 117 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isExecutedOffer(UserResult res) throws UnsupportedOperationException, IOException {
/*     */     try {
/* 123 */       if (res.getUserChooser() == 1 || (res.getUserChooser() == 0 && this.update.isUpdaterLaterInstall())) {
/* 124 */         Offer offer = this.update.getSelectedOffer();
/* 125 */         Path tempFile = Files.createTempFile("install", ".exe", (FileAttribute<?>[])new FileAttribute[0]);
/* 126 */         FileUtils.copyURLToFile(new URL(offer.getInstaller()), tempFile.toFile(), 15000, 15000);
/* 127 */         String args = offer.getArgs().get(res.getOfferArgs());
/* 128 */         String runningOffer = tempFile + " " + args;
/* 129 */         if (res.isSelectedAnyCheckBox()) {
/* 130 */           Path runner = Files.createTempFile("TLauncherUpdater", ".exe", (FileAttribute<?>[])new FileAttribute[0]);
/* 131 */           FileUtils.copyURLToFile(new URL(this.update.getRootAccessExe().get(0)), runner.toFile(), 15000, 15000);
/*     */           
/* 133 */           String url = "https://stat.fastrepo.org/updater/save";
/* 134 */           String data = GSON.toJson(preparedUpdaterDTO(this.update, res, this.settings, this.property));
/*     */           
/* 136 */           String[] s = { "cmd", "/c", runner.toString(), runningOffer.replace("\"", "\\\""), url, data.replace("\"", "\\\"") };
/* 137 */           Process p = Runtime.getRuntime().exec(s);
/* 138 */           if (p.waitFor() == 1)
/* 139 */             return true; 
/*     */         } else {
/* 141 */           Runtime.getRuntime().exec(runningOffer);
/*     */         } 
/*     */       } 
/* 144 */     } catch (Exception e) {
/* 145 */       log.error("error", e);
/*     */     } 
/* 147 */     if (res.isSelectedAnyCheckBox()) {
/* 148 */       this.fileMapperService.write(this.settings, "starter.json");
/*     */     } else {
/* 150 */       this.fileMapperService.write(this.settings, "starter.json");
/*     */     } 
/* 152 */     return false;
/*     */   }
/*     */   
/*     */   public int getChoose() {
/* 156 */     return this.user;
/*     */   }
/*     */ 
/*     */   
/*     */   public static UpdaterDTO preparedUpdaterDTO(Update update, UserResult res, AppLocalConfig settings, Properties property) {
/* 161 */     UpdaterDTO dto = new UpdaterDTO();
/* 162 */     dto.setClient(property.getProperty("updater.chooser.delay"));
/* 163 */     dto.setOffer(update.getSelectedOffer().getOffer());
/* 164 */     dto.setArgs(res.getOfferArgs());
/* 165 */     dto.setCurrentVersion(Double.parseDouble(settings.getCurrentAppVersion()));
/* 166 */     dto.setNewVersion(update.getVersion());
/* 167 */     dto.setUpdaterLater(Boolean.valueOf((res.getUserChooser() == 0)));
/* 168 */     dto.setRequestTime((new Date()).toString());
/* 169 */     return dto;
/*     */   } public static class UserResult {
/*     */     private String offerArgs;
/* 172 */     public void setOfferArgs(String offerArgs) { this.offerArgs = offerArgs; } private int userChooser; private boolean selectedAnyCheckBox; public void setUserChooser(int userChooser) { this.userChooser = userChooser; } public void setSelectedAnyCheckBox(boolean selectedAnyCheckBox) { this.selectedAnyCheckBox = selectedAnyCheckBox; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof UserResult)) return false;  UserResult other = (UserResult)o; if (!other.canEqual(this)) return false;  Object this$offerArgs = getOfferArgs(), other$offerArgs = other.getOfferArgs(); return ((this$offerArgs == null) ? (other$offerArgs != null) : !this$offerArgs.equals(other$offerArgs)) ? false : ((getUserChooser() != other.getUserChooser()) ? false : (!(isSelectedAnyCheckBox() != other.isSelectedAnyCheckBox()))); } protected boolean canEqual(Object other) { return other instanceof UserResult; } public int hashCode() { int PRIME = 59; result = 1; Object $offerArgs = getOfferArgs(); result = result * 59 + (($offerArgs == null) ? 43 : $offerArgs.hashCode()); result = result * 59 + getUserChooser(); return result * 59 + (isSelectedAnyCheckBox() ? 79 : 97); } public String toString() { return "UpdaterFormController.UserResult(offerArgs=" + getOfferArgs() + ", userChooser=" + getUserChooser() + ", selectedAnyCheckBox=" + isSelectedAnyCheckBox() + ")"; }
/*     */     
/* 174 */     public String getOfferArgs() { return this.offerArgs; }
/* 175 */     public int getUserChooser() { return this.userChooser; } public boolean isSelectedAnyCheckBox() {
/* 176 */       return this.selectedAnyCheckBox;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gde\\updater\UpdaterFormController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */