/*     */ package com.serenegiant.utils;
/*     */ 
/*     */ import android.util.Log;
/*     */ import java.util.concurrent.LinkedBlockingDeque;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MessageTask
/*     */   implements Runnable
/*     */ {
/*  28 */   private static final String TAG = MessageTask.class.getSimpleName();
/*     */   protected static final int REQUEST_TASK_NON = 0;
/*     */   protected static final int REQUEST_TASK_RUN = -1;
/*     */   protected static final int REQUEST_TASK_RUN_AND_WAIT = -2;
/*     */   protected static final int REQUEST_TASK_START = -8;
/*     */   protected static final int REQUEST_TASK_QUIT = -9;
/*     */   
/*     */   public static class TaskBreak extends RuntimeException {}
/*     */   
/*     */   protected static final class Request {
/*     */     int request;
/*     */     int arg1;
/*     */     int arg2;
/*     */     
/*  42 */     private Request() { this.request = this.request_for_result = 0; }
/*     */ 
/*     */     
/*     */     Object obj;
/*     */     
/*     */     int request_for_result;
/*     */     
/*     */     Object result;
/*     */     
/*     */     public Request(int _request, int _arg1, int _arg2, Object _obj) {
/*  52 */       this.request = _request;
/*  53 */       this.arg1 = _arg1;
/*  54 */       this.arg2 = _arg2;
/*  55 */       this.obj = _obj;
/*  56 */       this.request_for_result = 0;
/*     */     }
/*     */     
/*     */     public void setResult(Object result) {
/*  60 */       synchronized (this) {
/*  61 */         this.result = result;
/*  62 */         this.request = this.request_for_result = 0;
/*  63 */         notifyAll();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  69 */     public boolean equals(Object o) { return (o instanceof Request) ? ((this.request == ((Request)o).request && this.request_for_result == ((Request)o).request_for_result && this.arg1 == ((Request)o).arg1 && this.arg2 == ((Request)o).arg2 && this.obj == ((Request)o).obj)) : super
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  75 */         .equals(o); }
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
/*  86 */   private final Object mSync = new Object();
/*     */   
/*     */   private final int mMaxRequest;
/*     */   
/*     */   private final LinkedBlockingQueue<Request> mRequestPool;
/*     */   
/*     */   private final LinkedBlockingDeque<Request> mRequestQueue;
/*     */   
/*     */   private volatile boolean mIsRunning;
/*     */   
/*     */   private volatile boolean mFinished;
/*     */   private Thread mWorkerThread;
/*     */   
/*     */   public MessageTask() {
/* 100 */     this.mMaxRequest = -1;
/* 101 */     this.mRequestPool = new LinkedBlockingQueue<>();
/* 102 */     this.mRequestQueue = new LinkedBlockingDeque<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageTask(int init_num) {
/* 111 */     this.mMaxRequest = -1;
/* 112 */     this.mRequestPool = new LinkedBlockingQueue<>();
/* 113 */     this.mRequestQueue = new LinkedBlockingDeque<>();
/* 114 */     for (int i = 0; i < init_num && 
/* 115 */       this.mRequestPool.offer(new Request()); i++);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageTask(int max_request, int init_num) {
/* 126 */     this.mMaxRequest = max_request;
/* 127 */     this.mRequestPool = new LinkedBlockingQueue<>(max_request);
/* 128 */     this.mRequestQueue = new LinkedBlockingDeque<>(max_request);
/* 129 */     for (int i = 0; i < init_num && 
/* 130 */       this.mRequestPool.offer(new Request()); i++);
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
/*     */   protected void init(int arg1, int arg2, Object obj) {
/* 142 */     this.mFinished = false;
/* 143 */     this.mRequestQueue.offer(obtain(-8, arg1, arg2, obj));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void onInit(int paramInt1, int paramInt2, Object paramObject);
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void onStart();
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onBeforeStop() {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void onStop();
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void onRelease();
/*     */ 
/*     */ 
/*     */   
/* 169 */   protected boolean onError(Exception e) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Object processRequest(int paramInt1, int paramInt2, int paramInt3, Object paramObject) throws TaskBreak;
/*     */ 
/*     */ 
/*     */   
/* 178 */   protected Request takeRequest() throws InterruptedException { return this.mRequestQueue.take(); }
/*     */ 
/*     */   
/*     */   public boolean waitReady() {
/* 182 */     synchronized (this.mSync) {
/* 183 */       while (!this.mIsRunning && !this.mFinished) {
/*     */         try {
/* 185 */           this.mSync.wait(500L);
/* 186 */         } catch (InterruptedException e) {
/*     */           break;
/*     */         } 
/*     */       } 
/* 190 */       return this.mIsRunning;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 195 */   public boolean isRunning() { return this.mIsRunning; }
/*     */ 
/*     */ 
/*     */   
/* 199 */   public boolean isFinished() { return this.mFinished; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 204 */     Request request = null;
/* 205 */     this.mIsRunning = true;
/*     */     try {
/* 207 */       request = this.mRequestQueue.take();
/* 208 */     } catch (InterruptedException e) {
/* 209 */       this.mIsRunning = false;
/* 210 */       this.mFinished = true;
/*     */     } 
/* 212 */     synchronized (this.mSync) {
/* 213 */       if (this.mIsRunning) {
/* 214 */         this.mWorkerThread = Thread.currentThread();
/*     */         try {
/* 216 */           onInit(request.arg1, request.arg2, request.obj);
/* 217 */         } catch (Exception e) {
/* 218 */           Log.w(TAG, e);
/* 219 */           this.mIsRunning = false;
/* 220 */           this.mFinished = true;
/*     */         } 
/*     */       } 
/* 223 */       this.mSync.notifyAll();
/*     */     } 
/* 225 */     if (this.mIsRunning) {
/*     */       try {
/* 227 */         onStart();
/* 228 */       } catch (Exception e) {
/* 229 */         if (callOnError(e)) {
/* 230 */           this.mIsRunning = false;
/* 231 */           this.mFinished = true;
/*     */         } 
/*     */       } 
/*     */     }
/* 235 */     while (this.mIsRunning) {
/*     */       try {
/* 237 */         request = takeRequest();
/* 238 */         switch (request.request) {
/*     */           case 0:
/*     */             break;
/*     */           case -9:
/*     */             break;
/*     */           case -1:
/* 244 */             if (request.obj instanceof Runnable) {
/*     */               try {
/* 246 */                 ((Runnable)request.obj).run();
/* 247 */               } catch (Exception e) {}
/*     */             }
/*     */             break;
/*     */ 
/*     */           
/*     */           case -2:
/*     */             try {
/* 254 */               request.setResult(processRequest(request.request_for_result, request.arg1, request.arg2, request.obj));
/* 255 */             } catch (TaskBreak e) {
/* 256 */               request.setResult(null);
/*     */               break;
/* 258 */             } catch (Exception e) {
/* 259 */               request.setResult(null);
/*     */             } 
/*     */             break;
/*     */ 
/*     */           
/*     */           default:
/*     */             try {
/* 266 */               processRequest(request.request, request.arg1, request.arg2, request.obj);
/* 267 */             } catch (TaskBreak e) {
/*     */               break;
/* 269 */             } catch (Exception e) {}
/*     */             break;
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 275 */         request.request = request.request_for_result = 0;
/*     */         
/* 277 */         this.mRequestPool.offer(request);
/* 278 */       } catch (InterruptedException e) {
/*     */         break;
/*     */       } 
/*     */     } 
/* 282 */     boolean interrupted = Thread.interrupted();
/* 283 */     synchronized (this.mSync) {
/* 284 */       this.mWorkerThread = null;
/* 285 */       this.mIsRunning = false;
/* 286 */       this.mFinished = true;
/*     */     } 
/* 288 */     if (!interrupted) {
/*     */       try {
/* 290 */         onBeforeStop();
/* 291 */         onStop();
/* 292 */       } catch (Exception e) {
/* 293 */         callOnError(e);
/*     */       } 
/*     */     }
/*     */     try {
/* 297 */       onRelease();
/* 298 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/* 301 */     synchronized (this.mSync) {
/* 302 */       this.mSync.notifyAll();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean callOnError(Exception e) {
/*     */     try {
/* 314 */       return onError(e);
/* 315 */     } catch (Exception exception) {
/*     */ 
/*     */       
/* 318 */       return true;
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
/*     */   protected Request obtain(int request, int arg1, int arg2, Object obj) {
/* 331 */     Request req = this.mRequestPool.poll();
/* 332 */     if (req != null) {
/* 333 */       req.request = request;
/* 334 */       req.arg1 = arg1;
/* 335 */       req.arg2 = arg2;
/* 336 */       req.obj = obj;
/*     */     } else {
/* 338 */       req = new Request(request, arg1, arg2, obj);
/*     */     } 
/* 340 */     return req;
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
/* 352 */   public boolean offer(int request, int arg1, int arg2, Object obj) { return (!this.mFinished && this.mRequestQueue.offer(obtain(request, arg1, arg2, obj))); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 363 */   public boolean offer(int request, int arg1, Object obj) { return (!this.mFinished && this.mRequestQueue.offer(obtain(request, arg1, 0, obj))); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 374 */   public boolean offer(int request, int arg1, int arg2) { return (!this.mFinished && this.mIsRunning && this.mRequestQueue.offer(obtain(request, arg1, arg2, null))); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 384 */   public boolean offer(int request, int arg1) { return (!this.mFinished && this.mIsRunning && this.mRequestQueue.offer(obtain(request, arg1, 0, null))); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 393 */   public boolean offer(int request) { return (!this.mFinished && this.mIsRunning && this.mRequestQueue.offer(obtain(request, 0, 0, null))); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 403 */   public boolean offer(int request, Object obj) { return (!this.mFinished && this.mIsRunning && this.mRequestQueue.offer(obtain(request, 0, 0, obj))); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 413 */   public boolean offerFirst(int request, int arg1, int arg2, Object obj) { return (!this.mFinished && this.mIsRunning && this.mRequestQueue.offerFirst(obtain(request, arg1, arg2, obj))); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object offerAndWait(int request, int arg1, int arg2, Object obj) {
/* 427 */     if (!this.mFinished && request > 0) {
/* 428 */       Request req = obtain(-2, arg1, arg2, obj);
/* 429 */       synchronized (req) {
/* 430 */         req.request_for_result = request;
/* 431 */         req.result = null;
/* 432 */         this.mRequestQueue.offer(req);
/* 433 */         while (this.mIsRunning && req.request_for_result != 0) {
/*     */           try {
/* 435 */             req.wait(100L);
/* 436 */           } catch (InterruptedException e) {
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/* 441 */       return req.result;
/*     */     } 
/* 443 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 453 */   public boolean queueEvent(Runnable task) { return (!this.mFinished && task != null && offer(-1, task)); }
/*     */ 
/*     */   
/*     */   public void removeRequest(Request request) {
/* 457 */     for (Request req : this.mRequestQueue) {
/* 458 */       if (!this.mIsRunning || this.mFinished)
/* 459 */         break;  if (req.equals(request)) {
/* 460 */         this.mRequestQueue.remove(req);
/* 461 */         this.mRequestPool.offer(req);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void removeRequest(int request) {
/* 467 */     for (Request req : this.mRequestQueue) {
/* 468 */       if (!this.mIsRunning || this.mFinished)
/* 469 */         break;  if (req.request == request) {
/* 470 */         this.mRequestQueue.remove(req);
/* 471 */         this.mRequestPool.offer(req);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 480 */   public void release() { release(false); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void release(boolean interrupt) {
/* 488 */     boolean b = this.mIsRunning;
/* 489 */     this.mIsRunning = false;
/* 490 */     if (!this.mFinished) {
/* 491 */       this.mRequestQueue.clear();
/* 492 */       this.mRequestQueue.offerFirst(obtain(-9, 0, 0, null));
/* 493 */       synchronized (this.mSync) {
/* 494 */         if (b) {
/* 495 */           long current = Thread.currentThread().getId();
/* 496 */           long id = (this.mWorkerThread != null) ? this.mWorkerThread.getId() : current;
/* 497 */           if (id != current) {
/* 498 */             if (interrupt && this.mWorkerThread != null) {
/* 499 */               this.mWorkerThread.interrupt();
/*     */             }
/* 501 */             while (!this.mFinished) {
/*     */               try {
/* 503 */                 this.mSync.wait(300L);
/* 504 */               } catch (InterruptedException interruptedException) {}
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseSelf() {
/* 518 */     this.mIsRunning = false;
/* 519 */     if (!this.mFinished) {
/* 520 */       this.mRequestQueue.clear();
/* 521 */       this.mRequestQueue.offerFirst(obtain(-9, 0, 0, null));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 531 */   public void userBreak() throws TaskBreak { throw new TaskBreak(); }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\MessageTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */