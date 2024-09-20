/*    */ package com.beust.jcommander.internal;
/*    */ 
/*    */ import com.beust.jcommander.IStringConverter;
/*    */ import com.beust.jcommander.IStringConverterFactory;
/*    */ import com.beust.jcommander.converters.BigDecimalConverter;
/*    */ import com.beust.jcommander.converters.BooleanConverter;
/*    */ import com.beust.jcommander.converters.DoubleConverter;
/*    */ import com.beust.jcommander.converters.FileConverter;
/*    */ import com.beust.jcommander.converters.FloatConverter;
/*    */ import com.beust.jcommander.converters.ISO8601DateConverter;
/*    */ import com.beust.jcommander.converters.IntegerConverter;
/*    */ import com.beust.jcommander.converters.LongConverter;
/*    */ import com.beust.jcommander.converters.PathConverter;
/*    */ import com.beust.jcommander.converters.StringConverter;
/*    */ import com.beust.jcommander.converters.URIConverter;
/*    */ import com.beust.jcommander.converters.URLConverter;
/*    */ import java.io.File;
/*    */ import java.math.BigDecimal;
/*    */ import java.net.URI;
/*    */ import java.net.URL;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Date;
/*    */ import java.util.Map;
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
/*    */ public class DefaultConverterFactory
/*    */   implements IStringConverterFactory
/*    */ {
/* 52 */   private static Map<Class, Class<? extends IStringConverter<?>>> classConverters = Maps.newHashMap(); static {
/* 53 */     classConverters.put(String.class, StringConverter.class);
/* 54 */     classConverters.put(Integer.class, IntegerConverter.class);
/* 55 */     classConverters.put(int.class, IntegerConverter.class);
/* 56 */     classConverters.put(Long.class, LongConverter.class);
/* 57 */     classConverters.put(long.class, LongConverter.class);
/* 58 */     classConverters.put(Float.class, FloatConverter.class);
/* 59 */     classConverters.put(float.class, FloatConverter.class);
/* 60 */     classConverters.put(Double.class, DoubleConverter.class);
/* 61 */     classConverters.put(double.class, DoubleConverter.class);
/* 62 */     classConverters.put(Boolean.class, BooleanConverter.class);
/* 63 */     classConverters.put(boolean.class, BooleanConverter.class);
/* 64 */     classConverters.put(File.class, FileConverter.class);
/* 65 */     classConverters.put(BigDecimal.class, BigDecimalConverter.class);
/* 66 */     classConverters.put(Date.class, ISO8601DateConverter.class);
/* 67 */     classConverters.put(URI.class, URIConverter.class);
/* 68 */     classConverters.put(URL.class, URLConverter.class);
/*    */     
/*    */     try {
/* 71 */       classConverters.put(Path.class, PathConverter.class);
/* 72 */     } catch (NoClassDefFoundError noClassDefFoundError) {}
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Class<? extends IStringConverter<?>> getConverter(Class forType) {
/* 78 */     return classConverters.get(forType);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\internal\DefaultConverterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */