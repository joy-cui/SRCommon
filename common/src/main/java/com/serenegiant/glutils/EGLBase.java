/*     */ package com.serenegiant.glutils;
/*     */ 
/*     */ import android.os.Build;
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
/*     */ public abstract class EGLBase
/*     */ {
/*  25 */   public static final Object EGL_LOCK = new Object();
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int EGL_RECORDABLE_ANDROID = 12610;
/*     */ 
/*     */   
/*     */   public static final int EGL_CONTEXT_CLIENT_VERSION = 12440;
/*     */ 
/*     */   
/*     */   public static final int EGL_OPENGL_ES2_BIT = 4;
/*     */ 
/*     */   
/*     */   public static final int EGL_OPENGL_ES3_BIT_KHR = 64;
/*     */ 
/*     */ 
/*     */   
/*  42 */   public static EGLBase createFrom(IContext sharedContext, boolean withDepthBuffer, boolean isRecordable) { return createFrom(3, sharedContext, withDepthBuffer, 0, isRecordable); }
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
/*  55 */   public static EGLBase createFrom(IContext sharedContext, boolean withDepthBuffer, int stencilBits, boolean isRecordable) { return createFrom(3, sharedContext, withDepthBuffer, stencilBits, isRecordable); }
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
/*     */   public static EGLBase createFrom(int maxClientVersion, IContext sharedContext, boolean withDepthBuffer, int stencilBits, boolean isRecordable) {
/*  68 */     if (isEGL14Supported() && (sharedContext == null || sharedContext instanceof EGLBase14.Context)) {
/*  69 */       return new EGLBase14(maxClientVersion, (EGLBase14.Context)sharedContext, withDepthBuffer, stencilBits, isRecordable);
/*     */     }
/*  71 */     return new EGLBase10(maxClientVersion, (EGLBase10.Context)sharedContext, withDepthBuffer, stencilBits, isRecordable);
/*     */   }
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
/*     */   public static abstract class IContext {}
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
/*     */   public static abstract class IConfig {}
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
/* 106 */   public static boolean isEGL14Supported() { return (Build.VERSION.SDK_INT >= 18); }
/*     */   
/*     */   public abstract void release();
/*     */   
/*     */   public abstract String queryString(int paramInt);
/*     */   
/*     */   public abstract int getGlVersion();
/*     */   
/*     */   public abstract IContext getContext();
/*     */   
/*     */   public abstract IConfig getConfig();
/*     */   
/*     */   public abstract IEglSurface createFromSurface(Object paramObject);
/*     */   
/*     */   public abstract IEglSurface createOffscreen(int paramInt1, int paramInt2);
/*     */   
/*     */   public abstract void makeDefault();
/*     */   
/*     */   public abstract void sync();
/*     */   
/*     */   public static interface IEglSurface {
/*     */     void makeCurrent();
/*     */     
/*     */     void swap();
/*     */     
/*     */     IContext getContext();
/*     */     
/*     */     void swap(long param1Long);
/*     */     
/*     */     void release();
/*     */     
/*     */     boolean isValid();
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\glutils\EGLBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */