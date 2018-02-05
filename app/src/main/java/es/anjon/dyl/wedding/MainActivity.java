package es.anjon.dyl.wedding;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import es.anjon.dyl.wedding.fragments.HomeFragment;
import es.anjon.dyl.wedding.fragments.PhotosFragment;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MainActivity";
    private static final String TABLE_PLAN_KEY = "table_plan";
    private static final String QUIZ_KEY = "quiz";
    private static final String PHOTOS_KEY = "photos";
    private static final int TABLE_PLAN_ID = 201;
    private static final int QUIZ_ID = 203;
    private static final int PHOTOS_ID = 204;
    private static final LatLng ST_MARYS = new LatLng(51.4707221,-3.1772467);
    private static final LatLng ST_DAVIDS = new LatLng(51.46055,-3.1692467);

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            selectFragment(item);
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navigation.getMenu();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //TODO move to notification handler
        prefs.edit().putBoolean(TABLE_PLAN_KEY, true).apply();
        prefs.edit().putBoolean(QUIZ_KEY, true).apply();
        prefs.edit().putBoolean(PHOTOS_KEY, true).apply();

        if (prefs.getBoolean(TABLE_PLAN_KEY, false)) {
            menu.add(Menu.NONE, TABLE_PLAN_ID, Menu.NONE, getString(R.string.title_table_plan))
                    .setIcon(R.drawable.ic_home_black_24dp);
        }

        if (prefs.getBoolean(QUIZ_KEY, false)) {
            menu.add(Menu.NONE, QUIZ_ID, Menu.NONE, getString(R.string.title_quiz))
                    .setIcon(R.drawable.ic_home_black_24dp);
        }

        if (prefs.getBoolean(PHOTOS_KEY, false)) {
            menu.add(Menu.NONE, PHOTOS_ID, Menu.NONE, getString(R.string.title_photos))
                    .setIcon(R.drawable.ic_home_black_24dp);
        }

        selectFragment(navigation.getMenu().getItem(0));
    }

    private void selectFragment(MenuItem item) {
        Fragment frag = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                frag = HomeFragment.newInstance(getString(R.string.title_home));
                break;
            case R.id.navigation_map:
                frag = SupportMapFragment.newInstance();
                ((SupportMapFragment) frag).getMapAsync(this);
                break;
            case PHOTOS_ID:
                frag = PhotosFragment.newInstance();
                break;
        }

        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, frag, frag.getTag());
            ft.commit();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        googleMap.addMarker(new MarkerOptions().position(ST_MARYS)
                .title("St Mary's Church"));
        googleMap.addMarker(new MarkerOptions().position(ST_DAVIDS)
                .title("The Principal St David's Hotel"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ST_MARYS, 14));
    }

}
