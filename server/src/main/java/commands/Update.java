package commands;

import builders.BuildChecker;
import cmd.AbstractCommand;

import cmd.CmdType;
import collection.HumanSet;
import exceptions.ValidException;
import humans.HumanBeing;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class Update extends AbstractCommand {
    private final HumanSet humanSet;

    public Update(HumanSet humanSet) {
        super("update", "обновить значение элемента коллекции, id которого равен заданному", CmdType.SIMPLE_ARG);
        this.humanSet = humanSet;
    }

    @Override
    public <K extends Serializable> String action(K[] args) throws ValidException, InvocationTargetException, IllegalAccessException {
        String str = null;
        for (K arg : args) {
            if (arg instanceof String) {
                str = (String) arg;
            }

            if (str != null && BuildChecker.checkId(str)) {
                for (HumanBeing humanBeing : humanSet.getCollection()) {
                    if (humanBeing.getId().toString().equals(str)) {
                        HumanBeing human = humanSet.getHumanDirector().buildHuman();
                        human.setId(UUID.fromString(str));
                        return "element of collection been update";
                    } else {
                        return "no such element";
                    }
                }
            }
            return "Uncorrected UUID input";

        }
        return "кринж";
    }
}