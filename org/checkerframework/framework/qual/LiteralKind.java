/*    */ package org.checkerframework.framework.qual;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
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
/*    */ public enum LiteralKind
/*    */ {
/* 17 */   NULL,
/*    */   
/* 19 */   INT,
/*    */   
/* 21 */   LONG,
/*    */   
/* 23 */   FLOAT,
/*    */   
/* 25 */   DOUBLE,
/*    */   
/* 27 */   BOOLEAN,
/*    */   
/* 29 */   CHAR,
/*    */   
/* 31 */   STRING,
/*    */   
/* 33 */   ALL,
/*    */ 
/*    */ 
/*    */   
/* 37 */   PRIMITIVE;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static List<LiteralKind> allLiteralKinds() {
/* 46 */     List<LiteralKind> list = new ArrayList<>(Arrays.asList(values()));
/* 47 */     list.remove(ALL);
/* 48 */     list.remove(PRIMITIVE);
/* 49 */     return list;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static List<LiteralKind> primitiveLiteralKinds() {
/* 60 */     return new ArrayList<>(Arrays.asList(new LiteralKind[] { INT, LONG, FLOAT, DOUBLE, BOOLEAN, CHAR }));
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\checkerframework\framework\qual\LiteralKind.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */