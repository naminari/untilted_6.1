package collection;

import exceptions.ValidException;
import humans.Car;
import humans.Coordinates;
import humans.HumanBeing;

import java.util.Objects;

public class SimpleHumanValidator implements Validator<HumanBeing> {
    @Override
    public boolean checkElement(HumanBeing object) throws ValidException {
        boolean check = !Objects.isNull(object.getName()) &&
                !object.getName().isEmpty() &&
                checkCoordinates(object.getCoordinates()) &&
                !Objects.isNull(object.getCreationDate()) &&
                !Objects.isNull(object.getRealHero()) &&
                !Objects.isNull(object.getHasToothpick()) &&
                !Objects.isNull(object.getImpactSpeed()) &&
                !Objects.isNull(object.getWeaponType()) &&
                !Objects.isNull(object.getMood()) &&
                checkCar(object.getCar());
        if (!check) {
            System.out.println("simple product valid exception");
            throw new ValidException("Element isn't valid"); /// этор лютый прикол тут может вылететь ошибка
        }
        return true;
    }
    public boolean checkCoordinates(Coordinates coordinates) {
        return !Objects.isNull(coordinates) &&
                !Objects.isNull(coordinates.getX()) &&
                coordinates.getX() <= 532;
    }

    public boolean checkCar(Car car) {
        return !Objects.isNull(car) &&
                !Objects.isNull(car.getName()) &&
                !car.getName().isEmpty();
    }
}
