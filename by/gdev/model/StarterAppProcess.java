/*    */ package by.gdev.model;
/*    */ 
/*    */ public class StarterAppProcess {
/*    */   private Process process;
/*    */   private String line;
/*    */   private Exception exeption;
/*    */   private Integer errorCode;
/*    */   
/*  9 */   public void setProcess(Process process) { this.process = process; } public void setLine(String line) { this.line = line; } public void setExeption(Exception exeption) { this.exeption = exeption; } public void setErrorCode(Integer errorCode) { this.errorCode = errorCode; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof StarterAppProcess)) return false;  StarterAppProcess other = (StarterAppProcess)o; if (!other.canEqual(this)) return false;  Object this$process = getProcess(), other$process = other.getProcess(); if ((this$process == null) ? (other$process != null) : !this$process.equals(other$process)) return false;  Object this$line = getLine(), other$line = other.getLine(); if ((this$line == null) ? (other$line != null) : !this$line.equals(other$line)) return false;  Object this$exeption = getExeption(), other$exeption = other.getExeption(); if ((this$exeption == null) ? (other$exeption != null) : !this$exeption.equals(other$exeption)) return false;  Object this$errorCode = getErrorCode(), other$errorCode = other.getErrorCode(); return !((this$errorCode == null) ? (other$errorCode != null) : !this$errorCode.equals(other$errorCode)); } protected boolean canEqual(Object other) { return other instanceof StarterAppProcess; } public int hashCode() { int PRIME = 59; result = 1; Object $process = getProcess(); result = result * 59 + (($process == null) ? 43 : $process.hashCode()); Object $line = getLine(); result = result * 59 + (($line == null) ? 43 : $line.hashCode()); Object $exeption = getExeption(); result = result * 59 + (($exeption == null) ? 43 : $exeption.hashCode()); Object $errorCode = getErrorCode(); return result * 59 + (($errorCode == null) ? 43 : $errorCode.hashCode()); } public String toString() { return "StarterAppProcess(process=" + getProcess() + ", line=" + getLine() + ", exeption=" + getExeption() + ", errorCode=" + getErrorCode() + ")"; }
/*    */   
/* 11 */   public Process getProcess() { return this.process; }
/* 12 */   public String getLine() { return this.line; }
/* 13 */   public Exception getExeption() { return this.exeption; } public Integer getErrorCode() {
/* 14 */     return this.errorCode;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\model\StarterAppProcess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */