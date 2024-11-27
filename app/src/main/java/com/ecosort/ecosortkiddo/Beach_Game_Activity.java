package com.ecosort.ecosortkiddo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;

import com.ecosort.ecosortkiddo.Beach_Location_Activity;
import com.ecosort.ecosortkiddo.R;
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

public class Beach_Game_Activity extends AppCompatActivity {

    private static final int NUM_GARBAGE_IMAGE_VIEWS = 25;
    public int numberOfGarbageInLevel;
    private static final long TIMER_DURATION = 180000; // 3 minutes in milliseconds
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
    private SettingsDao settingsDao;
    private Settings settings;
    private String selectedLevel = "";
    private static Map<Integer, Integer> imageIdToNameMap =  new HashMap<>();
    int selectedLevelInt = 0;
    private boolean hasShownTutorial = false;
    private int correctAnswerId = R.id.recyclable; // Set the correct answer ID here
    private ImageView selectedGarbage = null;
    private CardView cardViewQuestionnaire;
    private ImageView trashBin;
    private ImageView recyclable, biodegradable, nonBiodegradable;
    private ImageView recyclableGame3, biodegradableGame3, nonBiodegradableGame3;
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
    private int currentStoryIndex = 0;
    private int[] tutorialImages = {
            R.drawable.beach_story1,
            R.drawable.beach_story2,
            R.drawable.beach_story3,
            R.drawable.beach_story4,
            R.drawable.beach_story5,
            R.drawable.beach_story6
    };
    private ImageView tutorialImageView;
    private CardView tutorialCardview;

    //Tutorial Filipino
    private int currentFilipinoStoryIndex = 0;
    private int[] tutorialFilipinoImages = {
            R.drawable.beach_filipino_story1,
            R.drawable.beach_filipino_story2,
            R.drawable.beach_filipino_story3,
            R.drawable.beach_filipino_story4,
            R.drawable.beach_filipino_story5,
            R.drawable.beach_filipino_story6
    };
    private ImageView tutorialFilipinoImageView;
    private CardView tutorialFilipinoCardview;

    //Earn Reward
    private int currentRewardIndex = 0;
    private int[] rewardImages = {
            R.drawable.beach_earn1,
            R.drawable.beach_earn2,
            R.drawable.beach_earn3,
            R.drawable.beach_story7,
            R.drawable.beach_story8,
            R.drawable.beach_story9,
            R.drawable.beach_story10,
            R.drawable.beach_story11,
    };
    private ImageView rewardImageView;
    private CardView rewardCardview;

    //Earn Reward Filipino
    private int currentFilipinoRewardIndex = 0;
    private int[] rewardFilipinoImages = {
            R.drawable.beach_filipino_earn1,
            R.drawable.beach_filipino_earn2,
            R.drawable.beach_filipino_earn3,
            R.drawable.beach_filipino_story7,
            R.drawable.beach_filipino_story8,
            R.drawable.beach_filipino_story9,
            R.drawable.beach_filipino_story10,
            R.drawable.beach_filipino_story11
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
        setContentView(R.layout.activity_beach_game);

        ConstraintLayout   layout = findViewById(R.id.activity_beach_game); // Replace with your layout ID
        selectedLevelInt = Integer.valueOf(selectedLevel);
        Log.d("DEBUG", "Selected Level: " + selectedLevelInt);
        if(selectedLevelInt == 1 || selectedLevelInt == 2){
            setupForLocation1Game();
            //startCountDownTimer(TIMER_DURATION);
        }
        if(selectedLevelInt == 3 || selectedLevelInt == 4){
            changeBackground(selectedLevelInt,layout);
            setupForLocation2Game();
            //startCountDownTimer(TIMER_DURATION);
        }
        if(selectedLevelInt == 5 || selectedLevelInt == 6){
            changeBackground(selectedLevelInt,layout);
            //startCountDownTimer(TIMER_DURATION);
            setupForLocation3Game();
        }

        container = findViewById(R.id.activity_beach_game);
        cardViewEndGame = findViewById(R.id.cardview_endgame);
        cardViewEndGame.setVisibility(View.GONE);
        cardView = findViewById(R.id.cardView_ingame);
        cardView.setVisibility(View.GONE);
        cardViewTimesOut = findViewById(R.id.cardview_times_out);
        textViewTimesOut = findViewById(R.id.textview_times_out);
        timeEditText = findViewById(R.id.time);
        timeEditText.setKeyListener(null);
        timeEditText.setVisibility(View.VISIBLE);

        Button nextButton = findViewById(R.id.beachButton_next);

        System.out.println("Selected Level: " + selectedLevel);
        TextView text = findViewById(R.id.labelLevel);
        text.setText("Level " + selectedLevel);

//        startCountDownTimer(TIMER_DURATION);


        tutorialCardview = findViewById(R.id.beach_story);
        tutorialCardview.setVisibility(View.GONE);

        tutorialFilipinoCardview = findViewById(R.id.beach_Filipinostory);
        tutorialFilipinoCardview.setVisibility(View.GONE);

        rewardCardview = findViewById(R.id.earnReward_cardviewBeach);
        rewardCardview.setVisibility(View.GONE);

        rewardFilipinoCardview = findViewById(R.id.earnFilipinoReward_cardviewBeach);
        rewardFilipinoCardview.setVisibility(View.GONE);

        //placeTrashCans(selectedLevelInt);
        showBackInGameCardView();
        setNoInGameCardView();
        setYesInGameCardView();
        setupWindowInsets();

        setupPlayAgainButton();
        setupNextLevelButton();
        setupBackButton();
        cardViewTransparent();

        settings = settingsDao.getSettings(1);

        Profile myProfile = profileDao.getProfile(1);
        if(myProfile.getTutorialDoneLocation4() == 0){
            pauseTimer();
            //Kung sakali heto yung translate sa language sa mga cardview

            if (Settings.LANGUAGE_ENGLISH.equalsIgnoreCase(settings.getLanguage())){
                setupStory();
            } else {
                setupFilipinoStory();
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
//                cardView.setVisibility(View.GONE);// Show the confirmation CardView instead of going back
//                //showBackInGameCardView();
//            }
//        });
    }

    private void translateText(String languageCode) {
        //Cardview Back
        TextView backCardview_text = findViewById(R.id.beachBackCardview_message);
        backCardview_text.setText(TranslatorUtil.translate(backCardview_text.getText().toString(), languageCode));

        Button noButton = findViewById(R.id.beachButton_no);
        noButton.setText(TranslatorUtil.translate(noButton.getText().toString(), languageCode));

        Button yesButton = findViewById(R.id.beachButton_yes);
        yesButton.setText(TranslatorUtil.translate(yesButton.getText().toString(), languageCode));

        //Cardview GameOver
        Button buttonTryAgain = findViewById(R.id.beachButton_try);
        buttonTryAgain.setText(TranslatorUtil.translate(buttonTryAgain.getText().toString(), languageCode));

        Button buttonNext = findViewById(R.id.beachButton_next);
        buttonNext.setText(TranslatorUtil.translate(buttonNext.getText().toString(), languageCode));

        Button buttonBack = findViewById(R.id.beachButton_back);
        buttonBack.setText(TranslatorUtil.translate(buttonBack.getText().toString(), languageCode));

        //Heto yung Sa Cardview Question
        TextView textQuestion = findViewById(R.id.questionText);
        textQuestion.setText(TranslatorUtil.translate(textQuestion.getText().toString(), languageCode));

        TextView textNextTutorial= findViewById(R.id.next_storyBeach);
        textNextTutorial.setText(TranslatorUtil.translate(textNextTutorial.getText().toString(), languageCode));

        //Heto yung sa time
        TextView textTimesUp = findViewById(R.id.textview_times_out);
        textTimesUp.setText(TranslatorUtil.translate(textTimesUp.getText().toString(), languageCode));


        //Garbages
        ImageView usedCanImage = findViewById(R.id.garbage1);
        String usedCanDescription = TranslatorUtil.translate(getString(R.string.usedCan_text), languageCode);
        usedCanImage.setContentDescription(usedCanDescription);

        ImageView pizzaImage = findViewById(R.id.garbage2);
        String pizzaDescription = TranslatorUtil.translate(getString(R.string.pizza_text), languageCode);
        pizzaImage.setContentDescription(pizzaDescription);

        ImageView treeBranchImage = findViewById(R.id.garbage3);
        String treeBracnhDescription = TranslatorUtil.translate(getString(R.string.treeBranch_text), languageCode);
        treeBranchImage.setContentDescription(treeBracnhDescription);

        ImageView boneImage = findViewById(R.id.garbage4);
        String boneDescription = TranslatorUtil.translate(getString(R.string.bone_text), languageCode);
        boneImage.setContentDescription(boneDescription);

        ImageView candyWrapperImage = findViewById(R.id.garbage5);
        String candyWrapperDescription = TranslatorUtil.translate(getString(R.string.candyWrapper_text), languageCode);
        candyWrapperImage.setContentDescription(candyWrapperDescription);

        ImageView rottenMeatImage = findViewById(R.id.garbage6);
        String rottenMeatDescription = TranslatorUtil.translate(getString(R.string.rottenMeat_text), languageCode);
        rottenMeatImage.setContentDescription(rottenMeatDescription);

        ImageView plasticCupImage = findViewById(R.id.garbage7);
        String plasticCupDescription = TranslatorUtil.translate(getString(R.string.platicCup_text), languageCode);
        plasticCupImage.setContentDescription(plasticCupDescription);

        ImageView garbageBagImage = findViewById(R.id.garbage8);
        String garbageBagDescription = TranslatorUtil.translate(getString(R.string.garbageBag_text), languageCode);
        garbageBagImage.setContentDescription(garbageBagDescription);

        ImageView paperRollImage = findViewById(R.id.garbage9);
        String paperRollDescription = TranslatorUtil.translate(getString(R.string.paperRoll_text), languageCode);
        paperRollImage.setContentDescription(paperRollDescription);

        ImageView bittenBurgerImage = findViewById(R.id.garbage10);
        String bittenBurgerDescription = TranslatorUtil.translate(getString(R.string.bittenBurger_text), languageCode);
        bittenBurgerImage.setContentDescription(bittenBurgerDescription);

        ImageView ketchupBottleImage = findViewById(R.id.garbage11);
        String ketchupBottleDescription = TranslatorUtil.translate(getString(R.string.ketchupBottle_text), languageCode);
        ketchupBottleImage.setContentDescription(ketchupBottleDescription);

        ImageView eggShellImage = findViewById(R.id.garbage12);
        String eggShellDescription = TranslatorUtil.translate(getString(R.string.eggShell_text), languageCode);
        eggShellImage.setContentDescription(eggShellDescription);

        ImageView usedCan2Image = findViewById(R.id.garbage13);
        String usedCan2Description = TranslatorUtil.translate(getString(R.string.usedCan2_text), languageCode);
        usedCan2Image.setContentDescription(usedCan2Description);

        ImageView brokenClassImage = findViewById(R.id.garbage14);
        String brokenClassDescription = TranslatorUtil.translate(getString(R.string.brokenClass_text), languageCode);
        brokenClassImage.setContentDescription(brokenClassDescription);

        ImageView lightBulbImage = findViewById(R.id.garbage15);
        String lightBulbDescription = TranslatorUtil.translate(getString(R.string.lightBulb_text), languageCode);
        lightBulbImage.setContentDescription(lightBulbDescription);

        ImageView bananaImage = findViewById(R.id.garbage16);
        String bananaDescription = TranslatorUtil.translate(getString(R.string.banana_text), languageCode);
        bananaImage.setContentDescription(bananaDescription);

        ImageView chipsBagImage = findViewById(R.id.garbage17);
        String chipsBagDescription = TranslatorUtil.translate(getString(R.string.chipsBag_text), languageCode);
        chipsBagImage.setContentDescription(chipsBagDescription);

        ImageView paperImage = findViewById(R.id.garbage18);
        String paperDescription = TranslatorUtil.translate(getString(R.string.paper_text), languageCode);
        paperImage.setContentDescription(paperDescription);

        ImageView fruitsFeelsImage = findViewById(R.id.garbage19);
        String fruitFeelsDescription = TranslatorUtil.translate(getString(R.string.fruitFeels_text), languageCode);
        fruitsFeelsImage.setContentDescription(fruitFeelsDescription);

        ImageView teaBagImage = findViewById(R.id.garbage20);
        String teaBagDescription = TranslatorUtil.translate(getString(R.string.teaBag_text), languageCode);
        teaBagImage.setContentDescription(teaBagDescription);

        ImageView paper2Image = findViewById(R.id.garbage21);
        String paper2Description = TranslatorUtil.translate(getString(R.string.paper2_text), languageCode);
        paper2Image.setContentDescription(paper2Description);

        ImageView bittenPizzaImage = findViewById(R.id.garbage22);
        String bittenPizzaDescription = TranslatorUtil.translate(getString(R.string.bittenPizza_text), languageCode);
        bittenPizzaImage.setContentDescription(bittenPizzaDescription);

        ImageView moldyBreadImage = findViewById(R.id.garbage23);
        String moldyBreadDescription = TranslatorUtil.translate(getString(R.string.moldyBread_text), languageCode);
        moldyBreadImage.setContentDescription(moldyBreadDescription);

        ImageView trashBagImage = findViewById(R.id.garbage24);
        String trashBagDescription = TranslatorUtil.translate(getString(R.string.trashBag_text), languageCode);
        trashBagImage.setContentDescription(trashBagDescription);

        ImageView brokenCarImage = findViewById(R.id.garbage25);
        String brokenCarDescription = TranslatorUtil.translate(getString(R.string.brokenCar_text), languageCode);
        brokenCarImage.setContentDescription(brokenCarDescription);

        TextView textNextFilipinoTutorial= findViewById(R.id.next_FilipinostoryBeach);
        textNextFilipinoTutorial.setText(TranslatorUtil.translate(textNextFilipinoTutorial.getText().toString(), languageCode));

        TextView textNextEarnReward= findViewById(R.id.next_earnRewardBeach);
        textNextEarnReward.setText(TranslatorUtil.translate(textNextEarnReward.getText().toString(), languageCode));

        TextView textNextFilipinoEarnReward= findViewById(R.id.nextFilipino_earnRewardBeach);
        textNextFilipinoEarnReward.setText(TranslatorUtil.translate(textNextFilipinoEarnReward.getText().toString(), languageCode));

    }

    private boolean checkAnswer(int selectedAnswerId) {
        boolean isAnswerCorrect=false;
        if (selectedAnswerId == correctAnswerId) {
            // Hide the card view if the answer is correct
            cardViewQuestionnaire = findViewById(R.id.cardView_questionnaire);
            cardViewQuestionnaire.setVisibility(View.GONE);
            dropTrashSound();
            //Toast.makeText(this, "Correct answer!", Toast.LENGTH_SHORT).show();
            isAnswerCorrect = true;
            disposedCount++;
            if(selectedGarbage != null){
                selectedGarbage.setVisibility(View.GONE);
                selectedGarbage = null;
            }
            if (disposedCount == numberOfGarbageInLevel) {
                showEndGameMessage(numberOfGarbageInLevel, selectedLevelInt);
            }
            enableBackButton();

        } else {
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
        return isAnswerCorrect;
    }

    private void enableBackButton() {
        ImageButton backButton = findViewById(R.id.back_button);
        if (backButton != null) {
            backButton.setEnabled(true);
        }
    }

    private void setupImageButtons() {
        ImageButton biodegradableButton = findViewById(R.id.biodegradable);
        ImageButton nonBiodegradableButton = findViewById(R.id.non_biodegradable);
        ImageButton recyclableButton = findViewById(R.id.recyclable);

        biodegradableButton.setOnClickListener(v -> checkAnswer(GarbageCategory.BIODEGRADABLE));
        nonBiodegradableButton.setOnClickListener(v -> checkAnswer(GarbageCategory.NON_BIODEGRADABLE));
        recyclableButton.setOnClickListener(v -> checkAnswer(GarbageCategory.RECYCLABLE));
    }

    public void setupForLocation2Game(){
        // Get garbage from the database
        List<Garbage> garbageList = garbageDao.getGarbageByLocationIdAndLevelId(Location.BEACH, Integer.valueOf(selectedLevel));
        numberOfGarbageInLevel = garbageList.size();
        int ctr = 0;

        if (numberOfGarbageInLevel > 0) {
            for (Garbage g : garbageList) {
                ctr++;
                Integer imageViewId = imageIdToNameMap.get(ctr);
                ImageView imageView = findViewById(imageViewId);
                if (imageView != null) {
                    // Set the horizontal and vertical bias
                    setBias(imageView, g.getLayoutConstraintHorizontalBias().floatValue(), g.getLayoutConstraintVerticalBias().floatValue());
                    // Make the ImageView clickable
                    setClickable(imageView, g);
                    imageView.setVisibility(View.VISIBLE); // Ensure the ImageView is visible
                }
            }
        }

        // Hide any remaining ImageViews
        for (int i = ctr + 1; i <= NUM_GARBAGE_IMAGE_VIEWS; i++) {
            Integer imageViewId = imageIdToNameMap.get(i);
            ImageView imageView = findViewById(imageViewId);
            if (imageView != null) {
                imageView.setVisibility(View.GONE);
            }
        }

        // Initialize victory and failed messages
        victoryMessage = findViewById(R.id.victory_message);
        failedMessage = findViewById(R.id.gameover_message);

        ImageView starImageView = findViewById(R.id.stars_got);

        setupImageButtons();

        //hide or show ng mga trash cans
//        findViewById(R.id.biodegradable).setVisibility(View.GONE);
//        findViewById(R.id.non_biodegradable).setVisibility(View.GONE);
//        findViewById(R.id.recyclable).setVisibility(View.GONE);
//        findViewById(R.id.bin).setVisibility(View.GONE);

    }

    public void setupForLocation3Game(){
        List<Garbage> garbageList = garbageDao.getGarbageByLocationIdAndLevelId(Location.BEACH, Integer.valueOf(selectedLevel));
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
                    setDraggableGame3(imageView, g, true);
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

        biodegradableGame3 = findViewById(R.id.biodegradable_game3);
        nonBiodegradableGame3 = findViewById(R.id.non_biodegradable_game3);
        recyclableGame3 = findViewById(R.id.recyclable_game3);

        recyclableGame3.setVisibility(View.GONE);
        nonBiodegradableGame3.setVisibility(View.GONE);
        biodegradableGame3.setVisibility(View.GONE);

        switch (selectedLevel) {
            case "5":
                biodegradableGame3.setVisibility(View.VISIBLE);
                nonBiodegradableGame3.setVisibility(View.VISIBLE);

                int biodegradableGarbageCountLevel4 = getGarbageCategoryCount(garbageList, GarbageCategory.BIODEGRADABLE);
                int nonBiodegradableGarbageCountLevel4 = getGarbageCategoryCount(garbageList, GarbageCategory.NON_BIODEGRADABLE);
                numberOfGarbageInLevel = nonBiodegradableGarbageCountLevel4 + biodegradableGarbageCountLevel4;
                break;
            case "6":
                recyclableGame3.setVisibility(View.VISIBLE);
                nonBiodegradableGame3.setVisibility(View.VISIBLE);
                biodegradableGame3.setVisibility(View.VISIBLE);

                int recyclableGarbageCountLevel5N6 = getGarbageCategoryCount(garbageList, GarbageCategory.RECYCLABLE);
                int nonBiodegradableGarbageCountLevel5N6 = getGarbageCategoryCount(garbageList, GarbageCategory.NON_BIODEGRADABLE);
                int biodegradableGarbageCountLevel5N6 = getGarbageCategoryCount(garbageList, GarbageCategory.BIODEGRADABLE);
                numberOfGarbageInLevel = recyclableGarbageCountLevel5N6 + nonBiodegradableGarbageCountLevel5N6 + biodegradableGarbageCountLevel5N6;
                break;
        }


        //biodegradable = 1
        biodegradableGame3.setOnDragListener((v, event) -> {
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

        //nonBiodegradable = 2
        nonBiodegradableGame3.setOnDragListener((v, event) -> {
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

        //recyclable = 3
        recyclableGame3.setOnDragListener((v, event) -> {
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

        showBackInGameCardView();
        setNoInGameCardView();
        setYesInGameCardView();
        setupWindowInsets();

        setupPlayAgainButton();
        setupNextLevelButton();
        setupBackButton();
    }

    public void setupForLocation1Game(){
        // Get garbage from the database
        List<Garbage> garbageList = garbageDao.getGarbageByLocationIdAndLevelId(Location.BEACH, Integer.valueOf(selectedLevel));
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
                    setDraggable(imageView, true);
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

        trashBin = findViewById(R.id.bin);
        trashBin.setOnDragListener((v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    return true;
                case DragEvent.ACTION_DROP:
                    View draggedView = (View) event.getLocalState();
                    container.removeView(draggedView);
                    dropTrashSound();
//                    Toast.makeText(getApplicationContext(), "Trash disposed!", Toast.LENGTH_SHORT).show();
                    disposedCount++;
                    if (disposedCount == numberOfGarbageInLevel) {
                        showEndGameMessage(numberOfGarbageInLevel, selectedLevelInt);
                    }
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    return true;
                default:
                    return false;
            }
        });

        //hide or show ng mga trash cans
        findViewById(R.id.biodegradable).setVisibility(View.GONE);
        findViewById(R.id.non_biodegradable).setVisibility(View.GONE);
        findViewById(R.id.recyclable).setVisibility(View.GONE);
        findViewById(R.id.bin).setVisibility(View.VISIBLE);
    }

    private void changeBackground(int level, ConstraintLayout  layout) {
        // Change the background based on the selected level
        if (level == 3) {
            layout.setBackgroundResource(R.drawable.beach_game_level2); // Replace with your drawable
        } else if (level == 4) {
            layout.setBackgroundResource(R.drawable.beach_game_level2); // Replace with your drawable
        }else if (level == 5) {
            layout.setBackgroundResource(R.drawable.beach_game_level3); // Replace with your drawable
        }else if (level == 6) {
            layout.setBackgroundResource(R.drawable.beach_game_level3); // Replace with your drawable
        }
    }

//    private void applyBlurEffect() {
//        Blurry.with(this)
//                .radius(10) // Adjust the blur radius
//                .sampling(2)
//                .onto((ViewGroup) findViewById(R.id.cardviewTransparent));
//    }

    private void setDraggable(ImageView imageView, boolean isDragEnabled) {
        if (isDragEnabled) {
            imageView.setOnTouchListener((v, event) -> {
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
        List<Garbage> garbageList = garbageDao.getGarbageByLocationIdAndLevelId(Location.BEACH, selectedLevelInt);
        int ctr = 0;
        for (Garbage g : garbageList) {
            ctr++;
            Integer imageViewId = imageIdToNameMap.get(ctr);
            ImageView imageView = findViewById(imageViewId);
            if (imageView != null){
                if(g.getLevelId() != 3 && g.getLevelId() != 4) {
                    setDraggable(imageView, true);  // Reapply the drag setting
                }else{
                    System.out.print("Test");
                }
            }
        }
    }

    // Method to enable or disable dragging globally
    private void setDragDisable() {
        // Reapply the drag settings to all garbage ImageViews
        List<Garbage> garbageList = garbageDao.getGarbageByLocationIdAndLevelId(Location.BEACH, selectedLevelInt);
        int ctr = 0;
        for (Garbage g : garbageList) {
            ctr++;
            Integer imageViewId = imageIdToNameMap.get(ctr);
            ImageView imageView = findViewById(imageViewId);
            if (imageView != null) {
                setDraggable(imageView, false);  // Reapply the drag setting
            }
        }
    }

    private void showBackInGameCardView() {
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            playButtonSound();
            toggleMusic(false);
            findViewById(R.id.click_blocker).setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);
            pauseTimer();
            setDragDisable();
        });
    }

    private void setNoInGameCardView() {
        Button buttonCvNo = findViewById(R.id.beachButton_no);
        if (buttonCvNo != null) {
            buttonCvNo.setOnClickListener(v -> {
                playButtonSound();
                cardView.setVisibility(View.GONE);
                toggleMusic(true);
                findViewById(R.id.click_blocker).setVisibility(View.GONE);
                // Hide the card view

                // Resume the timer
                startCountDownTimer(timeLeftInMillis);
                setDragEnabled();// Restart the timer with the remaining time
            });
        }
    }

    private void setYesInGameCardView() {
        Button buttonCvYes = findViewById(R.id.beachButton_yes);
        if (buttonCvYes != null) {
            buttonCvYes.setOnClickListener(v -> {
                playButtonSound();
                toggleMusic(false);
                Intent intent = new Intent(Beach_Game_Activity.this, Beach_Location_Activity.class);
                startActivity(intent);
                finish();
            });
        }
    }

    private void setupWindowInsets() {
        View mainView = findViewById(R.id.activity_beach_game);
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
                findViewById(R.id.click_blocker).setVisibility(View.VISIBLE);
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

        StarRating sr =  starRatingDao.getStarRatingByProfileIdLocationIdAndLevelId(1,Location.BEACH, selectedLevel);
        if(sr == null){
            double julianDayNumber = DateUtil.toJulianDayNumber(LocalDateTime.now());
            sr = new StarRating();
            sr.setProfileId(Profile.DEFAULT_PROFILE);
            sr.setLevelId(selectedLevel);
            sr.setStars(starRating);
            sr.setDateCreated(julianDayNumber);
            sr.setLocationId(Location.BEACH);
            starRatingDao.insertStarRating(sr);
        }else {
            int currentStar = sr.getStars();
            if(starRating > currentStar) {
                sr.setStars(starRating);
                starRatingDao.updateStarRating(sr);
            }
        }

        int starsCountInBeachLocation = starRatingDao.getTotalStarsByProfileIdAndLocationId(1,Location.BEACH);
        if (timeLeftInMillis <= 0) {
            if(starsCountInBeachLocation == StarRating.TOTAL_STARS_BEACH){
                //show na may na achieve sya
                Profile profile = profileDao.getProfile(1);
                int isCompleted = profile.getIsLocationCompletedBeach();
                if(isCompleted == 0){
                    profile.setIsLocationCompletedBeach(1);
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
            if(starsCountInBeachLocation == StarRating.TOTAL_STARS_BEACH){
                Profile profile = profileDao.getProfile(1);
                int isCompleted = profile.getIsLocationCompletedBeach();
                if(isCompleted == 0){
                    profile.setIsLocationCompletedBeach(1);
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
//        int starsCountInBeachLocation = starRatingDao.getTotalStarsByProfileIdAndLocationId(1,Location.BEACH);
//        if(starsCountInBeachLocation == StarRating.TOTAL_STARS_BEACH){
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

    private void disableBackButton() {
        ImageButton backButton = findViewById(R.id.back_button);
        if (backButton != null) {
            backButton.setEnabled(false);
        }
    }

    private void setupPlayAgainButton() {
        Button playAgainButton = findViewById(R.id.beachButton_try);
        if (playAgainButton != null) {
            playAgainButton.setOnClickListener(v -> {
                stopSound();
                playButtonSound();
                // Create an intent to restart the activity
                Intent intent = new Intent(Beach_Game_Activity.this, Beach_Game_Activity.class);
                // Optionally, pass the current level or any other necessary data
                intent.putExtra("selectedLevel", selectedLevel); // Ensure selectedLevel is defined in your class
                startActivity(intent);
                finish(); // Finish the current activity
            });
        }
    }

    private Button setupNextLevelButton() {
        Button nextButton = findViewById(R.id.beachButton_next); // Initialize the button
        if (nextButton != null) {
            nextButton.setOnClickListener(v -> {
                // Convert selectedLevel to an integer
                int currentLevel;
                try {
                    currentLevel = Integer.parseInt(selectedLevel); // Ensure selectedLevel is a valid string
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Error: Invalid level format!", Toast.LENGTH_SHORT).show();
                    return; // Exit if parsing fails
                }

                int nextLevel = currentLevel + 1; // Increment to get the next level

                // Create an Intent for the next level
                Intent intent;
                if (nextLevel <= 6) { // Assuming you have 6 levels
                    intent = new Intent(Beach_Game_Activity.this, Beach_Game_Activity.class);
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
                Toast.makeText(this, "Error: Invalid level format!", Toast.LENGTH_SHORT).show();
            }
        }
        return nextButton; // Return the button instance
    }

    private void setupBackButton() {
        Button backButton = findViewById(R.id.beachButton_back);
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                stopSound();
                playButtonSound();
                Intent intent = new Intent(Beach_Game_Activity.this, Beach_Location_Activity.class);
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

    private void setClickable(ImageView imageView, Garbage g) {
        cardViewQuestionnaire = findViewById(R.id.cardView_questionnaire);
        imageView.setOnClickListener(v -> {
            // Make cardView_questionnaire visible
            if (cardViewQuestionnaire != null) {
                cardViewQuestionnaire.setVisibility(View.VISIBLE);
                disableBackButton();

                // Set OnClickListener to the cardView_questionnaire to close it when clicked
                cardViewQuestionnaire.setOnClickListener(cardView -> {
                    cardViewQuestionnaire.setVisibility(View.GONE);
                    enableBackButton();
                });
            }

            ImageView questionImage = cardViewQuestionnaire.findViewById(R.id.questionImage);
            if (questionImage != null) {
                // Get the drawable resource ID of the clicked ImageView
                Drawable clickedDrawable = imageView.getDrawable();
                if (clickedDrawable != null) {
                    questionImage.setImageDrawable(clickedDrawable);
                }
            }

            TextView imageText = cardViewQuestionnaire.findViewById(R.id.imageText);
            if (imageText != null) {
                // Get the content  ription of the clicked ImageView
                String description = (String) imageView.getContentDescription();
                imageText.setText(description); // Update the TextView with the description
            }

            // Store the correct answer and selected garbage
            correctAnswerId = g.getGarbagecategoryId();
            selectedGarbage = imageView;
        });
    }

    //for game 5 and 6
    //kunin ung count ng category ng basura
    private int getGarbageCategoryCount(List<Garbage> garbageList, int garbageCategoryId) {
        return (int) garbageList.stream()
                .filter(g -> g.getGarbagecategoryId() == garbageCategoryId)
                .count();
    }

    private void setDraggableGame3(ImageView imageView, Garbage g, boolean isDragEnabled) {
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

    private void cardViewTransparent(){
        CardView cardViewQuestionnaire = findViewById(R.id.cardView_questionnaire);

        cardViewQuestionnaire.setBackgroundColor(Color.TRANSPARENT);
        cardViewQuestionnaire.setCardBackgroundColor(Color.TRANSPARENT);
        cardViewQuestionnaire.setCardElevation(0f);
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

    private void setupStory() {
        tutorialCardview = findViewById(R.id.beach_story);
        tutorialImageView = findViewById(R.id.beach_story1);
        TextView tutorialText = findViewById(R.id.next_storyBeach);

        if (selectedLevelInt == 1) { // Check if it's level one

            tutorialCardview.setVisibility(View.VISIBLE);
            tutorialImageView.setImageResource(tutorialImages[currentStoryIndex]);

            // Set the onClickListener for the tutorialText
            tutorialText.setOnClickListener(v -> {
                currentStoryIndex++;
                if (currentStoryIndex < tutorialImages.length) {
                    tutorialImageView.setImageResource(tutorialImages[currentStoryIndex]);
                } else {
                    tutorialCardview.setVisibility(View.GONE); // Hide the tutorial when the sequence is finished
                    startCountDownTimer(TIMER_DURATION);
                }
                playButtonSound();
            });
        }

        Profile myProfile = profileDao.getProfile(1);
        myProfile.setTutorialDoneLocation4(1);
        profileDao.updateProfile(myProfile);
    }

    private void setupFilipinoStory() {
        tutorialFilipinoCardview = findViewById(R.id.beach_Filipinostory);
        tutorialFilipinoImageView = findViewById(R.id.beach_Filipinostory1);
        TextView tutorialFilipinoText = findViewById(R.id.next_FilipinostoryBeach);

        if (selectedLevelInt == 1) { // Check if it's level one

            tutorialFilipinoCardview.setVisibility(View.VISIBLE);
            tutorialFilipinoImageView.setImageResource(tutorialFilipinoImages[currentFilipinoStoryIndex]);

            // Set the onClickListener for the tutorialText
            tutorialFilipinoText.setOnClickListener(v -> {
                currentFilipinoStoryIndex++;
                if (currentFilipinoStoryIndex < tutorialFilipinoImages.length) {
                    tutorialFilipinoImageView.setImageResource(tutorialFilipinoImages[currentFilipinoStoryIndex]);
                } else {
                    tutorialFilipinoCardview.setVisibility(View.GONE); // Hide the tutorial when the sequence is finished
                    startCountDownTimer(TIMER_DURATION);
                }
                playButtonSound();
            });
        }

        Profile myProfile = profileDao.getProfile(1);
        myProfile.setTutorialDoneLocation4(1);
        profileDao.updateProfile(myProfile);
    }

    private void earnRewardCardView() {
        rewardCardview = findViewById(R.id.earnReward_cardviewBeach);
        rewardImageView = findViewById(R.id.earnReward_viewBeach);
        TextView rewardText = findViewById(R.id.next_earnRewardBeach);

        applauseSound();

        rewardCardview.setVisibility(View.VISIBLE);
        //rewardImageView.setImageResource(rewardImages[currentRewardIndex]);

        rewardText.setOnClickListener(v -> {
            currentRewardIndex++;
            if (currentRewardIndex < rewardImages.length) {
                rewardImageView.setImageResource(rewardImages[currentRewardIndex]);
            } else {
//                rewardCardview.setVisibility(View.GONE); // Hide the tutorial when the sequence is finished
                Intent intent = new Intent(Beach_Game_Activity.this, Beach_Location_Activity.class);
                startActivity(intent);
                finish();
            }
            playButtonSound();
        });
    }

    private void earnFilipinoRewardCardView() {
        rewardFilipinoCardview = findViewById(R.id.earnFilipinoReward_cardviewBeach);
        rewardFilipinoImageView = findViewById(R.id.earnFilipinoReward_viewBeach);
        TextView rewardFilipinoText = findViewById(R.id.nextFilipino_earnRewardBeach);

        applauseSound();

        rewardFilipinoCardview.setVisibility(View.VISIBLE);
        //rewardImageView.setImageResource(rewardImages[currentRewardIndex]);

        rewardFilipinoText.setOnClickListener(v -> {
            currentFilipinoRewardIndex++;
            if (currentFilipinoRewardIndex < rewardFilipinoImages.length) {
                rewardFilipinoImageView.setImageResource(rewardFilipinoImages[currentFilipinoRewardIndex]);
            } else {
//                rewardCardview.setVisibility(View.GONE); // Hide the tutorial when the sequence is finished
                Intent intent = new Intent(Beach_Game_Activity.this, Beach_Location_Activity.class);
                startActivity(intent);
                finish();
            }
            playButtonSound();
        });
    }

    //The buttons on the phone itself
    @Override
    public void onBackPressed() {
        // Only show the cardView when the back button is pressed
        if (cardView.getVisibility() != View.VISIBLE) {
            findViewById(R.id.click_blocker).setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);
            toggleMusic(false);
            pauseTimer();
            setDragDisable();

        } else {
            // Proceed with normal back action if the cardView is already visible
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        // Conditional check for cardView visibility
        if (cardView.getVisibility() != View.VISIBLE) {
            // Use post() to ensure the UI update happens on the main thread
            cardView.post(() -> {
                findViewById(R.id.click_blocker).setVisibility(View.VISIBLE);
                cardView.setVisibility(View.VISIBLE);
                toggleMusic(false);
                pauseTimer();
                setDragDisable();
            });
        }
        super.onPause();
        cardView.post(() -> cardView.setVisibility(View.GONE));
    }

    @Override
    protected void onStop() {
        if (cardView.getVisibility() != View.VISIBLE) {
            // Use post() to ensure the UI update happens on the main thread
            cardView.post(() -> {
                findViewById(R.id.click_blocker).setVisibility(View.VISIBLE);
                cardView.setVisibility(View.VISIBLE);  // Make sure cardView becomes visible
                toggleMusic(false);                    // Pause music
                pauseTimer();                          // Pause the timer
                setDragDisable();                      // Disable drag functionality
            });
        }
        cardView.post(() -> cardView.setVisibility(View.VISIBLE));
        super.onStop();
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