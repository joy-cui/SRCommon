/*     */ package com.serenegiant.media;
/*     */ 
/*     */ import android.media.MediaCodec;
/*     */ import android.media.MediaFormat;
/*     */ import android.os.Handler;
/*     */ import android.os.Looper;
/*     */ import android.os.Message;
/*     */ import android.util.Log;
/*     */ import android.view.Surface;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Recorder
/*     */   implements IRecorder
/*     */ {
/*  34 */   private static final String TAG = Recorder.class.getSimpleName();
/*     */   
/*     */   public static final long CHECK_INTERVAL = 45000L;
/*     */   
/*     */   private final IRecorder.RecorderCallback mCallback;
/*     */   
/*     */   protected IMuxer mMuxer;
/*     */   
/*     */   private volatile int mEncoderCount;
/*     */   
/*     */   private volatile int mStartedCount;
/*     */   
/*     */   private int mState;
/*     */   protected Encoder mVideoEncoder;
/*     */   protected Encoder mAudioEncoder;
/*     */   private volatile boolean mVideoStarted;
/*     */   private volatile boolean mAudioStarted;
/*     */   private EosHandler mEosHandler;
/*     */   protected long mStartTime;
/*     */   
/*     */   public Recorder(IRecorder.RecorderCallback callback) {
/*  55 */     this.mCallback = callback;
/*  56 */     synchronized (this) {
/*  57 */       this.mState = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMuxer(IMuxer muxer) {
/*  66 */     this.mMuxer = muxer;
/*  67 */     this.mEncoderCount = this.mStartedCount = 0;
/*  68 */     synchronized (this) {
/*  69 */       this.mState = 1;
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
/*     */   public void prepare() {
/*  85 */     synchronized (this) {
/*  86 */       if (this.mState != 1)
/*  87 */         throw new IllegalStateException("prepare:state=" + this.mState); 
/*     */     } 
/*     */     try {
/*  90 */       if (this.mVideoEncoder != null)
/*  91 */         this.mVideoEncoder.prepare(); 
/*  92 */       if (this.mAudioEncoder != null)
/*  93 */         this.mAudioEncoder.prepare(); 
/*  94 */     } catch (Exception e) {
/*  95 */       callOnError(e);
/*     */       return;
/*     */     } 
/*  98 */     synchronized (this) {
/*  99 */       this.mState = 2;
/*     */     } 
/* 101 */     callOnPrepared();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startRecording() throws IllegalStateException {
/* 107 */     synchronized (this) {
/* 108 */       if (this.mState != 2)
/* 109 */         throw new IllegalStateException("start:not prepared"); 
/* 110 */       this.mState = 3;
/*     */     } 
/*     */     
/* 113 */     this.mStartTime = System.currentTimeMillis();
/* 114 */     if (this.mVideoEncoder != null)
/* 115 */       this.mVideoEncoder.start(); 
/* 116 */     if (this.mAudioEncoder != null)
/* 117 */       this.mAudioEncoder.start(); 
/* 118 */     if (this.mEosHandler == null)
/* 119 */       this.mEosHandler = EosHandler.createHandler(this); 
/* 120 */     this.mEosHandler.startCheckFreeSpace();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void stopRecording() {
/* 126 */     if (this.mEosHandler != null) {
/* 127 */       this.mEosHandler.terminate();
/* 128 */       this.mEosHandler = null;
/*     */     } 
/* 130 */     synchronized (this) {
/* 131 */       if (this.mState == 0 || this.mState == 1 || this.mState == 5) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 136 */       this.mState = 5;
/*     */     } 
/* 138 */     if (this.mAudioEncoder != null) {
/* 139 */       this.mAudioEncoder.stop();
/*     */     }
/* 141 */     if (this.mVideoEncoder != null) {
/* 142 */       this.mVideoEncoder.stop();
/*     */     }
/* 144 */     callOnStopped();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Surface getInputSurface() {
/* 150 */     return (this.mVideoEncoder instanceof SurfaceEncoder) ? ((SurfaceEncoder)this.mVideoEncoder)
/* 151 */       .getInputSurface() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 156 */   public Encoder getVideoEncoder() { return this.mVideoEncoder; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 161 */   public Encoder getAudioEncoder() { return this.mAudioEncoder; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 166 */   public synchronized boolean isStarted() { return (this.mState == 4); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 171 */   public synchronized boolean isReady() { return (this.mState == 4 || this.mState == 2); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 176 */   public synchronized boolean isStopping() { return (this.mState == 5); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 181 */   public synchronized boolean isStopped() { return (this.mState <= 1); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 186 */   public synchronized int getState() { return this.mState; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 191 */   public IMuxer getMuxer() { return this.mMuxer; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void frameAvailableSoon() {
/* 199 */     if (this.mVideoEncoder != null) {
/* 200 */       this.mVideoEncoder.frameAvailableSoon();
/*     */     }
/*     */   }
/*     */   
/*     */   public void release() {
/* 205 */     if (this.mAudioEncoder != null) {
/* 206 */       this.mAudioEncoder.release();
/* 207 */       this.mAudioEncoder = null;
/*     */     } 
/* 209 */     if (this.mVideoEncoder != null) {
/* 210 */       this.mVideoEncoder.release();
/* 211 */       this.mVideoEncoder = null;
/*     */     } 
/* 213 */     if (this.mMuxer != null) {
/* 214 */       this.mMuxer.release();
/* 215 */       this.mMuxer = null;
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
/*     */   public synchronized void addEncoder(Encoder encoder) {
/* 229 */     synchronized (this) {
/* 230 */       if (this.mState > 1)
/* 231 */         throw new IllegalStateException("addEncoder already prepared/started"); 
/*     */     } 
/* 233 */     if (encoder.isAudio()) {
/* 234 */       if (this.mAudioEncoder != null)
/* 235 */         throw new IllegalArgumentException("Video encoder already added."); 
/* 236 */       this.mAudioEncoder = encoder;
/*     */     } else {
/* 238 */       if (this.mVideoEncoder != null)
/* 239 */         throw new IllegalArgumentException("Video encoder already added."); 
/* 240 */       this.mVideoEncoder = encoder;
/*     */     } 
/* 242 */     this.mEncoderCount = ((this.mVideoEncoder != null) ? 1 : 0) + ((this.mAudioEncoder != null) ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void removeEncoder(Encoder encoder) {
/* 252 */     if (encoder instanceof VideoEncoder || encoder instanceof SurfaceEncoder) {
/* 253 */       this.mVideoEncoder = null;
/* 254 */       this.mVideoStarted = false;
/* 255 */     } else if (encoder instanceof AudioEncoder) {
/* 256 */       this.mAudioEncoder = null;
/* 257 */       this.mAudioStarted = false;
/*     */     } 
/* 259 */     this.mEncoderCount = ((this.mVideoEncoder != null) ? 1 : 0) + ((this.mAudioEncoder != null) ? 1 : 0);
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
/*     */   public synchronized boolean start(Encoder encoder) {
/* 271 */     if (this.mState != 3)
/* 272 */       throw new IllegalStateException("muxer has not prepared:state="); 
/* 273 */     if (encoder.equals(this.mVideoEncoder)) {
/* 274 */       this.mVideoStarted = true;
/* 275 */     } else if (encoder.equals(this.mAudioEncoder)) {
/* 276 */       this.mAudioStarted = true;
/*     */     } 
/* 278 */     this.mStartedCount++;
/* 279 */     while (this.mState == 3 && this.mEncoderCount > 0) {
/* 280 */       boolean canStart = ((this.mVideoEncoder == null || this.mVideoStarted) && (this.mAudioEncoder == null || this.mAudioStarted));
/*     */ 
/*     */       
/* 283 */       if (canStart) {
/* 284 */         this.mMuxer.start();
/* 285 */         this.mState = 4;
/* 286 */         notifyAll();
/* 287 */         callOnStarted();
/*     */         
/* 289 */         if (this.mEosHandler != null)
/* 290 */           this.mEosHandler.setDuration(VideoConfig.maxDuration); 
/*     */         break;
/*     */       } 
/*     */       try {
/* 294 */         wait(100L);
/* 295 */       } catch (InterruptedException e) {
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     
/* 300 */     return (this.mState == 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void stop(Encoder encoder) {
/* 309 */     if (encoder.equals(this.mVideoEncoder)) {
/* 310 */       if (this.mVideoStarted) {
/* 311 */         this.mVideoStarted = false;
/* 312 */         this.mStartedCount--;
/*     */       } 
/* 314 */     } else if (encoder.equals(this.mAudioEncoder) && 
/* 315 */       this.mAudioStarted) {
/* 316 */       this.mAudioStarted = false;
/* 317 */       this.mStartedCount--;
/*     */     } 
/*     */     
/* 320 */     if (this.mEncoderCount > 0 && this.mStartedCount <= 0) {
/* 321 */       if (this.mState == 5)
/*     */       {
/* 323 */         this.mMuxer.stop();
/*     */       }
/* 325 */       this.mState = 1;
/* 326 */       this.mVideoEncoder = null;
/* 327 */       this.mAudioEncoder = null;
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
/*     */   public synchronized int addTrack(Encoder encoder, MediaFormat format) {
/*     */     int trackIx;
/*     */     try {
/* 344 */       if (this.mState != 3)
/* 345 */         throw new IllegalStateException("muxer not ready:state=" + this.mState); 
/* 346 */       trackIx = this.mMuxer.addTrack(format);
/* 347 */     } catch (Exception e) {
/*     */       
/* 349 */       trackIx = -1;
/* 350 */       removeEncoder(encoder);
/*     */     } 
/*     */     
/* 353 */     return trackIx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeSampleData(int trackIndex, ByteBuffer byteBuf, MediaCodec.BufferInfo bufferInfo) {
/*     */     try {
/* 365 */       if (this.mStartedCount > 0) {
/* 366 */         this.mMuxer.writeSampleData(trackIndex, byteBuf, bufferInfo);
/*     */       }
/* 368 */     } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void callOnPrepared() {
/* 376 */     if (this.mCallback != null) {
/*     */       try {
/* 378 */         this.mCallback.onPrepared(this);
/* 379 */       } catch (Exception e) {
/* 380 */         Log.e(TAG, "onPrepared:", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected void callOnStarted() {
/* 386 */     if (this.mCallback != null) {
/*     */       try {
/* 388 */         this.mCallback.onStarted(this);
/* 389 */       } catch (Exception e) {
/* 390 */         Log.e(TAG, "onStarted:", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected void callOnStopped() {
/* 396 */     if (this.mCallback != null) {
/*     */       try {
/* 398 */         this.mCallback.onStopped(this);
/* 399 */       } catch (Exception e) {
/* 400 */         Log.e(TAG, "onStopped:", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected void callOnError(Exception e) {
/* 406 */     if (this.mCallback != null) {
/*     */       try {
/* 408 */         this.mCallback.onError(e);
/* 409 */       } catch (Exception e1) {
/* 410 */         Log.e(TAG, "onError:", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean check();
/*     */ 
/*     */   
/*     */   private static final class EosHandler
/*     */     extends Handler
/*     */   {
/*     */     private static final int MSG_CHECK_FREESPACE = 5;
/*     */     
/*     */     private static final int MSG_SEND_EOS = 8;
/*     */     
/*     */     private static final int MSG_SEND_QUIT = 9;
/*     */     
/*     */     private final EosThread mThread;
/*     */ 
/*     */     
/* 432 */     private EosHandler(EosThread thread) { this.mThread = thread; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static final EosHandler createHandler(Recorder recorder) {
/* 442 */       EosThread thread = new EosThread(recorder);
/* 443 */       thread.start();
/* 444 */       return thread.getHandler();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final void setDuration(long duration) {
/* 452 */       removeMessages(8);
/* 453 */       if (duration > 0L) {
/* 454 */         sendEmptyMessageDelayed(8, duration);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final void startCheckFreeSpace() {
/* 464 */       removeMessages(5);
/*     */       
/* 466 */       sendEmptyMessageDelayed(5, 45000L);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final void terminate() {
/* 474 */       removeMessages(8);
/* 475 */       removeMessages(5);
/*     */       
/* 477 */       sendEmptyMessage(9);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final void handleMessage(Message msg) {
/* 483 */       Recorder recorder = this.mThread.mWeakRecorder.get();
/* 484 */       if (recorder == null) {
/*     */         
/*     */         try {
/* 487 */           Looper.myLooper().quit();
/* 488 */         } catch (Exception exception) {}
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 493 */       switch (msg.what) {
/*     */         case 8:
/* 495 */           recorder.stopRecording();
/*     */           return;
/*     */         case 5:
/* 498 */           if (!this.mThread.check(recorder)) {
/* 499 */             sendEmptyMessageDelayed(5, 45000L);
/*     */           } else {
/* 501 */             recorder.stopRecording();
/*     */           } 
/*     */           return;
/*     */         case 9:
/*     */           try {
/* 506 */             Looper.myLooper().quit();
/* 507 */           } catch (Exception exception) {}
/*     */           return;
/*     */       } 
/*     */ 
/*     */       
/* 512 */       super.handleMessage(msg);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static final class EosThread
/*     */       extends Thread
/*     */     {
/* 520 */       private final Object mSync = new Object();
/*     */       
/*     */       private final WeakReference<Recorder> mWeakRecorder;
/*     */       private EosHandler mHandler;
/*     */       private boolean mIsReady = false;
/*     */       
/*     */       public EosThread(Recorder recorder) {
/* 527 */         super("EosThread");
/* 528 */         this.mWeakRecorder = new WeakReference<>(recorder);
/*     */       }
/*     */       
/*     */       private final EosHandler getHandler() {
/* 532 */         synchronized (this.mSync) {
/* 533 */           while (!this.mIsReady) {
/*     */             try {
/* 535 */               this.mSync.wait(300L);
/* 536 */             } catch (InterruptedException e) {
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/* 541 */         return this.mHandler;
/*     */       }
/*     */ 
/*     */       
/*     */       public final void run() {
/* 546 */         Looper.prepare();
/* 547 */         synchronized (this.mSync) {
/* 548 */           this.mHandler = new EosHandler(this);
/* 549 */           this.mIsReady = true;
/* 550 */           this.mSync.notify();
/*     */         } 
/* 552 */         Looper.loop();
/* 553 */         synchronized (this.mSync) {
/* 554 */           this.mIsReady = false;
/* 555 */           this.mHandler = null;
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 560 */       private boolean check(Recorder recorder) { return recorder.check(); }
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\Recorder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */