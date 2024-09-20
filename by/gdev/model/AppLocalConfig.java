/*    */ package by.gdev.model;
/*    */ public class AppLocalConfig {
/*    */   private String currentAppVersion;
/*    */   private String dir;
/*    */   private String skipUpdateVersion;
/*    */   private String updaterOfferInstallerDelay1;
/*    */   private String updaterOfferInstallerEmptyCheckboxDelay1;
/*    */   private long updaterDelay;
/*    */   private String lang;
/*    */   
/* 11 */   public void setCurrentAppVersion(String currentAppVersion) { this.currentAppVersion = currentAppVersion; } public void setDir(String dir) { this.dir = dir; } public void setSkipUpdateVersion(String skipUpdateVersion) { this.skipUpdateVersion = skipUpdateVersion; } public void setUpdaterOfferInstallerDelay1(String updaterOfferInstallerDelay1) { this.updaterOfferInstallerDelay1 = updaterOfferInstallerDelay1; } public void setUpdaterOfferInstallerEmptyCheckboxDelay1(String updaterOfferInstallerEmptyCheckboxDelay1) { this.updaterOfferInstallerEmptyCheckboxDelay1 = updaterOfferInstallerEmptyCheckboxDelay1; } public void setUpdaterDelay(long updaterDelay) { this.updaterDelay = updaterDelay; } public void setLang(String lang) { this.lang = lang; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof AppLocalConfig)) return false;  AppLocalConfig other = (AppLocalConfig)o; if (!other.canEqual(this)) return false;  Object this$currentAppVersion = getCurrentAppVersion(), other$currentAppVersion = other.getCurrentAppVersion(); if ((this$currentAppVersion == null) ? (other$currentAppVersion != null) : !this$currentAppVersion.equals(other$currentAppVersion)) return false;  Object this$dir = getDir(), other$dir = other.getDir(); if ((this$dir == null) ? (other$dir != null) : !this$dir.equals(other$dir)) return false;  Object this$skipUpdateVersion = getSkipUpdateVersion(), other$skipUpdateVersion = other.getSkipUpdateVersion(); if ((this$skipUpdateVersion == null) ? (other$skipUpdateVersion != null) : !this$skipUpdateVersion.equals(other$skipUpdateVersion)) return false;  Object this$updaterOfferInstallerDelay1 = getUpdaterOfferInstallerDelay1(), other$updaterOfferInstallerDelay1 = other.getUpdaterOfferInstallerDelay1(); if ((this$updaterOfferInstallerDelay1 == null) ? (other$updaterOfferInstallerDelay1 != null) : !this$updaterOfferInstallerDelay1.equals(other$updaterOfferInstallerDelay1)) return false;  Object this$updaterOfferInstallerEmptyCheckboxDelay1 = getUpdaterOfferInstallerEmptyCheckboxDelay1(), other$updaterOfferInstallerEmptyCheckboxDelay1 = other.getUpdaterOfferInstallerEmptyCheckboxDelay1(); if ((this$updaterOfferInstallerEmptyCheckboxDelay1 == null) ? (other$updaterOfferInstallerEmptyCheckboxDelay1 != null) : !this$updaterOfferInstallerEmptyCheckboxDelay1.equals(other$updaterOfferInstallerEmptyCheckboxDelay1)) return false;  if (getUpdaterDelay() != other.getUpdaterDelay()) return false;  Object this$lang = getLang(), other$lang = other.getLang(); return !((this$lang == null) ? (other$lang != null) : !this$lang.equals(other$lang)); } protected boolean canEqual(Object other) { return other instanceof AppLocalConfig; } public int hashCode() { int PRIME = 59; result = 1; Object $currentAppVersion = getCurrentAppVersion(); result = result * 59 + (($currentAppVersion == null) ? 43 : $currentAppVersion.hashCode()); Object $dir = getDir(); result = result * 59 + (($dir == null) ? 43 : $dir.hashCode()); Object $skipUpdateVersion = getSkipUpdateVersion(); result = result * 59 + (($skipUpdateVersion == null) ? 43 : $skipUpdateVersion.hashCode()); Object $updaterOfferInstallerDelay1 = getUpdaterOfferInstallerDelay1(); result = result * 59 + (($updaterOfferInstallerDelay1 == null) ? 43 : $updaterOfferInstallerDelay1.hashCode()); Object $updaterOfferInstallerEmptyCheckboxDelay1 = getUpdaterOfferInstallerEmptyCheckboxDelay1(); result = result * 59 + (($updaterOfferInstallerEmptyCheckboxDelay1 == null) ? 43 : $updaterOfferInstallerEmptyCheckboxDelay1.hashCode()); long $updaterDelay = getUpdaterDelay(); result = result * 59 + (int)($updaterDelay >>> 32L ^ $updaterDelay); Object $lang = getLang(); return result * 59 + (($lang == null) ? 43 : $lang.hashCode()); } public String toString() { return "AppLocalConfig(currentAppVersion=" + getCurrentAppVersion() + ", dir=" + getDir() + ", skipUpdateVersion=" + getSkipUpdateVersion() + ", updaterOfferInstallerDelay1=" + getUpdaterOfferInstallerDelay1() + ", updaterOfferInstallerEmptyCheckboxDelay1=" + getUpdaterOfferInstallerEmptyCheckboxDelay1() + ", updaterDelay=" + getUpdaterDelay() + ", lang=" + getLang() + ")"; }
/*    */ 
/*    */   
/* 14 */   public String getCurrentAppVersion() { return this.currentAppVersion; }
/* 15 */   public String getDir() { return this.dir; }
/* 16 */   public String getSkipUpdateVersion() { return this.skipUpdateVersion; }
/* 17 */   public String getUpdaterOfferInstallerDelay1() { return this.updaterOfferInstallerDelay1; }
/* 18 */   public String getUpdaterOfferInstallerEmptyCheckboxDelay1() { return this.updaterOfferInstallerEmptyCheckboxDelay1; }
/* 19 */   public long getUpdaterDelay() { return this.updaterDelay; } public String getLang() {
/* 20 */     return this.lang;
/*    */   }
/*    */   public boolean isSkippedVersion(String newVersion) {
/* 23 */     return newVersion.equals(this.skipUpdateVersion);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\model\AppLocalConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */