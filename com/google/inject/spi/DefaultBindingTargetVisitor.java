/*    */ package com.google.inject.spi;
/*    */ 
/*    */ import com.google.inject.Binding;
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
/*    */ public abstract class DefaultBindingTargetVisitor<T, V>
/*    */   implements BindingTargetVisitor<T, V>
/*    */ {
/*    */   protected V visitOther(Binding<? extends T> binding) {
/* 34 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public V visit(InstanceBinding<? extends T> instanceBinding) {
/* 39 */     return visitOther(instanceBinding);
/*    */   }
/*    */ 
/*    */   
/*    */   public V visit(ProviderInstanceBinding<? extends T> providerInstanceBinding) {
/* 44 */     return visitOther(providerInstanceBinding);
/*    */   }
/*    */ 
/*    */   
/*    */   public V visit(ProviderKeyBinding<? extends T> providerKeyBinding) {
/* 49 */     return visitOther(providerKeyBinding);
/*    */   }
/*    */ 
/*    */   
/*    */   public V visit(LinkedKeyBinding<? extends T> linkedKeyBinding) {
/* 54 */     return visitOther(linkedKeyBinding);
/*    */   }
/*    */ 
/*    */   
/*    */   public V visit(ExposedBinding<? extends T> exposedBinding) {
/* 59 */     return visitOther(exposedBinding);
/*    */   }
/*    */ 
/*    */   
/*    */   public V visit(UntargettedBinding<? extends T> untargettedBinding) {
/* 64 */     return visitOther(untargettedBinding);
/*    */   }
/*    */ 
/*    */   
/*    */   public V visit(ConstructorBinding<? extends T> constructorBinding) {
/* 69 */     return visitOther(constructorBinding);
/*    */   }
/*    */ 
/*    */   
/*    */   public V visit(ConvertedConstantBinding<? extends T> convertedConstantBinding) {
/* 74 */     return visitOther(convertedConstantBinding);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public V visit(ProviderBinding<? extends T> providerBinding) {
/* 81 */     return visitOther(providerBinding);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\DefaultBindingTargetVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */