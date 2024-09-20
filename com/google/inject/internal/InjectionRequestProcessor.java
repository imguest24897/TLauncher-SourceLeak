/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.inject.ConfigurationException;
/*     */ import com.google.inject.Stage;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.spi.InjectionPoint;
/*     */ import com.google.inject.spi.InjectionRequest;
/*     */ import com.google.inject.spi.StaticInjectionRequest;
/*     */ import java.util.List;
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
/*     */ final class InjectionRequestProcessor
/*     */   extends AbstractProcessor
/*     */ {
/*  39 */   private final List<StaticInjection> staticInjections = Lists.newArrayList();
/*     */   private final Initializer initializer;
/*     */   
/*     */   InjectionRequestProcessor(Errors errors, Initializer initializer) {
/*  43 */     super(errors);
/*  44 */     this.initializer = initializer;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean visit(StaticInjectionRequest request) {
/*  49 */     this.staticInjections.add(new StaticInjection(this.injector, request));
/*  50 */     this.injector.getBindingData().putStaticInjectionRequest(request);
/*  51 */     return Boolean.valueOf(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean visit(InjectionRequest<?> request) {
/*     */     Set<InjectionPoint> injectionPoints;
/*     */     try {
/*  58 */       injectionPoints = request.getInjectionPoints();
/*  59 */     } catch (ConfigurationException e) {
/*  60 */       this.errors.merge(e.getErrorMessages());
/*  61 */       injectionPoints = (Set<InjectionPoint>)e.getPartialValue();
/*     */     } 
/*     */     
/*  64 */     this.initializer.requestInjection(this.injector, request
/*  65 */         .getInstance(), null, request.getSource(), injectionPoints);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  71 */     this.injector
/*  72 */       .getBindingData()
/*  73 */       .putInjectionRequest(new InjectionRequest(request
/*     */           
/*  75 */           .getSource(), 
/*  76 */           TypeLiteral.get(request.getInstance().getClass()), null));
/*     */     
/*  78 */     return Boolean.valueOf(true);
/*     */   }
/*     */   
/*     */   void validate() {
/*  82 */     for (StaticInjection staticInjection : this.staticInjections) {
/*  83 */       staticInjection.validate();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void injectMembers() {
/*  93 */     for (StaticInjection staticInjection : this.staticInjections) {
/*  94 */       staticInjection.injectMembers();
/*     */     }
/*     */   }
/*     */   
/*     */   private class StaticInjection
/*     */   {
/*     */     final InjectorImpl injector;
/*     */     final Object source;
/*     */     final StaticInjectionRequest request;
/*     */     ImmutableList<SingleMemberInjector> memberInjectors;
/*     */     
/*     */     public StaticInjection(InjectorImpl injector, StaticInjectionRequest request) {
/* 106 */       this.injector = injector;
/* 107 */       this.source = request.getSource();
/* 108 */       this.request = request;
/*     */     }
/*     */     void validate() {
/*     */       Set<InjectionPoint> injectionPoints;
/* 112 */       Errors errorsForMember = InjectionRequestProcessor.this.errors.withSource(this.source);
/*     */       
/*     */       try {
/* 115 */         injectionPoints = this.request.getInjectionPoints();
/* 116 */       } catch (ConfigurationException e) {
/* 117 */         errorsForMember.merge(e.getErrorMessages());
/* 118 */         injectionPoints = (Set<InjectionPoint>)e.getPartialValue();
/*     */       } 
/* 120 */       if (injectionPoints != null) {
/* 121 */         this
/* 122 */           .memberInjectors = this.injector.membersInjectorStore.getInjectors(injectionPoints, errorsForMember);
/*     */       } else {
/* 124 */         this.memberInjectors = ImmutableList.of();
/*     */       } 
/*     */       
/* 127 */       InjectionRequestProcessor.this.errors.merge(errorsForMember);
/*     */     }
/*     */     
/*     */     void injectMembers() {
/* 131 */       InternalContext context = this.injector.enterContext();
/*     */       try {
/* 133 */         boolean isStageTool = (this.injector.options.stage == Stage.TOOL);
/* 134 */         for (UnmodifiableIterator<SingleMemberInjector> unmodifiableIterator = this.memberInjectors.iterator(); unmodifiableIterator.hasNext(); ) { SingleMemberInjector memberInjector = unmodifiableIterator.next();
/*     */ 
/*     */           
/* 137 */           if (!isStageTool || memberInjector.getInjectionPoint().isToolable()) {
/*     */             try {
/* 139 */               memberInjector.inject(context, null);
/* 140 */             } catch (InternalProvisionException e) {
/* 141 */               InjectionRequestProcessor.this.errors.merge(e);
/*     */             } 
/*     */           } }
/*     */       
/*     */       } finally {
/* 146 */         context.close();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\InjectionRequestProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */