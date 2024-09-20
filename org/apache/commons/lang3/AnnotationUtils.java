/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import org.apache.commons.lang3.builder.ToStringBuilder;
/*     */ import org.apache.commons.lang3.builder.ToStringStyle;
/*     */ import org.apache.commons.lang3.exception.UncheckedException;
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
/*     */ public class AnnotationUtils
/*     */ {
/*  50 */   private static final ToStringStyle TO_STRING_STYLE = new ToStringStyle()
/*     */     {
/*     */       private static final long serialVersionUID = 1L;
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
/*     */       protected String getShortClassName(Class<?> cls) {
/*  73 */         return ClassUtils.getAllInterfaces(cls).stream().filter(Annotation.class::isAssignableFrom).findFirst()
/*  74 */           .map(iface -> "@" + iface.getName())
/*  75 */           .orElse("");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       protected void appendDetail(StringBuffer buffer, String fieldName, Object value) {
/*  84 */         if (value instanceof Annotation) {
/*  85 */           value = AnnotationUtils.toString((Annotation)value);
/*     */         }
/*  87 */         super.appendDetail(buffer, fieldName, value);
/*     */       }
/*     */     };
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
/*     */   public static boolean equals(Annotation a1, Annotation a2) {
/* 114 */     if (a1 == a2) {
/* 115 */       return true;
/*     */     }
/* 117 */     if (a1 == null || a2 == null) {
/* 118 */       return false;
/*     */     }
/* 120 */     Class<? extends Annotation> type1 = a1.annotationType();
/* 121 */     Class<? extends Annotation> type2 = a2.annotationType();
/* 122 */     Validate.notNull(type1, "Annotation %s with null annotationType()", new Object[] { a1 });
/* 123 */     Validate.notNull(type2, "Annotation %s with null annotationType()", new Object[] { a2 });
/* 124 */     if (!type1.equals(type2)) {
/* 125 */       return false;
/*     */     }
/*     */     try {
/* 128 */       for (Method m : type1.getDeclaredMethods()) {
/* 129 */         if ((m.getParameterTypes()).length == 0 && 
/* 130 */           isValidAnnotationMemberType(m.getReturnType())) {
/* 131 */           Object v1 = m.invoke(a1, new Object[0]);
/* 132 */           Object v2 = m.invoke(a2, new Object[0]);
/* 133 */           if (!memberEquals(m.getReturnType(), v1, v2)) {
/* 134 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/* 138 */     } catch (ReflectiveOperationException ex) {
/* 139 */       return false;
/*     */     } 
/* 141 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int hashCode(Annotation a) {
/* 157 */     int result = 0;
/* 158 */     Class<? extends Annotation> type = a.annotationType();
/* 159 */     for (Method m : type.getDeclaredMethods()) {
/*     */       try {
/* 161 */         Object value = m.invoke(a, new Object[0]);
/* 162 */         if (value == null) {
/* 163 */           throw new IllegalStateException(String.format("Annotation method %s returned null", new Object[] { m }));
/*     */         }
/* 165 */         result += hashMember(m.getName(), value);
/* 166 */       } catch (ReflectiveOperationException ex) {
/* 167 */         throw new UncheckedException(ex);
/*     */       } 
/*     */     } 
/* 170 */     return result;
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
/*     */   public static String toString(Annotation a) {
/* 182 */     ToStringBuilder builder = new ToStringBuilder(a, TO_STRING_STYLE);
/* 183 */     for (Method m : a.annotationType().getDeclaredMethods()) {
/* 184 */       if ((m.getParameterTypes()).length <= 0)
/*     */         
/*     */         try {
/*     */           
/* 188 */           builder.append(m.getName(), m.invoke(a, new Object[0]));
/* 189 */         } catch (ReflectiveOperationException ex) {
/* 190 */           throw new UncheckedException(ex);
/*     */         }  
/*     */     } 
/* 193 */     return builder.build();
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isValidAnnotationMemberType(Class<?> type) {
/* 208 */     if (type == null) {
/* 209 */       return false;
/*     */     }
/* 211 */     if (type.isArray()) {
/* 212 */       type = type.getComponentType();
/*     */     }
/* 214 */     return (type.isPrimitive() || type.isEnum() || type.isAnnotation() || String.class
/* 215 */       .equals(type) || Class.class.equals(type));
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
/*     */   private static int hashMember(String name, Object value) {
/* 227 */     int part1 = name.hashCode() * 127;
/* 228 */     if (ObjectUtils.isArray(value)) {
/* 229 */       return part1 ^ arrayMemberHash(value.getClass().getComponentType(), value);
/*     */     }
/* 231 */     if (value instanceof Annotation) {
/* 232 */       return part1 ^ hashCode((Annotation)value);
/*     */     }
/* 234 */     return part1 ^ value.hashCode();
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
/*     */ 
/*     */   
/*     */   private static boolean memberEquals(Class<?> type, Object o1, Object o2) {
/* 248 */     if (o1 == o2) {
/* 249 */       return true;
/*     */     }
/* 251 */     if (o1 == null || o2 == null) {
/* 252 */       return false;
/*     */     }
/* 254 */     if (type.isArray()) {
/* 255 */       return arrayMemberEquals(type.getComponentType(), o1, o2);
/*     */     }
/* 257 */     if (type.isAnnotation()) {
/* 258 */       return equals((Annotation)o1, (Annotation)o2);
/*     */     }
/* 260 */     return o1.equals(o2);
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
/*     */   private static boolean arrayMemberEquals(Class<?> componentType, Object o1, Object o2) {
/* 272 */     if (componentType.isAnnotation()) {
/* 273 */       return annotationArrayMemberEquals((Annotation[])o1, (Annotation[])o2);
/*     */     }
/* 275 */     if (componentType.equals(byte.class)) {
/* 276 */       return Arrays.equals((byte[])o1, (byte[])o2);
/*     */     }
/* 278 */     if (componentType.equals(short.class)) {
/* 279 */       return Arrays.equals((short[])o1, (short[])o2);
/*     */     }
/* 281 */     if (componentType.equals(int.class)) {
/* 282 */       return Arrays.equals((int[])o1, (int[])o2);
/*     */     }
/* 284 */     if (componentType.equals(char.class)) {
/* 285 */       return Arrays.equals((char[])o1, (char[])o2);
/*     */     }
/* 287 */     if (componentType.equals(long.class)) {
/* 288 */       return Arrays.equals((long[])o1, (long[])o2);
/*     */     }
/* 290 */     if (componentType.equals(float.class)) {
/* 291 */       return Arrays.equals((float[])o1, (float[])o2);
/*     */     }
/* 293 */     if (componentType.equals(double.class)) {
/* 294 */       return Arrays.equals((double[])o1, (double[])o2);
/*     */     }
/* 296 */     if (componentType.equals(boolean.class)) {
/* 297 */       return Arrays.equals((boolean[])o1, (boolean[])o2);
/*     */     }
/* 299 */     return Arrays.equals((Object[])o1, (Object[])o2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean annotationArrayMemberEquals(Annotation[] a1, Annotation[] a2) {
/* 310 */     if (a1.length != a2.length) {
/* 311 */       return false;
/*     */     }
/* 313 */     for (int i = 0; i < a1.length; i++) {
/* 314 */       if (!equals(a1[i], a2[i])) {
/* 315 */         return false;
/*     */       }
/*     */     } 
/* 318 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int arrayMemberHash(Class<?> componentType, Object o) {
/* 329 */     if (componentType.equals(byte.class)) {
/* 330 */       return Arrays.hashCode((byte[])o);
/*     */     }
/* 332 */     if (componentType.equals(short.class)) {
/* 333 */       return Arrays.hashCode((short[])o);
/*     */     }
/* 335 */     if (componentType.equals(int.class)) {
/* 336 */       return Arrays.hashCode((int[])o);
/*     */     }
/* 338 */     if (componentType.equals(char.class)) {
/* 339 */       return Arrays.hashCode((char[])o);
/*     */     }
/* 341 */     if (componentType.equals(long.class)) {
/* 342 */       return Arrays.hashCode((long[])o);
/*     */     }
/* 344 */     if (componentType.equals(float.class)) {
/* 345 */       return Arrays.hashCode((float[])o);
/*     */     }
/* 347 */     if (componentType.equals(double.class)) {
/* 348 */       return Arrays.hashCode((double[])o);
/*     */     }
/* 350 */     if (componentType.equals(boolean.class)) {
/* 351 */       return Arrays.hashCode((boolean[])o);
/*     */     }
/* 353 */     return Arrays.hashCode((Object[])o);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\AnnotationUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */