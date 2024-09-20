/*     */ package ch.qos.logback.core.subst;
/*     */ 
/*     */ import ch.qos.logback.core.spi.PropertyContainer;
/*     */ import ch.qos.logback.core.spi.ScanException;
/*     */ import ch.qos.logback.core.util.OptionHelper;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NodeToStringTransformer
/*     */ {
/*     */   final Node node;
/*     */   final PropertyContainer propertyContainer0;
/*     */   final PropertyContainer propertyContainer1;
/*     */   
/*     */   public NodeToStringTransformer(Node node, PropertyContainer propertyContainer0, PropertyContainer propertyContainer1) {
/*  36 */     this.node = node;
/*  37 */     this.propertyContainer0 = propertyContainer0;
/*  38 */     this.propertyContainer1 = propertyContainer1;
/*     */   }
/*     */   
/*     */   public NodeToStringTransformer(Node node, PropertyContainer propertyContainer0) {
/*  42 */     this(node, propertyContainer0, null);
/*     */   }
/*     */   
/*     */   public static String substituteVariable(String input, PropertyContainer pc0, PropertyContainer pc1) throws ScanException {
/*  46 */     Node node = tokenizeAndParseString(input);
/*  47 */     NodeToStringTransformer nodeToStringTransformer = new NodeToStringTransformer(node, pc0, pc1);
/*  48 */     return nodeToStringTransformer.transform();
/*     */   }
/*     */   
/*     */   private static Node tokenizeAndParseString(String value) throws ScanException {
/*  52 */     Tokenizer tokenizer = new Tokenizer(value);
/*  53 */     List<Token> tokens = tokenizer.tokenize();
/*  54 */     Parser parser = new Parser(tokens);
/*  55 */     return parser.parse();
/*     */   }
/*     */   
/*     */   public String transform() throws ScanException {
/*  59 */     StringBuilder stringBuilder = new StringBuilder();
/*  60 */     compileNode(this.node, stringBuilder, new Stack<Node>());
/*  61 */     return stringBuilder.toString();
/*     */   }
/*     */   
/*     */   private void compileNode(Node inputNode, StringBuilder stringBuilder, Stack<Node> cycleCheckStack) throws ScanException {
/*  65 */     Node n = inputNode;
/*  66 */     while (n != null) {
/*  67 */       switch (n.type) {
/*     */         case LITERAL:
/*  69 */           handleLiteral(n, stringBuilder);
/*     */           break;
/*     */         case VARIABLE:
/*  72 */           handleVariable(n, stringBuilder, cycleCheckStack);
/*     */           break;
/*     */       } 
/*  75 */       n = n.next;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleVariable(Node n, StringBuilder stringBuilder, Stack<Node> cycleCheckStack) throws ScanException {
/*  82 */     if (haveVisitedNodeAlready(n, cycleCheckStack)) {
/*  83 */       cycleCheckStack.push(n);
/*  84 */       String error = constructRecursionErrorMessage(cycleCheckStack);
/*  85 */       throw new IllegalArgumentException(error);
/*     */     } 
/*  87 */     cycleCheckStack.push(n);
/*     */     
/*  89 */     StringBuilder keyBuffer = new StringBuilder();
/*  90 */     Node payload = (Node)n.payload;
/*  91 */     compileNode(payload, keyBuffer, cycleCheckStack);
/*  92 */     String key = keyBuffer.toString();
/*  93 */     String value = lookupKey(key);
/*     */     
/*  95 */     if (value != null) {
/*  96 */       Node innerNode = tokenizeAndParseString(value);
/*  97 */       compileNode(innerNode, stringBuilder, cycleCheckStack);
/*  98 */       cycleCheckStack.pop();
/*     */       
/*     */       return;
/*     */     } 
/* 102 */     if (n.defaultPart == null) {
/* 103 */       stringBuilder.append(key + "_IS_UNDEFINED");
/* 104 */       cycleCheckStack.pop();
/*     */       
/*     */       return;
/*     */     } 
/* 108 */     Node defaultPart = (Node)n.defaultPart;
/* 109 */     StringBuilder defaultPartBuffer = new StringBuilder();
/* 110 */     compileNode(defaultPart, defaultPartBuffer, cycleCheckStack);
/* 111 */     cycleCheckStack.pop();
/* 112 */     String defaultVal = defaultPartBuffer.toString();
/* 113 */     stringBuilder.append(defaultVal);
/*     */   }
/*     */   
/*     */   private String lookupKey(String key) {
/* 117 */     String value = this.propertyContainer0.getProperty(key);
/* 118 */     if (value != null) {
/* 119 */       return value;
/*     */     }
/* 121 */     if (this.propertyContainer1 != null) {
/* 122 */       value = this.propertyContainer1.getProperty(key);
/* 123 */       if (value != null) {
/* 124 */         return value;
/*     */       }
/*     */     } 
/* 127 */     value = OptionHelper.getSystemProperty(key, null);
/* 128 */     if (value != null) {
/* 129 */       return value;
/*     */     }
/* 131 */     value = OptionHelper.getEnv(key);
/* 132 */     if (value != null) {
/* 133 */       return value;
/*     */     }
/*     */     
/* 136 */     return null;
/*     */   }
/*     */   
/*     */   private void handleLiteral(Node n, StringBuilder stringBuilder) {
/* 140 */     stringBuilder.append((String)n.payload);
/*     */   }
/*     */   
/*     */   private String variableNodeValue(Node variableNode) {
/* 144 */     Node literalPayload = (Node)variableNode.payload;
/* 145 */     return (String)literalPayload.payload;
/*     */   }
/*     */   
/*     */   private String constructRecursionErrorMessage(Stack<Node> recursionNodes) {
/* 149 */     StringBuilder errorBuilder = new StringBuilder("Circular variable reference detected while parsing input [");
/*     */     
/* 151 */     for (Node stackNode : recursionNodes) {
/* 152 */       errorBuilder.append("${").append(variableNodeValue(stackNode)).append("}");
/* 153 */       if (recursionNodes.lastElement() != stackNode) {
/* 154 */         errorBuilder.append(" --> ");
/*     */       }
/*     */     } 
/* 157 */     errorBuilder.append("]");
/* 158 */     return errorBuilder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean haveVisitedNodeAlready(Node node, Stack<Node> cycleDetectionStack) {
/* 167 */     for (Node cycleNode : cycleDetectionStack) {
/* 168 */       if (equalNodes(node, cycleNode)) {
/* 169 */         return true;
/*     */       }
/*     */     } 
/* 172 */     return false;
/*     */   }
/*     */   
/*     */   private boolean equalNodes(Node node1, Node node2) {
/* 176 */     if (node1.type != null && !node1.type.equals(node2.type))
/* 177 */       return false; 
/* 178 */     if (node1.payload != null && !node1.payload.equals(node2.payload))
/* 179 */       return false; 
/* 180 */     if (node1.defaultPart != null && !node1.defaultPart.equals(node2.defaultPart)) {
/* 181 */       return false;
/*     */     }
/* 183 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\ch\qos\logback\core\subst\NodeToStringTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */