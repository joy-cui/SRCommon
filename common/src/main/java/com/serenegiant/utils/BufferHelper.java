/*     */ package com.serenegiant.utils;
/*     */ 
/*     */ import android.support.annotation.NonNull;
/*     */ import android.util.Log;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.FloatBuffer;
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
/*     */ public class BufferHelper
/*     */ {
/*     */   private static final int BUF_LEN = 256;
/*     */   
/*  34 */   public static final void dumpByteBuffer(String tag, ByteBuffer buffer, int offset, int size) { dumpByteBuffer(tag, buffer, offset, size, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void dumpByteBuffer(String tag, ByteBuffer buffer, int offset, int _size, boolean findAnnexB) {
/*  40 */     byte[] dump = new byte[256];
/*     */     
/*  42 */     if (buffer == null)
/*  43 */       return;  int n = buffer.limit();
/*  44 */     int pos = buffer.position();
/*     */ 
/*     */     
/*  47 */     int size = _size;
/*  48 */     if (size > n) size = n; 
/*  49 */     buffer.position(offset);
/*  50 */     StringBuilder sb = new StringBuilder();
/*     */     
/*  52 */     for (int i = offset; i < size; i += 256) {
/*  53 */       int sz = (i + 256 < size) ? 256 : (size - i);
/*  54 */       buffer.get(dump, 0, sz);
/*  55 */       sb.setLength(0);
/*  56 */       for (int j = 0; j < sz; j++) {
/*  57 */         sb.append(String.format("%02x", new Object[] { Byte.valueOf(dump[j]) }));
/*     */       } 
/*  59 */       if (findAnnexB) {
/*  60 */         int index = -1;
/*     */         do {
/*  62 */           index = byteComp(dump, index + 1, ANNEXB_START_MARK, ANNEXB_START_MARK.length);
/*  63 */           if (index < 0)
/*  64 */             continue;  Log.i(tag, "found ANNEXB: start index=" + index);
/*     */         }
/*  66 */         while (index >= 0);
/*     */       } 
/*     */     } 
/*  69 */     Log.i(tag, "dumpByteBuffer:" + sb.toString());
/*  70 */     buffer.position(pos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   public static final byte[] ANNEXB_START_MARK = new byte[] { 0, 0, 0, 1 };
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int SIZEOF_FLOAT = 4;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int byteComp(@NonNull byte[] array, int offset, @NonNull byte[] search, int len) {
/*  87 */     int index = -1;
/*  88 */     int n0 = array.length;
/*  89 */     int ns = search.length;
/*  90 */     if (n0 >= offset + len && ns >= len) {
/*  91 */       for (int i = offset; i < n0 - len; i++) {
/*  92 */         int j = len - 1;
/*  93 */         while (j >= 0 && 
/*  94 */           array[i + j] == search[j]) {
/*  95 */           j--;
/*     */         }
/*  97 */         if (j < 0) {
/*  98 */           index = i;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/* 103 */     return index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int findAnnexB(byte[] data, int offset) {
/* 114 */     int result = -1;
/* 115 */     if (data != null) {
/* 116 */       int len = data.length - 4;
/* 117 */       for (int i = offset; i < len; i++) {
/*     */         
/* 119 */         if (data[i] == 0 && data[i + 1] == 0)
/*     */         {
/*     */ 
/*     */           
/* 123 */           if (data[i + 2] == 1) {
/* 124 */             result = i;
/*     */             break;
/*     */           }  } 
/*     */       } 
/*     */     } 
/* 129 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FloatBuffer createFloatBuffer(float[] coords) {
/* 138 */     ByteBuffer bb = ByteBuffer.allocateDirect(coords.length * 4);
/* 139 */     bb.order(ByteOrder.nativeOrder());
/* 140 */     FloatBuffer fb = bb.asFloatBuffer();
/* 141 */     fb.put(coords);
/* 142 */     fb.position(0);
/* 143 */     return fb;
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\BufferHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */