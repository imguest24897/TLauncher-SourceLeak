/*    */ package com.google.thirdparty.publicsuffix;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ @Beta
/*    */ @GwtCompatible
/*    */ public enum PublicSuffixType
/*    */ {
/* 33 */   PRIVATE(':', ','),
/*    */   
/* 35 */   REGISTRY('!', '?');
/*    */ 
/*    */   
/*    */   private final char innerNodeCode;
/*    */   
/*    */   private final char leafNodeCode;
/*    */ 
/*    */   
/*    */   PublicSuffixType(char innerNodeCode, char leafNodeCode) {
/* 44 */     this.innerNodeCode = innerNodeCode;
/* 45 */     this.leafNodeCode = leafNodeCode;
/*    */   }
/*    */   
/*    */   char getLeafNodeCode() {
/* 49 */     return this.leafNodeCode;
/*    */   }
/*    */   
/*    */   char getInnerNodeCode() {
/* 53 */     return this.innerNodeCode;
/*    */   }
/*    */ 
/*    */   
/*    */   static PublicSuffixType fromCode(char code) {
/* 58 */     for (PublicSuffixType value : values()) {
/* 59 */       if (value.getInnerNodeCode() == code || value.getLeafNodeCode() == code) {
/* 60 */         return value;
/*    */       }
/*    */     } 
/* 63 */     throw new IllegalArgumentException((new StringBuilder(38)).append("No enum corresponding to given code: ").append(code).toString());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\thirdparty\publicsuffix\PublicSuffixType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */