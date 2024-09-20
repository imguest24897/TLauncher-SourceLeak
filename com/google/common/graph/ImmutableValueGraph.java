/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.Immutable;
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
/*     */ @Immutable(containerOf = {"N", "V"})
/*     */ @Beta
/*     */ public final class ImmutableValueGraph<N, V>
/*     */   extends StandardValueGraph<N, V>
/*     */ {
/*     */   private ImmutableValueGraph(ValueGraph<N, V> graph) {
/*  49 */     super(ValueGraphBuilder.from(graph), (Map<N, GraphConnections<N, V>>)getNodeConnections(graph), graph.edges().size());
/*     */   }
/*     */ 
/*     */   
/*     */   public static <N, V> ImmutableValueGraph<N, V> copyOf(ValueGraph<N, V> graph) {
/*  54 */     return (graph instanceof ImmutableValueGraph) ? 
/*  55 */       (ImmutableValueGraph<N, V>)graph : 
/*  56 */       new ImmutableValueGraph<>(graph);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static <N, V> ImmutableValueGraph<N, V> copyOf(ImmutableValueGraph<N, V> graph) {
/*  66 */     return (ImmutableValueGraph<N, V>)Preconditions.checkNotNull(graph);
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementOrder<N> incidentEdgeOrder() {
/*  71 */     return ElementOrder.stable();
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableGraph<N> asGraph() {
/*  76 */     return new ImmutableGraph<>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <N, V> ImmutableMap<N, GraphConnections<N, V>> getNodeConnections(ValueGraph<N, V> graph) {
/*  84 */     ImmutableMap.Builder<N, GraphConnections<N, V>> nodeConnections = ImmutableMap.builder();
/*  85 */     for (N node : graph.nodes()) {
/*  86 */       nodeConnections.put(node, connectionsOf(graph, node));
/*     */     }
/*  88 */     return nodeConnections.build();
/*     */   }
/*     */ 
/*     */   
/*     */   private static <N, V> GraphConnections<N, V> connectionsOf(final ValueGraph<N, V> graph, final N node) {
/*  93 */     Function<N, V> successorNodeToValueFn = new Function<N, V>()
/*     */       {
/*     */         public V apply(N successorNode)
/*     */         {
/*  97 */           return graph.edgeValueOrDefault(node, successorNode, null);
/*     */         }
/*     */       };
/* 100 */     return graph.isDirected() ? 
/* 101 */       DirectedGraphConnections.<N, V>ofImmutable(node, graph
/* 102 */         .incidentEdges(node), successorNodeToValueFn) : 
/* 103 */       UndirectedGraphConnections.<N, V>ofImmutable(
/* 104 */         Maps.asMap(graph.adjacentNodes(node), successorNodeToValueFn));
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
/*     */   public static class Builder<N, V>
/*     */   {
/*     */     private final MutableValueGraph<N, V> mutableValueGraph;
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
/*     */     Builder(ValueGraphBuilder<N, V> graphBuilder) {
/* 135 */       this
/* 136 */         .mutableValueGraph = graphBuilder.copy().incidentEdgeOrder(ElementOrder.stable()).build();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<N, V> addNode(N node) {
/* 148 */       this.mutableValueGraph.addNode(node);
/* 149 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<N, V> putEdgeValue(N nodeU, N nodeV, V value) {
/* 170 */       this.mutableValueGraph.putEdgeValue(nodeU, nodeV, value);
/* 171 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<N, V> putEdgeValue(EndpointPair<N> endpoints, V value) {
/* 195 */       this.mutableValueGraph.putEdgeValue(endpoints, value);
/* 196 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableValueGraph<N, V> build() {
/* 204 */       return ImmutableValueGraph.copyOf(this.mutableValueGraph);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\graph\ImmutableValueGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */