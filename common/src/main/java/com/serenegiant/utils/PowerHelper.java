/*    */ package com.serenegiant.utils;
/*    */ 
/*    */ import android.app.Activity;
/*    */ import android.app.KeyguardManager;
/*    */ import android.os.PowerManager;
/*    */ import android.util.Log;
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
/*    */ public class PowerHelper
/*    */ {
/*    */   private static final String TAG = "PowerHelper";
/*    */   
/*    */   public static void wake(Activity activity, boolean disable_keyguard, long lock_delayed) {
/*    */     try {
/* 42 */       PowerManager.WakeLock wakelock = ((PowerManager)activity.getSystemService("power")).newWakeLock(805306394, "disableLock");
/*    */ 
/*    */       
/* 45 */       if (lock_delayed > 0L) {
/* 46 */         wakelock.acquire(lock_delayed);
/*    */       } else {
/* 48 */         wakelock.acquire();
/*    */       } 
/*    */       
/*    */       try {
/* 52 */         KeyguardManager keyguard = (KeyguardManager)activity.getSystemService("keyguard");
/* 53 */         KeyguardManager.KeyguardLock keylock = keyguard.newKeyguardLock("PowerHelper");
/* 54 */         keylock.disableKeyguard();
/*    */       } finally {
/* 56 */         wakelock.release();
/*    */       } 
/*    */       
/* 59 */       activity.getWindow().addFlags(524416);
/*    */     }
/* 61 */     catch (Exception e) {
/* 62 */       Log.w("PowerHelper", e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\PowerHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */