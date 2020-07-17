/*     */ package com.serenegiant.glutils;
/*     */ 
/*     */ import com.serenegiant.utils.MessageTask;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class EglTask
/*     */   extends MessageTask
/*     */ {
/*     */   public static final int EGL_FLAG_DEPTH_BUFFER = 1;
/*     */   public static final int EGL_FLAG_RECORDABLE = 2;
/*     */   public static final int EGL_FLAG_STENCIL_1BIT = 4;
/*     */   public static final int EGL_FLAG_STENCIL_8BIT = 32;
/*  34 */   private EGLBase mEgl = null;
/*     */   
/*     */   private EGLBase.IEglSurface mEglHolder;
/*     */ 
/*     */   
/*  39 */   public EglTask(EGLBase.IContext sharedContext, int flags) { init(flags, 3, sharedContext); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  44 */   public EglTask(int maxClientVersion, EGLBase.IContext sharedContext, int flags) { init(flags, maxClientVersion, sharedContext); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onInit(int flags, int maxClientVersion, Object sharedContext) {
/*  54 */     if (sharedContext == null || sharedContext instanceof EGLBase.IContext) {
/*  55 */       int stencilBits = ((flags & 0x4) == 4) ? 1 : (((flags & 0x20) == 32) ? 8 : 0);
/*     */ 
/*     */       
/*  58 */       this.mEgl = EGLBase.createFrom(maxClientVersion, (EGLBase.IContext)sharedContext, ((flags & 0x1) == 1), stencilBits, ((flags & 0x2) == 2));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  63 */     if (this.mEgl == null) {
/*  64 */       callOnError(new RuntimeException("failed to create EglCore"));
/*  65 */       releaseSelf();
/*     */     } else {
/*  67 */       this.mEglHolder = this.mEgl.createOffscreen(1, 1);
/*  68 */       this.mEglHolder.makeCurrent();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Request takeRequest() throws InterruptedException {
/*  74 */     Request result = super.takeRequest();
/*  75 */     this.mEglHolder.makeCurrent();
/*  76 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  81 */   protected void onBeforeStop() { this.mEglHolder.makeCurrent(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onRelease() {
/*  86 */     this.mEglHolder.release();
/*  87 */     this.mEgl.release();
/*     */   }
/*     */ 
/*     */   
/*  91 */   protected EGLBase getEgl() { return this.mEgl; }
/*     */ 
/*     */ 
/*     */   
/*  95 */   protected EGLBase.IContext getEGLContext() { return this.mEgl.getContext(); }
/*     */ 
/*     */ 
/*     */   
/*  99 */   protected EGLBase.IConfig getConfig() { return this.mEgl.getConfig(); }
/*     */ 
/*     */ 
/*     */   
/* 103 */   protected EGLBase.IContext getContext() { return (this.mEgl != null) ? this.mEgl.getContext() : null; }
/*     */ 
/*     */ 
/*     */   
/* 107 */   protected void makeCurrent() { this.mEglHolder.makeCurrent(); }
/*     */ 
/*     */ 
/*     */   
/* 111 */   protected boolean isGLES3() { return (this.mEgl != null && this.mEgl.getGlVersion() > 2); }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\glutils\EglTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */