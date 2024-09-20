/*    */ package com.google.common.graph;
/*    */ 
/*    */ import java.util.AbstractSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class IncidentEdgeSet<N>
/*    */   extends AbstractSet<EndpointPair<N>>
/*    */ {
/*    */   protected final N node;
/*    */   protected final BaseGraph<N> graph;
/*    */   
/*    */   IncidentEdgeSet(BaseGraph<N> graph, N node) {
/* 32 */     this.graph = graph;
/* 33 */     this.node = node;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean remove(Object o) {
/* 38 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 43 */     if (this.graph.isDirected()) {
/* 44 */       return this.graph.inDegree(this.node) + this.graph
/* 45 */         .outDegree(this.node) - (
/* 46 */         this.graph.successors(this.node).contains(this.node) ? 1 : 0);
/*    */     }
/* 48 */     return this.graph.adjacentNodes(this.node).size();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean contains(Object obj) {
/* 54 */     if (!(obj instanceof EndpointPair)) {
/* 55 */       return false;
/*    */     }
/* 57 */     EndpointPair<?> endpointPair = (EndpointPair)obj;
/*    */     
/* 59 */     if (this.graph.isDirected()) {
/* 60 */       if (!endpointPair.isOrdered()) {
/* 61 */         return false;
/*    */       }
/*    */       
/* 64 */       Object source = endpointPair.source();
/* 65 */       Object target = endpointPair.target();
/* 66 */       return ((this.node.equals(source) && this.graph.successors(this.node).contains(target)) || (this.node
/* 67 */         .equals(target) && this.graph.predecessors(this.node).contains(source)));
/*    */     } 
/* 69 */     if (endpointPair.isOrdered()) {
/* 70 */       return false;
/*    */     }
/* 72 */     Set<N> adjacent = this.graph.adjacentNodes(this.node);
/* 73 */     Object nodeU = endpointPair.nodeU();
/* 74 */     Object nodeV = endpointPair.nodeV();
/*    */     
/* 76 */     return ((this.node.equals(nodeV) && adjacent.contains(nodeU)) || (this.node
/* 77 */       .equals(nodeU) && adjacent.contains(nodeV)));
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\graph\IncidentEdgeSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */