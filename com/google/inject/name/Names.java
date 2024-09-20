/*    */ package com.google.inject.name;
/*    */ 
/*    */ import com.google.inject.Binder;
/*    */ import com.google.inject.Key;
/*    */ import java.util.Enumeration;
/*    */ import java.util.Map;
/*    */ import java.util.Properties;
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
/*    */ public class Names
/*    */ {
/*    */   public static Named named(String name) {
/* 36 */     return new NamedImpl(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void bindProperties(Binder binder, Map<String, String> properties) {
/* 41 */     binder = binder.skipSources(new Class[] { Names.class });
/* 42 */     for (Map.Entry<String, String> entry : properties.entrySet()) {
/* 43 */       String key = entry.getKey();
/* 44 */       String value = entry.getValue();
/* 45 */       binder.bind(Key.get(String.class, new NamedImpl(key))).toInstance(value);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void bindProperties(Binder binder, Properties properties) {
/* 54 */     binder = binder.skipSources(new Class[] { Names.class });
/*    */ 
/*    */     
/* 57 */     for (Enumeration<?> e = properties.propertyNames(); e.hasMoreElements(); ) {
/* 58 */       String propertyName = (String)e.nextElement();
/* 59 */       String value = properties.getProperty(propertyName);
/* 60 */       binder.bind(Key.get(String.class, new NamedImpl(propertyName))).toInstance(value);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\name\Names.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */