package br.usp.each.typerace.server;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Game {

    private static final String PATH_TO_SETTINGS = "settings.json";

    private final int maxScore;
    private final int maxPlayer;
    private final int numberOfWords;
    private final Map<String, Player> scoreBoard;
    private boolean isGameFinished;
    private long timeInit;
    private long timeEnd;

    public Game(Map<String, Player> scoreBoard, boolean isGameFinished, long timeInit, long timeEnd) {

        JSONObject json = getJson(PATH_TO_SETTINGS);
        this.maxScore = json.getInt("maxScore");
        this.maxPlayer = json.getInt("maxPlayer");
        this.numberOfWords = json.getInt("numberOfWords");
        this.scoreBoard = scoreBoard;
        this.isGameFinished = isGameFinished;
        this.timeInit = timeInit;
        this.timeEnd = timeEnd;
    }

    @Contract("_ -> new")
    public static @NotNull JSONObject getJson(String path){
        File file = new File(path);
        String content = "";

        try {
            content = FileUtils.readFileToString(file,"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JSONObject(content);
    }


}
