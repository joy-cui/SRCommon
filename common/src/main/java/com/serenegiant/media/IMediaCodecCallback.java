package com.serenegiant.media;

public interface IMediaCodecCallback {
  void onPrepared(IMediaCodec paramIMediaCodec);
  
  void onStart(IMediaCodec paramIMediaCodec);
  
  boolean onFrameAvailable(IMediaCodec paramIMediaCodec, long paramLong);
  
  void onStop(IMediaCodec paramIMediaCodec);
  
  void onRelease(IMediaCodec paramIMediaCodec);
  
  boolean onError(IMediaCodec paramIMediaCodec, Exception paramException);
}


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\IMediaCodecCallback.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */