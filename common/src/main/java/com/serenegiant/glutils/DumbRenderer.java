/*     */ package com.serenegiant.glutils;
/*     */ 
/*     */ import android.graphics.SurfaceTexture;
/*     */ import android.support.annotation.NonNull;
/*     */ import android.util.Log;
/*     */ import android.view.Surface;
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
/*     */ public class DumbRenderer
/*     */   implements IRenderer
/*     */ {
/*  31 */   private static final String TAG = DumbRenderer.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  43 */   private final Object mSync = new Object(); private RendererTask mRendererTask;
/*     */   private static final int REQUEST_SET_SURFACE = 1;
/*  45 */   private int mMirror = 0;
/*     */   private static final int REQUEST_DRAW = 2;
/*     */   
/*     */   public DumbRenderer(EGLBase.IContext sharedContext, int flags, RendererDelegater delegater) {
/*  49 */     this.mRendererTask = new RendererTask(sharedContext, flags, delegater);
/*  50 */     (new Thread((Runnable)this.mRendererTask, TAG)).start();
/*  51 */     if (!this.mRendererTask.waitReady())
/*     */     {
/*  53 */       throw new RuntimeException("failed to start renderer thread"); } 
/*     */   }
/*     */   private static final int REQUEST_RESIZE = 3;
/*     */   private static final int REQUEST_MIRROR = 4;
/*     */   
/*     */   public void release() {
/*  59 */     synchronized (this.mSync) {
/*  60 */       if (this.mRendererTask != null) {
/*     */         
/*  62 */         this.mRendererTask.release();
/*  63 */         this.mRendererTask = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSurface(Surface surface) {
/*  70 */     synchronized (this.mSync) {
/*  71 */       if (this.mRendererTask != null) {
/*  72 */         this.mRendererTask.offer(1, surface);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSurface(SurfaceTexture surface) {
/*  79 */     synchronized (this.mSync) {
/*  80 */       if (this.mRendererTask != null) {
/*  81 */         this.mRendererTask.offer(1, surface);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMirror(int mirror) {
/*  88 */     synchronized (this.mSync) {
/*  89 */       if (this.mMirror != mirror) {
/*  90 */         this.mMirror = mirror;
/*  91 */         if (this.mRendererTask != null) {
/*  92 */           this.mRendererTask.offer(4, mirror % 4);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   public int getMirror() { return this.mMirror; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void resize(int width, int height) {
/* 106 */     synchronized (this.mSync) {
/* 107 */       if (this.mRendererTask != null) {
/* 108 */         this.mRendererTask.offer(3, width, height);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void requestRender(Object... args) {
/* 115 */     synchronized (this.mSync) {
/* 116 */       if (this.mRendererTask != null) {
/* 117 */         this.mRendererTask.offer(2, args);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static class RendererTask
/*     */     extends EglTask
/*     */   {
/*     */     private final RendererDelegater mDelegater;
/*     */     
/*     */     private int frameWidth;
/*     */     
/*     */     private int frameHeight;
/*     */     private int frameRotation;
/*     */     private int surfaceWidth;
/*     */     private int surfaceHeight;
/*     */     private boolean mirror;
/*     */     
/*     */     public RendererTask(EGLBase.IContext sharedContext, int flags, @NonNull DumbRenderer.RendererDelegater delegater) {
/* 137 */       super(sharedContext, flags);
/* 138 */       this.mDelegater = delegater;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void onStart() {
/* 143 */       makeCurrent();
/*     */       try {
/* 145 */         this.mDelegater.onStart(getEgl());
/* 146 */       } catch (Exception e) {
/* 147 */         Log.w(TAG, e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void onStop() {
/* 153 */       makeCurrent();
/*     */       try {
/* 155 */         this.mDelegater.onStop(getEgl());
/* 156 */       } catch (Exception e) {
/* 157 */         Log.w(TAG, e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected Object processRequest(int request, int arg1, int arg2, Object obj) throws TaskBreak {
/* 163 */       switch (request) {
/*     */         case 1:
/* 165 */           handleSetSurface(obj);
/*     */           break;
/*     */         case 2:
/* 168 */           handleDraw(new Object[] { obj });
/*     */           break;
/*     */         case 3:
/* 171 */           handleResize(arg1, arg2);
/*     */           break;
/*     */         case 4:
/* 174 */           handleMirror(arg1);
/*     */           break;
/*     */       } 
/* 177 */       return null;
/*     */     }
/*     */     
/*     */     private void handleSetSurface(Object surface) {
/* 181 */       makeCurrent();
/*     */       try {
/* 183 */         this.mDelegater.onSetSurface(getEgl(), surface);
/* 184 */       } catch (Exception e) {
/* 185 */         Log.w(TAG, e);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void handleResize(int width, int height) {
/* 190 */       if (this.surfaceWidth != width || this.surfaceHeight != height) {
/* 191 */         this.surfaceWidth = width;
/* 192 */         this.surfaceHeight = height;
/* 193 */         makeCurrent();
/*     */         try {
/* 195 */           this.mDelegater.onResize(getEgl(), width, height);
/* 196 */         } catch (Exception e) {
/* 197 */           Log.w(TAG, e);
/*     */         } 
/* 199 */         handleDraw(new Object[0]);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void handleDraw(Object... args) {
/* 204 */       makeCurrent();
/*     */       try {
/* 206 */         this.mDelegater.onDraw(getEgl(), args);
/* 207 */       } catch (Exception e) {
/* 208 */         Log.w(TAG, e);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void handleMirror(int mirror) {
/* 213 */       makeCurrent();
/*     */       try {
/* 215 */         this.mDelegater.onMirror(getEgl(), mirror);
/* 216 */       } catch (Exception e) {
/* 217 */         Log.w(TAG, e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface RendererDelegater {
/*     */     void onStart(EGLBase param1EGLBase);
/*     */     
/*     */     void onStop(EGLBase param1EGLBase);
/*     */     
/*     */     void onSetSurface(EGLBase param1EGLBase, Object param1Object);
/*     */     
/*     */     void onResize(EGLBase param1EGLBase, int param1Int1, int param1Int2);
/*     */     
/*     */     void onDraw(EGLBase param1EGLBase, Object... param1VarArgs);
/*     */     
/*     */     void onMirror(EGLBase param1EGLBase, int param1Int);
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\glutils\DumbRenderer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */