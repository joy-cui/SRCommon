/*     */ package com.serenegiant.media;
/*     */ 
/*     */ import android.media.MediaCodec;
/*     */ import android.media.MediaFormat;
/*     */ import android.view.Surface;
/*     */ import java.io.IOException;
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
/*     */ public abstract class AbstractRecorder
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private static final String TAG = "AbstractRecorder";
/*     */   protected final String mOutputPath;
/*     */   protected int mEncoderCount;
/*     */   protected int mStartedCount;
/*     */   protected volatile boolean mIsStarted;
/*     */   protected MediaEncoder mVideoEncoder;
/*     */   protected MediaEncoder mAudioEncoder;
/*     */   
/*     */   public AbstractRecorder(String output_path) {
/*  42 */     this.mOutputPath = output_path;
/*  43 */     this.mEncoderCount = this.mStartedCount = 0;
/*  44 */     this.mIsStarted = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void prepare() throws IOException {
/*  49 */     if (this.mVideoEncoder != null)
/*  50 */       this.mVideoEncoder.prepare(); 
/*  51 */     if (this.mAudioEncoder != null) {
/*  52 */       this.mAudioEncoder.prepare();
/*     */     }
/*     */   }
/*     */   
/*     */   public void startRecording() {
/*  57 */     if (this.mVideoEncoder != null)
/*  58 */       this.mVideoEncoder.start(); 
/*  59 */     if (this.mAudioEncoder != null) {
/*  60 */       this.mAudioEncoder.start();
/*     */     }
/*     */   }
/*     */   
/*     */   public void stopRecording() {
/*  65 */     if (this.mVideoEncoder != null) {
/*  66 */       this.mVideoEncoder.stop();
/*     */     }
/*  68 */     this.mVideoEncoder = null;
/*  69 */     if (this.mAudioEncoder != null) {
/*  70 */       this.mAudioEncoder.stop();
/*     */     }
/*  72 */     this.mAudioEncoder = null;
/*     */   }
/*     */   
/*     */   public void release() {
/*  76 */     if (this.mVideoEncoder != null) {
/*  77 */       this.mVideoEncoder.release();
/*  78 */       this.mVideoEncoder = null;
/*     */     } 
/*  80 */     if (this.mAudioEncoder != null) {
/*  81 */       this.mAudioEncoder.release();
/*  82 */       this.mAudioEncoder = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  87 */   public boolean isStarted() { return this.mIsStarted; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void addEncoder(MediaEncoder encoder) {
/*  95 */     if (encoder.isAudio()) {
/*  96 */       if (this.mAudioEncoder != null)
/*  97 */         throw new IllegalArgumentException("Video encoder already added."); 
/*  98 */       this.mAudioEncoder = encoder;
/*     */     } else {
/* 100 */       if (this.mVideoEncoder != null)
/* 101 */         throw new IllegalArgumentException("Video encoder already added."); 
/* 102 */       this.mVideoEncoder = encoder;
/*     */     } 
/* 104 */     this.mEncoderCount = ((this.mVideoEncoder != null) ? 1 : 0) + ((this.mAudioEncoder != null) ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void internal_start();
/*     */ 
/*     */ 
/*     */   
/*     */   synchronized boolean start() {
/* 114 */     this.mStartedCount++;
/* 115 */     if (this.mEncoderCount > 0 && this.mStartedCount == this.mEncoderCount) {
/* 116 */       internal_start();
/* 117 */       this.mIsStarted = true;
/* 118 */       notifyAll();
/*     */     } 
/*     */     
/* 121 */     return this.mIsStarted;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void internal_stop();
/*     */ 
/*     */   
/*     */   synchronized void stop() {
/* 130 */     this.mStartedCount--;
/* 131 */     if (this.mEncoderCount > 0 && this.mStartedCount <= 0) {
/* 132 */       this.mIsStarted = false;
/* 133 */       internal_stop();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract int addTrack(MediaFormat paramMediaFormat);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void writeSampleData(int paramInt, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int getWidth();
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int getHeight();
/*     */ 
/*     */ 
/*     */   
/*     */   public void frameAvailableSoon() {
/* 158 */     if (this.mVideoEncoder != null)
/* 159 */       this.mVideoEncoder.frameAvailableSoon(); 
/*     */   }
/*     */   
/*     */   public abstract Surface getInputSurface() throws IllegalStateException;
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\AbstractRecorder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */