package ru.yandex.practicum.exceptions;

public class WordleException extends RuntimeException {
    public WordleException() {
        super();
    }

    public WordleException(String message) {
        super(message);
    }
}
