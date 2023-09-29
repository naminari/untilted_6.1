package server;

import database.DatabaseHandler;
import exceptions.RegisterException;
import lombok.extern.slf4j.Slf4j;
import utils.Response;
import utils.Transit;
import utils.TypeCommand;
import utils.TypeResponse;

import java.io.Serializable;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class CustomerRegistrar {
    private final ConcurrentMap<SocketChannel, Integer> activeUsers = new ConcurrentHashMap<>();

    public Optional<Response> registerUser(Transit<? extends Serializable> transit, DatabaseHandler handler, SocketChannel channel) throws RegisterException {
        try {
            int userId = 0;
            synchronized (this) {
                userId = handler.getUserIdByName(transit.getUserName());
            }
            boolean isActive = activeUsers.containsValue(userId);
            TypeCommand type = transit.getType();
            if (!isActive) {

                if (type == TypeCommand.LOG_IN) {
                    int id = 0;
                    synchronized (this) {
                        id = handler.logIn(transit.getUserName(), transit.getPassword());
                    }

                    if (id != -1) {
                        activeUsers.put(channel, id);
                        log.info("User with id - " + id + "was logIn");
                        return Optional.of(new Response(TypeResponse.LOG_IN, "Log in successfully"));
                    } else {
                        return Optional.of(new Response(TypeResponse.BAD_RESPONSE, "Uncorrected login or password"));
                    }

                } else if (type == TypeCommand.REGISTER) {
                    int id = 0;
                    synchronized (this) {
                        id = handler.registerUser(transit.getUserName(), transit.getPassword());
                    }

                    if (id != -1) {
                        activeUsers.put(channel, id);
                        log.info("User with id - " + id + "was register");
                        return Optional.of(new Response(TypeResponse.REGISTER, "Register successfully"));
                    } else {
                        return Optional.of(new Response(TypeResponse.USER_ALREADY_EXISTS, "Login already exists"));
                    }

                } else {
                    throw new RegisterException("User isn't active, need registration or log in!");
                }

            } else {
                if (type == TypeCommand.LOG_IN) {
                    return Optional.of(new Response(TypeResponse.ALREADY_ACTIVE_SESSION, "Пользователь с таким id имеет активную сессию"));
                } else if (type == TypeCommand.REGISTER) {
                    throw new RegisterException("Login already exist");
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error(e.getSQLState(), e);
            return Optional.of(new Response(TypeResponse.BAD_RESPONSE, "Empty connection with database"));
        }
    }

    public void deleteUser(SocketChannel channel) {
        int id =  activeUsers.remove(channel);
        log.info("User with id - " + id + " is out");
    }

    public int getUserByChannel(SocketChannel channel) {
        return activeUsers.get(channel);
    }
}