/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.google.common.collect.Ordering;
/*     */ import com.google.inject.Binding;
/*     */ import com.google.inject.ConfigurationException;
/*     */ import com.google.inject.CreationException;
/*     */ import com.google.inject.Injector;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.ProvisionException;
/*     */ import com.google.inject.Scope;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.internal.util.SourceProvider;
/*     */ import com.google.inject.spi.ElementSource;
/*     */ import com.google.inject.spi.InterceptorBinding;
/*     */ import com.google.inject.spi.Message;
/*     */ import com.google.inject.spi.ScopeBinding;
/*     */ import com.google.inject.spi.TypeConverterBinding;
/*     */ import com.google.inject.spi.TypeListenerBinding;
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collection;
/*     */ import java.util.Formatter;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public final class Errors
/*     */   implements Serializable
/*     */ {
/*     */   private final Errors root;
/*     */   private final Errors parent;
/*     */   private final Object source;
/*     */   private List<Message> errors;
/*     */   private static final String CONSTRUCTOR_RULES = "Injectable classes must have either one (and only one) constructor annotated with @Inject or a zero-argument constructor that is not private.";
/*     */   
/*     */   static <T> T checkNotNull(T reference, String name) {
/*  76 */     if (reference != null) {
/*  77 */       return reference;
/*     */     }
/*     */     
/*  80 */     NullPointerException npe = new NullPointerException(name);
/*  81 */     throw new ConfigurationException(ImmutableSet.of(new Message(npe.toString(), npe)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void checkConfiguration(boolean condition, String format, Object... args) {
/*  89 */     if (condition) {
/*     */       return;
/*     */     }
/*     */     
/*  93 */     throw new ConfigurationException(ImmutableSet.of(new Message(format(format, args))));
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
/*     */   public Errors() {
/* 109 */     this.root = this;
/* 110 */     this.parent = null;
/* 111 */     this.source = SourceProvider.UNKNOWN_SOURCE;
/*     */   }
/*     */   
/*     */   public Errors(Object source) {
/* 115 */     this.root = this;
/* 116 */     this.parent = null;
/* 117 */     this.source = source;
/*     */   }
/*     */   
/*     */   private Errors(Errors parent, Object source) {
/* 121 */     this.root = parent.root;
/* 122 */     this.parent = parent;
/* 123 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   public Errors withSource(Object source) {
/* 128 */     return (source == this.source || source == SourceProvider.UNKNOWN_SOURCE) ? 
/* 129 */       this : 
/* 130 */       new Errors(this, source);
/*     */   }
/*     */   
/*     */   public Errors aopDisabled(InterceptorBinding binding) {
/* 134 */     return addMessage(ErrorId.AOP_DISABLED, "Binding interceptor is not supported when bytecode generation is disabled. %nInterceptor bound at: %s", new Object[] { binding
/*     */ 
/*     */ 
/*     */           
/* 138 */           .getSource() });
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
/*     */   public Errors missingImplementation(Key<?> key) {
/* 159 */     return addMessage(ErrorId.MISSING_IMPLEMENTATION, "No implementation for %s was bound.", new Object[] { key });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   <T> Errors missingImplementationWithHint(Key<T> key, Injector injector) {
/* 165 */     MissingImplementationError<T> error = new MissingImplementationError<>(key, injector, getSources());
/* 166 */     return addMessage(new Message(GuiceInternal.GUICE_INTERNAL, ErrorId.MISSING_IMPLEMENTATION, error));
/*     */   }
/*     */ 
/*     */   
/*     */   public Errors jitDisabled(Key<?> key) {
/* 171 */     return addMessage(ErrorId.JIT_DISABLED, "Explicit bindings are required and %s is not explicitly bound.", new Object[] { key });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Errors jitDisabledInParent(Key<?> key) {
/* 178 */     return addMessage(ErrorId.JIT_DISABLED_IN_PARENT, "Explicit bindings are required and %s would be bound in a parent injector.%nPlease add an explicit binding for it, either in the child or the parent.", new Object[] { key });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Errors atInjectRequired(TypeLiteral<?> type) {
/* 186 */     return addMessage(new Message(GuiceInternal.GUICE_INTERNAL, ErrorId.MISSING_CONSTRUCTOR, new MissingConstructorError(type, true, 
/*     */ 
/*     */ 
/*     */             
/* 190 */             getSources())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Errors converterReturnedNull(String stringValue, Object source, TypeLiteral<?> type, TypeConverterBinding typeConverterBinding) {
/* 198 */     return addMessage(ErrorId.CONVERTER_RETURNED_NULL, "Received null converting '%s' (bound at %s) to %s%n using %s.", new Object[] { stringValue, 
/*     */ 
/*     */ 
/*     */           
/* 202 */           convert(source), type, typeConverterBinding });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Errors conversionTypeError(String stringValue, Object source, TypeLiteral<?> type, TypeConverterBinding typeConverterBinding, Object converted) {
/* 213 */     return addMessage(ErrorId.CONVERSION_TYPE_ERROR, "Type mismatch converting '%s' (bound at %s) to %s%n using %s.%n Converter returned %s.", new Object[] { stringValue, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 219 */           convert(source), type, typeConverterBinding, converted });
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
/*     */   public Errors conversionError(String stringValue, Object source, TypeLiteral<?> type, TypeConverterBinding typeConverterBinding, RuntimeException cause) {
/* 231 */     return errorInUserCode(cause, "Error converting '%s' (bound at %s) to %s%n using %s.%n Reason: %s", new Object[] { stringValue, 
/*     */ 
/*     */ 
/*     */           
/* 235 */           convert(source), type, typeConverterBinding, cause });
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
/*     */   public Errors ambiguousTypeConversion(String stringValue, Object source, TypeLiteral<?> type, TypeConverterBinding a, TypeConverterBinding b) {
/* 247 */     return addMessage(ErrorId.AMBIGUOUS_TYPE_CONVERSION, "Multiple converters can convert '%s' (bound at %s) to %s:%n %s and%n %s.%n Please adjust your type converter configuration to avoid overlapping matches.", new Object[] { stringValue, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 254 */           convert(source), type, a, b });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Errors bindingToProvider() {
/* 261 */     return addMessage(ErrorId.BINDING_TO_PROVIDER, "Binding to Provider is not allowed.", new Object[0]);
/*     */   }
/*     */   
/*     */   public Errors notASubtype(Class<?> implementationType, Class<?> type) {
/* 265 */     return addMessage(ErrorId.NOT_A_SUBTYPE, "%s doesn't extend %s.", new Object[] { implementationType, type });
/*     */   }
/*     */   
/*     */   public Errors recursiveImplementationType() {
/* 269 */     return addMessage(ErrorId.RECURSIVE_IMPLEMENTATION_TYPE, "@ImplementedBy points to the same class it annotates.", new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Errors recursiveProviderType() {
/* 275 */     return addMessage(ErrorId.RECURSIVE_PROVIDER_TYPE, "@ProvidedBy points to the same class it annotates.", new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public Errors missingRuntimeRetention(Class<? extends Annotation> annotation) {
/* 280 */     return addMessage(ErrorId.MISSING_RUNTIME_RETENTION, 
/*     */         
/* 282 */         format("Please annotate %s with @Retention(RUNTIME).", new Object[] { annotation }), new Object[0]);
/*     */   }
/*     */   
/*     */   public Errors missingScopeAnnotation(Class<? extends Annotation> annotation) {
/* 286 */     return addMessage(ErrorId.MISSING_SCOPE_ANNOTATION, 
/*     */         
/* 288 */         format("Please annotate %s with @ScopeAnnotation.", new Object[] { annotation }), new Object[0]);
/*     */   }
/*     */   
/*     */   public Errors optionalConstructor(Constructor<?> constructor) {
/* 292 */     return addMessage(ErrorId.OPTIONAL_CONSTRUCTOR, "%s is annotated @Inject(optional=true), but constructors cannot be optional.", new Object[] { constructor });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Errors cannotBindToGuiceType(String simpleName) {
/* 299 */     return addMessage(ErrorId.BINDING_TO_GUICE_TYPE, "Binding to core guice framework type is not allowed: %s.", new Object[] { simpleName });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Errors scopeNotFound(Class<? extends Annotation> scopeAnnotation) {
/* 306 */     return addMessage(new Message(GuiceInternal.GUICE_INTERNAL, ErrorId.SCOPE_NOT_FOUND, new ScopeNotFoundError(scopeAnnotation, 
/*     */ 
/*     */ 
/*     */             
/* 310 */             getSources())));
/*     */   }
/*     */ 
/*     */   
/*     */   public Errors scopeAnnotationOnAbstractType(Class<? extends Annotation> scopeAnnotation, Class<?> type, Object source) {
/* 315 */     return addMessage(ErrorId.SCOPE_ANNOTATION_ON_ABSTRACT_TYPE, "%s is annotated with %s, but scope annotations are not supported for abstract types.%n Bound at %s.", new Object[] { type, scopeAnnotation, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 321 */           convert(source) });
/*     */   }
/*     */   
/*     */   public Errors misplacedBindingAnnotation(Member member, Annotation bindingAnnotation) {
/* 325 */     return addMessage(ErrorId.MISPLACED_BINDING_ANNOTATION, "%s is annotated with %s, but binding annotations should be applied to its parameters instead.", new Object[] { member, bindingAnnotation });
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
/*     */   public Errors missingConstructor(TypeLiteral<?> type) {
/* 339 */     return addMessage(new Message(GuiceInternal.GUICE_INTERNAL, ErrorId.MISSING_CONSTRUCTOR, new MissingConstructorError(type, false, 
/*     */ 
/*     */ 
/*     */             
/* 343 */             getSources())));
/*     */   }
/*     */   
/*     */   public Errors tooManyConstructors(Class<?> implementation) {
/* 347 */     return addMessage(ErrorId.TOO_MANY_CONSTRUCTORS, "%s has more than one constructor annotated with @Inject. %s", new Object[] { implementation, "Injectable classes must have either one (and only one) constructor annotated with @Inject or a zero-argument constructor that is not private." });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Errors constructorNotDefinedByType(Constructor<?> constructor, TypeLiteral<?> type) {
/* 355 */     return addMessage(ErrorId.CONSTRUCTOR_NOT_DEFINED_BY_TYPE, "%s does not define %s", new Object[] { type, constructor });
/*     */   }
/*     */ 
/*     */   
/*     */   public <K, V> Errors duplicateMapKey(Key<Map<K, V>> mapKey, Multimap<K, Binding<V>> duplicates) {
/* 360 */     return addMessage(new Message(GuiceInternal.GUICE_INTERNAL, ErrorId.DUPLICATE_MAP_KEY, new DuplicateMapKeyError<>(mapKey, duplicates, 
/*     */ 
/*     */ 
/*     */             
/* 364 */             getSources())));
/*     */   }
/*     */ 
/*     */   
/*     */   public Errors duplicateScopes(ScopeBinding existing, Class<? extends Annotation> annotationType, Scope scope) {
/* 369 */     return addMessage(ErrorId.DUPLICATE_SCOPES, "Scope %s is already bound to %s at %s.%n Cannot bind %s.", new Object[] { existing
/*     */ 
/*     */           
/* 372 */           .getScope(), annotationType, existing
/*     */           
/* 374 */           .getSource(), scope });
/*     */   }
/*     */ 
/*     */   
/*     */   public Errors voidProviderMethod() {
/* 379 */     return addMessage(ErrorId.VOID_PROVIDER_METHOD, "Provider methods must return a value. Do not return void.", new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public Errors missingConstantValues() {
/* 384 */     return addMessage(ErrorId.MISSING_CONSTANT_VALUES, "Missing constant value. Please call to(...).", new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public Errors cannotInjectInnerClass(Class<?> type) {
/* 389 */     return addMessage(ErrorId.INJECT_INNER_CLASS, "Injecting into inner classes is not supported.  Please use a 'static' class (top-level or nested) instead of %s.", new Object[] { type });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Errors duplicateBindingAnnotations(Member member, Class<? extends Annotation> a, Class<? extends Annotation> b) {
/* 398 */     return addMessage(ErrorId.DUPLICATE_BINDING_ANNOTATIONS, "%s has more than one annotation annotated with @BindingAnnotation: %s and %s", new Object[] { member, a, b });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Errors staticInjectionOnInterface(Class<?> clazz) {
/* 407 */     return addMessage(ErrorId.STATIC_INJECTION_ON_INTERFACE, "%s is an interface, but interfaces have no static injection points.", new Object[] { clazz });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Errors cannotInjectFinalField(Field field) {
/* 414 */     return addMessage(ErrorId.INJECT_FINAL_FIELD, "Injected field %s cannot be final.", new Object[] { field });
/*     */   }
/*     */ 
/*     */   
/*     */   public Errors atTargetIsMissingParameter(Annotation bindingAnnotation, String parameterName, Class<?> clazz) {
/* 419 */     return addMessage(ErrorId.AT_TARGET_IS_MISSING_PARAMETER, "Binding annotation %s must have PARAMETER listed in its @Targets. It was used on constructor parameter %s in %s.", new Object[] { bindingAnnotation, parameterName, clazz });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Errors cannotInjectAbstractMethod(Method method) {
/* 429 */     return addMessage(ErrorId.INJECT_ABSTRACT_METHOD, "Injected method %s cannot be abstract.", new Object[] { method });
/*     */   }
/*     */ 
/*     */   
/*     */   public Errors cannotInjectMethodWithTypeParameters(Method method) {
/* 434 */     return addMessage(ErrorId.INJECT_METHOD_WITH_TYPE_PARAMETER, "Injected method %s cannot declare type parameters of its own.", new Object[] { method });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Errors duplicateScopeAnnotations(Class<? extends Annotation> a, Class<? extends Annotation> b) {
/* 442 */     return addMessage(ErrorId.DUPLICATE_SCOPE_ANNOTATIONS, "More than one scope annotation was found: %s and %s.", new Object[] { a, b });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Errors recursiveBinding(Key<?> key, Key<?> linkedKey) {
/* 450 */     return addMessage(ErrorId.RECURSIVE_BINDING, "Binding points to itself. Key: %s", new Object[] {
/* 451 */           Messages.convert(key) });
/*     */   }
/*     */   
/*     */   Errors bindingAlreadySet(Binding<?> binding, Binding<?> original) {
/* 455 */     BindingAlreadySetError error = new BindingAlreadySetError(binding, original, getSources());
/* 456 */     return addMessage(new Message(GuiceInternal.GUICE_INTERNAL, ErrorId.BINDING_ALREADY_SET, error));
/*     */   }
/*     */ 
/*     */   
/*     */   public Errors bindingAlreadySet(Key<?> key, Object source) {
/* 461 */     return addMessage(ErrorId.BINDING_ALREADY_SET, "A binding to %s was already configured at %s.", new Object[] { key, 
/*     */ 
/*     */ 
/*     */           
/* 465 */           convert(source) });
/*     */   }
/*     */   
/*     */   public Errors jitBindingAlreadySet(Key<?> key) {
/* 469 */     return addMessage(ErrorId.JIT_BINDING_ALREADY_SET, "A just-in-time binding to %s was already configured on a parent injector.", new Object[] { key });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Errors childBindingAlreadySet(Key<?> key, Set<Object> sources) {
/* 480 */     Message message = new Message(GuiceInternal.GUICE_INTERNAL, ErrorId.CHILD_BINDING_ALREADY_SET, new ChildBindingAlreadySetError(key, sources, getSources()));
/* 481 */     return addMessage(message);
/*     */   }
/*     */   
/*     */   public Errors errorCheckingDuplicateBinding(Key<?> key, Object source, Throwable t) {
/* 485 */     return addMessage(ErrorId.OTHER, "A binding to %s was already configured at %s and an error was thrown while checking duplicate bindings.  Error: %s", new Object[] { key, 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 490 */           convert(source), t });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Errors errorNotifyingTypeListener(TypeListenerBinding listener, TypeLiteral<?> type, Throwable cause) {
/* 496 */     return errorInUserCode(cause, "Error notifying TypeListener %s (bound at %s) of %s.%n Reason: %s", new Object[] { listener
/*     */ 
/*     */           
/* 499 */           .getListener(), 
/* 500 */           convert(listener.getSource()), type, cause });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Errors exposedButNotBound(Key<?> key) {
/* 506 */     return addMessage(ErrorId.EXPOSED_BUT_NOT_BOUND, "Could not expose() %s, it must be explicitly bound.", new Object[] { key });
/*     */   }
/*     */ 
/*     */   
/*     */   public Errors keyNotFullySpecified(TypeLiteral<?> typeLiteral) {
/* 511 */     return addMessage(ErrorId.KEY_NOT_FULLY_SPECIFIED, "%s cannot be used as a key; It is not fully specified.", new Object[] { typeLiteral });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Errors errorEnhancingClass(Class<?> clazz, Throwable cause) {
/* 518 */     return errorInUserCode(cause, "Unable to method intercept: %s", new Object[] { clazz });
/*     */   }
/*     */   
/*     */   public static Collection<Message> getMessagesFromThrowable(Throwable throwable) {
/* 522 */     if (throwable instanceof ProvisionException)
/* 523 */       return ((ProvisionException)throwable).getErrorMessages(); 
/* 524 */     if (throwable instanceof ConfigurationException)
/* 525 */       return ((ConfigurationException)throwable).getErrorMessages(); 
/* 526 */     if (throwable instanceof CreationException) {
/* 527 */       return ((CreationException)throwable).getErrorMessages();
/*     */     }
/* 529 */     return (Collection<Message>)ImmutableSet.of();
/*     */   }
/*     */ 
/*     */   
/*     */   public Errors errorInUserCode(Throwable cause, String messageFormat, Object... arguments) {
/* 534 */     Collection<Message> messages = getMessagesFromThrowable(cause);
/*     */     
/* 536 */     if (!messages.isEmpty()) {
/* 537 */       return merge(messages);
/*     */     }
/* 539 */     return addMessage(ErrorId.ERROR_IN_USER_CODE, cause, messageFormat, arguments);
/*     */   }
/*     */ 
/*     */   
/*     */   public Errors cannotInjectRawProvider() {
/* 544 */     return addMessage(ErrorId.INJECT_RAW_PROVIDER, "Cannot inject a Provider that has no type parameter", new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public Errors cannotInjectRawMembersInjector() {
/* 549 */     return addMessage(ErrorId.INJECT_RAW_MEMBERS_INJECTOR, "Cannot inject a MembersInjector that has no type parameter", new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Errors cannotInjectTypeLiteralOf(Type unsupportedType) {
/* 555 */     return addMessage(ErrorId.OTHER, "Cannot inject a TypeLiteral of %s", new Object[] { unsupportedType });
/*     */   }
/*     */   
/*     */   public Errors cannotInjectRawTypeLiteral() {
/* 559 */     return addMessage(ErrorId.INJECT_RAW_TYPE_LITERAL, "Cannot inject a TypeLiteral that has no type parameter", new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public void throwCreationExceptionIfErrorsExist() {
/* 564 */     if (!hasErrors()) {
/*     */       return;
/*     */     }
/*     */     
/* 568 */     CreationException exception = new CreationException(getMessages());
/* 569 */     throw exception;
/*     */   }
/*     */   
/*     */   public void throwConfigurationExceptionIfErrorsExist() {
/* 573 */     if (!hasErrors()) {
/*     */       return;
/*     */     }
/*     */     
/* 577 */     ConfigurationException exception = new ConfigurationException(getMessages());
/* 578 */     throw exception;
/*     */   }
/*     */ 
/*     */   
/*     */   public void throwProvisionExceptionIfErrorsExist() {
/* 583 */     if (!hasErrors()) {
/*     */       return;
/*     */     }
/* 586 */     ProvisionException exception = new ProvisionException(getMessages());
/* 587 */     throw exception;
/*     */   }
/*     */   
/*     */   public Errors merge(Collection<Message> messages) {
/* 591 */     List<Object> sources = getSources();
/* 592 */     for (Message message : messages) {
/* 593 */       addMessage(Messages.mergeSources(sources, message));
/*     */     }
/* 595 */     return this;
/*     */   }
/*     */   
/*     */   public Errors merge(Errors moreErrors) {
/* 599 */     if (moreErrors.root == this.root || moreErrors.root.errors == null) {
/* 600 */       return this;
/*     */     }
/*     */     
/* 603 */     merge(moreErrors.root.errors);
/* 604 */     return this;
/*     */   }
/*     */   
/*     */   public Errors merge(InternalProvisionException ipe) {
/* 608 */     merge((Collection<Message>)ipe.getErrors());
/* 609 */     return this;
/*     */   }
/*     */   
/*     */   private List<Object> getSources() {
/* 613 */     List<Object> sources = Lists.newArrayList();
/* 614 */     for (Errors e = this; e != null; e = e.parent) {
/* 615 */       if (e.source != SourceProvider.UNKNOWN_SOURCE) {
/* 616 */         sources.add(0, e.source);
/*     */       }
/*     */     } 
/* 619 */     return sources;
/*     */   }
/*     */   
/*     */   public void throwIfNewErrors(int expectedSize) throws ErrorsException {
/* 623 */     if (size() == expectedSize) {
/*     */       return;
/*     */     }
/*     */     
/* 627 */     throw toException();
/*     */   }
/*     */   
/*     */   public ErrorsException toException() {
/* 631 */     return new ErrorsException(this);
/*     */   }
/*     */   
/*     */   public boolean hasErrors() {
/* 635 */     return (this.root.errors != null);
/*     */   }
/*     */   
/*     */   public Errors addMessage(String messageFormat, Object... arguments) {
/* 639 */     return addMessage(ErrorId.OTHER, null, messageFormat, arguments);
/*     */   }
/*     */   
/*     */   public Errors addMessage(ErrorId errorId, String messageFormat, Object... arguments) {
/* 643 */     return addMessage(errorId, null, messageFormat, arguments);
/*     */   }
/*     */ 
/*     */   
/*     */   private Errors addMessage(ErrorId errorId, Throwable cause, String messageFormat, Object... arguments) {
/* 648 */     addMessage(Messages.create(errorId, cause, getSources(), messageFormat, arguments));
/* 649 */     return this;
/*     */   }
/*     */   
/*     */   public Errors addMessage(Message message) {
/* 653 */     if (this.root.errors == null) {
/* 654 */       this.root.errors = Lists.newArrayList();
/*     */     }
/* 656 */     this.root.errors.add(message);
/* 657 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String format(String messageFormat, Object... arguments) {
/* 662 */     return Messages.format(messageFormat, arguments);
/*     */   }
/*     */   
/*     */   public List<Message> getMessages() {
/* 666 */     if (this.root.errors == null) {
/* 667 */       return (List<Message>)ImmutableList.of();
/*     */     }
/*     */     
/* 670 */     return (new Ordering<Message>(this)
/*     */       {
/*     */         public int compare(Message a, Message b) {
/* 673 */           return a.getSource().compareTo(b.getSource());
/*     */         }
/* 675 */       }).sortedCopy(this.root.errors);
/*     */   }
/*     */   
/*     */   public int size() {
/* 679 */     return (this.root.errors == null) ? 0 : this.root.errors.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public static Object convert(Object o) {
/* 684 */     return Messages.convert(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Object convert(Object o, ElementSource source) {
/* 689 */     return Messages.convert(o, source);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void formatSource(Formatter formatter, Object source) {
/* 694 */     formatter.format("  ", new Object[0]);
/* 695 */     (new SourceFormatter(source, formatter, false)).format();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\Errors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */