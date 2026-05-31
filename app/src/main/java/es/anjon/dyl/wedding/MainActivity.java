package es.anjon.dyl.wedding;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.anjon.dyl.wedding.fragments.HomeFragment;
import es.anjon.dyl.wedding.fragments.PhotosFragment;
import es.anjon.dyl.wedding.fragments.QuizFragment;
import es.anjon.dyl.wedding.fragments.TablePlanFragment;
import es.anjon.dyl.wedding.models.Navigation;
import es.anjon.dyl.wedding.services.Database;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MainActivity";
    private static final int HOME_ID = 201;
    private static final int MAP_ID = 202;
    private static final int TABLE_PLAN_ID = 203;
    private static final int QUIZ_ID = 204;
    private static final int PHOTOS_ID = 205;
    private static final LatLng ST_MARYS = new LatLng(51.473842,-3.172077);
    private static final LatLng ST_DAVIDS = new LatLng(51.4605074,-3.1672796);
    private Navigation mNav = new Navigation();

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
        final Menu menu = navigation.getMenu();
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mNav.loadPrefs(prefs);
        updateNavigation(menu);

        FirebaseDatabase firebaseDatabase = Database.getDatabase();
        firebaseDatabase.getReference(Navigation.KEY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange:" + dataSnapshot.getKey());
                mNav = dataSnapshot.getValue(Navigation.class);
                mNav.updatePrefs(prefs);
                updateNavigation(menu);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled:", databaseError.toException());
            }
        });
    }

    private void selectFragment(MenuItem item) {
        Fragment frag = null;

        switch (item.getItemId()) {
            case HOME_ID:
                frag = HomeFragment.newInstance(getString(R.string.title_home));
                break;
            case MAP_ID:
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

    /**
     * Update the navigation menu
     * @param menu the menu to update
     */
    private void updateNavigation(Menu menu) {
        Log.i(TAG, "updateNavigation:" + mNav.toString());
        menu.clear();
        menu.add(Menu.NONE, HOME_ID, Menu.NONE, getString(R.string.title_home))
                .setIcon(R.drawable.ic_home_black_24dp);
        menu.add(Menu.NONE, MAP_ID, Menu.NONE, getString(R.string.title_map))
                .setIcon(R.drawable.ic_map_black_24dp);

        if (mNav.isTablePlan()) {
            menu.add(Menu.NONE, TABLE_PLAN_ID, Menu.NONE, getString(R.string.title_table_plan))
                    .setIcon(R.drawable.ic_restaurant_menu_black_24dp);
        }

        if (mNav.isQuiz()) {
            menu.add(Menu.NONE, QUIZ_ID, Menu.NONE, getString(R.string.title_quiz))
                    .setIcon(R.drawable.ic_quiz_black_24dp);
        }

        if (mNav.isPhotos()) {
            menu.add(Menu.NONE, PHOTOS_ID, Menu.NONE, getString(R.string.title_photos))
                    .setIcon(R.drawable.ic_photos_black_24dp);
        }
        selectFragment(menu.getItem(0));
    }

}
