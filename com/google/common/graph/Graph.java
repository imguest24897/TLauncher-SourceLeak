package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.DoNotMock;
import java.util.Set;

@DoNotMock("Use GraphBuilder to create a real instance")
@Beta
public interface Graph<N> extends BaseGraph<N> {
  Set<N> nodes();
  
  Set<EndpointPair<N>> edges();
  
  boolean isDirected();
  
  boolean allowsSelfLoops();
  
  ElementOrder<N> nodeOrder();
  
  ElementOrder<N> incidentEdgeOrder();
  
  Set<N> adjacentNodes(N paramN);
  
  Set<N> predecessors(N paramN);
  
  Set<N> successors(N paramN);
  
  Set<EndpointPair<N>> incidentEdges(N paramN);
  
  int degree(N paramN);
  
  int inDegree(N paramN);
  
  int outDegree(N paramN);
  
  boolean hasEdgeConnecting(N paramN1, N paramN2);
  
  boolean hasEdgeConnecting(EndpointPair<N> paramEndpointPair);
  
  boolean equals(Object paramObject);
  
  int hashCode();
}


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\graph\Graph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */