/*    */ package by.gdev.http.download.model;
/*    */ 
/*    */ public class RequestMetadata {
/*    */   private String lastModified;
/*    */   private String eTag;
/*    */   private String sha1;
/*    */   
/*    */   public void setLastModified(String lastModified) {
/*  9 */     this.lastModified = lastModified; } public void setETag(String eTag) { this.eTag = eTag; } public void setSha1(String sha1) { this.sha1 = sha1; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof RequestMetadata)) return false;  RequestMetadata other = (RequestMetadata)o; if (!other.canEqual(this)) return false;  Object this$lastModified = getLastModified(), other$lastModified = other.getLastModified(); if ((this$lastModified == null) ? (other$lastModified != null) : !this$lastModified.equals(other$lastModified)) return false;  Object this$eTag = getETag(), other$eTag = other.getETag(); if ((this$eTag == null) ? (other$eTag != null) : !this$eTag.equals(other$eTag)) return false;  Object this$sha1 = getSha1(), other$sha1 = other.getSha1(); return !((this$sha1 == null) ? (other$sha1 != null) : !this$sha1.equals(other$sha1)); } protected boolean canEqual(Object other) { return other instanceof RequestMetadata; } public int hashCode() { int PRIME = 59; result = 1; Object $lastModified = getLastModified(); result = result * 59 + (($lastModified == null) ? 43 : $lastModified.hashCode()); Object $eTag = getETag(); result = result * 59 + (($eTag == null) ? 43 : $eTag.hashCode()); Object $sha1 = getSha1(); return result * 59 + (($sha1 == null) ? 43 : $sha1.hashCode()); } public String toString() { return "RequestMetadata(lastModified=" + getLastModified() + ", eTag=" + getETag() + ", sha1=" + getSha1() + ")"; }
/*    */   
/* 11 */   public String getLastModified() { return this.lastModified; }
/* 12 */   public String getETag() { return this.eTag; } public String getSha1() {
/* 13 */     return this.sha1;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\http\download\model\RequestMetadata.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */