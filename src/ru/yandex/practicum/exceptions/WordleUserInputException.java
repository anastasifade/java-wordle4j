package ru.yandex.practicum.exceptions;

public class WordleUserInputException extends RuntimeException {

    public WordleUserInputException() {
        super();
    }

    public WordleUserInputException(String message) {
        super(message);
    }
}
