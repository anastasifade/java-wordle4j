package ru.yandex.practicum.wordle;

import ru.yandex.practicum.exceptions.WordleDictionaryException;

import java.io.PrintWriter;
import java.util.*;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */

public class WordleDictionary {

    private static final Random random = new Random();

    private final Set<String> words;
    private final String[] wordsArray;

    public WordleDictionary(Set<String> words, PrintWriter writer) {
        try {
            if (words == null) {
                throw new WordleDictionaryException("ОШИБКА РАБОТЫ ПРОГРАММЫ.\nОшибка при cоздании словаря - список слов пуст.");
            }
        } catch (WordleDictionaryException e) {
            e.printStackTrace(writer);
            writer.flush();

            // повторный вызов ошибки для выхода из программы, т.к. продолжение работы без словаря невозможно
            throw e;
        }

        this.words = words;
        this.wordsArray = words.toArray(new String[0]);
    }

    public String getRandomWord() {
        return wordsArray[random.nextInt(words.size())];
    }

    public boolean has(String word) {
        return words.contains(word);
    }

    public Set<String> getWords() {
        return Set.copyOf(words);
    }


}
