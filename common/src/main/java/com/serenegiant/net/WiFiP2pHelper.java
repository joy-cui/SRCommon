/*     */ package com.serenegiant.net;
/*     */ 
/*     */ import android.content.BroadcastReceiver;
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.content.IntentFilter;
/*     */ import android.net.NetworkInfo;
/*     */ import android.net.wifi.p2p.WifiP2pConfig;
/*     */ import android.net.wifi.p2p.WifiP2pDevice;
/*     */ import android.net.wifi.p2p.WifiP2pDeviceList;
/*     */ import android.net.wifi.p2p.WifiP2pInfo;
/*     */ import android.net.wifi.p2p.WifiP2pManager;
/*     */ import android.support.annotation.NonNull;
/*     */ import android.util.Log;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WiFiP2pHelper
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*  58 */   private static final String TAG = WiFiP2pHelper.class.getSimpleName();
/*     */ 
/*     */ 
/*     */   
/*     */   private static WiFiP2pHelper sWiFiP2PHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized WiFiP2pHelper getInstance(@NonNull Context context) {
/*  68 */     if (sWiFiP2PHelper == null || sWiFiP2PHelper.mWeakContext.get() == null) {
/*  69 */       sWiFiP2PHelper = new WiFiP2pHelper(context);
/*     */     }
/*  71 */     return sWiFiP2PHelper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   public static synchronized void release() { sWiFiP2PHelper = null; }
/*     */ 
/*     */ 
/*     */   
/*  82 */   private final Set<WiFiP2pListener> mListeners = new CopyOnWriteArraySet<>();
/*     */   
/*  84 */   private final List<WifiP2pDevice> mAvailableDevices = new ArrayList<>();
/*     */   
/*     */   private final WeakReference<Context> mWeakContext;
/*     */   
/*     */   private final WifiP2pManager mWifiP2pManager;
/*     */   
/*     */   private WifiP2pManager.Channel mChannel;
/*     */   
/*     */   private WiFiDirectBroadcastReceiver mReceiver;
/*     */   
/*     */   private boolean mIsWifiP2pEnabled;
/*     */   
/*     */   private WifiP2pDevice mWifiP2pDevice;
/*     */   
/*     */   private int mRetryCount;
/*     */   
/*     */   private final WifiP2pManager.ChannelListener mChannelListener;
/*     */   
/*     */   private final WifiP2pManager.PeerListListener mPeerListListener;
/*     */   
/*     */   private final WifiP2pManager.ConnectionInfoListener mConnectionInfoListener;
/*     */   
/*     */   public synchronized void register() {
/* 107 */     Context context = this.mWeakContext.get();
/* 108 */     if ((((context != null) ? 1 : 0) & ((this.mReceiver == null) ? 1 : 0)) != 0) {
/* 109 */       this.mChannel = this.mWifiP2pManager.initialize(context, context
/* 110 */           .getMainLooper(), this.mChannelListener);
/* 111 */       this.mReceiver = new WiFiDirectBroadcastReceiver(this.mWifiP2pManager, this.mChannel, this);
/* 112 */       IntentFilter intentFilter = new IntentFilter();
/* 113 */       intentFilter.addAction("android.net.wifi.p2p.STATE_CHANGED");
/* 114 */       intentFilter.addAction("android.net.wifi.p2p.PEERS_CHANGED");
/* 115 */       intentFilter.addAction("android.net.wifi.p2p.CONNECTION_STATE_CHANGE");
/* 116 */       intentFilter.addAction("android.net.wifi.p2p.THIS_DEVICE_CHANGED");
/* 117 */       context.registerReceiver(this.mReceiver, intentFilter);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void unregister() {
/* 126 */     this.mIsWifiP2pEnabled = false;
/* 127 */     this.mRetryCount = 0;
/* 128 */     internalDisconnect(null);
/* 129 */     if (this.mReceiver != null) {
/* 130 */       Context context = this.mWeakContext.get();
/*     */       try {
/* 132 */         context.unregisterReceiver(this.mReceiver);
/* 133 */       } catch (Exception e) {
/* 134 */         Log.w(TAG, e);
/*     */       } 
/* 136 */       this.mReceiver = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 145 */   public void add(@NonNull WiFiP2pListener listener) { this.mListeners.add(listener); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 153 */   public void remove(@NonNull WiFiP2pListener listener) { this.mListeners.remove(listener); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void startDiscovery() throws IllegalStateException {
/* 162 */     if (this.mChannel != null) {
/* 163 */       this.mWifiP2pManager.discoverPeers(this.mChannel, new WifiP2pManager.ActionListener() {
/*     */             public void onSuccess() {}
/*     */ 
/*     */ 
/*     */             
/*     */             public void onFailure(int reason) {
/* 169 */               WiFiP2pHelper.this.callOnError(new RuntimeException("failed to start discovery, reason=" + reason));
/*     */             }
/*     */           });
/*     */     } else {
/* 173 */       throw new IllegalStateException("not registered");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void connect(@NonNull String remoteMacAddress) {
/* 183 */     WifiP2pConfig config = new WifiP2pConfig();
/* 184 */     config.deviceAddress = remoteMacAddress;
/* 185 */     config.wps.setup = 0;
/* 186 */     connect(config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void connect(@NonNull WifiP2pDevice device) {
/* 195 */     WifiP2pConfig config = new WifiP2pConfig();
/* 196 */     config.deviceAddress = device.deviceAddress;
/* 197 */     config.wps.setup = 0;
/* 198 */     connect(config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void connect(@NonNull WifiP2pConfig config) throws IllegalStateException {
/* 208 */     if (this.mChannel != null) {
/* 209 */       this.mWifiP2pManager.connect(this.mChannel, config, new WifiP2pManager.ActionListener() {
/*     */             public void onSuccess() {}
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             public void onFailure(int reason) {
/* 216 */               WiFiP2pHelper.this.callOnError(new RuntimeException("failed to connect, reason=" + reason));
/*     */             }
/*     */           });
/*     */     } else {
/* 220 */       throw new IllegalStateException("not registered");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void internalDisconnect(WifiP2pManager.ActionListener listener) {
/* 229 */     if (this.mWifiP2pManager != null) {
/* 230 */       if (this.mWifiP2pDevice == null || this.mWifiP2pDevice.status == 0) {
/*     */ 
/*     */         
/* 233 */         if (this.mChannel != null) {
/* 234 */           this.mWifiP2pManager.removeGroup(this.mChannel, listener);
/*     */         }
/* 236 */       } else if (this.mWifiP2pDevice.status == 3 || this.mWifiP2pDevice.status == 1) {
/*     */ 
/*     */ 
/*     */         
/* 240 */         this.mWifiP2pManager.cancelConnect(this.mChannel, listener);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void disconnect() {
/* 250 */     internalDisconnect(new WifiP2pManager.ActionListener()
/*     */         {
/*     */           public void onSuccess() {}
/*     */ 
/*     */ 
/*     */           
/* 256 */           public void onFailure(int reason) { WiFiP2pHelper.this.callOnError(new RuntimeException("failed to disconnect, reason=" + reason)); }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 266 */   public synchronized boolean isWiFiP2pEnabled() { return (this.mChannel != null && this.mIsWifiP2pEnabled); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized void setIsWifiP2pEnabled(boolean enabled) {
/* 274 */     this.mIsWifiP2pEnabled = enabled;
/* 275 */     callOnStateChanged(enabled);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized void resetData() {
/* 283 */     if (isConnectedOrConnecting()) {
/* 284 */       callOnDisconnect();
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
/* 297 */   private synchronized void updateDevice(WifiP2pDevice device) { this.mWifiP2pDevice = device; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 305 */   public synchronized boolean isConnected() { return (this.mWifiP2pDevice != null && this.mWifiP2pDevice.status == 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 313 */   public synchronized boolean isConnectedOrConnecting() { return (this.mWifiP2pDevice != null && (this.mWifiP2pDevice.status == 0 || this.mWifiP2pDevice.status == 1)); }
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
/*     */   protected void callOnStateChanged(boolean enabled) {
/* 327 */     for (WiFiP2pListener listener : this.mListeners) {
/*     */       try {
/* 329 */         listener.onStateChanged(enabled);
/* 330 */       } catch (Exception e1) {
/* 331 */         Log.w(TAG, e1);
/* 332 */         this.mListeners.remove(listener);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void callOnUpdateDevices(@NonNull List<WifiP2pDevice> devices) {
/* 343 */     for (WiFiP2pListener listener : this.mListeners) {
/*     */       try {
/* 345 */         listener.onUpdateDevices(devices);
/* 346 */       } catch (Exception e1) {
/* 347 */         Log.w(TAG, e1);
/* 348 */         this.mListeners.remove(listener);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void callOnConnect(@NonNull WifiP2pInfo info) {
/* 359 */     for (WiFiP2pListener listener : this.mListeners) {
/*     */       try {
/* 361 */         listener.onConnect(info);
/* 362 */       } catch (Exception e1) {
/* 363 */         Log.w(TAG, e1);
/* 364 */         this.mListeners.remove(listener);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void callOnDisconnect() {
/* 374 */     for (WiFiP2pListener listener : this.mListeners) {
/*     */       try {
/* 376 */         listener.onDisconnect();
/* 377 */       } catch (Exception e1) {
/* 378 */         Log.w(TAG, e1);
/* 379 */         this.mListeners.remove(listener);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void callOnError(Exception e) {
/* 390 */     for (WiFiP2pListener listener : this.mListeners) {
/*     */       try {
/* 392 */         listener.onError(e);
/* 393 */       } catch (Exception e1) {
/* 394 */         Log.w(TAG, e1);
/* 395 */         this.mListeners.remove(listener);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private WiFiP2pHelper(@NonNull Context context) {
/* 403 */     this.mChannelListener = new WifiP2pManager.ChannelListener()
/*     */       {
/*     */         
/*     */         public void onChannelDisconnected()
/*     */         {
/* 408 */           WiFiP2pHelper.this.setIsWifiP2pEnabled(false);
/* 409 */           WiFiP2pHelper.this.resetData();
/* 410 */           synchronized (WiFiP2pHelper.this) {
/* 411 */             WiFiP2pHelper.this.mChannel = null;
/*     */           } 
/* 413 */           if (WiFiP2pHelper.this.mRetryCount == 0) {
/*     */             
/* 415 */             WiFiP2pHelper.this.mRetryCount++;
/* 416 */             Context context = WiFiP2pHelper.this.mWeakContext.get();
/* 417 */             if ((((context != null) ? 1 : 0) & ((WiFiP2pHelper.this.mReceiver == null) ? 1 : 0)) != 0)
/*     */             {
/*     */               
/* 420 */               WiFiP2pHelper.this.mChannel = WiFiP2pHelper.this.mWifiP2pManager.initialize(context, context
/* 421 */                   .getMainLooper(), WiFiP2pHelper.this.mChannelListener);
/*     */             }
/*     */           } 
/*     */         }
/*     */       };
/*     */ 
/*     */     
/* 428 */     this.mPeerListListener = new WifiP2pManager.PeerListListener()
/*     */       {
/*     */         
/*     */         public void onPeersAvailable(WifiP2pDeviceList peers)
/*     */         {
/* 433 */           Collection<WifiP2pDevice> devices = peers.getDeviceList();
/* 434 */           synchronized (WiFiP2pHelper.this.mAvailableDevices) {
/* 435 */             WiFiP2pHelper.this.mAvailableDevices.clear();
/* 436 */             WiFiP2pHelper.this.mAvailableDevices.addAll(devices);
/*     */           } 
/* 438 */           WiFiP2pHelper.this.callOnUpdateDevices(WiFiP2pHelper.this.mAvailableDevices);
/*     */         }
/*     */       };
/*     */ 
/*     */     
/* 443 */     this.mConnectionInfoListener = new WifiP2pManager.ConnectionInfoListener()
/*     */       {
/*     */         
/*     */         public void onConnectionInfoAvailable(WifiP2pInfo info)
/*     */         {
/* 448 */           if (info != null) {
/* 449 */             WiFiP2pHelper.this.callOnConnect(info);
/*     */           }
/*     */         }
/*     */       };
/*     */     this.mWeakContext = new WeakReference<>(context);
/*     */     this.mWifiP2pManager = (WifiP2pManager)context.getSystemService("wifip2p");
/*     */   }
/*     */ 
/*     */   
/*     */   private static class WiFiDirectBroadcastReceiver
/*     */     extends BroadcastReceiver
/*     */   {
/*     */     @NonNull
/*     */     private WifiP2pManager mManager;
/*     */     
/*     */     @NonNull
/*     */     private WifiP2pManager.Channel mChannel;
/*     */     
/*     */     @NonNull
/*     */     private WiFiP2pHelper mParent;
/*     */     
/*     */     public WiFiDirectBroadcastReceiver(@NonNull WifiP2pManager manager, @NonNull WifiP2pManager.Channel channel, @NonNull WiFiP2pHelper parent) {
/* 471 */       this.mManager = manager;
/* 472 */       this.mChannel = channel;
/* 473 */       this.mParent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onReceive(Context context, Intent intent) {
/* 478 */       String action = (intent != null) ? intent.getAction() : null;
/* 479 */       if ("android.net.wifi.p2p.STATE_CHANGED".equals(action)) {
/*     */ 
/*     */         
/*     */         try {
/* 483 */           int state = intent.getIntExtra("wifi_p2p_state", -1);
/* 484 */           if (state == 2) {
/*     */             
/* 486 */             this.mParent.setIsWifiP2pEnabled(true);
/*     */           } else {
/*     */             
/* 489 */             this.mParent.setIsWifiP2pEnabled(false);
/* 490 */             this.mParent.resetData();
/*     */           }
/*     */         
/* 493 */         } catch (Exception e) {
/* 494 */           this.mParent.callOnError(e);
/*     */         } 
/* 496 */       } else if ("android.net.wifi.p2p.PEERS_CHANGED".equals(action)) {
/*     */ 
/*     */         
/*     */         try {
/*     */ 
/*     */ 
/*     */           
/* 503 */           this.mManager.requestPeers(this.mChannel, this.mParent.mPeerListListener);
/* 504 */         } catch (Exception e) {
/* 505 */           this.mParent.callOnError(e);
/*     */         }
/*     */       
/* 508 */       } else if ("android.net.wifi.p2p.CONNECTION_STATE_CHANGE".equals(action)) {
/*     */         
/*     */         try {
/* 511 */           NetworkInfo networkInfo = (NetworkInfo)intent.getParcelableExtra("networkInfo");
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 516 */           if (networkInfo.isConnected()) {
/*     */ 
/*     */             
/* 519 */             this.mManager.requestConnectionInfo(this.mChannel, this.mParent.mConnectionInfoListener);
/*     */           } else {
/*     */             
/* 522 */             this.mParent.resetData();
/*     */           } 
/* 524 */         } catch (Exception e) {
/* 525 */           this.mParent.callOnError(e);
/*     */         } 
/* 527 */       } else if ("android.net.wifi.p2p.THIS_DEVICE_CHANGED".equals(action)) {
/*     */ 
/*     */         
/*     */         try {
/* 531 */           WifiP2pDevice device = (WifiP2pDevice)intent.getParcelableExtra("wifiP2pDevice");
/*     */           
/* 533 */           this.mParent.updateDevice(device);
/* 534 */         } catch (Exception e) {
/* 535 */           this.mParent.callOnError(e);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\net\WiFiP2pHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */