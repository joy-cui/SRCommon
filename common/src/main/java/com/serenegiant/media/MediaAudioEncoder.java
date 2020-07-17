/*     */ package com.serenegiant.media;
/*     */ 
/*     */ import android.annotation.TargetApi;
/*     */ import android.media.AudioRecord;
/*     */ import android.media.MediaCodec;
/*     */ import android.media.MediaCodecInfo;
/*     */ import android.media.MediaCodecList;
/*     */ import android.media.MediaFormat;
/*     */ import android.os.Process;
/*     */ import android.util.Log;
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
/*     */ @TargetApi(16)
/*     */ public class MediaAudioEncoder
/*     */   extends MediaEncoder
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private static final String TAG = "MediaAudioEncoder";
/*     */   private static final String MIME_TYPE = "audio/mp4a-latm";
/*     */   private static final int SAMPLE_RATE = 44100;
/*     */   private static final int BIT_RATE = 64000;
/*     */   public static final int SAMPLES_PER_FRAME = 1024;
/*     */   public static final int FRAMES_PER_BUFFER = 25;
/*  49 */   private AudioThread mAudioThread = null;
/*     */ 
/*     */   
/*  52 */   public MediaAudioEncoder(MediaMovieRecorder muxer, IMediaCodecCallback listener) { super(true, muxer, listener); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() throws IOException {
/*  58 */     this.mTrackIndex = -1;
/*  59 */     this.mMuxerStarted = this.mIsEOS = false;
/*     */     
/*  61 */     MediaCodecInfo audioCodecInfo = selectAudioCodec("audio/mp4a-latm");
/*  62 */     if (audioCodecInfo == null) {
/*  63 */       Log.e("MediaAudioEncoder", "Unable to find an appropriate codec for audio/mp4a-latm");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  68 */     MediaFormat audioFormat = MediaFormat.createAudioFormat("audio/mp4a-latm", 44100, 1);
/*  69 */     audioFormat.setInteger("aac-profile", 2);
/*  70 */     audioFormat.setInteger("channel-mask", 16);
/*  71 */     audioFormat.setInteger("bitrate", 64000);
/*  72 */     audioFormat.setInteger("channel-count", 1);
/*     */ 
/*     */ 
/*     */     
/*  76 */     this.mMediaCodec = MediaCodec.createEncoderByType("audio/mp4a-latm");
/*  77 */     this.mMediaCodec.configure(audioFormat, null, null, 1);
/*  78 */     this.mMediaCodec.start();
/*  79 */     this.mIsPrepared = true;
/*     */     
/*  81 */     callOnPrepared();
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  86 */     super.start();
/*     */     
/*  88 */     if (this.mAudioThread == null) {
/*  89 */       this.mAudioThread = new AudioThread();
/*  90 */       this.mAudioThread.start();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void release() {
/*  96 */     this.mAudioThread = null;
/*  97 */     super.release();
/*     */   }
/*     */   
/* 100 */   private static final int[] AUDIO_SOURCES = new int[] { 5, 1, 0, 7, 6 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class AudioThread
/*     */     extends Thread
/*     */   {
/*     */     private AudioThread() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/* 116 */       Process.setThreadPriority(-19);
/*     */       try {
/* 118 */         int min_buffer_size = AudioRecord.getMinBufferSize(44100, 16, 2);
/*     */ 
/*     */         
/* 121 */         int buffer_size = 25600;
/* 122 */         if (buffer_size < min_buffer_size) {
/* 123 */           buffer_size = (min_buffer_size / 1024 + 1) * 1024 * 2;
/*     */         }
/* 125 */         AudioRecord audioRecord = null;
/* 126 */         for (int source : AUDIO_SOURCES) {
/*     */           try {
/* 128 */             audioRecord = new AudioRecord(source, 44100, 16, 2, buffer_size);
/*     */ 
/*     */             
/* 131 */             if (audioRecord != null && 
/* 132 */               audioRecord.getState() != 1) {
/* 133 */               audioRecord.release();
/* 134 */               audioRecord = null;
/*     */             }
/*     */           
/* 137 */           } catch (Exception e) {
/* 138 */             audioRecord = null;
/*     */           } 
/* 140 */           if (audioRecord != null)
/*     */             break; 
/*     */         } 
/* 143 */         if (audioRecord != null) {
/*     */           try {
/* 145 */             if (MediaAudioEncoder.this.mIsCapturing && audioRecord.getState() == 1) {
/* 146 */               ByteBuffer buf = ByteBuffer.allocateDirect(1024);
/*     */ 
/*     */               
/* 149 */               audioRecord.startRecording();
/*     */               try {
/* 151 */                 while (MediaAudioEncoder.this.mIsCapturing && !MediaAudioEncoder.this.mRequestStop && !MediaAudioEncoder.this.mIsEOS) {
/*     */                   
/* 153 */                   buf.clear();
/* 154 */                   int readBytes = audioRecord.read(buf, 1024);
/* 155 */                   if (readBytes > 0) {
/*     */                     
/* 157 */                     buf.position(readBytes);
/* 158 */                     buf.flip();
/* 159 */                     MediaAudioEncoder.this.encode(buf, readBytes, MediaAudioEncoder.this.getPTSUs());
/* 160 */                     MediaAudioEncoder.this.frameAvailableSoon();
/*     */                   } 
/*     */                 } 
/*     */ 
/*     */ 
/*     */                 
/* 166 */                 MediaAudioEncoder.this.frameAvailableSoon();
/*     */               } finally {
/*     */                 
/* 169 */                 audioRecord.stop();
/*     */               } 
/*     */             } 
/*     */           } finally {
/*     */             
/* 174 */             audioRecord.release();
/*     */           } 
/*     */         }
/* 177 */       } catch (Exception e) {
/* 178 */         Log.e("MediaAudioEncoder", "AudioThread#run", e);
/*     */       } 
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
/*     */   private static final MediaCodecInfo selectAudioCodec(String mimeType) {
/* 192 */     MediaCodecInfo result = null;
/*     */     
/* 194 */     int numCodecs = MediaCodecList.getCodecCount(); int i;
/* 195 */     label18: for (i = 0; i < numCodecs; i++) {
/* 196 */       MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
/* 197 */       if (codecInfo.isEncoder()) {
/*     */ 
/*     */         
/* 200 */         String[] types = codecInfo.getSupportedTypes();
/* 201 */         for (int j = 0; j < types.length; j++) {
/*     */           
/* 203 */           if (types[j].equalsIgnoreCase(mimeType)) {
/* 204 */             result = codecInfo; break label18;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 209 */     return result;
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\MediaAudioEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */