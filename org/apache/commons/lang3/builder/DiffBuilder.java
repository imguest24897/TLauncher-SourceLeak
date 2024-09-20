/*     */ package org.apache.commons.lang3.builder;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DiffBuilder<T>
/*     */   implements Builder<DiffResult<T>>
/*     */ {
/*     */   private final List<Diff<?>> diffs;
/*     */   private final boolean objectsTriviallyEqual;
/*     */   private final T left;
/*     */   private final T right;
/*     */   private final ToStringStyle style;
/*     */   
/*     */   public DiffBuilder(T lhs, T rhs, ToStringStyle style, boolean testTriviallyEqual) {
/* 108 */     Objects.requireNonNull(lhs, "lhs");
/* 109 */     Objects.requireNonNull(rhs, "rhs");
/*     */     
/* 111 */     this.diffs = new ArrayList<>();
/* 112 */     this.left = lhs;
/* 113 */     this.right = rhs;
/* 114 */     this.style = style;
/*     */ 
/*     */     
/* 117 */     this.objectsTriviallyEqual = (testTriviallyEqual && Objects.equals(lhs, rhs));
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
/*     */   public DiffBuilder(T lhs, T rhs, ToStringStyle style) {
/* 147 */     this(lhs, rhs, style, true);
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
/*     */   public DiffBuilder<T> append(String fieldName, final boolean lhs, final boolean rhs) {
/* 165 */     validateFieldNameNotNull(fieldName);
/*     */     
/* 167 */     if (this.objectsTriviallyEqual) {
/* 168 */       return this;
/*     */     }
/* 170 */     if (lhs != rhs) {
/* 171 */       this.diffs.add(new Diff<Boolean>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Boolean getLeft() {
/* 176 */               return Boolean.valueOf(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Boolean getRight() {
/* 181 */               return Boolean.valueOf(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 185 */     return this;
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
/*     */   public DiffBuilder<T> append(String fieldName, final boolean[] lhs, final boolean[] rhs) {
/* 203 */     validateFieldNameNotNull(fieldName);
/* 204 */     if (this.objectsTriviallyEqual) {
/* 205 */       return this;
/*     */     }
/* 207 */     if (!Arrays.equals(lhs, rhs)) {
/* 208 */       this.diffs.add(new Diff<Boolean[]>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Boolean[] getLeft() {
/* 213 */               return ArrayUtils.toObject(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Boolean[] getRight() {
/* 218 */               return ArrayUtils.toObject(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 222 */     return this;
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
/*     */   public DiffBuilder<T> append(String fieldName, final byte lhs, final byte rhs) {
/* 240 */     validateFieldNameNotNull(fieldName);
/* 241 */     if (this.objectsTriviallyEqual) {
/* 242 */       return this;
/*     */     }
/* 244 */     if (lhs != rhs) {
/* 245 */       this.diffs.add(new Diff<Byte>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Byte getLeft() {
/* 250 */               return Byte.valueOf(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Byte getRight() {
/* 255 */               return Byte.valueOf(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 259 */     return this;
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
/*     */   public DiffBuilder<T> append(String fieldName, final byte[] lhs, final byte[] rhs) {
/* 277 */     validateFieldNameNotNull(fieldName);
/*     */     
/* 279 */     if (this.objectsTriviallyEqual) {
/* 280 */       return this;
/*     */     }
/* 282 */     if (!Arrays.equals(lhs, rhs)) {
/* 283 */       this.diffs.add(new Diff<Byte[]>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Byte[] getLeft() {
/* 288 */               return ArrayUtils.toObject(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Byte[] getRight() {
/* 293 */               return ArrayUtils.toObject(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 297 */     return this;
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
/*     */   public DiffBuilder<T> append(String fieldName, final char lhs, final char rhs) {
/* 315 */     validateFieldNameNotNull(fieldName);
/*     */     
/* 317 */     if (this.objectsTriviallyEqual) {
/* 318 */       return this;
/*     */     }
/* 320 */     if (lhs != rhs) {
/* 321 */       this.diffs.add(new Diff<Character>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Character getLeft() {
/* 326 */               return Character.valueOf(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Character getRight() {
/* 331 */               return Character.valueOf(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 335 */     return this;
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
/*     */   public DiffBuilder<T> append(String fieldName, final char[] lhs, final char[] rhs) {
/* 353 */     validateFieldNameNotNull(fieldName);
/*     */     
/* 355 */     if (this.objectsTriviallyEqual) {
/* 356 */       return this;
/*     */     }
/* 358 */     if (!Arrays.equals(lhs, rhs)) {
/* 359 */       this.diffs.add(new Diff<Character[]>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Character[] getLeft() {
/* 364 */               return ArrayUtils.toObject(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Character[] getRight() {
/* 369 */               return ArrayUtils.toObject(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 373 */     return this;
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
/*     */   public DiffBuilder<T> append(String fieldName, final double lhs, final double rhs) {
/* 391 */     validateFieldNameNotNull(fieldName);
/*     */     
/* 393 */     if (this.objectsTriviallyEqual) {
/* 394 */       return this;
/*     */     }
/* 396 */     if (Double.doubleToLongBits(lhs) != Double.doubleToLongBits(rhs)) {
/* 397 */       this.diffs.add(new Diff<Double>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Double getLeft() {
/* 402 */               return Double.valueOf(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Double getRight() {
/* 407 */               return Double.valueOf(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 411 */     return this;
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
/*     */   public DiffBuilder<T> append(String fieldName, final double[] lhs, final double[] rhs) {
/* 429 */     validateFieldNameNotNull(fieldName);
/*     */     
/* 431 */     if (this.objectsTriviallyEqual) {
/* 432 */       return this;
/*     */     }
/* 434 */     if (!Arrays.equals(lhs, rhs)) {
/* 435 */       this.diffs.add(new Diff<Double[]>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Double[] getLeft() {
/* 440 */               return ArrayUtils.toObject(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Double[] getRight() {
/* 445 */               return ArrayUtils.toObject(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 449 */     return this;
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
/*     */   public DiffBuilder<T> append(String fieldName, final float lhs, final float rhs) {
/* 467 */     validateFieldNameNotNull(fieldName);
/*     */     
/* 469 */     if (this.objectsTriviallyEqual) {
/* 470 */       return this;
/*     */     }
/* 472 */     if (Float.floatToIntBits(lhs) != Float.floatToIntBits(rhs)) {
/* 473 */       this.diffs.add(new Diff<Float>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Float getLeft() {
/* 478 */               return Float.valueOf(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Float getRight() {
/* 483 */               return Float.valueOf(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 487 */     return this;
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
/*     */   public DiffBuilder<T> append(String fieldName, final float[] lhs, final float[] rhs) {
/* 505 */     validateFieldNameNotNull(fieldName);
/*     */     
/* 507 */     if (this.objectsTriviallyEqual) {
/* 508 */       return this;
/*     */     }
/* 510 */     if (!Arrays.equals(lhs, rhs)) {
/* 511 */       this.diffs.add(new Diff<Float[]>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Float[] getLeft() {
/* 516 */               return ArrayUtils.toObject(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Float[] getRight() {
/* 521 */               return ArrayUtils.toObject(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 525 */     return this;
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
/*     */   public DiffBuilder<T> append(String fieldName, final int lhs, final int rhs) {
/* 543 */     validateFieldNameNotNull(fieldName);
/*     */     
/* 545 */     if (this.objectsTriviallyEqual) {
/* 546 */       return this;
/*     */     }
/* 548 */     if (lhs != rhs) {
/* 549 */       this.diffs.add(new Diff<Integer>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Integer getLeft() {
/* 554 */               return Integer.valueOf(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Integer getRight() {
/* 559 */               return Integer.valueOf(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 563 */     return this;
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
/*     */   public DiffBuilder<T> append(String fieldName, final int[] lhs, final int[] rhs) {
/* 581 */     validateFieldNameNotNull(fieldName);
/*     */     
/* 583 */     if (this.objectsTriviallyEqual) {
/* 584 */       return this;
/*     */     }
/* 586 */     if (!Arrays.equals(lhs, rhs)) {
/* 587 */       this.diffs.add(new Diff<Integer[]>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Integer[] getLeft() {
/* 592 */               return ArrayUtils.toObject(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Integer[] getRight() {
/* 597 */               return ArrayUtils.toObject(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 601 */     return this;
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
/*     */   public DiffBuilder<T> append(String fieldName, final long lhs, final long rhs) {
/* 619 */     validateFieldNameNotNull(fieldName);
/*     */     
/* 621 */     if (this.objectsTriviallyEqual) {
/* 622 */       return this;
/*     */     }
/* 624 */     if (lhs != rhs) {
/* 625 */       this.diffs.add(new Diff<Long>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Long getLeft() {
/* 630 */               return Long.valueOf(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Long getRight() {
/* 635 */               return Long.valueOf(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 639 */     return this;
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
/*     */   public DiffBuilder<T> append(String fieldName, final long[] lhs, final long[] rhs) {
/* 657 */     validateFieldNameNotNull(fieldName);
/*     */     
/* 659 */     if (this.objectsTriviallyEqual) {
/* 660 */       return this;
/*     */     }
/* 662 */     if (!Arrays.equals(lhs, rhs)) {
/* 663 */       this.diffs.add(new Diff<Long[]>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Long[] getLeft() {
/* 668 */               return ArrayUtils.toObject(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Long[] getRight() {
/* 673 */               return ArrayUtils.toObject(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 677 */     return this;
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
/*     */   public DiffBuilder<T> append(String fieldName, final short lhs, final short rhs) {
/* 695 */     validateFieldNameNotNull(fieldName);
/*     */     
/* 697 */     if (this.objectsTriviallyEqual) {
/* 698 */       return this;
/*     */     }
/* 700 */     if (lhs != rhs) {
/* 701 */       this.diffs.add(new Diff<Short>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Short getLeft() {
/* 706 */               return Short.valueOf(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Short getRight() {
/* 711 */               return Short.valueOf(rhs);
/*     */             }
/*     */           });
/*     */     }
/* 715 */     return this;
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
/*     */   public DiffBuilder<T> append(String fieldName, final short[] lhs, final short[] rhs) {
/* 733 */     validateFieldNameNotNull(fieldName);
/*     */     
/* 735 */     if (this.objectsTriviallyEqual) {
/* 736 */       return this;
/*     */     }
/* 738 */     if (!Arrays.equals(lhs, rhs)) {
/* 739 */       this.diffs.add(new Diff<Short[]>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Short[] getLeft() {
/* 744 */               return ArrayUtils.toObject(lhs);
/*     */             }
/*     */ 
/*     */             
/*     */             public Short[] getRight() {
/* 749 */               return ArrayUtils.toObject(rhs);
/*     */             }
/*     */           });
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DiffBuilder<T> append(String fieldName, final Object lhs, final Object rhs) {
/*     */     Object objectToTest;
/* 771 */     validateFieldNameNotNull(fieldName);
/* 772 */     if (this.objectsTriviallyEqual) {
/* 773 */       return this;
/*     */     }
/* 775 */     if (lhs == rhs) {
/* 776 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 780 */     if (lhs != null) {
/* 781 */       objectToTest = lhs;
/*     */     } else {
/*     */       
/* 784 */       objectToTest = rhs;
/*     */     } 
/*     */     
/* 787 */     if (ObjectUtils.isArray(objectToTest)) {
/* 788 */       if (objectToTest instanceof boolean[]) {
/* 789 */         return append(fieldName, (boolean[])lhs, (boolean[])rhs);
/*     */       }
/* 791 */       if (objectToTest instanceof byte[]) {
/* 792 */         return append(fieldName, (byte[])lhs, (byte[])rhs);
/*     */       }
/* 794 */       if (objectToTest instanceof char[]) {
/* 795 */         return append(fieldName, (char[])lhs, (char[])rhs);
/*     */       }
/* 797 */       if (objectToTest instanceof double[]) {
/* 798 */         return append(fieldName, (double[])lhs, (double[])rhs);
/*     */       }
/* 800 */       if (objectToTest instanceof float[]) {
/* 801 */         return append(fieldName, (float[])lhs, (float[])rhs);
/*     */       }
/* 803 */       if (objectToTest instanceof int[]) {
/* 804 */         return append(fieldName, (int[])lhs, (int[])rhs);
/*     */       }
/* 806 */       if (objectToTest instanceof long[]) {
/* 807 */         return append(fieldName, (long[])lhs, (long[])rhs);
/*     */       }
/* 809 */       if (objectToTest instanceof short[]) {
/* 810 */         return append(fieldName, (short[])lhs, (short[])rhs);
/*     */       }
/*     */       
/* 813 */       return append(fieldName, (Object[])lhs, (Object[])rhs);
/*     */     } 
/*     */ 
/*     */     
/* 817 */     if (Objects.equals(lhs, rhs)) {
/* 818 */       return this;
/*     */     }
/*     */     
/* 821 */     this.diffs.add(new Diff(fieldName)
/*     */         {
/*     */           private static final long serialVersionUID = 1L;
/*     */           
/*     */           public Object getLeft() {
/* 826 */             return lhs;
/*     */           }
/*     */ 
/*     */           
/*     */           public Object getRight() {
/* 831 */             return rhs;
/*     */           }
/*     */         });
/*     */     
/* 835 */     return this;
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
/*     */   public DiffBuilder<T> append(String fieldName, final Object[] lhs, final Object[] rhs) {
/* 853 */     validateFieldNameNotNull(fieldName);
/* 854 */     if (this.objectsTriviallyEqual) {
/* 855 */       return this;
/*     */     }
/*     */     
/* 858 */     if (!Arrays.equals(lhs, rhs)) {
/* 859 */       this.diffs.add(new Diff<Object[]>(fieldName)
/*     */           {
/*     */             private static final long serialVersionUID = 1L;
/*     */             
/*     */             public Object[] getLeft() {
/* 864 */               return lhs;
/*     */             }
/*     */ 
/*     */             
/*     */             public Object[] getRight() {
/* 869 */               return rhs;
/*     */             }
/*     */           });
/*     */     }
/*     */     
/* 874 */     return this;
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
/*     */   public DiffBuilder<T> append(String fieldName, DiffResult<T> diffResult) {
/* 911 */     validateFieldNameNotNull(fieldName);
/* 912 */     Objects.requireNonNull(diffResult, "diffResult");
/* 913 */     if (this.objectsTriviallyEqual) {
/* 914 */       return this;
/*     */     }
/* 916 */     diffResult.getDiffs().forEach(diff -> append(fieldName + "." + diff.getFieldName(), diff.getLeft(), diff.getRight()));
/* 917 */     return this;
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
/*     */   public DiffResult<T> build() {
/* 929 */     return new DiffResult<>(this.left, this.right, this.diffs, this.style);
/*     */   }
/*     */   
/*     */   private void validateFieldNameNotNull(String fieldName) {
/* 933 */     Objects.requireNonNull(fieldName, "fieldName");
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\builder\DiffBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */