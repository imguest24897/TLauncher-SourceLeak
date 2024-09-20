/*    */ package by.gdev.model;
/*    */ 
/*    */ import by.gdev.util.model.download.Repo;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AppConfig
/*    */ {
/*    */   public void setComment(String comment) {
/* 14 */     this.comment = comment; } public void setAppName(String appName) { this.appName = appName; } public void setAppVersion(String appVersion) { this.appVersion = appVersion; } public void setMainClass(String mainClass) { this.mainClass = mainClass; } public void setAppArguments(List<String> appArguments) { this.appArguments = appArguments; } public void setJvmArguments(List<String> jvmArguments) { this.jvmArguments = jvmArguments; } public void setAppFileRepo(Repo appFileRepo) { this.appFileRepo = appFileRepo; } public void setAppResources(Repo appResources) { this.appResources = appResources; } public void setAppDependencies(Repo appDependencies) { this.appDependencies = appDependencies; } public void setJavaRepo(Repo javaRepo) { this.javaRepo = javaRepo; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof AppConfig)) return false;  AppConfig other = (AppConfig)o; if (!other.canEqual(this)) return false;  Object this$comment = getComment(), other$comment = other.getComment(); if ((this$comment == null) ? (other$comment != null) : !this$comment.equals(other$comment)) return false;  Object this$appName = getAppName(), other$appName = other.getAppName(); if ((this$appName == null) ? (other$appName != null) : !this$appName.equals(other$appName)) return false;  Object this$appVersion = getAppVersion(), other$appVersion = other.getAppVersion(); if ((this$appVersion == null) ? (other$appVersion != null) : !this$appVersion.equals(other$appVersion)) return false;  Object this$mainClass = getMainClass(), other$mainClass = other.getMainClass(); if ((this$mainClass == null) ? (other$mainClass != null) : !this$mainClass.equals(other$mainClass)) return false;  Object<String> this$appArguments = (Object<String>)getAppArguments(), other$appArguments = (Object<String>)other.getAppArguments(); if ((this$appArguments == null) ? (other$appArguments != null) : !this$appArguments.equals(other$appArguments)) return false;  Object<String> this$jvmArguments = (Object<String>)getJvmArguments(), other$jvmArguments = (Object<String>)other.getJvmArguments(); if ((this$jvmArguments == null) ? (other$jvmArguments != null) : !this$jvmArguments.equals(other$jvmArguments)) return false;  Object this$appFileRepo = getAppFileRepo(), other$appFileRepo = other.getAppFileRepo(); if ((this$appFileRepo == null) ? (other$appFileRepo != null) : !this$appFileRepo.equals(other$appFileRepo)) return false;  Object this$appResources = getAppResources(), other$appResources = other.getAppResources(); if ((this$appResources == null) ? (other$appResources != null) : !this$appResources.equals(other$appResources)) return false;  Object this$appDependencies = getAppDependencies(), other$appDependencies = other.getAppDependencies(); if ((this$appDependencies == null) ? (other$appDependencies != null) : !this$appDependencies.equals(other$appDependencies)) return false;  Object this$javaRepo = getJavaRepo(), other$javaRepo = other.getJavaRepo(); return !((this$javaRepo == null) ? (other$javaRepo != null) : !this$javaRepo.equals(other$javaRepo)); } protected boolean canEqual(Object other) { return other instanceof AppConfig; } public int hashCode() { int PRIME = 59; result = 1; Object $comment = getComment(); result = result * 59 + (($comment == null) ? 43 : $comment.hashCode()); Object $appName = getAppName(); result = result * 59 + (($appName == null) ? 43 : $appName.hashCode()); Object $appVersion = getAppVersion(); result = result * 59 + (($appVersion == null) ? 43 : $appVersion.hashCode()); Object $mainClass = getMainClass(); result = result * 59 + (($mainClass == null) ? 43 : $mainClass.hashCode()); Object<String> $appArguments = (Object<String>)getAppArguments(); result = result * 59 + (($appArguments == null) ? 43 : $appArguments.hashCode()); Object<String> $jvmArguments = (Object<String>)getJvmArguments(); result = result * 59 + (($jvmArguments == null) ? 43 : $jvmArguments.hashCode()); Object $appFileRepo = getAppFileRepo(); result = result * 59 + (($appFileRepo == null) ? 43 : $appFileRepo.hashCode()); Object $appResources = getAppResources(); result = result * 59 + (($appResources == null) ? 43 : $appResources.hashCode()); Object $appDependencies = getAppDependencies(); result = result * 59 + (($appDependencies == null) ? 43 : $appDependencies.hashCode()); Object $javaRepo = getJavaRepo(); return result * 59 + (($javaRepo == null) ? 43 : $javaRepo.hashCode()); } public String toString() { return "AppConfig(comment=" + getComment() + ", appName=" + getAppName() + ", appVersion=" + getAppVersion() + ", mainClass=" + getMainClass() + ", appArguments=" + getAppArguments() + ", jvmArguments=" + getJvmArguments() + ", appFileRepo=" + getAppFileRepo() + ", appResources=" + getAppResources() + ", appDependencies=" + getAppDependencies() + ", javaRepo=" + getJavaRepo() + ")"; }
/*    */   
/* 16 */   private String comment = "Config file for desktop-starter example"; private String appName; private String appVersion; private String mainClass; private List<String> appArguments; public String getComment() { return this.comment; }
/*    */   
/*    */   private List<String> jvmArguments; private Repo appFileRepo; private Repo appResources; private Repo appDependencies; private Repo javaRepo;
/*    */   public String getAppName() {
/* 20 */     return this.appName;
/*    */   }
/*    */   
/*    */   public String getAppVersion() {
/* 24 */     return this.appVersion;
/*    */   }
/*    */   
/*    */   public String getMainClass() {
/* 28 */     return this.mainClass;
/*    */   }
/*    */   
/*    */   public List<String> getAppArguments() {
/* 32 */     return this.appArguments;
/*    */   }
/*    */   
/*    */   public List<String> getJvmArguments() {
/* 36 */     return this.jvmArguments;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Repo getAppFileRepo() {
/* 43 */     return this.appFileRepo;
/*    */   }
/*    */   
/*    */   public Repo getAppResources() {
/* 47 */     return this.appResources;
/*    */   }
/*    */   
/*    */   public Repo getAppDependencies() {
/* 51 */     return this.appDependencies;
/*    */   }
/*    */   
/*    */   public Repo getJavaRepo() {
/* 55 */     return this.javaRepo;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\model\AppConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */