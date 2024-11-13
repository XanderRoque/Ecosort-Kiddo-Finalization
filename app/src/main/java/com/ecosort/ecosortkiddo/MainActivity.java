package com.ecosort.ecosortkiddo;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ecosort.ecosortkiddo.dao.ProfileDao;
import com.ecosort.ecosortkiddo.dao.SettingsDao;
import com.ecosort.ecosortkiddo.model.Settings;
import com.ecosort.ecosortkiddo.utils.TranslatorUtil;

public class MainActivity extends AppCompatActivity {
    //New Push The Huge Files Are Gone

    private com.ecosort.ecosortkiddo.MyDBHelper dbHelper;
    private ProfileDao profileDao;
    private SettingsDao settingsDao;
    private Settings settings;
    private MediaPlayer mysound, mymusic;
    private CardView settings_cardview, settingsButtonCardview, musicCardview, soundCardview, languageCardview;
    private Button button_music, button_sound, button_language;
    private Button buttonBackMusic, buttonBackSound, buttonBackLanguage;
    private ImageButton button_settings, exitButton;
    private Button button_soundon, button_soundoff;
    private boolean isSoundOn = true;
    private Button button_musicon, button_musicoff;
    private boolean isMusicOn = true;
    private Button buttonLocations, buttonProfile, button_insert;

    private boolean doubleBackToExitPressedOnce = false;
    private Handler handler = new Handler();
    private Runnable resetBackPressed = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    private void initializeDependencies(){
        // Initialize the MyDBHelper
        dbHelper = new MyDBHelper(this);
        // Initialize the ProfileDao
        profileDao = new ProfileDao(this);
        settingsDao = new SettingsDao(this);

        mysound = MediaPlayer.create(this, R.raw.buttons);
        mysound.setLooping(false);
        mysound.seekTo(0);
//        mysound.setVolume(0.5f, 0.5f);

        mymusic = MediaPlayer.create(this, R.raw.ingame_music);

//        soundManager = new SoundManager(mysound);

        settings_cardview = findViewById(R.id.settings_cardview);
        settingsButtonCardview = findViewById(R.id.settings_button_cardview);
        musicCardview = findViewById(R.id.music_cardview);
        soundCardview = findViewById(R.id.sound_cardview);
        languageCardview = findViewById(R.id.language_cardview);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        String language = sharedPreferences.getString("language", "en");  // Default is English
//        LocaleHelper.setLocale(this, language);
        super.onCreate(savedInstanceState);

        // Use the utility class to enable fullscreen and immersive modes
        Hide_Navigation.enableFullscreen(this);
        Hide_Navigation.enableTrueImmersiveMode(this); // Or UIUtils.enableImmersiveMode(this);

        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this); // Assuming this handles edge-to-edge display, adjust as per your library

        initializeDependencies();

        setupButtons(); // Method to set up button click listeners
        setupSystemBarsPadding(); // Method to handle system bars padding
        ButtonSettings();
        cardViewTransparent();
        setupBackCardViewButtons();
        setSoundButton();
        setMusicButton();
        setupInformationAboutUs();



        settings = settingsDao.getSettings(1);
        if(settings == null){
            settings = new Settings(1, 1, Settings.LANGUAGE_ENGLISH, 1);
            settingsDao.insertSettings(settings);
        }
        Log.d("MainActivity", "Settings: " + settings.toString());

        //String languageFromDB = settings.getLanguage();
        //LocaleHelper.setLocale(MainActivity.this, languageFromDB);  // Change to English


        toggleSound(settings.getSound() == 1 ? true : false);
        toggleMusic(settings.getMusic() == 1 ? true : false);

//        List<Profile> profiles = readProfiles();
//        for (Profile profile : profiles) {
//            Log.d("MainActivity", "Profile ID: " + profile.getProfileId() + ", Name: " + profile.getName() + ", Avatar ID: " + profile.getAvatarId());
//        }

        findViewById(R.id.button_EnglishLanguage).setOnClickListener(v -> {
            playButtonSound();
            settings.setLanguage(Settings.LANGUAGE_ENGLISH);
            settingsDao.updateSettings(settings);
            recreate();
        });

        // Filipino Button Click Listener
        findViewById(R.id.button_FilipinoLanguage).setOnClickListener(v -> {
            playButtonSound();
            settings.setLanguage(Settings.LANGUAGE_FILIPINO);
            settingsDao.updateSettings(settings);
            recreate();
        });

        //convert to chosen language
        translateText(settings.getLanguage());


    }

    private void translateText(String languageCode){
        //kunin ung mga element na kailangan itranslate
        Button buttonEnglishLanguage = findViewById(R.id.button_EnglishLanguage);
        buttonEnglishLanguage.setText(TranslatorUtil.translate(buttonEnglishLanguage.getText().toString(), languageCode));

        Button buttonFilipinoLanguage = findViewById(R.id.button_FilipinoLanguage);
        buttonFilipinoLanguage.setText(TranslatorUtil.translate(buttonFilipinoLanguage.getText().toString(), languageCode));

//        Main Activity
        Button buttonLocations = findViewById(R.id.button_locations);
        buttonLocations.setText(TranslatorUtil.translate(buttonLocations.getText().toString(), languageCode));

        Button buttonProfile = findViewById(R.id.button_profile);
        buttonProfile.setText(TranslatorUtil.translate(buttonProfile.getText().toString(), languageCode));

//        settings
        Button buttonMusic = findViewById(R.id.button_music);
        buttonMusic.setText(TranslatorUtil.translate(buttonMusic.getText().toString(), languageCode));

        Button buttonSound = findViewById(R.id.button_sound);
        buttonSound.setText(TranslatorUtil.translate(buttonSound.getText().toString(), languageCode));

        Button buttonLanguage = findViewById(R.id.button_language);
        buttonLanguage.setText(TranslatorUtil.translate(buttonLanguage.getText().toString(), languageCode));

        //music
        TextView textMusic = findViewById(R.id.txt_music);
        textMusic.setText(TranslatorUtil.translate(textMusic.getText().toString(), languageCode));

        Button buttonMusicOn = findViewById(R.id.button_musicon);
        buttonMusicOn.setText(TranslatorUtil.translate(buttonMusicOn.getText().toString(), languageCode));

        Button buttonMusicOff = findViewById(R.id.button_musicoff);
        buttonMusicOff.setText(TranslatorUtil.translate(buttonMusicOff.getText().toString(), languageCode));

        Button buttonBackMusic = findViewById(R.id.button_back_music);
        buttonBackMusic.setText(TranslatorUtil.translate(buttonBackMusic.getText().toString(), languageCode));

        //sound
        TextView textSound = findViewById(R.id.txt_sound);
        textSound.setText(TranslatorUtil.translate(textSound.getText().toString(), languageCode));

        Button buttonSoundOn = findViewById(R.id.button_soundon);
        buttonSoundOn.setText(TranslatorUtil.translate(buttonSoundOn.getText().toString(), languageCode));

        Button buttonSoundOff = findViewById(R.id.button_soundoff);
        buttonSoundOff.setText(TranslatorUtil.translate(buttonSoundOff.getText().toString(), languageCode));

        Button buttonBackSound = findViewById(R.id.button_back_sound);
        buttonBackSound.setText(TranslatorUtil.translate(buttonBackSound.getText().toString(), languageCode));

        //language
        TextView textLanguage = findViewById(R.id.txt_language);
        textLanguage.setText(TranslatorUtil.translate(textLanguage.getText().toString(), languageCode));

        Button buttonBackLanguage = findViewById(R.id.button_back_language);
        buttonBackLanguage.setText(TranslatorUtil.translate(buttonBackLanguage.getText().toString(), languageCode));
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish(); // Close the app
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        // Reset the flag after 2 seconds
        handler.postDelayed(resetBackPressed, 2000);
    }

    //    Sound
    private void setSoundButton() {
        button_soundon = findViewById(R.id.button_soundon);
        button_soundoff = findViewById(R.id.button_soundoff);

        // Set the default sound state to enabled
        isSoundOn = true; // Default state
         //toggleSound(isSoundOn); // Ensure sound is enabled at startup

        if (button_soundon != null) {
            button_soundon.setOnClickListener(view -> {
                Toast.makeText(this, "Sound is on", Toast.LENGTH_SHORT).show();
                playButtonSound();
                toggleSound(true, true); // Enable all sounds
            });
        }

        if (button_soundoff != null) {
            button_soundoff.setOnClickListener(view -> {
                Toast.makeText(this, "Sound is off", Toast.LENGTH_SHORT).show();
                playButtonSound();
                toggleSound(false, true); // Disable all sounds
            });
        }

        // Update the UI initially based on the current sound state
        updateSoundUI();
    }

    private void toggleSound(boolean enable){
        toggleSound(enable, false);
    }
    private void toggleSound(boolean enable, boolean isSave) {
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

        if(isSave){
            settingsDao.updateSettings(settings);
        }
        updateSoundUI();
    }

    private void updateSoundUI() {
        if (button_soundon != null) {
            button_soundon.setEnabled(!isSoundOn); // Disable when sound is on
        }
        if (button_soundoff != null) {
            button_soundoff.setEnabled(isSoundOn); // Disable when sound is off
        }
    }

    public void playButtonSound() {
        if (isSoundOn && mysound != null) {
            mysound.start();
        }
    }

//    Music
private void setMusicButton() {
    button_musicon = findViewById(R.id.button_musicon);
    button_musicoff = findViewById(R.id.button_musicoff);

    // Set the default sound state to enabled
    isMusicOn = true; // Default state
    //toggleSound(isSoundOn); // Ensure sound is enabled at startup

    if (button_musicon != null) {
        button_musicon.setOnClickListener(view -> {
            Toast.makeText(this, "Music is on", Toast.LENGTH_SHORT).show();
            playButtonSound();
            toggleMusic(true, true); // Enable all sounds
        });
    }

    if (button_musicoff != null) {
        button_musicoff.setOnClickListener(view -> {
            Toast.makeText(this, "Music is off", Toast.LENGTH_SHORT).show();
            playButtonSound();
            toggleMusic(false, true); // Disable all sounds
        });
    }

    // Update the UI initially based on the current sound state
    updateMusicUI();
}

    private void toggleMusic(boolean enable){
        toggleMusic(enable, false);
    }
    private void toggleMusic(boolean enable, boolean isSave) {
        if (enable) {
            isMusicOn = true;
            if (mymusic != null) {
                mymusic.setVolume(1.0f, 1.0f); // Enable sound
            }
            // Unmute system sounds (optional, if you want to control global sounds)
            AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        } else {
            isMusicOn = false;
            if (mymusic != null) {
                mymusic.setVolume(0f, 0f); // Disable sound
            }
            // Mute system sounds (optional, if you want to control global sounds)
            AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        }
        settings.setMusic(isMusicOn ? 1 : 0);

        if(isSave){
            settingsDao.updateSettings(settings);
        }
        updateMusicUI();
    }

    private void updateMusicUI() {
        if (button_musicon != null) {
            button_musicon.setEnabled(!isMusicOn); // Disable when sound is on
        }
        if (button_musicoff != null) {
            button_musicoff.setEnabled(isMusicOn); // Disable when sound is off
        }
    }

    public void playButtonMusic() {
        if (isMusicOn && mymusic != null) {
            mymusic.start();
        }
    }

    private void setupButtons() {
        buttonLocations = findViewById(R.id.button_locations);
        buttonLocations.setOnClickListener(view -> {
            playButtonSound();
            startActivity(new Intent(MainActivity.this, LocationsActivity.class));
        });

        buttonProfile = findViewById(R.id.button_profile);
        buttonProfile.setOnClickListener(view -> {
            playButtonSound();
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        });

        button_insert = findViewById(R.id.reset_data);
        button_insert.setOnClickListener(view -> {
            // Insert a new profile
//            Profile newProfile =  new Profile("Messi", 1);
//            profileDao.insertProfile(newProfile);
//
//            List<Profile> profiles = readProfiles();
//            for (Profile profile : profiles) {
//                Log.d("MainActivity", "Profile ID: " + profile.getProfileId() + ", Name: " + profile.getName() + ", Avatar ID: " + profile.getAvatarId());
//            }
            dbHelper.copyDatabase();
            playButtonSound();
        });
    }

    private void ButtonSettings() {
        // Initialize the settings button
        button_settings = findViewById(R.id.button_settings);
        if (button_settings != null) {
            button_settings.setOnClickListener(view -> {
                playButtonSound();
                disableButton();
                settings_cardview.setVisibility(View.VISIBLE);
                settingsButtonCardview.setVisibility(View.VISIBLE);
                musicCardview.setVisibility(View.GONE);
                soundCardview.setVisibility(View.GONE);
                languageCardview.setVisibility(View.GONE);
            });
        }

        // Initialize the music button
        button_music = findViewById(R.id.button_music);
        if (button_music != null) {
            button_music.setOnClickListener(view -> {
                playButtonSound();
                settings_cardview.setVisibility(View.VISIBLE);
                settingsButtonCardview.setVisibility(View.GONE);
                musicCardview.setVisibility(View.VISIBLE);
                soundCardview.setVisibility(View.GONE);
                languageCardview.setVisibility(View.GONE);
            });
        }

        button_sound = findViewById(R.id.button_sound);
        if (button_sound != null) {
            button_sound.setOnClickListener(view -> {
                playButtonSound();
                settings_cardview.setVisibility(View.VISIBLE);
                settingsButtonCardview.setVisibility(View.GONE);
                musicCardview.setVisibility(View.GONE);
                soundCardview.setVisibility(View.VISIBLE);
                languageCardview.setVisibility(View.GONE);
            });
        }

        button_language = findViewById(R.id.button_language);
        if (button_language != null) {
            button_language.setOnClickListener(view -> {
                playButtonSound();
                settings_cardview.setVisibility(View.VISIBLE);
                settingsButtonCardview.setVisibility(View.GONE);
                musicCardview.setVisibility(View.GONE);
                soundCardview.setVisibility(View.GONE);
                languageCardview.setVisibility(View.VISIBLE);
            });
        }

        exitButton = findViewById(R.id.exitbtn_settings);
        if (exitButton != null) {
            exitButton.setOnClickListener(view -> {
                playButtonSound();
                settings_cardview.setVisibility(View.GONE);
                settingsButtonCardview.setVisibility(View.GONE);
                musicCardview.setVisibility(View.GONE);
                enableButton();
            });
        }
    }

    private void disableButton(){
        buttonLocations.setEnabled(false);
        buttonProfile.setEnabled(false);
        button_insert.setEnabled(false);
        button_settings.setEnabled(false);
    }

    private void enableButton() {
        buttonLocations.setEnabled(true);
        buttonProfile.setEnabled(true);
        button_insert.setEnabled(true);
        button_settings.setEnabled(true);
    }

//    transparent
    private void cardViewTransparent(){
        settingsButtonCardview = findViewById(R.id.settings_button_cardview);

// Set the nested CardView's background and card background to transparent
        settingsButtonCardview.setBackgroundColor(Color.TRANSPARENT);
        settingsButtonCardview.setCardBackgroundColor(Color.TRANSPARENT);

// Optionally, remove the elevation to avoid shadows
        settingsButtonCardview.setCardElevation(0f);
    }

    private void setupBackCardViewButtons() {
        buttonBackMusic = findViewById(R.id.button_back_music);
        if (buttonBackMusic != null) {
            buttonBackMusic.setOnClickListener(view -> {
                playButtonSound();
                settings_cardview.setVisibility(View.VISIBLE);
                settingsButtonCardview.setVisibility(View.VISIBLE);
                musicCardview.setVisibility(View.GONE);
                soundCardview.setVisibility(View.GONE);
                languageCardview.setVisibility(View.GONE);
            });
        }

        buttonBackSound = findViewById(R.id.button_back_sound);
        if (buttonBackSound != null) {
            buttonBackSound.setOnClickListener(view -> {
                playButtonSound();
                settings_cardview.setVisibility(View.VISIBLE);
                settingsButtonCardview.setVisibility(View.VISIBLE);
                musicCardview.setVisibility(View.GONE);
                soundCardview.setVisibility(View.GONE);
                languageCardview.setVisibility(View.GONE);
            });
        }

        buttonBackLanguage = findViewById(R.id.button_back_language);
        if (buttonBackLanguage != null) {
            buttonBackLanguage.setOnClickListener(view -> {
                playButtonSound();
                settings_cardview.setVisibility(View.VISIBLE);
                settingsButtonCardview.setVisibility(View.VISIBLE);
                musicCardview.setVisibility(View.GONE);
                soundCardview.setVisibility(View.GONE);
                languageCardview.setVisibility(View.GONE);
            });
        }

    }

    private void setupSystemBarsPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

//    private List<Profile> readProfiles() {
//        List<Profile> profiles = new ArrayList<>();
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        String[] projection = {
//                "profile_id", "name", "avatar_id"
//        };
//
//        try (Cursor cursor = db.query(
//                Profile.TABLE_NAME,
//                projection,
//                null,
//                null,
//                null,
//                null,
//                null
//        )) {
//            while (cursor.moveToNext()) {
//                int profileId = cursor.getInt(cursor.getColumnIndexOrThrow("profile_id"));
//                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
//                int avatarId = cursor.getInt(cursor.getColumnIndexOrThrow("avatar_id"));
//                int achievement = cursor.getInt(cursor.getColumnIndexOrThrow("achievement"));
//                int tutorial_done_location_1 = cursor.getInt(cursor.getColumnIndexOrThrow("tutorial_done_location_1"));
//                int tutorial_done_location_2 = cursor.getInt(cursor.getColumnIndexOrThrow("tutorial_done_location_2"));
//                int tutorial_done_location_3 = cursor.getInt(cursor.getColumnIndexOrThrow("tutorial_done_location_3"));
//                profiles.add(new Profile(profileId, name, avatarId, achievement, tutorial_done_location_1, tutorial_done_location_2, tutorial_done_location_3));
//            }
//        } catch (Exception e) {
//            Log.e("MainActivity", "Error reading profiles", e);
//        }
//        return profiles;
//    }

    @Override
    protected void onDestroy() {
        if (mysound != null) {
            mysound.release(); // Release the MediaPlayer when it's no longer needed
            mysound = null;
        }
        dbHelper.close(); // Close the database helper
        super.onDestroy();
    }

    private void setupInformationAboutUs(){
        View blocker = findViewById(R.id.click_blocker);


        ImageView aboutUs_imageView = findViewById(R.id.aboutUs);
        CardView aboutUs_cardView = findViewById(R.id.about_us);

        Button ourTeam_button = findViewById(R.id.ourTeam_button);
        Button reference_button = findViewById(R.id.reference_button);
        Button terms_button = findViewById(R.id.terms_button);

        TextView txt_backAboutUs = findViewById(R.id.txt_backAboutUs);

        CardView cardView_ourTeam = findViewById(R.id.cardView_ourTeam);
        CardView cardView_reference = findViewById(R.id.cardView_reference);
        CardView cardView_terms = findViewById(R.id.cardView_terms);

        int originalColor = Color.parseColor("#00000000");

        if (aboutUs_imageView != null) {
            aboutUs_imageView.setOnClickListener(view -> {
                playButtonSound();
                blocker.setVisibility(View.VISIBLE);
                aboutUs_cardView.setVisibility(View.VISIBLE);

                aboutUs_cardView.setClickable(false);

            });
        }

        if (ourTeam_button != null) {
            ourTeam_button.setOnClickListener(view -> {
                playButtonSound();
                blocker.setVisibility(View.VISIBLE);
                cardView_ourTeam.setVisibility(View.VISIBLE);
                cardView_reference.setVisibility(View.GONE);
                cardView_terms.setVisibility(View.GONE);

                // Set the clicked button to a new color
                ourTeam_button.setBackgroundColor(Color.parseColor("#E6EBBE95"));

                // Reset other buttons to their original state
                reference_button.setBackgroundColor(originalColor);
                reference_button.setAlpha(1.0f);
                terms_button.setBackgroundColor(originalColor);
                terms_button.setAlpha(1.0f);
            });
        }

        if (reference_button != null) {
            reference_button.setOnClickListener(view -> {
                playButtonSound();
                blocker.setVisibility(View.VISIBLE);
                cardView_reference.setVisibility(View.VISIBLE);
                cardView_ourTeam.setVisibility(View.GONE);
                cardView_terms.setVisibility(View.GONE);

                // Set the clicked button to a new color
                reference_button.setBackgroundColor(Color.parseColor("#E6EBBE95"));

                // Reset other buttons to their original state
                ourTeam_button.setBackgroundColor(originalColor);
                ourTeam_button.setAlpha(1.0f);
                terms_button.setBackgroundColor(originalColor);
                terms_button.setAlpha(1.0f);
            });
        }

        if (terms_button != null) {
            terms_button.setOnClickListener(view -> {
                playButtonSound();
                blocker.setVisibility(View.VISIBLE);
                cardView_terms.setVisibility(View.VISIBLE);
                cardView_reference.setVisibility(View.GONE);
                cardView_ourTeam.setVisibility(View.GONE);

                // Set the clicked button to a new color
                terms_button.setBackgroundColor(Color.parseColor("#E6EBBE95"));

                // Reset other buttons to their original state
                ourTeam_button.setBackgroundColor(originalColor);
                ourTeam_button.setAlpha(1.0f);
                reference_button.setBackgroundColor(originalColor);
                reference_button.setAlpha(1.0f);
            });
        }

        if (txt_backAboutUs != null) {
            txt_backAboutUs.setOnClickListener(view -> {
                playButtonSound();
                blocker.setVisibility(View.GONE);
                aboutUs_cardView.setVisibility(View.GONE);

                aboutUs_cardView.setClickable(true);
            });
        }
    }

}