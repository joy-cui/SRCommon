package com.serenegiant.glutils;

import android.graphics.SurfaceTexture;
import android.view.Surface;

public interface IRendererHolder extends IRendererCommon {
  boolean isRunning();
  
  void release();
  
  Surface getSurface();
  
  SurfaceTexture getSurfaceTexture();
  
  void reset();
  
  void resize(int paramInt1, int paramInt2);
  
  void addSurface(int paramInt, Object paramObject, boolean paramBoolean);
  
  void addSurface(int paramInt1, Object paramObject, boolean paramBoolean, int paramInt2);
  
  void removeSurface(int paramInt);
  
  boolean isEnabled(int paramInt);
  
  void setEnabled(int paramInt, boolean paramBoolean);
  
  void requestFrame();
  
  int getCount();
  
  void captureStillAsync(String paramString);
  
  void captureStill(String paramString);
}


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\glutils\IRendererHolder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */