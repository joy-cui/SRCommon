/*     */ package com.serenegiant.media;
/*     */ 
/*     */ import android.media.AudioRecord;
/*     */ import android.os.Process;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AudioEncoderBuffered
/*     */   extends AbstractAudioEncoder
/*     */ {
/*  25 */   private final int MAX_POOL_SIZE = 100;
/*  26 */   private final int MAX_QUEUE_SIZE = 100;
/*     */   
/*  28 */   private AudioThread mAudioThread = null;
/*  29 */   private DequeueThread mDequeueThread = null;
/*     */ 
/*     */ 
/*     */   
/*  33 */   protected int mBufferSize = 1024;
/*  34 */   protected final LinkedBlockingQueue<AudioData> mPool = new LinkedBlockingQueue<>(100);
/*  35 */   protected final LinkedBlockingQueue<AudioData> mAudioQueue = new LinkedBlockingQueue<>(100);
/*     */   private int mBufferNum; public void start() { super.start(); if (this.mAudioThread == null) {
/*     */       this.mAudioThread = new AudioThread(); this.mAudioThread.start(); this.mDequeueThread = new DequeueThread();
/*     */       this.mDequeueThread.start();
/*  39 */     }  } public AudioEncoderBuffered(IRecorder recorder, EncoderListener listener, int audio_source, int audio_channels) { super(recorder, listener, audio_source, audio_channels);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  65 */     this.mBufferNum = 0;
/*     */     if (audio_source < 0 || audio_source > 7)
/*  67 */       throw new IllegalArgumentException("invalid audio source:" + audio_source);  } private AudioData obtain() { AudioData result = null;
/*     */     try {
/*  69 */       result = this.mPool.poll(20L, TimeUnit.MILLISECONDS);
/*  70 */     } catch (InterruptedException interruptedException) {}
/*     */     
/*  72 */     if (result == null && this.mBufferNum < 100) {
/*  73 */       result = new AudioData(this.mBufferSize);
/*  74 */       this.mBufferNum++;
/*     */     } 
/*  76 */     if (result != null)
/*  77 */       result.size = 0; 
/*  78 */     return result; } public void stop() {
/*     */     this.mAudioThread = null;
/*     */     this.mDequeueThread = null;
/*     */     super.stop();
/*  82 */   } protected void recycle(AudioData data) { this.mPool.offer(data); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class AudioThread
/*     */     extends Thread
/*     */   {
/*  90 */     public AudioThread() { super("AudioThread"); }
/*     */ 
/*     */ 
/*     */     
/*     */     public final void run() {
/*  95 */       Process.setThreadPriority(-16);
/*  96 */       int buffer_size = AudioSampler.getAudioBufferSize(AudioEncoderBuffered.this.mChannelCount, AudioEncoderBuffered.this.mSampleRate, 1024, 25);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 105 */       AudioRecord audioRecord = AudioSampler.createAudioRecord(AudioEncoderBuffered.this.mAudioSource, AudioEncoderBuffered.this.mSampleRate, AudioEncoderBuffered.this.mChannelCount, 2, buffer_size);
/*     */       
/* 107 */       int err_count = 0;
/* 108 */       if (audioRecord != null) {
/*     */         try {
/* 110 */           if (AudioEncoderBuffered.this.mIsCapturing) {
/*     */ 
/*     */ 
/*     */             
/* 114 */             audioRecord.startRecording();
/*     */             try {
/*     */               do {
/*     */                 int readBytes;
/* 118 */                 synchronized (AudioEncoderBuffered.this.mSync) {
/* 119 */                   if (!AudioEncoderBuffered.this.mIsCapturing || AudioEncoderBuffered.this.mRequestStop || AudioEncoderBuffered.this.mIsEOS)
/*     */                     break; 
/* 121 */                 }  AudioData data = AudioEncoderBuffered.this.obtain();
/* 122 */                 ByteBuffer buffer = data.mBuffer;
/* 123 */                 buffer.clear();
/*     */                 try {
/* 125 */                   readBytes = audioRecord.read(buffer, 1024);
/* 126 */                 } catch (Exception e) {
/*     */                   break;
/*     */                 } 
/*     */                 
/* 130 */                 if (readBytes == -2) {
/*     */                   
/* 132 */                   err_count++;
/* 133 */                   AudioEncoderBuffered.this.recycle(data);
/* 134 */                 } else if (readBytes == -3) {
/*     */                   
/* 136 */                   err_count++;
/* 137 */                   AudioEncoderBuffered.this.recycle(data);
/* 138 */                 } else if (readBytes > 0) {
/*     */                   
/* 140 */                   err_count = 0;
/* 141 */                   data.presentationTimeUs = AudioEncoderBuffered.this.getInputPTSUs();
/* 142 */                   data.size = readBytes;
/* 143 */                   buffer.position(readBytes);
/* 144 */                   buffer.flip();
/* 145 */                   AudioEncoderBuffered.this.mAudioQueue.offer(data);
/*     */                 } 
/* 147 */               } while (err_count <= 10);
/*     */             } finally {
/*     */               
/* 150 */               audioRecord.stop();
/*     */             } 
/*     */           } 
/* 153 */         } catch (Exception readBytes) {
/*     */           Exception exception;
/*     */         } finally {
/* 156 */           audioRecord.release();
/*     */         } 
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class DequeueThread
/*     */     extends Thread
/*     */   {
/* 170 */     public DequeueThread() { super("DequeueThread"); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final void run() {
/* 176 */       int frame_count = 0; while (true) {
/*     */         AudioData data;
/* 178 */         synchronized (AudioEncoderBuffered.this.mSync) {
/* 179 */           if (!AudioEncoderBuffered.this.mIsCapturing || AudioEncoderBuffered.this.mRequestStop || AudioEncoderBuffered.this.mIsEOS)
/*     */             break; 
/*     */         }  try {
/* 182 */           data = AudioEncoderBuffered.this.mAudioQueue.poll(30L, TimeUnit.MILLISECONDS);
/* 183 */         } catch (InterruptedException e1) {
/*     */           break;
/*     */         } 
/* 186 */         if (data != null) {
/* 187 */           if (data.size > 0) {
/* 188 */             AudioEncoderBuffered.this.encode(data.mBuffer, data.size, data.presentationTimeUs);
/* 189 */             AudioEncoderBuffered.this.frameAvailableSoon();
/* 190 */             frame_count++;
/*     */           } 
/* 192 */           AudioEncoderBuffered.this.recycle(data);
/*     */         } 
/*     */       } 
/* 195 */       if (frame_count == 0) {
/*     */ 
/*     */         
/* 198 */         ByteBuffer buf = ByteBuffer.allocateDirect(AudioEncoderBuffered.this.mBufferSize).order(ByteOrder.nativeOrder());
/* 199 */         for (int i = 0; AudioEncoderBuffered.this.mIsCapturing && i < 5; i++) {
/* 200 */           buf.position(1024);
/* 201 */           buf.flip();
/* 202 */           AudioEncoderBuffered.this.encode(buf, 1024, AudioEncoderBuffered.this.getInputPTSUs());
/* 203 */           AudioEncoderBuffered.this.frameAvailableSoon();
/* 204 */           synchronized (this) {
/*     */             try {
/* 206 */               wait(50L);
/* 207 */             } catch (InterruptedException interruptedException) {}
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\AudioEncoderBuffered.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */