/*     */ package ch.qos.logback.core;
/*     */ 
/*     */ import ch.qos.logback.core.filter.Filter;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import ch.qos.logback.core.spi.FilterAttachableImpl;
/*     */ import ch.qos.logback.core.spi.FilterReply;
/*     */ import ch.qos.logback.core.status.Status;
/*     */ import ch.qos.logback.core.status.WarnStatus;
/*     */ import java.util.List;
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
/*     */ public abstract class UnsynchronizedAppenderBase<E>
/*     */   extends ContextAwareBase
/*     */   implements Appender<E>
/*     */ {
/*     */   protected boolean started = false;
/*  42 */   private ThreadLocal<Boolean> guard = new ThreadLocal<Boolean>();
/*     */ 
/*     */ 
/*     */   
/*     */   protected String name;
/*     */ 
/*     */   
/*  49 */   private FilterAttachableImpl<E> fai = new FilterAttachableImpl();
/*     */   
/*     */   public String getName() {
/*  52 */     return this.name;
/*     */   }
/*     */   
/*  55 */   private int statusRepeatCount = 0;
/*  56 */   private int exceptionCount = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   static final int ALLOWED_REPEATS = 3;
/*     */ 
/*     */ 
/*     */   
/*     */   public void doAppend(E eventObject) {
/*  65 */     if (Boolean.TRUE.equals(this.guard.get())) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/*  70 */       this.guard.set(Boolean.TRUE);
/*     */       
/*  72 */       if (!this.started) {
/*  73 */         if (this.statusRepeatCount++ < 3) {
/*  74 */           addStatus((Status)new WarnStatus("Attempted to append to non started appender [" + this.name + "].", this));
/*     */         }
/*     */         
/*     */         return;
/*     */       } 
/*  79 */       if (getFilterChainDecision(eventObject) == FilterReply.DENY) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/*  84 */       append(eventObject);
/*     */     }
/*  86 */     catch (Exception e) {
/*  87 */       if (this.exceptionCount++ < 3) {
/*  88 */         addError("Appender [" + this.name + "] failed to append.", e);
/*     */       }
/*     */     } finally {
/*  91 */       this.guard.set(Boolean.FALSE);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void append(E paramE);
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 101 */     this.name = name;
/*     */   }
/*     */   
/*     */   public void start() {
/* 105 */     this.started = true;
/*     */   }
/*     */   
/*     */   public void stop() {
/* 109 */     this.started = false;
/*     */   }
/*     */   
/*     */   public boolean isStarted() {
/* 113 */     return this.started;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 117 */     return getClass().getName() + "[" + this.name + "]";
/*     */   }
/*     */   
/*     */   public void addFilter(Filter<E> newFilter) {
/* 121 */     this.fai.addFilter(newFilter);
/*     */   }
/*     */   
/*     */   public void clearAllFilters() {
/* 125 */     this.fai.clearAllFilters();
/*     */   }
/*     */   
/*     */   public List<Filter<E>> getCopyOfAttachedFiltersList() {
/* 129 */     return this.fai.getCopyOfAttachedFiltersList();
/*     */   }
/*     */   
/*     */   public FilterReply getFilterChainDecision(E event) {
/* 133 */     return this.fai.getFilterChainDecision(event);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\ch\qos\logback\core\UnsynchronizedAppenderBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */