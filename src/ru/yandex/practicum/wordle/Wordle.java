package ru.yandex.practicum.wordle;

import ru.yandex.practicum.exceptions.WordleDictionaryException;
import ru.yandex.practicum.exceptions.WordleUserInputException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Wordle {

    private static final File logs = new File("log.txt");
    private static final File words = new File("words_ru.txt");
    private static final Scanner scanner = new Scanner(System.in);

    public static final String CORRECT_LETTER_CORRECT_SPOT = "+";
    public static final String CORRECT_LETTER_WRONG_SPOT = "^";
    public static final String WRONG_LETTER = "-";

    public static final int WORD_LENGTH = 5;
    public static final int MAX_ATTEMPTS = 6;

    private static WordleGame game;

    public static void main(String[] args) {

        try (PrintWriter writer = new PrintWriter(logs)) {

            WordleDictionaryLoader dictLoader = new WordleDictionaryLoader(words, writer);
            WordleDictionary dictionary = dictLoader.loadDictionary();

            boolean gameRunning = true;
            while (gameRunning) {
                try {
                    game = new WordleGame(dictionary, writer);
                    try {
                        gameRunning = gameLoop();
                    } catch (WordleUserInputException e) {
                        System.out.println(e.getMessage());
                    }
                } catch (Exception e) {
                    writer.println("НЕПРЕДВИДЕННАЯ ОШИБКА.");
                    e.printStackTrace(writer);
                    writer.flush();
                }
            }
        } catch (WordleDictionaryException e) {
            System.out.println("Критическая ошибка при загрузке программы. Продолжение работы программы невозможно." +
                               "Подробности в логе.");
        } catch (FileNotFoundException e) {
            System.out.println("Критическая ошибка при загрузке программы. Продолжение работы программы невозможно.");
            e.printStackTrace();
        } finally {
            separator();
            System.out.println("Работа программы завершена.");
        }
    }

    private static boolean gameLoop() {
        printMenu();
        return processCommand(getChoice());
    }


    private static boolean processCommand(UserChoice choice) {
        switch (choice) {
            case PLAY -> play();
            case RULES -> printRules();
            case QUIT -> {
                return false;
            }
        }

        return true;
    }

    private static void play() {
        separator();
        System.out.println("Игра началась.");

        while (!game.isOver()) {
            System.out.printf("Попытка %d/%d:", game.getSteps() + 1, MAX_ATTEMPTS);
            String guess = guess();
            try {
                game.processGuess(guess);
                System.out.println(game.getGuess());
                System.out.println(game.getFeedback());
            } catch (WordleUserInputException e) {
                System.out.println(e.getMessage());
            }
        }

        if (game.isVictory()) {
            System.out.println("Победа!");
        } else {
            System.out.println("Быть может, повезет в другой раз.");
        }
    }

    // Методы для вывода информации на экран.

    private static void printMenu() {
        separator();
        System.out.println("WORDLE");
        System.out.println("1. Играть.");
        System.out.println("2. Правила.");
        System.out.println("3. Выход.");
        System.out.print("Введите команду:");
    }

    private static void printRules() {
        separator();
        System.out.print("""
                            ПРАВИЛА:
                            Цель данной игры - угадать случайно загадонное слово длиной 5 символов.
                            Вам будет предложено ввести любое слово указанной длины.
                            После ввода Вам будет предоставлен набор из 5 символов, который позволит Вам понять,
                            насколько Вы были близки к верному ответу.
                            Значения символов представлены ниже:
                            """.stripIndent());
        System.out.printf("[%s] - буква указана верно, на верной позиции.\n", CORRECT_LETTER_CORRECT_SPOT);
        System.out.printf("[%s] - буква присутствует в загаданном слове, но на другой позиции.\n",
                                                                            CORRECT_LETTER_WRONG_SPOT);
        System.out.printf("[%s] - буква отсутствует в загаданном слове.\n", WRONG_LETTER);
        System.out.println("Количество попыток в игре ограничено: всего можно совершить не более 6 попыток на одно слово.");
        System.out.println("Если Вы не можете придумать подходящее слово, попросите программу Вам помочь:\n" +
                           "нажмите ПРОБЕЛ, и программа выберет подходящее слово за Вас.");
        System.out.print("Нажмите ENTER, чтобы вернуться в главное меню:");
        scanner.nextLine();
    }

    public static void separator() {
        System.out.println("_".repeat(20) + "\n");
    }

    // Методы для получения пользовательского ввода

    private static UserChoice getChoice() throws WordleUserInputException {
        try {
            int num = scanner.nextInt();
            if (num < UserChoice.MIN_CHOICE || num > UserChoice.MAX_CHOICE) {
                throw new WordleUserInputException(String.format("Введенный номер команды не поддерживается. Ожидается значение в диапазоне [%d-%d].\n",
                        UserChoice.MIN_CHOICE, UserChoice.MAX_CHOICE));
            }
            return UserChoice.getChoice(num);
        } catch (InputMismatchException e) {
            throw new WordleUserInputException(String.format("Некорректный формат ввода команды. Ожидается численное значение [%d-%d].\n",
                    UserChoice.MIN_CHOICE, UserChoice.MAX_CHOICE));
        } finally {
            scanner.nextLine();
        }
    }

    private static String guess()  {
        String guess = scanner.nextLine().trim().toLowerCase();
        return normalizeGuess(guess);
    }

    public static String normalizeGuess(String guess) {
        if (guess.contains("ё")) {
            guess = guess.replace("ё", "е");
        }
        return guess.trim().toLowerCase();
    }



}
