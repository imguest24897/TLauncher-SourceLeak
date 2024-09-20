/*    */ package by.gdev.model;
/*    */ public class ExceptionMessage {
/*    */   private String message;
/*    */   
/*  5 */   public void setMessage(String message) { this.message = message; } private String link; private Throwable error; public void setLink(String link) { this.link = link; } public void setError(Throwable error) { this.error = error; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof ExceptionMessage)) return false;  ExceptionMessage other = (ExceptionMessage)o; if (!other.canEqual(this)) return false;  Object this$message = getMessage(), other$message = other.getMessage(); if ((this$message == null) ? (other$message != null) : !this$message.equals(other$message)) return false;  Object this$link = getLink(), other$link = other.getLink(); if ((this$link == null) ? (other$link != null) : !this$link.equals(other$link)) return false;  Object this$error = getError(), other$error = other.getError(); return !((this$error == null) ? (other$error != null) : !this$error.equals(other$error)); } protected boolean canEqual(Object other) { return other instanceof ExceptionMessage; } public int hashCode() { int PRIME = 59; result = 1; Object $message = getMessage(); result = result * 59 + (($message == null) ? 43 : $message.hashCode()); Object $link = getLink(); result = result * 59 + (($link == null) ? 43 : $link.hashCode()); Object $error = getError(); return result * 59 + (($error == null) ? 43 : $error.hashCode()); } public String toString() { return "ExceptionMessage(message=" + getMessage() + ", link=" + getLink() + ", error=" + getError() + ")"; }
/*    */   
/*  7 */   public String getMessage() { return this.message; }
/*  8 */   public String getLink() { return this.link; } public Throwable getError() {
/*  9 */     return this.error;
/*    */   }
/*    */   public ExceptionMessage(String message) {
/* 12 */     this.message = message;
/*    */   }
/*    */   
/*    */   public ExceptionMessage(String message, Throwable t) {
/* 16 */     this.message = message;
/* 17 */     this.error = t;
/*    */   }
/*    */ 
/*    */   
/*    */   public ExceptionMessage(String message, String link) {
/* 22 */     this.message = message;
/* 23 */     this.link = link;
/*    */   }
/*    */   
/*    */   public String printValidationMessage() {
/* 27 */     return this.message;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\by\gdev\model\ExceptionMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */