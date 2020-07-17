package com.serenegiant.media;

public interface IFrameCallback {
  void onPrepared();
  
  void onFinished();
  
  boolean onFrameAvailable(long paramLong);
}


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\IFrameCallback.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */