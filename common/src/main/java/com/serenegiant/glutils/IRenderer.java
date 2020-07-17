package com.serenegiant.glutils;

import android.graphics.SurfaceTexture;
import android.view.Surface;

public interface IRenderer extends IRendererCommon {
  void release();
  
  void setSurface(Surface paramSurface);
  
  void setSurface(SurfaceTexture paramSurfaceTexture);
  
  void resize(int paramInt1, int paramInt2);
  
  void requestRender(Object... paramVarArgs);
}


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\glutils\IRenderer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */