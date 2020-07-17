package com.serenegiant.media;

import android.view.Surface;

public interface EncoderListener {
  void onStartEncode(Encoder paramEncoder, Surface paramSurface, int paramInt, boolean paramBoolean);
  
  void onStopEncode(Encoder paramEncoder);
  
  void onDestroy(Encoder paramEncoder);
  
  void onError(Exception paramException);
}


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\EncoderListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */