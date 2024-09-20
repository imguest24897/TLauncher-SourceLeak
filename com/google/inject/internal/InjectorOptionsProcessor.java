/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.inject.Stage;
/*    */ import com.google.inject.spi.DisableCircularProxiesOption;
/*    */ import com.google.inject.spi.RequireAtInjectOnConstructorsOption;
/*    */ import com.google.inject.spi.RequireExactBindingAnnotationsOption;
/*    */ import com.google.inject.spi.RequireExplicitBindingsOption;
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
/*    */ class InjectorOptionsProcessor
/*    */   extends AbstractProcessor
/*    */ {
/*    */   private boolean disableCircularProxies = false;
/*    */   private boolean jitDisabled = false;
/*    */   private boolean atInjectRequired = false;
/*    */   private boolean exactBindingAnnotationsRequired = false;
/*    */   
/*    */   InjectorOptionsProcessor(Errors errors) {
/* 42 */     super(errors);
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean visit(DisableCircularProxiesOption option) {
/* 47 */     this.disableCircularProxies = true;
/* 48 */     return Boolean.valueOf(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean visit(RequireExplicitBindingsOption option) {
/* 53 */     this.jitDisabled = true;
/* 54 */     return Boolean.valueOf(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean visit(RequireAtInjectOnConstructorsOption option) {
/* 59 */     this.atInjectRequired = true;
/* 60 */     return Boolean.valueOf(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean visit(RequireExactBindingAnnotationsOption option) {
/* 65 */     this.exactBindingAnnotationsRequired = true;
/* 66 */     return Boolean.valueOf(true);
/*    */   }
/*    */   
/*    */   InjectorImpl.InjectorOptions getOptions(Stage stage, InjectorImpl.InjectorOptions parentOptions) {
/* 70 */     Preconditions.checkNotNull(stage, "stage must be set");
/* 71 */     if (parentOptions == null) {
/* 72 */       return new InjectorImpl.InjectorOptions(stage, this.jitDisabled, this.disableCircularProxies, this.atInjectRequired, this.exactBindingAnnotationsRequired);
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 79 */     Preconditions.checkState((stage == parentOptions.stage), "child & parent stage don't match");
/* 80 */     return new InjectorImpl.InjectorOptions(stage, (this.jitDisabled || parentOptions.jitDisabled), (this.disableCircularProxies || parentOptions.disableCircularProxies), (this.atInjectRequired || parentOptions.atInjectRequired), (this.exactBindingAnnotationsRequired || parentOptions.exactBindingAnnotationsRequired));
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\InjectorOptionsProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */