package commands;

import builders.BuildChecker;
import cmd.AbstractCommand;

import cmd.CmdType;
import collection.HumanSet;
import exceptions.ExecuteException;
import exceptions.ValidException;
import humans.WeaponType;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class CountLessWeapon extends AbstractCommand {
    HumanSet humanSet;
    public CountLessWeapon(HumanSet humanSet){
        super("count_less_than_weapon_type", "вывести количество элементов, значение поля weaponType которых меньше заданного", CmdType.SIMPLE_ARG);
        this.humanSet = humanSet;
    }
//    @Override
//    public ActionResult action(CmdArgs args) throws FileNotFoundException {
//        if (humanSet.getCollection().isEmpty()){
//            return new ActionResult(true, "Collection is empty");
//        }
//        if (BuildChecker.checkWeaponType(args.getArgs()[0])) {
//            WeaponType weaponType = WeaponType.getWeaponTypeByNumber(Integer.parseInt(args.getArgs()[0]));
//            int res = humanSet.countLessWeapon(weaponType);
//            return new ActionResult(true, Integer.toString(res));
//        }else {
//            return new ActionResult(true, "Uncorrected input, enter a number from 1 to 4 for the corresponding Enum values");
//        }
//    }

    @Override
    public <K extends Serializable> String action(K[] args) throws FileNotFoundException, ValidException, InvocationTargetException, IllegalAccessException, ExecuteException {
        if (humanSet.getCollection().isEmpty()){
            return "Collection is empty";
        }
        Optional<Integer> weapon = Optional.empty();
        for (K arg : args) {
            if (arg instanceof Integer) {
                weapon = Optional.of((Integer) arg);
            }

        if (BuildChecker.checkWeaponType(weapon.toString())) {
            WeaponType weaponType = WeaponType.getWeaponTypeByNumber(Integer.parseInt(weapon.toString()));
            int res = humanSet.countLessWeapon(weaponType);
            return Integer.toString(res);
        }else {
            return "Uncorrected input, enter a number from 1 to 4 for the corresponding Enum values";
        }
    }
        return "кринж";
}
}
