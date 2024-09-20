/*    */ package com.google.common.graph;
/*    */ 
/*    */ import com.google.common.base.Optional;
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
/*    */ 
/*    */ abstract class AbstractGraphBuilder<N>
/*    */ {
/*    */   final boolean directed;
/*    */   boolean allowsSelfLoops = false;
/* 29 */   ElementOrder<N> nodeOrder = ElementOrder.insertion();
/* 30 */   ElementOrder<N> incidentEdgeOrder = ElementOrder.unordered();
/*    */   
/* 32 */   Optional<Integer> expectedNodeCount = Optional.absent();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   AbstractGraphBuilder(boolean directed) {
/* 41 */     this.directed = directed;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\graph\AbstractGraphBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */