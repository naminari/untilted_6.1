package commands;

import cmd.*;
import cmd.Command;
import exceptions.ExecuteException;
import exceptions.ValidException;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

public class Help extends AbstractCommand {
    private String message = "";
    private final CmdHandler cmdHandler;

    public Help(CmdHandler cmdHandler) {
        super("help", "вывести справку по доступным командам", CmdType.NO_ARGS);
        this.cmdHandler = cmdHandler;
    }


    private String createMessage(){
        Collection<Command> commands = cmdHandler.getCmds().values();
        for (Command cmd : commands){
            message += cmd.getName() + ": " + cmd.getDescription() + "\n";
        }
        return message;
    }

    @Override
    public <K extends Serializable> String action(CommandArgs<K> args) throws FileNotFoundException, ValidException, InvocationTargetException, IllegalAccessException, ExecuteException {
        if (message.equals("")){
            message = createMessage();
        }
        return  message;
    }
}