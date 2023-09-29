package commands;

import cmd.AbstractCommand;

import cmd.CmdType;
import cmd.CommandArgs;
import collection.HumanSet;
import database.DatabaseHandler;
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
    public <K extends Serializable> String action(CommandArgs<K> args) throws FileNotFoundException, ValidException, InvocationTargetException, IllegalAccessException, ExecuteException {
        System.out.println(Arrays.toString(args.getArgs()));

//        HumanBeing humanBeing;
//        if (args.getArgs().length == 1){
//            humanBeing = (HumanBeing) args.getArgs()[0];
//        }
//        else {
//            String[] data = new String[args.getArgs().length];
//
//            for (int i = 0; i < args.getArgs().length; i++) {
//                data[i] = String.valueOf(args.getArgs()[i]);
//            }
//            humanBeing = humanSet.getHumanDirector().buildHuman(data);
//        }
        Optional<HumanBeing> optional = Optional.empty();
        for (Serializable arg : args.getArgs()) {
            if (arg instanceof HumanBeing) {
                optional = Optional.of((HumanBeing) arg);
            }
        }
        boolean isMin = humanSet.Min(optional.orElseThrow(() -> new ExecuteException("Message isn't valid")));
        DatabaseHandler handler = args.getHandler();
        int userId = args.getUserId();
        if (isMin) {
            Optional<HumanBeing> human = handler.addProduct(optional.get(), userId);
            return humanSet.add(human.orElseThrow(() -> new ExecuteException("Problems with database connection")));
        } else {
            return "Product is less or equals max product";
        }
    }
}

