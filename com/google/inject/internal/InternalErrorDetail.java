/*    */ package com.google.inject.internal;
/*    */ 
/*    */ import com.google.common.base.CaseFormat;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.inject.spi.ErrorDetail;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class InternalErrorDetail<T extends ErrorDetail<T>>
/*    */   extends ErrorDetail<T>
/*    */ {
/* 16 */   private static final ImmutableSet<ErrorId> DOCUMENTED_ERRORS = ImmutableSet.builder()
/* 17 */     .add(ErrorId.BINDING_ALREADY_SET)
/* 18 */     .add(ErrorId.CAN_NOT_PROXY_CLASS)
/* 19 */     .add(ErrorId.CIRCULAR_PROXY_DISABLED)
/* 20 */     .add(ErrorId.DUPLICATE_BINDING_ANNOTATIONS)
/* 21 */     .add(ErrorId.DUPLICATE_ELEMENT)
/* 22 */     .add(ErrorId.DUPLICATE_SCOPES)
/* 23 */     .add(ErrorId.ERROR_INJECTING_CONSTRUCTOR)
/* 24 */     .add(ErrorId.ERROR_INJECTING_METHOD)
/* 25 */     .add(ErrorId.ERROR_IN_CUSTOM_PROVIDER)
/* 26 */     .add(ErrorId.INJECT_INNER_CLASS)
/* 27 */     .add(ErrorId.MISSING_CONSTRUCTOR)
/* 28 */     .add(ErrorId.MISSING_IMPLEMENTATION)
/* 29 */     .add(ErrorId.NULL_INJECTED_INTO_NON_NULLABLE)
/* 30 */     .add(ErrorId.NULL_VALUE_IN_MAP)
/* 31 */     .add(ErrorId.SCOPE_NOT_FOUND)
/* 32 */     .add(ErrorId.TOO_MANY_CONSTRUCTORS)
/* 33 */     .build();
/*    */   
/*    */   private static final String DOC_BASE_URL = "https://github.com/google/guice/wiki/";
/*    */   
/*    */   protected final ErrorId errorId;
/*    */ 
/*    */   
/*    */   protected InternalErrorDetail(ErrorId errorId, String message, List<Object> sources, Throwable cause) {
/* 41 */     super(message, sources, cause);
/* 42 */     this.errorId = errorId;
/*    */   }
/*    */ 
/*    */   
/*    */   protected final Optional<String> getLearnMoreLink() {
/* 47 */     if (DOCUMENTED_ERRORS.contains(this.errorId)) {
/* 48 */       String.valueOf(this.errorId.name()); return Optional.of((String.valueOf(this.errorId.name()).length() != 0) ? String.valueOf("https://github.com/google/guice/wiki/").concat(String.valueOf(this.errorId.name())) : new String(String.valueOf("https://github.com/google/guice/wiki/")));
/*    */     } 
/* 50 */     return Optional.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   protected final Optional<String> getErrorIdentifier() {
/* 55 */     if (this.errorId == ErrorId.OTHER) {
/* 56 */       return Optional.empty();
/*    */     }
/* 58 */     String.valueOf(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, this.errorId.name())); String id = (String.valueOf(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, this.errorId.name())).length() != 0) ? "Guice/".concat(String.valueOf(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, this.errorId.name()))) : new String("Guice/");
/* 59 */     return Optional.of(id);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\InternalErrorDetail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */