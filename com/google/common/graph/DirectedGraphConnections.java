/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ final class DirectedGraphConnections<N, V>
/*     */   implements GraphConnections<N, V>
/*     */ {
/*     */   private static final class PredAndSucc
/*     */   {
/*     */     private final Object successorValue;
/*     */     
/*     */     PredAndSucc(Object successorValue) {
/*  62 */       this.successorValue = successorValue;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class NodeConnection<N>
/*     */   {
/*     */     final N node;
/*     */ 
/*     */ 
/*     */     
/*     */     NodeConnection(N node) {
/*  76 */       this.node = (N)Preconditions.checkNotNull(node);
/*     */     }
/*     */     
/*     */     static final class Pred<N> extends NodeConnection<N> {
/*     */       Pred(N node) {
/*  81 */         super(node);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean equals(Object that) {
/*  86 */         if (that instanceof Pred) {
/*  87 */           return this.node.equals(((Pred)that).node);
/*     */         }
/*  89 */         return false;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public int hashCode() {
/*  96 */         return Pred.class.hashCode() + this.node.hashCode();
/*     */       }
/*     */     }
/*     */     
/*     */     static final class Succ<N> extends NodeConnection<N> {
/*     */       Succ(N node) {
/* 102 */         super(node);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean equals(Object that) {
/* 107 */         if (that instanceof Succ) {
/* 108 */           return this.node.equals(((Succ)that).node);
/*     */         }
/* 110 */         return false;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public int hashCode()
/*     */       {
/* 117 */         return Succ.class.hashCode() + this.node.hashCode(); } } } static final class Pred<N> extends NodeConnection<N> { Pred(N node) { super(node); } public boolean equals(Object that) { if (that instanceof Pred) return this.node.equals(((Pred)that).node);  return false; } public int hashCode() { return Pred.class.hashCode() + this.node.hashCode(); } } static final class Succ<N> extends NodeConnection<N> { public int hashCode() { return Succ.class.hashCode() + this.node.hashCode(); } Succ(N node) { super(node); } public boolean equals(Object that) {
/*     */       if (that instanceof Succ)
/*     */         return this.node.equals(((Succ)that).node); 
/*     */       return false;
/*     */     } }
/* 122 */    private static final Object PRED = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map<N, Object> adjacentNodeValues;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final List<NodeConnection<N>> orderedNodeConnections;
/*     */ 
/*     */ 
/*     */   
/*     */   private int predecessorCount;
/*     */ 
/*     */ 
/*     */   
/*     */   private int successorCount;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DirectedGraphConnections(Map<N, Object> adjacentNodeValues, List<NodeConnection<N>> orderedNodeConnections, int predecessorCount, int successorCount) {
/* 146 */     this.adjacentNodeValues = (Map<N, Object>)Preconditions.checkNotNull(adjacentNodeValues);
/* 147 */     this.orderedNodeConnections = orderedNodeConnections;
/* 148 */     this.predecessorCount = Graphs.checkNonNegative(predecessorCount);
/* 149 */     this.successorCount = Graphs.checkNonNegative(successorCount);
/* 150 */     Preconditions.checkState((predecessorCount <= adjacentNodeValues
/* 151 */         .size() && successorCount <= adjacentNodeValues
/* 152 */         .size()));
/*     */   }
/*     */   
/*     */   static <N, V> DirectedGraphConnections<N, V> of(ElementOrder<N> incidentEdgeOrder) {
/*     */     List<NodeConnection<N>> orderedNodeConnections;
/* 157 */     int initialCapacity = 4;
/*     */ 
/*     */     
/* 160 */     switch (incidentEdgeOrder.type()) {
/*     */       case UNORDERED:
/* 162 */         orderedNodeConnections = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 171 */         return new DirectedGraphConnections<>(new HashMap<>(initialCapacity, 1.0F), orderedNodeConnections, 0, 0);case STABLE: orderedNodeConnections = new ArrayList<>(); return new DirectedGraphConnections<>(new HashMap<>(initialCapacity, 1.0F), orderedNodeConnections, 0, 0);
/*     */     } 
/*     */     throw new AssertionError(incidentEdgeOrder.type());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <N, V> DirectedGraphConnections<N, V> ofImmutable(N thisNode, Iterable<EndpointPair<N>> incidentEdges, Function<N, V> successorNodeToValueFn) {
/* 180 */     Preconditions.checkNotNull(thisNode);
/* 181 */     Preconditions.checkNotNull(successorNodeToValueFn);
/*     */     
/* 183 */     Map<N, Object> adjacentNodeValues = new HashMap<>();
/*     */     
/* 185 */     ImmutableList.Builder<NodeConnection<N>> orderedNodeConnectionsBuilder = ImmutableList.builder();
/* 186 */     int predecessorCount = 0;
/* 187 */     int successorCount = 0;
/*     */     
/* 189 */     for (EndpointPair<N> incidentEdge : incidentEdges) {
/* 190 */       if (incidentEdge.nodeU().equals(thisNode) && incidentEdge.nodeV().equals(thisNode)) {
/*     */ 
/*     */         
/* 193 */         adjacentNodeValues.put(thisNode, new PredAndSucc(successorNodeToValueFn.apply(thisNode)));
/*     */         
/* 195 */         orderedNodeConnectionsBuilder.add(new NodeConnection.Pred<>(thisNode));
/* 196 */         orderedNodeConnectionsBuilder.add(new NodeConnection.Succ<>(thisNode));
/* 197 */         predecessorCount++;
/* 198 */         successorCount++; continue;
/* 199 */       }  if (incidentEdge.nodeV().equals(thisNode)) {
/* 200 */         N predecessor = incidentEdge.nodeU();
/*     */         
/* 202 */         Object object = adjacentNodeValues.put(predecessor, PRED);
/* 203 */         if (object != null) {
/* 204 */           adjacentNodeValues.put(predecessor, new PredAndSucc(object));
/*     */         }
/*     */         
/* 207 */         orderedNodeConnectionsBuilder.add(new NodeConnection.Pred<>(predecessor));
/* 208 */         predecessorCount++; continue;
/*     */       } 
/* 210 */       Preconditions.checkArgument(incidentEdge.nodeU().equals(thisNode));
/*     */       
/* 212 */       N successor = incidentEdge.nodeV();
/* 213 */       V value = (V)successorNodeToValueFn.apply(successor);
/*     */       
/* 215 */       Object existingValue = adjacentNodeValues.put(successor, value);
/* 216 */       if (existingValue != null) {
/* 217 */         Preconditions.checkArgument((existingValue == PRED));
/* 218 */         adjacentNodeValues.put(successor, new PredAndSucc(value));
/*     */       } 
/*     */       
/* 221 */       orderedNodeConnectionsBuilder.add(new NodeConnection.Succ<>(successor));
/* 222 */       successorCount++;
/*     */     } 
/*     */ 
/*     */     
/* 226 */     return new DirectedGraphConnections<>(adjacentNodeValues, (List<NodeConnection<N>>)orderedNodeConnectionsBuilder
/*     */         
/* 228 */         .build(), predecessorCount, successorCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<N> adjacentNodes() {
/* 235 */     if (this.orderedNodeConnections == null) {
/* 236 */       return Collections.unmodifiableSet(this.adjacentNodeValues.keySet());
/*     */     }
/* 238 */     return new AbstractSet<N>()
/*     */       {
/*     */         public UnmodifiableIterator<N> iterator() {
/* 241 */           final Iterator<DirectedGraphConnections.NodeConnection<N>> nodeConnections = DirectedGraphConnections.this.orderedNodeConnections.iterator();
/* 242 */           final Set<N> seenNodes = new HashSet<>();
/* 243 */           return (UnmodifiableIterator<N>)new AbstractIterator<N>(this)
/*     */             {
/*     */               protected N computeNext() {
/* 246 */                 while (nodeConnections.hasNext()) {
/* 247 */                   DirectedGraphConnections.NodeConnection<N> nodeConnection = nodeConnections.next();
/* 248 */                   boolean added = seenNodes.add(nodeConnection.node);
/* 249 */                   if (added) {
/* 250 */                     return nodeConnection.node;
/*     */                   }
/*     */                 } 
/* 253 */                 return (N)endOfData();
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/* 260 */           return DirectedGraphConnections.this.adjacentNodeValues.size();
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean contains(Object obj) {
/* 265 */           return DirectedGraphConnections.this.adjacentNodeValues.containsKey(obj);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<N> predecessors() {
/* 273 */     return new AbstractSet<N>()
/*     */       {
/*     */         public UnmodifiableIterator<N> iterator() {
/* 276 */           if (DirectedGraphConnections.this.orderedNodeConnections == null) {
/* 277 */             final Iterator<Map.Entry<N, Object>> entries = DirectedGraphConnections.this.adjacentNodeValues.entrySet().iterator();
/* 278 */             return (UnmodifiableIterator<N>)new AbstractIterator<N>(this)
/*     */               {
/*     */                 protected N computeNext() {
/* 281 */                   while (entries.hasNext()) {
/* 282 */                     Map.Entry<N, Object> entry = entries.next();
/* 283 */                     if (DirectedGraphConnections.isPredecessor(entry.getValue())) {
/* 284 */                       return entry.getKey();
/*     */                     }
/*     */                   } 
/* 287 */                   return (N)endOfData();
/*     */                 }
/*     */               };
/*     */           } 
/* 291 */           final Iterator<DirectedGraphConnections.NodeConnection<N>> nodeConnections = DirectedGraphConnections.this.orderedNodeConnections.iterator();
/* 292 */           return (UnmodifiableIterator<N>)new AbstractIterator<N>(this)
/*     */             {
/*     */               protected N computeNext() {
/* 295 */                 while (nodeConnections.hasNext()) {
/* 296 */                   DirectedGraphConnections.NodeConnection<N> nodeConnection = nodeConnections.next();
/* 297 */                   if (nodeConnection instanceof DirectedGraphConnections.NodeConnection.Pred) {
/* 298 */                     return nodeConnection.node;
/*     */                   }
/*     */                 } 
/* 301 */                 return (N)endOfData();
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public int size() {
/* 309 */           return DirectedGraphConnections.this.predecessorCount;
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean contains(Object obj) {
/* 314 */           return DirectedGraphConnections.isPredecessor(DirectedGraphConnections.this.adjacentNodeValues.get(obj));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> successors() {
/* 321 */     return new AbstractSet<N>()
/*     */       {
/*     */         public UnmodifiableIterator<N> iterator() {
/* 324 */           if (DirectedGraphConnections.this.orderedNodeConnections == null) {
/* 325 */             final Iterator<Map.Entry<N, Object>> entries = DirectedGraphConnections.this.adjacentNodeValues.entrySet().iterator();
/* 326 */             return (UnmodifiableIterator<N>)new AbstractIterator<N>(this)
/*     */               {
/*     */                 protected N computeNext() {
/* 329 */                   while (entries.hasNext()) {
/* 330 */                     Map.Entry<N, Object> entry = entries.next();
/* 331 */                     if (DirectedGraphConnections.isSuccessor(entry.getValue())) {
/* 332 */                       return entry.getKey();
/*     */                     }
/*     */                   } 
/* 335 */                   return (N)endOfData();
/*     */                 }
/*     */               };
/*     */           } 
/* 339 */           final Iterator<DirectedGraphConnections.NodeConnection<N>> nodeConnections = DirectedGraphConnections.this.orderedNodeConnections.iterator();
/* 340 */           return (UnmodifiableIterator<N>)new AbstractIterator<N>(this)
/*     */             {
/*     */               protected N computeNext() {
/* 343 */                 while (nodeConnections.hasNext()) {
/* 344 */                   DirectedGraphConnections.NodeConnection<N> nodeConnection = nodeConnections.next();
/* 345 */                   if (nodeConnection instanceof DirectedGraphConnections.NodeConnection.Succ) {
/* 346 */                     return nodeConnection.node;
/*     */                   }
/*     */                 } 
/* 349 */                 return (N)endOfData();
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public int size() {
/* 357 */           return DirectedGraphConnections.this.successorCount;
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean contains(Object obj) {
/* 362 */           return DirectedGraphConnections.isSuccessor(DirectedGraphConnections.this.adjacentNodeValues.get(obj));
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public Iterator<EndpointPair<N>> incidentEdgeIterator(final N thisNode) {
/*     */     final Iterator<EndpointPair<N>> resultWithDoubleSelfLoop;
/* 369 */     Preconditions.checkNotNull(thisNode);
/*     */ 
/*     */     
/* 372 */     if (this.orderedNodeConnections == null) {
/*     */       
/* 374 */       resultWithDoubleSelfLoop = Iterators.concat(
/* 375 */           Iterators.transform(
/* 376 */             predecessors().iterator(), new Function<N, EndpointPair<N>>(this)
/*     */             {
/*     */               public EndpointPair<N> apply(N predecessor)
/*     */               {
/* 380 */                 return EndpointPair.ordered(predecessor, (N)thisNode);
/*     */               }
/* 383 */             }), Iterators.transform(
/* 384 */             successors().iterator(), new Function<N, EndpointPair<N>>(this)
/*     */             {
/*     */               public EndpointPair<N> apply(N successor)
/*     */               {
/* 388 */                 return EndpointPair.ordered((N)thisNode, successor);
/*     */               }
/*     */             }));
/*     */     } else {
/*     */       
/* 393 */       resultWithDoubleSelfLoop = Iterators.transform(this.orderedNodeConnections
/* 394 */           .iterator(), new Function<NodeConnection<N>, EndpointPair<N>>(this)
/*     */           {
/*     */             public EndpointPair<N> apply(DirectedGraphConnections.NodeConnection<N> connection)
/*     */             {
/* 398 */               if (connection instanceof DirectedGraphConnections.NodeConnection.Succ) {
/* 399 */                 return EndpointPair.ordered((N)thisNode, connection.node);
/*     */               }
/* 401 */               return EndpointPair.ordered(connection.node, (N)thisNode);
/*     */             }
/*     */           });
/*     */     } 
/*     */ 
/*     */     
/* 407 */     final AtomicBoolean alreadySeenSelfLoop = new AtomicBoolean(false);
/* 408 */     return (Iterator<EndpointPair<N>>)new AbstractIterator<EndpointPair<N>>(this)
/*     */       {
/*     */         protected EndpointPair<N> computeNext() {
/* 411 */           while (resultWithDoubleSelfLoop.hasNext()) {
/* 412 */             EndpointPair<N> edge = resultWithDoubleSelfLoop.next();
/* 413 */             if (edge.nodeU().equals(edge.nodeV())) {
/* 414 */               if (!alreadySeenSelfLoop.getAndSet(true))
/* 415 */                 return edge; 
/*     */               continue;
/*     */             } 
/* 418 */             return edge;
/*     */           } 
/*     */           
/* 421 */           return (EndpointPair<N>)endOfData();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V value(N node) {
/* 429 */     Preconditions.checkNotNull(node);
/* 430 */     Object value = this.adjacentNodeValues.get(node);
/* 431 */     if (value == PRED) {
/* 432 */       return null;
/*     */     }
/* 434 */     if (value instanceof PredAndSucc) {
/* 435 */       return (V)((PredAndSucc)value).successorValue;
/*     */     }
/* 437 */     return (V)value;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removePredecessor(N node) {
/*     */     boolean removedPredecessor;
/* 443 */     Preconditions.checkNotNull(node);
/*     */     
/* 445 */     Object previousValue = this.adjacentNodeValues.get(node);
/*     */ 
/*     */     
/* 448 */     if (previousValue == PRED) {
/* 449 */       this.adjacentNodeValues.remove(node);
/* 450 */       removedPredecessor = true;
/* 451 */     } else if (previousValue instanceof PredAndSucc) {
/* 452 */       this.adjacentNodeValues.put(node, ((PredAndSucc)previousValue).successorValue);
/* 453 */       removedPredecessor = true;
/*     */     } else {
/* 455 */       removedPredecessor = false;
/*     */     } 
/*     */     
/* 458 */     if (removedPredecessor) {
/* 459 */       Graphs.checkNonNegative(--this.predecessorCount);
/*     */       
/* 461 */       if (this.orderedNodeConnections != null) {
/* 462 */         this.orderedNodeConnections.remove(new NodeConnection.Pred<>(node));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public V removeSuccessor(Object node) {
/*     */     Object removedValue;
/* 470 */     Preconditions.checkNotNull(node);
/* 471 */     Object previousValue = this.adjacentNodeValues.get(node);
/*     */ 
/*     */     
/* 474 */     if (previousValue == null || previousValue == PRED) {
/* 475 */       removedValue = null;
/* 476 */     } else if (previousValue instanceof PredAndSucc) {
/* 477 */       this.adjacentNodeValues.put((N)node, PRED);
/* 478 */       removedValue = ((PredAndSucc)previousValue).successorValue;
/*     */     } else {
/* 480 */       this.adjacentNodeValues.remove(node);
/* 481 */       removedValue = previousValue;
/*     */     } 
/*     */     
/* 484 */     if (removedValue != null) {
/* 485 */       Graphs.checkNonNegative(--this.successorCount);
/*     */       
/* 487 */       if (this.orderedNodeConnections != null) {
/* 488 */         this.orderedNodeConnections.remove(new NodeConnection.Succ(node));
/*     */       }
/*     */     } 
/*     */     
/* 492 */     return (V)removedValue;
/*     */   }
/*     */   
/*     */   public void addPredecessor(N node, V unused) {
/*     */     boolean addedPredecessor;
/* 497 */     Object previousValue = this.adjacentNodeValues.put(node, PRED);
/*     */ 
/*     */     
/* 500 */     if (previousValue == null) {
/* 501 */       addedPredecessor = true;
/* 502 */     } else if (previousValue instanceof PredAndSucc) {
/*     */       
/* 504 */       this.adjacentNodeValues.put(node, previousValue);
/* 505 */       addedPredecessor = false;
/* 506 */     } else if (previousValue != PRED) {
/*     */       
/* 508 */       this.adjacentNodeValues.put(node, new PredAndSucc(previousValue));
/* 509 */       addedPredecessor = true;
/*     */     } else {
/* 511 */       addedPredecessor = false;
/*     */     } 
/*     */     
/* 514 */     if (addedPredecessor) {
/* 515 */       Graphs.checkPositive(++this.predecessorCount);
/*     */       
/* 517 */       if (this.orderedNodeConnections != null) {
/* 518 */         this.orderedNodeConnections.add(new NodeConnection.Pred<>(node));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V addSuccessor(N node, V value) {
/* 526 */     Object previousSuccessor, previousValue = this.adjacentNodeValues.put(node, value);
/*     */ 
/*     */     
/* 529 */     if (previousValue == null) {
/* 530 */       previousSuccessor = null;
/* 531 */     } else if (previousValue instanceof PredAndSucc) {
/* 532 */       this.adjacentNodeValues.put(node, new PredAndSucc(value));
/* 533 */       previousSuccessor = ((PredAndSucc)previousValue).successorValue;
/* 534 */     } else if (previousValue == PRED) {
/* 535 */       this.adjacentNodeValues.put(node, new PredAndSucc(value));
/* 536 */       previousSuccessor = null;
/*     */     } else {
/* 538 */       previousSuccessor = previousValue;
/*     */     } 
/*     */     
/* 541 */     if (previousSuccessor == null) {
/* 542 */       Graphs.checkPositive(++this.successorCount);
/*     */       
/* 544 */       if (this.orderedNodeConnections != null) {
/* 545 */         this.orderedNodeConnections.add(new NodeConnection.Succ<>(node));
/*     */       }
/*     */     } 
/*     */     
/* 549 */     return (V)previousSuccessor;
/*     */   }
/*     */   
/*     */   private static boolean isPredecessor(Object value) {
/* 553 */     return (value == PRED || value instanceof PredAndSucc);
/*     */   }
/*     */   
/*     */   private static boolean isSuccessor(Object value) {
/* 557 */     return (value != PRED && value != null);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\graph\DirectedGraphConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */