package com.serenegiant.mediaeffect;

import com.serenegiant.glutils.TextureOffscreen;

public interface ISource {
  ISource reset();
  
  ISource resize(int paramInt1, int paramInt2);
  
  ISource apply(IEffect paramIEffect);
  
  int getWidth();
  
  int getHeight();
  
  int[] getSourceTexId();
  
  int getOutputTexId();
  
  float[] getTexMatrix();
  
  TextureOffscreen getOutputTexture();
  
  void release();
}


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\mediaeffect\ISource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */