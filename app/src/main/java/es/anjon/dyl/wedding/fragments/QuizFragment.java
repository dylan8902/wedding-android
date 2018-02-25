package es.anjon.dyl.wedding.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import es.anjon.dyl.wedding.R;
import es.anjon.dyl.wedding.quiz.Result;
import es.anjon.dyl.wedding.services.Quiz;

public class QuizFragment extends Fragment {

    private static final int NO_ANSWER = -1;
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

        // TODO has the quiz already been done?
        mQuiz = new Quiz();
        mSetupView.setVisibility(View.VISIBLE);

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
            finishQuiz();
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

    private void finishQuiz() {
        mScoreView.setText(String.format(Locale.UK, "Score: %d", mQuiz.getScore()));
        mQuizView.setVisibility(View.GONE);
        mResultView.setVisibility(View.VISIBLE);
        new Result(mQuiz.getScore(), mSpinner.getSelectedItem().toString()).save();
    }

    private void toast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

}
