/*     */ package org.apache.commons.lang3.exception;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.commons.lang3.tuple.ImmutablePair;
/*     */ import org.apache.commons.lang3.tuple.Pair;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultExceptionContext
/*     */   implements ExceptionContext, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 20110706L;
/*  47 */   private final List<Pair<String, Object>> contextValues = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultExceptionContext addContextValue(String label, Object value) {
/*  54 */     this.contextValues.add(new ImmutablePair(label, value));
/*  55 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Pair<String, Object>> getContextEntries() {
/*  63 */     return this.contextValues;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getContextLabels() {
/*  71 */     return (Set<String>)stream().map(Pair::getKey).collect(Collectors.toSet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Object> getContextValues(String label) {
/*  79 */     return (List<Object>)stream().filter(pair -> StringUtils.equals(label, (CharSequence)pair.getKey())).map(Pair::getValue).collect(Collectors.toList());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getFirstContextValue(String label) {
/*  87 */     return stream().filter(pair -> StringUtils.equals(label, (CharSequence)pair.getKey())).findFirst().map(Pair::getValue).orElse(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedExceptionMessage(String baseMessage) {
/*  98 */     StringBuilder buffer = new StringBuilder(256);
/*  99 */     if (baseMessage != null) {
/* 100 */       buffer.append(baseMessage);
/*     */     }
/*     */     
/* 103 */     if (!this.contextValues.isEmpty()) {
/* 104 */       if (buffer.length() > 0) {
/* 105 */         buffer.append('\n');
/*     */       }
/* 107 */       buffer.append("Exception Context:\n");
/*     */       
/* 109 */       int i = 0;
/* 110 */       for (Pair<String, Object> pair : this.contextValues) {
/* 111 */         buffer.append("\t[");
/* 112 */         buffer.append(++i);
/* 113 */         buffer.append(':');
/* 114 */         buffer.append((String)pair.getKey());
/* 115 */         buffer.append("=");
/* 116 */         Object value = pair.getValue();
/* 117 */         if (value == null) {
/* 118 */           buffer.append("null");
/*     */         } else {
/*     */           String valueStr;
/*     */           try {
/* 122 */             valueStr = value.toString();
/* 123 */           } catch (Exception e) {
/* 124 */             valueStr = "Exception thrown on toString(): " + ExceptionUtils.getStackTrace(e);
/*     */           } 
/* 126 */           buffer.append(valueStr);
/*     */         } 
/* 128 */         buffer.append("]\n");
/*     */       } 
/* 130 */       buffer.append("---------------------------------");
/*     */     } 
/* 132 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultExceptionContext setContextValue(String label, Object value) {
/* 140 */     this.contextValues.removeIf(p -> StringUtils.equals(label, (CharSequence)p.getKey()));
/* 141 */     addContextValue(label, value);
/* 142 */     return this;
/*     */   }
/*     */   
/*     */   private Stream<Pair<String, Object>> stream() {
/* 146 */     return this.contextValues.stream();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\exception\DefaultExceptionContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */