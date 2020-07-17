/*     */ package com.serenegiant.media;
/*     */ 
/*     */ import android.annotation.TargetApi;
/*     */ import android.media.AudioTrack;
/*     */ import android.media.MediaCodec;
/*     */ import android.media.MediaExtractor;
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
/*     */ @TargetApi(16)
/*     */ public class MediaAudioDecoder
/*     */   extends MediaDecoder
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private int mAudioChannels;
/*     */   private int mAudioSampleRate;
/*     */   private int mAudioInputBufSize;
/*     */   private byte[] mAudioOutTempBuf;
/*     */   private AudioTrack mAudioTrack;
/*     */   
/*     */   @TargetApi(16)
/*     */   protected int handlePrepare(MediaExtractor media_extractor) {
/*  51 */     int track_index = selectTrack(media_extractor, "audio/");
/*  52 */     if (track_index >= 0) {
/*  53 */       MediaFormat format = media_extractor.getTrackFormat(track_index);
/*  54 */       this.mAudioChannels = format.getInteger("channel-count");
/*  55 */       this.mAudioSampleRate = format.getInteger("sample-rate");
/*  56 */       int min_buf_size = AudioTrack.getMinBufferSize(this.mAudioSampleRate, (this.mAudioChannels == 1) ? 4 : 12, 2);
/*     */ 
/*     */       
/*  59 */       int max_input_size = format.getInteger("max-input-size");
/*  60 */       this.mAudioInputBufSize = (min_buf_size > 0) ? (min_buf_size * this.mAudioChannels * 2) : max_input_size;
/*  61 */       if (this.mAudioInputBufSize > max_input_size) this.mAudioInputBufSize = max_input_size; 
/*  62 */       int frameSizeInBytes = this.mAudioChannels * 2;
/*  63 */       this.mAudioInputBufSize = this.mAudioInputBufSize / frameSizeInBytes * frameSizeInBytes;
/*     */     } 
/*     */     
/*  66 */     return track_index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MediaCodec createCodec(MediaExtractor media_extractor, int track_index, MediaFormat format) throws IOException, IllegalArgumentException {
/*  73 */     MediaCodec codec = super.createCodec(media_extractor, track_index, format);
/*  74 */     if (codec != null) {
/*  75 */       ByteBuffer[] buffers = codec.getOutputBuffers();
/*  76 */       int sz = buffers[0].capacity();
/*  77 */       if (sz <= 0) {
/*  78 */         sz = this.mAudioInputBufSize;
/*     */       }
/*  80 */       this.mAudioOutTempBuf = new byte[sz];
/*     */       try {
/*  82 */         this.mAudioTrack = new AudioTrack(3, this.mAudioSampleRate, (this.mAudioChannels == 1) ? 4 : 12, 2, this.mAudioInputBufSize, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  88 */         this.mAudioTrack.play();
/*  89 */       } catch (Exception e) {
/*  90 */         Log.e(this.TAG, "failed to start audio track playing", e);
/*  91 */         if (this.mAudioTrack != null) {
/*  92 */           this.mAudioTrack.release();
/*  93 */           this.mAudioTrack = null;
/*     */         } 
/*  95 */         throw e;
/*     */       } 
/*     */     } 
/*  98 */     return codec;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 103 */   protected Surface getOutputSurface() { return null; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean handleOutput(ByteBuffer buffer, int offset, int size, long presentationTimeUs) {
/* 108 */     if (this.mAudioOutTempBuf.length < size) {
/* 109 */       this.mAudioOutTempBuf = new byte[size];
/*     */     }
/* 111 */     buffer.position(offset);
/* 112 */     buffer.get(this.mAudioOutTempBuf, 0, size);
/* 113 */     buffer.clear();
/* 114 */     if (this.mAudioTrack != null)
/* 115 */       this.mAudioTrack.write(this.mAudioOutTempBuf, 0, size); 
/* 116 */     return true;
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\MediaAudioDecoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */