/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.inject.Binding;
/*     */ import com.google.inject.spi.ProvisionListener;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ final class ProvisionListenerStackCallback<T>
/*     */ {
/*  33 */   private static final ProvisionListener[] EMPTY_LISTENER = new ProvisionListener[0];
/*     */ 
/*     */   
/*  36 */   private static final ProvisionListenerStackCallback<?> EMPTY_CALLBACK = new ProvisionListenerStackCallback(null, 
/*  37 */       (List<ProvisionListener>)ImmutableList.of());
/*     */   
/*     */   private final ProvisionListener[] listeners;
/*     */   
/*     */   private final Binding<T> binding;
/*     */   
/*     */   public static <T> ProvisionListenerStackCallback<T> emptyListener() {
/*  44 */     return (ProvisionListenerStackCallback)EMPTY_CALLBACK;
/*     */   }
/*     */   
/*     */   public ProvisionListenerStackCallback(Binding<T> binding, List<ProvisionListener> listeners) {
/*  48 */     this.binding = binding;
/*  49 */     if (listeners.isEmpty()) {
/*  50 */       this.listeners = EMPTY_LISTENER;
/*     */     } else {
/*  52 */       Set<ProvisionListener> deDuplicated = Sets.newLinkedHashSet(listeners);
/*  53 */       this.listeners = deDuplicated.<ProvisionListener>toArray(new ProvisionListener[deDuplicated.size()]);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean hasListeners() {
/*  58 */     return (this.listeners.length > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public T provision(InternalContext context, ProvisionCallback<T> callable) throws InternalProvisionException {
/*  63 */     Provision provision = new Provision(callable);
/*  64 */     RuntimeException caught = null;
/*     */     try {
/*  66 */       provision.provision();
/*  67 */     } catch (RuntimeException t) {
/*  68 */       caught = t;
/*     */     } 
/*     */     
/*  71 */     if (provision.exceptionDuringProvision != null)
/*  72 */       throw provision.exceptionDuringProvision; 
/*  73 */     if (caught != null) {
/*     */       
/*  75 */       Object listener = (provision.erredListener != null) ? provision.erredListener.getClass() : "(unknown)";
/*  76 */       throw InternalProvisionException.errorInUserCode(ErrorId.OTHER, caught, "Error notifying ProvisionListener %s of %s.%n Reason: %s", new Object[] { listener, this.binding
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*  81 */             .getKey(), caught });
/*     */     } 
/*     */     
/*  84 */     return provision.result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class Provision
/*     */     extends ProvisionListener.ProvisionInvocation<T>
/*     */   {
/*     */     final ProvisionListenerStackCallback.ProvisionCallback<T> callable;
/*     */ 
/*     */     
/*  95 */     int index = -1;
/*     */     T result;
/*     */     InternalProvisionException exceptionDuringProvision;
/*     */     ProvisionListener erredListener;
/*     */     
/*     */     public Provision(ProvisionListenerStackCallback.ProvisionCallback<T> callable) {
/* 101 */       this.callable = callable;
/*     */     }
/*     */ 
/*     */     
/*     */     public T provision() {
/* 106 */       this.index++;
/* 107 */       if (this.index == ProvisionListenerStackCallback.this.listeners.length) {
/*     */         try {
/* 109 */           this.result = this.callable.call();
/* 110 */         } catch (InternalProvisionException ipe) {
/* 111 */           this.exceptionDuringProvision = ipe;
/* 112 */           throw ipe.toProvisionException();
/*     */         } 
/* 114 */       } else if (this.index < ProvisionListenerStackCallback.this.listeners.length) {
/* 115 */         int currentIdx = this.index;
/*     */         try {
/* 117 */           ProvisionListenerStackCallback.this.listeners[this.index].onProvision(this);
/* 118 */         } catch (RuntimeException re) {
/* 119 */           this.erredListener = ProvisionListenerStackCallback.this.listeners[currentIdx];
/* 120 */           throw re;
/*     */         } 
/* 122 */         if (currentIdx == this.index)
/*     */         {
/* 124 */           provision();
/*     */         }
/*     */       } else {
/* 127 */         throw new IllegalStateException("Already provisioned in this listener.");
/*     */       } 
/* 129 */       return this.result;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Binding<T> getBinding() {
/* 137 */       return ProvisionListenerStackCallback.this.binding;
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface ProvisionCallback<T> {
/*     */     T call() throws InternalProvisionException;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\ProvisionListenerStackCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */