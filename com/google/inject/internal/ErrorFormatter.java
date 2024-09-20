/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import java.util.Formatter;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class ErrorFormatter
/*    */ {
/*    */   static void formatSources(int index, List<Object> sources, Formatter formatter) {
/* 14 */     for (int i = 0; i < sources.size(); i++) {
/* 15 */       Object source = sources.get(i);
/* 16 */       if (i == 0) {
/* 17 */         formatter.format("%-3s: ", new Object[] { Integer.valueOf(index) });
/*    */       } else {
/* 19 */         formatter.format(SourceFormatter.INDENT, new Object[0]);
/*    */       } 
/* 21 */       (new SourceFormatter(source, formatter, (i == 0))).format();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   static void formatSources(List<Object> sources, Formatter formatter) {
/* 27 */     for (int i = 0; i < sources.size(); i++) {
/* 28 */       Object source = sources.get(i);
/* 29 */       formatter.format("  ", new Object[0]);
/* 30 */       (new SourceFormatter(source, formatter, (i == 0))).format();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ErrorFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */