package ru.yandex.practicum.wordle;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WordleTest {

    @Test
    public void shouldTrimInputWhenNormalizing() {
        String input = "слово ";
        String output = Wordle.normalizeGuess(input);
        String expectedOutput = "слово";

        Assertions.assertEquals(expectedOutput, output);
    }

    @Test
    public void shouldReturnLowerCaseWhenNormalizingInput() {
        String input = "СЛОВО";
        String output = Wordle.normalizeGuess(input);
        String expectedOutput = "слово";

        Assertions.assertEquals(expectedOutput, output);
    }

    @Test
    public void shouldReplaceLetterEWhenNormalizingInput() {
        String input = "котёл";
        String output = Wordle.normalizeGuess(input);
        String expectedOutput = "котел";

        Assertions.assertEquals(expectedOutput, output);
    }

}
