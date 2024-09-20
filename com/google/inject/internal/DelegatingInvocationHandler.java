/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.lang.reflect.InvocationHandler;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
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
/*    */ class DelegatingInvocationHandler<T>
/*    */   implements InvocationHandler
/*    */ {
/*    */   private volatile boolean initialized;
/*    */   private T delegate;
/*    */   
/*    */   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/*    */     try {
/* 34 */       Preconditions.checkState(this.initialized, "This is a proxy used to support circular references. The object we're proxying is not constructed yet. Please wait until after injection has completed to use this object.");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 40 */       Preconditions.checkNotNull(this.delegate, "This is a proxy used to support circular references. The object we're  proxying is initialized to null. No methods can be called.");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 50 */       return method.invoke(this.delegate, args);
/* 51 */     } catch (IllegalAccessException e) {
/* 52 */       throw new RuntimeException(e);
/* 53 */     } catch (IllegalArgumentException e) {
/* 54 */       throw new RuntimeException(e);
/* 55 */     } catch (InvocationTargetException e) {
/* 56 */       throw e.getTargetException();
/*    */     } 
/*    */   }
/*    */   
/*    */   void setDelegate(T delegate) {
/* 61 */     this.delegate = delegate;
/* 62 */     this.initialized = true;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\DelegatingInvocationHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */