package br.usp.each.typerace.server;

import com.opencsv.exceptions.CsvException;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.chrono.ChronoLocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Game {

    private static final String PATH_TO_SETTINGS = "settings.json";

    private final int maxScore;
    private final int maxPlayer;
    private final int numberOfWords;
    private final Map<String, Player> scoreBoard;
    private final Set<String> words = new HashSet<>();
    private boolean isGameFinished;

    public Game(Map<String, Player> scoreBoard) throws IOException, CsvException {

        JSONObject json = getJson(PATH_TO_SETTINGS);
        this.maxScore = json.getInt("maxScore");
        this.maxPlayer = json.getInt("maxPlayer");
        this.numberOfWords = json.getInt("numberOfWords");
        this.scoreBoard = new HashMap<>();
        this.isGameFinished = false;
    }

    public void init(String path) throws IOException, CsvException {
        GerateList gl = new GerateList(path, numberOfWords);
        words.addAll(gl.getList());
    }

    public boolean addPlayer(String id){
        if(maxPlayer == scoreBoard.size())
            return false;
        Player player = new Player(id, 0, 0, words);
        scoreBoard.put(id, player);
        return true;
    }

    public Player removePlayer(String id){
        Player removed = scoreBoard.get(id);
        scoreBoard.remove(id, removed);
        return removed;
    }

    public List<Player> getPlayers(){
        List<Player> playerList = (List<Player>) scoreBoard.values();
        return playerList.stream().sorted().collect(Collectors.toList());
    }

    public boolean checkAnswer(String id, String answer){
        return scoreBoard.get(id).checkAnswer(answer);
    }

    public void updateStatus(){
        scoreBoard.forEach((id, player) -> {
            if(player.getCorrect() + player.getWrong() == this.maxScore)
                this.isGameFinished = true;
        });
    }

    public int getMaxScore() {
        return maxScore;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public Set<String> getWords() {
        return words;
    }

    public boolean isGameFinished() {
        return isGameFinished;
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

        return new JSONObject(content).getJSONObject("Game");
    }



}
