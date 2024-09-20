/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.inject.spi.ProvisionListenerBinding;
/*    */ import com.google.inject.spi.TypeListenerBinding;
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
/*    */ final class ListenerBindingProcessor
/*    */   extends AbstractProcessor
/*    */ {
/*    */   ListenerBindingProcessor(Errors errors) {
/* 30 */     super(errors);
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean visit(TypeListenerBinding binding) {
/* 35 */     this.injector.getBindingData().addTypeListener(binding);
/* 36 */     return Boolean.valueOf(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean visit(ProvisionListenerBinding binding) {
/* 41 */     this.injector.getBindingData().addProvisionListener(binding);
/* 42 */     return Boolean.valueOf(true);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ListenerBindingProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */