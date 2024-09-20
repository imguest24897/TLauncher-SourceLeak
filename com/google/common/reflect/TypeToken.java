/*      */ package com.google.common.reflect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Joiner;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.collect.FluentIterable;
/*      */ import com.google.common.collect.ForwardingSet;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.google.common.collect.ImmutableSet;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Ordering;
/*      */ import com.google.common.collect.UnmodifiableIterator;
/*      */ import com.google.common.primitives.Primitives;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.GenericArrayType;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.lang.reflect.ParameterizedType;
/*      */ import java.lang.reflect.Type;
/*      */ import java.lang.reflect.TypeVariable;
/*      */ import java.lang.reflect.WildcardType;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Beta
/*      */ public abstract class TypeToken<T>
/*      */   extends TypeCapture<T>
/*      */   implements Serializable
/*      */ {
/*      */   private final Type runtimeType;
/*      */   private transient TypeResolver invariantTypeResolver;
/*      */   private transient TypeResolver covariantTypeResolver;
/*      */   private static final long serialVersionUID = 3637540370352322684L;
/*      */   
/*      */   protected TypeToken() {
/*  124 */     this.runtimeType = capture();
/*  125 */     Preconditions.checkState(!(this.runtimeType instanceof TypeVariable), "Cannot construct a TypeToken for a type variable.\nYou probably meant to call new TypeToken<%s>(getClass()) that can resolve the type variable for you.\nIf you do need to create a TypeToken of a type variable, please use TypeToken.of() instead.", this.runtimeType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected TypeToken(Class<?> declaringClass) {
/*  155 */     Type captured = capture();
/*  156 */     if (captured instanceof Class) {
/*  157 */       this.runtimeType = captured;
/*      */     } else {
/*  159 */       this.runtimeType = TypeResolver.covariantly(declaringClass).resolveType(captured);
/*      */     } 
/*      */   }
/*      */   
/*      */   private TypeToken(Type type) {
/*  164 */     this.runtimeType = (Type)Preconditions.checkNotNull(type);
/*      */   }
/*      */ 
/*      */   
/*      */   public static <T> TypeToken<T> of(Class<T> type) {
/*  169 */     return new SimpleTypeToken<>(type);
/*      */   }
/*      */ 
/*      */   
/*      */   public static TypeToken<?> of(Type type) {
/*  174 */     return new SimpleTypeToken(type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Class<? super T> getRawType() {
/*  194 */     Class<?> rawType = (Class)getRawTypes().iterator().next();
/*      */     
/*  196 */     Class<? super T> result = (Class)rawType;
/*  197 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Type getType() {
/*  202 */     return this.runtimeType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final <X> TypeToken<T> where(TypeParameter<X> typeParam, TypeToken<X> typeArg) {
/*  226 */     TypeResolver resolver = (new TypeResolver()).where(
/*  227 */         (Map<TypeResolver.TypeVariableKey, ? extends Type>)ImmutableMap.of(new TypeResolver.TypeVariableKey(typeParam.typeVariable), typeArg.runtimeType));
/*      */ 
/*      */     
/*  230 */     return new SimpleTypeToken<>(resolver.resolveType(this.runtimeType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final <X> TypeToken<T> where(TypeParameter<X> typeParam, Class<X> typeArg) {
/*  252 */     return where(typeParam, of(typeArg));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeToken<?> resolveType(Type type) {
/*  265 */     Preconditions.checkNotNull(type);
/*      */ 
/*      */     
/*  268 */     return of(getInvariantTypeResolver().resolveType(type));
/*      */   }
/*      */   
/*      */   private TypeToken<?> resolveSupertype(Type type) {
/*  272 */     TypeToken<?> supertype = of(getCovariantTypeResolver().resolveType(type));
/*      */     
/*  274 */     supertype.covariantTypeResolver = this.covariantTypeResolver;
/*  275 */     supertype.invariantTypeResolver = this.invariantTypeResolver;
/*  276 */     return supertype;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final TypeToken<? super T> getGenericSuperclass() {
/*  292 */     if (this.runtimeType instanceof TypeVariable)
/*      */     {
/*  294 */       return boundAsSuperclass(((TypeVariable)this.runtimeType).getBounds()[0]);
/*      */     }
/*  296 */     if (this.runtimeType instanceof WildcardType)
/*      */     {
/*  298 */       return boundAsSuperclass(((WildcardType)this.runtimeType).getUpperBounds()[0]);
/*      */     }
/*  300 */     Type superclass = getRawType().getGenericSuperclass();
/*  301 */     if (superclass == null) {
/*  302 */       return null;
/*      */     }
/*      */     
/*  305 */     TypeToken<? super T> superToken = (TypeToken)resolveSupertype(superclass);
/*  306 */     return superToken;
/*      */   }
/*      */   
/*      */   private TypeToken<? super T> boundAsSuperclass(Type bound) {
/*  310 */     TypeToken<?> token = of(bound);
/*  311 */     if (token.getRawType().isInterface()) {
/*  312 */       return null;
/*      */     }
/*      */     
/*  315 */     TypeToken<? super T> superclass = (TypeToken)token;
/*  316 */     return superclass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final ImmutableList<TypeToken<? super T>> getGenericInterfaces() {
/*  332 */     if (this.runtimeType instanceof TypeVariable) {
/*  333 */       return boundsAsInterfaces(((TypeVariable)this.runtimeType).getBounds());
/*      */     }
/*  335 */     if (this.runtimeType instanceof WildcardType) {
/*  336 */       return boundsAsInterfaces(((WildcardType)this.runtimeType).getUpperBounds());
/*      */     }
/*  338 */     ImmutableList.Builder<TypeToken<? super T>> builder = ImmutableList.builder();
/*  339 */     for (Type interfaceType : getRawType().getGenericInterfaces()) {
/*      */ 
/*      */       
/*  342 */       TypeToken<? super T> resolvedInterface = (TypeToken)resolveSupertype(interfaceType);
/*  343 */       builder.add(resolvedInterface);
/*      */     } 
/*  345 */     return builder.build();
/*      */   }
/*      */   
/*      */   private ImmutableList<TypeToken<? super T>> boundsAsInterfaces(Type[] bounds) {
/*  349 */     ImmutableList.Builder<TypeToken<? super T>> builder = ImmutableList.builder();
/*  350 */     for (Type bound : bounds) {
/*      */       
/*  352 */       TypeToken<? super T> boundType = (TypeToken)of(bound);
/*  353 */       if (boundType.getRawType().isInterface()) {
/*  354 */         builder.add(boundType);
/*      */       }
/*      */     } 
/*  357 */     return builder.build();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeSet getTypes() {
/*  372 */     return new TypeSet();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeToken<? super T> getSupertype(Class<? super T> superclass) {
/*  381 */     Preconditions.checkArgument(
/*  382 */         someRawTypeIsSubclassOf(superclass), "%s is not a super class of %s", superclass, this);
/*      */ 
/*      */ 
/*      */     
/*  386 */     if (this.runtimeType instanceof TypeVariable) {
/*  387 */       return getSupertypeFromUpperBounds(superclass, ((TypeVariable)this.runtimeType).getBounds());
/*      */     }
/*  389 */     if (this.runtimeType instanceof WildcardType) {
/*  390 */       return getSupertypeFromUpperBounds(superclass, ((WildcardType)this.runtimeType).getUpperBounds());
/*      */     }
/*  392 */     if (superclass.isArray()) {
/*  393 */       return getArraySupertype(superclass);
/*      */     }
/*      */ 
/*      */     
/*  397 */     TypeToken<? super T> supertype = (TypeToken)resolveSupertype((toGenericType((Class)superclass)).runtimeType);
/*  398 */     return supertype;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeToken<? extends T> getSubtype(Class<?> subclass) {
/*  407 */     Preconditions.checkArgument(!(this.runtimeType instanceof TypeVariable), "Cannot get subtype of type variable <%s>", this);
/*      */     
/*  409 */     if (this.runtimeType instanceof WildcardType) {
/*  410 */       return getSubtypeFromLowerBounds(subclass, ((WildcardType)this.runtimeType).getLowerBounds());
/*      */     }
/*      */     
/*  413 */     if (isArray()) {
/*  414 */       return getArraySubtype(subclass);
/*      */     }
/*      */     
/*  417 */     Preconditions.checkArgument(
/*  418 */         getRawType().isAssignableFrom(subclass), "%s isn't a subclass of %s", subclass, this);
/*  419 */     Type resolvedTypeArgs = resolveTypeArgsForSubclass(subclass);
/*      */     
/*  421 */     TypeToken<? extends T> subtype = (TypeToken)of(resolvedTypeArgs);
/*  422 */     Preconditions.checkArgument(subtype
/*  423 */         .isSubtypeOf(this), "%s does not appear to be a subtype of %s", subtype, this);
/*  424 */     return subtype;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSupertypeOf(TypeToken<?> type) {
/*  436 */     return type.isSubtypeOf(getType());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSupertypeOf(Type type) {
/*  448 */     return of(type).isSubtypeOf(getType());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSubtypeOf(TypeToken<?> type) {
/*  460 */     return isSubtypeOf(type.getType());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSubtypeOf(Type supertype) {
/*  472 */     Preconditions.checkNotNull(supertype);
/*  473 */     if (supertype instanceof WildcardType)
/*      */     {
/*      */ 
/*      */       
/*  477 */       return any(((WildcardType)supertype).getLowerBounds()).isSupertypeOf(this.runtimeType);
/*      */     }
/*      */ 
/*      */     
/*  481 */     if (this.runtimeType instanceof WildcardType)
/*      */     {
/*  483 */       return any(((WildcardType)this.runtimeType).getUpperBounds()).isSubtypeOf(supertype);
/*      */     }
/*      */ 
/*      */     
/*  487 */     if (this.runtimeType instanceof TypeVariable) {
/*  488 */       return (this.runtimeType.equals(supertype) || 
/*  489 */         any(((TypeVariable)this.runtimeType).getBounds()).isSubtypeOf(supertype));
/*      */     }
/*  491 */     if (this.runtimeType instanceof GenericArrayType) {
/*  492 */       return of(supertype).isSupertypeOfArray((GenericArrayType)this.runtimeType);
/*      */     }
/*      */     
/*  495 */     if (supertype instanceof Class)
/*  496 */       return someRawTypeIsSubclassOf((Class)supertype); 
/*  497 */     if (supertype instanceof ParameterizedType)
/*  498 */       return isSubtypeOfParameterizedType((ParameterizedType)supertype); 
/*  499 */     if (supertype instanceof GenericArrayType) {
/*  500 */       return isSubtypeOfArrayType((GenericArrayType)supertype);
/*      */     }
/*  502 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isArray() {
/*  511 */     return (getComponentType() != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isPrimitive() {
/*  520 */     return (this.runtimeType instanceof Class && ((Class)this.runtimeType).isPrimitive());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeToken<T> wrap() {
/*  530 */     if (isPrimitive()) {
/*      */       
/*  532 */       Class<T> type = (Class<T>)this.runtimeType;
/*  533 */       return of(Primitives.wrap(type));
/*      */     } 
/*  535 */     return this;
/*      */   }
/*      */   
/*      */   private boolean isWrapper() {
/*  539 */     return Primitives.allWrapperTypes().contains(this.runtimeType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeToken<T> unwrap() {
/*  549 */     if (isWrapper()) {
/*      */       
/*  551 */       Class<T> type = (Class<T>)this.runtimeType;
/*  552 */       return of(Primitives.unwrap(type));
/*      */     } 
/*  554 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeToken<?> getComponentType() {
/*  562 */     Type componentType = Types.getComponentType(this.runtimeType);
/*  563 */     if (componentType == null) {
/*  564 */       return null;
/*      */     }
/*  566 */     return of(componentType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Invokable<T, Object> method(Method method) {
/*  575 */     Preconditions.checkArgument(
/*  576 */         someRawTypeIsSubclassOf(method.getDeclaringClass()), "%s not declared by %s", method, this);
/*      */ 
/*      */ 
/*      */     
/*  580 */     return new Invokable.MethodInvokable<T>(method)
/*      */       {
/*      */         Type getGenericReturnType() {
/*  583 */           return TypeToken.this.getCovariantTypeResolver().resolveType(super.getGenericReturnType());
/*      */         }
/*      */ 
/*      */         
/*      */         Type[] getGenericParameterTypes() {
/*  588 */           return TypeToken.this.getInvariantTypeResolver().resolveTypesInPlace(super.getGenericParameterTypes());
/*      */         }
/*      */ 
/*      */         
/*      */         Type[] getGenericExceptionTypes() {
/*  593 */           return TypeToken.this.getCovariantTypeResolver().resolveTypesInPlace(super.getGenericExceptionTypes());
/*      */         }
/*      */ 
/*      */         
/*      */         public TypeToken<T> getOwnerType() {
/*  598 */           return TypeToken.this;
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/*  603 */           String str1 = String.valueOf(getOwnerType()), str2 = super.toString(); return (new StringBuilder(1 + String.valueOf(str1).length() + String.valueOf(str2).length())).append(str1).append(".").append(str2).toString();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Invokable<T, T> constructor(Constructor<?> constructor) {
/*  614 */     Preconditions.checkArgument(
/*  615 */         (constructor.getDeclaringClass() == getRawType()), "%s not declared by %s", constructor, 
/*      */ 
/*      */         
/*  618 */         getRawType());
/*  619 */     return new Invokable.ConstructorInvokable<T>(constructor)
/*      */       {
/*      */         Type getGenericReturnType() {
/*  622 */           return TypeToken.this.getCovariantTypeResolver().resolveType(super.getGenericReturnType());
/*      */         }
/*      */ 
/*      */         
/*      */         Type[] getGenericParameterTypes() {
/*  627 */           return TypeToken.this.getInvariantTypeResolver().resolveTypesInPlace(super.getGenericParameterTypes());
/*      */         }
/*      */ 
/*      */         
/*      */         Type[] getGenericExceptionTypes() {
/*  632 */           return TypeToken.this.getCovariantTypeResolver().resolveTypesInPlace(super.getGenericExceptionTypes());
/*      */         }
/*      */ 
/*      */         
/*      */         public TypeToken<T> getOwnerType() {
/*  637 */           return TypeToken.this;
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/*  642 */           String str1 = String.valueOf(getOwnerType()), str2 = Joiner.on(", ").join((Object[])getGenericParameterTypes()); return (new StringBuilder(2 + String.valueOf(str1).length() + String.valueOf(str2).length())).append(str1).append("(").append(str2).append(")").toString();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public class TypeSet
/*      */     extends ForwardingSet<TypeToken<? super T>>
/*      */     implements Serializable
/*      */   {
/*      */     private transient ImmutableSet<TypeToken<? super T>> types;
/*      */ 
/*      */     
/*      */     private static final long serialVersionUID = 0L;
/*      */ 
/*      */ 
/*      */     
/*      */     public TypeSet interfaces() {
/*  661 */       return new TypeToken.InterfaceSet(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public TypeSet classes() {
/*  666 */       return new TypeToken.ClassSet();
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<TypeToken<? super T>> delegate() {
/*  671 */       ImmutableSet<TypeToken<? super T>> filteredTypes = this.types;
/*  672 */       if (filteredTypes == null) {
/*      */ 
/*      */ 
/*      */         
/*  676 */         ImmutableList<TypeToken<? super T>> collectedTypes = (ImmutableList)TypeToken.TypeCollector.FOR_GENERIC_TYPE.collectTypes(TypeToken.this);
/*  677 */         return 
/*      */ 
/*      */           
/*  680 */           (Set<TypeToken<? super T>>)(this.types = FluentIterable.from((Iterable)collectedTypes).filter(TypeToken.TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet());
/*      */       } 
/*  682 */       return (Set<TypeToken<? super T>>)filteredTypes;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Class<? super T>> rawTypes() {
/*  691 */       ImmutableList<Class<? super T>> collectedTypes = (ImmutableList)TypeToken.TypeCollector.FOR_RAW_TYPE.collectTypes((Iterable<? extends Class<?>>)TypeToken.this.getRawTypes());
/*  692 */       return (Set<Class<? super T>>)ImmutableSet.copyOf((Collection)collectedTypes);
/*      */     }
/*      */   }
/*      */   
/*      */   private final class InterfaceSet
/*      */     extends TypeSet
/*      */   {
/*      */     private final transient TypeToken<T>.TypeSet allTypes;
/*      */     private transient ImmutableSet<TypeToken<? super T>> interfaces;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     InterfaceSet(TypeToken<T>.TypeSet allTypes) {
/*  704 */       this.allTypes = allTypes;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<TypeToken<? super T>> delegate() {
/*  709 */       ImmutableSet<TypeToken<? super T>> result = this.interfaces;
/*  710 */       if (result == null) {
/*  711 */         return 
/*  712 */           (Set<TypeToken<? super T>>)(this.interfaces = FluentIterable.from((Iterable)this.allTypes).filter(TypeToken.TypeFilter.INTERFACE_ONLY).toSet());
/*      */       }
/*  714 */       return (Set<TypeToken<? super T>>)result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public TypeToken<T>.TypeSet interfaces() {
/*  720 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Class<? super T>> rawTypes() {
/*  728 */       ImmutableList<Class<? super T>> collectedTypes = (ImmutableList)TypeToken.TypeCollector.FOR_RAW_TYPE.collectTypes((Iterable<? extends Class<?>>)TypeToken.this.getRawTypes());
/*  729 */       return (Set<Class<? super T>>)FluentIterable.from((Iterable)collectedTypes)
/*  730 */         .filter(new Predicate<Class<?>>(this)
/*      */           {
/*      */             public boolean apply(Class<?> type)
/*      */             {
/*  734 */               return type.isInterface();
/*      */             }
/*  737 */           }).toSet();
/*      */     }
/*      */ 
/*      */     
/*      */     public TypeToken<T>.TypeSet classes() {
/*  742 */       throw new UnsupportedOperationException("interfaces().classes() not supported.");
/*      */     }
/*      */     
/*      */     private Object readResolve() {
/*  746 */       return TypeToken.this.getTypes().interfaces();
/*      */     }
/*      */   }
/*      */   
/*      */   private final class ClassSet
/*      */     extends TypeSet {
/*      */     private transient ImmutableSet<TypeToken<? super T>> classes;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     private ClassSet() {}
/*      */     
/*      */     protected Set<TypeToken<? super T>> delegate() {
/*  758 */       ImmutableSet<TypeToken<? super T>> result = this.classes;
/*  759 */       if (result == null) {
/*      */ 
/*      */ 
/*      */         
/*  763 */         ImmutableList<TypeToken<? super T>> collectedTypes = TypeToken.TypeCollector.FOR_GENERIC_TYPE.classesOnly().collectTypes(TypeToken.this);
/*  764 */         return 
/*      */ 
/*      */           
/*  767 */           (Set<TypeToken<? super T>>)(this.classes = FluentIterable.from((Iterable)collectedTypes).filter(TypeToken.TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet());
/*      */       } 
/*  769 */       return (Set<TypeToken<? super T>>)result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public TypeToken<T>.TypeSet classes() {
/*  775 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Class<? super T>> rawTypes() {
/*  783 */       ImmutableList<Class<? super T>> collectedTypes = TypeToken.TypeCollector.FOR_RAW_TYPE.classesOnly().collectTypes((Iterable<? extends Class<? super T>>)TypeToken.this.getRawTypes());
/*  784 */       return (Set<Class<? super T>>)ImmutableSet.copyOf((Collection)collectedTypes);
/*      */     }
/*      */ 
/*      */     
/*      */     public TypeToken<T>.TypeSet interfaces() {
/*  789 */       throw new UnsupportedOperationException("classes().interfaces() not supported.");
/*      */     }
/*      */     
/*      */     private Object readResolve() {
/*  793 */       return TypeToken.this.getTypes().classes();
/*      */     }
/*      */   }
/*      */   
/*      */   private enum TypeFilter
/*      */     implements Predicate<TypeToken<?>>
/*      */   {
/*  800 */     IGNORE_TYPE_VARIABLE_OR_WILDCARD
/*      */     {
/*      */       public boolean apply(TypeToken<?> type) {
/*  803 */         return (!(type.runtimeType instanceof TypeVariable) && 
/*  804 */           !(type.runtimeType instanceof WildcardType));
/*      */       }
/*      */     },
/*  807 */     INTERFACE_ONLY
/*      */     {
/*      */       public boolean apply(TypeToken<?> type) {
/*  810 */         return type.getRawType().isInterface();
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object o) {
/*  820 */     if (o instanceof TypeToken) {
/*  821 */       TypeToken<?> that = (TypeToken)o;
/*  822 */       return this.runtimeType.equals(that.runtimeType);
/*      */     } 
/*  824 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  829 */     return this.runtimeType.hashCode();
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/*  834 */     return Types.toString(this.runtimeType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object writeReplace() {
/*  841 */     return of((new TypeResolver()).resolveType(this.runtimeType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   final TypeToken<T> rejectTypeVariables() {
/*  850 */     (new TypeVisitor()
/*      */       {
/*      */         void visitTypeVariable(TypeVariable<?> type)
/*      */         {
/*  854 */           String str = String.valueOf(TypeToken.this.runtimeType); throw new IllegalArgumentException((new StringBuilder(58 + String.valueOf(str).length())).append(str).append("contains a type variable and is not safe for the operation").toString());
/*      */         }
/*      */ 
/*      */         
/*      */         void visitWildcardType(WildcardType type) {
/*  859 */           visit(type.getLowerBounds());
/*  860 */           visit(type.getUpperBounds());
/*      */         }
/*      */ 
/*      */         
/*      */         void visitParameterizedType(ParameterizedType type) {
/*  865 */           visit(type.getActualTypeArguments());
/*  866 */           visit(new Type[] { type.getOwnerType() });
/*      */         }
/*      */ 
/*      */         
/*      */         void visitGenericArrayType(GenericArrayType type) {
/*  871 */           visit(new Type[] { type.getGenericComponentType() });
/*      */         }
/*  873 */       }).visit(new Type[] { this.runtimeType });
/*  874 */     return this;
/*      */   }
/*      */   
/*      */   private boolean someRawTypeIsSubclassOf(Class<?> superclass) {
/*  878 */     for (UnmodifiableIterator<Class<?>> unmodifiableIterator = getRawTypes().iterator(); unmodifiableIterator.hasNext(); ) { Class<?> rawType = unmodifiableIterator.next();
/*  879 */       if (superclass.isAssignableFrom(rawType)) {
/*  880 */         return true;
/*      */       } }
/*      */     
/*  883 */     return false;
/*      */   }
/*      */   
/*      */   private boolean isSubtypeOfParameterizedType(ParameterizedType supertype) {
/*  887 */     Class<?> matchedClass = of(supertype).getRawType();
/*  888 */     if (!someRawTypeIsSubclassOf(matchedClass)) {
/*  889 */       return false;
/*      */     }
/*  891 */     TypeVariable[] arrayOfTypeVariable = (TypeVariable[])matchedClass.getTypeParameters();
/*  892 */     Type[] supertypeArgs = supertype.getActualTypeArguments();
/*  893 */     for (int i = 0; i < arrayOfTypeVariable.length; i++) {
/*  894 */       Type subtypeParam = getCovariantTypeResolver().resolveType(arrayOfTypeVariable[i]);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  899 */       if (!of(subtypeParam).is(supertypeArgs[i], arrayOfTypeVariable[i])) {
/*  900 */         return false;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  906 */     return (Modifier.isStatic(((Class)supertype.getRawType()).getModifiers()) || supertype
/*  907 */       .getOwnerType() == null || 
/*  908 */       isOwnedBySubtypeOf(supertype.getOwnerType()));
/*      */   }
/*      */   
/*      */   private boolean isSubtypeOfArrayType(GenericArrayType supertype) {
/*  912 */     if (this.runtimeType instanceof Class) {
/*  913 */       Class<?> fromClass = (Class)this.runtimeType;
/*  914 */       if (!fromClass.isArray()) {
/*  915 */         return false;
/*      */       }
/*  917 */       return of(fromClass.getComponentType()).isSubtypeOf(supertype.getGenericComponentType());
/*  918 */     }  if (this.runtimeType instanceof GenericArrayType) {
/*  919 */       GenericArrayType fromArrayType = (GenericArrayType)this.runtimeType;
/*  920 */       return of(fromArrayType.getGenericComponentType())
/*  921 */         .isSubtypeOf(supertype.getGenericComponentType());
/*      */     } 
/*  923 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isSupertypeOfArray(GenericArrayType subtype) {
/*  928 */     if (this.runtimeType instanceof Class) {
/*  929 */       Class<?> thisClass = (Class)this.runtimeType;
/*  930 */       if (!thisClass.isArray()) {
/*  931 */         return thisClass.isAssignableFrom(Object[].class);
/*      */       }
/*  933 */       return of(subtype.getGenericComponentType()).isSubtypeOf(thisClass.getComponentType());
/*  934 */     }  if (this.runtimeType instanceof GenericArrayType) {
/*  935 */       return of(subtype.getGenericComponentType())
/*  936 */         .isSubtypeOf(((GenericArrayType)this.runtimeType).getGenericComponentType());
/*      */     }
/*  938 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean is(Type formalType, TypeVariable<?> declaration) {
/*  969 */     if (this.runtimeType.equals(formalType)) {
/*  970 */       return true;
/*      */     }
/*  972 */     if (formalType instanceof WildcardType) {
/*  973 */       WildcardType your = canonicalizeWildcardType(declaration, (WildcardType)formalType);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  979 */       return (every(your.getUpperBounds()).isSupertypeOf(this.runtimeType) && 
/*  980 */         every(your.getLowerBounds()).isSubtypeOf(this.runtimeType));
/*      */     } 
/*  982 */     return canonicalizeWildcardsInType(this.runtimeType).equals(canonicalizeWildcardsInType(formalType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Type canonicalizeTypeArg(TypeVariable<?> declaration, Type typeArg) {
/* 1004 */     return (typeArg instanceof WildcardType) ? 
/* 1005 */       canonicalizeWildcardType(declaration, (WildcardType)typeArg) : 
/* 1006 */       canonicalizeWildcardsInType(typeArg);
/*      */   }
/*      */   
/*      */   private static Type canonicalizeWildcardsInType(Type type) {
/* 1010 */     if (type instanceof ParameterizedType) {
/* 1011 */       return canonicalizeWildcardsInParameterizedType((ParameterizedType)type);
/*      */     }
/* 1013 */     if (type instanceof GenericArrayType) {
/* 1014 */       return Types.newArrayType(
/* 1015 */           canonicalizeWildcardsInType(((GenericArrayType)type).getGenericComponentType()));
/*      */     }
/* 1017 */     return type;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static WildcardType canonicalizeWildcardType(TypeVariable<?> declaration, WildcardType type) {
/* 1025 */     Type[] declared = declaration.getBounds();
/* 1026 */     List<Type> upperBounds = new ArrayList<>();
/* 1027 */     for (Type bound : type.getUpperBounds()) {
/* 1028 */       if (!any(declared).isSubtypeOf(bound)) {
/* 1029 */         upperBounds.add(canonicalizeWildcardsInType(bound));
/*      */       }
/*      */     } 
/* 1032 */     return new Types.WildcardTypeImpl(type.getLowerBounds(), upperBounds.<Type>toArray(new Type[0]));
/*      */   }
/*      */ 
/*      */   
/*      */   private static ParameterizedType canonicalizeWildcardsInParameterizedType(ParameterizedType type) {
/* 1037 */     Class<?> rawType = (Class)type.getRawType();
/* 1038 */     TypeVariable[] arrayOfTypeVariable = (TypeVariable[])rawType.getTypeParameters();
/* 1039 */     Type[] typeArgs = type.getActualTypeArguments();
/* 1040 */     for (int i = 0; i < typeArgs.length; i++) {
/* 1041 */       typeArgs[i] = canonicalizeTypeArg(arrayOfTypeVariable[i], typeArgs[i]);
/*      */     }
/* 1043 */     return Types.newParameterizedTypeWithOwner(type.getOwnerType(), rawType, typeArgs);
/*      */   }
/*      */ 
/*      */   
/*      */   private static Bounds every(Type[] bounds) {
/* 1048 */     return new Bounds(bounds, false);
/*      */   }
/*      */ 
/*      */   
/*      */   private static Bounds any(Type[] bounds) {
/* 1053 */     return new Bounds(bounds, true);
/*      */   }
/*      */   
/*      */   private static class Bounds {
/*      */     private final Type[] bounds;
/*      */     private final boolean target;
/*      */     
/*      */     Bounds(Type[] bounds, boolean target) {
/* 1061 */       this.bounds = bounds;
/* 1062 */       this.target = target;
/*      */     }
/*      */     
/*      */     boolean isSubtypeOf(Type supertype) {
/* 1066 */       for (Type bound : this.bounds) {
/* 1067 */         if (TypeToken.of(bound).isSubtypeOf(supertype) == this.target) {
/* 1068 */           return this.target;
/*      */         }
/*      */       } 
/* 1071 */       return !this.target;
/*      */     }
/*      */     
/*      */     boolean isSupertypeOf(Type subtype) {
/* 1075 */       TypeToken<?> type = TypeToken.of(subtype);
/* 1076 */       for (Type bound : this.bounds) {
/* 1077 */         if (type.isSubtypeOf(bound) == this.target) {
/* 1078 */           return this.target;
/*      */         }
/*      */       } 
/* 1081 */       return !this.target;
/*      */     }
/*      */   }
/*      */   
/*      */   private ImmutableSet<Class<? super T>> getRawTypes() {
/* 1086 */     final ImmutableSet.Builder<Class<?>> builder = ImmutableSet.builder();
/* 1087 */     (new TypeVisitor(this)
/*      */       {
/*      */         void visitTypeVariable(TypeVariable<?> t) {
/* 1090 */           visit(t.getBounds());
/*      */         }
/*      */ 
/*      */         
/*      */         void visitWildcardType(WildcardType t) {
/* 1095 */           visit(t.getUpperBounds());
/*      */         }
/*      */ 
/*      */         
/*      */         void visitParameterizedType(ParameterizedType t) {
/* 1100 */           builder.add(t.getRawType());
/*      */         }
/*      */ 
/*      */         
/*      */         void visitClass(Class<?> t) {
/* 1105 */           builder.add(t);
/*      */         }
/*      */ 
/*      */         
/*      */         void visitGenericArrayType(GenericArrayType t) {
/* 1110 */           builder.add(Types.getArrayClass(TypeToken.of(t.getGenericComponentType()).getRawType()));
/*      */         }
/* 1112 */       }).visit(new Type[] { this.runtimeType });
/*      */ 
/*      */     
/* 1115 */     ImmutableSet<Class<? super T>> result = builder.build();
/* 1116 */     return result;
/*      */   }
/*      */   
/*      */   private boolean isOwnedBySubtypeOf(Type supertype) {
/* 1120 */     for (TypeToken<?> type : (Iterable<TypeToken<?>>)getTypes()) {
/* 1121 */       Type ownerType = type.getOwnerTypeIfPresent();
/* 1122 */       if (ownerType != null && of(ownerType).isSubtypeOf(supertype)) {
/* 1123 */         return true;
/*      */       }
/*      */     } 
/* 1126 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Type getOwnerTypeIfPresent() {
/* 1134 */     if (this.runtimeType instanceof ParameterizedType)
/* 1135 */       return ((ParameterizedType)this.runtimeType).getOwnerType(); 
/* 1136 */     if (this.runtimeType instanceof Class) {
/* 1137 */       return ((Class)this.runtimeType).getEnclosingClass();
/*      */     }
/* 1139 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   static <T> TypeToken<? extends T> toGenericType(Class<T> cls) {
/* 1152 */     if (cls.isArray()) {
/*      */       
/* 1154 */       Type arrayOfGenericType = Types.newArrayType(
/*      */           
/* 1156 */           (toGenericType((Class)cls.getComponentType())).runtimeType);
/*      */       
/* 1158 */       TypeToken<? extends T> result = (TypeToken)of(arrayOfGenericType);
/* 1159 */       return result;
/*      */     } 
/* 1161 */     TypeVariable[] arrayOfTypeVariable = (TypeVariable[])cls.getTypeParameters();
/*      */ 
/*      */ 
/*      */     
/* 1165 */     Type ownerType = (cls.isMemberClass() && !Modifier.isStatic(cls.getModifiers())) ? (toGenericType((Class)cls.getEnclosingClass())).runtimeType : null;
/*      */     
/* 1167 */     if (arrayOfTypeVariable.length > 0 || (ownerType != null && ownerType != cls.getEnclosingClass())) {
/*      */ 
/*      */ 
/*      */       
/* 1171 */       TypeToken<? extends T> type = (TypeToken)of(Types.newParameterizedTypeWithOwner(ownerType, cls, (Type[])arrayOfTypeVariable));
/* 1172 */       return type;
/*      */     } 
/* 1174 */     return of(cls);
/*      */   }
/*      */ 
/*      */   
/*      */   private TypeResolver getCovariantTypeResolver() {
/* 1179 */     TypeResolver resolver = this.covariantTypeResolver;
/* 1180 */     if (resolver == null) {
/* 1181 */       resolver = this.covariantTypeResolver = TypeResolver.covariantly(this.runtimeType);
/*      */     }
/* 1183 */     return resolver;
/*      */   }
/*      */   
/*      */   private TypeResolver getInvariantTypeResolver() {
/* 1187 */     TypeResolver resolver = this.invariantTypeResolver;
/* 1188 */     if (resolver == null) {
/* 1189 */       resolver = this.invariantTypeResolver = TypeResolver.invariantly(this.runtimeType);
/*      */     }
/* 1191 */     return resolver;
/*      */   }
/*      */ 
/*      */   
/*      */   private TypeToken<? super T> getSupertypeFromUpperBounds(Class<? super T> supertype, Type[] upperBounds) {
/* 1196 */     for (Type upperBound : upperBounds) {
/*      */       
/* 1198 */       TypeToken<? super T> bound = (TypeToken)of(upperBound);
/* 1199 */       if (bound.isSubtypeOf(supertype)) {
/*      */         
/* 1201 */         TypeToken<? super T> result = bound.getSupertype(supertype);
/* 1202 */         return result;
/*      */       } 
/*      */     } 
/* 1205 */     String str1 = String.valueOf(supertype), str2 = String.valueOf(this); throw new IllegalArgumentException((new StringBuilder(23 + String.valueOf(str1).length() + String.valueOf(str2).length())).append(str1).append(" isn't a super type of ").append(str2).toString());
/*      */   }
/*      */   
/*      */   private TypeToken<? extends T> getSubtypeFromLowerBounds(Class<?> subclass, Type[] lowerBounds) {
/* 1209 */     if (lowerBounds.length > 0) {
/*      */       
/* 1211 */       TypeToken<? extends T> bound = (TypeToken)of(lowerBounds[0]);
/*      */       
/* 1213 */       return bound.getSubtype(subclass);
/*      */     } 
/* 1215 */     String str1 = String.valueOf(subclass), str2 = String.valueOf(this); throw new IllegalArgumentException((new StringBuilder(21 + String.valueOf(str1).length() + String.valueOf(str2).length())).append(str1).append(" isn't a subclass of ").append(str2).toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private TypeToken<? super T> getArraySupertype(Class<? super T> supertype) {
/* 1223 */     TypeToken<?> componentType = (TypeToken)Preconditions.checkNotNull(getComponentType(), "%s isn't a super type of %s", supertype, this);
/*      */ 
/*      */     
/* 1226 */     TypeToken<?> componentSupertype = componentType.getSupertype(supertype.getComponentType());
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1231 */     TypeToken<? super T> result = (TypeToken)of(newArrayClassOrGenericArrayType(componentSupertype.runtimeType));
/* 1232 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   private TypeToken<? extends T> getArraySubtype(Class<?> subclass) {
/* 1237 */     TypeToken<?> componentSubtype = getComponentType().getSubtype(subclass.getComponentType());
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1242 */     TypeToken<? extends T> result = (TypeToken)of(newArrayClassOrGenericArrayType(componentSubtype.runtimeType));
/* 1243 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Type resolveTypeArgsForSubclass(Class<?> subclass) {
/* 1251 */     if (this.runtimeType instanceof Class && ((subclass
/* 1252 */       .getTypeParameters()).length == 0 || (
/* 1253 */       getRawType().getTypeParameters()).length != 0))
/*      */     {
/* 1255 */       return subclass;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1264 */     TypeToken<?> genericSubtype = toGenericType(subclass);
/*      */ 
/*      */     
/* 1267 */     Type supertypeWithArgsFromSubtype = (genericSubtype.getSupertype(getRawType())).runtimeType;
/* 1268 */     return (new TypeResolver())
/* 1269 */       .where(supertypeWithArgsFromSubtype, this.runtimeType)
/* 1270 */       .resolveType(genericSubtype.runtimeType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Type newArrayClassOrGenericArrayType(Type componentType) {
/* 1278 */     return Types.JavaVersion.JAVA7.newArrayType(componentType);
/*      */   }
/*      */   
/*      */   private static final class SimpleTypeToken<T> extends TypeToken<T> { private static final long serialVersionUID = 0L;
/*      */     
/*      */     SimpleTypeToken(Type type) {
/* 1284 */       super(type);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static abstract class TypeCollector<K>
/*      */   {
/*      */     private TypeCollector() {}
/*      */ 
/*      */ 
/*      */     
/* 1297 */     static final TypeCollector<TypeToken<?>> FOR_GENERIC_TYPE = new TypeCollector<TypeToken<?>>()
/*      */       {
/*      */         Class<?> getRawType(TypeToken<?> type)
/*      */         {
/* 1301 */           return type.getRawType();
/*      */         }
/*      */ 
/*      */         
/*      */         Iterable<? extends TypeToken<?>> getInterfaces(TypeToken<?> type) {
/* 1306 */           return (Iterable<? extends TypeToken<?>>)type.getGenericInterfaces();
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         TypeToken<?> getSuperclass(TypeToken<?> type) {
/* 1312 */           return type.getGenericSuperclass();
/*      */         }
/*      */       };
/*      */     
/* 1316 */     static final TypeCollector<Class<?>> FOR_RAW_TYPE = new TypeCollector<Class<?>>()
/*      */       {
/*      */         Class<?> getRawType(Class<?> type)
/*      */         {
/* 1320 */           return type;
/*      */         }
/*      */ 
/*      */         
/*      */         Iterable<? extends Class<?>> getInterfaces(Class<?> type) {
/* 1325 */           return Arrays.asList(type.getInterfaces());
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         Class<?> getSuperclass(Class<?> type) {
/* 1331 */           return type.getSuperclass();
/*      */         }
/*      */       };
/*      */ 
/*      */     
/*      */     final TypeCollector<K> classesOnly() {
/* 1337 */       return new ForwardingTypeCollector<K>(this, this)
/*      */         {
/*      */           Iterable<? extends K> getInterfaces(K type) {
/* 1340 */             return (Iterable<? extends K>)ImmutableSet.of();
/*      */           }
/*      */ 
/*      */           
/*      */           ImmutableList<K> collectTypes(Iterable<? extends K> types) {
/* 1345 */             ImmutableList.Builder<K> builder = ImmutableList.builder();
/* 1346 */             for (K type : types) {
/* 1347 */               if (!getRawType(type).isInterface()) {
/* 1348 */                 builder.add(type);
/*      */               }
/*      */             } 
/* 1351 */             return super.collectTypes((Iterable<? extends K>)builder.build());
/*      */           }
/*      */         };
/*      */     }
/*      */     
/*      */     final ImmutableList<K> collectTypes(K type) {
/* 1357 */       return collectTypes((Iterable<? extends K>)ImmutableList.of(type));
/*      */     }
/*      */ 
/*      */     
/*      */     ImmutableList<K> collectTypes(Iterable<? extends K> types) {
/* 1362 */       Map<K, Integer> map = Maps.newHashMap();
/* 1363 */       for (K type : types) {
/* 1364 */         collectTypes(type, map);
/*      */       }
/* 1366 */       return sortKeysByValue(map, (Comparator<? super Integer>)Ordering.natural().reverse());
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     private int collectTypes(K type, Map<? super K, Integer> map) {
/* 1372 */       Integer existing = map.get(type);
/* 1373 */       if (existing != null)
/*      */       {
/* 1375 */         return existing.intValue();
/*      */       }
/*      */       
/* 1378 */       int aboveMe = getRawType(type).isInterface() ? 1 : 0;
/* 1379 */       for (K interfaceType : getInterfaces(type)) {
/* 1380 */         aboveMe = Math.max(aboveMe, collectTypes(interfaceType, map));
/*      */       }
/* 1382 */       K superclass = getSuperclass(type);
/* 1383 */       if (superclass != null) {
/* 1384 */         aboveMe = Math.max(aboveMe, collectTypes(superclass, map));
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1391 */       map.put(type, Integer.valueOf(aboveMe + 1));
/* 1392 */       return aboveMe + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     private static <K, V> ImmutableList<K> sortKeysByValue(final Map<K, V> map, final Comparator<? super V> valueComparator) {
/* 1397 */       Ordering<K> keyOrdering = new Ordering<K>()
/*      */         {
/*      */           public int compare(K left, K right)
/*      */           {
/* 1401 */             return valueComparator.compare(map.get(left), map.get(right));
/*      */           }
/*      */         };
/* 1404 */       return keyOrdering.immutableSortedCopy(map.keySet());
/*      */     }
/*      */     
/*      */     abstract Class<?> getRawType(K param1K);
/*      */     
/*      */     abstract Iterable<? extends K> getInterfaces(K param1K);
/*      */     
/*      */     abstract K getSuperclass(K param1K);
/*      */     
/*      */     private static class ForwardingTypeCollector<K>
/*      */       extends TypeCollector<K> {
/*      */       private final TypeToken.TypeCollector<K> delegate;
/*      */       
/*      */       ForwardingTypeCollector(TypeToken.TypeCollector<K> delegate) {
/* 1418 */         this.delegate = delegate;
/*      */       }
/*      */ 
/*      */       
/*      */       Class<?> getRawType(K type) {
/* 1423 */         return this.delegate.getRawType(type);
/*      */       }
/*      */ 
/*      */       
/*      */       Iterable<? extends K> getInterfaces(K type) {
/* 1428 */         return this.delegate.getInterfaces(type);
/*      */       }
/*      */ 
/*      */       
/*      */       K getSuperclass(K type) {
/* 1433 */         return this.delegate.getSuperclass(type);
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\reflect\TypeToken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */