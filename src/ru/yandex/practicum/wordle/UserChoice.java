package ru.yandex.practicum.wordle;

import java.util.HashMap;
import java.util.Map;

public enum UserChoice {

    PLAY(1),
    RULES(2),
    QUIT(3);

    public static final int MIN_CHOICE = 1;
    public static final int MAX_CHOICE = 3;

    private static final Map<Integer, UserChoice> choices = new HashMap<>();

    private final int num;


    static {
        for (UserChoice choice : UserChoice.values()) {
            choices.put(choice.num, choice);
        }
    }

    UserChoice(int num) {
        this.num = num;
    }

    public static UserChoice getChoice(int num) {
        if (!choices.containsKey(num)) {
            return null;
        }

        return choices.get(num);
    }

}
