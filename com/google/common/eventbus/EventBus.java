/*     */ package com.google.common.eventbus;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.util.concurrent.MoreExecutors;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public class EventBus
/*     */ {
/*  98 */   private static final Logger logger = Logger.getLogger(EventBus.class.getName());
/*     */   
/*     */   private final String identifier;
/*     */   
/*     */   private final Executor executor;
/*     */   private final SubscriberExceptionHandler exceptionHandler;
/* 104 */   private final SubscriberRegistry subscribers = new SubscriberRegistry(this);
/*     */   
/*     */   private final Dispatcher dispatcher;
/*     */   
/*     */   public EventBus() {
/* 109 */     this("default");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EventBus(String identifier) {
/* 119 */     this(identifier, 
/*     */         
/* 121 */         MoreExecutors.directExecutor(), 
/* 122 */         Dispatcher.perThreadDispatchQueue(), LoggingHandler.INSTANCE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EventBus(SubscriberExceptionHandler exceptionHandler) {
/* 133 */     this("default", 
/*     */         
/* 135 */         MoreExecutors.directExecutor(), 
/* 136 */         Dispatcher.perThreadDispatchQueue(), exceptionHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   EventBus(String identifier, Executor executor, Dispatcher dispatcher, SubscriberExceptionHandler exceptionHandler) {
/* 145 */     this.identifier = (String)Preconditions.checkNotNull(identifier);
/* 146 */     this.executor = (Executor)Preconditions.checkNotNull(executor);
/* 147 */     this.dispatcher = (Dispatcher)Preconditions.checkNotNull(dispatcher);
/* 148 */     this.exceptionHandler = (SubscriberExceptionHandler)Preconditions.checkNotNull(exceptionHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String identifier() {
/* 157 */     return this.identifier;
/*     */   }
/*     */ 
/*     */   
/*     */   final Executor executor() {
/* 162 */     return this.executor;
/*     */   }
/*     */ 
/*     */   
/*     */   void handleSubscriberException(Throwable e, SubscriberExceptionContext context) {
/* 167 */     Preconditions.checkNotNull(e);
/* 168 */     Preconditions.checkNotNull(context);
/*     */     try {
/* 170 */       this.exceptionHandler.handleException(e, context);
/* 171 */     } catch (Throwable e2) {
/*     */       
/* 173 */       logger.log(Level.SEVERE, 
/*     */           
/* 175 */           String.format(Locale.ROOT, "Exception %s thrown while handling exception: %s", new Object[] { e2, e }), e2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void register(Object object) {
/* 186 */     this.subscribers.register(object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unregister(Object object) {
/* 196 */     this.subscribers.unregister(object);
/*     */   }
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
/*     */   public void post(Object event) {
/* 210 */     Iterator<Subscriber> eventSubscribers = this.subscribers.getSubscribers(event);
/* 211 */     if (eventSubscribers.hasNext()) {
/* 212 */       this.dispatcher.dispatch(event, eventSubscribers);
/* 213 */     } else if (!(event instanceof DeadEvent)) {
/*     */       
/* 215 */       post(new DeadEvent(this, event));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 221 */     return MoreObjects.toStringHelper(this).addValue(this.identifier).toString();
/*     */   }
/*     */   
/*     */   static final class LoggingHandler
/*     */     implements SubscriberExceptionHandler {
/* 226 */     static final LoggingHandler INSTANCE = new LoggingHandler();
/*     */ 
/*     */     
/*     */     public void handleException(Throwable exception, SubscriberExceptionContext context) {
/* 230 */       Logger logger = logger(context);
/* 231 */       if (logger.isLoggable(Level.SEVERE)) {
/* 232 */         logger.log(Level.SEVERE, message(context), exception);
/*     */       }
/*     */     }
/*     */     
/*     */     private static Logger logger(SubscriberExceptionContext context) {
/* 237 */       String str1 = EventBus.class.getName(), str2 = context.getEventBus().identifier(); return Logger.getLogger((new StringBuilder(1 + String.valueOf(str1).length() + String.valueOf(str2).length())).append(str1).append(".").append(str2).toString());
/*     */     }
/*     */     
/*     */     private static String message(SubscriberExceptionContext context) {
/* 241 */       Method method = context.getSubscriberMethod();
/*     */       
/* 243 */       String str1 = method.getName();
/*     */       
/* 245 */       String str2 = method.getParameterTypes()[0].getName();
/*     */ 
/*     */       
/* 248 */       String str3 = String.valueOf(context.getSubscriber());
/*     */       
/* 250 */       String str4 = String.valueOf(context.getEvent()); return (new StringBuilder(80 + String.valueOf(str1).length() + String.valueOf(str2).length() + String.valueOf(str3).length() + String.valueOf(str4).length())).append("Exception thrown by subscriber method ").append(str1).append('(').append(str2).append(')').append(" on subscriber ").append(str3).append(" when dispatching event: ").append(str4).toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\eventbus\EventBus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */