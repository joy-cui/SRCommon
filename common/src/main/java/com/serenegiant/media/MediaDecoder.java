/*     */ package com.serenegiant.media;
/*     */ 
/*     */ import android.annotation.TargetApi;
/*     */ import android.content.Context;
/*     */ import android.media.MediaCodec;
/*     */ import android.media.MediaExtractor;
/*     */ import android.media.MediaFormat;
/*     */ import android.media.MediaMetadataRetriever;
/*     */ import android.net.Uri;
/*     */ import android.text.TextUtils;
/*     */ import android.util.Log;
/*     */ import android.view.Surface;
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class MediaDecoder
/*     */   implements IMediaCodec
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*  43 */   private static final String TAG_STATIC = MediaDecoder.class.getSimpleName();
/*  44 */   protected final String TAG = getClass().getSimpleName();
/*     */ 
/*     */   
/*     */   protected static final int STATE_UNINITIALIZED = 0;
/*     */ 
/*     */   
/*     */   protected static final int STATE_INITIALIZED = 1;
/*     */ 
/*     */   
/*     */   protected static final int STATE_PREPARED = 2;
/*     */ 
/*     */   
/*     */   protected static final int STATE_PLAYING = 3;
/*     */   
/*     */   protected static final int STATE_PAUSED = 4;
/*     */   
/*     */   protected static final int STATE_WAIT = 5;
/*     */   
/*     */   private static final int TIMEOUT_USEC = 10000;
/*     */   
/*  64 */   private final Object mSync = new Object();
/*     */   
/*     */   private IMediaCodecCallback mCallback;
/*     */   
/*     */   private volatile boolean mIsRunning;
/*     */   private volatile boolean mInputDone;
/*     */   private volatile boolean mOutputDone;
/*     */   private MediaMetadataRetriever mMediaMetadataRetriever;
/*     */   private MediaExtractor mMediaExtractor;
/*     */   private MediaCodec mMediaCodec;
/*     */   private int mTrackIndex;
/*     */   private long mDuration;
/*     */   private int mBitRate;
/*     */   private MediaCodec.BufferInfo mBufferInfo;
/*     */   private ByteBuffer[] mInputBuffers;
/*     */   private ByteBuffer[] mOutputBuffers;
/*     */   private long mStartTime;
/*     */   private long presentationTimeUs;
/*  82 */   private long mRequestTime = -1L;
/*     */   
/*  84 */   protected int mState = 0;
/*     */ 
/*     */   
/*     */   private final Runnable mPlaybackTask;
/*     */ 
/*     */   
/*  90 */   public void setCallback(IMediaCodecCallback callback) { this.mCallback = callback; }
/*     */ 
/*     */ 
/*     */   
/*  94 */   public IMediaCodecCallback getCallback() { return this.mCallback; }
/*     */ 
/*     */ 
/*     */   
/*  98 */   public long getDuration() { return this.mDuration; }
/*     */ 
/*     */ 
/*     */   
/* 102 */   public int getBitRate() { return this.mBitRate; }
/*     */ 
/*     */   
/*     */   public void setDataSource(String path) throws IOException {
/* 106 */     release();
/*     */     try {
/* 108 */       this.mMediaMetadataRetriever = new MediaMetadataRetriever();
/* 109 */       this.mMediaMetadataRetriever.setDataSource(path);
/* 110 */       updateMovieInfo(this.mMediaMetadataRetriever);
/* 111 */       this.mMediaExtractor = new MediaExtractor();
/* 112 */       this.mMediaExtractor.setDataSource(path);
/* 113 */       this.mState = 1;
/* 114 */     } catch (IOException e) {
/* 115 */       internal_release();
/* 116 */       if (!callErrorHandler(e))
/* 117 */         throw e; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setDataSource(String path, Map<String, String> headers) throws IOException {
/* 122 */     release();
/*     */     try {
/* 124 */       this.mMediaMetadataRetriever = new MediaMetadataRetriever();
/* 125 */       this.mMediaMetadataRetriever.setDataSource(path, headers);
/* 126 */       updateMovieInfo(this.mMediaMetadataRetriever);
/* 127 */       this.mMediaExtractor = new MediaExtractor();
/* 128 */       this.mMediaExtractor.setDataSource(path, headers);
/* 129 */       this.mState = 1;
/* 130 */     } catch (IOException e) {
/* 131 */       internal_release();
/* 132 */       if (!callErrorHandler(e))
/* 133 */         throw e; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setDataSource(FileDescriptor fd) throws IOException {
/* 138 */     release();
/*     */     try {
/* 140 */       this.mMediaMetadataRetriever = new MediaMetadataRetriever();
/* 141 */       this.mMediaMetadataRetriever.setDataSource(fd);
/* 142 */       updateMovieInfo(this.mMediaMetadataRetriever);
/* 143 */       this.mMediaExtractor = new MediaExtractor();
/* 144 */       this.mMediaExtractor.setDataSource(fd);
/* 145 */       this.mState = 1;
/* 146 */     } catch (IOException e) {
/* 147 */       internal_release();
/* 148 */       if (!callErrorHandler(e))
/* 149 */         throw e; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setDataSource(FileDescriptor fd, long offset, long length) throws IOException {
/* 154 */     release();
/*     */     try {
/* 156 */       this.mMediaMetadataRetriever = new MediaMetadataRetriever();
/* 157 */       this.mMediaMetadataRetriever.setDataSource(fd, offset, length);
/* 158 */       updateMovieInfo(this.mMediaMetadataRetriever);
/* 159 */       this.mMediaExtractor = new MediaExtractor();
/* 160 */       this.mMediaExtractor.setDataSource(fd, offset, length);
/* 161 */       this.mState = 1;
/* 162 */     } catch (IOException e) {
/* 163 */       internal_release();
/* 164 */       if (!callErrorHandler(e))
/* 165 */         throw e; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setDataSource(Context context, Uri uri, Map<String, String> headers) throws IOException {
/* 170 */     release();
/*     */     try {
/* 172 */       this.mMediaMetadataRetriever = new MediaMetadataRetriever();
/* 173 */       this.mMediaMetadataRetriever.setDataSource(context, uri);
/* 174 */       updateMovieInfo(this.mMediaMetadataRetriever);
/* 175 */       this.mMediaExtractor = new MediaExtractor();
/* 176 */       this.mMediaExtractor.setDataSource(context, uri, headers);
/* 177 */       this.mState = 1;
/* 178 */     } catch (IOException e) {
/* 179 */       internal_release();
/* 180 */       if (!callErrorHandler(e))
/* 181 */         throw e; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setDataSource(Context context, Uri uri) throws IOException {
/* 186 */     release();
/* 187 */     this.mMediaMetadataRetriever = new MediaMetadataRetriever();
/*     */     try {
/* 189 */       this.mMediaMetadataRetriever.setDataSource(context, uri);
/* 190 */       updateMovieInfo(this.mMediaMetadataRetriever);
/* 191 */       this.mMediaExtractor = new MediaExtractor();
/* 192 */       this.mMediaExtractor.setDataSource(context, uri, null);
/* 193 */       this.mState = 1;
/* 194 */     } catch (IOException e) {
/* 195 */       release();
/* 196 */       if (!callErrorHandler(e)) {
/* 197 */         throw e;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void prepare() throws IOException {
/* 203 */     if (this.mMediaExtractor == null) {
/* 204 */       IllegalStateException e = new IllegalStateException("DataSource not set yet");
/* 205 */       if (!callErrorHandler(e))
/* 206 */         throw e; 
/*     */       return;
/*     */     } 
/* 209 */     if (this.mState != 1) {
/* 210 */       IllegalStateException e = new IllegalStateException("already prepared");
/* 211 */       if (!callErrorHandler(e)) {
/* 212 */         throw e;
/*     */       }
/*     */       return;
/*     */     } 
/*     */     try {
/* 217 */       this.mTrackIndex = handlePrepare(this.mMediaExtractor);
/* 218 */       if (this.mTrackIndex >= 0) {
/* 219 */         this.mMediaExtractor.selectTrack(this.mTrackIndex);
/* 220 */         MediaFormat format = this.mMediaExtractor.getTrackFormat(this.mTrackIndex);
/* 221 */         this.mDuration = format.getLong("durationUs");
/* 222 */         this.mMediaCodec = createCodec(this.mMediaExtractor, this.mTrackIndex, format);
/*     */       } else {
/* 224 */         throw new IOException("track not found");
/*     */       } 
/* 226 */     } catch (Exception e) {
/* 227 */       if (this.mMediaExtractor != null) {
/* 228 */         this.mMediaExtractor.release();
/* 229 */         this.mMediaExtractor = null;
/*     */       } 
/* 231 */       if (!callErrorHandler(e))
/* 232 */         throw e; 
/*     */     } 
/* 234 */     if (this.mTrackIndex >= 0 && this.mMediaCodec != null) {
/* 235 */       this.mState = 2;
/* 236 */       callOnPrepared();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 242 */   public boolean isPrepared() { return (this.mState >= 2); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 247 */   public boolean isRunning() { return (this.mState == 3); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MediaCodec createCodec(MediaExtractor media_extractor, int track_index, MediaFormat format) throws IOException {
/* 255 */     MediaCodec codec = null;
/* 256 */     if (track_index >= 0) {
/* 257 */       String mime = format.getString("mime");
/* 258 */       codec = MediaCodec.createDecoderByType(mime);
/* 259 */       codec.configure(format, getOutputSurface(), null, 0);
/* 260 */       codec.start();
/*     */     } 
/*     */     
/* 263 */     return codec;
/*     */   }
/*     */ 
/*     */   
/*     */   public void restart() {
/* 268 */     if (this.mState == 5) {
/* 269 */       synchronized (this.mSync) {
/* 270 */         this.mMediaExtractor.unselectTrack(this.mTrackIndex);
/* 271 */         this.mMediaExtractor.selectTrack(this.mTrackIndex);
/* 272 */         this.mState = 3;
/* 273 */         this.mSync.notifyAll();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/* 281 */     boolean needRestart = true;
/* 282 */     switch (this.mState) {
/*     */       case 3:
/*     */         return;
/*     */       case 4:
/* 286 */         needRestart = false;
/*     */       case 2:
/* 288 */         this.mState = 3;
/*     */         break;
/*     */       default:
/* 291 */         throw new IllegalStateException("invalid state:" + this.mState);
/*     */     } 
/* 293 */     if (needRestart) {
/* 294 */       this.presentationTimeUs = -1L;
/* 295 */       this.mBufferInfo = new MediaCodec.BufferInfo();
/* 296 */       this.mInputBuffers = this.mMediaCodec.getInputBuffers();
/* 297 */       this.mOutputBuffers = this.mMediaCodec.getOutputBuffers();
/* 298 */       (new Thread(this.mPlaybackTask, this.TAG)).start();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 305 */     synchronized (this.mSync) {
/* 306 */       this.mIsRunning = false;
/* 307 */       if (this.mState >= 3) {
/* 308 */         this.mSync.notifyAll();
/*     */         try {
/* 310 */           this.mSync.wait(50L);
/* 311 */         } catch (InterruptedException interruptedException) {}
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final void internal_stop() {
/* 319 */     switch (this.mState) {
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/* 323 */         if (this.mMediaCodec != null) {
/* 324 */           this.mMediaCodec.stop();
/*     */         }
/* 326 */         this.mState = 2;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void pause() {
/* 332 */     switch (this.mState) {
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/* 336 */         this.mState = 4;
/*     */         return;
/*     */     } 
/* 339 */     IllegalStateException e = new IllegalStateException();
/* 340 */     if (!callErrorHandler(e)) {
/* 341 */       throw e;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void release() {
/* 348 */     if (this.mState != 0) {
/* 349 */       stop();
/* 350 */       this.mState = 0;
/* 351 */       callOnRelease();
/*     */     } 
/* 353 */     internal_release();
/*     */   }
/*     */   
/*     */   private void internal_release() {
/* 357 */     if (this.mMediaCodec != null) {
/* 358 */       this.mMediaCodec.release();
/* 359 */       this.mMediaCodec = null;
/*     */     } 
/* 361 */     if (this.mMediaExtractor != null) {
/* 362 */       this.mMediaExtractor.release();
/* 363 */       this.mMediaExtractor = null;
/*     */     } 
/* 365 */     if (this.mMediaMetadataRetriever != null) {
/* 366 */       this.mMediaMetadataRetriever.release();
/* 367 */       this.mMediaMetadataRetriever = null;
/*     */     } 
/* 369 */     this.mTrackIndex = -1;
/* 370 */     this.mDuration = 0L;
/* 371 */     this.mBitRate = 0;
/*     */   }
/*     */ 
/*     */   
/* 375 */   public void seek(long newTime) { this.mRequestTime = newTime; }
/*     */ 
/*     */ 
/*     */   
/*     */   private final void handleSeek(long newTime) {
/* 380 */     if (newTime < 0L)
/*     */       return; 
/* 382 */     if (this.mMediaExtractor != null) {
/*     */       
/* 384 */       this.mMediaExtractor.seekTo(newTime, 0);
/* 385 */       this.mMediaExtractor.advance();
/*     */     } 
/* 387 */     this.mRequestTime = -1L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaDecoder() {
/* 393 */     this.mPlaybackTask = new Runnable()
/*     */       {
/*     */         public final void run()
/*     */         {
/* 397 */           MediaDecoder.this.mInputDone = MediaDecoder.this.mOutputDone = false;
/* 398 */           MediaDecoder.this.mIsRunning = true;
/* 399 */           MediaDecoder.this.callOnStart();
/* 400 */           while (!MediaDecoder.this.mInputDone || !MediaDecoder.this.mOutputDone) {
/*     */             try {
/* 402 */               if (MediaDecoder.this.mRequestTime >= 0L) {
/* 403 */                 MediaDecoder.this.handleSeek(MediaDecoder.this.mRequestTime);
/*     */               }
/* 405 */               if (!MediaDecoder.this.mInputDone) {
/* 406 */                 MediaDecoder.this.internal_HandleInput();
/*     */               }
/* 408 */               if (!MediaDecoder.this.mOutputDone) {
/* 409 */                 MediaDecoder.this.internal_handleOutput();
/*     */               }
/* 411 */               if (!MediaDecoder.this.mIsRunning || (MediaDecoder.this.mInputDone && MediaDecoder.this.mOutputDone)) {
/* 412 */                 MediaDecoder.this.mState = 5;
/* 413 */                 MediaDecoder.this.callOnStop();
/* 414 */                 if (MediaDecoder.this.mIsRunning) {
/* 415 */                   MediaDecoder.this.mMediaCodec.flush();
/* 416 */                   synchronized (MediaDecoder.this.mSync) {
/* 417 */                     if (MediaDecoder.this.mState == 5) {
/*     */                       try {
/* 419 */                         MediaDecoder.this.mSync.wait();
/* 420 */                       } catch (InterruptedException interruptedException) {}
/*     */                     }
/*     */                   } 
/*     */                   
/* 424 */                   if (MediaDecoder.this.mIsRunning) {
/* 425 */                     MediaDecoder.this.callOnStart();
/* 426 */                     MediaDecoder.this.mStartTime = MediaDecoder.this.presentationTimeUs = -1L;
/* 427 */                     MediaDecoder.this.mInputDone = MediaDecoder.this.mOutputDone = false;
/* 428 */                     MediaDecoder.this.mState = 3;
/*     */                     continue;
/*     */                   } 
/*     */                 } 
/*     */                 break;
/*     */               } 
/* 434 */             } catch (Exception e) {
/* 435 */               Log.e(MediaDecoder.this.TAG, "PlaybackTask:", e);
/* 436 */               MediaDecoder.this.callErrorHandler(e);
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/* 441 */           MediaDecoder.this.internal_stop();
/* 442 */           synchronized (MediaDecoder.this.mSync) {
/* 443 */             MediaDecoder.this.mSync.notifyAll();
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   protected void internal_HandleInput() {
/* 450 */     boolean b = false;
/* 451 */     if (!this.mInputDone) {
/* 452 */       if (this.presentationTimeUs < 0L) {
/* 453 */         this.presentationTimeUs = this.mMediaExtractor.getSampleTime();
/*     */       }
/*     */       
/* 456 */       if (this.presentationTimeUs >= 0L) {
/* 457 */         this.presentationTimeUs = handleInput(this.presentationTimeUs);
/* 458 */         b = true;
/*     */       } 
/*     */     } 
/* 461 */     if (!b) {
/*     */       
/* 463 */       int inputBufIndex = this.mMediaCodec.dequeueInputBuffer(10000L);
/* 464 */       if (inputBufIndex >= 0) {
/* 465 */         this.mMediaCodec.queueInputBuffer(inputBufIndex, 0, 0, 0L, 4);
/*     */ 
/*     */         
/* 468 */         this.mInputDone = true;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected long handleInput(long presentationTimeUs) {
/* 474 */     long result = presentationTimeUs;
/* 475 */     int inputBufIndex = this.mMediaCodec.dequeueInputBuffer(10000L);
/* 476 */     if (inputBufIndex >= 0) {
/* 477 */       int size = this.mMediaExtractor.readSampleData(this.mInputBuffers[inputBufIndex], 0);
/*     */       
/* 479 */       if (size > 0) {
/* 480 */         this.mMediaCodec.queueInputBuffer(inputBufIndex, 0, size, presentationTimeUs, 0);
/*     */       }
/* 482 */       this.mMediaExtractor.advance();
/* 483 */       result = -1L;
/*     */     } 
/* 485 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void internal_handleOutput() {
/* 491 */     int decoderStatus = this.mMediaCodec.dequeueOutputBuffer(this.mBufferInfo, 10000L);
/* 492 */     if (decoderStatus == -1)
/*     */       return; 
/* 494 */     if (decoderStatus == -3) {
/* 495 */       this.mOutputBuffers = this.mMediaCodec.getOutputBuffers();
/*     */     }
/* 497 */     else if (decoderStatus == -2) {
/* 498 */       MediaFormat mediaFormat = this.mMediaCodec.getOutputFormat();
/*     */     }
/* 500 */     else if (decoderStatus < 0) {
/* 501 */       RuntimeException e = new RuntimeException("unexpected result from dequeueOutputBuffer: " + decoderStatus);
/*     */       
/* 503 */       if (!callErrorHandler(e))
/* 504 */         throw e; 
/*     */     } else {
/* 506 */       boolean doRender = false;
/* 507 */       if (this.mBufferInfo.size > 0) {
/* 508 */         doRender = !handleOutput(this.mOutputBuffers[decoderStatus], 0, this.mBufferInfo.size, this.mBufferInfo.presentationTimeUs);
/*     */         
/* 510 */         if (doRender && (
/* 511 */           this.mCallback == null || !this.mCallback.onFrameAvailable(this, this.mBufferInfo.presentationTimeUs))) {
/* 512 */           this.mStartTime = adjustPresentationTime(this.mStartTime, this.mBufferInfo.presentationTimeUs);
/*     */         }
/*     */       } 
/* 515 */       this.mMediaCodec.releaseOutputBuffer(decoderStatus, doRender);
/* 516 */       if ((this.mBufferInfo.flags & 0x4) != 0)
/*     */       {
/* 518 */         this.mOutputDone = true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean callErrorHandler(Exception e) {
/* 534 */     if (this.mCallback != null) {
/* 535 */       return this.mCallback.onError(this, e);
/*     */     }
/* 537 */     return false;
/*     */   }
/*     */   
/*     */   protected void callOnPrepared() {
/* 541 */     if (this.mCallback != null) {
/*     */       try {
/* 543 */         this.mCallback.onPrepared(this);
/* 544 */       } catch (Exception e) {
/* 545 */         Log.w(this.TAG, "callOnPrepared", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected void callOnStart() {
/* 551 */     if (this.mCallback != null) {
/*     */       try {
/* 553 */         this.mCallback.onStart(this);
/* 554 */       } catch (Exception e) {
/* 555 */         Log.w(this.TAG, "callOnStart", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected void callOnStop() {
/* 561 */     if (this.mCallback != null) {
/*     */       try {
/* 563 */         this.mCallback.onStop(this);
/* 564 */       } catch (Exception e) {
/* 565 */         Log.w(this.TAG, "callOnStop", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected void callOnRelease() {
/* 571 */     if (this.mCallback != null) {
/*     */       try {
/* 573 */         this.mCallback.onRelease(this);
/* 574 */       } catch (Exception e) {
/* 575 */         Log.w(this.TAG, "callOnRelease", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected void updateMovieInfo(MediaMetadataRetriever metadata) {
/* 581 */     this.mBitRate = 0;
/* 582 */     String value = metadata.extractMetadata(20);
/* 583 */     if (!TextUtils.isEmpty(value)) {
/* 584 */       this.mBitRate = Integer.parseInt(value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long adjustPresentationTime(long startTime, long presentationTimeUs) {
/* 595 */     if (startTime > 0L) {
/* 596 */       long t = presentationTimeUs - System.nanoTime() / 1000L - startTime;
/* 597 */       for (; t > 0L; t = presentationTimeUs - System.nanoTime() / 1000L - startTime) {
/* 598 */         synchronized (this.mSync) {
/*     */           try {
/* 600 */             this.mSync.wait(t / 1000L, (int)(t % 1000L * 1000L));
/* 601 */           } catch (InterruptedException interruptedException) {}
/*     */ 
/*     */           
/* 604 */           if (!this.mIsRunning)
/*     */             break; 
/*     */         } 
/*     */       } 
/* 608 */       return startTime;
/*     */     } 
/* 610 */     return System.nanoTime() / 1000L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final int selectTrack(MediaExtractor extractor, String mimeType) {
/* 621 */     int numTracks = extractor.getTrackCount();
/*     */ 
/*     */     
/* 624 */     for (int i = 0; i < numTracks; i++) {
/* 625 */       MediaFormat format = extractor.getTrackFormat(i);
/* 626 */       String mime = format.getString("mime");
/* 627 */       if (mime.startsWith(mimeType))
/*     */       {
/* 629 */         return i;
/*     */       }
/*     */     } 
/* 632 */     return -1;
/*     */   }
/*     */   
/*     */   protected abstract int handlePrepare(MediaExtractor paramMediaExtractor);
/*     */   
/*     */   protected abstract Surface getOutputSurface();
/*     */   
/*     */   protected abstract boolean handleOutput(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, long paramLong);
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\MediaDecoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */