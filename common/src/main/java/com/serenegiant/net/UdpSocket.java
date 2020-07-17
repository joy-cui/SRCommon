//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.serenegiant.net;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

public class UdpSocket {
    private DatagramChannel channel;
    private final InetSocketAddress broadcast;
    private SocketAddress sender;
    private String localAddress;
    private String remoteAddress;
    private int remotePort;

    public UdpSocket(int port) throws SocketException {
        try {
            InetAddress address = null;
            this.channel = DatagramChannel.open();
            this.channel.configureBlocking(false);
            DatagramSocket sock = this.channel.socket();
            sock.setBroadcast(true);
            sock.setReuseAddress(true);
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            Iterator var5 = Collections.list(interfaces).iterator();

            while(true) {
                NetworkInterface intf;
                do {
                    if (!var5.hasNext()) {
                        this.localAddress = address.getHostAddress();
                        sock.bind(new InetSocketAddress(InetAddress.getByAddress(new byte[]{0, 0, 0, 0}), port));
                        byte[] addr = address.getAddress();
                        addr[3] = -1;
                        InetAddress broadcast_addr = InetAddress.getByAddress(addr);
                        this.broadcast = new InetSocketAddress(broadcast_addr, port);
                        return;
                    }

                    intf = (NetworkInterface)var5.next();
                } while(intf.isLoopback());

                Enumeration<InetAddress> inetAddresses = intf.getInetAddresses();
                Iterator var8 = Collections.list(inetAddresses).iterator();

                while(var8.hasNext()) {
                    InetAddress addr = (InetAddress)var8.next();
                    if (addr instanceof Inet4Address) {
                        address = addr;
                    }
                }
            }
        } catch (Exception var10) {
            throw new SocketException("UdpSocket#constructor:" + var10);
        }
    }

    public void release() {
        if (this.channel != null) {
            try {
                try {
                    this.setSoTimeout(200);
                } catch (Exception var2) {
                }

                this.channel.close();
            } catch (Exception var3) {
            }
        }

        this.channel = null;
    }

    public DatagramChannel channel() {
        return this.channel;
    }

    public DatagramSocket socket() {
        return this.channel.socket();
    }

    public void setReceiveBufferSize(int sz) throws SocketException {
        DatagramSocket socket = this.channel != null ? this.channel.socket() : null;
        if (socket != null) {
            socket.setReceiveBufferSize(sz);
        }

    }

    public void setReuseAddress(boolean reuse) throws SocketException {
        DatagramSocket socket = this.channel != null ? this.channel.socket() : null;
        if (socket != null) {
            socket.setReuseAddress(reuse);
        }

    }

    public boolean getReuseAddress() throws SocketException, IllegalStateException {
        DatagramSocket socket = this.channel != null ? this.channel.socket() : null;
        if (socket != null) {
            return socket.getReuseAddress();
        } else {
            throw new IllegalStateException("already released");
        }
    }

    public void setBroadcast(boolean broadcast) throws SocketException {
        DatagramSocket socket = this.channel != null ? this.channel.socket() : null;
        if (socket != null) {
            socket.setBroadcast(broadcast);
        }

    }

    public boolean getBroadcast() throws SocketException, IllegalStateException {
        DatagramSocket socket = this.channel != null ? this.channel.socket() : null;
        if (socket != null) {
            return socket.getBroadcast();
        } else {
            throw new IllegalStateException("already released");
        }
    }

    public void setSoTimeout(int timeout) throws SocketException {
        DatagramSocket socket = this.channel != null ? this.channel.socket() : null;
        if (socket != null) {
            socket.setSoTimeout(timeout);
        }

    }

    public int getSoTimeout() throws SocketException, IllegalStateException {
        DatagramSocket socket = this.channel != null ? this.channel.socket() : null;
        if (socket != null) {
            return socket.getSoTimeout();
        } else {
            throw new IllegalStateException("already released");
        }
    }

    public String local() {
        return this.localAddress;
    }

    public String remote() {
        return this.remoteAddress;
    }

    public int remotePort() {
        return this.remotePort;
    }

    public void broadcast(ByteBuffer buffer) throws IOException, IllegalStateException {
        if (this.channel == null) {
            throw new IllegalStateException("already released");
        } else {
            this.channel.send(buffer, this.broadcast);
        }
    }

    public void broadcast(byte[] bytes) throws IOException, IllegalStateException {
        this.broadcast(ByteBuffer.wrap(bytes));
    }

    public int receive(ByteBuffer buffer) throws IOException, IllegalStateException {
        if (this.channel == null) {
            throw new IllegalStateException("already released");
        } else {
            int read = buffer.remaining();
            this.sender = this.channel.receive(buffer);
            if (this.sender == null) {
                return -1;
            } else {
                InetSocketAddress remote = (InetSocketAddress)this.sender;
                this.remoteAddress = remote.getAddress().getHostAddress();
                this.remotePort = remote.getPort();
                return read - buffer.remaining();
            }
        }
    }
}
