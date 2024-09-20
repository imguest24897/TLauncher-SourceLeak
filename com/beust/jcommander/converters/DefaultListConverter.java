/*    */ package com.beust.jcommander.converters;
/*    */ 
/*    */ import com.beust.jcommander.IStringConverter;
/*    */ import com.beust.jcommander.internal.Lists;
/*    */ import java.util.List;
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
/*    */ public class DefaultListConverter<T>
/*    */   implements IStringConverter<List<T>>
/*    */ {
/*    */   private final IParameterSplitter splitter;
/*    */   private final IStringConverter<T> converter;
/*    */   
/*    */   public DefaultListConverter(IParameterSplitter splitter, IStringConverter<T> converter) {
/* 24 */     this.splitter = splitter;
/* 25 */     this.converter = converter;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<T> convert(String value) {
/* 30 */     List<T> result = Lists.newArrayList();
/* 31 */     for (String param : this.splitter.split(value)) {
/* 32 */       result.add((T)this.converter.convert(param));
/*    */     }
/* 34 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\converters\DefaultListConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */