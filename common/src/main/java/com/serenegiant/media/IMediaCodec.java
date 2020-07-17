package com.serenegiant.media;

import java.io.IOException;

public interface IMediaCodec {
  public static final int TIMEOUT_USEC = 10000;
  
  void prepare() throws IOException;
  
  void start();
  
  void stop();
  
  void release();
  
  boolean isPrepared();
  
  boolean isRunning();
}


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\IMediaCodec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */