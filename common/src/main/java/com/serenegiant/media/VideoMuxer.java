/*     */ package com.serenegiant.media;
/*     */ 
/*     */ import android.annotation.SuppressLint;
/*     */ import android.annotation.TargetApi;
/*     */ import android.media.MediaCodec;
/*     */ import android.media.MediaFormat;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class VideoMuxer
/*     */   implements IMuxer
/*     */ {
/*     */   private static boolean isLoaded = false;
/*     */   protected long mNativePtr;
/*     */   private volatile boolean mIsStarted;
/*     */   private int mLastTrackIndex;
/*     */   
/*     */   static  {
/*  44 */     if (!isLoaded) {
/*  45 */       System.loadLibrary("c++_shared");
/*  46 */       System.loadLibrary("jpeg-turbo1500");
/*  47 */       System.loadLibrary("png16");
/*  48 */       System.loadLibrary("common");
/*  49 */       System.loadLibrary("mediaencoder");
/*  50 */       isLoaded = true;
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VideoMuxer(String path)
/*     */   {
/*  83 */     this.mLastTrackIndex = -1; this.mNativePtr = nativeCreate(path); } public VideoMuxer(int fd) { this.mLastTrackIndex = -1;
/*     */     this.mNativePtr = nativeCreateFromFD(fd); }
/*     */   
/*     */   @SuppressLint({"InlinedApi"})
/*  87 */   public int addTrack(MediaFormat format) { if (format == null) {
/*  88 */       throw new IllegalArgumentException("format must not be null.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  93 */     HashMap<String, Object> map = new HashMap<>();
/*  94 */     if (format.containsKey("mime"))
/*  95 */       map.put("mime", format.getString("mime")); 
/*  96 */     if (format.containsKey("width"))
/*  97 */       map.put("width", Integer.valueOf(format.getInteger("width"))); 
/*  98 */     if (format.containsKey("height"))
/*  99 */       map.put("height", Integer.valueOf(format.getInteger("height"))); 
/* 100 */     if (format.containsKey("bitrate"))
/* 101 */       map.put("bitrate", Integer.valueOf(format.getInteger("bitrate"))); 
/* 102 */     if (format.containsKey("color-format"))
/* 103 */       map.put("color-format", Integer.valueOf(format.getInteger("color-format"))); 
/* 104 */     if (format.containsKey("frame-rate"))
/* 105 */       map.put("frame-rate", Integer.valueOf(format.getInteger("frame-rate"))); 
/* 106 */     if (format.containsKey("i-frame-interval"))
/* 107 */       map.put("i-frame-interval", Integer.valueOf(format.getInteger("i-frame-interval"))); 
/* 108 */     if (format.containsKey("max-input-size"))
/* 109 */       map.put("max-input-size", Integer.valueOf(format.getInteger("max-input-size"))); 
/* 110 */     if (format.containsKey("durationUs"))
/* 111 */       map.put("durationUs", Integer.valueOf(format.getInteger("durationUs"))); 
/* 112 */     if (format.containsKey("what"))
/* 113 */       map.put("what", Integer.valueOf(format.getInteger("what"))); 
/* 114 */     if (format.containsKey("csd-0"))
/* 115 */       map.put("csd-0", format.getByteBuffer("csd-0")); 
/* 116 */     if (format.containsKey("csd-1")) {
/* 117 */       map.put("csd-1", format.getByteBuffer("csd-1"));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 122 */     Object[] ks = map.keySet().toArray();
/* 123 */     int n = ks.length;
/* 124 */     String[] keys = new String[n];
/* 125 */     for (int i = 0; i < n; i++)
/* 126 */       keys[i] = (String)ks[i]; 
/* 127 */     Collection<Object> values = map.values();
/* 128 */     int trackIndex = nativeAddTrack(this.mNativePtr, keys, values.toArray());
/*     */ 
/*     */ 
/*     */     
/* 132 */     if (this.mLastTrackIndex >= trackIndex) {
/* 133 */       throw new IllegalArgumentException("Invalid format.");
/*     */     }
/* 135 */     this.mLastTrackIndex = trackIndex;
/* 136 */     return trackIndex; }
/*     */   public void release() { if (this.mNativePtr != 0L) {
/*     */       nativeDestroy(this.mNativePtr);
/*     */       this.mNativePtr = 0L;
/*     */     }  }
/*     */   protected void finalize() throws Throwable { release();
/* 142 */     super.finalize(); } public void start() { int res = -1;
/* 143 */     if (this.mNativePtr != 0L) {
/* 144 */       res = nativeStart(this.mNativePtr);
/*     */     }
/* 146 */     if (res != 0)
/* 147 */       throw new IllegalStateException("failed to start muxer"); 
/* 148 */     this.mIsStarted = true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 154 */     this.mIsStarted = false;
/* 155 */     if (this.mNativePtr != 0L) {
/* 156 */       int res = nativeStop(this.mNativePtr);
/* 157 */       if (res != 0) {
/* 158 */         throw new RuntimeException("failed to stop muxer");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeSampleData(int trackIndex, ByteBuffer buf, MediaCodec.BufferInfo bufferInfo) {
/* 165 */     int res = 1;
/* 166 */     if (this.mNativePtr != 0L) {
/* 167 */       res = nativeWriteSampleData(this.mNativePtr, trackIndex, buf, bufferInfo.offset, bufferInfo.size, bufferInfo.presentationTimeUs, bufferInfo.flags);
/*     */     }
/*     */     
/* 170 */     if (res != 0) {
/* 171 */       switch (res) {
/*     */         case -2000:
/* 173 */           throw new TimeoutException();
/*     */         case 1:
/* 175 */           throw new IllegalStateException("muxer already released.");
/*     */       } 
/* 177 */       throw new IllegalArgumentException("writeSampleData:err=" + res);
/*     */     } 
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
/*     */ 
/*     */   
/* 194 */   public boolean isStarted() { return this.mIsStarted; }
/*     */   
/*     */   private final native long nativeCreate(String paramString);
/*     */   
/*     */   private final native long nativeCreateFromFD(int paramInt);
/*     */   
/*     */   private final native void nativeDestroy(long paramLong);
/*     */   
/*     */   private static final native int nativeAddTrack(long paramLong, String[] paramArrayOfString, Object[] paramArrayOfObject);
/*     */   
/*     */   private static final native int nativeStart(long paramLong);
/*     */   
/*     */   private static final native int nativeStop(long paramLong);
/*     */   
/*     */   private static final native int nativeWriteSampleData(long paramLong1, int paramInt1, ByteBuffer paramByteBuffer, int paramInt2, int paramInt3, long paramLong2, int paramInt4);
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\VideoMuxer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */