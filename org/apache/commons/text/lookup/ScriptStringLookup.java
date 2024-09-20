/*    */ package org.apache.commons.text.lookup;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import javax.script.ScriptEngine;
/*    */ import javax.script.ScriptEngineManager;
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
/*    */ final class ScriptStringLookup
/*    */   extends AbstractStringLookup
/*    */ {
/* 50 */   static final ScriptStringLookup INSTANCE = new ScriptStringLookup();
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
/*    */   public String lookup(String key) {
/* 70 */     if (key == null) {
/* 71 */       return null;
/*    */     }
/* 73 */     String[] keys = key.split(SPLIT_STR, 2);
/* 74 */     int keyLen = keys.length;
/* 75 */     if (keyLen != 2) {
/* 76 */       throw IllegalArgumentExceptions.format("Bad script key format [%s]; expected format is EngineName:Script.", new Object[] { key });
/*    */     }
/*    */     
/* 79 */     String engineName = keys[0];
/* 80 */     String script = keys[1];
/*    */     try {
/* 82 */       ScriptEngine scriptEngine = (new ScriptEngineManager()).getEngineByName(engineName);
/* 83 */       if (scriptEngine == null) {
/* 84 */         throw new IllegalArgumentException("No script engine named " + engineName);
/*    */       }
/* 86 */       return Objects.toString(scriptEngine.eval(script), null);
/* 87 */     } catch (Exception e) {
/* 88 */       throw IllegalArgumentExceptions.format(e, "Error in script engine [%s] evaluating script [%s].", new Object[] { engineName, script });
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\lookup\ScriptStringLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */