/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.math.IntMath;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collections;
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
/*     */ @Beta
/*     */ public abstract class AbstractNetwork<N, E>
/*     */   implements Network<N, E>
/*     */ {
/*     */   public Graph<N> asGraph() {
/*  57 */     return new AbstractGraph<N>()
/*     */       {
/*     */         public Set<N> nodes() {
/*  60 */           return AbstractNetwork.this.nodes();
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<EndpointPair<N>> edges() {
/*  65 */           if (AbstractNetwork.this.allowsParallelEdges()) {
/*  66 */             return super.edges();
/*     */           }
/*     */ 
/*     */           
/*  70 */           return new AbstractSet<EndpointPair<N>>()
/*     */             {
/*     */               public Iterator<EndpointPair<N>> iterator() {
/*  73 */                 return Iterators.transform(AbstractNetwork.this
/*  74 */                     .edges().iterator(), new Function<E, EndpointPair<N>>()
/*     */                     {
/*     */                       public EndpointPair<N> apply(E edge)
/*     */                       {
/*  78 */                         return AbstractNetwork.this.incidentNodes(edge);
/*     */                       }
/*     */                     });
/*     */               }
/*     */ 
/*     */               
/*     */               public int size() {
/*  85 */                 return AbstractNetwork.this.edges().size();
/*     */               }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               public boolean contains(Object obj) {
/*  94 */                 if (!(obj instanceof EndpointPair)) {
/*  95 */                   return false;
/*     */                 }
/*  97 */                 EndpointPair<?> endpointPair = (EndpointPair)obj;
/*  98 */                 return (AbstractNetwork.null.this.isOrderingCompatible(endpointPair) && AbstractNetwork.null.this
/*  99 */                   .nodes().contains(endpointPair.nodeU()) && AbstractNetwork.null.this
/* 100 */                   .successors((N)endpointPair.nodeU()).contains(endpointPair.nodeV()));
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */         
/*     */         public ElementOrder<N> nodeOrder() {
/* 107 */           return AbstractNetwork.this.nodeOrder();
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public ElementOrder<N> incidentEdgeOrder() {
/* 114 */           return ElementOrder.unordered();
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean isDirected() {
/* 119 */           return AbstractNetwork.this.isDirected();
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean allowsSelfLoops() {
/* 124 */           return AbstractNetwork.this.allowsSelfLoops();
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<N> adjacentNodes(N node) {
/* 129 */           return AbstractNetwork.this.adjacentNodes(node);
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<N> predecessors(N node) {
/* 134 */           return AbstractNetwork.this.predecessors(node);
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<N> successors(N node) {
/* 139 */           return AbstractNetwork.this.successors(node);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int degree(N node) {
/* 148 */     if (isDirected()) {
/* 149 */       return IntMath.saturatedAdd(inEdges(node).size(), outEdges(node).size());
/*     */     }
/* 151 */     return IntMath.saturatedAdd(incidentEdges(node).size(), edgesConnecting(node, node).size());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int inDegree(N node) {
/* 157 */     return isDirected() ? inEdges(node).size() : degree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public int outDegree(N node) {
/* 162 */     return isDirected() ? outEdges(node).size() : degree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> adjacentEdges(E edge) {
/* 167 */     EndpointPair<N> endpointPair = incidentNodes(edge);
/*     */     
/* 169 */     Sets.SetView setView = Sets.union(incidentEdges(endpointPair.nodeU()), incidentEdges(endpointPair.nodeV()));
/* 170 */     return (Set<E>)Sets.difference((Set)setView, (Set)ImmutableSet.of(edge));
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> edgesConnecting(N nodeU, N nodeV) {
/* 175 */     Set<E> outEdgesU = outEdges(nodeU);
/* 176 */     Set<E> inEdgesV = inEdges(nodeV);
/* 177 */     return (outEdgesU.size() <= inEdgesV.size()) ? 
/* 178 */       Collections.<E>unmodifiableSet(Sets.filter(outEdgesU, connectedPredicate(nodeU, nodeV))) : 
/* 179 */       Collections.<E>unmodifiableSet(Sets.filter(inEdgesV, connectedPredicate(nodeV, nodeU)));
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> edgesConnecting(EndpointPair<N> endpoints) {
/* 184 */     validateEndpoints(endpoints);
/* 185 */     return edgesConnecting(endpoints.nodeU(), endpoints.nodeV());
/*     */   }
/*     */   
/*     */   private Predicate<E> connectedPredicate(final N nodePresent, final N nodeToCheck) {
/* 189 */     return new Predicate<E>()
/*     */       {
/*     */         public boolean apply(E edge) {
/* 192 */           return AbstractNetwork.this.incidentNodes(edge).adjacentNode(nodePresent).equals(nodeToCheck);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<E> edgeConnecting(N nodeU, N nodeV) {
/* 199 */     return Optional.ofNullable(edgeConnectingOrNull(nodeU, nodeV));
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<E> edgeConnecting(EndpointPair<N> endpoints) {
/* 204 */     validateEndpoints(endpoints);
/* 205 */     return edgeConnecting(endpoints.nodeU(), endpoints.nodeV());
/*     */   }
/*     */ 
/*     */   
/*     */   public E edgeConnectingOrNull(N nodeU, N nodeV) {
/* 210 */     Set<E> edgesConnecting = edgesConnecting(nodeU, nodeV);
/* 211 */     switch (edgesConnecting.size()) {
/*     */       case 0:
/* 213 */         return null;
/*     */       case 1:
/* 215 */         return edgesConnecting.iterator().next();
/*     */     } 
/* 217 */     throw new IllegalArgumentException(String.format("Cannot call edgeConnecting() when parallel edges exist between %s and %s. Consider calling edgesConnecting() instead.", new Object[] { nodeU, nodeV }));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E edgeConnectingOrNull(EndpointPair<N> endpoints) {
/* 223 */     validateEndpoints(endpoints);
/* 224 */     return edgeConnectingOrNull(endpoints.nodeU(), endpoints.nodeV());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(N nodeU, N nodeV) {
/* 229 */     Preconditions.checkNotNull(nodeU);
/* 230 */     Preconditions.checkNotNull(nodeV);
/* 231 */     return (nodes().contains(nodeU) && successors(nodeU).contains(nodeV));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
/* 236 */     Preconditions.checkNotNull(endpoints);
/* 237 */     if (!isOrderingCompatible(endpoints)) {
/* 238 */       return false;
/*     */     }
/* 240 */     return hasEdgeConnecting(endpoints.nodeU(), endpoints.nodeV());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void validateEndpoints(EndpointPair<?> endpoints) {
/* 248 */     Preconditions.checkNotNull(endpoints);
/* 249 */     Preconditions.checkArgument(isOrderingCompatible(endpoints), "Mismatch: unordered endpoints cannot be used with directed graphs");
/*     */   }
/*     */   
/*     */   protected final boolean isOrderingCompatible(EndpointPair<?> endpoints) {
/* 253 */     return (endpoints.isOrdered() || !isDirected());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean equals(Object obj) {
/* 258 */     if (obj == this) {
/* 259 */       return true;
/*     */     }
/* 261 */     if (!(obj instanceof Network)) {
/* 262 */       return false;
/*     */     }
/* 264 */     Network<?, ?> other = (Network<?, ?>)obj;
/*     */     
/* 266 */     return (isDirected() == other.isDirected() && 
/* 267 */       nodes().equals(other.nodes()) && 
/* 268 */       edgeIncidentNodesMap(this).equals(edgeIncidentNodesMap(other)));
/*     */   }
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/* 273 */     return edgeIncidentNodesMap(this).hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 280 */     boolean bool1 = isDirected();
/*     */     
/* 282 */     boolean bool2 = allowsParallelEdges();
/*     */     
/* 284 */     boolean bool3 = allowsSelfLoops();
/*     */     
/* 286 */     String str1 = String.valueOf(nodes());
/*     */     
/* 288 */     String str2 = String.valueOf(edgeIncidentNodesMap(this)); return (new StringBuilder(87 + String.valueOf(str1).length() + String.valueOf(str2).length())).append("isDirected: ").append(bool1).append(", allowsParallelEdges: ").append(bool2).append(", allowsSelfLoops: ").append(bool3).append(", nodes: ").append(str1).append(", edges: ").append(str2).toString();
/*     */   }
/*     */   
/*     */   private static <N, E> Map<E, EndpointPair<N>> edgeIncidentNodesMap(final Network<N, E> network) {
/* 292 */     Function<E, EndpointPair<N>> edgeToIncidentNodesFn = new Function<E, EndpointPair<N>>()
/*     */       {
/*     */         public EndpointPair<N> apply(E edge)
/*     */         {
/* 296 */           return network.incidentNodes(edge);
/*     */         }
/*     */       };
/* 299 */     return Maps.asMap(network.edges(), edgeToIncidentNodesFn);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\graph\AbstractNetwork.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */