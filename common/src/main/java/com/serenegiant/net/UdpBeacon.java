/*     */ package com.serenegiant.net;
/*     */ 
/*     */ import android.os.Handler;
/*     */ import android.os.SystemClock;
/*     */ import android.support.annotation.Nullable;
/*     */ import android.util.Log;
/*     */ import com.serenegiant.utils.HandlerThreadHandler;
/*     */ import java.io.IOException;
/*     */ import java.net.SocketException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Locale;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CopyOnWriteArraySet;
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
/*     */ public class UdpBeacon
/*     */ {
/*     */   private static final String TAG = "UdpBeacon";
/*     */   private static final int BEACON_UDP_PORT = 9999;
/*     */   private static final byte BEACON_VERSION = 1;
/*     */   public static final int BEACON_SIZE = 23;
/*     */   private static final long DEFAULT_BEACON_SEND_INTERVALS_MS = 3000L;
/*  49 */   private static final Charset CHARSET = Charset.forName("UTF-8");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int SO_TIMEOUT_MS = 2000;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class Beacon
/*     */   {
/*     */     public static final String BEACON_IDENTITY = "SAKI";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final UUID uuid;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int listenPort;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int extraBytes;
/*     */ 
/*     */ 
/*     */     
/*     */     private final ByteBuffer extras;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Beacon(ByteBuffer buffer) {
/*  87 */       this.uuid = new UUID(buffer.getLong(), buffer.getLong());
/*  88 */       int port = buffer.getShort();
/*  89 */       this.listenPort = (port < 0) ? (0xFFFF & port) : port;
/*  90 */       if (buffer.remaining() > 1) {
/*     */         
/*  92 */         this.extraBytes = buffer.get() & 0xFF;
/*  93 */         if (this.extraBytes > 0) {
/*  94 */           this.extras = ByteBuffer.allocateDirect(this.extraBytes);
/*  95 */           this.extras.put(buffer);
/*  96 */           this.extras.flip();
/*     */         } else {
/*  98 */           this.extras = null;
/*     */         } 
/*     */       } else {
/* 101 */         this.extraBytes = 0;
/* 102 */         this.extras = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 107 */     public Beacon(UUID uuid, int port) { this(uuid, port, 0); }
/*     */ 
/*     */     
/*     */     public Beacon(UUID uuid, int port, int extras) {
/* 111 */       this.uuid = uuid;
/* 112 */       this.listenPort = port;
/* 113 */       this.extraBytes = extras & 0xFF;
/* 114 */       if (this.extraBytes > 0) {
/* 115 */         this.extras = ByteBuffer.allocateDirect(this.extraBytes);
/*     */       } else {
/* 117 */         this.extras = null;
/*     */       } 
/*     */     }
/*     */     
/*     */     public byte[] asBytes() {
/* 122 */       byte[] bytes = new byte[23 + ((this.extraBytes > 0) ? (this.extraBytes + 1) : 0)];
/* 123 */       ByteBuffer buffer = ByteBuffer.wrap(bytes);
/* 124 */       buffer.put("SAKI".getBytes());
/* 125 */       buffer.put((byte)1);
/* 126 */       buffer.putLong(this.uuid.getMostSignificantBits());
/* 127 */       buffer.putLong(this.uuid.getLeastSignificantBits());
/* 128 */       buffer.putShort((short)this.listenPort);
/* 129 */       if (this.extraBytes > 0) {
/* 130 */         buffer.put((byte)this.extraBytes);
/* 131 */         this.extras.clear();
/* 132 */         this.extras.flip();
/* 133 */         buffer.put(this.extras);
/*     */       } 
/* 135 */       buffer.flip();
/* 136 */       return bytes;
/*     */     }
/*     */ 
/*     */     
/* 140 */     public ByteBuffer extra() { return this.extras; }
/*     */ 
/*     */ 
/*     */     
/* 144 */     public void extra(byte[] extra) { extra(ByteBuffer.wrap(extra)); }
/*     */ 
/*     */     
/*     */     public void extra(ByteBuffer extra) {
/* 148 */       int n = (extra != null) ? extra.remaining() : -1;
/* 149 */       if (this.extraBytes > 0 && this.extraBytes <= n) {
/* 150 */         this.extras.clear();
/* 151 */         this.extras.put(extra);
/* 152 */         this.extras.flip();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 157 */     public String toString() { return String.format(Locale.US, "Beacon(%s,port=%d,extra=%d)", new Object[] { this.uuid.toString(), Integer.valueOf(this.listenPort), Integer.valueOf(this.extraBytes) }); }
/*     */   }
/*     */ 
/*     */   
/* 161 */   private final Object mSync = new Object();
/* 162 */   private final CopyOnWriteArraySet<UdpBeaconCallback> mCallbacks = new CopyOnWriteArraySet<>();
/*     */   
/*     */   private Handler mAsyncHandler;
/*     */   
/*     */   private final UUID uuid;
/*     */   
/*     */   private final byte[] beaconBytes;
/*     */   
/*     */   private final long mBeaconIntervalsMs;
/*     */   
/*     */   private final long mRcvMinIntervalsMs;
/*     */   
/*     */   private Thread mBeaconThread;
/*     */   private boolean mReceiveOnly;
/*     */   private volatile boolean mIsRunning;
/*     */   private volatile boolean mReleased;
/*     */   private final Runnable mBeaconTask;
/*     */   
/* 180 */   public UdpBeacon(@Nullable UdpBeaconCallback callback) { this(callback, 9999, 3000L, false, 0L); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 190 */   public UdpBeacon(@Nullable UdpBeaconCallback callback, long beacon_intervals_ms) { this(callback, 9999, beacon_intervals_ms, false, 0L); }
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
/* 201 */   public UdpBeacon(@Nullable UdpBeaconCallback callback, boolean receiveOnly) { this(callback, 9999, 3000L, false, 0L); }
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
/* 215 */   public UdpBeacon(@Nullable UdpBeaconCallback callback, boolean receiveOnly, long rcv_min_intervals_ms) { this(callback, 9999, 3000L, false, rcv_min_intervals_ms); }
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
/* 229 */   public UdpBeacon(@Nullable UdpBeaconCallback callback, long beacon_intervals_ms, boolean receiveOnly) { this(callback, 9999, beacon_intervals_ms, receiveOnly, 0L); }
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
/* 244 */   public UdpBeacon(@Nullable UdpBeaconCallback callback, long beacon_intervals_ms, boolean receiveOnly, long rcv_min_intervals_ms) { this(callback, 9999, beacon_intervals_ms, receiveOnly, rcv_min_intervals_ms); }
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
/*     */   public void finalize() throws Throwable {
/* 274 */     release();
/* 275 */     super.finalize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void release() {
/* 283 */     if (!this.mReleased) {
/* 284 */       this.mReleased = true;
/* 285 */       stop();
/* 286 */       this.mCallbacks.clear();
/* 287 */       synchronized (this.mSync) {
/* 288 */         if (this.mAsyncHandler != null) {
/*     */           try {
/* 290 */             this.mAsyncHandler.getLooper().quit();
/* 291 */           } catch (Exception exception) {}
/*     */ 
/*     */           
/* 294 */           this.mAsyncHandler = null;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addCallback(UdpBeaconCallback callback) {
/* 301 */     if (callback != null) {
/* 302 */       this.mCallbacks.add(callback);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 307 */   public void removeCallback(UdpBeaconCallback callback) { this.mCallbacks.remove(callback); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/* 316 */     checkReleased();
/* 317 */     synchronized (this.mSync) {
/* 318 */       if (this.mBeaconThread == null) {
/* 319 */         this.mIsRunning = true;
/* 320 */         this.mBeaconThread = new Thread(this.mBeaconTask, "UdpBeaconTask");
/* 321 */         this.mBeaconThread.start();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/*     */     Thread thread;
/* 330 */     this.mIsRunning = false;
/*     */     
/* 332 */     synchronized (this.mSync) {
/* 333 */       thread = this.mBeaconThread;
/* 334 */       this.mBeaconThread = null;
/* 335 */       this.mSync.notifyAll();
/*     */     } 
/* 337 */     if (thread != null && thread.isAlive()) {
/* 338 */       thread.interrupt();
/*     */       try {
/* 340 */         thread.join();
/* 341 */       } catch (Exception e) {
/* 342 */         Log.d("UdpBeacon", e.getMessage());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 352 */   public void shot() throws IllegalStateException { shot(1); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void shot(int n) throws IllegalStateException {
/* 360 */     checkReleased();
/* 361 */     synchronized (this.mSync) {
/* 362 */       (new Thread(new BeaconShotTask(n), "UdpOneShotBeaconTask")).start();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 371 */   public boolean isActive() { return this.mIsRunning; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReceiveOnly(boolean receiveOnly) throws IllegalStateException {
/* 381 */     checkReleased();
/* 382 */     synchronized (this.mSync) {
/* 383 */       if (this.mIsRunning) {
/* 384 */         throw new IllegalStateException("beacon is already active");
/*     */       }
/* 386 */       this.mReceiveOnly = receiveOnly;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 395 */   public boolean isReceiveOnly() { return this.mReceiveOnly; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkReleased() throws IllegalStateException {
/* 403 */     if (this.mReleased) {
/* 404 */       throw new IllegalStateException("already released");
/*     */     }
/*     */   }
/*     */   
/*     */   private final void callOnError(final Exception e) {
/* 409 */     if (this.mReleased) {
/* 410 */       Log.w("UdpBeacon", e);
/*     */       return;
/*     */     } 
/* 413 */     synchronized (this.mSync) {
/* 414 */       if (this.mAsyncHandler != null) {
/* 415 */         this.mAsyncHandler.post(new Runnable()
/*     */             {
/*     */               public void run() {
/* 418 */                 for (UdpBeaconCallback callback : UdpBeacon.this.mCallbacks) {
/*     */                   try {
/* 420 */                     callback.onError(e);
/* 421 */                   } catch (Exception e) {
/* 422 */                     UdpBeacon.this.mCallbacks.remove(callback);
/* 423 */                     Log.w("UdpBeacon", e);
/*     */                   } 
/*     */                 } 
/*     */               }
/*     */             });
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean waitWithoutException(Object sync, long wait_time_ms) {
/* 438 */     boolean result = false;
/* 439 */     synchronized (sync) {
/*     */       try {
/* 441 */         sync.wait(wait_time_ms);
/* 442 */       } catch (InterruptedException e) {
/* 443 */         result = true;
/*     */       } 
/*     */     } 
/* 446 */     return result;
/*     */   }
/*     */   
/*     */   private final class BeaconShotTask
/*     */     implements Runnable {
/*     */     private final int shotNums;
/*     */     
/* 453 */     public BeaconShotTask(int shotNums) { this.shotNums = shotNums; }
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/* 459 */         UdpSocket socket = new UdpSocket(9999);
/* 460 */         socket.setReuseAddress(true);
/* 461 */         socket.setSoTimeout(2000);
/*     */         try {
/* 463 */           for (int i = 0; i < this.shotNums && 
/* 464 */             !UdpBeacon.this.mReleased; i++) {
/* 465 */             UdpBeacon.this.sendBeacon(socket);
/* 466 */             if (UdpBeacon.this.waitWithoutException(this, UdpBeacon.this.mBeaconIntervalsMs)) {
/*     */               break;
/*     */             }
/*     */           } 
/*     */         } finally {
/* 471 */           socket.release();
/*     */         } 
/* 473 */       } catch (SocketException e) {
/* 474 */         UdpBeacon.this.callOnError(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public UdpBeacon(@Nullable UdpBeaconCallback callback, int port, long beacon_intervals_ms, boolean receiveOnly, long rcv_min_intervals_ms)
/*     */   {
/* 482 */     this.mBeaconTask = new Runnable()
/*     */       {
/*     */         public void run() {
/* 485 */           ByteBuffer buffer = ByteBuffer.allocateDirect(256);
/*     */           try {
/* 487 */             UdpSocket socket = new UdpSocket(9999);
/* 488 */             socket.setReceiveBufferSize(256);
/* 489 */             socket.setReuseAddress(true);
/* 490 */             socket.setSoTimeout(2000);
/* 491 */             Thread rcvThread = new Thread(new ReceiverTask(socket));
/* 492 */             rcvThread.start();
/* 493 */             long next_send = SystemClock.elapsedRealtime();
/*     */             try {
/* 495 */               while (UdpBeacon.this.mIsRunning && !UdpBeacon.this.mReleased) {
/* 496 */                 if (!UdpBeacon.this.mReceiveOnly) {
/*     */                   
/* 498 */                   long t = next_send - SystemClock.elapsedRealtime();
/* 499 */                   if (!UdpBeacon.this.mReceiveOnly && t <= 0L) {
/* 500 */                     next_send = SystemClock.elapsedRealtime() + UdpBeacon.this.mBeaconIntervalsMs;
/* 501 */                     UdpBeacon.this.sendBeacon(socket); continue;
/* 502 */                   }  if (UdpBeacon.this.waitWithoutException(this, t))
/*     */                     break;  continue;
/*     */                 } 
/* 505 */                 if (UdpBeacon.this.waitWithoutException(this, 2000L)) {
/*     */                   break;
/*     */                 }
/*     */               } 
/*     */             } finally {
/* 510 */               UdpBeacon.this.mIsRunning = false;
/* 511 */               socket.release();
/*     */               try {
/* 513 */                 rcvThread.interrupt();
/* 514 */               } catch (Exception e) {
/* 515 */                 Log.w("UdpBeacon", e);
/*     */               } 
/*     */             } 
/* 518 */           } catch (Exception e) {
/* 519 */             UdpBeacon.this.callOnError(e);
/*     */           } 
/* 521 */           UdpBeacon.this.mIsRunning = false;
/* 522 */           synchronized (UdpBeacon.this.mSync) {
/* 523 */             UdpBeacon.this.mBeaconThread = null;
/*     */           }  }
/*     */       };
/*     */     if (callback != null)
/*     */       this.mCallbacks.add(callback); 
/*     */     this.mAsyncHandler = (Handler)HandlerThreadHandler.createHandler("UdpBeaconAsync");
/*     */     this.uuid = UUID.randomUUID();
/*     */     Beacon beacon = new Beacon(this.uuid, port);
/*     */     this.beaconBytes = beacon.asBytes();
/*     */     this.mBeaconIntervalsMs = beacon_intervals_ms;
/*     */     this.mReceiveOnly = receiveOnly;
/* 534 */     this.mRcvMinIntervalsMs = rcv_min_intervals_ms; } private class ReceiverTask implements Runnable { private ReceiverTask(UdpSocket udpSocket) { this.mUdpSocket = udpSocket; }
/*     */     
/*     */     private final UdpSocket mUdpSocket;
/*     */     
/*     */     public void run() {
/* 539 */       ByteBuffer buffer = ByteBuffer.allocateDirect(256);
/* 540 */       UdpSocket socket = this.mUdpSocket;
/* 541 */       long next_rcv = SystemClock.elapsedRealtime();
/* 542 */       while (UdpBeacon.this.mIsRunning && !UdpBeacon.this.mReleased) {
/* 543 */         if (UdpBeacon.this.mRcvMinIntervalsMs > 0L) {
/* 544 */           long t = next_rcv - SystemClock.elapsedRealtime();
/* 545 */           if (t > 0L && 
/* 546 */             UdpBeacon.this.waitWithoutException(this, t)) {
/*     */             break;
/*     */           }
/*     */           
/* 550 */           next_rcv = SystemClock.elapsedRealtime() + UdpBeacon.this.mRcvMinIntervalsMs;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 558 */           buffer.clear();
/* 559 */           int length = socket.receive(buffer);
/* 560 */           if (!UdpBeacon.this.mIsRunning)
/* 561 */             break;  buffer.rewind();
/* 562 */           if (length != 23 || 
/* 563 */             buffer.get() != 83 || buffer
/* 564 */             .get() != 65 || buffer
/* 565 */             .get() != 75 || buffer
/* 566 */             .get() != 73 || buffer
/* 567 */             .get() != 1) {
/*     */             continue;
/*     */           }
/* 570 */           final Beacon remote_beacon = new Beacon(buffer);
/* 571 */           if (!UdpBeacon.this.uuid.equals(remote_beacon.uuid)) {
/*     */             
/* 573 */             final String remoteAddr = socket.remote();
/* 574 */             final int remotePort = socket.remotePort();
/* 575 */             synchronized (UdpBeacon.this.mSync) {
/* 576 */               if (UdpBeacon.this.mAsyncHandler == null)
/* 577 */                 break;  UdpBeacon.this.mAsyncHandler.post(new Runnable()
/*     */                   {
/*     */                     public void run() {
/* 580 */                       for (UdpBeaconCallback callback : UdpBeacon.this.mCallbacks) {
/*     */                         try {
/* 582 */                           callback.onReceiveBeacon(remote_beacon.uuid, remoteAddr, remotePort);
/* 583 */                         } catch (Exception e) {
/* 584 */                           UdpBeacon.this.mCallbacks.remove(callback);
/* 585 */                           Log.w("UdpBeacon", e);
/*     */                         }
/*     */                       
/*     */                       } 
/*     */                     }
/*     */                   });
/*     */             } 
/*     */           } 
/* 593 */         } catch (ClosedChannelException e) {
/*     */           
/*     */           break;
/* 596 */         } catch (IOException iOException) {
/*     */         
/* 598 */         } catch (IllegalStateException e) {
/*     */           break;
/* 600 */         } catch (Exception e) {
/* 601 */           Log.w("UdpBeacon", e);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void sendBeacon(UdpSocket socket) {
/*     */     try {
/* 614 */       socket.broadcast(this.beaconBytes);
/* 615 */     } catch (IOException e) {
/* 616 */       Log.w("UdpBeacon", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static interface UdpBeaconCallback {
/*     */     void onReceiveBeacon(UUID param1UUID, String param1String, int param1Int);
/*     */     
/*     */     void onError(Exception param1Exception);
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\net\UdpBeacon.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */