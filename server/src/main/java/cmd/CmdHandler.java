package cmd;

import database.DatabaseHandler;
import exceptions.CmdArgsAmountException;
import exceptions.ExecuteException;
import exceptions.ValidException;
import humans.HumanBeing;
import utils.Transit;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class CmdHandler<T extends Collection<HumanBeing>> {

    private final HashMap<String, Command> cmds;
    private final LinkedList<Command> cmdHistory;

    public CmdHandler() {
        this.cmds = new HashMap<>();
        this.cmdHistory = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            cmdHistory.add(null);
        }
    }

    public LinkedList<Command> getCmdHistory() {
        return cmdHistory;
    }

    public void addComm(Command c) {
        final String name = c.getName();
        if (!isInCmds(name)) {
            this.cmds.put(name, c);
        }
    }

    public boolean isInCmds(String name) {
        return this.cmds.containsKey(name);
    }

    public void addCmds(Command... comms) {
        for (Command c : comms) {
            addComm(c);
        }
    }
    public HashMap<String, Command> getCmds() {
        return cmds;
    }

    public String executeCmd(Transit<? extends Serializable> transit, int userId, DatabaseHandler handler) throws CmdArgsAmountException, ExecuteException, FileNotFoundException, ValidException, InvocationTargetException, IllegalAccessException {
        Command command = getCmds().get(transit.getType().getName());
        String result;
        if (command.getCmdType() == CmdType.SIMPLE_ARG && transit.getArgs().length == 0){
            throw new ExecuteException("Missing argument");
        } else {
                CommandArgs<Serializable> commandArgs = new CommandArgs<>(transit.getArgs(), userId, handler);
                result = command.action(commandArgs);
//            if (transit.getArgs().length == 1) {
//                result = command.action(transit.getArgs());
//            }
//            else {
//                List<Serializable> list = new ArrayList<>(Arrays.asList(transit.getArgs()));
//                list.remove(0);
//                result = command.action(list.toArray(new Serializable[0]));
//            }
            cmdHistory.addLast(command);
            cmdHistory.removeFirst();
            return result;
        }
    }
}
