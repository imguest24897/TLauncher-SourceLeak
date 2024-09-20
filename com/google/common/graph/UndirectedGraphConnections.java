/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Iterators;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
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
/*     */ final class UndirectedGraphConnections<N, V>
/*     */   implements GraphConnections<N, V>
/*     */ {
/*     */   private final Map<N, V> adjacentNodeValues;
/*     */   
/*     */   private UndirectedGraphConnections(Map<N, V> adjacentNodeValues) {
/*  44 */     this.adjacentNodeValues = (Map<N, V>)Preconditions.checkNotNull(adjacentNodeValues);
/*     */   }
/*     */   
/*     */   static <N, V> UndirectedGraphConnections<N, V> of(ElementOrder<N> incidentEdgeOrder) {
/*  48 */     switch (incidentEdgeOrder.type()) {
/*     */       case UNORDERED:
/*  50 */         return new UndirectedGraphConnections<>(new HashMap<>(2, 1.0F));
/*     */       
/*     */       case STABLE:
/*  53 */         return new UndirectedGraphConnections<>(new LinkedHashMap<>(2, 1.0F));
/*     */     } 
/*     */     
/*  56 */     throw new AssertionError(incidentEdgeOrder.type());
/*     */   }
/*     */ 
/*     */   
/*     */   static <N, V> UndirectedGraphConnections<N, V> ofImmutable(Map<N, V> adjacentNodeValues) {
/*  61 */     return new UndirectedGraphConnections<>((Map<N, V>)ImmutableMap.copyOf(adjacentNodeValues));
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> adjacentNodes() {
/*  66 */     return Collections.unmodifiableSet(this.adjacentNodeValues.keySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> predecessors() {
/*  71 */     return adjacentNodes();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> successors() {
/*  76 */     return adjacentNodes();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<EndpointPair<N>> incidentEdgeIterator(final N thisNode) {
/*  81 */     return Iterators.transform(this.adjacentNodeValues
/*  82 */         .keySet().iterator(), new Function<N, EndpointPair<N>>(this)
/*     */         {
/*     */           public EndpointPair<N> apply(N incidentNode)
/*     */           {
/*  86 */             return EndpointPair.unordered((N)thisNode, incidentNode);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public V value(N node) {
/*  93 */     return this.adjacentNodeValues.get(node);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removePredecessor(N node) {
/*  99 */     V unused = removeSuccessor(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public V removeSuccessor(N node) {
/* 104 */     return this.adjacentNodeValues.remove(node);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPredecessor(N node, V value) {
/* 110 */     V unused = addSuccessor(node, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public V addSuccessor(N node, V value) {
/* 115 */     return this.adjacentNodeValues.put(node, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\graph\UndirectedGraphConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */