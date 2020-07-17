/*    */ package com.serenegiant.utils;
/*    */ 
/*    */ import android.os.Build;
/*    */ import java.util.concurrent.LinkedBlockingQueue;
/*    */ import java.util.concurrent.ThreadPoolExecutor;
/*    */ import java.util.concurrent.TimeUnit;
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
/*    */ public class ThreadPool
/*    */ {
/*    */   private static final int CORE_POOL_SIZE = 4;
/*    */   private static final int MAX_POOL_SIZE = 32;
/*    */   private static final int KEEP_ALIVE_TIME = 10;
/* 33 */   private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(4, 32, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
/*    */ 
/*    */ 
/*    */   
/*    */   static  {
/* 38 */     if (Build.VERSION.SDK_INT >= 9) {
/* 39 */       EXECUTOR.allowCoreThreadTimeOut(true);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public static void preStartAllCoreThreads() { EXECUTOR.prestartAllCoreThreads(); }
/*    */ 
/*    */ 
/*    */   
/* 50 */   public static void queueEvent(Runnable command) { EXECUTOR.execute(command); }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\ThreadPool.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */