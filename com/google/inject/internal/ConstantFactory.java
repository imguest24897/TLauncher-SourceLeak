/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.common.base.MoreObjects;
/*    */ import com.google.inject.spi.Dependency;
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
/*    */ final class ConstantFactory<T>
/*    */   implements InternalFactory<T>
/*    */ {
/*    */   private final Initializable<T> initializable;
/*    */   
/*    */   public ConstantFactory(Initializable<T> initializable) {
/* 28 */     this.initializable = initializable;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public T get(InternalContext context, Dependency<?> dependency, boolean linked) throws InternalProvisionException {
/* 34 */     return this.initializable.get();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 39 */     return MoreObjects.toStringHelper(ConstantFactory.class).add("value", this.initializable).toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ConstantFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */