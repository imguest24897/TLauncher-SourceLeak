/*     */ package com.google.inject.spi;
/*     */ 
/*     */ import com.google.inject.Binding;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DefaultElementVisitor<V>
/*     */   implements ElementVisitor<V>
/*     */ {
/*     */   protected V visitOther(Element element) {
/*  34 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public V visit(Message message) {
/*  39 */     return visitOther(message);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> V visit(Binding<T> binding) {
/*  44 */     return visitOther((Element)binding);
/*     */   }
/*     */ 
/*     */   
/*     */   public V visit(InterceptorBinding interceptorBinding) {
/*  49 */     return visitOther(interceptorBinding);
/*     */   }
/*     */ 
/*     */   
/*     */   public V visit(ScopeBinding scopeBinding) {
/*  54 */     return visitOther(scopeBinding);
/*     */   }
/*     */ 
/*     */   
/*     */   public V visit(TypeConverterBinding typeConverterBinding) {
/*  59 */     return visitOther(typeConverterBinding);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> V visit(ProviderLookup<T> providerLookup) {
/*  64 */     return visitOther(providerLookup);
/*     */   }
/*     */ 
/*     */   
/*     */   public V visit(InjectionRequest<?> injectionRequest) {
/*  69 */     return visitOther(injectionRequest);
/*     */   }
/*     */ 
/*     */   
/*     */   public V visit(StaticInjectionRequest staticInjectionRequest) {
/*  74 */     return visitOther(staticInjectionRequest);
/*     */   }
/*     */ 
/*     */   
/*     */   public V visit(PrivateElements privateElements) {
/*  79 */     return visitOther(privateElements);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> V visit(MembersInjectorLookup<T> lookup) {
/*  84 */     return visitOther(lookup);
/*     */   }
/*     */ 
/*     */   
/*     */   public V visit(TypeListenerBinding binding) {
/*  89 */     return visitOther(binding);
/*     */   }
/*     */ 
/*     */   
/*     */   public V visit(ProvisionListenerBinding binding) {
/*  94 */     return visitOther(binding);
/*     */   }
/*     */ 
/*     */   
/*     */   public V visit(DisableCircularProxiesOption option) {
/*  99 */     return visitOther(option);
/*     */   }
/*     */ 
/*     */   
/*     */   public V visit(RequireExplicitBindingsOption option) {
/* 104 */     return visitOther(option);
/*     */   }
/*     */ 
/*     */   
/*     */   public V visit(RequireAtInjectOnConstructorsOption option) {
/* 109 */     return visitOther(option);
/*     */   }
/*     */ 
/*     */   
/*     */   public V visit(RequireExactBindingAnnotationsOption option) {
/* 114 */     return visitOther(option);
/*     */   }
/*     */ 
/*     */   
/*     */   public V visit(ModuleAnnotatedMethodScannerBinding binding) {
/* 119 */     return visitOther(binding);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\DefaultElementVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */