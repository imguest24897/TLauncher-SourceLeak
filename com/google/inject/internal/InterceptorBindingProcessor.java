/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.inject.spi.InterceptorBinding;
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
/*    */ final class InterceptorBindingProcessor
/*    */   extends AbstractProcessor
/*    */ {
/*    */   InterceptorBindingProcessor(Errors errors) {
/* 30 */     super(errors);
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean visit(InterceptorBinding command) {
/* 35 */     if (InternalFlags.isBytecodeGenEnabled()) {
/* 36 */       this.injector
/* 37 */         .getBindingData()
/* 38 */         .addMethodAspect(new MethodAspect(command
/*    */             
/* 40 */             .getClassMatcher(), command
/* 41 */             .getMethodMatcher(), command
/* 42 */             .getInterceptors()));
/*    */     } else {
/* 44 */       this.errors.aopDisabled(command);
/*    */     } 
/*    */     
/* 47 */     return Boolean.valueOf(true);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\InterceptorBindingProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */