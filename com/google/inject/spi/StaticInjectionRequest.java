/*    */ package com.google.inject.spi;
/*    */ 
/*    */ import com.google.common.base.Objects;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.inject.Binder;
/*    */ import com.google.inject.ConfigurationException;
/*    */ import java.util.Set;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class StaticInjectionRequest
/*    */   implements Element
/*    */ {
/*    */   private final Object source;
/*    */   private final Class<?> type;
/*    */   
/*    */   StaticInjectionRequest(Object source, Class<?> type) {
/* 42 */     this.source = Preconditions.checkNotNull(source, "source");
/* 43 */     this.type = (Class)Preconditions.checkNotNull(type, "type");
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getSource() {
/* 48 */     return this.source;
/*    */   }
/*    */   
/*    */   public Class<?> getType() {
/* 52 */     return this.type;
/*    */   }
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
/*    */   public Set<InjectionPoint> getInjectionPoints() throws ConfigurationException {
/* 68 */     return InjectionPoint.forStaticMethodsAndFields(this.type);
/*    */   }
/*    */ 
/*    */   
/*    */   public void applyTo(Binder binder) {
/* 73 */     binder.withSource(getSource()).requestStaticInjection(new Class[] { this.type });
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T acceptVisitor(ElementVisitor<T> visitor) {
/* 78 */     return visitor.visit(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 83 */     return (obj instanceof StaticInjectionRequest && ((StaticInjectionRequest)obj).source
/* 84 */       .equals(this.source) && ((StaticInjectionRequest)obj).type
/* 85 */       .equals(this.type));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 90 */     return Objects.hashCode(new Object[] { this.source, this.type });
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\StaticInjectionRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */