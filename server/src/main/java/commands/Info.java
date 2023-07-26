package commands;

import cmd.AbstractCommand;
import cmd.CmdType;
import collection.HumanSet;
import exceptions.ExecuteException;
import exceptions.ValidException;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public class Info extends AbstractCommand {
    private final HumanSet humanSet;
    public Info(HumanSet humanSet){
        super("info", "вывести информацию о коллекции", CmdType.NO_ARGS);

        this.humanSet = humanSet;
    }
//    @Override
//    public ActionResult action(CmdArgs args) {
//        return new ActionResult(true, "This collection's type is a " + humanSet.getCollection().getClass().getName() + ", it contains " + humanSet.getCollection().size() + " elements.");
//
//    }

    @Override
    public <K extends Serializable> String action(K[] args) throws FileNotFoundException, ValidException, InvocationTargetException, IllegalAccessException, ExecuteException {
        return "This collection's type is a " + humanSet.getCollection().getClass().getName() + ", it contains " + humanSet.getCollection().size() + " elements.";
    }
}
