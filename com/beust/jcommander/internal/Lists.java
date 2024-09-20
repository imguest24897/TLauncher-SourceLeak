/*    */ package com.beust.jcommander.internal;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.LinkedList;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Lists
/*    */ {
/*    */   public static <K> List<K> newArrayList() {
/* 30 */     return new ArrayList<>();
/*    */   }
/*    */   
/*    */   public static <K> List<K> newArrayList(Collection<K> c) {
/* 34 */     return new ArrayList<>(c);
/*    */   }
/*    */   
/*    */   public static <K> List<K> newArrayList(K... c) {
/* 38 */     return new ArrayList<>(Arrays.asList(c));
/*    */   }
/*    */   
/*    */   public static <K> List<K> newArrayList(int size) {
/* 42 */     return new ArrayList<>(size);
/*    */   }
/*    */   
/*    */   public static <K> LinkedList<K> newLinkedList() {
/* 46 */     return new LinkedList<>();
/*    */   }
/*    */   
/*    */   public static <K> LinkedList<K> newLinkedList(Collection<K> c) {
/* 50 */     return new LinkedList<>(c);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\internal\Lists.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */