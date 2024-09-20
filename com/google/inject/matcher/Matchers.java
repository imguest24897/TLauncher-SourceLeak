/*     */ package com.google.inject.matcher;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Matchers
/*     */ {
/*     */   public static Matcher<Object> any() {
/*  39 */     return ANY;
/*     */   }
/*     */   
/*  42 */   private static final Matcher<Object> ANY = new Any();
/*     */   
/*     */   private static class Any
/*     */     extends AbstractMatcher<Object> implements Serializable {
/*     */     public boolean matches(Object o) {
/*  47 */       return true;
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     private Any() {}
/*     */     public String toString() {
/*  52 */       return "any()";
/*     */     }
/*     */     
/*     */     public Object readResolve() {
/*  56 */       return Matchers.any();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Matcher<T> not(Matcher<? super T> p) {
/*  64 */     return new Not<>(p);
/*     */   }
/*     */   
/*     */   private static class Not<T> extends AbstractMatcher<T> implements Serializable { final Matcher<? super T> delegate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private Not(Matcher<? super T> delegate) {
/*  71 */       this.delegate = (Matcher<? super T>)Preconditions.checkNotNull(delegate, "delegate");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(T t) {
/*  76 */       return !this.delegate.matches(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/*  81 */       return (other instanceof Not && ((Not)other).delegate.equals(this.delegate));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/*  86 */       return -this.delegate.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  91 */       String str = String.valueOf(this.delegate); return (new StringBuilder(5 + String.valueOf(str).length())).append("not(").append(str).append(")").toString();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void checkForRuntimeRetention(Class<? extends Annotation> annotationType) {
/*  98 */     Retention retention = annotationType.<Retention>getAnnotation(Retention.class);
/*  99 */     Preconditions.checkArgument((retention != null && retention
/* 100 */         .value() == RetentionPolicy.RUNTIME), "Annotation %s is missing RUNTIME retention", annotationType
/*     */         
/* 102 */         .getSimpleName());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Matcher<AnnotatedElement> annotatedWith(Class<? extends Annotation> annotationType) {
/* 108 */     return new AnnotatedWithType(annotationType);
/*     */   }
/*     */   
/*     */   private static class AnnotatedWithType extends AbstractMatcher<AnnotatedElement> implements Serializable {
/*     */     private final Class<? extends Annotation> annotationType;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public AnnotatedWithType(Class<? extends Annotation> annotationType) {
/* 116 */       this.annotationType = (Class<? extends Annotation>)Preconditions.checkNotNull(annotationType, "annotation type");
/* 117 */       Matchers.checkForRuntimeRetention(annotationType);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(AnnotatedElement element) {
/* 122 */       return element.isAnnotationPresent(this.annotationType);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 127 */       return (other instanceof AnnotatedWithType && ((AnnotatedWithType)other).annotationType
/* 128 */         .equals(this.annotationType));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 133 */       return 37 * this.annotationType.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 138 */       String str = this.annotationType.getSimpleName(); return (new StringBuilder(21 + String.valueOf(str).length())).append("annotatedWith(").append(str).append(".class)").toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Matcher<AnnotatedElement> annotatedWith(Annotation annotation) {
/* 146 */     return new AnnotatedWith(annotation);
/*     */   }
/*     */   
/*     */   private static class AnnotatedWith extends AbstractMatcher<AnnotatedElement> implements Serializable {
/*     */     private final Annotation annotation;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public AnnotatedWith(Annotation annotation) {
/* 154 */       this.annotation = (Annotation)Preconditions.checkNotNull(annotation, "annotation");
/* 155 */       Matchers.checkForRuntimeRetention(annotation.annotationType());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(AnnotatedElement element) {
/* 160 */       Annotation fromElement = element.getAnnotation((Class)this.annotation.annotationType());
/* 161 */       return (fromElement != null && this.annotation.equals(fromElement));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 166 */       return (other instanceof AnnotatedWith && ((AnnotatedWith)other).annotation
/* 167 */         .equals(this.annotation));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 172 */       return 37 * this.annotation.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 177 */       String str = String.valueOf(this.annotation); return (new StringBuilder(15 + String.valueOf(str).length())).append("annotatedWith(").append(str).append(")").toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Matcher<Class> subclassesOf(Class<?> superclass) {
/* 185 */     return new SubclassesOf(superclass);
/*     */   }
/*     */   
/*     */   private static class SubclassesOf extends AbstractMatcher<Class> implements Serializable { private final Class<?> superclass;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public SubclassesOf(Class<?> superclass) {
/* 192 */       this.superclass = (Class)Preconditions.checkNotNull(superclass, "superclass");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Class<?> subclass) {
/* 197 */       return this.superclass.isAssignableFrom(subclass);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 202 */       return (other instanceof SubclassesOf && ((SubclassesOf)other).superclass.equals(this.superclass));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 207 */       return 37 * this.superclass.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 212 */       String str = this.superclass.getSimpleName(); return (new StringBuilder(20 + String.valueOf(str).length())).append("subclassesOf(").append(str).append(".class)").toString();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Matcher<Object> only(Object value) {
/* 220 */     return new Only(value);
/*     */   }
/*     */   
/*     */   private static class Only extends AbstractMatcher<Object> implements Serializable { private final Object value;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public Only(Object value) {
/* 227 */       this.value = Preconditions.checkNotNull(value, "value");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Object other) {
/* 232 */       return this.value.equals(other);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 237 */       return (other instanceof Only && ((Only)other).value.equals(this.value));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 242 */       return 37 * this.value.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 247 */       String str = String.valueOf(this.value); return (new StringBuilder(6 + String.valueOf(str).length())).append("only(").append(str).append(")").toString();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Matcher<Object> identicalTo(Object value) {
/* 255 */     return new IdenticalTo(value);
/*     */   }
/*     */   
/*     */   private static class IdenticalTo extends AbstractMatcher<Object> implements Serializable { private final Object value;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public IdenticalTo(Object value) {
/* 262 */       this.value = Preconditions.checkNotNull(value, "value");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Object other) {
/* 267 */       return (this.value == other);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 272 */       return (other instanceof IdenticalTo && ((IdenticalTo)other).value == this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 277 */       return 37 * System.identityHashCode(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 282 */       String str = String.valueOf(this.value); return (new StringBuilder(13 + String.valueOf(str).length())).append("identicalTo(").append(str).append(")").toString();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Matcher<Class> inPackage(Package targetPackage) {
/* 293 */     return new InPackage(targetPackage);
/*     */   }
/*     */   
/*     */   private static class InPackage extends AbstractMatcher<Class> implements Serializable { private final transient Package targetPackage;
/*     */     private final String packageName;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public InPackage(Package targetPackage) {
/* 301 */       this.targetPackage = (Package)Preconditions.checkNotNull(targetPackage, "package");
/* 302 */       this.packageName = targetPackage.getName();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Class c) {
/* 307 */       return c.getPackage().equals(this.targetPackage);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 312 */       return (other instanceof InPackage && ((InPackage)other).targetPackage.equals(this.targetPackage));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 317 */       return 37 * this.targetPackage.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 322 */       String str = this.targetPackage.getName(); return (new StringBuilder(11 + String.valueOf(str).length())).append("inPackage(").append(str).append(")").toString();
/*     */     }
/*     */     
/*     */     public Object readResolve() {
/* 326 */       return Matchers.inPackage(Package.getPackage(this.packageName));
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Matcher<Class> inSubpackage(String targetPackageName) {
/* 339 */     return new InSubpackage(targetPackageName);
/*     */   }
/*     */   
/*     */   private static class InSubpackage extends AbstractMatcher<Class> implements Serializable { private final String targetPackageName;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public InSubpackage(String targetPackageName) {
/* 346 */       this.targetPackageName = targetPackageName;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Class c) {
/* 351 */       String classPackageName = c.getPackage().getName();
/* 352 */       return (classPackageName.equals(this.targetPackageName) || classPackageName
/* 353 */         .startsWith(String.valueOf(this.targetPackageName).concat(".")));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 358 */       return (other instanceof InSubpackage && ((InSubpackage)other).targetPackageName
/* 359 */         .equals(this.targetPackageName));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 364 */       return 37 * this.targetPackageName.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 369 */       String str = this.targetPackageName; return (new StringBuilder(14 + String.valueOf(str).length())).append("inSubpackage(").append(str).append(")").toString();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Matcher<Method> returns(Matcher<? super Class<?>> returnType) {
/* 377 */     return new Returns(returnType);
/*     */   }
/*     */   
/*     */   private static class Returns extends AbstractMatcher<Method> implements Serializable { private final Matcher<? super Class<?>> returnType;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public Returns(Matcher<? super Class<?>> returnType) {
/* 384 */       this.returnType = (Matcher<? super Class<?>>)Preconditions.checkNotNull(returnType, "return type matcher");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Method m) {
/* 389 */       return this.returnType.matches(m.getReturnType());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 394 */       return (other instanceof Returns && ((Returns)other).returnType.equals(this.returnType));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 399 */       return 37 * this.returnType.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 404 */       String str = String.valueOf(this.returnType); return (new StringBuilder(9 + String.valueOf(str).length())).append("returns(").append(str).append(")").toString();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\matcher\Matchers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */