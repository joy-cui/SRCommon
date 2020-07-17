/*    */ package com.serenegiant.media;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.ByteOrder;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AudioData
/*    */ {
/*    */   ByteBuffer mBuffer;
/*    */   int size;
/*    */   long presentationTimeUs;
/*    */   
/* 35 */   public AudioData(int size) { this.mBuffer = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void set(ByteBuffer buffer, int _size, long _presentationTimeUs) {
/* 40 */     this.presentationTimeUs = _presentationTimeUs;
/* 41 */     this.size = _size;
/* 42 */     if (this.mBuffer == null || this.mBuffer.capacity() < _size) {
/* 43 */       this.mBuffer = ByteBuffer.allocateDirect(_size).order(ByteOrder.nativeOrder());
/*    */     }
/* 45 */     this.mBuffer.clear();
/* 46 */     this.mBuffer.put(buffer);
/* 47 */     this.mBuffer.position(_size);
/* 48 */     this.mBuffer.flip();
/*    */   }
/*    */   
/*    */   public void clear() {
/* 52 */     this.size = 0;
/* 53 */     this.mBuffer.clear();
/*    */   }
/*    */ 
/*    */   
/* 57 */   public int size() { return this.size; }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public long presentationTimeUs() { return this.presentationTimeUs; }
/*    */ 
/*    */   
/*    */   public void get(byte[] buffer) {
/* 65 */     if (buffer == null || buffer.length < this.size) {
/* 66 */       throw new ArrayIndexOutOfBoundsException("");
/*    */     }
/* 68 */     this.mBuffer.clear();
/* 69 */     this.mBuffer.get(buffer);
/*    */   }
/*    */   
/*    */   public void get(ByteBuffer buffer) {
/* 73 */     if (buffer == null || buffer.remaining() < this.size) {
/* 74 */       throw new ArrayIndexOutOfBoundsException("");
/*    */     }
/* 76 */     this.mBuffer.clear();
/* 77 */     buffer.put(this.mBuffer);
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\AudioData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */