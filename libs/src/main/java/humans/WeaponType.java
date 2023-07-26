package humans;

import java.util.ArrayList;
import java.util.List;

public enum WeaponType {
    HAMMER(1, "Hammer - 1"),
    AXE(2, "Axe - 2"),
    SHOTGUN(3, "Shotgun - 3"),
    RIFLE(4, "Rifle - 4");
    private final int number;
    private final String name;

    WeaponType(int number, String name) {
        this.name = name;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public static WeaponType getWeaponTypeByNumber(int i) {
        for (WeaponType type : WeaponType.values()) {
            if (type.number == i) {
                return type;
            }
        }
        return null;
    }

    public static List<String> getWeaponTypeList() {
        List<String> types = new ArrayList<>();
        for (WeaponType type : WeaponType.values()) {
            types.add(type.name);
        }
        return types;
    }
}
