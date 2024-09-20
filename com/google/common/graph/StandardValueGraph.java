/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class StandardValueGraph<N, V>
/*     */   extends AbstractValueGraph<N, V>
/*     */ {
/*     */   private final boolean isDirected;
/*     */   private final boolean allowsSelfLoops;
/*     */   private final ElementOrder<N> nodeOrder;
/*     */   protected final MapIteratorCache<N, GraphConnections<N, V>> nodeConnections;
/*     */   protected long edgeCount;
/*     */   
/*     */   StandardValueGraph(AbstractGraphBuilder<? super N> builder) {
/*  57 */     this(builder, builder.nodeOrder
/*     */         
/*  59 */         .createMap(((Integer)builder.expectedNodeCount
/*  60 */           .or(Integer.valueOf(10))).intValue()), 0L);
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
/*     */   StandardValueGraph(AbstractGraphBuilder<? super N> builder, Map<N, GraphConnections<N, V>> nodeConnections, long edgeCount) {
/*  72 */     this.isDirected = builder.directed;
/*  73 */     this.allowsSelfLoops = builder.allowsSelfLoops;
/*  74 */     this.nodeOrder = builder.nodeOrder.cast();
/*     */     
/*  76 */     this
/*     */ 
/*     */       
/*  79 */       .nodeConnections = (nodeConnections instanceof java.util.TreeMap) ? new MapRetrievalCache<>(nodeConnections) : new MapIteratorCache<>(nodeConnections);
/*  80 */     this.edgeCount = Graphs.checkNonNegative(edgeCount);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> nodes() {
/*  85 */     return this.nodeConnections.unmodifiableKeySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDirected() {
/*  90 */     return this.isDirected;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean allowsSelfLoops() {
/*  95 */     return this.allowsSelfLoops;
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementOrder<N> nodeOrder() {
/* 100 */     return this.nodeOrder;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> adjacentNodes(N node) {
/* 105 */     return checkedConnections(node).adjacentNodes();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> predecessors(N node) {
/* 110 */     return checkedConnections(node).predecessors();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> successors(N node) {
/* 115 */     return checkedConnections(node).successors();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<EndpointPair<N>> incidentEdges(N node) {
/* 120 */     final GraphConnections<N, V> connections = checkedConnections(node);
/*     */     
/* 122 */     return new IncidentEdgeSet<N>(this, this, node)
/*     */       {
/*     */         public Iterator<EndpointPair<N>> iterator() {
/* 125 */           return connections.incidentEdgeIterator(this.node);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(N nodeU, N nodeV) {
/* 132 */     return hasEdgeConnecting_internal((N)Preconditions.checkNotNull(nodeU), (N)Preconditions.checkNotNull(nodeV));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
/* 137 */     Preconditions.checkNotNull(endpoints);
/* 138 */     return (isOrderingCompatible(endpoints) && 
/* 139 */       hasEdgeConnecting_internal(endpoints.nodeU(), endpoints.nodeV()));
/*     */   }
/*     */ 
/*     */   
/*     */   public V edgeValueOrDefault(N nodeU, N nodeV, V defaultValue) {
/* 144 */     return edgeValueOrDefault_internal((N)Preconditions.checkNotNull(nodeU), (N)Preconditions.checkNotNull(nodeV), defaultValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public V edgeValueOrDefault(EndpointPair<N> endpoints, V defaultValue) {
/* 149 */     validateEndpoints(endpoints);
/* 150 */     return edgeValueOrDefault_internal(endpoints.nodeU(), endpoints.nodeV(), defaultValue);
/*     */   }
/*     */ 
/*     */   
/*     */   protected long edgeCount() {
/* 155 */     return this.edgeCount;
/*     */   }
/*     */   
/*     */   protected final GraphConnections<N, V> checkedConnections(N node) {
/* 159 */     GraphConnections<N, V> connections = this.nodeConnections.get(node);
/* 160 */     if (connections == null) {
/* 161 */       Preconditions.checkNotNull(node);
/* 162 */       String str = String.valueOf(node); throw new IllegalArgumentException((new StringBuilder(38 + String.valueOf(str).length())).append("Node ").append(str).append(" is not an element of this graph.").toString());
/*     */     } 
/* 164 */     return connections;
/*     */   }
/*     */   
/*     */   protected final boolean containsNode(N node) {
/* 168 */     return this.nodeConnections.containsKey(node);
/*     */   }
/*     */   
/*     */   protected final boolean hasEdgeConnecting_internal(N nodeU, N nodeV) {
/* 172 */     GraphConnections<N, V> connectionsU = this.nodeConnections.get(nodeU);
/* 173 */     return (connectionsU != null && connectionsU.successors().contains(nodeV));
/*     */   }
/*     */   
/*     */   protected final V edgeValueOrDefault_internal(N nodeU, N nodeV, V defaultValue) {
/* 177 */     GraphConnections<N, V> connectionsU = this.nodeConnections.get(nodeU);
/* 178 */     V value = (connectionsU == null) ? null : connectionsU.value(nodeV);
/* 179 */     return (value == null) ? defaultValue : value;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\graph\StandardValueGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */