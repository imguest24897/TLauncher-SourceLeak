/*      */ package org.apache.commons.lang3.reflect;
/*      */ 
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.GenericArrayType;
/*      */ import java.lang.reflect.GenericDeclaration;
/*      */ import java.lang.reflect.ParameterizedType;
/*      */ import java.lang.reflect.Type;
/*      */ import java.lang.reflect.TypeVariable;
/*      */ import java.lang.reflect.WildcardType;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import org.apache.commons.lang3.ArrayUtils;
/*      */ import org.apache.commons.lang3.ClassUtils;
/*      */ import org.apache.commons.lang3.ObjectUtils;
/*      */ import org.apache.commons.lang3.Validate;
/*      */ import org.apache.commons.lang3.builder.Builder;
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
/*      */ public class TypeUtils
/*      */ {
/*      */   private static final class GenericArrayTypeImpl
/*      */     implements GenericArrayType
/*      */   {
/*      */     private final Type componentType;
/*      */     
/*      */     private GenericArrayTypeImpl(Type componentType) {
/*   63 */       this.componentType = componentType;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/*   71 */       return (obj == this || (obj instanceof GenericArrayType && TypeUtils.equals(this, (GenericArrayType)obj)));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Type getGenericComponentType() {
/*   79 */       return this.componentType;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*   87 */       int result = 1072;
/*   88 */       result |= this.componentType.hashCode();
/*   89 */       return result;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*   97 */       return TypeUtils.toString(this);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class ParameterizedTypeImpl
/*      */     implements ParameterizedType
/*      */   {
/*      */     private final Class<?> raw;
/*      */ 
/*      */     
/*      */     private final Type useOwner;
/*      */ 
/*      */     
/*      */     private final Type[] typeArguments;
/*      */ 
/*      */ 
/*      */     
/*      */     private ParameterizedTypeImpl(Class<?> rawClass, Type useOwner, Type[] typeArguments) {
/*  117 */       this.raw = rawClass;
/*  118 */       this.useOwner = useOwner;
/*  119 */       this.typeArguments = Arrays.<Type, Type>copyOf(typeArguments, typeArguments.length, Type[].class);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/*  127 */       return (obj == this || (obj instanceof ParameterizedType && TypeUtils.equals(this, (ParameterizedType)obj)));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Type[] getActualTypeArguments() {
/*  135 */       return (Type[])this.typeArguments.clone();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Type getOwnerType() {
/*  143 */       return this.useOwner;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Type getRawType() {
/*  151 */       return this.raw;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  159 */       int result = 1136;
/*  160 */       result |= this.raw.hashCode();
/*  161 */       result <<= 4;
/*  162 */       result |= Objects.hashCode(this.useOwner);
/*  163 */       result <<= 8;
/*  164 */       result |= Arrays.hashCode((Object[])this.typeArguments);
/*  165 */       return result;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  173 */       return TypeUtils.toString(this);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class WildcardTypeBuilder
/*      */     implements Builder<WildcardType>
/*      */   {
/*      */     private Type[] upperBounds;
/*      */ 
/*      */ 
/*      */     
/*      */     private Type[] lowerBounds;
/*      */ 
/*      */ 
/*      */     
/*      */     private WildcardTypeBuilder() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public WildcardType build() {
/*  196 */       return new TypeUtils.WildcardTypeImpl(this.upperBounds, this.lowerBounds);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public WildcardTypeBuilder withLowerBounds(Type... bounds) {
/*  205 */       this.lowerBounds = bounds;
/*  206 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public WildcardTypeBuilder withUpperBounds(Type... bounds) {
/*  215 */       this.upperBounds = bounds;
/*  216 */       return this;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class WildcardTypeImpl
/*      */     implements WildcardType
/*      */   {
/*      */     private final Type[] upperBounds;
/*      */ 
/*      */     
/*      */     private final Type[] lowerBounds;
/*      */ 
/*      */ 
/*      */     
/*      */     private WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
/*  234 */       this.upperBounds = (Type[])ObjectUtils.defaultIfNull(upperBounds, ArrayUtils.EMPTY_TYPE_ARRAY);
/*  235 */       this.lowerBounds = (Type[])ObjectUtils.defaultIfNull(lowerBounds, ArrayUtils.EMPTY_TYPE_ARRAY);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/*  243 */       return (obj == this || (obj instanceof WildcardType && TypeUtils.equals(this, (WildcardType)obj)));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Type[] getLowerBounds() {
/*  251 */       return (Type[])this.lowerBounds.clone();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Type[] getUpperBounds() {
/*  259 */       return (Type[])this.upperBounds.clone();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  267 */       int result = 18688;
/*  268 */       result |= Arrays.hashCode((Object[])this.upperBounds);
/*  269 */       result <<= 8;
/*  270 */       result |= Arrays.hashCode((Object[])this.lowerBounds);
/*  271 */       return result;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  279 */       return TypeUtils.toString(this);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  287 */   public static final WildcardType WILDCARD_ALL = wildcardType().withUpperBounds(new Type[] { Object.class }).build();
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
/*      */   private static <T> StringBuilder appendAllTo(StringBuilder builder, String sep, T... types) {
/*  300 */     Validate.notEmpty(Validate.noNullElements((Object[])types));
/*  301 */     if (types.length > 0) {
/*  302 */       builder.append(toString(types[0]));
/*  303 */       for (int i = 1; i < types.length; i++) {
/*  304 */         builder.append(sep).append(toString(types[i]));
/*      */       }
/*      */     } 
/*  307 */     return builder;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void appendRecursiveTypes(StringBuilder builder, int[] recursiveTypeIndexes, Type[] argumentTypes) {
/*  312 */     for (int i = 0; i < recursiveTypeIndexes.length; i++) {
/*  313 */       appendAllTo(builder.append('<'), ", ", new String[] { argumentTypes[i].toString() }).append('>');
/*      */     } 
/*      */     
/*  316 */     Type[] argumentsFiltered = (Type[])ArrayUtils.removeAll((Object[])argumentTypes, recursiveTypeIndexes);
/*      */     
/*  318 */     if (argumentsFiltered.length > 0) {
/*  319 */       appendAllTo(builder.append('<'), ", ", argumentsFiltered).append('>');
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String classToString(Class<?> cls) {
/*  331 */     if (cls.isArray()) {
/*  332 */       return toString(cls.getComponentType()) + "[]";
/*      */     }
/*      */     
/*  335 */     StringBuilder buf = new StringBuilder();
/*      */     
/*  337 */     if (cls.getEnclosingClass() != null) {
/*  338 */       buf.append(classToString(cls.getEnclosingClass())).append('.').append(cls.getSimpleName());
/*      */     } else {
/*  340 */       buf.append(cls.getName());
/*      */     } 
/*  342 */     if ((cls.getTypeParameters()).length > 0) {
/*  343 */       buf.append('<');
/*  344 */       appendAllTo(buf, ", ", (Object[])cls.getTypeParameters());
/*  345 */       buf.append('>');
/*      */     } 
/*  347 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean containsTypeVariables(Type type) {
/*  358 */     if (type instanceof TypeVariable) {
/*  359 */       return true;
/*      */     }
/*  361 */     if (type instanceof Class) {
/*  362 */       return ((((Class)type).getTypeParameters()).length > 0);
/*      */     }
/*  364 */     if (type instanceof ParameterizedType) {
/*  365 */       for (Type arg : ((ParameterizedType)type).getActualTypeArguments()) {
/*  366 */         if (containsTypeVariables(arg)) {
/*  367 */           return true;
/*      */         }
/*      */       } 
/*  370 */       return false;
/*      */     } 
/*  372 */     if (type instanceof WildcardType) {
/*  373 */       WildcardType wild = (WildcardType)type;
/*  374 */       return (containsTypeVariables(getImplicitLowerBounds(wild)[0]) || 
/*  375 */         containsTypeVariables(getImplicitUpperBounds(wild)[0]));
/*      */     } 
/*  377 */     if (type instanceof GenericArrayType) {
/*  378 */       return containsTypeVariables(((GenericArrayType)type).getGenericComponentType());
/*      */     }
/*  380 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean containsVariableTypeSameParametrizedTypeBound(TypeVariable<?> typeVariable, ParameterizedType parameterizedType) {
/*  385 */     return ArrayUtils.contains((Object[])typeVariable.getBounds(), parameterizedType);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Map<TypeVariable<?>, Type> determineTypeArguments(Class<?> cls, ParameterizedType superParameterizedType) {
/*  422 */     Objects.requireNonNull(cls, "cls");
/*  423 */     Objects.requireNonNull(superParameterizedType, "superParameterizedType");
/*      */     
/*  425 */     Class<?> superClass = getRawType(superParameterizedType);
/*      */ 
/*      */     
/*  428 */     if (!isAssignable(cls, superClass)) {
/*  429 */       return null;
/*      */     }
/*      */     
/*  432 */     if (cls.equals(superClass)) {
/*  433 */       return getTypeArguments(superParameterizedType, superClass, (Map<TypeVariable<?>, Type>)null);
/*      */     }
/*      */ 
/*      */     
/*  437 */     Type midType = getClosestParentType(cls, superClass);
/*      */ 
/*      */     
/*  440 */     if (midType instanceof Class) {
/*  441 */       return determineTypeArguments((Class)midType, superParameterizedType);
/*      */     }
/*      */     
/*  444 */     ParameterizedType midParameterizedType = (ParameterizedType)midType;
/*  445 */     Class<?> midClass = getRawType(midParameterizedType);
/*      */ 
/*      */     
/*  448 */     Map<TypeVariable<?>, Type> typeVarAssigns = determineTypeArguments(midClass, superParameterizedType);
/*      */     
/*  450 */     mapTypeVariablesToArguments(cls, midParameterizedType, typeVarAssigns);
/*      */     
/*  452 */     return typeVarAssigns;
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
/*      */   private static boolean equals(GenericArrayType genericArrayType, Type type) {
/*  464 */     return (type instanceof GenericArrayType && 
/*  465 */       equals(genericArrayType.getGenericComponentType(), ((GenericArrayType)type).getGenericComponentType()));
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
/*      */   private static boolean equals(ParameterizedType parameterizedType, Type type) {
/*  477 */     if (type instanceof ParameterizedType) {
/*  478 */       ParameterizedType other = (ParameterizedType)type;
/*  479 */       if (equals(parameterizedType.getRawType(), other.getRawType()) && 
/*  480 */         equals(parameterizedType.getOwnerType(), other.getOwnerType())) {
/*  481 */         return equals(parameterizedType.getActualTypeArguments(), other.getActualTypeArguments());
/*      */       }
/*      */     } 
/*  484 */     return false;
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
/*      */   public static boolean equals(Type type1, Type type2) {
/*  496 */     if (Objects.equals(type1, type2)) {
/*  497 */       return true;
/*      */     }
/*  499 */     if (type1 instanceof ParameterizedType) {
/*  500 */       return equals((ParameterizedType)type1, type2);
/*      */     }
/*  502 */     if (type1 instanceof GenericArrayType) {
/*  503 */       return equals((GenericArrayType)type1, type2);
/*      */     }
/*  505 */     if (type1 instanceof WildcardType) {
/*  506 */       return equals((WildcardType)type1, type2);
/*      */     }
/*  508 */     return false;
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
/*      */   private static boolean equals(Type[] type1, Type[] type2) {
/*  520 */     if (type1.length == type2.length) {
/*  521 */       for (int i = 0; i < type1.length; i++) {
/*  522 */         if (!equals(type1[i], type2[i])) {
/*  523 */           return false;
/*      */         }
/*      */       } 
/*  526 */       return true;
/*      */     } 
/*  528 */     return false;
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
/*      */   private static boolean equals(WildcardType wildcardType, Type type) {
/*  540 */     if (type instanceof WildcardType) {
/*  541 */       WildcardType other = (WildcardType)type;
/*  542 */       return (equals(getImplicitLowerBounds(wildcardType), getImplicitLowerBounds(other)) && 
/*  543 */         equals(getImplicitUpperBounds(wildcardType), getImplicitUpperBounds(other)));
/*      */     } 
/*  545 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Type[] extractTypeArgumentsFrom(Map<TypeVariable<?>, Type> mappings, TypeVariable<?>[] variables) {
/*  556 */     Type[] result = new Type[variables.length];
/*  557 */     int index = 0;
/*  558 */     for (TypeVariable<?> var : variables) {
/*  559 */       Validate.isTrue(mappings.containsKey(var), "missing argument mapping for %s", new Object[] { toString(var) });
/*  560 */       result[index++] = mappings.get(var);
/*      */     } 
/*  562 */     return result;
/*      */   }
/*      */   
/*      */   private static int[] findRecursiveTypes(ParameterizedType parameterizedType) {
/*  566 */     Type[] filteredArgumentTypes = Arrays.<Type>copyOf(parameterizedType.getActualTypeArguments(), (parameterizedType
/*  567 */         .getActualTypeArguments()).length);
/*  568 */     int[] indexesToRemove = new int[0];
/*  569 */     for (int i = 0; i < filteredArgumentTypes.length; i++) {
/*  570 */       if (filteredArgumentTypes[i] instanceof TypeVariable && containsVariableTypeSameParametrizedTypeBound((TypeVariable)filteredArgumentTypes[i], parameterizedType))
/*      */       {
/*  572 */         indexesToRemove = ArrayUtils.add(indexesToRemove, i);
/*      */       }
/*      */     } 
/*  575 */     return indexesToRemove;
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
/*      */   public static GenericArrayType genericArrayType(Type componentType) {
/*  587 */     return new GenericArrayTypeImpl(Objects.<Type>requireNonNull(componentType, "componentType"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String genericArrayTypeToString(GenericArrayType genericArrayType) {
/*  598 */     return String.format("%s[]", new Object[] { toString(genericArrayType.getGenericComponentType()) });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Type getArrayComponentType(Type type) {
/*  608 */     if (type instanceof Class) {
/*  609 */       Class<?> cls = (Class)type;
/*  610 */       return cls.isArray() ? cls.getComponentType() : null;
/*      */     } 
/*  612 */     if (type instanceof GenericArrayType) {
/*  613 */       return ((GenericArrayType)type).getGenericComponentType();
/*      */     }
/*  615 */     return null;
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
/*      */   private static Type getClosestParentType(Class<?> cls, Class<?> superClass) {
/*  628 */     if (superClass.isInterface()) {
/*      */       
/*  630 */       Type[] interfaceTypes = cls.getGenericInterfaces();
/*      */       
/*  632 */       Type genericInterface = null;
/*      */ 
/*      */       
/*  635 */       for (Type midType : interfaceTypes) {
/*      */         Class<?> midClass;
/*      */         
/*  638 */         if (midType instanceof ParameterizedType) {
/*  639 */           midClass = getRawType((ParameterizedType)midType);
/*  640 */         } else if (midType instanceof Class) {
/*  641 */           midClass = (Class)midType;
/*      */         } else {
/*  643 */           throw new IllegalStateException("Unexpected generic interface type found: " + midType);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  649 */         if (isAssignable(midClass, superClass) && 
/*  650 */           isAssignable(genericInterface, midClass)) {
/*  651 */           genericInterface = midType;
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*  656 */       if (genericInterface != null) {
/*  657 */         return genericInterface;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  663 */     return cls.getGenericSuperclass();
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
/*      */   public static Type[] getImplicitBounds(TypeVariable<?> typeVariable) {
/*  677 */     Objects.requireNonNull(typeVariable, "typeVariable");
/*  678 */     Type[] bounds = typeVariable.getBounds();
/*      */     
/*  680 */     (new Type[1])[0] = Object.class; return (bounds.length == 0) ? new Type[1] : normalizeUpperBounds(bounds);
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
/*      */   public static Type[] getImplicitLowerBounds(WildcardType wildcardType) {
/*  694 */     Objects.requireNonNull(wildcardType, "wildcardType");
/*  695 */     Type[] bounds = wildcardType.getLowerBounds();
/*      */     
/*  697 */     (new Type[1])[0] = null; return (bounds.length == 0) ? new Type[1] : bounds;
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
/*      */   public static Type[] getImplicitUpperBounds(WildcardType wildcardType) {
/*  712 */     Objects.requireNonNull(wildcardType, "wildcardType");
/*  713 */     Type[] bounds = wildcardType.getUpperBounds();
/*      */     
/*  715 */     (new Type[1])[0] = Object.class; return (bounds.length == 0) ? new Type[1] : normalizeUpperBounds(bounds);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Class<?> getRawType(ParameterizedType parameterizedType) {
/*  726 */     Type rawType = parameterizedType.getRawType();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  733 */     if (!(rawType instanceof Class)) {
/*  734 */       throw new IllegalStateException("Wait... What!? Type of rawType: " + rawType);
/*      */     }
/*      */     
/*  737 */     return (Class)rawType;
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
/*      */   public static Class<?> getRawType(Type type, Type assigningType) {
/*  753 */     if (type instanceof Class)
/*      */     {
/*  755 */       return (Class)type;
/*      */     }
/*      */     
/*  758 */     if (type instanceof ParameterizedType)
/*      */     {
/*  760 */       return getRawType((ParameterizedType)type);
/*      */     }
/*      */     
/*  763 */     if (type instanceof TypeVariable) {
/*  764 */       if (assigningType == null) {
/*  765 */         return null;
/*      */       }
/*      */ 
/*      */       
/*  769 */       Object genericDeclaration = ((TypeVariable)type).getGenericDeclaration();
/*      */ 
/*      */ 
/*      */       
/*  773 */       if (!(genericDeclaration instanceof Class)) {
/*  774 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  779 */       Map<TypeVariable<?>, Type> typeVarAssigns = getTypeArguments(assigningType, (Class)genericDeclaration);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  784 */       if (typeVarAssigns == null) {
/*  785 */         return null;
/*      */       }
/*      */ 
/*      */       
/*  789 */       Type typeArgument = typeVarAssigns.get(type);
/*      */       
/*  791 */       if (typeArgument == null) {
/*  792 */         return null;
/*      */       }
/*      */ 
/*      */       
/*  796 */       return getRawType(typeArgument, assigningType);
/*      */     } 
/*      */     
/*  799 */     if (type instanceof GenericArrayType) {
/*      */       
/*  801 */       Class<?> rawComponentType = getRawType(((GenericArrayType)type)
/*  802 */           .getGenericComponentType(), assigningType);
/*      */ 
/*      */       
/*  805 */       return (rawComponentType != null) ? Array.newInstance(rawComponentType, 0).getClass() : null;
/*      */     } 
/*      */ 
/*      */     
/*  809 */     if (type instanceof WildcardType) {
/*  810 */       return null;
/*      */     }
/*      */     
/*  813 */     throw new IllegalArgumentException("unknown type: " + type);
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
/*      */   private static Map<TypeVariable<?>, Type> getTypeArguments(Class<?> cls, Class<?> toClass, Map<TypeVariable<?>, Type> subtypeVarAssigns) {
/*  827 */     if (!isAssignable(cls, toClass)) {
/*  828 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  832 */     if (cls.isPrimitive()) {
/*      */       
/*  834 */       if (toClass.isPrimitive())
/*      */       {
/*      */         
/*  837 */         return new HashMap<>();
/*      */       }
/*      */ 
/*      */       
/*  841 */       cls = ClassUtils.primitiveToWrapper(cls);
/*      */     } 
/*      */ 
/*      */     
/*  845 */     HashMap<TypeVariable<?>, Type> typeVarAssigns = (subtypeVarAssigns == null) ? new HashMap<>() : new HashMap<>(subtypeVarAssigns);
/*      */ 
/*      */ 
/*      */     
/*  849 */     if (toClass.equals(cls)) {
/*  850 */       return typeVarAssigns;
/*      */     }
/*      */ 
/*      */     
/*  854 */     return getTypeArguments(getClosestParentType(cls, toClass), toClass, typeVarAssigns);
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
/*      */   public static Map<TypeVariable<?>, Type> getTypeArguments(ParameterizedType type) {
/*  870 */     return getTypeArguments(type, getRawType(type), (Map<TypeVariable<?>, Type>)null);
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
/*      */   private static Map<TypeVariable<?>, Type> getTypeArguments(ParameterizedType parameterizedType, Class<?> toClass, Map<TypeVariable<?>, Type> subtypeVarAssigns) {
/*      */     Map<TypeVariable<?>, Type> typeVarAssigns;
/*  884 */     Class<?> cls = getRawType(parameterizedType);
/*      */ 
/*      */     
/*  887 */     if (!isAssignable(cls, toClass)) {
/*  888 */       return null;
/*      */     }
/*      */     
/*  891 */     Type ownerType = parameterizedType.getOwnerType();
/*      */ 
/*      */     
/*  894 */     if (ownerType instanceof ParameterizedType) {
/*      */       
/*  896 */       ParameterizedType parameterizedOwnerType = (ParameterizedType)ownerType;
/*  897 */       typeVarAssigns = getTypeArguments(parameterizedOwnerType, 
/*  898 */           getRawType(parameterizedOwnerType), subtypeVarAssigns);
/*      */     } else {
/*      */       
/*  901 */       typeVarAssigns = (subtypeVarAssigns == null) ? new HashMap<>() : new HashMap<>(subtypeVarAssigns);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  906 */     Type[] typeArgs = parameterizedType.getActualTypeArguments();
/*      */     
/*  908 */     TypeVariable[] arrayOfTypeVariable = (TypeVariable[])cls.getTypeParameters();
/*      */ 
/*      */     
/*  911 */     for (int i = 0; i < arrayOfTypeVariable.length; i++) {
/*  912 */       Type typeArg = typeArgs[i];
/*  913 */       typeVarAssigns.put(arrayOfTypeVariable[i], typeVarAssigns
/*      */           
/*  915 */           .getOrDefault(typeArg, typeArg));
/*      */     } 
/*      */ 
/*      */     
/*  919 */     if (toClass.equals(cls))
/*      */     {
/*  921 */       return typeVarAssigns;
/*      */     }
/*      */ 
/*      */     
/*  925 */     return getTypeArguments(getClosestParentType(cls, toClass), toClass, typeVarAssigns);
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
/*      */   public static Map<TypeVariable<?>, Type> getTypeArguments(Type type, Class<?> toClass) {
/*  967 */     return getTypeArguments(type, toClass, (Map<TypeVariable<?>, Type>)null);
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
/*      */   private static Map<TypeVariable<?>, Type> getTypeArguments(Type type, Class<?> toClass, Map<TypeVariable<?>, Type> subtypeVarAssigns) {
/*  980 */     if (type instanceof Class) {
/*  981 */       return getTypeArguments((Class)type, toClass, subtypeVarAssigns);
/*      */     }
/*      */     
/*  984 */     if (type instanceof ParameterizedType) {
/*  985 */       return getTypeArguments((ParameterizedType)type, toClass, subtypeVarAssigns);
/*      */     }
/*      */     
/*  988 */     if (type instanceof GenericArrayType) {
/*  989 */       return getTypeArguments(((GenericArrayType)type).getGenericComponentType(), 
/*  990 */           toClass.isArray() ? toClass.getComponentType() : toClass, subtypeVarAssigns);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  995 */     if (type instanceof WildcardType) {
/*  996 */       for (Type bound : getImplicitUpperBounds((WildcardType)type)) {
/*      */         
/*  998 */         if (isAssignable(bound, toClass)) {
/*  999 */           return getTypeArguments(bound, toClass, subtypeVarAssigns);
/*      */         }
/*      */       } 
/*      */       
/* 1003 */       return null;
/*      */     } 
/*      */     
/* 1006 */     if (type instanceof TypeVariable) {
/* 1007 */       for (Type bound : getImplicitBounds((TypeVariable)type)) {
/*      */         
/* 1009 */         if (isAssignable(bound, toClass)) {
/* 1010 */           return getTypeArguments(bound, toClass, subtypeVarAssigns);
/*      */         }
/*      */       } 
/*      */       
/* 1014 */       return null;
/*      */     } 
/* 1016 */     throw new IllegalStateException("found an unhandled type: " + type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isArrayType(Type type) {
/* 1026 */     return (type instanceof GenericArrayType || (type instanceof Class && ((Class)type).isArray()));
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
/*      */   private static boolean isAssignable(Type type, Class<?> toClass) {
/* 1038 */     if (type == null)
/*      */     {
/* 1040 */       return (toClass == null || !toClass.isPrimitive());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1045 */     if (toClass == null) {
/* 1046 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1050 */     if (toClass.equals(type)) {
/* 1051 */       return true;
/*      */     }
/*      */     
/* 1054 */     if (type instanceof Class)
/*      */     {
/* 1056 */       return ClassUtils.isAssignable((Class)type, toClass);
/*      */     }
/*      */     
/* 1059 */     if (type instanceof ParameterizedType)
/*      */     {
/* 1061 */       return isAssignable(getRawType((ParameterizedType)type), toClass);
/*      */     }
/*      */ 
/*      */     
/* 1065 */     if (type instanceof TypeVariable) {
/*      */ 
/*      */       
/* 1068 */       for (Type bound : ((TypeVariable)type).getBounds()) {
/* 1069 */         if (isAssignable(bound, toClass)) {
/* 1070 */           return true;
/*      */         }
/*      */       } 
/*      */       
/* 1074 */       return false;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1079 */     if (type instanceof GenericArrayType) {
/* 1080 */       return (toClass.equals(Object.class) || (toClass
/* 1081 */         .isArray() && 
/* 1082 */         isAssignable(((GenericArrayType)type).getGenericComponentType(), toClass
/* 1083 */           .getComponentType())));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1088 */     if (type instanceof WildcardType) {
/* 1089 */       return false;
/*      */     }
/*      */     
/* 1092 */     throw new IllegalStateException("found an unhandled type: " + type);
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
/*      */   private static boolean isAssignable(Type type, GenericArrayType toGenericArrayType, Map<TypeVariable<?>, Type> typeVarAssigns) {
/* 1107 */     if (type == null) {
/* 1108 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1113 */     if (toGenericArrayType == null) {
/* 1114 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1118 */     if (toGenericArrayType.equals(type)) {
/* 1119 */       return true;
/*      */     }
/*      */     
/* 1122 */     Type toComponentType = toGenericArrayType.getGenericComponentType();
/*      */     
/* 1124 */     if (type instanceof Class) {
/* 1125 */       Class<?> cls = (Class)type;
/*      */ 
/*      */       
/* 1128 */       return (cls.isArray() && 
/* 1129 */         isAssignable(cls.getComponentType(), toComponentType, typeVarAssigns));
/*      */     } 
/*      */     
/* 1132 */     if (type instanceof GenericArrayType)
/*      */     {
/* 1134 */       return isAssignable(((GenericArrayType)type).getGenericComponentType(), toComponentType, typeVarAssigns);
/*      */     }
/*      */ 
/*      */     
/* 1138 */     if (type instanceof WildcardType) {
/*      */       
/* 1140 */       for (Type bound : getImplicitUpperBounds((WildcardType)type)) {
/* 1141 */         if (isAssignable(bound, toGenericArrayType)) {
/* 1142 */           return true;
/*      */         }
/*      */       } 
/*      */       
/* 1146 */       return false;
/*      */     } 
/*      */     
/* 1149 */     if (type instanceof TypeVariable) {
/*      */ 
/*      */       
/* 1152 */       for (Type bound : getImplicitBounds((TypeVariable)type)) {
/* 1153 */         if (isAssignable(bound, toGenericArrayType)) {
/* 1154 */           return true;
/*      */         }
/*      */       } 
/*      */       
/* 1158 */       return false;
/*      */     } 
/*      */     
/* 1161 */     if (type instanceof ParameterizedType)
/*      */     {
/*      */ 
/*      */       
/* 1165 */       return false;
/*      */     }
/*      */     
/* 1168 */     throw new IllegalStateException("found an unhandled type: " + type);
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
/*      */   private static boolean isAssignable(Type type, ParameterizedType toParameterizedType, Map<TypeVariable<?>, Type> typeVarAssigns) {
/* 1182 */     if (type == null) {
/* 1183 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1188 */     if (toParameterizedType == null) {
/* 1189 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1193 */     if (type instanceof GenericArrayType) {
/* 1194 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1198 */     if (toParameterizedType.equals(type)) {
/* 1199 */       return true;
/*      */     }
/*      */ 
/*      */     
/* 1203 */     Class<?> toClass = getRawType(toParameterizedType);
/*      */ 
/*      */     
/* 1206 */     Map<TypeVariable<?>, Type> fromTypeVarAssigns = getTypeArguments(type, toClass, (Map<TypeVariable<?>, Type>)null);
/*      */ 
/*      */     
/* 1209 */     if (fromTypeVarAssigns == null) {
/* 1210 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1216 */     if (fromTypeVarAssigns.isEmpty()) {
/* 1217 */       return true;
/*      */     }
/*      */ 
/*      */     
/* 1221 */     Map<TypeVariable<?>, Type> toTypeVarAssigns = getTypeArguments(toParameterizedType, toClass, typeVarAssigns);
/*      */ 
/*      */ 
/*      */     
/* 1225 */     for (TypeVariable<?> var : toTypeVarAssigns.keySet()) {
/* 1226 */       Type toTypeArg = unrollVariableAssignments(var, toTypeVarAssigns);
/* 1227 */       Type fromTypeArg = unrollVariableAssignments(var, fromTypeVarAssigns);
/*      */       
/* 1229 */       if (toTypeArg == null && fromTypeArg instanceof Class) {
/*      */         continue;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1236 */       if (fromTypeArg != null && toTypeArg != null && 
/* 1237 */         !toTypeArg.equals(fromTypeArg) && (!(toTypeArg instanceof WildcardType) || 
/* 1238 */         !isAssignable(fromTypeArg, toTypeArg, typeVarAssigns)))
/*      */       {
/* 1240 */         return false;
/*      */       }
/*      */     } 
/* 1243 */     return true;
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
/*      */   public static boolean isAssignable(Type type, Type toType) {
/* 1257 */     return isAssignable(type, toType, (Map<TypeVariable<?>, Type>)null);
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
/*      */   private static boolean isAssignable(Type type, Type toType, Map<TypeVariable<?>, Type> typeVarAssigns) {
/* 1271 */     if (toType == null || toType instanceof Class) {
/* 1272 */       return isAssignable(type, (Class)toType);
/*      */     }
/*      */     
/* 1275 */     if (toType instanceof ParameterizedType) {
/* 1276 */       return isAssignable(type, (ParameterizedType)toType, typeVarAssigns);
/*      */     }
/*      */     
/* 1279 */     if (toType instanceof GenericArrayType) {
/* 1280 */       return isAssignable(type, (GenericArrayType)toType, typeVarAssigns);
/*      */     }
/*      */     
/* 1283 */     if (toType instanceof WildcardType) {
/* 1284 */       return isAssignable(type, (WildcardType)toType, typeVarAssigns);
/*      */     }
/*      */     
/* 1287 */     if (toType instanceof TypeVariable) {
/* 1288 */       return isAssignable(type, (TypeVariable)toType, typeVarAssigns);
/*      */     }
/*      */     
/* 1291 */     throw new IllegalStateException("found an unhandled type: " + toType);
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
/*      */   private static boolean isAssignable(Type type, TypeVariable<?> toTypeVariable, Map<TypeVariable<?>, Type> typeVarAssigns) {
/* 1306 */     if (type == null) {
/* 1307 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1312 */     if (toTypeVariable == null) {
/* 1313 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1317 */     if (toTypeVariable.equals(type)) {
/* 1318 */       return true;
/*      */     }
/*      */     
/* 1321 */     if (type instanceof TypeVariable) {
/*      */ 
/*      */ 
/*      */       
/* 1325 */       Type[] bounds = getImplicitBounds((TypeVariable)type);
/*      */       
/* 1327 */       for (Type bound : bounds) {
/* 1328 */         if (isAssignable(bound, toTypeVariable, typeVarAssigns)) {
/* 1329 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1334 */     if (type instanceof Class || type instanceof ParameterizedType || type instanceof GenericArrayType || type instanceof WildcardType)
/*      */     {
/* 1336 */       return false;
/*      */     }
/*      */     
/* 1339 */     throw new IllegalStateException("found an unhandled type: " + type);
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
/*      */   private static boolean isAssignable(Type type, WildcardType toWildcardType, Map<TypeVariable<?>, Type> typeVarAssigns) {
/* 1354 */     if (type == null) {
/* 1355 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1360 */     if (toWildcardType == null) {
/* 1361 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1365 */     if (toWildcardType.equals(type)) {
/* 1366 */       return true;
/*      */     }
/*      */     
/* 1369 */     Type[] toUpperBounds = getImplicitUpperBounds(toWildcardType);
/* 1370 */     Type[] toLowerBounds = getImplicitLowerBounds(toWildcardType);
/*      */     
/* 1372 */     if (type instanceof WildcardType) {
/* 1373 */       WildcardType wildcardType = (WildcardType)type;
/* 1374 */       Type[] upperBounds = getImplicitUpperBounds(wildcardType);
/* 1375 */       Type[] lowerBounds = getImplicitLowerBounds(wildcardType);
/*      */       
/* 1377 */       for (Type toBound : toUpperBounds) {
/*      */ 
/*      */         
/* 1380 */         toBound = substituteTypeVariables(toBound, typeVarAssigns);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1385 */         for (Type bound : upperBounds) {
/* 1386 */           if (!isAssignable(bound, toBound, typeVarAssigns)) {
/* 1387 */             return false;
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/* 1392 */       for (Type toBound : toLowerBounds) {
/*      */ 
/*      */         
/* 1395 */         toBound = substituteTypeVariables(toBound, typeVarAssigns);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1400 */         for (Type bound : lowerBounds) {
/* 1401 */           if (!isAssignable(toBound, bound, typeVarAssigns)) {
/* 1402 */             return false;
/*      */           }
/*      */         } 
/*      */       } 
/* 1406 */       return true;
/*      */     } 
/*      */     
/* 1409 */     for (Type toBound : toUpperBounds) {
/*      */ 
/*      */       
/* 1412 */       if (!isAssignable(type, substituteTypeVariables(toBound, typeVarAssigns), typeVarAssigns))
/*      */       {
/* 1414 */         return false;
/*      */       }
/*      */     } 
/*      */     
/* 1418 */     for (Type toBound : toLowerBounds) {
/*      */ 
/*      */       
/* 1421 */       if (!isAssignable(substituteTypeVariables(toBound, typeVarAssigns), type, typeVarAssigns))
/*      */       {
/* 1423 */         return false;
/*      */       }
/*      */     } 
/* 1426 */     return true;
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
/*      */   public static boolean isInstance(Object value, Type type) {
/* 1438 */     if (type == null) {
/* 1439 */       return false;
/*      */     }
/*      */     
/* 1442 */     return (value == null) ? ((!(type instanceof Class) || !((Class)type).isPrimitive())) : 
/* 1443 */       isAssignable(value.getClass(), type, (Map<TypeVariable<?>, Type>)null);
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
/*      */   private static <T> void mapTypeVariablesToArguments(Class<T> cls, ParameterizedType parameterizedType, Map<TypeVariable<?>, Type> typeVarAssigns) {
/* 1457 */     Type ownerType = parameterizedType.getOwnerType();
/*      */     
/* 1459 */     if (ownerType instanceof ParameterizedType)
/*      */     {
/* 1461 */       mapTypeVariablesToArguments(cls, (ParameterizedType)ownerType, typeVarAssigns);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1468 */     Type[] typeArgs = parameterizedType.getActualTypeArguments();
/*      */ 
/*      */ 
/*      */     
/* 1472 */     TypeVariable[] arrayOfTypeVariable = (TypeVariable[])getRawType(parameterizedType).getTypeParameters();
/*      */ 
/*      */     
/* 1475 */     List<TypeVariable<Class<T>>> typeVarList = Arrays.asList(cls
/* 1476 */         .getTypeParameters());
/*      */     
/* 1478 */     for (int i = 0; i < typeArgs.length; i++) {
/* 1479 */       TypeVariable<?> typeVar = arrayOfTypeVariable[i];
/* 1480 */       Type typeArg = typeArgs[i];
/*      */ 
/*      */       
/* 1483 */       if (typeVarList.contains(typeArg) && typeVarAssigns
/*      */ 
/*      */         
/* 1486 */         .containsKey(typeVar))
/*      */       {
/* 1488 */         typeVarAssigns.put((TypeVariable)typeArg, typeVarAssigns.get(typeVar));
/*      */       }
/*      */     } 
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
/*      */   public static Type[] normalizeUpperBounds(Type[] bounds) {
/* 1519 */     Objects.requireNonNull(bounds, "bounds");
/*      */     
/* 1521 */     if (bounds.length < 2) {
/* 1522 */       return bounds;
/*      */     }
/*      */     
/* 1525 */     Set<Type> types = new HashSet<>(bounds.length);
/*      */     
/* 1527 */     for (Type type1 : bounds) {
/* 1528 */       boolean subtypeFound = false;
/*      */       
/* 1530 */       for (Type type2 : bounds) {
/* 1531 */         if (type1 != type2 && isAssignable(type2, type1, (Map<TypeVariable<?>, Type>)null)) {
/* 1532 */           subtypeFound = true;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/* 1537 */       if (!subtypeFound) {
/* 1538 */         types.add(type1);
/*      */       }
/*      */     } 
/*      */     
/* 1542 */     return types.<Type>toArray(ArrayUtils.EMPTY_TYPE_ARRAY);
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
/*      */   public static final ParameterizedType parameterize(Class<?> rawClass, Map<TypeVariable<?>, Type> typeVariableMap) {
/* 1556 */     Objects.requireNonNull(rawClass, "rawClass");
/* 1557 */     Objects.requireNonNull(typeVariableMap, "typeVariableMap");
/* 1558 */     return parameterizeWithOwner((Type)null, rawClass, 
/* 1559 */         extractTypeArgumentsFrom(typeVariableMap, (TypeVariable<?>[])rawClass.getTypeParameters()));
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
/*      */   public static final ParameterizedType parameterize(Class<?> rawClass, Type... typeArguments) {
/* 1572 */     return parameterizeWithOwner((Type)null, rawClass, typeArguments);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String parameterizedTypeToString(ParameterizedType parameterizedType) {
/* 1583 */     StringBuilder builder = new StringBuilder();
/*      */     
/* 1585 */     Type useOwner = parameterizedType.getOwnerType();
/* 1586 */     Class<?> raw = (Class)parameterizedType.getRawType();
/*      */     
/* 1588 */     if (useOwner == null) {
/* 1589 */       builder.append(raw.getName());
/*      */     } else {
/* 1591 */       if (useOwner instanceof Class) {
/* 1592 */         builder.append(((Class)useOwner).getName());
/*      */       } else {
/* 1594 */         builder.append(useOwner.toString());
/*      */       } 
/* 1596 */       builder.append('.').append(raw.getSimpleName());
/*      */     } 
/*      */     
/* 1599 */     int[] recursiveTypeIndexes = findRecursiveTypes(parameterizedType);
/*      */     
/* 1601 */     if (recursiveTypeIndexes.length > 0) {
/* 1602 */       appendRecursiveTypes(builder, recursiveTypeIndexes, parameterizedType.getActualTypeArguments());
/*      */     } else {
/* 1604 */       appendAllTo(builder.append('<'), ", ", parameterizedType.getActualTypeArguments()).append('>');
/*      */     } 
/*      */     
/* 1607 */     return builder.toString();
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
/*      */   public static final ParameterizedType parameterizeWithOwner(Type owner, Class<?> rawClass, Map<TypeVariable<?>, Type> typeVariableMap) {
/* 1623 */     Objects.requireNonNull(rawClass, "rawClass");
/* 1624 */     Objects.requireNonNull(typeVariableMap, "typeVariableMap");
/* 1625 */     return parameterizeWithOwner(owner, rawClass, 
/* 1626 */         extractTypeArgumentsFrom(typeVariableMap, (TypeVariable<?>[])rawClass.getTypeParameters()));
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
/*      */   public static final ParameterizedType parameterizeWithOwner(Type owner, Class<?> rawClass, Type... typeArguments) {
/*      */     Type useOwner;
/* 1642 */     Objects.requireNonNull(rawClass, "rawClass");
/*      */     
/* 1644 */     if (rawClass.getEnclosingClass() == null) {
/* 1645 */       Validate.isTrue((owner == null), "no owner allowed for top-level %s", new Object[] { rawClass });
/* 1646 */       useOwner = null;
/* 1647 */     } else if (owner == null) {
/* 1648 */       useOwner = rawClass.getEnclosingClass();
/*      */     } else {
/* 1650 */       Validate.isTrue(isAssignable(owner, rawClass.getEnclosingClass()), "%s is invalid owner type for parameterized %s", new Object[] { owner, rawClass });
/*      */       
/* 1652 */       useOwner = owner;
/*      */     } 
/* 1654 */     Validate.noNullElements((Object[])typeArguments, "null type argument at index %s", new Object[0]);
/* 1655 */     Validate.isTrue(((rawClass.getTypeParameters()).length == typeArguments.length), "invalid number of type parameters specified: expected %d, got %d", new Object[] {
/* 1656 */           Integer.valueOf((rawClass.getTypeParameters()).length), 
/* 1657 */           Integer.valueOf(typeArguments.length)
/*      */         });
/* 1659 */     return new ParameterizedTypeImpl(rawClass, useOwner, typeArguments);
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
/*      */   private static Type substituteTypeVariables(Type type, Map<TypeVariable<?>, Type> typeVarAssigns) {
/* 1671 */     if (type instanceof TypeVariable && typeVarAssigns != null) {
/* 1672 */       Type replacementType = typeVarAssigns.get(type);
/*      */       
/* 1674 */       if (replacementType == null) {
/* 1675 */         throw new IllegalArgumentException("missing assignment type for type variable " + type);
/*      */       }
/*      */       
/* 1678 */       return replacementType;
/*      */     } 
/* 1680 */     return type;
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
/*      */   public static String toLongString(TypeVariable<?> typeVariable) {
/* 1692 */     Objects.requireNonNull(typeVariable, "typeVariable");
/* 1693 */     StringBuilder buf = new StringBuilder();
/* 1694 */     GenericDeclaration d = (GenericDeclaration)typeVariable.getGenericDeclaration();
/* 1695 */     if (d instanceof Class) {
/* 1696 */       Class<?> c = (Class)d;
/*      */       while (true) {
/* 1698 */         if (c.getEnclosingClass() == null) {
/* 1699 */           buf.insert(0, c.getName());
/*      */           break;
/*      */         } 
/* 1702 */         buf.insert(0, c.getSimpleName()).insert(0, '.');
/* 1703 */         c = c.getEnclosingClass();
/*      */       } 
/* 1705 */     } else if (d instanceof Type) {
/* 1706 */       buf.append(toString((Type)d));
/*      */     } else {
/* 1708 */       buf.append(d);
/*      */     } 
/* 1710 */     return buf.append(':').append(typeVariableToString(typeVariable)).toString();
/*      */   }
/*      */   
/*      */   private static <T> String toString(T object) {
/* 1714 */     return (object instanceof Type) ? toString((Type)object) : object.toString();
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
/*      */   public static String toString(Type type) {
/* 1726 */     Objects.requireNonNull(type, "type");
/* 1727 */     if (type instanceof Class) {
/* 1728 */       return classToString((Class)type);
/*      */     }
/* 1730 */     if (type instanceof ParameterizedType) {
/* 1731 */       return parameterizedTypeToString((ParameterizedType)type);
/*      */     }
/* 1733 */     if (type instanceof WildcardType) {
/* 1734 */       return wildcardTypeToString((WildcardType)type);
/*      */     }
/* 1736 */     if (type instanceof TypeVariable) {
/* 1737 */       return typeVariableToString((TypeVariable)type);
/*      */     }
/* 1739 */     if (type instanceof GenericArrayType) {
/* 1740 */       return genericArrayTypeToString((GenericArrayType)type);
/*      */     }
/* 1742 */     throw new IllegalArgumentException(ObjectUtils.identityToString(type));
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
/*      */   public static boolean typesSatisfyVariables(Map<TypeVariable<?>, Type> typeVariableMap) {
/* 1760 */     Objects.requireNonNull(typeVariableMap, "typeVariableMap");
/*      */ 
/*      */     
/* 1763 */     for (Map.Entry<TypeVariable<?>, Type> entry : typeVariableMap.entrySet()) {
/* 1764 */       TypeVariable<?> typeVar = entry.getKey();
/* 1765 */       Type type = entry.getValue();
/*      */       
/* 1767 */       for (Type bound : getImplicitBounds(typeVar)) {
/* 1768 */         if (!isAssignable(type, substituteTypeVariables(bound, typeVariableMap), typeVariableMap))
/*      */         {
/* 1770 */           return false;
/*      */         }
/*      */       } 
/*      */     } 
/* 1774 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String typeVariableToString(TypeVariable<?> typeVariable) {
/* 1785 */     StringBuilder buf = new StringBuilder(typeVariable.getName());
/* 1786 */     Type[] bounds = typeVariable.getBounds();
/* 1787 */     if (bounds.length > 0 && (bounds.length != 1 || !Object.class.equals(bounds[0]))) {
/* 1788 */       buf.append(" extends ");
/* 1789 */       appendAllTo(buf, " & ", typeVariable.getBounds());
/*      */     } 
/* 1791 */     return buf.toString();
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
/*      */   private static Type[] unrollBounds(Map<TypeVariable<?>, Type> typeArguments, Type[] bounds) {
/* 1803 */     Type[] result = bounds;
/* 1804 */     int i = 0;
/* 1805 */     for (; i < result.length; i++) {
/* 1806 */       Type unrolled = unrollVariables(typeArguments, result[i]);
/* 1807 */       if (unrolled == null) {
/* 1808 */         result = (Type[])ArrayUtils.remove((Object[])result, i--);
/*      */       } else {
/* 1810 */         result[i] = unrolled;
/*      */       } 
/*      */     } 
/* 1813 */     return result;
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
/*      */   private static Type unrollVariableAssignments(TypeVariable<?> typeVariable, Map<TypeVariable<?>, Type> typeVarAssigns) {
/*      */     Type result;
/*      */     while (true) {
/* 1828 */       result = typeVarAssigns.get(typeVariable);
/* 1829 */       if (!(result instanceof TypeVariable) || result.equals(typeVariable)) {
/*      */         break;
/*      */       }
/* 1832 */       typeVariable = (TypeVariable)result;
/*      */     } 
/* 1834 */     return result;
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
/*      */   public static Type unrollVariables(Map<TypeVariable<?>, Type> typeArguments, Type type) {
/* 1846 */     if (typeArguments == null) {
/* 1847 */       typeArguments = Collections.emptyMap();
/*      */     }
/* 1849 */     if (containsTypeVariables(type)) {
/* 1850 */       if (type instanceof TypeVariable) {
/* 1851 */         return unrollVariables(typeArguments, typeArguments.get(type));
/*      */       }
/* 1853 */       if (type instanceof ParameterizedType) {
/* 1854 */         Map<TypeVariable<?>, Type> parameterizedTypeArguments; ParameterizedType p = (ParameterizedType)type;
/*      */         
/* 1856 */         if (p.getOwnerType() == null) {
/* 1857 */           parameterizedTypeArguments = typeArguments;
/*      */         } else {
/* 1859 */           parameterizedTypeArguments = new HashMap<>(typeArguments);
/* 1860 */           parameterizedTypeArguments.putAll(getTypeArguments(p));
/*      */         } 
/* 1862 */         Type[] args = p.getActualTypeArguments();
/* 1863 */         for (int i = 0; i < args.length; i++) {
/* 1864 */           Type unrolled = unrollVariables(parameterizedTypeArguments, args[i]);
/* 1865 */           if (unrolled != null) {
/* 1866 */             args[i] = unrolled;
/*      */           }
/*      */         } 
/* 1869 */         return parameterizeWithOwner(p.getOwnerType(), (Class)p.getRawType(), args);
/*      */       } 
/* 1871 */       if (type instanceof WildcardType) {
/* 1872 */         WildcardType wild = (WildcardType)type;
/* 1873 */         return wildcardType().withUpperBounds(unrollBounds(typeArguments, wild.getUpperBounds()))
/* 1874 */           .withLowerBounds(unrollBounds(typeArguments, wild.getLowerBounds())).build();
/*      */       } 
/*      */     } 
/* 1877 */     return type;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static WildcardTypeBuilder wildcardType() {
/* 1887 */     return new WildcardTypeBuilder();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String wildcardTypeToString(WildcardType wildcardType) {
/* 1898 */     StringBuilder buf = (new StringBuilder()).append('?');
/* 1899 */     Type[] lowerBounds = wildcardType.getLowerBounds();
/* 1900 */     Type[] upperBounds = wildcardType.getUpperBounds();
/* 1901 */     if (lowerBounds.length > 1 || (lowerBounds.length == 1 && lowerBounds[0] != null)) {
/* 1902 */       appendAllTo(buf.append(" super "), " & ", lowerBounds);
/* 1903 */     } else if (upperBounds.length > 1 || (upperBounds.length == 1 && !Object.class.equals(upperBounds[0]))) {
/* 1904 */       appendAllTo(buf.append(" extends "), " & ", upperBounds);
/*      */     } 
/* 1906 */     return buf.toString();
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
/*      */   public static <T> Typed<T> wrap(Class<T> type) {
/* 1918 */     return wrap(type);
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
/*      */   public static <T> Typed<T> wrap(Type type) {
/* 1930 */     return () -> type;
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\reflect\TypeUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */