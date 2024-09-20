/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
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
/*    */ class RealElement
/*    */   implements Element
/*    */ {
/* 25 */   private static final AtomicInteger nextUniqueId = new AtomicInteger(1);
/*    */   
/*    */   private final int uniqueId;
/*    */   private final String setName;
/*    */   private final Element.Type type;
/*    */   private final String keyType;
/*    */   
/*    */   RealElement(String setName, Element.Type type, String keyType) {
/* 33 */     this(setName, type, keyType, nextUniqueId.incrementAndGet());
/*    */   }
/*    */   
/*    */   RealElement(String setName, Element.Type type, String keyType, int uniqueId) {
/* 37 */     this.uniqueId = uniqueId;
/* 38 */     this.setName = setName;
/* 39 */     this.type = type;
/* 40 */     this.keyType = keyType;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setName() {
/* 45 */     return this.setName;
/*    */   }
/*    */ 
/*    */   
/*    */   public int uniqueId() {
/* 50 */     return this.uniqueId;
/*    */   }
/*    */ 
/*    */   
/*    */   public Element.Type type() {
/* 55 */     return this.type;
/*    */   }
/*    */ 
/*    */   
/*    */   public String keyType() {
/* 60 */     return this.keyType;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends Annotation> annotationType() {
/* 65 */     return (Class)Element.class;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 71 */     String str1 = Element.class.getName(), str2 = this.setName; int i = this.uniqueId; String str3 = String.valueOf(this.type), str4 = this.keyType; return (new StringBuilder(49 + String.valueOf(str1).length() + String.valueOf(str2).length() + String.valueOf(str3).length() + String.valueOf(str4).length())).append("@").append(str1).append("(setName=").append(str2).append(",uniqueId=").append(i).append(", type=").append(str3).append(", keyType=").append(str4).append(")").toString();
/*    */   }
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
/*    */   public boolean equals(Object o) {
/* 85 */     return (o instanceof Element && ((Element)o)
/* 86 */       .setName().equals(setName()) && ((Element)o)
/* 87 */       .uniqueId() == uniqueId() && ((Element)o)
/* 88 */       .type() == type() && ((Element)o)
/* 89 */       .keyType().equals(keyType()));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 94 */     return (127 * "setName".hashCode() ^ this.setName.hashCode()) + (127 * "uniqueId"
/* 95 */       .hashCode() ^ this.uniqueId) + (127 * "type"
/* 96 */       .hashCode() ^ this.type.hashCode()) + (127 * "keyType"
/* 97 */       .hashCode() ^ this.keyType.hashCode());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\RealElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */