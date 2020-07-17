package com.serenegiant.glutils;

import java.io.IOException;

public interface ITexture {
  void release();
  
  void bind();
  
  void unbind();
  
  int getTexTarget();
  
  int getTexture();
  
  float[] getTexMatrix();
  
  void getTexMatrix(float[] paramArrayOffloat, int paramInt);
  
  int getTexWidth();
  
  int getTexHeight();
  
  void loadTexture(String paramString) throws NullPointerException, IOException;
}


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\glutils\ITexture.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */