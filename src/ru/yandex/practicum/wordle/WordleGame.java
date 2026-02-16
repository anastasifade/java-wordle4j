package ru.yandex.practicum.wordle;

import ru.yandex.practicum.exceptions.WordleUserInputException;

import java.io.PrintWriter;
import java.util.*;

import static ru.yandex.practicum.wordle.Wordle.CORRECT_LETTER_CORRECT_SPOT;
import static ru.yandex.practicum.wordle.Wordle.CORRECT_LETTER_WRONG_SPOT;
import static ru.yandex.practicum.wordle.Wordle.WRONG_LETTER;

public class WordleGame {

    private static final Random random = new Random();

    private static final String HINT = "";

    private final PrintWriter writer;
    private final WordleDictionary dictionary;
    private final List<String> validWords;
    private final String answer;


    private String feedback;
    private String guess;
    private boolean isOver;
    private boolean victory;
    private int steps;




    public WordleGame(final WordleDictionary dictionary, final PrintWriter writer) {
        this.writer = writer;
        this.dictionary = dictionary;
        validWords = new ArrayList<>(dictionary.getWords());
        answer = dictionary.getRandomWord();
        feedback = "";
        guess = "";
        victory = false;
        isOver = false;
        steps = 0;
    }


    public void processGuess(String guess) throws WordleUserInputException {

        this.guess = guess;
        validateGuess();

        if (guess.equals(HINT)) {
            this.guess = validWords.get(random.nextInt(validWords.size()));
        }

        WordleWordTracker tracker = new WordleWordTracker();
        processFeedback(tracker);
        updateWords(tracker);

        steps++;
        if (steps >= Wordle.MAX_ATTEMPTS) {
            isOver = true;
        }

    }

    // TODO: изменить метод на private после завершения этапа тестирования
    public void processFeedback(WordleWordTracker tracker) {

        // если слово угадано верно, алгоритм проверки можно пропустить
        if (guess.equals(answer)) {
            feedback = CORRECT_LETTER_CORRECT_SPOT.repeat(Wordle.WORD_LENGTH);
            isOver = true;
            victory = true;
            return;
        }

        char[] answerLetters = answer.toCharArray();
        char[] guessLetters = guess.toCharArray();
        String[] feedbackArray = new String[Wordle.WORD_LENGTH];

        for (int i = 0; i < guessLetters.length; i++) {

            // проверка на совпадение букв на верной позиции
            if (answerLetters[i] == guessLetters[i]) {
                feedbackArray[i] = CORRECT_LETTER_CORRECT_SPOT;
                tracker.confirmPosition(guessLetters[i], i, true);

                // после успешной проверки буква "удаляется" из списка букв загаданного слова,
                // чтобы последующие буквы не были по ошибке отмечены символом "^"
                answerLetters[i] = 0;

                // после успешной проверки буква "удаляется" из списка букв введенного пользователем слова или подсказки,
                // чтобы предотвратить возникновение ошибок при второй итерации по списку букв
                guessLetters[i] = 0;

                continue;
            }

            if (answer.indexOf(guessLetters[i]) < 0) {
                feedbackArray[i] = WRONG_LETTER;
                tracker.exclude(guessLetters[i]);
                guessLetters[i] = 0;
            }
        }

        for (int i = 0; i < guessLetters.length; i++) {
            if (guessLetters[i] == 0) continue;

            // поиск возможных совпадений буквы на других позициях в загаданном слове
            boolean matchFound = false;
            for (int j = 0; j < Wordle.WORD_LENGTH; j++) {
                if (answerLetters[j] == 0) continue;

                if (guessLetters[i] == answerLetters[j]) {
                    feedbackArray[i] = CORRECT_LETTER_WRONG_SPOT;
                    tracker.confirmPosition(guessLetters[i], i, false);
                    answerLetters[j] = 0;
                    guessLetters[i] = 0;
                    matchFound = true;
                    break;
                }
            }

            // обработка граничных случаев - если остались буквы, не отмеченные как обработанные в слове при
            // проверке (гарантированные совпадения, гарантированно отсутствующие в слове), но без обнаруженной пары -
            // это повторы ранее угаданных букв
            // программа не должна повторно предалагать слова с такой буквой в данном положении,
            // при этом буква не должна попасть в список исключенных букв

            if (!matchFound) {
                feedbackArray[i] = WRONG_LETTER;
                tracker.confirmPosition(guessLetters[i], i, false);
            }
        }

        StringBuilder feedbackBuilder = new StringBuilder();
        for (String element : feedbackArray) {
            feedbackBuilder.append(element);
        }

        feedback = feedbackBuilder.toString();
    }

    private void updateWords(WordleWordTracker tracker) {
        validWords.removeIf(s -> !tracker.isValidWord(s));
    }

    private void validateGuess() throws WordleUserInputException {

        if (guess.equals(HINT)) return;

        if (guess.length() != Wordle.WORD_LENGTH) {
            throw new WordleUserInputException(String.format("В слове должно быть %d букв. Попробуйте снова.",
                                                                                                 Wordle.WORD_LENGTH));
        }

        if (!dictionary.has(guess)) {
            throw new WordleUserInputException("Такого слова мы не знаем. Попробуйте другое.");
        }
    }

    // ГЕТТЕРЫ

    public String getAnswer() {
        return this.answer;
    }

    public String getGuess() {
        return this.guess;
    }

    public String getFeedback() {
        return this.feedback;
    }

    public int getSteps() {
        return this.steps;
    }

    public boolean isVictory() {
        return this.victory;
    }

    public boolean isOver() {
        return this.isOver;
    }

    // СЕТТЕРЫ - созданы для упрощения тестирования

    public void setGuess(String guess) {
        this.guess = guess;
    }

}
