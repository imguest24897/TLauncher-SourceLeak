/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.time.Duration;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.commons.lang3.time.DurationUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ThreadUtils
/*     */ {
/*     */   @Deprecated
/*     */   private static final class AlwaysTruePredicate
/*     */     implements ThreadPredicate, ThreadGroupPredicate
/*     */   {
/*     */     private AlwaysTruePredicate() {}
/*     */     
/*     */     public boolean test(Thread thread) {
/*  56 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(ThreadGroup threadGroup) {
/*  61 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static class NamePredicate
/*     */     implements ThreadPredicate, ThreadGroupPredicate
/*     */   {
/*     */     private final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public NamePredicate(String name) {
/*  85 */       Objects.requireNonNull(name, "name");
/*  86 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(Thread thread) {
/*  91 */       return (thread != null && thread.getName().equals(this.name));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(ThreadGroup threadGroup) {
/*  96 */       return (threadGroup != null && threadGroup.getName().equals(this.name));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @FunctionalInterface
/*     */   public static interface ThreadGroupPredicate
/*     */   {
/*     */     boolean test(ThreadGroup param1ThreadGroup);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static class ThreadIdPredicate
/*     */     implements ThreadPredicate
/*     */   {
/*     */     private final long threadId;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ThreadIdPredicate(long threadId) {
/* 135 */       if (threadId <= 0L) {
/* 136 */         throw new IllegalArgumentException("The thread id must be greater than zero");
/*     */       }
/* 138 */       this.threadId = threadId;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(Thread thread) {
/* 143 */       return (thread != null && thread.getId() == this.threadId);
/*     */     }
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
/*     */   @Deprecated
/* 171 */   public static final AlwaysTruePredicate ALWAYS_TRUE_PREDICATE = new AlwaysTruePredicate();
/*     */   
/*     */   private static final Predicate<?> ALWAYS_TRUE = t -> true;
/*     */ 
/*     */   
/*     */   private static <T> Predicate<T> alwaysTruePredicate() {
/* 177 */     return (Predicate)ALWAYS_TRUE;
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
/*     */   public static Thread findThreadById(long threadId) {
/* 193 */     if (threadId <= 0L) {
/* 194 */       throw new IllegalArgumentException("The thread id must be greater than zero");
/*     */     }
/* 196 */     Collection<Thread> result = findThreads(t -> (t != null && t.getId() == threadId));
/* 197 */     return result.isEmpty() ? null : result.iterator().next();
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
/*     */   public static Thread findThreadById(long threadId, String threadGroupName) {
/* 216 */     Objects.requireNonNull(threadGroupName, "threadGroupName");
/* 217 */     Thread thread = findThreadById(threadId);
/* 218 */     if (thread != null && thread.getThreadGroup() != null && thread.getThreadGroup().getName().equals(threadGroupName)) {
/* 219 */       return thread;
/*     */     }
/* 221 */     return null;
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
/*     */   public static Thread findThreadById(long threadId, ThreadGroup threadGroup) {
/* 240 */     Objects.requireNonNull(threadGroup, "threadGroup");
/* 241 */     Thread thread = findThreadById(threadId);
/* 242 */     if (thread != null && threadGroup.equals(thread.getThreadGroup())) {
/* 243 */       return thread;
/*     */     }
/* 245 */     return null;
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
/*     */   public static Collection<ThreadGroup> findThreadGroups(Predicate<ThreadGroup> predicate) {
/* 261 */     return findThreadGroups(getSystemThreadGroup(), true, predicate);
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
/*     */   public static Collection<ThreadGroup> findThreadGroups(ThreadGroup threadGroup, boolean recurse, Predicate<ThreadGroup> predicate) {
/* 277 */     Objects.requireNonNull(threadGroup, "threadGroup");
/* 278 */     Objects.requireNonNull(predicate, "predicate");
/*     */     
/* 280 */     int count = threadGroup.activeGroupCount();
/*     */     
/*     */     while (true) {
/* 283 */       ThreadGroup[] threadGroups = new ThreadGroup[count + count / 2 + 1];
/* 284 */       count = threadGroup.enumerate(threadGroups, recurse);
/*     */       
/* 286 */       if (count < threadGroups.length) {
/* 287 */         return Collections.unmodifiableCollection((Collection<? extends ThreadGroup>)Stream.<ThreadGroup>of(threadGroups).filter(predicate).collect(Collectors.toList()));
/*     */       }
/*     */     } 
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
/*     */   @Deprecated
/*     */   public static Collection<ThreadGroup> findThreadGroups(ThreadGroup threadGroup, boolean recurse, ThreadGroupPredicate predicate) {
/* 304 */     return findThreadGroups(threadGroup, recurse, predicate::test);
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
/*     */   @Deprecated
/*     */   public static Collection<ThreadGroup> findThreadGroups(ThreadGroupPredicate predicate) {
/* 321 */     return findThreadGroups(getSystemThreadGroup(), true, predicate);
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
/*     */   public static Collection<ThreadGroup> findThreadGroupsByName(String threadGroupName) {
/* 337 */     return findThreadGroups(predicateThreadGroup(threadGroupName));
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
/*     */   public static Collection<Thread> findThreads(Predicate<Thread> predicate) {
/* 354 */     return findThreads(getSystemThreadGroup(), true, predicate);
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
/*     */   public static Collection<Thread> findThreads(ThreadGroup threadGroup, boolean recurse, Predicate<Thread> predicate) {
/* 370 */     Objects.requireNonNull(threadGroup, "The group must not be null");
/* 371 */     Objects.requireNonNull(predicate, "The predicate must not be null");
/* 372 */     int count = threadGroup.activeCount();
/*     */     
/*     */     while (true) {
/* 375 */       Thread[] threads = new Thread[count + count / 2 + 1];
/* 376 */       count = threadGroup.enumerate(threads, recurse);
/*     */       
/* 378 */       if (count < threads.length) {
/* 379 */         return Collections.unmodifiableCollection((Collection<? extends Thread>)Stream.<Thread>of(threads).filter(predicate).collect(Collectors.toList()));
/*     */       }
/*     */     } 
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
/*     */   @Deprecated
/*     */   public static Collection<Thread> findThreads(ThreadGroup threadGroup, boolean recurse, ThreadPredicate predicate) {
/* 396 */     return findThreads(threadGroup, recurse, predicate::test);
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
/*     */   @Deprecated
/*     */   public static Collection<Thread> findThreads(ThreadPredicate predicate) {
/* 414 */     return findThreads(getSystemThreadGroup(), true, predicate);
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
/*     */   public static Collection<Thread> findThreadsByName(String threadName) {
/* 430 */     return findThreads(predicateThread(threadName));
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
/*     */   public static Collection<Thread> findThreadsByName(String threadName, String threadGroupName) {
/* 448 */     Objects.requireNonNull(threadName, "threadName");
/* 449 */     Objects.requireNonNull(threadGroupName, "threadGroupName");
/* 450 */     return Collections.unmodifiableCollection((Collection<? extends Thread>)findThreadGroups(predicateThreadGroup(threadGroupName)).stream()
/* 451 */         .flatMap(group -> findThreads(group, false, predicateThread(threadName)).stream()).collect(Collectors.toList()));
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
/*     */   public static Collection<Thread> findThreadsByName(String threadName, ThreadGroup threadGroup) {
/* 469 */     return findThreads(threadGroup, false, predicateThread(threadName));
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
/*     */   public static Collection<ThreadGroup> getAllThreadGroups() {
/* 483 */     return findThreadGroups(alwaysTruePredicate());
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
/*     */   public static Collection<Thread> getAllThreads() {
/* 497 */     return findThreads(alwaysTruePredicate());
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
/*     */   public static ThreadGroup getSystemThreadGroup() {
/* 511 */     ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
/* 512 */     while (threadGroup != null && threadGroup.getParent() != null) {
/* 513 */       threadGroup = threadGroup.getParent();
/*     */     }
/* 515 */     return threadGroup;
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
/*     */   public static void join(Thread thread, Duration duration) throws InterruptedException {
/* 528 */     DurationUtils.accept(thread::join, duration);
/*     */   }
/*     */   
/*     */   private static <T> Predicate<T> namePredicate(String name, Function<T, String> nameGetter) {
/* 532 */     return t -> (t != null && Objects.equals(nameGetter.apply(t), Objects.requireNonNull(name)));
/*     */   }
/*     */   
/*     */   private static Predicate<Thread> predicateThread(String threadName) {
/* 536 */     return namePredicate(threadName, Thread::getName);
/*     */   }
/*     */   
/*     */   private static Predicate<ThreadGroup> predicateThreadGroup(String threadGroupName) {
/* 540 */     return namePredicate(threadGroupName, ThreadGroup::getName);
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
/*     */   public static void sleep(Duration duration) throws InterruptedException {
/* 552 */     DurationUtils.accept(Thread::sleep, duration);
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
/*     */   public static void sleepQuietly(Duration duration) {
/*     */     try {
/* 566 */       sleep(duration);
/* 567 */     } catch (InterruptedException interruptedException) {}
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   @FunctionalInterface
/*     */   public static interface ThreadPredicate {
/*     */     boolean test(Thread param1Thread);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\ThreadUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */