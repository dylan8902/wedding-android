package es.anjon.dyl.wedding.quiz;

public class Question {
    private String text;
    private int answer;

    public Question(String text, int answer) {
        this.text = text;
        this.answer = answer;
    }

    public int getAnswer() {
        return answer;
    }

    public String toString() {
        return text;
    }
}
