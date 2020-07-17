package com.serenegiant.media;

import android.media.MediaCodec;
import android.media.MediaFormat;
import java.nio.ByteBuffer;

public interface IMuxer {
  int addTrack(MediaFormat paramMediaFormat);
  
  void writeSampleData(int paramInt, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
  
  void start();
  
  void stop();
  
  void release();
  
  boolean isStarted();
}


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\IMuxer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */