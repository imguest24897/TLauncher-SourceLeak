/*    */ package by.gdev.util.model.download;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JvmRepo
/*    */   extends Repo
/*    */ {
/*    */   private String jreDirectoryName;
/*    */   
/*    */   public void setJreDirectoryName(String jreDirectoryName) {
/* 11 */     this.jreDirectoryName = jreDirectoryName;
/* 12 */   } public String toString() { return "JvmRepo(super=" + super.toString() + ", jreDirectoryName=" + getJreDirectoryName() + ")"; }
/* 13 */   public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof JvmRepo)) return false;  JvmRepo other = (JvmRepo)o; if (!other.canEqual(this)) return false;  if (!super.equals(o)) return false;  Object this$jreDirectoryName = getJreDirectoryName(), other$jreDirectoryName = other.getJreDirectoryName(); return !((this$jreDirectoryName == null) ? (other$jreDirectoryName != null) : !this$jreDirectoryName.equals(other$jreDirectoryName)); } protected boolean canEqual(Object other) { return other instanceof JvmRepo; } public int hashCode() { int PRIME = 59; result = super.hashCode(); Object $jreDirectoryName = getJreDirectoryName(); return result * 59 + (($jreDirectoryName == null) ? 43 : $jreDirectoryName.hashCode()); }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getJreDirectoryName() {
/* 18 */     return this.jreDirectoryName;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gde\\util\model\download\JvmRepo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */