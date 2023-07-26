package cmd;

import exceptions.ExecuteException;
import exceptions.ValidException;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public abstract class AbstractCommand implements Command{
    protected final String name;
    protected final String description;
    protected final CmdType cmdType;
    public AbstractCommand(String n, String d, CmdType t){
        this.name = n;
        this.description = d;
        this.cmdType = t;
    }
    public abstract <K extends Serializable> String action(K[] args) throws FileNotFoundException, ValidException, InvocationTargetException, IllegalAccessException, ExecuteException;
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public CmdType getCmdType(){
        return cmdType;
    }
}