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
/*     */ @Beta
/*     */ public final class NetworkBuilder<N, E>
/*     */   extends AbstractGraphBuilder<N>
/*     */ {
/*     */   boolean allowsParallelEdges = false;
/*  72 */   ElementOrder<? super E> edgeOrder = ElementOrder.insertion();
/*  73 */   Optional<Integer> expectedEdgeCount = Optional.absent();
/*     */ 
/*     */   
/*     */   private NetworkBuilder(boolean directed) {
/*  77 */     super(directed);
/*     */   }
/*     */ 
/*     */   
/*     */   public static NetworkBuilder<Object, Object> directed() {
/*  82 */     return new NetworkBuilder<>(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public static NetworkBuilder<Object, Object> undirected() {
/*  87 */     return new NetworkBuilder<>(false);
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
/*     */   public static <N, E> NetworkBuilder<N, E> from(Network<N, E> network) {
/*  99 */     return (new NetworkBuilder<>(network.isDirected()))
/* 100 */       .allowsParallelEdges(network.allowsParallelEdges())
/* 101 */       .allowsSelfLoops(network.allowsSelfLoops())
/* 102 */       .nodeOrder(network.nodeOrder())
/* 103 */       .edgeOrder(network.edgeOrder());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <N1 extends N, E1 extends E> ImmutableNetwork.Builder<N1, E1> immutable() {
/* 114 */     NetworkBuilder<N1, E1> castBuilder = cast();
/* 115 */     return new ImmutableNetwork.Builder<>(castBuilder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NetworkBuilder<N, E> allowsParallelEdges(boolean allowsParallelEdges) {
/* 125 */     this.allowsParallelEdges = allowsParallelEdges;
/* 126 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NetworkBuilder<N, E> allowsSelfLoops(boolean allowsSelfLoops) {
/* 137 */     this.allowsSelfLoops = allowsSelfLoops;
/* 138 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NetworkBuilder<N, E> expectedNodeCount(int expectedNodeCount) {
/* 147 */     this.expectedNodeCount = Optional.of(Integer.valueOf(Graphs.checkNonNegative(expectedNodeCount)));
/* 148 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NetworkBuilder<N, E> expectedEdgeCount(int expectedEdgeCount) {
/* 157 */     this.expectedEdgeCount = Optional.of(Integer.valueOf(Graphs.checkNonNegative(expectedEdgeCount)));
/* 158 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <N1 extends N> NetworkBuilder<N1, E> nodeOrder(ElementOrder<N1> nodeOrder) {
/* 167 */     NetworkBuilder<N1, E> newBuilder = cast();
/* 168 */     newBuilder.nodeOrder = (ElementOrder<N>)Preconditions.checkNotNull(nodeOrder);
/* 169 */     return newBuilder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <E1 extends E> NetworkBuilder<N, E1> edgeOrder(ElementOrder<E1> edgeOrder) {
/* 178 */     NetworkBuilder<N, E1> newBuilder = cast();
/* 179 */     newBuilder.edgeOrder = (ElementOrder<? super E>)Preconditions.checkNotNull(edgeOrder);
/* 180 */     return newBuilder;
/*     */   }
/*     */ 
/*     */   
/*     */   public <N1 extends N, E1 extends E> MutableNetwork<N1, E1> build() {
/* 185 */     return new StandardMutableNetwork<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private <N1 extends N, E1 extends E> NetworkBuilder<N1, E1> cast() {
/* 190 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\graph\NetworkBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */