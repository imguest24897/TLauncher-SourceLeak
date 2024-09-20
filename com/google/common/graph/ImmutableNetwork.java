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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable(containerOf = {"N", "E"})
/*     */ @Beta
/*     */ public final class ImmutableNetwork<N, E>
/*     */   extends StandardNetwork<N, E>
/*     */ {
/*     */   private ImmutableNetwork(Network<N, E> network) {
/*  52 */     super(
/*  53 */         NetworkBuilder.from(network), getNodeConnections(network), getEdgeToReferenceNode(network));
/*     */   }
/*     */ 
/*     */   
/*     */   public static <N, E> ImmutableNetwork<N, E> copyOf(Network<N, E> network) {
/*  58 */     return (network instanceof ImmutableNetwork) ? 
/*  59 */       (ImmutableNetwork<N, E>)network : 
/*  60 */       new ImmutableNetwork<>(network);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static <N, E> ImmutableNetwork<N, E> copyOf(ImmutableNetwork<N, E> network) {
/*  70 */     return (ImmutableNetwork<N, E>)Preconditions.checkNotNull(network);
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableGraph<N> asGraph() {
/*  75 */     return new ImmutableGraph<>(super.asGraph());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <N, E> Map<N, NetworkConnections<N, E>> getNodeConnections(Network<N, E> network) {
/*  82 */     ImmutableMap.Builder<N, NetworkConnections<N, E>> nodeConnections = ImmutableMap.builder();
/*  83 */     for (N node : network.nodes()) {
/*  84 */       nodeConnections.put(node, connectionsOf(network, node));
/*     */     }
/*  86 */     return (Map<N, NetworkConnections<N, E>>)nodeConnections.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <N, E> Map<E, N> getEdgeToReferenceNode(Network<N, E> network) {
/*  93 */     ImmutableMap.Builder<E, N> edgeToReferenceNode = ImmutableMap.builder();
/*  94 */     for (E edge : network.edges()) {
/*  95 */       edgeToReferenceNode.put(edge, network.incidentNodes(edge).nodeU());
/*     */     }
/*  97 */     return (Map<E, N>)edgeToReferenceNode.build();
/*     */   }
/*     */   
/*     */   private static <N, E> NetworkConnections<N, E> connectionsOf(Network<N, E> network, N node) {
/* 101 */     if (network.isDirected()) {
/* 102 */       Map<E, N> inEdgeMap = Maps.asMap(network.inEdges(node), sourceNodeFn(network));
/* 103 */       Map<E, N> outEdgeMap = Maps.asMap(network.outEdges(node), targetNodeFn(network));
/* 104 */       int selfLoopCount = network.edgesConnecting(node, node).size();
/* 105 */       return network.allowsParallelEdges() ? 
/* 106 */         DirectedMultiNetworkConnections.<N, E>ofImmutable(inEdgeMap, outEdgeMap, selfLoopCount) : 
/* 107 */         DirectedNetworkConnections.<N, E>ofImmutable(inEdgeMap, outEdgeMap, selfLoopCount);
/*     */     } 
/*     */     
/* 110 */     Map<E, N> incidentEdgeMap = Maps.asMap(network.incidentEdges(node), adjacentNodeFn(network, node));
/* 111 */     return network.allowsParallelEdges() ? 
/* 112 */       UndirectedMultiNetworkConnections.<N, E>ofImmutable(incidentEdgeMap) : 
/* 113 */       UndirectedNetworkConnections.<N, E>ofImmutable(incidentEdgeMap);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <N, E> Function<E, N> sourceNodeFn(final Network<N, E> network) {
/* 118 */     return new Function<E, N>()
/*     */       {
/*     */         public N apply(E edge) {
/* 121 */           return network.incidentNodes(edge).source();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static <N, E> Function<E, N> targetNodeFn(final Network<N, E> network) {
/* 127 */     return new Function<E, N>()
/*     */       {
/*     */         public N apply(E edge) {
/* 130 */           return network.incidentNodes(edge).target();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static <N, E> Function<E, N> adjacentNodeFn(final Network<N, E> network, final N node) {
/* 136 */     return new Function<E, N>()
/*     */       {
/*     */         public N apply(E edge) {
/* 139 */           return network.incidentNodes(edge).adjacentNode(node);
/*     */         }
/*     */       };
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
/*     */   public static class Builder<N, E>
/*     */   {
/*     */     private final MutableNetwork<N, E> mutableNetwork;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Builder(NetworkBuilder<N, E> networkBuilder) {
/* 172 */       this.mutableNetwork = networkBuilder.build();
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
/*     */     public Builder<N, E> addNode(N node) {
/* 184 */       this.mutableNetwork.addNode(node);
/* 185 */       return this;
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
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<N, E> addEdge(N nodeU, N nodeV, E edge) {
/* 211 */       this.mutableNetwork.addEdge(nodeU, nodeV, edge);
/* 212 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<N, E> addEdge(EndpointPair<N> endpoints, E edge) {
/* 242 */       this.mutableNetwork.addEdge(endpoints, edge);
/* 243 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableNetwork<N, E> build() {
/* 251 */       return ImmutableNetwork.copyOf(this.mutableNetwork);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\graph\ImmutableNetwork.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */