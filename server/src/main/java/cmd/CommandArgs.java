package cmd;

import database.DatabaseHandler;

import java.io.Serializable;

public class CommandArgs<T extends Serializable> {
    private final Serializable[] args;
    private final int userId;
    private final DatabaseHandler handler;

    public CommandArgs(Serializable[] args, int userId, DatabaseHandler handler) {
        this.args = args;
        this.userId = userId;
        this.handler = handler;
    }

    public Serializable[] getArgs() {
        return args;
    }

    public int getUserId() {
        return userId;
    }

    public DatabaseHandler getHandler() {
        return handler;
    }
}