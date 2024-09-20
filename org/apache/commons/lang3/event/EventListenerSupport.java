/*     */ package org.apache.commons.lang3.event;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EventListenerSupport<L>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 3593265990380473632L;
/*  80 */   private List<L> listeners = new CopyOnWriteArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient L proxy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient L[] prototypeArray;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> EventListenerSupport<T> create(Class<T> listenerInterface) {
/* 110 */     return new EventListenerSupport<>(listenerInterface);
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
/*     */   public EventListenerSupport(Class<L> listenerInterface) {
/* 126 */     this(listenerInterface, Thread.currentThread().getContextClassLoader());
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
/*     */   public EventListenerSupport(Class<L> listenerInterface, ClassLoader classLoader) {
/* 143 */     this();
/* 144 */     Objects.requireNonNull(listenerInterface, "listenerInterface");
/* 145 */     Objects.requireNonNull(classLoader, "classLoader");
/* 146 */     Validate.isTrue(listenerInterface.isInterface(), "Class %s is not an interface", new Object[] { listenerInterface
/* 147 */           .getName() });
/* 148 */     initializeTransientFields(listenerInterface, classLoader);
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
/*     */   public L fire() {
/* 167 */     return this.proxy;
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
/*     */   public void addListener(L listener) {
/* 183 */     addListener(listener, true);
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
/*     */   public void addListener(L listener, boolean allowDuplicate) {
/* 198 */     Objects.requireNonNull(listener, "listener");
/* 199 */     if (allowDuplicate || !this.listeners.contains(listener)) {
/* 200 */       this.listeners.add(listener);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getListenerCount() {
/* 210 */     return this.listeners.size();
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
/*     */   public void removeListener(L listener) {
/* 222 */     Objects.requireNonNull(listener, "listener");
/* 223 */     this.listeners.remove(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public L[] getListeners() {
/* 233 */     return this.listeners.toArray(this.prototypeArray);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
/* 242 */     ArrayList<L> serializableListeners = new ArrayList<>();
/*     */ 
/*     */     
/* 245 */     ObjectOutputStream testObjectOutputStream = new ObjectOutputStream(new ByteArrayOutputStream());
/* 246 */     for (L listener : this.listeners) {
/*     */       try {
/* 248 */         testObjectOutputStream.writeObject(listener);
/* 249 */         serializableListeners.add(listener);
/* 250 */       } catch (IOException exception) {
/*     */         
/* 252 */         testObjectOutputStream = new ObjectOutputStream(new ByteArrayOutputStream());
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 259 */     objectOutputStream.writeObject(serializableListeners.toArray(this.prototypeArray));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
/* 270 */     L[] srcListeners = (L[])objectInputStream.readObject();
/*     */     
/* 272 */     this.listeners = new CopyOnWriteArrayList<>(srcListeners);
/*     */     
/* 274 */     Class<L> listenerInterface = ArrayUtils.getComponentType((Object[])srcListeners);
/*     */     
/* 276 */     initializeTransientFields(listenerInterface, Thread.currentThread().getContextClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initializeTransientFields(Class<L> listenerInterface, ClassLoader classLoader) {
/* 286 */     this.prototypeArray = (L[])ArrayUtils.newInstance(listenerInterface, 0);
/* 287 */     createProxy(listenerInterface, classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void createProxy(Class<L> listenerInterface, ClassLoader classLoader) {
/* 296 */     this.proxy = listenerInterface.cast(Proxy.newProxyInstance(classLoader, new Class[] { listenerInterface
/* 297 */           }, createInvocationHandler()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected InvocationHandler createInvocationHandler() {
/* 306 */     return new ProxyInvocationHandler();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private EventListenerSupport() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class ProxyInvocationHandler
/*     */     implements InvocationHandler
/*     */   {
/*     */     public Object invoke(Object unusedProxy, Method method, Object[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
/* 328 */       for (L listener : EventListenerSupport.this.listeners) {
/* 329 */         method.invoke(listener, args);
/*     */       }
/* 331 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\event\EventListenerSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */