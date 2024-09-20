/*    */ package by.gdev.model;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class RepoVersion {
/*    */   private List<String> repo;
/*    */   private List<String> urls;
/*    */   private String relUrl;
/*    */   
/*    */   public void setRepo(List<String> repo) {
/* 11 */     this.repo = repo; } public void setUrls(List<String> urls) { this.urls = urls; } public void setRelUrl(String relUrl) { this.relUrl = relUrl; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof RepoVersion)) return false;  RepoVersion other = (RepoVersion)o; if (!other.canEqual(this)) return false;  Object<String> this$repo = (Object<String>)getRepo(), other$repo = (Object<String>)other.getRepo(); if ((this$repo == null) ? (other$repo != null) : !this$repo.equals(other$repo)) return false;  Object<String> this$urls = (Object<String>)getUrls(), other$urls = (Object<String>)other.getUrls(); if ((this$urls == null) ? (other$urls != null) : !this$urls.equals(other$urls)) return false;  Object this$relUrl = getRelUrl(), other$relUrl = other.getRelUrl(); return !((this$relUrl == null) ? (other$relUrl != null) : !this$relUrl.equals(other$relUrl)); } protected boolean canEqual(Object other) { return other instanceof RepoVersion; } public int hashCode() { int PRIME = 59; result = 1; Object<String> $repo = (Object<String>)getRepo(); result = result * 59 + (($repo == null) ? 43 : $repo.hashCode()); Object<String> $urls = (Object<String>)getUrls(); result = result * 59 + (($urls == null) ? 43 : $urls.hashCode()); Object $relUrl = getRelUrl(); return result * 59 + (($relUrl == null) ? 43 : $relUrl.hashCode()); } public String toString() { return "RepoVersion(repo=" + getRepo() + ", urls=" + getUrls() + ", relUrl=" + getRelUrl() + ")"; }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<String> getRepo() {
/* 16 */     return this.repo; }
/* 17 */   public List<String> getUrls() { return this.urls; } public String getRelUrl() {
/* 18 */     return this.relUrl;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\model\RepoVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */