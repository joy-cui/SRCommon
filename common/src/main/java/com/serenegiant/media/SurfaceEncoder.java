/*     */ package com.serenegiant.media;
/*     */ 
/*     */ import android.annotation.TargetApi;
/*     */ import android.media.MediaCodec;
/*     */ import android.media.MediaCodecInfo;
/*     */ import android.media.MediaFormat;
/*     */ import android.view.Surface;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class SurfaceEncoder
/*     */   extends AbstractVideoEncoder
/*     */ {
/*     */   protected Surface mInputSurface;
/*     */   
/*  36 */   public SurfaceEncoder(IRecorder recorder, EncoderListener listener) { super("video/avc", recorder, listener); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  44 */   public Surface getInputSurface() { return this.mInputSurface; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   public int getCaptureFormat() { return 0; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() throws Exception {
/*  54 */     this.mTrackIndex = -1;
/*  55 */     this.mRecorderStarted = false;
/*  56 */     this.mIsCapturing = true;
/*  57 */     this.mIsEOS = false;
/*     */     
/*  59 */     MediaCodecInfo codecInfo = selectVideoCodec("video/avc");
/*  60 */     if (codecInfo == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  66 */     boolean mayFail = (this.mWidth >= 1000 || this.mHeight >= 1000);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  72 */     MediaFormat format = MediaFormat.createVideoFormat("video/avc", this.mWidth, this.mHeight);
/*     */ 
/*     */ 
/*     */     
/*  76 */     format.setInteger("color-format", 2130708361);
/*  77 */     format.setInteger("bitrate", (this.mBitRate > 0) ? this.mBitRate : VideoConfig.getBitrate(this.mWidth, this.mHeight));
/*  78 */     format.setInteger("frame-rate", (this.mFramerate > 0) ? this.mFramerate : VideoConfig.getCaptureFps());
/*  79 */     format.setInteger("i-frame-interval", (this.mIFrameIntervals > 0) ? this.mIFrameIntervals : VideoConfig.getIFrame());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  84 */     this.mMediaCodec = MediaCodec.createEncoderByType("video/avc");
/*  85 */     this.mMediaCodec.configure(format, null, null, 1);
/*  86 */     this.mInputSurface = this.mMediaCodec.createInputSurface();
/*  87 */     this.mMediaCodec.start();
/*     */     
/*  89 */     callOnStartEncode(this.mInputSurface, getCaptureFormat(), mayFail);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void release() {
/*  98 */     super.release();
/*  99 */     this.mInputSurface = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void signalEndOfInputStream() {
/* 105 */     this.mIsEOS = true;
/* 106 */     if (this.mMediaCodec != null)
/* 107 */       this.mMediaCodec.signalEndOfInputStream(); 
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\SurfaceEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */