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

public class Add extends AbstractCommand {
    private HumanSet humanSet;
    public Add(HumanSet humanSet){
        super("add", "добавить новый элемент в коллекцию", CmdType.COMPLEX_ARG);
        this.humanSet = humanSet;
    }
//    @Override
//    public ActionResult action(CmdArgs args) {
//        try{
//            humanSet.add(humanSet.getHumanDirector().buildHuman(args.getArgs()));
//            return new ActionResult(true, "element added to collection");
//        } catch (InvocationTargetException | IllegalAccessException | ValidException e){
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
        return humanSet.add(Optional.ofNullable(humanBeing).orElseThrow(() -> new ExecuteException("Empty arguments add cmd")));
    }
}
