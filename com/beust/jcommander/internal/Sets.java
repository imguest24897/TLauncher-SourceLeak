/*    */ package com.beust.jcommander.internal;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.LinkedHashSet;
/*    */ import java.util.Set;
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
/*    */ public class Sets
/*    */ {
/*    */   public static <K> Set<K> newHashSet() {
/* 28 */     return new HashSet<>();
/*    */   }
/*    */   
/*    */   public static <K> Set<K> newLinkedHashSet() {
/* 32 */     return new LinkedHashSet<>();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\internal\Sets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */