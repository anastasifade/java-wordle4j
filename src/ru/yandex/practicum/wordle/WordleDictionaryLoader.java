package ru.yandex.practicum.wordle;

import ru.yandex.practicum.exceptions.WordleDictionaryException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {

    private final File dictionary;
    private final PrintWriter writer;

    public WordleDictionaryLoader(File dictionary, PrintWriter writer) {
        this.dictionary = dictionary;
        this.writer = writer;
    }

    public WordleDictionary loadDictionary() throws WordleDictionaryException {
        return new WordleDictionary(getWords(), writer);
    }

    private Set<String> getWords() throws WordleDictionaryException {
        Set<String> words = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(dictionary, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() == Wordle.WORD_LENGTH) {
                    if (line.contains("ё")) {
                        line = line.replace("ё", "e");
                    }
                    words.add(line);
                }
            }
            return words;

        } catch (FileNotFoundException e) {
            throw new WordleDictionaryException("ОШИБКА РАБОТЫ ПРОГРАММЫ.\n" +
                      "Ошибка при работе со словарем - файл не найден: " + dictionary.getPath());
        } catch (IOException e) {
            throw new WordleDictionaryException("ОШИБКА РАБОТЫ ПРОГРАММЫ.\n" +
                    "Ошибка при работе со словарем - ошибка при чтении файла: " + dictionary.getPath());
        } catch (WordleDictionaryException e) {
            e.printStackTrace(writer);
            writer.flush();

            // повторный вызов ошибки для выхода из программы, т.к. продолжение работы без словаря невозможно
            throw e;
        }

    }
}
