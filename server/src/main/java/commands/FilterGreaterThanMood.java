package commands;

import builders.BuildChecker;
import cmd.AbstractCommand;

import cmd.CmdType;
import collection.HumanSet;
import exceptions.ExecuteException;
import exceptions.ValidException;
import humans.Mood;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public class FilterGreaterThanMood extends AbstractCommand {
    private HumanSet humanSet;
    public FilterGreaterThanMood(HumanSet humanSet){
        super("filter_greater_than_mood", "вывести элементы, значение поля mood которых больше заданного", CmdType.SIMPLE_ARG);
        this.humanSet = humanSet;
    }
//    @Override
//    public ActionResult action(CmdArgs args) throws FileNotFoundException {
//        if (BuildChecker.checkMood(args.getArgs()[0])) {
//            Mood mood  = Mood.getMoodByNumber(Integer.parseInt(args.getArgs()[0]));
//            String res = humanSet.filterGreaterThanMood(mood);
//            return new ActionResult(true, res);
//        } else {
//            return new ActionResult(true, "Uncorrected input, enter a number from 1 to 4 for the corresponding Enum values");
//        }
//    }
    @Override
    public <K extends Serializable> String action(K[] args) throws FileNotFoundException, ValidException, InvocationTargetException, IllegalAccessException, ExecuteException {
        if (BuildChecker.checkMood(args[0].toString())) {
            Mood mood  = Mood.getMoodByNumber(Integer.parseInt(args[0].toString()));
            return  humanSet.filterGreaterThanMood(mood);
        } else {
            return "Uncorrected input, enter a number from 1 to 4 for the corresponding Enum values";
        }
    }
}
