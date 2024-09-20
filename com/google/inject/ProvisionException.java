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
/*    */ 
/*    */ public final class ProvisionException
/*    */   extends RuntimeException
/*    */ {
/*    */   private final ImmutableSet<Message> messages;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public ProvisionException(Iterable<Message> messages) {
/* 39 */     this.messages = ImmutableSet.copyOf(messages);
/* 40 */     Preconditions.checkArgument(!this.messages.isEmpty());
/* 41 */     initCause(Messages.getOnlyCause((Collection)this.messages));
/*    */   }
/*    */   
/*    */   public ProvisionException(String message, Throwable cause) {
/* 45 */     super(cause);
/* 46 */     this.messages = ImmutableSet.of(new Message(message, cause));
/*    */   }
/*    */   
/*    */   public ProvisionException(String message) {
/* 50 */     this.messages = ImmutableSet.of(new Message(message));
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<Message> getErrorMessages() {
/* 55 */     return (Collection<Message>)this.messages;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 60 */     return Messages.formatMessages("Unable to provision, see the following errors", (Collection)this.messages);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\ProvisionException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */