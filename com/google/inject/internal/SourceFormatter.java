/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.internal.util.Classes;
/*     */ import com.google.inject.internal.util.StackTraceElements;
/*     */ import com.google.inject.spi.Dependency;
/*     */ import com.google.inject.spi.ElementSource;
/*     */ import com.google.inject.spi.InjectionPoint;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Parameter;
/*     */ import java.util.Formatter;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ final class SourceFormatter
/*     */ {
/*  24 */   static final String INDENT = Strings.repeat(" ", 5);
/*     */   
/*     */   private final Object source;
/*     */   private final Formatter formatter;
/*     */   private final boolean omitPreposition;
/*     */   private final String moduleStack;
/*     */   
/*     */   SourceFormatter(Object source, Formatter formatter, boolean omitPreposition) {
/*  32 */     if (source instanceof ElementSource) {
/*  33 */       ElementSource elementSource = (ElementSource)source;
/*  34 */       this.source = elementSource.getDeclaringSource();
/*  35 */       this.moduleStack = getModuleStack(elementSource);
/*     */     } else {
/*  37 */       this.source = source;
/*  38 */       this.moduleStack = "";
/*     */     } 
/*  40 */     this.formatter = formatter;
/*  41 */     this.omitPreposition = omitPreposition;
/*     */   }
/*     */   
/*     */   void format() {
/*  45 */     boolean appendModuleSource = !this.moduleStack.isEmpty();
/*  46 */     if (this.source instanceof Dependency) {
/*  47 */       formatDependency((Dependency)this.source);
/*  48 */     } else if (this.source instanceof InjectionPoint) {
/*  49 */       formatInjectionPoint(null, (InjectionPoint)this.source);
/*  50 */     } else if (this.source instanceof Class) {
/*  51 */       this.formatter.format("%s%s%n", new Object[] { preposition("at "), StackTraceElements.forType((Class)this.source) });
/*  52 */     } else if (this.source instanceof Member) {
/*  53 */       formatMember((Member)this.source);
/*  54 */     } else if (this.source instanceof com.google.inject.TypeLiteral) {
/*  55 */       this.formatter.format("%s%s%n", new Object[] { preposition("while locating "), this.source });
/*  56 */     } else if (this.source instanceof Key) {
/*  57 */       formatKey((Key)this.source);
/*  58 */     } else if (this.source instanceof Thread) {
/*  59 */       appendModuleSource = false;
/*  60 */       this.formatter.format("%s%s%n", new Object[] { preposition("in thread "), this.source });
/*     */     } else {
/*  62 */       this.formatter.format("%s%s%n", new Object[] { preposition("at "), this.source });
/*     */     } 
/*     */     
/*  65 */     if (appendModuleSource) {
/*  66 */       this.formatter.format("%s \\_ installed by: %s%n", new Object[] { INDENT, this.moduleStack });
/*     */     }
/*     */   }
/*     */   
/*     */   private String preposition(String prepostition) {
/*  71 */     if (this.omitPreposition) {
/*  72 */       return "";
/*     */     }
/*  74 */     return prepostition;
/*     */   }
/*     */   
/*     */   private void formatDependency(Dependency<?> dependency) {
/*  78 */     InjectionPoint injectionPoint = dependency.getInjectionPoint();
/*  79 */     if (injectionPoint != null) {
/*  80 */       formatInjectionPoint(dependency, injectionPoint);
/*     */     } else {
/*  82 */       formatKey(dependency.getKey());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void formatKey(Key<?> key) {
/*  87 */     this.formatter.format("%s%s%n", new Object[] { preposition("while locating "), Messages.convert(key) });
/*     */   }
/*     */   
/*     */   private void formatMember(Member member) {
/*  91 */     this.formatter.format("%s%s%n", new Object[] { preposition("at "), StackTraceElements.forMember(member) });
/*     */   }
/*     */   
/*     */   private void formatInjectionPoint(Dependency<?> dependency, InjectionPoint injectionPoint) {
/*  95 */     Member member = injectionPoint.getMember();
/*  96 */     Class<? extends Member> memberType = Classes.memberType(member);
/*  97 */     formatMember(injectionPoint.getMember());
/*  98 */     if (memberType == Field.class) {
/*  99 */       this.formatter.format("%s \\_ for field %s%n", new Object[] { INDENT, Messages.redBold(member.getName()) });
/* 100 */     } else if (dependency != null) {
/*     */       
/* 102 */       this.formatter.format("%s \\_ for %s%n", new Object[] { INDENT, getParameterName(dependency) });
/*     */     } 
/*     */   }
/*     */   
/*     */   static String getModuleStack(ElementSource elementSource) {
/* 107 */     if (elementSource == null) {
/* 108 */       return "";
/*     */     }
/* 110 */     List<String> modules = Lists.newArrayList(elementSource.getModuleClassNames());
/*     */     
/* 112 */     while (elementSource.getOriginalElementSource() != null) {
/* 113 */       elementSource = elementSource.getOriginalElementSource();
/* 114 */       modules.addAll(0, elementSource.getModuleClassNames());
/*     */     } 
/* 116 */     if (modules.size() <= 1) {
/* 117 */       return "";
/*     */     }
/* 119 */     return String.join(" -> ", Lists.reverse(modules));
/*     */   }
/*     */   
/*     */   static String getParameterName(Dependency<?> dependency) {
/* 123 */     int parameterIndex = dependency.getParameterIndex();
/* 124 */     int ordinal = parameterIndex + 1;
/* 125 */     Member member = dependency.getInjectionPoint().getMember();
/* 126 */     Parameter parameter = null;
/* 127 */     if (member instanceof Constructor) {
/* 128 */       parameter = ((Constructor)member).getParameters()[parameterIndex];
/* 129 */     } else if (member instanceof Method) {
/* 130 */       parameter = ((Method)member).getParameters()[parameterIndex];
/*     */     } 
/* 132 */     String parameterName = "";
/* 133 */     if (parameter != null && parameter.isNamePresent()) {
/* 134 */       parameterName = parameter.getName();
/*     */     }
/* 136 */     (new Object[3])[0] = 
/*     */       
/* 138 */       Integer.valueOf(ordinal); (new Object[3])[1] = 
/* 139 */       getOrdinalSuffix(ordinal);
/* 140 */     String.valueOf(Messages.redBold(parameterName)); return String.format("%s%s parameter%s", new Object[] { null, null, parameterName.isEmpty() ? "" : ((String.valueOf(Messages.redBold(parameterName)).length() != 0) ? " ".concat(String.valueOf(Messages.redBold(parameterName))) : new String(" ")) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getOrdinalSuffix(int ordinal) {
/* 151 */     Preconditions.checkArgument((ordinal >= 0));
/* 152 */     if (ordinal / 10 % 10 == 1)
/*     */     {
/* 154 */       return "th";
/*     */     }
/*     */     
/* 157 */     switch (ordinal % 10) {
/*     */       case 1:
/* 159 */         return "st";
/*     */       case 2:
/* 161 */         return "nd";
/*     */       case 3:
/* 163 */         return "rd";
/*     */     } 
/* 165 */     return "th";
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\SourceFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */