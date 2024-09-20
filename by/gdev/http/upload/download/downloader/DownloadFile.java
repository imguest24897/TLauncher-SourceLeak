/*    */ package by.gdev.http.upload.download.downloader;
/*    */ public class DownloadFile {
/*    */   private String uri;
/*    */   private String file;
/*    */   
/*  6 */   public void setUri(String uri) { this.uri = uri; } public void setFile(String file) { this.file = file; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof DownloadFile)) return false;  DownloadFile other = (DownloadFile)o; if (!other.canEqual(this)) return false;  Object this$uri = getUri(), other$uri = other.getUri(); if ((this$uri == null) ? (other$uri != null) : !this$uri.equals(other$uri)) return false;  Object this$file = getFile(), other$file = other.getFile(); return !((this$file == null) ? (other$file != null) : !this$file.equals(other$file)); } protected boolean canEqual(Object other) { return other instanceof DownloadFile; } public int hashCode() { int PRIME = 59; result = 1; Object $uri = getUri(); result = result * 59 + (($uri == null) ? 43 : $uri.hashCode()); Object $file = getFile(); return result * 59 + (($file == null) ? 43 : $file.hashCode()); } public String toString() { return "DownloadFile(uri=" + getUri() + ", file=" + getFile() + ")"; } public DownloadFile(String uri, String file) {
/*  7 */     this.uri = uri; this.file = file;
/*    */   }
/*    */   
/* 10 */   public String getUri() { return this.uri; } public String getFile() {
/* 11 */     return this.file;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\htt\\upload\download\downloader\DownloadFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */