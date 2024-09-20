/*    */ package com.google.inject.spi;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.inject.Binder;
/*    */ import com.google.inject.TypeLiteral;
/*    */ import com.google.inject.internal.Errors;
/*    */ import com.google.inject.matcher.Matcher;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class TypeConverterBinding
/*    */   implements Element
/*    */ {
/*    */   private final Object source;
/*    */   private final Matcher<? super TypeLiteral<?>> typeMatcher;
/*    */   private final TypeConverter typeConverter;
/*    */   
/*    */   public TypeConverterBinding(Object source, Matcher<? super TypeLiteral<?>> typeMatcher, TypeConverter typeConverter) {
/* 46 */     this.source = Preconditions.checkNotNull(source, "source");
/* 47 */     this.typeMatcher = (Matcher<? super TypeLiteral<?>>)Preconditions.checkNotNull(typeMatcher, "typeMatcher");
/* 48 */     this.typeConverter = (TypeConverter)Preconditions.checkNotNull(typeConverter, "typeConverter");
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getSource() {
/* 53 */     return this.source;
/*    */   }
/*    */   
/*    */   public Matcher<? super TypeLiteral<?>> getTypeMatcher() {
/* 57 */     return this.typeMatcher;
/*    */   }
/*    */   
/*    */   public TypeConverter getTypeConverter() {
/* 61 */     return this.typeConverter;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T acceptVisitor(ElementVisitor<T> visitor) {
/* 66 */     return visitor.visit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void applyTo(Binder binder) {
/* 71 */     binder.withSource(getSource()).convertToTypes(this.typeMatcher, this.typeConverter);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 76 */     String str1 = String.valueOf(this.typeConverter), str2 = String.valueOf(this.typeMatcher);
/*    */ 
/*    */ 
/*    */     
/* 80 */     String str3 = String.valueOf(Errors.convert(this.source)); return (new StringBuilder(27 + String.valueOf(str1).length() + String.valueOf(str2).length() + String.valueOf(str3).length())).append(str1).append(" which matches ").append(str2).append(" (bound at ").append(str3).append(")").toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\TypeConverterBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */