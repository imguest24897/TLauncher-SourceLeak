/*     */ package com.google.common.eventbus;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.google.common.collect.HashMultimap;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.common.primitives.Primitives;
/*     */ import com.google.common.reflect.TypeToken;
/*     */ import com.google.common.util.concurrent.UncheckedExecutionException;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.CopyOnWriteArraySet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class SubscriberRegistry
/*     */ {
/*  65 */   private final ConcurrentMap<Class<?>, CopyOnWriteArraySet<Subscriber>> subscribers = Maps.newConcurrentMap();
/*     */   
/*     */   @Weak
/*     */   private final EventBus bus;
/*     */   
/*     */   SubscriberRegistry(EventBus bus) {
/*  71 */     this.bus = (EventBus)Preconditions.checkNotNull(bus);
/*     */   }
/*     */ 
/*     */   
/*     */   void register(Object listener) {
/*  76 */     Multimap<Class<?>, Subscriber> listenerMethods = findAllSubscribers(listener);
/*     */     
/*  78 */     for (Map.Entry<Class<?>, Collection<Subscriber>> entry : (Iterable<Map.Entry<Class<?>, Collection<Subscriber>>>)listenerMethods.asMap().entrySet()) {
/*  79 */       Class<?> eventType = entry.getKey();
/*  80 */       Collection<Subscriber> eventMethodsInListener = entry.getValue();
/*     */       
/*  82 */       CopyOnWriteArraySet<Subscriber> eventSubscribers = this.subscribers.get(eventType);
/*     */       
/*  84 */       if (eventSubscribers == null) {
/*  85 */         CopyOnWriteArraySet<Subscriber> newSet = new CopyOnWriteArraySet<>();
/*     */         
/*  87 */         eventSubscribers = (CopyOnWriteArraySet<Subscriber>)MoreObjects.firstNonNull(this.subscribers.putIfAbsent(eventType, newSet), newSet);
/*     */       } 
/*     */       
/*  90 */       eventSubscribers.addAll(eventMethodsInListener);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void unregister(Object listener) {
/*  96 */     Multimap<Class<?>, Subscriber> listenerMethods = findAllSubscribers(listener);
/*     */     
/*  98 */     for (Map.Entry<Class<?>, Collection<Subscriber>> entry : (Iterable<Map.Entry<Class<?>, Collection<Subscriber>>>)listenerMethods.asMap().entrySet()) {
/*  99 */       Class<?> eventType = entry.getKey();
/* 100 */       Collection<Subscriber> listenerMethodsForType = entry.getValue();
/*     */       
/* 102 */       CopyOnWriteArraySet<Subscriber> currentSubscribers = this.subscribers.get(eventType);
/* 103 */       if (currentSubscribers == null || !currentSubscribers.removeAll(listenerMethodsForType)) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 108 */         String str = String.valueOf(listener); throw new IllegalArgumentException((new StringBuilder(65 + String.valueOf(str).length())).append("missing event subscriber for an annotated method. Is ").append(str).append(" registered?").toString());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   Set<Subscriber> getSubscribersForTesting(Class<?> eventType) {
/* 119 */     return (Set<Subscriber>)MoreObjects.firstNonNull(this.subscribers.get(eventType), ImmutableSet.of());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Iterator<Subscriber> getSubscribers(Object event) {
/* 127 */     ImmutableSet<Class<?>> eventTypes = flattenHierarchy(event.getClass());
/*     */ 
/*     */     
/* 130 */     List<Iterator<Subscriber>> subscriberIterators = Lists.newArrayListWithCapacity(eventTypes.size());
/*     */     
/* 132 */     for (UnmodifiableIterator<Class<?>> unmodifiableIterator = eventTypes.iterator(); unmodifiableIterator.hasNext(); ) { Class<?> eventType = unmodifiableIterator.next();
/* 133 */       CopyOnWriteArraySet<Subscriber> eventSubscribers = this.subscribers.get(eventType);
/* 134 */       if (eventSubscribers != null)
/*     */       {
/* 136 */         subscriberIterators.add(eventSubscribers.iterator());
/*     */       } }
/*     */ 
/*     */     
/* 140 */     return Iterators.concat(subscriberIterators.iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 150 */   private static final LoadingCache<Class<?>, ImmutableList<Method>> subscriberMethodsCache = CacheBuilder.newBuilder()
/* 151 */     .weakKeys()
/* 152 */     .build(new CacheLoader<Class<?>, ImmutableList<Method>>()
/*     */       {
/*     */         public ImmutableList<Method> load(Class<?> concreteClass) throws Exception
/*     */         {
/* 156 */           return SubscriberRegistry.getAnnotatedMethodsNotCached(concreteClass);
/*     */         }
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Multimap<Class<?>, Subscriber> findAllSubscribers(Object listener) {
/* 164 */     HashMultimap hashMultimap = HashMultimap.create();
/* 165 */     Class<?> clazz = listener.getClass();
/* 166 */     for (UnmodifiableIterator<Method> unmodifiableIterator = getAnnotatedMethods(clazz).iterator(); unmodifiableIterator.hasNext(); ) { Method method = unmodifiableIterator.next();
/* 167 */       Class<?>[] parameterTypes = method.getParameterTypes();
/* 168 */       Class<?> eventType = parameterTypes[0];
/* 169 */       hashMultimap.put(eventType, Subscriber.create(this.bus, listener, method)); }
/*     */     
/* 171 */     return (Multimap<Class<?>, Subscriber>)hashMultimap;
/*     */   }
/*     */   
/*     */   private static ImmutableList<Method> getAnnotatedMethods(Class<?> clazz) {
/*     */     try {
/* 176 */       return (ImmutableList<Method>)subscriberMethodsCache.getUnchecked(clazz);
/* 177 */     } catch (UncheckedExecutionException e) {
/* 178 */       Throwables.throwIfUnchecked(e.getCause());
/* 179 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static ImmutableList<Method> getAnnotatedMethodsNotCached(Class<?> clazz) {
/* 184 */     Set<? extends Class<?>> supertypes = TypeToken.of(clazz).getTypes().rawTypes();
/* 185 */     Map<MethodIdentifier, Method> identifiers = Maps.newHashMap();
/* 186 */     for (Class<?> supertype : supertypes) {
/* 187 */       for (Method method : supertype.getDeclaredMethods()) {
/* 188 */         if (method.isAnnotationPresent((Class)Subscribe.class) && !method.isSynthetic()) {
/*     */           
/* 190 */           Class<?>[] parameterTypes = method.getParameterTypes();
/* 191 */           Preconditions.checkArgument((parameterTypes.length == 1), "Method %s has @Subscribe annotation but has %s parameters. Subscriber methods must have exactly 1 parameter.", method, parameterTypes.length);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 198 */           Preconditions.checkArgument(
/* 199 */               !parameterTypes[0].isPrimitive(), "@Subscribe method %s's parameter is %s. Subscriber methods cannot accept primitives. Consider changing the parameter to %s.", method, parameterTypes[0]
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 204 */               .getName(), 
/* 205 */               Primitives.wrap(parameterTypes[0]).getSimpleName());
/*     */           
/* 207 */           MethodIdentifier ident = new MethodIdentifier(method);
/* 208 */           if (!identifiers.containsKey(ident)) {
/* 209 */             identifiers.put(ident, method);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 214 */     return ImmutableList.copyOf(identifiers.values());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 219 */   private static final LoadingCache<Class<?>, ImmutableSet<Class<?>>> flattenHierarchyCache = CacheBuilder.newBuilder()
/* 220 */     .weakKeys()
/* 221 */     .build(new CacheLoader<Class<?>, ImmutableSet<Class<?>>>()
/*     */       {
/*     */ 
/*     */         
/*     */         public ImmutableSet<Class<?>> load(Class<?> concreteClass)
/*     */         {
/* 227 */           return ImmutableSet.copyOf(
/* 228 */               TypeToken.of(concreteClass).getTypes().rawTypes());
/*     */         }
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static ImmutableSet<Class<?>> flattenHierarchy(Class<?> concreteClass) {
/*     */     try {
/* 239 */       return (ImmutableSet<Class<?>>)flattenHierarchyCache.getUnchecked(concreteClass);
/* 240 */     } catch (UncheckedExecutionException e) {
/* 241 */       throw Throwables.propagate(e.getCause());
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final class MethodIdentifier
/*     */   {
/*     */     private final String name;
/*     */     private final List<Class<?>> parameterTypes;
/*     */     
/*     */     MethodIdentifier(Method method) {
/* 251 */       this.name = method.getName();
/* 252 */       this.parameterTypes = Arrays.asList(method.getParameterTypes());
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 257 */       return Objects.hashCode(new Object[] { this.name, this.parameterTypes });
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 262 */       if (o instanceof MethodIdentifier) {
/* 263 */         MethodIdentifier ident = (MethodIdentifier)o;
/* 264 */         return (this.name.equals(ident.name) && this.parameterTypes.equals(ident.parameterTypes));
/*     */       } 
/* 266 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\eventbus\SubscriberRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */