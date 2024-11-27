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

import com.ecosort.ecosortkiddo.Hide_Navigation;
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

public class Backyard_Location_Activity extends AppCompatActivity {

    // CardView references for play and end game levels
    private CardView[] cardViewsPlayLocationTwo = new CardView[6];
    private CardView cardViewEndGameLocationTwo;
    //    private ImageView starsImageView;
    private StarRatingDao starRatingDao;
    private Settings settings;
    private SettingsDao settingsDao;
    private ImageButton exitButton;
    private CardView tutorialCv;
    private ImageView tutorialImageView;
    private int currentTutorialIndex = 0;
    private int[] tutorialImages = {
            R.drawable.backyard_tutorial1,
            R.drawable.backyard_tutorial2,
            R.drawable.backyard_tutorial3
    };
    private MediaPlayer mysound;
    private boolean isSoundOn = true;
    private ImageButton tutorialButton;

    private CardView tutorialFilipinoCv;
    private ImageView tutorialFilipinoImageView;
    private int currentFilipinoTutorialIndex = 0;
    private int[] tutorialFilipinoImages = {
            R.drawable.backyard_filipino_tutorial1,
            R.drawable.backyard_filipino_tutorial2,
            R.drawable.backyard_filipino_tutorial3
    };
    private ImageButton tutorialFilipinoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeDependencies();

        // Enable fullscreen and immersive modes
        Hide_Navigation.enableFullscreen(this);
        Hide_Navigation.enableImmersiveMode(this);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_backyard_location);

        tutorialButton = findViewById(R.id.backyard_info_btn);
        tutorialCv = findViewById(R.id.backyard_tutorial);
        tutorialCv.setVisibility(View.GONE);

        tutorialFilipinoButton = findViewById(R.id.backyard_info_btn);
        tutorialFilipinoCv = findViewById(R.id.backyard_Filipinotutorial);
        tutorialFilipinoCv.setVisibility(View.GONE);

        initializeViews();
        setupExitButton();
        setupLevelButtons();
        setupBackButtons();
        setupPlayButtons();
        setupWindowInsets();

        settings = settingsDao.getSettings(1);

        if (Settings.LANGUAGE_ENGLISH.equalsIgnoreCase(settings.getLanguage())){
            tutorialbackyard();
        } else {
            tutorialFilipinobackyard();
        }

        toggleSound(settings.getSound() == 1 ? true : false);

        //convert to chosen language
        translateText(settings.getLanguage());
    }

    private void translateText(String languageCode) {
        //kunin ung mga element na kailangan itranslate
        TextView textSelectLevel = findViewById(R.id.txt_select_level);
        textSelectLevel.setText(TranslatorUtil.translate(textSelectLevel.getText().toString(), languageCode));

        //Level 1
        Button buttonPlay = findViewById(R.id.btn_play_location2xxlevel1);
        buttonPlay.setText(TranslatorUtil.translate(buttonPlay.getText().toString(), languageCode));

        Button buttonBack = findViewById(R.id.btn_back_loc2lvl1);
        buttonBack.setText(TranslatorUtil.translate(buttonBack.getText().toString(), languageCode));

        //Level 2
        Button buttonPlay2 = findViewById(R.id.btn_play_location2xxlevel2);
        buttonPlay2.setText(TranslatorUtil.translate(buttonPlay2.getText().toString(), languageCode));

        Button buttonBack2 = findViewById(R.id.btn_back_loc2lvl2);
        buttonBack2.setText(TranslatorUtil.translate(buttonBack2.getText().toString(), languageCode));

        //Level 3
        Button buttonPlay3 = findViewById(R.id.btn_play_location2xxlevel3);
        buttonPlay3.setText(TranslatorUtil.translate(buttonPlay3.getText().toString(), languageCode));

        Button buttonBack3 = findViewById(R.id.btn_back_loc2lvl3);
        buttonBack3.setText(TranslatorUtil.translate(buttonBack3.getText().toString(), languageCode));

        //Level 4
        Button buttonPlay4 = findViewById(R.id.btn_play_location2xxlevel4);
        buttonPlay4.setText(TranslatorUtil.translate(buttonPlay4.getText().toString(), languageCode));

        Button buttonBack4 = findViewById(R.id.btn_back_loc2lvl4);
        buttonBack4.setText(TranslatorUtil.translate(buttonBack4.getText().toString(), languageCode));

        //Level 5
        Button buttonPlay5 = findViewById(R.id.btn_play_location2xxlevel5);
        buttonPlay5.setText(TranslatorUtil.translate(buttonPlay5.getText().toString(), languageCode));

        Button buttonBack5 = findViewById(R.id.btn_back_loc2vl5);
        buttonBack5.setText(TranslatorUtil.translate(buttonBack5.getText().toString(), languageCode));

        //Level 6
        Button buttonPlay6 = findViewById(R.id.btn_play_location2xxlevel6);
        buttonPlay6.setText(TranslatorUtil.translate(buttonPlay6.getText().toString(), languageCode));

        Button buttonBack6 = findViewById(R.id.btn_back_loc2lvl6);
        buttonBack6.setText(TranslatorUtil.translate(buttonBack6.getText().toString(), languageCode));

        //Try
        Button buttonTry = findViewById(R.id.btn_try);
        buttonTry.setText(TranslatorUtil.translate(buttonTry.getText().toString(), languageCode));

        //Next
        Button buttonNext = findViewById(R.id.btn_next);
        buttonNext.setText(TranslatorUtil.translate(buttonNext.getText().toString(), languageCode));

        //Next Tutorial
        TextView textNext = findViewById(R.id.backyard_next_tutorial);
        textNext.setText(TranslatorUtil.translate(textNext.getText().toString(), languageCode));

        TextView textNextFilipino = findViewById(R.id.backyard_next_filipino_tutorial);
        textNextFilipino.setText(TranslatorUtil.translate(textNextFilipino.getText().toString(), languageCode));
    }

//    @Override
//    public void onBackPressed() {
//        boolean isAnyCardViewVisible = false;
//
//        // Check if any of the CardViews are visible
//        for (CardView cardView : cardViewsPlayLocationTwo) {
//            if (cardView.getVisibility() == View.VISIBLE) {
//                isAnyCardViewVisible = true;
//                break;
//            }
//        }
//
//        // If any CardView is visible, hide all CardViews
//        if (isAnyCardViewVisible) {
//            for (CardView cardView : cardViewsPlayLocationTwo) {
//                cardView.setVisibility(View.GONE);
//                setButtonsEnable(true);
//                tutorialButton.setEnabled(true);
//                tutorialButton.setAlpha(1.0f);
//                exitButton.setEnabled(true);
//                exitButton.setAlpha(1.0f);
//            }
//        } else {
//            // If no CardView is visible, navigate to LocationsActivity
//            Intent intent = new Intent(this, LocationsActivity.class);
//            startActivity(intent);
//            finish();
//            super.onBackPressed();
//            // Optionally close the current activity
//            // No need to call super.onBackPressed(), as you're handling the navigation manually
//        }
//    }

    private void initializeDependencies() {
        // Initialize the ProfileDao
        starRatingDao = new StarRatingDao(this);
        settingsDao = new SettingsDao(this);

        mysound = MediaPlayer.create(this, R.raw.buttons);
        mysound.setLooping(false);

    }

    private void initializeViews() {

        exitButton = findViewById(R.id.exit_button_location2);

        List<StarRating> starRatingList = starRatingDao.getStarRatingByProfileIdAndLocationId(Profile.DEFAULT_PROFILE, Location.BACKYARD);
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

                int drawableResId = Backyard_Game_Activity.getStarDrawable(starsOnLevel);
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

        for (StarRating sr : starRatingList) {

            int starsOnLevel = sr.getStars();

            totalStarsInCurrentLocation = totalStarsInCurrentLocation + starsOnLevel;
            System.out.println(sr.getLevelId() + " " + sr.getStars());

            // Updated: Changed to use starRatingLevelingMapping
            Integer starRatingPerLevel = starRatingLevelingMapping.get(sr.getLevelId());
            if (starRatingPerLevel != null) {
                ImageView iv = findViewById(starRatingPerLevel);
                iv.setVisibility(View.VISIBLE);

                int drawableResId = House_Game_Activity.getStarDrawable(starsOnLevel);
                Log.d("EndGame", "Drawable resource ID: " + drawableResId);
                iv.setImageResource(drawableResId);
            }

            if (starsOnLevel > 0 && sr.getLevelId() < 6) {
                Integer nextLevelId = imageButtonMapping.get(sr.getLevelId() + 1);
                if (nextLevelId != null) {
                    ImageButton ib = findViewById(nextLevelId);
                    setButtonState(nextLevelId, true);
                }
            }
        }


        TextView totalStarsTextView = findViewById(R.id.backyard_score);
        totalStarsTextView.setText(String.valueOf(totalStarsInCurrentLocation) + " / " + StarRating.TOTAL_STARS_BACKYARD);

        int[] cardViewIds = {
                R.id.cardview_play__location2xxlevel1, R.id.cardview_play__location2xxlevel2,
                R.id.cardview_play__location2xxlevel3, R.id.cardview_play__location2xxlevel4,
                R.id.cardview_play__location2xxlevel5, R.id.cardview_play__location2xxlevel6
        };
        for (int i = 0; i < cardViewIds.length; i++) {
            cardViewsPlayLocationTwo[i] = findViewById(cardViewIds[i]);
            cardViewsPlayLocationTwo[i].setVisibility(View.GONE);
        }

        cardViewEndGameLocationTwo = findViewById(R.id.cardview_endgame_loc2lvl1);
        cardViewEndGameLocationTwo.setVisibility(View.GONE);
    }

    private void tutorialbackyard() {
        tutorialImageView = findViewById(R.id.backyard_tutorial1);
        TextView tutorialText = findViewById(R.id.backyard_next_tutorial);
        View clickBlocker = findViewById(R.id.click_blocker);

        tutorialButton.setOnClickListener(v -> {
            playButtonSound();
            // Show the tutorial CardView and reset the index
            clickBlocker.setVisibility(View.VISIBLE);
            tutorialCv.setVisibility(View.VISIBLE);

            // Set the initial tutorial image
            tutorialImageView.setImageResource(tutorialImages[currentTutorialIndex]);

            // Set up the click listener for advancing through the tutorial
            tutorialText.setOnClickListener(v1 -> {
                playButtonSound();
                currentTutorialIndex++;
                if (currentTutorialIndex < tutorialImages.length) {
                    // Set the next tutorial image
                    tutorialImageView.setImageResource(tutorialImages[currentTutorialIndex]);
                } else {
                    // Hide the tutorial when the sequence is finished
                    tutorialCv.setVisibility(View.GONE);
                    clickBlocker.setVisibility(View.GONE);
                    currentTutorialIndex = 0; // Optionally reset the index for future use
                }
            });
        });
    }

    private void tutorialFilipinobackyard() {
        tutorialFilipinoImageView = findViewById(R.id.backyard_Filipinotutorial1);
        TextView tutorialFilipinoText = findViewById(R.id.backyard_next_filipino_tutorial);
        View clickBlocker = findViewById(R.id.click_blocker);

        tutorialFilipinoButton.setOnClickListener(v -> {
            playButtonSound();
            // Show the tutorial CardView and reset the index
            clickBlocker.setVisibility(View.VISIBLE);
            tutorialFilipinoCv.setVisibility(View.VISIBLE);

            // Set the initial tutorial image
            tutorialFilipinoImageView.setImageResource(tutorialFilipinoImages[currentFilipinoTutorialIndex]);

            // Set up the click listener for advancing through the tutorial
            tutorialFilipinoText.setOnClickListener(v1 -> {
                playButtonSound();
                currentFilipinoTutorialIndex++;
                if (currentFilipinoTutorialIndex < tutorialFilipinoImages.length) {
                    // Set the next tutorial image
                    tutorialFilipinoImageView.setImageResource(tutorialFilipinoImages[currentFilipinoTutorialIndex]);
                } else {
                    // Hide the tutorial when the sequence is finished
                    tutorialFilipinoCv.setVisibility(View.GONE);
                    clickBlocker.setVisibility(View.GONE);
                    currentFilipinoTutorialIndex = 0; // Optionally reset the index for future use
                }
            });
        });
    }

    private void setupExitButton() {
        if (exitButton != null) {
            exitButton.setOnClickListener(v -> {
                Intent intent = new Intent(Backyard_Location_Activity.this, LocationsActivity.class);
                startActivity(intent);
                playButtonSound();
                finish(); // Finish the current activity
            });
        }
    }

    private void setupLevelButtons() {
        int[] levelButtonIds = {
                R.id.btn_play_loc2level1, R.id.btn_play_loc2level2, R.id.btn_play_loc2level3,
                R.id.btn_play_loc2level4, R.id.btn_play_loc2level5, R.id.btn_play_loc2level6
        };
        for (int i = 0; i < levelButtonIds.length; i++) {
            setupLevelButton(levelButtonIds[i], cardViewsPlayLocationTwo[i]);
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
                    exitButton.setAlpha(0.4f);   // Set the alpha to indicate it's disabled
                }
            });
        }
    }

    private void disableAllCardViews() {
        for (CardView cardView : cardViewsPlayLocationTwo) {
            if (cardView != null) {
                cardView.setEnabled(false);
                cardView.setVisibility(View.GONE);
                setButtonsEnable(false);
                tutorialButton.setEnabled(false);
                tutorialButton.setAlpha(0.5f);
            }
        }
    }

    private void setupBackButtons() {
        int[] backButtonIds = {
                R.id.btn_back_loc2lvl1, R.id.btn_back_loc2lvl2, R.id.btn_back_loc2lvl3,
                R.id.btn_back_loc2lvl4, R.id.btn_back_loc2vl5, R.id.btn_back_loc2lvl6
        };
        for (int i = 0; i < backButtonIds.length; i++) {
            setupBackButton(backButtonIds[i], cardViewsPlayLocationTwo[i]);
        }
    }

    private void setupBackButton(int buttonId, CardView cardView) {
        Button backButton = findViewById(buttonId);
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                playButtonSound();
                cardView.setVisibility(View.GONE);
//                setButtonsEnabled(true);
                tutorialButton.setEnabled(true);
                tutorialButton.setAlpha(1.0f);

                // Re-enable the exit button
                if (exitButton != null) {
                    exitButton.setEnabled(true);  // Re-enable the exit button
                    exitButton.setAlpha(1.0f);    // Set the alpha back to fully opaque
                }
            });
        }
    }

    private void setupPlayButtons() {
        setupPlayButton(R.id.btn_play_location2xxlevel1, Backyard_Game_Activity.class);
        setupPlayButton(R.id.btn_play_location2xxlevel2, Backyard_Game_Activity.class);
        setupPlayButton(R.id.btn_play_location2xxlevel3, Backyard_Game_Activity.class);
        setupPlayButton(R.id.btn_play_location2xxlevel4, Backyard_Game_Activity.class);
        setupPlayButton(R.id.btn_play_location2xxlevel5, Backyard_Game_Activity.class);
        setupPlayButton(R.id.btn_play_location2xxlevel6, Backyard_Game_Activity.class);
    }

    static Map<Integer, String> buttonLevelMapping = new HashMap<>();
    static {
        buttonLevelMapping.put(Integer.valueOf(R.id.btn_play_location2xxlevel1), "1");
        buttonLevelMapping.put(Integer.valueOf(R.id.btn_play_location2xxlevel2), "2");
        buttonLevelMapping.put(Integer.valueOf(R.id.btn_play_location2xxlevel3), "3");
        buttonLevelMapping.put(Integer.valueOf(R.id.btn_play_location2xxlevel4), "4");
        buttonLevelMapping.put(Integer.valueOf(R.id.btn_play_location2xxlevel5), "5");
        buttonLevelMapping.put(Integer.valueOf(R.id.btn_play_location2xxlevel6), "6");
    }

    static Map<Integer, Integer> starRatingLevelMapping = new HashMap<>();
    static {
        starRatingLevelMapping.put(1, Integer.valueOf(R.id.location2_star_rating_level1));
        starRatingLevelMapping.put(2, Integer.valueOf(R.id.location2_star_rating_level2));
        starRatingLevelMapping.put(3, Integer.valueOf(R.id.location2_star_rating_level3));
        starRatingLevelMapping.put(4, Integer.valueOf(R.id.location2_star_rating_level4));
        starRatingLevelMapping.put(5, Integer.valueOf(R.id.location2_star_rating_level5));
        starRatingLevelMapping.put(6, Integer.valueOf(R.id.location2_star_rating_level6));
    }

    static Map<Integer, Integer> imageButtonMapping = new HashMap<>();
    static {
        imageButtonMapping.put(1, Integer.valueOf(R.id.btn_play_loc2level1));
        imageButtonMapping.put(2, Integer.valueOf(R.id.btn_play_loc2level2));
        imageButtonMapping.put(3, Integer.valueOf(R.id.btn_play_loc2level3));
        imageButtonMapping.put(4, Integer.valueOf(R.id.btn_play_loc2level4));
        imageButtonMapping.put(5, Integer.valueOf(R.id.btn_play_loc2level5));
        imageButtonMapping.put(6, Integer.valueOf(R.id.btn_play_loc2level6));
    }

    static Map<Integer, Integer> starRatingLevelingMapping = new HashMap<>();
    static {
        starRatingLevelingMapping.put(1, Integer.valueOf(R.id.location2_starRating_level1));
        starRatingLevelingMapping.put(2, Integer.valueOf(R.id.location2_starRating_level2));
        starRatingLevelingMapping.put(3, Integer.valueOf(R.id.location2_starRating_level3));
        starRatingLevelingMapping.put(4, Integer.valueOf(R.id.location2_starRating_level4));
        starRatingLevelingMapping.put(5, Integer.valueOf(R.id.location2_starRating_level5));
        starRatingLevelingMapping.put(6, Integer.valueOf(R.id.location2_starRating_level6));
    }

    private void setupPlayButton(int buttonId, Class<?> activityClass) {
        Button playButton = findViewById(buttonId);
        if (playButton != null) {
            playButton.setOnClickListener(v -> {
                if (activityClass != null) {
                    Intent intent = new Intent(Backyard_Location_Activity.this, Backyard_Game_Activity.class);
                    playButtonSound();
                    intent.putExtra("selectedLevel", buttonLevelMapping.get(buttonId));
                    startActivity(intent);
                }
            });
        }
    }

    private void setButtonsEnabled(boolean isEnabled) {
        int[] buttonIds = {
                R.id.btn_play_loc2level1, R.id.btn_play_loc2level2,
                R.id.btn_play_loc2level3, R.id.btn_play_loc2level4, R.id.btn_play_loc2level5,
                R.id.btn_play_loc2level6
        };
        for (int buttonId : buttonIds) {
            setButtonState(buttonId, isEnabled);
        }
    }

    private void setButtonsEnable(boolean isEnabled) {
        int[] buttonIds = {
                R.id.btn_play_loc2level1, R.id.btn_play_loc2level2,
                R.id.btn_play_loc2level3, R.id.btn_play_loc2level4, R.id.btn_play_loc2level5,
                R.id.btn_play_loc2level6
        };
    }

    private void setButtonState(int buttonId, boolean isEnabled) {
        View button = findViewById(buttonId);
        button.setEnabled(isEnabled);
        button.setAlpha(isEnabled ? 1.0f : 0.5f);
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.backyard_location), (v, insets) -> {
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
        if (mysound != null && !mysound.isPlaying()) {
            mysound.start();
        }
    }
}
