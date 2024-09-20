/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.common.base.Objects;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.inject.TypeLiteral;
/*    */ import com.google.inject.spi.ErrorDetail;
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Modifier;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Formatter;
/*    */ import java.util.List;
/*    */ 
/*    */ final class MissingConstructorError
/*    */   extends InternalErrorDetail<MissingConstructorError> {
/*    */   private final TypeLiteral<?> type;
/*    */   private final boolean atInjectRequired;
/*    */   
/*    */   MissingConstructorError(TypeLiteral<?> type, boolean atInjectRequired, List<Object> sources) {
/* 19 */     super(ErrorId.MISSING_CONSTRUCTOR, (new StringBuilder(36 + String.valueOf(str).length())).append("No injectable constructor for type ").append(str).append(".").toString(), sources, (Throwable)null);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 24 */     this.type = type;
/* 25 */     this.atInjectRequired = atInjectRequired;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isMergeable(ErrorDetail<?> other) {
/* 30 */     if (other instanceof MissingConstructorError) {
/* 31 */       MissingConstructorError otherMissing = (MissingConstructorError)other;
/* 32 */       return (Objects.equal(this.type, otherMissing.type) && 
/* 33 */         Objects.equal(Boolean.valueOf(this.atInjectRequired), Boolean.valueOf(otherMissing.atInjectRequired)));
/*    */     } 
/* 35 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void formatDetail(List<ErrorDetail<?>> mergeableErrors, Formatter formatter) {
/* 40 */     formatter.format("%n", new Object[0]);
/* 41 */     Class<?> rawType = this.type.getRawType();
/* 42 */     if (this.atInjectRequired) {
/* 43 */       formatter.format("Injector is configured to require @Inject constructors but %s does not have a @Inject annotated constructor.%n", new Object[] { rawType });
/*    */     
/*    */     }
/*    */     else {
/*    */       
/* 48 */       Constructor<?> noArgConstructor = null;
/*    */       try {
/* 50 */         noArgConstructor = this.type.getRawType().getDeclaredConstructor(new Class[0]);
/* 51 */       } catch (NoSuchMethodException noSuchMethodException) {}
/*    */ 
/*    */       
/* 54 */       if (noArgConstructor == null) {
/* 55 */         formatter.format("%s does not have a @Inject annotated constructor or a no-arg constructor.%n", new Object[] { rawType });
/*    */       }
/* 57 */       else if (Modifier.isPrivate(noArgConstructor.getModifiers()) && 
/* 58 */         !Modifier.isPrivate(rawType.getModifiers())) {
/* 59 */         formatter.format("%s has a private no-arg constructor but the class is not private. Guice can only use a private no-arg constructor if it is defined in a private class.%n", new Object[] { rawType });
/*    */       } 
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 65 */     formatter.format("%n", new Object[0]);
/*    */     
/* 67 */     List<List<Object>> sourcesList = new ArrayList<>();
/* 68 */     sourcesList.add(getSources());
/* 69 */     mergeableErrors.forEach(error -> sourcesList.add(error.getSources()));
/*    */     
/* 71 */     formatter.format("%s%n", new Object[] { Messages.bold("Requested by:") });
/* 72 */     int sourceListIndex = 1;
/* 73 */     for (List<Object> sources : sourcesList) {
/* 74 */       ErrorFormatter.formatSources(sourceListIndex++, Lists.reverse(sources), formatter);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public MissingConstructorError withSources(List<Object> newSources) {
/* 80 */     return new MissingConstructorError(this.type, this.atInjectRequired, newSources);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\MissingConstructorError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */