package builders;

import collection.Validator;
import com.fasterxml.uuid.Generators;
import exceptions.ValidException;
import humans.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

public class ArgsHumanBuilder {
    private final HumanBeing human = new HumanBeing();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void setId() {
        UUID uuid = Generators.timeBasedGenerator().generate();
        human.setId(uuid);
    }
    @Order(1)
    public void setName(String name) throws ValidException {
        if (name == null || name.isEmpty()) {
            throw new ValidException("Uncorrect input (product's name)");
        }
        human.setName(name);
    }

    @Order(2)
    public void setCoordinates(String x, String y) throws ValidException {
        if (!BuildChecker.checkXCoordinate(x) || !BuildChecker.checkYCoordinate(y)) {
            throw new ValidException("Uncorrect input (product's coordinates)");
        }
        human.setCoordinates(new Coordinates(Long.parseLong(x), Float.parseFloat(y)));
    }

    @Order(3)
    public void setCreationDate(String date, String time) throws ValidException {
        String datetime = String.format("%s %s", date, time);
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(datetime, formatter);
            human.setCreationDate(localDateTime);
        } catch (DateTimeParseException e) {
            throw new ValidException(e.getMessage());
        }
    }

    @Order(4)
    public void setRealHero(String realHero) throws ValidException {
        boolean realHeroFlag = false;
        if (realHero.equals("t")){
            realHeroFlag = true;
        } else if (!realHero.equals("f")) {
            throw new ValidException("Uncorrected input (Real hero availability?)");
        }
        human.setRealHero(realHeroFlag);
    }
    @Order(5)
    public void setHasToothpick(String toothpick) throws ValidException {
        boolean toothpickFlag = false;
        if (toothpick.equals("t")){
            toothpickFlag = true;
        } else if (!toothpick.equals("f")) {
            throw new ValidException("Uncorrected input (Toothpick availability?)");
        }
        human.setRealHero(toothpickFlag);
    }
    @Order(6)
    public void setImpactSpeed(String speed) throws ValidException {
        if (!BuildChecker.checkImpactSpeed(speed)) {
            throw new ValidException("Uncorrected input (product's coordinates)");
        }
        human.setImpactSpeed(Long.parseLong(speed));
    }
    @Order(7)
    public void setWeaponType(String weaponType) throws ValidException {
        if (!BuildChecker.checkWeaponType(weaponType)) {
            throw new ValidException("Uncorrected input (weapon_type-enum)");
        }
        human.setWeaponType(WeaponType.getWeaponTypeByNumber(Integer.parseInt(weaponType)));
    }
    @Order(8)
    public void setMood(String mood) throws ValidException {
        if (!BuildChecker.checkMood(mood)) {
            throw new ValidException("Uncorrected input (mood-enum)");
        }
        human.setMood(Mood.getMoodByNumber(Integer.parseInt(mood)));
    }
    @Order(9)
    public void setCar(String car) throws ValidException {
        if (car == null || car.isEmpty()) {
            throw new ValidException("Uncorrected input (human car's name)");
        }
        human.setCar(new Car(car));
    }

    public HumanBeing getHuman(Validator<HumanBeing> validator) throws ValidException {
        return validator.checkElement(human) ? human : null;
    }
}
