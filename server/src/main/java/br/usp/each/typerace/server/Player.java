package br.usp.each.typerace.server;


import java.util.Set;

public class Player implements Comparable {
    private final String playerId;
    private int correct;
    private int wrong;
    private final Set<String> currentWords;

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


    @Override
    public int compareTo(Object o) {
        Player p = (Player) o;
        if(this.correct == p.correct)
            return p.wrong - this.wrong;
        return this.correct - p.correct;
    }

    public boolean checkAnswer(String answer){
        if(currentWords.contains(answer.toLowerCase())){
            rightAnswer();
            currentWords.remove(answer);
            return true;
        }

        return false;
    }
}
