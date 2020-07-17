/*     */ package com.serenegiant.utils;
/*     */ 
/*     */ import android.media.MediaCodecInfo;
/*     */ import android.media.MediaCodecList;
/*     */ import android.text.TextUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import org.json.JSONException;
/*     */ import org.json.JSONObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MediaInfo
/*     */ {
/*     */   public static JSONObject get() throws JSONException {
/*  36 */     JSONObject result = new JSONObject();
/*     */     try {
/*  38 */       result.put("VIDEO", getVideo());
/*  39 */     } catch (Exception e) {
/*  40 */       result.put("VIDEO", e.getMessage());
/*     */     } 
/*     */     try {
/*  43 */       result.put("AUDIO", getAudio());
/*  44 */     } catch (Exception e) {
/*  45 */       result.put("AUDIO", e.getMessage());
/*     */     } 
/*  47 */     return result;
/*     */   }
/*     */   
/*     */   private static final JSONObject getVideo() throws JSONException {
/*  51 */     JSONObject result = new JSONObject();
/*     */     
/*  53 */     int numCodecs = getCodecCount();
/*  54 */     for (int i = 0; i < numCodecs; i++) {
/*  55 */       MediaCodecInfo codecInfo = getCodecInfoAt(i);
/*  56 */       JSONObject codec = new JSONObject();
/*  57 */       String[] types = codecInfo.getSupportedTypes();
/*  58 */       int n = types.length;
/*  59 */       boolean isvideo = false;
/*  60 */       for (int j = 0; j < n; j++) {
/*  61 */         if (types[j].startsWith("video/")) {
/*  62 */           MediaCodecInfo.CodecCapabilities capabilities; isvideo = true;
/*  63 */           codec.put(Integer.toString(j), types[j]);
/*     */           
/*  65 */           Thread.currentThread().setPriority(10);
/*     */           try {
/*  67 */             capabilities = getCodecCapabilities(codecInfo, types[j]);
/*     */           } finally {
/*     */             
/*  70 */             Thread.currentThread().setPriority(5);
/*     */           } 
/*     */           try {
/*  73 */             int[] colorFormats = capabilities.colorFormats;
/*  74 */             int m = (colorFormats != null) ? colorFormats.length : 0;
/*  75 */             if (m > 0) {
/*  76 */               JSONObject caps = new JSONObject();
/*  77 */               for (int k = 0; k < m; k++) {
/*  78 */                 caps.put(String.format("COLOR_FORMAT(%d)", new Object[] { Integer.valueOf(k) }), getColorFormatName(colorFormats[k]));
/*     */               } 
/*  80 */               codec.put("COLOR_FORMATS", caps);
/*     */             } 
/*  82 */           } catch (Exception e) {
/*  83 */             codec.put("COLOR_FORMATS", e.getMessage());
/*     */           } 
/*     */           try {
/*  86 */             MediaCodecInfo.CodecProfileLevel[] profileLevel = capabilities.profileLevels;
/*  87 */             int m = (profileLevel != null) ? profileLevel.length : 0;
/*  88 */             if (m > 0) {
/*  89 */               JSONObject profiles = new JSONObject();
/*  90 */               for (int k = 0; k < m; k++) {
/*  91 */                 profiles.put(Integer.toString(k), getProfileLevelString(types[j], profileLevel[k]));
/*     */               }
/*  93 */               codec.put("PROFILES", profiles);
/*     */             } 
/*  95 */           } catch (Exception e) {
/*  96 */             codec.put("PROFILES", e.getMessage());
/*     */           } 
/*     */         } 
/*     */       } 
/* 100 */       if (isvideo)
/* 101 */         result.put(codecInfo.getName(), codec); 
/*     */     } 
/* 103 */     return result;
/*     */   }
/*     */   
/*     */   private static final JSONObject getAudio() throws JSONException {
/* 107 */     JSONObject result = new JSONObject();
/*     */     
/* 109 */     int numCodecs = getCodecCount();
/* 110 */     for (int i = 0; i < numCodecs; i++) {
/* 111 */       MediaCodecInfo codecInfo = getCodecInfoAt(i);
/* 112 */       JSONObject codec = new JSONObject();
/* 113 */       String[] types = codecInfo.getSupportedTypes();
/* 114 */       int n = types.length;
/* 115 */       boolean isaudio = false;
/* 116 */       for (int j = 0; j < n; j++) {
/* 117 */         if (types[j].startsWith("audio/")) {
/* 118 */           isaudio = true;
/* 119 */           codec.put(Integer.toString(j), types[j]);
/*     */         } 
/*     */       } 
/* 122 */       if (isaudio)
/* 123 */         result.put(codecInfo.getName(), codec); 
/*     */     } 
/* 125 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getColorFormatName(int colorFormat) {
/* 134 */     switch (colorFormat) {
/*     */       case 3:
/* 136 */         return "COLOR_Format12bitRGB444";
/*     */       case 5:
/* 138 */         return "COLOR_Format16bitARGB1555";
/*     */       case 4:
/* 140 */         return "COLOR_Format16bitARGB4444";
/*     */       case 7:
/* 142 */         return "COLOR_Format16bitBGR565";
/*     */       case 6:
/* 144 */         return "COLOR_Format16bitRGB565";
/*     */       case 41:
/* 146 */         return "COLOR_Format18BitBGR666";
/*     */       case 9:
/* 148 */         return "COLOR_Format18bitARGB1665";
/*     */       case 8:
/* 150 */         return "COLOR_Format18bitRGB666";
/*     */       case 10:
/* 152 */         return "COLOR_Format19bitARGB1666";
/*     */       case 43:
/* 154 */         return "COLOR_Format24BitABGR6666";
/*     */       case 42:
/* 156 */         return "COLOR_Format24BitARGB6666";
/*     */       case 13:
/* 158 */         return "COLOR_Format24bitARGB1887";
/*     */       case 12:
/* 160 */         return "COLOR_Format24bitBGR888";
/*     */       case 11:
/* 162 */         return "COLOR_Format24bitRGB888";
/*     */       case 14:
/* 164 */         return "COLOR_Format25bitARGB1888";
/*     */       case 16:
/* 166 */         return "COLOR_Format32bitARGB8888";
/*     */       case 15:
/* 168 */         return "COLOR_Format32bitBGRA8888";
/*     */       case 2:
/* 170 */         return "COLOR_Format8bitRGB332";
/*     */       case 27:
/* 172 */         return "COLOR_FormatCbYCrY";
/*     */       case 28:
/* 174 */         return "COLOR_FormatCrYCbY";
/*     */       case 36:
/* 176 */         return "COLOR_FormatL16";
/*     */       case 33:
/* 178 */         return "COLOR_FormatL2";
/*     */       case 37:
/* 180 */         return "COLOR_FormatL24";
/*     */       case 38:
/* 182 */         return "COLOR_FormatL32";
/*     */       case 34:
/* 184 */         return "COLOR_FormatL4";
/*     */       case 35:
/* 186 */         return "COLOR_FormatL8";
/*     */       case 1:
/* 188 */         return "COLOR_FormatMonochrome";
/*     */       case 31:
/* 190 */         return "COLOR_FormatRawBayer10bit";
/*     */       case 30:
/* 192 */         return "COLOR_FormatRawBayer8bit";
/*     */       case 32:
/* 194 */         return "COLOR_FormatRawBayer8bitcompressed";
/*     */       case 2130708361:
/* 196 */         return "COLOR_FormatSurface_COLOR_FormatAndroidOpaque";
/*     */       case 25:
/* 198 */         return "COLOR_FormatYCbYCr";
/*     */       case 26:
/* 200 */         return "COLOR_FormatYCrYCb";
/*     */       case 18:
/* 202 */         return "COLOR_FormatYUV411PackedPlanar";
/*     */       case 17:
/* 204 */         return "COLOR_FormatYUV411Planar";
/*     */       case 20:
/* 206 */         return "COLOR_FormatYUV420PackedPlanar";
/*     */       case 39:
/* 208 */         return "COLOR_FormatYUV420PackedSemiPlanar";
/*     */       case 19:
/* 210 */         return "COLOR_FormatYUV420Planar";
/*     */       case 21:
/* 212 */         return "COLOR_FormatYUV420SemiPlanar";
/*     */       case 23:
/* 214 */         return "COLOR_FormatYUV422PackedPlanar";
/*     */       case 40:
/* 216 */         return "COLOR_FormatYUV422PackedSemiPlanar";
/*     */       case 22:
/* 218 */         return "COLOR_FormatYUV422Planar";
/*     */       case 24:
/* 220 */         return "COLOR_FormatYUV422SemiPlanar";
/*     */       case 29:
/* 222 */         return "COLOR_FormatYUV444Interleaved";
/*     */       case 2141391872:
/* 224 */         return "COLOR_QCOM_FormatYUV420SemiPlanar";
/*     */       case 2130706688:
/* 226 */         return "COLOR_TI_FormatYUV420PackedSemiPlanar";
/*     */       case 1862270976:
/* 228 */         return "OMX_COLOR_FormatKhronosExtensions";
/*     */       case 2135033992:
/* 230 */         return "COLOR_FormatYUV420Flexible";
/*     */       case 2141391875:
/* 232 */         return "OMX_QCOM_COLOR_FormatYUV420PackedSemiPlanar64x32Tile2m8ka";
/*     */       case 2143289346:
/* 234 */         return "OMX_SEC_COLOR_FormatNV12Tiled";
/*     */       case 2141391876:
/* 236 */         return "OMX_QCOM_COLOR_FormatYUV420PackedSemiPlanar32m";
/*     */     } 
/* 238 */     return String.format(Locale.getDefault(), "COLOR_Format_Unknown(%d)", new Object[] { Integer.valueOf(colorFormat) });
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getProfileLevelString(String mimeType, MediaCodecInfo.CodecProfileLevel profileLevel) {
/* 243 */     String result = null;
/* 244 */     if (!TextUtils.isEmpty(mimeType))
/* 245 */     { if (mimeType.equalsIgnoreCase("video/avc"))
/* 246 */       { switch (profileLevel.profile) {
/*     */           
/*     */           case 1:
/* 249 */             result = "AVCProfileBaseline"; break;
/*     */           case 2:
/* 251 */             result = "AVCProfileMain"; break;
/*     */           case 4:
/* 253 */             result = "AVCProfileExtended"; break;
/*     */           case 8:
/* 255 */             result = "AVCProfileHigh"; break;
/*     */           case 16:
/* 257 */             result = "AVCProfileHigh10"; break;
/*     */           case 32:
/* 259 */             result = "AVCProfileHigh422"; break;
/*     */           case 64:
/* 261 */             result = "AVCProfileHigh444"; break;
/*     */           default:
/* 263 */             result = "unknown profile " + profileLevel.profile; break;
/*     */         } 
/* 265 */         switch (profileLevel.level)
/*     */         
/*     */         { case 1:
/* 268 */             result = result + ".AVCLevel1";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 460 */             return result;case 2: result = result + ".AVCLevel1b"; return result;case 4: result = result + ".AVCLevel11"; return result;case 8: result = result + ".AVCLevel12"; return result;case 16: result = result + ".AVCLevel13"; return result;case 32: result = result + ".AVCLevel2"; return result;case 64: result = result + ".AVCLevel21"; return result;case 128: result = result + ".AVCLevel22"; return result;case 256: result = result + ".AVCLevel3"; return result;case 512: result = result + ".AVCLevel31"; return result;case 1024: result = result + ".AVCLevel32"; return result;case 2048: result = result + ".AVCLevel4"; return result;case 4096: result = result + ".AVCLevel41"; return result;case 8192: result = result + ".AVCLevel42"; return result;case 16384: result = result + ".AVCLevel5"; return result;case 32768: result = result + ".AVCLevel51"; return result; }  result = result + ".unknown level " + profileLevel.level; } else if (mimeType.equalsIgnoreCase("video/h263")) { switch (profileLevel.profile) { case 1: result = "H263ProfileBaseline"; break;case 2: result = "H263ProfileH320Coding"; break;case 4: result = "H263ProfileBackwardCompatible"; break;case 8: result = "H263ProfileISWV2"; break;case 16: result = "H263ProfileISWV3"; break;case 32: result = "H263ProfileHighCompression"; break;case 64: result = "H263ProfileInternet"; break;case 128: result = "H263ProfileInterlace"; break;case 256: result = "H263ProfileHighLatency"; break;default: result = "unknown profile " + profileLevel.profile; break; }  switch (profileLevel.level) { case 1: result = result + ".H263Level10"; return result;case 2: result = result + ".H263Level20"; return result;case 4: result = result + ".H263Level30"; return result;case 8: result = result + ".H263Level40"; return result;case 16: result = result + ".H263Level45"; return result;case 32: result = result + ".H263Level50"; return result;case 64: result = result + ".H263Level60"; return result;case 128: result = result + ".H263Level70"; return result; }  result = result + ".unknown level " + profileLevel.level; } else if (mimeType.equalsIgnoreCase("video/mpeg4")) { switch (profileLevel.profile) { case 1: result = "MPEG4ProfileSimple"; break;case 2: result = "MPEG4ProfileSimpleScalable"; break;case 4: result = "MPEG4ProfileCore"; break;case 8: result = "MPEG4ProfileMain"; break;case 16: result = "MPEG4ProfileNbit"; break;case 32: result = "MPEG4ProfileScalableTexture"; break;case 64: result = "MPEG4ProfileSimpleFace"; break;case 128: result = "MPEG4ProfileSimpleFBA"; break;case 256: result = "MPEG4ProfileBasicAnimated"; break;case 512: result = "MPEG4ProfileHybrid"; break;case 1024: result = "MPEG4ProfileAdvancedRealTime"; break;case 2048: result = "MPEG4ProfileCoreScalable"; break;case 4096: result = "MPEG4ProfileAdvancedCoding"; break;case 8192: result = "MPEG4ProfileAdvancedCore"; break;case 16384: result = "MPEG4ProfileAdvancedScalable"; break;case 32768: result = "MPEG4ProfileAdvancedSimple"; break;default: result = "unknown profile " + profileLevel.profile; break; }  switch (profileLevel.level) { case 1: result = result + ".MPEG4Level0"; return result;case 2: result = result + ".MPEG4Level0b"; return result;case 4: result = result + ".MPEG4Level1"; return result;case 8: result = result + ".MPEG4Level2"; return result;case 16: result = result + ".MPEG4Level3"; return result;case 32: result = result + ".MPEG4Level4"; return result;case 64: result = result + ".MPEG4Level4a"; return result;case 128: result = result + ".MPEG4Level5"; return result; }  result = result + ".unknown level " + profileLevel.level; } else if (mimeType.equalsIgnoreCase("ausio/aac")) { switch (profileLevel.level) { case 1: result = "AACObjectMain"; return result;case 2: result = "AACObjectLC"; return result;case 3: result = "AACObjectSSR"; return result;case 4: result = "AACObjectLTP"; return result;case 5: result = "AACObjectHE"; return result;case 6: result = "AACObjectScalable"; return result;case 17: result = "AACObjectERLC"; return result;case 23: result = "AACObjectLD"; return result;case 29: result = "AACObjectHE_PS"; return result;case 39: result = "AACObjectELD"; return result; }  result = "profile:unknown " + profileLevel.profile; } else if (mimeType.equalsIgnoreCase("video/vp8")) { switch (profileLevel.profile) { case 1: result = "VP8ProfileMain"; break;default: result = "unknown profile " + profileLevel.profile; break; }  switch (profileLevel.level) { case 1: result = result + ".VP8Level_Version0"; return result;case 2: result = result + ".VP8Level_Version1"; return result;case 4: result = result + ".VP8Level_Version2"; return result;case 8: result = result + ".VP8Level_Version3"; return result; }  result = result + ".unknown level" + profileLevel.level; } else { result = "unknown profile " + profileLevel.profile; }  } else { result = "mime type is null"; }  return result;
/*     */   }
/*     */ 
/*     */   
/* 464 */   private static final List<MediaCodecInfo> sCodecList = new ArrayList<>();
/*     */ 
/*     */   
/*     */   private static final void updateCodecs() {
/* 468 */     if (sCodecList.size() == 0) {
/*     */       
/* 470 */       int n = MediaCodecList.getCodecCount();
/* 471 */       for (int i = 0; i < n; i++) {
/* 472 */         sCodecList.add(MediaCodecList.getCodecInfoAt(i));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static final int getCodecCount() {
/* 478 */     updateCodecs();
/* 479 */     return sCodecList.size();
/*     */   }
/*     */   
/*     */   public static final List<MediaCodecInfo> getCodecs() {
/* 483 */     updateCodecs();
/* 484 */     return sCodecList;
/*     */   }
/*     */   
/*     */   public static final MediaCodecInfo getCodecInfoAt(int ix) {
/* 488 */     updateCodecs();
/* 489 */     return sCodecList.get(ix);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 494 */   private static final HashMap<String, HashMap<MediaCodecInfo, MediaCodecInfo.CodecCapabilities>> sCapabilities = new HashMap<>();
/*     */   
/*     */   public static MediaCodecInfo.CodecCapabilities getCodecCapabilities(MediaCodecInfo codecInfo, String mimeType) {
/* 497 */     HashMap<MediaCodecInfo, MediaCodecInfo.CodecCapabilities> caps = sCapabilities.get(mimeType);
/* 498 */     if (caps == null) {
/* 499 */       caps = new HashMap<>();
/* 500 */       sCapabilities.put(mimeType, caps);
/*     */     } 
/* 502 */     MediaCodecInfo.CodecCapabilities capabilities = caps.get(codecInfo);
/* 503 */     if (capabilities == null) {
/*     */       
/* 505 */       Thread.currentThread().setPriority(10);
/*     */       try {
/* 507 */         capabilities = codecInfo.getCapabilitiesForType(mimeType);
/* 508 */         caps.put(codecInfo, capabilities);
/*     */       } finally {
/*     */         
/* 511 */         Thread.currentThread().setPriority(5);
/*     */       } 
/*     */     } 
/* 514 */     return capabilities;
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\MediaInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */