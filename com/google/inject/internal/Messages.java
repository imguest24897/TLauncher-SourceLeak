/*     */ package com.google.inject.internal;
/*     */ 
/*     */ import com.google.common.base.Equivalence;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.internal.util.Classes;
/*     */ import com.google.inject.spi.ElementSource;
/*     */ import com.google.inject.spi.ErrorDetail;
/*     */ import com.google.inject.spi.Message;
/*     */ import java.lang.reflect.Member;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Formatter;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Messages
/*     */ {
/*     */   static Message mergeSources(List<Object> sources, Message message) {
/*  44 */     List<Object> messageSources = message.getSources();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  50 */     if (!sources.isEmpty() && 
/*  51 */       !messageSources.isEmpty() && 
/*  52 */       Objects.equal(messageSources.get(0), sources.get(sources.size() - 1))) {
/*  53 */       messageSources = messageSources.subList(1, messageSources.size());
/*     */     }
/*  55 */     return message.withSource(
/*  56 */         (List)ImmutableList.builder().addAll(sources).addAll(messageSources).build());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String format(String messageFormat, Object... arguments) {
/*  64 */     for (int i = 0; i < arguments.length; i++) {
/*  65 */       arguments[i] = convert(arguments[i]);
/*     */     }
/*  67 */     return String.format(messageFormat, arguments);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String formatMessages(String heading, Collection<Message> errorMessages) {
/*  72 */     Formatter fmt = (new Formatter()).format(heading, new Object[0]).format(":%n%n", new Object[0]);
/*  73 */     int index = 1;
/*  74 */     boolean displayCauses = (getOnlyCause(errorMessages) == null);
/*     */ 
/*     */     
/*  77 */     List<ErrorDetail<?>> remainingErrors = (List<ErrorDetail<?>>)errorMessages.stream().map(Message::getErrorDetail).collect(Collectors.toList());
/*     */     
/*  79 */     Map<Equivalence.Wrapper<Throwable>, Integer> causes = Maps.newHashMap();
/*  80 */     while (!remainingErrors.isEmpty()) {
/*  81 */       ErrorDetail<?> currentError = remainingErrors.get(0);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  86 */       Objects.requireNonNull(currentError); Map<Boolean, List<ErrorDetail<?>>> partitionedByMergeable = (Map<Boolean, List<ErrorDetail<?>>>)remainingErrors.subList(1, remainingErrors.size()).stream().collect(Collectors.partitioningBy(currentError::isMergeable));
/*     */       
/*  88 */       remainingErrors = partitionedByMergeable.get(Boolean.valueOf(false));
/*     */       
/*  90 */       currentError.format(index, partitionedByMergeable.get(Boolean.valueOf(true)), fmt);
/*     */       
/*  92 */       Throwable cause = currentError.getCause();
/*  93 */       if (displayCauses && cause != null) {
/*  94 */         Equivalence.Wrapper<Throwable> causeEquivalence = ThrowableEquivalence.INSTANCE.wrap(cause);
/*  95 */         if (!causes.containsKey(causeEquivalence)) {
/*  96 */           causes.put(causeEquivalence, Integer.valueOf(index));
/*  97 */           fmt.format("Caused by: %s", new Object[] { Throwables.getStackTraceAsString(cause) });
/*     */         } else {
/*  99 */           int causeIdx = ((Integer)causes.get(causeEquivalence)).intValue();
/* 100 */           fmt.format("Caused by: %s (same stack trace as error #%s)", new Object[] { cause
/*     */                 
/* 102 */                 .getClass().getName(), Integer.valueOf(causeIdx) });
/*     */         } 
/*     */       } 
/* 105 */       fmt.format("%n", new Object[0]);
/* 106 */       index++;
/*     */     } 
/*     */     
/* 109 */     if (index == 2) {
/* 110 */       fmt.format("1 error", new Object[0]);
/*     */     } else {
/* 112 */       fmt.format("%s errors", new Object[] { Integer.valueOf(index - 1) });
/*     */     } 
/*     */     
/* 115 */     return PackageNameCompressor.compressPackagesInMessage(fmt.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Message create(ErrorId errorId, String messageFormat, Object... arguments) {
/* 126 */     return create(errorId, null, messageFormat, arguments);
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
/*     */   public static Message create(ErrorId errorId, Throwable cause, String messageFormat, Object... arguments) {
/* 139 */     return create(errorId, cause, (List<Object>)ImmutableList.of(), messageFormat, arguments);
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
/*     */   public static Message create(ErrorId errorId, Throwable cause, List<Object> sources, String messageFormat, Object... arguments) {
/* 157 */     String message = format(messageFormat, arguments);
/* 158 */     return new Message(errorId, sources, message, cause);
/*     */   }
/*     */ 
/*     */   
/*     */   static Object convert(Object o) {
/* 163 */     ElementSource source = null;
/* 164 */     if (o instanceof ElementSource) {
/* 165 */       source = (ElementSource)o;
/* 166 */       o = source.getDeclaringSource();
/*     */     } 
/* 168 */     return convert(o, source);
/*     */   }
/*     */   
/*     */   static Object convert(Object o, ElementSource source) {
/* 172 */     for (Converter<?> converter : converters) {
/* 173 */       if (converter.appliesTo(o)) {
/* 174 */         return appendModules(converter.convert(o), source);
/*     */       }
/*     */     } 
/* 177 */     return appendModules(o, source);
/*     */   }
/*     */   
/*     */   private static Object appendModules(Object source, ElementSource elementSource) {
/* 181 */     String modules = SourceFormatter.getModuleStack(elementSource);
/* 182 */     if (modules.length() == 0) {
/* 183 */       return source;
/*     */     }
/* 185 */     String str1 = String.valueOf(source); return (new StringBuilder(17 + String.valueOf(str1).length() + String.valueOf(modules).length())).append(str1).append(" (installed by: ").append(modules).append(")").toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static abstract class Converter<T>
/*     */   {
/*     */     final Class<T> type;
/*     */     
/*     */     Converter(Class<T> type) {
/* 194 */       this.type = type;
/*     */     }
/*     */     
/*     */     boolean appliesTo(Object o) {
/* 198 */       return (o != null && this.type.isAssignableFrom(o.getClass()));
/*     */     }
/*     */     
/*     */     String convert(Object o) {
/* 202 */       return toString(this.type.cast(o));
/*     */     }
/*     */ 
/*     */     
/*     */     abstract String toString(T param1T);
/*     */   }
/*     */ 
/*     */   
/* 210 */   private static final Collection<Converter<?>> converters = (Collection<Converter<?>>)ImmutableList.of(new Converter<Class>(Class.class)
/*     */       {
/*     */         public String toString(Class c)
/*     */         {
/* 214 */           return c.getName();
/*     */         }
/*     */       },  new Converter<Member>(Member.class)
/*     */       {
/*     */         public String toString(Member member)
/*     */         {
/* 220 */           return Classes.toString(member);
/*     */         }
/*     */       },  new Converter<Key>(Key.class)
/*     */       {
/*     */         public String toString(Key key)
/*     */         {
/* 226 */           if (key.getAnnotationType() != null) {
/* 227 */             String str1 = String.valueOf(key.getTypeLiteral());
/*     */             
/* 229 */             String str2 = String.valueOf((key.getAnnotation() != null) ? key.getAnnotation() : key.getAnnotationType()); return (new StringBuilder(16 + String.valueOf(str1).length() + String.valueOf(str2).length())).append(str1).append(" annotated with ").append(str2).toString();
/*     */           } 
/* 231 */           return key.getTypeLiteral().toString();
/*     */         }
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Throwable getOnlyCause(Collection<Message> messages) {
/* 241 */     Throwable onlyCause = null;
/* 242 */     for (Message message : messages) {
/* 243 */       Throwable messageCause = message.getCause();
/* 244 */       if (messageCause == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 248 */       if (onlyCause != null && !ThrowableEquivalence.INSTANCE.equivalent(onlyCause, messageCause)) {
/* 249 */         return null;
/*     */       }
/*     */       
/* 252 */       onlyCause = messageCause;
/*     */     } 
/*     */     
/* 255 */     return onlyCause;
/*     */   }
/*     */   
/*     */   private static final class ThrowableEquivalence extends Equivalence<Throwable> {
/* 259 */     static final ThrowableEquivalence INSTANCE = new ThrowableEquivalence();
/*     */ 
/*     */     
/*     */     protected boolean doEquivalent(Throwable a, Throwable b) {
/* 263 */       return (a.getClass().equals(b.getClass()) && 
/* 264 */         Objects.equal(a.getMessage(), b.getMessage()) && 
/* 265 */         Arrays.equals((Object[])a.getStackTrace(), (Object[])b.getStackTrace()) && 
/* 266 */         equivalent(a.getCause(), b.getCause()));
/*     */     }
/*     */ 
/*     */     
/*     */     protected int doHash(Throwable t) {
/* 271 */       return Objects.hashCode(new Object[] { Integer.valueOf(t.getClass().hashCode()), t.getMessage(), Integer.valueOf(hash(t.getCause())) });
/*     */     }
/*     */   }
/*     */   
/*     */   private enum FormatOptions {
/* 276 */     RED("\033[31m"),
/* 277 */     BOLD("\033[1m"),
/* 278 */     FAINT("\033[2m"),
/* 279 */     ITALIC("\033[3m"),
/* 280 */     UNDERLINE("\033[4m"),
/* 281 */     RESET("\033[0m");
/*     */     
/*     */     private final String ansiCode;
/*     */     
/*     */     FormatOptions(String ansiCode) {
/* 286 */       this.ansiCode = ansiCode;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final String formatText(String text, FormatOptions... options) {
/* 291 */     if (!InternalFlags.enableColorizeErrorMessages()) {
/* 292 */       return text;
/*     */     }
/* 294 */     return String.format("%s%s%s", new Object[] {
/*     */           
/* 296 */           Arrays.<FormatOptions>stream(options).map(option -> FormatOptions.access$000(option)).collect(Collectors.joining()), text, 
/*     */           
/* 298 */           FormatOptions.access$000(FormatOptions.RESET) });
/*     */   }
/*     */   
/*     */   public static final String bold(String text) {
/* 302 */     return formatText(text, new FormatOptions[] { FormatOptions.BOLD });
/*     */   }
/*     */   
/*     */   public static final String redBold(String text) {
/* 306 */     return formatText(text, new FormatOptions[] { FormatOptions.RED, FormatOptions.BOLD });
/*     */   }
/*     */   
/*     */   public static final String underline(String text) {
/* 310 */     return formatText(text, new FormatOptions[] { FormatOptions.UNDERLINE });
/*     */   }
/*     */   
/*     */   public static final String faint(String text) {
/* 314 */     return formatText(text, new FormatOptions[] { FormatOptions.FAINT });
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\Messages.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */