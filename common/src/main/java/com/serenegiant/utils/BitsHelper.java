/*     */ package com.serenegiant.utils;
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
/*     */ public final class BitsHelper
/*     */ {
/*     */   public static int countBits(byte v) {
/*  32 */     int count = (v & 0x55) + (v >>> 1 & 0x55);
/*  33 */     count = (count & 0x33) + (count >>> 2 & 0x33);
/*  34 */     return (count & 0xF) + (count >>> 4 & 0xF);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int countBits(short v) {
/*  42 */     int count = (v & 0x5555) + (v >>> 1 & 0x5555);
/*  43 */     count = (count & 0x3333) + (count >>> 2 & 0x3333);
/*  44 */     count = (count & 0xF0F) + (count >>> 4 & 0xF0F);
/*  45 */     return (count & 0xFF) + (count >>> 8 & 0xFF);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int countBits(int v) {
/*  54 */     int count = (v & 0x55555555) + (v >>> 1 & 0x55555555);
/*  55 */     count = (count & 0x33333333) + (count >>> 2 & 0x33333333);
/*  56 */     count = (count & 0xF0F0F0F) + (count >>> 4 & 0xF0F0F0F);
/*  57 */     count = (count & 0xFF00FF) + (count >>> 8 & 0xFF00FF);
/*  58 */     return (count & 0xFFFF) + (count >>> 16 & 0xFFFF);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int countBits(long v) {
/*  67 */     long count = (v & 0x5555555555555555L) + (v >>> 1L & 0x5555555555555555L);
/*  68 */     count = (count & 0x3333333333333333L) + (count >>> 2L & 0x3333333333333333L);
/*  69 */     count = (count & 0xF0F0F0F0F0F0F0FL) + (count >>> 4L & 0xF0F0F0F0F0F0F0FL);
/*  70 */     count = (count & 0xFF00FF00FF00FFL) + (count >>> 8L & 0xFF00FF00FF00FFL);
/*  71 */     count = (count & 0xFFFF0000FFFFL) + (count >>> 16L & 0xFFFF0000FFFFL);
/*  72 */     return (int)((count & 0xFFFFFFFFL) + (count >>> 32L & 0xFFFFFFFFL));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int MSB(byte v) {
/*  81 */     if (v == 0) return 0; 
/*  82 */     v = (byte)(v | v >>> 1);
/*  83 */     v = (byte)(v | v >>> 2);
/*  84 */     v = (byte)(v | v >>> 4);
/*  85 */     return countBits(v) - 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int MSB(short v) {
/*  94 */     if (v == 0) return 0; 
/*  95 */     v = (short)(v | v >>> 1);
/*  96 */     v = (short)(v | v >>> 2);
/*  97 */     v = (short)(v | v >>> 4);
/*  98 */     v = (short)(v | v >>> 8);
/*  99 */     return countBits(v) - 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int MSB(int v) {
/* 108 */     if (v == 0) return 0; 
/* 109 */     v |= v >>> 1;
/* 110 */     v |= v >>> 2;
/* 111 */     v |= v >>> 4;
/* 112 */     v |= v >>> 8;
/* 113 */     v |= v >>> 16;
/* 114 */     return countBits(v) - 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int MSB(long v) {
/* 124 */     if (v == 0L) return 0; 
/* 125 */     v |= v >>> 1L;
/* 126 */     v |= v >>> 2L;
/* 127 */     v |= v >>> 4L;
/* 128 */     v |= v >>> 8L;
/* 129 */     v |= v >>> 16L;
/* 130 */     v |= v >>> 32L;
/* 131 */     return countBits(v) - 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int LSB(byte v) {
/* 141 */     if (v == 0) return 0; 
/* 142 */     v = (byte)(v | v << 1);
/* 143 */     v = (byte)(v | v << 2);
/* 144 */     v = (byte)(v | v << 4);
/* 145 */     return 8 - countBits(v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int LSB(short v) {
/* 155 */     if (v == 0) return 0; 
/* 156 */     v = (short)(v | v << 1);
/* 157 */     v = (short)(v | v << 2);
/* 158 */     v = (short)(v | v << 4);
/* 159 */     v = (short)(v | v << 8);
/* 160 */     return 16 - countBits(v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int LSB(int v) {
/* 170 */     if (v == 0) return 0; 
/* 171 */     v |= v << 1;
/* 172 */     v |= v << 2;
/* 173 */     v |= v << 4;
/* 174 */     v |= v << 8;
/* 175 */     v |= v << 16;
/* 176 */     return 32 - countBits(v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int LSB(long v) {
/* 186 */     if (v == 0L) return 0; 
/* 187 */     v |= v << 1L;
/* 188 */     v |= v << 2L;
/* 189 */     v |= v << 4L;
/* 190 */     v |= v << 8L;
/* 191 */     v |= v << 16L;
/* 192 */     v |= v << 32L;
/* 193 */     return 64 - countBits(v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int squareBits(byte v) {
/* 202 */     if (v == 0) return 0; 
/* 203 */     return 1 << MSB(v - 1) + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int squareBits(short v) {
/* 212 */     if (v == 0) return 0; 
/* 213 */     return 1 << MSB(v - 1) + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int squareBits(int v) {
/* 222 */     if (v == 0) return 0; 
/* 223 */     return 1 << MSB(v - 1) + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int squareBits(long v) {
/* 232 */     if (v == 0L) return 0; 
/* 233 */     return 1 << MSB(v - 1L) + 1;
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\BitsHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */