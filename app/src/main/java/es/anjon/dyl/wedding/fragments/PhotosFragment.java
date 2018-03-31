package es.anjon.dyl.wedding.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import es.anjon.dyl.wedding.R;

public class PhotosFragment extends Fragment {

    private static final String TAG = "PhotosFragment";

    public static Fragment newInstance() {
        PhotosFragment frag = new PhotosFragment();
        frag.setRetainInstance(true);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button shareButton = view.findViewById(R.id.button_share_photos);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Share button!");
                Uri url = Uri.parse(getString(R.string.photos_url));
                Intent intent = new Intent(Intent.ACTION_VIEW, url);
                startActivity(intent);
            };
        });
    }

}
