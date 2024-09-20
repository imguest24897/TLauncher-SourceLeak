/*     */ package org.apache.commons.lang3.builder;
/*     */ 
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Objects;
/*     */ import org.apache.commons.lang3.ArraySorter;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.stream.Streams;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReflectionToStringBuilder
/*     */   extends ToStringBuilder
/*     */ {
/*     */   private boolean appendStatics;
/*     */   private boolean appendTransients;
/*     */   private boolean excludeNullValues;
/*     */   protected String[] excludeFieldNames;
/*     */   protected String[] includeFieldNames;
/*     */   private Class<?> upToClass;
/*     */   
/*     */   static String[] toNoNullStringArray(Collection<String> collection) {
/* 113 */     if (collection == null) {
/* 114 */       return ArrayUtils.EMPTY_STRING_ARRAY;
/*     */     }
/* 116 */     return toNoNullStringArray(collection.toArray());
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
/*     */   static String[] toNoNullStringArray(Object[] array) {
/* 129 */     return (String[])Streams.nonNull(array).map(Objects::toString).toArray(x$0 -> new String[x$0]);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(Object object) {
/* 156 */     return toString(object, (ToStringStyle)null, false, false, (Class<? super Object>)null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(Object object, ToStringStyle style) {
/* 189 */     return toString(object, style, false, false, (Class<? super Object>)null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(Object object, ToStringStyle style, boolean outputTransients) {
/* 228 */     return toString(object, style, outputTransients, false, (Class<? super Object>)null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(Object object, ToStringStyle style, boolean outputTransients, boolean outputStatics) {
/* 275 */     return toString(object, style, outputTransients, outputStatics, (Class<? super Object>)null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> String toString(T object, ToStringStyle style, boolean outputTransients, boolean outputStatics, boolean excludeNullValues, Class<? super T> reflectUpToClass) {
/* 331 */     return (new ReflectionToStringBuilder(object, style, null, reflectUpToClass, outputTransients, outputStatics, excludeNullValues))
/* 332 */       .toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> String toString(T object, ToStringStyle style, boolean outputTransients, boolean outputStatics, Class<? super T> reflectUpToClass) {
/* 386 */     return (new ReflectionToStringBuilder(object, style, null, reflectUpToClass, outputTransients, outputStatics))
/* 387 */       .toString();
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
/*     */   public static String toStringExclude(Object object, Collection<String> excludeFieldNames) {
/* 400 */     return toStringExclude(object, toNoNullStringArray(excludeFieldNames));
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
/*     */   public static String toStringExclude(Object object, String... excludeFieldNames) {
/* 413 */     return (new ReflectionToStringBuilder(object)).setExcludeFieldNames(excludeFieldNames).toString();
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
/*     */   public static String toStringInclude(Object object, Collection<String> includeFieldNames) {
/* 428 */     return toStringInclude(object, toNoNullStringArray(includeFieldNames));
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
/*     */   public static String toStringInclude(Object object, String... includeFieldNames) {
/* 443 */     return (new ReflectionToStringBuilder(object)).setIncludeFieldNames(includeFieldNames).toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReflectionToStringBuilder(Object object) {
/* 493 */     super(Objects.requireNonNull(object, "obj"));
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
/*     */   public ReflectionToStringBuilder(Object object, ToStringStyle style) {
/* 511 */     super(Objects.requireNonNull(object, "obj"), style);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReflectionToStringBuilder(Object object, ToStringStyle style, StringBuffer buffer) {
/* 535 */     super(Objects.requireNonNull(object, "obj"), style, buffer);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ReflectionToStringBuilder(T object, ToStringStyle style, StringBuffer buffer, Class<? super T> reflectUpToClass, boolean outputTransients, boolean outputStatics) {
/* 560 */     super(Objects.requireNonNull(object, "obj"), style, buffer);
/* 561 */     setUpToClass(reflectUpToClass);
/* 562 */     setAppendTransients(outputTransients);
/* 563 */     setAppendStatics(outputStatics);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ReflectionToStringBuilder(T object, ToStringStyle style, StringBuffer buffer, Class<? super T> reflectUpToClass, boolean outputTransients, boolean outputStatics, boolean excludeNullValues) {
/* 591 */     super(Objects.requireNonNull(object, "obj"), style, buffer);
/* 592 */     setUpToClass(reflectUpToClass);
/* 593 */     setAppendTransients(outputTransients);
/* 594 */     setAppendStatics(outputStatics);
/* 595 */     setExcludeNullValues(excludeNullValues);
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
/*     */   protected boolean accept(Field field) {
/* 611 */     if (field.getName().indexOf('$') != -1)
/*     */     {
/* 613 */       return false;
/*     */     }
/* 615 */     if (Modifier.isTransient(field.getModifiers()) && !isAppendTransients())
/*     */     {
/* 617 */       return false;
/*     */     }
/* 619 */     if (Modifier.isStatic(field.getModifiers()) && !isAppendStatics())
/*     */     {
/* 621 */       return false;
/*     */     }
/*     */     
/* 624 */     if (this.excludeFieldNames != null && 
/* 625 */       Arrays.binarySearch((Object[])this.excludeFieldNames, field.getName()) >= 0)
/*     */     {
/* 627 */       return false;
/*     */     }
/*     */     
/* 630 */     if (ArrayUtils.isNotEmpty((Object[])this.includeFieldNames))
/*     */     {
/* 632 */       return (Arrays.binarySearch((Object[])this.includeFieldNames, field.getName()) >= 0);
/*     */     }
/*     */     
/* 635 */     return !field.isAnnotationPresent((Class)ToStringExclude.class);
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
/*     */   protected void appendFieldsIn(Class<?> clazz) {
/* 650 */     if (clazz.isArray()) {
/* 651 */       reflectionAppendArray(getObject());
/*     */       
/*     */       return;
/*     */     } 
/* 655 */     Field[] fields = (Field[])ArraySorter.sort((Object[])clazz.getDeclaredFields(), Comparator.comparing(Field::getName));
/* 656 */     AccessibleObject.setAccessible((AccessibleObject[])fields, true);
/* 657 */     for (Field field : fields) {
/* 658 */       String fieldName = field.getName();
/* 659 */       if (accept(field)) {
/*     */         
/* 661 */         Object fieldValue = Reflection.getUnchecked(field, getObject());
/* 662 */         if (!this.excludeNullValues || fieldValue != null) {
/* 663 */           append(fieldName, fieldValue, !field.isAnnotationPresent((Class)ToStringSummary.class));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getExcludeFieldNames() {
/* 675 */     return (String[])this.excludeFieldNames.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getIncludeFieldNames() {
/* 685 */     return (String[])this.includeFieldNames.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getUpToClass() {
/* 694 */     return this.upToClass;
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
/*     */   protected Object getValue(Field field) throws IllegalAccessException {
/* 712 */     return field.get(getObject());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAppendStatics() {
/* 722 */     return this.appendStatics;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAppendTransients() {
/* 731 */     return this.appendTransients;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExcludeNullValues() {
/* 741 */     return this.excludeNullValues;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReflectionToStringBuilder reflectionAppendArray(Object array) {
/* 752 */     getStyle().reflectionAppendArrayDetail(getStringBuffer(), null, array);
/* 753 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAppendStatics(boolean appendStatics) {
/* 764 */     this.appendStatics = appendStatics;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAppendTransients(boolean appendTransients) {
/* 774 */     this.appendTransients = appendTransients;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReflectionToStringBuilder setExcludeFieldNames(String... excludeFieldNamesParam) {
/* 785 */     if (excludeFieldNamesParam == null) {
/* 786 */       this.excludeFieldNames = null;
/*     */     } else {
/*     */       
/* 789 */       this.excludeFieldNames = (String[])ArraySorter.sort((Object[])toNoNullStringArray((Object[])excludeFieldNamesParam));
/*     */     } 
/* 791 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExcludeNullValues(boolean excludeNullValues) {
/* 802 */     this.excludeNullValues = excludeNullValues;
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
/*     */   public ReflectionToStringBuilder setIncludeFieldNames(String... includeFieldNamesParam) {
/* 814 */     if (includeFieldNamesParam == null) {
/* 815 */       this.includeFieldNames = null;
/*     */     } else {
/*     */       
/* 818 */       this.includeFieldNames = (String[])ArraySorter.sort((Object[])toNoNullStringArray((Object[])includeFieldNamesParam));
/*     */     } 
/* 820 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUpToClass(Class<?> clazz) {
/* 830 */     if (clazz != null) {
/* 831 */       Object object = getObject();
/* 832 */       if (object != null && !clazz.isInstance(object)) {
/* 833 */         throw new IllegalArgumentException("Specified class is not a superclass of the object");
/*     */       }
/*     */     } 
/* 836 */     this.upToClass = clazz;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 846 */     if (getObject() == null) {
/* 847 */       return getStyle().getNullText();
/*     */     }
/*     */     
/* 850 */     validate();
/*     */     
/* 852 */     Class<?> clazz = getObject().getClass();
/* 853 */     appendFieldsIn(clazz);
/* 854 */     while (clazz.getSuperclass() != null && clazz != getUpToClass()) {
/* 855 */       clazz = clazz.getSuperclass();
/* 856 */       appendFieldsIn(clazz);
/*     */     } 
/* 858 */     return super.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validate() {
/* 865 */     if (ArrayUtils.containsAny((Object[])this.excludeFieldNames, (Object[])this.includeFieldNames)) {
/* 866 */       ToStringStyle.unregister(getObject());
/* 867 */       throw new IllegalStateException("includeFieldNames and excludeFieldNames must not intersect");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\builder\ReflectionToStringBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */