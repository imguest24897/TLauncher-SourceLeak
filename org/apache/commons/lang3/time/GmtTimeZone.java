/*     */ package org.apache.commons.lang3.time;
/*     */ 
/*     */ import java.util.Date;
/*     */ import java.util.Objects;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class GmtTimeZone
/*     */   extends TimeZone
/*     */ {
/*     */   private static final int MILLISECONDS_PER_MINUTE = 60000;
/*     */   private static final int MINUTES_PER_HOUR = 60;
/*     */   private static final int HOURS_PER_DAY = 24;
/*     */   static final long serialVersionUID = 1L;
/*     */   private final int offset;
/*     */   private final String zoneId;
/*     */   
/*     */   private static StringBuilder twoDigits(StringBuilder sb, int n) {
/*  38 */     return sb.append((char)(48 + n / 10)).append((char)(48 + n % 10));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   GmtTimeZone(boolean negate, int hours, int minutes) {
/*  45 */     if (hours >= 24) {
/*  46 */       throw new IllegalArgumentException(hours + " hours out of range");
/*     */     }
/*  48 */     if (minutes >= 60) {
/*  49 */       throw new IllegalArgumentException(minutes + " minutes out of range");
/*     */     }
/*  51 */     int milliseconds = (minutes + hours * 60) * 60000;
/*  52 */     this.offset = negate ? -milliseconds : milliseconds;
/*     */     
/*  54 */     this
/*     */ 
/*     */ 
/*     */       
/*  58 */       .zoneId = twoDigits(twoDigits((new StringBuilder(9)).append("GMT").append(negate ? 45 : 43), hours).append(':'), minutes).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  64 */     if (this == obj) {
/*  65 */       return true;
/*     */     }
/*  67 */     if (!(obj instanceof GmtTimeZone)) {
/*  68 */       return false;
/*     */     }
/*  70 */     GmtTimeZone other = (GmtTimeZone)obj;
/*  71 */     return (this.offset == other.offset && Objects.equals(this.zoneId, other.zoneId));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getID() {
/*  76 */     return this.zoneId;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOffset(int era, int year, int month, int day, int dayOfWeek, int milliseconds) {
/*  81 */     return this.offset;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRawOffset() {
/*  86 */     return this.offset;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  91 */     return Objects.hash(new Object[] { Integer.valueOf(this.offset), this.zoneId });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean inDaylightTime(Date date) {
/*  96 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRawOffset(int offsetMillis) {
/* 101 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 106 */     return "[GmtTimeZone id=\"" + this.zoneId + "\",offset=" + this.offset + ']';
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean useDaylightTime() {
/* 111 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\time\GmtTimeZone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */