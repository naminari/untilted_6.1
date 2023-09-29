package client;

import builders.HumanDirector;
import exceptions.ValidException;
import humans.HumanBeing;
import utils.Message;
import utils.Transit;
import utils.TypeCommand;
import utils.TypeMessage;


import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Client {
    private static final int EIGHT_KILOBYTES = 8192;

    private final HumanDirector director;
    private String password;
    private String login;
    private SocketChannel channel;
    private final SocketAddress address;
    private final List<Transit> requests = new ArrayList<>();

    private final List<String> history = new ArrayList<>();
    private final ByteBuffer readWriteBuffer = ByteBuffer.allocate(EIGHT_KILOBYTES);


    public Client(HumanDirector director, String hostName, int port) {
        this.director = director;
        this.address = new InetSocketAddress(hostName, port);
    }

    public Message send(TypeCommand type, Serializable[] args) {
        try {
            if (Objects.isNull(channel)) {
                connect();
            }
            if (!channel.isOpen()){
                reConnect();
            }

            Transit<Serializable> object;
            if (checkСomplexityOfCommand(type)) {
                HumanBeing humanBeing = director.buildHuman(Arrays.toString(args));    /// WARNING
                object = packCommandToTransit(type, new Serializable[]{humanBeing});
            } else {
                object = packCommandToTransit(type, args);
            }
            history.add(object.getType().getName());
            ByteBuffer buffer = serializeTransit(object);
            sendMessage(buffer);
            return receiveMessage();  // короче прикол в том что сокет закрыт со стороны сервера поэтому при чтении он ничего не видит как я понял
        } catch (IOException e) {
            return new Message(TypeMessage.EMPTY_CONNECTION_WITH_SERVER, e.getMessage());  /// ошибка
        } catch (ClassNotFoundException e) {
            return new Message(TypeMessage.BAD_RESPONSE, e.getMessage());
        } catch (NullPointerException e) {
            return new Message(TypeMessage.BAD_RESPONSE, "Такой команды не существует");
        } catch (ValidException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean checkConnection() {
        Message message = send(TypeCommand.HEAD, null);
        if (message.getTypeMessage() == TypeMessage.EMPTY_REGISTRATION) {
            return logIn().getTypeMessage() == TypeMessage.LOG_IN;
        } else return message.getTypeMessage() != TypeMessage.BAD_RESPONSE &&
                message.getTypeMessage() != TypeMessage.EMPTY_CONNECTION_WITH_SERVER;
    }

    private void sendMessage(ByteBuffer byteBuffer) throws IOException {
        this.channel.write(byteBuffer);
    }
    private boolean checkСomplexityOfCommand(TypeCommand typeCommand){
        return typeCommand.getName().equals("add") || typeCommand.getName().equals("add_if_min") || typeCommand.getName().equals("remove_lower");
    }

    private void connect() throws IOException {
        channel = SocketChannel.open();
        channel.connect(address);
        channel.configureBlocking(false);             /// блкирует сокет
    }
    void reConnect() throws IOException {
        channel.close();
        connect();
    }
    public Message register() {
        return send(TypeCommand.REGISTER, null);
    }

    public Message logIn() {
        return send(TypeCommand.LOG_IN, null);
    }

    private Message receiveMessage() throws IOException, ClassNotFoundException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(16384);
        int readBytes = 0;
        while (readBytes == 0) {
            readBytes = this.channel.read(byteBuffer);     /// ошибка
        }
        byteBuffer.flip();
        int count = byteBuffer.getInt();
        List<byte[]> data = new ArrayList<>();
        readByteArraysFromChannelToList(data, count);
        return deserializeMessage(mergeByteArrays(data));
    }

    private Message deserializeMessage(byte[] data) throws IOException, ClassNotFoundException {
        try (var stream = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (Message) stream.readObject();
        }
    }

    private Transit<Serializable> packCommandToTransit(TypeCommand type, Serializable[] args) {
        return new Transit<>(type, args, login, password);
    }
    private void readByteArraysFromChannelToList(List<byte[]> list, int count) throws IOException {
        int readData = 0;
        while (count > 0) {
            readWriteBuffer.clear();
            while (readData == 0) {
                readData = this.channel.read(readWriteBuffer);
            }
            readWriteBuffer.flip();
            byte[] part = new byte[readData];
            System.arraycopy(readWriteBuffer.array(), 0, part, 0, readData);
            list.add(part);
            count--;
            readData = 0;
        }
    }
    private byte[] mergeByteArrays(List<byte[]> data) {
        int totalLength = 0;
        for (byte[] array : data) {
            totalLength += array.length;
        }
        byte[] result = new byte[totalLength];
        int currentIndex = 0;
        for (byte[] array : data) {
            System.arraycopy(array, 0, result, currentIndex, array.length);
            currentIndex += array.length;
        }
        return result;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    private ByteBuffer serializeTransit(Transit<Serializable> object) throws IOException {
        var byteStream = new ByteArrayOutputStream();
        try (var objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(object);
            return ByteBuffer.wrap(byteStream.toByteArray());
        }

    }
    public void showPendingRequests() {
        for (Transit request : requests) {
            if (Objects.isNull(request.getArgs())) {
                System.out.printf("%d) %s \n", requests.indexOf(request), request.getType().getName());
            } else {
                System.out.printf("%d) %s - %s \n", requests.indexOf(request), request.getType().getName(), Arrays.toString(request.getArgs()));
            }
        }
    }

    public Message sendPendingRequest(int value) throws IOException {
        Transit object = requests.remove(value);
        return send(object.getType(), object.getArgs());
    }
    public HumanBeing createHuman(String... args) throws ValidException, InvocationTargetException, IllegalAccessException {
        return director.buildHuman(args);
    }

}
