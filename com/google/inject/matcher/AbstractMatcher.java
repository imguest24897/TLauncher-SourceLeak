/*    */ package com.google.inject.matcher;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public abstract class AbstractMatcher<T>
/*    */   implements Matcher<T>
/*    */ {
/*    */   public Matcher<T> and(Matcher<? super T> other) {
/* 30 */     return new AndMatcher<>(this, other);
/*    */   }
/*    */ 
/*    */   
/*    */   public Matcher<T> or(Matcher<? super T> other) {
/* 35 */     return new OrMatcher<>(this, other);
/*    */   }
/*    */   
/*    */   private static class AndMatcher<T> extends AbstractMatcher<T> implements Serializable {
/*    */     private final Matcher<? super T> a;
/*    */     
/*    */     public AndMatcher(Matcher<? super T> a, Matcher<? super T> b) {
/* 42 */       this.a = a;
/* 43 */       this.b = b;
/*    */     }
/*    */     private final Matcher<? super T> b; private static final long serialVersionUID = 0L;
/*    */     
/*    */     public boolean matches(T t) {
/* 48 */       return (this.a.matches(t) && this.b.matches(t));
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean equals(Object other) {
/* 53 */       return (other instanceof AndMatcher && ((AndMatcher)other).a
/* 54 */         .equals(this.a) && ((AndMatcher)other).b
/* 55 */         .equals(this.b));
/*    */     }
/*    */ 
/*    */     
/*    */     public int hashCode() {
/* 60 */       return 41 * (this.a.hashCode() ^ this.b.hashCode());
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 65 */       String str1 = String.valueOf(this.a), str2 = String.valueOf(this.b); return (new StringBuilder(7 + String.valueOf(str1).length() + String.valueOf(str2).length())).append("and(").append(str1).append(", ").append(str2).append(")").toString();
/*    */     }
/*    */   }
/*    */   
/*    */   private static class OrMatcher<T> extends AbstractMatcher<T> implements Serializable {
/*    */     private final Matcher<? super T> a;
/*    */     private final Matcher<? super T> b;
/*    */     private static final long serialVersionUID = 0L;
/*    */     
/*    */     public OrMatcher(Matcher<? super T> a, Matcher<? super T> b) {
/* 75 */       this.a = a;
/* 76 */       this.b = b;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean matches(T t) {
/* 81 */       return (this.a.matches(t) || this.b.matches(t));
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean equals(Object other) {
/* 86 */       return (other instanceof OrMatcher && ((OrMatcher)other).a
/* 87 */         .equals(this.a) && ((OrMatcher)other).b
/* 88 */         .equals(this.b));
/*    */     }
/*    */ 
/*    */     
/*    */     public int hashCode() {
/* 93 */       return 37 * (this.a.hashCode() ^ this.b.hashCode());
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 98 */       String str1 = String.valueOf(this.a), str2 = String.valueOf(this.b); return (new StringBuilder(6 + String.valueOf(str1).length() + String.valueOf(str2).length())).append("or(").append(str1).append(", ").append(str2).append(")").toString();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\matcher\AbstractMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */