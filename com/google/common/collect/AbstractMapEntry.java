/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Objects;
/*    */ import java.util.Map;
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
/*    */ @GwtCompatible
/*    */ abstract class AbstractMapEntry<K, V>
/*    */   implements Map.Entry<K, V>
/*    */ {
/*    */   public abstract K getKey();
/*    */   
/*    */   public abstract V getValue();
/*    */   
/*    */   public V setValue(V value) {
/* 41 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object object) {
/* 46 */     if (object instanceof Map.Entry) {
/* 47 */       Map.Entry<?, ?> that = (Map.Entry<?, ?>)object;
/* 48 */       return (Objects.equal(getKey(), that.getKey()) && 
/* 49 */         Objects.equal(getValue(), that.getValue()));
/*    */     } 
/* 51 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 56 */     K k = getKey();
/* 57 */     V v = getValue();
/* 58 */     return ((k == null) ? 0 : k.hashCode()) ^ ((v == null) ? 0 : v.hashCode());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 64 */     String str1 = String.valueOf(getKey()), str2 = String.valueOf(getValue()); return (new StringBuilder(1 + String.valueOf(str1).length() + String.valueOf(str2).length())).append(str1).append("=").append(str2).toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\collect\AbstractMapEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */