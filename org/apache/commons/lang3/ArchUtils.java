/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.lang3.arch.Processor;
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
/*     */ public class ArchUtils
/*     */ {
/*  38 */   private static final Map<String, Processor> ARCH_TO_PROCESSOR = new HashMap<>(); static {
/*  39 */     init();
/*     */   }
/*     */   
/*     */   private static void init() {
/*  43 */     init_X86_32Bit();
/*  44 */     init_X86_64Bit();
/*  45 */     init_IA64_32Bit();
/*  46 */     init_IA64_64Bit();
/*  47 */     init_PPC_32Bit();
/*  48 */     init_PPC_64Bit();
/*  49 */     init_Aarch_64Bit();
/*     */   }
/*     */   
/*     */   private static void init_Aarch_64Bit() {
/*  53 */     addProcessors(new Processor(Processor.Arch.BIT_64, Processor.Type.AARCH_64), new String[] { "aarch64" });
/*     */   }
/*     */   
/*     */   private static void init_X86_32Bit() {
/*  57 */     addProcessors(new Processor(Processor.Arch.BIT_32, Processor.Type.X86), new String[] { "x86", "i386", "i486", "i586", "i686", "pentium" });
/*     */   }
/*     */   
/*     */   private static void init_X86_64Bit() {
/*  61 */     addProcessors(new Processor(Processor.Arch.BIT_64, Processor.Type.X86), new String[] { "x86_64", "amd64", "em64t", "universal" });
/*     */   }
/*     */   
/*     */   private static void init_IA64_32Bit() {
/*  65 */     addProcessors(new Processor(Processor.Arch.BIT_32, Processor.Type.IA_64), new String[] { "ia64_32", "ia64n" });
/*     */   }
/*     */   
/*     */   private static void init_IA64_64Bit() {
/*  69 */     addProcessors(new Processor(Processor.Arch.BIT_64, Processor.Type.IA_64), new String[] { "ia64", "ia64w" });
/*     */   }
/*     */   
/*     */   private static void init_PPC_32Bit() {
/*  73 */     addProcessors(new Processor(Processor.Arch.BIT_32, Processor.Type.PPC), new String[] { "ppc", "power", "powerpc", "power_pc", "power_rs" });
/*     */   }
/*     */   
/*     */   private static void init_PPC_64Bit() {
/*  77 */     addProcessors(new Processor(Processor.Arch.BIT_64, Processor.Type.PPC), new String[] { "ppc64", "power64", "powerpc64", "power_pc64", "power_rs64" });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void addProcessor(String key, Processor processor) {
/*  88 */     if (ARCH_TO_PROCESSOR.containsKey(key)) {
/*  89 */       throw new IllegalStateException("Key " + key + " already exists in processor map");
/*     */     }
/*  91 */     ARCH_TO_PROCESSOR.put(key, processor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void addProcessors(Processor processor, String... keys) {
/* 102 */     Streams.of((Object[])keys).forEach(e -> addProcessor(e, processor));
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
/*     */   public static Processor getProcessor() {
/* 115 */     return getProcessor(SystemProperties.getOsArch());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Processor getProcessor(String value) {
/* 126 */     return ARCH_TO_PROCESSOR.get(value);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\ArchUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */