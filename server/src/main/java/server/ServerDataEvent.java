package server;

import java.nio.channels.SocketChannel;

public class ServerDataEvent {
    private final SocketChannel channel;
    private final Server server;
    private final byte[] data;

    public ServerDataEvent(SocketChannel channel, Server server, byte[] data) {
        this.channel = channel;
        this.server = server;
        this.data = data;
    }

    public Server getServer() {
        return server;
    }

    public byte[] getData() {
        return data;
    }

    public SocketChannel getChannel() {
        return channel;
    }
}