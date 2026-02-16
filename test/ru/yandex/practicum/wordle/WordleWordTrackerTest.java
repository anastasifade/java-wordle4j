package ru.yandex.practicum.wordle;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WordleWordTrackerTest {

    private WordleWordTracker tracker;

    @BeforeEach
    public void createTracker() {
        tracker = new WordleWordTracker();
    }

    @Test
    public void shouldReturnTrueIfWordHasNoExcludedLetters() {
        tracker.exclude('a');
        Assertions.assertTrue(tracker.isValidWord("bbbbb"));
    }

    @Test
    public void shouldReturnFalseIsWordHasExcludedLetters() {
        tracker.exclude('a');
        Assertions.assertFalse(tracker.isValidWord("abcde"));
    }

    @Test
    public void shouldReturnTrueIfWordHasAllConfirmedLetters() {
        tracker.confirm('a');
        tracker.confirm('b');
        Assertions.assertTrue(tracker.isValidWord("abcde"));
    }

    @Test
    public void shouldReturnFalseIfWordHasOnlySomeOfTheConfirmedLetters() {
        tracker.confirm('a');
        tracker.confirm('b');
        Assertions.assertFalse(tracker.isValidWord("bbbbb"));
    }

    @Test
    public void shouldReturnFalseIfWordHasNoConfirmedLetters() {
        tracker.confirm('a');
        tracker.confirm('b');
        Assertions.assertFalse(tracker.isValidWord("ccccc"));
    }

    @Test
    public void shouldReturnTrueIfWordHasCorrectLetterAtConfirmedPosition() {
        tracker.confirmPosition('a', 0, true);
        Assertions.assertTrue(tracker.isValidWord("abcde"));
    }

    @Test
    public void shouldReturnFalsIfWordHasWrongLetterAtConfirmedPosition() {
        tracker.confirmPosition('a', 0, true);
        Assertions.assertFalse(tracker.isValidWord("edcba"));
    }

    @Test
    public void shouldProcessLettersAtdWrongPositionsCorrectly() {
        tracker.confirmPosition('a', 0, false);
        Assertions.assertTrue(tracker.isValidWord("edcba"));
        Assertions.assertFalse(tracker.isValidWord("abcde"));
    }

    @Test
    public void shouldProcessAllChecksCorrectly() {
        tracker.confirmPosition('a', 0, true);
        tracker.confirmPosition('b', 2, false);
        tracker.exclude('f');

        Assertions.assertTrue(tracker.isValidWord("abcde"));
        Assertions.assertFalse(tracker.isValidWord("fcbda"));
    }

}
