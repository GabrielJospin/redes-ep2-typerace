package br.usp.each.typerace.server;


import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
/**
 * The class to use with the Player in the game
 * */
public class Player implements Comparator {
    private final String playerId;
    private int correct;
    private int wrong;
    private final Set<String> currentWords;

    public Player() {
        playerId = "";
        currentWords = new HashSet<>();
    }

    /**
     * @param playerId: String, the name of the player,
     * @param correct: int, the number of corrects words
     * @param wrong: int, the number of wrong words
     * @param currentWords: Set of Strong, the list of current words
     * */
    public Player(String playerId, int correct, int wrong, Set<String> currentWords) {
        this.playerId = playerId;
        this.correct = correct;
        this.wrong = wrong;
        this.currentWords = currentWords;
    }

    /**
     * up the number of right answers
     * */
    public void rightAnswer(){
        this.correct++;
    }

    /**
     * up the number of wrong answers
     * */
    public void wrongAnswer(){
        this.wrong++;
    }

    /**
     * get the player id
     *
     * @return String, playerId
     * */
    public String getPlayerId() {
        return playerId;
    }

    /**
     * get the number of correct words
     *
     * @return int, correct
     * */
    public int getCorrect() {
        return correct;
    }

    /**
     * get the number of wrong words
     *
     * @return int, wrong
     * */
    public int getWrong() {
        return wrong;
    }

    /**
     * get the current words
     *
     * @return Set of String, correct
     * */
    public Set<String> getCurrentWords() {
        return currentWords;
    }


    /**
     * check if the answer is right and up the right or wrong answers value and if is right, remove form set
     *
     * @return boolean, if the answer is right
     * */
    public boolean checkAnswer(String answer){
        if(currentWords.contains(answer.toLowerCase())){
            rightAnswer();
            currentWords.remove(answer);
            return true;
        }
        wrongAnswer();
        return false;
    }

    /**
     * method to compare the players by correct answers
     * */
    @Override
    public int compare(Object t1, Object o) {
        Player t = (Player) t1;
        Player p = (Player) o;

        if(t.correct == p.correct)
            return p.wrong - t.wrong;
        return t.correct - p.correct;
    }
}
