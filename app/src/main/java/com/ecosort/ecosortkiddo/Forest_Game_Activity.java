package com.ecosort.ecosortkiddo;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;

import com.ecosort.ecosortkiddo.dao.GarbageDao;
import com.ecosort.ecosortkiddo.dao.ProfileDao;
import com.ecosort.ecosortkiddo.dao.SettingsDao;
import com.ecosort.ecosortkiddo.dao.StarRatingDao;
import com.ecosort.ecosortkiddo.model.Garbage;
import com.ecosort.ecosortkiddo.model.GarbageCategory;
import com.ecosort.ecosortkiddo.model.Location;
import com.ecosort.ecosortkiddo.model.Profile;
import com.ecosort.ecosortkiddo.model.Settings;
import com.ecosort.ecosortkiddo.model.StarRating;
import com.ecosort.ecosortkiddo.utils.DateUtil;
import com.ecosort.ecosortkiddo.utils.TranslatorUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Forest_Game_Activity extends AppCompatActivity {

    private static final int NUM_GARBAGE_IMAGE_VIEWS = 25;
    public int numberOfGarbageInLevel;
    private static final long TIMER_DURATION = 180000; // 20 seconds in milliseconds
    private ConstraintLayout container;
    private EditText timeEditText;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private int disposedCount = 0;
    private CardView cardViewTimesOut, cardView, cardViewEndGame;
    private TextView textViewTimesOut;
    private ImageView victoryMessage;
    private ImageView failedMessage;
    private Button nextButton;
    private StarRatingDao starRatingDao;
    private GarbageDao garbageDao;
    private ProfileDao profileDao;
    private Settings settings;
    private SettingsDao settingsDao;
    private String selectedLevel = "";
    private static Map<Integer, Integer> imageIdToNameMap =  new HashMap<>();
    int selectedLevelInt = 0;
    private ImageView recyclable;
    private ImageView biodegradable;
    private ImageView nonBiodegradable;
    private ImageView selectedGarbage = null;
    private int selectedGarbageCategoryId;
    private MediaPlayer mysound;
    private boolean isSoundOn = true;
    private MediaPlayer drop_trash_sound;
    private boolean isDropTrashSoundOn = true;
    private MediaPlayer wrong_answer_sound;
    private boolean isWrongAnswer = true;
    private MediaPlayer mymusic;
    private boolean isMusicOn = true;
    private MediaPlayer applause_CompletedStar;
    private boolean isApplauseSoundOn = true;
    private MediaPlayer gameOverSound;
    private boolean isGameOverSoundOn = true;

    static {
        imageIdToNameMap.put(1, R.id.garbage1);
        imageIdToNameMap.put(2, R.id.garbage2);
        imageIdToNameMap.put(3, R.id.garbage3);
        imageIdToNameMap.put(4, R.id.garbage4);
        imageIdToNameMap.put(5, R.id.garbage5);
        imageIdToNameMap.put(6, R.id.garbage6);
        imageIdToNameMap.put(7, R.id.garbage7);
        imageIdToNameMap.put(8, R.id.garbage8);
        imageIdToNameMap.put(9, R.id.garbage9);
        imageIdToNameMap.put(10, R.id.garbage10);
        imageIdToNameMap.put(11, R.id.garbage11);
        imageIdToNameMap.put(12, R.id.garbage12);
        imageIdToNameMap.put(13, R.id.garbage13);
        imageIdToNameMap.put(14, R.id.garbage14);
        imageIdToNameMap.put(15, R.id.garbage15);
        imageIdToNameMap.put(16, R.id.garbage16);
        imageIdToNameMap.put(17, R.id.garbage17);
        imageIdToNameMap.put(18, R.id.garbage18);
        imageIdToNameMap.put(19, R.id.garbage19);
        imageIdToNameMap.put(20, R.id.garbage20);
        imageIdToNameMap.put(21, R.id.garbage21);
        imageIdToNameMap.put(22, R.id.garbage22);
        imageIdToNameMap.put(23, R.id.garbage23);
        imageIdToNameMap.put(24, R.id.garbage24);
        imageIdToNameMap.put(25, R.id.garbage25);
    }

    //Tutorial English
    private int currentTutorialIndex = 0;
    private int[] tutorialImages = {
            R.drawable.forest_story1,
            R.drawable.forest_story2,
            R.drawable.forest_story3,
            R.drawable.forest_story4,
            R.drawable.forest_tutorial1,
            R.drawable.forest_tutorial2,
            R.drawable.forest_tutorial3
    };
    private ImageView tutorialImageView;
    private CardView tutorialCardview;

    //Tutorial Filipino
    private int currentFilipinoTutorialIndex = 0;
    private int[] tutorialFilipinoImages = {
            R.drawable.forest_filipino_story1,
            R.drawable.forest_filipino_story2,
            R.drawable.forest_filipino_story3,
            R.drawable.forest_filipino_story4,
            R.drawable.forest_filipino_tutorial1,
            R.drawable.forest_filipino_tutorial2,
            R.drawable.forest_filipino_tutorial3
    };
    private ImageView tutorialFilipinoImageView;
    private CardView tutorialFilipinoCardview;

    //Earn Reward
    private int currentRewardIndex = 0;
    private int[] rewardImages = {
            R.drawable.forest_earn1,
            R.drawable.forest_earn2,
            R.drawable.forest_earn3,
            R.drawable.forest_story5,
            R.drawable.forest_story6
    };
    private ImageView rewardImageView;
    private CardView rewardCardview;

    //Earn Reward Filipino
    private int currentFilipinoRewardIndex = 0;
    private int[] rewardFilipinoImages = {
            R.drawable.forest_filipino_earn1,
            R.drawable.forest_filipino_earn2,
            R.drawable.forest_filipino_earn3,
            R.drawable.forest_filipino_story5,
            R.drawable.forest_filipino_story6
    };
    private ImageView rewardFilipinoImageView;
    private CardView rewardFilipinoCardview;

    private void initializeDependencies() {
        // Initialize the ProfileDao
        starRatingDao = new StarRatingDao(this);
        garbageDao = new GarbageDao(this);
        profileDao = new ProfileDao(this);
        settingsDao = new SettingsDao(this);

        mysound = MediaPlayer.create(this, R.raw.buttons);
        mymusic = MediaPlayer.create(this, R.raw.ingame_music);
        mymusic.setLooping(true);
        drop_trash_sound = MediaPlayer.create(this, R.raw.drop_trash);
        wrong_answer_sound = MediaPlayer.create(this, R.raw.wrong_answer);

        applause_CompletedStar = MediaPlayer.create(this, R.raw.applause);

        gameOverSound = MediaPlayer.create(this, R.raw.game_over);
    }

    private void setBias(ImageView imageView, float horizontalBias, float verticalBias) {
        // Get the current layout parameters
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();

        // Set the horizontal and vertical biases
        params.horizontalBias = horizontalBias;
        params.verticalBias = verticalBias;

        // Apply the updated layout parameters
        imageView.setLayoutParams(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeDependencies();
        Intent intent = getIntent();
        selectedLevel = intent.getStringExtra("selectedLevel");

        Hide_Navigation.enableFullscreen(this);
        Hide_Navigation.enableTrueImmersiveMode(this);
        setContentView(R.layout.activity_forest_game);

        ConstraintLayout   layout = findViewById(R.id.activity_forest_game); // Replace with your layout ID
        selectedLevelInt = Integer.valueOf(selectedLevel);
        Log.d("DEBUG", "Selected Level: " + selectedLevelInt);
        if(selectedLevelInt == 3 || selectedLevelInt == 4){
            changeBackground(selectedLevelInt,layout);
        }

        if(selectedLevelInt == 5 || selectedLevelInt == 6){
            changeBackground(selectedLevelInt,layout);
        }

        container = findViewById(R.id.activity_forest_game);
        placeTrashCans(selectedLevelInt);

        cardViewEndGame = findViewById(R.id.cardview_endgame);
        cardViewEndGame.setVisibility(View.GONE);
        cardView = findViewById(R.id.cardView_ingame);
        cardView.setVisibility(View.GONE);
        cardViewTimesOut = findViewById(R.id.cardview_times_out);
        textViewTimesOut = findViewById(R.id.textview_times_out);
        timeEditText = findViewById(R.id.time);
        timeEditText.setKeyListener(null);
        timeEditText.setVisibility(View.VISIBLE);

        Button nextButton = findViewById(R.id.forestButton_next);

        System.out.println("Selected Level: " + selectedLevel);
        TextView text = findViewById(R.id.labelLevel);
        text.setText("Level " + selectedLevel);

//        startCountDownTimer(TIMER_DURATION);

        // Get garbage from the database
        List<Garbage> garbageList = garbageDao.getGarbageByLocationIdAndLevelId(Location.FOREST, Integer.valueOf(selectedLevel));
        numberOfGarbageInLevel = garbageList.size();
        int ctr =0;
        if(numberOfGarbageInLevel > 0){
            for (Garbage g: garbageList) {
                ctr++;
                Integer imageViewId = imageIdToNameMap.get(ctr);
                ImageView imageView = findViewById(imageViewId);
                // Set the horizontal and vertical bias
                if (imageView != null) {
                    setBias(imageView, g.getLayoutConstraintHorizontalBias().floatValue(), g.getLayoutConstraintVerticalBias().floatValue()); // Adjust the biases as needed (0.0 to 1.0)
                    setDraggable(imageView, g, true);
                }
            }
        }
        //will hide the remaining garbage
        for (int i = ctr+1; i <= NUM_GARBAGE_IMAGE_VIEWS; i++) {
            Integer imageViewId = imageIdToNameMap.get(i);
            ImageView imageView = findViewById(imageViewId);
            imageView.setVisibility(View.GONE);
        }

//        victory and failed message
        victoryMessage = findViewById(R.id.victory_message);
        failedMessage = findViewById(R.id.gameover_message);

        ImageView starImageView = findViewById(R.id.stars_got);

        biodegradable = findViewById(R.id.biodegradable);
        nonBiodegradable = findViewById(R.id.non_biodegradable);
        recyclable = findViewById(R.id.recyclable);

        recyclable.setVisibility(View.GONE);
        nonBiodegradable.setVisibility(View.GONE);
        biodegradable.setVisibility(View.GONE);

        switch (selectedLevel) {
            case "1":
                biodegradable.setVisibility(View.VISIBLE);
                int biodegradableGarbageCountLevel1 = getGarbageCategoryCount(garbageList, GarbageCategory.BIODEGRADABLE);

                numberOfGarbageInLevel = biodegradableGarbageCountLevel1;
                break;
            case "2":
                biodegradable.setVisibility(View.VISIBLE);
                nonBiodegradable.setVisibility(View.VISIBLE);

                int biodegradableGarbageCountLevel2 = getGarbageCategoryCount(garbageList, GarbageCategory.BIODEGRADABLE);
                int nonBiodegradableGarbageCountLevel2 = getGarbageCategoryCount(garbageList, GarbageCategory.NON_BIODEGRADABLE);
                numberOfGarbageInLevel = biodegradableGarbageCountLevel2 + nonBiodegradableGarbageCountLevel2;
                break;
            case "3":
                recyclable.setVisibility(View.VISIBLE);
                nonBiodegradable.setVisibility(View.VISIBLE);

                int recyclableGarbageCountLevel3 = getGarbageCategoryCount(garbageList, GarbageCategory.RECYCLABLE);
                int nonBiodegradableGarbageCountLevel3 = getGarbageCategoryCount(garbageList, GarbageCategory.NON_BIODEGRADABLE);
                numberOfGarbageInLevel = recyclableGarbageCountLevel3 + nonBiodegradableGarbageCountLevel3;
                break;
            case "4":
                biodegradable.setVisibility(View.VISIBLE);
                nonBiodegradable.setVisibility(View.VISIBLE);

                int biodegradableGarbageCountLevel4 = getGarbageCategoryCount(garbageList, GarbageCategory.BIODEGRADABLE);
                int nonBiodegradableGarbageCountLevel4 = getGarbageCategoryCount(garbageList, GarbageCategory.NON_BIODEGRADABLE);
                numberOfGarbageInLevel = nonBiodegradableGarbageCountLevel4 + biodegradableGarbageCountLevel4;
                break;
            case "5":
            case "6":
                recyclable.setVisibility(View.VISIBLE);
                nonBiodegradable.setVisibility(View.VISIBLE);
                biodegradable.setVisibility(View.VISIBLE);

                int recyclableGarbageCountLevel5N6 = getGarbageCategoryCount(garbageList, GarbageCategory.RECYCLABLE);
                int nonBiodegradableGarbageCountLevel5N6 = getGarbageCategoryCount(garbageList, GarbageCategory.NON_BIODEGRADABLE);
                int biodegradableGarbageCountLevel5N6 = getGarbageCategoryCount(garbageList, GarbageCategory.BIODEGRADABLE);
                numberOfGarbageInLevel = recyclableGarbageCountLevel5N6 + nonBiodegradableGarbageCountLevel5N6 + biodegradableGarbageCountLevel5N6;
                break;
        }


        //biodegradable = 1
        biodegradable.setOnDragListener((v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    return true;
                case DragEvent.ACTION_DROP:
                    System.out.println(selectedGarbageCategoryId);
                    if(selectedGarbageCategoryId == GarbageCategory.BIODEGRADABLE){
                        View draggedView = (View) event.getLocalState();
                        container.removeView(draggedView);
                        dropTrashSound();
                        //Toast.makeText(getApplicationContext(), "Trash disposed!", Toast.LENGTH_SHORT).show();
                        disposedCount++;
                        if (disposedCount == numberOfGarbageInLevel) {
                            showEndGameMessage(numberOfGarbageInLevel, selectedLevelInt);
                        }
                    } else{
                        wrongAnswerSound();

                        // Subtract 3 seconds from the timer
                        timeLeftInMillis -= 3000; // Deduct 3 seconds (3000 milliseconds)

                        // Ensure the timer doesn't go below 0
                        if (timeLeftInMillis < 0) {
                            timeLeftInMillis = 0;
                        }

                        // Show a message to the user
                        Toast.makeText(this, "Try again. 3 seconds deducted.", Toast.LENGTH_SHORT).show();

                        // Cancel the current timer and start a new one with the updated time
                        if (countDownTimer != null) {
                            countDownTimer.cancel(); // Stop the current timer
                        }
                        startCountDownTimer(timeLeftInMillis); // Start the timer with updated time
                    }
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    return true;
                default:
                    return false;
            }
        });

        //nonBiodegradable = 2
        nonBiodegradable.setOnDragListener((v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    return true;
                case DragEvent.ACTION_DROP:
                    System.out.println(selectedGarbageCategoryId);
                    if(selectedGarbageCategoryId == GarbageCategory.NON_BIODEGRADABLE){
                        View draggedView = (View) event.getLocalState();
                        container.removeView(draggedView);
                        dropTrashSound();
//                        Toast.makeText(getApplicationContext(), "Trash disposed!", Toast.LENGTH_SHORT).show();
                        disposedCount++;
                        if (disposedCount == numberOfGarbageInLevel) {
                            showEndGameMessage(numberOfGarbageInLevel, selectedLevelInt);
                        }
                    }else{
                        wrongAnswerSound();

                        // Subtract 3 seconds from the timer
                        timeLeftInMillis -= 3000; // Deduct 3 seconds (3000 milliseconds)

                        // Ensure the timer doesn't go below 0
                        if (timeLeftInMillis < 0) {
                            timeLeftInMillis = 0;
                        }

                        // Show a message to the user
                        Toast.makeText(this, "Try again. 3 seconds deducted.", Toast.LENGTH_SHORT).show();

                        // Cancel the current timer and start a new one with the updated time
                        if (countDownTimer != null) {
                            countDownTimer.cancel(); // Stop the current timer
                        }
                        startCountDownTimer(timeLeftInMillis); // Start the timer with updated time
                    }
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    return true;
                default:
                    return false;
            }
        });

        //recyclable = 3
        recyclable.setOnDragListener((v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    return true;
                case DragEvent.ACTION_DROP:
                    System.out.println(selectedGarbageCategoryId);
                    if(selectedGarbageCategoryId == GarbageCategory.RECYCLABLE){
                        View draggedView = (View) event.getLocalState();
                        container.removeView(draggedView);
                        dropTrashSound();
                        //Toast.makeText(getApplicationContext(), "Trash disposed!", Toast.LENGTH_SHORT).show();
                        disposedCount++;
                        if (disposedCount == numberOfGarbageInLevel) {
                            showEndGameMessage(numberOfGarbageInLevel, selectedLevelInt);
                        }
                    }else{
                        wrongAnswerSound();

                        // Subtract 3 seconds from the timer
                        timeLeftInMillis -= 3000; // Deduct 3 seconds (3000 milliseconds)

                        // Ensure the timer doesn't go below 0
                        if (timeLeftInMillis < 0) {
                            timeLeftInMillis = 0;
                        }

                        // Show a message to the user
                        Toast.makeText(this, "Try again. 3 seconds deducted.", Toast.LENGTH_SHORT).show();

                        // Cancel the current timer and start a new one with the updated time
                        if (countDownTimer != null) {
                            countDownTimer.cancel(); // Stop the current timer
                        }
                        startCountDownTimer(timeLeftInMillis); // Start the timer with updated time
                    }
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    return true;
                default:
                    return false;
            }
        });

        tutorialCardview = findViewById(R.id.forest_tutorial);
        tutorialCardview.setVisibility(View.GONE);

        tutorialFilipinoCardview = findViewById(R.id.forest_Filipinotutorial);
        tutorialFilipinoCardview.setVisibility(View.GONE);

        rewardCardview = findViewById(R.id.earnReward_cardviewForest);
        rewardCardview.setVisibility(View.GONE);

        rewardFilipinoCardview = findViewById(R.id.earnFilipinoReward_cardviewForest);
        rewardFilipinoCardview.setVisibility(View.GONE);

//        Profile myProfile = profileDao.getProfile(1);
//        if(myProfile.getTutorialDoneLocation3() == 0){
//            pauseTimer();
//            setupTutorial();
//        }else{
//            startCountDownTimer(TIMER_DURATION);
//        }

        showBackInGameCardView();
        setNoInGameCardView();
        setYesInGameCardView();
        setupWindowInsets();

        setupPlayAgainButton();
        setupNextLevelButton();
        setupBackButton();

        settings = settingsDao.getSettings(1);

        Profile myProfile = profileDao.getProfile(1);
        if(myProfile.getTutorialDoneLocation3() == 0){
            pauseTimer();
            //Kung sakali heto yung translate sa language sa mga cardview

            //setupTutorial();
            if (Settings.LANGUAGE_ENGLISH.equalsIgnoreCase(settings.getLanguage())){
                setupTutorial();
            } else {
                setupFilipinoTutorial();
            }
        }else{
            startCountDownTimer(TIMER_DURATION);
        }

        toggleSound(settings.getSound() == 1 ? true : false);
        toggleMusic(settings.getMusic() == 1 ? true : false);
        playButtonMusic();
        Log.d("MainActivity", "Settings: " + settings.toString());

        //convert to chosen language
        translateText(settings.getLanguage());

//        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                showBackInGameCardView(); // Show the confirmation CardView instead of going back
//            }
//        });
    }

    private void translateText(String languageCode) {
        //Cardview Back
        TextView backCardview_text = findViewById(R.id.forestbackCardview_message);
        backCardview_text.setText(TranslatorUtil.translate(backCardview_text.getText().toString(), languageCode));

        Button noButton = findViewById(R.id.forestButton_no);
        noButton.setText(TranslatorUtil.translate(noButton.getText().toString(), languageCode));

        Button yesButton = findViewById(R.id.forestButton_yes);
        yesButton.setText(TranslatorUtil.translate(yesButton.getText().toString(), languageCode));

        //Cardview GameOver
        Button buttonTryAgain = findViewById(R.id.forestButton_try);
        buttonTryAgain.setText(TranslatorUtil.translate(buttonTryAgain.getText().toString(), languageCode));

        Button buttonNext = findViewById(R.id.forestButton_next);
        buttonNext.setText(TranslatorUtil.translate(buttonNext.getText().toString(), languageCode));

        Button buttonBack = findViewById(R.id.forestButon_back);
        buttonBack.setText(TranslatorUtil.translate(buttonBack.getText().toString(), languageCode));

        TextView textNextTutorial= findViewById(R.id.forest_next_tutorial);
        textNextTutorial.setText(TranslatorUtil.translate(textNextTutorial.getText().toString(), languageCode));

        TextView textTimesUp = findViewById(R.id.textview_times_out);
        textTimesUp.setText(TranslatorUtil.translate(textTimesUp.getText().toString(), languageCode));

        TextView textNextFilipinoTutorial= findViewById(R.id.next_Filipinotutorialforest);
        textNextFilipinoTutorial.setText(TranslatorUtil.translate(textNextFilipinoTutorial.getText().toString(), languageCode));

        TextView textNextEarnReward= findViewById(R.id.next_earnRewardForest);
        textNextEarnReward.setText(TranslatorUtil.translate(textNextEarnReward.getText().toString(), languageCode));

        TextView textNextFilipinoEarnReward= findViewById(R.id.nextFilipino_earnRewardForest);
        textNextFilipinoEarnReward.setText(TranslatorUtil.translate(textNextFilipinoEarnReward.getText().toString(), languageCode));

    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed(); // Proceed with normal back action if CardView is not visible
//        cardView.setVisibility(View.VISIBLE);
//        //playButtonSound();
//        toggleMusic(false);
//        pauseTimer();
//        setDragDisable();// Hide the CardView if it's currently visible
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        cardView.setVisibility(View.VISIBLE);
//        //playButtonSound();
//        toggleMusic(false);
//        pauseTimer();
//        setDragDisable();
//
//        // Your other logic (e.g., saving game state) here
//        Log.d("Debug", "App is paused - might have been sent to the background");
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        cardView.setVisibility(View.VISIBLE);
//        //playButtonSound();
//        toggleMusic(false);
//        pauseTimer();
//        setDragDisable();
//
//        // Your other logic here
//        Log.d("Debug", "App is stopped - likely sent to the home screen");
//    }

    private void setupTutorial() {
        tutorialCardview = findViewById(R.id.forest_tutorial);
        tutorialImageView = findViewById(R.id.forest_tutorial1);
        TextView tutorialText = findViewById(R.id.forest_next_tutorial);

        if (selectedLevelInt == 1) { // Check if it's level one

            tutorialCardview.setVisibility(View.VISIBLE);
            tutorialImageView.setImageResource(tutorialImages[currentTutorialIndex]);

            // Set the onClickListener for the tutorialText
            tutorialText.setOnClickListener(v -> {
                currentTutorialIndex++;
                if (currentTutorialIndex < tutorialImages.length) {
                    tutorialImageView.setImageResource(tutorialImages[currentTutorialIndex]);
                } else {
                    tutorialCardview.setVisibility(View.GONE); // Hide the tutorial when the sequence is finished
                    startCountDownTimer(TIMER_DURATION);
                }
                playButtonSound();
            });
        }

        Profile myProfile = profileDao.getProfile(1);
        myProfile.setTutorialDoneLocation3(1);
        profileDao.updateProfile(myProfile);
    }

    private void setupFilipinoTutorial() {
        tutorialFilipinoCardview = findViewById(R.id.forest_Filipinotutorial);
        tutorialFilipinoImageView = findViewById(R.id.forest_Filipinotutorial1);
        TextView tutorialFilipinoText = findViewById(R.id.next_Filipinotutorialforest);

        if (selectedLevelInt == 1) { // Check if it's level one

            tutorialFilipinoCardview.setVisibility(View.VISIBLE);
            tutorialFilipinoImageView.setImageResource(tutorialFilipinoImages[currentFilipinoTutorialIndex]);

            // Set the onClickListener for the tutorialText
            tutorialFilipinoText.setOnClickListener(v -> {
                currentFilipinoTutorialIndex++;
                if (currentFilipinoTutorialIndex < tutorialFilipinoImages.length) {
                    tutorialFilipinoImageView.setImageResource(tutorialFilipinoImages[currentFilipinoTutorialIndex]);
                } else {
                    tutorialFilipinoCardview.setVisibility(View.GONE); // Hide the tutorial when the sequence is finished
                    startCountDownTimer(TIMER_DURATION);
                }
                playButtonSound();
            });
        }

        Profile myProfile = profileDao.getProfile(1);
        myProfile.setTutorialDoneLocation3(1);
        profileDao.updateProfile(myProfile);
    }

    private void earnRewardCardView() {
        rewardCardview = findViewById(R.id.earnReward_cardviewForest);
        rewardImageView = findViewById(R.id.earnReward_viewForest);
        TextView rewardText = findViewById(R.id.next_earnRewardForest);

        applauseSound();

        rewardCardview.setVisibility(View.VISIBLE);
        //rewardImageView.setImageResource(rewardImages[currentRewardIndex]);

        rewardText.setOnClickListener(v -> {
            currentRewardIndex++;
            if (currentRewardIndex < rewardImages.length) {
                rewardImageView.setImageResource(rewardImages[currentRewardIndex]);
            } else {
//                rewardCardview.setVisibility(View.GONE); // Hide the tutorial when the sequence is finished
                Intent intent = new Intent(Forest_Game_Activity.this, Forest_Location_Activity.class);
                startActivity(intent);
                finish();
            }
            playButtonSound();
        });
    }

    private void earnFilipinoRewardCardView() {
        rewardFilipinoCardview = findViewById(R.id.earnFilipinoReward_cardviewForest);
        rewardFilipinoImageView = findViewById(R.id.earnFilipinoReward_viewForest);
        TextView rewardFilipinoText = findViewById(R.id.nextFilipino_earnRewardForest);

        applauseSound();

        rewardFilipinoCardview.setVisibility(View.VISIBLE);
        //rewardImageView.setImageResource(rewardImages[currentRewardIndex]);

        rewardFilipinoText.setOnClickListener(v -> {
            currentFilipinoRewardIndex++;
            if (currentFilipinoRewardIndex < rewardFilipinoImages.length) {
                rewardFilipinoImageView.setImageResource(rewardFilipinoImages[currentFilipinoRewardIndex]);
            } else {
//                rewardCardview.setVisibility(View.GONE); // Hide the tutorial when the sequence is finished
                Intent intent = new Intent(Forest_Game_Activity.this, Forest_Location_Activity.class);
                startActivity(intent);
                finish();
            }
            playButtonSound();
        });

    }

    //kunin ung count ng category ng basura
    private int getGarbageCategoryCount(List<Garbage> garbageList, int garbageCategoryId) {
        return (int) garbageList.stream()
                .filter(g -> g.getGarbagecategoryId() == garbageCategoryId)
                .count();
    }


    private void changeBackground(int level, ConstraintLayout  layout) {
        // Change the background based on the selected level
        if (level == 3) {
            layout.setBackgroundResource(R.drawable.forest_shed_level); // Replace with your drawable
        } else if (level == 4) {
            layout.setBackgroundResource(R.drawable.forest_shed_level); // Replace with your drawable
        }else if (level == 5) {
            layout.setBackgroundResource(R.drawable.forest_camping_level); // Replace with your drawable
        }else if (level == 6) {
            layout.setBackgroundResource(R.drawable.forest_camping_level); // Replace with your drawable
        }
    }

    // Method to place trash cans based on the selected level
    private void placeTrashCans(int level) {
        biodegradable = findViewById(R.id.biodegradable);
        nonBiodegradable = findViewById(R.id.non_biodegradable);
        recyclable = findViewById(R.id.recyclable);

        ConstraintLayout.LayoutParams paramsBiodegradable = (ConstraintLayout.LayoutParams) biodegradable.getLayoutParams();
        ConstraintLayout.LayoutParams paramsNonBiodegradable = (ConstraintLayout.LayoutParams) nonBiodegradable.getLayoutParams();
        ConstraintLayout.LayoutParams paramsRecyclable = (ConstraintLayout.LayoutParams) recyclable.getLayoutParams();

        if (level == 1 || level == 2) {
            // Place trash cans for levels 1 and 2
            paramsBiodegradable.horizontalBias = 0.71f; // Example values, adjust as needed
            paramsBiodegradable.verticalBias = 0.805f;

            paramsNonBiodegradable.horizontalBias = 0.596f;
            paramsNonBiodegradable.verticalBias = 0.805f;

            paramsRecyclable.horizontalBias = 0.480f;
            paramsRecyclable.verticalBias = 0.805f;

        } else if (level == 3 || level == 4) {
            // Place trash cans for levels 3 and 4
            paramsBiodegradable.horizontalBias = 0.236f;
            paramsBiodegradable.verticalBias = 0.44f;

            paramsNonBiodegradable.horizontalBias = 0.121f;
            paramsNonBiodegradable.verticalBias = 0.44f;

            paramsRecyclable.horizontalBias = 0.006f;
            paramsRecyclable.verticalBias = 0.44f;

        } else if (level == 5 || level == 6) {
            // Place trash cans for levels 5 and 6
            paramsBiodegradable.horizontalBias = 0.236f;
            paramsBiodegradable.verticalBias = 0.999f;

            paramsNonBiodegradable.horizontalBias = 0.121f;
            paramsNonBiodegradable.verticalBias = 0.999f;

            paramsRecyclable.horizontalBias = 0.005f;
            paramsRecyclable.verticalBias = 0.999f;
        }

        // Apply the updated layout parameters
        biodegradable.setLayoutParams(paramsBiodegradable);
        nonBiodegradable.setLayoutParams(paramsNonBiodegradable);
        recyclable.setLayoutParams(paramsRecyclable);
    }

//    private void applyBlurEffect() {
//        Blurry.with(this)
//                .radius(10) // Adjust the blur radius
//                .sampling(2)
//                .onto((ViewGroup) findViewById(R.id.cardviewTransparent));
//    }

    private void setPosition(ImageView imageView, int xPercent, int yPercent) {
        int x = (int) (xPercent / 100.0 * container.getWidth());
        int y = (int) (yPercent / 100.0 * container.getHeight());
        imageView.setX(x);
        imageView.setY(y);
    }
    //selectedGarbage = imageView;
    // Method to set the ImageView draggable or not
    private void setDraggable(ImageView imageView, Garbage g, boolean isDragEnabled) {
        if (isDragEnabled) {
            imageView.setOnTouchListener((v, event) -> {
                selectedGarbage = imageView;
                selectedGarbageCategoryId = g.getGarbagecategoryId();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                    v.startDragAndDrop(null, shadowBuilder, v, 0);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.performClick();
                    return true;
                }
                return false;
            });
        } else {
            if (imageView != null){
                imageView.setOnTouchListener((v, event) -> {return false;});  // Disable dragging
            }
        }
    }

    // Method to enable or disable dragging globally
    private void setDragEnabled() {
            // Reapply the drag settings to all garbage ImageViews
            List<Garbage> garbageList = garbageDao.getGarbageByLocationIdAndLevelId(Location.FOREST, selectedLevelInt);
            int ctr = 0;
            for (Garbage g : garbageList) {
                ctr++;
                Integer imageViewId = imageIdToNameMap.get(ctr);
                ImageView imageView = findViewById(imageViewId);
                if (imageView != null){
                    setDraggable(imageView, g, true);  // Reapply the drag setting
                }
            }
    }

    // Method to enable or disable dragging globally
    private void setDragDisable() {
        // Reapply the drag settings to all garbage ImageViews
        List<Garbage> garbageList = garbageDao.getGarbageByLocationIdAndLevelId(Location.FOREST, selectedLevelInt);
        int ctr = 0;
        for (Garbage g : garbageList) {
            ctr++;
            Integer imageViewId = imageIdToNameMap.get(ctr);
            ImageView imageView = findViewById(imageViewId);
            if (imageView != null) {
                setDraggable(imageView, g, false);  // Reapply the drag setting
            }
        }
    }

    private void showBackInGameCardView() {
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            playButtonSound();
            toggleMusic(false);
            cardView.setVisibility(View.VISIBLE);
            pauseTimer();
            setDragDisable();
        });
    }

    private void setNoInGameCardView() {
        Button buttonCvNo = findViewById(R.id.forestButton_no);
        if (buttonCvNo != null) {
            buttonCvNo.setOnClickListener(v -> {
                playButtonSound();
                toggleMusic(true);
                cardView.setVisibility(View.GONE); // Hide the card view

                // Resume the timer
                startCountDownTimer(timeLeftInMillis); // Restart the timer with the remaining time

                setDragEnabled();
//                enableDragAndDrop();
            });
        }
    }

    private void setYesInGameCardView() {
        Button buttonCvYes = findViewById(R.id.forestButton_yes);
        if (buttonCvYes != null) {
            buttonCvYes.setOnClickListener(v -> {
                playButtonSound();
                toggleMusic(false);
                Intent intent = new Intent(Forest_Game_Activity.this, Forest_Location_Activity.class);
                startActivity(intent);
                finish();
            });
        }
    }

    private void setupWindowInsets() {
        View mainView = findViewById(R.id.activity_forest_game);
        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void startCountDownTimer(long durationInMillis) {
        countDownTimer = new CountDownTimer(durationInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                int minutes = (int) (timeLeftInMillis / 1000) / 60;
                int seconds = (int) (timeLeftInMillis / 1000) % 60;
                String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
                timeEditText.setText(timeLeftFormatted);
            }

            public void onFinish() {
                timeLeftInMillis = 0;
                timeEditText.setText("00:00");
                showEndGameMessage(numberOfGarbageInLevel, selectedLevelInt);
            }
        }.start();
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void showEndGameMessage(int numberOfGarbageInLevel, int selectedLevel) {
        pauseTimer();
        setDragDisable();
        disableBackButton();
        toggleMusic(false);

        int starRating = calculateStarRating(disposedCount, numberOfGarbageInLevel);
        Log.d("EndGame", "Calculated star rating: " + starRating);
        ImageView starImageView = findViewById(R.id.stars_got);
        if (starImageView != null) {
            int drawableResId = getStarDrawable(starRating);
            Log.d("EndGame", "Drawable resource ID: " + drawableResId);
            starImageView.setImageResource(drawableResId);
            Log.d("EndGame", "Set drawable resource on ImageView");
        }

        // Disable "Next" button if zero stars
        if (starRating == 0) {
            if (victoryMessage != null) {
                victoryMessage.setVisibility(View.GONE);
            }
            if (failedMessage != null) {
                failedMessage.setVisibility(View.VISIBLE);
            }

            Button nextButton = setupNextLevelButton(); // Assuming setupNextLevelButton returns the button
            if (nextButton != null) {
                nextButton.setVisibility(View.GONE);
                nextButton.setEnabled(false); // Disable the "Next" button
            }
        } else {
            if (victoryMessage != null) {
                victoryMessage.setVisibility(View.VISIBLE); // Show victoryMessage
                // Change the image based on language settings
                if (Settings.LANGUAGE_ENGLISH.equalsIgnoreCase(settings.getLanguage())) {
                    victoryMessage.setImageResource(R.drawable.completed); // English image
                } else {
                    victoryMessage.setImageResource(R.drawable.filipino_complete); // Tagalog image
                }
            }
            if (failedMessage != null) {
                failedMessage.setVisibility(View.GONE);
            }
            Button nextButton = setupNextLevelButton();
            if (nextButton != null) {
                nextButton.setEnabled(true); // Ensure the "Next" button is enabled
            }
        }

        // Play sound after the result is displayed (delay for sound)
        if (starRating > 0) {
            // Delay applause sound for 500ms
            new Handler().postDelayed(() -> {
                applauseSound(); // Play applause sound
            }, 500); // 500ms delay for applauseSound
        } else {
            // No delay for gameOverSound
            gameOverSound(); // Play failure sound immediately
        }

//        if (timeLeftInMillis <= 0) {
//            cardViewTimesOut.setVisibility(View.VISIBLE);
//            new Handler().postDelayed(() -> {
//                cardViewTimesOut.setVisibility(View.GONE);
//                cardViewEndGame.setVisibility(View.VISIBLE);
//            }, 5000);
//        } else {
//            new Handler().postDelayed(() -> cardViewEndGame.setVisibility(View.VISIBLE), 500);
//        }

        StarRating sr =  starRatingDao.getStarRatingByProfileIdLocationIdAndLevelId(1,Location.FOREST, selectedLevel);
        if(sr == null){
            double julianDayNumber = DateUtil.toJulianDayNumber(LocalDateTime.now());
            sr = new StarRating();
            sr.setProfileId(Profile.DEFAULT_PROFILE);
            sr.setLevelId(selectedLevel);
            sr.setStars(starRating);
            sr.setDateCreated(julianDayNumber);
            sr.setLocationId(Location.FOREST);
            starRatingDao.insertStarRating(sr);
        }else {
            int currentStar = sr.getStars();
            if(starRating > currentStar) {
                sr.setStars(starRating);
                starRatingDao.updateStarRating(sr);
            }
        }

        int starsCountInForestLocation = starRatingDao.getTotalStarsByProfileIdAndLocationId(1,Location.FOREST);
        if (timeLeftInMillis <= 0) {
            if(starsCountInForestLocation == StarRating.TOTAL_STARS_FOREST){
                //show na may na achieve sya
                Profile profile = profileDao.getProfile(1);
                int isCompleted = profile.getIsLocationCompletedForest();
                if(isCompleted == 0){
                    profile.setIsLocationCompletedForest(1);
                    profileDao.updateProfile(profile);
                    //show na may na achieve sya

                    if (Settings.LANGUAGE_ENGLISH.equalsIgnoreCase(settings.getLanguage())){
                        earnRewardCardView();
                    } else {
                        earnFilipinoRewardCardView();
                    }
                }else{
                    cardViewEndGame.setVisibility(View.VISIBLE);
                }
            }else {
                cardViewTimesOut.setVisibility(View.VISIBLE);
                new Handler().postDelayed(() -> {
                    cardViewTimesOut.setVisibility(View.GONE);
                    cardViewEndGame.setVisibility(View.VISIBLE);
                }, 5000);
            }
        } else {
            if(starsCountInForestLocation == StarRating.TOTAL_STARS_FOREST){
                Profile profile = profileDao.getProfile(1);
                int isCompleted = profile.getIsLocationCompletedForest();
                if(isCompleted == 0){
                    profile.setIsLocationCompletedForest(1);
                    profileDao.updateProfile(profile);
                    //show na may na achieve sya

                    if (Settings.LANGUAGE_ENGLISH.equalsIgnoreCase(settings.getLanguage())){
                        earnRewardCardView();
                    } else {
                        earnFilipinoRewardCardView();
                    }
                }else{
                    cardViewEndGame.setVisibility(View.VISIBLE);
                }
            }else {
                new Handler().postDelayed(() -> cardViewEndGame.setVisibility(View.VISIBLE), 500);
            }
        }

        //get the count of all stars in the current location, if 18 then display the cardview
//        int starsCountInForestLocation = starRatingDao.getTotalStarsByProfileIdAndLocationId(1,Location.FOREST);
//        if(starsCountInForestLocation == StarRating.TOTAL_STARS_FOREST){
//            //show na may na achieve sya
//
//        }
    }

    public static int getStarDrawable(int starRating) {
        switch (starRating) {
            case 3:
                return R.drawable.stars_three;
            case 2:
                return R.drawable.stars_two;
            case 1:
                return R.drawable.stars_one;
            default:
                return R.drawable.stars_zero;
        }
    }

    private int calculateStarRating(int disposedCount, int totalItems) {
        if (totalItems <= 0) {
            // Avoid division by zero and handle unexpected cases
            return 0;
        }

        double performanceFactor = ((double) disposedCount / totalItems) * 100;
        Log.d("StarRating", "disposedCount: " + disposedCount + ", totalItems: " + totalItems + ", performanceFactor: " + performanceFactor);

        // Use a small tolerance to account for floating-point precision issues
        final double TOLERANCE = 0.01;
        if (performanceFactor >= 99.99 - TOLERANCE) {
            return 3;
        } else if (performanceFactor >=50) {
            return 2;
        } else if (performanceFactor >= 1) {
            return 1;
        } else {
            return 0;
        }
    }

    // Ensure MyTouchListener is defined in your activity
    private class MyTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // Drag and drop logic
            return true;
        }
    }

//    private void disableDragAndDrop() {
//        int childCount = container.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View child = container.getChildAt(i);
//            if (child instanceof ImageView) {
//                child.setOnTouchListener(null);
//            }
//        }
//    }

    private void disableBackButton() {
        ImageButton backButton = findViewById(R.id.back_button);
        if (backButton != null) {
            backButton.setEnabled(false);
        }
    }

    private void setupPlayAgainButton() {
        Button playAgainButton = findViewById(R.id.forestButton_try);
        if (playAgainButton != null) {
            playAgainButton.setOnClickListener(v -> {
                stopSound();
                playButtonSound();
                // Create an intent to restart the activity
                Intent intent = new Intent(Forest_Game_Activity.this, Forest_Game_Activity.class);
                // Optionally, pass the current level or any other necessary data
                intent.putExtra("selectedLevel", selectedLevel); // Ensure selectedLevel is defined in your class
                startActivity(intent);
                finish(); // Finish the current activity
            });
        }
    }

    private Button setupNextLevelButton() {
        Button nextButton = findViewById(R.id.forestButton_next); // Initialize the button
        if (nextButton != null) {
            nextButton.setOnClickListener(v -> {
                // Convert selectedLevel to an integer
                int currentLevel;
                try {
                    currentLevel = Integer.parseInt(selectedLevel); // Ensure selectedLevel is a valid string
                } catch (NumberFormatException e) {
                    //Toast.makeText(this, "Error: Invalid level format!", Toast.LENGTH_SHORT).show();
                    return; // Exit if parsing fails
                }

                int nextLevel = currentLevel + 1; // Increment to get the next level

                // Create an Intent for the next level
                Intent intent;
                if (nextLevel <= 6) { // Assuming you have 6 levels
                    intent = new Intent(Forest_Game_Activity.this, Forest_Game_Activity.class);
                    intent.putExtra("selectedLevel", String.valueOf(nextLevel));
                    startActivity(intent);
                    finish(); // Finish the current activity
                } else {
                    // Handle the case where there are no more levels
//                    Toast.makeText(this, "No more levels available!", Toast.LENGTH_SHORT).show();
                    nextButton.setVisibility(View.GONE);
                    nextButton.setEnabled(false); // Disable the button
                }
                playButtonSound();
                stopSound();
            });

            int currentLevel;
            try {
                currentLevel = Integer.parseInt(selectedLevel); // Ensure selectedLevel is a valid string
                if (currentLevel == 6) {
                    nextButton.setVisibility(View.GONE);
                    nextButton.setEnabled(false); // Disable the button
                }
            } catch (NumberFormatException e) {
                //Toast.makeText(this, "Error: Invalid level format!", Toast.LENGTH_SHORT).show();
            }
        }
        return nextButton; // Return the button instance
    }

    private void setupBackButton() {
        Button backButton = findViewById(R.id.forestButon_back);
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                stopSound();
                playButtonSound();
                Intent intent = new Intent(Forest_Game_Activity.this, Forest_Location_Activity.class);
                startActivity(intent);
                finish();
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    //sound
    private void toggleSound(boolean enable) {
        if (enable) {
            isSoundOn = true;
            if (mysound != null) {
                mysound.setVolume(1.0f, 1.0f); // Enable sound
            }

            if (drop_trash_sound != null) {
                drop_trash_sound.setVolume(1.0f, 1.0f); // Enable sound
            }
            if(applause_CompletedStar != null){
                applause_CompletedStar.setVolume(0.6f, 0.6f); // Enable sound
            }

            if(gameOverSound != null){
                gameOverSound.setVolume(0.7f, 0.7f); // Enable sound
            }
            // Unmute system sounds (optional, if you want to control global sounds)
            AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        } else {
            isSoundOn = false;
            if (mysound != null) {
                mysound.setVolume(0f, 0f); // Disable sound
            }
            if (drop_trash_sound != null) {
                drop_trash_sound.setVolume(0f, 0f); // Disable sound
            }
            if (applause_CompletedStar != null) {
                applause_CompletedStar.setVolume(0f, 0f); // Disable sound
            }

            if (gameOverSound != null) {
                gameOverSound.setVolume(0f, 0f); // Disable sound
            }
            // Mute system sounds (optional, if you want to control global sounds)
            AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        }
        settings.setSound(isSoundOn ? 1 : 0);
    }

    public void playButtonSound() {
        if (isSoundOn && mysound != null) {
            mysound.start();
        }
    }

    //Music
    private void toggleMusic(boolean enable) {
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
        settings.setSound(isMusicOn ? 1 : 0);
    }

    public void playButtonMusic() {
        if (isMusicOn && mymusic != null) {
            mymusic.start();
        }
    }

    public void dropTrashSound() {
        if (isDropTrashSoundOn && drop_trash_sound != null) {
            drop_trash_sound.start();
        }
    }

    public void wrongAnswerSound() {
        if (isWrongAnswer && wrong_answer_sound != null) {
            wrong_answer_sound.start();
        }
    }

    public void applauseSound() {
        if (isApplauseSoundOn && applause_CompletedStar != null) {
            applause_CompletedStar.start();
        }
    }

    public void gameOverSound() {
        if (isGameOverSoundOn && gameOverSound != null) {
            gameOverSound.start();
        }
    }

    private void stopSound() {
        if (applause_CompletedStar != null) {
            if (applause_CompletedStar.isPlaying()) {
                applause_CompletedStar.stop();
            }
            applause_CompletedStar.release();
            applause_CompletedStar = null;
        }

        // Do the same for failure sound if you have one
        if (gameOverSound != null) {
            if (gameOverSound.isPlaying()) {
                gameOverSound.stop();
            }
            gameOverSound.release();
            gameOverSound = null;
        }
    }
}