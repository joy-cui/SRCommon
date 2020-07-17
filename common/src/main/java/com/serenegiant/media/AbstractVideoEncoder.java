/*     */ package com.serenegiant.media;
/*     */ 
/*     */ import android.annotation.TargetApi;
/*     */ import android.media.MediaCodecInfo;
/*     */ import android.media.MediaFormat;
/*     */ import android.os.Bundle;
/*     */ import android.view.Surface;
/*     */ import com.serenegiant.utils.BuildCheck;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class AbstractVideoEncoder
/*     */   extends AbstractEncoder
/*     */ {
/*     */   public static final String MIME_AVC = "video/avc";
/*     */   public static final int OMX_COLOR_FormatUnused = 0;
/*     */   public static final int OMX_COLOR_FormatMonochrome = 1;
/*     */   public static final int OMX_COLOR_Format8bitRGB332 = 2;
/*     */   public static final int OMX_COLOR_Format12bitRGB444 = 3;
/*     */   public static final int OMX_COLOR_Format16bitARGB4444 = 4;
/*     */   public static final int OMX_COLOR_Format16bitARGB1555 = 5;
/*     */   public static final int OMX_COLOR_Format16bitRGB565 = 6;
/*     */   public static final int OMX_COLOR_Format16bitBGR565 = 7;
/*     */   public static final int OMX_COLOR_Format18bitRGB666 = 8;
/*     */   public static final int OMX_COLOR_Format18bitARGB1665 = 9;
/*     */   public static final int OMX_COLOR_Format19bitARGB1666 = 10;
/*     */   public static final int OMX_COLOR_Format24bitRGB888 = 11;
/*     */   public static final int OMX_COLOR_Format24bitBGR888 = 12;
/*     */   public static final int OMX_COLOR_Format24bitARGB1887 = 13;
/*     */   public static final int OMX_COLOR_Format25bitARGB1888 = 14;
/*     */   public static final int OMX_COLOR_Format32bitBGRA8888 = 15;
/*     */   public static final int OMX_COLOR_Format32bitARGB8888 = 16;
/*     */   public static final int OMX_COLOR_FormatYUV411Planar = 17;
/*     */   public static final int OMX_COLOR_FormatYUV411PackedPlanar = 18;
/*     */   public static final int OMX_COLOR_FormatYUV420Planar = 19;
/*     */   public static final int OMX_COLOR_FormatYUV420PackedPlanar = 20;
/*     */   public static final int OMX_COLOR_FormatYUV420SemiPlanar = 21;
/*     */   public static final int OMX_COLOR_FormatYUV422Planar = 22;
/*     */   public static final int OMX_COLOR_FormatYUV422PackedPlanar = 23;
/*     */   public static final int OMX_COLOR_FormatYUV422SemiPlanar = 24;
/*     */   public static final int OMX_COLOR_FormatYCbYCr = 25;
/*     */   public static final int OMX_COLOR_FormatYCrYCb = 26;
/*     */   public static final int OMX_COLOR_FormatCbYCrY = 27;
/*     */   public static final int OMX_COLOR_FormatCrYCbY = 28;
/*     */   public static final int OMX_COLOR_FormatYUV444Interleaved = 29;
/*     */   public static final int OMX_COLOR_FormatRawBayer8bit = 30;
/*     */   public static final int OMX_COLOR_FormatRawBayer10bit = 31;
/*     */   public static final int OMX_COLOR_FormatRawBayer8bitcompressed = 32;
/*     */   public static final int OMX_COLOR_FormatL2 = 33;
/*     */   public static final int OMX_COLOR_FormatL4 = 34;
/*     */   public static final int OMX_COLOR_FormatL8 = 35;
/*     */   public static final int OMX_COLOR_FormatL16 = 36;
/*     */   public static final int OMX_COLOR_FormatL24 = 37;
/*     */   public static final int OMX_COLOR_FormatL32 = 38;
/*     */   public static final int OMX_COLOR_FormatYUV420PackedSemiPlanar = 39;
/*     */   public static final int OMX_COLOR_FormatYUV422PackedSemiPlanar = 40;
/*     */   public static final int OMX_COLOR_Format18BitBGR666 = 41;
/*     */   public static final int OMX_COLOR_Format24BitARGB6666 = 42;
/*     */   public static final int OMX_COLOR_Format24BitABGR6666 = 43;
/*     */   public static final int OMX_COLOR_FormatKhronosExtensions = 1862270976;
/*     */   public static final int OMX_COLOR_FormatVendorStartUnused = 2130706432;
/*     */   public static final int OMX_COLOR_FormatAndroidOpaque = 2130708361;
/*     */   public static final int OMX_TI_COLOR_FormatYUV420PackedSemiPlanar = 2130706688;
/*     */   public static final int OMX_QCOM_COLOR_FormatYVU420SemiPlanar = 2141391872;
/*     */   public static final int OMX_QCOM_COLOR_FormatYUV420PackedSemiPlanar64x32Tile2m8ka = 2141391875;
/*     */   public static final int OMX_SEC_COLOR_FormatNV12Tiled = 2143289346;
/*     */   public static final int OMX_QCOM_COLOR_FormatYUV420PackedSemiPlanar32m = 2141391876;
/*     */   public static final int OMX_COLOR_FormatMax = 2147483647;
/*     */   protected int mColorFormat;
/*     */   protected int mWidth;
/*     */   protected int mHeight;
/* 130 */   protected int mBitRate = -1;
/* 131 */   protected int mFramerate = -1;
/* 132 */   protected int mIFrameIntervals = -1;
/*     */ 
/*     */   
/* 135 */   public AbstractVideoEncoder(String mime, IRecorder recorder, EncoderListener listener) { super(mime, recorder, listener); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVideoSize(int width, int height) {
/* 146 */     this.mWidth = width;
/* 147 */     this.mHeight = height;
/* 148 */     this.mBitRate = VideoConfig.getBitrate(width, height);
/*     */   }
/*     */   
/*     */   public void setVideoConfig(int bitRate, int frameRate, int iFrameIntervals) {
/* 152 */     this.mBitRate = bitRate;
/* 153 */     this.mFramerate = frameRate;
/* 154 */     this.mIFrameIntervals = iFrameIntervals;
/*     */   }
/*     */ 
/*     */   
/* 158 */   public int getWidth() { return this.mWidth; }
/*     */ 
/*     */ 
/*     */   
/* 162 */   public int getHeight() { return this.mHeight; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int getCaptureFormat();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Surface getInputSurface();
/*     */ 
/*     */ 
/*     */   
/*     */   public final MediaCodecInfo selectVideoCodec(String mimeType) {
/* 177 */     int numCodecs = getCodecCount();
/* 178 */     for (int i = 0; i < numCodecs; i++) {
/* 179 */       MediaCodecInfo codecInfo = getCodecInfoAt(i);
/*     */       
/* 181 */       if (codecInfo.isEncoder()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 207 */         String[] types = codecInfo.getSupportedTypes();
/* 208 */         int n = types.length;
/*     */         
/* 210 */         for (int j = 0; j < n; j++) {
/* 211 */           if (types[j].equalsIgnoreCase(mimeType)) {
/*     */             
/* 213 */             int format = selectColorFormat(codecInfo, mimeType);
/* 214 */             if (format > 0) {
/* 215 */               this.mColorFormat = format;
/* 216 */               return codecInfo;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 221 */     }  return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 229 */   public static int[] recognizedFormats = new int[] { 19, 21, 2141391872 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 238 */   public final boolean isAudio() { return false; }
/*     */ 
/*     */   
/* 241 */   public static boolean supportsAdaptiveStreaming = BuildCheck.isKitKat();
/*     */   
/*     */   @TargetApi(19)
/*     */   public void adjustBitrate(int targetBitrate) {
/* 245 */     if (supportsAdaptiveStreaming && this.mMediaCodec != null) {
/* 246 */       Bundle bitrate = new Bundle();
/* 247 */       bitrate.putInt("video-bitrate", targetBitrate);
/* 248 */       this.mMediaCodec.setParameters(bitrate);
/* 249 */     } else if (!supportsAdaptiveStreaming) {
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected MediaFormat createOutputFormat(byte[] csd, int size, int ix0, int ix1) {
/*     */     MediaFormat outFormat;
/* 257 */     if (ix0 >= 0) {
/* 258 */       outFormat = MediaFormat.createVideoFormat(this.MIME_TYPE, this.mWidth, this.mHeight);
/* 259 */       ByteBuffer csd0 = ByteBuffer.allocateDirect(ix1 - ix0).order(ByteOrder.nativeOrder());
/* 260 */       csd0.put(csd, ix0, ix1 - ix0);
/* 261 */       csd0.flip();
/* 262 */       outFormat.setByteBuffer("csd-0", csd0);
/* 263 */       if (ix1 > ix0) {
/*     */         
/* 265 */         ByteBuffer csd1 = ByteBuffer.allocateDirect(size - ix1 + ix0).order(ByteOrder.nativeOrder());
/* 266 */         csd1.put(csd, ix1, size - ix1 + ix0);
/* 267 */         csd1.flip();
/* 268 */         outFormat.setByteBuffer("csd-1", csd1);
/*     */       } 
/*     */     } else {
/* 271 */       throw new RuntimeException("unexpected csd data came.");
/*     */     } 
/* 273 */     return outFormat;
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
/*     */   private static final boolean isRecognizedVideoFormat(int colorFormat) {
/* 288 */     int n = (recognizedFormats != null) ? recognizedFormats.length : 0;
/* 289 */     for (int i = 0; i < n; i++) {
/* 290 */       if (recognizedFormats[i] == colorFormat) {
/* 291 */         return true;
/*     */       }
/*     */     } 
/* 294 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int selectColorFormat(MediaCodecInfo codecInfo, String mimeType) {
/* 303 */     int result = 0;
/* 304 */     MediaCodecInfo.CodecCapabilities capabilities = getCodecCapabilities(codecInfo, mimeType);
/* 305 */     int[] colorFormats = capabilities.colorFormats;
/* 306 */     int n = colorFormats.length;
/*     */ 
/*     */     
/* 309 */     for (int i = 0; i < n; i++) {
/* 310 */       int colorFormat = colorFormats[i];
/*     */ 
/*     */ 
/*     */       
/* 314 */       if (isRecognizedVideoFormat(colorFormat)) {
/* 315 */         result = colorFormat;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     
/* 321 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void dumpVideoCodecEncoders() {
/* 327 */     int numCodecs = getCodecCount();
/* 328 */     for (int i = 0; i < numCodecs; i++) {
/* 329 */       MediaCodecInfo codecInfo = getCodecInfoAt(i);
/*     */       
/* 331 */       if (codecInfo.isEncoder()) {
/*     */ 
/*     */ 
/*     */         
/* 335 */         String[] types = codecInfo.getSupportedTypes();
/* 336 */         for (int j = 0; j < types.length; j++)
/*     */         {
/*     */           
/* 339 */           selectColorFormat(codecInfo, types[j]);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final boolean isSemiPlanarYUV(int colorFormat) {
/* 349 */     switch (colorFormat) {
/*     */       case 19:
/*     */       case 20:
/* 352 */         return false;
/*     */       case 21:
/*     */       case 39:
/*     */       case 2130706688:
/*     */       case 2141391872:
/* 357 */         return true;
/*     */     } 
/* 359 */     throw new RuntimeException("unknown format " + colorFormat);
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\AbstractVideoEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */