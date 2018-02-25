package es.anjon.dyl.wedding.services;

import java.util.ArrayList;
import java.util.List;

import es.anjon.dyl.wedding.quiz.Question;

public class Quiz {

    public static int ALICE = 1;
    public static int DYLAN = 2;
    public static int BOTH = 3;
    private List<Question> questions;
    private int score;
    private int questionCounter;

    public Quiz() {
        this.questions = new ArrayList<>();
        this.questions.add(new Question("Who hitch hiked to Morocco?", BOTH));
        this.questions.add(new Question("Who has grade 1 Clarinet?", ALICE));
        this.questions.add(new Question("Who hates Baked Beans?", DYLAN));
        this.questions.add(new Question("Who was born in England?", BOTH));
        this.questions.add(new Question("Who jumped out of a plane?", BOTH));
        this.questions.add(new Question("Who has a degree from York University?", ALICE));
        this.questions.add(new Question("Who is a published poet?", DYLAN));
        this.questions.add(new Question("Who has been on Safari?", DYLAN));
        this.questions.add(new Question("Who has an innie belly button?", BOTH));
        this.questions.add(new Question("Who has been to San Francisco?", ALICE));
        this.questions.add(new Question("Who doesn't drink tea or coffee?", DYLAN));
        this.questions.add(new Question("Who sings in a choir?", BOTH));
        this.questions.add(new Question("Who has appeared in a Bollywood music video with over 100 million views on YouTube?", ALICE));
        this.questions.add(new Question("Who is a big Star Wars fan?", ALICE));
        this.questions.add(new Question("Who proposed?", DYLAN));
        this.score = 0;
        this.questionCounter = 0;
    }

    public Question getQuestion() {
        return questions.get(questionCounter);
    }

    public boolean answer(int answer) {
        if (answer == questions.get(questionCounter).getAnswer()) {
            score++;
            questionCounter++;
            return true;
        } else {
            questionCounter++;
            return false;
        }
    }

    public int getScore() {
        return score;
    }

    public boolean hasQuestion() {
        return getQuestion() != null;
    }

}
