/*     */ package com.serenegiant.media;
/*     */ 
/*     */ import android.annotation.SuppressLint;
/*     */ import android.annotation.TargetApi;
/*     */ import android.media.MediaCodecInfo;
/*     */ import android.support.annotation.NonNull;
/*     */ import android.util.Log;
/*     */ import com.serenegiant.utils.BufferHelper;
/*     */ import com.serenegiant.utils.BuildCheck;
/*     */ import com.serenegiant.utils.MediaInfo;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class MediaCodecHelper
/*     */ {
/*  38 */   private static final String TAG = MediaCodecHelper.class.getSimpleName();
/*     */ 
/*     */   
/*     */   public static final String MIME_AVC = "video/avc";
/*     */   
/*     */   @SuppressLint({"InlinedApi"})
/*  44 */   public static final int BUFFER_FLAG_KEY_FRAME = BuildCheck.isLollipop() ? 1 : 1;
/*     */   
/*  46 */   public static final byte[] START_MARKER = BufferHelper.ANNEXB_START_MARK;
/*     */   
/*     */   public static final int OMX_COLOR_FormatUnused = 0;
/*     */   
/*     */   public static final int OMX_COLOR_FormatMonochrome = 1;
/*     */   
/*     */   public static final int OMX_COLOR_Format8bitRGB332 = 2;
/*     */   
/*     */   public static final int OMX_COLOR_Format12bitRGB444 = 3;
/*     */   
/*     */   public static final int OMX_COLOR_Format16bitARGB4444 = 4;
/*     */   
/*     */   public static final int OMX_COLOR_Format16bitARGB1555 = 5;
/*     */   
/*     */   public static final int OMX_COLOR_Format16bitRGB565 = 6;
/*     */   
/*     */   public static final int OMX_COLOR_Format16bitBGR565 = 7;
/*     */   
/*     */   public static final int OMX_COLOR_Format18bitRGB666 = 8;
/*     */   
/*     */   public static final int OMX_COLOR_Format18bitARGB1665 = 9;
/*     */   
/*     */   public static final int OMX_COLOR_Format19bitARGB1666 = 10;
/*     */   
/*     */   public static final int OMX_COLOR_Format24bitRGB888 = 11;
/*     */   
/*     */   public static final int OMX_COLOR_Format24bitBGR888 = 12;
/*     */   
/*     */   public static final int OMX_COLOR_Format24bitARGB1887 = 13;
/*     */   
/*     */   public static final int OMX_COLOR_Format25bitARGB1888 = 14;
/*     */   
/*     */   public static final int OMX_COLOR_Format32bitBGRA8888 = 15;
/*     */   
/*     */   public static final int OMX_COLOR_Format32bitARGB8888 = 16;
/*     */   
/*     */   public static final int OMX_COLOR_FormatYUV411Planar = 17;
/*     */   
/*     */   public static final int OMX_COLOR_FormatYUV411PackedPlanar = 18;
/*     */   
/*     */   public static final int OMX_COLOR_FormatYUV420Planar = 19;
/*     */   
/*     */   public static final int OMX_COLOR_FormatYUV420PackedPlanar = 20;
/*     */   
/*     */   public static final int OMX_COLOR_FormatYUV420SemiPlanar = 21;
/*     */   
/*     */   public static final int OMX_COLOR_FormatYUV422Planar = 22;
/*     */   
/*     */   public static final int OMX_COLOR_FormatYUV422PackedPlanar = 23;
/*     */   
/*     */   public static final int OMX_COLOR_FormatYUV422SemiPlanar = 24;
/*     */   
/*     */   public static final int OMX_COLOR_FormatYCbYCr = 25;
/*     */   
/*     */   public static final int OMX_COLOR_FormatYCrYCb = 26;
/*     */   
/*     */   public static final int OMX_COLOR_FormatCbYCrY = 27;
/*     */   
/*     */   public static final int OMX_COLOR_FormatCrYCbY = 28;
/*     */   
/*     */   public static final int OMX_COLOR_FormatYUV444Interleaved = 29;
/*     */   
/*     */   public static final int OMX_COLOR_FormatRawBayer8bit = 30;
/*     */   
/*     */   public static final int OMX_COLOR_FormatRawBayer10bit = 31;
/*     */   
/*     */   public static final int OMX_COLOR_FormatRawBayer8bitcompressed = 32;
/*     */   
/*     */   public static final int OMX_COLOR_FormatL2 = 33;
/*     */   
/*     */   public static final int OMX_COLOR_FormatL4 = 34;
/*     */   
/*     */   public static final int OMX_COLOR_FormatL8 = 35;
/*     */   
/*     */   public static final int OMX_COLOR_FormatL16 = 36;
/*     */   
/*     */   public static final int OMX_COLOR_FormatL24 = 37;
/*     */   
/*     */   public static final int OMX_COLOR_FormatL32 = 38;
/*     */   
/*     */   public static final int OMX_COLOR_FormatYUV420PackedSemiPlanar = 39;
/*     */   
/*     */   public static final int OMX_COLOR_FormatYUV422PackedSemiPlanar = 40;
/*     */   
/*     */   public static final int OMX_COLOR_Format18BitBGR666 = 41;
/*     */   
/*     */   public static final int OMX_COLOR_Format24BitARGB6666 = 42;
/*     */   
/*     */   public static final int OMX_COLOR_Format24BitABGR6666 = 43;
/*     */   
/*     */   public static final int OMX_COLOR_FormatKhronosExtensions = 1862270976;
/*     */   
/*     */   public static final int OMX_COLOR_FormatVendorStartUnused = 2130706432;
/*     */   
/*     */   public static final int OMX_COLOR_FormatAndroidOpaque = 2130708361;
/*     */   
/*     */   public static final int OMX_TI_COLOR_FormatYUV420PackedSemiPlanar = 2130706688;
/*     */   
/*     */   public static final int OMX_QCOM_COLOR_FormatYVU420SemiPlanar = 2141391872;
/*     */   
/*     */   public static final int OMX_QCOM_COLOR_FormatYUV420PackedSemiPlanar64x32Tile2m8ka = 2141391875;
/*     */   public static final int OMX_SEC_COLOR_FormatNV12Tiled = 2143289346;
/*     */   public static final int OMX_QCOM_COLOR_FormatYUV420PackedSemiPlanar32m = 2141391876;
/*     */   public static final int OMX_COLOR_FormatMax = 2147483647;
/*     */   
/*     */   public static MediaCodecInfo selectVideoCodec(String mimeType) {
/* 152 */     int numCodecs = getCodecCount();
/* 153 */     for (int i = 0; i < numCodecs; i++) {
/* 154 */       MediaCodecInfo codecInfo = getCodecInfoAt(i);
/*     */       
/* 156 */       if (codecInfo.isEncoder()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 182 */         String[] types = codecInfo.getSupportedTypes();
/* 183 */         int n = types.length;
/*     */         
/* 185 */         for (int j = 0; j < n; j++) {
/* 186 */           if (types[j].equalsIgnoreCase(mimeType)) {
/*     */             
/* 188 */             int format = selectColorFormat(codecInfo, mimeType);
/* 189 */             if (format > 0)
/* 190 */               return codecInfo; 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 195 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 203 */   public static int[] recognizedFormats = new int[] { 19, 21, 2141391872 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean isRecognizedVideoFormat(int colorFormat) {
/* 216 */     int n = (recognizedFormats != null) ? recognizedFormats.length : 0;
/* 217 */     for (int i = 0; i < n; i++) {
/* 218 */       if (recognizedFormats[i] == colorFormat) {
/* 219 */         return true;
/*     */       }
/*     */     } 
/* 222 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int selectColorFormat(MediaCodecInfo codecInfo, String mimeType) {
/* 231 */     int result = 0;
/* 232 */     MediaCodecInfo.CodecCapabilities capabilities = getCodecCapabilities(codecInfo, mimeType);
/* 233 */     int[] colorFormats = capabilities.colorFormats;
/* 234 */     int n = colorFormats.length;
/*     */     
/* 236 */     for (int i = 0; i < n; i++) {
/* 237 */       int colorFormat = colorFormats[i];
/* 238 */       if (isRecognizedVideoFormat(colorFormat)) {
/* 239 */         result = colorFormat;
/*     */         break;
/*     */       } 
/*     */     } 
/* 243 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void dumpVideoCodecEncoders() {
/* 251 */     int numCodecs = getCodecCount();
/* 252 */     for (int i = 0; i < numCodecs; i++) {
/* 253 */       MediaCodecInfo codecInfo = getCodecInfoAt(i);
/*     */       
/* 255 */       if (codecInfo.isEncoder()) {
/*     */ 
/*     */ 
/*     */         
/* 259 */         String[] types = codecInfo.getSupportedTypes();
/* 260 */         for (int j = 0; j < types.length; j++) {
/* 261 */           Log.i(TAG, "codec:" + codecInfo.getName() + ",MIME:" + types[j]);
/*     */           
/* 263 */           selectColorFormat(codecInfo, types[j]);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean isSemiPlanarYUV(int colorFormat) {
/* 273 */     switch (colorFormat) {
/*     */       case 19:
/*     */       case 20:
/* 276 */         return false;
/*     */       case 21:
/*     */       case 39:
/*     */       case 2130706688:
/*     */       case 2141391872:
/* 281 */         return true;
/*     */     } 
/* 283 */     throw new RuntimeException("unknown format " + colorFormat);
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
/*     */   private static final MediaCodecInfo selectAudioCodec(String mimeType) {
/* 296 */     MediaCodecInfo result = null;
/*     */     
/* 298 */     int numCodecs = getCodecCount(); int i;
/* 299 */     label18: for (i = 0; i < numCodecs; i++) {
/* 300 */       MediaCodecInfo codecInfo = getCodecInfoAt(i);
/* 301 */       if (codecInfo.isEncoder()) {
/*     */ 
/*     */         
/* 304 */         String[] types = codecInfo.getSupportedTypes();
/* 305 */         for (int j = 0; j < types.length; j++) {
/*     */           
/* 307 */           if (types[j].equalsIgnoreCase(mimeType)) {
/* 308 */             result = codecInfo; break label18;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 313 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 318 */   public static final int getCodecCount() { return MediaInfo.getCodecCount(); }
/*     */ 
/*     */ 
/*     */   
/* 322 */   public static final List<MediaCodecInfo> getCodecs() { return MediaInfo.getCodecs(); }
/*     */ 
/*     */ 
/*     */   
/* 326 */   public static final MediaCodecInfo getCodecInfoAt(int ix) { return MediaInfo.getCodecInfoAt(ix); }
/*     */ 
/*     */ 
/*     */   
/* 330 */   public static MediaCodecInfo.CodecCapabilities getCodecCapabilities(MediaCodecInfo codecInfo, String mimeType) { return MediaInfo.getCodecCapabilities(codecInfo, mimeType); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean checkProfileLevel(String mimeType, MediaCodecInfo info) {
/* 340 */     if (info != null && 
/* 341 */       mimeType.equalsIgnoreCase("video/avc")) {
/* 342 */       MediaCodecInfo.CodecCapabilities caps = getCodecCapabilities(info, mimeType);
/* 343 */       MediaCodecInfo.CodecProfileLevel[] profileLevel = caps.profileLevels;
/* 344 */       for (int j = 0; j < profileLevel.length; j++) {
/* 345 */         if ((profileLevel[j]).level >= 16384) {
/* 346 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/* 350 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getProfileLevelString(String mimeType, MediaCodecInfo.CodecProfileLevel profileLevel) {
/* 360 */     String result = null;
/* 361 */     if (mimeType.equalsIgnoreCase("video/avc"))
/* 362 */     { switch (profileLevel.profile) {
/*     */         
/*     */         case 1:
/* 365 */           result = "profile:AVCProfileBaseline"; break;
/*     */         case 2:
/* 367 */           result = "profile:AVCProfileMain"; break;
/*     */         case 4:
/* 369 */           result = "profile:AVCProfileExtended"; break;
/*     */         case 8:
/* 371 */           result = "profile:AVCProfileHigh"; break;
/*     */         case 16:
/* 373 */           result = "profile:AVCProfileHigh10"; break;
/*     */         case 32:
/* 375 */           result = "profile:AVCProfileHigh422"; break;
/*     */         case 64:
/* 377 */           result = "profile:AVCProfileHigh444"; break;
/*     */         default:
/* 379 */           result = "profile:unknown " + profileLevel.profile; break;
/*     */       } 
/* 381 */       switch (profileLevel.level)
/*     */       
/*     */       { case 1:
/* 384 */           result = result + ",level=AVCLevel1";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 515 */           return result;case 2: result = result + ",level=AVCLevel1b"; return result;case 4: result = result + ",level=AVCLevel11"; return result;case 8: result = result + ",level=AVCLevel12"; return result;case 16: result = result + ",level=AVCLevel13"; return result;case 32: result = result + ",level=AVCLevel2"; return result;case 64: result = result + ",level=AVCLevel21"; return result;case 128: result = result + ",level=AVCLevel22"; return result;case 256: result = result + ",level=AVCLevel3"; return result;case 512: result = result + ",level=AVCLevel31"; return result;case 1024: result = result + ",level=AVCLevel32"; return result;case 2048: result = result + ",level=AVCLevel4"; return result;case 4096: result = result + ",level=AVCLevel41"; return result;case 8192: result = result + ",level=AVCLevel42"; return result;case 16384: result = result + ",level=AVCLevel5"; return result;case 32768: result = result + ",level=AVCLevel51"; return result; }  result = result + ",level=unknown " + profileLevel.level; } else if (mimeType.equalsIgnoreCase("video/h263")) { switch (profileLevel.profile) {  }  result = "profile:unknown " + profileLevel.profile; switch (profileLevel.level) {  }  result = result + ",level=unknown " + profileLevel.level; } else if (mimeType.equalsIgnoreCase("video/mpeg4")) { switch (profileLevel.profile) {  }  result = "profile:unknown " + profileLevel.profile; switch (profileLevel.level) {  }  result = result + ",level=unknown " + profileLevel.level; } else if (mimeType.equalsIgnoreCase("ausio/aac")) { switch (profileLevel.level) {  }  result = "profile:unknown " + profileLevel.profile; } else if (mimeType.equalsIgnoreCase("video/vp8")) { switch (profileLevel.profile) {  }  result = "profile:unknown " + profileLevel.profile; switch (profileLevel.level) {  }  result = result + ",level=unknown " + profileLevel.level; }  return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 525 */   public static final int findStartMarker(@NonNull byte[] array, int offset) { return BufferHelper.byteComp(array, offset, START_MARKER, START_MARKER.length); }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\MediaCodecHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */