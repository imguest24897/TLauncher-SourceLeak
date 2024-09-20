/*    */ package by.gdev.model;
/*    */ 
/*    */ import by.gdev.util.OSInfo;
/*    */ import by.gdev.util.model.download.JvmRepo;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JVMConfig
/*    */ {
/*    */   private Map<OSInfo.OSType, Map<OSInfo.Arch, Map<String, JvmRepo>>> jvms;
/*    */   
/*    */   public String toString() {
/* 18 */     return "JVMConfig(jvms=" + getJvms() + ")"; } public int hashCode() { int PRIME = 59; result = 1; Object<OSInfo.OSType, Map<OSInfo.Arch, Map<String, JvmRepo>>> $jvms = (Object<OSInfo.OSType, Map<OSInfo.Arch, Map<String, JvmRepo>>>)getJvms(); return result * 59 + (($jvms == null) ? 43 : $jvms.hashCode()); } protected boolean canEqual(Object other) { return other instanceof JVMConfig; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof JVMConfig)) return false;  JVMConfig other = (JVMConfig)o; if (!other.canEqual(this)) return false;  Object<OSInfo.OSType, Map<OSInfo.Arch, Map<String, JvmRepo>>> this$jvms = (Object<OSInfo.OSType, Map<OSInfo.Arch, Map<String, JvmRepo>>>)getJvms(), other$jvms = (Object<OSInfo.OSType, Map<OSInfo.Arch, Map<String, JvmRepo>>>)other.getJvms(); return !((this$jvms == null) ? (other$jvms != null) : !this$jvms.equals(other$jvms)); } public void setJvms(Map<OSInfo.OSType, Map<OSInfo.Arch, Map<String, JvmRepo>>> jvms) { this.jvms = jvms; }
/*    */    public Map<OSInfo.OSType, Map<OSInfo.Arch, Map<String, JvmRepo>>> getJvms() {
/* 20 */     return this.jvms;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\model\JVMConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */