/*     */ package com.serenegiant.media;
/*     */ 
/*     */ import android.annotation.TargetApi;
/*     */ import android.media.MediaCodec;
/*     */ import android.media.MediaFormat;
/*     */ import android.util.Log;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @TargetApi(16)
/*     */ public abstract class MediaEncoder
/*     */   implements IMediaCodec
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*  36 */   private final String TAG = getClass().getSimpleName();
/*     */   
/*     */   protected final boolean mIsAudio;
/*     */   
/*  40 */   protected final Object mSync = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean mIsPrepared;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected volatile boolean mIsCapturing;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int mRequestDrain;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected volatile boolean mRequestStop;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean mIsEOS;
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean mMuxerStarted;
/*     */ 
/*     */ 
/*     */   
/*     */   protected int mTrackIndex;
/*     */ 
/*     */ 
/*     */   
/*     */   protected MediaCodec mMediaCodec;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final WeakReference<AbstractRecorder> mWeakMuxer;
/*     */ 
/*     */ 
/*     */   
/*     */   private MediaCodec.BufferInfo mBufferInfo;
/*     */ 
/*     */ 
/*     */   
/*     */   private final IMediaCodecCallback mCallback;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Runnable mEncodeTask;
/*     */ 
/*     */ 
/*     */   
/*     */   private long prevOutputPTSUs;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/* 103 */     synchronized (this.mSync) {
/* 104 */       this.mIsCapturing = true;
/* 105 */       this.mRequestStop = false;
/* 106 */       this.mSync.notifyAll();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 116 */     synchronized (this.mSync) {
/* 117 */       if (!this.mIsCapturing || this.mRequestStop) {
/*     */         return;
/*     */       }
/* 120 */       this.mRequestStop = true;
/* 121 */       this.mSync.notifyAll();
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
/*     */   public boolean frameAvailableSoon() {
/* 133 */     synchronized (this.mSync) {
/* 134 */       if (!this.mIsCapturing || this.mRequestStop) {
/* 135 */         return false;
/*     */       }
/* 137 */       this.mRequestDrain++;
/* 138 */       this.mSync.notifyAll();
/*     */     } 
/* 140 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 145 */   public boolean isPrepared() { return this.mIsPrepared; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 150 */   public boolean isRunning() { return this.mIsCapturing; }
/*     */ 
/*     */ 
/*     */   
/* 154 */   public boolean isAudio() { return this.mIsAudio; }
/*     */ 
/*     */ 
/*     */   
/* 158 */   protected boolean callErrorHandler(Exception e) { return (this.mCallback != null && this.mCallback.onError(this, e)); }
/*     */ 
/*     */   
/*     */   protected void callOnPrepared() {
/* 162 */     if (this.mCallback != null) {
/*     */       try {
/* 164 */         this.mCallback.onPrepared(this);
/* 165 */       } catch (Exception e) {
/* 166 */         Log.w(this.TAG, "callOnPrepared", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected void callOnStart() {
/* 172 */     if (this.mCallback != null) {
/*     */       try {
/* 174 */         this.mCallback.onStart(this);
/* 175 */       } catch (Exception e) {
/* 176 */         Log.w(this.TAG, "callOnStart", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected void callOnStop() {
/* 182 */     if (this.mCallback != null) {
/*     */       try {
/* 184 */         this.mCallback.onStop(this);
/* 185 */       } catch (Exception e) {
/* 186 */         Log.w(this.TAG, "callOnStop", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected void callOnRelease() {
/* 192 */     if (this.mCallback != null)
/*     */       try {
/* 194 */         this.mCallback.onRelease(this);
/* 195 */       } catch (Exception e) {
/* 196 */         Log.w(this.TAG, "callOnRelease", e);
/*     */       }  
/*     */   }
/*     */   
/*     */   public MediaEncoder(boolean is_audio, AbstractRecorder muxer, IMediaCodecCallback listener) {
/* 201 */     this.mEncodeTask = new Runnable()
/*     */       {
/*     */ 
/*     */         
/*     */         public void run()
/*     */         {
/* 207 */           synchronized (MediaEncoder.this.mSync) {
/* 208 */             MediaEncoder.this.mRequestStop = false;
/* 209 */             MediaEncoder.this.mRequestDrain = 0;
/* 210 */             MediaEncoder.this.mSync.notify();
/*     */             try {
/* 212 */               MediaEncoder.this.mSync.wait();
/* 213 */               MediaEncoder.this.callOnStart();
/* 214 */             } catch (InterruptedException interruptedException) {}
/*     */           } 
/*     */           
/*     */           while (true) {
/*     */             boolean localRequestDrain;
/*     */             boolean localRequestStop;
/* 220 */             synchronized (MediaEncoder.this.mSync) {
/* 221 */               localRequestStop = MediaEncoder.this.mRequestStop;
/* 222 */               localRequestDrain = (MediaEncoder.this.mRequestDrain > 0);
/* 223 */               if (localRequestDrain)
/* 224 */                 MediaEncoder.this.mRequestDrain--; 
/* 225 */               if (!localRequestDrain && !localRequestStop) {
/*     */                 try {
/* 227 */                   MediaEncoder.this.mSync.wait();
/* 228 */                 } catch (InterruptedException e) {
/*     */                   break;
/*     */                 } 
/*     */                 continue;
/*     */               } 
/*     */             } 
/* 234 */             if (localRequestStop) {
/*     */               
/* 236 */               MediaEncoder.this.callOnStop();
/* 237 */               MediaEncoder.this.drain();
/*     */               
/* 239 */               MediaEncoder.this.signalEndOfInputStream();
/*     */               
/* 241 */               MediaEncoder.this.drain();
/*     */               break;
/*     */             } 
/* 244 */             if (localRequestDrain) {
/* 245 */               MediaEncoder.this.drain();
/*     */             }
/*     */           } 
/*     */           
/* 249 */           MediaEncoder.this.release();
/*     */           
/* 251 */           synchronized (MediaEncoder.this.mSync) {
/* 252 */             MediaEncoder.this.mRequestStop = true;
/* 253 */             MediaEncoder.this.mIsCapturing = false;
/*     */           } 
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 444 */     this.prevOutputPTSUs = 0L; if (listener == null)
/*     */       throw new NullPointerException("MediaEncoderListener is null");  if (muxer == null)
/*     */       throw new NullPointerException("MediaMuxerWrapper is null");  this.mIsAudio = is_audio; this.mWeakMuxer = new WeakReference<>(muxer); muxer.addEncoder(this); this.mCallback = listener; synchronized (this.mSync) {
/*     */       this.mBufferInfo = new MediaCodec.BufferInfo(); (new Thread(this.mEncodeTask, getClass().getSimpleName())).start(); try {
/*     */         this.mSync.wait();
/*     */       } catch (InterruptedException interruptedException) {}
/* 450 */     }  } protected long getPTSUs() { long result = System.nanoTime() / 1000L;
/*     */ 
/*     */     
/* 453 */     if (result < this.prevOutputPTSUs)
/* 454 */       result = this.prevOutputPTSUs - result + result; 
/* 455 */     return result; }
/*     */ 
/*     */   
/*     */   public void release() {
/*     */     this.mIsCapturing = false;
/*     */     if (this.mMediaCodec != null)
/*     */       try {
/*     */         this.mMediaCodec.stop();
/*     */         this.mMediaCodec.release();
/*     */         this.mMediaCodec = null;
/*     */       } catch (Exception e) {
/*     */         boolean handled = callErrorHandler(e);
/*     */         if (!handled)
/*     */           Log.e(this.TAG, "failed releasing MediaCodec", e); 
/*     */       }  
/*     */     if (this.mMuxerStarted) {
/*     */       AbstractRecorder muxer = this.mWeakMuxer.get();
/*     */       if (muxer != null)
/*     */         try {
/*     */           muxer.stop();
/*     */         } catch (Exception e) {
/*     */           boolean handled = callErrorHandler(e);
/*     */           if (!handled)
/*     */             Log.e(this.TAG, "failed stopping muxer", e); 
/*     */         }  
/*     */     } 
/*     */     this.mBufferInfo = null;
/*     */     callOnRelease();
/*     */   }
/*     */   
/*     */   protected void signalEndOfInputStream() { encode(null, 0, getPTSUs()); }
/*     */   
/*     */   protected void encode(ByteBuffer buffer, int length, long presentationTimeUs) {
/*     */     if (!this.mIsCapturing || this.mRequestStop || this.mMediaCodec == null)
/*     */       return; 
/*     */     ByteBuffer[] inputBuffers = this.mMediaCodec.getInputBuffers();
/*     */     while (this.mIsCapturing) {
/*     */       int inputBufferIndex = this.mMediaCodec.dequeueInputBuffer(10000L);
/*     */       if (inputBufferIndex >= 0) {
/*     */         ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
/*     */         inputBuffer.clear();
/*     */         if (buffer != null)
/*     */           inputBuffer.put(buffer); 
/*     */         if (length > 0) {
/*     */           this.mMediaCodec.queueInputBuffer(inputBufferIndex, 0, length, presentationTimeUs, 0);
/*     */           break;
/*     */         } 
/*     */         this.mIsEOS = true;
/*     */         this.mMediaCodec.queueInputBuffer(inputBufferIndex, 0, 0, presentationTimeUs, 4);
/*     */         break;
/*     */       } 
/*     */       if (inputBufferIndex == -1);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void drain() {
/*     */     if (this.mMediaCodec == null)
/*     */       return; 
/*     */     ByteBuffer[] encoderOutputBuffers = this.mMediaCodec.getOutputBuffers();
/*     */     int count = 0;
/*     */     AbstractRecorder muxer = this.mWeakMuxer.get();
/*     */     if (muxer == null) {
/*     */       Log.w(this.TAG, "muxer is unexpectedly null");
/*     */       return;
/*     */     } 
/*     */     label61: while (this.mIsCapturing) {
/*     */       try {
/*     */         int encoderStatus = this.mMediaCodec.dequeueOutputBuffer(this.mBufferInfo, 10000L);
/*     */         if (encoderStatus == -1) {
/*     */           if (!this.mIsEOS && ++count > 5)
/*     */             break; 
/*     */           continue;
/*     */         } 
/*     */         if (encoderStatus == -3) {
/*     */           encoderOutputBuffers = this.mMediaCodec.getOutputBuffers();
/*     */           continue;
/*     */         } 
/*     */         if (encoderStatus == -2) {
/*     */           if (this.mMuxerStarted) {
/*     */             RuntimeException e = new RuntimeException("format changed twice");
/*     */             boolean handled = callErrorHandler(e);
/*     */             if (!handled)
/*     */               throw e; 
/*     */           } 
/*     */           MediaFormat format = this.mMediaCodec.getOutputFormat();
/*     */           this.mTrackIndex = muxer.addTrack(format);
/*     */           this.mMuxerStarted = true;
/*     */           if (!muxer.start())
/*     */             synchronized (muxer) {
/*     */               while (!muxer.isStarted() && this.mIsCapturing) {
/*     */                 try {
/*     */                   muxer.wait(100L);
/*     */                 } catch (InterruptedException e) {
/*     */                   break label61;
/*     */                 } 
/*     */               } 
/*     */               continue;
/*     */             }  
/*     */           continue;
/*     */         } 
/*     */         if (encoderStatus < 0)
/*     */           continue; 
/*     */         ByteBuffer encodedData = encoderOutputBuffers[encoderStatus];
/*     */         if (encodedData == null) {
/*     */           RuntimeException e = new RuntimeException("encoderOutputBuffer " + encoderStatus + " was null");
/*     */           boolean handled = callErrorHandler(e);
/*     */           if (!handled)
/*     */             throw e; 
/*     */         } 
/*     */         if ((this.mBufferInfo.flags & 0x2) != 0)
/*     */           this.mBufferInfo.size = 0; 
/*     */         if (this.mBufferInfo.size != 0) {
/*     */           count = 0;
/*     */           if (!this.mMuxerStarted)
/*     */             throw new RuntimeException("drain:muxer hasn't started"); 
/*     */           this.mBufferInfo.presentationTimeUs = getPTSUs();
/*     */           muxer.writeSampleData(this.mTrackIndex, encodedData, this.mBufferInfo);
/*     */           this.prevOutputPTSUs = this.mBufferInfo.presentationTimeUs;
/*     */         } 
/*     */         this.mMediaCodec.releaseOutputBuffer(encoderStatus, false);
/*     */         if ((this.mBufferInfo.flags & 0x4) != 0) {
/*     */           this.mIsCapturing = false;
/*     */           break;
/*     */         } 
/*     */       } catch (Exception e) {
/*     */         callErrorHandler(e);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\MediaEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */