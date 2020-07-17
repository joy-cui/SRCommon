/*     */ package com.serenegiant.media;
/*     */ 
/*     */ import android.os.Process;
/*     */ import android.util.Log;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class IAudioSampler
/*     */ {
/*  31 */   private final String TAG = getClass().getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   private final int MAX_POOL_SIZE = 200;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   private final int MAX_QUEUE_SIZE = 200;
/*     */ 
/*     */   
/*  65 */   private final LinkedBlockingQueue<AudioData> mPool = new LinkedBlockingQueue<>(200);
/*  66 */   private final LinkedBlockingQueue<AudioData> mAudioQueue = new LinkedBlockingQueue<>(200);
/*     */   
/*     */   private CallbackThread mCallbackThread;
/*     */   
/*  70 */   private final Object mCallbackSync = new Object();
/*  71 */   private final List<SoundSamplerCallback> mCallbacks = new ArrayList<>();
/*     */   
/*     */   protected volatile boolean mIsCapturing;
/*     */   
/*     */   protected int mDefaultBufferSize;
/*     */   
/*     */   private int mBufferNum;
/*     */   
/*     */   private long prevInputPTSUs;
/*     */   
/*     */   public void release() {
/*  82 */     if (isStarted()) {
/*  83 */       stop();
/*     */     }
/*     */     
/*  86 */     synchronized (this.mCallbackSync) {
/*  87 */       this.mCallbacks.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void start() {
/*  98 */     synchronized (this.mCallbackSync) {
/*  99 */       if (this.mCallbackThread == null) {
/* 100 */         this.mIsCapturing = true;
/* 101 */         this.mCallbackThread = new CallbackThread();
/* 102 */         this.mCallbackThread.start();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void stop() {
/* 114 */     synchronized (this.mCallbackSync) {
/* 115 */       boolean capturing = this.mIsCapturing;
/* 116 */       this.mIsCapturing = false;
/* 117 */       this.mCallbackThread = null;
/* 118 */       if (capturing) {
/*     */         try {
/* 120 */           this.mCallbackSync.wait();
/* 121 */         } catch (InterruptedException interruptedException) {}
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCallback(SoundSamplerCallback callback) {
/* 133 */     synchronized (this.mCallbackSync) {
/* 134 */       this.mCallbacks.add(callback);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeCallback(SoundSamplerCallback callback) {
/* 143 */     synchronized (this.mCallbackSync) {
/* 144 */       while (this.mCallbacks.remove(callback));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 153 */   public boolean isStarted() { return this.mIsCapturing; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 182 */   public int getBufferSize() { return this.mDefaultBufferSize; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void callOnData(AudioData data) {
/* 190 */     synchronized (this.mCallbackSync) {
/* 191 */       for (SoundSamplerCallback callback : this.mCallbacks) {
/*     */         try {
/* 193 */           data.mBuffer.rewind();
/* 194 */           callback.onData(data.mBuffer, data.size, data.presentationTimeUs);
/* 195 */         } catch (Exception e) {
/* 196 */           Log.w(this.TAG, "callOnData:", e);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void callOnError(Exception e) {
/* 207 */     synchronized (this.mCallbackSync)
/* 208 */     { for (SoundSamplerCallback callback : this.mCallbacks)
/*     */       { 
/* 210 */         try { callback.onError(e); }
/* 211 */         catch (Exception e1)
/* 212 */         { Log.w(this.TAG, "callOnError:", e1); }  }  } 
/*     */   }
/*     */   protected void init_pool(int default_buffer_size) { this.mDefaultBufferSize = default_buffer_size; this.mAudioQueue.clear(); this.mPool.clear(); for (int i = 0; i < 8; i++)
/*     */       this.mPool.add(new AudioData(default_buffer_size));  }
/*     */   protected AudioData obtain() { AudioData result = null; if (!this.mPool.isEmpty()) { result = this.mPool.poll(); } else if (this.mBufferNum < 200) { result = new AudioData(this.mDefaultBufferSize); this.mBufferNum++; }  if (result != null)
/*     */       result.size = 0;  return result; } protected void recycle(AudioData data) { if (!this.mPool.offer(data))
/* 218 */       this.mBufferNum--;  } public IAudioSampler() { this.mDefaultBufferSize = 1024;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 231 */     this.mBufferNum = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 279 */     this.prevInputPTSUs = -1L; }
/*     */ 
/*     */   
/*     */   protected void addAudioData(AudioData data) { this.mAudioQueue.offer(data); }
/*     */   
/*     */   protected AudioData pollAudioData(long timout_msec) throws InterruptedException { return this.mAudioQueue.poll(timout_msec, TimeUnit.MILLISECONDS); }
/*     */   
/*     */   protected long getInputPTSUs() {
/* 287 */     long result = System.nanoTime() / 1000L;
/* 288 */     if (result <= this.prevInputPTSUs) {
/* 289 */       result = this.prevInputPTSUs + 9643L;
/*     */     }
/* 291 */     this.prevInputPTSUs = result;
/* 292 */     return result;
/*     */   }
/*     */   public abstract int getAudioSource();
/*     */   public abstract int getChannels();
/*     */   public abstract int getSamplingFrequency();
/*     */   
/*     */   public abstract int getBitResolution();
/*     */   
/* 300 */   private final class CallbackThread extends Thread { public CallbackThread() { super("AudioSampler"); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final void run() {
/* 306 */       Process.setThreadPriority(-16);
/*     */       
/* 308 */       while (IAudioSampler.this.mIsCapturing) {
/*     */         AudioData data; try {
/* 310 */           data = IAudioSampler.this.pollAudioData(100L);
/* 311 */         } catch (InterruptedException e) {
/*     */           break;
/*     */         } 
/* 314 */         if (data != null) {
/* 315 */           IAudioSampler.this.callOnData(data);
/*     */           
/* 317 */           IAudioSampler.this.recycle(data);
/*     */         } 
/*     */       } 
/* 320 */       synchronized (IAudioSampler.this.mCallbackSync) {
/* 321 */         IAudioSampler.this.mCallbackSync.notifyAll();
/*     */       } 
/*     */     } }
/*     */ 
/*     */   
/*     */   public static interface SoundSamplerCallback {
/*     */     void onData(ByteBuffer param1ByteBuffer, int param1Int, long param1Long);
/*     */     
/*     */     void onError(Exception param1Exception);
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\IAudioSampler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */