/*      */ package org.apache.commons.lang3.exception;
/*      */ 
/*      */ import java.io.PrintStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.StringWriter;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.UndeclaredThrowableException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.stream.Stream;
/*      */ import org.apache.commons.lang3.ArrayUtils;
/*      */ import org.apache.commons.lang3.ClassUtils;
/*      */ import org.apache.commons.lang3.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ExceptionUtils
/*      */ {
/*   49 */   private static final String[] CAUSE_METHOD_NAMES = new String[] { "getCause", "getNextException", "getTargetException", "getException", "getSourceException", "getRootCause", "getCausedByException", "getNested", "getLinkedException", "getNestedException", "getLinkedCause", "getThrowable" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int NOT_FOUND = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final String WRAPPED_MARKER = " [wrapped] ";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <R, T extends Throwable> R eraseType(Throwable throwable) throws T {
/*   82 */     throw (T)throwable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void forEach(Throwable throwable, Consumer<Throwable> consumer) {
/*  102 */     stream(throwable).forEach(consumer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Throwable getCause(Throwable throwable) {
/*  135 */     return getCause(throwable, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Throwable getCause(Throwable throwable, String[] methodNames) {
/*  153 */     if (throwable == null) {
/*  154 */       return null;
/*      */     }
/*  156 */     if (methodNames == null) {
/*  157 */       Throwable cause = throwable.getCause();
/*  158 */       if (cause != null) {
/*  159 */         return cause;
/*      */       }
/*  161 */       methodNames = CAUSE_METHOD_NAMES;
/*      */     } 
/*  163 */     return Stream.<String>of(methodNames).map(m -> getCauseUsingMethodName(throwable, m)).filter(Objects::nonNull).findFirst().orElse(null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Throwable getCauseUsingMethodName(Throwable throwable, String methodName) {
/*  175 */     if (methodName != null) {
/*  176 */       Method method = null;
/*      */       try {
/*  178 */         method = throwable.getClass().getMethod(methodName, new Class[0]);
/*  179 */       } catch (NoSuchMethodException|SecurityException noSuchMethodException) {}
/*      */ 
/*      */ 
/*      */       
/*  183 */       if (method != null && Throwable.class.isAssignableFrom(method.getReturnType())) {
/*      */         try {
/*  185 */           return (Throwable)method.invoke(throwable, new Object[0]);
/*  186 */         } catch (IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException illegalAccessException) {}
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  191 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static String[] getDefaultCauseMethodNames() {
/*  205 */     return (String[])ArrayUtils.clone((Object[])CAUSE_METHOD_NAMES);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getMessage(Throwable th) {
/*  220 */     if (th == null) {
/*  221 */       return "";
/*      */     }
/*  223 */     String clsName = ClassUtils.getShortClassName(th, null);
/*  224 */     return clsName + ": " + StringUtils.defaultString(th.getMessage());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Throwable getRootCause(Throwable throwable) {
/*  245 */     List<Throwable> list = getThrowableList(throwable);
/*  246 */     return list.isEmpty() ? null : list.get(list.size() - 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getRootCauseMessage(Throwable throwable) {
/*  261 */     Throwable root = getRootCause(throwable);
/*  262 */     return getMessage((root == null) ? throwable : root);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] getRootCauseStackTrace(Throwable throwable) {
/*  279 */     return getRootCauseStackTraceList(throwable).<String>toArray(ArrayUtils.EMPTY_STRING_ARRAY);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<String> getRootCauseStackTraceList(Throwable throwable) {
/*  295 */     if (throwable == null) {
/*  296 */       return Collections.emptyList();
/*      */     }
/*  298 */     Throwable[] throwables = getThrowables(throwable);
/*  299 */     int count = throwables.length;
/*  300 */     List<String> frames = new ArrayList<>();
/*  301 */     List<String> nextTrace = getStackFrameList(throwables[count - 1]);
/*  302 */     for (int i = count; --i >= 0; ) {
/*  303 */       List<String> trace = nextTrace;
/*  304 */       if (i != 0) {
/*  305 */         nextTrace = getStackFrameList(throwables[i - 1]);
/*  306 */         removeCommonFrames(trace, nextTrace);
/*      */       } 
/*  308 */       if (i == count - 1) {
/*  309 */         frames.add(throwables[i].toString());
/*      */       } else {
/*  311 */         frames.add(" [wrapped] " + throwables[i].toString());
/*      */       } 
/*  313 */       frames.addAll(trace);
/*      */     } 
/*  315 */     return frames;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static List<String> getStackFrameList(Throwable throwable) {
/*  331 */     String stackTrace = getStackTrace(throwable);
/*  332 */     String linebreak = System.lineSeparator();
/*  333 */     StringTokenizer frames = new StringTokenizer(stackTrace, linebreak);
/*  334 */     List<String> list = new ArrayList<>();
/*  335 */     boolean traceStarted = false;
/*  336 */     while (frames.hasMoreTokens()) {
/*  337 */       String token = frames.nextToken();
/*      */       
/*  339 */       int at = token.indexOf("at");
/*  340 */       if (at != -1 && token.substring(0, at).trim().isEmpty()) {
/*  341 */         traceStarted = true;
/*  342 */         list.add(token); continue;
/*  343 */       }  if (traceStarted) {
/*      */         break;
/*      */       }
/*      */     } 
/*  347 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String[] getStackFrames(String stackTrace) {
/*  359 */     String linebreak = System.lineSeparator();
/*  360 */     StringTokenizer frames = new StringTokenizer(stackTrace, linebreak);
/*  361 */     List<String> list = new ArrayList<>();
/*  362 */     while (frames.hasMoreTokens()) {
/*  363 */       list.add(frames.nextToken());
/*      */     }
/*  365 */     return list.<String>toArray(ArrayUtils.EMPTY_STRING_ARRAY);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] getStackFrames(Throwable throwable) {
/*  382 */     if (throwable == null) {
/*  383 */       return ArrayUtils.EMPTY_STRING_ARRAY;
/*      */     }
/*  385 */     return getStackFrames(getStackTrace(throwable));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getStackTrace(Throwable throwable) {
/*  401 */     if (throwable == null) {
/*  402 */       return "";
/*      */     }
/*  404 */     StringWriter sw = new StringWriter();
/*  405 */     throwable.printStackTrace(new PrintWriter(sw, true));
/*  406 */     return sw.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getThrowableCount(Throwable throwable) {
/*  426 */     return getThrowableList(throwable).size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<Throwable> getThrowableList(Throwable throwable) {
/*  449 */     List<Throwable> list = new ArrayList<>();
/*  450 */     while (throwable != null && !list.contains(throwable)) {
/*  451 */       list.add(throwable);
/*  452 */       throwable = throwable.getCause();
/*      */     } 
/*  454 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Throwable[] getThrowables(Throwable throwable) {
/*  477 */     return getThrowableList(throwable).<Throwable>toArray(ArrayUtils.EMPTY_THROWABLE_ARRAY);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean hasCause(Throwable chain, Class<? extends Throwable> type) {
/*  495 */     if (chain instanceof UndeclaredThrowableException) {
/*  496 */       chain = chain.getCause();
/*      */     }
/*  498 */     return type.isInstance(chain);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int indexOf(Throwable throwable, Class<? extends Throwable> type, int fromIndex, boolean subclass) {
/*  513 */     if (throwable == null || type == null) {
/*  514 */       return -1;
/*      */     }
/*  516 */     if (fromIndex < 0) {
/*  517 */       fromIndex = 0;
/*      */     }
/*  519 */     Throwable[] throwables = getThrowables(throwable);
/*  520 */     if (fromIndex >= throwables.length) {
/*  521 */       return -1;
/*      */     }
/*  523 */     if (subclass) {
/*  524 */       for (int i = fromIndex; i < throwables.length; i++) {
/*  525 */         if (type.isAssignableFrom(throwables[i].getClass())) {
/*  526 */           return i;
/*      */         }
/*      */       } 
/*      */     } else {
/*  530 */       for (int i = fromIndex; i < throwables.length; i++) {
/*  531 */         if (type.equals(throwables[i].getClass())) {
/*  532 */           return i;
/*      */         }
/*      */       } 
/*      */     } 
/*  536 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOfThrowable(Throwable throwable, Class<? extends Throwable> clazz) {
/*  554 */     return indexOf(throwable, clazz, 0, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOfThrowable(Throwable throwable, Class<? extends Throwable> clazz, int fromIndex) {
/*  577 */     return indexOf(throwable, clazz, fromIndex, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOfType(Throwable throwable, Class<? extends Throwable> type) {
/*  596 */     return indexOf(throwable, type, 0, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOfType(Throwable throwable, Class<? extends Throwable> type, int fromIndex) {
/*  620 */     return indexOf(throwable, type, fromIndex, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isChecked(Throwable throwable) {
/*  632 */     return (throwable != null && !(throwable instanceof Error) && !(throwable instanceof RuntimeException));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isUnchecked(Throwable throwable) {
/*  644 */     return (throwable != null && (throwable instanceof Error || throwable instanceof RuntimeException));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void printRootCauseStackTrace(Throwable throwable) {
/*  666 */     printRootCauseStackTrace(throwable, System.err);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void printRootCauseStackTrace(Throwable throwable, PrintStream printStream) {
/*  690 */     if (throwable == null) {
/*      */       return;
/*      */     }
/*  693 */     Objects.requireNonNull(printStream, "printStream");
/*  694 */     getRootCauseStackTraceList(throwable).forEach(printStream::println);
/*  695 */     printStream.flush();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void printRootCauseStackTrace(Throwable throwable, PrintWriter printWriter) {
/*  719 */     if (throwable == null) {
/*      */       return;
/*      */     }
/*  722 */     Objects.requireNonNull(printWriter, "printWriter");
/*  723 */     getRootCauseStackTraceList(throwable).forEach(printWriter::println);
/*  724 */     printWriter.flush();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void removeCommonFrames(List<String> causeFrames, List<String> wrapperFrames) {
/*  736 */     Objects.requireNonNull(causeFrames, "causeFrames");
/*  737 */     Objects.requireNonNull(wrapperFrames, "wrapperFrames");
/*  738 */     int causeFrameIndex = causeFrames.size() - 1;
/*  739 */     int wrapperFrameIndex = wrapperFrames.size() - 1;
/*  740 */     while (causeFrameIndex >= 0 && wrapperFrameIndex >= 0) {
/*      */ 
/*      */       
/*  743 */       String causeFrame = causeFrames.get(causeFrameIndex);
/*  744 */       String wrapperFrame = wrapperFrames.get(wrapperFrameIndex);
/*  745 */       if (causeFrame.equals(wrapperFrame)) {
/*  746 */         causeFrames.remove(causeFrameIndex);
/*      */       }
/*  748 */       causeFrameIndex--;
/*  749 */       wrapperFrameIndex--;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <R> R rethrow(Throwable throwable) {
/*  813 */     return eraseType(throwable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Stream<Throwable> stream(Throwable throwable) {
/*  835 */     return getThrowableList(throwable).stream();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <T extends Throwable> T throwableOf(Throwable throwable, Class<T> type, int fromIndex, boolean subclass) {
/*  851 */     if (throwable == null || type == null) {
/*  852 */       return null;
/*      */     }
/*  854 */     if (fromIndex < 0) {
/*  855 */       fromIndex = 0;
/*      */     }
/*  857 */     Throwable[] throwables = getThrowables(throwable);
/*  858 */     if (fromIndex >= throwables.length) {
/*  859 */       return null;
/*      */     }
/*  861 */     if (subclass) {
/*  862 */       for (int i = fromIndex; i < throwables.length; i++) {
/*  863 */         if (type.isAssignableFrom(throwables[i].getClass())) {
/*  864 */           return type.cast(throwables[i]);
/*      */         }
/*      */       } 
/*      */     } else {
/*  868 */       for (int i = fromIndex; i < throwables.length; i++) {
/*  869 */         if (type.equals(throwables[i].getClass())) {
/*  870 */           return type.cast(throwables[i]);
/*      */         }
/*      */       } 
/*      */     } 
/*  874 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends Throwable> T throwableOfThrowable(Throwable throwable, Class<T> clazz) {
/*  894 */     return throwableOf(throwable, clazz, 0, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends Throwable> T throwableOfThrowable(Throwable throwable, Class<T> clazz, int fromIndex) {
/*  919 */     return throwableOf(throwable, clazz, fromIndex, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends Throwable> T throwableOfType(Throwable throwable, Class<T> type) {
/*  939 */     return throwableOf(throwable, type, 0, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends Throwable> T throwableOfType(Throwable throwable, Class<T> type, int fromIndex) {
/*  964 */     return throwableOf(throwable, type, fromIndex, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T throwUnchecked(T throwable) {
/*  977 */     if (throwable instanceof RuntimeException) {
/*  978 */       throw (RuntimeException)throwable;
/*      */     }
/*  980 */     if (throwable instanceof Error) {
/*  981 */       throw (Error)throwable;
/*      */     }
/*  983 */     return throwable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <R> R wrapAndThrow(Throwable throwable) {
/* 1009 */     throw new UndeclaredThrowableException((Throwable)throwUnchecked(throwable));
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\exception\ExceptionUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */