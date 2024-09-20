/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.inject.Guice;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.MembersInjector;
/*     */ import com.google.inject.Provides;
/*     */ import com.google.inject.ProvisionException;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.internal.util.SourceProvider;
/*     */ import com.google.inject.internal.util.StackTraceElements;
/*     */ import com.google.inject.spi.Dependency;
/*     */ import com.google.inject.spi.InjectionListener;
/*     */ import com.google.inject.spi.Message;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class InternalProvisionException
/*     */   extends Exception
/*     */ {
/*  67 */   private static final Logger logger = Logger.getLogger(Guice.class.getName());
/*     */   
/*  69 */   private static final Set<Dependency<?>> warnedDependencies = Collections.newSetFromMap(new ConcurrentHashMap<>());
/*     */ 
/*     */   
/*     */   public static InternalProvisionException circularDependenciesDisabled(Class<?> expectedType) {
/*  73 */     return create(ErrorId.CIRCULAR_PROXY_DISABLED, "Found a circular dependency involving %s, and circular dependencies are disabled.", new Object[] { expectedType });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InternalProvisionException cannotProxyClass(Class<?> expectedType) {
/*  80 */     return create(ErrorId.CAN_NOT_PROXY_CLASS, "Tried proxying %s to support a circular dependency, but it is not an interface.", new Object[] { expectedType });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InternalProvisionException create(ErrorId errorId, String format, Object... arguments) {
/*  88 */     return new InternalProvisionException(Messages.create(errorId, format, arguments));
/*     */   }
/*     */ 
/*     */   
/*     */   public static InternalProvisionException errorInUserCode(ErrorId errorId, Throwable cause, String messageFormat, Object... arguments) {
/*  93 */     Collection<Message> messages = Errors.getMessagesFromThrowable(cause);
/*  94 */     if (!messages.isEmpty())
/*     */     {
/*     */       
/*  97 */       return new InternalProvisionException(messages);
/*     */     }
/*  99 */     return new InternalProvisionException(
/* 100 */         Messages.create(errorId, cause, messageFormat, arguments));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static InternalProvisionException subtypeNotProvided(Class<? extends Provider<?>> providerType, Class<?> type) {
/* 106 */     return create(ErrorId.SUBTYPE_NOT_PROVIDED, "%s doesn't provide instances of %s.", new Object[] { providerType, type });
/*     */   }
/*     */ 
/*     */   
/*     */   public static InternalProvisionException errorInProvider(Throwable cause) {
/* 111 */     return errorInUserCode(ErrorId.ERROR_IN_CUSTOM_PROVIDER, cause, "%s", new Object[] { cause });
/*     */   }
/*     */   
/*     */   public static InternalProvisionException errorInjectingMethod(Throwable cause) {
/* 115 */     return errorInUserCode(ErrorId.ERROR_INJECTING_METHOD, cause, "%s", new Object[] { cause });
/*     */   }
/*     */   
/*     */   public static InternalProvisionException errorInjectingConstructor(Throwable cause) {
/* 119 */     return errorInUserCode(ErrorId.ERROR_INJECTING_CONSTRUCTOR, cause, "%s", new Object[] { cause });
/*     */   }
/*     */ 
/*     */   
/*     */   public static InternalProvisionException errorInUserInjector(MembersInjector<?> listener, TypeLiteral<?> type, RuntimeException cause) {
/* 124 */     return errorInUserCode(ErrorId.ERROR_IN_USER_INJECTOR, cause, "Error injecting %s using %s.%n Reason: %s", new Object[] { type, listener, cause });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InternalProvisionException jitDisabled(Key<?> key) {
/* 134 */     return create(ErrorId.JIT_DISABLED, "Explicit bindings are required and %s is not explicitly bound.", new Object[] { key });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InternalProvisionException errorNotifyingInjectionListener(InjectionListener<?> listener, TypeLiteral<?> type, RuntimeException cause) {
/* 142 */     return errorInUserCode(ErrorId.OTHER, cause, "Error notifying InjectionListener %s of %s.%n Reason: %s", new Object[] { listener, type, cause });
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
/*     */   static void onNullInjectedIntoNonNullableDependency(Object source, Dependency<?> dependency) throws InternalProvisionException {
/* 158 */     if (dependency.getInjectionPoint().getMember() instanceof Method) {
/* 159 */       Method annotated = (Method)dependency.getInjectionPoint().getMember();
/* 160 */       if (annotated.isAnnotationPresent((Class)Provides.class)) {
/* 161 */         switch (InternalFlags.getNullableProvidesOption()) {
/*     */           case IGNORE:
/*     */             return;
/*     */ 
/*     */ 
/*     */           
/*     */           case WARN:
/* 168 */             if (warnedDependencies.add(dependency)) {
/* 169 */               logger.log(Level.WARNING, "Guice injected null into {0} (a {1}), please mark it @Nullable. Use -Dguice_check_nullable_provides_params=ERROR to turn this into an error.", new Object[] {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                     
/* 175 */                     SourceFormatter.getParameterName(dependency), 
/* 176 */                     Messages.convert(dependency.getKey())
/*     */                   });
/*     */             }
/*     */             return;
/*     */         } 
/*     */ 
/*     */       
/*     */       }
/*     */     } 
/* 185 */     String parameterName = (dependency.getParameterIndex() != -1) ? SourceFormatter.getParameterName(dependency) : "";
/*     */     
/* 187 */     Object memberStackTraceElement = StackTraceElements.forMember(dependency.getInjectionPoint().getMember());
/*     */ 
/*     */ 
/*     */     
/* 191 */     String str1 = String.valueOf(memberStackTraceElement); Object formattedDependency = parameterName.isEmpty() ? memberStackTraceElement : (new StringBuilder(8 + String.valueOf(parameterName).length() + String.valueOf(str1).length())).append("the ").append(parameterName).append(" of ").append(str1).toString();
/* 192 */     throw create(ErrorId.NULL_INJECTED_INTO_NON_NULLABLE, "null returned by binding at %s%n but %s is not @Nullable", new Object[] { source, formattedDependency
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 197 */         }).addSource(source);
/*     */   }
/*     */   
/* 200 */   private final List<Object> sourcesToPrepend = new ArrayList();
/*     */   private final ImmutableList<Message> errors;
/*     */   
/*     */   InternalProvisionException(Message error) {
/* 204 */     this((Iterable<Message>)ImmutableList.of(error));
/*     */   }
/*     */   
/*     */   private InternalProvisionException(Iterable<Message> errors) {
/* 208 */     this.errors = ImmutableList.copyOf(errors);
/* 209 */     Preconditions.checkArgument(!this.errors.isEmpty(), "Can't create a provision exception with no errors");
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
/*     */   InternalProvisionException addSource(Object source) {
/* 224 */     if (source == SourceProvider.UNKNOWN_SOURCE) {
/* 225 */       return this;
/*     */     }
/* 227 */     int sz = this.sourcesToPrepend.size();
/* 228 */     if (sz > 0 && this.sourcesToPrepend.get(sz - 1) == source)
/*     */     {
/*     */ 
/*     */       
/* 232 */       return this;
/*     */     }
/* 234 */     this.sourcesToPrepend.add(source);
/* 235 */     return this;
/*     */   }
/*     */   
/*     */   ImmutableList<Message> getErrors() {
/* 239 */     ImmutableList.Builder<Message> builder = ImmutableList.builder();
/*     */ 
/*     */     
/* 242 */     List<Object> newSources = Lists.reverse(this.sourcesToPrepend);
/* 243 */     for (UnmodifiableIterator<Message> unmodifiableIterator = this.errors.iterator(); unmodifiableIterator.hasNext(); ) { Message error = unmodifiableIterator.next();
/* 244 */       builder.add(Messages.mergeSources(newSources, error)); }
/*     */     
/* 246 */     return builder.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public ProvisionException toProvisionException() {
/* 251 */     ProvisionException exception = new ProvisionException((Iterable)getErrors());
/* 252 */     return exception;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\InternalProvisionException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */