/*     */ package com.google.inject.internal.util;
/*     */ 
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Member;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class StackTraceElements
/*     */ {
/*  35 */   private static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];
/*  36 */   private static final InMemoryStackTraceElement[] EMPTY_INMEMORY_STACK_TRACE = new InMemoryStackTraceElement[0];
/*     */ 
/*     */ 
/*     */   
/*  40 */   static final LoadingCache<Class<?>, LineNumbers> lineNumbersCache = CacheBuilder.newBuilder()
/*  41 */     .weakKeys()
/*  42 */     .softValues()
/*  43 */     .build(new CacheLoader<Class<?>, LineNumbers>()
/*     */       {
/*     */         public LineNumbers load(Class<?> key)
/*     */         {
/*     */           try {
/*  48 */             return new LineNumbers(key);
/*  49 */           } catch (IOException e) {
/*  50 */             throw new RuntimeException(e);
/*     */           } 
/*     */         }
/*     */       });
/*     */ 
/*     */   
/*  56 */   private static final ConcurrentMap<InMemoryStackTraceElement, InMemoryStackTraceElement> elementCache = new ConcurrentHashMap<>();
/*  57 */   private static final ConcurrentMap<String, String> stringCache = new ConcurrentHashMap<>();
/*     */   
/*     */   private static final String UNKNOWN_SOURCE = "Unknown Source";
/*     */   
/*     */   public static Object forMember(Member member) {
/*  62 */     if (member == null) {
/*  63 */       return SourceProvider.UNKNOWN_SOURCE;
/*     */     }
/*     */     
/*  66 */     Class<?> declaringClass = member.getDeclaringClass();
/*  67 */     LineNumbers lineNumbers = (LineNumbers)lineNumbersCache.getUnchecked(declaringClass);
/*  68 */     String fileName = lineNumbers.getSource();
/*  69 */     Integer lineNumberOrNull = lineNumbers.getLineNumber(member);
/*  70 */     int lineNumber = (lineNumberOrNull == null) ? lineNumbers.getFirstLine() : lineNumberOrNull.intValue();
/*  71 */     Class<? extends Member> memberType = Classes.memberType(member);
/*  72 */     String memberName = (memberType == Constructor.class) ? "<init>" : member.getName();
/*  73 */     return new StackTraceElement(declaringClass.getName(), memberName, fileName, lineNumber);
/*     */   }
/*     */   
/*     */   public static Object forType(Class<?> implementation) {
/*  77 */     LineNumbers lineNumbers = (LineNumbers)lineNumbersCache.getUnchecked(implementation);
/*  78 */     int lineNumber = lineNumbers.getFirstLine();
/*  79 */     String fileName = lineNumbers.getSource();
/*  80 */     return new StackTraceElement(implementation.getName(), "class", fileName, lineNumber);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void clearCache() {
/*  85 */     elementCache.clear();
/*  86 */     stringCache.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static InMemoryStackTraceElement[] convertToInMemoryStackTraceElement(StackTraceElement[] stackTraceElements) {
/*  92 */     if (stackTraceElements.length == 0) {
/*  93 */       return EMPTY_INMEMORY_STACK_TRACE;
/*     */     }
/*  95 */     InMemoryStackTraceElement[] inMemoryStackTraceElements = new InMemoryStackTraceElement[stackTraceElements.length];
/*     */     
/*  97 */     for (int i = 0; i < stackTraceElements.length; i++) {
/*  98 */       inMemoryStackTraceElements[i] = 
/*  99 */         weakIntern(new InMemoryStackTraceElement(stackTraceElements[i]));
/*     */     }
/* 101 */     return inMemoryStackTraceElements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StackTraceElement[] convertToStackTraceElement(InMemoryStackTraceElement[] inMemoryStackTraceElements) {
/* 109 */     if (inMemoryStackTraceElements.length == 0) {
/* 110 */       return EMPTY_STACK_TRACE;
/*     */     }
/* 112 */     StackTraceElement[] stackTraceElements = new StackTraceElement[inMemoryStackTraceElements.length];
/*     */     
/* 114 */     for (int i = 0; i < inMemoryStackTraceElements.length; i++) {
/* 115 */       String declaringClass = inMemoryStackTraceElements[i].getClassName();
/* 116 */       String methodName = inMemoryStackTraceElements[i].getMethodName();
/* 117 */       int lineNumber = inMemoryStackTraceElements[i].getLineNumber();
/* 118 */       stackTraceElements[i] = new StackTraceElement(declaringClass, methodName, "Unknown Source", lineNumber);
/*     */     } 
/*     */     
/* 121 */     return stackTraceElements;
/*     */   }
/*     */ 
/*     */   
/*     */   private static InMemoryStackTraceElement weakIntern(InMemoryStackTraceElement inMemoryStackTraceElement) {
/* 126 */     InMemoryStackTraceElement cached = elementCache.get(inMemoryStackTraceElement);
/* 127 */     if (cached != null) {
/* 128 */       return cached;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 134 */     inMemoryStackTraceElement = new InMemoryStackTraceElement(weakIntern(inMemoryStackTraceElement.getClassName()), weakIntern(inMemoryStackTraceElement.getMethodName()), inMemoryStackTraceElement.getLineNumber());
/* 135 */     elementCache.put(inMemoryStackTraceElement, inMemoryStackTraceElement);
/* 136 */     return inMemoryStackTraceElement;
/*     */   }
/*     */   
/*     */   private static String weakIntern(String s) {
/* 140 */     String cached = stringCache.get(s);
/* 141 */     if (cached != null) {
/* 142 */       return cached;
/*     */     }
/* 144 */     stringCache.put(s, s);
/* 145 */     return s;
/*     */   }
/*     */   
/*     */   public static class InMemoryStackTraceElement
/*     */   {
/*     */     private final String declaringClass;
/*     */     private final String methodName;
/*     */     private final int lineNumber;
/*     */     
/*     */     InMemoryStackTraceElement(StackTraceElement ste) {
/* 155 */       this(ste.getClassName(), ste.getMethodName(), ste.getLineNumber());
/*     */     }
/*     */     
/*     */     InMemoryStackTraceElement(String declaringClass, String methodName, int lineNumber) {
/* 159 */       this.declaringClass = declaringClass;
/* 160 */       this.methodName = methodName;
/* 161 */       this.lineNumber = lineNumber;
/*     */     }
/*     */     
/*     */     String getClassName() {
/* 165 */       return this.declaringClass;
/*     */     }
/*     */     
/*     */     String getMethodName() {
/* 169 */       return this.methodName;
/*     */     }
/*     */     
/*     */     int getLineNumber() {
/* 173 */       return this.lineNumber;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 178 */       if (obj == this) {
/* 179 */         return true;
/*     */       }
/* 181 */       if (!(obj instanceof InMemoryStackTraceElement)) {
/* 182 */         return false;
/*     */       }
/* 184 */       InMemoryStackTraceElement e = (InMemoryStackTraceElement)obj;
/* 185 */       return (e.declaringClass.equals(this.declaringClass) && e.lineNumber == this.lineNumber && this.methodName
/*     */         
/* 187 */         .equals(e.methodName));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 192 */       int result = 31 * this.declaringClass.hashCode() + this.methodName.hashCode();
/* 193 */       result = 31 * result + this.lineNumber;
/* 194 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 199 */       String str1 = this.declaringClass, str2 = this.methodName; int i = this.lineNumber; return (new StringBuilder(14 + String.valueOf(str1).length() + String.valueOf(str2).length())).append(str1).append(".").append(str2).append("(").append(i).append(")").toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\interna\\util\StackTraceElements.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */