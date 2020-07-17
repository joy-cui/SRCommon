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
/*     */ public class Endian
/*     */ {
/*  23 */   public static boolean be2boolean(byte[] bytes, int offset) { return (bytes[offset] != 0); }
/*     */ 
/*     */ 
/*     */   
/*  27 */   public static boolean le2boolean(byte[] bytes, int offset) { return (bytes[offset] != 0); }
/*     */ 
/*     */ 
/*     */   
/*  31 */   public static char be2char(byte[] bytes, int offset) { return (char)((bytes[offset + 1] & 0xFF) + (bytes[offset] << 8)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  36 */   public static char le2char(byte[] bytes, int offset) { return (char)((bytes[offset] & 0xFF) + (bytes[offset + 1] << 8)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  41 */   public static short be2short(byte[] bytes, int offset) { return (short)((bytes[offset + 1] & 0xFF) + (bytes[offset] << 8)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   public static short le2short(byte[] bytes, int offset) { return (short)((bytes[offset] & 0xFF) + (bytes[offset + 1] << 8)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   public static int be2int(byte[] bytes, int offset) { return (bytes[offset + 3] & 0xFF) + ((bytes[offset + 2] & 0xFF) << 8) + ((bytes[offset + 1] & 0xFF) << 16) + (bytes[offset] << 24); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   public static int le2int(byte[] bytes, int offset) { return (bytes[offset] & 0xFF) + ((bytes[offset + 1] & 0xFF) << 8) + ((bytes[offset + 2] & 0xFF) << 16) + (bytes[offset + 3] << 24); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   public static float be2float(byte[] bytes, int offset) { return Float.intBitsToFloat(be2int(bytes, offset)); }
/*     */ 
/*     */ 
/*     */   
/*  69 */   public static float le2float(byte[] bytes, int offset) { return Float.intBitsToFloat(le2int(bytes, offset)); }
/*     */ 
/*     */ 
/*     */   
/*  73 */   public static long be2long(byte[] bytes, int offset) { return (bytes[offset + 7] & 0xFFL) + ((bytes[offset + 6] & 0xFFL) << 8L) + ((bytes[offset + 5] & 0xFFL) << 16L) + ((bytes[offset + 4] & 0xFFL) << 24L) + ((bytes[offset + 3] & 0xFFL) << 32L) + ((bytes[offset + 2] & 0xFFL) << 40L) + ((bytes[offset + 1] & 0xFFL) << 48L) + (bytes[offset] << 56L); }
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
/*  84 */   public static long le2long(byte[] bytes, int offset) { return (bytes[offset] & 0xFFL) + ((bytes[offset + 1] & 0xFFL) << 8L) + ((bytes[offset + 2] & 0xFFL) << 16L) + ((bytes[offset + 3] & 0xFFL) << 24L) + ((bytes[offset + 4] & 0xFFL) << 32L) + ((bytes[offset + 5] & 0xFFL) << 40L) + ((bytes[offset + 6] & 0xFFL) << 48L) + (bytes[offset + 7] << 56L); }
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
/*  95 */   public static double be2double(byte[] bytes, int offset) { return Double.longBitsToDouble(be2long(bytes, offset)); }
/*     */ 
/*     */ 
/*     */   
/*  99 */   public static double le2double(byte[] bytes, int offset) { return Double.longBitsToDouble(le2long(bytes, offset)); }
/*     */ 
/*     */ 
/*     */   
/* 103 */   public static void bool2be(byte[] bytes, int offset, boolean value) { bytes[offset] = (byte)(value ? 1 : 0); }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public static void bool2le(byte[] bytes, int offset, boolean value) { bytes[offset] = (byte)(value ? 1 : 0); }
/*     */ 
/*     */   
/*     */   public static void char2be(byte[] bytes, int offset, char value) {
/* 111 */     bytes[offset + 1] = (byte)value;
/* 112 */     bytes[offset] = (byte)(value >>> 8);
/*     */   }
/*     */   
/*     */   public static void char2le(byte[] bytes, int offset, char value) {
/* 116 */     bytes[offset] = (byte)value;
/* 117 */     bytes[offset + 1] = (byte)(value >>> 8);
/*     */   }
/*     */   
/*     */   public static void short2be(byte[] b, int offset, short value) {
/* 121 */     b[offset + 1] = (byte)value;
/* 122 */     b[offset] = (byte)(value >>> 8);
/*     */   }
/*     */   
/*     */   public static void short2le(byte[] b, int offset, short value) {
/* 126 */     b[offset] = (byte)value;
/* 127 */     b[offset + 1] = (byte)(value >>> 8);
/*     */   }
/*     */   
/*     */   public static void int2be(byte[] bytes, int offset, int value) {
/* 131 */     bytes[offset + 3] = (byte)value;
/* 132 */     bytes[offset + 2] = (byte)(value >>> 8);
/* 133 */     bytes[offset + 1] = (byte)(value >>> 16);
/* 134 */     bytes[offset] = (byte)(value >>> 24);
/*     */   }
/*     */   
/*     */   public static void int2le(byte[] bytes, int offset, int value) {
/* 138 */     bytes[offset] = (byte)value;
/* 139 */     bytes[offset + 1] = (byte)(value >>> 8);
/* 140 */     bytes[offset + 2] = (byte)(value >>> 16);
/* 141 */     bytes[offset + 3] = (byte)(value >>> 24);
/*     */   }
/*     */ 
/*     */   
/* 145 */   public static void float2be(byte[] bytes, int offset, float value) { int2be(bytes, offset, Float.floatToIntBits(value)); }
/*     */ 
/*     */ 
/*     */   
/* 149 */   public static void float2le(byte[] bytes, int offset, float value) { int2le(bytes, offset, Float.floatToIntBits(value)); }
/*     */ 
/*     */   
/*     */   public static void long2be(byte[] bytes, int offset, long val) {
/* 153 */     bytes[offset + 7] = (byte)(int)val;
/* 154 */     bytes[offset + 6] = (byte)(int)(val >>> 8L);
/* 155 */     bytes[offset + 5] = (byte)(int)(val >>> 16L);
/* 156 */     bytes[offset + 4] = (byte)(int)(val >>> 24L);
/* 157 */     bytes[offset + 3] = (byte)(int)(val >>> 32L);
/* 158 */     bytes[offset + 2] = (byte)(int)(val >>> 40L);
/* 159 */     bytes[offset + 1] = (byte)(int)(val >>> 48L);
/* 160 */     bytes[offset] = (byte)(int)(val >>> 56L);
/*     */   }
/*     */   
/*     */   public static void long2le(byte[] bytes, int offset, long val) {
/* 164 */     bytes[offset] = (byte)(int)val;
/* 165 */     bytes[offset + 1] = (byte)(int)(val >>> 8L);
/* 166 */     bytes[offset + 2] = (byte)(int)(val >>> 16L);
/* 167 */     bytes[offset + 3] = (byte)(int)(val >>> 24L);
/* 168 */     bytes[offset + 4] = (byte)(int)(val >>> 32L);
/* 169 */     bytes[offset + 5] = (byte)(int)(val >>> 40L);
/* 170 */     bytes[offset + 6] = (byte)(int)(val >>> 48L);
/* 171 */     bytes[offset + 7] = (byte)(int)(val >>> 56L);
/*     */   }
/*     */ 
/*     */   
/* 175 */   public static void double2be(byte[] bytes, int offset, double value) { long2be(bytes, offset, Double.doubleToLongBits(value)); }
/*     */ 
/*     */ 
/*     */   
/* 179 */   public static void double2le(byte[] bytes, int offset, double value) { long2le(bytes, offset, Double.doubleToLongBits(value)); }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegian\\utils\Endian.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */