/*    */ package com.beust.jcommander.converters;
/*    */ 
/*    */ import com.beust.jcommander.IStringConverter;
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
/*    */ public abstract class BaseConverter<T>
/*    */   implements IStringConverter<T>
/*    */ {
/*    */   private String optionName;
/*    */   
/*    */   public BaseConverter(String optionName) {
/* 33 */     this.optionName = optionName;
/*    */   }
/*    */   
/*    */   public String getOptionName() {
/* 37 */     return this.optionName;
/*    */   }
/*    */   
/*    */   protected String getErrorString(String value, String to) {
/* 41 */     return "\"" + getOptionName() + "\": couldn't convert \"" + value + "\" to " + to;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\converters\BaseConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */