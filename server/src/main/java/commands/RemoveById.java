package commands;

import builders.BuildChecker;
import cmd.AbstractCommand;
import cmd.CmdType;
import collection.HumanSet;
import exceptions.ExecuteException;
import exceptions.ValidException;
import humans.HumanBeing;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.UUID;

public class RemoveById extends AbstractCommand {
    HumanSet humanSet;
    public RemoveById(HumanSet humanSet){
        super("remove_by_id", "удалить элемент из коллекции по его id", CmdType.SIMPLE_ARG);
        this.humanSet = humanSet;
    }
//    @Override
//    public ActionResult action(CmdArgs args) {
//        if (humanSet.getCollection().isEmpty()){
//            return new ActionResult(true, "Collection is empty");
//        }
//        if (BuildChecker.checkId(args.getArgs()[0])) {
//            humanSet.removeById(args.getArgs()[0]);
//            return new ActionResult(true, "Successfully deleted element with id " + Arrays.toString(args.getArgs()));
//        } else {
//            return new ActionResult(true, "No such element");
//        }
//    }
    @Override
    public <K extends Serializable> String action(K[] args) throws FileNotFoundException, ValidException, InvocationTargetException, IllegalAccessException, ExecuteException {
        UUID id = null;
        for (K arg : args){
            if (arg instanceof UUID){
                id = (UUID) arg;
            }
        }
        if (humanSet.removeById(id)){
            return "delete successfully";
        } else {
            return "element with this id wasn't found";
        }
    }
}