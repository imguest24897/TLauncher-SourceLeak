/*     */ package com.google.inject.internal.aop;
/*     */ 
/*     */ import com.google.inject.internal.asm.;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class BytecodeTasks
/*     */ {
/*     */   public static void pushInteger(.MethodVisitor mv, int value) {
/*  45 */     if (value < -1) {
/*  46 */       mv.visitLdcInsn(Integer.valueOf(value));
/*  47 */     } else if (value <= 5) {
/*  48 */       mv.visitInsn(3 + value);
/*  49 */     } else if (value <= 127) {
/*  50 */       mv.visitIntInsn(16, value);
/*  51 */     } else if (value <= 32767) {
/*  52 */       mv.visitIntInsn(17, value);
/*     */     } else {
/*  54 */       mv.visitLdcInsn(Integer.valueOf(value));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void packArguments(.MethodVisitor mv, Class<?>[] parameterTypes) {
/*  60 */     pushInteger(mv, parameterTypes.length);
/*  61 */     mv.visitTypeInsn(189, "java/lang/Object");
/*  62 */     int parameterIndex = 0;
/*  63 */     int slot = 1;
/*  64 */     for (Class<?> parameterType : parameterTypes) {
/*  65 */       mv.visitInsn(89);
/*  66 */       pushInteger(mv, parameterIndex++);
/*  67 */       slot += loadArgument(mv, parameterType, slot);
/*  68 */       if (parameterType.isPrimitive()) {
/*  69 */         box(mv, .Type.getType(parameterType));
/*     */       }
/*  71 */       mv.visitInsn(83);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void unpackArguments(.MethodVisitor mv, Class<?>[] parameterTypes) {
/*  77 */     int parameterIndex = 0;
/*  78 */     for (Class<?> parameterType : parameterTypes) {
/*     */       
/*  80 */       mv.visitVarInsn(25, 2);
/*  81 */       pushInteger(mv, parameterIndex++);
/*  82 */       mv.visitInsn(50);
/*  83 */       if (parameterType.isPrimitive()) {
/*  84 */         unbox(mv, .Type.getType(parameterType));
/*     */       } else {
/*  86 */         mv.visitTypeInsn(192, .Type.getInternalName(parameterType));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static int loadArgument(.MethodVisitor mv, Class<?> parameterType, int slot) {
/*  93 */     if (!parameterType.isPrimitive()) {
/*  94 */       mv.visitVarInsn(25, slot);
/*  95 */       return 1;
/*     */     } 
/*     */     
/*  98 */     .Type primitiveType = .Type.getType(parameterType);
/*  99 */     mv.visitVarInsn(primitiveType.getOpcode(21), slot);
/* 100 */     return primitiveType.getSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void box(.MethodVisitor mv, .Type primitiveType) {
/*     */     String wrapper, descriptor;
/* 108 */     switch (primitiveType.getSort()) {
/*     */       case 1:
/* 110 */         wrapper = "java/lang/Boolean";
/* 111 */         descriptor = "(Z)Ljava/lang/Boolean;";
/*     */         break;
/*     */       case 2:
/* 114 */         wrapper = "java/lang/Character";
/* 115 */         descriptor = "(C)Ljava/lang/Character;";
/*     */         break;
/*     */       case 3:
/* 118 */         wrapper = "java/lang/Byte";
/* 119 */         descriptor = "(B)Ljava/lang/Byte;";
/*     */         break;
/*     */       case 4:
/* 122 */         wrapper = "java/lang/Short";
/* 123 */         descriptor = "(S)Ljava/lang/Short;";
/*     */         break;
/*     */       case 5:
/* 126 */         wrapper = "java/lang/Integer";
/* 127 */         descriptor = "(I)Ljava/lang/Integer;";
/*     */         break;
/*     */       case 6:
/* 130 */         wrapper = "java/lang/Float";
/* 131 */         descriptor = "(F)Ljava/lang/Float;";
/*     */         break;
/*     */       case 7:
/* 134 */         wrapper = "java/lang/Long";
/* 135 */         descriptor = "(J)Ljava/lang/Long;";
/*     */         break;
/*     */       case 8:
/* 138 */         wrapper = "java/lang/Double";
/* 139 */         descriptor = "(D)Ljava/lang/Double;";
/*     */         break;
/*     */       
/*     */       default:
/*     */         return;
/*     */     } 
/* 145 */     mv.visitMethodInsn(184, wrapper, "valueOf", descriptor, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void unbox(.MethodVisitor mv, .Type primitiveType) {
/*     */     String wrapper, method, descriptor;
/* 154 */     switch (primitiveType.getSort()) {
/*     */       case 1:
/* 156 */         wrapper = "java/lang/Boolean";
/* 157 */         method = "booleanValue";
/* 158 */         descriptor = "()Z";
/*     */         break;
/*     */       case 2:
/* 161 */         wrapper = "java/lang/Character";
/* 162 */         method = "charValue";
/* 163 */         descriptor = "()C";
/*     */         break;
/*     */       case 3:
/* 166 */         wrapper = "java/lang/Byte";
/* 167 */         method = "byteValue";
/* 168 */         descriptor = "()B";
/*     */         break;
/*     */       case 4:
/* 171 */         wrapper = "java/lang/Short";
/* 172 */         method = "shortValue";
/* 173 */         descriptor = "()S";
/*     */         break;
/*     */       case 5:
/* 176 */         wrapper = "java/lang/Integer";
/* 177 */         method = "intValue";
/* 178 */         descriptor = "()I";
/*     */         break;
/*     */       case 6:
/* 181 */         wrapper = "java/lang/Float";
/* 182 */         method = "floatValue";
/* 183 */         descriptor = "()F";
/*     */         break;
/*     */       case 7:
/* 186 */         wrapper = "java/lang/Long";
/* 187 */         method = "longValue";
/* 188 */         descriptor = "()J";
/*     */         break;
/*     */       case 8:
/* 191 */         wrapper = "java/lang/Double";
/* 192 */         method = "doubleValue";
/* 193 */         descriptor = "()D";
/*     */         break;
/*     */       
/*     */       default:
/*     */         return;
/*     */     } 
/* 199 */     mv.visitTypeInsn(192, wrapper);
/* 200 */     mv.visitMethodInsn(182, wrapper, method, descriptor, false);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\google\inject\internal\aop\BytecodeTasks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */