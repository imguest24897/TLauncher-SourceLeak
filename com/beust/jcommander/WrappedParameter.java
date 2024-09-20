/*     */ package com.beust.jcommander;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WrappedParameter
/*     */ {
/*     */   private Parameter parameter;
/*     */   private DynamicParameter dynamicParameter;
/*     */   
/*     */   public WrappedParameter(Parameter p) {
/*  15 */     this.parameter = p;
/*     */   }
/*     */   
/*     */   public WrappedParameter(DynamicParameter p) {
/*  19 */     this.dynamicParameter = p;
/*     */   }
/*     */   
/*     */   public Parameter getParameter() {
/*  23 */     return this.parameter;
/*     */   }
/*     */   
/*     */   public DynamicParameter getDynamicParameter() {
/*  27 */     return this.dynamicParameter;
/*     */   }
/*     */   
/*     */   public int arity() {
/*  31 */     return (this.parameter != null) ? this.parameter.arity() : 1;
/*     */   }
/*     */   
/*     */   public boolean hidden() {
/*  35 */     return (this.parameter != null) ? this.parameter.hidden() : this.dynamicParameter.hidden();
/*     */   }
/*     */   
/*     */   public boolean required() {
/*  39 */     return (this.parameter != null) ? this.parameter.required() : this.dynamicParameter.required();
/*     */   }
/*     */   
/*     */   public boolean password() {
/*  43 */     return (this.parameter != null) ? this.parameter.password() : false;
/*     */   }
/*     */   
/*     */   public String[] names() {
/*  47 */     return (this.parameter != null) ? this.parameter.names() : this.dynamicParameter.names();
/*     */   }
/*     */   
/*     */   public boolean variableArity() {
/*  51 */     return (this.parameter != null) ? this.parameter.variableArity() : false;
/*     */   }
/*     */   
/*     */   public int order() {
/*  55 */     return (this.parameter != null) ? this.parameter.order() : this.dynamicParameter.order();
/*     */   }
/*     */   
/*     */   public Class<? extends IParameterValidator>[] validateWith() {
/*  59 */     return (this.parameter != null) ? this.parameter.validateWith() : this.dynamicParameter.validateWith();
/*     */   }
/*     */   
/*     */   public Class<? extends IValueValidator>[] validateValueWith() {
/*  63 */     return (this.parameter != null) ? this.parameter
/*  64 */       .validateValueWith() : this.dynamicParameter
/*  65 */       .validateValueWith();
/*     */   }
/*     */   
/*     */   public boolean echoInput() {
/*  69 */     return (this.parameter != null) ? this.parameter.echoInput() : false;
/*     */   }
/*     */   
/*     */   public void addValue(Parameterized parameterized, Object object, Object value) {
/*     */     try {
/*  74 */       addValue(parameterized, object, value, null);
/*  75 */     } catch (IllegalAccessException e) {
/*  76 */       throw new ParameterException("Couldn't set " + object + " to " + value, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addValue(Parameterized parameterized, Object object, Object value, Field field) throws IllegalAccessException {
/*  82 */     if (this.parameter != null) {
/*  83 */       if (field != null) {
/*  84 */         field.set(object, value);
/*     */       } else {
/*  86 */         parameterized.set(object, value);
/*     */       } 
/*     */     } else {
/*  89 */       String a = this.dynamicParameter.assignment();
/*  90 */       String sv = value.toString();
/*     */       
/*  92 */       int aInd = sv.indexOf(a);
/*  93 */       if (aInd == -1) {
/*  94 */         throw new ParameterException("Dynamic parameter expected a value of the form a" + a + "b but got:" + sv);
/*     */       }
/*     */ 
/*     */       
/*  98 */       callPut(object, parameterized, sv.substring(0, aInd), sv.substring(aInd + 1));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void callPut(Object object, Parameterized parameterized, String key, String value) {
/*     */     try {
/* 105 */       Method m = findPut(parameterized.getType());
/* 106 */       m.invoke(parameterized.get(object), new Object[] { key, value });
/* 107 */     } catch (SecurityException|IllegalAccessException|NoSuchMethodException|java.lang.reflect.InvocationTargetException e) {
/* 108 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   private Method findPut(Class<?> cls) throws SecurityException, NoSuchMethodException {
/* 113 */     return cls.getMethod("put", new Class[] { Object.class, Object.class });
/*     */   }
/*     */   
/*     */   public String getAssignment() {
/* 117 */     return (this.dynamicParameter != null) ? this.dynamicParameter.assignment() : "";
/*     */   }
/*     */   
/*     */   public boolean isHelp() {
/* 121 */     return (this.parameter != null && this.parameter.help());
/*     */   }
/*     */   
/*     */   public boolean isNonOverwritableForced() {
/* 125 */     return (this.parameter != null && this.parameter.forceNonOverwritable());
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\WrappedParameter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */