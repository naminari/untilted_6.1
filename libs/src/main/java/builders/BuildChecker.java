package builders;

public class BuildChecker {
    private static final String WEAPON_TYPE_ENUM = "[1-4]";
    private static final String LONG_VALUE = "^-?[0-9]{1,17}$";
    private static final String DOUBLE_VALUE = "^[-+]?[0-9]*\\.?[0-9]+$";
    private static final String MOOD_ENUM = "[1-4]";
    private static final String UUID_FORMAT = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$";
    public static boolean checkId(String id) {
        return id.matches(UUID_FORMAT);
    }
    public static boolean checkWeaponType(String measure) {
        return measure.matches(WEAPON_TYPE_ENUM);
    }
    public static boolean checkXCoordinate(String x) {
        boolean l = x.matches(LONG_VALUE);
        return l && Long.parseLong(x) <= 532;
    }
    public static boolean checkYCoordinate(String y) {
        return y.length() < 14 && y.matches(DOUBLE_VALUE);
    }
    public static boolean checkMood(String color) {
        return color.matches(MOOD_ENUM);
    }

    public static boolean checkImpactSpeed(String speed) {
        return speed.matches(LONG_VALUE);
    }

}
