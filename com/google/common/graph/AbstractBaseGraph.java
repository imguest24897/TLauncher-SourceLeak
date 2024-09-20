/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.common.math.IntMath;
/*     */ import com.google.common.primitives.Ints;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Iterator;
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
/*     */ abstract class AbstractBaseGraph<N>
/*     */   implements BaseGraph<N>
/*     */ {
/*     */   protected long edgeCount() {
/*  52 */     long degreeSum = 0L;
/*  53 */     for (N node : nodes()) {
/*  54 */       degreeSum += degree(node);
/*     */     }
/*     */     
/*  57 */     Preconditions.checkState(((degreeSum & 0x1L) == 0L));
/*  58 */     return degreeSum >>> 1L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<EndpointPair<N>> edges() {
/*  67 */     return new AbstractSet<EndpointPair<N>>()
/*     */       {
/*     */         public UnmodifiableIterator<EndpointPair<N>> iterator() {
/*  70 */           return (UnmodifiableIterator)EndpointPairIterator.of(AbstractBaseGraph.this);
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/*  75 */           return Ints.saturatedCast(AbstractBaseGraph.this.edgeCount());
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean remove(Object o) {
/*  80 */           throw new UnsupportedOperationException();
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public boolean contains(Object obj) {
/*  89 */           if (!(obj instanceof EndpointPair)) {
/*  90 */             return false;
/*     */           }
/*  92 */           EndpointPair<?> endpointPair = (EndpointPair)obj;
/*  93 */           return (AbstractBaseGraph.this.isOrderingCompatible(endpointPair) && AbstractBaseGraph.this
/*  94 */             .nodes().contains(endpointPair.nodeU()) && AbstractBaseGraph.this
/*  95 */             .successors(endpointPair.nodeU()).contains(endpointPair.nodeV()));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementOrder<N> incidentEdgeOrder() {
/* 102 */     return ElementOrder.unordered();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<EndpointPair<N>> incidentEdges(N node) {
/* 107 */     Preconditions.checkNotNull(node);
/* 108 */     Preconditions.checkArgument(nodes().contains(node), "Node %s is not an element of this graph.", node);
/* 109 */     return new IncidentEdgeSet<N>(this, this, node)
/*     */       {
/*     */         public UnmodifiableIterator<EndpointPair<N>> iterator() {
/* 112 */           if (this.graph.isDirected()) {
/* 113 */             return Iterators.unmodifiableIterator(
/* 114 */                 Iterators.concat(
/* 115 */                   Iterators.transform(this.graph
/* 116 */                     .predecessors(this.node).iterator(), new Function<N, EndpointPair<N>>()
/*     */                     {
/*     */                       public EndpointPair<N> apply(N predecessor)
/*     */                       {
/* 120 */                         return EndpointPair.ordered(predecessor, AbstractBaseGraph.null.this.node);
/*     */                       }
/* 123 */                     }), Iterators.transform(
/*     */                     
/* 125 */                     (Iterator)Sets.difference(this.graph.successors(this.node), (Set)ImmutableSet.of(this.node)).iterator(), new Function<N, EndpointPair<N>>()
/*     */                     {
/*     */                       public EndpointPair<N> apply(N successor)
/*     */                       {
/* 129 */                         return EndpointPair.ordered(AbstractBaseGraph.null.this.node, successor);
/*     */                       }
/*     */                     })));
/*     */           }
/* 133 */           return Iterators.unmodifiableIterator(
/* 134 */               Iterators.transform(this.graph
/* 135 */                 .adjacentNodes(this.node).iterator(), new Function<N, EndpointPair<N>>()
/*     */                 {
/*     */                   public EndpointPair<N> apply(N adjacentNode)
/*     */                   {
/* 139 */                     return EndpointPair.unordered(AbstractBaseGraph.null.this.node, adjacentNode);
/*     */                   }
/*     */                 }));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int degree(N node) {
/* 149 */     if (isDirected()) {
/* 150 */       return IntMath.saturatedAdd(predecessors(node).size(), successors(node).size());
/*     */     }
/* 152 */     Set<N> neighbors = adjacentNodes(node);
/* 153 */     int selfLoopCount = (allowsSelfLoops() && neighbors.contains(node)) ? 1 : 0;
/* 154 */     return IntMath.saturatedAdd(neighbors.size(), selfLoopCount);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int inDegree(N node) {
/* 160 */     return isDirected() ? predecessors(node).size() : degree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public int outDegree(N node) {
/* 165 */     return isDirected() ? successors(node).size() : degree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(N nodeU, N nodeV) {
/* 170 */     Preconditions.checkNotNull(nodeU);
/* 171 */     Preconditions.checkNotNull(nodeV);
/* 172 */     return (nodes().contains(nodeU) && successors(nodeU).contains(nodeV));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
/* 177 */     Preconditions.checkNotNull(endpoints);
/* 178 */     if (!isOrderingCompatible(endpoints)) {
/* 179 */       return false;
/*     */     }
/* 181 */     N nodeU = endpoints.nodeU();
/* 182 */     N nodeV = endpoints.nodeV();
/* 183 */     return (nodes().contains(nodeU) && successors(nodeU).contains(nodeV));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void validateEndpoints(EndpointPair<?> endpoints) {
/* 191 */     Preconditions.checkNotNull(endpoints);
/* 192 */     Preconditions.checkArgument(isOrderingCompatible(endpoints), "Mismatch: unordered endpoints cannot be used with directed graphs");
/*     */   }
/*     */   
/*     */   protected final boolean isOrderingCompatible(EndpointPair<?> endpoints) {
/* 196 */     return (endpoints.isOrdered() || !isDirected());
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\graph\AbstractBaseGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */