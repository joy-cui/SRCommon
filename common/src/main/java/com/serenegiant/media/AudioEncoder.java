/*     */ package com.serenegiant.media;
/*     */ 
/*     */ import android.annotation.TargetApi;
/*     */ import android.media.AudioRecord;
/*     */ import android.media.MediaCodec;
/*     */ import android.media.MediaCodecInfo;
/*     */ import android.media.MediaFormat;
/*     */ import android.os.Process;
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
/*     */ @TargetApi(16)
/*     */ public class AudioEncoder
/*     */   extends AbstractEncoder
/*     */ {
/*     */   public static final String AUDIO_MIME_TYPE = "audio/mp4a-latm";
/*  32 */   private AudioThread mAudioThread = null;
/*     */   
/*     */   protected final int mAudioSource;
/*     */   
/*     */   protected final int mChannelCount;
/*     */   protected final int mSampleRate;
/*     */   
/*     */   public AudioEncoder(IRecorder recorder, EncoderListener listener, int audio_source, int audio_channels) {
/*  40 */     super("audio/mp4a-latm", recorder, listener);
/*     */     
/*  42 */     this.mAudioSource = audio_source;
/*  43 */     this.mSampleRate = 44100;
/*  44 */     this.mChannelCount = audio_channels;
/*  45 */     if (audio_source < 0 || audio_source > 7)
/*     */     {
/*  47 */       throw new IllegalArgumentException("invalid audio source:" + audio_source);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void prepare() throws Exception {
/*  53 */     this.mTrackIndex = -1;
/*  54 */     this.mRecorderStarted = this.mIsEOS = false;
/*     */ 
/*     */     
/*  57 */     MediaCodecInfo audioCodecInfo = selectAudioCodec(this.MIME_TYPE);
/*  58 */     if (audioCodecInfo == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  64 */     MediaFormat audioFormat = MediaFormat.createAudioFormat(this.MIME_TYPE, this.mSampleRate, this.mChannelCount);
/*  65 */     audioFormat.setInteger("aac-profile", 2);
/*  66 */     audioFormat.setInteger("channel-mask", (this.mChannelCount == 1) ? 16 : 12);
/*     */     
/*  68 */     audioFormat.setInteger("bitrate", 64000);
/*  69 */     audioFormat.setInteger("channel-count", 1);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     this.mMediaCodec = MediaCodec.createEncoderByType(this.MIME_TYPE);
/*  75 */     this.mMediaCodec.configure(audioFormat, null, null, 1);
/*  76 */     this.mMediaCodec.start();
/*     */     
/*  78 */     callOnStartEncode(null, -1, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  83 */     super.start();
/*  84 */     if (this.mAudioThread == null) {
/*     */       
/*  86 */       this.mAudioThread = new AudioThread();
/*  87 */       this.mAudioThread.start();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void release() {
/*  94 */     this.mAudioThread = null;
/*  95 */     super.release();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final class AudioThread
/*     */     extends Thread
/*     */   {
/* 103 */     public AudioThread() { super("AudioThread"); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final void run() {
/* 109 */       Process.setThreadPriority(-16);
/* 110 */       int buffer_size = AudioSampler.getAudioBufferSize(AudioEncoder.this.mChannelCount, AudioEncoder.this.mSampleRate, 1024, 25);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 119 */       AudioRecord audioRecord = AudioSampler.createAudioRecord(AudioEncoder.this.mAudioSource, AudioEncoder.this.mSampleRate, AudioEncoder.this.mChannelCount, 2, buffer_size);
/*     */       
/* 121 */       int frame_count = 0, err_count = 0;
/* 122 */       ByteBuffer buf = ByteBuffer.allocateDirect(buffer_size).order(ByteOrder.nativeOrder());
/* 123 */       if (audioRecord != null) {
/*     */         try {
/* 125 */           if (AudioEncoder.this.mIsCapturing) {
/*     */ 
/*     */             
/* 128 */             audioRecord.startRecording(); try {
/*     */               do {
/*     */                 int readBytes;
/* 131 */                 synchronized (AudioEncoder.this.mSync) {
/* 132 */                   if (!AudioEncoder.this.mIsCapturing || AudioEncoder.this.mRequestStop || AudioEncoder.this.mIsEOS)
/*     */                     break; 
/* 134 */                 }  buf.clear();
/*     */                 try {
/* 136 */                   readBytes = audioRecord.read(buf, 1024 * AudioEncoder.this.mChannelCount);
/* 137 */                 } catch (Exception e) {
/*     */                   break;
/*     */                 } 
/*     */                 
/* 141 */                 if (readBytes == -2) {
/*     */                   
/* 143 */                   err_count++;
/* 144 */                 } else if (readBytes == -3) {
/*     */                   
/* 146 */                   err_count++;
/* 147 */                 } else if (readBytes > 0) {
/* 148 */                   err_count = 0;
/* 149 */                   frame_count++;
/*     */                   
/* 151 */                   buf.position(readBytes);
/* 152 */                   buf.flip();
/* 153 */                   AudioEncoder.this.encode(buf, readBytes, AudioEncoder.this.getInputPTSUs());
/* 154 */                   AudioEncoder.this.frameAvailableSoon();
/*     */                 } 
/* 156 */               } while (err_count <= 10);
/*     */               
/* 158 */               if (frame_count > 0)
/* 159 */                 AudioEncoder.this.frameAvailableSoon(); 
/*     */             } finally {
/* 161 */               audioRecord.stop();
/*     */             } 
/*     */           } 
/* 164 */         } catch (Exception readBytes) {
/*     */           Exception exception;
/*     */         } finally {
/* 167 */           audioRecord.release();
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 172 */       if (frame_count == 0)
/*     */       {
/*     */         
/* 175 */         for (int i = 0; AudioEncoder.this.mIsCapturing && i < 5; i++) {
/* 176 */           buf.position(1024);
/* 177 */           buf.flip();
/* 178 */           AudioEncoder.this.encode(buf, 1024, AudioEncoder.this.getInputPTSUs());
/* 179 */           AudioEncoder.this.frameAvailableSoon();
/* 180 */           synchronized (this) {
/*     */             try {
/* 182 */               wait(50L);
/* 183 */             } catch (InterruptedException interruptedException) {}
/*     */           } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final MediaCodecInfo selectAudioCodec(String mimeType) {
/* 200 */     MediaCodecInfo result = null;
/*     */     
/* 202 */     int numCodecs = getCodecCount(); int i;
/* 203 */     label18: for (i = 0; i < numCodecs; i++) {
/* 204 */       MediaCodecInfo codecInfo = getCodecInfoAt(i);
/* 205 */       if (codecInfo.isEncoder()) {
/*     */ 
/*     */         
/* 208 */         String[] types = codecInfo.getSupportedTypes();
/* 209 */         for (int j = 0; j < types.length; j++) {
/*     */           
/* 211 */           if (types[j].equalsIgnoreCase(mimeType)) {
/* 212 */             result = codecInfo; break label18;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 217 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 222 */   public boolean isAudio() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MediaFormat createOutputFormat(byte[] csd, int size, int ix0, int ix1) {
/* 232 */     MediaFormat outFormat = MediaFormat.createAudioFormat(this.MIME_TYPE, this.mSampleRate, this.mChannelCount);
/*     */     
/* 234 */     ByteBuffer csd0 = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
/* 235 */     csd0.put(csd, 0, size);
/* 236 */     csd0.flip();
/* 237 */     outFormat.setByteBuffer("csd-0", csd0);
/* 238 */     return outFormat;
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\AudioEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */