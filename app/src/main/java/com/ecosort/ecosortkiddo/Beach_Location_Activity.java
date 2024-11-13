package com.ecosort.ecosortkiddo;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ecosort.ecosortkiddo.LocationsActivity;
import com.ecosort.ecosortkiddo.R;
import com.ecosort.ecosortkiddo.dao.SettingsDao;
import com.ecosort.ecosortkiddo.dao.StarRatingDao;
import com.ecosort.ecosortkiddo.model.Location;
import com.ecosort.ecosortkiddo.model.Profile;
import com.ecosort.ecosortkiddo.model.Settings;
import com.ecosort.ecosortkiddo.model.StarRating;
import com.ecosort.ecosortkiddo.utils.TranslatorUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Beach_Location_Activity extends AppCompatActivity {

    // CardView references for play and end game levels
    private CardView[] cardViewsPlayLocationFour = new CardView[6];
    private CardView cardViewEndGameLocationFour;
    //    private ImageView starsImageView;
    private StarRatingDao starRatingDao;
    private Settings settings;
    private SettingsDao settingsDao;
    private ImageButton exitButton;
    private MediaPlayer mysound;
    private boolean isSoundOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeDependencies();

        // Enable fullscreen and immersive modes
        Hide_Navigation.enableFullscreen(this);
        Hide_Navigation.enableImmersiveMode(this);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_beach_location);

        initializeViews();
        setupExitButton();
        setupLevelButtons();
        setupBackButtons();
        setupPlayButtons();
        setupWindowInsets();

        settings = settingsDao.getSettings(1);
        toggleSound(settings.getSound() == 1 ? true : false);

        //convert to chosen language
        translateText(settings.getLanguage());
    }

    private void translateText(String languageCode) {
        //kunin ung mga element na kailangan itranslate
        TextView textSelectLevel = findViewById(R.id.txt_select_level_beach);
        textSelectLevel.setText(TranslatorUtil.translate(textSelectLevel.getText().toString(), languageCode));

        //Level 1
        Button buttonPlay = findViewById(R.id.btn_play_location4xxlevel1);
        buttonPlay.setText(TranslatorUtil.translate(buttonPlay.getText().toString(), languageCode));

        Button buttonBack = findViewById(R.id.btn_back_loc4lvl1);
        buttonBack.setText(TranslatorUtil.translate(buttonBack.getText().toString(), languageCode));

        //Level 2
        Button buttonPlay2 = findViewById(R.id.btn_play_location4xxlevel2);
        buttonPlay2.setText(TranslatorUtil.translate(buttonPlay2.getText().toString(), languageCode));

        Button buttonBack2 = findViewById(R.id.btn_back_loc4lvl2);
        buttonBack2.setText(TranslatorUtil.translate(buttonBack2.getText().toString(), languageCode));

        //Level 3
        Button buttonPlay3 = findViewById(R.id.btn_play_location4xxlevel3);
        buttonPlay3.setText(TranslatorUtil.translate(buttonPlay3.getText().toString(), languageCode));

        Button buttonBack3 = findViewById(R.id.btn_back_loc4lvl3);
        buttonBack3.setText(TranslatorUtil.translate(buttonBack3.getText().toString(), languageCode));

        //Level 4
        Button buttonPlay4 = findViewById(R.id.btn_play_location4xxlevel4);
        buttonPlay4.setText(TranslatorUtil.translate(buttonPlay4.getText().toString(), languageCode));

        Button buttonBack4 = findViewById(R.id.btn_back_loc4lvl4);
        buttonBack4.setText(TranslatorUtil.translate(buttonBack4.getText().toString(), languageCode));

        //Level 5
        Button buttonPlay5 = findViewById(R.id.btn_play_location4xxlevel5);
        buttonPlay5.setText(TranslatorUtil.translate(buttonPlay5.getText().toString(), languageCode));

        Button buttonBack5 = findViewById(R.id.btn_back_loc4lvl5);
        buttonBack5.setText(TranslatorUtil.translate(buttonBack5.getText().toString(), languageCode));

        //Level 6
        Button buttonPlay6 = findViewById(R.id.btn_play_location4xxlevel6);
        buttonPlay6.setText(TranslatorUtil.translate(buttonPlay6.getText().toString(), languageCode));

        Button buttonBack6 = findViewById(R.id.btn_back_loc4lvl6);
        buttonBack6.setText(TranslatorUtil.translate(buttonBack6.getText().toString(), languageCode));

        //Try
        Button buttonTry = findViewById(R.id.btn_try);
        buttonTry.setText(TranslatorUtil.translate(buttonTry.getText().toString(), languageCode));

        //Next
        Button buttonNext = findViewById(R.id.btn_next);
        buttonNext.setText(TranslatorUtil.translate(buttonNext.getText().toString(), languageCode));

    }

    @Override
    public void onBackPressed() {
        boolean isAnyCardViewVisible = false;

        // Check if any of the CardViews are visible
        for (CardView cardView : cardViewsPlayLocationFour) {
            if (cardView.getVisibility() == View.VISIBLE) {
                isAnyCardViewVisible = true;
                break;
            }
        }

        // If any CardView is visible, hide all CardViews
        if (isAnyCardViewVisible) {
            for (CardView cardView : cardViewsPlayLocationFour) {
                cardView.setVisibility(View.GONE);
                setButtonsEnable(true);
                exitButton.setEnabled(true);
                exitButton.setAlpha(1.0f);
            }
        } else {
            // If no CardView is visible, navigate to LocationsActivity
            Intent intent = new Intent(this, LocationsActivity.class);
            startActivity(intent);
            finish();
            super.onBackPressed();
            // Optionally close the current activity
            // No need to call super.onBackPressed(), as you're handling the navigation manually
        }
    }

    private void initializeDependencies() {
        // Initialize the ProfileDao
        starRatingDao = new StarRatingDao(this);
        settingsDao = new SettingsDao(this);

        mysound = MediaPlayer.create(this, R.raw.buttons);
        mysound.setLooping(false);
    }

    private void initializeViews() {

        exitButton = findViewById(R.id.exit_button_location4);

        List<StarRating> starRatingList = starRatingDao.getStarRatingByProfileIdAndLocationId(Profile.DEFAULT_PROFILE, Location.BEACH);
        System.out.println("Star Rating List:");
        int totalStarsInCurrentLocation = 0;

        //disabled all buttons except for button 1
        //level 1 button should be enabled
        setButtonsEnabled(false);
        setButtonState(imageButtonMapping.get(1), true);

        for (StarRating sr : starRatingList){

            int starsOnLevel = sr.getStars();

            totalStarsInCurrentLocation = totalStarsInCurrentLocation +starsOnLevel;
            System.out.println(sr.getLevelId() + " " + sr.getStars());

            Integer starRatingPerLevel = starRatingLevelMapping.get(sr.getLevelId());
            if(starRatingPerLevel != null){
                ImageView iv = findViewById(starRatingPerLevel);
                iv.setVisibility(View.VISIBLE);

                int drawableResId = Beach_Game_Activity.getStarDrawable(starsOnLevel);
                Log.d("EndGame", "Drawable resource ID: " + drawableResId);
                iv.setImageResource(drawableResId);
            }

            if(starsOnLevel > 0 && sr.getLevelId() < 6) {
                Integer nextLevelId = imageButtonMapping.get(sr.getLevelId() + 1);
                if (nextLevelId != null) {
                    ImageButton ib = findViewById(nextLevelId);
                    setButtonState(nextLevelId, true);
                }
            }
        }

        TextView totalStarsTextView = findViewById(R.id.beach_score);
        totalStarsTextView.setText(String.valueOf(totalStarsInCurrentLocation) + " / " + StarRating.TOTAL_STARS_BEACH);

        int[] cardViewIds = {
                R.id.cardview_play__location4xxlevel1, R.id.cardview_play__location4xxlevel2,
                R.id.cardview_play__location4xxlevel3, R.id.cardview_play__location4xxlevel4,
                R.id.cardview_play__location4xxlevel5, R.id.cardview_play__location4xxlevel6
        };
        for (int i = 0; i < cardViewIds.length; i++) {
            cardViewsPlayLocationFour[i] = findViewById(cardViewIds[i]);
            cardViewsPlayLocationFour[i].setVisibility(View.GONE);
        }

        cardViewEndGameLocationFour = findViewById(R.id.cardview_endgame_loc4lvl1);
        cardViewEndGameLocationFour.setVisibility(View.GONE);
    }

    private void setupExitButton() {
        if (exitButton != null) {
            exitButton.setOnClickListener(v -> {
                playButtonSound();
                Intent intent = new Intent(Beach_Location_Activity.this, LocationsActivity.class);
                startActivity(intent);
                finish(); // Finish the current activity
            });
        }
    }

    private void setupLevelButtons() {
        int[] levelButtonIds = {
                R.id.btn_play_loc4level1, R.id.btn_play_loc4level2, R.id.btn_play_loc4level3,
                R.id.btn_play_loc4level4, R.id.btn_play_loc4level5, R.id.btn_play_loc4level6
        };
        for (int i = 0; i < levelButtonIds.length; i++) {
            setupLevelButton(levelButtonIds[i], cardViewsPlayLocationFour[i]);
        }
    }

    private void setupLevelButton(int buttonId, CardView cardView) {
        ImageButton levelButton = findViewById(buttonId);
        if (levelButton != null) {
            levelButton.setOnClickListener(v -> {
                playButtonSound();
                disableAllCardViews();
                cardView.setVisibility(View.VISIBLE);
//                setButtonsEnabled(false);

                if (exitButton != null) {
                    exitButton.setEnabled(false); // Disable the exit button
                    exitButton.setAlpha(0.5f);   // Set the alpha to indicate it's disabled
                }
            });
        }
    }

    private void disableAllCardViews() {
        for (CardView cardView : cardViewsPlayLocationFour) {
            if (cardView != null) {
                cardView.setEnabled(false);
                cardView.setVisibility(View.GONE);
                setButtonsEnable(false);
            }
        }
    }

    private void setupBackButtons() {
        int[] backButtonIds = {
                R.id.btn_back_loc4lvl1, R.id.btn_back_loc4lvl2, R.id.btn_back_loc4lvl3,
                R.id.btn_back_loc4lvl4, R.id.btn_back_loc4lvl5, R.id.btn_back_loc4lvl6
        };
        for (int i = 0; i < backButtonIds.length; i++) {
            setupBackButton(backButtonIds[i], cardViewsPlayLocationFour[i]);
        }
    }

    private void setupBackButton(int buttonId, CardView cardView) {
        Button backButton = findViewById(buttonId);
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                playButtonSound();
                cardView.setVisibility(View.GONE);
                setButtonsEnable(true);
//                setButtonsEnabled(true);

                // Re-enable the exit button
                if (exitButton != null) {
                    exitButton.setEnabled(true);  // Re-enable the exit button
                    exitButton.setAlpha(1.0f);    // Set the alpha back to fully opaque
                }
            });
        }
    }

    private void setupPlayButtons() {
        setupPlayButton(R.id.btn_play_location4xxlevel1, Beach_Game_Activity.class);
        setupPlayButton(R.id.btn_play_location4xxlevel2, Beach_Game_Activity.class);
        setupPlayButton(R.id.btn_play_location4xxlevel3, Beach_Game_Activity.class);
        setupPlayButton(R.id.btn_play_location4xxlevel4, Beach_Game_Activity.class);
        setupPlayButton(R.id.btn_play_location4xxlevel5, Beach_Game_Activity.class);
        setupPlayButton(R.id.btn_play_location4xxlevel6, Beach_Game_Activity.class);
    }

    static Map<Integer, String> buttonLevelMapping = new HashMap<>();
    static {
        buttonLevelMapping.put(Integer.valueOf(R.id.btn_play_location4xxlevel1), "1");
        buttonLevelMapping.put(Integer.valueOf(R.id.btn_play_location4xxlevel2), "2");
        buttonLevelMapping.put(Integer.valueOf(R.id.btn_play_location4xxlevel3), "3");
        buttonLevelMapping.put(Integer.valueOf(R.id.btn_play_location4xxlevel4), "4");
        buttonLevelMapping.put(Integer.valueOf(R.id.btn_play_location4xxlevel5), "5");
        buttonLevelMapping.put(Integer.valueOf(R.id.btn_play_location4xxlevel6), "6");
    }

    static Map<Integer, Integer> starRatingLevelMapping = new HashMap<>();
    static {
        starRatingLevelMapping.put(1, Integer.valueOf(R.id.location4_star_rating_level1));
        starRatingLevelMapping.put(2, Integer.valueOf(R.id.location4_star_rating_level2));
        starRatingLevelMapping.put(3, Integer.valueOf(R.id.location4_star_rating_level3));
        starRatingLevelMapping.put(4, Integer.valueOf(R.id.location4_star_rating_level4));
        starRatingLevelMapping.put(5, Integer.valueOf(R.id.location4_star_rating_level5));
        starRatingLevelMapping.put(6, Integer.valueOf(R.id.location4_star_rating_level6));
    }

    static Map<Integer, Integer> imageButtonMapping = new HashMap<>();
    static {
        imageButtonMapping.put(1, Integer.valueOf(R.id.btn_play_loc4level1));
        imageButtonMapping.put(2, Integer.valueOf(R.id.btn_play_loc4level2));
        imageButtonMapping.put(3, Integer.valueOf(R.id.btn_play_loc4level3));
        imageButtonMapping.put(4, Integer.valueOf(R.id.btn_play_loc4level4));
        imageButtonMapping.put(5, Integer.valueOf(R.id.btn_play_loc4level5));
        imageButtonMapping.put(6, Integer.valueOf(R.id.btn_play_loc4level6));
    }

    private void setupPlayButton(int buttonId, Class<?> activityClass) {
        Button playButton = findViewById(buttonId);
        if (playButton != null) {
            playButton.setOnClickListener(v -> {
                if (activityClass != null) {
                    playButtonSound();
                    Intent intent = new Intent(Beach_Location_Activity.this, Beach_Game_Activity.class);
                    intent.putExtra("selectedLevel", buttonLevelMapping.get(buttonId));
                    startActivity(intent);
                }
            });
        }
    }

    private void setButtonsEnabled(boolean isEnabled) {
        int[] buttonIds = {
                R.id.btn_play_loc4level1, R.id.btn_play_loc4level2,
                R.id.btn_play_loc4level3, R.id.btn_play_loc4level4, R.id.btn_play_loc4level5,
                R.id.btn_play_loc4level6
        };
        for (int buttonId : buttonIds) {
            setButtonState(buttonId, isEnabled);
        }
    }
    private void setButtonsEnable(boolean isEnabled) {
        int[] buttonIds = {
                R.id.btn_play_loc4level1, R.id.btn_play_loc4level2,
                R.id.btn_play_loc4level3, R.id.btn_play_loc4level4, R.id.btn_play_loc4level5,
                R.id.btn_play_loc4level6
        };
    }

    private void setButtonState(int buttonId, boolean isEnabled) {
        View button = findViewById(buttonId);
        button.setEnabled(isEnabled);
        button.setAlpha(isEnabled ? 1.0f : 0.5f);
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.beach_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
}