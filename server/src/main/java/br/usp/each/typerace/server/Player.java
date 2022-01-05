package br.usp.each.typerace.server;


import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Player implements Comparator {
    private final String playerId;
    private int correct;
    private int wrong;
    private final Set<String> currentWords;

    public Player() {
        playerId = "";
        currentWords = new HashSet<>();
    }

    public Player(String playerId, int correct, int wrong, Set<String> currentWords) {
        this.playerId = playerId;
        this.correct = correct;
        this.wrong = wrong;
        this.currentWords = currentWords;
    }

    public void rightAnswer(){
        this.correct++;
    }

    public void wrongAnswer(){
        this.wrong++;
    }

    public String getPlayerId() {
        return playerId;
    }

    public int getCorrect() {
        return correct;
    }

    public int getWrong() {
        return wrong;
    }

    public Set<String> getCurrentWords() {
        return currentWords;
    }



    public boolean checkAnswer(String answer){
        if(currentWords.contains(answer.toLowerCase())){
            rightAnswer();
            currentWords.remove(answer);
            return true;
        }
        wrongAnswer();
        return false;
    }

    @Override
    public int compare(Object t1, Object o) {
        Player t = (Player) t1;
        Player p = (Player) o;

        if(t.correct == p.correct)
            return p.wrong - t.wrong;
        return t.correct - p.correct;
    }
}
