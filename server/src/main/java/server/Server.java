package server;

import cmd.CmdHandler;

import com.ctc.wstx.exc.WstxOutputException;
import exceptions.CmdArgsAmountException;
import exceptions.ExecuteException;
import exceptions.ValidException;
import lombok.extern.slf4j.Slf4j;
import utils.Message;
import utils.Transit;
import utils.TypeMessage;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;

import static java.nio.channels.SelectionKey.OP_WRITE;
import static server.Type.CHANGEOPS;
@Slf4j

public class Server {
    private final Selector selector;
    private static final int EIGHT_KILOBYTES = 8192;
    private final List<ChangeRequest> changeRequests = new LinkedList<>();
    private final Map<SocketChannel, List<byte[]>> pendingData = new ConcurrentHashMap<>();
    private final WorkerParameters parameters;
    private final ForkJoinPool pool = new ForkJoinPool();
    private final ByteBuffer readWriteBuffer = ByteBuffer.allocate(EIGHT_KILOBYTES);

    public Server(String hostName, int port, WorkerParameters parameters) throws IOException {
        this.parameters = parameters;
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        channel.socket().bind(new InetSocketAddress(hostName, port));
        selector = Selector.open();
        channel.register(selector, SelectionKey.OP_ACCEPT);
        new Thread(new Admin()).start();
    }

    public void run() throws IOException {
        while (true) {
            synchronized (changeRequests) {
                Iterator<ChangeRequest> iterator = changeRequests.iterator();
                while (iterator.hasNext()) {
                    ChangeRequest c = iterator.next();
                    if (c.getType() == CHANGEOPS) {
                        Optional<SocketChannel> channel = Optional.ofNullable(c.getChannel());
                        Optional<SelectionKey> key = channel.map(ch -> ch.keyFor(selector));
                        if (key.isPresent()) {
                            key.get().interestOps(c.getOps());
                        } else {
                            iterator.remove();
                        }
                    }
                }
                changeRequests.clear();
            }
            selector.select();
            Iterator<SelectionKey> keysIterator = selector.selectedKeys().iterator();
            while (keysIterator.hasNext()) {
                SelectionKey key = keysIterator.next();
                keysIterator.remove();
                if (key.isValid()) {
                    if (key.isAcceptable()) {
                        accept(key);
                    } else if (key.isReadable()) {
                        read(key);
                    } else if (key.isWritable()) {
                        write(key);
                    }
                }
            }
        }
    }

    private void accept(SelectionKey key) {
        try {
            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = serverChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = null;
        try {

            socketChannel = (SocketChannel) key.channel();
            readWriteBuffer.clear();
            int numRead = socketChannel.read(readWriteBuffer);

            if (numRead == -1) {
                throw new IOException();
            }

            parameters.getQueue().putData(this, socketChannel, readWriteBuffer.array(), numRead);
            pool.execute(new Worker(parameters));

        } catch (IOException e) {
            parameters.getRegistrar().deleteUser(socketChannel);
            pendingData.remove(socketChannel);
            socketChannel.close();
            log.error(e.getMessage(), e);
        }
    }

    private void write(SelectionKey key) {
        try {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            List<byte[]> list = pendingData.get(socketChannel);

            while (!list.isEmpty()) {
                writeListOfByteArraysToChannel(list, socketChannel);
            }

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            key.interestOps(SelectionKey.OP_READ);
        }
    }

    private void writeListOfByteArraysToChannel(List<byte[]> list, SocketChannel channel) throws IOException {
        byte[] data = list.get(0);
        int countOfBuffers = calculateCountOfBuffers(data.length);
        sendClientCountOfBuffers(countOfBuffers, channel);

        List<byte[]> bytes = splitByteArray(data, EIGHT_KILOBYTES);

        for (byte[] b : bytes) {
            readWriteBuffer.clear();
            readWriteBuffer.put(b);
            readWriteBuffer.flip();
            channel.write(readWriteBuffer);
        }

        list.remove(0);
    }

    private void sendClientCountOfBuffers(int countOfBuffers, SocketChannel channel) throws IOException {
        ByteBuffer lengthBuffer = ByteBuffer.allocate(Integer.BYTES);
        lengthBuffer.putInt(countOfBuffers);
        lengthBuffer.rewind();
        channel.write(lengthBuffer);
    }

    private int calculateCountOfBuffers(int dataSize) {
        double result = (double) dataSize / EIGHT_KILOBYTES;
        return (int) Math.ceil(result);
    }

    private List<byte[]> splitByteArray(byte[] data, int split) {

        int chunks = (int) Math.ceil((double) data.length / split);
        List<byte[]> result = new ArrayList<>();

        for (int i = 0; i < chunks; i++) {
            int start = i * split;
            int end = Math.min(start + split, data.length);
            result.add(Arrays.copyOfRange(data, start, end));
        }

        return result;
    }


    void send(SocketChannel channel, byte[] data) {
        synchronized (changeRequests) {
            changeRequests.add(new ChangeRequest(CHANGEOPS, channel, OP_WRITE));
            List<byte[]> list = pendingData.computeIfAbsent(channel, k -> new ArrayList<>());
            list.add(data);
        }
        selector.wakeup();
    }
}