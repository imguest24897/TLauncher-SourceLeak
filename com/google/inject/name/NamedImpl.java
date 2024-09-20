/*    */ package com.google.inject.name;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.inject.internal.Annotations;
/*    */ import java.io.Serializable;
/*    */ import java.lang.annotation.Annotation;
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
/*    */ class NamedImpl
/*    */   implements Named, Serializable
/*    */ {
/*    */   private final String value;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public NamedImpl(String value) {
/* 30 */     this.value = (String)Preconditions.checkNotNull(value, "name");
/*    */   }
/*    */ 
/*    */   
/*    */   public String value() {
/* 35 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 41 */     return 127 * "value".hashCode() ^ this.value.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 46 */     if (!(o instanceof Named)) {
/* 47 */       return false;
/*    */     }
/*    */     
/* 50 */     Named other = (Named)o;
/* 51 */     return this.value.equals(other.value());
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 56 */     String str1 = Named.class.getName(), str2 = Annotations.memberValueString("value", this.value); return (new StringBuilder(3 + String.valueOf(str1).length() + String.valueOf(str2).length())).append("@").append(str1).append("(").append(str2).append(")").toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends Annotation> annotationType() {
/* 61 */     return (Class)Named.class;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\name\NamedImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */