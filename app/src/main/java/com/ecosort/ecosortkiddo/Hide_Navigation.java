package com.ecosort.ecosortkiddo;

import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Hide_Navigation {

    /**
     * Enable fullscreen mode for the given activity.
     * @param activity The activity for which to enable fullscreen mode.
     */
    public static void enableFullscreen(AppCompatActivity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    /**
     * Enable immersive mode for the given activity.
     * @param activity The activity for which to enable immersive mode.
     */
    public static void enableImmersiveMode(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    /**
     * Enable true immersive mode for the given activity, hiding both the status bar and navigation bar.
     * @param activity The activity for which to enable true immersive mode.
     */
    public static void enableTrueImmersiveMode(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}