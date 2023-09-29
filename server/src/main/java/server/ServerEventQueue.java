package server;

import lombok.extern.slf4j.Slf4j;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Slf4j
public class ServerEventQueue {
    private final BlockingQueue<ServerDataEvent> queue = new ArrayBlockingQueue<>(5);

    void putData(Server server, SocketChannel channel, byte[] buffer, int numRead) {
        byte[] dataCopy = new byte[numRead];
        System.arraycopy(buffer, 0, dataCopy, 0, numRead);
        try {
            queue.put(new ServerDataEvent(channel, server, dataCopy));
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    ServerDataEvent take() throws InterruptedException {
        return queue.take();
    }

}