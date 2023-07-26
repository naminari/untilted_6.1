package cmd;

import exceptions.CmdArgsAmountException;
import exceptions.ExecuteException;
import exceptions.ValidException;
import utils.Transit;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class CmdHandler {

    private final HashMap<String, Command> cmds;
    private final LinkedList<Command> cmdHistory;    /// Походу это нужно только на сервере

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

    public String executeCmd(Transit<? extends Serializable> transit) throws CmdArgsAmountException, ExecuteException, FileNotFoundException, ValidException, InvocationTargetException, IllegalAccessException {
        Command command = getCmds().get(transit.getType().getName());
        String result = null;
        if (command.getCmdType() != CmdType.NO_ARGS && transit.getArgs().length == 0){                 //// и к моему огромному удивлению это тоже
            throw new ExecuteException("Missing argument");
        }else {
            if (transit.getArgs().length == 1) {
                result = command.action(transit.getArgs());
            }
//            else {
//                List<String> list = new ArrayList<>(Arrays.asList(transit.getArgs()));
//                list.remove(0);
//                result = command.action(list.toArray(new String[0]));
            cmdHistory.addLast(command);
            cmdHistory.removeFirst();
            return result;
        }
    }
}
