/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.common.collect.Lists;
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
/*    */ class ProcessedBindingData
/*    */ {
/* 30 */   private final List<CreationListener> creationListeners = Lists.newArrayList();
/* 31 */   private final List<Runnable> uninitializedBindings = Lists.newArrayList();
/* 32 */   private final List<Runnable> delayedUninitializedBindings = Lists.newArrayList();
/*    */   
/*    */   void addCreationListener(CreationListener listener) {
/* 35 */     this.creationListeners.add(listener);
/*    */   }
/*    */   
/*    */   void addUninitializedBinding(Runnable runnable) {
/* 39 */     this.uninitializedBindings.add(runnable);
/*    */   }
/*    */   
/*    */   void addDelayedUninitializedBinding(Runnable runnable) {
/* 43 */     this.delayedUninitializedBindings.add(runnable);
/*    */   }
/*    */ 
/*    */   
/*    */   void initializeBindings() {
/* 48 */     for (Runnable initializer : this.uninitializedBindings) {
/* 49 */       initializer.run();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void runCreationListeners(Errors errors) {
/* 59 */     for (CreationListener creationListener : this.creationListeners) {
/* 60 */       creationListener.notify(errors);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void initializeDelayedBindings() {
/* 70 */     for (Runnable initializer : this.delayedUninitializedBindings)
/* 71 */       initializer.run(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ProcessedBindingData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */