/*      */ package com.serenegiant.media;
/*      */ 
/*      */ import android.annotation.SuppressLint;
/*      */ import android.annotation.TargetApi;
/*      */ import android.content.res.AssetFileDescriptor;
/*      */ import android.media.AudioTrack;
/*      */ import android.media.MediaCodec;
/*      */ import android.media.MediaExtractor;
/*      */ import android.media.MediaFormat;
/*      */ import android.media.MediaMetadataRetriever;
/*      */ import android.text.TextUtils;
/*      */ import android.util.Log;
/*      */ import android.view.Surface;
/*      */ import com.serenegiant.utils.BuildCheck;
/*      */ import java.io.File;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.nio.ByteBuffer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @SuppressLint({"InlinedApi"})
/*      */ @TargetApi(16)
/*      */ public class MediaMoviePlayer
/*      */ {
/*      */   private static final boolean DEBUG = false;
/*      */   private static final String TAG_STATIC = "MediaMoviePlayer:";
/*   49 */   private final String TAG = "MediaMoviePlayer:" + getClass().getSimpleName(); private final IFrameCallback mCallback; private final boolean mAudioEnabled; private static final int TIMEOUT_USEC = 10000; private static final int STATE_STOP = 0; private static final int STATE_PREPARED = 1; private static final int STATE_PLAYING = 2; private static final int STATE_PAUSED = 3; private static final int REQ_NON = 0; private static final int REQ_PREPARE = 1; private static final int REQ_START = 2; private static final int REQ_SEEK = 3; private static final int REQ_STOP = 4; private static final int REQ_PAUSE = 5; private static final int REQ_RESUME = 6; private static final int REQ_QUIT = 9; protected MediaMetadataRetriever mMetadata;
/*      */   private final Object mSync;
/*      */   private volatile boolean mIsRunning;
/*      */   private int mState;
/*      */   private Object mSource;
/*      */   private long mDuration;
/*      */   private int mRequest;
/*      */   private long mRequestTime;
/*      */   private final Object mVideoSync;
/*      */   private final Surface mOutputSurface;
/*      */   protected MediaExtractor mVideoMediaExtractor;
/*      */   private MediaCodec mVideoMediaCodec;
/*      */   private MediaCodec.BufferInfo mVideoBufferInfo;
/*      */   private ByteBuffer[] mVideoInputBuffers;
/*      */   private ByteBuffer[] mVideoOutputBuffers;
/*      */   private long mVideoStartTime;
/*      */   private long previousVideoPresentationTimeUs;
/*      */   private volatile int mVideoTrackIndex;
/*      */   private boolean mVideoInputDone;
/*      */   private boolean mVideoOutputDone;
/*      */   private int mVideoWidth;
/*      */   private int mVideoHeight;
/*      */   private int mBitrate;
/*      */   
/*   73 */   public final int getWidth() { return this.mVideoWidth; }
/*      */   private float mFrameRate; private int mRotation; private final Object mAudioSync; protected MediaExtractor mAudioMediaExtractor; private MediaCodec mAudioMediaCodec; private MediaCodec.BufferInfo mAudioBufferInfo;
/*      */   private ByteBuffer[] mAudioInputBuffers;
/*      */   
/*   77 */   public final int getHeight() { return this.mVideoHeight; }
/*      */   private ByteBuffer[] mAudioOutputBuffers; private long mAudioStartTime; private long previousAudioPresentationTimeUs; private volatile int mAudioTrackIndex; private boolean mAudioInputDone; private boolean mAudioOutputDone; private int mAudioChannels; private int mAudioSampleRate; private int mAudioInputBufSize; private boolean mHasAudio; private byte[] mAudioOutTempBuf; private AudioTrack mAudioTrack; private final Runnable mMoviePlayerTask; private final Runnable mVideoTask;
/*      */   private final Runnable mAudioTask;
/*      */   
/*   81 */   public final int getBitRate() { return this.mBitrate; }
/*      */ 
/*      */ 
/*      */   
/*   85 */   public final float getFramerate() { return this.mFrameRate; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   92 */   public final int getRotation() { return this.mRotation; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  100 */   public final long getDurationUs() { return this.mDuration; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  108 */   public final int getSampleRate() { return this.mAudioSampleRate; }
/*      */ 
/*      */ 
/*      */   
/*  112 */   public final boolean hasAudio() { return this.mHasAudio; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void prepare(String src) {
/*  121 */     synchronized (this.mSync) {
/*  122 */       this.mSource = src;
/*  123 */       this.mRequest = 1;
/*  124 */       this.mSync.notifyAll();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void prepare(AssetFileDescriptor src) {
/*  134 */     synchronized (this.mSync) {
/*  135 */       this.mSource = src;
/*  136 */       this.mRequest = 1;
/*  137 */       this.mSync.notifyAll();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void play() {
/*  147 */     synchronized (this.mSync) {
/*  148 */       if (this.mState == 2)
/*  149 */         return;  this.mRequest = 2;
/*  150 */       this.mSync.notifyAll();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void seek(long newTime) {
/*  161 */     synchronized (this.mSync) {
/*  162 */       this.mRequest = 3;
/*  163 */       this.mRequestTime = newTime;
/*  164 */       this.mSync.notifyAll();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void stop() {
/*  173 */     synchronized (this.mSync) {
/*  174 */       if (this.mState != 0) {
/*  175 */         this.mRequest = 4;
/*  176 */         this.mSync.notifyAll();
/*      */         try {
/*  178 */           this.mSync.wait(50L);
/*  179 */         } catch (InterruptedException interruptedException) {}
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void pause() {
/*  191 */     synchronized (this.mSync) {
/*  192 */       this.mRequest = 5;
/*  193 */       this.mSync.notifyAll();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void resume() {
/*  203 */     synchronized (this.mSync) {
/*  204 */       this.mRequest = 6;
/*  205 */       this.mSync.notifyAll();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void release() {
/*  214 */     stop();
/*  215 */     synchronized (this.mSync) {
/*  216 */       this.mRequest = 9;
/*  217 */       this.mSync.notifyAll();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MediaMoviePlayer(Surface outputSurface, IFrameCallback callback, boolean audio_enable) throws NullPointerException
/*      */   {
/*  248 */     this.mSync = new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  256 */     this.mVideoSync = new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  264 */     this.previousVideoPresentationTimeUs = -1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  274 */     this.mAudioSync = new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  281 */     this.previousAudioPresentationTimeUs = -1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  297 */     this.mMoviePlayerTask = new Runnable()
/*      */       {
/*      */         public final void run() {
/*  300 */           boolean local_isRunning = false;
/*      */           
/*      */           try {
/*  303 */             synchronized (MediaMoviePlayer.this.mSync) {
/*  304 */               local_isRunning = MediaMoviePlayer.this.mIsRunning = true;
/*  305 */               MediaMoviePlayer.this.mState = 0;
/*  306 */               MediaMoviePlayer.this.mRequest = 0;
/*  307 */               MediaMoviePlayer.this.mRequestTime = -1L;
/*  308 */               MediaMoviePlayer.this.mSync.notifyAll();
/*      */             } 
/*  310 */             while (local_isRunning) {
/*      */               try {
/*  312 */                 int local_req; synchronized (MediaMoviePlayer.this.mSync) {
/*  313 */                   local_isRunning = MediaMoviePlayer.this.mIsRunning;
/*  314 */                   local_req = MediaMoviePlayer.this.mRequest;
/*  315 */                   MediaMoviePlayer.this.mRequest = 0;
/*      */                 } 
/*  317 */                 switch (MediaMoviePlayer.this.mState) {
/*      */                   case 0:
/*  319 */                     local_isRunning = MediaMoviePlayer.this.processStop(local_req);
/*      */                   
/*      */                   case 1:
/*  322 */                     local_isRunning = MediaMoviePlayer.this.processPrepared(local_req);
/*      */                   
/*      */                   case 2:
/*  325 */                     local_isRunning = MediaMoviePlayer.this.processPlaying(local_req);
/*      */                   
/*      */                   case 3:
/*  328 */                     local_isRunning = MediaMoviePlayer.this.processPaused(local_req);
/*      */                 } 
/*      */               
/*  331 */               } catch (InterruptedException e) {
/*      */                 break;
/*  333 */               } catch (Exception e) {
/*  334 */                 Log.e(MediaMoviePlayer.this.TAG, "MoviePlayerTask:", e);
/*      */                 
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           } finally {
/*  340 */             MediaMoviePlayer.this.handleStop();
/*      */           } 
/*      */         }
/*      */       };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  349 */     this.mVideoTask = new Runnable()
/*      */       {
/*      */         public void run()
/*      */         {
/*  353 */           while (MediaMoviePlayer.this.mIsRunning && !MediaMoviePlayer.this.mVideoInputDone && !MediaMoviePlayer.this.mVideoOutputDone) {
/*      */             try {
/*  355 */               if (!MediaMoviePlayer.this.mVideoInputDone) {
/*  356 */                 MediaMoviePlayer.this.handleInputVideo();
/*      */               }
/*  358 */               if (!MediaMoviePlayer.this.mVideoOutputDone) {
/*  359 */                 MediaMoviePlayer.this.handleOutputVideo(MediaMoviePlayer.this.mCallback);
/*      */               }
/*  361 */             } catch (Exception e) {
/*  362 */               Log.e(MediaMoviePlayer.this.TAG, "VideoTask:", e);
/*      */               
/*      */               break;
/*      */             } 
/*      */           } 
/*  367 */           synchronized (MediaMoviePlayer.this.mSync) {
/*  368 */             MediaMoviePlayer.this.mVideoInputDone = MediaMoviePlayer.this.mVideoOutputDone = true;
/*  369 */             MediaMoviePlayer.this.mSync.notifyAll();
/*      */           } 
/*      */         }
/*      */       };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  378 */     this.mAudioTask = new Runnable()
/*      */       {
/*      */         public void run()
/*      */         {
/*  382 */           while (MediaMoviePlayer.this.mIsRunning && !MediaMoviePlayer.this.mAudioInputDone && !MediaMoviePlayer.this.mAudioOutputDone) {
/*      */             try {
/*  384 */               if (!MediaMoviePlayer.this.mAudioInputDone) {
/*  385 */                 MediaMoviePlayer.this.handleInputAudio();
/*      */               }
/*  387 */               if (!MediaMoviePlayer.this.mAudioOutputDone) {
/*  388 */                 MediaMoviePlayer.this.handleOutputAudio(MediaMoviePlayer.this.mCallback);
/*      */               }
/*  390 */             } catch (Exception e) {
/*  391 */               Log.e(MediaMoviePlayer.this.TAG, "VideoTask:", e);
/*      */               
/*      */               break;
/*      */             } 
/*      */           } 
/*  396 */           synchronized (MediaMoviePlayer.this.mSync) {
/*  397 */             MediaMoviePlayer.this.mAudioInputDone = MediaMoviePlayer.this.mAudioOutputDone = true;
/*  398 */             MediaMoviePlayer.this.mSync.notifyAll();
/*      */           }  } };
/*      */     if (outputSurface == null || callback == null)
/*      */       throw new NullPointerException("outputSurface and callback should not be null"); 
/*      */     this.mOutputSurface = outputSurface;
/*      */     this.mCallback = callback;
/*      */     this.mAudioEnabled = audio_enable;
/*      */     (new Thread(this.mMoviePlayerTask, this.TAG)).start();
/*      */     synchronized (this.mSync) {
/*      */       try {
/*      */         if (!this.mIsRunning)
/*      */           this.mSync.wait(); 
/*      */       } catch (InterruptedException interruptedException) {}
/*  411 */     }  } private final boolean processStop(int req) throws InterruptedException, IOException { boolean local_isRunning = true;
/*  412 */     switch (req) {
/*      */       case 1:
/*  414 */         handlePrepare(this.mSource);
/*      */         break;
/*      */       case 2:
/*      */       case 5:
/*      */       case 6:
/*  419 */         throw new IllegalStateException("invalid state:" + this.mState);
/*      */       case 9:
/*  421 */         local_isRunning = false;
/*      */         break;
/*      */ 
/*      */       
/*      */       default:
/*  426 */         synchronized (this.mSync) {
/*  427 */           this.mSync.wait();
/*      */         } 
/*      */         break;
/*      */     } 
/*  431 */     synchronized (this.mSync) {
/*  432 */       local_isRunning &= this.mIsRunning;
/*      */     } 
/*  434 */     return local_isRunning; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final boolean processPrepared(int req) throws InterruptedException {
/*  443 */     boolean local_isRunning = true;
/*  444 */     switch (req) {
/*      */       case 2:
/*  446 */         handleStart();
/*      */         break;
/*      */       case 5:
/*      */       case 6:
/*  450 */         throw new IllegalStateException("invalid state:" + this.mState);
/*      */       case 4:
/*  452 */         handleStop();
/*      */         break;
/*      */       case 9:
/*  455 */         local_isRunning = false;
/*      */         break;
/*      */ 
/*      */       
/*      */       default:
/*  460 */         synchronized (this.mSync) {
/*  461 */           this.mSync.wait();
/*      */         } 
/*      */         break;
/*      */     } 
/*  465 */     synchronized (this.mSync) {
/*  466 */       local_isRunning &= this.mIsRunning;
/*      */     } 
/*  468 */     return local_isRunning;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final boolean processPlaying(int req) {
/*  476 */     boolean local_isRunning = true;
/*  477 */     switch (req) {
/*      */       case 1:
/*      */       case 2:
/*      */       case 6:
/*  481 */         throw new IllegalStateException("invalid state:" + this.mState);
/*      */       case 3:
/*  483 */         handleSeek(this.mRequestTime);
/*      */         break;
/*      */       case 4:
/*  486 */         handleStop();
/*      */         break;
/*      */       case 5:
/*  489 */         handlePause();
/*      */         break;
/*      */       case 9:
/*  492 */         local_isRunning = false;
/*      */         break;
/*      */       default:
/*  495 */         handleLoop(this.mCallback);
/*      */         break;
/*      */     } 
/*  498 */     synchronized (this.mSync) {
/*  499 */       local_isRunning &= this.mIsRunning;
/*      */     } 
/*  501 */     return local_isRunning;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final boolean processPaused(int req) throws InterruptedException {
/*  510 */     boolean local_isRunning = true;
/*  511 */     switch (req) {
/*      */       case 1:
/*      */       case 2:
/*  514 */         throw new IllegalStateException("invalid state:" + this.mState);
/*      */       case 3:
/*  516 */         handleSeek(this.mRequestTime);
/*      */         break;
/*      */       case 4:
/*  519 */         handleStop();
/*      */         break;
/*      */       case 6:
/*  522 */         handleResume();
/*      */         break;
/*      */       case 9:
/*  525 */         local_isRunning = false;
/*      */         break;
/*      */       
/*      */       default:
/*  529 */         synchronized (this.mSync) {
/*  530 */           this.mSync.wait();
/*      */         } 
/*      */         break;
/*      */     } 
/*  534 */     synchronized (this.mSync) {
/*  535 */       local_isRunning &= this.mIsRunning;
/*      */     } 
/*  537 */     return local_isRunning;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final void handlePrepare(Object source) throws IOException {
/*  549 */     synchronized (this.mSync) {
/*  550 */       if (this.mState != 0) {
/*  551 */         throw new RuntimeException("invalid state:" + this.mState);
/*      */       }
/*      */     } 
/*  554 */     this.mVideoTrackIndex = this.mAudioTrackIndex = -1;
/*  555 */     if (source instanceof String) {
/*  556 */       String srcString = (String)source;
/*  557 */       File src = new File(srcString);
/*  558 */       if (TextUtils.isEmpty(srcString) || !src.canRead()) {
/*  559 */         throw new FileNotFoundException("Unable to read " + source);
/*      */       }
/*  561 */       this.mMetadata = new MediaMetadataRetriever();
/*  562 */       this.mMetadata.setDataSource((String)source);
/*  563 */     } else if (source instanceof AssetFileDescriptor) {
/*  564 */       this.mMetadata = new MediaMetadataRetriever();
/*  565 */       this.mMetadata.setDataSource(((AssetFileDescriptor)source).getFileDescriptor());
/*      */     } else {
/*  567 */       throw new IllegalArgumentException("unknown source type:source=" + source);
/*      */     } 
/*  569 */     updateMovieInfo();
/*      */     
/*  571 */     this.mVideoTrackIndex = internal_prepare_video(source);
/*      */     
/*  573 */     if (this.mAudioEnabled)
/*  574 */       this.mAudioTrackIndex = internal_prepare_audio(source); 
/*  575 */     this.mHasAudio = (this.mAudioTrackIndex >= 0);
/*  576 */     if (this.mVideoTrackIndex < 0 && this.mAudioTrackIndex < 0) {
/*  577 */       throw new RuntimeException("No video and audio track found in " + source);
/*      */     }
/*  579 */     synchronized (this.mSync) {
/*  580 */       this.mState = 1;
/*      */     } 
/*  582 */     this.mCallback.onPrepared();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @SuppressLint({"NewApi"})
/*      */   protected int internal_prepare_video(Object source) {
/*  591 */     int trackindex = -1;
/*  592 */     this.mVideoMediaExtractor = new MediaExtractor();
/*      */     try {
/*  594 */       if (source instanceof String) {
/*  595 */         this.mVideoMediaExtractor.setDataSource((String)source);
/*  596 */       } else if (source instanceof AssetFileDescriptor) {
/*  597 */         if (BuildCheck.isAndroid7()) {
/*  598 */           this.mVideoMediaExtractor.setDataSource((AssetFileDescriptor)source);
/*      */         } else {
/*  600 */           this.mVideoMediaExtractor.setDataSource(((AssetFileDescriptor)source).getFileDescriptor());
/*      */         } 
/*      */       } else {
/*      */         
/*  604 */         throw new IllegalArgumentException("unknown source type:source=" + source);
/*      */       } 
/*  606 */       trackindex = selectTrack(this.mVideoMediaExtractor, "video/");
/*  607 */       if (trackindex >= 0) {
/*  608 */         this.mVideoMediaExtractor.selectTrack(trackindex);
/*  609 */         MediaFormat format = this.mVideoMediaExtractor.getTrackFormat(trackindex);
/*  610 */         this.mVideoWidth = format.getInteger("width");
/*  611 */         this.mVideoHeight = format.getInteger("height");
/*  612 */         this.mDuration = format.getLong("durationUs");
/*      */       
/*      */       }
/*      */     
/*      */     }
/*  617 */     catch (IOException iOException) {}
/*      */     
/*  619 */     return trackindex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @SuppressLint({"NewApi"})
/*      */   protected int internal_prepare_audio(Object source) {
/*  628 */     int trackindex = -1;
/*  629 */     this.mAudioMediaExtractor = new MediaExtractor();
/*      */     try {
/*  631 */       if (source instanceof String) {
/*  632 */         this.mAudioMediaExtractor.setDataSource((String)source);
/*  633 */       } else if (source instanceof AssetFileDescriptor) {
/*  634 */         if (BuildCheck.isAndroid7()) {
/*  635 */           this.mVideoMediaExtractor.setDataSource((AssetFileDescriptor)source);
/*      */         } else {
/*  637 */           this.mVideoMediaExtractor.setDataSource(((AssetFileDescriptor)source).getFileDescriptor());
/*      */         } 
/*      */       } else {
/*      */         
/*  641 */         throw new IllegalArgumentException("unknown source type:source=" + source);
/*      */       } 
/*  643 */       trackindex = selectTrack(this.mAudioMediaExtractor, "audio/");
/*  644 */       if (trackindex >= 0) {
/*  645 */         this.mAudioMediaExtractor.selectTrack(trackindex);
/*  646 */         MediaFormat format = this.mAudioMediaExtractor.getTrackFormat(trackindex);
/*  647 */         this.mAudioChannels = format.getInteger("channel-count");
/*  648 */         this.mAudioSampleRate = format.getInteger("sample-rate");
/*  649 */         int min_buf_size = AudioTrack.getMinBufferSize(this.mAudioSampleRate, (this.mAudioChannels == 1) ? 4 : 12, 2);
/*      */ 
/*      */         
/*  652 */         int max_input_size = format.getInteger("max-input-size");
/*  653 */         this.mAudioInputBufSize = (min_buf_size > 0) ? (min_buf_size * 4) : max_input_size;
/*  654 */         if (this.mAudioInputBufSize > max_input_size) this.mAudioInputBufSize = max_input_size; 
/*  655 */         int frameSizeInBytes = this.mAudioChannels * 2;
/*  656 */         this.mAudioInputBufSize = this.mAudioInputBufSize / frameSizeInBytes * frameSizeInBytes;
/*      */ 
/*      */         
/*  659 */         this.mAudioTrack = new AudioTrack(3, this.mAudioSampleRate, (this.mAudioChannels == 1) ? 4 : 12, 2, this.mAudioInputBufSize, 1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/*  666 */           this.mAudioTrack.play();
/*  667 */         } catch (Exception e) {
/*  668 */           Log.e(this.TAG, "failed to start audio track playing", e);
/*  669 */           this.mAudioTrack.release();
/*  670 */           this.mAudioTrack = null;
/*      */         } 
/*      */       } 
/*  673 */     } catch (IOException iOException) {}
/*      */     
/*  675 */     return trackindex;
/*      */   }
/*      */   
/*      */   protected void updateMovieInfo() {
/*  679 */     this.mVideoWidth = this.mVideoHeight = this.mRotation = this.mBitrate = 0;
/*  680 */     this.mDuration = 0L;
/*  681 */     this.mFrameRate = 0.0F;
/*  682 */     String value = this.mMetadata.extractMetadata(18);
/*  683 */     if (!TextUtils.isEmpty(value)) {
/*  684 */       this.mVideoWidth = Integer.parseInt(value);
/*      */     }
/*  686 */     value = this.mMetadata.extractMetadata(19);
/*  687 */     if (!TextUtils.isEmpty(value)) {
/*  688 */       this.mVideoHeight = Integer.parseInt(value);
/*      */     }
/*  690 */     value = this.mMetadata.extractMetadata(24);
/*  691 */     if (!TextUtils.isEmpty(value)) {
/*  692 */       this.mRotation = Integer.parseInt(value);
/*      */     }
/*  694 */     value = this.mMetadata.extractMetadata(20);
/*  695 */     if (!TextUtils.isEmpty(value)) {
/*  696 */       this.mBitrate = Integer.parseInt(value);
/*      */     }
/*  698 */     value = this.mMetadata.extractMetadata(9);
/*  699 */     if (!TextUtils.isEmpty(value)) {
/*  700 */       this.mDuration = Long.parseLong(value) * 1000L;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private final void handleStart() {
/*  706 */     synchronized (this.mSync) {
/*  707 */       if (this.mState != 1)
/*  708 */         throw new RuntimeException("invalid state:" + this.mState); 
/*  709 */       this.mState = 2;
/*      */     } 
/*  711 */     if (this.mRequestTime > 0L) {
/*  712 */       handleSeek(this.mRequestTime);
/*      */     }
/*  714 */     this.previousVideoPresentationTimeUs = this.previousAudioPresentationTimeUs = -1L;
/*  715 */     this.mVideoInputDone = this.mVideoOutputDone = true;
/*  716 */     Thread videoThread = null, audioThread = null;
/*  717 */     if (this.mVideoTrackIndex >= 0) {
/*  718 */       MediaCodec codec = internal_start_video(this.mVideoMediaExtractor, this.mVideoTrackIndex);
/*  719 */       if (codec != null) {
/*  720 */         this.mVideoMediaCodec = codec;
/*  721 */         this.mVideoBufferInfo = new MediaCodec.BufferInfo();
/*  722 */         this.mVideoInputBuffers = codec.getInputBuffers();
/*  723 */         this.mVideoOutputBuffers = codec.getOutputBuffers();
/*      */       } 
/*  725 */       this.mVideoInputDone = this.mVideoOutputDone = false;
/*  726 */       videoThread = new Thread(this.mVideoTask, "VideoTask");
/*      */     } 
/*  728 */     this.mAudioInputDone = this.mAudioOutputDone = true;
/*  729 */     if (this.mAudioTrackIndex >= 0) {
/*  730 */       MediaCodec codec = internal_start_audio(this.mAudioMediaExtractor, this.mAudioTrackIndex);
/*  731 */       if (codec != null) {
/*  732 */         this.mAudioMediaCodec = codec;
/*  733 */         this.mAudioBufferInfo = new MediaCodec.BufferInfo();
/*  734 */         this.mAudioInputBuffers = codec.getInputBuffers();
/*  735 */         this.mAudioOutputBuffers = codec.getOutputBuffers();
/*      */       } 
/*  737 */       this.mAudioInputDone = this.mAudioOutputDone = false;
/*  738 */       audioThread = new Thread(this.mAudioTask, "AudioTask");
/*      */     } 
/*  740 */     if (videoThread != null) videoThread.start(); 
/*  741 */     if (audioThread != null) audioThread.start();
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected MediaCodec internal_start_video(MediaExtractor media_extractor, int trackIndex) {
/*  751 */     MediaCodec codec = null;
/*  752 */     if (trackIndex >= 0) {
/*  753 */       MediaFormat format = media_extractor.getTrackFormat(trackIndex);
/*  754 */       String mime = format.getString("mime");
/*      */       try {
/*  756 */         codec = MediaCodec.createDecoderByType(mime);
/*  757 */         codec.configure(format, this.mOutputSurface, null, 0);
/*  758 */         codec.start();
/*      */       }
/*  760 */       catch (IOException e) {
/*  761 */         Log.w(this.TAG, e);
/*      */       } 
/*      */     } 
/*  764 */     return codec;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected MediaCodec internal_start_audio(MediaExtractor media_extractor, int trackIndex) {
/*  774 */     MediaCodec codec = null;
/*  775 */     if (trackIndex >= 0) {
/*  776 */       MediaFormat format = media_extractor.getTrackFormat(trackIndex);
/*  777 */       String mime = format.getString("mime");
/*      */       try {
/*  779 */         codec = MediaCodec.createDecoderByType(mime);
/*  780 */         codec.configure(format, null, null, 0);
/*  781 */         codec.start();
/*      */ 
/*      */         
/*  784 */         ByteBuffer[] buffers = codec.getOutputBuffers();
/*  785 */         int sz = buffers[0].capacity();
/*  786 */         if (sz <= 0) {
/*  787 */           sz = this.mAudioInputBufSize;
/*      */         }
/*  789 */         this.mAudioOutTempBuf = new byte[sz];
/*  790 */       } catch (IOException e) {
/*  791 */         Log.w(this.TAG, e);
/*      */       } 
/*      */     } 
/*  794 */     return codec;
/*      */   }
/*      */ 
/*      */   
/*      */   private final void handleSeek(long newTime) {
/*  799 */     if (newTime < 0L)
/*      */       return; 
/*  801 */     if (this.mVideoTrackIndex >= 0) {
/*  802 */       this.mVideoMediaExtractor.seekTo(newTime, 2);
/*  803 */       this.mVideoMediaExtractor.advance();
/*      */     } 
/*  805 */     if (this.mAudioTrackIndex >= 0) {
/*  806 */       this.mAudioMediaExtractor.seekTo(newTime, 2);
/*  807 */       this.mAudioMediaExtractor.advance();
/*      */     } 
/*  809 */     this.mRequestTime = -1L;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final void handleLoop(IFrameCallback frameCallback) {
/*  815 */     synchronized (this.mSync) {
/*      */       try {
/*  817 */         this.mSync.wait();
/*  818 */       } catch (InterruptedException interruptedException) {}
/*      */     } 
/*      */     
/*  821 */     if (this.mVideoInputDone && this.mVideoOutputDone && this.mAudioInputDone && this.mAudioOutputDone)
/*      */     {
/*  823 */       handleStop();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean internal_process_input(MediaCodec codec, MediaExtractor extractor, ByteBuffer[] inputBuffers, long presentationTimeUs, boolean isAudio) {
/*  836 */     boolean result = true;
/*  837 */     while (this.mIsRunning) {
/*  838 */       int inputBufIndex = codec.dequeueInputBuffer(10000L);
/*  839 */       if (inputBufIndex == -1)
/*      */         break; 
/*  841 */       if (inputBufIndex >= 0) {
/*  842 */         int size = extractor.readSampleData(inputBuffers[inputBufIndex], 0);
/*  843 */         if (size > 0) {
/*  844 */           codec.queueInputBuffer(inputBufIndex, 0, size, presentationTimeUs, 0);
/*      */         }
/*  846 */         result = extractor.advance();
/*      */         break;
/*      */       } 
/*      */     } 
/*  850 */     return result;
/*      */   }
/*      */   
/*      */   private final void handleInputVideo() {
/*  854 */     long presentationTimeUs = this.mVideoMediaExtractor.getSampleTime();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  859 */     boolean b = internal_process_input(this.mVideoMediaCodec, this.mVideoMediaExtractor, this.mVideoInputBuffers, presentationTimeUs, false);
/*      */     
/*  861 */     if (!b) {
/*      */       
/*  863 */       while (this.mIsRunning) {
/*  864 */         int inputBufIndex = this.mVideoMediaCodec.dequeueInputBuffer(10000L);
/*  865 */         if (inputBufIndex >= 0) {
/*  866 */           this.mVideoMediaCodec.queueInputBuffer(inputBufIndex, 0, 0, 0L, 4);
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */       
/*  872 */       synchronized (this.mSync) {
/*  873 */         this.mVideoInputDone = true;
/*  874 */         this.mSync.notifyAll();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final void handleOutputVideo(IFrameCallback frameCallback) {
/*  883 */     while (this.mIsRunning && !this.mVideoOutputDone) {
/*  884 */       int decoderStatus = this.mVideoMediaCodec.dequeueOutputBuffer(this.mVideoBufferInfo, 10000L);
/*  885 */       if (decoderStatus == -1)
/*      */         return; 
/*  887 */       if (decoderStatus == -3) {
/*  888 */         this.mVideoOutputBuffers = this.mVideoMediaCodec.getOutputBuffers(); continue;
/*      */       } 
/*  890 */       if (decoderStatus == -2) {
/*  891 */         MediaFormat mediaFormat = this.mVideoMediaCodec.getOutputFormat(); continue;
/*      */       } 
/*  893 */       if (decoderStatus < 0) {
/*  894 */         throw new RuntimeException("unexpected result from video decoder.dequeueOutputBuffer: " + decoderStatus);
/*      */       }
/*      */       
/*  897 */       boolean doRender = false;
/*  898 */       if (this.mVideoBufferInfo.size > 0) {
/*      */         
/*  900 */         doRender = (this.mVideoBufferInfo.size != 0 && !internal_write_video(this.mVideoOutputBuffers[decoderStatus], 0, this.mVideoBufferInfo.size, this.mVideoBufferInfo.presentationTimeUs));
/*      */         
/*  902 */         if (doRender && 
/*  903 */           !frameCallback.onFrameAvailable(this.mVideoBufferInfo.presentationTimeUs)) {
/*  904 */           this.mVideoStartTime = adjustPresentationTime(this.mVideoSync, this.mVideoStartTime, this.mVideoBufferInfo.presentationTimeUs);
/*      */         }
/*      */       } 
/*  907 */       this.mVideoMediaCodec.releaseOutputBuffer(decoderStatus, doRender);
/*  908 */       if ((this.mVideoBufferInfo.flags & 0x4) != 0)
/*      */       {
/*  910 */         synchronized (this.mSync) {
/*  911 */           this.mVideoOutputDone = true;
/*  912 */           this.mSync.notifyAll();
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  927 */   protected boolean internal_write_video(ByteBuffer buffer, int offset, int size, long presentationTimeUs) { return false; }
/*      */ 
/*      */   
/*      */   private final void handleInputAudio() {
/*  931 */     long presentationTimeUs = this.mAudioMediaExtractor.getSampleTime();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  936 */     boolean b = internal_process_input(this.mAudioMediaCodec, this.mAudioMediaExtractor, this.mAudioInputBuffers, presentationTimeUs, true);
/*      */     
/*  938 */     if (!b) {
/*      */       
/*  940 */       while (this.mIsRunning) {
/*  941 */         int inputBufIndex = this.mAudioMediaCodec.dequeueInputBuffer(10000L);
/*  942 */         if (inputBufIndex >= 0) {
/*  943 */           this.mAudioMediaCodec.queueInputBuffer(inputBufIndex, 0, 0, 0L, 4);
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */       
/*  949 */       synchronized (this.mSync) {
/*  950 */         this.mAudioInputDone = true;
/*  951 */         this.mSync.notifyAll();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private final void handleOutputAudio(IFrameCallback frameCallback) {
/*  958 */     while (this.mIsRunning && !this.mAudioOutputDone) {
/*  959 */       int decoderStatus = this.mAudioMediaCodec.dequeueOutputBuffer(this.mAudioBufferInfo, 10000L);
/*  960 */       if (decoderStatus == -1)
/*      */         return; 
/*  962 */       if (decoderStatus == -3) {
/*  963 */         this.mAudioOutputBuffers = this.mAudioMediaCodec.getOutputBuffers(); continue;
/*      */       } 
/*  965 */       if (decoderStatus == -2) {
/*  966 */         MediaFormat mediaFormat = this.mAudioMediaCodec.getOutputFormat(); continue;
/*      */       } 
/*  968 */       if (decoderStatus < 0) {
/*  969 */         throw new RuntimeException("unexpected result from audio decoder.dequeueOutputBuffer: " + decoderStatus);
/*      */       }
/*      */       
/*  972 */       if (this.mAudioBufferInfo.size > 0) {
/*  973 */         internal_write_audio(this.mAudioOutputBuffers[decoderStatus], 0, this.mAudioBufferInfo.size, this.mAudioBufferInfo.presentationTimeUs);
/*      */         
/*  975 */         if (!frameCallback.onFrameAvailable(this.mAudioBufferInfo.presentationTimeUs))
/*  976 */           this.mAudioStartTime = adjustPresentationTime(this.mAudioSync, this.mAudioStartTime, this.mAudioBufferInfo.presentationTimeUs); 
/*      */       } 
/*  978 */       this.mAudioMediaCodec.releaseOutputBuffer(decoderStatus, false);
/*  979 */       if ((this.mAudioBufferInfo.flags & 0x4) != 0)
/*      */       {
/*  981 */         synchronized (this.mSync) {
/*  982 */           this.mAudioOutputDone = true;
/*  983 */           this.mSync.notifyAll();
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean internal_write_audio(ByteBuffer buffer, int offset, int size, long presentationTimeUs) {
/*  999 */     if (this.mAudioOutTempBuf.length < size) {
/* 1000 */       this.mAudioOutTempBuf = new byte[size];
/*      */     }
/* 1002 */     buffer.position(offset);
/* 1003 */     buffer.get(this.mAudioOutTempBuf, 0, size);
/* 1004 */     buffer.clear();
/* 1005 */     if (this.mAudioTrack != null)
/* 1006 */       this.mAudioTrack.write(this.mAudioOutTempBuf, 0, size); 
/* 1007 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected long adjustPresentationTime(Object sync, long startTime, long presentationTimeUs) {
/* 1018 */     if (startTime > 0L) {
/* 1019 */       long t = presentationTimeUs - System.nanoTime() / 1000L - startTime;
/* 1020 */       for (; t > 0L; t = presentationTimeUs - System.nanoTime() / 1000L - startTime) {
/* 1021 */         synchronized (sync) {
/*      */           try {
/* 1023 */             sync.wait(t / 1000L, (int)(t % 1000L * 1000L));
/* 1024 */           } catch (InterruptedException interruptedException) {}
/*      */           
/* 1026 */           if (this.mState == 4 || this.mState == 9)
/*      */             break; 
/*      */         } 
/*      */       } 
/* 1030 */       return startTime;
/*      */     } 
/* 1032 */     return System.nanoTime() / 1000L;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final void handleStop() {
/* 1038 */     synchronized (this.mVideoTask) {
/* 1039 */       internal_stop_video();
/* 1040 */       this.mVideoTrackIndex = -1;
/*      */     } 
/* 1042 */     synchronized (this.mAudioTask) {
/* 1043 */       internal_stop_audio();
/* 1044 */       this.mAudioTrackIndex = -1;
/*      */     } 
/* 1046 */     if (this.mVideoMediaCodec != null) {
/* 1047 */       this.mVideoMediaCodec.stop();
/* 1048 */       this.mVideoMediaCodec.release();
/* 1049 */       this.mVideoMediaCodec = null;
/*      */     } 
/* 1051 */     if (this.mAudioMediaCodec != null) {
/* 1052 */       this.mAudioMediaCodec.stop();
/* 1053 */       this.mAudioMediaCodec.release();
/* 1054 */       this.mAudioMediaCodec = null;
/*      */     } 
/* 1056 */     if (this.mVideoMediaExtractor != null) {
/* 1057 */       this.mVideoMediaExtractor.release();
/* 1058 */       this.mVideoMediaExtractor = null;
/*      */     } 
/* 1060 */     if (this.mAudioMediaExtractor != null) {
/* 1061 */       this.mAudioMediaExtractor.release();
/* 1062 */       this.mAudioMediaExtractor = null;
/*      */     } 
/* 1064 */     this.mVideoBufferInfo = this.mAudioBufferInfo = null;
/* 1065 */     this.mVideoInputBuffers = this.mVideoOutputBuffers = null;
/* 1066 */     this.mAudioInputBuffers = this.mAudioOutputBuffers = null;
/* 1067 */     if (this.mMetadata != null) {
/* 1068 */       this.mMetadata.release();
/* 1069 */       this.mMetadata = null;
/*      */     } 
/* 1071 */     synchronized (this.mSync) {
/* 1072 */       this.mVideoOutputDone = this.mVideoInputDone = this.mAudioOutputDone = this.mAudioInputDone = true;
/* 1073 */       this.mState = 0;
/*      */     } 
/* 1075 */     this.mCallback.onFinished();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void internal_stop_video() {}
/*      */ 
/*      */   
/*      */   protected void internal_stop_audio() {
/* 1084 */     if (this.mAudioTrack != null) {
/* 1085 */       if (this.mAudioTrack.getState() != 0)
/* 1086 */         this.mAudioTrack.stop(); 
/* 1087 */       this.mAudioTrack.release();
/* 1088 */       this.mAudioTrack = null;
/*      */     } 
/* 1090 */     this.mAudioOutTempBuf = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final void handlePause() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final void handleResume() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final int selectTrack(MediaExtractor extractor, String mimeType) {
/* 1110 */     int numTracks = extractor.getTrackCount();
/*      */ 
/*      */     
/* 1113 */     for (int i = 0; i < numTracks; i++) {
/* 1114 */       MediaFormat format = extractor.getTrackFormat(i);
/* 1115 */       String mime = format.getString("mime");
/* 1116 */       if (mime.startsWith(mimeType))
/*      */       {
/*      */ 
/*      */         
/* 1120 */         return i;
/*      */       }
/*      */     } 
/* 1123 */     return -1;
/*      */   }
/*      */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\MediaMoviePlayer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */