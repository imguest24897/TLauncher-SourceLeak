/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.inject.Key;
/*    */ import com.google.inject.MembersInjector;
/*    */ import com.google.inject.Provider;
/*    */ import com.google.inject.TypeLiteral;
/*    */ import com.google.inject.spi.Element;
/*    */ import com.google.inject.spi.MembersInjectorLookup;
/*    */ import com.google.inject.spi.ProviderLookup;
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
/*    */ final class DeferredLookups
/*    */   implements Lookups
/*    */ {
/*    */   private final InjectorImpl injector;
/* 37 */   private final List<Element> lookups = Lists.newArrayList();
/*    */   
/*    */   DeferredLookups(InjectorImpl injector) {
/* 40 */     this.injector = injector;
/*    */   }
/*    */ 
/*    */   
/*    */   void initialize(Errors errors) {
/* 45 */     this.injector.lookups = this.injector;
/* 46 */     (new LookupProcessor(errors)).process(this.injector, this.lookups);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Provider<T> getProvider(Key<T> key) {
/* 51 */     ProviderLookup<T> lookup = new ProviderLookup(key, key);
/* 52 */     this.lookups.add(lookup);
/* 53 */     return lookup.getProvider();
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> type) {
/* 58 */     MembersInjectorLookup<T> lookup = new MembersInjectorLookup(type, type);
/* 59 */     this.lookups.add(lookup);
/* 60 */     return lookup.getMembersInjector();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\DeferredLookups.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */