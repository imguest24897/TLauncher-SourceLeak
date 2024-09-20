/*    */ package com.beust.jcommander;
/*    */ 
/*    */ 
/*    */ public class StringKey
/*    */   implements FuzzyMap.IKey
/*    */ {
/*    */   private String name;
/*    */   
/*    */   public StringKey(String name) {
/* 10 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 15 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 20 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 25 */     int prime = 31;
/* 26 */     int result = 1;
/* 27 */     result = 31 * result + ((this.name == null) ? 0 : this.name.hashCode());
/* 28 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 33 */     if (this == obj)
/* 34 */       return true; 
/* 35 */     if (obj == null)
/* 36 */       return false; 
/* 37 */     if (getClass() != obj.getClass())
/* 38 */       return false; 
/* 39 */     StringKey other = (StringKey)obj;
/* 40 */     if (this.name == null) {
/* 41 */       if (other.name != null)
/* 42 */         return false; 
/* 43 */     } else if (!this.name.equals(other.name)) {
/* 44 */       return false;
/* 45 */     }  return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\StringKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */