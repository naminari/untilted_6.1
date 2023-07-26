package commands;

import cmd.*;
import exceptions.ExecuteException;
import exceptions.ValidException;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

public class History extends AbstractCommand {
    private final CmdHandler cmdHandler;
    public History(CmdHandler cmdHandler){
        super("history", "вывести последние 8 команд", CmdType.NO_ARGS);
        this.cmdHandler = cmdHandler;
    }
    @Override
    public <K extends Serializable> String action(K[] args) throws FileNotFoundException, ValidException, InvocationTargetException, IllegalAccessException, ExecuteException {
        LinkedList<Command> cmds = cmdHandler.getCmdHistory();
        StringBuilder str = new StringBuilder();
        for (Command cmd : cmds){
            if (cmd != null){
                str.append(cmd.getName()).append("\n");
            }
        }
        return str.toString();
    }
}
