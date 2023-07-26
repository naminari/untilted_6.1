package commands;

import cmd.AbstractCommand;

import cmd.CmdType;
import collection.HumanSet;
import exceptions.ExecuteException;
import exceptions.ValidException;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public class Clear extends AbstractCommand {
    private HumanSet humanSet;

    public Clear(HumanSet humanSet){
        super("clear", "очистить коллекцию", CmdType.NO_ARGS);
        this.humanSet = humanSet;
    }

    @Override
    public <K extends Serializable> String action(K[] args) {
        humanSet.getCollection().clear();
        return "Collection has been cleared";
    }

//    public String action() {
//        humanSet.clear();
//
//        ///return new ActionResult(true, "Successfully cleared collection");
//    }
}
