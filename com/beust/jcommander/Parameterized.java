/*     */ package com.beust.jcommander;
/*     */ 
/*     */ import com.beust.jcommander.internal.Lists;
/*     */ import com.beust.jcommander.internal.Sets;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Parameterized
/*     */ {
/*     */   private Field field;
/*     */   private Method method;
/*     */   private Method getter;
/*     */   private WrappedParameter wrappedParameter;
/*     */   private ParametersDelegate parametersDelegate;
/*     */   
/*     */   public Parameterized(WrappedParameter wp, ParametersDelegate pd, Field field, Method method) {
/*  28 */     this.wrappedParameter = wp;
/*  29 */     this.method = method;
/*  30 */     this.field = field;
/*  31 */     if (this.field != null) {
/*  32 */       if (pd == null) {
/*  33 */         setFieldAccessible(this.field);
/*     */       } else {
/*  35 */         setFieldAccessibleWithoutFinalCheck(this.field);
/*     */       } 
/*     */     }
/*  38 */     this.parametersDelegate = pd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void describeClassTree(Class<?> inputClass, Set<Class<?>> setOfClasses) {
/*  50 */     if (inputClass == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  55 */     if (Object.class.equals(inputClass) || setOfClasses.contains(inputClass)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  60 */     setOfClasses.add(inputClass);
/*     */ 
/*     */     
/*  63 */     describeClassTree(inputClass.getSuperclass(), setOfClasses);
/*     */ 
/*     */     
/*  66 */     for (Class<?> hasInterface : inputClass.getInterfaces()) {
/*  67 */       describeClassTree(hasInterface, setOfClasses);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Set<Class<?>> describeClassTree(Class<?> inputClass) {
/*  79 */     if (inputClass == null) {
/*  80 */       return Collections.emptySet();
/*     */     }
/*     */ 
/*     */     
/*  84 */     Set<Class<?>> classes = Sets.newLinkedHashSet();
/*     */ 
/*     */     
/*  87 */     describeClassTree(inputClass, classes);
/*     */     
/*  89 */     return classes;
/*     */   }
/*     */   
/*     */   public static List<Parameterized> parseArg(Object arg) {
/*  93 */     List<Parameterized> result = Lists.newArrayList();
/*     */     
/*  95 */     Class<?> rootClass = arg.getClass();
/*     */ 
/*     */ 
/*     */     
/*  99 */     Set<Class<?>> types = describeClassTree(rootClass);
/*     */ 
/*     */     
/* 102 */     for (Class<?> cls : types) {
/*     */ 
/*     */       
/* 105 */       for (Field f : cls.getDeclaredFields()) {
/* 106 */         Annotation annotation = f.getAnnotation((Class)Parameter.class);
/* 107 */         Annotation delegateAnnotation = f.getAnnotation((Class)ParametersDelegate.class);
/* 108 */         Annotation dynamicParameter = f.getAnnotation((Class)DynamicParameter.class);
/* 109 */         if (annotation != null) {
/* 110 */           result.add(new Parameterized(new WrappedParameter((Parameter)annotation), null, f, null));
/*     */         }
/* 112 */         else if (dynamicParameter != null) {
/* 113 */           result.add(new Parameterized(new WrappedParameter((DynamicParameter)dynamicParameter), null, f, null));
/*     */         }
/* 115 */         else if (delegateAnnotation != null) {
/* 116 */           result.add(new Parameterized(null, (ParametersDelegate)delegateAnnotation, f, null));
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 122 */       for (Method m : cls.getDeclaredMethods()) {
/* 123 */         m.setAccessible(true);
/* 124 */         Annotation annotation = m.getAnnotation((Class)Parameter.class);
/* 125 */         Annotation delegateAnnotation = m.getAnnotation((Class)ParametersDelegate.class);
/* 126 */         Annotation dynamicParameter = m.getAnnotation((Class)DynamicParameter.class);
/* 127 */         if (annotation != null) {
/* 128 */           result.add(new Parameterized(new WrappedParameter((Parameter)annotation), null, null, m));
/*     */         }
/* 130 */         else if (dynamicParameter != null) {
/* 131 */           result.add(new Parameterized(new WrappedParameter((DynamicParameter)dynamicParameter), null, null, m));
/*     */         }
/* 133 */         else if (delegateAnnotation != null) {
/* 134 */           result.add(new Parameterized(null, (ParametersDelegate)delegateAnnotation, null, m));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 140 */     return result;
/*     */   }
/*     */   
/*     */   public WrappedParameter getWrappedParameter() {
/* 144 */     return this.wrappedParameter;
/*     */   }
/*     */   
/*     */   public Class<?> getType() {
/* 148 */     if (this.method != null) {
/* 149 */       return this.method.getParameterTypes()[0];
/*     */     }
/* 151 */     return this.field.getType();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 156 */     if (this.method != null) {
/* 157 */       return this.method.getName();
/*     */     }
/* 159 */     return this.field.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object get(Object object) {
/*     */     try {
/* 165 */       if (this.method != null) {
/* 166 */         if (this.getter == null) {
/* 167 */           setGetter(object);
/*     */         }
/* 169 */         return this.getter.invoke(object, new Object[0]);
/*     */       } 
/* 171 */       return this.field.get(object);
/*     */     }
/* 173 */     catch (SecurityException|IllegalArgumentException|InvocationTargetException|IllegalAccessException e) {
/* 174 */       throw new ParameterException(e);
/* 175 */     } catch (NoSuchMethodException e) {
/*     */       
/* 177 */       String name = this.method.getName();
/* 178 */       String fieldName = Character.toLowerCase(name.charAt(3)) + name.substring(4);
/* 179 */       Object result = null;
/*     */       try {
/* 181 */         Field field = this.method.getDeclaringClass().getDeclaredField(fieldName);
/* 182 */         if (field != null) {
/* 183 */           setFieldAccessible(field);
/* 184 */           result = field.get(object);
/*     */         } 
/* 186 */       } catch (NoSuchFieldException|IllegalAccessException noSuchFieldException) {}
/*     */ 
/*     */       
/* 189 */       return result;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setGetter(Object object) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
/* 194 */     if (Boolean.class.getSimpleName().toLowerCase().equals(getType().getName())) {
/*     */       
/*     */       try {
/* 197 */         this
/* 198 */           .getter = object.getClass().getMethod("is" + this.method.getName().substring(3), new Class[0]);
/*     */         
/*     */         return;
/* 201 */       } catch (NoSuchMethodException noSuchMethodException) {}
/*     */     }
/*     */ 
/*     */     
/* 205 */     this
/* 206 */       .getter = object.getClass().getMethod("g" + this.method.getName().substring(1), new Class[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 211 */     int prime = 31;
/* 212 */     int result = 1;
/* 213 */     result = 31 * result + ((this.field == null) ? 0 : this.field.hashCode());
/* 214 */     result = 31 * result + ((this.method == null) ? 0 : this.method.hashCode());
/* 215 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 220 */     if (this == obj)
/* 221 */       return true; 
/* 222 */     if (obj == null)
/* 223 */       return false; 
/* 224 */     if (getClass() != obj.getClass())
/* 225 */       return false; 
/* 226 */     Parameterized other = (Parameterized)obj;
/* 227 */     if (this.field == null) {
/* 228 */       if (other.field != null)
/* 229 */         return false; 
/* 230 */     } else if (!this.field.equals(other.field)) {
/* 231 */       return false;
/* 232 */     }  if (this.method == null) {
/* 233 */       if (other.method != null)
/* 234 */         return false; 
/* 235 */     } else if (!this.method.equals(other.method)) {
/* 236 */       return false;
/* 237 */     }  return true;
/*     */   }
/*     */   
/*     */   public boolean isDynamicParameter(Field field) {
/* 241 */     if (this.method != null) {
/* 242 */       return (this.method.getAnnotation(DynamicParameter.class) != null);
/*     */     }
/* 244 */     return (this.field.getAnnotation(DynamicParameter.class) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void setFieldAccessible(Field f) {
/* 249 */     if (Modifier.isFinal(f.getModifiers())) {
/* 250 */       throw new ParameterException("Cannot use final field " + f
/* 251 */           .getDeclaringClass().getName() + "#" + f.getName() + " as a parameter; compile-time constant inlining may hide new values written to it.");
/*     */     }
/*     */     
/* 254 */     f.setAccessible(true);
/*     */   }
/*     */   
/*     */   private static void setFieldAccessibleWithoutFinalCheck(Field f) {
/* 258 */     f.setAccessible(true);
/*     */   }
/*     */   
/*     */   private static String errorMessage(Method m, Exception ex) {
/* 262 */     return "Could not invoke " + m + "\n    Reason: " + ex.getMessage();
/*     */   }
/*     */   
/*     */   public void set(Object object, Object value) {
/*     */     try {
/* 267 */       if (this.method != null) {
/* 268 */         this.method.invoke(object, new Object[] { value });
/*     */       } else {
/* 270 */         this.field.set(object, value);
/*     */       } 
/* 272 */     } catch (IllegalAccessException|IllegalArgumentException ex) {
/* 273 */       throw new ParameterException(errorMessage(this.method, ex));
/* 274 */     } catch (InvocationTargetException ex) {
/*     */       
/* 276 */       if (ex.getTargetException() instanceof ParameterException) {
/* 277 */         throw (ParameterException)ex.getTargetException();
/*     */       }
/* 279 */       throw new ParameterException(errorMessage(this.method, ex));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ParametersDelegate getDelegateAnnotation() {
/* 285 */     return this.parametersDelegate;
/*     */   }
/*     */   
/*     */   public Type getGenericType() {
/* 289 */     if (this.method != null) {
/* 290 */       return this.method.getGenericParameterTypes()[0];
/*     */     }
/* 292 */     return this.field.getGenericType();
/*     */   }
/*     */ 
/*     */   
/*     */   public Parameter getParameter() {
/* 297 */     return this.wrappedParameter.getParameter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type findFieldGenericType() {
/* 304 */     if (this.method != null) {
/* 305 */       return null;
/*     */     }
/* 307 */     if (this.field.getGenericType() instanceof ParameterizedType) {
/* 308 */       ParameterizedType p = (ParameterizedType)this.field.getGenericType();
/* 309 */       Type cls = p.getActualTypeArguments()[0];
/* 310 */       if (cls instanceof Class)
/* 311 */         return cls; 
/* 312 */       if (cls instanceof WildcardType) {
/* 313 */         WildcardType wildcardType = (WildcardType)cls;
/* 314 */         if ((wildcardType.getLowerBounds()).length > 0) {
/* 315 */           return wildcardType.getLowerBounds()[0];
/*     */         }
/* 317 */         if ((wildcardType.getUpperBounds()).length > 0) {
/* 318 */           return wildcardType.getUpperBounds()[0];
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 324 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isDynamicParameter() {
/* 328 */     return (this.wrappedParameter.getDynamicParameter() != null);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\Parameterized.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */