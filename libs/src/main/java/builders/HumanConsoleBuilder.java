package builders;

import humans.*;
import utils.UserInput;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.UUID;

public class HumanConsoleBuilder {
    private final HumanBeing human;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public HumanConsoleBuilder(HumanBeing human) {
        this.human = human;
    }

    public void setId() {
        human.setId(UUID.randomUUID());
    }

    public void setName() {
        System.out.println("Enter human's name");
        String humanName = UserInput.input().trim();
        human.setName(humanName);
    }

    public void setCoordinates() {
        Long x = null;
        String y = "";

        while (Objects.isNull(x)) {
            System.out.println("Enter human's coordinate x, P.S: long number <= 532");
            String lineX = UserInput.input().trim();
            if (BuildChecker.checkXCoordinate(lineX)) {
                x = Long.parseLong(lineX);
                if (x > 532) {
                    System.out.println("More than 532");
                    x = null;
                    continue;
                }
                while (y.equals("")) {
                    System.out.println("Enter human's coordinate y, P.S: float number");
                    String lineY = UserInput.input().trim();
                    if (BuildChecker.checkYCoordinate(lineY)) {
                        y = lineY;
                    } else {
                        System.out.println("Uncorrect input");
                    }
                }
            } else {
                System.out.println("Uncorrect input");
            }
        }
        this.human.setCoordinates(new Coordinates(x, Float.parseFloat(y)));
    }

    public void setCreationDate() {
        LocalDateTime localDateTime = null;
        while (Objects.isNull(localDateTime)) {
            System.out.println("Enter human's creation date, P.S: \"yyyy-MM-dd HH:mm:ss\"");
            try {
                localDateTime = LocalDateTime.parse(UserInput.input().trim(), formatter);
            } catch (DateTimeParseException e) {
                System.out.println(e.getMessage());
            }
        }
        human.setCreationDate(localDateTime);
    }

    public void setRealHero() {
        Boolean flag = null;
        while (Objects.isNull(flag)) {
            System.out.println("Enter human's realHero flag: t/f");
            String line = UserInput.input().trim();
            if (line.equals("t")) {
                flag = true;
            } else if (line.equals("f")) {
                flag = false;
            }
        }
        human.setRealHero(flag);
    }

    public void setToothPick() {
        Boolean flag = null;
        while (Objects.isNull(flag)) {
            System.out.println("Does human have toothpick? Enter: t/f");
            String line = UserInput.input().trim();
            if (line.equals("t")) {
                flag = true;
            } else if (line.equals("f")) {
                flag = false;
            }
        }
        human.setHasToothpick(flag);
    }

    public void setImpactSpeed() {
        Long x = null;

        while (Objects.isNull(x)) {
            System.out.println("Enter human's impact speed, P.S: long number");
            String lineX = UserInput.input().trim();
            if (BuildChecker.checkXCoordinate(lineX)) {
                x = Long.parseLong(lineX);
            } else {
                System.out.println("Uncorrect input");
            }
        }
        this.human.setImpactSpeed(x);
    }

    public void setWeaponType() {
        WeaponType type = null;
        while (Objects.isNull(type)) {
            System.out.println("Choose a number, which matches with weapon type:");
            System.out.println(WeaponType.getWeaponTypeList());
            String value = UserInput.input().trim();
            if (BuildChecker.checkWeaponType(value)) {
                type = WeaponType.getWeaponTypeByNumber(Integer.parseInt(value));
            } else {
                System.out.println("Uncorrect input");
            }
        }
        this.human.setWeaponType(type);
    }

    public void setMood() {
        Mood mood = null;
        while (Objects.isNull(mood)) {
            System.out.println("Choose a number, which matches with mood:");
            System.out.println(Mood.getMoodList());
            String value = UserInput.input().trim();
            if (BuildChecker.checkMood(value)) {
                mood = Mood.getMoodByNumber(Integer.parseInt(value));
            } else {
                System.out.println("Uncorrect input");
            }
        }
        this.human.setMood(mood);
    }

    public void setCar() {
        System.out.println("Enter human's car name");
        String carName = UserInput.input().trim();
        human.setCar(new Car(carName));
    }

    public HumanBeing getHuman() {
        return human;
    }
}
