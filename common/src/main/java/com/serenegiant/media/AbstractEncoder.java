//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.serenegiant.media;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecInfo.CodecProfileLevel;
import android.os.Process;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Surface;
import com.serenegiant.utils.MediaInfo;
import java.nio.ByteBuffer;
import java.util.List;

@TargetApi(16)
public abstract class AbstractEncoder implements Encoder, Runnable {
    private static final String TAG = AbstractEncoder.class.getSimpleName();
    public static final int TIMEOUT_USEC = 10000;
    private volatile int mRequestDrain;
    protected volatile boolean mIsCapturing;
    protected boolean mRequestStop;
    protected volatile boolean mRecorderStarted;
    protected boolean mIsEOS;
    protected int mTrackIndex;
    protected MediaCodec mMediaCodec;
    private BufferInfo mBufferInfo;
    private IRecorder mRecorder;
    protected final Object mSync = new Object();
    private final EncoderListener mListener;
    protected final String MIME_TYPE;
    private long prevOutputPTSUs = -1L;
    private long prevInputPTSUs = -1L;
    protected static final byte[] START_MARK = new byte[]{0, 0, 0, 1};

    public static final int getCodecCount() {
        return MediaInfo.getCodecCount();
    }

    public static final List<MediaCodecInfo> getCodecs() {
        return MediaInfo.getCodecs();
    }

    public static final MediaCodecInfo getCodecInfoAt(int ix) {
        return MediaInfo.getCodecInfoAt(ix);
    }

    public static CodecCapabilities getCodecCapabilities(MediaCodecInfo codecInfo, String mimeType) {
        return MediaInfo.getCodecCapabilities(codecInfo, mimeType);
    }

    public AbstractEncoder(String mime_type, IRecorder recorder, EncoderListener listener) {
        if (listener == null) {
            throw new NullPointerException("EncodeListener is null");
        } else if (recorder == null) {
            throw new NullPointerException("IMuxer is null");
        } else {
            this.MIME_TYPE = mime_type;
            this.mRecorder = recorder;
            this.mListener = listener;
            recorder.addEncoder(this);
            this.mBufferInfo = new BufferInfo();
            synchronized(this.mSync) {
                (new Thread(this, this.getClass().getSimpleName())).start();

                try {
                    this.mSync.wait();
                } catch (InterruptedException var7) {
                }

            }
        }
    }

    public IRecorder getRecorder() {
        return this.mRecorder;
    }

    public String getOutputPath() {
        return this.mRecorder != null ? this.mRecorder.getOutputPath() : null;
    }

    protected void finalize() throws Throwable {
        this.mRecorder = null;
        this.release();
        super.finalize();
    }

    protected void callOnStartEncode(Surface source, int captureFormat, boolean mayFail) {
        try {
            this.mListener.onStartEncode(this, source, captureFormat, mayFail);
        } catch (Exception var5) {
            Log.w(TAG, var5);
        }

    }

    protected void callOnError(Exception e) {
        try {
            this.mListener.onError(e);
        } catch (Exception var3) {
            Log.w(TAG, var3);
        }

    }

    public void start() {
        synchronized(this.mSync) {
            this.mIsCapturing = true;
            this.mRequestStop = false;
            this.mRequestDrain = 0;
        }
    }

    public void stop() {
        synchronized(this.mSync) {
            if (!this.mRequestStop) {
                this.mRequestStop = true;
                this.mSync.notifyAll();
            }
        }
    }

    public void frameAvailableSoon() {
        synchronized(this.mSync) {
            if (this.mIsCapturing && !this.mRequestStop) {
                ++this.mRequestDrain;
                this.mSync.notifyAll();
            }
        }
    }

    public void run() {
        Process.setThreadPriority(-4);
        synchronized(this.mSync) {
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

            if (localRequestStop) {
                this.drain();
                this.signalEndOfInputStream();
                this.drain();
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
        }

        synchronized(this.mSync) {
            this.mRequestStop = true;
            this.mIsCapturing = false;
        }
    }

    public void release() {
        if (this.mIsCapturing) {
            try {
                this.mListener.onStopEncode(this);
            } catch (Exception var5) {
            }
        }

        this.mIsCapturing = false;
        if (this.mMediaCodec != null) {
            try {
                this.mMediaCodec.stop();
                this.mMediaCodec.release();
                this.mMediaCodec = null;
            } catch (Exception var4) {
            }
        }

        if (this.mRecorderStarted) {
            this.mRecorderStarted = false;
            if (this.mRecorder != null) {
                try {
                    this.mRecorder.stop(this);
                } catch (Exception var3) {
                }
            }
        }

        try {
            this.mListener.onDestroy(this);
        } catch (Exception var2) {
        }

        this.mBufferInfo = null;
        this.mRecorder = null;
    }

    public void signalEndOfInputStream() {
        this.encode((ByteBuffer)null, 0, this.getInputPTSUs());
    }

    public void encode(ByteBuffer buffer) {
    }

    public boolean isCapturing() {
        synchronized(this.mSync) {
            return this.mIsCapturing;
        }
    }

    public void encode(ByteBuffer buffer, int length, long presentationTimeUs) {
        synchronized(this.mSync) {
            if (!this.mIsCapturing || this.mRequestStop) {
                return;
            }

            if (this.mMediaCodec == null) {
                return;
            }
        }

        ByteBuffer[] inputBuffers = this.mMediaCodec.getInputBuffers();

        while(this.mIsCapturing) {
            int inputBufferIndex = this.mMediaCodec.dequeueInputBuffer(10000L);
            if (inputBufferIndex >= 0) {
                ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
                inputBuffer.clear();
                if (buffer != null) {
                    inputBuffer.put(buffer);
                }

                if (length <= 0) {
                    this.mIsEOS = true;
                    this.mMediaCodec.queueInputBuffer(inputBufferIndex, 0, 0, presentationTimeUs, 4);
                } else {
                    this.mMediaCodec.queueInputBuffer(inputBufferIndex, 0, length, presentationTimeUs, 0);
                }
                break;
            }

            if (inputBufferIndex == -1) {
                this.frameAvailableSoon();
            }
        }

    }

    private final void drain() {
        if (this.mMediaCodec != null) {
            ByteBuffer[] encoderOutputBuffers;
            try {
                encoderOutputBuffers = this.mMediaCodec.getOutputBuffers();
            } catch (IllegalStateException var12) {
                return;
            }

            int count = 0;
            IRecorder recorder = this.mRecorder;
            if (recorder != null) {
                while(this.mIsCapturing) {
                    int encoderStatus = this.mMediaCodec.dequeueOutputBuffer(this.mBufferInfo, 10000L);
                    if (encoderStatus == -1) {
                        if (!this.mIsEOS) {
                            ++count;
                            if (count > 5) {
                                break;
                            }
                        }
                    } else if (encoderStatus == -3) {
                        encoderOutputBuffers = this.mMediaCodec.getOutputBuffers();
                    } else if (encoderStatus == -2) {
                        if (this.mRecorderStarted) {
                            throw new RuntimeException("format changed twice");
                        }

                        MediaFormat format = this.mMediaCodec.getOutputFormat();
                        if (!this.startRecorder(recorder, format)) {
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
                                int ix0 = this.byteComp(tmp, 0, START_MARK, START_MARK.length);
                                int ix1 = this.byteComp(tmp, ix0 + 1, START_MARK, START_MARK.length);
                                MediaFormat outFormat = this.createOutputFormat(tmp, this.mBufferInfo.size, ix0, ix1);
                                if (!this.startRecorder(recorder, outFormat)) {
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
                                recorder.writeSampleData(this.mTrackIndex, encodedData, this.mBufferInfo);
                            } catch (TimeoutException var10) {
                                recorder.stopRecording();
                            } catch (Exception var11) {
                                recorder.stopRecording();
                            }
                        }

                        this.mMediaCodec.releaseOutputBuffer(encoderStatus, false);
                        if ((this.mBufferInfo.flags & 4) != 0) {
                            this.stopRecorder(recorder);
                            break;
                        }
                    }
                }

            }
        }
    }

    protected abstract MediaFormat createOutputFormat(byte[] var1, int var2, int var3, int var4);

    public boolean startRecorder(IRecorder recorder, MediaFormat outFormat) {
        this.mTrackIndex = recorder.addTrack(this, outFormat);
        if (this.mTrackIndex >= 0) {
            this.mRecorderStarted = true;
            if (!recorder.start(this)) {
            }
        } else {
            recorder.removeEncoder(this);
        }

        return recorder.isStarted();
    }

    public void stopRecorder(IRecorder recorder) {
        this.mRecorderStarted = this.mIsCapturing = false;
    }

    protected long getInputPTSUs() {
        long result = System.nanoTime() / 1000L;
        if (result <= this.prevInputPTSUs) {
            result = this.prevInputPTSUs + 9643L;
        }

        this.prevInputPTSUs = result;
        return result;
    }

    protected long getNextOutputPTSUs(long presentationTimeUs) {
        if (presentationTimeUs <= this.prevOutputPTSUs) {
            presentationTimeUs = this.prevOutputPTSUs + 9643L;
        }

        this.prevOutputPTSUs = presentationTimeUs;
        return presentationTimeUs;
    }

    public static boolean checkProfileLevel(String mimeType, MediaCodecInfo info) {
        if (info != null && mimeType.equalsIgnoreCase("video/avc")) {
            CodecCapabilities caps = getCodecCapabilities(info, mimeType);
            CodecProfileLevel[] profileLevel = caps.profileLevels;

            for(int j = 0; j < profileLevel.length; ++j) {
                if (profileLevel[j].level >= 16384) {
                    return false;
                }
            }
        }

        return true;
    }

    public static void dumpProfileLevel(String mimeType, MediaCodecInfo info) {
        if (info != null) {
        }

    }

    public static String getProfileLevelString(String mimeType, CodecProfileLevel profileLevel) {
        String result = null;
        if (mimeType.equalsIgnoreCase("video/avc")) {
            switch(profileLevel.profile) {
                case 1:
                    result = "profile:AVCProfileBaseline";
                    break;
                case 2:
                    result = "profile:AVCProfileMain";
                    break;
                case 4:
                    result = "profile:AVCProfileExtended";
                    break;
                case 8:
                    result = "profile:AVCProfileHigh";
                    break;
                case 16:
                    result = "profile:AVCProfileHigh10";
                    break;
                case 32:
                    result = "profile:AVCProfileHigh422";
                    break;
                case 64:
                    result = "profile:AVCProfileHigh444";
                    break;
                default:
                    result = "profile:unknown " + profileLevel.profile;
            }

            switch(profileLevel.level) {
                case 1:
                    result = result + ",level=AVCLevel1";
                    break;
                case 2:
                    result = result + ",level=AVCLevel1b";
                    break;
                case 4:
                    result = result + ",level=AVCLevel11";
                    break;
                case 8:
                    result = result + ",level=AVCLevel12";
                    break;
                case 16:
                    result = result + ",level=AVCLevel13";
                    break;
                case 32:
                    result = result + ",level=AVCLevel2";
                    break;
                case 64:
                    result = result + ",level=AVCLevel21";
                    break;
                case 128:
                    result = result + ",level=AVCLevel22";
                    break;
                case 256:
                    result = result + ",level=AVCLevel3";
                    break;
                case 512:
                    result = result + ",level=AVCLevel31";
                    break;
                case 1024:
                    result = result + ",level=AVCLevel32";
                    break;
                case 2048:
                    result = result + ",level=AVCLevel4";
                    break;
                case 4096:
                    result = result + ",level=AVCLevel41";
                    break;
                case 8192:
                    result = result + ",level=AVCLevel42";
                    break;
                case 16384:
                    result = result + ",level=AVCLevel5";
                    break;
                case 32768:
                    result = result + ",level=AVCLevel51";
                    break;
                default:
                    result = result + ",level=unknown " + profileLevel.level;
            }
        } else if (mimeType.equalsIgnoreCase("video/h263")) {
            switch(profileLevel.profile) {
                case 1:
                case 2:
                case 4:
                case 8:
                case 16:
                case 32:
                case 64:
                case 128:
                case 256:
                default:
                    result = "profile:unknown " + profileLevel.profile;
                    switch(profileLevel.level) {
                        case 1:
                        case 2:
                        case 4:
                        case 8:
                        case 16:
                        case 32:
                        case 64:
                        case 128:
                        default:
                            result = result + ",level=unknown " + profileLevel.level;
                    }
            }
        } else if (mimeType.equalsIgnoreCase("video/mpeg4")) {
            switch(profileLevel.profile) {
                case 1:
                case 2:
                case 4:
                case 8:
                case 16:
                case 32:
                case 64:
                case 128:
                case 256:
                case 512:
                case 1024:
                case 2048:
                case 4096:
                case 8192:
                case 16384:
                case 32768:
                default:
                    result = "profile:unknown " + profileLevel.profile;
                    switch(profileLevel.level) {
                        case 1:
                        case 2:
                        case 4:
                        case 8:
                        case 16:
                        case 32:
                        case 64:
                        case 128:
                        default:
                            result = result + ",level=unknown " + profileLevel.level;
                    }
            }
        } else if (mimeType.equalsIgnoreCase("ausio/aac")) {
            switch(profileLevel.level) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                default:
                    result = "profile:unknown " + profileLevel.profile;
            }
        } else if (mimeType.equalsIgnoreCase("video/vp8")) {
            switch(profileLevel.profile) {
                case 1:
                default:
                    result = "profile:unknown " + profileLevel.profile;
                    switch(profileLevel.level) {
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        default:
                            result = result + ",level=unknown " + profileLevel.level;
                    }
            }
        }

        return result;
    }

    protected final int byteComp(@NonNull byte[] array, int offset, @NonNull byte[] search, int len) {
        int index = -1;
        int n0 = array.length;
        int ns = search.length;
        if (n0 >= offset + len && ns >= len) {
            for(int i = offset; i < n0 - len; ++i) {
                int j;
                for(j = len - 1; j >= 0 && array[i + j] == search[j]; --j) {
                }

                if (j < 0) {
                    index = i;
                    break;
                }
            }
        }

        return index;
    }
}
