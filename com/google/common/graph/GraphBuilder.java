/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.DoNotMock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @DoNotMock
/*     */ @Beta
/*     */ public final class GraphBuilder<N>
/*     */   extends AbstractGraphBuilder<N>
/*     */ {
/*     */   private GraphBuilder(boolean directed) {
/*  71 */     super(directed);
/*     */   }
/*     */ 
/*     */   
/*     */   public static GraphBuilder<Object> directed() {
/*  76 */     return new GraphBuilder(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public static GraphBuilder<Object> undirected() {
/*  81 */     return new GraphBuilder(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N> GraphBuilder<N> from(Graph<N> graph) {
/*  92 */     return (new GraphBuilder(graph.isDirected()))
/*  93 */       .allowsSelfLoops(graph.allowsSelfLoops())
/*  94 */       .nodeOrder(graph.nodeOrder())
/*  95 */       .incidentEdgeOrder(graph.incidentEdgeOrder());
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
/*     */   public <N1 extends N> ImmutableGraph.Builder<N1> immutable() {
/* 109 */     GraphBuilder<N1> castBuilder = cast();
/* 110 */     return new ImmutableGraph.Builder<>(castBuilder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GraphBuilder<N> allowsSelfLoops(boolean allowsSelfLoops) {
/* 121 */     this.allowsSelfLoops = allowsSelfLoops;
/* 122 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GraphBuilder<N> expectedNodeCount(int expectedNodeCount) {
/* 131 */     this.expectedNodeCount = Optional.of(Integer.valueOf(Graphs.checkNonNegative(expectedNodeCount)));
/* 132 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <N1 extends N> GraphBuilder<N1> nodeOrder(ElementOrder<N1> nodeOrder) {
/* 141 */     GraphBuilder<N1> newBuilder = cast();
/* 142 */     newBuilder.nodeOrder = (ElementOrder<N>)Preconditions.checkNotNull(nodeOrder);
/* 143 */     return newBuilder;
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
/*     */   
/*     */   public <N1 extends N> GraphBuilder<N1> incidentEdgeOrder(ElementOrder<N1> incidentEdgeOrder) {
/* 160 */     Preconditions.checkArgument((incidentEdgeOrder
/* 161 */         .type() == ElementOrder.Type.UNORDERED || incidentEdgeOrder
/* 162 */         .type() == ElementOrder.Type.STABLE), "The given elementOrder (%s) is unsupported. incidentEdgeOrder() only supports ElementOrder.unordered() and ElementOrder.stable().", incidentEdgeOrder);
/*     */ 
/*     */ 
/*     */     
/* 166 */     GraphBuilder<N1> newBuilder = cast();
/* 167 */     newBuilder.incidentEdgeOrder = (ElementOrder<N>)Preconditions.checkNotNull(incidentEdgeOrder);
/* 168 */     return newBuilder;
/*     */   }
/*     */ 
/*     */   
/*     */   public <N1 extends N> MutableGraph<N1> build() {
/* 173 */     return new StandardMutableGraph<>(this);
/*     */   }
/*     */   
/*     */   GraphBuilder<N> copy() {
/* 177 */     GraphBuilder<N> newBuilder = new GraphBuilder(this.directed);
/* 178 */     newBuilder.allowsSelfLoops = this.allowsSelfLoops;
/* 179 */     newBuilder.nodeOrder = this.nodeOrder;
/* 180 */     newBuilder.expectedNodeCount = this.expectedNodeCount;
/* 181 */     newBuilder.incidentEdgeOrder = this.incidentEdgeOrder;
/* 182 */     return newBuilder;
/*     */   }
/*     */ 
/*     */   
/*     */   private <N1 extends N> GraphBuilder<N1> cast() {
/* 187 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\graph\GraphBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */