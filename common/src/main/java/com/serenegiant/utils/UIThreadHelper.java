/*    */ package com.serenegiant.utils;
/*    */ 
/*    */ import android.os.Handler;
/*    */ import android.os.Looper;
/*    */ import android.support.annotation.NonNull;
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
/*    */ public final class UIThreadHelper
/*    */ {
/* 27 */   private static final String TAG = UIThreadHelper.class.getSimpleName();
/*    */ 
/*    */   
/* 30 */   private static final Handler sUIHandler = new Handler(Looper.getMainLooper());
/*    */   
/* 32 */   private static final Thread sUiThread = sUIHandler.getLooper().getThread();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final void runOnUiThread(@NonNull Runnable task) {
/* 39 */     if (Thread.currentThread() != sUiThread) {
/* 40 */       sUIHandler.post(task);
/*    */     } else {
/*    */       try {
/* 43 */         task.run();
/* 44 */       } catch (Exception e) {
/* 45 */         Log.w(TAG, e);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public static final void runOnUiThread(@NonNull Runnable task, long duration) {
/* 51 */     if (duration > 0L || Thread.currentThread() != sUiThread) {
/* 52 */       sUIHandler.postDelayed(task, duration);
/*    */     } else {
/*    */       try {
/* 55 */         task.run();
/* 56 */       } catch (Exception e) {
/* 57 */         Log.w(TAG, e);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 63 */   public static final void removeFromUiThread(@NonNull Runnable task) { sUIHandler.removeCallbacks(task); }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\UIThreadHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */