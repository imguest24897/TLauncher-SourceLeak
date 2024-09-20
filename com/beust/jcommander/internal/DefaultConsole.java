/*    */ package com.beust.jcommander.internal;
/*    */ 
/*    */ import com.beust.jcommander.ParameterException;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class DefaultConsole
/*    */   implements Console {
/*    */   private final PrintStream target;
/*    */   
/*    */   public DefaultConsole(PrintStream target) {
/* 14 */     this.target = target;
/*    */   }
/*    */   
/*    */   public DefaultConsole() {
/* 18 */     this.target = System.out;
/*    */   }
/*    */   
/*    */   public void print(String msg) {
/* 22 */     this.target.print(msg);
/*    */   }
/*    */   
/*    */   public void println(String msg) {
/* 26 */     this.target.println(msg);
/*    */   }
/*    */ 
/*    */   
/*    */   public char[] readPassword(boolean echoInput) {
/*    */     try {
/* 32 */       InputStreamReader isr = new InputStreamReader(System.in);
/* 33 */       BufferedReader in = new BufferedReader(isr);
/* 34 */       String result = in.readLine();
/* 35 */       return result.toCharArray();
/*    */     }
/* 37 */     catch (IOException e) {
/* 38 */       throw new ParameterException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\internal\DefaultConsole.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */