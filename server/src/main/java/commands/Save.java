package commands;

import cmd.AbstractCommand;
import cmd.CmdType;
import collection.HumanSet;
import exceptions.ExecuteException;
import exceptions.ValidException;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class Save extends AbstractCommand {
    private HumanSet humanSet;
    public Save(HumanSet humanSet){
        super("save", "сохранить коллекцию в файл", CmdType.NO_ARGS);
        this.humanSet = humanSet;
    }
//    @Override
//    public ActionResult action(CmdArgs args) throws FileNotFoundException {
//        humanSet.save(args.getArgs());
//        return new ActionResult(true, "Collection been saved into file");
//    }

    @Override
    public <K extends Serializable> String action(K[] args) throws FileNotFoundException, ValidException, InvocationTargetException, IllegalAccessException, ExecuteException {
        String str = null;
        for (K arg : args) {
            if (arg instanceof String) {
                str = (String) arg;
            }
            if (str == null){
                humanSet.save(new String[0]);
            } else {
                String[] strings = new String[1];
                Arrays.fill(strings, str);
                humanSet.save(strings);
                }
            }
        return "Collection been saved into file";
    }
}
