package es.anjon.dyl.wedding;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import es.anjon.dyl.wedding.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TABLE_PLAN_KEY = "table_plan";
    private static final String QUIZ_KEY = "quiz";
    private static final String PHOTOS_KEY = "photos";
    private static final int TABLE_PLAN_ID = 201;
    private static final int QUIZ_ID = 203;
    private static final int PHOTOS_ID = 204;

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
                frag = HomeFragment.newInstance(getString(R.string.title_map));
                break;
        }

        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, frag, frag.getTag());
            ft.commit();
        }
    }

}
