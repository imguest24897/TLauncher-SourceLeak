/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.inject.MembersInjector;
/*    */ import com.google.inject.Provider;
/*    */ import com.google.inject.spi.MembersInjectorLookup;
/*    */ import com.google.inject.spi.ProviderLookup;
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
/*    */ final class LookupProcessor
/*    */   extends AbstractProcessor
/*    */ {
/*    */   LookupProcessor(Errors errors) {
/* 33 */     super(errors);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> Boolean visit(MembersInjectorLookup<T> lookup) {
/*    */     try {
/* 40 */       MembersInjector<T> membersInjector = this.injector.membersInjectorStore.get(lookup.getType(), this.errors);
/* 41 */       lookup.initializeDelegate(membersInjector);
/* 42 */       this.injector.getBindingData().putMembersInjectorLookup(lookup);
/* 43 */     } catch (ErrorsException e) {
/* 44 */       this.errors.merge(e.getErrors());
/*    */     } 
/*    */     
/* 47 */     return Boolean.valueOf(true);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> Boolean visit(ProviderLookup<T> lookup) {
/*    */     try {
/* 54 */       Provider<T> provider = this.injector.getProviderOrThrow(lookup.getDependency(), this.errors);
/* 55 */       lookup.initializeDelegate(provider);
/* 56 */       this.injector.getBindingData().putProviderLookup(lookup);
/* 57 */     } catch (ErrorsException e) {
/* 58 */       this.errors.merge(e.getErrors());
/*    */     } 
/*    */     
/* 61 */     return Boolean.valueOf(true);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\LookupProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */