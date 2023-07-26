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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Client {
    private final HumanDirector director;
    private SocketChannel channel;
    private final SocketAddress address;
    private final List<String> history = new ArrayList<>();

    public Client(HumanDirector director, String hostName, int port) {
        this.director = director;
        this.address = new InetSocketAddress(hostName, port);
    }

    public Message send(TypeCommand type, Serializable[] args) {
        try {
            if (Objects.isNull(channel) || !channel.isOpen()) {                /// норм ли что соединение при закрытом сокете или его отсутствии
                connect();
            }
            Transit<Serializable> object = packCommandToTransit(type, args);  /// упаковывает команду для отправки
            history.add(object.getType().getName());                          /// добавляет в историю выполнение команды
            ByteBuffer buffer = serializeTransit(object);                     /// упаковывает транхит в буфер для передачи их

            sendMessage(buffer);                                              /// отправляет буфер
            return receiveMessage();                                          /// возвращает сообщение
        } catch (IOException e) {
            return new Message(TypeMessage.EMPTY_CONNECTION_WITH_SERVER, e.getMessage());
        } catch (ClassNotFoundException e) {
            return new Message(TypeMessage.BAD_RESPONSE, e.getMessage());
        }
    }

    public String getHistory() {
        if (history.size() > 11) {                                            /// Выводит последние 12 команд
            StringBuilder buffer = new StringBuilder();
            for (int i = history.size() - 12; i < history.size() - 1; i++) {
                buffer.append(history.get(i)).append(System.lineSeparator());
            }
            return buffer.toString();
        } else {
            if (history.isEmpty()) {
                return "Empty commands' history";
            } else {
                return history.stream()
                        .collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }

    public void addHistory(String history) {
        this.history.add(history);
    }

    public HumanBeing createProduct(String... args) throws ValidException, InvocationTargetException, IllegalAccessException {
        return director.buildHuman(args);
    }

    private void sendMessage(ByteBuffer byteBuffer) throws IOException {
        System.out.println("хуй");
        this.channel.write(byteBuffer);
    }

    private void connect() throws IOException {
        channel = SocketChannel.open();
        channel.connect(address);
        channel.configureBlocking(false);             /// блкирует сокет
    }

    private Message receiveMessage() throws IOException, ClassNotFoundException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(16384);
        int readBytes = 0;
        while (readBytes == 0) {
            readBytes = this.channel.read(byteBuffer);
        }
        byteBuffer.flip();
        byte[] data = new byte[byteBuffer.remaining()];
        byteBuffer.get(data);
        return deserializeMessage(data);
    }

    private Message deserializeMessage(byte[] data) throws IOException, ClassNotFoundException {
        try (var stream = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (Message) stream.readObject();
        }
    }

    private Transit<Serializable> packCommandToTransit(TypeCommand type, Serializable[] args) {
        return new Transit<>(type, args);
    }

    private ByteBuffer serializeTransit(Transit<Serializable> object) throws IOException {
        var byteStream = new ByteArrayOutputStream();
        try (var objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(object);
            return ByteBuffer.wrap(byteStream.toByteArray());                   /// перенос байтового массива в буфер
        }
    }
}
