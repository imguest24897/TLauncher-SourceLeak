/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.inject.Binding;
/*    */ import com.google.inject.spi.BindingTargetVisitor;
/*    */ import com.google.inject.spi.UntargettedBinding;
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
/*    */ class UntargettedBindingProcessor
/*    */   extends AbstractBindingProcessor
/*    */ {
/*    */   UntargettedBindingProcessor(Errors errors, ProcessedBindingData processedBindingData) {
/* 30 */     super(errors, processedBindingData);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Boolean visit(Binding<T> binding) {
/* 35 */     return (Boolean)binding.acceptTargetVisitor((BindingTargetVisitor)new AbstractBindingProcessor.Processor<T, Boolean>((BindingImpl)binding)
/*    */         {
/*    */           public Boolean visit(UntargettedBinding<? extends T> untargetted)
/*    */           {
/* 39 */             prepareBinding();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */             
/* 45 */             if (this.key.getAnnotationType() != null) {
/* 46 */               UntargettedBindingProcessor.this.errors.missingImplementationWithHint(this.key, UntargettedBindingProcessor.this.injector);
/* 47 */               UntargettedBindingProcessor.this.putBinding(UntargettedBindingProcessor.this.invalidBinding(UntargettedBindingProcessor.this.injector, this.key, this.source));
/* 48 */               return Boolean.valueOf(true);
/*    */             } 
/*    */ 
/*    */ 
/*    */             
/*    */             try {
/* 54 */               BindingImpl<T> binding = UntargettedBindingProcessor.this.injector.createUninitializedBinding(this.key, this.scoping, this.source, UntargettedBindingProcessor.this.errors, false);
/* 55 */               scheduleInitialization(binding);
/* 56 */               UntargettedBindingProcessor.this.putBinding(binding);
/* 57 */             } catch (ErrorsException e) {
/* 58 */               UntargettedBindingProcessor.this.errors.merge(e.getErrors());
/* 59 */               UntargettedBindingProcessor.this.putBinding(UntargettedBindingProcessor.this.invalidBinding(UntargettedBindingProcessor.this.injector, this.key, this.source));
/*    */             } 
/*    */             
/* 62 */             return Boolean.valueOf(true);
/*    */           }
/*    */ 
/*    */           
/*    */           protected Boolean visitOther(Binding<? extends T> binding) {
/* 67 */             return Boolean.valueOf(false);
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\UntargettedBindingProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */