package es.anjon.dyl.wedding.models;

import android.content.SharedPreferences;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

public class Navigation {
    public static final String KEY = "navigation";
    public static final String TABLE_PLAN_KEY = "table_plan";
    public static final String QUIZ_KEY = "quiz";
    public static final String PHOTOS_KEY = "photos";

    @PropertyName("table_plan")
    public boolean tablePlan;

    public boolean quiz;
    public boolean photos;

    public Navigation() {
    }

    public boolean isTablePlan() {
        return tablePlan;
    }

    public void setTablePlan(boolean tablePlan) {
        this.tablePlan = tablePlan;
    }

    public boolean isQuiz() {
        return quiz;
    }

    public void setQuiz(boolean quiz) {
        this.quiz = quiz;
    }

    public boolean isPhotos() {
        return photos;
    }

    public void setPhotos(boolean photos) {
        this.photos = photos;
    }

    @Exclude
    public void loadPrefs(SharedPreferences prefs) {
        setTablePlan(prefs.getBoolean(TABLE_PLAN_KEY, false));
        setQuiz(prefs.getBoolean(QUIZ_KEY, false));
        setPhotos(prefs.getBoolean(PHOTOS_KEY, false));
    }

    @Exclude
    public void updatePrefs(SharedPreferences prefs) {
        prefs.edit().putBoolean(TABLE_PLAN_KEY, isTablePlan()).apply();
        prefs.edit().putBoolean(QUIZ_KEY, isQuiz()).apply();
        prefs.edit().putBoolean(PHOTOS_KEY, isPhotos()).apply();
    }

    @Override
    public String toString() {
        return "Navigation{tablePlan=" + tablePlan +
                ", quiz=" + quiz + ", photos=" + photos + '}';
    }

}
