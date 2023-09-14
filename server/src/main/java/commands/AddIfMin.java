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
import java.util.Arrays;
import java.util.Optional;

public class AddIfMin extends AbstractCommand {
    private final HumanSet humanSet;
    public AddIfMin(HumanSet humanSet){
        super("add_if_min", "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции (сравнение по impact speed)", CmdType.COMPLEX_ARG);
        this.humanSet = humanSet;
    }
//    @Override
//    public ActionResult action(CmdArgs args) {
//        try{
//            humanSet.addIfMin(humanSet.getHumanDirector().buildHuman(args.getArgs()));
//            return new ActionResult(true, "element added to collection");
//        } catch (InvocationTargetException | IllegalAccessException | ValidException e){
//            return new ActionResult(false, e.toString());
//        }
//    }
    @Override
    public <K extends Serializable> String action(K[] args) throws FileNotFoundException, ValidException, InvocationTargetException, IllegalAccessException, ExecuteException {
        System.out.println(Arrays.toString(args));
        HumanBeing humanBeing;
        if (args.length == 1){
            humanBeing = (HumanBeing) args[0];
        }
        else {
            String[] data = new String[args.length];

            for (int i = 0; i < args.length; i++) {
                data[i] = String.valueOf(args[i]);    /// явепияв
            }
            humanBeing = humanSet.getHumanDirector().buildHuman(data);
        }



        return humanSet.addIfMin(humanBeing);
    }
}

