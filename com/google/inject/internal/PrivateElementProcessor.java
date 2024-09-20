/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.inject.spi.PrivateElements;
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
/*    */ final class PrivateElementProcessor
/*    */   extends AbstractProcessor
/*    */ {
/* 30 */   private final List<InjectorShell.Builder> injectorShellBuilders = Lists.newArrayList();
/*    */   
/*    */   PrivateElementProcessor(Errors errors) {
/* 33 */     super(errors);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Boolean visit(PrivateElements privateElements) {
/* 39 */     InjectorShell.Builder builder = (new InjectorShell.Builder()).parent(this.injector).privateElements(privateElements);
/* 40 */     this.injectorShellBuilders.add(builder);
/* 41 */     return Boolean.valueOf(true);
/*    */   }
/*    */   
/*    */   public List<InjectorShell.Builder> getInjectorShellBuilders() {
/* 45 */     return this.injectorShellBuilders;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\PrivateElementProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */