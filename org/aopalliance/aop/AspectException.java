/*    */ package org.aopalliance.aop;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.io.PrintWriter;
/*    */ import java.io.StringWriter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AspectException
/*    */   extends RuntimeException
/*    */ {
/*    */   private String message;
/*    */   private String stackTrace;
/*    */   private Throwable t;
/*    */   
/*    */   public AspectException(String paramString) {
/* 28 */     super(paramString);
/* 29 */     this.message = paramString;
/* 30 */     this.stackTrace = paramString;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AspectException(String paramString, Throwable paramThrowable) {
/* 39 */     super(paramString + "; nested exception is " + paramThrowable.getMessage());
/* 40 */     this.t = paramThrowable;
/* 41 */     StringWriter stringWriter = new StringWriter();
/* 42 */     paramThrowable.printStackTrace(new PrintWriter(stringWriter));
/* 43 */     this.stackTrace = stringWriter.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Throwable getCause() {
/* 52 */     return this.t;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 56 */     return getMessage();
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 60 */     return this.message;
/*    */   }
/*    */   
/*    */   public void printStackTrace() {
/* 64 */     System.err.print(this.stackTrace);
/*    */   }
/*    */   
/*    */   public void printStackTrace(PrintStream paramPrintStream) {
/* 68 */     printStackTrace(new PrintWriter(paramPrintStream));
/*    */   }
/*    */   
/*    */   public void printStackTrace(PrintWriter paramPrintWriter) {
/* 72 */     paramPrintWriter.print(this.stackTrace);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\aopalliance\aop\AspectException.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */