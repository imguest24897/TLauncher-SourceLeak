/*     */ package org.apache.commons.lang3.builder;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Arrays;
/*     */ import org.apache.commons.lang3.ArraySorter;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.reflect.FieldUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReflectionDiffBuilder<T>
/*     */   implements Builder<DiffResult<T>>
/*     */ {
/*     */   private final T left;
/*     */   private final T right;
/*     */   private final DiffBuilder<T> diffBuilder;
/*     */   private String[] excludeFieldNames;
/*     */   
/*     */   public ReflectionDiffBuilder(T lhs, T rhs, ToStringStyle style) {
/* 106 */     this.left = lhs;
/* 107 */     this.right = rhs;
/* 108 */     this.diffBuilder = new DiffBuilder<>(lhs, rhs, style);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getExcludeFieldNames() {
/* 118 */     return (String[])this.excludeFieldNames.clone();
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
/*     */   public ReflectionDiffBuilder<T> setExcludeFieldNames(String... excludeFieldNamesParam) {
/* 131 */     if (excludeFieldNamesParam == null) {
/* 132 */       this.excludeFieldNames = ArrayUtils.EMPTY_STRING_ARRAY;
/*     */     } else {
/*     */       
/* 135 */       this.excludeFieldNames = (String[])ArraySorter.sort((Object[])ReflectionToStringBuilder.toNoNullStringArray((Object[])excludeFieldNamesParam));
/*     */     } 
/* 137 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DiffResult<T> build() {
/* 142 */     if (this.left.equals(this.right)) {
/* 143 */       return this.diffBuilder.build();
/*     */     }
/*     */     
/* 146 */     appendFields(this.left.getClass());
/* 147 */     return this.diffBuilder.build();
/*     */   }
/*     */   
/*     */   private void appendFields(Class<?> clazz) {
/* 151 */     for (Field field : FieldUtils.getAllFields(clazz)) {
/* 152 */       if (accept(field)) {
/*     */         try {
/* 154 */           this.diffBuilder.append(field.getName(), FieldUtils.readField(field, this.left, true), FieldUtils.readField(field, this.right, true));
/* 155 */         } catch (IllegalAccessException e) {
/*     */ 
/*     */           
/* 158 */           throw new IllegalArgumentException("Unexpected IllegalAccessException: " + e.getMessage(), e);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean accept(Field field) {
/* 165 */     if (field.getName().indexOf('$') != -1) {
/* 166 */       return false;
/*     */     }
/* 168 */     if (Modifier.isTransient(field.getModifiers())) {
/* 169 */       return false;
/*     */     }
/* 171 */     if (Modifier.isStatic(field.getModifiers())) {
/* 172 */       return false;
/*     */     }
/* 174 */     if (this.excludeFieldNames != null && 
/* 175 */       Arrays.binarySearch((Object[])this.excludeFieldNames, field.getName()) >= 0)
/*     */     {
/* 177 */       return false;
/*     */     }
/* 179 */     return !field.isAnnotationPresent((Class)DiffExclude.class);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\builder\ReflectionDiffBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */