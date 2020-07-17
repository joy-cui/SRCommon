/*    */ package com.serenegiant.media;
/*    */ 
/*    */ import android.annotation.TargetApi;
/*    */ import android.media.MediaCodec;
/*    */ import android.media.MediaFormat;
/*    */ import android.media.MediaMuxer;
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @TargetApi(18)
/*    */ public class MediaMuxerWrapper
/*    */   implements IMuxer
/*    */ {
/*    */   private final MediaMuxer mMuxer;
/*    */   private volatile boolean mIsStarted;
/*    */   
/* 37 */   public MediaMuxerWrapper(String output_path, int format) throws IOException { this.mMuxer = new MediaMuxer(output_path, format); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public int addTrack(MediaFormat format) { return this.mMuxer.addTrack(format); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public void writeSampleData(int trackIndex, ByteBuffer byteBuf, MediaCodec.BufferInfo bufferInfo) { this.mMuxer.writeSampleData(trackIndex, byteBuf, bufferInfo); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void start() {
/* 52 */     this.mMuxer.start();
/* 53 */     this.mIsStarted = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 58 */     this.mIsStarted = false;
/* 59 */     this.mMuxer.stop();
/*    */   }
/*    */ 
/*    */   
/*    */   public void release() {
/* 64 */     this.mIsStarted = false;
/* 65 */     this.mMuxer.release();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 70 */   public boolean isStarted() { return this.mIsStarted; }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\MediaMuxerWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */