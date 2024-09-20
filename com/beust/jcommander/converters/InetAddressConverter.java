/*    */ package com.beust.jcommander.converters;
/*    */ 
/*    */ import com.beust.jcommander.IStringConverter;
/*    */ import java.net.InetAddress;
/*    */ import java.net.UnknownHostException;
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
/*    */ public class InetAddressConverter
/*    */   implements IStringConverter<InetAddress>
/*    */ {
/*    */   public InetAddress convert(String host) {
/*    */     try {
/* 34 */       return InetAddress.getByName(host);
/* 35 */     } catch (UnknownHostException e) {
/* 36 */       throw new IllegalArgumentException(host, e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovanchik\Downloads\TLauncher.jar!\com\beust\jcommander\converters\InetAddressConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */