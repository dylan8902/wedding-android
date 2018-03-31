package es.anjon.dyl.wedding.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import es.anjon.dyl.wedding.R;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private static final String ARG_TEXT = "arg_text";
    private static final int DELAY = 100;
    private static final String WEDDING_DATE = "2018-04-07 13:00:00";
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN, Locale.UK);

    private TextView mTextView;
    Thread mThread;

    public static Fragment newInstance(String text) {
        Fragment frag = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        frag.setArguments(args);
        frag.setRetainInstance(true);
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

        mTextView = (TextView) view.findViewById(R.id.countdown);
        mThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(DELAY);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                countdown();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    Log.e(TAG, "When is the big day?");
                }
            }
        };
        mThread.start();
    }

    @Override
    public void onDetach() {
        mThread.interrupt();
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        mThread.interrupt();
        super.onDestroy();
    }

    /**
     * Update the countdown clock
     */
    private void countdown() {
        Date weddingDate = new Date();

        try {
            weddingDate = DATE_FORMAT.parse(WEDDING_DATE);
        } catch (ParseException e) {
            Log.e(TAG, "When is the big day?");
        }
        long difference = weddingDate.getTime() - System.currentTimeMillis();
        if (difference < 0) {
            mTextView.setVisibility(View.GONE);
        }

        String secondPlural = "";
        int seconds = (int) (difference / 1000) % 60 ;
        if ((seconds > 1) || (seconds == 0)) {
            secondPlural = "s";
        }

        String minutePlural = "";
        int minutes = (int) ((difference / (1000*60)) % 60);
        if ((minutes > 1) || (minutes == 0))  {
            minutePlural = "s";
        }

        String hourPlural = "";
        int hours = (int) ((difference / (1000*60*60)) % 24);
        if ((hours > 1) || (hours == 0))  {
            hourPlural = "s";
        }

        String dayPlural = "";
        int days = (int) ((difference / (1000*60*60*24)) % 365);
        if ((days > 1) || (days == 0))  {
            dayPlural = "s";
        }

        String countdown = String.format(Locale.UK, "%d day%s, %d hour%s, %d minute%s, %d second%s",
                days, dayPlural, hours, hourPlural, minutes, minutePlural, seconds, secondPlural);
        if (mTextView != null) {
            mTextView.setText(countdown);
        }
    }

}
