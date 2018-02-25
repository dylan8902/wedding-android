package es.anjon.dyl.wedding.quiz;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Result {

    private static final String QUIZ_KEY = "quiz";
    private static final String TABLES_KEY = "tables";
    private int score;
    private String table;

    public Result() {

    }

    public Result(int score, String table) {
        this.score = score;
        this.table = table;
    }

    public int getScore() {
        return score;
    }

    public String getTable() {
        return table;
    }

    @Exclude
    public void save() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mTablesRef = mDatabase.getReference(QUIZ_KEY).child(TABLES_KEY);
        DatabaseReference mResultRef = mTablesRef.child(getTable()).push();
        mResultRef.setValue(this);
        Log.i("Result", "Result has been saved as " + mResultRef.getKey());
    }

}
