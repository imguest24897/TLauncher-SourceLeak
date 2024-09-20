/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class StandardMutableValueGraph<N, V>
/*     */   extends StandardValueGraph<N, V>
/*     */   implements MutableValueGraph<N, V>
/*     */ {
/*     */   private final ElementOrder<N> incidentEdgeOrder;
/*     */   
/*     */   StandardMutableValueGraph(AbstractGraphBuilder<? super N> builder) {
/*  48 */     super(builder);
/*  49 */     this.incidentEdgeOrder = builder.incidentEdgeOrder.cast();
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementOrder<N> incidentEdgeOrder() {
/*  54 */     return this.incidentEdgeOrder;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean addNode(N node) {
/*  60 */     Preconditions.checkNotNull(node, "node");
/*     */     
/*  62 */     if (containsNode(node)) {
/*  63 */       return false;
/*     */     }
/*     */     
/*  66 */     addNodeInternal(node);
/*  67 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private GraphConnections<N, V> addNodeInternal(N node) {
/*  77 */     GraphConnections<N, V> connections = newConnections();
/*  78 */     Preconditions.checkState((this.nodeConnections.put(node, connections) == null));
/*  79 */     return connections;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V putEdgeValue(N nodeU, N nodeV, V value) {
/*  85 */     Preconditions.checkNotNull(nodeU, "nodeU");
/*  86 */     Preconditions.checkNotNull(nodeV, "nodeV");
/*  87 */     Preconditions.checkNotNull(value, "value");
/*     */     
/*  89 */     if (!allowsSelfLoops()) {
/*  90 */       Preconditions.checkArgument(!nodeU.equals(nodeV), "Cannot add self-loop edge on node %s, as self-loops are not allowed. To construct a graph that allows self-loops, call allowsSelfLoops(true) on the Builder.", nodeU);
/*     */     }
/*     */     
/*  93 */     GraphConnections<N, V> connectionsU = this.nodeConnections.get(nodeU);
/*  94 */     if (connectionsU == null) {
/*  95 */       connectionsU = addNodeInternal(nodeU);
/*     */     }
/*  97 */     V previousValue = connectionsU.addSuccessor(nodeV, value);
/*  98 */     GraphConnections<N, V> connectionsV = this.nodeConnections.get(nodeV);
/*  99 */     if (connectionsV == null) {
/* 100 */       connectionsV = addNodeInternal(nodeV);
/*     */     }
/* 102 */     connectionsV.addPredecessor(nodeU, value);
/* 103 */     if (previousValue == null) {
/* 104 */       Graphs.checkPositive(++this.edgeCount);
/*     */     }
/* 106 */     return previousValue;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V putEdgeValue(EndpointPair<N> endpoints, V value) {
/* 112 */     validateEndpoints(endpoints);
/* 113 */     return putEdgeValue(endpoints.nodeU(), endpoints.nodeV(), value);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean removeNode(N node) {
/* 119 */     Preconditions.checkNotNull(node, "node");
/*     */     
/* 121 */     GraphConnections<N, V> connections = this.nodeConnections.get(node);
/* 122 */     if (connections == null) {
/* 123 */       return false;
/*     */     }
/*     */     
/* 126 */     if (allowsSelfLoops())
/*     */     {
/* 128 */       if (connections.removeSuccessor(node) != null) {
/* 129 */         connections.removePredecessor(node);
/* 130 */         this.edgeCount--;
/*     */       } 
/*     */     }
/*     */     
/* 134 */     for (N successor : connections.successors()) {
/* 135 */       ((GraphConnections)this.nodeConnections.getWithoutCaching(successor)).removePredecessor(node);
/* 136 */       this.edgeCount--;
/*     */     } 
/* 138 */     if (isDirected()) {
/* 139 */       for (N predecessor : connections.predecessors()) {
/* 140 */         Preconditions.checkState((((GraphConnections)this.nodeConnections.getWithoutCaching(predecessor)).removeSuccessor(node) != null));
/* 141 */         this.edgeCount--;
/*     */       } 
/*     */     }
/* 144 */     this.nodeConnections.remove(node);
/* 145 */     Graphs.checkNonNegative(this.edgeCount);
/* 146 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V removeEdge(N nodeU, N nodeV) {
/* 152 */     Preconditions.checkNotNull(nodeU, "nodeU");
/* 153 */     Preconditions.checkNotNull(nodeV, "nodeV");
/*     */     
/* 155 */     GraphConnections<N, V> connectionsU = this.nodeConnections.get(nodeU);
/* 156 */     GraphConnections<N, V> connectionsV = this.nodeConnections.get(nodeV);
/* 157 */     if (connectionsU == null || connectionsV == null) {
/* 158 */       return null;
/*     */     }
/*     */     
/* 161 */     V previousValue = connectionsU.removeSuccessor(nodeV);
/* 162 */     if (previousValue != null) {
/* 163 */       connectionsV.removePredecessor(nodeU);
/* 164 */       Graphs.checkNonNegative(--this.edgeCount);
/*     */     } 
/* 166 */     return previousValue;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V removeEdge(EndpointPair<N> endpoints) {
/* 172 */     validateEndpoints(endpoints);
/* 173 */     return removeEdge(endpoints.nodeU(), endpoints.nodeV());
/*     */   }
/*     */   
/*     */   private GraphConnections<N, V> newConnections() {
/* 177 */     return isDirected() ? 
/* 178 */       DirectedGraphConnections.<N, V>of(this.incidentEdgeOrder) : 
/* 179 */       UndirectedGraphConnections.<N, V>of(this.incidentEdgeOrder);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\graph\StandardMutableValueGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */