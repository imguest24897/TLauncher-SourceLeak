/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.inject.Binder;
/*     */ import com.google.inject.Injector;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.PrivateBinder;
/*     */ import com.google.inject.spi.Element;
/*     */ import com.google.inject.spi.ElementVisitor;
/*     */ import com.google.inject.spi.PrivateElements;
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
/*     */ public final class PrivateElementsImpl
/*     */   implements PrivateElements
/*     */ {
/*     */   private final Object source;
/*  50 */   private List<Element> elementsMutable = Lists.newArrayList();
/*  51 */   private List<ExposureBuilder<?>> exposureBuilders = Lists.newArrayList();
/*     */ 
/*     */   
/*     */   private ImmutableList<Element> elements;
/*     */   
/*     */   private ImmutableMap<Key<?>, Object> exposedKeysToSources;
/*     */   
/*     */   private Injector injector;
/*     */ 
/*     */   
/*     */   public PrivateElementsImpl(Object source) {
/*  62 */     this.source = Preconditions.checkNotNull(source, "source");
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getSource() {
/*  67 */     return this.source;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Element> getElements() {
/*  72 */     if (this.elements == null) {
/*  73 */       this.elements = ImmutableList.copyOf(this.elementsMutable);
/*  74 */       this.elementsMutable = null;
/*     */     } 
/*     */     
/*  77 */     return (List<Element>)this.elements;
/*     */   }
/*     */ 
/*     */   
/*     */   public Injector getInjector() {
/*  82 */     return this.injector;
/*     */   }
/*     */   
/*     */   public void initInjector(Injector injector) {
/*  86 */     Preconditions.checkState((this.injector == null), "injector already initialized");
/*  87 */     this.injector = (Injector)Preconditions.checkNotNull(injector, "injector");
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Key<?>> getExposedKeys() {
/*  92 */     if (this.exposedKeysToSources == null) {
/*  93 */       Map<Key<?>, Object> exposedKeysToSourcesMutable = Maps.newLinkedHashMap();
/*  94 */       for (ExposureBuilder<?> exposureBuilder : this.exposureBuilders) {
/*  95 */         exposedKeysToSourcesMutable.put(exposureBuilder.getKey(), exposureBuilder.getSource());
/*     */       }
/*  97 */       this.exposedKeysToSources = ImmutableMap.copyOf(exposedKeysToSourcesMutable);
/*  98 */       this.exposureBuilders = null;
/*     */     } 
/*     */     
/* 101 */     return (Set<Key<?>>)this.exposedKeysToSources.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T acceptVisitor(ElementVisitor<T> visitor) {
/* 106 */     return (T)visitor.visit(this);
/*     */   }
/*     */   
/*     */   public List<Element> getElementsMutable() {
/* 110 */     return this.elementsMutable;
/*     */   }
/*     */   
/*     */   public void addExposureBuilder(ExposureBuilder<?> exposureBuilder) {
/* 114 */     this.exposureBuilders.add(exposureBuilder);
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyTo(Binder binder) {
/* 119 */     PrivateBinder privateBinder = binder.withSource(this.source).newPrivateBinder();
/*     */     
/* 121 */     for (Element element : getElements()) {
/* 122 */       element.applyTo((Binder)privateBinder);
/*     */     }
/*     */     
/* 125 */     getExposedKeys();
/* 126 */     for (UnmodifiableIterator<Map.Entry<Key<?>, Object>> unmodifiableIterator = this.exposedKeysToSources.entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<Key<?>, Object> entry = unmodifiableIterator.next();
/* 127 */       privateBinder.withSource(entry.getValue()).expose(entry.getKey()); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getExposedSource(Key<?> key) {
/* 133 */     getExposedKeys();
/* 134 */     Object source = this.exposedKeysToSources.get(key);
/* 135 */     Preconditions.checkArgument((source != null), "%s not exposed by %s.", key, this);
/* 136 */     return source;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 141 */     return MoreObjects.toStringHelper(PrivateElements.class)
/* 142 */       .add("exposedKeys", getExposedKeys())
/* 143 */       .add("source", getSource())
/* 144 */       .toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\PrivateElementsImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */