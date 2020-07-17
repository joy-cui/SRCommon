/*     */ package com.serenegiant.media;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VideoConfig
/*     */ {
/*  32 */   private static float BPP = 0.25F;
/*     */   
/*     */   public static final int FPS_MIN = 2;
/*     */   
/*     */   public static final int FPS_MAX = 30;
/*     */   
/*  38 */   private static float IFRAME_INTERVAL = 10.0F;
/*     */   
/*     */   private static final int IFRAME_MIN = 1;
/*     */   
/*     */   private static final int IFRAME_MAX = 30;
/*     */   
/*  44 */   private static float IFI = IFRAME_INTERVAL * 30.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   private static int captureFps = 15;
/*     */ 
/*     */ 
/*     */   
/*  53 */   public static long maxDuration = 30000L;
/*     */ 
/*     */ 
/*     */   
/*  57 */   public static long repeatInterval = 60000L;
/*     */ 
/*     */ 
/*     */   
/*  61 */   public static int maxRepeats = 1;
/*     */ 
/*     */   
/*  64 */   public static void setCaptureFps(int fps) { captureFps = (fps > 30) ? 30 : ((fps < 2) ? 2 : fps); }
/*     */ 
/*     */ 
/*     */   
/*  68 */   public static int getCaptureFps() { return (captureFps > 30) ? 30 : ((captureFps < 2) ? 2 : captureFps); }
/*     */ 
/*     */   
/*     */   public static void setIFrame(float iframe_interval) {
/*  72 */     IFRAME_INTERVAL = iframe_interval;
/*  73 */     IFI = IFRAME_INTERVAL * 30.0F;
/*     */   }
/*     */   public static final int getIFrame() {
/*     */     float iframe;
/*  77 */     int fps = getCaptureFps();
/*     */     
/*     */     try {
/*  80 */       if (fps < 2)
/*  81 */       { iframe = 1.0F; }
/*     */       else
/*  83 */       { iframe = (float)Math.ceil((IFI / fps)); } 
/*  84 */     } catch (Exception e) {
/*  85 */       iframe = IFRAME_INTERVAL;
/*     */     } 
/*  87 */     if ((int)iframe < 1) { iframe = 1.0F; }
/*  88 */     else if ((int)iframe > 30) { iframe = 30.0F; }
/*     */     
/*  90 */     return (int)iframe;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   public static int getBitrate(int width, int height) { return getBitrate(width, height, getCaptureFps(), BPP); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   public static int getBitrate(int width, int height, int frameRate) { return getBitrate(width, height, frameRate, BPP); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getBitrate(int width, int height, int frameRate, float bpp) {
/* 123 */     int r = (int)(Math.floor((bpp * frameRate * width * height / 1000.0F / 100.0F)) * 100.0D) * 1000;
/* 124 */     if (r < 200000) { r = 200000; }
/* 125 */     else if (r > 14000000) { r = 14000000; }
/*     */     
/* 127 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setBPP(int width, int height, int bitrate) {
/* 138 */     int captureFps = getCaptureFps();
/* 139 */     BPP = bitrate / (captureFps * width * height);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setBPP(float bpp) throws IllegalArgumentException {
/* 148 */     if (bpp < 0.05F || bpp > 0.3F) {
/* 149 */       throw new IllegalArgumentException("bpp should be within [0.05,0.3]");
/*     */     }
/* 151 */     BPP = bpp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 159 */   public static float bpp() { return BPP; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getSizeRate(int width, int height) {
/* 169 */     int bitrate = getBitrate(width, height);
/* 170 */     return bitrate * 60 / 8;
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\VideoConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */