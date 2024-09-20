/*      */ package org.apache.commons.lang3.builder;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Array;
/*      */ import java.util.Collection;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.WeakHashMap;
/*      */ import org.apache.commons.lang3.ClassUtils;
/*      */ import org.apache.commons.lang3.ObjectUtils;
/*      */ import org.apache.commons.lang3.StringEscapeUtils;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class ToStringStyle
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = -2587890625525655916L;
/*   84 */   public static final ToStringStyle DEFAULT_STYLE = new DefaultToStringStyle();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   98 */   public static final ToStringStyle MULTI_LINE_STYLE = new MultiLineToStringStyle();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  109 */   public static final ToStringStyle NO_FIELD_NAMES_STYLE = new NoFieldNameToStringStyle();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  121 */   public static final ToStringStyle SHORT_PREFIX_STYLE = new ShortPrefixToStringStyle();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  131 */   public static final ToStringStyle SIMPLE_STYLE = new SimpleToStringStyle();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  143 */   public static final ToStringStyle NO_CLASS_NAME_STYLE = new NoClassNameToStringStyle();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  162 */   public static final ToStringStyle JSON_STYLE = new JsonToStringStyle();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  169 */   private static final ThreadLocal<WeakHashMap<Object, Object>> REGISTRY = new ThreadLocal<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Map<Object, Object> getRegistry() {
/*  187 */     return REGISTRY.get();
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
/*      */   static boolean isRegistered(Object value) {
/*  200 */     Map<Object, Object> m = getRegistry();
/*  201 */     return (m != null && m.containsKey(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void register(Object value) {
/*  212 */     if (value != null) {
/*  213 */       Map<Object, Object> m = getRegistry();
/*  214 */       if (m == null) {
/*  215 */         REGISTRY.set(new WeakHashMap<>());
/*      */       }
/*  217 */       getRegistry().put(value, null);
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
/*      */   static void unregister(Object value) {
/*  232 */     if (value != null) {
/*  233 */       Map<Object, Object> m = getRegistry();
/*  234 */       if (m != null) {
/*  235 */         m.remove(value);
/*  236 */         if (m.isEmpty()) {
/*  237 */           REGISTRY.remove();
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean useFieldNames = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean useClassName = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean useShortClassName;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean useIdentityHashCode = true;
/*      */ 
/*      */ 
/*      */   
/*  266 */   private String contentStart = "[";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  271 */   private String contentEnd = "]";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  276 */   private String fieldNameValueSeparator = "=";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean fieldSeparatorAtStart;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean fieldSeparatorAtEnd;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  291 */   private String fieldSeparator = ",";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  296 */   private String arrayStart = "{";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  301 */   private String arraySeparator = ",";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean arrayContentDetail = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  311 */   private String arrayEnd = "}";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean defaultFullDetail = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  322 */   private String nullText = "<null>";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  327 */   private String sizeStartText = "<size=";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  332 */   private String sizeEndText = ">";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  337 */   private String summaryObjectStartText = "<";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  342 */   private String summaryObjectEndText = ">";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void appendSuper(StringBuffer buffer, String superToString) {
/*  361 */     appendToString(buffer, superToString);
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
/*      */   public void appendToString(StringBuffer buffer, String toString) {
/*  375 */     if (toString != null) {
/*  376 */       int pos1 = toString.indexOf(this.contentStart) + this.contentStart.length();
/*  377 */       int pos2 = toString.lastIndexOf(this.contentEnd);
/*  378 */       if (pos1 != pos2 && pos1 >= 0 && pos2 >= 0) {
/*  379 */         if (this.fieldSeparatorAtStart) {
/*  380 */           removeLastFieldSeparator(buffer);
/*      */         }
/*  382 */         buffer.append(toString, pos1, pos2);
/*  383 */         appendFieldSeparator(buffer);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void appendStart(StringBuffer buffer, Object object) {
/*  395 */     if (object != null) {
/*  396 */       appendClassName(buffer, object);
/*  397 */       appendIdentityHashCode(buffer, object);
/*  398 */       appendContentStart(buffer);
/*  399 */       if (this.fieldSeparatorAtStart) {
/*  400 */         appendFieldSeparator(buffer);
/*      */       }
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
/*      */   public void appendEnd(StringBuffer buffer, Object object) {
/*  413 */     if (!this.fieldSeparatorAtEnd) {
/*  414 */       removeLastFieldSeparator(buffer);
/*      */     }
/*  416 */     appendContentEnd(buffer);
/*  417 */     unregister(object);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void removeLastFieldSeparator(StringBuffer buffer) {
/*  427 */     if (StringUtils.endsWith(buffer, this.fieldSeparator)) {
/*  428 */       buffer.setLength(buffer.length() - this.fieldSeparator.length());
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
/*      */   public void append(StringBuffer buffer, String fieldName, Object value, Boolean fullDetail) {
/*  444 */     appendFieldStart(buffer, fieldName);
/*      */     
/*  446 */     if (value == null) {
/*  447 */       appendNullText(buffer, fieldName);
/*      */     } else {
/*      */       
/*  450 */       appendInternal(buffer, fieldName, value, isFullDetail(fullDetail));
/*      */     } 
/*      */     
/*  453 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendInternal(StringBuffer buffer, String fieldName, Object value, boolean detail) {
/*  476 */     if (isRegistered(value) && !(value instanceof Number) && !(value instanceof Boolean) && !(value instanceof Character)) {
/*      */       
/*  478 */       appendCyclicObject(buffer, fieldName, value);
/*      */       
/*      */       return;
/*      */     } 
/*  482 */     register(value);
/*      */     
/*      */     try {
/*  485 */       if (value instanceof Collection) {
/*  486 */         if (detail) {
/*  487 */           appendDetail(buffer, fieldName, (Collection)value);
/*      */         } else {
/*  489 */           appendSummarySize(buffer, fieldName, ((Collection)value).size());
/*      */         }
/*      */       
/*  492 */       } else if (value instanceof Map) {
/*  493 */         if (detail) {
/*  494 */           appendDetail(buffer, fieldName, (Map<?, ?>)value);
/*      */         } else {
/*  496 */           appendSummarySize(buffer, fieldName, ((Map)value).size());
/*      */         }
/*      */       
/*  499 */       } else if (value instanceof long[]) {
/*  500 */         if (detail) {
/*  501 */           appendDetail(buffer, fieldName, (long[])value);
/*      */         } else {
/*  503 */           appendSummary(buffer, fieldName, (long[])value);
/*      */         }
/*      */       
/*  506 */       } else if (value instanceof int[]) {
/*  507 */         if (detail) {
/*  508 */           appendDetail(buffer, fieldName, (int[])value);
/*      */         } else {
/*  510 */           appendSummary(buffer, fieldName, (int[])value);
/*      */         }
/*      */       
/*  513 */       } else if (value instanceof short[]) {
/*  514 */         if (detail) {
/*  515 */           appendDetail(buffer, fieldName, (short[])value);
/*      */         } else {
/*  517 */           appendSummary(buffer, fieldName, (short[])value);
/*      */         }
/*      */       
/*  520 */       } else if (value instanceof byte[]) {
/*  521 */         if (detail) {
/*  522 */           appendDetail(buffer, fieldName, (byte[])value);
/*      */         } else {
/*  524 */           appendSummary(buffer, fieldName, (byte[])value);
/*      */         }
/*      */       
/*  527 */       } else if (value instanceof char[]) {
/*  528 */         if (detail) {
/*  529 */           appendDetail(buffer, fieldName, (char[])value);
/*      */         } else {
/*  531 */           appendSummary(buffer, fieldName, (char[])value);
/*      */         }
/*      */       
/*  534 */       } else if (value instanceof double[]) {
/*  535 */         if (detail) {
/*  536 */           appendDetail(buffer, fieldName, (double[])value);
/*      */         } else {
/*  538 */           appendSummary(buffer, fieldName, (double[])value);
/*      */         }
/*      */       
/*  541 */       } else if (value instanceof float[]) {
/*  542 */         if (detail) {
/*  543 */           appendDetail(buffer, fieldName, (float[])value);
/*      */         } else {
/*  545 */           appendSummary(buffer, fieldName, (float[])value);
/*      */         }
/*      */       
/*  548 */       } else if (value instanceof boolean[]) {
/*  549 */         if (detail) {
/*  550 */           appendDetail(buffer, fieldName, (boolean[])value);
/*      */         } else {
/*  552 */           appendSummary(buffer, fieldName, (boolean[])value);
/*      */         }
/*      */       
/*  555 */       } else if (ObjectUtils.isArray(value)) {
/*  556 */         if (detail) {
/*  557 */           appendDetail(buffer, fieldName, (Object[])value);
/*      */         } else {
/*  559 */           appendSummary(buffer, fieldName, (Object[])value);
/*      */         }
/*      */       
/*  562 */       } else if (detail) {
/*  563 */         appendDetail(buffer, fieldName, value);
/*      */       } else {
/*  565 */         appendSummary(buffer, fieldName, value);
/*      */       } 
/*      */     } finally {
/*  568 */       unregister(value);
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
/*      */   protected void appendCyclicObject(StringBuffer buffer, String fieldName, Object value) {
/*  585 */     ObjectUtils.identityToString(buffer, value);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, Object value) {
/*  598 */     buffer.append(value);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, Collection<?> coll) {
/*  610 */     buffer.append(coll);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, Map<?, ?> map) {
/*  622 */     buffer.append(map);
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
/*      */   protected void appendSummary(StringBuffer buffer, String fieldName, Object value) {
/*  635 */     buffer.append(this.summaryObjectStartText);
/*  636 */     buffer.append(getShortClassName(value.getClass()));
/*  637 */     buffer.append(this.summaryObjectEndText);
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
/*      */   public void append(StringBuffer buffer, String fieldName, long value) {
/*  649 */     appendFieldStart(buffer, fieldName);
/*  650 */     appendDetail(buffer, fieldName, value);
/*  651 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, long value) {
/*  663 */     buffer.append(value);
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
/*      */   public void append(StringBuffer buffer, String fieldName, int value) {
/*  675 */     appendFieldStart(buffer, fieldName);
/*  676 */     appendDetail(buffer, fieldName, value);
/*  677 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, int value) {
/*  689 */     buffer.append(value);
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
/*      */   public void append(StringBuffer buffer, String fieldName, short value) {
/*  701 */     appendFieldStart(buffer, fieldName);
/*  702 */     appendDetail(buffer, fieldName, value);
/*  703 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, short value) {
/*  715 */     buffer.append(value);
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
/*      */   public void append(StringBuffer buffer, String fieldName, byte value) {
/*  727 */     appendFieldStart(buffer, fieldName);
/*  728 */     appendDetail(buffer, fieldName, value);
/*  729 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, byte value) {
/*  741 */     buffer.append(value);
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
/*      */   public void append(StringBuffer buffer, String fieldName, char value) {
/*  753 */     appendFieldStart(buffer, fieldName);
/*  754 */     appendDetail(buffer, fieldName, value);
/*  755 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, char value) {
/*  767 */     buffer.append(value);
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
/*      */   public void append(StringBuffer buffer, String fieldName, double value) {
/*  779 */     appendFieldStart(buffer, fieldName);
/*  780 */     appendDetail(buffer, fieldName, value);
/*  781 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, double value) {
/*  793 */     buffer.append(value);
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
/*      */   public void append(StringBuffer buffer, String fieldName, float value) {
/*  805 */     appendFieldStart(buffer, fieldName);
/*  806 */     appendDetail(buffer, fieldName, value);
/*  807 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, float value) {
/*  819 */     buffer.append(value);
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
/*      */   public void append(StringBuffer buffer, String fieldName, boolean value) {
/*  831 */     appendFieldStart(buffer, fieldName);
/*  832 */     appendDetail(buffer, fieldName, value);
/*  833 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, boolean value) {
/*  845 */     buffer.append(value);
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
/*      */   public void append(StringBuffer buffer, String fieldName, Object[] array, Boolean fullDetail) {
/*  859 */     appendFieldStart(buffer, fieldName);
/*      */     
/*  861 */     if (array == null) {
/*  862 */       appendNullText(buffer, fieldName);
/*      */     }
/*  864 */     else if (isFullDetail(fullDetail)) {
/*  865 */       appendDetail(buffer, fieldName, array);
/*      */     } else {
/*      */       
/*  868 */       appendSummary(buffer, fieldName, array);
/*      */     } 
/*      */     
/*  871 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, Object[] array) {
/*  884 */     buffer.append(this.arrayStart);
/*  885 */     for (int i = 0; i < array.length; i++) {
/*  886 */       appendDetail(buffer, fieldName, i, array[i]);
/*      */     }
/*  888 */     buffer.append(this.arrayEnd);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, int i, Object item) {
/*  902 */     if (i > 0) {
/*  903 */       buffer.append(this.arraySeparator);
/*      */     }
/*  905 */     if (item == null) {
/*  906 */       appendNullText(buffer, fieldName);
/*      */     } else {
/*  908 */       appendInternal(buffer, fieldName, item, this.arrayContentDetail);
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
/*      */   protected void reflectionAppendArrayDetail(StringBuffer buffer, String fieldName, Object array) {
/*  922 */     buffer.append(this.arrayStart);
/*  923 */     int length = Array.getLength(array);
/*  924 */     for (int i = 0; i < length; i++) {
/*  925 */       appendDetail(buffer, fieldName, i, Array.get(array, i));
/*      */     }
/*  927 */     buffer.append(this.arrayEnd);
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
/*      */   protected void appendSummary(StringBuffer buffer, String fieldName, Object[] array) {
/*  940 */     appendSummarySize(buffer, fieldName, array.length);
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
/*      */   public void append(StringBuffer buffer, String fieldName, long[] array, Boolean fullDetail) {
/*  954 */     appendFieldStart(buffer, fieldName);
/*      */     
/*  956 */     if (array == null) {
/*  957 */       appendNullText(buffer, fieldName);
/*      */     }
/*  959 */     else if (isFullDetail(fullDetail)) {
/*  960 */       appendDetail(buffer, fieldName, array);
/*      */     } else {
/*      */       
/*  963 */       appendSummary(buffer, fieldName, array);
/*      */     } 
/*      */     
/*  966 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, long[] array) {
/*  979 */     buffer.append(this.arrayStart);
/*  980 */     for (int i = 0; i < array.length; i++) {
/*  981 */       if (i > 0) {
/*  982 */         buffer.append(this.arraySeparator);
/*      */       }
/*  984 */       appendDetail(buffer, fieldName, array[i]);
/*      */     } 
/*  986 */     buffer.append(this.arrayEnd);
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
/*      */   protected void appendSummary(StringBuffer buffer, String fieldName, long[] array) {
/*  999 */     appendSummarySize(buffer, fieldName, array.length);
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
/*      */   public void append(StringBuffer buffer, String fieldName, int[] array, Boolean fullDetail) {
/* 1013 */     appendFieldStart(buffer, fieldName);
/*      */     
/* 1015 */     if (array == null) {
/* 1016 */       appendNullText(buffer, fieldName);
/*      */     }
/* 1018 */     else if (isFullDetail(fullDetail)) {
/* 1019 */       appendDetail(buffer, fieldName, array);
/*      */     } else {
/*      */       
/* 1022 */       appendSummary(buffer, fieldName, array);
/*      */     } 
/*      */     
/* 1025 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, int[] array) {
/* 1038 */     buffer.append(this.arrayStart);
/* 1039 */     for (int i = 0; i < array.length; i++) {
/* 1040 */       if (i > 0) {
/* 1041 */         buffer.append(this.arraySeparator);
/*      */       }
/* 1043 */       appendDetail(buffer, fieldName, array[i]);
/*      */     } 
/* 1045 */     buffer.append(this.arrayEnd);
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
/*      */   protected void appendSummary(StringBuffer buffer, String fieldName, int[] array) {
/* 1058 */     appendSummarySize(buffer, fieldName, array.length);
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
/*      */   public void append(StringBuffer buffer, String fieldName, short[] array, Boolean fullDetail) {
/* 1072 */     appendFieldStart(buffer, fieldName);
/*      */     
/* 1074 */     if (array == null) {
/* 1075 */       appendNullText(buffer, fieldName);
/*      */     }
/* 1077 */     else if (isFullDetail(fullDetail)) {
/* 1078 */       appendDetail(buffer, fieldName, array);
/*      */     } else {
/*      */       
/* 1081 */       appendSummary(buffer, fieldName, array);
/*      */     } 
/*      */     
/* 1084 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, short[] array) {
/* 1097 */     buffer.append(this.arrayStart);
/* 1098 */     for (int i = 0; i < array.length; i++) {
/* 1099 */       if (i > 0) {
/* 1100 */         buffer.append(this.arraySeparator);
/*      */       }
/* 1102 */       appendDetail(buffer, fieldName, array[i]);
/*      */     } 
/* 1104 */     buffer.append(this.arrayEnd);
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
/*      */   protected void appendSummary(StringBuffer buffer, String fieldName, short[] array) {
/* 1117 */     appendSummarySize(buffer, fieldName, array.length);
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
/*      */   public void append(StringBuffer buffer, String fieldName, byte[] array, Boolean fullDetail) {
/* 1131 */     appendFieldStart(buffer, fieldName);
/*      */     
/* 1133 */     if (array == null) {
/* 1134 */       appendNullText(buffer, fieldName);
/*      */     }
/* 1136 */     else if (isFullDetail(fullDetail)) {
/* 1137 */       appendDetail(buffer, fieldName, array);
/*      */     } else {
/*      */       
/* 1140 */       appendSummary(buffer, fieldName, array);
/*      */     } 
/*      */     
/* 1143 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, byte[] array) {
/* 1156 */     buffer.append(this.arrayStart);
/* 1157 */     for (int i = 0; i < array.length; i++) {
/* 1158 */       if (i > 0) {
/* 1159 */         buffer.append(this.arraySeparator);
/*      */       }
/* 1161 */       appendDetail(buffer, fieldName, array[i]);
/*      */     } 
/* 1163 */     buffer.append(this.arrayEnd);
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
/*      */   protected void appendSummary(StringBuffer buffer, String fieldName, byte[] array) {
/* 1176 */     appendSummarySize(buffer, fieldName, array.length);
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
/*      */   public void append(StringBuffer buffer, String fieldName, char[] array, Boolean fullDetail) {
/* 1190 */     appendFieldStart(buffer, fieldName);
/*      */     
/* 1192 */     if (array == null) {
/* 1193 */       appendNullText(buffer, fieldName);
/*      */     }
/* 1195 */     else if (isFullDetail(fullDetail)) {
/* 1196 */       appendDetail(buffer, fieldName, array);
/*      */     } else {
/*      */       
/* 1199 */       appendSummary(buffer, fieldName, array);
/*      */     } 
/*      */     
/* 1202 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, char[] array) {
/* 1215 */     buffer.append(this.arrayStart);
/* 1216 */     for (int i = 0; i < array.length; i++) {
/* 1217 */       if (i > 0) {
/* 1218 */         buffer.append(this.arraySeparator);
/*      */       }
/* 1220 */       appendDetail(buffer, fieldName, array[i]);
/*      */     } 
/* 1222 */     buffer.append(this.arrayEnd);
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
/*      */   protected void appendSummary(StringBuffer buffer, String fieldName, char[] array) {
/* 1235 */     appendSummarySize(buffer, fieldName, array.length);
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
/*      */   public void append(StringBuffer buffer, String fieldName, double[] array, Boolean fullDetail) {
/* 1249 */     appendFieldStart(buffer, fieldName);
/*      */     
/* 1251 */     if (array == null) {
/* 1252 */       appendNullText(buffer, fieldName);
/*      */     }
/* 1254 */     else if (isFullDetail(fullDetail)) {
/* 1255 */       appendDetail(buffer, fieldName, array);
/*      */     } else {
/*      */       
/* 1258 */       appendSummary(buffer, fieldName, array);
/*      */     } 
/*      */     
/* 1261 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, double[] array) {
/* 1274 */     buffer.append(this.arrayStart);
/* 1275 */     for (int i = 0; i < array.length; i++) {
/* 1276 */       if (i > 0) {
/* 1277 */         buffer.append(this.arraySeparator);
/*      */       }
/* 1279 */       appendDetail(buffer, fieldName, array[i]);
/*      */     } 
/* 1281 */     buffer.append(this.arrayEnd);
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
/*      */   protected void appendSummary(StringBuffer buffer, String fieldName, double[] array) {
/* 1294 */     appendSummarySize(buffer, fieldName, array.length);
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
/*      */   public void append(StringBuffer buffer, String fieldName, float[] array, Boolean fullDetail) {
/* 1308 */     appendFieldStart(buffer, fieldName);
/*      */     
/* 1310 */     if (array == null) {
/* 1311 */       appendNullText(buffer, fieldName);
/*      */     }
/* 1313 */     else if (isFullDetail(fullDetail)) {
/* 1314 */       appendDetail(buffer, fieldName, array);
/*      */     } else {
/*      */       
/* 1317 */       appendSummary(buffer, fieldName, array);
/*      */     } 
/*      */     
/* 1320 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, float[] array) {
/* 1333 */     buffer.append(this.arrayStart);
/* 1334 */     for (int i = 0; i < array.length; i++) {
/* 1335 */       if (i > 0) {
/* 1336 */         buffer.append(this.arraySeparator);
/*      */       }
/* 1338 */       appendDetail(buffer, fieldName, array[i]);
/*      */     } 
/* 1340 */     buffer.append(this.arrayEnd);
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
/*      */   protected void appendSummary(StringBuffer buffer, String fieldName, float[] array) {
/* 1353 */     appendSummarySize(buffer, fieldName, array.length);
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
/*      */   public void append(StringBuffer buffer, String fieldName, boolean[] array, Boolean fullDetail) {
/* 1367 */     appendFieldStart(buffer, fieldName);
/*      */     
/* 1369 */     if (array == null) {
/* 1370 */       appendNullText(buffer, fieldName);
/*      */     }
/* 1372 */     else if (isFullDetail(fullDetail)) {
/* 1373 */       appendDetail(buffer, fieldName, array);
/*      */     } else {
/*      */       
/* 1376 */       appendSummary(buffer, fieldName, array);
/*      */     } 
/*      */     
/* 1379 */     appendFieldEnd(buffer, fieldName);
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
/*      */   protected void appendDetail(StringBuffer buffer, String fieldName, boolean[] array) {
/* 1392 */     buffer.append(this.arrayStart);
/* 1393 */     for (int i = 0; i < array.length; i++) {
/* 1394 */       if (i > 0) {
/* 1395 */         buffer.append(this.arraySeparator);
/*      */       }
/* 1397 */       appendDetail(buffer, fieldName, array[i]);
/*      */     } 
/* 1399 */     buffer.append(this.arrayEnd);
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
/*      */   protected void appendSummary(StringBuffer buffer, String fieldName, boolean[] array) {
/* 1412 */     appendSummarySize(buffer, fieldName, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void appendClassName(StringBuffer buffer, Object object) {
/* 1422 */     if (this.useClassName && object != null) {
/* 1423 */       register(object);
/* 1424 */       if (this.useShortClassName) {
/* 1425 */         buffer.append(getShortClassName(object.getClass()));
/*      */       } else {
/* 1427 */         buffer.append(object.getClass().getName());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void appendIdentityHashCode(StringBuffer buffer, Object object) {
/* 1439 */     if (isUseIdentityHashCode() && object != null) {
/* 1440 */       register(object);
/* 1441 */       buffer.append('@');
/* 1442 */       buffer.append(ObjectUtils.identityHashCodeHex(object));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void appendContentStart(StringBuffer buffer) {
/* 1452 */     buffer.append(this.contentStart);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void appendContentEnd(StringBuffer buffer) {
/* 1461 */     buffer.append(this.contentEnd);
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
/*      */   protected void appendNullText(StringBuffer buffer, String fieldName) {
/* 1473 */     buffer.append(this.nullText);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void appendFieldSeparator(StringBuffer buffer) {
/* 1482 */     buffer.append(this.fieldSeparator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void appendFieldStart(StringBuffer buffer, String fieldName) {
/* 1492 */     if (this.useFieldNames && fieldName != null) {
/* 1493 */       buffer.append(fieldName);
/* 1494 */       buffer.append(this.fieldNameValueSeparator);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void appendFieldEnd(StringBuffer buffer, String fieldName) {
/* 1505 */     appendFieldSeparator(buffer);
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
/*      */   protected void appendSummarySize(StringBuffer buffer, String fieldName, int size) {
/* 1524 */     buffer.append(this.sizeStartText);
/* 1525 */     buffer.append(size);
/* 1526 */     buffer.append(this.sizeEndText);
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
/*      */   protected boolean isFullDetail(Boolean fullDetailRequest) {
/* 1544 */     if (fullDetailRequest == null) {
/* 1545 */       return this.defaultFullDetail;
/*      */     }
/* 1547 */     return fullDetailRequest.booleanValue();
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
/*      */   protected String getShortClassName(Class<?> cls) {
/* 1560 */     return ClassUtils.getShortClassName(cls);
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
/*      */   protected boolean isUseClassName() {
/* 1572 */     return this.useClassName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setUseClassName(boolean useClassName) {
/* 1581 */     this.useClassName = useClassName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isUseShortClassName() {
/* 1591 */     return this.useShortClassName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setUseShortClassName(boolean useShortClassName) {
/* 1601 */     this.useShortClassName = useShortClassName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isUseIdentityHashCode() {
/* 1610 */     return this.useIdentityHashCode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setUseIdentityHashCode(boolean useIdentityHashCode) {
/* 1619 */     this.useIdentityHashCode = useIdentityHashCode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isUseFieldNames() {
/* 1628 */     return this.useFieldNames;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setUseFieldNames(boolean useFieldNames) {
/* 1637 */     this.useFieldNames = useFieldNames;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isDefaultFullDetail() {
/* 1647 */     return this.defaultFullDetail;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setDefaultFullDetail(boolean defaultFullDetail) {
/* 1657 */     this.defaultFullDetail = defaultFullDetail;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isArrayContentDetail() {
/* 1666 */     return this.arrayContentDetail;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setArrayContentDetail(boolean arrayContentDetail) {
/* 1675 */     this.arrayContentDetail = arrayContentDetail;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getArrayStart() {
/* 1684 */     return this.arrayStart;
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
/*      */   protected void setArrayStart(String arrayStart) {
/* 1696 */     if (arrayStart == null) {
/* 1697 */       arrayStart = "";
/*      */     }
/* 1699 */     this.arrayStart = arrayStart;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getArrayEnd() {
/* 1708 */     return this.arrayEnd;
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
/*      */   protected void setArrayEnd(String arrayEnd) {
/* 1720 */     if (arrayEnd == null) {
/* 1721 */       arrayEnd = "";
/*      */     }
/* 1723 */     this.arrayEnd = arrayEnd;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getArraySeparator() {
/* 1732 */     return this.arraySeparator;
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
/*      */   protected void setArraySeparator(String arraySeparator) {
/* 1744 */     if (arraySeparator == null) {
/* 1745 */       arraySeparator = "";
/*      */     }
/* 1747 */     this.arraySeparator = arraySeparator;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getContentStart() {
/* 1756 */     return this.contentStart;
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
/*      */   protected void setContentStart(String contentStart) {
/* 1768 */     if (contentStart == null) {
/* 1769 */       contentStart = "";
/*      */     }
/* 1771 */     this.contentStart = contentStart;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getContentEnd() {
/* 1780 */     return this.contentEnd;
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
/*      */   protected void setContentEnd(String contentEnd) {
/* 1792 */     if (contentEnd == null) {
/* 1793 */       contentEnd = "";
/*      */     }
/* 1795 */     this.contentEnd = contentEnd;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getFieldNameValueSeparator() {
/* 1804 */     return this.fieldNameValueSeparator;
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
/*      */   protected void setFieldNameValueSeparator(String fieldNameValueSeparator) {
/* 1816 */     if (fieldNameValueSeparator == null) {
/* 1817 */       fieldNameValueSeparator = "";
/*      */     }
/* 1819 */     this.fieldNameValueSeparator = fieldNameValueSeparator;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getFieldSeparator() {
/* 1828 */     return this.fieldSeparator;
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
/*      */   protected void setFieldSeparator(String fieldSeparator) {
/* 1840 */     if (fieldSeparator == null) {
/* 1841 */       fieldSeparator = "";
/*      */     }
/* 1843 */     this.fieldSeparator = fieldSeparator;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isFieldSeparatorAtStart() {
/* 1854 */     return this.fieldSeparatorAtStart;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setFieldSeparatorAtStart(boolean fieldSeparatorAtStart) {
/* 1865 */     this.fieldSeparatorAtStart = fieldSeparatorAtStart;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isFieldSeparatorAtEnd() {
/* 1876 */     return this.fieldSeparatorAtEnd;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setFieldSeparatorAtEnd(boolean fieldSeparatorAtEnd) {
/* 1887 */     this.fieldSeparatorAtEnd = fieldSeparatorAtEnd;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getNullText() {
/* 1896 */     return this.nullText;
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
/*      */   protected void setNullText(String nullText) {
/* 1908 */     if (nullText == null) {
/* 1909 */       nullText = "";
/*      */     }
/* 1911 */     this.nullText = nullText;
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
/*      */   protected String getSizeStartText() {
/* 1923 */     return this.sizeStartText;
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
/*      */   protected void setSizeStartText(String sizeStartText) {
/* 1938 */     if (sizeStartText == null) {
/* 1939 */       sizeStartText = "";
/*      */     }
/* 1941 */     this.sizeStartText = sizeStartText;
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
/*      */   protected String getSizeEndText() {
/* 1953 */     return this.sizeEndText;
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
/*      */   protected void setSizeEndText(String sizeEndText) {
/* 1968 */     if (sizeEndText == null) {
/* 1969 */       sizeEndText = "";
/*      */     }
/* 1971 */     this.sizeEndText = sizeEndText;
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
/*      */   protected String getSummaryObjectStartText() {
/* 1983 */     return this.summaryObjectStartText;
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
/*      */   protected void setSummaryObjectStartText(String summaryObjectStartText) {
/* 1998 */     if (summaryObjectStartText == null) {
/* 1999 */       summaryObjectStartText = "";
/*      */     }
/* 2001 */     this.summaryObjectStartText = summaryObjectStartText;
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
/*      */   protected String getSummaryObjectEndText() {
/* 2013 */     return this.summaryObjectEndText;
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
/*      */   protected void setSummaryObjectEndText(String summaryObjectEndText) {
/* 2028 */     if (summaryObjectEndText == null) {
/* 2029 */       summaryObjectEndText = "";
/*      */     }
/* 2031 */     this.summaryObjectEndText = summaryObjectEndText;
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
/*      */   private static final class DefaultToStringStyle
/*      */     extends ToStringStyle
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object readResolve() {
/* 2063 */       return DEFAULT_STYLE;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class NoFieldNameToStringStyle
/*      */     extends ToStringStyle
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     NoFieldNameToStringStyle() {
/* 2085 */       setUseFieldNames(false);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object readResolve() {
/* 2094 */       return NO_FIELD_NAMES_STYLE;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class ShortPrefixToStringStyle
/*      */     extends ToStringStyle
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ShortPrefixToStringStyle() {
/* 2116 */       setUseShortClassName(true);
/* 2117 */       setUseIdentityHashCode(false);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object readResolve() {
/* 2125 */       return SHORT_PREFIX_STYLE;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class SimpleToStringStyle
/*      */     extends ToStringStyle
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     SimpleToStringStyle() {
/* 2147 */       setUseClassName(false);
/* 2148 */       setUseIdentityHashCode(false);
/* 2149 */       setUseFieldNames(false);
/* 2150 */       setContentStart("");
/* 2151 */       setContentEnd("");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object readResolve() {
/* 2159 */       return SIMPLE_STYLE;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class MultiLineToStringStyle
/*      */     extends ToStringStyle
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     MultiLineToStringStyle() {
/* 2180 */       setContentStart("[");
/* 2181 */       setFieldSeparator(System.lineSeparator() + "  ");
/* 2182 */       setFieldSeparatorAtStart(true);
/* 2183 */       setContentEnd(System.lineSeparator() + "]");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object readResolve() {
/* 2192 */       return MULTI_LINE_STYLE;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class NoClassNameToStringStyle
/*      */     extends ToStringStyle
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     NoClassNameToStringStyle() {
/* 2214 */       setUseClassName(false);
/* 2215 */       setUseIdentityHashCode(false);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object readResolve() {
/* 2224 */       return NO_CLASS_NAME_STYLE;
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
/*      */   private static final class JsonToStringStyle
/*      */     extends ToStringStyle
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private static final String FIELD_NAME_QUOTE = "\"";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     JsonToStringStyle() {
/* 2254 */       setUseClassName(false);
/* 2255 */       setUseIdentityHashCode(false);
/*      */       
/* 2257 */       setContentStart("{");
/* 2258 */       setContentEnd("}");
/*      */       
/* 2260 */       setArrayStart("[");
/* 2261 */       setArrayEnd("]");
/*      */       
/* 2263 */       setFieldSeparator(",");
/* 2264 */       setFieldNameValueSeparator(":");
/*      */       
/* 2266 */       setNullText("null");
/*      */       
/* 2268 */       setSummaryObjectStartText("\"<");
/* 2269 */       setSummaryObjectEndText(">\"");
/*      */       
/* 2271 */       setSizeStartText("\"<size=");
/* 2272 */       setSizeEndText(">\"");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void append(StringBuffer buffer, String fieldName, Object[] array, Boolean fullDetail) {
/* 2279 */       if (fieldName == null) {
/* 2280 */         throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
/*      */       }
/*      */       
/* 2283 */       if (!isFullDetail(fullDetail)) {
/* 2284 */         throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
/*      */       }
/*      */ 
/*      */       
/* 2288 */       super.append(buffer, fieldName, array, fullDetail);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void append(StringBuffer buffer, String fieldName, long[] array, Boolean fullDetail) {
/* 2295 */       if (fieldName == null) {
/* 2296 */         throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
/*      */       }
/*      */       
/* 2299 */       if (!isFullDetail(fullDetail)) {
/* 2300 */         throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
/*      */       }
/*      */ 
/*      */       
/* 2304 */       super.append(buffer, fieldName, array, fullDetail);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void append(StringBuffer buffer, String fieldName, int[] array, Boolean fullDetail) {
/* 2311 */       if (fieldName == null) {
/* 2312 */         throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
/*      */       }
/*      */       
/* 2315 */       if (!isFullDetail(fullDetail)) {
/* 2316 */         throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
/*      */       }
/*      */ 
/*      */       
/* 2320 */       super.append(buffer, fieldName, array, fullDetail);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void append(StringBuffer buffer, String fieldName, short[] array, Boolean fullDetail) {
/* 2327 */       if (fieldName == null) {
/* 2328 */         throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
/*      */       }
/*      */       
/* 2331 */       if (!isFullDetail(fullDetail)) {
/* 2332 */         throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
/*      */       }
/*      */ 
/*      */       
/* 2336 */       super.append(buffer, fieldName, array, fullDetail);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void append(StringBuffer buffer, String fieldName, byte[] array, Boolean fullDetail) {
/* 2343 */       if (fieldName == null) {
/* 2344 */         throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
/*      */       }
/*      */       
/* 2347 */       if (!isFullDetail(fullDetail)) {
/* 2348 */         throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
/*      */       }
/*      */ 
/*      */       
/* 2352 */       super.append(buffer, fieldName, array, fullDetail);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void append(StringBuffer buffer, String fieldName, char[] array, Boolean fullDetail) {
/* 2359 */       if (fieldName == null) {
/* 2360 */         throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
/*      */       }
/*      */       
/* 2363 */       if (!isFullDetail(fullDetail)) {
/* 2364 */         throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
/*      */       }
/*      */ 
/*      */       
/* 2368 */       super.append(buffer, fieldName, array, fullDetail);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void append(StringBuffer buffer, String fieldName, double[] array, Boolean fullDetail) {
/* 2375 */       if (fieldName == null) {
/* 2376 */         throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
/*      */       }
/*      */       
/* 2379 */       if (!isFullDetail(fullDetail)) {
/* 2380 */         throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
/*      */       }
/*      */ 
/*      */       
/* 2384 */       super.append(buffer, fieldName, array, fullDetail);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void append(StringBuffer buffer, String fieldName, float[] array, Boolean fullDetail) {
/* 2391 */       if (fieldName == null) {
/* 2392 */         throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
/*      */       }
/*      */       
/* 2395 */       if (!isFullDetail(fullDetail)) {
/* 2396 */         throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
/*      */       }
/*      */ 
/*      */       
/* 2400 */       super.append(buffer, fieldName, array, fullDetail);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void append(StringBuffer buffer, String fieldName, boolean[] array, Boolean fullDetail) {
/* 2407 */       if (fieldName == null) {
/* 2408 */         throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
/*      */       }
/*      */       
/* 2411 */       if (!isFullDetail(fullDetail)) {
/* 2412 */         throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
/*      */       }
/*      */ 
/*      */       
/* 2416 */       super.append(buffer, fieldName, array, fullDetail);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void append(StringBuffer buffer, String fieldName, Object value, Boolean fullDetail) {
/* 2423 */       if (fieldName == null) {
/* 2424 */         throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
/*      */       }
/*      */       
/* 2427 */       if (!isFullDetail(fullDetail)) {
/* 2428 */         throw new UnsupportedOperationException("FullDetail must be true when using JsonToStringStyle");
/*      */       }
/*      */ 
/*      */       
/* 2432 */       super.append(buffer, fieldName, value, fullDetail);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void appendDetail(StringBuffer buffer, String fieldName, char value) {
/* 2437 */       appendValueAsString(buffer, String.valueOf(value));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     protected void appendDetail(StringBuffer buffer, String fieldName, Object value) {
/* 2443 */       if (value == null) {
/* 2444 */         appendNullText(buffer, fieldName);
/*      */         
/*      */         return;
/*      */       } 
/* 2448 */       if (value instanceof String || value instanceof Character) {
/* 2449 */         appendValueAsString(buffer, value.toString());
/*      */         
/*      */         return;
/*      */       } 
/* 2453 */       if (value instanceof Number || value instanceof Boolean) {
/* 2454 */         buffer.append(value);
/*      */         
/*      */         return;
/*      */       } 
/* 2458 */       String valueAsString = value.toString();
/* 2459 */       if (isJsonObject(valueAsString) || isJsonArray(valueAsString)) {
/* 2460 */         buffer.append(value);
/*      */         
/*      */         return;
/*      */       } 
/* 2464 */       appendDetail(buffer, fieldName, valueAsString);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void appendDetail(StringBuffer buffer, String fieldName, Collection<?> coll) {
/* 2469 */       if (coll != null && !coll.isEmpty()) {
/* 2470 */         buffer.append(getArrayStart());
/* 2471 */         int i = 0;
/* 2472 */         for (Object item : coll) {
/* 2473 */           appendDetail(buffer, fieldName, i++, item);
/*      */         }
/* 2475 */         buffer.append(getArrayEnd());
/*      */         
/*      */         return;
/*      */       } 
/* 2479 */       buffer.append(coll);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void appendDetail(StringBuffer buffer, String fieldName, Map<?, ?> map) {
/* 2484 */       if (map != null && !map.isEmpty()) {
/* 2485 */         buffer.append(getContentStart());
/*      */         
/* 2487 */         boolean firstItem = true;
/* 2488 */         for (Map.Entry<?, ?> entry : map.entrySet()) {
/* 2489 */           String keyStr = Objects.toString(entry.getKey(), null);
/* 2490 */           if (keyStr != null) {
/* 2491 */             if (firstItem) {
/* 2492 */               firstItem = false;
/*      */             } else {
/* 2494 */               appendFieldEnd(buffer, keyStr);
/*      */             } 
/* 2496 */             appendFieldStart(buffer, keyStr);
/* 2497 */             Object value = entry.getValue();
/* 2498 */             if (value == null) {
/* 2499 */               appendNullText(buffer, keyStr); continue;
/*      */             } 
/* 2501 */             appendInternal(buffer, keyStr, value, true);
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 2506 */         buffer.append(getContentEnd());
/*      */         
/*      */         return;
/*      */       } 
/* 2510 */       buffer.append(map);
/*      */     }
/*      */     
/*      */     private boolean isJsonArray(String valueAsString) {
/* 2514 */       return (valueAsString.startsWith(getArrayStart()) && valueAsString
/* 2515 */         .endsWith(getArrayEnd()));
/*      */     }
/*      */     
/*      */     private boolean isJsonObject(String valueAsString) {
/* 2519 */       return (valueAsString.startsWith(getContentStart()) && valueAsString
/* 2520 */         .endsWith(getContentEnd()));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void appendValueAsString(StringBuffer buffer, String value) {
/* 2530 */       buffer.append('"').append(StringEscapeUtils.escapeJson(value)).append('"');
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     protected void appendFieldStart(StringBuffer buffer, String fieldName) {
/* 2536 */       if (fieldName == null) {
/* 2537 */         throw new UnsupportedOperationException("Field names are mandatory when using JsonToStringStyle");
/*      */       }
/*      */ 
/*      */       
/* 2541 */       super.appendFieldStart(buffer, "\"" + StringEscapeUtils.escapeJson(fieldName) + "\"");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object readResolve() {
/* 2551 */       return JSON_STYLE;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\builder\ToStringStyle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */