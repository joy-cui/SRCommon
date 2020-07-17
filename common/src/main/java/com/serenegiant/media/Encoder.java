package com.serenegiant.media;

import java.nio.ByteBuffer;

public interface Encoder {
  void prepare() throws Exception;
  
  void start();
  
  void stop();
  
  void release();
  
  void signalEndOfInputStream();
  
  void encode(ByteBuffer paramByteBuffer);
  
  void encode(ByteBuffer paramByteBuffer, int paramInt, long paramLong);
  
  void frameAvailableSoon();
  
  boolean isCapturing();
  
  String getOutputPath();
  
  boolean isAudio();
}


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\Encoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */