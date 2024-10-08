/*     */ package org.apache.commons.lang3.concurrent;
/*     */ 
/*     */ import java.util.EnumMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EventCountCircuitBreaker
/*     */   extends AbstractCircuitBreaker<Integer>
/*     */ {
/* 141 */   private static final Map<AbstractCircuitBreaker.State, StateStrategy> STRATEGY_MAP = createStrategyMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final AtomicReference<CheckIntervalData> checkIntervalData;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int openingThreshold;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final long openingInterval;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int closingThreshold;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final long closingInterval;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EventCountCircuitBreaker(int openingThreshold, long openingInterval, TimeUnit openingUnit, int closingThreshold, long closingInterval, TimeUnit closingUnit) {
/* 177 */     this.checkIntervalData = new AtomicReference<>(new CheckIntervalData(0, 0L));
/* 178 */     this.openingThreshold = openingThreshold;
/* 179 */     this.openingInterval = openingUnit.toNanos(openingInterval);
/* 180 */     this.closingThreshold = closingThreshold;
/* 181 */     this.closingInterval = closingUnit.toNanos(closingInterval);
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
/*     */   public EventCountCircuitBreaker(int openingThreshold, long checkInterval, TimeUnit checkUnit, int closingThreshold) {
/* 199 */     this(openingThreshold, checkInterval, checkUnit, closingThreshold, checkInterval, checkUnit);
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
/*     */   public EventCountCircuitBreaker(int threshold, long checkInterval, TimeUnit checkUnit) {
/* 214 */     this(threshold, checkInterval, checkUnit, threshold);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOpeningThreshold() {
/* 225 */     return this.openingThreshold;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getOpeningInterval() {
/* 234 */     return this.openingInterval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getClosingThreshold() {
/* 245 */     return this.closingThreshold;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getClosingInterval() {
/* 254 */     return this.closingInterval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkState() {
/* 264 */     return performStateCheck(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean incrementAndCheckState(Integer increment) {
/* 272 */     return performStateCheck(increment.intValue());
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
/*     */   public boolean incrementAndCheckState() {
/* 284 */     return incrementAndCheckState(Integer.valueOf(1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void open() {
/* 295 */     super.open();
/* 296 */     this.checkIntervalData.set(new CheckIntervalData(0, nanoTime()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 307 */     super.close();
/* 308 */     this.checkIntervalData.set(new CheckIntervalData(0, nanoTime()));
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
/*     */   private boolean performStateCheck(int increment) {
/*     */     CheckIntervalData currentData;
/*     */     CheckIntervalData nextData;
/*     */     AbstractCircuitBreaker.State currentState;
/*     */     do {
/* 324 */       long time = nanoTime();
/* 325 */       currentState = this.state.get();
/* 326 */       currentData = this.checkIntervalData.get();
/* 327 */       nextData = nextCheckIntervalData(increment, currentData, currentState, time);
/* 328 */     } while (!updateCheckIntervalData(currentData, nextData));
/*     */ 
/*     */ 
/*     */     
/* 332 */     if (stateStrategy(currentState).isStateTransition(this, currentData, nextData)) {
/* 333 */       currentState = currentState.oppositeState();
/* 334 */       changeStateAndStartNewCheckInterval(currentState);
/*     */     } 
/* 336 */     return !isOpen(currentState);
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
/*     */   private boolean updateCheckIntervalData(CheckIntervalData currentData, CheckIntervalData nextData) {
/* 351 */     return (currentData == nextData || this.checkIntervalData
/* 352 */       .compareAndSet(currentData, nextData));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void changeStateAndStartNewCheckInterval(AbstractCircuitBreaker.State newState) {
/* 362 */     changeState(newState);
/* 363 */     this.checkIntervalData.set(new CheckIntervalData(0, nanoTime()));
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
/*     */   private CheckIntervalData nextCheckIntervalData(int increment, CheckIntervalData currentData, AbstractCircuitBreaker.State currentState, long time) {
/*     */     CheckIntervalData nextData;
/* 380 */     if (stateStrategy(currentState).isCheckIntervalFinished(this, currentData, time)) {
/* 381 */       nextData = new CheckIntervalData(increment, time);
/*     */     } else {
/* 383 */       nextData = currentData.increment(increment);
/*     */     } 
/* 385 */     return nextData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long nanoTime() {
/* 395 */     return System.nanoTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static StateStrategy stateStrategy(AbstractCircuitBreaker.State state) {
/* 406 */     return STRATEGY_MAP.get(state);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Map<AbstractCircuitBreaker.State, StateStrategy> createStrategyMap() {
/* 416 */     Map<AbstractCircuitBreaker.State, StateStrategy> map = new EnumMap<>(AbstractCircuitBreaker.State.class);
/* 417 */     map.put(AbstractCircuitBreaker.State.CLOSED, new StateStrategyClosed());
/* 418 */     map.put(AbstractCircuitBreaker.State.OPEN, new StateStrategyOpen());
/* 419 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CheckIntervalData
/*     */   {
/*     */     private final int eventCount;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final long checkIntervalStart;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     CheckIntervalData(int count, long intervalStart) {
/* 441 */       this.eventCount = count;
/* 442 */       this.checkIntervalStart = intervalStart;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getEventCount() {
/* 451 */       return this.eventCount;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getCheckIntervalStart() {
/* 460 */       return this.checkIntervalStart;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CheckIntervalData increment(int delta) {
/* 471 */       return (delta == 0) ? this : new CheckIntervalData(getEventCount() + delta, 
/* 472 */           getCheckIntervalStart());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class StateStrategy
/*     */   {
/*     */     private StateStrategy() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isCheckIntervalFinished(EventCountCircuitBreaker breaker, EventCountCircuitBreaker.CheckIntervalData currentData, long now) {
/* 492 */       return (now - currentData.getCheckIntervalStart() > fetchCheckInterval(breaker));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public abstract boolean isStateTransition(EventCountCircuitBreaker param1EventCountCircuitBreaker, EventCountCircuitBreaker.CheckIntervalData param1CheckIntervalData1, EventCountCircuitBreaker.CheckIntervalData param1CheckIntervalData2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected abstract long fetchCheckInterval(EventCountCircuitBreaker param1EventCountCircuitBreaker);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class StateStrategyClosed
/*     */     extends StateStrategy
/*     */   {
/*     */     private StateStrategyClosed() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isStateTransition(EventCountCircuitBreaker breaker, EventCountCircuitBreaker.CheckIntervalData currentData, EventCountCircuitBreaker.CheckIntervalData nextData) {
/* 529 */       return (nextData.getEventCount() > breaker.getOpeningThreshold());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected long fetchCheckInterval(EventCountCircuitBreaker breaker) {
/* 537 */       return breaker.getOpeningInterval();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class StateStrategyOpen
/*     */     extends StateStrategy
/*     */   {
/*     */     private StateStrategyOpen() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isStateTransition(EventCountCircuitBreaker breaker, EventCountCircuitBreaker.CheckIntervalData currentData, EventCountCircuitBreaker.CheckIntervalData nextData) {
/* 551 */       return (nextData.getCheckIntervalStart() != currentData
/* 552 */         .getCheckIntervalStart() && currentData
/* 553 */         .getEventCount() < breaker.getClosingThreshold());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected long fetchCheckInterval(EventCountCircuitBreaker breaker) {
/* 561 */       return breaker.getClosingInterval();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\concurrent\EventCountCircuitBreaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */