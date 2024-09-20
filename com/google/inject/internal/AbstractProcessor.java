/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.inject.spi.DefaultElementVisitor;
/*    */ import com.google.inject.spi.Element;
/*    */ import com.google.inject.spi.ElementVisitor;
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
/*    */ 
/*    */ 
/*    */ abstract class AbstractProcessor
/*    */   extends DefaultElementVisitor<Boolean>
/*    */ {
/*    */   protected Errors errors;
/*    */   protected InjectorImpl injector;
/*    */   
/*    */   protected AbstractProcessor(Errors errors) {
/* 37 */     this.errors = errors;
/*    */   }
/*    */   
/*    */   public void process(Iterable<InjectorShell> isolatedInjectorBuilders) {
/* 41 */     for (InjectorShell injectorShell : isolatedInjectorBuilders) {
/* 42 */       process(injectorShell.getInjector(), injectorShell.getElements());
/*    */     }
/*    */   }
/*    */   
/*    */   public void process(InjectorImpl injector, List<Element> elements) {
/* 47 */     Errors errorsAnyElement = this.errors;
/* 48 */     this.injector = injector;
/*    */     try {
/* 50 */       elements.removeIf(e -> {
/*    */             this.errors = errorsAnyElement.withSource(e.getSource());
/*    */             
/*    */             return ((Boolean)e.acceptVisitor((ElementVisitor)this)).booleanValue();
/*    */           });
/*    */     } finally {
/* 56 */       this.errors = errorsAnyElement;
/* 57 */       this.injector = null;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected Boolean visitOther(Element element) {
/* 63 */     return Boolean.valueOf(false);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\AbstractProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */