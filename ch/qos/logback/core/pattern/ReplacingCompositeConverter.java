/*    */ package ch.qos.logback.core.pattern;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.regex.Pattern;
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
/*    */ public class ReplacingCompositeConverter<E>
/*    */   extends CompositeConverter<E>
/*    */ {
/*    */   Pattern pattern;
/*    */   String regex;
/*    */   String replacement;
/*    */   
/*    */   public void start() {
/* 26 */     List<String> optionList = getOptionList();
/* 27 */     if (optionList == null) {
/* 28 */       addError("at least two options are expected whereas you have declared none");
/*    */       
/*    */       return;
/*    */     } 
/* 32 */     int numOpts = optionList.size();
/*    */     
/* 34 */     if (numOpts < 2) {
/* 35 */       addError("at least two options are expected whereas you have declared only " + numOpts + "as [" + optionList + "]");
/*    */       return;
/*    */     } 
/* 38 */     this.regex = optionList.get(0);
/* 39 */     this.pattern = Pattern.compile(this.regex);
/* 40 */     this.replacement = optionList.get(1);
/* 41 */     super.start();
/*    */   }
/*    */ 
/*    */   
/*    */   protected String transform(E event, String in) {
/* 46 */     if (!this.started)
/* 47 */       return in; 
/* 48 */     return this.pattern.matcher(in).replaceAll(this.replacement);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\ch\qos\logback\core\pattern\ReplacingCompositeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */