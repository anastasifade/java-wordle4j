package ru.yandex.practicum.wordle;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class WordleDictionaryTest {

    private static WordleDictionary dict;

    @BeforeAll
    public static void createDictionary() {
        Set<String> words = Set.of("a", "b", "c");
        dict = new WordleDictionary(words, new PrintWriter(System.out));
    }

    @Test
    public void shouldReturnTrueIfHasWord() {
        Assertions.assertTrue(dict.has("a"));
    }

    @Test
    public void shouldReturnFalseIfDoesNotHaveWord() {
        Assertions.assertFalse(dict.has("d"));
    }

    @Test
    public void shouldReturnWordFromDictionaryWhenCallingGetRandomWord() {
        String word = dict.getRandomWord();
        Assertions.assertTrue(dict.has(word));
    }

    @Test
    public void modifyingSetFromGetWordsShouldNotModifyDictionaryWords() {
        Set<String> copy = new HashSet<>(dict.getWords());
        copy.remove("a");
        copy.add("d");

        Assertions.assertTrue(dict.has("a"));
        Assertions.assertFalse(dict.has("d"));
    }

}
