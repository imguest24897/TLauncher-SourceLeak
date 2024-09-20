/*    */ package com.beust.jcommander.converters;
/*    */ 
/*    */ import com.beust.jcommander.IStringConverter;
/*    */ import com.beust.jcommander.ParameterException;
/*    */ import java.util.EnumSet;
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
/*    */ public class EnumConverter<T extends Enum<T>>
/*    */   implements IStringConverter<T>
/*    */ {
/*    */   private final String optionName;
/*    */   private final Class<T> clazz;
/*    */   
/*    */   public EnumConverter(String optionName, Class<T> clazz) {
/* 24 */     this.optionName = optionName;
/* 25 */     this.clazz = clazz;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public T convert(String value) {
/*    */     try {
/* 32 */       return Enum.valueOf(this.clazz, value);
/* 33 */     } catch (IllegalArgumentException e) {
/* 34 */       return Enum.valueOf(this.clazz, value.toUpperCase());
/*    */     }
/* 36 */     catch (Exception e) {
/* 37 */       throw new ParameterException("Invalid value for " + this.optionName + " parameter. Allowed values:" + 
/* 38 */           EnumSet.allOf(this.clazz));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\converters\EnumConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */