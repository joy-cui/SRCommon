package com.serenegiant.media;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.view.Surface;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;

public interface IRecorder {
  public static final int STATE_UNINITIALIZED = 0;
  
  public static final int STATE_INITIALIZED = 1;
  
  public static final int STATE_PREPARED = 2;
  
  public static final int STATE_STARTING = 3;
  
  public static final int STATE_STARTED = 4;
  
  public static final int STATE_STOPPING = 5;
  
  void setMuxer(IMuxer paramIMuxer);
  
  void prepare();
  
  void startRecording() throws IllegalStateException;
  
  void stopRecording();
  
  Surface getInputSurface();
  
  Encoder getVideoEncoder();
  
  Encoder getAudioEncoder();
  
  boolean isStarted();
  
  boolean isReady();
  
  boolean isStopping();
  
  boolean isStopped();
  
  int getState();
  
  IMuxer getMuxer();
  
  String getOutputPath();
  
  void frameAvailableSoon();
  
  void release();
  
  void addEncoder(Encoder paramEncoder);
  
  void removeEncoder(Encoder paramEncoder);
  
  boolean start(Encoder paramEncoder);
  
  void stop(Encoder paramEncoder);
  
  int addTrack(Encoder paramEncoder, MediaFormat paramMediaFormat);
  
  void writeSampleData(int paramInt, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface RecorderState {}
  
  public static interface RecorderCallback {
    void onPrepared(IRecorder param1IRecorder);
    
    void onStarted(IRecorder param1IRecorder);
    
    void onStopped(IRecorder param1IRecorder);
    
    void onError(Exception param1Exception);
  }
}


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\media\IRecorder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */