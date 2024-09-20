/*    */ package com.beust.jcommander;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class Strings
/*    */ {
/*    */   public static boolean isStringEmpty(String s) {
/*  8 */     return (s == null || "".equals(s));
/*    */   }
/*    */   
/*    */   public static boolean startsWith(String s, String with, boolean isCaseSensitive) {
/* 12 */     if (isCaseSensitive) {
/* 13 */       return s.startsWith(with);
/*    */     }
/* 15 */     return s.toLowerCase().startsWith(with.toLowerCase());
/*    */   }
/*    */ 
/*    */   
/*    */   public static String join(String delimiter, List<String> args) {
/* 20 */     StringBuilder builder = new StringBuilder();
/*    */     
/* 22 */     for (int i = 0; i < args.size(); i++) {
/* 23 */       builder.append(args.get(i));
/*    */       
/* 25 */       if (i + 1 < args.size())
/* 26 */         builder.append(delimiter); 
/*    */     } 
/* 28 */     return builder.toString();
/*    */   }
/*    */   
/*    */   public static String join(String delimiter, Object[] args) {
/* 32 */     StringBuilder builder = new StringBuilder();
/*    */     
/* 34 */     for (int i = 0; i < args.length; i++) {
/* 35 */       builder.append(args[i]);
/*    */       
/* 37 */       if (i + 1 < args.length)
/* 38 */         builder.append(delimiter); 
/*    */     } 
/* 40 */     return builder.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\Strings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */