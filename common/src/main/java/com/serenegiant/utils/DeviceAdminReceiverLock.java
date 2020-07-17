/*    */ package com.serenegiant.utils;
/*    */ 
/*    */ import android.app.Activity;
/*    */ import android.app.admin.DeviceAdminReceiver;
/*    */ import android.app.admin.DevicePolicyManager;
/*    */ import android.content.ComponentName;
/*    */ import android.content.Context;
/*    */ import android.content.Intent;
/*    */ import android.os.Parcelable;
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
/*    */ public class DeviceAdminReceiverLock
/*    */   extends DeviceAdminReceiver
/*    */ {
/*    */   public static final String EXTRA_REQUEST_FINISH = "EXTRA_REQUEST_FINISH";
/*    */   private static final int REQ_SCREEN_LOCK = 412809;
/*    */   
/*    */   public static void requestScreenLock(Activity activity, boolean finish) {
/* 34 */     if (!checkScreenLock(activity, finish)) {
/*    */       
/* 36 */       Intent intent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
/* 37 */       intent.putExtra("android.app.extra.DEVICE_ADMIN", (Parcelable)new ComponentName((Context)activity, DeviceAdminReceiverLock.class));
/* 38 */       intent.putExtra("EXTRA_REQUEST_FINISH", finish);
/* 39 */       activity.startActivityForResult(intent, 412809);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static boolean checkScreenLock(Activity activity, boolean finish) {
/* 48 */     ComponentName cn = new ComponentName((Context)activity, DeviceAdminReceiverLock.class);
/* 49 */     DevicePolicyManager dpm = (DevicePolicyManager)activity.getSystemService("device_policy");
/* 50 */     if (dpm.isAdminActive(cn)) {
/*    */       
/* 52 */       dpm.lockNow();
/* 53 */       if (finish) {
/* 54 */         activity.finish();
/*    */       }
/* 56 */       return true;
/*    */     } 
/* 58 */     return false;
/*    */   }
/*    */   
/*    */   public static boolean onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
/* 62 */     switch (requestCode) {
/*    */       case 412809:
/* 64 */         if (resultCode == -1) {
/* 65 */           boolean finish = (data != null && data.getBooleanExtra("EXTRA_REQUEST_FINISH", false));
/*    */           
/* 67 */           checkScreenLock(activity, finish);
/* 68 */           return true;
/*    */         } 
/*    */         break;
/*    */     } 
/*    */     
/* 73 */     return false;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\DeviceAdminReceiverLock.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */