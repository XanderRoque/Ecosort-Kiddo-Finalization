package com.ecosort.ecosortkiddo;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ecosort.ecosortkiddo.dao.ProfileDao;
import com.ecosort.ecosortkiddo.dao.SettingsDao;
import com.ecosort.ecosortkiddo.model.Location;
import com.ecosort.ecosortkiddo.model.Profile;
import com.ecosort.ecosortkiddo.model.Settings;
import com.ecosort.ecosortkiddo.model.StarRating;
import com.ecosort.ecosortkiddo.utils.TranslatorUtil;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private ProfileDao profileDao;
    private Profile myProfile;
    private EditText username;
    private int selectedAvatarId;
    private CardView cardView;

    // Variables to store initial profile data
    private String initialUsername;
    private int initialAvatarId;
    private Settings settings;
    private SettingsDao settingsDao;
    private MediaPlayer mysound;
    private boolean isSoundOn = true;

    private void initializeDependencies() {
        // Initialize the ProfileDao
        profileDao = new ProfileDao(this);
        settingsDao = new SettingsDao(this);

        mysound = MediaPlayer.create(this, R.raw.buttons);
        mysound.setLooping(false);
        mysound.seekTo(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use the utility class to enable fullscreen and immersive modes
        Hide_Navigation.enableFullscreen(this);
        Hide_Navigation.enableImmersiveMode(this);

        initializeDependencies();

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_profile);
        cardView = findViewById(R.id.card_View);
        cardView.setVisibility(View.GONE);

        // Load profile data
        myProfile = profileDao.getProfile(1);
        int achievementCount = setProfileAchievement(); //update the achievement

        // Awards
        setAwards(achievementCount);

        int avatarId = myProfile.getAvatarId();
        selectedAvatarId = avatarId; // Set the initial selected avatar ID
        username = findViewById(R.id.profile_name);
        String name = myProfile.getName();

        // Store initial profile data
        initialUsername = name;
        initialAvatarId = avatarId;

        // Set initial avatar
        setAvatar(avatarId);

        // Set the username
        username.setText(name);

        // Set click listeners for avatar ImageViews
        setAvatarClickListeners();

        ImageButton exitButton = findViewById(R.id.exit_button_location1);
        if (exitButton != null) {
            exitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playButtonSound();
                    if (hasChanges()) {
                        findViewById(R.id.click_blocker).setVisibility(View.VISIBLE);
                        cardView.setVisibility(View.VISIBLE);
                    } else {
                        finish();
                    }
                }
            });
        }

        Button saveButton = findViewById(R.id.btn_Save);
        if (saveButton != null) {
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playButtonSound();
                    myProfile.setName(username.getText().toString());
                    myProfile.setAvatarId(selectedAvatarId); // Update the profile with the selected avatar ID
                    profileDao.updateProfile(myProfile);

                    findViewById(R.id.click_blocker).setVisibility(View.GONE);
                    finish();
                }
            });
        }

        Button cancelButton = findViewById(R.id.btn_dontsave);
        if (cancelButton != null) {
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playButtonSound();

                    // Reset the profile data to initial values
                    myProfile.setName(initialUsername);
                    myProfile.setAvatarId(initialAvatarId);

                    // Update the UI with the reset avatar
                    setAvatar(initialAvatarId);

                    // Navigate back to the MainActivity
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);

                    // Hide the click blocker
                    findViewById(R.id.click_blocker).setVisibility(View.GONE);

                    // Finish the current activity to clear it from the back stack
                    finish();
                }
            });
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        settings = settingsDao.getSettings(1);

        //convert to chosen language
        translateText(settings.getLanguage());
    }

    private void translateText(String languageCode){

        TextView textAchievements = findViewById(R.id.txt_achievements);
        textAchievements.setText(TranslatorUtil.translate(textAchievements.getText().toString(), languageCode));

        TextView textSaveMessage = findViewById(R.id.save_message);
        textSaveMessage.setText(TranslatorUtil.translate(textSaveMessage.getText().toString(), languageCode));

        Button buttonSave = findViewById(R.id.btn_Save);
        buttonSave.setText(TranslatorUtil.translate(buttonSave.getText().toString(), languageCode));

        Button buttonDontSave = findViewById(R.id.btn_dontsave);
        buttonDontSave.setText(TranslatorUtil.translate(buttonDontSave.getText().toString(), languageCode));

    }

    @Override
    public void onBackPressed() {

        if (hasChanges()) {
            // Show the CardView if any change is detected
            findViewById(R.id.click_blocker).setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);
        } else {
            // If no change, navigate to MainActivity
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//            finish();
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (hasChanges()) {
            // Show the CardView if any change is detected
            findViewById(R.id.click_blocker).setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);
        } else {
            // If no change, navigate to MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            //finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (hasChanges()) {
            // Show the CardView if any change is detected
            findViewById(R.id.click_blocker).setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);
        } else {
            // If no change, navigate to MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            //finish();
        }
    }

    private void setAvatar(int avatarId) {
        String filename = "avatar_" + avatarId;
        int resourceId = getResources().getIdentifier(filename, "drawable", getPackageName());
        ImageView imageAvatar = findViewById(R.id.change_avatar);
        imageAvatar.setImageResource(resourceId);
        Log.d("Setting image to", filename);

        // Update the avatar ID in the profile object
        myProfile.setAvatarId(avatarId);

        int achievementCount = computeAchievement();
        myProfile.setAchievement(achievementCount);

        // Save the updated profile to the database
        profileDao.updateProfile(myProfile);
    }

    private int setProfileAchievement() {

        int achievementCount = computeAchievement();
        myProfile.setAchievement(achievementCount);

        // Save the updated profile to the database
        profileDao.updateProfile(myProfile);

        return achievementCount;
    }

    private void setAvatarClickListeners() {
        int[] avatarIds = {R.id.first_avatar, R.id.second_avatar, R.id.third_avatar, R.id.fourth_avatar};

        for (int id : avatarIds) {
            ImageView avatarView = findViewById(id);
            avatarView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.first_avatar) {
                        playButtonSound();
                        selectedAvatarId = 1;
                    } else if (v.getId() == R.id.second_avatar) {
                        playButtonSound();
                        selectedAvatarId = 2;
                    } else if (v.getId() == R.id.third_avatar) {
                        playButtonSound();
                        selectedAvatarId = 3;
                    } else if (v.getId() == R.id.fourth_avatar) {
                        playButtonSound();
                        selectedAvatarId = 4;
                    }
                    setAvatar(selectedAvatarId); // Update avatar and save to database
                }
            });
        }
    }

    private void setAwards(int achievementCount) {
        ImageView garbageGuru = findViewById(R.id.garbageGuru);
        ImageView trashTactician = findViewById(R.id.trashTactician);
        ImageView ecoOrganizer = findViewById(R.id.ecoOrganizer);
        ImageView ecosortChampion = findViewById(R.id.eco_Master);

        // Define a method to set alpha and color filter for a view
        setAlphaAndColorFilter(garbageGuru, 0.8f, Color.argb(255, 23, 23, 23));
        setAlphaAndColorFilter(trashTactician, 0.8f, Color.argb(255, 23, 23, 23));
        setAlphaAndColorFilter(ecoOrganizer, 0.8f, Color.argb(255, 23, 23, 23));
        setAlphaAndColorFilter(ecosortChampion, 0.8f, Color.argb(255, 23, 23, 23));

        switch (achievementCount) {
            case 0:
                // No achievements unlocked
                // Do something (e.g., set all views to low transparency)
                setAlphaAndColorFilter(garbageGuru, 0.8f, Color.argb(255, 23, 23, 23));
                setAlphaAndColorFilter(trashTactician, 0.8f, Color.argb(255, 23, 23, 23));
                setAlphaAndColorFilter(ecoOrganizer, 0.8f, Color.argb(255, 23, 23, 23));
                setAlphaAndColorFilter(ecosortChampion, 0.8f, Color.argb(255, 23, 23, 23));
                break;

            case 1:
                // Only "Garbage Guru" unlocked (0001)
                // Set views or behavior related to Garbage Guru
                garbageGuru.clearColorFilter();
                garbageGuru.setAlpha(1.0f);
                setAlphaAndColorFilter(trashTactician, 0.8f, Color.argb(255, 23, 23, 23));
                setAlphaAndColorFilter(ecoOrganizer, 0.8f, Color.argb(255, 23, 23, 23));
                setAlphaAndColorFilter(ecosortChampion, 0.8f, Color.argb(255, 23, 23, 23));
                break;

            case 2:
                // Only "Trash Tactician" unlocked (0010)
                // Set views or behavior related to Trash Tactician
                setAlphaAndColorFilter(garbageGuru, 0.8f, Color.argb(255, 23, 23, 23));
                trashTactician.clearColorFilter();
                trashTactician.setAlpha(1.0f);
                setAlphaAndColorFilter(ecoOrganizer, 0.8f, Color.argb(255, 23, 23, 23));
                setAlphaAndColorFilter(ecosortChampion, 0.8f, Color.argb(255, 23, 23, 23));
                break;

            case 4:
                // Only "Eco-Organizer" unlocked (0100)
                // Set views or behavior related to Eco-Organizer
                setAlphaAndColorFilter(garbageGuru, 0.8f, Color.argb(255, 23, 23, 23));
                setAlphaAndColorFilter(trashTactician, 0.8f, Color.argb(255, 23, 23, 23));
                ecoOrganizer.clearColorFilter();
                ecoOrganizer.setAlpha(1.0f);
                setAlphaAndColorFilter(ecosortChampion, 0.8f, Color.argb(255, 23, 23, 23));
                break;

            case 8:
                // Only "Ecosort Champion" unlocked (1000)
                // Set views or behavior related to Ecosort Champion
                setAlphaAndColorFilter(garbageGuru, 0.8f, Color.argb(255, 23, 23, 23));
                setAlphaAndColorFilter(trashTactician, 0.8f, Color.argb(255, 23, 23, 23));
                setAlphaAndColorFilter(ecoOrganizer, 0.8f, Color.argb(255, 23, 23, 23));
                ecosortChampion.clearColorFilter();
                ecosortChampion.setAlpha(1.0f);
                break;

            case 3:
                // "Garbage Guru" and "Trash Tactician" unlocked (0011)
                // Set views for both achievements
                garbageGuru.clearColorFilter();
                garbageGuru.setAlpha(1.0f);
                trashTactician.clearColorFilter();
                trashTactician.setAlpha(1.0f);
                setAlphaAndColorFilter(ecoOrganizer, 0.8f, Color.argb(255, 23, 23, 23));
                setAlphaAndColorFilter(ecosortChampion, 0.8f, Color.argb(255, 23, 23, 23));
                break;

            case 5:
                // "Garbage Guru" and "Eco-Organizer" unlocked (0101)
                // Set views for both achievements
                garbageGuru.clearColorFilter();
                garbageGuru.setAlpha(1.0f);
                setAlphaAndColorFilter(trashTactician, 0.8f, Color.argb(255, 23, 23, 23));
                ecoOrganizer.clearColorFilter();
                ecoOrganizer.setAlpha(1.0f);
                setAlphaAndColorFilter(ecosortChampion, 0.8f, Color.argb(255, 23, 23, 23));
                break;

            case 9:
                // "Garbage Guru" and "Ecosort Champion" unlocked (1001)
                // Set views for both achievements
                garbageGuru.clearColorFilter();
                garbageGuru.setAlpha(1.0f);
                setAlphaAndColorFilter(trashTactician, 0.8f, Color.argb(255, 23, 23, 23));
                setAlphaAndColorFilter(ecoOrganizer, 0.8f, Color.argb(255, 23, 23, 23));
                ecosortChampion.clearColorFilter();
                ecosortChampion.setAlpha(1.0f);
                break;

            case 6:
                // "Trash Tactician" and "Eco-Organizer" unlocked (0110)
                // Set views for both achievements
                setAlphaAndColorFilter(garbageGuru, 0.8f, Color.argb(255, 23, 23, 23));
                trashTactician.clearColorFilter();
                trashTactician.setAlpha(1.0f);
                ecoOrganizer.clearColorFilter();
                ecoOrganizer.setAlpha(1.0f);
                setAlphaAndColorFilter(ecosortChampion, 0.8f, Color.argb(255, 23, 23, 23));
                break;

            case 10:
                // "Trash Tactician" and "Ecosort Champion" unlocked (1010)
                // Set views for both achievements
                setAlphaAndColorFilter(garbageGuru, 0.8f, Color.argb(255, 23, 23, 23));
                trashTactician.clearColorFilter();
                trashTactician.setAlpha(1.0f);
                setAlphaAndColorFilter(ecoOrganizer, 0.8f, Color.argb(255, 23, 23, 23));
                ecosortChampion.clearColorFilter();
                ecosortChampion.setAlpha(1.0f);
                break;

            case 12:
                // "Eco-Organizer" and "Ecosort Champion" unlocked (1100)
                // Set views for both achievements
                setAlphaAndColorFilter(garbageGuru, 0.8f, Color.argb(255, 23, 23, 23));
                setAlphaAndColorFilter(trashTactician, 0.8f, Color.argb(255, 23, 23, 23));
                ecoOrganizer.clearColorFilter();
                ecoOrganizer.setAlpha(1.0f);
                ecosortChampion.clearColorFilter();
                ecosortChampion.setAlpha(1.0f);
                break;

            case 7:
                // "Garbage Guru", "Trash Tactician", and "Eco-Organizer" unlocked (0111)
                // Set views for all three achievements
                garbageGuru.clearColorFilter();
                garbageGuru.setAlpha(1.0f);
                trashTactician.clearColorFilter();
                trashTactician.setAlpha(1.0f);
                ecoOrganizer.clearColorFilter();
                ecoOrganizer.setAlpha(1.0f);
                setAlphaAndColorFilter(ecosortChampion, 0.8f, Color.argb(255, 23, 23, 23));
                break;

            case 11:
                // "Garbage Guru", "Trash Tactician", and "Ecosort Champion" unlocked (1011)
                // Set views for all three achievements
                garbageGuru.clearColorFilter();
                garbageGuru.setAlpha(1.0f);
                trashTactician.clearColorFilter();
                trashTactician.setAlpha(1.0f);
                setAlphaAndColorFilter(ecoOrganizer, 0.8f, Color.argb(255, 23, 23, 23));
                ecosortChampion.clearColorFilter();
                ecosortChampion.setAlpha(1.0f);
                break;

            case 13:
                // "Garbage Guru", "Eco-Organizer", and "Ecosort Champion" unlocked (1101)
                // Set views for all three achievements
                garbageGuru.clearColorFilter();
                garbageGuru.setAlpha(1.0f);
                setAlphaAndColorFilter(trashTactician, 0.8f, Color.argb(255, 23, 23, 23));
                ecoOrganizer.clearColorFilter();
                ecoOrganizer.setAlpha(1.0f);
                ecosortChampion.clearColorFilter();
                ecosortChampion.setAlpha(1.0f);
                break;

            case 14:
                // "Trash Tactician", "Eco-Organizer", and "Ecosort Champion" unlocked (1110)
                // Set views for all three achievements
                setAlphaAndColorFilter(garbageGuru, 0.8f, Color.argb(255, 23, 23, 23));
                trashTactician.clearColorFilter();
                trashTactician.setAlpha(1.0f);
                ecoOrganizer.clearColorFilter();
                ecoOrganizer.setAlpha(1.0f);
                ecosortChampion.clearColorFilter();
                ecosortChampion.setAlpha(1.0f);
                break;

            case 15:
                // All achievements unlocked (1111)
                // Set views or behavior when all achievements are unlocked
                garbageGuru.clearColorFilter();
                garbageGuru.setAlpha(1.0f);
                trashTactician.clearColorFilter();
                trashTactician.setAlpha(1.0f);
                ecoOrganizer.clearColorFilter();
                ecoOrganizer.setAlpha(1.0f);
                ecosortChampion.clearColorFilter();
                ecosortChampion.setAlpha(1.0f);
                break;

            default:
                // Handle any other unexpected values
                setAlphaAndColorFilter(garbageGuru, 0.8f, Color.argb(255, 23, 23, 23));
                setAlphaAndColorFilter(trashTactician, 0.8f, Color.argb(255, 23, 23, 23));
                setAlphaAndColorFilter(ecoOrganizer, 0.8f, Color.argb(255, 23, 23, 23));
                setAlphaAndColorFilter(ecosortChampion, 0.8f, Color.argb(255, 23, 23, 23));
                break;
        }

//        switch (achievementCount) {
//            case 0:
//                // All views fully transparent
//                setAlphaAndColorFilter(garbageGuru, 0.8f, Color.argb(255, 23, 23, 23));
//                setAlphaAndColorFilter(trashTactician, 0.8f, Color.argb(255, 23, 23, 23));
//                setAlphaAndColorFilter(ecoOrganizer, 0.8f, Color.argb(255, 23, 23, 23));
//                setAlphaAndColorFilter(ecosortChampion, 0.8f, Color.argb(255, 23, 23, 23));
//                break;
//
//            case 1:
//                garbageGuru.clearColorFilter();
//                garbageGuru.setAlpha(1.0f);
//                setAlphaAndColorFilter(trashTactician, 0.8f, Color.argb(255, 23, 23, 23));
//                setAlphaAndColorFilter(ecoOrganizer, 0.8f, Color.argb(255, 23, 23, 23));
//                setAlphaAndColorFilter(ecosortChampion, 0.8f, Color.argb(255, 23, 23, 23));
//                break;
//
//            case 2:
//                setAlphaAndColorFilter(garbageGuru, 0.8f, Color.argb(255, 23, 23, 23));
//                trashTactician.clearColorFilter();
//                trashTactician.setAlpha(1.0f);
//                setAlphaAndColorFilter(ecoOrganizer, 0.8f, Color.argb(255, 23, 23, 23));
//                setAlphaAndColorFilter(ecosortChampion, 0.8f, Color.argb(255, 23, 23, 23));
//                break;
//
//            case 3:
//                garbageGuru.clearColorFilter();
//                garbageGuru.setAlpha(1.0f);
//                trashTactician.clearColorFilter();
//                trashTactician.setAlpha(1.0f);
//                setAlphaAndColorFilter(ecoOrganizer, 0.8f, Color.argb(255, 23, 23, 23));
//                setAlphaAndColorFilter(ecosortChampion, 0.8f, Color.argb(255, 23, 23, 23));
//                break;
//
//            case 4:
//                setAlphaAndColorFilter(garbageGuru, 0.8f, Color.argb(255, 23, 23, 23));
//                setAlphaAndColorFilter(trashTactician, 0.8f, Color.argb(255, 23, 23, 23));
//                ecoOrganizer.clearColorFilter();
//                ecoOrganizer.setAlpha(1.0f);
//                setAlphaAndColorFilter(ecosortChampion, 0.8f, Color.argb(255, 23, 23, 23));
//                break;
//
//            case 5:
//                garbageGuru.clearColorFilter();
//                garbageGuru.setAlpha(1.0f);
//                setAlphaAndColorFilter(trashTactician, 0.8f, Color.argb(255, 23, 23, 23));
//                ecoOrganizer.clearColorFilter();
//                ecoOrganizer.setAlpha(1.0f);
//                setAlphaAndColorFilter(ecosortChampion, 0.8f, Color.argb(255, 23, 23, 23));
//                break;
//
//            case 6:
//                setAlphaAndColorFilter(garbageGuru, 0.8f, Color.argb(255, 23, 23, 23));
//                trashTactician.clearColorFilter();
//                trashTactician.setAlpha(1.0f);
//                ecoOrganizer.clearColorFilter();
//                ecoOrganizer.setAlpha(1.0f);
//                setAlphaAndColorFilter(ecosortChampion, 0.8f, Color.argb(255, 23, 23, 23));
//                break;
//
//            default:
//                garbageGuru.clearColorFilter();
//                garbageGuru.setAlpha(1.0f);
//                trashTactician.clearColorFilter();
//                trashTactician.setAlpha(1.0f);
//                ecoOrganizer.clearColorFilter();
//                ecoOrganizer.setAlpha(1.0f);
//                ecosortChampion.clearColorFilter();
//                ecosortChampion.setAlpha(1.0f);
//                break;
//        }
    }

    // Method to set both alpha and color filter
    private void setAlphaAndColorFilter(ImageView view, float alpha, int color) {
        view.setAlpha(alpha);
        view.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }

    // Method to check if there are any changes
    private boolean hasChanges() {
        String currentUsername = username.getText().toString();
        return !currentUsername.equals(initialUsername) || selectedAvatarId != initialAvatarId;
    }

    private int computeAchievement(){
        int totalStarsLocationHome = profileDao.getTotalStarsByLocationId(Location.HOME);
        int totalStarsLocationBackyard = profileDao.getTotalStarsByLocationId(Location.BACKYARD);
        int totalStarsLocationForest= profileDao.getTotalStarsByLocationId(Location.FOREST);
        int totalStarsLocationBeach= profileDao.getTotalStarsByLocationId(Location.BEACH);

        int achievementCount = 0;

        if(totalStarsLocationHome == StarRating.TOTAL_STARS_HOME){
            achievementCount = achievementCount + 1;
        }

        if(totalStarsLocationBackyard == StarRating.TOTAL_STARS_BACKYARD){
            achievementCount = achievementCount + 2;
        }

        if(totalStarsLocationForest == StarRating.TOTAL_STARS_FOREST){
            achievementCount = achievementCount + 4;
        }

        if(totalStarsLocationBeach == StarRating.TOTAL_STARS_BEACH){
            achievementCount = achievementCount + 8;
        }

        return achievementCount;
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