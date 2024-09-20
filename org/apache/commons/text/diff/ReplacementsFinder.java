/*     */ package org.apache.commons.text.diff;
/*     */ 
/*     */ import java.util.ArrayList;
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
/*     */ public class ReplacementsFinder<T>
/*     */   implements CommandVisitor<T>
/*     */ {
/*     */   private final List<T> pendingInsertions;
/*     */   private final List<T> pendingDeletions;
/*     */   private int skipped;
/*     */   private final ReplacementsHandler<T> handler;
/*     */   
/*     */   public ReplacementsFinder(ReplacementsHandler<T> handler) {
/*  79 */     this.pendingInsertions = new ArrayList<>();
/*  80 */     this.pendingDeletions = new ArrayList<>();
/*  81 */     this.skipped = 0;
/*  82 */     this.handler = handler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitDeleteCommand(T object) {
/*  92 */     this.pendingDeletions.add(object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitInsertCommand(T object) {
/* 102 */     this.pendingInsertions.add(object);
/*     */   }
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
/*     */   public void visitKeepCommand(T object) {
/* 116 */     if (this.pendingDeletions.isEmpty() && this.pendingInsertions.isEmpty()) {
/* 117 */       this.skipped++;
/*     */     } else {
/* 119 */       this.handler.handleReplacement(this.skipped, this.pendingDeletions, this.pendingInsertions);
/* 120 */       this.pendingDeletions.clear();
/* 121 */       this.pendingInsertions.clear();
/* 122 */       this.skipped = 1;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\diff\ReplacementsFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */