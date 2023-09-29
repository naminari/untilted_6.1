package server;

import java.nio.channels.SocketChannel;

public class ChangeRequest {
    private final Type type;
    private final SocketChannel channel;
    private final int ops;


    public ChangeRequest(Type type, SocketChannel channel, int ops) {
        this.type = type;
        this.channel = channel;
        this.ops = ops;
    }

    public int getOps() {
        return ops;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public Type getType() {
        return type;
    }
}