/*    */ package org.apache.commons.text.lookup;
/*    */ 
/*    */ import java.util.Date;
/*    */ import org.apache.commons.lang3.time.FastDateFormat;
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
/*    */ 
/*    */ 
/*    */ final class DateStringLookup
/*    */   extends AbstractStringLookup
/*    */ {
/* 52 */   static final DateStringLookup INSTANCE = new DateStringLookup();
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
/*    */   
/*    */   private String formatDate(long dateMillis, String format) {
/* 69 */     FastDateFormat dateFormat = null;
/* 70 */     if (format != null) {
/*    */       try {
/* 72 */         dateFormat = FastDateFormat.getInstance(format);
/* 73 */       } catch (Exception ex) {
/* 74 */         throw IllegalArgumentExceptions.format(ex, "Invalid date format: [%s]", new Object[] { format });
/*    */       } 
/*    */     }
/* 77 */     if (dateFormat == null) {
/* 78 */       dateFormat = FastDateFormat.getInstance();
/*    */     }
/* 80 */     return dateFormat.format(new Date(dateMillis));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String lookup(String key) {
/* 92 */     return formatDate(System.currentTimeMillis(), key);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\lookup\DateStringLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */