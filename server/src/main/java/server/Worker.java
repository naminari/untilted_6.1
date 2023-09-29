package server;

import exceptions.CmdArgsAmountException;
import exceptions.ExecuteException;
import exceptions.RegisterException;
import exceptions.ValidException;
import lombok.extern.slf4j.Slf4j;
import utils.Response;
import utils.Transit;
import utils.TypeResponse;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.channels.SocketChannel;
import java.util.Optional;

@Slf4j
public class Worker implements Runnable {
    private final WorkerParameters parameters;

    public Worker(WorkerParameters parameters) {
        this.parameters = parameters;
    }


    @Override
    public void run() {
        ServerDataEvent event;
        try {
            event = parameters.getQueue().take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            event.getServer().send(event.getChannel(), execute(event.getData(), event.getChannel()));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private byte[] execute(byte[] data, SocketChannel channel) throws IOException {
        Response response;
        try {
            Transit<? extends Serializable> transit = deserializeMessage(data);
            try {
                Optional<Response> authorizationResult = parameters.getRegistrar().registerUser(transit, parameters.getDbHandler(), channel);

                if (authorizationResult.isPresent()) {
                    response = authorizationResult.get();
                } else {
                    try {
                        int userId = parameters.getRegistrar().getUserByChannel(channel);
                        String message = parameters.getHandler().executeCmd(transit, userId, parameters.getDbHandler());
                        response = new Response(TypeResponse.OK, message);
                    } catch (ExecuteException e) {
                        response = new Response(TypeResponse.BAD_RESPONSE, e.getMessage());
                    } catch (CmdArgsAmountException | ValidException | InvocationTargetException |
                             IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

            } catch (RegisterException e) {
                response = new Response(TypeResponse.EMPTY_REGISTRATION, e.getMessage());
            }

        } catch (IOException | ClassNotFoundException e) {
            response = new Response(TypeResponse.BAD_RESPONSE, "Uncorrected message");
        }
        return serializeResponse(response);
    }

    private Transit<? extends Serializable> deserializeMessage(byte[] data) throws IOException, ClassNotFoundException {
        try (var stream = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (Transit<? extends Serializable>) stream.readObject();
        }
    }

    private byte[] serializeResponse(Response response) throws IOException {
        var byteStream = new ByteArrayOutputStream();
        try (var objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(response);
            return byteStream.toByteArray();
        }
    }
}