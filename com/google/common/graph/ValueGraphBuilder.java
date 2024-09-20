/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class ValueGraphBuilder<N, V>
/*     */   extends AbstractGraphBuilder<N>
/*     */ {
/*     */   private ValueGraphBuilder(boolean directed) {
/*  73 */     super(directed);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ValueGraphBuilder<Object, Object> directed() {
/*  78 */     return new ValueGraphBuilder<>(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ValueGraphBuilder<Object, Object> undirected() {
/*  83 */     return new ValueGraphBuilder<>(false);
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
/*     */   public static <N, V> ValueGraphBuilder<N, V> from(ValueGraph<N, V> graph) {
/*  95 */     return (new ValueGraphBuilder<>(graph.isDirected()))
/*  96 */       .allowsSelfLoops(graph.allowsSelfLoops())
/*  97 */       .nodeOrder(graph.nodeOrder())
/*  98 */       .incidentEdgeOrder(graph.incidentEdgeOrder());
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
/*     */   public <N1 extends N, V1 extends V> ImmutableValueGraph.Builder<N1, V1> immutable() {
/* 113 */     ValueGraphBuilder<N1, V1> castBuilder = cast();
/* 114 */     return new ImmutableValueGraph.Builder<>(castBuilder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValueGraphBuilder<N, V> allowsSelfLoops(boolean allowsSelfLoops) {
/* 125 */     this.allowsSelfLoops = allowsSelfLoops;
/* 126 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValueGraphBuilder<N, V> expectedNodeCount(int expectedNodeCount) {
/* 135 */     this.expectedNodeCount = Optional.of(Integer.valueOf(Graphs.checkNonNegative(expectedNodeCount)));
/* 136 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <N1 extends N> ValueGraphBuilder<N1, V> nodeOrder(ElementOrder<N1> nodeOrder) {
/* 145 */     ValueGraphBuilder<N1, V> newBuilder = cast();
/* 146 */     newBuilder.nodeOrder = (ElementOrder<N>)Preconditions.checkNotNull(nodeOrder);
/* 147 */     return newBuilder;
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
/*     */   
/*     */   public <N1 extends N> ValueGraphBuilder<N1, V> incidentEdgeOrder(ElementOrder<N1> incidentEdgeOrder) {
/* 165 */     Preconditions.checkArgument((incidentEdgeOrder
/* 166 */         .type() == ElementOrder.Type.UNORDERED || incidentEdgeOrder
/* 167 */         .type() == ElementOrder.Type.STABLE), "The given elementOrder (%s) is unsupported. incidentEdgeOrder() only supports ElementOrder.unordered() and ElementOrder.stable().", incidentEdgeOrder);
/*     */ 
/*     */ 
/*     */     
/* 171 */     ValueGraphBuilder<N1, V> newBuilder = cast();
/* 172 */     newBuilder.incidentEdgeOrder = (ElementOrder<N>)Preconditions.checkNotNull(incidentEdgeOrder);
/* 173 */     return newBuilder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <N1 extends N, V1 extends V> MutableValueGraph<N1, V1> build() {
/* 180 */     return new StandardMutableValueGraph<>(this);
/*     */   }
/*     */   
/*     */   ValueGraphBuilder<N, V> copy() {
/* 184 */     ValueGraphBuilder<N, V> newBuilder = new ValueGraphBuilder(this.directed);
/* 185 */     newBuilder.allowsSelfLoops = this.allowsSelfLoops;
/* 186 */     newBuilder.nodeOrder = this.nodeOrder;
/* 187 */     newBuilder.expectedNodeCount = this.expectedNodeCount;
/* 188 */     newBuilder.incidentEdgeOrder = this.incidentEdgeOrder;
/* 189 */     return newBuilder;
/*     */   }
/*     */ 
/*     */   
/*     */   private <N1 extends N, V1 extends V> ValueGraphBuilder<N1, V1> cast() {
/* 194 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\graph\ValueGraphBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */