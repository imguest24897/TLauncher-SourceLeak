/*    */ package com.google.common.graph;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Beta
/*    */ public abstract class AbstractGraph<N>
/*    */   extends AbstractBaseGraph<N>
/*    */   implements Graph<N>
/*    */ {
/*    */   public final boolean equals(Object obj) {
/* 35 */     if (obj == this) {
/* 36 */       return true;
/*    */     }
/* 38 */     if (!(obj instanceof Graph)) {
/* 39 */       return false;
/*    */     }
/* 41 */     Graph<?> other = (Graph)obj;
/*    */     
/* 43 */     return (isDirected() == other.isDirected() && 
/* 44 */       nodes().equals(other.nodes()) && 
/* 45 */       edges().equals(other.edges()));
/*    */   }
/*    */ 
/*    */   
/*    */   public final int hashCode() {
/* 50 */     return edges().hashCode();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     boolean bool1 = isDirected();
/*    */     
/* 59 */     boolean bool2 = allowsSelfLoops();
/*    */     
/* 61 */     String str1 = String.valueOf(nodes());
/*    */     
/* 63 */     String str2 = String.valueOf(edges()); return (new StringBuilder(59 + String.valueOf(str1).length() + String.valueOf(str2).length())).append("isDirected: ").append(bool1).append(", allowsSelfLoops: ").append(bool2).append(", nodes: ").append(str1).append(", edges: ").append(str2).toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\graph\AbstractGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */