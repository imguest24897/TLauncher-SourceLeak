/*     */ package org.apache.commons.lang3.compare;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import org.apache.commons.lang3.ObjectUtils;
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
/*     */ public class ComparableUtils
/*     */ {
/*     */   public static class ComparableCheckBuilder<A extends Comparable<A>>
/*     */   {
/*     */     private final A a;
/*     */     
/*     */     private ComparableCheckBuilder(A a) {
/*  44 */       this.a = a;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean between(A b, A c) {
/*  55 */       return (betweenOrdered(b, c) || betweenOrdered(c, b));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean betweenExclusive(A b, A c) {
/*  66 */       return (betweenOrderedExclusive(b, c) || betweenOrderedExclusive(c, b));
/*     */     }
/*     */     
/*     */     private boolean betweenOrdered(A b, A c) {
/*  70 */       return (greaterThanOrEqualTo(b) && lessThanOrEqualTo(c));
/*     */     }
/*     */     
/*     */     private boolean betweenOrderedExclusive(A b, A c) {
/*  74 */       return (greaterThan(b) && lessThan(c));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equalTo(A b) {
/*  84 */       return (this.a.compareTo(b) == 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean greaterThan(A b) {
/*  94 */       return (this.a.compareTo(b) > 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean greaterThanOrEqualTo(A b) {
/* 104 */       return (this.a.compareTo(b) >= 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean lessThan(A b) {
/* 114 */       return (this.a.compareTo(b) < 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean lessThanOrEqualTo(A b) {
/* 124 */       return (this.a.compareTo(b) <= 0);
/*     */     }
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
/*     */   public static <A extends Comparable<A>> Predicate<A> between(A b, A c) {
/* 137 */     return a -> is(a).between(b, c);
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
/*     */   public static <A extends Comparable<A>> Predicate<A> betweenExclusive(A b, A c) {
/* 149 */     return a -> is(a).betweenExclusive(b, c);
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
/*     */   public static <A extends Comparable<A>> Predicate<A> ge(A b) {
/* 161 */     return a -> is(a).greaterThanOrEqualTo(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A extends Comparable<A>> Predicate<A> gt(A b) {
/* 172 */     return a -> is(a).greaterThan(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A extends Comparable<A>> ComparableCheckBuilder<A> is(A a) {
/* 183 */     return new ComparableCheckBuilder<>((Comparable)a);
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
/*     */   public static <A extends Comparable<A>> Predicate<A> le(A b) {
/* 195 */     return a -> is(a).lessThanOrEqualTo(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A extends Comparable<A>> Predicate<A> lt(A b) {
/* 206 */     return a -> is(a).lessThan(b);
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
/*     */   public static <A extends Comparable<A>> A max(A comparable1, A comparable2) {
/* 223 */     return (ObjectUtils.compare((Comparable)comparable1, (Comparable)comparable2, false) > 0) ? comparable1 : comparable2;
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
/*     */   public static <A extends Comparable<A>> A min(A comparable1, A comparable2) {
/* 240 */     return (ObjectUtils.compare((Comparable)comparable1, (Comparable)comparable2, true) < 0) ? comparable1 : comparable2;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\compare\ComparableUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */