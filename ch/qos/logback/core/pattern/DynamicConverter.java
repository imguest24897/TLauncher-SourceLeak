/*     */ package ch.qos.logback.core.pattern;
/*     */ 
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.spi.ContextAware;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import ch.qos.logback.core.spi.LifeCycle;
/*     */ import ch.qos.logback.core.status.Status;
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
/*     */ public abstract class DynamicConverter<E>
/*     */   extends FormattingConverter<E>
/*     */   implements LifeCycle, ContextAware
/*     */ {
/*  26 */   ContextAwareBase cab = new ContextAwareBase(this);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> optionList;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean started = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/*  43 */     this.started = true;
/*     */   }
/*     */   
/*     */   public void stop() {
/*  47 */     this.started = false;
/*     */   }
/*     */   
/*     */   public boolean isStarted() {
/*  51 */     return this.started;
/*     */   }
/*     */   
/*     */   public void setOptionList(List<String> optionList) {
/*  55 */     this.optionList = optionList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFirstOption() {
/*  65 */     if (this.optionList == null || this.optionList.size() == 0) {
/*  66 */       return null;
/*     */     }
/*  68 */     return this.optionList.get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<String> getOptionList() {
/*  73 */     return this.optionList;
/*     */   }
/*     */   
/*     */   public void setContext(Context context) {
/*  77 */     this.cab.setContext(context);
/*     */   }
/*     */   
/*     */   public Context getContext() {
/*  81 */     return this.cab.getContext();
/*     */   }
/*     */   
/*     */   public void addStatus(Status status) {
/*  85 */     this.cab.addStatus(status);
/*     */   }
/*     */   
/*     */   public void addInfo(String msg) {
/*  89 */     this.cab.addInfo(msg);
/*     */   }
/*     */   
/*     */   public void addInfo(String msg, Throwable ex) {
/*  93 */     this.cab.addInfo(msg, ex);
/*     */   }
/*     */   
/*     */   public void addWarn(String msg) {
/*  97 */     this.cab.addWarn(msg);
/*     */   }
/*     */   
/*     */   public void addWarn(String msg, Throwable ex) {
/* 101 */     this.cab.addWarn(msg, ex);
/*     */   }
/*     */   
/*     */   public void addError(String msg) {
/* 105 */     this.cab.addError(msg);
/*     */   }
/*     */   
/*     */   public void addError(String msg, Throwable ex) {
/* 109 */     this.cab.addError(msg, ex);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\ch\qos\logback\core\pattern\DynamicConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */