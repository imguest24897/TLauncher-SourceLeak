/*     */ package org.checkerframework.checker.regex;
/*     */ 
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.regex.PatternSyntaxException;
/*     */ import org.checkerframework.checker.regex.qual.Regex;
/*     */ import org.checkerframework.dataflow.qual.Pure;
/*     */ import org.checkerframework.dataflow.qual.SideEffectFree;
/*     */ import org.checkerframework.framework.qual.EnsuresQualifierIf;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class RegexUtil
/*     */ {
/*     */   private RegexUtil() {
/*  34 */     throw new Error("do not instantiate");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class CheckedPatternSyntaxException
/*     */     extends Exception
/*     */   {
/*     */     private static final long serialVersionUID = 6266881831979001480L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final PatternSyntaxException pse;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CheckedPatternSyntaxException(PatternSyntaxException pse) {
/*  81 */       this.pse = pse;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CheckedPatternSyntaxException(String desc, String regex, int index) {
/*  93 */       this(new PatternSyntaxException(desc, regex, index));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getDescription() {
/* 102 */       return this.pse.getDescription();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getIndex() {
/* 112 */       return this.pse.getIndex();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Pure
/*     */     public String getMessage() {
/* 125 */       return this.pse.getMessage();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getPattern() {
/* 134 */       return this.pse.getPattern();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Pure
/*     */   @EnsuresQualifierIf(result = true, expression = {"#1"}, qualifier = Regex.class)
/*     */   public static boolean isRegex(String s) {
/* 147 */     return isRegex(s, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Pure
/*     */   @EnsuresQualifierIf(result = true, expression = {"#1"}, qualifier = Regex.class)
/*     */   public static boolean isRegex(String s, int groups) {
/*     */     Pattern p;
/*     */     try {
/* 166 */       p = Pattern.compile(s);
/* 167 */     } catch (PatternSyntaxException e) {
/* 168 */       return false;
/*     */     } 
/* 170 */     return (getGroupCount(p) >= groups);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Pure
/*     */   @EnsuresQualifierIf(result = true, expression = {"#1"}, qualifier = Regex.class)
/*     */   public static boolean isRegex(char c) {
/* 187 */     return isRegex(Character.toString(c));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SideEffectFree
/*     */   public static String regexError(String s) {
/* 199 */     return regexError(s, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SideEffectFree
/*     */   public static String regexError(String s, int groups) {
/*     */     try {
/* 215 */       Pattern p = Pattern.compile(s);
/* 216 */       int actualGroups = getGroupCount(p);
/* 217 */       if (actualGroups < groups) {
/* 218 */         return regexErrorMessage(s, groups, actualGroups);
/*     */       }
/* 220 */     } catch (PatternSyntaxException e) {
/* 221 */       return e.getMessage();
/*     */     } 
/* 223 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SideEffectFree
/*     */   public static PatternSyntaxException regexException(String s) {
/* 235 */     return regexException(s, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SideEffectFree
/*     */   public static PatternSyntaxException regexException(String s, int groups) {
/*     */     try {
/* 251 */       Pattern p = Pattern.compile(s);
/* 252 */       int actualGroups = getGroupCount(p);
/* 253 */       if (actualGroups < groups) {
/* 254 */         return new PatternSyntaxException(
/* 255 */             regexErrorMessage(s, groups, actualGroups), s, -1);
/*     */       }
/* 257 */     } catch (PatternSyntaxException pse) {
/* 258 */       return pse;
/*     */     } 
/* 260 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SideEffectFree
/*     */   public static String asRegex(String s) {
/* 275 */     return asRegex(s, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SideEffectFree
/*     */   public static String asRegex(String s, int groups) {
/*     */     try {
/* 294 */       Pattern p = Pattern.compile(s);
/* 295 */       int actualGroups = getGroupCount(p);
/* 296 */       if (actualGroups < groups) {
/* 297 */         throw new Error(regexErrorMessage(s, groups, actualGroups));
/*     */       }
/* 299 */       return s;
/* 300 */     } catch (PatternSyntaxException e) {
/* 301 */       throw new Error(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SideEffectFree
/*     */   private static String regexErrorMessage(String s, int expectedGroups, int actualGroups) {
/* 316 */     return "regex \"" + s + "\" has " + actualGroups + " groups, but " + expectedGroups + " groups are needed.";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Pure
/*     */   private static int getGroupCount(Pattern p) {
/* 334 */     return p.matcher("").groupCount();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\checker\regex\RegexUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */