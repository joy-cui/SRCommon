/*    */ package com.serenegiant.utils;
/*    */ 
/*    */ import android.os.Handler;
/*    */ import android.os.HandlerThread;
/*    */ import android.os.Looper;
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
/*    */ public class HandlerThreadHandler
/*    */   extends Handler
/*    */ {
/*    */   private static final String TAG = "HandlerThreadHandler";
/*    */   
/* 29 */   public static final HandlerThreadHandler createHandler() { return createHandler("HandlerThreadHandler"); }
/*    */ 
/*    */   
/*    */   public static final HandlerThreadHandler createHandler(String name) {
/* 33 */     HandlerThread thread = new HandlerThread(name);
/* 34 */     thread.start();
/* 35 */     return new HandlerThreadHandler(thread.getLooper());
/*    */   }
/*    */ 
/*    */   
/* 39 */   public static final HandlerThreadHandler createHandler(Callback callback) { return createHandler("HandlerThreadHandler", callback); }
/*    */ 
/*    */   
/*    */   public static final HandlerThreadHandler createHandler(String name, Callback callback) {
/* 43 */     HandlerThread thread = new HandlerThread(name);
/* 44 */     thread.start();
/* 45 */     return new HandlerThreadHandler(thread.getLooper(), callback);
/*    */   }
/*    */ 
/*    */   
/* 49 */   private HandlerThreadHandler(Looper looper) { super(looper); }
/*    */ 
/*    */ 
/*    */   
/* 53 */   private HandlerThreadHandler(Looper looper, Callback callback) { super(looper, callback); }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\HandlerThreadHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */