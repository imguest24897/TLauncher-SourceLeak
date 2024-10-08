/*     */ package com.google.common.graph;
/*     */ 
/*     */ import java.util.Optional;
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
/*     */ abstract class ForwardingValueGraph<N, V>
/*     */   extends AbstractValueGraph<N, V>
/*     */ {
/*     */   public Set<N> nodes() {
/*  36 */     return delegate().nodes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long edgeCount() {
/*  45 */     return delegate().edges().size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDirected() {
/*  50 */     return delegate().isDirected();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean allowsSelfLoops() {
/*  55 */     return delegate().allowsSelfLoops();
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementOrder<N> nodeOrder() {
/*  60 */     return delegate().nodeOrder();
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementOrder<N> incidentEdgeOrder() {
/*  65 */     return delegate().incidentEdgeOrder();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> adjacentNodes(N node) {
/*  70 */     return delegate().adjacentNodes(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> predecessors(N node) {
/*  75 */     return delegate().predecessors(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> successors(N node) {
/*  80 */     return delegate().successors(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public int degree(N node) {
/*  85 */     return delegate().degree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public int inDegree(N node) {
/*  90 */     return delegate().inDegree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public int outDegree(N node) {
/*  95 */     return delegate().outDegree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(N nodeU, N nodeV) {
/* 100 */     return delegate().hasEdgeConnecting(nodeU, nodeV);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
/* 105 */     return delegate().hasEdgeConnecting(endpoints);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<V> edgeValue(N nodeU, N nodeV) {
/* 110 */     return delegate().edgeValue(nodeU, nodeV);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<V> edgeValue(EndpointPair<N> endpoints) {
/* 115 */     return delegate().edgeValue(endpoints);
/*     */   }
/*     */ 
/*     */   
/*     */   public V edgeValueOrDefault(N nodeU, N nodeV, V defaultValue) {
/* 120 */     return delegate().edgeValueOrDefault(nodeU, nodeV, defaultValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public V edgeValueOrDefault(EndpointPair<N> endpoints, V defaultValue) {
/* 125 */     return delegate().edgeValueOrDefault(endpoints, defaultValue);
/*     */   }
/*     */   
/*     */   protected abstract ValueGraph<N, V> delegate();
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\graph\ForwardingValueGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */