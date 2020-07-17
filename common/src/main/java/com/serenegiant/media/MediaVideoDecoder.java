/*     */ package com.serenegiant.media;
/*     */ 
/*     */ import android.annotation.TargetApi;
/*     */ import android.media.MediaCodec;
/*     */ import android.media.MediaExtractor;
/*     */ import android.media.MediaFormat;
/*     */ import android.media.MediaMetadataRetriever;
/*     */ import android.os.Build;
/*     */ import android.text.TextUtils;
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
/*     */ @TargetApi(16)
/*     */ public class MediaVideoDecoder
/*     */   extends MediaDecoder
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private Surface mSurface;
/*     */   private int mVideoWidth;
/*     */   private int mVideoHeight;
/*     */   private int mRotation;
/*     */   
/*  45 */   public int getVideoWidth() { return this.mVideoWidth; }
/*     */ 
/*     */ 
/*     */   
/*  49 */   public int getVideoHeight() { return this.mVideoHeight; }
/*     */ 
/*     */ 
/*     */   
/*  53 */   public int getRotation() { return this.mRotation; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int handlePrepare(MediaExtractor media_extractor) {
/*  58 */     int track_index = selectTrack(media_extractor, "video/");
/*  59 */     if (track_index >= 0) {
/*  60 */       MediaFormat format = media_extractor.getTrackFormat(track_index);
/*  61 */       this.mVideoWidth = format.getInteger("width");
/*  62 */       this.mVideoHeight = format.getInteger("height");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  67 */     return track_index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MediaCodec createCodec(MediaExtractor media_extractor, int track_index, MediaFormat format) throws IOException {
/*  74 */     if (Build.VERSION.SDK_INT > 18) {
/*  75 */       format.setInteger("push-blank-buffers-on-shutdown", 1);
/*     */     }
/*  77 */     return super.createCodec(media_extractor, track_index, format);
/*     */   }
/*     */ 
/*     */   
/*  81 */   public void setSurface(Surface surface) { this.mSurface = surface; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Surface getOutputSurface() {
/*  86 */     if (this.mSurface == null) {
/*  87 */       IllegalArgumentException e = new IllegalArgumentException("need to call setSurface before prepare");
/*  88 */       if (!callErrorHandler(e)) {
/*  89 */         throw e;
/*     */       }
/*     */     } 
/*  92 */     return this.mSurface;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  97 */   protected boolean handleOutput(ByteBuffer buffer, int offset, int size, long presentationTimeUs) { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateMovieInfo(MediaMetadataRetriever metadata) {
/* 102 */     super.updateMovieInfo(metadata);
/*     */     
/* 104 */     this.mVideoWidth = this.mVideoHeight = this.mRotation = 0;
/* 105 */     String value = metadata.extractMetadata(18);
/* 106 */     if (!TextUtils.isEmpty(value)) {
/* 107 */       this.mVideoWidth = Integer.parseInt(value);
/*     */     }
/* 109 */     value = metadata.extractMetadata(19);
/* 110 */     if (!TextUtils.isEmpty(value)) {
/* 111 */       this.mVideoHeight = Integer.parseInt(value);
/*     */     }
/* 113 */     value = metadata.extractMetadata(24);
/* 114 */     if (!TextUtils.isEmpty(value))
/* 115 */       this.mRotation = Integer.parseInt(value); 
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\MediaVideoDecoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */