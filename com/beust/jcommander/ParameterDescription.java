/*     */ package com.beust.jcommander;
/*     */ 
/*     */ import com.beust.jcommander.validators.NoValidator;
/*     */ import com.beust.jcommander.validators.NoValueValidator;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParameterDescription
/*     */ {
/*     */   private Object object;
/*     */   private WrappedParameter wrappedParameter;
/*     */   private Parameter parameterAnnotation;
/*     */   private DynamicParameter dynamicParameterAnnotation;
/*     */   private Parameterized parameterized;
/*     */   private boolean assigned = false;
/*     */   private ResourceBundle bundle;
/*     */   private String description;
/*     */   private JCommander jCommander;
/*     */   private Object defaultObject;
/*  45 */   private String longestName = "";
/*     */ 
/*     */ 
/*     */   
/*     */   public ParameterDescription(Object object, DynamicParameter annotation, Parameterized parameterized, ResourceBundle bundle, JCommander jc) {
/*  50 */     if (!Map.class.isAssignableFrom(parameterized.getType())) {
/*  51 */       throw new ParameterException("@DynamicParameter " + parameterized.getName() + " should be of type Map but is " + parameterized
/*     */           
/*  53 */           .getType().getName());
/*     */     }
/*     */     
/*  56 */     this.dynamicParameterAnnotation = annotation;
/*  57 */     this.wrappedParameter = new WrappedParameter(this.dynamicParameterAnnotation);
/*  58 */     init(object, parameterized, bundle, jc);
/*     */   }
/*     */ 
/*     */   
/*     */   public ParameterDescription(Object object, Parameter annotation, Parameterized parameterized, ResourceBundle bundle, JCommander jc) {
/*  63 */     this.parameterAnnotation = annotation;
/*  64 */     this.wrappedParameter = new WrappedParameter(this.parameterAnnotation);
/*  65 */     init(object, parameterized, bundle, jc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ResourceBundle findResourceBundle(Object o) {
/*  74 */     ResourceBundle result = null;
/*     */     
/*  76 */     Parameters p = o.getClass().<Parameters>getAnnotation(Parameters.class);
/*  77 */     if (p != null && !isEmpty(p.resourceBundle())) {
/*  78 */       result = ResourceBundle.getBundle(p.resourceBundle(), Locale.getDefault());
/*     */     } else {
/*  80 */       ResourceBundle a = o.getClass().<ResourceBundle>getAnnotation(ResourceBundle.class);
/*     */       
/*  82 */       if (a != null && !isEmpty(a.value())) {
/*  83 */         result = ResourceBundle.getBundle(a.value(), Locale.getDefault());
/*     */       }
/*     */     } 
/*     */     
/*  87 */     return result;
/*     */   }
/*     */   
/*     */   private boolean isEmpty(String s) {
/*  91 */     return (s == null || "".equals(s));
/*     */   }
/*     */   
/*     */   private void initDescription(String description, String descriptionKey, String[] names) {
/*  95 */     this.description = description;
/*  96 */     if (!"".equals(descriptionKey) && 
/*  97 */       this.bundle != null) {
/*  98 */       this.description = this.bundle.getString(descriptionKey);
/*     */     }
/*     */ 
/*     */     
/* 102 */     for (String name : names) {
/* 103 */       if (name.length() > this.longestName.length()) this.longestName = name;
/*     */     
/*     */     } 
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
/*     */   private void init(Object object, Parameterized parameterized, ResourceBundle bundle, JCommander jCommander) {
/* 121 */     this.object = object;
/* 122 */     this.parameterized = parameterized;
/* 123 */     this.bundle = bundle;
/* 124 */     if (this.bundle == null) {
/* 125 */       this.bundle = findResourceBundle(object);
/*     */     }
/* 127 */     this.jCommander = jCommander;
/*     */     
/* 129 */     if (this.parameterAnnotation != null) {
/*     */       String description;
/* 131 */       if (Enum.class.isAssignableFrom(parameterized.getType()) && this.parameterAnnotation
/* 132 */         .description().isEmpty()) {
/* 133 */         description = "Options: " + EnumSet.allOf(parameterized.getType());
/*     */       } else {
/* 135 */         description = this.parameterAnnotation.description();
/*     */       } 
/* 137 */       initDescription(description, this.parameterAnnotation.descriptionKey(), this.parameterAnnotation
/* 138 */           .names());
/* 139 */     } else if (this.dynamicParameterAnnotation != null) {
/* 140 */       initDescription(this.dynamicParameterAnnotation.description(), this.dynamicParameterAnnotation
/* 141 */           .descriptionKey(), this.dynamicParameterAnnotation
/* 142 */           .names());
/*     */     } else {
/* 144 */       throw new AssertionError("Shound never happen");
/*     */     } 
/*     */     
/*     */     try {
/* 148 */       this.defaultObject = parameterized.get(object);
/* 149 */     } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 155 */     if (this.defaultObject != null && 
/* 156 */       this.parameterAnnotation != null) {
/* 157 */       validateDefaultValues(this.parameterAnnotation.names());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void validateDefaultValues(String[] names) {
/* 163 */     String name = (names.length > 0) ? names[0] : "";
/* 164 */     validateValueParameter(name, this.defaultObject);
/*     */   }
/*     */   
/*     */   public String getLongestName() {
/* 168 */     return this.longestName;
/*     */   }
/*     */   
/*     */   public Object getDefault() {
/* 172 */     return this.defaultObject;
/*     */   }
/*     */   
/*     */   public String getDescription() {
/* 176 */     return this.description;
/*     */   }
/*     */   
/*     */   public Object getObject() {
/* 180 */     return this.object;
/*     */   }
/*     */   
/*     */   public String getNames() {
/* 184 */     StringBuilder sb = new StringBuilder();
/* 185 */     String[] names = this.wrappedParameter.names();
/* 186 */     for (int i = 0; i < names.length; i++) {
/* 187 */       if (i > 0) sb.append(", "); 
/* 188 */       sb.append(names[i]);
/*     */     } 
/* 190 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public WrappedParameter getParameter() {
/* 194 */     return this.wrappedParameter;
/*     */   }
/*     */   
/*     */   public Parameterized getParameterized() {
/* 198 */     return this.parameterized;
/*     */   }
/*     */   
/*     */   private boolean isMultiOption() {
/* 202 */     Class<?> fieldType = this.parameterized.getType();
/* 203 */     return (fieldType.equals(List.class) || fieldType.equals(Set.class) || this.parameterized
/* 204 */       .isDynamicParameter());
/*     */   }
/*     */   
/*     */   public void addValue(String value) {
/* 208 */     addValue(value, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAssigned() {
/* 215 */     return this.assigned;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAssigned(boolean b) {
/* 220 */     this.assigned = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addValue(String value, boolean isDefault) {
/* 229 */     addValue(null, value, isDefault, true, -1);
/*     */   }
/*     */   Object addValue(String name, String value, boolean isDefault, boolean validate, int currentIndex) {
/*     */     Object finalValue;
/* 233 */     p("Adding " + (isDefault ? "default " : "") + "value:" + value + " to parameter:" + this.parameterized
/* 234 */         .getName());
/* 235 */     if (name == null) {
/* 236 */       name = this.wrappedParameter.names()[0];
/*     */     }
/* 238 */     if ((currentIndex == 0 && this.assigned && !isMultiOption() && !this.jCommander.isParameterOverwritingAllowed()) || 
/* 239 */       isNonOverwritableForced()) {
/* 240 */       throw new ParameterException("Can only specify option " + name + " once.");
/*     */     }
/*     */     
/* 243 */     if (validate) {
/* 244 */       validateParameter(name, value);
/*     */     }
/*     */     
/* 247 */     Class<?> type = this.parameterized.getType();
/*     */     
/* 249 */     Object convertedValue = this.jCommander.convertValue(getParameterized(), getParameterized().getType(), name, value);
/* 250 */     if (validate) {
/* 251 */       validateValueParameter(name, convertedValue);
/*     */     }
/* 253 */     boolean isCollection = Collection.class.isAssignableFrom(type);
/*     */ 
/*     */     
/* 256 */     if (isCollection) {
/*     */       
/* 258 */       Collection<Object> l = (Collection<Object>)this.parameterized.get(this.object);
/* 259 */       if (l == null || fieldIsSetForTheFirstTime(isDefault)) {
/* 260 */         l = newCollection(type);
/* 261 */         this.parameterized.set(this.object, l);
/*     */       } 
/* 263 */       if (convertedValue instanceof Collection) {
/* 264 */         l.addAll((Collection)convertedValue);
/*     */       } else {
/* 266 */         l.add(convertedValue);
/*     */       } 
/* 268 */       finalValue = l;
/*     */     } else {
/*     */       
/* 271 */       List<SubParameterIndex> subParameters = findSubParameters(type);
/* 272 */       if (!subParameters.isEmpty()) {
/*     */         
/* 274 */         finalValue = handleSubParameters(value, currentIndex, type, subParameters);
/*     */       } else {
/*     */         
/* 277 */         this.wrappedParameter.addValue(this.parameterized, this.object, convertedValue);
/* 278 */         finalValue = convertedValue;
/*     */       } 
/*     */     } 
/* 281 */     if (!isDefault) this.assigned = true;
/*     */     
/* 283 */     return finalValue;
/*     */   }
/*     */ 
/*     */   
/*     */   private Object handleSubParameters(String value, int currentIndex, Class<?> type, List<SubParameterIndex> subParameters) {
/*     */     Object finalValue;
/* 289 */     SubParameterIndex sai = null;
/* 290 */     for (SubParameterIndex si : subParameters) {
/* 291 */       if (si.order == currentIndex) {
/* 292 */         sai = si;
/*     */         break;
/*     */       } 
/*     */     } 
/* 296 */     if (sai != null) {
/* 297 */       Object objectValue = this.parameterized.get(this.object);
/*     */       try {
/* 299 */         if (objectValue == null) {
/* 300 */           objectValue = type.newInstance();
/* 301 */           this.parameterized.set(this.object, objectValue);
/*     */         } 
/* 303 */         this.wrappedParameter.addValue(this.parameterized, objectValue, value, sai.field);
/* 304 */         finalValue = objectValue;
/* 305 */       } catch (InstantiationException|IllegalAccessException e) {
/* 306 */         throw new ParameterException("Couldn't instantiate " + type, e);
/*     */       } 
/*     */     } else {
/* 309 */       throw new ParameterException("Couldn't find where to assign parameter " + value + " in " + type);
/*     */     } 
/* 311 */     return finalValue;
/*     */   }
/*     */   
/*     */   public Parameter getParameterAnnotation() {
/* 315 */     return this.parameterAnnotation;
/*     */   }
/*     */   
/*     */   class SubParameterIndex {
/* 319 */     int order = -1;
/*     */     Field field;
/*     */     
/*     */     public SubParameterIndex(int order, Field field) {
/* 323 */       this.order = order;
/* 324 */       this.field = field;
/*     */     }
/*     */   }
/*     */   
/*     */   private List<SubParameterIndex> findSubParameters(Class<?> type) {
/* 329 */     List<SubParameterIndex> result = new ArrayList<>();
/* 330 */     for (Field field : type.getDeclaredFields()) {
/* 331 */       Annotation subParameter = field.getAnnotation((Class)SubParameter.class);
/* 332 */       if (subParameter != null) {
/* 333 */         SubParameter sa = (SubParameter)subParameter;
/* 334 */         result.add(new SubParameterIndex(sa.order(), field));
/*     */       } 
/*     */     } 
/* 337 */     return result;
/*     */   }
/*     */   
/*     */   private void validateParameter(String name, String value) {
/* 341 */     Class[] arrayOfClass = (Class[])this.wrappedParameter.validateWith();
/* 342 */     if (arrayOfClass != null && arrayOfClass.length > 0) {
/* 343 */       for (Class<? extends IParameterValidator> validator : arrayOfClass) {
/* 344 */         validateParameter(validator, name, value);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   void validateValueParameter(String name, Object value) {
/* 350 */     Class[] arrayOfClass = (Class[])this.wrappedParameter.validateValueWith();
/* 351 */     if (arrayOfClass != null && arrayOfClass.length > 0) {
/* 352 */       for (Class<? extends IValueValidator> validator : arrayOfClass) {
/* 353 */         validateValueParameter(validator, name, value);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void validateValueParameter(Class<? extends IValueValidator> validator, String name, Object value) {
/*     */     try {
/* 361 */       if (validator != NoValueValidator.class) {
/* 362 */         p("Validating value parameter:" + name + " value:" + value + " validator:" + validator);
/*     */       }
/* 364 */       ((IValueValidator<Object>)validator.newInstance()).validate(name, value);
/* 365 */     } catch (InstantiationException|IllegalAccessException e) {
/* 366 */       throw new ParameterException("Can't instantiate validator:" + e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void validateParameter(Class<? extends IParameterValidator> validator, String name, String value) {
/*     */     try {
/* 374 */       if (validator != NoValidator.class) {
/* 375 */         p("Validating parameter:" + name + " value:" + value + " validator:" + validator);
/*     */       }
/* 377 */       ((IParameterValidator)validator.newInstance()).validate(name, value);
/* 378 */       if (IParameterValidator2.class.isAssignableFrom(validator)) {
/* 379 */         IParameterValidator2 instance = (IParameterValidator2)validator.newInstance();
/* 380 */         instance.validate(name, value, this);
/*     */       } 
/* 382 */     } catch (InstantiationException|IllegalAccessException e) {
/* 383 */       throw new ParameterException("Can't instantiate validator:" + e);
/* 384 */     } catch (ParameterException ex) {
/* 385 */       throw ex;
/* 386 */     } catch (Exception ex) {
/* 387 */       throw new ParameterException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Collection<Object> newCollection(Class<?> type) {
/* 399 */     if (SortedSet.class.isAssignableFrom(type)) return new TreeSet(); 
/* 400 */     if (LinkedHashSet.class.isAssignableFrom(type)) return new LinkedHashSet(); 
/* 401 */     if (Set.class.isAssignableFrom(type)) return new HashSet(); 
/* 402 */     if (List.class.isAssignableFrom(type)) return new ArrayList();
/*     */     
/* 404 */     throw new ParameterException("Parameters of Collection type '" + type.getSimpleName() + "' are not supported. Please use List or Set instead.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean fieldIsSetForTheFirstTime(boolean isDefault) {
/* 414 */     return (!isDefault && !this.assigned);
/*     */   }
/*     */   
/*     */   private void p(String string) {
/* 418 */     if (System.getProperty("jcommander.debug") != null) {
/* 419 */       this.jCommander.getConsole().println("[ParameterDescription] " + string);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 425 */     return "[ParameterDescription " + this.parameterized.getName() + "]";
/*     */   }
/*     */   
/*     */   public boolean isDynamicParameter() {
/* 429 */     return (this.dynamicParameterAnnotation != null);
/*     */   }
/*     */   
/*     */   public boolean isHelp() {
/* 433 */     return this.wrappedParameter.isHelp();
/*     */   }
/*     */   
/*     */   public boolean isNonOverwritableForced() {
/* 437 */     return this.wrappedParameter.isNonOverwritableForced();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\ParameterDescription.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */