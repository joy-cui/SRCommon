/*     */ package com.serenegiant.glutils;
/*     */ 
/*     */ import android.graphics.Bitmap;
/*     */ import android.opengl.GLES20;
/*     */ import android.support.annotation.NonNull;
/*     */ import android.support.annotation.Nullable;
/*     */ import android.util.Log;
/*     */ import android.util.SparseArray;
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
/*     */ public class StaticTextureSource
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*  39 */   private static final String TAG = StaticTextureSource.class.getSimpleName();
/*     */   
/*  41 */   private final Object mSync = new Object();
/*     */   private RendererTask mRendererTask;
/*     */   private volatile boolean isRunning;
/*     */   private static final int REQUEST_DRAW = 1;
/*     */   private static final int REQUEST_ADD_SURFACE = 3;
/*     */   private static final int REQUEST_REMOVE_SURFACE = 4;
/*     */   private static final int REQUEST_SET_BITMAP = 7;
/*     */   private Runnable mOnFrameTask;
/*     */   
/*  50 */   public StaticTextureSource(float fps) { this(null, fps); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   public StaticTextureSource(@Nullable Bitmap bitmap) { this(bitmap, 10.0F); }
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
/*  83 */   public boolean isRunning() { return this.isRunning; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void release() {
/*  91 */     this.isRunning = false;
/*  92 */     synchronized (this.mSync) {
/*  93 */       this.mSync.notifyAll();
/*     */     } 
/*  95 */     if (this.mRendererTask != null) {
/*  96 */       this.mRendererTask.release();
/*     */     }
/*  98 */     synchronized (this.mSync) {
/*  99 */       this.mRendererTask = null;
/* 100 */       this.mSync.notifyAll();
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
/*     */   public void addSurface(int id, Object surface, boolean isRecordable) {
/* 113 */     synchronized (this.mSync) {
/* 114 */       this.mRendererTask.addSurface(id, surface);
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
/*     */   public void addSurface(int id, Object surface, boolean isRecordable, int maxFps) {
/* 127 */     synchronized (this.mSync) {
/* 128 */       this.mRendererTask.addSurface(id, surface, maxFps);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeSurface(int id) {
/* 138 */     synchronized (this.mSync) {
/* 139 */       this.mRendererTask.removeSurface(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void requestFrame() {
/* 148 */     synchronized (this.mSync) {
/* 149 */       this.mRendererTask.removeRequest(1);
/* 150 */       this.mRendererTask.offer(1);
/* 151 */       this.mSync.notify();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 160 */   public int getCount() { return this.mRendererTask.getCount(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBitmap(Bitmap bitmap) {
/* 170 */     if (bitmap != null) {
/* 171 */       synchronized (this.mSync) {
/* 172 */         this.mRendererTask.setBitmap(bitmap);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getWidth() {
/* 182 */     synchronized (this.mSync) {
/* 183 */       return (this.mRendererTask != null) ? this.mRendererTask.mVideoWidth : 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 192 */     synchronized (this.mSync) {
/* 193 */       return (this.mRendererTask != null) ? this.mRendererTask.mVideoHeight : 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class RendererTask
/*     */     extends EglTask
/*     */   {
/* 203 */     private final Object mClientSync = new Object();
/* 204 */     private final SparseArray<RendererSurfaceRec> mClients = new SparseArray();
/*     */     
/*     */     private final StaticTextureSource mParent;
/*     */     
/*     */     private final long mIntervalsNs;
/*     */     private GLDrawer2D mDrawer;
/*     */     
/*     */     public RendererTask(StaticTextureSource parent, int width, int height, float fps) {
/* 212 */       super(3, null, 0);
/* 213 */       this.mParent = parent;
/* 214 */       this.mVideoWidth = width;
/* 215 */       this.mVideoHeight = height;
/* 216 */       this.mIntervalsNs = (fps <= 0.0F) ? 100000000L : (long)(1.0E9F / fps);
/*     */     }
/*     */ 
/*     */     
/*     */     private int mVideoWidth;
/*     */     private int mVideoHeight;
/*     */     private TextureOffscreen mImageSource;
/*     */     
/*     */     protected void onStart() {
/* 225 */       this.mDrawer = new GLDrawer2D(false);
/* 226 */       synchronized (this.mParent.mSync) {
/* 227 */         this.mParent.isRunning = true;
/* 228 */         this.mParent.mSync.notifyAll();
/*     */       } 
/* 230 */       (new Thread(this.mParent.mOnFrameTask, TAG)).start();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void onStop() {
/* 240 */       synchronized (this.mParent.mSync) {
/* 241 */         this.mParent.isRunning = false;
/* 242 */         this.mParent.mSync.notifyAll();
/*     */       } 
/* 244 */       makeCurrent();
/* 245 */       if (this.mDrawer != null) {
/* 246 */         this.mDrawer.release();
/* 247 */         this.mDrawer = null;
/*     */       } 
/* 249 */       if (this.mImageSource != null) {
/* 250 */         this.mImageSource.release();
/* 251 */         this.mImageSource = null;
/*     */       } 
/* 253 */       handleRemoveAll();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 260 */     protected boolean onError(Exception e) { return false; }
/*     */ 
/*     */ 
/*     */     
/*     */     protected Object processRequest(int request, int arg1, int arg2, Object obj) {
/* 265 */       switch (request) {
/*     */         case 1:
/* 267 */           handleDraw();
/*     */           break;
/*     */         case 3:
/* 270 */           handleAddSurface(arg1, obj, arg2);
/*     */           break;
/*     */         case 4:
/* 273 */           handleRemoveSurface(arg1);
/*     */           break;
/*     */         case 7:
/* 276 */           handleSetBitmap((Bitmap)obj);
/*     */           break;
/*     */       } 
/* 279 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 288 */     public void addSurface(int id, Object surface) { addSurface(id, surface, -1); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addSurface(int id, Object surface, int maxFps) {
/* 297 */       checkFinished();
/* 298 */       if (!(surface instanceof android.graphics.SurfaceTexture) && !(surface instanceof android.view.Surface) && !(surface instanceof android.view.SurfaceHolder)) {
/* 299 */         throw new IllegalArgumentException("Surface should be one of Surface, SurfaceTexture or SurfaceHolder");
/*     */       }
/* 301 */       synchronized (this.mClientSync) {
/* 302 */         if (surface != null && this.mClients.get(id) == null) {
/*     */           while (true) {
/* 304 */             if (offer(3, id, maxFps, surface)) {
/*     */               try {
/* 306 */                 this.mClientSync.wait(); break;
/* 307 */               } catch (InterruptedException interruptedException) {
/*     */                 break;
/*     */               } 
/*     */             }
/*     */             
/*     */             try {
/* 313 */               this.mClientSync.wait(10L);
/* 314 */             } catch (InterruptedException e) {
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void removeSurface(int id) {
/* 328 */       synchronized (this.mClientSync) {
/* 329 */         if (this.mClients.get(id) != null) {
/*     */           while (true) {
/* 331 */             if (offer(4, id)) {
/*     */               try {
/* 333 */                 this.mClientSync.wait(); break;
/* 334 */               } catch (InterruptedException interruptedException) {
/*     */                 break;
/*     */               } 
/*     */             }
/*     */             
/*     */             try {
/* 340 */               this.mClientSync.wait(10L);
/* 341 */             } catch (InterruptedException e) {
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 355 */     public void setBitmap(@NonNull Bitmap bitmap) { offer(7, bitmap); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getCount() {
/* 363 */       synchronized (this.mClientSync) {
/* 364 */         return this.mClients.size();
/*     */       } 
/*     */     }
/*     */     
/*     */     private void checkFinished() {
/* 369 */       if (isFinished()) {
/* 370 */         throw new RuntimeException("already finished");
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
/*     */     private void handleDraw() {
/* 382 */       makeCurrent();
/*     */       
/* 384 */       if (this.mImageSource != null) {
/* 385 */         int texId = this.mImageSource.getTexture();
/* 386 */         synchronized (this.mClientSync) {
/* 387 */           int n = this.mClients.size();
/*     */           
/* 389 */           for (int i = n - 1; i >= 0; i--) {
/* 390 */             RendererSurfaceRec client = (RendererSurfaceRec)this.mClients.valueAt(i);
/* 391 */             if (client != null && client.canDraw()) {
/*     */               try {
/* 393 */                 client.draw(this.mDrawer, texId, null);
/* 394 */                 GLHelper.checkGlError("handleSetBitmap");
/* 395 */               } catch (Exception e) {
/*     */                 
/* 397 */                 this.mClients.removeAt(i);
/* 398 */                 client.release();
/*     */               } 
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } else {
/* 404 */         Log.w(TAG, "mImageSource is not ready");
/*     */       } 
/* 406 */       GLES20.glClear(16384);
/* 407 */       GLES20.glFlush();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void handleAddSurface(int id, Object surface, int maxFps) {
/* 418 */       checkSurface();
/* 419 */       synchronized (this.mClientSync) {
/* 420 */         RendererSurfaceRec client = (RendererSurfaceRec)this.mClients.get(id);
/* 421 */         if (client == null) {
/*     */           try {
/* 423 */             client = RendererSurfaceRec.newInstance(getEgl(), surface, maxFps);
/* 424 */             this.mClients.append(id, client);
/* 425 */           } catch (Exception e) {
/* 426 */             Log.w(TAG, "invalid surface: surface=" + surface, e);
/*     */           } 
/*     */         } else {
/* 429 */           Log.w(TAG, "surface is already added: id=" + id);
/*     */         } 
/* 431 */         this.mClientSync.notifyAll();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void handleRemoveSurface(int id) {
/* 441 */       synchronized (this.mClientSync) {
/* 442 */         RendererSurfaceRec client = (RendererSurfaceRec)this.mClients.get(id);
/* 443 */         if (client != null) {
/* 444 */           this.mClients.remove(id);
/* 445 */           client.release();
/*     */         } 
/* 447 */         checkSurface();
/* 448 */         this.mClientSync.notifyAll();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void handleRemoveAll() {
/* 457 */       synchronized (this.mClientSync) {
/* 458 */         int n = this.mClients.size();
/*     */         
/* 460 */         for (int i = 0; i < n; i++) {
/* 461 */           RendererSurfaceRec client = (RendererSurfaceRec)this.mClients.valueAt(i);
/* 462 */           if (client != null) {
/* 463 */             makeCurrent();
/* 464 */             client.release();
/*     */           } 
/*     */         } 
/* 467 */         this.mClients.clear();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void checkSurface() {
/* 477 */       synchronized (this.mClientSync) {
/* 478 */         int n = this.mClients.size();
/* 479 */         for (int i = 0; i < n; i++) {
/* 480 */           RendererSurfaceRec client = (RendererSurfaceRec)this.mClients.valueAt(i);
/* 481 */           if (client != null && !client.isValid()) {
/* 482 */             int id = this.mClients.keyAt(i);
/*     */             
/* 484 */             ((RendererSurfaceRec)this.mClients.valueAt(i)).release();
/* 485 */             this.mClients.remove(id);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void handleSetBitmap(Bitmap bitmap) {
/* 498 */       int width = bitmap.getWidth();
/* 499 */       int height = bitmap.getHeight();
/* 500 */       if (this.mImageSource == null) {
/* 501 */         this.mImageSource = new TextureOffscreen(width, height, false);
/* 502 */         GLHelper.checkGlError("handleSetBitmap");
/* 503 */         this.mImageSource.loadBitmap(bitmap);
/*     */       } else {
/* 505 */         this.mImageSource.loadBitmap(bitmap);
/*     */       } 
/* 507 */       this.mVideoWidth = width;
/* 508 */       this.mVideoHeight = height;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StaticTextureSource(@Nullable Bitmap bitmap, float fps) {
/* 516 */     this.mOnFrameTask = new Runnable()
/*     */       {
/*     */         public void run() {
/* 519 */           long ms = StaticTextureSource.this.mRendererTask.mIntervalsNs / 1000000L;
/* 520 */           int ns = (int)(StaticTextureSource.this.mRendererTask.mIntervalsNs % 1000000L);
/* 521 */           while (StaticTextureSource.this.isRunning && 
/* 522 */             StaticTextureSource.this.mRendererTask != null) {
/* 523 */             synchronized (StaticTextureSource.this.mSync) {
/*     */               try {
/* 525 */                 StaticTextureSource.this.mSync.wait(ms, ns);
/* 526 */                 if (StaticTextureSource.this.mRendererTask.mImageSource != null) {
/* 527 */                   StaticTextureSource.this.mRendererTask.removeRequest(1);
/* 528 */                   StaticTextureSource.this.mRendererTask.offer(1);
/* 529 */                   StaticTextureSource.this.mSync.notify();
/*     */                 } 
/* 531 */               } catch (Exception e) {
/* 532 */                 Log.w(TAG, e);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         }
/*     */       };
/*     */     int width = (bitmap != null) ? bitmap.getWidth() : 1;
/*     */     int height = (bitmap != null) ? bitmap.getHeight() : 1;
/*     */     this.mRendererTask = new RendererTask(this, width, height, fps);
/*     */     (new Thread((Runnable)this.mRendererTask, TAG)).start();
/*     */     if (!this.mRendererTask.waitReady())
/*     */       throw new RuntimeException("failed to start renderer thread"); 
/*     */     setBitmap(bitmap);
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\glutils\StaticTextureSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */