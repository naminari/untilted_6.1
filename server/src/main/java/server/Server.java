package server;

import cmd.CmdHandler;

import com.ctc.wstx.exc.WstxOutputException;
import exceptions.CmdArgsAmountException;
import exceptions.ExecuteException;
import exceptions.ValidException;
import utils.Message;
import utils.Transit;
import utils.TypeMessage;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {
    private final CmdHandler handler;
    private final ServerSocket serverSocket;

    private final Socket clientSocket;

    public Server(int port, CmdHandler handler) throws IOException {
        this.handler = handler;
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
    }

    public void run() {
        try {
            while (true) {

                Transit<? extends Serializable> transit = readTransit(clientSocket.getInputStream());
                System.out.println(transit.getType());
                Message message = new Message(TypeMessage.OK, handler.executeCmd(transit));
                System.out.println("Команда выполняется и формируется сообщение");
                var writeStream = clientSocket.getOutputStream();
                sendMessage(message, writeStream);
                System.out.println("Сообщение отправлено");


            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Ошибка при запуске сервера: " + e.getMessage());
        } catch (ExecuteException | ValidException | CmdArgsAmountException |
                 InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    private Transit<? extends Serializable> readTransit(InputStream stream) throws IOException, ClassNotFoundException {
        int availableBytes = 0;
        while (availableBytes == 0) {
            availableBytes = stream.available();
        }
        byte[] data = new byte[availableBytes];
        int count = stream.read(data);
        return deserializeTransit(data);
    }

    private void sendMessage(Message message, OutputStream stream) throws IOException {
        stream.write(serializeMessage(message));
    }

    private Transit<? extends Serializable> deserializeTransit(byte[] data) throws IOException, ClassNotFoundException {
        try (var stream = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (Transit<? extends Serializable>) stream.readObject();
        }
    }

    private byte[] serializeMessage(Message message) throws IOException {
        var byteStream = new ByteArrayOutputStream();
        try (var objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(message);
            return byteStream.toByteArray();
        }
    }

}
