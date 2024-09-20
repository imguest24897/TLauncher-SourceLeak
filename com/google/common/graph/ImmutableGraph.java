/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Functions;
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
/*     */ 
/*     */ @Immutable(containerOf = {"N"})
/*     */ @Beta
/*     */ public class ImmutableGraph<N>
/*     */   extends ForwardingGraph<N>
/*     */ {
/*     */   private final BaseGraph<N> backingGraph;
/*     */   
/*     */   ImmutableGraph(BaseGraph<N> backingGraph) {
/*  53 */     this.backingGraph = backingGraph;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <N> ImmutableGraph<N> copyOf(Graph<N> graph) {
/*  58 */     return (graph instanceof ImmutableGraph) ? 
/*  59 */       (ImmutableGraph<N>)graph : 
/*  60 */       new ImmutableGraph<>(new StandardValueGraph<>(
/*     */           
/*  62 */           GraphBuilder.from(graph), (Map<N, GraphConnections<N, ?>>)getNodeConnections(graph), graph.edges().size()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static <N> ImmutableGraph<N> copyOf(ImmutableGraph<N> graph) {
/*  72 */     return (ImmutableGraph<N>)Preconditions.checkNotNull(graph);
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementOrder<N> incidentEdgeOrder() {
/*  77 */     return ElementOrder.stable();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <N> ImmutableMap<N, GraphConnections<N, GraphConstants.Presence>> getNodeConnections(Graph<N> graph) {
/*  85 */     ImmutableMap.Builder<N, GraphConnections<N, GraphConstants.Presence>> nodeConnections = ImmutableMap.builder();
/*  86 */     for (N node : graph.nodes()) {
/*  87 */       nodeConnections.put(node, connectionsOf(graph, node));
/*     */     }
/*  89 */     return nodeConnections.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <N> GraphConnections<N, GraphConstants.Presence> connectionsOf(Graph<N> graph, N node) {
/*  95 */     Function<N, GraphConstants.Presence> edgeValueFn = Functions.constant(GraphConstants.Presence.EDGE_EXISTS);
/*  96 */     return graph.isDirected() ? 
/*  97 */       DirectedGraphConnections.<N, GraphConstants.Presence>ofImmutable(node, graph.incidentEdges(node), edgeValueFn) : 
/*  98 */       UndirectedGraphConnections.<N, GraphConstants.Presence>ofImmutable(
/*  99 */         Maps.asMap(graph.adjacentNodes(node), edgeValueFn));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BaseGraph<N> delegate() {
/* 104 */     return this.backingGraph;
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
/*     */   public static class Builder<N>
/*     */   {
/*     */     private final MutableGraph<N> mutableGraph;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Builder(GraphBuilder<N> graphBuilder) {
/* 135 */       this.mutableGraph = graphBuilder.copy().incidentEdgeOrder(ElementOrder.stable()).build();
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
/*     */     public Builder<N> addNode(N node) {
/* 147 */       this.mutableGraph.addNode(node);
/* 148 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<N> putEdge(N nodeU, N nodeV) {
/* 166 */       this.mutableGraph.putEdge(nodeU, nodeV);
/* 167 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<N> putEdge(EndpointPair<N> endpoints) {
/* 189 */       this.mutableGraph.putEdge(endpoints);
/* 190 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableGraph<N> build() {
/* 197 */       return ImmutableGraph.copyOf(this.mutableGraph);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\graph\ImmutableGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */