package commands;

import cmd.*;
import exceptions.ExecuteException;
import exceptions.ValidException;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public class Help extends AbstractCommand {
    private String message = "";
    private CmdHandler cmdHandler;

    public Help(CmdHandler cmdHandler) {
        super("help", "вывести справку по доступным командам", CmdType.NO_ARGS);
        this.cmdHandler = cmdHandler;
    }


    private String createMessage(){
        for (Command cmd : cmdHandler.getCmds().values()){
            message += cmd.getName() + ": " + cmd.getDescription() + "\n";
        }
        return message;
    }

    @Override
    public <K extends Serializable> String action(K[] args) throws FileNotFoundException, ValidException, InvocationTargetException, IllegalAccessException, ExecuteException {
        if (message.equals("")){
            message = createMessage();
        }
        return  message;
    }
}