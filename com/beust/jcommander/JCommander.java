/*      */ package com.beust.jcommander;
/*      */ 
/*      */ import com.beust.jcommander.converters.DefaultListConverter;
/*      */ import com.beust.jcommander.converters.EnumConverter;
/*      */ import com.beust.jcommander.converters.IParameterSplitter;
/*      */ import com.beust.jcommander.converters.NoConverter;
/*      */ import com.beust.jcommander.converters.StringConverter;
/*      */ import com.beust.jcommander.internal.Console;
/*      */ import com.beust.jcommander.internal.DefaultConsole;
/*      */ import com.beust.jcommander.internal.DefaultConverterFactory;
/*      */ import com.beust.jcommander.internal.JDK6Console;
/*      */ import com.beust.jcommander.internal.Lists;
/*      */ import com.beust.jcommander.internal.Maps;
/*      */ import com.beust.jcommander.internal.Nullable;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.IOException;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.ParameterizedType;
/*      */ import java.lang.reflect.Type;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.Paths;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Comparator;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.concurrent.CopyOnWriteArrayList;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class JCommander
/*      */ {
/*      */   public static final String DEBUG_PROPERTY = "jcommander.debug";
/*      */   private Map<FuzzyMap.IKey, ParameterDescription> descriptions;
/*   58 */   private List<Object> objects = Lists.newArrayList();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class MainParameter
/*      */   {
/*      */     Parameterized parameterized;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Object object;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Parameter annotation;
/*      */ 
/*      */ 
/*      */     
/*      */     private ParameterDescription description;
/*      */ 
/*      */ 
/*      */     
/*   84 */     private List<Object> multipleValue = null;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   89 */     private Object singleValue = null;
/*      */     
/*      */     private boolean firstTimeMainParameter = true;
/*      */     
/*      */     public ParameterDescription getDescription() {
/*   94 */       return this.description;
/*      */     }
/*      */     
/*      */     public void addValue(Object convertedValue) {
/*   98 */       if (this.multipleValue != null)
/*   99 */       { this.multipleValue.add(convertedValue); }
/*  100 */       else { if (this.singleValue != null) {
/*  101 */           throw new ParameterException("Only one main parameter allowed but found several: \"" + this.singleValue + "\" and \"" + convertedValue + "\"");
/*      */         }
/*      */         
/*  104 */         this.singleValue = convertedValue;
/*  105 */         this.parameterized.set(this.object, convertedValue); }
/*      */     
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  113 */   private IUsageFormatter usageFormatter = new DefaultUsageFormatter(this);
/*      */   
/*  115 */   private MainParameter mainParameter = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  125 */   private Map<Parameterized, ParameterDescription> requiredFields = Maps.newHashMap();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  130 */   private Map<Parameterized, ParameterDescription> fields = Maps.newHashMap();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  135 */   private Map<ProgramName, JCommander> commands = Maps.newLinkedHashMap();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  140 */   private Map<FuzzyMap.IKey, ProgramName> aliasMap = Maps.newLinkedHashMap();
/*      */ 
/*      */ 
/*      */   
/*      */   private String parsedCommand;
/*      */ 
/*      */ 
/*      */   
/*      */   private String parsedAlias;
/*      */ 
/*      */   
/*      */   private ProgramName programName;
/*      */ 
/*      */   
/*      */   private boolean helpWasSpecified;
/*      */ 
/*      */   
/*  157 */   private List<String> unknownArgs = Lists.newArrayList();
/*      */   
/*      */   private Console console;
/*      */   
/*      */   private final Options options;
/*      */   
/*      */   private final IVariableArity DEFAULT_VARIABLE_ARITY;
/*      */ 
/*      */   
/*      */   private static class Options
/*      */   {
/*      */     private ResourceBundle bundle;
/*      */     
/*      */     private IDefaultProvider defaultProvider;
/*      */ 
/*      */     
/*      */     private Options() {}
/*      */     
/*  175 */     private Comparator<? super ParameterDescription> parameterDescriptionComparator = new Comparator<ParameterDescription>()
/*      */       {
/*      */         public int compare(ParameterDescription p0, ParameterDescription p1)
/*      */         {
/*  179 */           WrappedParameter a0 = p0.getParameter();
/*  180 */           WrappedParameter a1 = p1.getParameter();
/*  181 */           if (a0 != null && a0.order() != -1 && a1 != null && a1.order() != -1)
/*  182 */             return Integer.compare(a0.order(), a1.order()); 
/*  183 */           if (a0 != null && a0.order() != -1)
/*  184 */             return -1; 
/*  185 */           if (a1 != null && a1.order() != -1) {
/*  186 */             return 1;
/*      */           }
/*  188 */           return p0.getLongestName().compareTo(p1.getLongestName());
/*      */         }
/*      */       };
/*      */     
/*  192 */     private int columnSize = 79;
/*      */     private boolean acceptUnknownOptions = false;
/*      */     private boolean allowParameterOverwriting = false;
/*      */     private boolean expandAtSign = true;
/*  196 */     private int verbose = 0;
/*      */     
/*      */     private boolean caseSensitiveOptions = true;
/*      */     
/*      */     private boolean allowAbbreviatedOptions = false;
/*      */     
/*  202 */     private final List<IStringConverterInstanceFactory> converterInstanceFactories = new CopyOnWriteArrayList<>();
/*  203 */     private Charset atFileCharset = Charset.defaultCharset();
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
/*      */   public JCommander() {
/*  220 */     this(new Options(null));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JCommander(Object object) {
/*  227 */     this(object, (ResourceBundle)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JCommander(Object object, @Nullable ResourceBundle bundle) {
/*  235 */     this(object, bundle, (String[])null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JCommander(Object object, @Nullable ResourceBundle bundle, String... args) {
/*  244 */     this();
/*  245 */     addObject(object);
/*  246 */     if (bundle != null) {
/*  247 */       setDescriptionsBundle(bundle);
/*      */     }
/*  249 */     createDescriptions();
/*  250 */     if (args != null) {
/*  251 */       parse(args);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public JCommander(Object object, String... args) {
/*  263 */     this(object);
/*  264 */     parse(args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExpandAtSign(boolean expandAtSign) {
/*  274 */     this.options.expandAtSign = expandAtSign;
/*      */   }
/*      */   public void setConsole(Console console) {
/*  277 */     this.console = console;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Console getConsole() {
/*  283 */     if (this.console == null) {
/*      */       try {
/*  285 */         Method consoleMethod = System.class.getDeclaredMethod("console", new Class[0]);
/*  286 */         Object console = consoleMethod.invoke(null, new Object[0]);
/*  287 */         this.console = (Console)new JDK6Console(console);
/*  288 */       } catch (Throwable t) {
/*  289 */         this.console = (Console)new DefaultConsole();
/*      */       } 
/*      */     }
/*  292 */     return this.console;
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
/*      */   public final void addObject(Object object) {
/*  305 */     if (object instanceof Iterable) {
/*      */       
/*  307 */       for (Object o : object) {
/*  308 */         this.objects.add(o);
/*      */       }
/*  310 */     } else if (object.getClass().isArray()) {
/*      */       
/*  312 */       for (Object o : (Object[])object) {
/*  313 */         this.objects.add(o);
/*      */       }
/*      */     } else {
/*      */       
/*  317 */       this.objects.add(object);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setDescriptionsBundle(ResourceBundle bundle) {
/*  327 */     this.options.bundle = bundle;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parse(String... args) {
/*      */     try {
/*  335 */       parse(true, args);
/*  336 */     } catch (ParameterException ex) {
/*  337 */       ex.setJCommander(this);
/*  338 */       throw ex;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parseWithoutValidation(String... args) {
/*  346 */     parse(false, args);
/*      */   }
/*      */   
/*      */   private void parse(boolean validate, String... args) {
/*  350 */     StringBuilder sb = new StringBuilder("Parsing \"");
/*  351 */     sb.append(Strings.join(" ", (Object[])args)).append("\"\n  with:").append(Strings.join(" ", this.objects.toArray()));
/*  352 */     p(sb.toString());
/*      */     
/*  354 */     if (this.descriptions == null) createDescriptions(); 
/*  355 */     initializeDefaultValues();
/*  356 */     parseValues(expandArgs(args), validate);
/*  357 */     if (validate) validateOptions(); 
/*      */   }
/*      */   
/*      */   private void initializeDefaultValues() {
/*  361 */     if (this.options.defaultProvider != null) {
/*  362 */       for (ParameterDescription pd : this.descriptions.values()) {
/*  363 */         initializeDefaultValue(pd);
/*      */       }
/*      */       
/*  366 */       for (Map.Entry<ProgramName, JCommander> entry : this.commands.entrySet()) {
/*  367 */         ((JCommander)entry.getValue()).initializeDefaultValues();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void validateOptions() {
/*  377 */     if (this.helpWasSpecified) {
/*      */       return;
/*      */     }
/*      */     
/*  381 */     if (!this.requiredFields.isEmpty()) {
/*  382 */       List<String> missingFields = new ArrayList<>();
/*  383 */       for (ParameterDescription pd : this.requiredFields.values()) {
/*  384 */         missingFields.add("[" + Strings.join(" | ", (Object[])pd.getParameter().names()) + "]");
/*      */       }
/*  386 */       String message = Strings.join(", ", missingFields);
/*  387 */       throw new ParameterException("The following " + 
/*  388 */           pluralize(this.requiredFields.size(), "option is required: ", "options are required: ") + message);
/*      */     } 
/*      */ 
/*      */     
/*  392 */     if (this.mainParameter != null && this.mainParameter.description != null) {
/*  393 */       ParameterDescription mainParameterDescription = this.mainParameter.description;
/*      */       
/*  395 */       if (mainParameterDescription.getParameter().required() && 
/*  396 */         !mainParameterDescription.isAssigned()) {
/*  397 */         throw new ParameterException("Main parameters are required (\"" + mainParameterDescription
/*  398 */             .getDescription() + "\")");
/*      */       }
/*      */ 
/*      */       
/*  402 */       int arity = mainParameterDescription.getParameter().arity();
/*  403 */       if (arity != -1) {
/*  404 */         Object value = mainParameterDescription.getParameterized().get(this.mainParameter.object);
/*  405 */         if (List.class.isAssignableFrom(value.getClass())) {
/*  406 */           int size = ((List)value).size();
/*  407 */           if (size != arity) {
/*  408 */             throw new ParameterException("There should be exactly " + arity + " main parameters but " + size + " were found");
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static String pluralize(int quantity, String singular, String plural) {
/*  418 */     return (quantity == 1) ? singular : plural;
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
/*      */   private String[] expandArgs(String[] originalArgv) {
/*  430 */     List<String> vResult1 = Lists.newArrayList();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  435 */     for (String arg : originalArgv) {
/*      */       
/*  437 */       if (arg.startsWith("@") && this.options.expandAtSign) {
/*  438 */         String fileName = arg.substring(1);
/*  439 */         vResult1.addAll(readFile(fileName));
/*      */       } else {
/*  441 */         List<String> expanded = expandDynamicArg(arg);
/*  442 */         vResult1.addAll(expanded);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  448 */     List<String> vResult2 = Lists.newArrayList();
/*  449 */     for (String arg : vResult1) {
/*  450 */       if (isOption(arg)) {
/*  451 */         String sep = getSeparatorFor(arg);
/*  452 */         if (!" ".equals(sep)) {
/*  453 */           String[] sp = arg.split("[" + sep + "]", 2);
/*  454 */           for (String ssp : sp)
/*  455 */             vResult2.add(ssp); 
/*      */           continue;
/*      */         } 
/*  458 */         vResult2.add(arg);
/*      */         continue;
/*      */       } 
/*  461 */       vResult2.add(arg);
/*      */     } 
/*      */ 
/*      */     
/*  465 */     return vResult2.<String>toArray(new String[vResult2.size()]);
/*      */   }
/*      */   
/*      */   private List<String> expandDynamicArg(String arg) {
/*  469 */     for (ParameterDescription pd : this.descriptions.values()) {
/*  470 */       if (pd.isDynamicParameter()) {
/*  471 */         for (String name : pd.getParameter().names()) {
/*  472 */           if (arg.startsWith(name) && !arg.equals(name)) {
/*  473 */             return Arrays.asList(new String[] { name, arg.substring(name.length()) });
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/*  479 */     return Arrays.asList(new String[] { arg });
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean matchArg(String arg, FuzzyMap.IKey key) {
/*  485 */     String kn = this.options.caseSensitiveOptions ? key.getName() : key.getName().toLowerCase();
/*  486 */     if (this.options.allowAbbreviatedOptions) {
/*  487 */       if (kn.startsWith(arg)) return true; 
/*      */     } else {
/*  489 */       ParameterDescription pd = this.descriptions.get(key);
/*  490 */       if (pd != null)
/*      */       
/*      */       { 
/*  493 */         String separator = getSeparatorFor(arg);
/*  494 */         if (!" ".equals(separator))
/*  495 */         { if (arg.startsWith(kn)) return true;
/*      */            }
/*  497 */         else if (kn.equals(arg)) { return true; }
/*      */         
/*      */          }
/*      */       
/*  501 */       else if (kn.equals(arg)) { return true; }
/*      */     
/*      */     } 
/*  504 */     return false;
/*      */   }
/*      */   
/*      */   private boolean isOption(String passedArg) {
/*  508 */     if (this.options.acceptUnknownOptions) return true;
/*      */     
/*  510 */     String arg = this.options.caseSensitiveOptions ? passedArg : passedArg.toLowerCase();
/*      */     
/*  512 */     for (FuzzyMap.IKey key : this.descriptions.keySet()) {
/*  513 */       if (matchArg(arg, key)) return true; 
/*      */     } 
/*  515 */     for (FuzzyMap.IKey key : this.commands.keySet()) {
/*  516 */       if (matchArg(arg, key)) return true;
/*      */     
/*      */     } 
/*  519 */     return false;
/*      */   }
/*      */   
/*      */   private ParameterDescription getPrefixDescriptionFor(String arg) {
/*  523 */     for (Map.Entry<FuzzyMap.IKey, ParameterDescription> es : this.descriptions.entrySet()) {
/*  524 */       if (Strings.startsWith(arg, ((FuzzyMap.IKey)es.getKey()).getName(), this.options.caseSensitiveOptions)) return es.getValue();
/*      */     
/*      */     } 
/*  527 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ParameterDescription getDescriptionFor(String arg) {
/*  535 */     return getPrefixDescriptionFor(arg);
/*      */   }
/*      */   
/*      */   private String getSeparatorFor(String arg) {
/*  539 */     ParameterDescription pd = getDescriptionFor(arg);
/*      */ 
/*      */     
/*  542 */     if (pd != null) {
/*  543 */       Parameters p = pd.getObject().getClass().<Parameters>getAnnotation(Parameters.class);
/*  544 */       if (p != null) return p.separators();
/*      */     
/*      */     } 
/*  547 */     return " ";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<String> readFile(String fileName) {
/*  558 */     List<String> result = Lists.newArrayList();
/*      */     
/*  560 */     try (BufferedReader bufRead = Files.newBufferedReader(Paths.get(fileName, new String[0]), this.options.atFileCharset)) {
/*      */       String line;
/*      */       
/*  563 */       while ((line = bufRead.readLine()) != null) {
/*      */         
/*  565 */         if (line.length() > 0 && !line.trim().startsWith("#")) {
/*  566 */           result.add(line);
/*      */         }
/*      */       } 
/*  569 */     } catch (IOException e) {
/*  570 */       throw new ParameterException("Could not read file " + fileName + ": " + e);
/*      */     } 
/*      */     
/*  573 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String trim(String string) {
/*  580 */     String result = string.trim();
/*  581 */     if (result.startsWith("\"") && result.endsWith("\"") && result.length() > 1) {
/*  582 */       result = result.substring(1, result.length() - 1);
/*      */     }
/*  584 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void createDescriptions() {
/*  591 */     this.descriptions = Maps.newHashMap();
/*      */     
/*  593 */     for (Object object : this.objects) {
/*  594 */       addDescription(object);
/*      */     }
/*      */   }
/*      */   
/*      */   private void addDescription(Object object) {
/*  599 */     Class<?> cls = object.getClass();
/*      */     
/*  601 */     List<Parameterized> parameterizeds = Parameterized.parseArg(object);
/*  602 */     for (Parameterized parameterized : parameterizeds) {
/*  603 */       WrappedParameter wp = parameterized.getWrappedParameter();
/*  604 */       if (wp != null && wp.getParameter() != null) {
/*  605 */         Parameter annotation = wp.getParameter();
/*      */ 
/*      */ 
/*      */         
/*  609 */         Parameter p = annotation;
/*  610 */         if ((p.names()).length == 0) {
/*  611 */           p("Found main parameter:" + parameterized);
/*  612 */           if (this.mainParameter != null) {
/*  613 */             throw new ParameterException("Only one @Parameter with no names attribute is allowed, found:" + this.mainParameter + " and " + parameterized);
/*      */           }
/*      */           
/*  616 */           this.mainParameter = new MainParameter();
/*  617 */           this.mainParameter.parameterized = parameterized;
/*  618 */           this.mainParameter.object = object;
/*  619 */           this.mainParameter.annotation = p;
/*  620 */           this.mainParameter.description = new ParameterDescription(object, p, parameterized, this.options
/*  621 */               .bundle, this);
/*      */           continue;
/*      */         } 
/*  624 */         ParameterDescription pd = new ParameterDescription(object, p, parameterized, this.options.bundle, this);
/*  625 */         for (String name : p.names()) {
/*  626 */           if (this.descriptions.containsKey(new StringKey(name))) {
/*  627 */             throw new ParameterException("Found the option " + name + " multiple times");
/*      */           }
/*  629 */           p("Adding description for " + name);
/*  630 */           this.fields.put(parameterized, pd);
/*  631 */           this.descriptions.put(new StringKey(name), pd);
/*      */           
/*  633 */           if (p.required()) this.requiredFields.put(parameterized, pd); 
/*      */         }  continue;
/*      */       } 
/*  636 */       if (parameterized.getDelegateAnnotation() != null) {
/*      */ 
/*      */ 
/*      */         
/*  640 */         Object delegateObject = parameterized.get(object);
/*  641 */         if (delegateObject == null) {
/*  642 */           throw new ParameterException("Delegate field '" + parameterized.getName() + "' cannot be null.");
/*      */         }
/*      */         
/*  645 */         addDescription(delegateObject); continue;
/*  646 */       }  if (wp != null && wp.getDynamicParameter() != null) {
/*      */ 
/*      */ 
/*      */         
/*  650 */         DynamicParameter dp = wp.getDynamicParameter();
/*  651 */         for (String name : dp.names()) {
/*  652 */           if (this.descriptions.containsKey(name)) {
/*  653 */             throw new ParameterException("Found the option " + name + " multiple times");
/*      */           }
/*  655 */           p("Adding description for " + name);
/*      */           
/*  657 */           ParameterDescription pd = new ParameterDescription(object, dp, parameterized, this.options.bundle, this);
/*  658 */           this.fields.put(parameterized, pd);
/*  659 */           this.descriptions.put(new StringKey(name), pd);
/*      */           
/*  661 */           if (dp.required()) this.requiredFields.put(parameterized, pd); 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void initializeDefaultValue(ParameterDescription pd) {
/*  668 */     for (String optionName : pd.getParameter().names()) {
/*  669 */       String def = this.options.defaultProvider.getDefaultValueFor(optionName);
/*  670 */       if (def != null) {
/*  671 */         p("Initializing " + optionName + " with default value:" + def);
/*  672 */         pd.addValue(def, true);
/*      */         
/*  674 */         this.requiredFields.remove(pd.getParameterized());
/*      */         return;
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
/*      */   private void parseValues(String[] args, boolean validate) {
/*  687 */     boolean commandParsed = false;
/*  688 */     int i = 0;
/*  689 */     boolean isDashDash = false;
/*  690 */     while (i < args.length && !commandParsed) {
/*  691 */       String arg = args[i];
/*  692 */       String a = trim(arg);
/*  693 */       args[i] = a;
/*  694 */       p("Parsing arg: " + a);
/*      */       
/*  696 */       JCommander jc = findCommandByAlias(arg);
/*  697 */       int increment = 1;
/*  698 */       if (!isDashDash && !"--".equals(a) && isOption(a) && jc == null) {
/*      */ 
/*      */ 
/*      */         
/*  702 */         ParameterDescription pd = findParameterDescription(a);
/*      */         
/*  704 */         if (pd != null) {
/*  705 */           if (pd.getParameter().password()) {
/*  706 */             increment = processPassword(args, i, pd, validate);
/*      */           }
/*  708 */           else if (pd.getParameter().variableArity()) {
/*      */ 
/*      */ 
/*      */             
/*  712 */             increment = processVariableArity(args, i, pd, validate);
/*      */           
/*      */           }
/*      */           else {
/*      */             
/*  717 */             Class<?> fieldType = pd.getParameterized().getType();
/*      */ 
/*      */ 
/*      */             
/*  721 */             if (pd.getParameter().arity() == -1 && isBooleanType(fieldType)) {
/*  722 */               handleBooleanOption(pd, fieldType);
/*      */             } else {
/*  724 */               increment = processFixedArity(args, i, pd, validate, fieldType);
/*      */             } 
/*      */             
/*  727 */             if (pd.isHelp()) {
/*  728 */               this.helpWasSpecified = true;
/*      */             }
/*      */           }
/*      */         
/*      */         }
/*  733 */         else if (this.options.acceptUnknownOptions) {
/*  734 */           this.unknownArgs.add(arg);
/*  735 */           i++;
/*  736 */           while (i < args.length && !isOption(args[i])) {
/*  737 */             this.unknownArgs.add(args[i++]);
/*      */           }
/*  739 */           increment = 0;
/*      */         } else {
/*  741 */           throw new ParameterException("Unknown option: " + arg);
/*      */ 
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/*  748 */       else if ("--".equals(arg) && !isDashDash) {
/*  749 */         isDashDash = true;
/*      */       }
/*  751 */       else if (this.commands.isEmpty()) {
/*      */ 
/*      */ 
/*      */         
/*  755 */         initMainParameterValue(arg);
/*  756 */         String value = a;
/*  757 */         Object convertedValue = value;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  762 */         if (this.mainParameter.annotation.converter() != null && this.mainParameter.annotation.converter() != NoConverter.class) {
/*  763 */           convertedValue = convertValue(this.mainParameter.parameterized, this.mainParameter.parameterized.getType(), null, value);
/*      */         }
/*      */         
/*  766 */         Type genericType = this.mainParameter.parameterized.getGenericType();
/*  767 */         if (genericType instanceof ParameterizedType) {
/*  768 */           ParameterizedType p = (ParameterizedType)genericType;
/*  769 */           Type cls = p.getActualTypeArguments()[0];
/*  770 */           if (cls instanceof Class) {
/*  771 */             convertedValue = convertValue(this.mainParameter.parameterized, (Class)cls, null, value);
/*      */           }
/*      */         } 
/*      */         
/*  775 */         for (Class<? extends IParameterValidator> validator : this.mainParameter.annotation.validateWith())
/*      */         {
/*  777 */           this.mainParameter.description.validateParameter(validator, "Default", value);
/*      */         }
/*      */ 
/*      */         
/*  781 */         this.mainParameter.description.setAssigned(true);
/*  782 */         this.mainParameter.addValue(convertedValue);
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  787 */         if (jc == null && validate)
/*  788 */           throw new MissingCommandException("Expected a command, got " + arg, arg); 
/*  789 */         if (jc != null) {
/*  790 */           this.parsedCommand = jc.programName.name;
/*  791 */           this.parsedAlias = arg;
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  796 */           jc.parse(validate, subArray(args, i + 1));
/*  797 */           commandParsed = true;
/*      */         } 
/*      */       } 
/*      */       
/*  801 */       i += increment;
/*      */     } 
/*      */ 
/*      */     
/*  805 */     for (ParameterDescription parameterDescription : this.descriptions.values()) {
/*  806 */       if (parameterDescription.isAssigned()) {
/*  807 */         ((ParameterDescription)this.fields.get(parameterDescription.getParameterized())).setAssigned(true);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isBooleanType(Class<?> fieldType) {
/*  814 */     return (Boolean.class.isAssignableFrom(fieldType) || boolean.class.isAssignableFrom(fieldType));
/*      */   }
/*      */ 
/*      */   
/*      */   private void handleBooleanOption(ParameterDescription pd, Class<?> fieldType) {
/*  819 */     Boolean value = (Boolean)pd.getParameterized().get(pd.getObject());
/*  820 */     if (value != null) {
/*  821 */       pd.addValue(value.booleanValue() ? "false" : "true");
/*  822 */     } else if (!fieldType.isPrimitive()) {
/*  823 */       pd.addValue("true");
/*      */     } 
/*  825 */     this.requiredFields.remove(pd.getParameterized());
/*      */   }
/*      */   
/*      */   private class DefaultVariableArity implements IVariableArity {
/*      */     private DefaultVariableArity() {}
/*      */     
/*      */     public int processVariableArity(String optionName, String[] options) {
/*  832 */       int i = 0;
/*  833 */       while (i < options.length && !JCommander.this.isOption(options[i])) {
/*  834 */         i++;
/*      */       }
/*  836 */       return i;
/*      */     }
/*      */   }
/*      */   
/*  840 */   private JCommander(Options options) { this.DEFAULT_VARIABLE_ARITY = new DefaultVariableArity(); if (options == null)
/*      */       throw new NullPointerException("options");  this.options = options;
/*      */     if (options.converterInstanceFactories.isEmpty())
/*  843 */       addConverterFactory((IStringConverterFactory)new DefaultConverterFactory());  } private final int determineArity(String[] args, int index, ParameterDescription pd, IVariableArity va) { List<String> currentArgs = Lists.newArrayList();
/*  844 */     for (int j = index + 1; j < args.length; j++) {
/*  845 */       currentArgs.add(args[j]);
/*      */     }
/*  847 */     return va.processVariableArity(pd.getParameter().names()[0], currentArgs
/*  848 */         .<String>toArray(new String[0])); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int processPassword(String[] args, int index, ParameterDescription pd, boolean validate) {
/*  855 */     int passwordArity = determineArity(args, index, pd, this.DEFAULT_VARIABLE_ARITY);
/*  856 */     if (passwordArity == 0) {
/*      */       
/*  858 */       char[] password = readPassword(pd.getDescription(), pd.getParameter().echoInput());
/*  859 */       pd.addValue(new String(password));
/*  860 */       this.requiredFields.remove(pd.getParameterized());
/*  861 */       return 1;
/*  862 */     }  if (passwordArity == 1)
/*      */     {
/*  864 */       return processFixedArity(args, index, pd, validate, List.class, 1);
/*      */     }
/*  866 */     throw new ParameterException("Password parameter must have at most 1 argument.");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int processVariableArity(String[] args, int index, ParameterDescription pd, boolean validate) {
/*      */     IVariableArity va;
/*  874 */     Object arg = pd.getObject();
/*      */     
/*  876 */     if (!(arg instanceof IVariableArity)) {
/*  877 */       va = this.DEFAULT_VARIABLE_ARITY;
/*      */     } else {
/*  879 */       va = (IVariableArity)arg;
/*      */     } 
/*      */     
/*  882 */     int arity = determineArity(args, index, pd, va);
/*  883 */     int result = processFixedArity(args, index, pd, validate, List.class, arity);
/*  884 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int processFixedArity(String[] args, int index, ParameterDescription pd, boolean validate, Class<?> fieldType) {
/*  891 */     int arity = pd.getParameter().arity();
/*  892 */     int n = (arity != -1) ? arity : 1;
/*      */     
/*  894 */     return processFixedArity(args, index, pd, validate, fieldType, n);
/*      */   }
/*      */ 
/*      */   
/*      */   private int processFixedArity(String[] args, int originalIndex, ParameterDescription pd, boolean validate, Class<?> fieldType, int arity) {
/*  899 */     int index = originalIndex;
/*  900 */     String arg = args[index];
/*      */     
/*  902 */     if (arity == 0 && isBooleanType(fieldType))
/*  903 */     { handleBooleanOption(pd, fieldType); }
/*  904 */     else { if (arity == 0)
/*  905 */         throw new ParameterException("Expected a value after parameter " + arg); 
/*  906 */       if (index < args.length - 1) {
/*  907 */         int offset = "--".equals(args[index + 1]) ? 1 : 0;
/*      */         
/*  909 */         Object finalValue = null;
/*  910 */         if (index + arity < args.length) {
/*  911 */           for (int j = 1; j <= arity; j++) {
/*  912 */             String value = args[index + j + offset];
/*  913 */             finalValue = pd.addValue(arg, value, false, validate, j - 1);
/*  914 */             this.requiredFields.remove(pd.getParameterized());
/*      */           } 
/*      */           
/*  917 */           if (finalValue != null && validate) {
/*  918 */             pd.validateValueParameter(arg, finalValue);
/*      */           }
/*  920 */           index += arity + offset;
/*      */         } else {
/*  922 */           throw new ParameterException("Expected " + arity + " values after " + arg);
/*      */         } 
/*      */       } else {
/*  925 */         throw new ParameterException("Expected a value after parameter " + arg);
/*      */       }  }
/*      */     
/*  928 */     return arity + 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private char[] readPassword(String description, boolean echoInput) {
/*  936 */     getConsole().print(description + ": ");
/*  937 */     return getConsole().readPassword(echoInput);
/*      */   }
/*      */   
/*      */   private String[] subArray(String[] args, int index) {
/*  941 */     int l = args.length - index;
/*  942 */     String[] result = new String[l];
/*  943 */     System.arraycopy(args, index, result, 0, l);
/*      */     
/*  945 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void initMainParameterValue(String arg) {
/*  953 */     if (this.mainParameter == null) {
/*  954 */       throw new ParameterException("Was passed main parameter '" + arg + "' but no main parameter was defined in your arg class");
/*      */     }
/*      */ 
/*      */     
/*  958 */     Object object = this.mainParameter.parameterized.get(this.mainParameter.object);
/*  959 */     Class<?> type = this.mainParameter.parameterized.getType();
/*      */ 
/*      */     
/*  962 */     if (List.class.isAssignableFrom(type)) {
/*      */       List result;
/*  964 */       if (object == null) {
/*  965 */         result = Lists.newArrayList();
/*      */       } else {
/*  967 */         result = (List)object;
/*      */       } 
/*      */       
/*  970 */       if (this.mainParameter.firstTimeMainParameter) {
/*  971 */         result.clear();
/*  972 */         this.mainParameter.firstTimeMainParameter = false;
/*      */       } 
/*      */       
/*  975 */       this.mainParameter.multipleValue = result;
/*  976 */       this.mainParameter.parameterized.set(this.mainParameter.object, result);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public String getMainParameterDescription() {
/*  982 */     if (this.descriptions == null) createDescriptions(); 
/*  983 */     return (this.mainParameter.annotation != null) ? this.mainParameter.annotation.description() : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setProgramName(String name) {
/*  991 */     setProgramName(name, new String[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getProgramName() {
/*  998 */     return (this.programName == null) ? null : this.programName.getName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getProgramDisplayName() {
/* 1005 */     return (this.programName == null) ? null : this.programName.getDisplayName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setProgramName(String name, String... aliases) {
/* 1015 */     this.programName = new ProgramName(name, Arrays.asList(aliases));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void usage() {
/* 1022 */     StringBuilder sb = new StringBuilder();
/* 1023 */     this.usageFormatter.usage(sb);
/* 1024 */     getConsole().println(sb.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUsageFormatter(IUsageFormatter usageFormatter) {
/* 1034 */     if (usageFormatter == null)
/* 1035 */       throw new IllegalArgumentException("Argument UsageFormatter must not be null"); 
/* 1036 */     this.usageFormatter = usageFormatter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IUsageFormatter getUsageFormatter() {
/* 1045 */     return this.usageFormatter;
/*      */   }
/*      */   
/*      */   public Options getOptions() {
/* 1049 */     return this.options;
/*      */   }
/*      */   
/*      */   public Map<FuzzyMap.IKey, ParameterDescription> getDescriptions() {
/* 1053 */     return this.descriptions;
/*      */   }
/*      */   
/*      */   public MainParameter getMainParameter() {
/* 1057 */     return this.mainParameter;
/*      */   }
/*      */   
/*      */   public static Builder newBuilder() {
/* 1061 */     return new Builder();
/*      */   }
/*      */   
/*      */   public static class Builder {
/* 1065 */     private JCommander jCommander = new JCommander();
/* 1066 */     private String[] args = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder addObject(Object o) {
/* 1080 */       this.jCommander.addObject(o);
/* 1081 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder resourceBundle(ResourceBundle bundle) {
/* 1089 */       this.jCommander.setDescriptionsBundle(bundle);
/* 1090 */       return this;
/*      */     }
/*      */     
/*      */     public Builder args(String[] args) {
/* 1094 */       this.args = args;
/* 1095 */       return this;
/*      */     }
/*      */     
/*      */     public Builder console(Console console) {
/* 1099 */       this.jCommander.setConsole(console);
/* 1100 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder expandAtSign(Boolean expand) {
/* 1110 */       this.jCommander.setExpandAtSign(expand.booleanValue());
/* 1111 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder programName(String name) {
/* 1118 */       this.jCommander.setProgramName(name);
/* 1119 */       return this;
/*      */     }
/*      */     
/*      */     public Builder columnSize(int columnSize) {
/* 1123 */       this.jCommander.setColumnSize(columnSize);
/* 1124 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder defaultProvider(IDefaultProvider provider) {
/* 1131 */       this.jCommander.setDefaultProvider(provider);
/* 1132 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder addConverterFactory(IStringConverterFactory factory) {
/* 1140 */       this.jCommander.addConverterFactory(factory);
/* 1141 */       return this;
/*      */     }
/*      */     
/*      */     public Builder verbose(int verbose) {
/* 1145 */       this.jCommander.setVerbose(verbose);
/* 1146 */       return this;
/*      */     }
/*      */     
/*      */     public Builder allowAbbreviatedOptions(boolean b) {
/* 1150 */       this.jCommander.setAllowAbbreviatedOptions(b);
/* 1151 */       return this;
/*      */     }
/*      */     
/*      */     public Builder acceptUnknownOptions(boolean b) {
/* 1155 */       this.jCommander.setAcceptUnknownOptions(b);
/* 1156 */       return this;
/*      */     }
/*      */     
/*      */     public Builder allowParameterOverwriting(boolean b) {
/* 1160 */       this.jCommander.setAllowParameterOverwriting(b);
/* 1161 */       return this;
/*      */     }
/*      */     
/*      */     public Builder atFileCharset(Charset charset) {
/* 1165 */       this.jCommander.setAtFileCharset(charset);
/* 1166 */       return this;
/*      */     }
/*      */     
/*      */     public Builder addConverterInstanceFactory(IStringConverterInstanceFactory factory) {
/* 1170 */       this.jCommander.addConverterInstanceFactory(factory);
/* 1171 */       return this;
/*      */     }
/*      */     
/*      */     public Builder addCommand(Object command) {
/* 1175 */       this.jCommander.addCommand(command);
/* 1176 */       return this;
/*      */     }
/*      */     
/*      */     public Builder addCommand(String name, Object command, String... aliases) {
/* 1180 */       this.jCommander.addCommand(name, command, aliases);
/* 1181 */       return this;
/*      */     }
/*      */     
/*      */     public Builder usageFormatter(IUsageFormatter usageFormatter) {
/* 1185 */       this.jCommander.setUsageFormatter(usageFormatter);
/* 1186 */       return this;
/*      */     }
/*      */     
/*      */     public JCommander build() {
/* 1190 */       if (this.args != null) {
/* 1191 */         this.jCommander.parse(this.args);
/*      */       }
/* 1193 */       return this.jCommander;
/*      */     }
/*      */   }
/*      */   
/*      */   public Map<Parameterized, ParameterDescription> getFields() {
/* 1198 */     return this.fields;
/*      */   }
/*      */   
/*      */   public Comparator<? super ParameterDescription> getParameterDescriptionComparator() {
/* 1202 */     return this.options.parameterDescriptionComparator;
/*      */   }
/*      */   
/*      */   public void setParameterDescriptionComparator(Comparator<? super ParameterDescription> c) {
/* 1206 */     this.options.parameterDescriptionComparator = c;
/*      */   }
/*      */   
/*      */   public void setColumnSize(int columnSize) {
/* 1210 */     this.options.columnSize = columnSize;
/*      */   }
/*      */   
/*      */   public int getColumnSize() {
/* 1214 */     return this.options.columnSize;
/*      */   }
/*      */   
/*      */   public ResourceBundle getBundle() {
/* 1218 */     return this.options.bundle;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ParameterDescription> getParameters() {
/* 1227 */     return new ArrayList<>(this.fields.values());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ParameterDescription getMainParameterValue() {
/* 1234 */     return this.mainParameter.description;
/*      */   }
/*      */   
/*      */   private void p(String string) {
/* 1238 */     if (this.options.verbose > 0 || System.getProperty("jcommander.debug") != null) {
/* 1239 */       getConsole().println("[JCommander] " + string);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDefaultProvider(IDefaultProvider defaultProvider) {
/* 1247 */     this.options.defaultProvider = defaultProvider;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addConverterFactory(final IStringConverterFactory converterFactory) {
/* 1255 */     addConverterInstanceFactory(new IStringConverterInstanceFactory()
/*      */         {
/*      */           public IStringConverter<?> getConverterInstance(Parameter parameter, Class<?> forType, String optionName)
/*      */           {
/* 1259 */             Class<? extends IStringConverter<?>> converterClass = converterFactory.getConverter(forType);
/*      */             try {
/* 1261 */               if (optionName == null) {
/* 1262 */                 optionName = ((parameter.names()).length > 0) ? parameter.names()[0] : "[Main class]";
/*      */               }
/* 1264 */               return (converterClass != null) ? (IStringConverter)JCommander.instantiateConverter(optionName, (Class)converterClass) : null;
/* 1265 */             } catch (InstantiationException|IllegalAccessException|InvocationTargetException e) {
/* 1266 */               throw new ParameterException(e);
/*      */             } 
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addConverterInstanceFactory(IStringConverterInstanceFactory converterInstanceFactory) {
/* 1277 */     this.options.converterInstanceFactories.add(0, converterInstanceFactory);
/*      */   }
/*      */   
/*      */   private IStringConverter<?> findConverterInstance(Parameter parameter, Class<?> forType, String optionName) {
/* 1281 */     for (IStringConverterInstanceFactory f : this.options.converterInstanceFactories) {
/* 1282 */       IStringConverter<?> result = f.getConverterInstance(parameter, forType, optionName);
/* 1283 */       if (result != null) return result;
/*      */     
/*      */     } 
/* 1286 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public Object convertValue(final Parameterized parameterized, Class<?> type, String optionName, String value) {
/*      */     DefaultListConverter defaultListConverter;
/*      */     IStringConverter<?> iStringConverter1;
/*      */     EnumConverter enumConverter;
/*      */     StringConverter stringConverter;
/* 1295 */     Parameter annotation = parameterized.getParameter();
/*      */ 
/*      */     
/* 1298 */     if (annotation == null) return value;
/*      */     
/* 1300 */     if (optionName == null) {
/* 1301 */       optionName = ((annotation.names()).length > 0) ? annotation.names()[0] : "[Main class]";
/*      */     }
/*      */     
/* 1304 */     IStringConverter<?> converter = null;
/* 1305 */     if (type.isAssignableFrom(List.class))
/*      */     {
/* 1307 */       converter = tryInstantiateConverter(optionName, (Class)annotation.listConverter());
/*      */     }
/* 1309 */     if (type.isAssignableFrom(List.class) && converter == null) {
/*      */       
/* 1311 */       IParameterSplitter splitter = tryInstantiateConverter(null, (Class)annotation.splitter());
/* 1312 */       defaultListConverter = new DefaultListConverter(splitter, new IStringConverter()
/*      */           {
/*      */             public Object convert(String value) {
/* 1315 */               Type genericType = parameterized.findFieldGenericType();
/* 1316 */               return JCommander.this.convertValue(parameterized, (genericType instanceof Class) ? (Class)genericType : String.class, null, value);
/*      */             }
/*      */           });
/*      */     } 
/*      */     
/* 1321 */     if (defaultListConverter == null) {
/* 1322 */       iStringConverter1 = tryInstantiateConverter(optionName, (Class)annotation.converter());
/*      */     }
/* 1324 */     if (iStringConverter1 == null) {
/* 1325 */       iStringConverter1 = findConverterInstance(annotation, type, optionName);
/*      */     }
/* 1327 */     if (iStringConverter1 == null && type.isEnum()) {
/* 1328 */       enumConverter = new EnumConverter(optionName, type);
/*      */     }
/* 1330 */     if (enumConverter == null) {
/* 1331 */       stringConverter = new StringConverter();
/*      */     }
/* 1333 */     return stringConverter.convert(value);
/*      */   }
/*      */   
/*      */   private static <T> T tryInstantiateConverter(String optionName, Class<T> converterClass) {
/* 1337 */     if (converterClass == NoConverter.class || converterClass == null) {
/* 1338 */       return null;
/*      */     }
/*      */     try {
/* 1341 */       return instantiateConverter(optionName, converterClass);
/* 1342 */     } catch (InstantiationException|IllegalAccessException|InvocationTargetException ignore) {
/* 1343 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static <T> T instantiateConverter(String optionName, Class<? extends T> converterClass) throws InstantiationException, IllegalAccessException, InvocationTargetException {
/* 1350 */     Constructor<T> ctor = null;
/* 1351 */     Constructor<T> stringCtor = null;
/* 1352 */     for (Constructor<T> c : (Constructor[])converterClass.getDeclaredConstructors()) {
/* 1353 */       c.setAccessible(true);
/* 1354 */       Class<?>[] types = c.getParameterTypes();
/* 1355 */       if (types.length == 1 && types[0].equals(String.class)) {
/* 1356 */         stringCtor = c;
/* 1357 */       } else if (types.length == 0) {
/* 1358 */         ctor = c;
/*      */       } 
/*      */     } 
/*      */     
/* 1362 */     return (stringCtor != null) ? stringCtor
/* 1363 */       .newInstance(new Object[] { optionName }) : ((ctor != null) ? ctor
/*      */       
/* 1365 */       .newInstance(new Object[0]) : null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addCommand(String name, Object object) {
/* 1373 */     addCommand(name, object, new String[0]);
/*      */   }
/*      */   
/*      */   public void addCommand(Object object) {
/* 1377 */     Parameters p = object.getClass().<Parameters>getAnnotation(Parameters.class);
/* 1378 */     if (p != null && (p.commandNames()).length > 0) {
/* 1379 */       for (String commandName : p.commandNames()) {
/* 1380 */         addCommand(commandName, object);
/*      */       }
/*      */     } else {
/* 1383 */       throw new ParameterException("Trying to add command " + object.getClass().getName() + " without specifying its names in @Parameters");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addCommand(String name, Object object, String... aliases) {
/* 1392 */     JCommander jc = new JCommander(this.options);
/* 1393 */     jc.addObject(object);
/* 1394 */     jc.createDescriptions();
/* 1395 */     jc.setProgramName(name, aliases);
/* 1396 */     ProgramName progName = jc.programName;
/* 1397 */     this.commands.put(progName, jc);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1406 */     this.aliasMap.put(new StringKey(name), progName);
/* 1407 */     for (String a : aliases) {
/* 1408 */       FuzzyMap.IKey alias = new StringKey(a);
/*      */       
/* 1410 */       if (!alias.equals(name)) {
/* 1411 */         ProgramName mappedName = this.aliasMap.get(alias);
/* 1412 */         if (mappedName != null && !mappedName.equals(progName)) {
/* 1413 */           throw new ParameterException("Cannot set alias " + alias + " for " + name + " command because it has already been defined for " + mappedName
/*      */ 
/*      */               
/* 1416 */               .name + " command");
/*      */         }
/* 1418 */         this.aliasMap.put(alias, progName);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public Map<String, JCommander> getCommands() {
/* 1424 */     Map<String, JCommander> res = Maps.newLinkedHashMap();
/*      */     
/* 1426 */     for (Map.Entry<ProgramName, JCommander> entry : this.commands.entrySet()) {
/* 1427 */       res.put((entry.getKey()).name, entry.getValue());
/*      */     }
/* 1429 */     return res;
/*      */   }
/*      */   
/*      */   public Map<ProgramName, JCommander> getRawCommands() {
/* 1433 */     Map<ProgramName, JCommander> res = Maps.newLinkedHashMap();
/*      */     
/* 1435 */     for (Map.Entry<ProgramName, JCommander> entry : this.commands.entrySet()) {
/* 1436 */       res.put(entry.getKey(), entry.getValue());
/*      */     }
/* 1438 */     return res;
/*      */   }
/*      */   
/*      */   public String getParsedCommand() {
/* 1442 */     return this.parsedCommand;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getParsedAlias() {
/* 1453 */     return this.parsedAlias;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String s(int count) {
/* 1460 */     StringBuilder result = new StringBuilder();
/* 1461 */     for (int i = 0; i < count; i++) {
/* 1462 */       result.append(" ");
/*      */     }
/*      */     
/* 1465 */     return result.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Object> getObjects() {
/* 1473 */     return this.objects;
/*      */   }
/*      */   
/*      */   private ParameterDescription findParameterDescription(String arg) {
/* 1477 */     return FuzzyMap.<ParameterDescription>findInMap(this.descriptions, new StringKey(arg), this.options
/* 1478 */         .caseSensitiveOptions, this.options.allowAbbreviatedOptions);
/*      */   }
/*      */   
/*      */   private JCommander findCommand(ProgramName name) {
/* 1482 */     return FuzzyMap.<JCommander>findInMap((Map)this.commands, name, this.options
/* 1483 */         .caseSensitiveOptions, this.options.allowAbbreviatedOptions);
/*      */   }
/*      */   
/*      */   private ProgramName findProgramName(String name) {
/* 1487 */     return FuzzyMap.<ProgramName>findInMap(this.aliasMap, new StringKey(name), this.options
/* 1488 */         .caseSensitiveOptions, this.options.allowAbbreviatedOptions);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JCommander findCommandByAlias(String commandOrAlias) {
/* 1495 */     ProgramName progName = findProgramName(commandOrAlias);
/* 1496 */     if (progName == null) {
/* 1497 */       return null;
/*      */     }
/* 1499 */     JCommander jc = findCommand(progName);
/* 1500 */     if (jc == null) {
/* 1501 */       throw new IllegalStateException("There appears to be inconsistency in the internal command database.  This is likely a bug. Please report.");
/*      */     }
/*      */ 
/*      */     
/* 1505 */     return jc;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final class ProgramName
/*      */     implements FuzzyMap.IKey
/*      */   {
/*      */     private final String name;
/*      */     private final List<String> aliases;
/*      */     
/*      */     ProgramName(String name, List<String> aliases) {
/* 1516 */       this.name = name;
/* 1517 */       this.aliases = aliases;
/*      */     }
/*      */ 
/*      */     
/*      */     public String getName() {
/* 1522 */       return this.name;
/*      */     }
/*      */     
/*      */     public String getDisplayName() {
/* 1526 */       StringBuilder sb = new StringBuilder();
/* 1527 */       sb.append(this.name);
/* 1528 */       if (!this.aliases.isEmpty()) {
/* 1529 */         sb.append("(");
/* 1530 */         Iterator<String> aliasesIt = this.aliases.iterator();
/* 1531 */         while (aliasesIt.hasNext()) {
/* 1532 */           sb.append(aliasesIt.next());
/* 1533 */           if (aliasesIt.hasNext()) {
/* 1534 */             sb.append(",");
/*      */           }
/*      */         } 
/* 1537 */         sb.append(")");
/*      */       } 
/* 1539 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1544 */       int prime = 31;
/* 1545 */       int result = 1;
/* 1546 */       result = 31 * result + ((this.name == null) ? 0 : this.name.hashCode());
/* 1547 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/* 1552 */       if (this == obj)
/* 1553 */         return true; 
/* 1554 */       if (obj == null)
/* 1555 */         return false; 
/* 1556 */       if (getClass() != obj.getClass())
/* 1557 */         return false; 
/* 1558 */       ProgramName other = (ProgramName)obj;
/* 1559 */       if (this.name == null) {
/* 1560 */         if (other.name != null)
/* 1561 */           return false; 
/* 1562 */       } else if (!this.name.equals(other.name)) {
/* 1563 */         return false;
/* 1564 */       }  return true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1573 */       return getDisplayName();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void setVerbose(int verbose) {
/* 1579 */     this.options.verbose = verbose;
/*      */   }
/*      */   
/*      */   public void setCaseSensitiveOptions(boolean b) {
/* 1583 */     this.options.caseSensitiveOptions = b;
/*      */   }
/*      */   
/*      */   public void setAllowAbbreviatedOptions(boolean b) {
/* 1587 */     this.options.allowAbbreviatedOptions = b;
/*      */   }
/*      */   
/*      */   public void setAcceptUnknownOptions(boolean b) {
/* 1591 */     this.options.acceptUnknownOptions = b;
/*      */   }
/*      */   
/*      */   public List<String> getUnknownOptions() {
/* 1595 */     return this.unknownArgs;
/*      */   }
/*      */   
/*      */   public void setAllowParameterOverwriting(boolean b) {
/* 1599 */     this.options.allowParameterOverwriting = b;
/*      */   }
/*      */   
/*      */   public boolean isParameterOverwritingAllowed() {
/* 1603 */     return this.options.allowParameterOverwriting;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAtFileCharset(Charset charset) {
/* 1611 */     this.options.atFileCharset = charset;
/*      */   }
/*      */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\JCommander.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */