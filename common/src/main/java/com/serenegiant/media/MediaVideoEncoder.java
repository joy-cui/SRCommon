/*     */ package com.serenegiant.media;
/*     */ 
/*     */ import android.annotation.TargetApi;
/*     */ import android.media.MediaCodec;
/*     */ import android.media.MediaCodecInfo;
/*     */ import android.media.MediaCodecList;
/*     */ import android.media.MediaFormat;
/*     */ import android.util.Log;
/*     */ import android.view.Surface;
/*     */ import java.io.IOException;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class MediaVideoEncoder
/*     */   extends MediaEncoder
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private static final String TAG = "MediaEncoder";
/*     */   private static final String MIME_TYPE = "video/avc";
/*     */   private static final int FRAME_RATE = 25;
/*     */   private static final float BPP = 0.25F;
/*     */   private Surface mSurface;
/*  45 */   private int mVideoWidth = 1280;
/*  46 */   private int mVideoHeight = 720;
/*     */ 
/*     */   
/*  49 */   public MediaVideoEncoder(MediaMovieRecorder muxer, IMediaCodecCallback listener) { this(1280, 720, muxer, listener); }
/*     */ 
/*     */   
/*     */   public MediaVideoEncoder(int width, int height, MediaMovieRecorder muxer, IMediaCodecCallback listener) {
/*  53 */     super(false, muxer, listener);
/*  54 */     this.mVideoWidth = width;
/*  55 */     this.mVideoHeight = height;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() throws IOException {
/*  61 */     this.mTrackIndex = -1;
/*  62 */     this.mMuxerStarted = this.mIsEOS = false;
/*     */     
/*  64 */     MediaCodecInfo videoCodecInfo = selectVideoCodec("video/avc");
/*  65 */     if (videoCodecInfo == null) {
/*  66 */       Log.e("MediaEncoder", "Unable to find an appropriate codec for video/avc");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  71 */     MediaFormat format = MediaFormat.createVideoFormat("video/avc", this.mVideoWidth, this.mVideoHeight);
/*  72 */     format.setInteger("color-format", 2130708361);
/*  73 */     format.setInteger("bitrate", calcBitRate());
/*  74 */     format.setInteger("frame-rate", 25);
/*  75 */     format.setInteger("i-frame-interval", 10);
/*     */ 
/*     */     
/*  78 */     this.mMediaCodec = MediaCodec.createEncoderByType("video/avc");
/*  79 */     this.mMediaCodec.configure(format, null, null, 1);
/*     */ 
/*     */     
/*  82 */     this.mSurface = this.mMediaCodec.createInputSurface();
/*  83 */     this.mMediaCodec.start();
/*  84 */     this.mIsPrepared = true;
/*     */     
/*  86 */     callOnPrepared();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void release() {
/*  92 */     if (this.mSurface != null) {
/*  93 */       this.mSurface.release();
/*  94 */       this.mSurface = null;
/*     */     } 
/*  96 */     super.release();
/*     */   }
/*     */   
/*     */   public void setVideoSize(int width, int height) throws IllegalArgumentException, IllegalStateException {
/* 100 */     if (width <= 0 || height <= 0)
/* 101 */       throw new IllegalArgumentException(String.format(Locale.US, "size(%d,%d)", new Object[] { Integer.valueOf(width), Integer.valueOf(height) })); 
/* 102 */     if (isRunning())
/* 103 */       throw new IllegalStateException("already start capturing"); 
/* 104 */     this.mVideoWidth = width;
/* 105 */     this.mVideoHeight = height;
/*     */   }
/*     */   
/*     */   public Surface getInputSurface() throws IllegalStateException {
/* 109 */     if (this.mSurface == null)
/* 110 */       throw new IllegalStateException("not prepared yet"); 
/* 111 */     return this.mSurface;
/*     */   }
/*     */ 
/*     */   
/* 115 */   public int getWidth() { return this.mVideoWidth; }
/*     */ 
/*     */ 
/*     */   
/* 119 */   public int getHeight() { return this.mVideoHeight; }
/*     */ 
/*     */   
/*     */   private int calcBitRate() {
/* 123 */     int bitrate = (int)(6.25F * this.mVideoWidth * this.mVideoHeight);
/* 124 */     Log.i("MediaEncoder", String.format("bitrate=%5.2f[Mbps]", new Object[] { Float.valueOf(bitrate / 1024.0F / 1024.0F) }));
/* 125 */     return bitrate;
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
/*     */   protected static final MediaCodecInfo selectVideoCodec(String mimeType) {
/* 137 */     int numCodecs = MediaCodecList.getCodecCount();
/* 138 */     for (int i = 0; i < numCodecs; i++) {
/* 139 */       MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
/*     */       
/* 141 */       if (codecInfo.isEncoder()) {
/*     */ 
/*     */ 
/*     */         
/* 145 */         String[] types = codecInfo.getSupportedTypes();
/* 146 */         for (int j = 0; j < types.length; j++) {
/* 147 */           if (types[j].equalsIgnoreCase(mimeType)) {
/*     */             
/* 149 */             int format = selectColorFormat(codecInfo, mimeType);
/* 150 */             if (format > 0)
/* 151 */               return codecInfo; 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 156 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final int selectColorFormat(MediaCodecInfo codecInfo, String mimeType) {
/*     */     MediaCodecInfo.CodecCapabilities caps;
/* 165 */     int result = 0;
/*     */     
/*     */     try {
/* 168 */       Thread.currentThread().setPriority(10);
/* 169 */       caps = codecInfo.getCapabilitiesForType(mimeType);
/*     */     } finally {
/* 171 */       Thread.currentThread().setPriority(5);
/*     */     } 
/*     */     
/* 174 */     for (int i = 0; i < caps.colorFormats.length; i++) {
/* 175 */       int colorFormat = caps.colorFormats[i];
/* 176 */       if (isRecognizedViewFormat(colorFormat)) {
/* 177 */         result = colorFormat;
/*     */         break;
/*     */       } 
/*     */     } 
/* 181 */     if (result == 0)
/* 182 */       Log.e("MediaEncoder", "couldn't find a good color format for " + codecInfo.getName() + " / " + mimeType); 
/* 183 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 191 */   protected static int[] recognizedFormats = new int[] { 2130708361 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final boolean isRecognizedViewFormat(int colorFormat) {
/* 201 */     int n = (recognizedFormats != null) ? recognizedFormats.length : 0;
/* 202 */     for (int i = 0; i < n; i++) {
/* 203 */       if (recognizedFormats[i] == colorFormat) {
/* 204 */         return true;
/*     */       }
/*     */     } 
/* 207 */     return false;
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\MediaVideoEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */