/*    */ package com.serenegiant.net;
/*    */ 
/*    */ import android.util.Log;
/*    */ import java.net.InetAddress;
/*    */ import java.net.NetworkInterface;
/*    */ import java.net.SocketException;
/*    */ import java.util.Enumeration;
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
/*    */ public class NetworkHelper
/*    */ {
/* 31 */   private static final String TAG = NetworkHelper.class.getSimpleName();
/*    */   
/*    */   public static String getLocalIPv4Address() {
/*    */     try {
/* 35 */       Enumeration<NetworkInterface> networkInterfaceEnum = NetworkInterface.getNetworkInterfaces();
/* 36 */       while (networkInterfaceEnum.hasMoreElements()) {
/*    */ 
/*    */         
/* 39 */         NetworkInterface networkInterface = networkInterfaceEnum.nextElement();
/* 40 */         Enumeration<InetAddress> ipAddressEnum = networkInterface.getInetAddresses();
/* 41 */         while (ipAddressEnum.hasMoreElements()) {
/*    */           
/* 43 */           InetAddress addr = ipAddressEnum.nextElement();
/* 44 */           if (!addr.isLoopbackAddress() && addr instanceof java.net.Inet4Address) {
/* 45 */             return addr.getHostAddress();
/*    */           }
/*    */         } 
/*    */       } 
/* 49 */     } catch (SocketException e) {
/* 50 */       Log.e(TAG, "getLocalIPv4Address", e);
/*    */     } 
/* 52 */     return null;
/*    */   }
/*    */   
/*    */   public static String getLocalIPv6Address() {
/*    */     try {
/* 57 */       Enumeration<NetworkInterface> networkInterfaceEnum = NetworkInterface.getNetworkInterfaces();
/* 58 */       while (networkInterfaceEnum.hasMoreElements()) {
/*    */ 
/*    */         
/* 61 */         NetworkInterface networkInterface = networkInterfaceEnum.nextElement();
/* 62 */         Enumeration<InetAddress> ipAddressEnum = networkInterface.getInetAddresses();
/* 63 */         while (ipAddressEnum.hasMoreElements()) {
/*    */           
/* 65 */           InetAddress addr = ipAddressEnum.nextElement();
/* 66 */           if (!addr.isLoopbackAddress() && addr instanceof java.net.Inet6Address) {
/* 67 */             return addr.getHostAddress();
/*    */           }
/*    */         } 
/*    */       } 
/* 71 */     } catch (SocketException e) {
/* 72 */       Log.w(TAG, "getLocalIPv6Address", e);
/*    */     } 
/* 74 */     return null;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\net\NetworkHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */