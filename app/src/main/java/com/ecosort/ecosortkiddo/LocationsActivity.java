package com.ecosort.ecosortkiddo;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ecosort.ecosortkiddo.MainActivity;
import com.ecosort.ecosortkiddo.R;
import com.ecosort.ecosortkiddo.dao.SettingsDao;
import com.ecosort.ecosortkiddo.dao.StarRatingDao;
import com.ecosort.ecosortkiddo.model.Location;
import com.ecosort.ecosortkiddo.model.Settings;
import com.ecosort.ecosortkiddo.model.StarRating;
import com.ecosort.ecosortkiddo.utils.TranslatorUtil;

public class LocationsActivity extends AppCompatActivity {

//    private SoundManager soundManager; // Declare the SoundManager instance
    private FrameLayout house_frame, backyard_frame, forest_frame, beach_frame;
    private Settings settings;
    private SettingsDao settingsDao;
    private MediaPlayer mysound;
    private boolean isSoundOn = true;
    private StarRatingDao starRatingDao;

    private void initializeDependencies(){
        settingsDao = new SettingsDao(this);
        starRatingDao = new StarRatingDao(this);

        mysound = MediaPlayer.create(this, R.raw.buttons);
        mysound.setLooping(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use the utility class to enable fullscreen and immersive modes
        Hide_Navigation.enableFullscreen(this);
        Hide_Navigation.enableImmersiveMode(this);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_locations);

        initializeDependencies();

        settings = settingsDao.getSettings(1);
        toggleSound(settings.getSound() == 1 ? true : false);

        locationFrame();
        System.out.print("Hi");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        translateText(settings.getLanguage());
    }

    private void translateText(String languageCode){
        //kunin ung mga element na kailangan itranslate
        TextView txtSelectLocation = findViewById(R.id.txt_select_location);
        txtSelectLocation.setText(TranslatorUtil.translate(txtSelectLocation.getText().toString(), languageCode));

        TextView txtHouse = findViewById(R.id.txt_house);
        txtHouse.setText(TranslatorUtil.translate(txtHouse.getText().toString(), languageCode));

        TextView txtBackyard = findViewById(R.id.txt_backyard);
        txtBackyard.setText(TranslatorUtil.translate(txtBackyard.getText().toString(), languageCode));

        TextView txtForest = findViewById(R.id.txt_forest);
        txtForest.setText(TranslatorUtil.translate(txtForest.getText().toString(), languageCode));

        TextView txtBeach = findViewById(R.id.txt_beach);
        txtBeach.setText(TranslatorUtil.translate(txtBeach.getText().toString(), languageCode));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        //NavUtils.navigateUpTo(this, intent);
        super.onBackPressed();
    }

    private void locationFrame() {

        house_frame = findViewById(R.id.frame_house);
        house_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ClickListener", "House frame clicked");
                playButtonSound();
                Intent intent = new Intent(LocationsActivity.this, House_Location_Activity.class);
                startActivity(intent);
            }
        });

        backyard_frame = findViewById(R.id.frame_backyard);
        backyard_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButtonSound();
                Intent intent = new Intent(LocationsActivity.this, Backyard_Location_Activity.class);
                startActivity(intent);
            }
        });


//        backyard_frame = findViewById(R.id.frame_backyard);
//        int starsCountInHomeLocation = starRatingDao.getTotalStarsByProfileIdAndLocationId(1, Location.HOME);
//        if(starsCountInHomeLocation == StarRating.TOTAL_STARS_HOME){
//        backyard_frame.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                playButtonSound();
//                Intent intent = new Intent(LocationsActivity.this, Backyard_Location_Activity.class);
//                startActivity(intent);
//            }
//        });
//        }else {
//            backyard_frame.setAlpha(0.6f);
//            backyard_frame.setEnabled(false);
//        }


        forest_frame = findViewById(R.id.frame_forest);
        forest_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButtonSound();
                Intent intent = new Intent(LocationsActivity.this, Forest_Location_Activity.class);
                startActivity(intent);
            }
        });

//        forest_frame = findViewById(R.id.frame_forest);
//        int starsCountInBackyardLocation = starRatingDao.getTotalStarsByProfileIdAndLocationId(1, Location.BACKYARD);
//        if(starsCountInBackyardLocation == StarRating.TOTAL_STARS_BACKYARD) {
//            forest_frame.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    playButtonSound();
//                    Intent intent = new Intent(LocationsActivity.this, Forest_Location_Activity.class);
//                    startActivity(intent);
//                }
//            });
//        }else{
//            forest_frame.setAlpha(0.6f);
//            forest_frame.setEnabled(false);
//        }

        beach_frame = findViewById(R.id.frame_beach);
        beach_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButtonSound();
                Intent intent = new Intent(LocationsActivity.this, Beach_Location_Activity.class);
                startActivity(intent);
            }
        });


        //Random games from beach
//        beach_frame = findViewById(R.id.frame_beach);
//        int starsCountInForestLocation = starRatingDao.getTotalStarsByProfileIdAndLocationId(1, Location.FOREST);
//        if(starsCountInForestLocation == StarRating.TOTAL_STARS_FOREST) {
//        beach_frame.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                playButtonSound();
//                Intent intent = new Intent(LocationsActivity.this, Beach_Location_Activity.class);
//                startActivity(intent);
//            }
//        });
//        }else{
//        // Disable the beach frame
//        beach_frame.setEnabled(false);  // Disables interaction
//        beach_frame.setAlpha(0.6f);     // Optional: Dim the frame to indicate it's disabled
//    }

        ImageButton exitButton = findViewById(R.id.exit_button_location1);
        if (exitButton != null) {
            exitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playButtonSound();
                    Intent intent = new Intent(LocationsActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    //sound
    private void toggleSound(boolean enable) {
        if (enable) {
            isSoundOn = true;
            if (mysound != null) {
                mysound.setVolume(1.0f, 1.0f); // Enable sound
            }
            // Unmute system sounds (optional, if you want to control global sounds)
            AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        } else {
            isSoundOn = false;
            if (mysound != null) {
                mysound.setVolume(0f, 0f); // Disable sound
            }
            // Mute system sounds (optional, if you want to control global sounds)
            AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        }
        settings.setSound(isSoundOn ? 1 : 0);
    }

    private void playButtonSound() {
        Log.d("Sound", "Playing button sound");
        if (mysound != null && !mysound.isPlaying()) {
            mysound.start();
        }
    }

//    private boolean unlockBeachLocation(){
//
//        return StarRatingDao.getStarRatingByProfileIdLocationIdAndLevelId == StarRating.TOTAL_STARS_HOME;
//    }
}
