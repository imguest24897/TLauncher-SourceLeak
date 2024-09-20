/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public final class Graphs
/*     */ {
/*     */   public static <N> boolean hasCycle(Graph<N> graph) {
/*  60 */     int numEdges = graph.edges().size();
/*  61 */     if (numEdges == 0) {
/*  62 */       return false;
/*     */     }
/*  64 */     if (!graph.isDirected() && numEdges >= graph.nodes().size()) {
/*  65 */       return true;
/*     */     }
/*     */ 
/*     */     
/*  69 */     Map<Object, NodeVisitState> visitedNodes = Maps.newHashMapWithExpectedSize(graph.nodes().size());
/*  70 */     for (N node : graph.nodes()) {
/*  71 */       if (subgraphHasCycle(graph, visitedNodes, node, null)) {
/*  72 */         return true;
/*     */       }
/*     */     } 
/*  75 */     return false;
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
/*     */   public static boolean hasCycle(Network<?, ?> network) {
/*  88 */     if (!network.isDirected() && network
/*  89 */       .allowsParallelEdges() && network
/*  90 */       .edges().size() > network.asGraph().edges().size()) {
/*  91 */       return true;
/*     */     }
/*  93 */     return hasCycle(network.asGraph());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <N> boolean subgraphHasCycle(Graph<N> graph, Map<Object, NodeVisitState> visitedNodes, N node, N previousNode) {
/* 103 */     NodeVisitState state = visitedNodes.get(node);
/* 104 */     if (state == NodeVisitState.COMPLETE) {
/* 105 */       return false;
/*     */     }
/* 107 */     if (state == NodeVisitState.PENDING) {
/* 108 */       return true;
/*     */     }
/*     */     
/* 111 */     visitedNodes.put(node, NodeVisitState.PENDING);
/* 112 */     for (N nextNode : graph.successors(node)) {
/* 113 */       if (canTraverseWithoutReusingEdge(graph, nextNode, previousNode) && 
/* 114 */         subgraphHasCycle(graph, visitedNodes, nextNode, node)) {
/* 115 */         return true;
/*     */       }
/*     */     } 
/* 118 */     visitedNodes.put(node, NodeVisitState.COMPLETE);
/* 119 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean canTraverseWithoutReusingEdge(Graph<?> graph, Object nextNode, Object previousNode) {
/* 130 */     if (graph.isDirected() || !Objects.equal(previousNode, nextNode)) {
/* 131 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 135 */     return false;
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
/*     */   public static <N> Graph<N> transitiveClosure(Graph<N> graph) {
/* 149 */     MutableGraph<N> transitiveClosure = GraphBuilder.<N>from(graph).allowsSelfLoops(true).build();
/*     */ 
/*     */ 
/*     */     
/* 153 */     if (graph.isDirected()) {
/*     */       
/* 155 */       for (N node : graph.nodes()) {
/* 156 */         for (N reachableNode : reachableNodes(graph, node)) {
/* 157 */           transitiveClosure.putEdge(node, reachableNode);
/*     */         }
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 163 */       Set<N> visitedNodes = new HashSet<>();
/* 164 */       for (N node : graph.nodes()) {
/* 165 */         if (!visitedNodes.contains(node)) {
/* 166 */           Set<N> reachableNodes = reachableNodes(graph, node);
/* 167 */           visitedNodes.addAll(reachableNodes);
/* 168 */           int pairwiseMatch = 1;
/* 169 */           for (N nodeU : reachableNodes) {
/* 170 */             for (N nodeV : Iterables.limit(reachableNodes, pairwiseMatch++)) {
/* 171 */               transitiveClosure.putEdge(nodeU, nodeV);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 178 */     return transitiveClosure;
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
/*     */   public static <N> Set<N> reachableNodes(Graph<N> graph, N node) {
/* 193 */     Preconditions.checkArgument(graph.nodes().contains(node), "Node %s is not an element of this graph.", node);
/* 194 */     return (Set<N>)ImmutableSet.copyOf(Traverser.<N>forGraph(graph).breadthFirst(node));
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
/*     */   public static <N> Graph<N> transpose(Graph<N> graph) {
/* 206 */     if (!graph.isDirected()) {
/* 207 */       return graph;
/*     */     }
/*     */     
/* 210 */     if (graph instanceof TransposedGraph) {
/* 211 */       return ((TransposedGraph)graph).graph;
/*     */     }
/*     */     
/* 214 */     return new TransposedGraph<>(graph);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N, V> ValueGraph<N, V> transpose(ValueGraph<N, V> graph) {
/* 222 */     if (!graph.isDirected()) {
/* 223 */       return graph;
/*     */     }
/*     */     
/* 226 */     if (graph instanceof TransposedValueGraph) {
/* 227 */       return ((TransposedValueGraph)graph).graph;
/*     */     }
/*     */     
/* 230 */     return new TransposedValueGraph<>(graph);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N, E> Network<N, E> transpose(Network<N, E> network) {
/* 238 */     if (!network.isDirected()) {
/* 239 */       return network;
/*     */     }
/*     */     
/* 242 */     if (network instanceof TransposedNetwork) {
/* 243 */       return ((TransposedNetwork)network).network;
/*     */     }
/*     */     
/* 246 */     return new TransposedNetwork<>(network);
/*     */   }
/*     */   
/*     */   static <N> EndpointPair<N> transpose(EndpointPair<N> endpoints) {
/* 250 */     if (endpoints.isOrdered()) {
/* 251 */       return EndpointPair.ordered(endpoints.target(), endpoints.source());
/*     */     }
/* 253 */     return endpoints;
/*     */   }
/*     */   
/*     */   private static class TransposedGraph<N>
/*     */     extends ForwardingGraph<N>
/*     */   {
/*     */     private final Graph<N> graph;
/*     */     
/*     */     TransposedGraph(Graph<N> graph) {
/* 262 */       this.graph = graph;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Graph<N> delegate() {
/* 267 */       return this.graph;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> predecessors(N node) {
/* 272 */       return delegate().successors(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> successors(N node) {
/* 277 */       return delegate().predecessors(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<EndpointPair<N>> incidentEdges(N node) {
/* 282 */       return new IncidentEdgeSet<N>(this, node)
/*     */         {
/*     */           public Iterator<EndpointPair<N>> iterator() {
/* 285 */             return Iterators.transform(Graphs.TransposedGraph.this
/* 286 */                 .delegate().incidentEdges(this.node).iterator(), new Function<EndpointPair<N>, EndpointPair<N>>()
/*     */                 {
/*     */                   public EndpointPair<N> apply(EndpointPair<N> edge)
/*     */                   {
/* 290 */                     return EndpointPair.of(Graphs.TransposedGraph.this.delegate(), edge.nodeV(), edge.nodeU());
/*     */                   }
/*     */                 });
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public int inDegree(N node) {
/* 299 */       return delegate().outDegree(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public int outDegree(N node) {
/* 304 */       return delegate().inDegree(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasEdgeConnecting(N nodeU, N nodeV) {
/* 309 */       return delegate().hasEdgeConnecting(nodeV, nodeU);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
/* 314 */       return delegate().hasEdgeConnecting(Graphs.transpose(endpoints));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TransposedValueGraph<N, V>
/*     */     extends ForwardingValueGraph<N, V>
/*     */   {
/*     */     private final ValueGraph<N, V> graph;
/*     */     
/*     */     TransposedValueGraph(ValueGraph<N, V> graph) {
/* 324 */       this.graph = graph;
/*     */     }
/*     */ 
/*     */     
/*     */     protected ValueGraph<N, V> delegate() {
/* 329 */       return this.graph;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> predecessors(N node) {
/* 334 */       return delegate().successors(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> successors(N node) {
/* 339 */       return delegate().predecessors(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public int inDegree(N node) {
/* 344 */       return delegate().outDegree(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public int outDegree(N node) {
/* 349 */       return delegate().inDegree(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasEdgeConnecting(N nodeU, N nodeV) {
/* 354 */       return delegate().hasEdgeConnecting(nodeV, nodeU);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
/* 359 */       return delegate().hasEdgeConnecting(Graphs.transpose(endpoints));
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<V> edgeValue(N nodeU, N nodeV) {
/* 364 */       return delegate().edgeValue(nodeV, nodeU);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<V> edgeValue(EndpointPair<N> endpoints) {
/* 369 */       return delegate().edgeValue(Graphs.transpose(endpoints));
/*     */     }
/*     */ 
/*     */     
/*     */     public V edgeValueOrDefault(N nodeU, N nodeV, V defaultValue) {
/* 374 */       return delegate().edgeValueOrDefault(nodeV, nodeU, defaultValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public V edgeValueOrDefault(EndpointPair<N> endpoints, V defaultValue) {
/* 379 */       return delegate().edgeValueOrDefault(Graphs.transpose(endpoints), defaultValue);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TransposedNetwork<N, E> extends ForwardingNetwork<N, E> {
/*     */     private final Network<N, E> network;
/*     */     
/*     */     TransposedNetwork(Network<N, E> network) {
/* 387 */       this.network = network;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Network<N, E> delegate() {
/* 392 */       return this.network;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> predecessors(N node) {
/* 397 */       return delegate().successors(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> successors(N node) {
/* 402 */       return delegate().predecessors(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public int inDegree(N node) {
/* 407 */       return delegate().outDegree(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public int outDegree(N node) {
/* 412 */       return delegate().inDegree(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<E> inEdges(N node) {
/* 417 */       return delegate().outEdges(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<E> outEdges(N node) {
/* 422 */       return delegate().inEdges(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public EndpointPair<N> incidentNodes(E edge) {
/* 427 */       EndpointPair<N> endpointPair = delegate().incidentNodes(edge);
/* 428 */       return EndpointPair.of(this.network, endpointPair.nodeV(), endpointPair.nodeU());
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<E> edgesConnecting(N nodeU, N nodeV) {
/* 433 */       return delegate().edgesConnecting(nodeV, nodeU);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<E> edgesConnecting(EndpointPair<N> endpoints) {
/* 438 */       return delegate().edgesConnecting(Graphs.transpose(endpoints));
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<E> edgeConnecting(N nodeU, N nodeV) {
/* 443 */       return delegate().edgeConnecting(nodeV, nodeU);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<E> edgeConnecting(EndpointPair<N> endpoints) {
/* 448 */       return delegate().edgeConnecting(Graphs.transpose(endpoints));
/*     */     }
/*     */ 
/*     */     
/*     */     public E edgeConnectingOrNull(N nodeU, N nodeV) {
/* 453 */       return delegate().edgeConnectingOrNull(nodeV, nodeU);
/*     */     }
/*     */ 
/*     */     
/*     */     public E edgeConnectingOrNull(EndpointPair<N> endpoints) {
/* 458 */       return delegate().edgeConnectingOrNull(Graphs.transpose(endpoints));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasEdgeConnecting(N nodeU, N nodeV) {
/* 463 */       return delegate().hasEdgeConnecting(nodeV, nodeU);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
/* 468 */       return delegate().hasEdgeConnecting(Graphs.transpose(endpoints));
/*     */     }
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
/*     */   public static <N> MutableGraph<N> inducedSubgraph(Graph<N> graph, Iterable<? extends N> nodes) {
/* 485 */     MutableGraph<N> subgraph = (nodes instanceof Collection) ? GraphBuilder.<N>from(graph).expectedNodeCount(((Collection)nodes).size()).build() : GraphBuilder.<N>from(graph).build();
/* 486 */     for (N node : nodes) {
/* 487 */       subgraph.addNode(node);
/*     */     }
/* 489 */     for (N node : subgraph.nodes()) {
/* 490 */       for (N successorNode : graph.successors(node)) {
/* 491 */         if (subgraph.nodes().contains(successorNode)) {
/* 492 */           subgraph.putEdge(node, successorNode);
/*     */         }
/*     */       } 
/*     */     } 
/* 496 */     return subgraph;
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
/*     */   public static <N, V> MutableValueGraph<N, V> inducedSubgraph(ValueGraph<N, V> graph, Iterable<? extends N> nodes) {
/* 512 */     MutableValueGraph<N, V> subgraph = (nodes instanceof Collection) ? ValueGraphBuilder.<N, V>from(graph).expectedNodeCount(((Collection)nodes).size()).build() : ValueGraphBuilder.<N, V>from(graph).build();
/* 513 */     for (N node : nodes) {
/* 514 */       subgraph.addNode(node);
/*     */     }
/* 516 */     for (N node : subgraph.nodes()) {
/* 517 */       for (N successorNode : graph.successors(node)) {
/* 518 */         if (subgraph.nodes().contains(successorNode)) {
/* 519 */           subgraph.putEdgeValue(node, successorNode, graph
/* 520 */               .edgeValueOrDefault(node, successorNode, null));
/*     */         }
/*     */       } 
/*     */     } 
/* 524 */     return subgraph;
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
/*     */   public static <N, E> MutableNetwork<N, E> inducedSubgraph(Network<N, E> network, Iterable<? extends N> nodes) {
/* 540 */     MutableNetwork<N, E> subgraph = (nodes instanceof Collection) ? NetworkBuilder.<N, E>from(network).expectedNodeCount(((Collection)nodes).size()).build() : NetworkBuilder.<N, E>from(network).build();
/* 541 */     for (N node : nodes) {
/* 542 */       subgraph.addNode(node);
/*     */     }
/* 544 */     for (N node : subgraph.nodes()) {
/* 545 */       for (E edge : network.outEdges(node)) {
/* 546 */         N successorNode = network.incidentNodes(edge).adjacentNode(node);
/* 547 */         if (subgraph.nodes().contains(successorNode)) {
/* 548 */           subgraph.addEdge(node, successorNode, edge);
/*     */         }
/*     */       } 
/*     */     } 
/* 552 */     return subgraph;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <N> MutableGraph<N> copyOf(Graph<N> graph) {
/* 557 */     MutableGraph<N> copy = GraphBuilder.<N>from(graph).expectedNodeCount(graph.nodes().size()).build();
/* 558 */     for (N node : graph.nodes()) {
/* 559 */       copy.addNode(node);
/*     */     }
/* 561 */     for (EndpointPair<N> edge : graph.edges()) {
/* 562 */       copy.putEdge(edge.nodeU(), edge.nodeV());
/*     */     }
/* 564 */     return copy;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N, V> MutableValueGraph<N, V> copyOf(ValueGraph<N, V> graph) {
/* 570 */     MutableValueGraph<N, V> copy = ValueGraphBuilder.<N, V>from(graph).expectedNodeCount(graph.nodes().size()).build();
/* 571 */     for (N node : graph.nodes()) {
/* 572 */       copy.addNode(node);
/*     */     }
/* 574 */     for (EndpointPair<N> edge : graph.edges()) {
/* 575 */       copy.putEdgeValue(edge
/* 576 */           .nodeU(), edge.nodeV(), graph.edgeValueOrDefault(edge.nodeU(), edge.nodeV(), null));
/*     */     }
/* 578 */     return copy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N, E> MutableNetwork<N, E> copyOf(Network<N, E> network) {
/* 587 */     MutableNetwork<N, E> copy = NetworkBuilder.<N, E>from(network).expectedNodeCount(network.nodes().size()).expectedEdgeCount(network.edges().size()).build();
/* 588 */     for (N node : network.nodes()) {
/* 589 */       copy.addNode(node);
/*     */     }
/* 591 */     for (E edge : network.edges()) {
/* 592 */       EndpointPair<N> endpointPair = network.incidentNodes(edge);
/* 593 */       copy.addEdge(endpointPair.nodeU(), endpointPair.nodeV(), edge);
/*     */     } 
/* 595 */     return copy;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static int checkNonNegative(int value) {
/* 600 */     Preconditions.checkArgument((value >= 0), "Not true that %s is non-negative.", value);
/* 601 */     return value;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static long checkNonNegative(long value) {
/* 606 */     Preconditions.checkArgument((value >= 0L), "Not true that %s is non-negative.", value);
/* 607 */     return value;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static int checkPositive(int value) {
/* 612 */     Preconditions.checkArgument((value > 0), "Not true that %s is positive.", value);
/* 613 */     return value;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static long checkPositive(long value) {
/* 618 */     Preconditions.checkArgument((value > 0L), "Not true that %s is positive.", value);
/* 619 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private enum NodeVisitState
/*     */   {
/* 628 */     PENDING,
/* 629 */     COMPLETE;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\graph\Graphs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */