/*    */ package org.apache.commons.lang3.builder;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import java.util.Objects;
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
/*    */ 
/*    */ class Reflection
/*    */ {
/*    */   static Object getUnchecked(Field field, Object obj) {
/*    */     try {
/* 38 */       return ((Field)Objects.<Field>requireNonNull(field, "field")).get(obj);
/* 39 */     } catch (IllegalAccessException e) {
/* 40 */       throw new IllegalArgumentException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\builder\Reflection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */