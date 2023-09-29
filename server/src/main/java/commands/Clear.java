package commands;

import cmd.AbstractCommand;

import cmd.CmdType;
import cmd.CommandArgs;
import collection.HumanSet;
import database.DatabaseHandler;
import exceptions.ExecuteException;
import exceptions.ValidException;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class Clear extends AbstractCommand {
    private HumanSet humanSet;

    public Clear(HumanSet humanSet){
        super("clear", "очистить коллекцию", CmdType.NO_ARGS);
        this.humanSet = humanSet;
    }

    @Override
    public <K extends Serializable> String action(CommandArgs<K> args) {
        try {
            DatabaseHandler handler = args.getHandler();
            int userId = args.getUserId();
            List<Integer> ids = handler.getHumansByUserId(userId);
            for (int id : ids) {
                handler.removeHuman(id);
                humanSet.removeById(id);
            }
            return "Your products were deleted";
        } catch (SQLException e) {
            return e.getMessage();
        }
    }
    }

//    public String action() {
//        humanSet.clear();
//
//        ///return new ActionResult(true, "Successfully cleared collection");
//    }

