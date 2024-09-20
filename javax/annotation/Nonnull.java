/*    */ package javax.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.annotation.Documented;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import javax.annotation.meta.TypeQualifier;
/*    */ import javax.annotation.meta.TypeQualifierValidator;
/*    */ import javax.annotation.meta.When;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Documented
/*    */ @TypeQualifier
/*    */ @Retention(RetentionPolicy.RUNTIME)
/*    */ public @interface Nonnull
/*    */ {
/*    */   When when() default When.ALWAYS;
/*    */   
/*    */   public static class Checker
/*    */     implements TypeQualifierValidator<Nonnull>
/*    */   {
/*    */     public When forConstantValue(Nonnull qualifierArgument, Object value) {
/* 27 */       if (value == null)
/* 28 */         return When.NEVER; 
/* 29 */       return When.ALWAYS;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\javax\annotation\Nonnull.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */