/*     */ package com.serenegiant.glutils;
/*     */ 
/*     */ import android.opengl.GLES20;
/*     */ import android.opengl.Matrix;
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
/*     */ class RendererSurfaceRec
/*     */ {
/*     */   private Object mSurface;
/*     */   private EGLBase.IEglSurface mTargetSurface;
/*     */   
/*  20 */   static RendererSurfaceRec newInstance(EGLBase egl, Object surface, int maxFps) { return (maxFps > 0) ? new RendererSurfaceRecHasWait(egl, surface, maxFps) : new RendererSurfaceRec(egl, surface); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  29 */   final float[] mMvpMatrix = new float[16];
/*     */ 
/*     */ 
/*     */   
/*     */   protected volatile boolean mEnable = true;
/*     */ 
/*     */ 
/*     */   
/*     */   private RendererSurfaceRec(EGLBase egl, Object surface) {
/*  38 */     this.mSurface = surface;
/*  39 */     this.mTargetSurface = egl.createFromSurface(surface);
/*  40 */     Matrix.setIdentityM(this.mMvpMatrix, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void release() {
/*  47 */     if (this.mTargetSurface != null) {
/*  48 */       this.mTargetSurface.release();
/*  49 */       this.mTargetSurface = null;
/*     */     } 
/*  51 */     this.mSurface = null;
/*     */   }
/*     */ 
/*     */   
/*  55 */   public boolean isValid() { return (this.mTargetSurface != null && this.mTargetSurface.isValid()); }
/*     */ 
/*     */ 
/*     */   
/*  59 */   public boolean isEnabled() { return this.mEnable; }
/*     */ 
/*     */ 
/*     */   
/*  63 */   public void setEnabled(boolean enable) { this.mEnable = enable; }
/*     */ 
/*     */ 
/*     */   
/*  67 */   public boolean canDraw() { return this.mEnable; }
/*     */ 
/*     */   
/*     */   public void draw(GLDrawer2D drawer, int textId, float[] texMatrix) {
/*  71 */     this.mTargetSurface.makeCurrent();
/*     */     
/*  73 */     GLES20.glClear(16384);
/*  74 */     drawer.setMvpMatrix(this.mMvpMatrix, 0);
/*  75 */     drawer.draw(textId, texMatrix, 0);
/*  76 */     this.mTargetSurface.swap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public void makeCurrent() { this.mTargetSurface.makeCurrent(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   public void swap() { this.mTargetSurface.swap(); }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class RendererSurfaceRecHasWait
/*     */     extends RendererSurfaceRec
/*     */   {
/*     */     private long mNextDraw;
/*     */ 
/*     */     
/*     */     private final long mIntervalsNs;
/*     */ 
/*     */     
/*     */     private RendererSurfaceRecHasWait(EGLBase egl, Object surface, int maxFps) {
/* 108 */       super(egl, surface);
/* 109 */       this.mIntervalsNs = 1000000000L / maxFps;
/* 110 */       this.mNextDraw = System.nanoTime() + this.mIntervalsNs;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 115 */     public boolean canDraw() { return (this.mEnable && System.nanoTime() > this.mNextDraw); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void draw(GLDrawer2D drawer, int textId, float[] texMatrix) {
/* 120 */       this.mNextDraw = System.nanoTime() + this.mIntervalsNs;
/* 121 */       super.draw(drawer, textId, texMatrix);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\glutils\RendererSurfaceRec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */