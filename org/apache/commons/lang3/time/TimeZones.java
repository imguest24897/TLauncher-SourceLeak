/*    */ package org.apache.commons.lang3.time;
/*    */ 
/*    */ import java.util.TimeZone;
/*    */ import org.apache.commons.lang3.ObjectUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TimeZones
/*    */ {
/*    */   public static final String GMT_ID = "GMT";
/* 45 */   public static final TimeZone GMT = TimeZone.getTimeZone("GMT");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static TimeZone toTimeZone(TimeZone timeZone) {
/* 55 */     return (TimeZone)ObjectUtils.getIfNull(timeZone, TimeZone::getDefault);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\org\apache\commons\lang3\time\TimeZones.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */