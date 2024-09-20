/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.inject.spi.Dependency;
/*    */ import com.google.inject.spi.InjectionPoint;
/*    */ import java.lang.reflect.Field;
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
/*    */ final class SingleFieldInjector
/*    */   implements SingleMemberInjector
/*    */ {
/*    */   final Field field;
/*    */   final InjectionPoint injectionPoint;
/*    */   final Dependency<?> dependency;
/*    */   final BindingImpl<?> binding;
/*    */   
/*    */   public SingleFieldInjector(InjectorImpl injector, InjectionPoint injectionPoint, Errors errors) throws ErrorsException {
/* 33 */     this.injectionPoint = injectionPoint;
/* 34 */     this.field = (Field)injectionPoint.getMember();
/* 35 */     this.dependency = injectionPoint.getDependencies().get(0);
/*    */ 
/*    */     
/* 38 */     this.field.setAccessible(true);
/* 39 */     this.binding = injector.getBindingOrThrow(this.dependency.getKey(), errors, InjectorImpl.JitLimitation.NO_JIT);
/*    */   }
/*    */ 
/*    */   
/*    */   public InjectionPoint getInjectionPoint() {
/* 44 */     return this.injectionPoint;
/*    */   }
/*    */ 
/*    */   
/*    */   public void inject(InternalContext context, Object o) throws InternalProvisionException {
/*    */     try {
/* 50 */       Object value = this.binding.getInternalFactory().get(context, this.dependency, false);
/* 51 */       this.field.set(o, value);
/* 52 */     } catch (InternalProvisionException e) {
/* 53 */       throw e.addSource(this.dependency);
/* 54 */     } catch (IllegalAccessException e) {
/* 55 */       throw new AssertionError(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\SingleFieldInjector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */