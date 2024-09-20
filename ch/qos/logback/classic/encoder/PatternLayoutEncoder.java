/*    */ package ch.qos.logback.classic.encoder;
/*    */ 
/*    */ import ch.qos.logback.classic.PatternLayout;
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.core.Layout;
/*    */ import ch.qos.logback.core.pattern.PatternLayoutEncoderBase;
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
/*    */ public class PatternLayoutEncoder
/*    */   extends PatternLayoutEncoderBase<ILoggingEvent>
/*    */ {
/*    */   public void start() {
/* 24 */     PatternLayout patternLayout = new PatternLayout();
/* 25 */     patternLayout.setContext(this.context);
/* 26 */     patternLayout.setPattern(getPattern());
/* 27 */     patternLayout.setOutputPatternAsHeader(this.outputPatternAsHeader);
/* 28 */     patternLayout.start();
/* 29 */     this.layout = (Layout)patternLayout;
/* 30 */     super.start();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\ch\qos\logback\classic\encoder\PatternLayoutEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */