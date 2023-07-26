package collection;

import exceptions.ValidException;
import humans.Car;
import humans.Coordinates;
import humans.HumanBeing;

import java.util.Objects;

public class HumanValidator implements Validator<HumanBeing> {
    public boolean checkCar(Car car) {
        return !Objects.isNull(car.getName());
    }

    public boolean checkCoordinates(Coordinates coordinates) {
        return !Objects.isNull(coordinates.getX()) && coordinates.getX() <= 532;
    }

    public boolean checkElement(HumanBeing human) throws ValidException {
        boolean isValid = !Objects.isNull(human) &&
                !Objects.isNull(human.getId()) &&
                !Objects.isNull(human.getName()) &&
                !Objects.isNull(human.getCoordinates()) &&
                !Objects.isNull(human.getCreationDate()) &&
                !Objects.isNull(human.getRealHero()) &&
                !Objects.isNull(human.getHasToothpick()) &&
                !Objects.isNull(human.getImpactSpeed()) &&
                !Objects.isNull(human.getWeaponType()) &&
                !Objects.isNull(human.getMood()) &&
                !Objects.isNull(human.getCar()) &&
                this.checkCar(human.getCar()) &&
                this.checkCoordinates(human.getCoordinates());
        if (!isValid) {
            throw new ValidException("Element isn't valid");
        }
        return true;
    }
}
