package com.serenegiant.widget;

public interface IScaledView {
  public static final int SCALE_MODE_KEEP_ASPECT = 0;
  
  public static final int SCALE_MODE_STRETCH_TO_FIT = 1;
  
  public static final int SCALE_MODE_CROP = 2;
  
  void setScaleMode(int paramInt);
  
  int getScaleMode();
}


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\widget\IScaledView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */