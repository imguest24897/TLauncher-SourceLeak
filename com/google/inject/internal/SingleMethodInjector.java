/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.inject.spi.InjectionPoint;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Modifier;
/*    */ import java.util.function.BiFunction;
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
/*    */ final class SingleMethodInjector
/*    */   implements SingleMemberInjector
/*    */ {
/*    */   private final InjectorImpl.MethodInvoker methodInvoker;
/*    */   private final SingleParameterInjector<?>[] parameterInjectors;
/*    */   private final InjectionPoint injectionPoint;
/*    */   
/*    */   SingleMethodInjector(InjectorImpl injector, InjectionPoint injectionPoint, Errors errors) throws ErrorsException {
/* 34 */     this.injectionPoint = injectionPoint;
/* 35 */     Method method = (Method)injectionPoint.getMember();
/* 36 */     this.methodInvoker = createMethodInvoker(method);
/* 37 */     this.parameterInjectors = injector.getParametersInjectors(injectionPoint.getDependencies(), errors);
/*    */   }
/*    */   
/*    */   private InjectorImpl.MethodInvoker createMethodInvoker(final Method method) {
/* 41 */     if (InternalFlags.isBytecodeGenEnabled()) {
/*    */       try {
/* 43 */         final BiFunction<Object, Object[], Object> fastMethod = BytecodeGen.fastMethod(method);
/* 44 */         if (fastMethod != null) {
/* 45 */           return new InjectorImpl.MethodInvoker(this)
/*    */             {
/*    */               public Object invoke(Object target, Object... parameters) throws InvocationTargetException
/*    */               {
/*    */                 try {
/* 50 */                   return fastMethod.apply(target, parameters);
/* 51 */                 } catch (Throwable e) {
/* 52 */                   throw new InvocationTargetException(e);
/*    */                 } 
/*    */               }
/*    */             };
/*    */         }
/* 57 */       } catch (Exception|LinkageError exception) {}
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 62 */     int modifiers = method.getModifiers();
/* 63 */     if (!Modifier.isPublic(modifiers) || 
/* 64 */       !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
/* 65 */       method.setAccessible(true);
/*    */     }
/*    */     
/* 68 */     return new InjectorImpl.MethodInvoker(this)
/*    */       {
/*    */         public Object invoke(Object target, Object... parameters) throws IllegalAccessException, InvocationTargetException
/*    */         {
/* 72 */           return method.invoke(target, parameters);
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   public InjectionPoint getInjectionPoint() {
/* 79 */     return this.injectionPoint;
/*    */   }
/*    */ 
/*    */   
/*    */   public void inject(InternalContext context, Object o) throws InternalProvisionException {
/* 84 */     Object[] parameters = SingleParameterInjector.getAll(context, this.parameterInjectors);
/*    */     
/*    */     try {
/* 87 */       this.methodInvoker.invoke(o, parameters);
/* 88 */     } catch (IllegalAccessException e) {
/* 89 */       throw new AssertionError(e);
/* 90 */     } catch (InvocationTargetException userException) {
/* 91 */       Throwable cause = (userException.getCause() != null) ? userException.getCause() : userException;
/* 92 */       throw InternalProvisionException.errorInjectingMethod(cause).addSource(this.injectionPoint);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\SingleMethodInjector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */