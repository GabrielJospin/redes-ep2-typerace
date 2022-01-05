package br.usp.each.typerace.server;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GerateList {

    private final String path;
    private final int numberOfWord;
    private final Set<String> list = new HashSet<>();

    public GerateList(String path, int numberOfWord) throws IOException, CsvException {
        this.path = path;
        this.numberOfWord = numberOfWord;

        CSVReader reader = new CSVReader(new FileReader(path));
        List<String> allWords = new ArrayList<>();

        reader.readAll().forEach(word -> {
            allWords.add(word[0]);
        });

        Random random = new Random();

        for(int i = 0; i < numberOfWord; i++) {
            int pos = random.nextInt(allWords.size());
            list.add(allWords.get(pos));
        }

    }

    public String getPath() {
        return path;
    }

    public int getNumberOfWord() {
        return numberOfWord;
    }

    public Set<String> getList() {
        return list;
    }
}
