package es.anjon.dyl.wedding.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import es.anjon.dyl.wedding.R;

public class HomeFragment extends Fragment {
    private static final String ARG_TEXT = "arg_text";

    private String mText;

    private View mContent;
    private TextView mTextView;

    public static Fragment newInstance(String text) {
        Fragment frag = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // retrieve text from bundle or savedInstanceState
        if (savedInstanceState == null) {
            Bundle args = getArguments();
            mText = args.getString(ARG_TEXT);
        } else {
            mText = savedInstanceState.getString(ARG_TEXT);
        }

        // initialize views
        mContent = view.findViewById(R.id.fragment_home);
        mTextView = (TextView) view.findViewById(R.id.message);
        mTextView.setText(mText);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(ARG_TEXT, mText);
        super.onSaveInstanceState(outState);
    }

}
