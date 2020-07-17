package com.serenegiant.glutils;

public interface IDrawer2D {
  void release();
  
  float[] getMvpMatrix();
  
  IDrawer2D setMvpMatrix(float[] paramArrayOffloat, int paramInt);
  
  void getMvpMatrix(float[] paramArrayOffloat, int paramInt);
  
  void draw(int paramInt1, float[] paramArrayOffloat, int paramInt2);
  
  void draw(ITexture paramITexture);
  
  void draw(TextureOffscreen paramTextureOffscreen);
}


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\glutils\IDrawer2D.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */