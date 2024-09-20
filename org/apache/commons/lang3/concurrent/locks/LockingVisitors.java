/*     */ package org.apache.commons.lang3.concurrent.locks;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import java.util.concurrent.locks.StampedLock;
/*     */ import java.util.function.Supplier;
/*     */ import org.apache.commons.lang3.function.Failable;
/*     */ import org.apache.commons.lang3.function.FailableConsumer;
/*     */ import org.apache.commons.lang3.function.FailableFunction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LockingVisitors
/*     */ {
/*     */   public static class LockVisitor<O, L>
/*     */   {
/*     */     private final L lock;
/*     */     private final O object;
/*     */     private final Supplier<Lock> readLockSupplier;
/*     */     private final Supplier<Lock> writeLockSupplier;
/*     */     
/*     */     protected LockVisitor(O object, L lock, Supplier<Lock> readLockSupplier, Supplier<Lock> writeLockSupplier) {
/* 121 */       this.object = Objects.requireNonNull(object, "object");
/* 122 */       this.lock = Objects.requireNonNull(lock, "lock");
/* 123 */       this.readLockSupplier = Objects.<Supplier<Lock>>requireNonNull(readLockSupplier, "readLockSupplier");
/* 124 */       this.writeLockSupplier = Objects.<Supplier<Lock>>requireNonNull(writeLockSupplier, "writeLockSupplier");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void acceptReadLocked(FailableConsumer<O, ?> consumer) {
/* 145 */       lockAcceptUnlock(this.readLockSupplier, consumer);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void acceptWriteLocked(FailableConsumer<O, ?> consumer) {
/* 166 */       lockAcceptUnlock(this.writeLockSupplier, consumer);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> T applyReadLocked(FailableFunction<O, T, ?> function) {
/* 205 */       return lockApplyUnlock(this.readLockSupplier, function);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> T applyWriteLocked(FailableFunction<O, T, ?> function) {
/* 232 */       return lockApplyUnlock(this.writeLockSupplier, function);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public L getLock() {
/* 241 */       return this.lock;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public O getObject() {
/* 250 */       return this.object;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void lockAcceptUnlock(Supplier<Lock> lockSupplier, FailableConsumer<O, ?> consumer) {
/* 265 */       Lock lock = lockSupplier.get();
/* 266 */       lock.lock();
/*     */       try {
/* 268 */         consumer.accept(this.object);
/* 269 */       } catch (Throwable t) {
/* 270 */         throw Failable.rethrow(t);
/*     */       } finally {
/* 272 */         lock.unlock();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected <T> T lockApplyUnlock(Supplier<Lock> lockSupplier, FailableFunction<O, T, ?> function) {
/* 292 */       Lock lock = lockSupplier.get();
/* 293 */       lock.lock();
/*     */       try {
/* 295 */         return (T)function.apply(this.object);
/* 296 */       } catch (Throwable t) {
/* 297 */         throw Failable.rethrow(t);
/*     */       } finally {
/* 299 */         lock.unlock();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ReadWriteLockVisitor<O>
/*     */     extends LockVisitor<O, ReadWriteLock>
/*     */   {
/*     */     protected ReadWriteLockVisitor(O object, ReadWriteLock readWriteLock) {
/* 324 */       super(object, readWriteLock, readWriteLock::readLock, readWriteLock::writeLock);
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
/*     */   public static class StampedLockVisitor<O>
/*     */     extends LockVisitor<O, StampedLock>
/*     */   {
/*     */     protected StampedLockVisitor(O object, StampedLock stampedLock) {
/* 347 */       super(object, stampedLock, stampedLock::asReadLock, stampedLock::asWriteLock);
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
/*     */   public static <O> ReadWriteLockVisitor<O> create(O object, ReadWriteLock readWriteLock) {
/* 361 */     return new ReadWriteLockVisitor<>(object, readWriteLock);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <O> ReadWriteLockVisitor<O> reentrantReadWriteLockVisitor(O object) {
/* 372 */     return create(object, new ReentrantReadWriteLock());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <O> StampedLockVisitor<O> stampedLockVisitor(O object) {
/* 383 */     return new StampedLockVisitor<>(object, new StampedLock());
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\concurrent\locks\LockingVisitors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */