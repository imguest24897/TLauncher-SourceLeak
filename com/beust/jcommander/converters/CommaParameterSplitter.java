/*   */ package com.beust.jcommander.converters;
/*   */ 
/*   */ import java.util.Arrays;
/*   */ import java.util.List;
/*   */ 
/*   */ public class CommaParameterSplitter
/*   */   implements IParameterSplitter {
/*   */   public List<String> split(String value) {
/* 9 */     return Arrays.asList(value.split(","));
/*   */   }
/*   */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\converters\CommaParameterSplitter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */