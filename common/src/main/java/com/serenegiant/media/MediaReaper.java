//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.serenegiant.media;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaCodec.BufferInfo;
import android.os.Process;
import android.support.annotation.NonNull;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@TargetApi(16)
public abstract class MediaReaper implements Runnable {
    private static final boolean DEBUG = false;
    private static final String TAG = MediaReaper.class.getSimpleName();
    public static final int REAPER_VIDEO = 0;
    public static final int REAPER_AUDIO = 1;
    public static final int TIMEOUT_USEC = 10000;
    protected final Object mSync = new Object();
    private final WeakReference<MediaCodec> mWeakEncoder;
    private final ReaperListener mListener;
    private final int mReaperType;
    private BufferInfo mBufferInfo;
    private volatile boolean mIsRunning;
    private volatile boolean mRecorderStarted;
    private boolean mRequestStop;
    private int mRequestDrain;
    private volatile boolean mIsEOS;
    private long prevOutputPTSUs = -1L;

    public MediaReaper(int trackIndex, MediaCodec encoder, @NonNull MediaReaper.ReaperListener listener) {
        this.mWeakEncoder = new WeakReference(encoder);
        this.mListener = listener;
        this.mReaperType = trackIndex;
        this.mBufferInfo = new BufferInfo();
        synchronized(this.mSync) {
            (new Thread(this, this.getClass().getSimpleName())).start();

            try {
                this.mSync.wait();
            } catch (InterruptedException var7) {
            }

        }
    }

    public void release() {
        this.mRequestStop = true;
        this.mIsRunning = false;
        MediaCodec encoder = (MediaCodec)this.mWeakEncoder.get();
        if (encoder != null) {
            try {
                encoder.release();
            } catch (Exception var5) {
                Log.w(TAG, var5);
            }
        }

        synchronized(this.mSync) {
            this.mSync.notifyAll();
        }
    }

    public void frameAvailableSoon() {
        synchronized(this.mSync) {
            if (this.mIsRunning && !this.mRequestStop) {
                ++this.mRequestDrain;
                this.mSync.notifyAll();
            }
        }
    }

    public void run() {
        Process.setThreadPriority(-4);
        synchronized(this.mSync) {
            this.mIsRunning = true;
            this.mRequestStop = false;
            this.mRequestDrain = 0;
            this.mSync.notify();
        }

        while(true) {
            boolean localRequestStop;
            boolean localRequestDrain;
            synchronized(this.mSync) {
                localRequestStop = this.mRequestStop;
                localRequestDrain = this.mRequestDrain > 0;
                if (localRequestDrain) {
                    --this.mRequestDrain;
                }
            }

            try {
                if (localRequestStop) {
                    this.drain();
                    this.mIsEOS = true;
                    this.release();
                    break;
                }

                if (localRequestDrain) {
                    this.drain();
                } else {
                    synchronized(this.mSync) {
                        try {
                            this.mSync.wait(50L);
                        } catch (InterruptedException var10) {
                            break;
                        }
                    }
                }
            } catch (IllegalStateException var12) {
                break;
            } catch (Exception var13) {
                Log.w(TAG, var13);
            }
        }

        synchronized(this.mSync) {
            this.mRequestStop = true;
            this.mIsRunning = false;
        }
    }

    private final void drain() {
        MediaCodec encoder = (MediaCodec)this.mWeakEncoder.get();
        if (encoder != null) {
            ByteBuffer[] encoderOutputBuffers;
            try {
                encoderOutputBuffers = encoder.getOutputBuffers();
            } catch (IllegalStateException var12) {
                return;
            }

            int count = 0;

            while(this.mIsRunning) {
                int encoderStatus = encoder.dequeueOutputBuffer(this.mBufferInfo, 10000L);
                if (encoderStatus == -1) {
                    if (!this.mIsEOS) {
                        ++count;
                        if (count > 5) {
                            break;
                        }
                    }
                } else if (encoderStatus == -3) {
                    encoderOutputBuffers = encoder.getOutputBuffers();
                } else if (encoderStatus == -2) {
                    if (this.mRecorderStarted) {
                        throw new RuntimeException("format changed twice");
                    }

                    MediaFormat format = encoder.getOutputFormat();
                    if (!this.callOnFormatChanged(format)) {
                        break;
                    }
                } else if (encoderStatus >= 0) {
                    ByteBuffer encodedData = encoderOutputBuffers[encoderStatus];
                    if (encodedData == null) {
                        throw new RuntimeException("encoderOutputBuffer " + encoderStatus + " was null");
                    }

                    if ((this.mBufferInfo.flags & 2) != 0) {
                        if (!this.mRecorderStarted) {
                            byte[] tmp = new byte[this.mBufferInfo.size];
                            encodedData.position(0);
                            encodedData.get(tmp, this.mBufferInfo.offset, this.mBufferInfo.size);
                            encodedData.position(0);
                            int ix0 = MediaCodecHelper.findStartMarker(tmp, 0);
                            int ix1 = MediaCodecHelper.findStartMarker(tmp, ix0 + 1);
                            MediaFormat outFormat = this.createOutputFormat(tmp, this.mBufferInfo.size, ix0, ix1);
                            if (!this.callOnFormatChanged(outFormat)) {
                                break;
                            }
                        }

                        this.mBufferInfo.size = 0;
                    }

                    if (this.mBufferInfo.size != 0) {
                        count = 0;
                        if (!this.mRecorderStarted) {
                            throw new RuntimeException("drain:muxer hasn't started");
                        }

                        try {
                            this.mBufferInfo.presentationTimeUs = this.getNextOutputPTSUs(this.mBufferInfo.presentationTimeUs);
                            this.mListener.writeSampleData(this.mReaperType, encodedData, this.mBufferInfo);
                        } catch (TimeoutException var10) {
                            this.callOnError(var10);
                        } catch (Exception var11) {
                            this.callOnError(var11);
                        }
                    }

                    encoder.releaseOutputBuffer(encoderStatus, false);
                    if ((this.mBufferInfo.flags & 4) != 0) {
                        this.callOnStop();
                        break;
                    }
                }
            }

        }
    }

    protected abstract MediaFormat createOutputFormat(byte[] var1, int var2, int var3, int var4);

    private boolean callOnFormatChanged(MediaFormat format) {
        try {
            this.mListener.onOutputFormatChanged(format);
            this.mRecorderStarted = true;
            return true;
        } catch (Exception var3) {
            this.callOnError(var3);
            return false;
        }
    }

    private void callOnStop() {
        try {
            this.mListener.onStop();
        } catch (Exception var2) {
            this.callOnError(var2);
        }

    }

    private void callOnError(Exception e) {
        try {
            this.mListener.onError(e);
        } catch (Exception var3) {
            Log.w(TAG, var3);
        }

    }

    protected long getNextOutputPTSUs(long presentationTimeUs) {
        if (presentationTimeUs <= this.prevOutputPTSUs) {
            presentationTimeUs = this.prevOutputPTSUs + 9643L;
        }

        this.prevOutputPTSUs = presentationTimeUs;
        return presentationTimeUs;
    }

    public static class VideoReaper extends MediaReaper {
        public static final String MIME_AVC = "video/avc";
        private final int mWidth;
        private final int mHeight;

        public VideoReaper(MediaCodec encoder, @NonNull MediaReaper.ReaperListener listener, int width, int height) {
            super(0, encoder, listener);
            this.mWidth = width;
            this.mHeight = height;
        }

        protected MediaFormat createOutputFormat(byte[] csd, int size, int ix0, int ix1) {
            if (ix0 >= 0) {
                MediaFormat outFormat = MediaFormat.createVideoFormat("video/avc", this.mWidth, this.mHeight);
                ByteBuffer csd0 = ByteBuffer.allocateDirect(ix1 - ix0).order(ByteOrder.nativeOrder());
                csd0.put(csd, ix0, ix1 - ix0);
                csd0.flip();
                outFormat.setByteBuffer("csd-0", csd0);
                if (ix1 > ix0) {
                    ByteBuffer csd1 = ByteBuffer.allocateDirect(size - ix1 + ix0).order(ByteOrder.nativeOrder());
                    csd1.put(csd, ix1, size - ix1 + ix0);
                    csd1.flip();
                    outFormat.setByteBuffer("csd-1", csd1);
                }

                return outFormat;
            } else {
                throw new RuntimeException("unexpected csd data came.");
            }
        }
    }

    public interface ReaperListener {
        void writeSampleData(int var1, ByteBuffer var2, BufferInfo var3);

        void onOutputFormatChanged(MediaFormat var1);

        void onStop();

        void onError(Exception var1);
    }
}
