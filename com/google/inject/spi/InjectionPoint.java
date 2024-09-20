/*     */ package com.google.inject.spi;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.ObjectArrays;
/*     */ import com.google.inject.ConfigurationException;
/*     */ import com.google.inject.Inject;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.internal.Annotations;
/*     */ import com.google.inject.internal.DeclaredMembers;
/*     */ import com.google.inject.internal.Errors;
/*     */ import com.google.inject.internal.ErrorsException;
/*     */ import com.google.inject.internal.KotlinSupport;
/*     */ import com.google.inject.internal.MoreTypes;
/*     */ import com.google.inject.internal.Nullability;
/*     */ import com.google.inject.internal.util.Classes;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.inject.Inject;
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
/*     */ public final class InjectionPoint
/*     */ {
/*  67 */   private static final Logger logger = Logger.getLogger(InjectionPoint.class.getName());
/*     */   
/*     */   private final boolean optional;
/*     */   private final Member member;
/*     */   private final TypeLiteral<?> declaringType;
/*     */   private final ImmutableList<Dependency<?>> dependencies;
/*     */   
/*     */   InjectionPoint(TypeLiteral<?> declaringType, Method method, boolean optional) {
/*  75 */     this.member = method;
/*  76 */     this.declaringType = declaringType;
/*  77 */     this.optional = optional;
/*  78 */     this
/*  79 */       .dependencies = forMember(new Errors(method), method, declaringType, method
/*     */ 
/*     */ 
/*     */         
/*  83 */         .getParameterAnnotations(), 
/*  84 */         KotlinSupport.getInstance().getIsParameterKotlinNullablePredicate(method));
/*     */   }
/*     */   
/*     */   InjectionPoint(TypeLiteral<?> declaringType, Constructor<?> constructor) {
/*  88 */     this.member = constructor;
/*  89 */     this.declaringType = declaringType;
/*  90 */     this.optional = false;
/*  91 */     Errors errors = new Errors(constructor);
/*  92 */     KotlinSupport.getInstance().checkConstructorParameterAnnotations(constructor, errors);
/*     */     
/*  94 */     this
/*  95 */       .dependencies = forMember(errors, constructor, declaringType, constructor
/*     */ 
/*     */ 
/*     */         
/*  99 */         .getParameterAnnotations(), 
/* 100 */         KotlinSupport.getInstance().getIsParameterKotlinNullablePredicate(constructor));
/*     */   }
/*     */   
/*     */   InjectionPoint(TypeLiteral<?> declaringType, Field field, boolean optional) {
/* 104 */     this.member = field;
/* 105 */     this.declaringType = declaringType;
/* 106 */     this.optional = optional;
/*     */     
/* 108 */     Annotation[] annotations = getAnnotations(field);
/*     */     
/* 110 */     Errors errors = new Errors(field);
/* 111 */     Key<?> key = null;
/*     */     try {
/* 113 */       key = Annotations.getKey(declaringType.getFieldType(field), field, annotations, errors);
/* 114 */     } catch (ConfigurationException e) {
/* 115 */       errors.merge(e.getErrorMessages());
/* 116 */     } catch (ErrorsException e) {
/* 117 */       errors.merge(e.getErrors());
/*     */     } 
/* 119 */     errors.throwConfigurationExceptionIfErrorsExist();
/*     */ 
/*     */ 
/*     */     
/* 123 */     boolean allowsNull = (Nullability.hasNullableAnnotation(annotations) || KotlinSupport.getInstance().isNullable(field));
/* 124 */     this.dependencies = ImmutableList.of(newDependency(key, allowsNull, -1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ImmutableList<Dependency<?>> forMember(Errors errors, Member member, TypeLiteral<?> type, Annotation[][] parameterAnnotationsPerParameter, Predicate<Integer> isParameterKotlinNullable) {
/* 133 */     List<Dependency<?>> dependencies = Lists.newArrayList();
/* 134 */     int index = 0;
/*     */     
/* 136 */     for (TypeLiteral<?> parameterType : (Iterable<TypeLiteral<?>>)type.getParameterTypes(member)) {
/*     */       try {
/* 138 */         Annotation[] parameterAnnotations = parameterAnnotationsPerParameter[index];
/* 139 */         Key<?> key = Annotations.getKey(parameterType, member, parameterAnnotations, errors);
/*     */ 
/*     */         
/* 142 */         boolean isNullable = (Nullability.hasNullableAnnotation(parameterAnnotations) || isParameterKotlinNullable.test(Integer.valueOf(index)));
/* 143 */         dependencies.add(newDependency(key, isNullable, index));
/* 144 */         index++;
/* 145 */       } catch (ConfigurationException e) {
/* 146 */         errors.merge(e.getErrorMessages());
/* 147 */       } catch (ErrorsException e) {
/* 148 */         errors.merge(e.getErrors());
/*     */       } 
/*     */     } 
/*     */     
/* 152 */     errors.throwConfigurationExceptionIfErrorsExist();
/* 153 */     return ImmutableList.copyOf(dependencies);
/*     */   }
/*     */ 
/*     */   
/*     */   private <T> Dependency<T> newDependency(Key<T> key, boolean allowsNull, int parameterIndex) {
/* 158 */     return new Dependency<>(this, key, allowsNull, parameterIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Member getMember() {
/* 164 */     return this.member;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Dependency<?>> getDependencies() {
/* 175 */     return (List<Dependency<?>>)this.dependencies;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOptional() {
/* 185 */     return this.optional;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isToolable() {
/* 194 */     return ((AnnotatedElement)this.member).isAnnotationPresent((Class)Toolable.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeLiteral<?> getDeclaringType() {
/* 205 */     return this.declaringType;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 210 */     return (o instanceof InjectionPoint && this.member
/* 211 */       .equals(((InjectionPoint)o).member) && this.declaringType
/* 212 */       .equals(((InjectionPoint)o).declaringType));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 217 */     return this.member.hashCode() ^ this.declaringType.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 222 */     return Classes.toString(this.member);
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
/*     */   public static <T> InjectionPoint forConstructor(Constructor<T> constructor) {
/* 234 */     return new InjectionPoint(TypeLiteral.get(constructor.getDeclaringClass()), constructor);
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
/*     */   public static <T> InjectionPoint forConstructor(Constructor<T> constructor, TypeLiteral<? extends T> type) {
/* 246 */     if (type.getRawType() != constructor.getDeclaringClass()) {
/* 247 */       (new Errors(type))
/* 248 */         .constructorNotDefinedByType(constructor, type)
/* 249 */         .throwConfigurationExceptionIfErrorsExist();
/*     */     }
/*     */     
/* 252 */     return new InjectionPoint(type, constructor);
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
/*     */   public static InjectionPoint forConstructorOf(TypeLiteral<?> type) {
/* 268 */     return forConstructorOf(type, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InjectionPoint forConstructorOf(TypeLiteral<?> type, boolean atInjectRequired) {
/* 288 */     Class<?> rawType = MoreTypes.getRawType(type.getType());
/* 289 */     Errors errors = new Errors(rawType);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 297 */     List<Constructor<?>> atInjectConstructors = (List<Constructor<?>>)Arrays.<Constructor<?>>stream(rawType.getDeclaredConstructors()).filter(constructor -> (constructor.isAnnotationPresent((Class)Inject.class) || constructor.isAnnotationPresent((Class)Inject.class))).collect(Collectors.toList());
/*     */     
/* 299 */     Constructor<?> injectableConstructor = null;
/*     */ 
/*     */ 
/*     */     
/* 303 */     Objects.requireNonNull(errors); atInjectConstructors.stream().filter(constructor -> constructor.isAnnotationPresent((Class)Inject.class)).filter(constructor -> ((Inject)constructor.<Inject>getAnnotation(Inject.class)).optional()).forEach(errors::optionalConstructor);
/*     */     
/* 305 */     if (atInjectConstructors.size() > 1) {
/* 306 */       errors.tooManyConstructors(rawType);
/*     */     } else {
/* 308 */       injectableConstructor = (Constructor)Iterables.getOnlyElement(atInjectConstructors, null);
/* 309 */       if (injectableConstructor != null) {
/* 310 */         checkForMisplacedBindingAnnotations(injectableConstructor, errors);
/*     */       }
/*     */     } 
/* 313 */     if (atInjectRequired && injectableConstructor == null) {
/* 314 */       errors.atInjectRequired(type);
/*     */     }
/* 316 */     errors.throwConfigurationExceptionIfErrorsExist();
/*     */     
/* 318 */     if (injectableConstructor != null) {
/* 319 */       return new InjectionPoint(type, injectableConstructor);
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 324 */       Constructor<?> noArgConstructor = rawType.getDeclaredConstructor(new Class[0]);
/*     */ 
/*     */       
/* 327 */       if (Modifier.isPrivate(noArgConstructor.getModifiers()) && 
/* 328 */         !Modifier.isPrivate(rawType.getModifiers())) {
/* 329 */         errors.missingConstructor(type);
/* 330 */         throw new ConfigurationException(errors.getMessages());
/*     */       } 
/*     */       
/* 333 */       checkForMisplacedBindingAnnotations(noArgConstructor, errors);
/* 334 */       return new InjectionPoint(type, noArgConstructor);
/* 335 */     } catch (NoSuchMethodException e) {
/* 336 */       errors.missingConstructor(type);
/* 337 */       throw new ConfigurationException(errors.getMessages());
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
/*     */ 
/*     */   
/*     */   public static InjectionPoint forConstructorOf(Class<?> type) {
/* 351 */     return forConstructorOf(TypeLiteral.get(type));
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
/*     */   public static <T> InjectionPoint forMethod(Method method, TypeLiteral<T> type) {
/* 363 */     return new InjectionPoint(type, method, false);
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
/*     */   public static Set<InjectionPoint> forStaticMethodsAndFields(TypeLiteral<?> type) {
/*     */     Set<InjectionPoint> result;
/* 378 */     Errors errors = new Errors();
/*     */ 
/*     */ 
/*     */     
/* 382 */     if (type.getRawType().isInterface()) {
/* 383 */       errors.staticInjectionOnInterface(type.getRawType());
/* 384 */       result = null;
/*     */     } else {
/* 386 */       result = getInjectionPoints(type, true, errors);
/*     */     } 
/*     */     
/* 389 */     if (errors.hasErrors()) {
/* 390 */       throw (new ConfigurationException(errors.getMessages())).withPartialValue(result);
/*     */     }
/* 392 */     return result;
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
/*     */   public static Set<InjectionPoint> forStaticMethodsAndFields(Class<?> type) {
/* 407 */     return forStaticMethodsAndFields(TypeLiteral.get(type));
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
/*     */   public static Set<InjectionPoint> forInstanceMethodsAndFields(TypeLiteral<?> type) {
/* 422 */     Errors errors = new Errors();
/* 423 */     Set<InjectionPoint> result = getInjectionPoints(type, false, errors);
/* 424 */     if (errors.hasErrors()) {
/* 425 */       throw (new ConfigurationException(errors.getMessages())).withPartialValue(result);
/*     */     }
/* 427 */     return result;
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
/*     */   public static Set<InjectionPoint> forInstanceMethodsAndFields(Class<?> type) {
/* 442 */     return forInstanceMethodsAndFields(TypeLiteral.get(type));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean checkForMisplacedBindingAnnotations(Member member, Errors errors) {
/* 448 */     Annotation misplacedBindingAnnotation = Annotations.findBindingAnnotation(errors, member, ((AnnotatedElement)member)
/* 449 */         .getAnnotations());
/* 450 */     if (misplacedBindingAnnotation == null) {
/* 451 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 456 */     if (member instanceof Method) {
/*     */       try {
/* 458 */         if (member.getDeclaringClass().getDeclaredField(member.getName()) != null) {
/* 459 */           return false;
/*     */         }
/* 461 */       } catch (NoSuchFieldException noSuchFieldException) {}
/*     */     }
/*     */ 
/*     */     
/* 465 */     errors.misplacedBindingAnnotation(member, misplacedBindingAnnotation);
/* 466 */     return true;
/*     */   }
/*     */   
/*     */   static abstract class InjectableMember
/*     */   {
/*     */     final TypeLiteral<?> declaringType;
/*     */     final boolean optional;
/*     */     final boolean jsr330;
/*     */     InjectableMember previous;
/*     */     InjectableMember next;
/*     */     
/*     */     InjectableMember(TypeLiteral<?> declaringType, Annotation atInject) {
/* 478 */       this.declaringType = declaringType;
/*     */       
/* 480 */       if (atInject.annotationType() == Inject.class) {
/* 481 */         this.optional = false;
/* 482 */         this.jsr330 = true;
/*     */         
/*     */         return;
/*     */       } 
/* 486 */       this.jsr330 = false;
/* 487 */       this.optional = ((Inject)atInject).optional();
/*     */     }
/*     */     
/*     */     abstract InjectionPoint toInjectionPoint();
/*     */   }
/*     */   
/*     */   static class InjectableField extends InjectableMember {
/*     */     final Field field;
/*     */     
/*     */     InjectableField(TypeLiteral<?> declaringType, Field field, Annotation atInject) {
/* 497 */       super(declaringType, atInject);
/* 498 */       this.field = field;
/*     */     }
/*     */ 
/*     */     
/*     */     InjectionPoint toInjectionPoint() {
/* 503 */       return new InjectionPoint(this.declaringType, this.field, this.optional);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class InjectableMethod
/*     */     extends InjectableMember
/*     */   {
/*     */     final Method method;
/*     */     
/*     */     boolean overrodeGuiceInject;
/*     */     
/*     */     InjectableMethod(TypeLiteral<?> declaringType, Method method, Annotation atInject) {
/* 516 */       super(declaringType, atInject);
/* 517 */       this.method = method;
/*     */     }
/*     */ 
/*     */     
/*     */     InjectionPoint toInjectionPoint() {
/* 522 */       return new InjectionPoint(this.declaringType, this.method, this.optional);
/*     */     }
/*     */     
/*     */     public boolean isFinal() {
/* 526 */       return Modifier.isFinal(this.method.getModifiers());
/*     */     }
/*     */   }
/*     */   
/*     */   static Annotation getAtInject(AnnotatedElement member) {
/* 531 */     Annotation a = (Annotation)member.getAnnotation(Inject.class);
/* 532 */     return (a == null) ? (Annotation)member.getAnnotation(Inject.class) : a;
/*     */   }
/*     */   
/*     */   static class InjectableMembers
/*     */   {
/*     */     InjectionPoint.InjectableMember head;
/*     */     InjectionPoint.InjectableMember tail;
/*     */     
/*     */     void add(InjectionPoint.InjectableMember member) {
/* 541 */       if (this.head == null) {
/* 542 */         this.head = this.tail = member;
/*     */       } else {
/* 544 */         member.previous = this.tail;
/* 545 */         this.tail.next = member;
/* 546 */         this.tail = member;
/*     */       } 
/*     */     }
/*     */     
/*     */     void remove(InjectionPoint.InjectableMember member) {
/* 551 */       if (member.previous != null) {
/* 552 */         member.previous.next = member.next;
/*     */       }
/* 554 */       if (member.next != null) {
/* 555 */         member.next.previous = member.previous;
/*     */       }
/* 557 */       if (this.head == member) {
/* 558 */         this.head = member.next;
/*     */       }
/* 560 */       if (this.tail == member) {
/* 561 */         this.tail = member.previous;
/*     */       }
/*     */     }
/*     */     
/*     */     boolean isEmpty() {
/* 566 */       return (this.head == null);
/*     */     }
/*     */   }
/*     */   
/*     */   enum Position
/*     */   {
/* 572 */     TOP,
/* 573 */     MIDDLE,
/* 574 */     BOTTOM;
/*     */   }
/*     */ 
/*     */   
/*     */   static class OverrideIndex
/*     */   {
/*     */     final InjectionPoint.InjectableMembers injectableMembers;
/*     */     
/*     */     Map<InjectionPoint.Signature, List<InjectionPoint.InjectableMethod>> bySignature;
/*     */     
/* 584 */     InjectionPoint.Position position = InjectionPoint.Position.TOP; Method lastMethod;
/*     */     
/*     */     OverrideIndex(InjectionPoint.InjectableMembers injectableMembers) {
/* 587 */       this.injectableMembers = injectableMembers;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     InjectionPoint.Signature lastSignature;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean removeIfOverriddenBy(Method method, boolean alwaysRemove, InjectionPoint.InjectableMethod injectableMethod) {
/* 608 */       if (this.position == InjectionPoint.Position.TOP)
/*     */       {
/* 610 */         return false;
/*     */       }
/*     */       
/* 613 */       if (this.bySignature == null) {
/*     */ 
/*     */         
/* 616 */         this.bySignature = new HashMap<>();
/* 617 */         InjectionPoint.InjectableMember member = this.injectableMembers.head;
/* 618 */         for (; member != null; 
/* 619 */           member = member.next) {
/* 620 */           if (member instanceof InjectionPoint.InjectableMethod) {
/*     */ 
/*     */             
/* 623 */             InjectionPoint.InjectableMethod im = (InjectionPoint.InjectableMethod)member;
/* 624 */             if (!im.isFinal()) {
/*     */ 
/*     */               
/* 627 */               List<InjectionPoint.InjectableMethod> list = new ArrayList<>();
/* 628 */               list.add(im);
/* 629 */               this.bySignature.put(new InjectionPoint.Signature(im.method), list);
/*     */             } 
/*     */           } 
/*     */         } 
/* 633 */       }  this.lastMethod = method;
/* 634 */       InjectionPoint.Signature signature = this.lastSignature = new InjectionPoint.Signature(method);
/* 635 */       List<InjectionPoint.InjectableMethod> methods = this.bySignature.get(signature);
/* 636 */       boolean removed = false;
/* 637 */       if (methods != null) {
/* 638 */         for (Iterator<InjectionPoint.InjectableMethod> iterator = methods.iterator(); iterator.hasNext(); ) {
/* 639 */           InjectionPoint.InjectableMethod possiblyOverridden = iterator.next();
/* 640 */           if (InjectionPoint.overrides(method, possiblyOverridden.method)) {
/* 641 */             boolean wasGuiceInject = (!possiblyOverridden.jsr330 || possiblyOverridden.overrodeGuiceInject);
/*     */             
/* 643 */             if (injectableMethod != null) {
/* 644 */               injectableMethod.overrodeGuiceInject = wasGuiceInject;
/*     */             }
/*     */ 
/*     */ 
/*     */             
/* 649 */             if (alwaysRemove || !wasGuiceInject) {
/* 650 */               removed = true;
/* 651 */               iterator.remove();
/* 652 */               this.injectableMembers.remove(possiblyOverridden);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/* 657 */       return removed;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void add(InjectionPoint.InjectableMethod injectableMethod) {
/* 665 */       this.injectableMembers.add(injectableMethod);
/* 666 */       if (this.position == InjectionPoint.Position.BOTTOM || injectableMethod.isFinal()) {
/*     */         return;
/*     */       }
/*     */       
/* 670 */       if (this.bySignature != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 676 */         InjectionPoint.Signature signature = (injectableMethod.method == this.lastMethod) ? this.lastSignature : new InjectionPoint.Signature(injectableMethod.method);
/* 677 */         ((List<InjectionPoint.InjectableMethod>)this.bySignature.computeIfAbsent(signature, k -> new ArrayList())).add(injectableMethod);
/*     */       } 
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
/*     */ 
/*     */ 
/*     */   
/*     */   private static Set<InjectionPoint> getInjectionPoints(TypeLiteral<?> type, boolean statics, Errors errors) {
/* 693 */     InjectableMembers injectableMembers = new InjectableMembers();
/* 694 */     OverrideIndex overrideIndex = null;
/*     */     
/* 696 */     List<TypeLiteral<?>> hierarchy = hierarchyFor(type);
/* 697 */     int topIndex = hierarchy.size() - 1;
/* 698 */     for (int i = topIndex; i >= 0; i--) {
/* 699 */       if (overrideIndex != null && i < topIndex)
/*     */       {
/* 701 */         if (i == 0) {
/* 702 */           overrideIndex.position = Position.BOTTOM;
/*     */         } else {
/* 704 */           overrideIndex.position = Position.MIDDLE;
/*     */         } 
/*     */       }
/*     */       
/* 708 */       TypeLiteral<?> current = hierarchy.get(i);
/*     */       
/* 710 */       for (Field field : getDeclaredFields(current)) {
/* 711 */         if (Modifier.isStatic(field.getModifiers()) == statics) {
/* 712 */           Annotation atInject = getAtInject(field);
/* 713 */           if (atInject != null) {
/* 714 */             InjectableField injectableField = new InjectableField(current, field, atInject);
/* 715 */             if (injectableField.jsr330 && Modifier.isFinal(field.getModifiers())) {
/* 716 */               errors.cannotInjectFinalField(field);
/*     */             }
/* 718 */             injectableMembers.add(injectableField);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 723 */       for (Method method : getDeclaredMethods(current)) {
/* 724 */         if (isEligibleForInjection(method, statics)) {
/* 725 */           Annotation atInject = getAtInject(method);
/* 726 */           if (atInject != null) {
/* 727 */             InjectableMethod injectableMethod = new InjectableMethod(current, method, atInject);
/* 728 */             if (checkForMisplacedBindingAnnotations(method, errors) || 
/* 729 */               !isValidMethod(injectableMethod, errors)) {
/* 730 */               if (overrideIndex != null)
/*     */               {
/* 732 */                 boolean removed = overrideIndex.removeIfOverriddenBy(method, false, injectableMethod);
/* 733 */                 if (removed) {
/* 734 */                   logger.log(Level.WARNING, "Method: {0} is not a valid injectable method (because it either has misplaced binding annotations or specifies type parameters) but is overriding a method that is valid. Because it is not valid, the method will not be injected. To fix this, make the method a valid injectable method.", method);
/*     */ 
/*     */ 
/*     */                 
/*     */                 }
/*     */ 
/*     */               
/*     */               }
/*     */ 
/*     */ 
/*     */             
/*     */             }
/* 746 */             else if (statics) {
/* 747 */               injectableMembers.add(injectableMethod);
/*     */             } else {
/* 749 */               if (overrideIndex == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 756 */                 overrideIndex = new OverrideIndex(injectableMembers);
/*     */               }
/*     */               else {
/*     */                 
/* 760 */                 overrideIndex.removeIfOverriddenBy(method, true, injectableMethod);
/*     */               } 
/* 762 */               overrideIndex.add(injectableMethod);
/*     */             }
/*     */           
/* 765 */           } else if (overrideIndex != null) {
/* 766 */             boolean removed = overrideIndex.removeIfOverriddenBy(method, false, null);
/* 767 */             if (removed) {
/* 768 */               logger.log(Level.WARNING, "Method: {0} is not annotated with @Inject but is overriding a method that is annotated with @javax.inject.Inject.Because it is not annotated with @Inject, the method will not be injected. To fix this, annotate the method with @Inject.", method);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 782 */     if (injectableMembers.isEmpty()) {
/* 783 */       return Collections.emptySet();
/*     */     }
/*     */     
/* 786 */     ImmutableSet.Builder<InjectionPoint> builder = ImmutableSet.builder();
/* 787 */     for (InjectableMember im = injectableMembers.head; im != null; im = im.next) {
/*     */       try {
/* 789 */         builder.add(im.toInjectionPoint());
/* 790 */       } catch (ConfigurationException ignorable) {
/* 791 */         if (!im.optional) {
/* 792 */           errors.merge(ignorable.getErrorMessages());
/*     */         }
/*     */       } 
/*     */     } 
/* 796 */     return (Set<InjectionPoint>)builder.build();
/*     */   }
/*     */   
/*     */   private static Field[] getDeclaredFields(TypeLiteral<?> type) {
/* 800 */     return DeclaredMembers.getDeclaredFields(type.getRawType());
/*     */   }
/*     */   
/*     */   private static Method[] getDeclaredMethods(TypeLiteral<?> type) {
/* 804 */     return DeclaredMembers.getDeclaredMethods(type.getRawType());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isEligibleForInjection(Method method, boolean statics) {
/* 826 */     return (Modifier.isStatic(method.getModifiers()) == statics && 
/* 827 */       !method.isBridge() && 
/* 828 */       !method.isSynthetic());
/*     */   }
/*     */   
/*     */   private static boolean isValidMethod(InjectableMethod injectableMethod, Errors errors) {
/* 832 */     boolean result = true;
/* 833 */     if (injectableMethod.jsr330) {
/* 834 */       Method method = injectableMethod.method;
/* 835 */       if (Modifier.isAbstract(method.getModifiers())) {
/* 836 */         errors.cannotInjectAbstractMethod(method);
/* 837 */         result = false;
/*     */       } 
/* 839 */       if ((method.getTypeParameters()).length > 0) {
/* 840 */         errors.cannotInjectMethodWithTypeParameters(method);
/* 841 */         result = false;
/*     */       } 
/*     */     } 
/* 844 */     return result;
/*     */   }
/*     */   
/*     */   private static List<TypeLiteral<?>> hierarchyFor(TypeLiteral<?> type) {
/* 848 */     List<TypeLiteral<?>> hierarchy = new ArrayList<>();
/* 849 */     TypeLiteral<?> current = type;
/* 850 */     while (current.getRawType() != Object.class) {
/* 851 */       hierarchy.add(current);
/* 852 */       current = current.getSupertype(current.getRawType().getSuperclass());
/*     */     } 
/* 854 */     return hierarchy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean overrides(Method a, Method b) {
/* 863 */     int modifiers = b.getModifiers();
/* 864 */     if (Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers)) {
/* 865 */       return true;
/*     */     }
/* 867 */     if (Modifier.isPrivate(modifiers)) {
/* 868 */       return false;
/*     */     }
/*     */     
/* 871 */     return a.getDeclaringClass().getPackage().equals(b.getDeclaringClass().getPackage());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Annotation[] getAnnotations(Field field) {
/* 881 */     Annotation[] javaAnnotations = field.getAnnotations();
/* 882 */     Annotation[] kotlinAnnotations = KotlinSupport.getInstance().getAnnotations(field);
/*     */     
/* 884 */     if (kotlinAnnotations.length == 0) {
/* 885 */       return javaAnnotations;
/*     */     }
/* 887 */     return (Annotation[])ObjectArrays.concat((Object[])javaAnnotations, (Object[])kotlinAnnotations, Annotation.class);
/*     */   }
/*     */ 
/*     */   
/*     */   static class Signature
/*     */   {
/*     */     final String name;
/*     */     final Class<?>[] parameterTypes;
/*     */     final int hash;
/*     */     
/*     */     Signature(Method method) {
/* 898 */       this.name = method.getName();
/* 899 */       this.parameterTypes = method.getParameterTypes();
/*     */       
/* 901 */       int h = this.name.hashCode();
/* 902 */       h = h * 31 + this.parameterTypes.length;
/* 903 */       for (Class<?> parameterType : this.parameterTypes) {
/* 904 */         h = h * 31 + parameterType.hashCode();
/*     */       }
/* 906 */       this.hash = h;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 911 */       return this.hash;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 916 */       if (!(o instanceof Signature)) {
/* 917 */         return false;
/*     */       }
/*     */       
/* 920 */       Signature other = (Signature)o;
/* 921 */       if (!this.name.equals(other.name)) {
/* 922 */         return false;
/*     */       }
/*     */       
/* 925 */       if (this.parameterTypes.length != other.parameterTypes.length) {
/* 926 */         return false;
/*     */       }
/*     */       
/* 929 */       for (int i = 0; i < this.parameterTypes.length; i++) {
/* 930 */         if (this.parameterTypes[i] != other.parameterTypes[i]) {
/* 931 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 935 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\InjectionPoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */