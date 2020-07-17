/*     */ package com.serenegiant.media;
/*     */ 
/*     */ import android.annotation.TargetApi;
/*     */ import android.media.MediaCodec;
/*     */ import android.media.MediaCodecInfo;
/*     */ import android.media.MediaFormat;
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
/*     */ @TargetApi(16)
/*     */ public abstract class AbstractAudioEncoder
/*     */   extends AbstractEncoder
/*     */ {
/*     */   public static final String AUDIO_MIME_TYPE = "audio/mp4a-latm";
/*     */   public static final int DEFAULT_SAMPLE_RATE = 44100;
/*     */   public static final int DEFAULT_BIT_RATE = 64000;
/*     */   public static final int SAMPLES_PER_FRAME = 1024;
/*     */   public static final int FRAMES_PER_BUFFER = 25;
/*     */   protected int mAudioSource;
/*     */   protected int mChannelCount;
/*     */   protected int mSampleRate;
/*     */   protected int mBitRate;
/*     */   
/*  41 */   public AbstractAudioEncoder(IRecorder recorder, EncoderListener listener, int audio_source, int audio_channels) { this(recorder, listener, audio_source, audio_channels, 44100, 64000); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractAudioEncoder(IRecorder recorder, EncoderListener listener, int audio_source, int audio_channels, int sample_rate, int bit_rate) {
/*  47 */     super("audio/mp4a-latm", recorder, listener);
/*     */     
/*  49 */     this.mAudioSource = audio_source;
/*  50 */     this.mChannelCount = audio_channels;
/*  51 */     this.mSampleRate = sample_rate;
/*  52 */     this.mBitRate = bit_rate;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() throws Exception {
/*  58 */     this.mTrackIndex = -1;
/*  59 */     this.mRecorderStarted = this.mIsEOS = false;
/*     */ 
/*     */     
/*  62 */     MediaCodecInfo audioCodecInfo = selectAudioCodec(this.MIME_TYPE);
/*  63 */     if (audioCodecInfo == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  69 */     MediaFormat audioFormat = MediaFormat.createAudioFormat(this.MIME_TYPE, this.mSampleRate, this.mChannelCount);
/*  70 */     audioFormat.setInteger("aac-profile", 2);
/*  71 */     audioFormat.setInteger("channel-mask", (this.mChannelCount == 1) ? 16 : 12);
/*     */     
/*  73 */     audioFormat.setInteger("bitrate", this.mBitRate);
/*  74 */     audioFormat.setInteger("channel-count", this.mChannelCount);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     this.mMediaCodec = MediaCodec.createEncoderByType(this.MIME_TYPE);
/*  80 */     this.mMediaCodec.configure(audioFormat, null, null, 1);
/*  81 */     this.mMediaCodec.start();
/*     */     
/*  83 */     callOnStartEncode(null, -1, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final MediaCodecInfo selectAudioCodec(String mimeType) {
/*  94 */     MediaCodecInfo result = null;
/*     */     
/*  96 */     int numCodecs = getCodecCount(); int i;
/*  97 */     label18: for (i = 0; i < numCodecs; i++) {
/*  98 */       MediaCodecInfo codecInfo = getCodecInfoAt(i);
/*  99 */       if (codecInfo.isEncoder()) {
/*     */ 
/*     */         
/* 102 */         String[] types = codecInfo.getSupportedTypes();
/* 103 */         for (int j = 0; j < types.length; j++) {
/*     */           
/* 105 */           if (types[j].equalsIgnoreCase(mimeType)) {
/* 106 */             result = codecInfo; break label18;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 111 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 116 */   public final boolean isAudio() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MediaFormat createOutputFormat(byte[] csd, int size, int ix0, int ix1) {
/* 122 */     if (ix0 >= 0);
/*     */ 
/*     */ 
/*     */     
/* 126 */     MediaFormat outFormat = MediaFormat.createAudioFormat(this.MIME_TYPE, this.mSampleRate, this.mChannelCount);
/*     */     
/* 128 */     ByteBuffer csd0 = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
/* 129 */     csd0.put(csd, 0, size);
/* 130 */     csd0.flip();
/* 131 */     outFormat.setByteBuffer("csd-0", csd0);
/* 132 */     return outFormat;
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\AbstractAudioEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */