/*    */ package ch.qos.logback.core.pattern.parser;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Node
/*    */ {
/*    */   static final int LITERAL = 0;
/*    */   static final int SIMPLE_KEYWORD = 1;
/*    */   static final int COMPOSITE_KEYWORD = 2;
/*    */   final int type;
/*    */   final Object value;
/*    */   Node next;
/*    */   
/*    */   Node(int type) {
/* 26 */     this(type, null);
/*    */   }
/*    */   
/*    */   Node(int type, Object value) {
/* 30 */     this.type = type;
/* 31 */     this.value = value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getType() {
/* 38 */     return this.type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getValue() {
/* 45 */     return this.value;
/*    */   }
/*    */   
/*    */   public Node getNext() {
/* 49 */     return this.next;
/*    */   }
/*    */   
/*    */   public void setNext(Node next) {
/* 53 */     this.next = next;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o) {
/* 57 */     if (this == o) {
/* 58 */       return true;
/*    */     }
/* 60 */     if (!(o instanceof Node)) {
/* 61 */       return false;
/*    */     }
/* 63 */     Node r = (Node)o;
/*    */     
/* 65 */     return (this.type == r.type && ((this.value != null) ? this.value.equals(r.value) : (r.value == null)) && ((this.next != null) ? this.next.equals(r.next) : (r.next == null)));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 70 */     int result = this.type;
/* 71 */     result = 31 * result + ((this.value != null) ? this.value.hashCode() : 0);
/* 72 */     return result;
/*    */   }
/*    */   
/*    */   String printNext() {
/* 76 */     if (this.next != null) {
/* 77 */       return " -> " + this.next;
/*    */     }
/* 79 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 84 */     StringBuilder buf = new StringBuilder();
/* 85 */     switch (this.type)
/*    */     { case 0:
/* 87 */         buf.append("LITERAL(" + this.value + ")");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 93 */         buf.append(printNext());
/*    */         
/* 95 */         return buf.toString(); }  buf.append(super.toString()); buf.append(printNext()); return buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\ch\qos\logback\core\pattern\parser\Node.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */