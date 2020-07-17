/*     */ package com.serenegiant.media;
/*     */ 
/*     */ import android.media.AudioRecord;
/*     */ import android.os.Process;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AudioSampler
/*     */   extends IAudioSampler
/*     */ {
/*     */   private AudioThread mAudioThread;
/*     */   private final int AUDIO_SOURCE;
/*     */   private final int SAMPLING_RATE;
/*     */   private final int CHANNEL_COUNT;
/*     */   private final int SAMPLES_PER_FRAME;
/*     */   private final int BUFFER_SIZE;
/*     */   private static final int AUDIO_FORMAT = 2;
/*     */   
/*     */   public AudioSampler(int audio_source, int channel_num, int sampling_rate, int samples_per_frame, int frames_per_buffer) {
/*  47 */     this.AUDIO_SOURCE = audio_source;
/*  48 */     this.CHANNEL_COUNT = channel_num;
/*  49 */     this.SAMPLING_RATE = sampling_rate;
/*  50 */     this.SAMPLES_PER_FRAME = samples_per_frame * channel_num;
/*  51 */     this.BUFFER_SIZE = getAudioBufferSize(channel_num, sampling_rate, samples_per_frame, frames_per_buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   public int getBufferSize() { return this.SAMPLES_PER_FRAME; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void start() {
/*  70 */     super.start();
/*  71 */     if (this.mAudioThread == null) {
/*  72 */       init_pool(this.SAMPLES_PER_FRAME);
/*     */       
/*  74 */       this.mAudioThread = new AudioThread();
/*  75 */       this.mAudioThread.start();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void stop() {
/*  85 */     this.mIsCapturing = false;
/*  86 */     this.mAudioThread = null;
/*  87 */     super.stop();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  92 */   public int getAudioSource() { return this.AUDIO_SOURCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final class AudioRecordRec
/*     */   {
/*     */     AudioRecord audioRecord;
/*     */ 
/*     */ 
/*     */     
/*     */     int bufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getAudioBufferSize(int channel_num, int sampling_rate, int samples_per_frame, int frames_per_buffer) {
/* 110 */     int min_buffer_size = AudioRecord.getMinBufferSize(sampling_rate, (channel_num == 1) ? 16 : 12, 2);
/*     */ 
/*     */     
/* 113 */     int buffer_size = samples_per_frame * frames_per_buffer;
/* 114 */     if (buffer_size < min_buffer_size)
/* 115 */       buffer_size = (min_buffer_size / samples_per_frame + 1) * samples_per_frame * 2 * channel_num; 
/* 116 */     return buffer_size;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static AudioRecord createAudioRecord(int source, int sampling_rate, int channels, int format, int buffer_size) {
/* 122 */     int[] AUDIO_SOURCES = { 0, 5, 1, 0, 7, 6 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     switch (source) { case 1:
/* 132 */         AUDIO_SOURCES[0] = 1; break;
/* 133 */       case 2: AUDIO_SOURCES[0] = 5; break;
/* 134 */       default: AUDIO_SOURCES[0] = 1; break; }
/*     */     
/* 136 */     AudioRecord audioRecord = null;
/* 137 */     for (int src : AUDIO_SOURCES) {
/*     */       try {
/* 139 */         audioRecord = new AudioRecord(src, sampling_rate, (channels == 1) ? 16 : 12, format, buffer_size);
/*     */ 
/*     */         
/* 142 */         if (audioRecord != null && 
/* 143 */           audioRecord.getState() != 1) {
/* 144 */           audioRecord.release();
/* 145 */           audioRecord = null;
/*     */         }
/*     */       
/* 148 */       } catch (Exception e) {
/* 149 */         audioRecord = null;
/*     */       } 
/* 151 */       if (audioRecord != null)
/*     */         break; 
/*     */     } 
/* 154 */     return audioRecord;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class AudioThread
/*     */     extends Thread
/*     */   {
/* 163 */     public AudioThread() { super("AudioThread"); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final void run() {
/* 169 */       Process.setThreadPriority(-16);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 177 */       AudioRecord audioRecord = AudioSampler.createAudioRecord(AudioSampler.this
/* 178 */           .AUDIO_SOURCE, AudioSampler.this.SAMPLING_RATE, AudioSampler.this.CHANNEL_COUNT, 2, AudioSampler.this.BUFFER_SIZE);
/* 179 */       int err_count = 0;
/* 180 */       if (audioRecord != null) {
/*     */         try {
/* 182 */           if (AudioSampler.this.mIsCapturing) {
/*     */ 
/*     */ 
/*     */             
/* 186 */             audioRecord.startRecording();
/*     */ 
/*     */             
/* 189 */             try { while (AudioSampler.this.mIsCapturing) {
/* 190 */                 AudioData data = AudioSampler.this.obtain();
/* 191 */                 if (data != null) {
/* 192 */                   int readBytes; ByteBuffer buffer = data.mBuffer;
/* 193 */                   buffer.clear();
/*     */                   
/*     */                   try {
/* 196 */                     readBytes = audioRecord.read(buffer, AudioSampler.this.SAMPLES_PER_FRAME);
/* 197 */                   } catch (Exception e) {
/*     */                     
/* 199 */                     AudioSampler.this.callOnError(e);
/*     */                     break;
/*     */                   } 
/* 202 */                   if (readBytes == -2) {
/*     */                     
/* 204 */                     err_count++;
/* 205 */                     AudioSampler.this.recycle(data);
/* 206 */                   } else if (readBytes == -3) {
/*     */                     
/* 208 */                     err_count++;
/* 209 */                     AudioSampler.this.recycle(data);
/* 210 */                   } else if (readBytes > 0) {
/* 211 */                     err_count = 0;
/* 212 */                     data.presentationTimeUs = AudioSampler.this.getInputPTSUs();
/* 213 */                     data.size = readBytes;
/* 214 */                     buffer.position(readBytes);
/* 215 */                     buffer.flip();
/*     */                     
/* 217 */                     AudioSampler.this.addAudioData(data);
/*     */                   } 
/*     */                 } 
/* 220 */                 if (err_count > 10)
/*     */                   break; 
/*     */               }  }
/* 223 */             finally { audioRecord.stop(); }
/*     */           
/*     */           } 
/* 226 */         } catch (Exception e) {
/*     */           
/* 228 */           AudioSampler.this.callOnError(e);
/*     */         } finally {
/* 230 */           audioRecord.release();
/*     */         } 
/*     */       } else {
/*     */         
/* 234 */         AudioSampler.this.callOnError(new RuntimeException("AudioRecord failed to initialize"));
/*     */       } 
/* 236 */       AudioSampler.this.stop();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 243 */   public int getChannels() { return this.CHANNEL_COUNT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 248 */   public int getSamplingFrequency() { return this.SAMPLING_RATE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 253 */   public int getBitResolution() { return 16; }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\AudioSampler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */