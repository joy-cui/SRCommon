/*    */ package com.serenegiant.utils;
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
/*    */ public class FpsCounter
/*    */ {
/*    */   private int cnt;
/*    */   private int prevCnt;
/*    */   private long startTime;
/*    */   private long prevTime;
/*    */   private float fps;
/*    */   private float totalFps;
/*    */   
/* 26 */   public FpsCounter() { reset(); }
/*    */ 
/*    */   
/*    */   public synchronized FpsCounter reset() {
/* 30 */     this.cnt = this.prevCnt = 0;
/* 31 */     this.startTime = this.prevTime = System.nanoTime() - 1L;
/* 32 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   public synchronized void count() { this.cnt++; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized FpsCounter update() {
/* 47 */     long t = System.nanoTime();
/* 48 */     this.fps = (this.cnt - this.prevCnt) * 1.0E9F / (float)(t - this.prevTime);
/* 49 */     this.prevCnt = this.cnt;
/* 50 */     this.prevTime = t;
/* 51 */     this.totalFps = this.cnt * 1.0E9F / (float)(t - this.startTime);
/* 52 */     return this;
/*    */   }
/*    */ 
/*    */   
/* 56 */   public synchronized float getFps() { return this.fps; }
/*    */ 
/*    */ 
/*    */   
/* 60 */   public synchronized float getTotalFps() { return this.totalFps; }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\FpsCounter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */