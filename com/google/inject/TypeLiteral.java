/*     */ package com.google.inject;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.inject.internal.MoreTypes;
/*     */ import com.google.inject.util.Types;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.util.List;
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
/*     */ public class TypeLiteral<T>
/*     */ {
/*     */   final Class<? super T> rawType;
/*     */   final Type type;
/*     */   final int hashCode;
/*     */   
/*     */   protected TypeLiteral() {
/*  75 */     this.type = getSuperclassTypeParameter(getClass());
/*  76 */     this.rawType = MoreTypes.getRawType(this.type);
/*  77 */     this.hashCode = this.type.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   TypeLiteral(Type type) {
/*  83 */     this.type = MoreTypes.canonicalize((Type)Preconditions.checkNotNull(type, "type"));
/*  84 */     this.rawType = MoreTypes.getRawType(this.type);
/*  85 */     this.hashCode = this.type.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Type getSuperclassTypeParameter(Class<?> subclass) {
/*  93 */     Type superclass = subclass.getGenericSuperclass();
/*  94 */     if (superclass instanceof Class) {
/*  95 */       throw new RuntimeException("Missing type parameter.");
/*     */     }
/*  97 */     ParameterizedType parameterized = (ParameterizedType)superclass;
/*  98 */     return MoreTypes.canonicalize(parameterized.getActualTypeArguments()[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   static TypeLiteral<?> fromSuperclassTypeParameter(Class<?> subclass) {
/* 103 */     return new TypeLiteral(getSuperclassTypeParameter(subclass));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Class<? super T> getRawType() {
/* 112 */     return this.rawType;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Type getType() {
/* 117 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final TypeLiteral<Provider<T>> providerType() {
/* 125 */     return (TypeLiteral)get(Types.providerOf(getType()));
/*     */   }
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/* 130 */     return this.hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean equals(Object o) {
/* 135 */     return (o instanceof TypeLiteral && MoreTypes.equals(this.type, ((TypeLiteral)o).type));
/*     */   }
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 140 */     return MoreTypes.typeToString(this.type);
/*     */   }
/*     */ 
/*     */   
/*     */   public static TypeLiteral<?> get(Type type) {
/* 145 */     return new TypeLiteral(type);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> TypeLiteral<T> get(Class<T> type) {
/* 150 */     return new TypeLiteral<>(type);
/*     */   }
/*     */ 
/*     */   
/*     */   private List<TypeLiteral<?>> resolveAll(Type[] types) {
/* 155 */     TypeLiteral[] arrayOfTypeLiteral = new TypeLiteral[types.length];
/* 156 */     for (int t = 0; t < types.length; t++) {
/* 157 */       arrayOfTypeLiteral[t] = resolve(types[t]);
/*     */     }
/* 159 */     return (List<TypeLiteral<?>>)ImmutableList.copyOf((Object[])arrayOfTypeLiteral);
/*     */   }
/*     */ 
/*     */   
/*     */   TypeLiteral<?> resolve(Type toResolve) {
/* 164 */     return get(resolveType(toResolve));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Type resolveType(Type toResolve) {
/* 170 */     while (toResolve instanceof TypeVariable) {
/* 171 */       TypeVariable<?> original = (TypeVariable)toResolve;
/* 172 */       toResolve = MoreTypes.resolveTypeVariable(this.type, this.rawType, original);
/* 173 */       if (toResolve == original) {
/* 174 */         return toResolve;
/*     */       }
/*     */     } 
/* 177 */     if (toResolve instanceof GenericArrayType) {
/* 178 */       GenericArrayType original = (GenericArrayType)toResolve;
/* 179 */       Type componentType = original.getGenericComponentType();
/* 180 */       Type newComponentType = resolveType(componentType);
/* 181 */       return (componentType == newComponentType) ? original : Types.arrayOf(newComponentType);
/*     */     } 
/* 183 */     if (toResolve instanceof ParameterizedType) {
/* 184 */       ParameterizedType original = (ParameterizedType)toResolve;
/* 185 */       Type ownerType = original.getOwnerType();
/* 186 */       Type newOwnerType = resolveType(ownerType);
/* 187 */       boolean changed = (newOwnerType != ownerType);
/*     */       
/* 189 */       Type[] args = original.getActualTypeArguments();
/* 190 */       for (int t = 0, length = args.length; t < length; t++) {
/* 191 */         Type resolvedTypeArgument = resolveType(args[t]);
/* 192 */         if (resolvedTypeArgument != args[t]) {
/* 193 */           if (!changed) {
/* 194 */             args = (Type[])args.clone();
/* 195 */             changed = true;
/*     */           } 
/* 197 */           args[t] = resolvedTypeArgument;
/*     */         } 
/*     */       } 
/*     */       
/* 201 */       return changed ? 
/* 202 */         Types.newParameterizedTypeWithOwner(newOwnerType, original.getRawType(), args) : 
/* 203 */         original;
/*     */     } 
/* 205 */     if (toResolve instanceof WildcardType) {
/* 206 */       WildcardType original = (WildcardType)toResolve;
/* 207 */       Type[] originalLowerBound = original.getLowerBounds();
/* 208 */       Type[] originalUpperBound = original.getUpperBounds();
/*     */       
/* 210 */       if (originalLowerBound.length == 1) {
/* 211 */         Type lowerBound = resolveType(originalLowerBound[0]);
/* 212 */         if (lowerBound != originalLowerBound[0]) {
/* 213 */           return Types.supertypeOf(lowerBound);
/*     */         }
/* 215 */       } else if (originalUpperBound.length == 1) {
/* 216 */         Type upperBound = resolveType(originalUpperBound[0]);
/* 217 */         if (upperBound != originalUpperBound[0]) {
/* 218 */           return Types.subtypeOf(upperBound);
/*     */         }
/*     */       } 
/* 221 */       return original;
/*     */     } 
/*     */     
/* 224 */     return toResolve;
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
/*     */   public TypeLiteral<?> getSupertype(Class<?> supertype) {
/* 238 */     Preconditions.checkArgument(supertype
/* 239 */         .isAssignableFrom(this.rawType), "%s is not a supertype of %s", supertype, this.type);
/* 240 */     return resolve(MoreTypes.getGenericSupertype(this.type, this.rawType, supertype));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeLiteral<?> getFieldType(Field field) {
/* 250 */     Preconditions.checkArgument(field
/* 251 */         .getDeclaringClass().isAssignableFrom(this.rawType), "%s is not defined by a supertype of %s", field, this.type);
/*     */ 
/*     */ 
/*     */     
/* 255 */     return resolve(field.getGenericType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<TypeLiteral<?>> getParameterTypes(Member methodOrConstructor) {
/*     */     Type[] genericParameterTypes;
/* 267 */     if (methodOrConstructor instanceof Method) {
/* 268 */       Method method = (Method)methodOrConstructor;
/* 269 */       Preconditions.checkArgument(method
/* 270 */           .getDeclaringClass().isAssignableFrom(this.rawType), "%s is not defined by a supertype of %s", method, this.type);
/*     */ 
/*     */ 
/*     */       
/* 274 */       genericParameterTypes = method.getGenericParameterTypes();
/*     */     }
/* 276 */     else if (methodOrConstructor instanceof Constructor) {
/* 277 */       Constructor<?> constructor = (Constructor)methodOrConstructor;
/* 278 */       Preconditions.checkArgument(constructor
/* 279 */           .getDeclaringClass().isAssignableFrom(this.rawType), "%s does not construct a supertype of %s", constructor, this.type);
/*     */ 
/*     */ 
/*     */       
/* 283 */       genericParameterTypes = constructor.getGenericParameterTypes();
/*     */     } else {
/*     */       
/* 286 */       String str = String.valueOf(methodOrConstructor); throw new IllegalArgumentException((new StringBuilder(31 + String.valueOf(str).length())).append("Not a method or a constructor: ").append(str).toString());
/*     */     } 
/*     */     
/* 289 */     return resolveAll(genericParameterTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<TypeLiteral<?>> getExceptionTypes(Member methodOrConstructor) {
/*     */     Type[] genericExceptionTypes;
/* 301 */     if (methodOrConstructor instanceof Method) {
/* 302 */       Method method = (Method)methodOrConstructor;
/* 303 */       Preconditions.checkArgument(method
/* 304 */           .getDeclaringClass().isAssignableFrom(this.rawType), "%s is not defined by a supertype of %s", method, this.type);
/*     */ 
/*     */ 
/*     */       
/* 308 */       genericExceptionTypes = method.getGenericExceptionTypes();
/*     */     }
/* 310 */     else if (methodOrConstructor instanceof Constructor) {
/* 311 */       Constructor<?> constructor = (Constructor)methodOrConstructor;
/* 312 */       Preconditions.checkArgument(constructor
/* 313 */           .getDeclaringClass().isAssignableFrom(this.rawType), "%s does not construct a supertype of %s", constructor, this.type);
/*     */ 
/*     */ 
/*     */       
/* 317 */       genericExceptionTypes = constructor.getGenericExceptionTypes();
/*     */     } else {
/*     */       
/* 320 */       String str = String.valueOf(methodOrConstructor); throw new IllegalArgumentException((new StringBuilder(31 + String.valueOf(str).length())).append("Not a method or a constructor: ").append(str).toString());
/*     */     } 
/*     */     
/* 323 */     return resolveAll(genericExceptionTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeLiteral<?> getReturnType(Method method) {
/* 333 */     Preconditions.checkArgument(method
/* 334 */         .getDeclaringClass().isAssignableFrom(this.rawType), "%s is not defined by a supertype of %s", method, this.type);
/*     */ 
/*     */ 
/*     */     
/* 338 */     return resolve(method.getGenericReturnType());
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\TypeLiteral.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */