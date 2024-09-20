/*    */ package org.apache.commons.text.lookup;
/*    */ 
/*    */ import org.apache.commons.lang3.StringUtils;
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
/*    */ abstract class AbstractStringLookup
/*    */   implements StringLookup
/*    */ {
/*    */   protected static final char SPLIT_CH = ':';
/* 37 */   protected static final String SPLIT_STR = String.valueOf(':');
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static String toLookupKey(String left, String right) {
/* 43 */     return toLookupKey(left, SPLIT_STR, right);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static String toLookupKey(String left, String separator, String right) {
/* 50 */     return left + separator + right;
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
/*    */   @Deprecated
/*    */   protected String substringAfter(String value, char ch) {
/* 63 */     return StringUtils.substringAfter(value, ch);
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
/*    */   @Deprecated
/*    */   protected String substringAfter(String value, String str) {
/* 76 */     return StringUtils.substringAfter(value, str);
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
/*    */   @Deprecated
/*    */   protected String substringAfterLast(String value, char ch) {
/* 89 */     return StringUtils.substringAfterLast(value, ch);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\lookup\AbstractStringLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */