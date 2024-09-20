/*    */ package com.google.inject;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.inject.internal.Messages;
/*    */ import com.google.inject.spi.Message;
/*    */ import java.util.Collection;
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
/*    */ public final class ConfigurationException
/*    */   extends RuntimeException
/*    */ {
/*    */   private final ImmutableSet<Message> messages;
/* 35 */   private Object partialValue = null;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public ConfigurationException(Iterable<Message> messages) {
/* 39 */     this.messages = ImmutableSet.copyOf(messages);
/* 40 */     initCause(Messages.getOnlyCause((Collection)this.messages));
/*    */   }
/*    */ 
/*    */   
/*    */   public ConfigurationException withPartialValue(Object partialValue) {
/* 45 */     Preconditions.checkState((this.partialValue == null), "Can't clobber existing partial value %s with %s", this.partialValue, partialValue);
/*    */     
/* 47 */     ConfigurationException result = new ConfigurationException((Iterable<Message>)this.messages);
/* 48 */     result.partialValue = partialValue;
/* 49 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<Message> getErrorMessages() {
/* 54 */     return (Collection<Message>)this.messages;
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
/*    */   public <E> E getPartialValue() {
/* 69 */     return (E)this.partialValue;
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 73 */     return Messages.formatMessages("Guice configuration errors", (Collection)this.messages);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\ConfigurationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */