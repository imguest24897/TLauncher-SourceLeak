/*     */ package com.google.inject.util;
/*     */ 
/*     */ import com.google.inject.Provider;
/*     */ import com.google.inject.internal.MoreTypes;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Types
/*     */ {
/*     */   public static ParameterizedType newParameterizedType(Type rawType, Type... typeArguments) {
/*  49 */     return newParameterizedTypeWithOwner(null, rawType, typeArguments);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ParameterizedType newParameterizedTypeWithOwner(Type ownerType, Type rawType, Type... typeArguments) {
/*  60 */     return (ParameterizedType)new MoreTypes.ParameterizedTypeImpl(ownerType, rawType, typeArguments);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static GenericArrayType arrayOf(Type componentType) {
/*  69 */     return (GenericArrayType)new MoreTypes.GenericArrayTypeImpl(componentType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static WildcardType subtypeOf(Type bound) {
/*  79 */     return (WildcardType)new MoreTypes.WildcardTypeImpl(new Type[] { bound }, MoreTypes.EMPTY_TYPE_ARRAY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static WildcardType supertypeOf(Type bound) {
/*  87 */     return (WildcardType)new MoreTypes.WildcardTypeImpl(new Type[] { Object.class }, new Type[] { bound });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ParameterizedType listOf(Type elementType) {
/*  96 */     return newParameterizedType(List.class, new Type[] { elementType });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ParameterizedType collectionOf(Type elementType) {
/* 105 */     return newParameterizedType(Collection.class, new Type[] { elementType });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ParameterizedType setOf(Type elementType) {
/* 114 */     return newParameterizedType(Set.class, new Type[] { elementType });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ParameterizedType mapOf(Type keyType, Type valueType) {
/* 124 */     return newParameterizedType(Map.class, new Type[] { keyType, valueType });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ParameterizedType providerOf(Type providedType) {
/* 135 */     return newParameterizedType(Provider.class, new Type[] { providedType });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type javaxProviderOf(Type type) {
/* 145 */     return newParameterizedType(Provider.class, new Type[] { type });
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\injec\\util\Types.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */