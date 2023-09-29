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

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
@Slf4j
public class Update extends AbstractCommand {
    private final HumanSet humanSet;

    public Update(HumanSet humanSet) {
        super("update", "обновить значение элемента коллекции, id которого равен заданному", CmdType.SIMPLE_ARG);
        this.humanSet = humanSet;
    }

    @Override
    public <K extends Serializable> String action(CommandArgs<K> args) throws ValidException, InvocationTargetException, IllegalAccessException {
        Optional<HumanBeing> product = Optional.empty();
        Optional<Integer> prodId = Optional.empty();
        for (Serializable arg : args.getArgs()) {
            if (arg instanceof HumanBeing) {
                product = Optional.of((HumanBeing) arg);
            } else if (arg instanceof Integer) {
                prodId = Optional.of((Integer) arg);
            }
        }

        DatabaseHandler handler = args.getHandler();
        int userId = args.getUserId();
        try {
            if (handler.checkById(prodId.orElseThrow(() -> new ExecuteException("You enter empty id")))) {

                if (handler.isOwner(prodId.get(), userId)) {
                    handler.updateHuman(product.orElseThrow(() -> new ExecuteException("You product isn't valid")), prodId.get());
                    humanSet.removeById(prodId.get());
                    HumanBeing humanBeing = product.get();
                    humanBeing.setId(prodId.get());
                    humanSet.add(humanBeing);
                    log.info(String.format("Element with id %d was updated", prodId.get()));
                    return String.format("Element with id %d was updated", prodId.get());
                } else {
                    log.warn(String.format("Permission denied, user - %d try to delete element %d", userId, prodId.get()));
                    return "Permission denied (You aren't a owner of this product)";
                }

            } else {
                return "Product with this id wasn't found";
            }
        } catch (SQLException | ExecuteException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }
        //        String str = null;
//        for (K arg : args) {
//            if (arg instanceof String) {
//                str = (String) arg;
//            }
//
//            if (str != null && BuildChecker.checkId(str)) {
//                for (HumanBeing humanBeing : humanSet.getCollection()) {
//                    if (humanBeing.getId().toString().equals(str)) {
//                        HumanBeing human = humanSet.getHumanDirector().buildHuman();
//                        human.setId(UUID.fromString(str));
//                        return "element of collection been update";
//                    } else {
//                        return "no such element";
//                    }
//                }
//            }
//            return "Uncorrected UUID input";
//
//        }
//        return "кринж";
//    }
}