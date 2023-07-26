package commands;

import cmd.AbstractCommand;
import cmd.CmdType;
import collection.HumanSet;
import exceptions.ExecuteException;
import exceptions.ValidException;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class Show extends AbstractCommand {
    private HumanSet humanSet;
    public Show(HumanSet humanSet){
        super("show", "вывести все элементы коллекции в строковом представлении", CmdType.NO_ARGS);
        this.humanSet = humanSet;
    }

//    @Override
//    public ActionResult action(CmdArgs args) {
//        String str = humanSet.toString();
//        if (!Objects.equals(str, "")){
//            return new ActionResult(true, humanSet.toString());
//        } else {
//            return new ActionResult(true, "There are no elements in the collection");
//        }
//    }

    @Override
    public <K extends Serializable> String action(K[] args) throws FileNotFoundException, ValidException, InvocationTargetException, IllegalAccessException, ExecuteException {
        return humanSet.toString();
    }
}
