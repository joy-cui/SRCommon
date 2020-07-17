package com.serenegiant.glutils;

import android.view.Surface;

public interface RenderHolderCallback {
  void onCreate(Surface paramSurface);
  
  void onFrameAvailable();
  
  void onDestroy();
}


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\glutils\RenderHolderCallback.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */