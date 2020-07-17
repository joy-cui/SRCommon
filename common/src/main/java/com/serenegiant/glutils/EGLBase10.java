/*     */ package com.serenegiant.glutils;
/*     */ 
/*     */ import android.graphics.Canvas;
/*     */ import android.graphics.Rect;
/*     */ import android.opengl.GLES10;
/*     */ import android.opengl.GLES20;
/*     */ import android.support.annotation.NonNull;
/*     */ import android.support.annotation.Nullable;
/*     */ import android.util.Log;
/*     */ import android.view.Surface;
/*     */ import android.view.SurfaceHolder;
/*     */ import com.serenegiant.utils.BuildCheck;
/*     */ import javax.microedition.khronos.egl.EGL10;
/*     */ import javax.microedition.khronos.egl.EGLConfig;
/*     */ import javax.microedition.khronos.egl.EGLContext;
/*     */ import javax.microedition.khronos.egl.EGLDisplay;
/*     */ import javax.microedition.khronos.egl.EGLSurface;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EGLBase10
/*     */   extends EGLBase
/*     */ {
/*     */   private static final String TAG = "EGLBase10";
/*  52 */   private EGL10 mEgl = null;
/*  53 */   private EGLDisplay mEglDisplay = null;
/*  54 */   private Config mEglConfig = null;
/*  55 */   private int mGlVersion = 2;
/*     */   
/*  57 */   private static final Context EGL_NO_CONTEXT = new Context(EGL10.EGL_NO_CONTEXT); @NonNull
/*  58 */   private Context mContext = EGL_NO_CONTEXT;
/*     */ 
/*     */   
/*     */   public static class Context
/*     */     extends IContext
/*     */   {
/*     */     public final EGLContext eglContext;
/*     */ 
/*     */     
/*  67 */     private Context(EGLContext context) { this.eglContext = context; }
/*     */   }
/*     */   
/*     */   public static class Config
/*     */     extends IConfig
/*     */   {
/*     */     public final EGLConfig eglConfig;
/*     */     
/*  75 */     private Config(EGLConfig eglConfig) { this.eglConfig = eglConfig; }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class MySurfaceHolder
/*     */     implements SurfaceHolder
/*     */   {
/*     */     private final Surface surface;
/*     */ 
/*     */ 
/*     */     
/*  88 */     public MySurfaceHolder(Surface surface) { this.surface = surface; }
/*     */ 
/*     */ 
/*     */     
/*  92 */     public Surface getSurface() { return this.surface; }
/*     */ 
/*     */ 
/*     */     
/*     */     public void addCallback(Callback callback) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void removeCallback(Callback callback) {}
/*     */ 
/*     */     
/* 103 */     public boolean isCreating() { return false; }
/*     */ 
/*     */ 
/*     */     
/*     */     public void setType(int type) {}
/*     */ 
/*     */     
/*     */     public void setFixedSize(int width, int height) {}
/*     */ 
/*     */     
/*     */     public void setSizeFromLayout() {}
/*     */ 
/*     */     
/*     */     public void setFormat(int format) {}
/*     */ 
/*     */     
/*     */     public void setKeepScreenOn(boolean screenOn) {}
/*     */ 
/*     */     
/* 122 */     public Canvas lockCanvas() { return null; }
/*     */ 
/*     */ 
/*     */     
/* 126 */     public Canvas lockCanvas(Rect dirty) { return null; }
/*     */ 
/*     */ 
/*     */     
/*     */     public void unlockCanvasAndPost(Canvas canvas) {}
/*     */ 
/*     */     
/* 133 */     public Rect getSurfaceFrame() { return null; }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class EglSurface
/*     */     implements IEglSurface
/*     */   {
/*     */     private final EGLBase10 mEglBase;
/*     */     
/* 142 */     private EGLSurface mEglSurface = EGL10.EGL_NO_SURFACE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private EglSurface(EGLBase10 eglBase, Object surface) throws IllegalArgumentException {
/* 151 */       this.mEglBase = eglBase;
/* 152 */       if (surface instanceof Surface && !BuildCheck.isAndroid4_2()) {
/*     */ 
/*     */         
/* 155 */         this.mEglSurface = this.mEglBase.createWindowSurface(new MySurfaceHolder((Surface)surface));
/* 156 */       } else if (surface instanceof Surface || surface instanceof SurfaceHolder || surface instanceof android.graphics.SurfaceTexture || surface instanceof android.view.SurfaceView) {
/*     */ 
/*     */ 
/*     */         
/* 160 */         this.mEglSurface = this.mEglBase.createWindowSurface(surface);
/*     */       } else {
/* 162 */         throw new IllegalArgumentException("unsupported surface");
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
/*     */     private EglSurface(EGLBase10 eglBase, int width, int height) {
/* 174 */       this.mEglBase = eglBase;
/* 175 */       if (width <= 0 || height <= 0) {
/* 176 */         this.mEglSurface = this.mEglBase.createOffscreenSurface(1, 1);
/*     */       } else {
/* 178 */         this.mEglSurface = this.mEglBase.createOffscreenSurface(width, height);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void makeCurrent() {
/* 188 */       this.mEglBase.makeCurrent(this.mEglSurface);
/* 189 */       if (this.mEglBase.getGlVersion() >= 2) {
/* 190 */         GLES20.glViewport(0, 0, this.mEglBase.getSurfaceWidth(this.mEglSurface), this.mEglBase.getSurfaceHeight(this.mEglSurface));
/*     */       } else {
/* 192 */         GLES10.glViewport(0, 0, this.mEglBase.getSurfaceWidth(this.mEglSurface), this.mEglBase.getSurfaceHeight(this.mEglSurface));
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 201 */     public void swap() { this.mEglBase.swap(this.mEglSurface); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 206 */     public void swap(long presentationTimeNs) { this.mEglBase.swap(this.mEglSurface, presentationTimeNs); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 211 */     public IContext getContext() { return this.mEglBase.getContext(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setPresentationTime(long presentationTimeNs) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isValid() {
/* 224 */       return (this.mEglSurface != null && this.mEglSurface != EGL10.EGL_NO_SURFACE && this.mEglBase
/* 225 */         .getSurfaceWidth(this.mEglSurface) > 0 && this.mEglBase.getSurfaceHeight(this.mEglSurface) > 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void release() {
/* 234 */       this.mEglBase.makeDefault();
/* 235 */       this.mEglBase.destroyWindowSurface(this.mEglSurface);
/* 236 */       this.mEglSurface = EGL10.EGL_NO_SURFACE;
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
/* 249 */   public EGLBase10(int maxClientVersion, Context sharedContext, boolean withDepthBuffer, int stencilBits, boolean isRecordable) { init(maxClientVersion, sharedContext, withDepthBuffer, stencilBits, isRecordable); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void release() {
/* 258 */     destroyContext();
/* 259 */     this.mContext = EGL_NO_CONTEXT;
/* 260 */     if (this.mEgl == null)
/* 261 */       return;  this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
/*     */     
/* 263 */     this.mEgl.eglTerminate(this.mEglDisplay);
/* 264 */     this.mEglDisplay = null;
/* 265 */     this.mEglConfig = null;
/* 266 */     this.mEgl = null;
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
/* 278 */     EglSurface eglSurface = new EglSurface(this, nativeWindow);
/* 279 */     eglSurface.makeCurrent();
/* 280 */     return eglSurface;
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
/* 293 */     EglSurface eglSurface = new EglSurface(this, width, height);
/* 294 */     eglSurface.makeCurrent();
/* 295 */     return eglSurface;
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
/* 306 */   public Context getContext() { return this.mContext; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 315 */   public Config getConfig() { return this.mEglConfig; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void makeDefault() {
/* 324 */     if (!this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT)) {
/* 325 */       Log.w("EGLBase10", "makeDefault:eglMakeCurrent:err=" + this.mEgl.eglGetError());
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
/* 337 */     this.mEgl.eglWaitGL();
/* 338 */     this.mEgl.eglWaitNative(12379, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 348 */   public String queryString(int what) { return this.mEgl.eglQueryString(this.mEglDisplay, what); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 357 */   public int getGlVersion() { return this.mGlVersion; }
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
/*     */   private final void init(int maxClientVersion, @Nullable Context sharedContext, boolean withDepthBuffer, int stencilBits, boolean isRecordable) {
/* 371 */     sharedContext = (sharedContext != null) ? sharedContext : EGL_NO_CONTEXT;
/* 372 */     if (this.mEgl == null) {
/* 373 */       this.mEgl = (EGL10)EGLContext.getEGL();
/* 374 */       this.mEglDisplay = this.mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
/* 375 */       if (this.mEglDisplay == EGL10.EGL_NO_DISPLAY) {
/* 376 */         throw new RuntimeException("eglGetDisplay failed");
/*     */       }
/*     */       
/* 379 */       int[] version = new int[2];
/* 380 */       if (!this.mEgl.eglInitialize(this.mEglDisplay, version)) {
/* 381 */         this.mEglDisplay = null;
/* 382 */         throw new RuntimeException("eglInitialize failed");
/*     */       } 
/*     */     } 
/*     */     
/* 386 */     if (maxClientVersion >= 3) {
/*     */       
/* 388 */       EGLConfig config = getConfig(3, withDepthBuffer, stencilBits, isRecordable);
/* 389 */       if (config != null) {
/* 390 */         EGLContext context = createContext(sharedContext, config, 3);
/* 391 */         if (this.mEgl.eglGetError() == 12288) {
/*     */           
/* 393 */           this.mEglConfig = new Config(config);
/* 394 */           this.mContext = new Context(context);
/* 395 */           this.mGlVersion = 3;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 400 */     if (maxClientVersion >= 2 && (this.mContext == null || this.mContext.eglContext == EGL10.EGL_NO_CONTEXT)) {
/* 401 */       EGLConfig config = getConfig(2, withDepthBuffer, stencilBits, isRecordable);
/* 402 */       if (config == null) {
/* 403 */         throw new RuntimeException("chooseConfig failed");
/*     */       }
/*     */       
/*     */       try {
/* 407 */         EGLContext context = createContext(sharedContext, config, 2);
/* 408 */         checkEglError("eglCreateContext");
/* 409 */         this.mEglConfig = new Config(config);
/* 410 */         this.mContext = new Context(context);
/* 411 */         this.mGlVersion = 2;
/* 412 */       } catch (Exception e) {
/* 413 */         if (isRecordable) {
/* 414 */           config = getConfig(2, withDepthBuffer, stencilBits, false);
/* 415 */           if (config == null) {
/* 416 */             throw new RuntimeException("chooseConfig failed");
/*     */           }
/*     */           
/* 419 */           EGLContext context = createContext(sharedContext, config, 2);
/* 420 */           checkEglError("eglCreateContext");
/* 421 */           this.mEglConfig = new Config(config);
/* 422 */           this.mContext = new Context(context);
/* 423 */           this.mGlVersion = 2;
/*     */         } 
/*     */       } 
/*     */     } 
/* 427 */     if (this.mContext == null || this.mContext.eglContext == EGL10.EGL_NO_CONTEXT) {
/* 428 */       EGLConfig config = getConfig(1, withDepthBuffer, stencilBits, isRecordable);
/* 429 */       if (config == null) {
/* 430 */         throw new RuntimeException("chooseConfig failed");
/*     */       }
/*     */       
/* 433 */       EGLContext context = createContext(sharedContext, config, 1);
/* 434 */       checkEglError("eglCreateContext");
/* 435 */       this.mEglConfig = new Config(config);
/* 436 */       this.mContext = new Context(context);
/* 437 */       this.mGlVersion = 1;
/*     */     } 
/*     */     
/* 440 */     int[] values = new int[1];
/* 441 */     this.mEgl.eglQueryContext(this.mEglDisplay, this.mContext.eglContext, 12440, values);
/* 442 */     Log.d("EGLBase10", "EGLContext created, client version " + values[0]);
/* 443 */     makeDefault();
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
/*     */   private final boolean makeCurrent(EGLSurface surface) {
/* 455 */     if (surface == null || surface == EGL10.EGL_NO_SURFACE) {
/* 456 */       int error = this.mEgl.eglGetError();
/* 457 */       if (error == 12299) {
/* 458 */         Log.e("EGLBase10", "makeCurrent:EGL_BAD_NATIVE_WINDOW");
/*     */       }
/* 460 */       return false;
/*     */     } 
/*     */     
/* 463 */     if (!this.mEgl.eglMakeCurrent(this.mEglDisplay, surface, surface, this.mContext.eglContext)) {
/* 464 */       Log.w("TAG", "eglMakeCurrent" + this.mEgl.eglGetError());
/* 465 */       return false;
/*     */     } 
/* 467 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private final int swap(EGLSurface surface) {
/* 472 */     if (!this.mEgl.eglSwapBuffers(this.mEglDisplay, surface)) {
/* 473 */       int err = this.mEgl.eglGetError();
/*     */       
/* 475 */       return err;
/*     */     } 
/* 477 */     return 12288;
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
/*     */   private final int swap(EGLSurface surface, long ignored) {
/* 490 */     if (!this.mEgl.eglSwapBuffers(this.mEglDisplay, surface)) {
/* 491 */       int err = this.mEgl.eglGetError();
/*     */       
/* 493 */       return err;
/*     */     } 
/* 495 */     return 12288;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final EGLContext createContext(@NonNull Context sharedContext, EGLConfig config, int version) {
/* 501 */     int[] attrib_list = { 12440, version, 12344 };
/*     */ 
/*     */ 
/*     */     
/* 505 */     EGLContext context = this.mEgl.eglCreateContext(this.mEglDisplay, config, sharedContext.eglContext, attrib_list);
/*     */     
/* 507 */     return context;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final void destroyContext() {
/* 513 */     if (!this.mEgl.eglDestroyContext(this.mEglDisplay, this.mContext.eglContext)) {
/* 514 */       Log.e("destroyContext", "display:" + this.mEglDisplay + " context: " + this.mContext.eglContext);
/* 515 */       Log.e("EGLBase10", "eglDestroyContext:" + this.mEgl.eglGetError());
/*     */     } 
/* 517 */     this.mContext = EGL_NO_CONTEXT;
/*     */   }
/*     */   
/*     */   private final int getSurfaceWidth(EGLSurface surface) {
/* 521 */     int[] value = new int[1];
/* 522 */     boolean ret = this.mEgl.eglQuerySurface(this.mEglDisplay, surface, 12375, value);
/* 523 */     if (!ret) value[0] = 0; 
/* 524 */     return value[0];
/*     */   }
/*     */   
/*     */   private final int getSurfaceHeight(EGLSurface surface) {
/* 528 */     int[] value = new int[1];
/* 529 */     boolean ret = this.mEgl.eglQuerySurface(this.mEglDisplay, surface, 12374, value);
/* 530 */     if (!ret) value[0] = 0; 
/* 531 */     return value[0];
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
/* 542 */     int[] surfaceAttribs = { 12344 };
/*     */ 
/*     */     
/* 545 */     EGLSurface result = null;
/*     */     try {
/* 547 */       result = this.mEgl.eglCreateWindowSurface(this.mEglDisplay, this.mEglConfig.eglConfig, nativeWindow, surfaceAttribs);
/* 548 */       if (result == null || result == EGL10.EGL_NO_SURFACE) {
/* 549 */         int error = this.mEgl.eglGetError();
/* 550 */         if (error == 12299) {
/* 551 */           Log.e("EGLBase10", "createWindowSurface returned EGL_BAD_NATIVE_WINDOW.");
/*     */         }
/* 553 */         throw new RuntimeException("createWindowSurface failed error=" + error);
/*     */       } 
/* 555 */       makeCurrent(result);
/*     */     }
/* 557 */     catch (Exception e) {
/* 558 */       Log.e("EGLBase10", "eglCreateWindowSurface", e);
/* 559 */       throw new IllegalArgumentException(e);
/*     */     } 
/* 561 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final EGLSurface createOffscreenSurface(int width, int height) {
/* 569 */     int[] surfaceAttribs = { 12375, width, 12374, height, 12344 };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 574 */     this.mEgl.eglWaitGL();
/* 575 */     EGLSurface result = null;
/*     */     try {
/* 577 */       result = this.mEgl.eglCreatePbufferSurface(this.mEglDisplay, this.mEglConfig.eglConfig, surfaceAttribs);
/* 578 */       checkEglError("eglCreatePbufferSurface");
/* 579 */       if (result == null) {
/* 580 */         throw new RuntimeException("surface was null");
/*     */       }
/* 582 */     } catch (IllegalArgumentException e) {
/* 583 */       Log.e("EGLBase10", "createOffscreenSurface", e);
/* 584 */     } catch (RuntimeException e) {
/* 585 */       Log.e("EGLBase10", "createOffscreenSurface", e);
/*     */     } 
/* 587 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final void destroyWindowSurface(EGLSurface surface) {
/* 593 */     if (surface != EGL10.EGL_NO_SURFACE) {
/* 594 */       this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
/*     */       
/* 596 */       this.mEgl.eglDestroySurface(this.mEglDisplay, surface);
/*     */     } 
/* 598 */     surface = EGL10.EGL_NO_SURFACE;
/*     */   }
/*     */ 
/*     */   
/*     */   private final void checkEglError(String msg) {
/*     */     int error;
/* 604 */     if ((error = this.mEgl.eglGetError()) != 12288) {
/* 605 */       throw new RuntimeException(msg + ": EGL error: 0x" + Integer.toHexString(error));
/*     */     }
/*     */   }
/*     */   
/*     */   private final EGLConfig getConfig(int version, boolean hasDepthBuffer, int stencilBits, boolean isRecordable) {
/* 610 */     int renderableType = 4;
/* 611 */     if (version >= 3) {
/* 612 */       renderableType |= 0x40;
/*     */     }
/*     */     
/* 615 */     int[] attribList = { 12352, renderableType, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12344, 12344, 12344, 12344, 12344, 12344, 12344 };
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
/* 628 */     int offset = 10;
/* 629 */     if (stencilBits > 0) {
/* 630 */       attribList[offset++] = 12326;
/* 631 */       attribList[offset++] = 8;
/*     */     } 
/* 633 */     if (hasDepthBuffer) {
/* 634 */       attribList[offset++] = 12325;
/* 635 */       attribList[offset++] = 16;
/*     */     } 
/* 637 */     if (isRecordable && BuildCheck.isAndroid4_3()) {
/* 638 */       attribList[offset++] = 12610;
/* 639 */       attribList[offset++] = 1;
/*     */     } 
/* 641 */     for (int i = attribList.length - 1; i >= offset; i--) {
/* 642 */       attribList[i] = 12344;
/*     */     }
/* 644 */     EGLConfig config = internalGetConfig(attribList);
/* 645 */     if (config == null && version == 2 && 
/* 646 */       isRecordable) {
/*     */       
/* 648 */       int n = attribList.length;
/* 649 */       for (int i = 10; i < n - 1; i += 2) {
/* 650 */         if (attribList[i] == 12610) {
/* 651 */           for (int j = i; j < n; j++) {
/* 652 */             attribList[j] = 12344;
/*     */           }
/*     */           break;
/*     */         } 
/*     */       } 
/* 657 */       config = internalGetConfig(attribList);
/*     */     } 
/*     */     
/* 660 */     if (config == null) {
/* 661 */       Log.w("EGLBase10", "try to fallback to RGB565");
/* 662 */       attribList[3] = 5;
/* 663 */       attribList[5] = 6;
/* 664 */       attribList[7] = 5;
/* 665 */       config = internalGetConfig(attribList);
/*     */     } 
/* 667 */     return config;
/*     */   }
/*     */   
/*     */   private EGLConfig internalGetConfig(int[] attribList) {
/* 671 */     EGLConfig[] configs = new EGLConfig[1];
/* 672 */     int[] numConfigs = new int[1];
/* 673 */     if (!this.mEgl.eglChooseConfig(this.mEglDisplay, attribList, configs, configs.length, numConfigs)) {
/* 674 */       return null;
/*     */     }
/* 676 */     return configs[0];
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\glutils\EGLBase10.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */