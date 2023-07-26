package utils;

import humans.Mood;

import java.util.ArrayList;
import java.util.List;

public enum TypeCommand {
    ADD("add"),
    ADD_IF_MIN("add_if_min"),
    CLEAR("clear"),
    COUNT_LESS_WEAPON("count_less_weapon"),
    EXECURE_SCRIPT("execute_script"),
    EXIT("exit"),
    FILTER_BY_IMPACT_SPEED("filter_by_impact_speed"),
    FILTER_GREATER_THAN_MOOD("filter_greater_than_mood"),
    HELP("help"),
    HISTORY("history"),
    INFO("info"),
    REMOVE_BY_ID("remove_by_id"),
    REMOVE_LOWER("remove_lower"),
    SAVE("save"),
    SHOW("show"),
    UPDATE("update");
    private final String name;
    TypeCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public static List<String> getCmdList() {
        List<String> cmds = new ArrayList<>();
        for (TypeCommand typeCommand : TypeCommand.values()) {
            cmds.add(typeCommand.name);
        }
        return cmds;
    }
    public static TypeCommand geTypeByName(String name) {
        for (TypeCommand command: TypeCommand.values()) {
            if (command.name.equals(name)) {
                return command;
            }
        }
        return null;
    }
}
