package com.serenegiant.net;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.support.annotation.NonNull;
import java.util.List;

public interface WiFiP2pListener {
  void onStateChanged(boolean paramBoolean);
  
  void onUpdateDevices(@NonNull List<WifiP2pDevice> paramList);
  
  void onConnect(@NonNull WifiP2pInfo paramWifiP2pInfo);
  
  void onDisconnect();
  
  void onError(Exception paramException);
}


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\net\WiFiP2pListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */