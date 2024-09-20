/*    */ package ch.qos.logback.core.util;
/*    */ 
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import java.util.concurrent.Executors;
/*    */ import java.util.concurrent.ScheduledExecutorService;
/*    */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*    */ import java.util.concurrent.SynchronousQueue;
/*    */ import java.util.concurrent.ThreadFactory;
/*    */ import java.util.concurrent.ThreadPoolExecutor;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
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
/*    */ public class ExecutorServiceUtil
/*    */ {
/* 36 */   private static final ThreadFactory THREAD_FACTORY = new ThreadFactory()
/*    */     {
/* 38 */       private final ThreadFactory defaultFactory = Executors.defaultThreadFactory();
/* 39 */       private final AtomicInteger threadNumber = new AtomicInteger(1);
/*    */       
/*    */       public Thread newThread(Runnable r) {
/* 42 */         Thread thread = this.defaultFactory.newThread(r);
/* 43 */         if (!thread.isDaemon()) {
/* 44 */           thread.setDaemon(true);
/*    */         }
/* 46 */         thread.setName("logback-" + this.threadNumber.getAndIncrement());
/* 47 */         return thread;
/*    */       }
/*    */     };
/*    */   
/*    */   public static ScheduledExecutorService newScheduledExecutorService() {
/* 52 */     return new ScheduledThreadPoolExecutor(8, THREAD_FACTORY);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ExecutorService newExecutorService() {
/* 61 */     return new ThreadPoolExecutor(0, 32, 0L, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(), THREAD_FACTORY);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void shutdown(ExecutorService executorService) {
/* 71 */     executorService.shutdownNow();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\ch\qos\logback\cor\\util\ExecutorServiceUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */