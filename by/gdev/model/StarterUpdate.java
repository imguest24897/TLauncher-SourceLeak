/*   */ package by.gdev.model;
/*   */ public class StarterUpdate { private String sha1;
/*   */   private String uri;
/*   */   
/* 5 */   public void setSha1(String sha1) { this.sha1 = sha1; } public void setUri(String uri) { this.uri = uri; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof StarterUpdate)) return false;  StarterUpdate other = (StarterUpdate)o; if (!other.canEqual(this)) return false;  Object this$sha1 = getSha1(), other$sha1 = other.getSha1(); if ((this$sha1 == null) ? (other$sha1 != null) : !this$sha1.equals(other$sha1)) return false;  Object this$uri = getUri(), other$uri = other.getUri(); return !((this$uri == null) ? (other$uri != null) : !this$uri.equals(other$uri)); } protected boolean canEqual(Object other) { return other instanceof StarterUpdate; } public int hashCode() { int PRIME = 59; result = 1; Object $sha1 = getSha1(); result = result * 59 + (($sha1 == null) ? 43 : $sha1.hashCode()); Object $uri = getUri(); return result * 59 + (($uri == null) ? 43 : $uri.hashCode()); } public String toString() { return "StarterUpdate(sha1=" + getSha1() + ", uri=" + getUri() + ")"; }
/*   */   
/* 7 */   public String getSha1() { return this.sha1; } public String getUri() {
/* 8 */     return this.uri;
/*   */   } }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\model\StarterUpdate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */