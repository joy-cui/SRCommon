/*     */ package com.serenegiant.media;
/*     */ 
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
/*     */ public class AudioSamplerEncoder
/*     */   extends AbstractAudioEncoder
/*     */ {
/*     */   private final boolean mSamplerCreated;
/*     */   private final IAudioSampler mSampler;
/*  35 */   private int frame_count = 0;
/*     */   private final IAudioSampler.SoundSamplerCallback mSoundSamplerCallback;
/*     */   
/*     */   public AudioSamplerEncoder(IRecorder recorder, EncoderListener listener, int audio_source, IAudioSampler sampler) {
/*  39 */     super(recorder, listener, audio_source, (sampler != null) ? sampler
/*  40 */         .getChannels() : 1, (sampler != null) ? sampler
/*  41 */         .getSamplingFrequency() : 44100, 64000);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  85 */     this.mSoundSamplerCallback = new IAudioSampler.SoundSamplerCallback()
/*     */       {
/*     */         
/*     */         public void onData(ByteBuffer buffer, int size, long presentationTimeUs)
/*     */         {
/*  90 */           synchronized (AudioSamplerEncoder.this.mSync) {
/*     */             
/*  92 */             if (!AudioSamplerEncoder.this.mIsCapturing || AudioSamplerEncoder.this.mRequestStop || AudioSamplerEncoder.this.mIsEOS)
/*     */               return; 
/*  94 */           }  if (size > 0) {
/*     */             
/*  96 */             AudioSamplerEncoder.this.frameAvailableSoon();
/*  97 */             AudioSamplerEncoder.this.encode(buffer, size, presentationTimeUs);
/*  98 */             AudioSamplerEncoder.this.frame_count++;
/*     */           } 
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void onError(Exception e) {}
/*     */       };
/* 113 */     this.mAudioTask = new Runnable()
/*     */       {
/*     */         public void run() {
/*     */           while (true) {
/* 117 */             synchronized (AudioSamplerEncoder.this.mSync) {
/* 118 */               if (!AudioSamplerEncoder.this.mIsCapturing || AudioSamplerEncoder.this.mRequestStop || AudioSamplerEncoder.this.mIsEOS)
/*     */                 break;  try {
/* 120 */                 AudioSamplerEncoder.this.mSync.wait();
/* 121 */               } catch (InterruptedException e) {
/*     */                 break;
/*     */               } 
/*     */             } 
/*     */           } 
/* 126 */           if (AudioSamplerEncoder.this.frame_count == 0) {
/*     */ 
/*     */             
/* 129 */             ByteBuffer buf = ByteBuffer.allocateDirect(1024).order(ByteOrder.nativeOrder());
/* 130 */             for (int i = 0; AudioSamplerEncoder.this.mIsCapturing && i < 5; i++) {
/* 131 */               buf.clear();
/* 132 */               buf.position(1024);
/* 133 */               buf.flip();
/* 134 */               AudioSamplerEncoder.this.encode(buf, 1024, AudioSamplerEncoder.this.getInputPTSUs());
/* 135 */               AudioSamplerEncoder.this.frameAvailableSoon();
/* 136 */               synchronized (this) {
/*     */                 try {
/* 138 */                   wait(50L);
/* 139 */                 } catch (InterruptedException interruptedException) {}
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         }
/*     */       };
/*     */     if (sampler == null) {
/*     */       if (audio_source < 0 || audio_source > 7)
/*     */         throw new IllegalArgumentException("invalid audio source:" + audio_source); 
/*     */       sampler = new AudioSampler(audio_source, 1, 44100, 1024, 25);
/*     */       this.mSamplerCreated = true;
/*     */     } else {
/*     */       this.mSamplerCreated = false;
/*     */     } 
/*     */     this.mSampler = sampler;
/*     */   }
/*     */   
/*     */   private final Runnable mAudioTask;
/*     */   
/*     */   public void start() {
/*     */     super.start();
/*     */     this.mSampler.addCallback(this.mSoundSamplerCallback);
/*     */     if (this.mSamplerCreated)
/*     */       this.mSampler.start(); 
/*     */     (new Thread(this.mAudioTask, "AudioTask")).start();
/*     */   }
/*     */   
/*     */   public void stop() {
/*     */     this.mSampler.removeCallback(this.mSoundSamplerCallback);
/*     */     if (this.mSamplerCreated)
/*     */       this.mSampler.stop(); 
/*     */     super.stop();
/*     */   }
/*     */   
/*     */   public void release() {
/*     */     if (this.mSamplerCreated)
/*     */       this.mSampler.release(); 
/*     */     super.release();
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\AudioSamplerEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */