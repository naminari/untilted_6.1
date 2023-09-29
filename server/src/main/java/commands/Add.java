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

public class Add extends AbstractCommand {
    private final HumanSet humanSet;
    public Add(HumanSet humanSet){
        super("add", "добавить новый элемент в коллекцию", CmdType.COMPLEX_ARG);
        this.humanSet = humanSet;
    }
    @Override
    public <K extends Serializable> String action(CommandArgs<K> args) throws FileNotFoundException, ValidException, InvocationTargetException, IllegalAccessException, ExecuteException {
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
        DatabaseHandler handler = args.getHandler();
        Optional<HumanBeing> human = handler.addProduct(optional.orElseThrow(() -> new ExecuteException("Message isn't valid")), args.getUserId());
        return humanSet.add(human.orElseThrow(() -> new ExecuteException("Some problems with database, please repeat request")));
    }
}
