/*     */ package org.apache.commons.lang3.builder;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
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
/*     */ public class DiffResult<T>
/*     */   implements Iterable<Diff<?>>
/*     */ {
/*     */   public static final String OBJECTS_SAME_STRING = "";
/*     */   private static final String DIFFERS_STRING = "differs from";
/*     */   private final List<Diff<?>> diffList;
/*     */   private final T lhs;
/*     */   private final T rhs;
/*     */   private final ToStringStyle style;
/*     */   
/*     */   DiffResult(T lhs, T rhs, List<Diff<?>> diffList, ToStringStyle style) {
/*  71 */     Objects.requireNonNull(lhs, "lhs");
/*  72 */     Objects.requireNonNull(rhs, "rhs");
/*  73 */     Objects.requireNonNull(diffList, "diffList");
/*     */     
/*  75 */     this.diffList = diffList;
/*  76 */     this.lhs = lhs;
/*  77 */     this.rhs = rhs;
/*     */     
/*  79 */     if (style == null) {
/*  80 */       this.style = ToStringStyle.DEFAULT_STYLE;
/*     */     } else {
/*  82 */       this.style = style;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T getLeft() {
/*  93 */     return this.lhs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T getRight() {
/* 103 */     return this.rhs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Diff<?>> getDiffs() {
/* 113 */     return Collections.unmodifiableList(this.diffList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumberOfDiffs() {
/* 122 */     return this.diffList.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringStyle getToStringStyle() {
/* 131 */     return this.style;
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
/*     */   public String toString() {
/* 165 */     return toString(this.style);
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
/*     */   public String toString(ToStringStyle style) {
/* 178 */     if (this.diffList.isEmpty()) {
/* 179 */       return "";
/*     */     }
/*     */     
/* 182 */     ToStringBuilder lhsBuilder = new ToStringBuilder(this.lhs, style);
/* 183 */     ToStringBuilder rhsBuilder = new ToStringBuilder(this.rhs, style);
/*     */     
/* 185 */     this.diffList.forEach(diff -> {
/*     */           lhsBuilder.append(diff.getFieldName(), diff.getLeft());
/*     */           
/*     */           rhsBuilder.append(diff.getFieldName(), diff.getRight());
/*     */         });
/* 190 */     return String.format("%s %s %s", new Object[] { lhsBuilder.build(), "differs from", rhsBuilder.build() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<Diff<?>> iterator() {
/* 200 */     return this.diffList.iterator();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\builder\DiffResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */