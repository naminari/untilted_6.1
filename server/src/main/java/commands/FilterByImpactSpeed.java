package commands;

import builders.BuildChecker;
import cmd.AbstractCommand;
import cmd.CmdType;
import collection.HumanSet;
import exceptions.ExecuteException;
import exceptions.ValidException;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public class FilterByImpactSpeed extends AbstractCommand {
    HumanSet humanSet;
    public FilterByImpactSpeed(HumanSet humanSet){
        super("filter_by_impact_speed", " вывести элементы, значение поля impactSpeed которых равно заданному", CmdType.SIMPLE_ARG);
        this.humanSet = humanSet;
    }
//    @Override
//    public ActionResult action(CmdArgs args) throws FileNotFoundException {
//        if (humanSet.getCollection().isEmpty()){
//            return new ActionResult(true, "Collection is empty");
//        }
//        if (BuildChecker.checkImpactSpeed(args.getArgs()[0])) {
//            String res = humanSet.filterByImpactSpeed(args.getArgs()[0]);
//            return new ActionResult(true, res);
//        } else {
//            return new ActionResult(true, "Enter the numeric value of impact speed");
//        }
//    }

    @Override
    public <K extends Serializable> String action(K[] args) throws FileNotFoundException, ValidException, InvocationTargetException, IllegalAccessException, ExecuteException {
        if (humanSet.getCollection().isEmpty()){
            return "Collection is empty";
        }
        if (BuildChecker.checkImpactSpeed(args[0].toString())) {
            return humanSet.filterByImpactSpeed(args[0].toString());
        } else {
            return "Enter the numeric value of impact speed";
        }
    }
}
