/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.inject.ConfigurationException;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.util.Types;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.GenericDeclaration;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.util.Arrays;
/*     */ import java.util.NoSuchElementException;
/*     */ import javax.inject.Provider;
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
/*     */ public class MoreTypes
/*     */ {
/*  46 */   public static final Type[] EMPTY_TYPE_ARRAY = new Type[0];
/*     */ 
/*     */ 
/*     */   
/*  50 */   private static final ImmutableMap<TypeLiteral<?>, TypeLiteral<?>> PRIMITIVE_TO_WRAPPER = (new ImmutableMap.Builder())
/*     */     
/*  52 */     .put(TypeLiteral.get(boolean.class), TypeLiteral.get(Boolean.class))
/*  53 */     .put(TypeLiteral.get(byte.class), TypeLiteral.get(Byte.class))
/*  54 */     .put(TypeLiteral.get(short.class), TypeLiteral.get(Short.class))
/*  55 */     .put(TypeLiteral.get(int.class), TypeLiteral.get(Integer.class))
/*  56 */     .put(TypeLiteral.get(long.class), TypeLiteral.get(Long.class))
/*  57 */     .put(TypeLiteral.get(float.class), TypeLiteral.get(Float.class))
/*  58 */     .put(TypeLiteral.get(double.class), TypeLiteral.get(Double.class))
/*  59 */     .put(TypeLiteral.get(char.class), TypeLiteral.get(Character.class))
/*  60 */     .put(TypeLiteral.get(void.class), TypeLiteral.get(Void.class))
/*  61 */     .build();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Key<T> canonicalizeKey(Key<T> key) {
/*  70 */     if (key.getClass() == Key.class) {
/*  71 */       return key;
/*     */     }
/*  73 */     return key.ofType(key.getTypeLiteral());
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
/*     */   public static <T> TypeLiteral<T> canonicalizeForKey(TypeLiteral<T> typeLiteral) {
/*  88 */     Type type = typeLiteral.getType();
/*  89 */     if (!isFullySpecified(type)) {
/*  90 */       Errors errors = (new Errors()).keyNotFullySpecified(typeLiteral);
/*  91 */       throw new ConfigurationException(errors.getMessages());
/*     */     } 
/*     */     
/*  94 */     if (typeLiteral.getRawType() == Provider.class) {
/*  95 */       ParameterizedType parameterizedType = (ParameterizedType)type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 102 */       TypeLiteral<T> guiceProviderType = TypeLiteral.get(Types.providerOf(getSharedTypeArguments(parameterizedType)[0]));
/* 103 */       return guiceProviderType;
/*     */     } 
/*     */ 
/*     */     
/* 107 */     TypeLiteral<T> wrappedPrimitives = (TypeLiteral<T>)PRIMITIVE_TO_WRAPPER.get(typeLiteral);
/* 108 */     if (wrappedPrimitives != null) {
/* 109 */       return wrappedPrimitives;
/*     */     }
/*     */ 
/*     */     
/* 113 */     if (typeLiteral.getClass() == TypeLiteral.class) {
/* 114 */       return typeLiteral;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 120 */     TypeLiteral<T> recreated = TypeLiteral.get(typeLiteral.getType());
/* 121 */     return recreated;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isFullySpecified(Type type) {
/* 126 */     if (type instanceof Class) {
/* 127 */       return true;
/*     */     }
/* 129 */     if (type instanceof CompositeType) {
/* 130 */       return ((CompositeType)type).isFullySpecified();
/*     */     }
/* 132 */     if (type instanceof TypeVariable) {
/* 133 */       return false;
/*     */     }
/*     */     
/* 136 */     return ((CompositeType)canonicalize(type)).isFullySpecified();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type canonicalize(Type type) {
/* 145 */     if (type instanceof Class) {
/* 146 */       Class<?> c = (Class)type;
/* 147 */       return c.isArray() ? new GenericArrayTypeImpl(canonicalize(c.getComponentType())) : c;
/*     */     } 
/* 149 */     if (type instanceof CompositeType) {
/* 150 */       return type;
/*     */     }
/* 152 */     if (type instanceof ParameterizedType) {
/* 153 */       ParameterizedType p = (ParameterizedType)type;
/* 154 */       return new ParameterizedTypeImpl(p.getOwnerType(), p.getRawType(), getSharedTypeArguments(p));
/*     */     } 
/* 156 */     if (type instanceof GenericArrayType) {
/* 157 */       GenericArrayType g = (GenericArrayType)type;
/* 158 */       return new GenericArrayTypeImpl(g.getGenericComponentType());
/*     */     } 
/* 160 */     if (type instanceof WildcardType) {
/* 161 */       WildcardType w = (WildcardType)type;
/* 162 */       return new WildcardTypeImpl(w.getUpperBounds(), w.getLowerBounds());
/*     */     } 
/*     */ 
/*     */     
/* 166 */     return type;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Class<?> getRawType(Type type) {
/* 171 */     if (type instanceof Class)
/*     */     {
/* 173 */       return (Class)type;
/*     */     }
/* 175 */     if (type instanceof ParameterizedType) {
/* 176 */       ParameterizedType parameterizedType = (ParameterizedType)type;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 181 */       Type rawType = parameterizedType.getRawType();
/* 182 */       Preconditions.checkArgument(rawType instanceof Class, "Expected a Class, but <%s> is of type %s", type, type
/*     */ 
/*     */ 
/*     */           
/* 186 */           .getClass().getName());
/* 187 */       return (Class)rawType;
/*     */     } 
/* 189 */     if (type instanceof GenericArrayType) {
/* 190 */       Type componentType = ((GenericArrayType)type).getGenericComponentType();
/* 191 */       return Array.newInstance(getRawType(componentType), 0).getClass();
/*     */     } 
/* 193 */     if (type instanceof TypeVariable || type instanceof WildcardType)
/*     */     {
/*     */       
/* 196 */       return Object.class;
/*     */     }
/*     */     
/* 199 */     String str1 = String.valueOf(type);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 204 */     String str2 = type.getClass().getName(); throw new IllegalArgumentException((new StringBuilder(76 + String.valueOf(str1).length() + String.valueOf(str2).length())).append("Expected a Class, ParameterizedType, or GenericArrayType, but <").append(str1).append("> is of type ").append(str2).toString());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equals(Type a, Type b) {
/* 210 */     if (a == b)
/*     */     {
/* 212 */       return true;
/*     */     }
/* 214 */     if (a instanceof Class)
/*     */     {
/* 216 */       return a.equals(b);
/*     */     }
/* 218 */     if (a instanceof ParameterizedType) {
/* 219 */       if (!(b instanceof ParameterizedType)) {
/* 220 */         return false;
/*     */       }
/*     */       
/* 223 */       ParameterizedType pa = (ParameterizedType)a;
/* 224 */       ParameterizedType pb = (ParameterizedType)b;
/* 225 */       return (Objects.equal(pa.getOwnerType(), pb.getOwnerType()) && pa
/* 226 */         .getRawType().equals(pb.getRawType()) && 
/* 227 */         Arrays.equals((Object[])getSharedTypeArguments(pa), (Object[])getSharedTypeArguments(pb)));
/*     */     } 
/* 229 */     if (a instanceof GenericArrayType) {
/* 230 */       if (!(b instanceof GenericArrayType)) {
/* 231 */         return false;
/*     */       }
/*     */       
/* 234 */       GenericArrayType ga = (GenericArrayType)a;
/* 235 */       GenericArrayType gb = (GenericArrayType)b;
/* 236 */       return equals(ga.getGenericComponentType(), gb.getGenericComponentType());
/*     */     } 
/* 238 */     if (a instanceof WildcardType) {
/* 239 */       if (!(b instanceof WildcardType)) {
/* 240 */         return false;
/*     */       }
/*     */       
/* 243 */       WildcardType wa = (WildcardType)a;
/* 244 */       WildcardType wb = (WildcardType)b;
/* 245 */       return (Arrays.equals((Object[])wa.getUpperBounds(), (Object[])wb.getUpperBounds()) && 
/* 246 */         Arrays.equals((Object[])wa.getLowerBounds(), (Object[])wb.getLowerBounds()));
/*     */     } 
/* 248 */     if (a instanceof TypeVariable) {
/* 249 */       if (!(b instanceof TypeVariable)) {
/* 250 */         return false;
/*     */       }
/* 252 */       TypeVariable<?> va = (TypeVariable)a;
/* 253 */       TypeVariable<?> vb = (TypeVariable)b;
/* 254 */       return (va.getGenericDeclaration().equals(vb.getGenericDeclaration()) && va
/* 255 */         .getName().equals(vb.getName()));
/*     */     } 
/*     */ 
/*     */     
/* 259 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int hashCodeOrZero(Object o) {
/* 264 */     return (o != null) ? o.hashCode() : 0;
/*     */   }
/*     */   
/*     */   public static String typeToString(Type type) {
/* 268 */     return (type instanceof Class) ? ((Class)type).getName() : type.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type getGenericSupertype(Type type, Class<?> rawType, Class<?> toResolve) {
/* 277 */     if (toResolve == rawType) {
/* 278 */       return type;
/*     */     }
/*     */ 
/*     */     
/* 282 */     if (toResolve.isInterface()) {
/* 283 */       Class<?>[] interfaces = rawType.getInterfaces();
/* 284 */       for (int i = 0, length = interfaces.length; i < length; i++) {
/* 285 */         if (interfaces[i] == toResolve)
/* 286 */           return rawType.getGenericInterfaces()[i]; 
/* 287 */         if (toResolve.isAssignableFrom(interfaces[i])) {
/* 288 */           return getGenericSupertype(rawType.getGenericInterfaces()[i], interfaces[i], toResolve);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 294 */     if (!rawType.isInterface()) {
/* 295 */       while (rawType != Object.class) {
/* 296 */         Class<?> rawSupertype = rawType.getSuperclass();
/* 297 */         if (rawSupertype == toResolve)
/* 298 */           return rawType.getGenericSuperclass(); 
/* 299 */         if (toResolve.isAssignableFrom(rawSupertype)) {
/* 300 */           return getGenericSupertype(rawType.getGenericSuperclass(), rawSupertype, toResolve);
/*     */         }
/* 302 */         rawType = rawSupertype;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 307 */     return toResolve;
/*     */   }
/*     */   
/*     */   public static Type resolveTypeVariable(Type type, Class<?> rawType, TypeVariable<?> unknown) {
/* 311 */     Class<?> declaredByRaw = declaringClassOf(unknown);
/*     */ 
/*     */     
/* 314 */     if (declaredByRaw == null) {
/* 315 */       return unknown;
/*     */     }
/*     */     
/* 318 */     Type declaredBy = getGenericSupertype(type, rawType, declaredByRaw);
/* 319 */     if (declaredBy instanceof ParameterizedType) {
/* 320 */       int index = indexOf((Object[])declaredByRaw.getTypeParameters(), unknown);
/* 321 */       return getSharedTypeArguments((ParameterizedType)declaredBy)[index];
/*     */     } 
/*     */     
/* 324 */     return unknown;
/*     */   }
/*     */   
/*     */   private static int indexOf(Object[] array, Object toFind) {
/* 328 */     for (int i = 0; i < array.length; i++) {
/* 329 */       if (toFind.equals(array[i])) {
/* 330 */         return i;
/*     */       }
/*     */     } 
/* 333 */     throw new NoSuchElementException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Type[] getSharedTypeArguments(ParameterizedType p) {
/* 341 */     if (p instanceof ParameterizedTypeImpl) {
/* 342 */       return ((ParameterizedTypeImpl)p).typeArguments;
/*     */     }
/* 344 */     return p.getActualTypeArguments();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<?> declaringClassOf(TypeVariable<?> typeVariable) {
/* 352 */     GenericDeclaration genericDeclaration = (GenericDeclaration)typeVariable.getGenericDeclaration();
/* 353 */     return (genericDeclaration instanceof Class) ? (Class)genericDeclaration : null;
/*     */   }
/*     */   
/*     */   public static class ParameterizedTypeImpl
/*     */     implements ParameterizedType, Serializable, CompositeType {
/*     */     private final Type ownerType;
/*     */     private final Type rawType;
/*     */     final Type[] typeArguments;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public ParameterizedTypeImpl(Type ownerType, Type rawType, Type... typeArguments) {
/* 364 */       ensureOwnerType(ownerType, rawType);
/*     */       
/* 366 */       this.ownerType = (ownerType == null) ? null : MoreTypes.canonicalize(ownerType);
/* 367 */       this.rawType = MoreTypes.canonicalize(rawType);
/* 368 */       int providedArgumentLength = typeArguments.length;
/* 369 */       Type[] clonedTypeArguments = (Type[])typeArguments.clone();
/* 370 */       int validArgLength = providedArgumentLength;
/* 371 */       if (this.rawType instanceof Class) {
/* 372 */         Class<?> klass = (Class)this.rawType;
/* 373 */         int classArgumentLength = (klass.getTypeParameters()).length;
/*     */         
/* 375 */         if (providedArgumentLength < classArgumentLength) {
/*     */ 
/*     */ 
/*     */           
/* 379 */           String str = klass.getName(); throw new IllegalArgumentException((new StringBuilder(168 + String.valueOf(str).length())).append("Length of provided type arguments is less than length of required parameters for class:").append(str).append(" provided type argument length:").append(providedArgumentLength).append(" length of class parameters:").append(classArgumentLength).toString());
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 384 */         if (providedArgumentLength > classArgumentLength) {
/* 385 */           validArgLength = classArgumentLength;
/*     */         }
/*     */       } 
/*     */       
/* 389 */       this.typeArguments = new Type[validArgLength];
/* 390 */       for (int t = 0; t < validArgLength; t++) {
/* 391 */         Preconditions.checkNotNull(clonedTypeArguments[t], "type parameter");
/* 392 */         MoreTypes.checkNotPrimitive(clonedTypeArguments[t], "type parameters");
/* 393 */         this.typeArguments[t] = MoreTypes.canonicalize(clonedTypeArguments[t]);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Type[] getActualTypeArguments() {
/* 399 */       return (Type[])this.typeArguments.clone();
/*     */     }
/*     */ 
/*     */     
/*     */     public Type getRawType() {
/* 404 */       return this.rawType;
/*     */     }
/*     */ 
/*     */     
/*     */     public Type getOwnerType() {
/* 409 */       return this.ownerType;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isFullySpecified() {
/* 414 */       if (this.ownerType != null && !MoreTypes.isFullySpecified(this.ownerType)) {
/* 415 */         return false;
/*     */       }
/*     */       
/* 418 */       if (!MoreTypes.isFullySpecified(this.rawType)) {
/* 419 */         return false;
/*     */       }
/*     */       
/* 422 */       for (Type type : this.typeArguments) {
/* 423 */         if (!MoreTypes.isFullySpecified(type)) {
/* 424 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 428 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 433 */       return (other instanceof ParameterizedType && 
/* 434 */         MoreTypes.equals(this, (ParameterizedType)other));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 439 */       return Arrays.hashCode((Object[])this.typeArguments) ^ this.rawType.hashCode() ^ MoreTypes.hashCodeOrZero(this.ownerType);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 444 */       StringBuilder stringBuilder = new StringBuilder(30 * (this.typeArguments.length + 1));
/* 445 */       stringBuilder.append(MoreTypes.typeToString(this.rawType));
/*     */       
/* 447 */       if (this.typeArguments.length == 0) {
/* 448 */         return stringBuilder.toString();
/*     */       }
/*     */       
/* 451 */       stringBuilder.append("<").append(MoreTypes.typeToString(this.typeArguments[0]));
/* 452 */       for (int i = 1; i < this.typeArguments.length; i++) {
/* 453 */         stringBuilder.append(", ").append(MoreTypes.typeToString(this.typeArguments[i]));
/*     */       }
/* 455 */       return stringBuilder.append(">").toString();
/*     */     }
/*     */     
/*     */     private static void ensureOwnerType(Type ownerType, Type rawType) {
/* 459 */       if (rawType instanceof Class) {
/* 460 */         Class<?> rawTypeAsClass = (Class)rawType;
/* 461 */         Preconditions.checkArgument((ownerType != null || rawTypeAsClass
/* 462 */             .getEnclosingClass() == null), "No owner type for enclosed %s", rawType);
/*     */ 
/*     */         
/* 465 */         Preconditions.checkArgument((ownerType == null || rawTypeAsClass
/* 466 */             .getEnclosingClass() != null), "Owner type for unenclosed %s", rawType);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class GenericArrayTypeImpl
/*     */     implements GenericArrayType, Serializable, CompositeType
/*     */   {
/*     */     private final Type componentType;
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public GenericArrayTypeImpl(Type componentType) {
/* 480 */       this.componentType = MoreTypes.canonicalize(componentType);
/*     */     }
/*     */ 
/*     */     
/*     */     public Type getGenericComponentType() {
/* 485 */       return this.componentType;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isFullySpecified() {
/* 490 */       return MoreTypes.isFullySpecified(this.componentType);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 495 */       return (o instanceof GenericArrayType && MoreTypes.equals(this, (GenericArrayType)o));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 500 */       return this.componentType.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 505 */       return String.valueOf(MoreTypes.typeToString(this.componentType)).concat("[]");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class WildcardTypeImpl
/*     */     implements WildcardType, Serializable, CompositeType
/*     */   {
/*     */     private final Type upperBound;
/*     */     
/*     */     private final Type lowerBound;
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */     
/*     */     public WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
/* 521 */       Preconditions.checkArgument((lowerBounds.length <= 1), "Must have at most one lower bound.");
/* 522 */       Preconditions.checkArgument((upperBounds.length == 1), "Must have exactly one upper bound.");
/*     */       
/* 524 */       if (lowerBounds.length == 1) {
/* 525 */         Preconditions.checkNotNull(lowerBounds[0], "lowerBound");
/* 526 */         MoreTypes.checkNotPrimitive(lowerBounds[0], "wildcard bounds");
/* 527 */         Preconditions.checkArgument((upperBounds[0] == Object.class), "bounded both ways");
/* 528 */         this.lowerBound = MoreTypes.canonicalize(lowerBounds[0]);
/* 529 */         this.upperBound = Object.class;
/*     */       } else {
/*     */         
/* 532 */         Preconditions.checkNotNull(upperBounds[0], "upperBound");
/* 533 */         MoreTypes.checkNotPrimitive(upperBounds[0], "wildcard bounds");
/* 534 */         this.lowerBound = null;
/* 535 */         this.upperBound = MoreTypes.canonicalize(upperBounds[0]);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Type[] getUpperBounds() {
/* 541 */       return new Type[] { this.upperBound };
/*     */     }
/*     */ 
/*     */     
/*     */     public Type[] getLowerBounds() {
/* 546 */       (new Type[1])[0] = this.lowerBound; return (this.lowerBound != null) ? new Type[1] : MoreTypes.EMPTY_TYPE_ARRAY;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isFullySpecified() {
/* 551 */       return (MoreTypes.isFullySpecified(this.upperBound) && (this.lowerBound == null || MoreTypes
/* 552 */         .isFullySpecified(this.lowerBound)));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 557 */       return (other instanceof WildcardType && MoreTypes.equals(this, (WildcardType)other));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 563 */       return ((this.lowerBound != null) ? (31 + this.lowerBound.hashCode()) : 1) ^ 31 + this.upperBound.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 568 */       if (this.lowerBound != null) {
/* 569 */         String.valueOf(MoreTypes.typeToString(this.lowerBound)); return (String.valueOf(MoreTypes.typeToString(this.lowerBound)).length() != 0) ? "? super ".concat(String.valueOf(MoreTypes.typeToString(this.lowerBound))) : new String("? super ");
/* 570 */       }  if (this.upperBound == Object.class) {
/* 571 */         return "?";
/*     */       }
/* 573 */       String.valueOf(MoreTypes.typeToString(this.upperBound)); return (String.valueOf(MoreTypes.typeToString(this.upperBound)).length() != 0) ? "? extends ".concat(String.valueOf(MoreTypes.typeToString(this.upperBound))) : new String("? extends ");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void checkNotPrimitive(Type type, String use) {
/* 581 */     Preconditions.checkArgument((!(type instanceof Class) || 
/* 582 */         !((Class)type).isPrimitive()), "Primitive types are not allowed in %s: %s", use, type);
/*     */   }
/*     */   
/*     */   private static interface CompositeType {
/*     */     boolean isFullySpecified();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\MoreTypes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */