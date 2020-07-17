package com.serenegiant.glutils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface IRendererCommon {
  public static final int MIRROR_NORMAL = 0;
  
  public static final int MIRROR_HORIZONTAL = 1;
  
  public static final int MIRROR_VERTICAL = 2;
  
  public static final int MIRROR_BOTH = 3;
  
  public static final int MIRROR_NUM = 4;
  
  void setMirror(int paramInt);
  
  int getMirror();
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface MirrorMode {}
}


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\glutils\IRendererCommon.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */