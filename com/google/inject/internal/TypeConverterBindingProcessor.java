/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.internal.util.SourceProvider;
/*     */ import com.google.inject.matcher.AbstractMatcher;
/*     */ import com.google.inject.matcher.Matcher;
/*     */ import com.google.inject.matcher.Matchers;
/*     */ import com.google.inject.spi.TypeConverter;
/*     */ import com.google.inject.spi.TypeConverterBinding;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
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
/*     */ final class TypeConverterBindingProcessor
/*     */   extends AbstractProcessor
/*     */ {
/*     */   TypeConverterBindingProcessor(Errors errors) {
/*  39 */     super(errors);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void prepareBuiltInConverters(InjectorImpl injector) {
/*  45 */     convertToPrimitiveType(injector, int.class, (Class)Integer.class);
/*  46 */     convertToPrimitiveType(injector, long.class, (Class)Long.class);
/*  47 */     convertToPrimitiveType(injector, boolean.class, (Class)Boolean.class);
/*  48 */     convertToPrimitiveType(injector, byte.class, (Class)Byte.class);
/*  49 */     convertToPrimitiveType(injector, short.class, (Class)Short.class);
/*  50 */     convertToPrimitiveType(injector, float.class, (Class)Float.class);
/*  51 */     convertToPrimitiveType(injector, double.class, (Class)Double.class);
/*     */     
/*  53 */     convertToClass(injector, Character.class, new TypeConverter()
/*     */         {
/*     */ 
/*     */           
/*     */           public Object convert(String value, TypeLiteral<?> toType)
/*     */           {
/*  59 */             value = value.trim();
/*  60 */             if (value.length() != 1) {
/*  61 */               throw new RuntimeException("Length != 1.");
/*     */             }
/*  63 */             return Character.valueOf(value.charAt(0));
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/*  68 */             return "TypeConverter<Character>";
/*     */           }
/*     */         });
/*     */     
/*  72 */     convertToClasses(injector, 
/*     */         
/*  74 */         Matchers.subclassesOf(Enum.class), new TypeConverter()
/*     */         {
/*     */           
/*     */           public Object convert(String value, TypeLiteral<?> toType)
/*     */           {
/*  79 */             return Enum.valueOf(toType.getRawType(), value);
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/*  84 */             return "TypeConverter<E extends Enum<E>>";
/*     */           }
/*     */         });
/*     */     
/*  88 */     internalConvertToTypes(injector, (Matcher<? super TypeLiteral<?>>)new AbstractMatcher<TypeLiteral<?>>()
/*     */         {
/*     */           
/*     */           public boolean matches(TypeLiteral<?> typeLiteral)
/*     */           {
/*  93 */             return (typeLiteral.getRawType() == Class.class);
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/*  98 */             return "Class<?>";
/*     */           }
/*     */         },  new TypeConverter()
/*     */         {
/*     */           public Object convert(String value, TypeLiteral<?> toType)
/*     */           {
/*     */             try {
/* 105 */               return Class.forName(value);
/* 106 */             } catch (ClassNotFoundException e) {
/* 107 */               throw new RuntimeException(e.getMessage());
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 113 */             return "TypeConverter<Class<?>>";
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> void convertToPrimitiveType(InjectorImpl injector, Class<T> primitiveType, final Class<T> wrapperType) {
/*     */     try {
/* 122 */       String.valueOf(capitalize(primitiveType.getName())); final Method parser = wrapperType.getMethod((String.valueOf(capitalize(primitiveType.getName())).length() != 0) ? "parse".concat(String.valueOf(capitalize(primitiveType.getName()))) : new String("parse"), new Class[] { String.class });
/*     */       
/* 124 */       TypeConverter typeConverter = new TypeConverter()
/*     */         {
/*     */           public Object convert(String value, TypeLiteral<?> toType)
/*     */           {
/*     */             try {
/* 129 */               return parser.invoke(null, new Object[] { value });
/* 130 */             } catch (IllegalAccessException e) {
/* 131 */               throw new AssertionError(e);
/* 132 */             } catch (InvocationTargetException e) {
/* 133 */               throw new RuntimeException(e.getTargetException().getMessage());
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 139 */             String str = wrapperType.getSimpleName(); return (new StringBuilder(15 + String.valueOf(str).length())).append("TypeConverter<").append(str).append(">").toString();
/*     */           }
/*     */         };
/*     */       
/* 143 */       convertToClass(injector, wrapperType, typeConverter);
/* 144 */     } catch (NoSuchMethodException e) {
/* 145 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> void convertToClass(InjectorImpl injector, Class<T> type, TypeConverter converter) {
/* 151 */     convertToClasses(injector, Matchers.identicalTo(type), converter);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void convertToClasses(InjectorImpl injector, final Matcher<? super Class<?>> typeMatcher, TypeConverter converter) {
/* 156 */     internalConvertToTypes(injector, (Matcher<? super TypeLiteral<?>>)new AbstractMatcher<TypeLiteral<?>>()
/*     */         {
/*     */           
/*     */           public boolean matches(TypeLiteral<?> typeLiteral)
/*     */           {
/* 161 */             Type type = typeLiteral.getType();
/* 162 */             if (!(type instanceof Class)) {
/* 163 */               return false;
/*     */             }
/* 165 */             Class<?> clazz = (Class)type;
/* 166 */             return typeMatcher.matches(clazz);
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 171 */             return typeMatcher.toString();
/*     */           }
/*     */         },  converter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void internalConvertToTypes(InjectorImpl injector, Matcher<? super TypeLiteral<?>> typeMatcher, TypeConverter converter) {
/* 179 */     injector
/* 180 */       .getBindingData()
/* 181 */       .addConverter(new TypeConverterBinding(SourceProvider.UNKNOWN_SOURCE, typeMatcher, converter));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean visit(TypeConverterBinding command) {
/* 187 */     this.injector
/* 188 */       .getBindingData()
/* 189 */       .addConverter(new TypeConverterBinding(command
/*     */           
/* 191 */           .getSource(), command.getTypeMatcher(), command.getTypeConverter()));
/* 192 */     return Boolean.valueOf(true);
/*     */   }
/*     */   
/*     */   private static String capitalize(String s) {
/* 196 */     if (s.length() == 0) {
/* 197 */       return s;
/*     */     }
/* 199 */     char first = s.charAt(0);
/* 200 */     char capitalized = Character.toUpperCase(first);
/* 201 */     String str = s.substring(1); return (first == capitalized) ? s : (new StringBuilder(1 + String.valueOf(str).length())).append(capitalized).append(str).toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\TypeConverterBindingProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */