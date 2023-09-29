package server;

import cmd.CmdHandler;
import database.DatabaseHandler;
import humans.HumanBeing;

import java.util.Queue;

public class WorkerParameters {
    private final CmdHandler<Queue<HumanBeing>> handler;
    private final DatabaseHandler dbHandler;
    private final CustomerRegistrar registrar;
    private final ServerEventQueue queue;

    public WorkerParameters(CmdHandler<Queue<HumanBeing>> handler, DatabaseHandler dbHandler, CustomerRegistrar registrar, ServerEventQueue queue) {
        this.handler = handler;
        this.dbHandler = dbHandler;
        this.registrar = registrar;
        this.queue = queue;
    }

    public CmdHandler<Queue<HumanBeing>> getHandler() {
        return handler;
    }

    public DatabaseHandler getDbHandler() {
        return dbHandler;
    }

    public CustomerRegistrar getRegistrar() {
        return registrar;
    }

    public ServerEventQueue getQueue() {
        return queue;
    }
}