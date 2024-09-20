/*    */ package org.apache.commons.text.lookup;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class FunctionStringLookup<V>
/*    */   extends AbstractStringLookup
/*    */ {
/*    */   private final Function<String, V> function;
/*    */   
/*    */   static <R> FunctionStringLookup<R> on(Function<String, R> function) {
/* 40 */     return new FunctionStringLookup<>(function);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static <V> FunctionStringLookup<V> on(Map<String, V> map) {
/* 51 */     Objects.requireNonNull(StringLookupFactory.toMap(map)); return on(StringLookupFactory.toMap(map)::get);
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
/*    */   private FunctionStringLookup(Function<String, V> function) {
/* 65 */     this.function = function;
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
/*    */   public String lookup(String key) {
/*    */     V obj;
/* 80 */     if (this.function == null) {
/* 81 */       return null;
/*    */     }
/*    */     
/*    */     try {
/* 85 */       obj = this.function.apply(key);
/* 86 */     } catch (SecurityException|NullPointerException|IllegalArgumentException e) {
/*    */ 
/*    */       
/* 89 */       return null;
/*    */     } 
/* 91 */     return Objects.toString(obj, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 96 */     return super.toString() + " [function=" + this.function + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\lookup\FunctionStringLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */