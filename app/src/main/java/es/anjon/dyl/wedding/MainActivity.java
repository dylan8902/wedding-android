package es.anjon.dyl.wedding;

import android.content.SharedPreferences;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import es.anjon.dyl.wedding.fragments.HomeFragment;
import es.anjon.dyl.wedding.fragments.PhotosFragment;
import es.anjon.dyl.wedding.fragments.QuizFragment;
import es.anjon.dyl.wedding.fragments.TablePlanFragment;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MainActivity";
    private static final String TABLE_PLAN_KEY = "table_plan";
    private static final String QUIZ_KEY = "quiz";
    private static final String PHOTOS_KEY = "photos";
    private static final int TABLE_PLAN_ID = 201;
    private static final int QUIZ_ID = 203;
    private static final int PHOTOS_ID = 204;
    private static final LatLng ST_MARYS = new LatLng(51.473842,-3.172077);
    private static final LatLng ST_DAVIDS = new LatLng(51.4605074,-3.1672796);

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

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInAnonymously().addOnCompleteListener(
                this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            Log.d(TAG, "signInAnonymously:success " + user.toString());
                        } else {
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                        }
                    }
                });

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
                    .setIcon(R.drawable.ic_restaurant_menu_black_24dp);
        }

        if (prefs.getBoolean(QUIZ_KEY, false)) {
            menu.add(Menu.NONE, QUIZ_ID, Menu.NONE, getString(R.string.title_quiz))
                    .setIcon(R.drawable.ic_quiz_black_24dp);
        }

        if (prefs.getBoolean(PHOTOS_KEY, false)) {
            menu.add(Menu.NONE, PHOTOS_ID, Menu.NONE, getString(R.string.title_photos))
                    .setIcon(R.drawable.ic_photos_black_24dp);
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
            case TABLE_PLAN_ID:
                frag = TablePlanFragment.newInstance();
                break;
            case QUIZ_ID:
                frag = QuizFragment.newInstance();
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
        googleMap.addMarker(new MarkerOptions().position(ST_MARYS)
                .title("St Mary's Church"));
        googleMap.addMarker(new MarkerOptions().position(ST_DAVIDS)
                .title("The Principal St David's Hotel"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ST_MARYS, 14));
    }

}
