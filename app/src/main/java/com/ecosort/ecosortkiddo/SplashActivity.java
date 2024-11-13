package com.ecosort.ecosortkiddo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ecosort.ecosortkiddo.R;
import com.ecosort.ecosortkiddo.dao.ProfileDao;
import com.ecosort.ecosortkiddo.dao.SettingsDao;

public class SplashActivity extends AppCompatActivity {

    private ImageView imageView;
    private MyDBHelper dbHelper;

    private void initializeDependencies(){
        // Initialize the MyDBHelper
        dbHelper = new MyDBHelper(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use the utility class to enable fullscreen and immersive modes
        Hide_Navigation.enableFullscreen(this);
        Hide_Navigation.enableTrueImmersiveMode(this); // Or UIUtils.enableImmersiveMode(this);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        initializeDependencies();

        imageView = findViewById(R.id.imageView);

        //dbHelper.copyDatabase();

        // Applying fade-in animation to the ImageView
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        imageView.startAnimation(fadeIn);

        // Using Handler to delay transition to MainActivity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Applying fade-out animation
            Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
            imageView.startAnimation(fadeOut);

            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    // Ensure the new activity starts only after fade-out ends
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    // Applying fade-in and fade-out animations for activity transition
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    // Finish the splash activity
                    finish();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
        }, 3000); // Duration of delay

        // Apply window insets to view
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
    }
}
