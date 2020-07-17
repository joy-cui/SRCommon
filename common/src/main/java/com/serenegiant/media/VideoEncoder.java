/*     */ package com.serenegiant.media;
/*     */ 
/*     */ import android.annotation.TargetApi;
/*     */ import android.media.MediaCodec;
/*     */ import android.media.MediaCodecInfo;
/*     */ import android.media.MediaFormat;
/*     */ import android.util.Log;
/*     */ import android.view.Surface;
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
/*     */ @TargetApi(16)
/*     */ public final class VideoEncoder
/*     */   extends AbstractVideoEncoder
/*     */ {
/*  34 */   private static final String TAG = VideoEncoder.class.getSimpleName();
/*     */   private static boolean isLoaded = false; protected long mNativePtr; private final boolean mAlign16; private int BUF_SIZE; private byte[] mEncodeBytes; public void prepare() throws Exception { this.mRecorderStarted = false; this.mIsCapturing = true; this.mIsEOS = false; MediaCodecInfo codecInfo = selectVideoCodec("video/avc"); if (codecInfo == null) { Log.e(TAG, "Unable to find an appropriate codec for video/avc"); return; }  boolean mayFail = (this.mWidth >= 1000 || this.mHeight >= 1000); MediaFormat format = MediaFormat.createVideoFormat("video/avc", this.mWidth, this.mHeight); format.setInteger("color-format", this.mColorFormat); format.setInteger("bitrate", (this.mBitRate > 0) ? this.mBitRate : VideoConfig.getBitrate(this.mWidth, this.mHeight)); format.setInteger("frame-rate", (this.mFramerate > 0) ? this.mFramerate : VideoConfig.getCaptureFps()); format.setInteger("i-frame-interval", (this.mIFrameIntervals > 0) ? this.mIFrameIntervals : VideoConfig.getIFrame()); Log.i(TAG, "format: " + format); this.mMediaCodec = MediaCodec.createEncoderByType("video/avc"); this.mMediaCodec.configure(format, null, null, 1); this.mMediaCodec.start(); if (this.mAlign16) { if (this.mWidth / 16 * 16 != this.mWidth)
/*     */         this.mWidth = (this.mWidth / 16 + 1) * 16;  if (this.mHeight / 16 * 16 != this.mHeight)
/*     */         this.mHeight = (this.mHeight / 16 + 1) * 16;  }
/*  38 */      nativePrepare(this.mNativePtr, this.mWidth, this.mHeight, this.mColorFormat); callOnStartEncode(null, -1, mayFail); } static  { if (!isLoaded) {
/*  39 */       System.loadLibrary("c++_shared");
/*  40 */       System.loadLibrary("jpeg-turbo1500");
/*  41 */       System.loadLibrary("png16");
/*  42 */       System.loadLibrary("common");
/*  43 */       System.loadLibrary("mediaencoder");
/*  44 */       isLoaded = true;
/*     */     }  }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VideoEncoder(Recorder recorder, EncoderListener listener, boolean align16) {
/*  56 */     super("video/avc", recorder, listener);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 185 */     this.BUF_SIZE = this.mWidth * (int)Math.ceil((this.mHeight / 16.0F)) * 16 * 2 * 3 / 4;
/* 186 */     this.mEncodeBytes = new byte[this.BUF_SIZE];
/*     */     this.mAlign16 = align16;
/*     */     this.mNativePtr = nativeCreate(); } public void stop() {
/*     */     if (this.mNativePtr != 0L)
/*     */       nativeStop(this.mNativePtr); 
/*     */     super.stop();
/*     */   }
/* 193 */   public void encode(ByteBuffer buffer) { synchronized (this.mSync) {
/* 194 */       if (!this.mIsCapturing || this.mRequestStop) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 202 */       buffer.rewind();
/* 203 */       encode(buffer, buffer.limit(), getInputPTSUs());
/* 204 */     } catch (Exception e) {
/* 205 */       callOnError(e);
/*     */     }  } public void release() { stop(); if (this.mNativePtr != 0L) {
/*     */       nativeDestroy(this.mNativePtr);
/*     */       this.mNativePtr = 0L;
/*     */     } 
/*     */     super.release(); }
/* 211 */   public int getCaptureFormat() { return -1; } public synchronized boolean startRecorder(IRecorder recorder, MediaFormat outFormat) { int h, w; try { w = outFormat.getInteger("width"); } catch (Exception e) { w = this.mWidth; }
/*     */      try { h = outFormat.getInteger("height"); }
/*     */     catch (Exception e) { h = this.mHeight; }
/*     */      nativeResize(this.mNativePtr, w, h, this.mColorFormat); int sz = w * h * 2 * 3 / 4; if (sz != this.BUF_SIZE) { this.BUF_SIZE = sz; this.mEncodeBytes = new byte[this.BUF_SIZE]; }
/*     */      return super.startRecorder(recorder, outFormat); }
/* 216 */   public Surface getInputSurface() { return null; }
/*     */   
/*     */   public void stopRecorder(IRecorder recorder) {
/*     */     if (this.mRecorderStarted)
/*     */       nativeStop(this.mNativePtr); 
/*     */     super.stopRecorder(recorder);
/*     */   }
/*     */   
/*     */   private final native long nativeCreate();
/*     */   
/*     */   private final native void nativeDestroy(long paramLong);
/*     */   
/*     */   private static final native int nativePrepare(long paramLong, int paramInt1, int paramInt2, int paramInt3);
/*     */   
/*     */   private static final native int nativeResize(long paramLong, int paramInt1, int paramInt2, int paramInt3);
/*     */   
/*     */   private static final native int nativeStop(long paramLong);
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\VideoEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */