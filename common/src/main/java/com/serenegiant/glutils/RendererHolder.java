/*     */ package com.serenegiant.glutils;
/*     */ 
/*     */ import android.annotation.SuppressLint;
/*     */ import android.graphics.Bitmap;
/*     */ import android.graphics.SurfaceTexture;
/*     */ import android.opengl.GLES20;
/*     */ import android.support.annotation.Nullable;
/*     */ import android.util.Log;
/*     */ import android.util.SparseArray;
/*     */ import android.view.Surface;
/*     */ import com.serenegiant.utils.BuildCheck;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
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
/*     */ public class RendererHolder
/*     */   implements IRendererHolder
/*     */ {
/*  49 */   private static final String TAG = RendererHolder.class.getSimpleName();
/*     */   
/*  51 */   private final Object mSync = new Object();
/*     */ 
/*     */   
/*     */   private final RenderHolderCallback mCallback;
/*     */ 
/*     */   
/*     */   private volatile boolean isRunning;
/*     */ 
/*     */   
/*     */   private File mCaptureFile;
/*     */ 
/*     */   
/*     */   private final RendererTask mRendererTask;
/*     */ 
/*     */   
/*     */   private static final int REQUEST_DRAW = 1;
/*     */ 
/*     */   
/*     */   private static final int REQUEST_UPDATE_SIZE = 2;
/*     */ 
/*     */   
/*     */   private static final int REQUEST_ADD_SURFACE = 3;
/*     */ 
/*     */   
/*     */   private static final int REQUEST_REMOVE_SURFACE = 4;
/*     */   
/*     */   private static final int REQUEST_RECREATE_MASTER_SURFACE = 5;
/*     */   
/*     */   private static final int REQUEST_MIRROR = 6;
/*     */   
/*     */   private final Runnable mCaptureTask;
/*     */ 
/*     */   
/*  84 */   public boolean isRunning() { return this.isRunning; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void release() {
/*  93 */     this.mRendererTask.release();
/*  94 */     synchronized (this.mSync) {
/*  95 */       this.isRunning = false;
/*  96 */       this.mSync.notifyAll();
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
/* 107 */   public Surface getSurface() { return this.mRendererTask.getSurface(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   public SurfaceTexture getSurfaceTexture() { return this.mRendererTask.getSurfaceTexture(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   public void reset() { this.mRendererTask.checkMasterSurface(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 134 */   public void resize(int width, int height) { this.mRendererTask.resize(width, height); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   public void setMirror(int mirror) { this.mRendererTask.mirror(mirror % 4); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 149 */   public int getMirror() { return this.mRendererTask.mirror(); }
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
/* 161 */   public void addSurface(int id, Object surface, boolean isRecordable) { this.mRendererTask.addSurface(id, surface); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 167 */   public void addSurface(int id, Object surface, boolean isRecordable, int maxFps) { this.mRendererTask.addSurface(id, surface, maxFps); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 177 */   public void removeSurface(int id) { this.mRendererTask.removeSurface(id); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 187 */   public boolean isEnabled(int id) { return this.mRendererTask.isEnabled(id); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 197 */   public void setEnabled(int id, boolean enable) { this.mRendererTask.setEnabled(id, enable); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void requestFrame() {
/* 206 */     this.mRendererTask.removeRequest(1);
/* 207 */     this.mRendererTask.offer(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 216 */   public int getCount() { return this.mRendererTask.getCount(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void captureStillAsync(String path) {
/* 227 */     File file = new File(path);
/* 228 */     synchronized (this.mSync) {
/* 229 */       this.mCaptureFile = file;
/* 230 */       this.mSync.notifyAll();
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
/*     */   public void captureStill(String path) {
/* 242 */     File file = new File(path);
/* 243 */     synchronized (this.mSync) {
/* 244 */       this.mCaptureFile = file;
/* 245 */       this.mSync.notifyAll();
/*     */       
/*     */       try {
/* 248 */         this.mSync.wait();
/* 249 */       } catch (InterruptedException interruptedException) {}
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
/*     */   private static final class RendererTask
/*     */     extends EglTask
/*     */   {
/* 272 */     private final Object mClientSync = new Object();
/* 273 */     private final SparseArray<RendererSurfaceRec> mClients = new SparseArray();
/*     */     private final RendererHolder mParent;
/*     */     private GLDrawer2D mDrawer;
/*     */     private int mTexId;
/*     */     private SurfaceTexture mMasterTexture;
/* 278 */     final float[] mTexMatrix = new float[16]; private Surface mMasterSurface;
/*     */     private int mVideoWidth;
/*     */     private int mVideoHeight;
/* 281 */     private int mMirror = 0;
/*     */     private final SurfaceTexture.OnFrameAvailableListener mOnFrameAvailableListener;
/*     */     
/*     */     public RendererTask(RendererHolder parent, int width, int height) {
/* 285 */       super(3, null, 2);
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
/* 754 */       this.mOnFrameAvailableListener = new SurfaceTexture.OnFrameAvailableListener()
/*     */         {
/*     */           public void onFrameAvailable(SurfaceTexture surfaceTexture) {
/* 757 */             RendererTask.this.offer(1);
/*     */           }
/*     */         }; this.mParent = parent; this.mVideoWidth = width; this.mVideoHeight = height;
/*     */     }
/*     */     protected void onStart() { this.mDrawer = new GLDrawer2D(true); handleReCreateMasterSurface(); synchronized (this.mParent.mSync) { this.mParent.isRunning = true; this.mParent.mSync.notifyAll(); }  }
/*     */     protected void onStop() { synchronized (this.mParent.mSync) { this.mParent.isRunning = false; this.mParent.mSync.notifyAll(); }  makeCurrent(); if (this.mDrawer != null) { this.mDrawer.release(); this.mDrawer = null; }  handleReleaseMasterSurface(); handleRemoveAll(); }
/*     */     protected boolean onError(Exception e) { return false; }
/*     */     protected Object processRequest(int request, int arg1, int arg2, Object obj) { switch (request) { case 1: handleDraw(); break;case 2: handleResize(arg1, arg2); break;case 3: handleAddSurface(arg1, obj, arg2); break;case 4: handleRemoveSurface(arg1); break;case 5: handleReCreateMasterSurface(); break;case 6: handleMirror(arg1); break; }  return null; }
/*     */     public Surface getSurface() { checkMasterSurface(); return this.mMasterSurface; } public SurfaceTexture getSurfaceTexture() { checkMasterSurface(); return this.mMasterTexture; } public void addSurface(int id, Object surface) { addSurface(id, surface, -1); } public void addSurface(int id, Object surface, int maxFps) { checkFinished(); if (!(surface instanceof SurfaceTexture) && !(surface instanceof Surface) && !(surface instanceof android.view.SurfaceHolder)) throw new IllegalArgumentException("Surface should be one of Surface, SurfaceTexture or SurfaceHolder");  synchronized (this.mClientSync) { if (this.mClients.get(id) == null) while (isRunning()) { if (offer(3, id, maxFps, surface)) try { this.mClientSync.wait(); break; } catch (InterruptedException interruptedException) { break; }   try { this.mClientSync.wait(10L); } catch (InterruptedException e) { break; }  }   }  } public void removeSurface(int id) { synchronized (this.mClientSync) { if (this.mClients.get(id) != null) while (isRunning()) { if (offer(4, id)) try { this.mClientSync.wait(); break; } catch (InterruptedException interruptedException) { break; }   try { this.mClientSync.wait(10L); } catch (InterruptedException e) { break; }  }   }  } public boolean isEnabled(int id) { synchronized (this.mClientSync) { RendererSurfaceRec rec = (RendererSurfaceRec)this.mClients.get(id); return (rec != null && rec.isEnabled()); }  } public void setEnabled(int id, boolean enable) { synchronized (this.mClientSync) { RendererSurfaceRec rec = (RendererSurfaceRec)this.mClients.get(id); if (rec != null) rec.setEnabled(enable);  }  } public int getCount() { synchronized (this.mClientSync) { return this.mClients.size(); }  } public void resize(int width, int height) { checkFinished(); if (this.mVideoWidth != width || this.mVideoHeight != height) offer(2, width, height);  } public void mirror(int mirror) { checkFinished(); if (this.mMirror != mirror) offer(6, mirror);  } public int mirror() { return this.mMirror; } public void checkMasterSurface() { checkFinished(); if (this.mMasterSurface == null || !this.mMasterSurface.isValid()) { Log.d(TAG, "checkMasterSurface:invalid master surface"); offerAndWait(5, 0, 0, null); }  } private void checkFinished() { if (isFinished()) throw new RuntimeException("already finished");  } private void handleDraw() { if (this.mMasterSurface == null || !this.mMasterSurface.isValid()) { Log.w(TAG, "checkMasterSurface:invalid master surface"); offer(5); return; }  try { makeCurrent(); this.mMasterTexture.updateTexImage(); this.mMasterTexture.getTransformMatrix(this.mTexMatrix); } catch (Exception e) { Log.w(TAG, "draw:thread id =" + Thread.currentThread().getId(), e); offer(5); return; }  synchronized (this.mParent.mCaptureTask) { this.mParent.mCaptureTask.notify(); }  synchronized (this.mClientSync) { int n = this.mClients.size(); for (int i = n - 1; i >= 0; i--) { RendererSurfaceRec client = (RendererSurfaceRec)this.mClients.valueAt(i); if (client != null && client.canDraw()) try { client.draw(this.mDrawer, this.mTexId, this.mTexMatrix); } catch (Exception e) { this.mClients.removeAt(i); client.release(); }   }  }  if (this.mParent.mCallback != null) try { this.mParent.mCallback.onFrameAvailable(); } catch (Exception exception) {}  GLES20.glClear(16384); GLES20.glFlush(); } private void handleAddSurface(int id, Object surface, int maxFps) { checkSurface(); synchronized (this.mClientSync) { RendererSurfaceRec client = (RendererSurfaceRec)this.mClients.get(id); if (client == null) { try { client = RendererSurfaceRec.newInstance(getEgl(), surface, maxFps); setMirror(client, this.mMirror); this.mClients.append(id, client); } catch (Exception e) { Log.w(TAG, "invalid surface: surface=" + surface, e); }  } else { Log.w(TAG, "surface is already added: id=" + id); }  this.mClientSync.notifyAll(); }  } private void handleRemoveSurface(int id) { synchronized (this.mClientSync) { RendererSurfaceRec client = (RendererSurfaceRec)this.mClients.get(id); if (client != null) { this.mClients.remove(id); client.release(); }  checkSurface(); this.mClientSync.notifyAll(); }  } private void handleRemoveAll() { synchronized (this.mClientSync) { int n = this.mClients.size(); for (int i = 0; i < n; i++) { RendererSurfaceRec client = (RendererSurfaceRec)this.mClients.valueAt(i); if (client != null) { makeCurrent(); client.release(); }  }  this.mClients.clear(); }  } private void checkSurface() { synchronized (this.mClientSync) { int n = this.mClients.size(); for (int i = 0; i < n; i++) { RendererSurfaceRec client = (RendererSurfaceRec)this.mClients.valueAt(i); if (client != null && !client.isValid()) { int id = this.mClients.keyAt(i); ((RendererSurfaceRec)this.mClients.valueAt(i)).release(); this.mClients.remove(id); }  }  }  } @SuppressLint({"NewApi"}) private void handleReCreateMasterSurface() { makeCurrent(); handleReleaseMasterSurface(); makeCurrent(); this.mTexId = GLHelper.initTex(36197, 9728); this.mMasterTexture = new SurfaceTexture(this.mTexId); this.mMasterSurface = new Surface(this.mMasterTexture); if (BuildCheck.isAndroid4_1()) this.mMasterTexture.setDefaultBufferSize(this.mVideoWidth, this.mVideoHeight);  this.mMasterTexture.setOnFrameAvailableListener(this.mOnFrameAvailableListener); try { if (this.mParent.mCallback != null) this.mParent.mCallback.onCreate(this.mMasterSurface);  } catch (Exception e) { Log.w(TAG, e); }  } private void handleReleaseMasterSurface() { try { if (this.mParent.mCallback != null) this.mParent.mCallback.onDestroy();  } catch (Exception e) { Log.w(TAG, e); }  this.mMasterSurface = null; if (this.mMasterTexture != null) { this.mMasterTexture.release(); this.mMasterTexture = null; }  if (this.mTexId != 0) { GLHelper.deleteTex(this.mTexId); this.mTexId = 0; }  } @SuppressLint({"NewApi"}) private void handleResize(int width, int height) { this.mVideoWidth = width; this.mVideoHeight = height; if (BuildCheck.isAndroid4_1()) this.mMasterTexture.setDefaultBufferSize(this.mVideoWidth, this.mVideoHeight);  } private void handleMirror(int mirror) { this.mMirror = mirror; synchronized (this.mClientSync) { int n = this.mClients.size(); for (int i = 0; i < n; i++) { RendererSurfaceRec client = (RendererSurfaceRec)this.mClients.valueAt(i); if (client != null) setMirror(client, mirror);  }  }  } private void setMirror(RendererSurfaceRec client, int mirror) { float[] mvp = client.mMvpMatrix; switch (mirror) { case 0: mvp[0] = Math.abs(mvp[0]); mvp[5] = Math.abs(mvp[5]); break;case 1: mvp[0] = -Math.abs(mvp[0]); mvp[5] = Math.abs(mvp[5]); break;case 2: mvp[0] = Math.abs(mvp[0]); mvp[5] = -Math.abs(mvp[5]); break;case 3: mvp[0] = -Math.abs(mvp[0]); mvp[5] = -Math.abs(mvp[5]); break; }  }
/* 766 */   } public RendererHolder(int width, int height, @Nullable RenderHolderCallback callback) { this.mCaptureTask = new Runnable()
/*     */       {
/*     */         EGLBase eglBase;
/*     */         
/*     */         EGLBase.IEglSurface captureSurface;
/*     */         GLDrawer2D drawer;
/*     */         
/*     */         public void run() {
/* 774 */           synchronized (RendererHolder.this.mSync) {
/*     */             
/* 776 */             if (!RendererHolder.this.isRunning) {
/*     */               try {
/* 778 */                 RendererHolder.this.mSync.wait();
/* 779 */               } catch (InterruptedException interruptedException) {}
/*     */             }
/*     */           } 
/*     */           
/* 783 */           init();
/* 784 */           if (this.eglBase.getGlVersion() > 2) {
/* 785 */             captureLoopGLES3();
/*     */           } else {
/* 787 */             captureLoopGLES2();
/*     */           } 
/*     */           
/* 790 */           release();
/*     */         }
/*     */ 
/*     */         
/*     */         private final void init() {
/* 795 */           this.eglBase = EGLBase.createFrom(3, RendererHolder.this.mRendererTask.getContext(), false, 0, false);
/* 796 */           this.captureSurface = this.eglBase.createOffscreen(RendererHolder.this.mRendererTask.mVideoWidth, RendererHolder.this.mRendererTask.mVideoHeight);
/* 797 */           this.drawer = new GLDrawer2D(true);
/* 798 */           this.drawer.getMvpMatrix()[5] = this.drawer.getMvpMatrix()[5] * -1.0F;
/*     */         }
/*     */         
/*     */         private final void captureLoopGLES2() {
/* 802 */           int width = -1, height = -1;
/* 803 */           ByteBuffer buf = null;
/* 804 */           File captureFile = null;
/*     */           
/* 806 */           while (RendererHolder.this.isRunning) {
/* 807 */             synchronized (RendererHolder.this.mSync) {
/* 808 */               if (captureFile == null) {
/* 809 */                 if (RendererHolder.this.mCaptureFile == null) {
/*     */                   try {
/* 811 */                     RendererHolder.this.mSync.wait();
/* 812 */                   } catch (InterruptedException e) {
/*     */                     break;
/*     */                   } 
/*     */                 }
/* 816 */                 if (RendererHolder.this.mCaptureFile != null) {
/*     */                   
/* 818 */                   captureFile = RendererHolder.this.mCaptureFile;
/* 819 */                   RendererHolder.this.mCaptureFile = null;
/*     */                 } 
/*     */                 continue;
/*     */               } 
/* 823 */               if ((((buf == null) ? 1 : 0) | ((width != RendererHolder.this.mRendererTask.mVideoWidth) ? 1 : 0)) != 0 || height != RendererHolder.this.mRendererTask.mVideoHeight) {
/* 824 */                 width = RendererHolder.this.mRendererTask.mVideoWidth;
/* 825 */                 height = RendererHolder.this.mRendererTask.mVideoHeight;
/* 826 */                 buf = ByteBuffer.allocateDirect(width * height * 4);
/* 827 */                 buf.order(ByteOrder.LITTLE_ENDIAN);
/* 828 */                 if (this.captureSurface != null) {
/* 829 */                   this.captureSurface.release();
/* 830 */                   this.captureSurface = null;
/*     */                 } 
/* 832 */                 this.captureSurface = this.eglBase.createOffscreen(width, height);
/*     */               } 
/* 834 */               if (RendererHolder.this.isRunning) {
/* 835 */                 this.captureSurface.makeCurrent();
/* 836 */                 this.drawer.draw(RendererHolder.this.mRendererTask.mTexId, RendererHolder.this.mRendererTask.mTexMatrix, 0);
/* 837 */                 this.captureSurface.swap();
/* 838 */                 buf.clear();
/* 839 */                 GLES20.glReadPixels(0, 0, width, height, 6408, 5121, buf);
/*     */                 
/* 841 */                 Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.PNG;
/* 842 */                 if (captureFile.toString().endsWith(".jpg")) {
/* 843 */                   compressFormat = Bitmap.CompressFormat.JPEG;
/*     */                 }
/* 845 */                 BufferedOutputStream os = null;
/*     */                 try {
/*     */                   try {
/* 848 */                     os = new BufferedOutputStream(new FileOutputStream(captureFile));
/* 849 */                     Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
/* 850 */                     buf.clear();
/* 851 */                     bmp.copyPixelsFromBuffer(buf);
/* 852 */                     bmp.compress(compressFormat, 90, os);
/* 853 */                     bmp.recycle();
/* 854 */                     os.flush();
/*     */                   } finally {
/* 856 */                     if (os != null) os.close(); 
/*     */                   } 
/* 858 */                 } catch (FileNotFoundException e) {
/* 859 */                   Log.w(TAG, "failed to save file", e);
/* 860 */                 } catch (IOException e) {
/* 861 */                   Log.w(TAG, "failed to save file", e);
/*     */                 } 
/*     */               } 
/*     */               
/* 865 */               captureFile = null;
/* 866 */               RendererHolder.this.mSync.notifyAll();
/*     */             } 
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/*     */         private final void captureLoopGLES3() {
/* 873 */           int width = -1, height = -1;
/* 874 */           ByteBuffer buf = null;
/* 875 */           File captureFile = null;
/*     */           
/* 877 */           while (RendererHolder.this.isRunning) {
/* 878 */             synchronized (RendererHolder.this.mSync) {
/* 879 */               if (captureFile == null) {
/* 880 */                 if (RendererHolder.this.mCaptureFile == null) {
/*     */                   try {
/* 882 */                     RendererHolder.this.mSync.wait();
/* 883 */                   } catch (InterruptedException e) {
/*     */                     break;
/*     */                   } 
/*     */                 }
/* 887 */                 if (RendererHolder.this.mCaptureFile != null) {
/*     */                   
/* 889 */                   captureFile = RendererHolder.this.mCaptureFile;
/* 890 */                   RendererHolder.this.mCaptureFile = null;
/*     */                 } 
/*     */                 continue;
/*     */               } 
/* 894 */               if ((((buf == null) ? 1 : 0) | ((width != RendererHolder.this.mRendererTask.mVideoWidth) ? 1 : 0)) != 0 || height != RendererHolder.this.mRendererTask.mVideoHeight) {
/* 895 */                 width = RendererHolder.this.mRendererTask.mVideoWidth;
/* 896 */                 height = RendererHolder.this.mRendererTask.mVideoHeight;
/* 897 */                 buf = ByteBuffer.allocateDirect(width * height * 4);
/* 898 */                 buf.order(ByteOrder.LITTLE_ENDIAN);
/* 899 */                 if (this.captureSurface != null) {
/* 900 */                   this.captureSurface.release();
/* 901 */                   this.captureSurface = null;
/*     */                 } 
/* 903 */                 this.captureSurface = this.eglBase.createOffscreen(width, height);
/*     */               } 
/* 905 */               if (RendererHolder.this.isRunning) {
/* 906 */                 this.captureSurface.makeCurrent();
/* 907 */                 this.drawer.draw(RendererHolder.this.mRendererTask.mTexId, RendererHolder.this.mRendererTask.mTexMatrix, 0);
/* 908 */                 this.captureSurface.swap();
/* 909 */                 buf.clear();
/* 910 */                 GLES20.glReadPixels(0, 0, width, height, 6408, 5121, buf);
/*     */                 
/* 912 */                 Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.PNG;
/* 913 */                 if (captureFile.toString().endsWith(".jpg")) {
/* 914 */                   compressFormat = Bitmap.CompressFormat.JPEG;
/*     */                 }
/* 916 */                 BufferedOutputStream os = null;
/*     */                 try {
/*     */                   try {
/* 919 */                     os = new BufferedOutputStream(new FileOutputStream(captureFile));
/* 920 */                     Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
/* 921 */                     buf.clear();
/* 922 */                     bmp.copyPixelsFromBuffer(buf);
/* 923 */                     bmp.compress(compressFormat, 90, os);
/* 924 */                     bmp.recycle();
/* 925 */                     os.flush();
/*     */                   } finally {
/* 927 */                     if (os != null) os.close(); 
/*     */                   } 
/* 929 */                 } catch (FileNotFoundException e) {
/* 930 */                   Log.w(TAG, "failed to save file", e);
/* 931 */                 } catch (IOException e) {
/* 932 */                   Log.w(TAG, "failed to save file", e);
/*     */                 } 
/*     */               } 
/*     */               
/* 936 */               captureFile = null;
/* 937 */               RendererHolder.this.mSync.notifyAll();
/*     */             } 
/*     */           } 
/*     */         }
/*     */         
/*     */         private final void release() {
/* 943 */           if (this.captureSurface != null) {
/* 944 */             this.captureSurface.makeCurrent();
/* 945 */             if (this.drawer != null) {
/* 946 */               this.drawer.release();
/*     */             }
/* 948 */             this.captureSurface.release();
/* 949 */             this.captureSurface = null;
/*     */           } 
/* 951 */           if (this.drawer != null) {
/* 952 */             this.drawer.release();
/* 953 */             this.drawer = null;
/*     */           } 
/* 955 */           if (this.eglBase != null) {
/* 956 */             this.eglBase.release();
/* 957 */             this.eglBase = null;
/*     */           } 
/*     */         }
/*     */       };
/*     */     this.mCallback = callback;
/*     */     this.mRendererTask = new RendererTask(this, width, height);
/*     */     (new Thread((Runnable)this.mRendererTask, TAG)).start();
/*     */     if (!this.mRendererTask.waitReady())
/*     */       throw new RuntimeException("failed to start renderer thread"); 
/*     */     (new Thread(this.mCaptureTask, "CaptureTask")).start();
/*     */     synchronized (this.mSync) {
/*     */       if (!this.isRunning)
/*     */         try {
/*     */           this.mSync.wait();
/*     */         } catch (InterruptedException interruptedException) {} 
/*     */     }  }
/*     */ 
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\glutils\RendererHolder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */