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
/*    */ public class CreationException
/*    */   extends RuntimeException
/*    */ {
/*    */   private final ImmutableSet<Message> messages;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public CreationException(Collection<Message> messages) {
/* 38 */     this.messages = ImmutableSet.copyOf(messages);
/* 39 */     Preconditions.checkArgument(!this.messages.isEmpty());
/* 40 */     initCause(Messages.getOnlyCause((Collection)this.messages));
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<Message> getErrorMessages() {
/* 45 */     return (Collection<Message>)this.messages;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 50 */     return Messages.formatMessages("Unable to create injector, see the following errors", (Collection)this.messages);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\CreationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */