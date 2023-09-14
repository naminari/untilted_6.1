package commands;

import cmd.AbstractCommand;
import cmd.CmdType;
import collection.HumanSet;
import exceptions.ExecuteException;
import exceptions.ValidException;

import java.io.File;
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
            str = (String) arg;
        }
        if (str != null){
            File file = new File(str);
            humanSet.save(file);
        } else {
            humanSet.save(humanSet.getFile());
        }
        return "Collection been saved into file";
    }
}
