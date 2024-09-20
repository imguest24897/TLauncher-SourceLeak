/*     */ package com.google.inject.spi;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.inject.Binding;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.RestrictedBindingSource;
/*     */ import com.google.inject.internal.Errors;
/*     */ import com.google.inject.internal.GuiceInternal;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Arrays;
/*     */ import java.util.Deque;
/*     */ import java.util.Formatter;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BindingSourceRestriction
/*     */ {
/*  53 */   private static final Logger logger = Logger.getLogger(RestrictedBindingSource.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Optional<String> getMissingImplementationSuggestion(GuiceInternal guiceInternal, Key<?> key) {
/*  65 */     Preconditions.checkNotNull(guiceInternal);
/*  66 */     RestrictedBindingSource restriction = getRestriction(key);
/*  67 */     if (restriction == null) {
/*  68 */       return Optional.empty();
/*     */     }
/*  70 */     return Optional.of(
/*  71 */         String.format("%nHint: This key is restricted and cannot be bound directly. Restriction explanation: %s", new Object[] {
/*     */ 
/*     */             
/*  74 */             restriction.explanation()
/*     */           }));
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
/*     */   public static ImmutableList<Message> check(GuiceInternal guiceInternal, List<Element> elements) {
/*  93 */     Preconditions.checkNotNull(guiceInternal);
/*  94 */     ImmutableList<Message> errorMessages = check(elements);
/*     */     
/*  96 */     elements.forEach(BindingSourceRestriction::clear);
/*  97 */     return errorMessages;
/*     */   }
/*     */   
/*     */   private static ImmutableList<Message> check(List<Element> elements) {
/* 101 */     ImmutableList.Builder<Message> errorMessagesBuilder = ImmutableList.builder();
/* 102 */     for (Element element : elements) {
/* 103 */       errorMessagesBuilder.addAll((Iterable)check(element));
/*     */     }
/* 105 */     return errorMessagesBuilder.build();
/*     */   }
/*     */   
/*     */   private static ImmutableList<Message> check(Element element) {
/* 109 */     return element.<ImmutableList<Message>>acceptVisitor(new DefaultElementVisitor<ImmutableList<Message>>()
/*     */         {
/*     */           
/*     */           protected ImmutableList<Message> visitOther(Element element)
/*     */           {
/* 114 */             return ImmutableList.of();
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> ImmutableList<Message> visit(Binding<T> binding) {
/* 119 */             Optional<Message> errorMessage = BindingSourceRestriction.check(binding);
/* 120 */             if (errorMessage.isPresent()) {
/* 121 */               return ImmutableList.of(errorMessage.get());
/*     */             }
/* 123 */             return ImmutableList.of();
/*     */           }
/*     */ 
/*     */           
/*     */           public ImmutableList<Message> visit(PrivateElements privateElements) {
/* 128 */             return BindingSourceRestriction.check(privateElements.getElements());
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private static Optional<Message> check(Binding<?> binding) {
/* 134 */     Key<?> key = binding.getKey();
/*     */     
/* 136 */     ElementSource elementSource = (ElementSource)binding.getSource();
/* 137 */     RestrictedBindingSource restriction = getRestriction(key);
/* 138 */     if (restriction == null) {
/* 139 */       return Optional.empty();
/*     */     }
/* 141 */     ImmutableSet<Class<? extends Annotation>> permits = getAllPermits(elementSource);
/*     */     
/* 143 */     ImmutableSet<Class<? extends Annotation>> acceptablePermits = ImmutableSet.copyOf((Object[])restriction.permits());
/* 144 */     Objects.requireNonNull(acceptablePermits); boolean bindingPermitted = permits.stream().anyMatch(acceptablePermits::contains);
/* 145 */     if (bindingPermitted || isExempt(elementSource, restriction.exemptModules())) {
/* 146 */       return Optional.empty();
/*     */     }
/*     */     
/* 149 */     String violationMessage = getViolationMessage(key, restriction
/* 150 */         .explanation(), acceptablePermits, (key.getAnnotationType() != null));
/* 151 */     if (restriction.restrictionLevel() == RestrictedBindingSource.RestrictionLevel.WARNING) {
/* 152 */       Formatter sourceFormatter = new Formatter();
/* 153 */       Errors.formatSource(sourceFormatter, elementSource);
/* 154 */       String str = String.valueOf(sourceFormatter); logger.log(Level.WARNING, (new StringBuilder(1 + String.valueOf(violationMessage).length() + String.valueOf(str).length())).append(violationMessage).append("\n").append(str).toString());
/* 155 */       return Optional.empty();
/*     */     } 
/* 157 */     return Optional.of(new Message(elementSource, violationMessage));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getViolationMessage(Key<?> key, String explanation, ImmutableSet<Class<? extends Annotation>> acceptablePermits, boolean annotationRestricted) {
/* 165 */     return String.format("Unable to bind key: %s. One of the modules that created this binding has to be annotated with one of %s, because the key's %s is annotated with @RestrictedBindingSource. %s", new Object[] { key, acceptablePermits
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 170 */           .stream().map(a -> { String.valueOf(a.getName()); return (String.valueOf(a.getName()).length() != 0) ? "@".concat(String.valueOf(a.getName())) : new String("@"); }).collect(Collectors.toList()), 
/* 171 */           annotationRestricted ? "annotation" : "type", explanation });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ImmutableSet<Class<? extends Annotation>> getAllPermits(ElementSource elementSource) {
/* 178 */     ImmutableSet.Builder<Class<? extends Annotation>> permitsBuilder = ImmutableSet.builder();
/* 179 */     permitsBuilder.addAll((Iterable)elementSource.moduleSource.getPermitMap().getPermits(elementSource));
/* 180 */     if (elementSource.scanner != null) {
/* 181 */       Objects.requireNonNull(permitsBuilder); getPermits(elementSource.scanner.getClass()).forEach(permitsBuilder::add);
/*     */     } 
/* 183 */     if (elementSource.getOriginalElementSource() != null && elementSource.trustedOriginalElementSource)
/*     */     {
/* 185 */       permitsBuilder.addAll((Iterable)getAllPermits(elementSource.getOriginalElementSource()));
/*     */     }
/* 187 */     return permitsBuilder.build();
/*     */   }
/*     */   
/*     */   private static boolean isExempt(ElementSource elementSource, String exemptModulesRegex) {
/* 191 */     if (exemptModulesRegex.isEmpty()) {
/* 192 */       return false;
/*     */     }
/* 194 */     Pattern exemptModulePattern = Pattern.compile(exemptModulesRegex);
/*     */     
/* 196 */     return StreamSupport.stream(getAllModules(elementSource).spliterator(), false)
/* 197 */       .anyMatch(moduleName -> exemptModulePattern.matcher(moduleName).matches());
/*     */   }
/*     */   
/*     */   private static Iterable<String> getAllModules(ElementSource elementSource) {
/* 201 */     List<String> modules = elementSource.getModuleClassNames();
/* 202 */     if (elementSource.getOriginalElementSource() == null || !elementSource.trustedOriginalElementSource)
/*     */     {
/* 204 */       return modules;
/*     */     }
/* 206 */     return Iterables.concat(modules, getAllModules(elementSource.getOriginalElementSource()));
/*     */   }
/*     */   
/*     */   private static void clear(Element element) {
/* 210 */     element.acceptVisitor(new DefaultElementVisitor<Void>()
/*     */         {
/*     */           
/*     */           protected Void visitOther(Element element)
/*     */           {
/* 215 */             Object source = element.getSource();
/*     */             
/* 217 */             if (source instanceof ElementSource) {
/* 218 */               BindingSourceRestriction.clear((ElementSource)source);
/*     */             }
/* 220 */             return null;
/*     */           }
/*     */ 
/*     */           
/*     */           public Void visit(PrivateElements privateElements) {
/* 225 */             privateElements.getElements().forEach(x$0 -> BindingSourceRestriction.clear(x$0));
/* 226 */             return null;
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private static void clear(ElementSource elementSource) {
/* 232 */     while (elementSource != null) {
/* 233 */       elementSource.moduleSource.getPermitMap().clear();
/* 234 */       elementSource = elementSource.getOriginalElementSource();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static RestrictedBindingSource getRestriction(Key<?> key) {
/* 245 */     return (key.getAnnotationType() == null) ? 
/* 246 */       (RestrictedBindingSource)key.getTypeLiteral().getRawType().getAnnotation(RestrictedBindingSource.class) : 
/* 247 */       (RestrictedBindingSource)key.getAnnotationType().getAnnotation(RestrictedBindingSource.class);
/*     */   }
/*     */ 
/*     */   
/*     */   static interface PermitMap
/*     */   {
/*     */     ImmutableSet<Class<? extends Annotation>> getPermits(ElementSource param1ElementSource);
/*     */     
/*     */     void clear();
/*     */   }
/*     */   
/*     */   static final class PermitMapConstruction
/*     */   {
/*     */     private static final class PermitMapImpl
/*     */       implements BindingSourceRestriction.PermitMap
/*     */     {
/*     */       Map<ModuleSource, ImmutableSet<Class<? extends Annotation>>> modulePermits;
/*     */       
/*     */       private PermitMapImpl() {}
/*     */       
/*     */       public ImmutableSet<Class<? extends Annotation>> getPermits(ElementSource elementSource) {
/* 268 */         return this.modulePermits.get(elementSource.moduleSource);
/*     */       }
/*     */ 
/*     */       
/*     */       public void clear() {
/* 273 */         this.modulePermits = null;
/*     */       }
/*     */     }
/*     */     
/* 277 */     final Map<ModuleSource, ImmutableSet<Class<? extends Annotation>>> modulePermits = new HashMap<>();
/*     */ 
/*     */     
/* 280 */     ImmutableSet<Class<? extends Annotation>> currentModulePermits = ImmutableSet.of();
/*     */     
/* 282 */     final Deque<ImmutableSet<Class<? extends Annotation>>> modulePermitsStack = new ArrayDeque<>();
/*     */     
/* 284 */     final PermitMapImpl permitMap = new PermitMapImpl();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     BindingSourceRestriction.PermitMap getPermitMap() {
/* 291 */       return this.permitMap;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void restoreCurrentModulePermits(ModuleSource moduleSource) {
/* 300 */       this.currentModulePermits = this.modulePermits.get(moduleSource);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void pushModule(Class<?> module, ModuleSource moduleSource) {
/* 308 */       List<Class<? extends Annotation>> newModulePermits = (List<Class<? extends Annotation>>)BindingSourceRestriction.getPermits(module).filter(permit -> !this.currentModulePermits.contains(permit)).collect(Collectors.toList());
/*     */ 
/*     */       
/* 311 */       this.modulePermitsStack.push(this.currentModulePermits);
/* 312 */       if (!newModulePermits.isEmpty()) {
/* 313 */         this
/*     */ 
/*     */ 
/*     */           
/* 317 */           .currentModulePermits = ImmutableSet.builder().addAll((Iterable)this.currentModulePermits).addAll(newModulePermits).build();
/*     */       }
/* 319 */       this.modulePermits.put(moduleSource, this.currentModulePermits);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void popModule() {
/* 325 */       this.currentModulePermits = this.modulePermitsStack.pop();
/*     */     }
/*     */ 
/*     */     
/*     */     void finish() {
/* 330 */       this.permitMap.modulePermits = this.modulePermits;
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     static boolean isElementSourceCleared(ElementSource elementSource) {
/* 335 */       PermitMapImpl permitMap = (PermitMapImpl)elementSource.moduleSource.getPermitMap();
/* 336 */       return (permitMap.modulePermits == null);
/*     */     }
/*     */   }
/*     */   
/*     */   private static Stream<Class<? extends Annotation>> getPermits(Class<?> clazz) {
/* 341 */     Stream<Annotation> annotations = Arrays.stream(clazz.getAnnotations());
/*     */     
/* 343 */     if (clazz.getAnnotatedSuperclass() != null)
/*     */     {
/* 345 */       annotations = Stream.concat(annotations, 
/* 346 */           Arrays.stream(clazz.getAnnotatedSuperclass().getAnnotations()));
/*     */     }
/* 348 */     return annotations
/* 349 */       .<Class<? extends Annotation>>map(Annotation::annotationType)
/* 350 */       .filter(a -> a.isAnnotationPresent((Class)RestrictedBindingSource.Permit.class));
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\spi\BindingSourceRestriction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */