/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.errorprone.annotations.DoNotMock;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
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
/*     */ @DoNotMock("Call forGraph or forTree, passing a lambda or a Graph with the desired edges (built with GraphBuilder)")
/*     */ @Beta
/*     */ public abstract class Traverser<N>
/*     */ {
/*     */   private final SuccessorsFunction<N> successorFunction;
/*     */   
/*     */   private Traverser(SuccessorsFunction<N> successorFunction) {
/*  70 */     this.successorFunction = (SuccessorsFunction<N>)Preconditions.checkNotNull(successorFunction);
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
/*     */   public static <N> Traverser<N> forGraph(final SuccessorsFunction<N> graph) {
/*  98 */     return new Traverser<N>(graph)
/*     */       {
/*     */         Traverser.Traversal<N> newTraversal() {
/* 101 */           return Traverser.Traversal.inGraph(graph);
/*     */         }
/*     */       };
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
/*     */   public static <N> Traverser<N> forTree(final SuccessorsFunction<N> tree) {
/* 180 */     if (tree instanceof BaseGraph) {
/* 181 */       Preconditions.checkArgument(((BaseGraph)tree).isDirected(), "Undirected graphs can never be trees.");
/*     */     }
/* 183 */     if (tree instanceof Network) {
/* 184 */       Preconditions.checkArgument(((Network)tree).isDirected(), "Undirected networks can never be trees.");
/*     */     }
/* 186 */     return new Traverser<N>(tree)
/*     */       {
/*     */         Traverser.Traversal<N> newTraversal() {
/* 189 */           return Traverser.Traversal.inTree(tree);
/*     */         }
/*     */       };
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
/*     */   public final Iterable<N> breadthFirst(N startNode) {
/* 226 */     return breadthFirst((Iterable<? extends N>)ImmutableSet.of(startNode));
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
/*     */   public final Iterable<N> breadthFirst(Iterable<? extends N> startNodes) {
/* 240 */     final ImmutableSet<N> validated = validate(startNodes);
/* 241 */     return new Iterable<N>()
/*     */       {
/*     */         public Iterator<N> iterator() {
/* 244 */           return Traverser.this.newTraversal().breadthFirst((Iterator<? extends N>)validated.iterator());
/*     */         }
/*     */       };
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
/*     */   public final Iterable<N> depthFirstPreOrder(N startNode) {
/* 281 */     return depthFirstPreOrder((Iterable<? extends N>)ImmutableSet.of(startNode));
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
/*     */   public final Iterable<N> depthFirstPreOrder(Iterable<? extends N> startNodes) {
/* 295 */     final ImmutableSet<N> validated = validate(startNodes);
/* 296 */     return new Iterable<N>()
/*     */       {
/*     */         public Iterator<N> iterator() {
/* 299 */           return Traverser.this.newTraversal().preOrder((Iterator<? extends N>)validated.iterator());
/*     */         }
/*     */       };
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
/*     */   public final Iterable<N> depthFirstPostOrder(N startNode) {
/* 336 */     return depthFirstPostOrder((Iterable<? extends N>)ImmutableSet.of(startNode));
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
/*     */   public final Iterable<N> depthFirstPostOrder(Iterable<? extends N> startNodes) {
/* 350 */     final ImmutableSet<N> validated = validate(startNodes);
/* 351 */     return new Iterable<N>()
/*     */       {
/*     */         public Iterator<N> iterator() {
/* 354 */           return Traverser.this.newTraversal().postOrder((Iterator<? extends N>)validated.iterator());
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ImmutableSet<N> validate(Iterable<? extends N> startNodes) {
/* 363 */     ImmutableSet<N> copy = ImmutableSet.copyOf(startNodes);
/* 364 */     for (UnmodifiableIterator<N> unmodifiableIterator = copy.iterator(); unmodifiableIterator.hasNext(); ) { N node = unmodifiableIterator.next();
/* 365 */       this.successorFunction.successors(node); }
/*     */     
/* 367 */     return copy;
/*     */   }
/*     */ 
/*     */   
/*     */   abstract Traversal<N> newTraversal();
/*     */ 
/*     */   
/*     */   private static abstract class Traversal<N>
/*     */   {
/*     */     final SuccessorsFunction<N> successorFunction;
/*     */     
/*     */     Traversal(SuccessorsFunction<N> successorFunction) {
/* 379 */       this.successorFunction = successorFunction;
/*     */     }
/*     */     
/*     */     static <N> Traversal<N> inGraph(SuccessorsFunction<N> graph) {
/* 383 */       final Set<N> visited = new HashSet<>();
/* 384 */       return new Traversal<N>(graph)
/*     */         {
/*     */           N visitNext(Deque<Iterator<? extends N>> horizon) {
/* 387 */             Iterator<? extends N> top = horizon.getFirst();
/* 388 */             while (top.hasNext()) {
/* 389 */               N element = (N)Preconditions.checkNotNull(top.next());
/* 390 */               if (visited.add(element)) {
/* 391 */                 return element;
/*     */               }
/*     */             } 
/* 394 */             horizon.removeFirst();
/* 395 */             return null;
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     static <N> Traversal<N> inTree(SuccessorsFunction<N> tree) {
/* 401 */       return new Traversal<N>(tree)
/*     */         {
/*     */           N visitNext(Deque<Iterator<? extends N>> horizon) {
/* 404 */             Iterator<? extends N> top = horizon.getFirst();
/* 405 */             if (top.hasNext()) {
/* 406 */               return (N)Preconditions.checkNotNull(top.next());
/*     */             }
/* 408 */             horizon.removeFirst();
/* 409 */             return null;
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     final Iterator<N> breadthFirst(Iterator<? extends N> startNodes) {
/* 415 */       return topDown(startNodes, Traverser.InsertionOrder.BACK);
/*     */     }
/*     */     
/*     */     final Iterator<N> preOrder(Iterator<? extends N> startNodes) {
/* 419 */       return topDown(startNodes, Traverser.InsertionOrder.FRONT);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Iterator<N> topDown(Iterator<? extends N> startNodes, final Traverser.InsertionOrder order) {
/* 429 */       final Deque<Iterator<? extends N>> horizon = new ArrayDeque<>();
/* 430 */       horizon.add(startNodes);
/* 431 */       return (Iterator<N>)new AbstractIterator<N>()
/*     */         {
/*     */           protected N computeNext() {
/*     */             while (true) {
/* 435 */               N next = Traverser.Traversal.this.visitNext(horizon);
/* 436 */               if (next != null) {
/* 437 */                 Iterator<? extends N> successors = Traverser.Traversal.this.successorFunction.successors(next).iterator();
/* 438 */                 if (successors.hasNext())
/*     */                 {
/*     */                   
/* 441 */                   order.insertInto(horizon, successors);
/*     */                 }
/* 443 */                 return next;
/*     */               } 
/* 445 */               if (horizon.isEmpty())
/* 446 */                 return (N)endOfData(); 
/*     */             } 
/*     */           }
/*     */         };
/*     */     }
/*     */     final Iterator<N> postOrder(Iterator<? extends N> startNodes) {
/* 452 */       final Deque<N> ancestorStack = new ArrayDeque<>();
/* 453 */       final Deque<Iterator<? extends N>> horizon = new ArrayDeque<>();
/* 454 */       horizon.add(startNodes);
/* 455 */       return (Iterator<N>)new AbstractIterator<N>()
/*     */         {
/*     */           protected N computeNext() {
/* 458 */             for (N next = Traverser.Traversal.this.visitNext(horizon); next != null; next = Traverser.Traversal.this.visitNext(horizon)) {
/* 459 */               Iterator<? extends N> successors = Traverser.Traversal.this.successorFunction.successors(next).iterator();
/* 460 */               if (!successors.hasNext()) {
/* 461 */                 return next;
/*     */               }
/* 463 */               horizon.addFirst(successors);
/* 464 */               ancestorStack.push(next);
/*     */             } 
/* 466 */             return ancestorStack.isEmpty() ? (N)endOfData() : ancestorStack.pop();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract N visitNext(Deque<Iterator<? extends N>> param1Deque);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private enum InsertionOrder
/*     */   {
/* 487 */     FRONT
/*     */     {
/*     */       <T> void insertInto(Deque<T> deque, T value) {
/* 490 */         deque.addFirst(value);
/*     */       }
/*     */     },
/* 493 */     BACK
/*     */     {
/*     */       <T> void insertInto(Deque<T> deque, T value) {
/* 496 */         deque.addLast(value);
/*     */       }
/*     */     };
/*     */     
/*     */     abstract <T> void insertInto(Deque<T> param1Deque, T param1T);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\common\graph\Traverser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */