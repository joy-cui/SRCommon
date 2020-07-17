/*     */ package com.serenegiant.net;
/*     */ 
/*     */ import android.support.annotation.NonNull;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.DoubleBuffer;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.LongBuffer;
/*     */ import java.nio.ShortBuffer;
/*     */ import java.nio.channels.ByteChannel;
/*     */ import java.nio.charset.Charset;
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
/*     */ public class ChannelHelper
/*     */ {
/*  38 */   private static final Charset UTF8 = Charset.forName("UTF-8");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean readBoolean(@NonNull ByteChannel channel) throws IOException {
/*  49 */     ByteBuffer buf = ByteBuffer.allocate(1);
/*  50 */     int readBytes = channel.read(buf);
/*  51 */     if (readBytes != 1) throw new IOException(); 
/*  52 */     buf.clear();
/*  53 */     return (buf.get() != 0);
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
/*     */   public static byte readByte(@NonNull ByteChannel channel) throws IOException {
/*  65 */     ByteBuffer buf = ByteBuffer.allocate(1);
/*  66 */     int readBytes = channel.read(buf);
/*  67 */     if (readBytes != 1) throw new IOException(); 
/*  68 */     buf.clear();
/*  69 */     return buf.get();
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
/*     */   public static char readChar(@NonNull ByteChannel channel) throws IOException {
/*  81 */     ByteBuffer buf = ByteBuffer.allocate(2);
/*  82 */     int readBytes = channel.read(buf);
/*  83 */     if (readBytes != 2) throw new IOException(); 
/*  84 */     buf.clear();
/*  85 */     return buf.getChar();
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
/*     */   public static short readShort(@NonNull ByteChannel channel) throws IOException {
/*  97 */     ByteBuffer buf = ByteBuffer.allocate(2);
/*  98 */     int readBytes = channel.read(buf);
/*  99 */     if (readBytes != 2) throw new IOException(); 
/* 100 */     buf.clear();
/* 101 */     return buf.getShort();
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
/*     */   public static int readInt(@NonNull ByteChannel channel) throws IOException {
/* 113 */     ByteBuffer buf = ByteBuffer.allocate(4);
/* 114 */     int readBytes = channel.read(buf);
/* 115 */     if (readBytes != 4) throw new IOException(); 
/* 116 */     buf.clear();
/* 117 */     return buf.getInt();
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
/*     */   public static long readLong(@NonNull ByteChannel channel) throws IOException {
/* 129 */     ByteBuffer buf = ByteBuffer.allocate(8);
/* 130 */     int readBytes = channel.read(buf);
/* 131 */     if (readBytes != 8) throw new IOException(); 
/* 132 */     buf.clear();
/* 133 */     return buf.getLong();
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
/*     */   public static float readFloat(@NonNull ByteChannel channel) throws IOException {
/* 145 */     ByteBuffer buf = ByteBuffer.allocate(4);
/* 146 */     int readBytes = channel.read(buf);
/* 147 */     if (readBytes != 4) throw new IOException(); 
/* 148 */     buf.clear();
/* 149 */     return buf.getFloat();
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
/*     */   public static double readDouble(@NonNull ByteChannel channel) throws IOException {
/* 161 */     ByteBuffer buf = ByteBuffer.allocate(8);
/* 162 */     int readBytes = channel.read(buf);
/* 163 */     if (readBytes != 8) throw new IOException(); 
/* 164 */     buf.clear();
/* 165 */     return buf.getDouble();
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
/*     */   public static String readString(@NonNull ByteChannel channel) throws IOException {
/* 177 */     int bytes = readInt(channel);
/* 178 */     byte[] buf = new byte[bytes];
/* 179 */     ByteBuffer b = ByteBuffer.wrap(buf);
/* 180 */     int readBytes = channel.read(b);
/* 181 */     if (readBytes != bytes) throw new IOException(); 
/* 182 */     return new String(buf, UTF8);
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
/*     */   public static boolean[] readBooleanArray(@NonNull ByteChannel channel) throws IOException {
/* 194 */     int n = readInt(channel);
/* 195 */     ByteBuffer buf = ByteBuffer.allocate(n);
/* 196 */     int readBytes = channel.read(buf);
/* 197 */     if (readBytes != n) throw new IOException(); 
/* 198 */     buf.clear();
/* 199 */     boolean[] result = new boolean[n];
/* 200 */     for (int i = 0; i < n; i++) {
/* 201 */       result[i] = (buf.get() != 0);
/*     */     }
/* 203 */     return result;
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
/*     */   public static byte[] readByteArray(@NonNull ByteChannel channel) throws IOException {
/* 215 */     int n = readInt(channel);
/* 216 */     byte[] result = new byte[n];
/* 217 */     ByteBuffer buf = ByteBuffer.wrap(result);
/* 218 */     int readBytes = channel.read(buf);
/* 219 */     if (readBytes != n) throw new IOException(); 
/* 220 */     return result;
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
/*     */   public static char[] readCharArray(@NonNull ByteChannel channel) throws IOException {
/* 232 */     int n = readInt(channel);
/* 233 */     ByteBuffer buf = ByteBuffer.allocate(n * 2);
/* 234 */     int readBytes = channel.read(buf);
/* 235 */     if (readBytes != n * 2) throw new IOException(); 
/* 236 */     buf.clear();
/* 237 */     CharBuffer result = buf.asCharBuffer();
/* 238 */     if (result.hasArray()) {
/* 239 */       return result.array();
/*     */     }
/* 241 */     char[] b = new char[n];
/* 242 */     result.get(b);
/* 243 */     return b;
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
/*     */   public static short[] readShortArray(@NonNull ByteChannel channel) throws IOException {
/* 256 */     int n = readInt(channel);
/* 257 */     ByteBuffer buf = ByteBuffer.allocate(n * 2);
/* 258 */     int readBytes = channel.read(buf);
/* 259 */     if (readBytes != n * 2) throw new IOException(); 
/* 260 */     buf.clear();
/* 261 */     ShortBuffer result = buf.asShortBuffer();
/* 262 */     if (result.hasArray()) {
/* 263 */       return result.array();
/*     */     }
/* 265 */     short[] b = new short[n];
/* 266 */     result.get(b);
/* 267 */     return b;
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
/*     */   public static int[] readIntArray(@NonNull ByteChannel channel) throws IOException {
/* 280 */     int n = readInt(channel);
/* 281 */     ByteBuffer buf = ByteBuffer.allocate(n * 4);
/* 282 */     int readBytes = channel.read(buf);
/* 283 */     if (readBytes != n * 4) throw new IOException(); 
/* 284 */     buf.clear();
/* 285 */     IntBuffer result = buf.asIntBuffer();
/* 286 */     if (result.hasArray()) {
/* 287 */       return result.array();
/*     */     }
/* 289 */     int[] b = new int[n];
/* 290 */     result.get(b);
/* 291 */     return b;
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
/*     */   public static long[] readLongArray(@NonNull ByteChannel channel) throws IOException {
/* 304 */     int n = readInt(channel);
/* 305 */     ByteBuffer buf = ByteBuffer.allocate(n * 8);
/* 306 */     int readBytes = channel.read(buf);
/* 307 */     if (readBytes != n * 8) throw new IOException(); 
/* 308 */     buf.clear();
/* 309 */     LongBuffer result = buf.asLongBuffer();
/* 310 */     if (result.hasArray()) {
/* 311 */       return result.array();
/*     */     }
/* 313 */     long[] b = new long[n];
/* 314 */     result.get(b);
/* 315 */     return b;
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
/*     */   public static float[] readFloatArray(@NonNull ByteChannel channel) throws IOException {
/* 328 */     int n = readInt(channel);
/* 329 */     ByteBuffer buf = ByteBuffer.allocate(n * 4);
/* 330 */     int readBytes = channel.read(buf);
/* 331 */     if (readBytes != n * 4) throw new IOException(); 
/* 332 */     buf.clear();
/* 333 */     FloatBuffer result = buf.asFloatBuffer();
/* 334 */     if (result.hasArray()) {
/* 335 */       return result.array();
/*     */     }
/* 337 */     float[] b = new float[n];
/* 338 */     result.get(b);
/* 339 */     return b;
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
/*     */   public static double[] readDoubleArray(@NonNull ByteChannel channel) throws IOException {
/* 352 */     int n = readInt(channel);
/* 353 */     ByteBuffer buf = ByteBuffer.allocate(n * 8);
/* 354 */     int readBytes = channel.read(buf);
/* 355 */     if (readBytes != n * 8) throw new IOException(); 
/* 356 */     buf.clear();
/* 357 */     DoubleBuffer result = buf.asDoubleBuffer();
/* 358 */     if (result.hasArray()) {
/* 359 */       return result.array();
/*     */     }
/* 361 */     double[] b = new double[n];
/* 362 */     result.get(b);
/* 363 */     return b;
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
/*     */   public static ByteBuffer readByteBuffer(@NonNull ByteChannel channel) throws IOException {
/* 376 */     int n = readInt(channel);
/* 377 */     ByteBuffer buf = ByteBuffer.allocateDirect(n);
/* 378 */     int readBytes = channel.read(buf);
/* 379 */     if (readBytes != n) throw new IOException(); 
/* 380 */     buf.position(n);
/* 381 */     buf.flip();
/* 382 */     return buf;
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
/*     */ 
/*     */   
/*     */   public static ByteBuffer readByteBuffer(@NonNull ByteChannel channel, @NonNull ByteBuffer buf) throws IOException {
/* 397 */     int n = readInt(channel);
/* 398 */     if (buf.remaining() < n) {
/* 399 */       ByteBuffer temp = ByteBuffer.allocate(n);
/* 400 */       channel.read(temp);
/* 401 */       throw new IOException();
/*     */     } 
/* 403 */     int readBytes = channel.read(buf);
/* 404 */     if (readBytes != n) throw new IOException(); 
/* 405 */     buf.position(n);
/* 406 */     buf.flip();
/* 407 */     return buf;
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
/*     */   public static void write(@NonNull ByteChannel channel, boolean value) throws IOException {
/* 419 */     ByteBuffer buf = ByteBuffer.allocate(1);
/* 420 */     buf.put((byte)(value ? 1 : 0));
/* 421 */     buf.flip();
/* 422 */     channel.write(buf);
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
/*     */   public static void write(@NonNull ByteChannel channel, byte value) throws IOException {
/* 434 */     ByteBuffer buf = ByteBuffer.allocate(1);
/* 435 */     buf.put(value);
/* 436 */     buf.flip();
/* 437 */     channel.write(buf);
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
/*     */   public static void write(@NonNull ByteChannel channel, char value) throws IOException {
/* 449 */     ByteBuffer buf = ByteBuffer.allocate(2);
/* 450 */     buf.putChar(value);
/* 451 */     buf.flip();
/* 452 */     channel.write(buf);
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
/*     */   public static void write(@NonNull ByteChannel channel, short value) throws IOException {
/* 464 */     ByteBuffer buf = ByteBuffer.allocate(2);
/* 465 */     buf.putShort(value);
/* 466 */     buf.flip();
/* 467 */     channel.write(buf);
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
/*     */   public static void write(@NonNull ByteChannel channel, int value) throws IOException {
/* 479 */     ByteBuffer buf = ByteBuffer.allocate(4);
/* 480 */     buf.putInt(value);
/* 481 */     buf.flip();
/* 482 */     channel.write(buf);
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
/*     */   public static void write(@NonNull ByteChannel channel, long value) throws IOException {
/* 494 */     ByteBuffer buf = ByteBuffer.allocate(8);
/* 495 */     buf.putLong(value);
/* 496 */     buf.flip();
/* 497 */     channel.write(buf);
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
/*     */   public static void write(@NonNull ByteChannel channel, float value) throws IOException {
/* 509 */     ByteBuffer buf = ByteBuffer.allocate(4);
/* 510 */     buf.putFloat(value);
/* 511 */     buf.flip();
/* 512 */     channel.write(buf);
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
/*     */   public static void write(@NonNull ByteChannel channel, double value) throws IOException {
/* 524 */     ByteBuffer buf = ByteBuffer.allocate(8);
/* 525 */     buf.putDouble(value);
/* 526 */     buf.flip();
/* 527 */     channel.write(buf);
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
/*     */   public static void write(@NonNull ByteChannel channel, @NonNull String value) throws IOException {
/* 539 */     byte[] buf = value.getBytes(UTF8);
/* 540 */     write(channel, buf.length);
/* 541 */     channel.write(ByteBuffer.wrap(buf));
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
/*     */   public static void write(@NonNull ByteChannel channel, @NonNull boolean[] value) throws IOException {
/* 553 */     int n = value.length;
/* 554 */     write(channel, n);
/* 555 */     ByteBuffer buf = ByteBuffer.allocate(n);
/* 556 */     for (int i = 0; i < n; i++) {
/* 557 */       buf.put((byte)(value[i] ? 1 : 0));
/*     */     }
/* 559 */     buf.flip();
/* 560 */     channel.write(buf);
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
/*     */   public static void write(@NonNull ByteChannel channel, @NonNull byte[] value) throws IOException {
/* 572 */     write(channel, value.length);
/* 573 */     channel.write(ByteBuffer.wrap(value));
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
/*     */   public static void write(@NonNull ByteChannel channel, @NonNull char[] value) throws IOException {
/* 585 */     int n = value.length;
/* 586 */     ByteBuffer buf = ByteBuffer.allocate(n * 2);
/* 587 */     for (int i = 0; i < n; i++) {
/* 588 */       buf.putChar(value[i]);
/*     */     }
/* 590 */     buf.flip();
/* 591 */     channel.write(buf);
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
/*     */   public static void write(@NonNull ByteChannel channel, @NonNull short[] value) throws IOException {
/* 603 */     int n = value.length;
/* 604 */     ByteBuffer buf = ByteBuffer.allocate(n * 2);
/* 605 */     for (int i = 0; i < n; i++) {
/* 606 */       buf.putShort(value[i]);
/*     */     }
/* 608 */     buf.flip();
/* 609 */     channel.write(buf);
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
/*     */   public static void write(@NonNull ByteChannel channel, @NonNull int[] value) throws IOException {
/* 621 */     int n = value.length;
/* 622 */     ByteBuffer buf = ByteBuffer.allocate(n * 4);
/* 623 */     for (int i = 0; i < n; i++) {
/* 624 */       buf.putInt(value[i]);
/*     */     }
/* 626 */     buf.flip();
/* 627 */     channel.write(buf);
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
/*     */   public static void write(@NonNull ByteChannel channel, @NonNull long[] value) throws IOException {
/* 639 */     int n = value.length;
/* 640 */     ByteBuffer buf = ByteBuffer.allocate(n * 8);
/* 641 */     for (int i = 0; i < n; i++) {
/* 642 */       buf.putLong(value[i]);
/*     */     }
/* 644 */     buf.flip();
/* 645 */     channel.write(buf);
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
/*     */   public static void write(@NonNull ByteChannel channel, @NonNull float[] value) throws IOException {
/* 657 */     int n = value.length;
/* 658 */     ByteBuffer buf = ByteBuffer.allocate(n * 4);
/* 659 */     for (int i = 0; i < n; i++) {
/* 660 */       buf.putFloat(value[i]);
/*     */     }
/* 662 */     buf.flip();
/* 663 */     channel.write(buf);
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
/*     */   public static void write(@NonNull ByteChannel channel, @NonNull double[] value) throws IOException {
/* 675 */     int n = value.length;
/* 676 */     ByteBuffer buf = ByteBuffer.allocate(n * 8);
/* 677 */     for (int i = 0; i < n; i++) {
/* 678 */       buf.putDouble(value[i]);
/*     */     }
/* 680 */     buf.flip();
/* 681 */     channel.write(buf);
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
/*     */   public static void write(@NonNull ByteChannel channel, @NonNull ByteBuffer value) throws IOException {
/* 693 */     write(channel, value.remaining());
/* 694 */     channel.write(value);
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\net\ChannelHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */