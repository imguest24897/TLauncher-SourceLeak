/*     */ package org.apache.commons.text.diff;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StringsComparator
/*     */ {
/*     */   private final String left;
/*     */   private final String right;
/*     */   private final int[] vDown;
/*     */   private final int[] vUp;
/*     */   
/*     */   private static final class Snake
/*     */   {
/*     */     private final int start;
/*     */     private final int end;
/*     */     private final int diag;
/*     */     
/*     */     Snake(int start, int end, int diag) {
/*  78 */       this.start = start;
/*  79 */       this.end = end;
/*  80 */       this.diag = diag;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getDiag() {
/*  89 */       return this.diag;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getEnd() {
/*  98 */       return this.end;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getStart() {
/* 107 */       return this.start;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringsComparator(String left, String right) {
/* 142 */     this.left = left;
/* 143 */     this.right = right;
/*     */     
/* 145 */     int size = left.length() + right.length() + 2;
/* 146 */     this.vDown = new int[size];
/* 147 */     this.vUp = new int[size];
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
/*     */   private void buildScript(int start1, int end1, int start2, int end2, EditScript<Character> script) {
/* 161 */     Snake middle = getMiddleSnake(start1, end1, start2, end2);
/*     */     
/* 163 */     if (middle == null || (middle
/* 164 */       .getStart() == end1 && middle.getDiag() == end1 - end2) || (middle
/* 165 */       .getEnd() == start1 && middle.getDiag() == start1 - start2)) {
/*     */       
/* 167 */       int i = start1;
/* 168 */       int j = start2;
/* 169 */       while (i < end1 || j < end2) {
/* 170 */         if (i < end1 && j < end2 && this.left.charAt(i) == this.right.charAt(j)) {
/* 171 */           script.append(new KeepCommand<>(Character.valueOf(this.left.charAt(i))));
/* 172 */           i++;
/* 173 */           j++; continue;
/* 174 */         }  if (end1 - start1 > end2 - start2) {
/* 175 */           script.append(new DeleteCommand<>(Character.valueOf(this.left.charAt(i))));
/* 176 */           i++; continue;
/*     */         } 
/* 178 */         script.append(new InsertCommand<>(Character.valueOf(this.right.charAt(j))));
/* 179 */         j++;
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 185 */       buildScript(start1, middle.getStart(), start2, middle
/* 186 */           .getStart() - middle.getDiag(), script);
/*     */       
/* 188 */       for (int i = middle.getStart(); i < middle.getEnd(); i++) {
/* 189 */         script.append(new KeepCommand<>(Character.valueOf(this.left.charAt(i))));
/*     */       }
/* 191 */       buildScript(middle.getEnd(), end1, middle
/* 192 */           .getEnd() - middle.getDiag(), end2, script);
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
/*     */   private Snake buildSnake(int start, int diag, int end1, int end2) {
/* 207 */     int end = start;
/* 208 */     while (end - diag < end2 && end < end1 && this.left
/*     */       
/* 210 */       .charAt(end) == this.right.charAt(end - diag)) {
/* 211 */       end++;
/*     */     }
/* 213 */     return new Snake(start, end, diag);
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
/*     */   private Snake getMiddleSnake(int start1, int end1, int start2, int end2) {
/* 236 */     int m = end1 - start1;
/* 237 */     int n = end2 - start2;
/* 238 */     if (m == 0 || n == 0) {
/* 239 */       return null;
/*     */     }
/*     */     
/* 242 */     int delta = m - n;
/* 243 */     int sum = n + m;
/* 244 */     int offset = ((sum % 2 == 0) ? sum : (sum + 1)) / 2;
/* 245 */     this.vDown[1 + offset] = start1;
/* 246 */     this.vUp[1 + offset] = end1 + 1;
/*     */     
/* 248 */     for (int d = 0; d <= offset; d++) {
/*     */       int k;
/* 250 */       for (k = -d; k <= d; k += 2) {
/*     */ 
/*     */         
/* 253 */         int i = k + offset;
/* 254 */         if (k == -d || (k != d && this.vDown[i - 1] < this.vDown[i + 1])) {
/* 255 */           this.vDown[i] = this.vDown[i + 1];
/*     */         } else {
/* 257 */           this.vDown[i] = this.vDown[i - 1] + 1;
/*     */         } 
/*     */         
/* 260 */         int x = this.vDown[i];
/* 261 */         int y = x - start1 + start2 - k;
/*     */         
/* 263 */         while (x < end1 && y < end2 && this.left.charAt(x) == this.right.charAt(y)) {
/* 264 */           this.vDown[i] = ++x;
/* 265 */           y++;
/*     */         } 
/*     */         
/* 268 */         if (delta % 2 != 0 && delta - d <= k && k <= delta + d && 
/* 269 */           this.vUp[i - delta] <= this.vDown[i]) {
/* 270 */           return buildSnake(this.vUp[i - delta], k + start1 - start2, end1, end2);
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 276 */       for (k = delta - d; k <= delta + d; k += 2) {
/*     */         
/* 278 */         int i = k + offset - delta;
/* 279 */         if (k == delta - d || (k != delta + d && this.vUp[i + 1] <= this.vUp[i - 1])) {
/*     */           
/* 281 */           this.vUp[i] = this.vUp[i + 1] - 1;
/*     */         } else {
/* 283 */           this.vUp[i] = this.vUp[i - 1];
/*     */         } 
/*     */         
/* 286 */         int x = this.vUp[i] - 1;
/* 287 */         int y = x - start1 + start2 - k;
/* 288 */         while (x >= start1 && y >= start2 && this.left
/* 289 */           .charAt(x) == this.right.charAt(y)) {
/* 290 */           this.vUp[i] = x--;
/* 291 */           y--;
/*     */         } 
/*     */         
/* 294 */         if (delta % 2 == 0 && -d <= k && k <= d && 
/* 295 */           this.vUp[i] <= this.vDown[i + delta]) {
/* 296 */           return buildSnake(this.vUp[i], k + start1 - start2, end1, end2);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 303 */     throw new IllegalStateException("Internal Error");
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
/*     */   public EditScript<Character> getScript() {
/* 321 */     EditScript<Character> script = new EditScript<>();
/* 322 */     buildScript(0, this.left.length(), 0, this.right.length(), script);
/* 323 */     return script;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\text\diff\StringsComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */