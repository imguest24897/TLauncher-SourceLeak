/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectStreamClass;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SerializationUtils
/*     */ {
/*     */   static class ClassLoaderAwareObjectInputStream
/*     */     extends ObjectInputStream
/*     */   {
/*  64 */     private static final Map<String, Class<?>> primitiveTypes = new HashMap<>();
/*     */     private final ClassLoader classLoader;
/*     */     
/*     */     static {
/*  68 */       primitiveTypes.put("byte", byte.class);
/*  69 */       primitiveTypes.put("short", short.class);
/*  70 */       primitiveTypes.put("int", int.class);
/*  71 */       primitiveTypes.put("long", long.class);
/*  72 */       primitiveTypes.put("float", float.class);
/*  73 */       primitiveTypes.put("double", double.class);
/*  74 */       primitiveTypes.put("boolean", boolean.class);
/*  75 */       primitiveTypes.put("char", char.class);
/*  76 */       primitiveTypes.put("void", void.class);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     ClassLoaderAwareObjectInputStream(InputStream in, ClassLoader classLoader) throws IOException {
/*  89 */       super(in);
/*  90 */       this.classLoader = classLoader;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
/* 103 */       String name = desc.getName();
/*     */       try {
/* 105 */         return Class.forName(name, false, this.classLoader);
/* 106 */       } catch (ClassNotFoundException ex) {
/*     */         try {
/* 108 */           return Class.forName(name, false, Thread.currentThread().getContextClassLoader());
/* 109 */         } catch (ClassNotFoundException cnfe) {
/* 110 */           Class<?> cls = primitiveTypes.get(name);
/* 111 */           if (cls != null) {
/* 112 */             return cls;
/*     */           }
/* 114 */           throw cnfe;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Serializable> T clone(T object) {
/* 136 */     if (object == null) {
/* 137 */       return null;
/*     */     }
/* 139 */     byte[] objectData = serialize((Serializable)object);
/* 140 */     ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
/*     */     
/* 142 */     Class<T> cls = ObjectUtils.getClass(object);
/* 143 */     try (ClassLoaderAwareObjectInputStream in = new ClassLoaderAwareObjectInputStream(bais, cls.getClassLoader())) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 148 */       return cls.cast(in.readObject());
/*     */     }
/* 150 */     catch (ClassNotFoundException|IOException ex) {
/* 151 */       throw new SerializationException(
/* 152 */           String.format("%s while reading cloned object data", new Object[] { ex.getClass().getSimpleName() }), ex);
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
/*     */   public static <T> T deserialize(byte[] objectData) {
/* 173 */     Objects.requireNonNull(objectData, "objectData");
/* 174 */     return deserialize(new ByteArrayInputStream(objectData));
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
/*     */   public static <T> T deserialize(InputStream inputStream) {
/* 205 */     Objects.requireNonNull(inputStream, "inputStream");
/* 206 */     try (ObjectInputStream in = new ObjectInputStream(inputStream)) {
/*     */       
/* 208 */       T obj = (T)in.readObject();
/* 209 */       return obj;
/* 210 */     } catch (ClassNotFoundException|IOException ex) {
/* 211 */       throw new SerializationException(ex);
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
/*     */   public static <T extends Serializable> T roundtrip(T obj) {
/* 228 */     return (T)deserialize(serialize((Serializable)obj));
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
/*     */   public static byte[] serialize(Serializable obj) {
/* 240 */     ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
/* 241 */     serialize(obj, baos);
/* 242 */     return baos.toByteArray();
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
/*     */   public static void serialize(Serializable obj, OutputStream outputStream) {
/* 262 */     Objects.requireNonNull(outputStream, "outputStream");
/* 263 */     try (ObjectOutputStream out = new ObjectOutputStream(outputStream)) {
/* 264 */       out.writeObject(obj);
/* 265 */     } catch (IOException ex) {
/* 266 */       throw new SerializationException(ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\SerializationUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */