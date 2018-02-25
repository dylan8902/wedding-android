package es.anjon.dyl.wedding.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import es.anjon.dyl.wedding.R;
import es.anjon.dyl.wedding.services.Quiz;

public class QuizFragment extends Fragment {

    private static final String TAG = "QuizFragment";
    private static final int NO_ANSWER = -1;
    private RadioGroup mAnswer;
    private TextView mQuestionTextView;
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
        mAnswer = view.findViewById(R.id.answer);
        mQuestionTextView = view.findViewById(R.id.question);

        // TODO has the quiz already been done?
        mQuiz = new Quiz();

        Button submitButton = view.findViewById(R.id.button_quiz_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Submit button!");
                submit();
            };
        });
    }

    private void submit() {
        int answer = getAnswer();
        if (answer == NO_ANSWER) {
            // TODO prompt user to select answer
            return;
        }
        boolean correct = mQuiz.answer(answer);
        // TODO show response
        if (mQuiz.hasQuestion()) {
            nextQuestion();
        } else {
            // TODO end the quiz
        }
    }

    private int getAnswer() {
        switch(mAnswer.getCheckedRadioButtonId()) {
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
        mQuestionTextView.setText(mQuiz.getQuestion().toString());
        mAnswer.clearCheck();
    }

}
