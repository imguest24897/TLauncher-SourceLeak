/*    */ package com.google.inject;
/*    */ 
/*    */ import com.google.inject.internal.InternalInjectorCreator;
/*    */ import java.util.Arrays;
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
/*    */ public final class Guice
/*    */ {
/*    */   public static Injector createInjector(Module... modules) {
/* 59 */     return createInjector(Arrays.asList(modules));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Injector createInjector(Iterable<? extends Module> modules) {
/* 69 */     return createInjector(Stage.DEVELOPMENT, modules);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Injector createInjector(Stage stage, Module... modules) {
/* 78 */     return createInjector(stage, Arrays.asList(modules));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Injector createInjector(Stage stage, Iterable<? extends Module> modules) {
/* 87 */     return (new InternalInjectorCreator()).stage(stage).addModules(modules).build();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\Guice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */