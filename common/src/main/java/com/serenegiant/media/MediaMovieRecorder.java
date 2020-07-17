/*     */ package com.serenegiant.media;
/*     */ 
/*     */ import android.annotation.TargetApi;
/*     */ import android.media.MediaCodec;
/*     */ import android.media.MediaFormat;
/*     */ import android.util.Log;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @TargetApi(18)
/*     */ public class MediaMovieRecorder
/*     */   extends AbstractRecorder
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private static final String TAG = "MediaMovieRecorder";
/*     */   private final IMuxer mMuxer;
/*     */   private IRecorderCallback mRecorderCallback;
/*     */   private final boolean hasAudioEncoder;
/*     */   private final IMediaCodecCallback mMediaCodecCallback;
/*     */   
/*     */   public MediaMovieRecorder(String output_path, boolean audio_recording) throws IOException {
/*  50 */     super(output_path);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 114 */     this.mMediaCodecCallback = new IMediaCodecCallback()
/*     */       {
/*     */         public void onPrepared(IMediaCodec codec) {
/* 117 */           boolean isPrepared = (MediaMovieRecorder.this.mVideoEncoder.isPrepared() && (!MediaMovieRecorder.this.hasAudioEncoder || MediaMovieRecorder.this.mAudioEncoder.isPrepared()));
/*     */           
/* 119 */           if (isPrepared && MediaMovieRecorder.this.mRecorderCallback != null) {
/*     */             try {
/* 121 */               MediaMovieRecorder.this.mRecorderCallback.onPrepared(MediaMovieRecorder.this);
/* 122 */             } catch (Exception e) {
/* 123 */               Log.w("MediaMovieRecorder", e);
/*     */             } 
/*     */           }
/*     */         }
/*     */ 
/*     */         
/*     */         public void onStart(IMediaCodec codec) {
/* 130 */           boolean isStarted = (MediaMovieRecorder.this.mVideoEncoder.isRunning() && (!MediaMovieRecorder.this.hasAudioEncoder || MediaMovieRecorder.this.mAudioEncoder.isRunning()));
/*     */           
/* 132 */           if (isStarted && MediaMovieRecorder.this.mRecorderCallback != null) {
/*     */             try {
/* 134 */               MediaMovieRecorder.this.mRecorderCallback.onStart(MediaMovieRecorder.this);
/* 135 */             } catch (Exception e) {
/* 136 */               Log.w("MediaMovieRecorder", e);
/*     */             } 
/*     */           }
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 143 */         public boolean onFrameAvailable(IMediaCodec codec, long presentationTimeUs) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void onStop(IMediaCodec codec) {
/* 149 */           if (MediaMovieRecorder.this.mRecorderCallback != null) {
/*     */             try {
/* 151 */               MediaMovieRecorder.this.mRecorderCallback.onStop(MediaMovieRecorder.this);
/* 152 */               MediaMovieRecorder.this.release();
/* 153 */             } catch (Exception e) {
/* 154 */               Log.w("MediaMovieRecorder", e);
/*     */             } 
/*     */           }
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void onRelease(IMediaCodec codec) {}
/*     */ 
/*     */ 
/*     */         
/* 166 */         public boolean onError(IMediaCodec codec, Exception e) { return false; }
/*     */       };
/*     */     this.mMuxer = new MediaMuxerWrapper(output_path, 0);
/*     */     new MediaVideoEncoder(this, this.mMediaCodecCallback);
/*     */     if (audio_recording)
/*     */       new MediaAudioEncoder(this, this.mMediaCodecCallback); 
/*     */     this.hasAudioEncoder = audio_recording;
/*     */   }
/*     */   
/*     */   public void setCallback(IRecorderCallback callback) { this.mRecorderCallback = callback; }
/*     */   
/*     */   public IRecorderCallback getCallback() { return this.mRecorderCallback; }
/*     */   
/*     */   public void setVideoSize(int width, int height) { ((MediaVideoEncoder)this.mVideoEncoder).setVideoSize(width, height); }
/*     */   
/*     */   public int getWidth() { return (this.mVideoEncoder != null) ? ((MediaVideoEncoder)this.mVideoEncoder).getWidth() : 0; }
/*     */   
/*     */   public int getHeight() { return (this.mVideoEncoder != null) ? ((MediaVideoEncoder)this.mVideoEncoder).getHeight() : 0; }
/*     */   
/*     */   public Surface getInputSurface() throws IllegalStateException { return ((MediaVideoEncoder)this.mVideoEncoder).getInputSurface(); }
/*     */   
/*     */   protected void internal_start() { this.mMuxer.start(); }
/*     */   
/*     */   protected void internal_stop() {
/*     */     this.mMuxer.stop();
/*     */     this.mMuxer.release();
/*     */   }
/*     */   
/*     */   int addTrack(MediaFormat format) {
/*     */     if (this.mIsStarted)
/*     */       throw new IllegalStateException("muxer already started"); 
/*     */     int trackIx = this.mMuxer.addTrack(format);
/*     */     return trackIx;
/*     */   }
/*     */   
/*     */   void writeSampleData(int trackIndex, ByteBuffer byteBuf, MediaCodec.BufferInfo bufferInfo) {
/*     */     if (this.mIsStarted)
/*     */       this.mMuxer.writeSampleData(trackIndex, byteBuf, bufferInfo); 
/*     */   }
/*     */   
/*     */   public static interface IRecorderCallback {
/*     */     void onPrepared(MediaMovieRecorder param1MediaMovieRecorder);
/*     */     
/*     */     void onStart(MediaMovieRecorder param1MediaMovieRecorder);
/*     */     
/*     */     void onStop(MediaMovieRecorder param1MediaMovieRecorder);
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\MediaMovieRecorder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */