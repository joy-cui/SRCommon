/*     */ package com.serenegiant.net;
/*     */ 
/*     */ import android.annotation.SuppressLint;
/*     */ import android.content.BroadcastReceiver;
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.content.IntentFilter;
/*     */ import android.net.ConnectivityManager;
/*     */ import android.net.Network;
/*     */ import android.net.NetworkInfo;
/*     */ import android.os.Handler;
/*     */ import android.os.Looper;
/*     */ import android.support.v4.content.LocalBroadcastManager;
/*     */ import android.util.Log;
/*     */ import com.serenegiant.utils.BuildCheck;
/*     */ import com.serenegiant.utils.ComponentUtils;
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
/*     */ public class NetworkChangedReceiver
/*     */   extends BroadcastReceiver
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*  45 */   private static final String TAG = NetworkChangedReceiver.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String KEY_NETWORK_CHANGED_IS_CONNECTED_OR_CONNECTING = "KEY_NETWORK_CHANGED_IS_CONNECTED_OR_CONNECTING";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String KEY_NETWORK_CHANGED_IS_CONNECTED = "KEY_NETWORK_CHANGED_IS_CONNECTED";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String KEY_NETWORK_CHANGED_ACTIVE_NETWORK_MASK = "KEY_NETWORK_CHANGED_ACTIVE_NETWORK_MASK";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int NETWORK_TYPE_MOBILE = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int NETWORK_TYPE_WIFI = 2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int NETWORK_TYPE_MOBILE_MMS = 4;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int NETWORK_TYPE_MOBILE_SUPL = 8;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int NETWORK_TYPE_MOBILE_DUN = 16;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int NETWORK_TYPE_MOBILE_HIPRI = 32;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int NETWORK_TYPE_WIMAX = 64;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int NETWORK_TYPE_BLUETOOTH = 128;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int NETWORK_TYPE_ETHERNET = 512;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int NETWORK_MASK_INTERNET_WIFI = 706;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 138 */   public static void enable(Context context) { ComponentUtils.enable(context, NetworkChangedReceiver.class); }
/*     */ 
/*     */ 
/*     */   
/* 142 */   public static void disable(Context context) { ComponentUtils.disable(context, NetworkChangedReceiver.class); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 152 */   public static NetworkChangedReceiver registerGlobal(Context context) { return registerGlobal(context, null); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NetworkChangedReceiver registerGlobal(Context context, OnNetworkChangedListener listener) {
/* 163 */     NetworkChangedReceiver receiver = new NetworkChangedReceiver(listener);
/* 164 */     IntentFilter intentFilter = new IntentFilter();
/* 165 */     intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
/* 166 */     synchronized (sSync) {
/* 167 */       context.registerReceiver(receiver, intentFilter);
/* 168 */       sGlobalReceiverNum++;
/*     */     } 
/* 170 */     return receiver;
/*     */   }
/*     */   
/*     */   public static boolean isGlobalRegistered() {
/* 174 */     synchronized (sSync) {
/* 175 */       return (sGlobalReceiverNum > 0);
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
/*     */   public static void unregisterGlobal(Context context, NetworkChangedReceiver receiver) {
/* 187 */     synchronized (sSync) {
/* 188 */       if (context != null && receiver != null) {
/* 189 */         sGlobalReceiverNum--;
/*     */         try {
/* 191 */           context.unregisterReceiver(receiver);
/* 192 */         } catch (Exception exception) {}
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
/*     */   public static NetworkChangedReceiver registerLocal(Context context, OnNetworkChangedListener listener) {
/*     */     final int activeNetworkMask, isConnected, isConnectedOrConnecting;
/* 207 */     final NetworkChangedReceiver receiver = new NetworkChangedReceiver(listener);
/* 208 */     IntentFilter intentFilter = new IntentFilter();
/* 209 */     intentFilter.addAction("com.serenegiant.net.CONNECTIVITY_CHANGE");
/* 210 */     LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context.getApplicationContext());
/* 211 */     broadcastManager.registerReceiver(receiver, intentFilter);
/* 212 */     Handler handler = null;
/*     */     try {
/* 214 */       handler = new Handler(Looper.getMainLooper());
/* 215 */     } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 220 */     synchronized (sSync) {
/* 221 */       isConnectedOrConnecting = sIsConnectedOrConnecting;
/* 222 */       isConnected = sIsConnected;
/* 223 */       activeNetworkMask = sActiveNetworkMask;
/*     */     } 
/* 225 */     if (handler != null) {
/* 226 */       handler.post(new Runnable()
/*     */           {
/*     */             public void run() {
/* 229 */               receiver.callOnNetworkChanged(isConnectedOrConnecting, isConnected, activeNetworkMask);
/*     */             }
/*     */           });
/*     */     } else {
/* 233 */       receiver.callOnNetworkChanged(isConnectedOrConnecting, isConnected, activeNetworkMask);
/*     */     } 
/* 235 */     return receiver;
/*     */   }
/*     */ 
/*     */   
/*     */   public static interface OnNetworkChangedListener
/*     */   {
/*     */     void onNetworkChanged(int param1Int1, int param1Int2, int param1Int3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void unregisterLocal(Context context, NetworkChangedReceiver receiver) {
/* 246 */     LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context.getApplicationContext());
/*     */     try {
/* 248 */       broadcastManager.unregisterReceiver(receiver);
/* 249 */     } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 255 */   private static final Object sSync = new Object();
/*     */   
/*     */   private static int sGlobalReceiverNum;
/* 258 */   private static int sIsConnectedOrConnecting = 0;
/*     */   
/* 260 */   private static int sIsConnected = 0;
/*     */   
/* 262 */   private static int sActiveNetworkMask = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ACTION_GLOBAL_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ACTION_LOCAL_CONNECTIVITY_CHANGE = "com.serenegiant.net.CONNECTIVITY_CHANGE";
/*     */ 
/*     */   
/*     */   private OnNetworkChangedListener mListener;
/*     */ 
/*     */ 
/*     */   
/* 277 */   public NetworkChangedReceiver() { this.mListener = null; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 282 */   private NetworkChangedReceiver(OnNetworkChangedListener listener) { this.mListener = listener; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void unregister(Context context) {
/*     */     try {
/* 288 */       unregisterGlobal(context, this);
/* 289 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/*     */     try {
/* 293 */       unregisterLocal(context, this);
/* 294 */     } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 302 */   private static final int[] NETWORKS = new int[] { 0, 1, 1, 2, 2, 4, 3, 8, 4, 16, 5, 32, 6, 64, 7, 128, 9, 512 };
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
/*     */   public void onReceive(Context context, Intent intent) {
/* 318 */     String action = (intent != null) ? intent.getAction() : null;
/* 319 */     if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
/* 320 */       onReceiveGlobal(context, intent);
/* 321 */     } else if ("com.serenegiant.net.CONNECTIVITY_CHANGE".equals(action)) {
/* 322 */       onReceiveLocal(context, intent);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SuppressLint({"NewApi"})
/*     */   private void onReceiveGlobal(Context context, Intent intent) {
/* 333 */     ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
/* 334 */     LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context.getApplicationContext());
/*     */ 
/*     */ 
/*     */     
/* 338 */     int isConnectedOrConnecting = 0;
/* 339 */     int isConnected = 0;
/*     */     
/* 341 */     if (BuildCheck.isAndroid5()) {
/* 342 */       Network[] networks = connectivityManager.getAllNetworks();
/* 343 */       if (networks != null) {
/* 344 */         for (Network network : networks) {
/* 345 */           NetworkInfo info = connectivityManager.getNetworkInfo(network);
/* 346 */           if (info != null) {
/* 347 */             isConnectedOrConnecting |= info.isConnectedOrConnecting() ? (1 << info.getType()) : 0;
/* 348 */             isConnected |= info.isConnected() ? (1 << info.getType()) : 0;
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } else {
/* 353 */       int n = NETWORKS.length;
/* 354 */       for (int i = 0; i < n; i += 2) {
/* 355 */         NetworkInfo info = connectivityManager.getNetworkInfo(NETWORKS[i]);
/* 356 */         if (info != null) {
/* 357 */           isConnectedOrConnecting |= info.isConnectedOrConnecting() ? NETWORKS[i + 1] : 0;
/* 358 */           isConnected |= info.isConnected() ? NETWORKS[i + 1] : 0;
/*     */         } 
/*     */       } 
/*     */     } 
/* 362 */     NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
/* 363 */     int activeNetworkMask = (activeNetworkInfo != null) ? (1 << activeNetworkInfo.getType()) : 0;
/* 364 */     synchronized (sSync) {
/* 365 */       sIsConnectedOrConnecting = isConnectedOrConnecting;
/* 366 */       sIsConnected = isConnected;
/* 367 */       sActiveNetworkMask = activeNetworkMask;
/* 368 */       sSync.notifyAll();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 373 */     callOnNetworkChanged(isConnectedOrConnecting, isConnected, activeNetworkMask);
/*     */     
/* 375 */     Intent networkChangedIntent = new Intent("com.serenegiant.net.CONNECTIVITY_CHANGE");
/* 376 */     networkChangedIntent.putExtra("KEY_NETWORK_CHANGED_IS_CONNECTED_OR_CONNECTING", isConnectedOrConnecting);
/* 377 */     networkChangedIntent.putExtra("KEY_NETWORK_CHANGED_IS_CONNECTED", isConnected);
/* 378 */     networkChangedIntent.putExtra("KEY_NETWORK_CHANGED_ACTIVE_NETWORK_MASK", activeNetworkMask);
/* 379 */     broadcastManager.sendBroadcast(networkChangedIntent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void onReceiveLocal(Context context, Intent intent) {
/* 387 */     int isConnectedOrConnecting = intent.getIntExtra("KEY_NETWORK_CHANGED_IS_CONNECTED_OR_CONNECTING", 0);
/* 388 */     int isConnected = intent.getIntExtra("KEY_NETWORK_CHANGED_IS_CONNECTED", 0);
/* 389 */     int activeNetworkInfo = intent.getIntExtra("KEY_NETWORK_CHANGED_ACTIVE_NETWORK_MASK", 0);
/* 390 */     callOnNetworkChanged(isConnectedOrConnecting, isConnected, activeNetworkInfo);
/*     */   }
/*     */   
/*     */   private void callOnNetworkChanged(int isConnectedOrConnecting, int isConnected, int activeNetworkInfo) {
/* 394 */     if (this.mListener != null) {
/*     */       try {
/* 396 */         this.mListener.onNetworkChanged(isConnectedOrConnecting, isConnected, activeNetworkInfo);
/* 397 */       } catch (Exception e) {
/* 398 */         Log.w(TAG, e);
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
/*     */   public static boolean isWifiNetworkReachable() {
/*     */     int isConnectedOrConnecting;
/* 414 */     synchronized (sSync) {
/* 415 */       isConnectedOrConnecting = sIsConnectedOrConnecting & sActiveNetworkMask;
/*     */     } 
/* 417 */     return ((isConnectedOrConnecting & 0x2C2) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isWifiNetworkReachable(Context context) {
/* 427 */     ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService("connectivity");
/* 428 */     NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
/* 429 */     if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) {
/* 430 */       int type = activeNetworkInfo.getType();
/* 431 */       return (type == 1 || type == 6 || type == 7 || type == 9);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 436 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isMobileNetworkReachable() {
/*     */     int isConnectedOrConnecting;
/* 446 */     synchronized (sSync) {
/* 447 */       isConnectedOrConnecting = sIsConnectedOrConnecting & sActiveNetworkMask;
/*     */     } 
/* 449 */     return ((isConnectedOrConnecting & 0x1) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isMobileNetworkReachable(Context context) {
/* 459 */     ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService("connectivity");
/* 460 */     NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
/* 461 */     if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) {
/* 462 */       int type = activeNetworkInfo.getType();
/* 463 */       return (type == 0);
/*     */     } 
/* 465 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNetworkReachable() {
/*     */     int isConnectedOrConnecting;
/* 475 */     synchronized (sSync) {
/* 476 */       isConnectedOrConnecting = sIsConnectedOrConnecting & sActiveNetworkMask;
/*     */     } 
/* 478 */     return (isConnectedOrConnecting != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNetworkReachable(Context context) {
/* 486 */     ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService("connectivity");
/* 487 */     NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
/* 488 */     return (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting());
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\net\NetworkChangedReceiver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */