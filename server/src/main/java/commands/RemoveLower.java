package commands;

import cmd.AbstractCommand;
import cmd.CmdType;
import collection.HumanSet;
import exceptions.ExecuteException;
import exceptions.ValidException;
import humans.HumanBeing;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class RemoveLower extends AbstractCommand {
    HumanSet humanSet;

    public RemoveLower(HumanSet humanSet) {
        super("remove_lower", "удалить из коллекции все элементы, меньшие, чем заданный", CmdType.COMPLEX_ARG);
        this.humanSet = humanSet;
    }

//    @Override
//    public ActionResult action(CmdArgs args) throws FileNotFoundException {
//        try {
//            Optional<HumanBeing> optional = Optional.empty();
//            for (K arg : args) {
//                if (arg instanceof HumanBeing) {
//                    optional = Optional.of((HumanBeing) arg);
//                }
//            }
//            humanSet.removeLower(humanSet.getHumanDirector().buildHuman(args.getArgs()));
//            return new ActionResult(true, "Successfully removed all lower objects");
//        } catch (InvocationTargetException | IllegalAccessException | ValidException e) {
//            return new ActionResult(false, e.toString());
//        }
//    }

    @Override
    public <K extends Serializable> String action(K[] args) throws FileNotFoundException, ValidException, InvocationTargetException, IllegalAccessException, ExecuteException {
        HumanBeing humanBeing = null;
        for (K arg: args) {
            if (arg instanceof HumanBeing) {
                humanBeing = (HumanBeing) arg;
            }
        }
        humanSet.removeLower(humanBeing);
        return "Successfully removed all lower objects";
    }
}