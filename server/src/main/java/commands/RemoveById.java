package commands;

import builders.BuildChecker;
import cmd.AbstractCommand;
import cmd.CmdType;
import cmd.CommandArgs;
import collection.HumanSet;
import database.DatabaseHandler;
import exceptions.ExecuteException;
import exceptions.ValidException;
import humans.HumanBeing;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;
@Slf4j
public class RemoveById extends AbstractCommand {
    HumanSet humanSet;
    public RemoveById(HumanSet humanSet){
        super("remove_by_id", "удалить элемент из коллекции по его id", CmdType.SIMPLE_ARG);
        this.humanSet = humanSet;
    }
//    @Override
//    public ActionResult action(CmdArgs args) {
//        if (humanSet.getCollection().isEmpty()){
//            return new ActionResult(true, "Collection is empty");
//        }
//        if (BuildChecker.checkId(args.getArgs()[0])) {
//            humanSet.removeById(args.getArgs()[0]);
//            return new ActionResult(true, "Successfully deleted element with id " + Arrays.toString(args.getArgs()));
//        } else {
//            return new ActionResult(true, "No such element");
//        }
//    }
    @Override
    public <K extends Serializable> String action(CommandArgs<K> args) throws FileNotFoundException, ValidException, InvocationTargetException, IllegalAccessException, ExecuteException {
        Integer id = null;
        for (Serializable arg : args.getArgs()){
            if (arg instanceof UUID){
                id = (Integer) arg;
            }
        }
        DatabaseHandler handler = args.getHandler();
        int userId = args.getUserId();

        try {
            if (handler.isOwner(id, userId)) {
                handler.removeHuman(id);
                if (humanSet.removeById(id)) {
                    log.info(String.format("%s was deleted successfully", id));
                    return "Delete successfully";
                } else {
                    log.warn("Element with this id wasn't found");
                    return "Element with this id wasn't found";
                }
            } else {
                log.warn(String.format("Permission denied, user - %d try to delete element %d", userId, id));
                return "Permission denied (You aren't a owner of this product)";
            }
        } catch (SQLException e) {
            log.error(e.getSQLState(), e);
            return e.getMessage();
        }
//        if (humanSet.removeById(id)){
//            return "delete successfully";
//        } else {
//            return "element with this id wasn't found";
//        }
    }
}