//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.serenegiant.bluetooth;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

public class BluetoothManager {
    private static final String TAG = BluetoothManager.class.getSimpleName();
    public static final UUID UUID_SPP = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static final int STATE_RELEASED = -1;
    public static final int STATE_NONE = 0;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;
    @NonNull
    private final Object mSync;
    @NonNull
    private final WeakReference<Context> mWeakContext;
    private final Set<BluetoothManagerCallback> mCallbacks;
    @NonNull
    private final UUID mSecureProfileUUID;
    @NonNull
    private final UUID mInSecureProfileUUID;
    @NonNull
    private final BluetoothAdapter mAdapter;
    private final String mName;
    private volatile int mState;
    private ListeningThread mSecureListeningThread;
    private ListeningThread mInSecureListeningThread;
    private ConnectingThread mConnectingThread;
    private ReceiverThread mReceiverThread;
    private Handler mAsyncHandler;
    private final List<BluetoothDeviceInfo> mDiscoveredDeviceList;
    private final BroadcastReceiver mBroadcastReceiver;

    public static boolean isAvailable() {
        try {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            return adapter != null;
        } catch (Exception var1) {
            Log.w(TAG, var1);
            return false;
        }
    }

    public static boolean isEnabled() {
        try {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            return adapter != null && adapter.isEnabled();
        } catch (Exception var1) {
            Log.w(TAG, var1);
            return false;
        }
    }

    public static boolean requestBluetoothEnable(@NonNull Activity activity, int requestCode) throws SecurityException {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && !adapter.isEnabled()) {
            Intent intent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
            activity.startActivityForResult(intent, requestCode);
        }

        return adapter != null && adapter.isEnabled();
    }

    public static boolean requestBluetoothEnable(@NonNull Fragment fragment, int requestCode) throws SecurityException {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && !adapter.isEnabled()) {
            Intent intent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
            fragment.startActivityForResult(intent, requestCode);
        }

        return adapter != null && adapter.isEnabled();
    }

    public static boolean requestBluetoothEnable(@NonNull android.support.v4.app.Fragment fragment, int requestCode) throws SecurityException {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && !adapter.isEnabled()) {
            Intent intent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
            fragment.startActivityForResult(intent, requestCode);
        }

        return adapter != null && adapter.isEnabled();
    }

    @Nullable
    public static Set<BluetoothDevice> getBondedDevices() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return adapter != null && adapter.isEnabled() ? adapter.getBondedDevices() : null;
    }

    public static boolean requestDiscoverable(@NonNull Activity activity, int duration) throws IllegalStateException {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && adapter.isEnabled()) {
            if (adapter.getScanMode() != 23) {
                Intent intent = new Intent("android.bluetooth.adapter.action.REQUEST_DISCOVERABLE");
                intent.putExtra("android.bluetooth.adapter.extra.DISCOVERABLE_DURATION", duration);
                activity.startActivity(intent);
            }

            return adapter.getScanMode() == 23;
        } else {
            throw new IllegalStateException("bluetoothに対応していないか無効になっている");
        }
    }

    public static boolean requestDiscoverable(@NonNull Fragment fragment, int duration) throws IllegalStateException {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && adapter.isEnabled()) {
            if (adapter.getScanMode() != 23) {
                Intent intent = new Intent("android.bluetooth.adapter.action.REQUEST_DISCOVERABLE");
                if (duration > 0 && duration <= 300) {
                    intent.putExtra("android.bluetooth.adapter.extra.DISCOVERABLE_DURATION", duration);
                }

                fragment.startActivity(intent);
            }

            return adapter.getScanMode() == 23;
        } else {
            throw new IllegalStateException("bluetoothに対応していないか無効になっている");
        }
    }

    public BluetoothManager(@NonNull Context context, String name, @NonNull UUID secureProfileUUID, @NonNull BluetoothManager.BluetoothManagerCallback callback) {
        this(context, name, secureProfileUUID, (UUID)null, callback);
    }

    public BluetoothManager(@NonNull Context context, String name, @NonNull UUID secureProfileUUID, @Nullable UUID inSecureProfileUUID, @NonNull BluetoothManager.BluetoothManagerCallback callback) {
        this.mSync = new Object();
        this.mCallbacks = new CopyOnWriteArraySet();
        this.mDiscoveredDeviceList = new ArrayList();
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent != null ? intent.getAction() : null;
                if ("android.bluetooth.device.action.FOUND".equals(action)) {
                    BluetoothDevice device = (BluetoothDevice)intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                    if (device.getBondState() != 12) {
                        synchronized(BluetoothManager.this.mDiscoveredDeviceList) {
                            BluetoothManager.this.mDiscoveredDeviceList.add(new BluetoothDeviceInfo(device));
                        }

                        BluetoothManager.this.callOnDiscover();
                    }
                } else if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(action)) {
                    BluetoothManager.this.callOnDiscover();
                }

            }
        };
        this.mWeakContext = new WeakReference(context);
        this.mName = !TextUtils.isEmpty(name) ? name : Build.MODEL + "_" + Build.ID;
        this.mSecureProfileUUID = secureProfileUUID;
        this.mInSecureProfileUUID = inSecureProfileUUID != null ? inSecureProfileUUID : secureProfileUUID;
        if (callback != null) {
            this.mCallbacks.add(callback);
        }

        this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.mAdapter != null && this.mAdapter.isEnabled()) {
            this.mState = 0;
            HandlerThread thread = new HandlerThread(TAG);
            thread.start();
            this.mAsyncHandler = new Handler(thread.getLooper());
            IntentFilter filter = new IntentFilter("android.bluetooth.device.action.FOUND");
            filter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
            context.registerReceiver(this.mBroadcastReceiver, filter);
        } else {
            throw new IllegalStateException("bluetoothに対応していないか無効になっている");
        }
    }

    public void release() {
        this.mCallbacks.clear();
        synchronized(this.mSync) {
            if (this.mState != -1) {
                this.mState = -1;
                this.stop();
                if (this.mAsyncHandler != null) {
                    try {
                        this.mAsyncHandler.getLooper().quit();
                    } catch (Exception var5) {
                    }

                    this.mAsyncHandler = null;
                }
            }
        }

        try {
            this.getContext().unregisterReceiver(this.mBroadcastReceiver);
        } catch (Exception var4) {
        }

    }

    public void addCallback(BluetoothManagerCallback callback) {
        if (callback != null) {
            this.mCallbacks.add(callback);
        }

    }

    public void removeCallback(BluetoothManagerCallback callback) {
        this.mCallbacks.remove(callback);
    }

    public Collection<BluetoothDeviceInfo> getPairedDevices() {
        this.checkReleased();
        List<BluetoothDeviceInfo> result = new ArrayList();
        synchronized(this.mSync) {
            if (this.mAdapter.isDiscovering()) {
                this.mAdapter.cancelDiscovery();
            }

            Set<BluetoothDevice> pairedDevices = this.mAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                Iterator var4 = pairedDevices.iterator();

                while(var4.hasNext()) {
                    BluetoothDevice device = (BluetoothDevice)var4.next();
                    result.add(new BluetoothDeviceInfo(device));
                }
            }

            return result;
        }
    }

    public void startDiscovery() throws IllegalStateException {
        synchronized(this.mSync) {
            if (this.mAdapter.isDiscovering()) {
                this.mAdapter.cancelDiscovery();
            }

            Set<BluetoothDevice> pairedDevices = this.mAdapter.getBondedDevices();
            synchronized(this.mDiscoveredDeviceList) {
                this.mDiscoveredDeviceList.clear();
                if (pairedDevices.size() > 0) {
                    Iterator var4 = pairedDevices.iterator();

                    while(var4.hasNext()) {
                        BluetoothDevice device = (BluetoothDevice)var4.next();
                        this.mDiscoveredDeviceList.add(new BluetoothDeviceInfo(device));
                    }

                    this.callOnDiscover();
                }
            }

            this.mAdapter.startDiscovery();
        }
    }

    public void stopDiscovery() {
        synchronized(this.mSync) {
            if (this.mAdapter.isDiscovering()) {
                this.mAdapter.cancelDiscovery();
            }

        }
    }

    public void startListen() {
        synchronized(this.mSync) {
            this.checkReleased();
            this.internalStartListen();
        }
    }

    public void connect(BluetoothDeviceInfo info) throws IllegalStateException {
        this.checkReleased();
        this.connect(this.mAdapter.getRemoteDevice(info.address));
    }

    public void connect(String macAddress) throws IllegalArgumentException, IllegalStateException {
        this.checkReleased();
        this.connect(this.mAdapter.getRemoteDevice(macAddress));
    }

    public void connect(BluetoothDevice device) throws IllegalStateException {
        synchronized(this.mSync) {
            this.checkReleased();
            this.internalCancel(2, false);

            try {
                this.mConnectingThread = new ConnectingThread(device, true);
            } catch (IOException var7) {
                try {
                    this.mConnectingThread = new ConnectingThread(device, false);
                } catch (IOException var6) {
                    throw new IllegalStateException(var6);
                }
            }

            this.mConnectingThread.start();
        }
    }

    public void stop() {
        synchronized(this.mSync) {
            this.internalCancel(0, true);
        }
    }

    public void send(byte[] message) throws IllegalStateException {
        synchronized(this.mSync) {
            this.checkReleased();
            if (this.mReceiverThread != null) {
                this.mReceiverThread.write(message);
            }

        }
    }

    public void send(byte[] message, int offset, int len) throws IllegalStateException {
        synchronized(this.mSync) {
            this.checkReleased();
            if (this.mReceiverThread != null) {
                this.mReceiverThread.write(message, offset, len);
            }

        }
    }

    public int getState() {
        synchronized(this.mSync) {
            return this.mState;
        }
    }

    public boolean isReleased() {
        synchronized(this.mSync) {
            return this.mState == -1;
        }
    }

    public boolean isConnected() {
        synchronized(this.mSync) {
            return this.mState == 3;
        }
    }

    public boolean isListening() {
        synchronized(this.mSync) {
            return this.mState == 1;
        }
    }

    protected Context getContext() {
        return (Context)this.mWeakContext.get();
    }

    private void checkReleased() throws IllegalStateException {
        if (this.mState == -1) {
            throw new IllegalStateException("already released");
        }
    }

    private void internalStartListen() {
        synchronized(this.mSync) {
            this.internalCancel(1, false);
            if (!this.isReleased()) {
                if (this.mSecureListeningThread == null) {
                    this.mSecureListeningThread = new ListeningThread(true);
                    this.mSecureListeningThread.start();
                }

                if (this.mInSecureListeningThread == null) {
                    this.mInSecureListeningThread = new ListeningThread(false);
                    this.mInSecureListeningThread.start();
                }

            }
        }
    }

    private void internalCancel(int newState, boolean cancelListening) {
        if (this.mAdapter.isDiscovering()) {
            this.mAdapter.cancelDiscovery();
        }

        if (this.mConnectingThread != null) {
            this.mConnectingThread.cancel();
            this.mConnectingThread = null;
        }

        if (this.mReceiverThread != null) {
            this.mReceiverThread.cancel();
            this.mReceiverThread = null;
        }

        if (this.mState == -1 || cancelListening) {
            if (this.mSecureListeningThread != null) {
                this.mSecureListeningThread.cancel();
                this.mSecureListeningThread = null;
            }

            if (this.mInSecureListeningThread != null) {
                this.mInSecureListeningThread.cancel();
                this.mInSecureListeningThread = null;
            }
        }

        this.setState(newState);
    }

    protected void callOnDiscover() {
        final ArrayList devices;
        synchronized(this.mDiscoveredDeviceList) {
            devices = new ArrayList(this.mDiscoveredDeviceList);
        }

        synchronized(this.mSync) {
            if (this.mAsyncHandler != null) {
                this.mAsyncHandler.post(new Runnable() {
                    public void run() {
                        Iterator var1 = BluetoothManager.this.mCallbacks.iterator();

                        while(var1.hasNext()) {
                            BluetoothManagerCallback callback = (BluetoothManagerCallback)var1.next();

                            try {
                                callback.onDiscover(devices);
                            } catch (Exception var4) {
                                BluetoothManager.this.mCallbacks.remove(callback);
                                Log.w(BluetoothManager.TAG, var4);
                            }
                        }

                    }
                });
            }

        }
    }

    protected void callOnConnect(final BluetoothDevice device) throws IllegalStateException {
        synchronized(this.mSync) {
            if (!this.isReleased()) {
                if (this.mAsyncHandler != null) {
                    this.mAsyncHandler.post(new Runnable() {
                        public void run() {
                            Iterator var1 = BluetoothManager.this.mCallbacks.iterator();

                            while(var1.hasNext()) {
                                BluetoothManagerCallback callback = (BluetoothManagerCallback)var1.next();

                                try {
                                    callback.onConnect(device.getName(), device.getAddress());
                                } catch (Exception var4) {
                                    BluetoothManager.this.mCallbacks.remove(callback);
                                    Log.w(BluetoothManager.TAG, var4);
                                }
                            }

                        }
                    });
                }

            }
        }
    }

    protected void callOnDisConnect() {
        synchronized(this.mSync) {
            if (this.isReleased()) {
                return;
            }

            if (this.mAsyncHandler != null) {
                this.mAsyncHandler.post(new Runnable() {
                    public void run() {
                        Iterator var1 = BluetoothManager.this.mCallbacks.iterator();

                        while(var1.hasNext()) {
                            BluetoothManagerCallback callback = (BluetoothManagerCallback)var1.next();

                            try {
                                callback.onDisconnect();
                            } catch (Exception var4) {
                                BluetoothManager.this.mCallbacks.remove(callback);
                                Log.w(BluetoothManager.TAG, var4);
                            }
                        }

                    }
                });
            }
        }

        if (!this.isReleased()) {
            this.internalStartListen();
        }

    }

    protected void callOnFailed() {
        synchronized(this.mSync) {
            if (this.isReleased()) {
                return;
            }

            if (this.mAsyncHandler != null) {
                this.mAsyncHandler.post(new Runnable() {
                    public void run() {
                        Iterator var1 = BluetoothManager.this.mCallbacks.iterator();

                        while(var1.hasNext()) {
                            BluetoothManagerCallback callback = (BluetoothManagerCallback)var1.next();

                            try {
                                callback.onFailed();
                            } catch (Exception var4) {
                                BluetoothManager.this.mCallbacks.remove(callback);
                                Log.w(BluetoothManager.TAG, var4);
                            }
                        }

                    }
                });
            }
        }

        if (!this.isReleased()) {
            this.internalStartListen();
        }

    }

    protected void callOnReceive(byte[] message, final int length) {
        final byte[] msg = new byte[length];
        System.arraycopy(message, 0, msg, 0, length);
        synchronized(this.mSync) {
            if (!this.isReleased()) {
                if (this.mAsyncHandler != null) {
                    this.mAsyncHandler.post(new Runnable() {
                        public void run() {
                            Iterator var1 = BluetoothManager.this.mCallbacks.iterator();

                            while(var1.hasNext()) {
                                BluetoothManagerCallback callback = (BluetoothManagerCallback)var1.next();

                                try {
                                    callback.onReceive(msg, length);
                                } catch (Exception var4) {
                                    BluetoothManager.this.mCallbacks.remove(callback);
                                    Log.w(BluetoothManager.TAG, var4);
                                }
                            }

                        }
                    });
                }

            }
        }
    }

    private void setState(int state) {
        synchronized(this.mSync) {
            if (this.mState != -1) {
                this.mState = state;
            }

        }
    }

    protected void onConnect(BluetoothSocket socket, BluetoothDevice device) {
        synchronized(this.mSync) {
            this.internalCancel(3, true);
            this.mReceiverThread = new ReceiverThread(socket);
            this.mReceiverThread.start();
            this.callOnConnect(device);
        }
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device, boolean secure) throws IOException {
        return secure ? device.createRfcommSocketToServiceRecord(this.mSecureProfileUUID) : device.createInsecureRfcommSocketToServiceRecord(this.mInSecureProfileUUID);
    }

    private class ConnectingThread extends BluetoothSocketThread {
        private final BluetoothDevice mmDevice;

        public ConnectingThread(BluetoothDevice device, boolean secure) throws IOException {
            super("ConnectingThread:" + BluetoothManager.this.mName, BluetoothManager.this.createBluetoothSocket(device, secure));
            this.mmDevice = device;
        }

        public void run() {
            if (BluetoothManager.this.mAdapter.isDiscovering()) {
                BluetoothManager.this.mAdapter.cancelDiscovery();
            }

            try {
                this.mmSocket.connect();
            } catch (IOException var6) {
                Log.w(BluetoothManager.TAG, var6);

                try {
                    this.mmSocket.close();
                } catch (IOException var5) {
                    if (!this.mIsCanceled) {
                        Log.w(BluetoothManager.TAG, "failed to close socket", var5);
                    }
                }

                BluetoothManager.this.callOnFailed();
                return;
            }

            synchronized(BluetoothManager.this.mSync) {
                BluetoothManager.this.mConnectingThread = null;
            }

            BluetoothManager.this.onConnect(this.mmSocket, this.mmDevice);
        }
    }

    private class ListeningThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;
        private volatile boolean mIsCanceled;

        public ListeningThread(boolean secure) {
            super("ListeningThread:" + BluetoothManager.this.mName);
            BluetoothServerSocket tmp = null;

            try {
                if (secure) {
                    tmp = BluetoothManager.this.mAdapter.listenUsingRfcommWithServiceRecord(BluetoothManager.this.mName, BluetoothManager.this.mSecureProfileUUID);
                } else {
                    tmp = BluetoothManager.this.mAdapter.listenUsingInsecureRfcommWithServiceRecord(BluetoothManager.this.mName, BluetoothManager.this.mInSecureProfileUUID);
                }
            } catch (IOException var5) {
                Log.w(BluetoothManager.TAG, var5);
            }

            this.mmServerSocket = tmp;
        }

        public void run() {
            while(true) {
                if (BluetoothManager.this.mState != 3) {
                    BluetoothSocket socket;
                    try {
                        socket = this.mmServerSocket.accept();
                    } catch (IOException var4) {
                        if (!this.mIsCanceled) {
                            Log.d(BluetoothManager.TAG, var4.getMessage());
                        }

                        return;
                    }

                    if (socket == null) {
                        continue;
                    }

                    switch(BluetoothManager.this.mState) {
                        case 0:
                        case 3:
                            try {
                                socket.close();
                            } catch (IOException var3) {
                                Log.w(BluetoothManager.TAG, "Could not close unwanted socket", var3);
                            }
                            continue;
                        case 1:
                        case 2:
                            BluetoothManager.this.onConnect(socket, socket.getRemoteDevice());
                            break;
                        default:
                            continue;
                    }
                }

                return;
            }
        }

        public void cancel() {
            this.mIsCanceled = true;

            try {
                this.mmServerSocket.close();
            } catch (IOException var2) {
                Log.e(BluetoothManager.TAG, "close() of server failed", var2);
            }

        }
    }

    private class ReceiverThread extends BluetoothSocketThread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ReceiverThread(BluetoothSocket socket) {
            super("ReceiverThread:" + BluetoothManager.this.mName, socket);
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException var6) {
                Log.e(BluetoothManager.TAG, "temp sockets not created", var6);
            }

            this.mmInStream = tmpIn;
            this.mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];

            while(BluetoothManager.this.mState == 3) {
                try {
                    int bytes = this.mmInStream.read(buffer);
                    if (bytes > 0) {
                        BluetoothManager.this.callOnReceive(buffer, bytes);
                    }
                } catch (IOException var4) {
                    if (!this.mIsCanceled) {
                        Log.d(BluetoothManager.TAG, "disconnected", var4);
                    }

                    BluetoothManager.this.callOnDisConnect();
                    break;
                }
            }

        }

        public void write(byte[] buffer) throws IllegalStateException {
            if (BluetoothManager.this.mState != 3) {
                throw new IllegalStateException("already disconnected");
            } else {
                try {
                    this.mmOutStream.write(buffer);
                } catch (IOException var3) {
                    if (!this.mIsCanceled) {
                        throw new IllegalStateException(var3);
                    }
                }

            }
        }

        public void write(byte[] buffer, int offset, int len) throws IllegalStateException {
            if (BluetoothManager.this.mState != 3) {
                throw new IllegalStateException("already disconnected");
            } else {
                try {
                    this.mmOutStream.write(buffer, offset, len);
                } catch (IOException var5) {
                    if (!this.mIsCanceled) {
                        throw new IllegalStateException(var5);
                    }
                }

            }
        }
    }

    private abstract static class BluetoothSocketThread extends Thread {
        protected final BluetoothSocket mmSocket;
        protected volatile boolean mIsCanceled;

        public BluetoothSocketThread(String name, BluetoothSocket socket) {
            super(name);
            this.mmSocket = socket;
        }

        public void cancel() {
            this.mIsCanceled = true;

            try {
                this.mmSocket.close();
            } catch (IOException var2) {
                Log.e(BluetoothManager.TAG, "failed to call BluetoothSocket#close", var2);
            }

        }
    }

    public interface BluetoothManagerCallback {
        void onDiscover(Collection<BluetoothDeviceInfo> var1);

        void onConnect(String var1, String var2);

        void onDisconnect();

        void onFailed();

        void onReceive(byte[] var1, int var2);
    }
}
