package server;

import cmd.CmdHandler;

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

public class Server {
    private int port;
    private final CmdHandler handler;

    public Server(int port, CmdHandler handler) {
        this.port = port;
        this.handler = handler;
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)){
             Socket clientSocket = serverSocket.accept();
             System.out.println("hui");

             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Transit<? extends Serializable> transit = readTransit(clientSocket.getInputStream());


            System.out.println("Сервер запущен и ожидает подключения клиента...");

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Получено сообщение от клиента: " + inputLine);
                // Здесь можно обработать полученное сообщение
                // и отправить ответ клиенту с помощью out.println()
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при запуске сервера: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
//        while (true) {
//            try (ServerSocket server = new ServerSocket(port)) {/// сокет сервера
//                System.out.println("хуй");
//                Socket socket = server.accept();                             /// сокет с клиента
//                var readStream = socket.getInputStream();                    /// чтение входящего инпута
//                Transit<? extends Serializable> transit = readTransit(readStream);  /// создание нового транзита из инпута
//                System.out.println("Транзит читается");
//                Message message = new Message(TypeMessage.OK, handler.executeCmd(transit));  /// создание сообщения о корректной передаче данных на сервер и результате выполнения команды
//                System.out.println("Команда выполняется и формируется сообщение");
//                var writeStream = socket.getOutputStream();
//                sendMessage(message, writeStream);
//                System.out.println("Сообщение отправлено");
//            } catch (ClassNotFoundException e) {
//                System.out.println(e.getMessage());
//            } catch (IOException | CmdArgsAmountException | ExecuteException | ValidException |
//                     InvocationTargetException | IllegalAccessException e) {
//                System.out.println(e.getMessage() + "!");
//            }
//        }
    }

    private Transit<? extends Serializable> readTransit(InputStream stream) throws IOException, ClassNotFoundException {
        byte[] data = new byte[stream.available()];
        while (stream.available() > 0) {
            int count = stream.read(data);
        }
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
