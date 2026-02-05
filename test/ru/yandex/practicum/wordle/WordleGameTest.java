package ru.yandex.practicum.wordle;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.WordleUserInputException;

import java.io.PrintWriter;
import java.util.Set;

public class WordleGameTest {


    private static PrintWriter writer;
    private static WordleDictionary dict;

    private WordleGame game;

    @BeforeAll
    public static void createDictionary() {
        writer = new PrintWriter(System.out);
        Set<String> words = Set.of("a".repeat(Wordle.WORD_LENGTH),
                                   "b".repeat(Wordle.WORD_LENGTH),
                                   "c".repeat(Wordle.WORD_LENGTH),
                                   "d".repeat(Wordle.WORD_LENGTH),
                                   "e".repeat(Wordle.WORD_LENGTH));
        dict = new WordleDictionary(words, writer);
    }

    @BeforeEach
    public void createGame(){
        game = new WordleGame(dict, writer);
    }

    @Test
    public void throwsExceptionIfGuessIsOfWrongLength() {
        String guess = "a".repeat(Wordle.WORD_LENGTH + 1);
        Assertions.assertThrows(WordleUserInputException.class, () -> game.processGuess(guess));
    }

    @Test
    public void throwsExceptionIfGuessIsNotInDictionary() {
        String guess = "z".repeat(Wordle.WORD_LENGTH);
        Assertions.assertThrows(WordleUserInputException.class, () -> game.processGuess(guess));
    }

    @Test
    public void doesNotThrowExceptionIfGuessIsEmpty() {
        String guess = "";
        Assertions.assertDoesNotThrow(() -> game.processGuess(guess));
    }

    @Test
    public void replacesEmptyGuessWithHintFromDictionary() {
        String guess = "";
        game.processGuess(guess);
        Assertions.assertTrue(dict.has(game.getGuess()));
    }

    @Test
    public void gameIsOverIfGuessMatchesAnswer() {
        String guess = game.getAnswer();
        game.processGuess(guess);
        Assertions.assertTrue(game.isOver());
    }

    @Test
    public void gameIsOverWhenAttemptsReachMax() {

        String answer = game.getAnswer();
        String guess;

        do {
            guess = dict.getRandomWord();
        } while (guess.equals(answer));

        for (int i = 0; i < Wordle.MAX_ATTEMPTS; i++) {
            game.processGuess(guess);
        }

        Assertions.assertTrue(game.isOver());
    }

    @Test
    public void gameEndsInVictoryIfGuessMatchesAnswer() {
        String guess = game.getAnswer();
        game.processGuess(guess);
        Assertions.assertTrue(game.isVictory());
    }

    @Test
    public void gameEndsInDefeatIsAttemptsReachMaxAndWordNotGuessed() {
        String answer = game.getAnswer();
        String guess;

        do {
            guess = dict.getRandomWord();
        } while (guess.equals(answer));

        for (int i = 0; i < Wordle.MAX_ATTEMPTS; i++) {
            game.processGuess(guess);
        }

        Assertions.assertFalse(game.isVictory());
    }

    @Test
    public void shouldCorrectlyMarkLettersInFeedback() {
        Set<String> singleWordSet = Set.of("apple");
        WordleDictionary singleWordDictionary = new WordleDictionary(singleWordSet, writer);
        game = new WordleGame(singleWordDictionary, writer);

        // answer = "apple"
        game.setGuess("abbbb");
        game.processFeedback(new WordleWordTracker());
        String expectedFeedback = "+----";
        Assertions.assertEquals(expectedFeedback, game.getFeedback());

        // answer = "apple"
        game.setGuess("babbb");
        game.processFeedback(new WordleWordTracker());
        expectedFeedback = "-^---";
        Assertions.assertEquals(expectedFeedback, game.getFeedback());

        // answer = "apple"
        game.setGuess("pbppb");
        game.processFeedback(new WordleWordTracker());
        expectedFeedback = "^-+--";
        Assertions.assertEquals(expectedFeedback, game.getFeedback());
    }

}
