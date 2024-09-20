/*    */ package com.beust.jcommander.converters;
/*    */ 
/*    */ import com.beust.jcommander.ParameterException;
/*    */ import java.nio.file.InvalidPathException;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
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
/*    */ public class PathConverter
/*    */   extends BaseConverter<Path>
/*    */ {
/*    */   public PathConverter(String optionName) {
/* 35 */     super(optionName);
/*    */   }
/*    */   
/*    */   public Path convert(String value) {
/*    */     try {
/* 40 */       return Paths.get(value, new String[0]);
/* 41 */     } catch (InvalidPathException e) {
/* 42 */       String encoded = escapeUnprintable(value);
/* 43 */       throw new ParameterException(getErrorString(encoded, "a path"));
/*    */     } 
/*    */   }
/*    */   
/*    */   private static String escapeUnprintable(String value) {
/* 48 */     StringBuilder bldr = new StringBuilder();
/* 49 */     for (char c : value.toCharArray()) {
/* 50 */       if (c < ' ') {
/* 51 */         bldr.append("\\u").append(String.format("%04X", new Object[] { Integer.valueOf(c) }));
/*    */       } else {
/* 53 */         bldr.append(c);
/*    */       } 
/*    */     } 
/* 56 */     return bldr.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\converters\PathConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */