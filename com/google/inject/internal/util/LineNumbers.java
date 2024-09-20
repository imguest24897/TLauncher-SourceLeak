/*     */ package com.google.inject.internal.util;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.inject.internal.asm.;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class LineNumbers
/*     */ {
/*     */   private static final int ASM_API_LEVEL = 589824;
/*     */   private final Class<?> type;
/*  48 */   private final Map<String, Integer> lines = Maps.newHashMap();
/*     */   private String source;
/*  50 */   private int firstLine = Integer.MAX_VALUE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LineNumbers(Class<?> type) throws IOException {
/*  58 */     this.type = type;
/*     */     
/*  60 */     if (!type.isArray()) {
/*  61 */       InputStream in = null;
/*     */       try {
/*  63 */         String str = type.getName().replace('.', '/'); in = type.getResourceAsStream((new StringBuilder(7 + String.valueOf(str).length())).append("/").append(str).append(".class").toString());
/*  64 */       } catch (IllegalStateException illegalStateException) {}
/*     */ 
/*     */       
/*  67 */       if (in != null) {
/*     */         
/*  69 */         try { (new .ClassReader(in)).accept(new LineNumberReader(), 4); }
/*  70 */         catch (UnsupportedOperationException unsupportedOperationException)
/*     */         
/*     */         { 
/*     */           try {
/*     */ 
/*     */             
/*  76 */             in.close();
/*  77 */           } catch (IOException iOException) {} } finally { try { in.close(); } catch (IOException iOException) {} }
/*     */       
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSource() {
/*  90 */     return this.source;
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
/*     */   public Integer getLineNumber(Member member) {
/* 102 */     Preconditions.checkArgument(
/* 103 */         (this.type == member.getDeclaringClass()), "Member %s belongs to %s, not %s", member, member
/*     */ 
/*     */         
/* 106 */         .getDeclaringClass(), this.type);
/*     */     
/* 108 */     return this.lines.get(memberKey(member));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFirstLine() {
/* 113 */     return (this.firstLine == Integer.MAX_VALUE) ? 1 : this.firstLine;
/*     */   }
/*     */   
/*     */   private String memberKey(Member member) {
/* 117 */     Preconditions.checkNotNull(member, "member");
/* 118 */     if (member instanceof java.lang.reflect.Field)
/* 119 */       return member.getName(); 
/* 120 */     if (member instanceof Method) {
/* 121 */       String.valueOf(.Type.getMethodDescriptor((Method)member)); return (String.valueOf(.Type.getMethodDescriptor((Method)member)).length() != 0) ? String.valueOf(member.getName()).concat(String.valueOf(.Type.getMethodDescriptor((Method)member))) : new String(String.valueOf(member.getName()));
/*     */     } 
/* 123 */     if (member instanceof Constructor) {
/* 124 */       StringBuilder sb = (new StringBuilder()).append("<init>(");
/* 125 */       for (Class<?> param : ((Constructor)member).getParameterTypes()) {
/* 126 */         sb.append(.Type.getDescriptor(param));
/*     */       }
/* 128 */       return sb.append(")V").toString();
/*     */     } 
/*     */     
/* 131 */     String str = String.valueOf(member.getClass()); throw new IllegalArgumentException((new StringBuilder(45 + String.valueOf(str).length())).append("Unsupported implementation class for Member, ").append(str).toString());
/*     */   }
/*     */   
/*     */   private class LineNumberReader
/*     */     extends .ClassVisitor
/*     */   {
/* 137 */     private int line = -1;
/*     */     private String pendingMethod;
/*     */     private String name;
/*     */     
/*     */     LineNumberReader() {
/* 142 */       super(589824);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
/* 153 */       this.name = name;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public .MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
/* 159 */       if ((access & 0x2) != 0) {
/* 160 */         return null;
/*     */       }
/* 162 */       String.valueOf(desc); this.pendingMethod = (String.valueOf(desc).length() != 0) ? String.valueOf(name).concat(String.valueOf(desc)) : new String(String.valueOf(name));
/* 163 */       this.line = -1;
/* 164 */       return new LineNumberMethodVisitor();
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitSource(String source, String debug) {
/* 169 */       LineNumbers.this.source = source;
/*     */     }
/*     */     
/*     */     public void visitLineNumber(int line, .Label start) {
/* 173 */       if (line < LineNumbers.this.firstLine) {
/* 174 */         LineNumbers.this.firstLine = line;
/*     */       }
/*     */       
/* 177 */       this.line = line;
/* 178 */       if (this.pendingMethod != null) {
/* 179 */         LineNumbers.this.lines.put(this.pendingMethod, Integer.valueOf(line));
/* 180 */         this.pendingMethod = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public .FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
/* 187 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public .AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/* 192 */       return new LineNumberAnnotationVisitor(this);
/*     */     }
/*     */     
/*     */     public .AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
/* 196 */       return new LineNumberAnnotationVisitor(this);
/*     */     }
/*     */     
/*     */     class LineNumberMethodVisitor extends .MethodVisitor {
/*     */       LineNumberMethodVisitor() {
/* 201 */         super(589824);
/*     */       }
/*     */ 
/*     */       
/*     */       public .AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/* 206 */         return new LineNumbers.LineNumberReader.LineNumberAnnotationVisitor(LineNumbers.LineNumberReader.this);
/*     */       }
/*     */ 
/*     */       
/*     */       public .AnnotationVisitor visitAnnotationDefault() {
/* 211 */         return new LineNumbers.LineNumberReader.LineNumberAnnotationVisitor(LineNumbers.LineNumberReader.this);
/*     */       }
/*     */ 
/*     */       
/*     */       public void visitFieldInsn(int opcode, String owner, String name, String desc) {
/* 216 */         if (opcode == 181 && LineNumbers.LineNumberReader.this
/* 217 */           .name.equals(owner) && 
/* 218 */           !LineNumbers.this.lines.containsKey(name) && LineNumbers.LineNumberReader.this
/* 219 */           .line != -1) {
/* 220 */           LineNumbers.this.lines.put(name, Integer.valueOf(LineNumbers.LineNumberReader.this.line));
/*     */         }
/*     */       }
/*     */ 
/*     */       
/*     */       public void visitLineNumber(int line, .Label start) {
/* 226 */         LineNumbers.LineNumberReader.this.visitLineNumber(line, start);
/*     */       }
/*     */     }
/*     */     
/*     */     class LineNumberAnnotationVisitor extends .AnnotationVisitor {
/*     */       LineNumberAnnotationVisitor(LineNumbers.LineNumberReader this$1) {
/* 232 */         super(589824);
/*     */       }
/*     */ 
/*     */       
/*     */       public .AnnotationVisitor visitAnnotation(String name, String desc) {
/* 237 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public .AnnotationVisitor visitArray(String name) {
/* 242 */         return this;
/*     */       }
/*     */       
/*     */       public void visitLocalVariable(String name, String desc, String signature, .Label start, .Label end, int index) {}
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\interna\\util\LineNumbers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */