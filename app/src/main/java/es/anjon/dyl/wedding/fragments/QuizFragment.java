package es.anjon.dyl.wedding.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.anjon.dyl.wedding.R;
import es.anjon.dyl.wedding.adapters.QuizResultAdapter;
import es.anjon.dyl.wedding.models.QuizTableResult;
import es.anjon.dyl.wedding.models.Result;
import es.anjon.dyl.wedding.services.Quiz;

public class QuizFragment extends Fragment {

    private static final String TAG = "QuizFragment";
    private static final int NO_ANSWER = -1;
    private static final int NO_SCORE = -1;
    private static final String SCORE = "Score";
    private Query tableScoresRef;
    private ChildEventListener childEventListener;
    private RadioGroup mAnswerView;
    private TextView mQuestionView;
    private TextView mScoreView;
    private Button mStart;
    private Button mSubmit;
    private LinearLayout mSetupView;
    private LinearLayout mQuizView;
    private LinearLayout mResultView;
    private Spinner mSpinner;
    private Quiz mQuiz;
    private SharedPreferences mPrefs;
    private RecyclerView mTableScores;

    public static Fragment newInstance() {
        return new QuizFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAnswerView = view.findViewById(R.id.answer);
        mQuestionView = view.findViewById(R.id.question);
        mScoreView = view.findViewById(R.id.score);
        mSetupView = view.findViewById(R.id.quiz_setup);
        mStart = view.findViewById(R.id.button_quiz_start);
        mSubmit = view.findViewById(R.id.button_quiz_submit);
        mQuizView = view.findViewById(R.id.quiz);
        mResultView = view.findViewById(R.id.results);
        mSpinner = view.findViewById(R.id.table);
        mTableScores = view.findViewById(R.id.table_scores);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        int score = mPrefs.getInt(SCORE, NO_SCORE);
        if (score != NO_SCORE) {
            finishQuiz(score);
        } else {
            setupQuiz();
        }

        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAnswer();
            }
        });
    }

    private void setupQuiz() {
        mQuiz = new Quiz();
        mSetupView.setVisibility(View.VISIBLE);
    }

    private void startQuiz() {
        nextQuestion();
        mSetupView.setVisibility(View.GONE);
        mQuizView.setVisibility(View.VISIBLE);
    }

    private void submitAnswer() {
        int answer = getAnswer();
        if (answer == NO_ANSWER) {
            toast(getString(R.string.quiz_no_answer));
            return;
        }
        mQuiz.answer(answer);
        if (mQuiz.hasQuestion()) {
            nextQuestion();
        } else {
            mPrefs.edit().putInt(SCORE, mQuiz.getScore()).apply();
            new Result(mQuiz.getScore(), mSpinner.getSelectedItem().toString()).save();
            finishQuiz(mQuiz.getScore());
        }
    }

    private int getAnswer() {
        switch(mAnswerView.getCheckedRadioButtonId()) {
            case R.id.alice:
                return Quiz.ALICE;
            case R.id.dylan:
                return Quiz.DYLAN;
            case R.id.both:
                return Quiz.BOTH;
            default:
                return NO_ANSWER;
        }
    }

    private void nextQuestion() {
        mQuestionView.setText(mQuiz.getQuestion().toString());
        mAnswerView.clearCheck();
    }

    private void finishQuiz(int finalScore) {
        mScoreView.setText(String.format(Locale.UK, "Score: %d", finalScore));
        mQuizView.setVisibility(View.GONE);
        mResultView.setVisibility(View.VISIBLE);
        mTableScores.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mTableScores.setLayoutManager(layoutManager);
        final List<QuizTableResult> quizTableResults = new ArrayList<>();
        final List<String> quizTableKeys = new ArrayList<>();
        final QuizResultAdapter adapter = new QuizResultAdapter(quizTableResults);
        mTableScores.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        tableScoresRef = database.getReference(Result.QUIZ_KEY).child(Result.TABLES_KEY);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                QuizTableResult tableResult = dataSnapshot.getValue(QuizTableResult.class);
                tableResult.setKey(dataSnapshot.getKey());
                quizTableResults.add(tableResult);
                quizTableKeys.add(dataSnapshot.getKey());
                adapter.notifyItemInserted(quizTableResults.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
                QuizTableResult tableResult = dataSnapshot.getValue(QuizTableResult.class);
                tableResult.setKey(dataSnapshot.getKey());
                int tableResultIndex = quizTableKeys.indexOf(dataSnapshot.getKey());
                if (tableResultIndex > -1) {
                    quizTableResults.set(tableResultIndex, tableResult);
                    adapter.notifyItemChanged(tableResultIndex);
                } else {
                    Log.w(TAG, "onChildChanged:unknown_child:" + dataSnapshot.getKey());
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
                int tableResultIndex = quizTableKeys.indexOf(dataSnapshot.getKey());
                if (tableResultIndex > -1) {
                    quizTableKeys.remove(tableResultIndex);
                    quizTableResults.remove(tableResultIndex);
                    adapter.notifyItemRemoved(tableResultIndex);
                } else {
                    Log.w(TAG, "onChildRemoved:unknown_child:" + dataSnapshot.getKey());
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        };
        tableScoresRef.addChildEventListener(childEventListener);
    }

    private void toast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (childEventListener != null) {
            tableScoresRef.removeEventListener(childEventListener);
        }
    }

}
