package ru.yandex.practicum.wordle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordleWordTracker {

    private final Map<Integer, Character> correctPositions;
    private final Map<Character, Set<Integer>> wrongPositions;
    private final Set<Character> confirmedLetters;
    private final Set<Character> excludedLetters;

    public WordleWordTracker() {
        correctPositions = new HashMap<>();
        wrongPositions = new HashMap<>();
        confirmedLetters = new HashSet<>();
        excludedLetters  = new HashSet<>();
    }

    public void confirmPosition(char letter, int position, boolean isCorrectPosition) {
        confirmedLetters.add(letter);
        if (isCorrectPosition) {
            correctPositions.put(position, letter);
        } else {
            if (!wrongPositions.containsKey(letter)) {
                wrongPositions.put(letter, new HashSet<>());
            }
            wrongPositions.get(letter).add(position);
        }
    }

    public void confirm(char letter) {
        confirmedLetters.add(letter);
    }

    public void exclude(char letter) {
        excludedLetters.add(letter);
    }

    public boolean isValidWord(String word) {
        return !(containsExcludedLetters(word)) &&
                containsAllConfirmedLetters(word) &&
                !(hasLettersAtWrongPosition(word)) &&
                hasLettersAtCorrectPositions(word);
    }

    private boolean containsAllConfirmedLetters(String word) {

        if (confirmedLetters.isEmpty()) return true;

        for (Character letter : confirmedLetters) {
            if (word.indexOf(letter) < 0) return false;
        }

        return true;
    }

    private boolean containsExcludedLetters(String word) {

        if (excludedLetters.isEmpty()) return false;

        for (char letter : word.toCharArray()) {
            if (excludedLetters.contains(letter)) return true;
        }

        return false;
    }

    private boolean hasLettersAtCorrectPositions(String word) {

        if (correctPositions.isEmpty()) return true;

        for (Integer position : correctPositions.keySet()) {
            if (word.charAt(position) != correctPositions.get(position)) {
                return false;
            }
        }

        return true;
    }

    private boolean hasLettersAtWrongPosition(String word) {

        if (wrongPositions.isEmpty()) return false;

        for (int i = 0; i < Wordle.WORD_LENGTH; i++) {
            char letter = word.charAt(i);
            if (wrongPositions.containsKey(letter)) {
                if (wrongPositions.get(letter).contains(i)) {
                    return true;
                }
            }
        }
        return false;
    }

}
