/*     */ package com.serenegiant.glutils;
/*     */ 
/*     */ import android.annotation.TargetApi;
/*     */ import android.opengl.EGL14;
/*     */ import android.opengl.EGLConfig;
/*     */ import android.opengl.EGLContext;
/*     */ import android.opengl.EGLDisplay;
/*     */ import android.opengl.EGLExt;
/*     */ import android.opengl.EGLSurface;
/*     */ import android.opengl.GLES10;
/*     */ import android.opengl.GLES20;
/*     */ import android.support.annotation.NonNull;
/*     */ import android.util.Log;
/*     */ import com.serenegiant.utils.BuildCheck;
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
/*     */ 
/*     */ 
/*     */ @TargetApi(18)
/*     */ public class EGLBase14
/*     */   extends EGLBase
/*     */ {
/*     */   private static final String TAG = "EGLBase14";
/*  45 */   private static final Context EGL_NO_CONTEXT = new Context(EGL14.EGL_NO_CONTEXT);
/*     */   
/*  47 */   private Config mEglConfig = null; @NonNull
/*  48 */   private Context mContext = EGL_NO_CONTEXT;
/*     */   
/*  50 */   private EGLDisplay mEglDisplay = EGL14.EGL_NO_DISPLAY;
/*  51 */   private EGLContext mDefaultContext = EGL14.EGL_NO_CONTEXT;
/*  52 */   private int mGlVersion = 2;
/*     */   
/*     */   private final int[] mSurfaceDimension;
/*     */   
/*     */   public static class Context
/*     */     extends IContext
/*     */   {
/*     */     public final EGLContext eglContext;
/*     */     
/*  61 */     private Context(EGLContext context) { this.eglContext = context; }
/*     */   }
/*     */   
/*     */   public static class Config
/*     */     extends IConfig
/*     */   {
/*     */     public final EGLConfig eglConfig;
/*     */     
/*  69 */     private Config(EGLConfig eglConfig) { this.eglConfig = eglConfig; }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class EglSurface
/*     */     implements IEglSurface
/*     */   {
/*     */     private final EGLBase14 mEglBase;
/*     */     
/*  78 */     private EGLSurface mEglSurface = EGL14.EGL_NO_SURFACE;
/*     */ 
/*     */     
/*     */     private EglSurface(EGLBase14 eglBase, Object surface) throws IllegalArgumentException {
/*  82 */       this.mEglBase = eglBase;
/*  83 */       if (surface instanceof android.view.Surface || surface instanceof android.view.SurfaceHolder || surface instanceof android.graphics.SurfaceTexture || surface instanceof android.view.SurfaceView) {
/*     */ 
/*     */ 
/*     */         
/*  87 */         this.mEglSurface = this.mEglBase.createWindowSurface(surface);
/*     */       } else {
/*  89 */         throw new IllegalArgumentException("unsupported surface");
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private EglSurface(EGLBase14 eglBase, int width, int height) {
/* 101 */       this.mEglBase = eglBase;
/* 102 */       if (width <= 0 || height <= 0) {
/* 103 */         this.mEglSurface = this.mEglBase.createOffscreenSurface(1, 1);
/*     */       } else {
/* 105 */         this.mEglSurface = this.mEglBase.createOffscreenSurface(width, height);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void makeCurrent() {
/* 111 */       this.mEglBase.makeCurrent(this.mEglSurface);
/* 112 */       if (this.mEglBase.getGlVersion() >= 2) {
/* 113 */         GLES20.glViewport(0, 0, this.mEglBase.getSurfaceWidth(this.mEglSurface), this.mEglBase.getSurfaceHeight(this.mEglSurface));
/*     */       } else {
/* 115 */         GLES10.glViewport(0, 0, this.mEglBase.getSurfaceWidth(this.mEglSurface), this.mEglBase.getSurfaceHeight(this.mEglSurface));
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 121 */     public void swap() { this.mEglBase.swap(this.mEglSurface); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     public void swap(long presentationTimeNs) { this.mEglBase.swap(this.mEglSurface, presentationTimeNs); }
/*     */ 
/*     */ 
/*     */     
/* 130 */     public void setPresentationTime(long presentationTimeNs) { EGLExt.eglPresentationTimeANDROID(this.mEglBase.mEglDisplay, this.mEglSurface, presentationTimeNs); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 135 */     public IContext getContext() { return this.mEglBase.getContext(); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isValid() {
/* 140 */       return (this.mEglSurface != null && this.mEglSurface != EGL14.EGL_NO_SURFACE && this.mEglBase
/* 141 */         .getSurfaceWidth(this.mEglSurface) > 0 && this.mEglBase.getSurfaceHeight(this.mEglSurface) > 0);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void release() {
/* 147 */       this.mEglBase.makeDefault();
/* 148 */       this.mEglBase.destroyWindowSurface(this.mEglSurface);
/* 149 */       this.mEglSurface = EGL14.EGL_NO_SURFACE;
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void release() {
/* 171 */     if (this.mEglDisplay != EGL14.EGL_NO_DISPLAY) {
/* 172 */       destroyContext();
/* 173 */       EGL14.eglTerminate(this.mEglDisplay);
/* 174 */       EGL14.eglReleaseThread();
/*     */     } 
/* 176 */     this.mEglDisplay = EGL14.EGL_NO_DISPLAY;
/* 177 */     this.mContext = EGL_NO_CONTEXT;
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
/*     */   public EglSurface createFromSurface(Object nativeWindow) {
/* 189 */     EglSurface eglSurface = new EglSurface(this, nativeWindow);
/* 190 */     eglSurface.makeCurrent();
/* 191 */     return eglSurface;
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
/*     */   public EglSurface createOffscreen(int width, int height) {
/* 204 */     EglSurface eglSurface = new EglSurface(this, width, height);
/* 205 */     eglSurface.makeCurrent();
/* 206 */     return eglSurface;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 215 */   public String queryString(int what) { return EGL14.eglQueryString(this.mEglDisplay, what); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 224 */   public int getGlVersion() { return this.mGlVersion; }
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
/* 235 */   public Context getContext() { return this.mContext; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 244 */   public Config getConfig() { return this.mEglConfig; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void makeDefault() {
/* 253 */     if (!EGL14.eglMakeCurrent(this.mEglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT)) {
/* 254 */       Log.w("TAG", "makeDefault" + EGL14.eglGetError());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sync() {
/* 266 */     EGL14.eglWaitGL();
/* 267 */     EGL14.eglWaitNative(12379);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(int maxClientVersion, Context sharedContext, boolean withDepthBuffer, int stencilBits, boolean isRecordable) {
/* 274 */     if (this.mEglDisplay != EGL14.EGL_NO_DISPLAY) {
/* 275 */       throw new RuntimeException("EGL already set up");
/*     */     }
/*     */     
/* 278 */     this.mEglDisplay = EGL14.eglGetDisplay(0);
/* 279 */     if (this.mEglDisplay == EGL14.EGL_NO_DISPLAY) {
/* 280 */       throw new RuntimeException("eglGetDisplay failed");
/*     */     }
/*     */     
/* 283 */     int[] version = new int[2];
/* 284 */     if (!EGL14.eglInitialize(this.mEglDisplay, version, 0, version, 1)) {
/* 285 */       this.mEglDisplay = null;
/* 286 */       throw new RuntimeException("eglInitialize failed");
/*     */     } 
/*     */     
/* 289 */     sharedContext = (sharedContext != null) ? sharedContext : EGL_NO_CONTEXT;
/*     */ 
/*     */     
/* 292 */     if (maxClientVersion >= 3) {
/*     */       
/* 294 */       EGLConfig config = getConfig(3, withDepthBuffer, stencilBits, isRecordable);
/* 295 */       if (config != null) {
/* 296 */         EGLContext context = createContext(sharedContext, config, 3);
/* 297 */         if (EGL14.eglGetError() == 12288) {
/* 298 */           this.mEglConfig = new Config(config);
/* 299 */           this.mContext = new Context(context);
/* 300 */           this.mGlVersion = 3;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 305 */     if (maxClientVersion >= 2 && (this.mContext == null || this.mContext.eglContext == EGL14.EGL_NO_CONTEXT)) {
/* 306 */       EGLConfig config = getConfig(2, withDepthBuffer, stencilBits, isRecordable);
/* 307 */       if (config == null) {
/* 308 */         throw new RuntimeException("chooseConfig failed");
/*     */       }
/*     */       
/*     */       try {
/* 312 */         EGLContext context = createContext(sharedContext, config, 2);
/* 313 */         checkEglError("eglCreateContext");
/* 314 */         this.mEglConfig = new Config(config);
/* 315 */         this.mContext = new Context(context);
/* 316 */         this.mGlVersion = 2;
/* 317 */       } catch (Exception e) {
/* 318 */         if (isRecordable) {
/* 319 */           config = getConfig(2, withDepthBuffer, stencilBits, false);
/* 320 */           if (config == null) {
/* 321 */             throw new RuntimeException("chooseConfig failed");
/*     */           }
/*     */           
/* 324 */           EGLContext context = createContext(sharedContext, config, 2);
/* 325 */           checkEglError("eglCreateContext");
/* 326 */           this.mEglConfig = new Config(config);
/* 327 */           this.mContext = new Context(context);
/* 328 */           this.mGlVersion = 2;
/*     */         } 
/*     */       } 
/*     */     } 
/* 332 */     if (this.mContext == null || this.mContext.eglContext == EGL14.EGL_NO_CONTEXT) {
/* 333 */       EGLConfig config = getConfig(1, withDepthBuffer, stencilBits, isRecordable);
/* 334 */       if (config == null) {
/* 335 */         throw new RuntimeException("chooseConfig failed");
/*     */       }
/*     */       
/* 338 */       EGLContext context = createContext(sharedContext, config, 1);
/* 339 */       checkEglError("eglCreateContext");
/* 340 */       this.mEglConfig = new Config(config);
/* 341 */       this.mContext = new Context(context);
/* 342 */       this.mGlVersion = 1;
/*     */     } 
/*     */     
/* 345 */     int[] values = new int[1];
/* 346 */     EGL14.eglQueryContext(this.mEglDisplay, this.mContext.eglContext, 12440, values, 0);
/* 347 */     Log.d("EGLBase14", "EGLContext created, client version " + values[0]);
/* 348 */     makeDefault();
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
/*     */   private boolean makeCurrent(EGLSurface surface) {
/* 360 */     if (surface == null || surface == EGL14.EGL_NO_SURFACE) {
/* 361 */       int error = EGL14.eglGetError();
/* 362 */       if (error == 12299) {
/* 363 */         Log.e("EGLBase14", "makeCurrent:returned EGL_BAD_NATIVE_WINDOW.");
/*     */       }
/* 365 */       return false;
/*     */     } 
/*     */     
/* 368 */     if (!EGL14.eglMakeCurrent(this.mEglDisplay, surface, surface, this.mContext.eglContext)) {
/* 369 */       Log.w("TAG", "eglMakeCurrent" + EGL14.eglGetError());
/* 370 */       return false;
/*     */     } 
/* 372 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private int swap(EGLSurface surface) {
/* 377 */     if (!EGL14.eglSwapBuffers(this.mEglDisplay, surface)) {
/* 378 */       int err = EGL14.eglGetError();
/*     */       
/* 380 */       return err;
/*     */     } 
/* 382 */     return 12288;
/*     */   }
/*     */ 
/*     */   
/*     */   private int swap(EGLSurface surface, long presentationTimeNs) {
/* 387 */     EGLExt.eglPresentationTimeANDROID(this.mEglDisplay, surface, presentationTimeNs);
/* 388 */     if (!EGL14.eglSwapBuffers(this.mEglDisplay, surface)) {
/* 389 */       int err = EGL14.eglGetError();
/*     */       
/* 391 */       return err;
/*     */     } 
/* 393 */     return 12288;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private EGLContext createContext(Context sharedContext, EGLConfig config, int version) {
/* 399 */     int[] attrib_list = { 12440, version, 12344 };
/*     */ 
/*     */ 
/*     */     
/* 403 */     EGLContext context = EGL14.eglCreateContext(this.mEglDisplay, config, sharedContext.eglContext, attrib_list, 0);
/*     */     
/* 405 */     return context;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void destroyContext() {
/* 411 */     if (!EGL14.eglDestroyContext(this.mEglDisplay, this.mContext.eglContext)) {
/* 412 */       Log.e("destroyContext", "display:" + this.mEglDisplay + " context: " + this.mContext.eglContext);
/* 413 */       Log.e("EGLBase14", "eglDestroyContext:" + EGL14.eglGetError());
/*     */     } 
/* 415 */     this.mContext = EGL_NO_CONTEXT;
/* 416 */     if (this.mDefaultContext != EGL14.EGL_NO_CONTEXT) {
/* 417 */       if (!EGL14.eglDestroyContext(this.mEglDisplay, this.mDefaultContext)) {
/* 418 */         Log.e("destroyContext", "display:" + this.mEglDisplay + " context: " + this.mDefaultContext);
/* 419 */         Log.e("EGLBase14", "eglDestroyContext:" + EGL14.eglGetError());
/*     */       } 
/* 421 */       this.mDefaultContext = EGL14.EGL_NO_CONTEXT;
/*     */     } 
/*     */   }
/*     */   
/* 425 */   public EGLBase14(int maxClientVersion, Context sharedContext, boolean withDepthBuffer, int stencilBits, boolean isRecordable) { this.mSurfaceDimension = new int[2];
/*     */     init(maxClientVersion, sharedContext, withDepthBuffer, stencilBits, isRecordable); } private final int getSurfaceWidth(EGLSurface surface) {
/* 427 */     boolean ret = EGL14.eglQuerySurface(this.mEglDisplay, surface, 12375, this.mSurfaceDimension, 0);
/* 428 */     if (!ret) this.mSurfaceDimension[0] = 0; 
/* 429 */     return this.mSurfaceDimension[0];
/*     */   }
/*     */   
/*     */   private final int getSurfaceHeight(EGLSurface surface) {
/* 433 */     boolean ret = EGL14.eglQuerySurface(this.mEglDisplay, surface, 12374, this.mSurfaceDimension, 1);
/* 434 */     if (!ret) this.mSurfaceDimension[1] = 0; 
/* 435 */     return this.mSurfaceDimension[1];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final EGLSurface createWindowSurface(Object nativeWindow) {
/* 446 */     int[] surfaceAttribs = { 12344 };
/*     */ 
/*     */     
/* 449 */     EGLSurface result = null;
/*     */     try {
/* 451 */       result = EGL14.eglCreateWindowSurface(this.mEglDisplay, this.mEglConfig.eglConfig, nativeWindow, surfaceAttribs, 0);
/* 452 */       if (result == null || result == EGL14.EGL_NO_SURFACE) {
/* 453 */         int error = EGL14.eglGetError();
/* 454 */         if (error == 12299) {
/* 455 */           Log.e("EGLBase14", "createWindowSurface returned EGL_BAD_NATIVE_WINDOW.");
/*     */         }
/* 457 */         throw new RuntimeException("createWindowSurface failed error=" + error);
/*     */       } 
/* 459 */       makeCurrent(result);
/*     */     }
/* 461 */     catch (Exception e) {
/* 462 */       Log.e("EGLBase14", "eglCreateWindowSurface", e);
/* 463 */       throw new IllegalArgumentException(e);
/*     */     } 
/* 465 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final EGLSurface createOffscreenSurface(int width, int height) {
/* 473 */     int[] surfaceAttribs = { 12375, width, 12374, height, 12344 };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 478 */     EGLSurface result = null;
/*     */     try {
/* 480 */       result = EGL14.eglCreatePbufferSurface(this.mEglDisplay, this.mEglConfig.eglConfig, surfaceAttribs, 0);
/* 481 */       checkEglError("eglCreatePbufferSurface");
/* 482 */       if (result == null) {
/* 483 */         throw new RuntimeException("surface was null");
/*     */       }
/* 485 */     } catch (IllegalArgumentException e) {
/* 486 */       Log.e("EGLBase14", "createOffscreenSurface", e);
/* 487 */     } catch (RuntimeException e) {
/* 488 */       Log.e("EGLBase14", "createOffscreenSurface", e);
/*     */     } 
/* 490 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void destroyWindowSurface(EGLSurface surface) {
/* 496 */     if (surface != EGL14.EGL_NO_SURFACE) {
/* 497 */       EGL14.eglMakeCurrent(this.mEglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
/*     */       
/* 499 */       EGL14.eglDestroySurface(this.mEglDisplay, surface);
/*     */     } 
/* 501 */     surface = EGL14.EGL_NO_SURFACE;
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkEglError(String msg) {
/*     */     int error;
/* 507 */     if ((error = EGL14.eglGetError()) != 12288) {
/* 508 */       throw new RuntimeException(msg + ": EGL error: 0x" + Integer.toHexString(error));
/*     */     }
/*     */   }
/*     */   
/*     */   private EGLConfig getConfig(int version, boolean hasDepthBuffer, int stencilBits, boolean isRecordable) {
/* 513 */     int renderableType = 4;
/* 514 */     if (version >= 3) {
/* 515 */       renderableType |= 0x40;
/*     */     }
/* 517 */     int[] attribList = { 12352, renderableType, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12344, 12344, 12344, 12344, 12344, 12344, 12344 };
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
/* 530 */     int offset = 10;
/* 531 */     if (stencilBits > 0) {
/* 532 */       attribList[offset++] = 12326;
/* 533 */       attribList[offset++] = stencilBits;
/*     */     } 
/* 535 */     if (hasDepthBuffer) {
/* 536 */       attribList[offset++] = 12325;
/* 537 */       attribList[offset++] = 16;
/*     */     } 
/* 539 */     if (isRecordable && BuildCheck.isAndroid4_3()) {
/* 540 */       attribList[offset++] = 12610;
/* 541 */       attribList[offset++] = 1;
/*     */     } 
/* 543 */     for (int i = attribList.length - 1; i >= offset; i--) {
/* 544 */       attribList[i] = 12344;
/*     */     }
/* 546 */     EGLConfig config = internalGetConfig(attribList);
/* 547 */     if (config == null && version == 2 && 
/* 548 */       isRecordable) {
/*     */       
/* 550 */       int n = attribList.length;
/* 551 */       for (int i = 10; i < n - 1; i += 2) {
/* 552 */         if (attribList[i] == 12610) {
/* 553 */           for (int j = i; j < n; j++) {
/* 554 */             attribList[j] = 12344;
/*     */           }
/*     */           break;
/*     */         } 
/*     */       } 
/* 559 */       config = internalGetConfig(attribList);
/*     */     } 
/*     */     
/* 562 */     if (config == null) {
/* 563 */       Log.w("EGLBase14", "try to fallback to RGB565");
/* 564 */       attribList[3] = 5;
/* 565 */       attribList[5] = 6;
/* 566 */       attribList[7] = 5;
/* 567 */       config = internalGetConfig(attribList);
/*     */     } 
/* 569 */     return config;
/*     */   }
/*     */   
/*     */   private EGLConfig internalGetConfig(int[] attribList) {
/* 573 */     EGLConfig[] configs = new EGLConfig[1];
/* 574 */     int[] numConfigs = new int[1];
/* 575 */     if (!EGL14.eglChooseConfig(this.mEglDisplay, attribList, 0, configs, 0, configs.length, numConfigs, 0)) {
/* 576 */       return null;
/*     */     }
/* 578 */     return configs[0];
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\glutils\EGLBase14.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */