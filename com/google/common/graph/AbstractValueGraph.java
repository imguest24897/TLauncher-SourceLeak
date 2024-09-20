/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.collect.Maps;
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
/*     */ @Beta
/*     */ public abstract class AbstractValueGraph<N, V>
/*     */   extends AbstractBaseGraph<N>
/*     */   implements ValueGraph<N, V>
/*     */ {
/*     */   public Graph<N> asGraph() {
/*  45 */     return new AbstractGraph<N>()
/*     */       {
/*     */         public Set<N> nodes() {
/*  48 */           return AbstractValueGraph.this.nodes();
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<EndpointPair<N>> edges() {
/*  53 */           return AbstractValueGraph.this.edges();
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean isDirected() {
/*  58 */           return AbstractValueGraph.this.isDirected();
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean allowsSelfLoops() {
/*  63 */           return AbstractValueGraph.this.allowsSelfLoops();
/*     */         }
/*     */ 
/*     */         
/*     */         public ElementOrder<N> nodeOrder() {
/*  68 */           return AbstractValueGraph.this.nodeOrder();
/*     */         }
/*     */ 
/*     */         
/*     */         public ElementOrder<N> incidentEdgeOrder() {
/*  73 */           return AbstractValueGraph.this.incidentEdgeOrder();
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<N> adjacentNodes(N node) {
/*  78 */           return AbstractValueGraph.this.adjacentNodes(node);
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<N> predecessors(N node) {
/*  83 */           return AbstractValueGraph.this.predecessors(node);
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<N> successors(N node) {
/*  88 */           return AbstractValueGraph.this.successors(node);
/*     */         }
/*     */ 
/*     */         
/*     */         public int degree(N node) {
/*  93 */           return AbstractValueGraph.this.degree(node);
/*     */         }
/*     */ 
/*     */         
/*     */         public int inDegree(N node) {
/*  98 */           return AbstractValueGraph.this.inDegree(node);
/*     */         }
/*     */ 
/*     */         
/*     */         public int outDegree(N node) {
/* 103 */           return AbstractValueGraph.this.outDegree(node);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<V> edgeValue(N nodeU, N nodeV) {
/* 110 */     return Optional.ofNullable(edgeValueOrDefault(nodeU, nodeV, null));
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<V> edgeValue(EndpointPair<N> endpoints) {
/* 115 */     return Optional.ofNullable(edgeValueOrDefault(endpoints, null));
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean equals(Object obj) {
/* 120 */     if (obj == this) {
/* 121 */       return true;
/*     */     }
/* 123 */     if (!(obj instanceof ValueGraph)) {
/* 124 */       return false;
/*     */     }
/* 126 */     ValueGraph<?, ?> other = (ValueGraph<?, ?>)obj;
/*     */     
/* 128 */     return (isDirected() == other.isDirected() && 
/* 129 */       nodes().equals(other.nodes()) && 
/* 130 */       edgeValueMap(this).equals(edgeValueMap(other)));
/*     */   }
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/* 135 */     return edgeValueMap(this).hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 142 */     boolean bool1 = isDirected();
/*     */     
/* 144 */     boolean bool2 = allowsSelfLoops();
/*     */     
/* 146 */     String str1 = String.valueOf(nodes());
/*     */     
/* 148 */     String str2 = String.valueOf(edgeValueMap(this)); return (new StringBuilder(59 + String.valueOf(str1).length() + String.valueOf(str2).length())).append("isDirected: ").append(bool1).append(", allowsSelfLoops: ").append(bool2).append(", nodes: ").append(str1).append(", edges: ").append(str2).toString();
/*     */   }
/*     */   
/*     */   private static <N, V> Map<EndpointPair<N>, V> edgeValueMap(final ValueGraph<N, V> graph) {
/* 152 */     Function<EndpointPair<N>, V> edgeToValueFn = new Function<EndpointPair<N>, V>()
/*     */       {
/*     */         public V apply(EndpointPair<N> edge)
/*     */         {
/* 156 */           return (V)graph.edgeValueOrDefault(edge.nodeU(), edge.nodeV(), null);
/*     */         }
/*     */       };
/* 159 */     return Maps.asMap(graph.edges(), edgeToValueFn);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\graph\AbstractValueGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */