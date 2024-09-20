/*     */ package org.apache.commons.lang3.arch;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Processor
/*     */ {
/*     */   private final Arch arch;
/*     */   private final Type type;
/*     */   
/*     */   public enum Arch
/*     */   {
/*  43 */     BIT_32("32-bit"),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  48 */     BIT_64("64-bit"),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  53 */     UNKNOWN("Unknown");
/*     */ 
/*     */     
/*     */     private final String label;
/*     */ 
/*     */ 
/*     */     
/*     */     Arch(String label) {
/*  61 */       this.label = label;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getLabel() {
/*  70 */       return this.label;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Type
/*     */   {
/*  92 */     AARCH_64("AArch64"),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  97 */     X86("x86"),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     IA_64("IA-64"),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 107 */     PPC("PPC"),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 112 */     UNKNOWN("Unknown");
/*     */ 
/*     */     
/*     */     private final String label;
/*     */ 
/*     */ 
/*     */     
/*     */     Type(String label) {
/* 120 */       this.label = label;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getLabel() {
/* 130 */       return this.label;
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
/*     */   public Processor(Arch arch, Type type) {
/* 146 */     this.arch = arch;
/* 147 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Arch getArch() {
/* 158 */     return this.arch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getType() {
/* 169 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean is32Bit() {
/* 178 */     return (Arch.BIT_32 == this.arch);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean is64Bit() {
/* 187 */     return (Arch.BIT_64 == this.arch);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAarch64() {
/* 198 */     return (Type.AARCH_64 == this.type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIA64() {
/* 207 */     return (Type.IA_64 == this.type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPPC() {
/* 216 */     return (Type.PPC == this.type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isX86() {
/* 225 */     return (Type.X86 == this.type);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 230 */     StringBuilder builder = new StringBuilder();
/* 231 */     builder.append(this.type.getLabel()).append(' ').append(this.arch.getLabel());
/* 232 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\arch\Processor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */