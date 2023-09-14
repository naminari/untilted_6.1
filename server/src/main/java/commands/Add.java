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

public class Add extends AbstractCommand {
    private final HumanSet humanSet;
    public Add(HumanSet humanSet){
        super("add", "добавить новый элемент в коллекцию", CmdType.COMPLEX_ARG);
        this.humanSet = humanSet;
    }
    @Override
    public <K extends Serializable> String action(K[] args) throws FileNotFoundException, ValidException, InvocationTargetException, IllegalAccessException, ExecuteException {
        HumanBeing humanBeing;
        if (args.length == 1){
            humanBeing = (HumanBeing) args[0];
        }
        else {
            String[] data = new String[args.length];

            for (int i = 0; i < args.length; i++) {
                data[i] = String.valueOf(args[i]);
            }
            humanBeing = humanSet.getHumanDirector().buildHuman(data);
        }
        return humanSet.add(humanBeing);
    }
}
