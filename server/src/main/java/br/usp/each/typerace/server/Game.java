package br.usp.each.typerace.server;

import com.opencsv.exceptions.CsvException;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
/**
 * class with the Rules of the game
 * This class will store and organize all the game
 */

public class Game {

    private static final String PATH_TO_SETTINGS = "settings.json";

    private final int maxScore;
    private final int maxPlayer;
    private final int numberOfWords;
    private final Map<String, Player> scoreBoard;
    private final Set<String> words = new HashSet<>();
    private Game.Status status;

    public enum Status {START, RUNNING, FINISHED }

    /**
     * Config with the Json Object in settings.json
     * set maxScore, maxPlayer, numberOfWords
     * and set status to Status.START
     * */
    public Game(){

        JSONObject json = getJson(PATH_TO_SETTINGS);
        this.maxScore = json.getInt("maxScore");
        this.maxPlayer = json.getInt("maxPlayer");
        this.numberOfWords = json.getInt("numberOfWords");
        this.scoreBoard = new HashMap<>();
        this.status = Status.START;
    }

    /**
     * init the game
     * and get the words of this game
     *
     * @param path: the Path to .csv used in {@link br.usp.each.typerace.server.GerateList}
     * @throws IOException: if it can't read the file
     * @throws CsvException: if the .csv has an error
     * */
    public void init(String path) throws IOException, CsvException {
        GerateList gl = new GerateList(path, numberOfWords);
        words.addAll(gl.getList());
    }

    /**
     * Add a Player in list of players, if the number of playes is over the max
     * @param id: The name of the player
     * @return boolean: false, if can't be add player, and true, if it can.
     *
     * */
    public boolean addPlayer(String id){
        if(maxPlayer == scoreBoard.size())
            return false;
        Player player = new Player(id, 0, 0, words);
        scoreBoard.put(id, player);
        return true;
    }

    /** Remove the player from the id
     *
     * @param id: the nemme of the player
     *
     * */
    public void removePlayer(String id){
        Player removed = scoreBoard.get(id);
        scoreBoard.remove(id, removed);
    }

    /**
     * get the list of all players
     *
     * @return List of Player playerList;
     * */
    public List<Player> getPlayers(){
        List<Player> playerList = new ArrayList<Player>(scoreBoard.values());
        return playerList.stream().sorted().collect(Collectors.toList());
    }

    /**
     * check if the answer is right and updateStatus to know if the game is and
     * @return if the answer is right
     * */
    public boolean checkAnswer(String id, String answer){
        boolean back = scoreBoard.get(id).checkAnswer(answer);
        updateStatus();
        return back;
    }

    /**
     * see if the number of rights is equal at the number of words of the maxScore is lower than the number of games
     * */
    public void updateStatus(){
        scoreBoard.forEach((id, player) -> {
            if(player.getCorrect() == numberOfWords ||
                    player.getCorrect() + player.getWrong() == getMaxScore())
                this.status = Status.FINISHED;
        });
    }

    /**
     * get the maxScore (the limit of games)
     * @return int maxScore
     * */
    public int getMaxScore() {
        return maxScore;
    }

    /**
     * get the max of players
     * @return int maxPlayer
     * */
    public int getMaxPlayer() {
        return maxPlayer;
    }

    /**
     * get the words of the game
     *
     * @return Set of String words;
     * */
    public Set<String> getWords() {
        return words;
    }

    /**
     * return if the game is finished
     *
     * @return boolean status == Finished
     * */
    public boolean isGameFinished() {
        return status.equals(Status.FINISHED);
    }

    /**
     * get status
     *
     * @return enum Status
     * */
    public Status getStatus() {
        return status;
    }

    /**
     * Set the enum status
     *
     * @param status: Enum of status in Game.Status{START, RUNNING, FINISHED }
     * */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * get a JSON object
     * @param path: The string to the path
     * @return JSONObject json
     * */
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
