package es.anjon.dyl.wedding.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class QuizTableResult {

    String key;
    Map<String, Result> results = new HashMap<>();

    public QuizTableResult() {

    }

    public Map<String, Result> getResults() {
        return results;
    }

    public void setResults(Map<String, Result> results) {
        this.results = results;
    }

    @Exclude
    public String getTable() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    @Exclude
    public String getScore() {
        double score = 0;
        for(Result result : results.values()) {
            score += result.getScore();
        }
        return String.format("%.1f", score / results.size());
    }

    @Override
    public String toString() {
        return "QuizTableResult{ table='" + getTable() + "', results='" + results + "'}";
    }

}
