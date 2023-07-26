package humans;

import java.util.ArrayList;
import java.util.List;

public enum Mood {
    LONGING(1, "Longing - 1"),
    APATHY(2, "Apathy - 2"),
    CALM(3, "Calm - 3"),
    FRENZY(4, "Frenzy - 4");
    private final int number;
    private final String name;

    Mood(int number, String name) {
        this.name = name;
        this.number = number;
    }

    public static Mood getMoodByNumber(int i) {
        for (Mood mood : Mood.values()) {
            if (mood.number == i) {
                return mood;
            }
        }
        return null;
    }

    public static List<String> getMoodList() {
        List<String> moods = new ArrayList<>();
        for (Mood mood : Mood.values()) {
            moods.add(mood.name);
        }
        return moods;
    }

    public int getNumber() {
        return number;
    }
}
