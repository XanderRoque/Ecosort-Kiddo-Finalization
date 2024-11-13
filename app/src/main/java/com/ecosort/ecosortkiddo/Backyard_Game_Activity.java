package com.ecosort.ecosortkiddo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
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
//import jp.wasabeef.blurry.Blurry;

public class Backyard_Game_Activity extends AppCompatActivity {

    private static final int NUM_GARBAGE_IMAGE_VIEWS = 25;
    public int numberOfGarbageInLevel;
    private static final long TIMER_DURATION = 180000; // 3 minutes in milliseconds
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
    private int correctAnswerId = R.id.recyclable; // Set the correct answer ID here
    private ImageView selectedGarbage = null;
    private CardView cardViewQuestionnaire;
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
    //private View click_blocker;

    //Tutorial English
    private int currentTutorialIndex = 0;
    private int[] tutorialImages = {
            R.drawable.backyard_story1,
            R.drawable.backyard_story2,
            R.drawable.backyard_story3,
            R.drawable.backyard_story4,
            R.drawable.backyard_tutorial1,
            R.drawable.backyard_tutorial2,
            R.drawable.backyard_tutorial3
    };
    private ImageView tutorialImageView;
    private CardView tutorialCardview;

    //Tutorial Filipino
    private int currentFilipinoTutorialIndex = 0;
    private int[] tutorialFilipinoImages = {
            R.drawable.backyard_filipino_story1,
            R.drawable.backyard_filipino_story2,
            R.drawable.backyard_filipino_story3,
            R.drawable.backyard_filipino_story4,
            R.drawable.backyard_filipino_tutorial1,
            R.drawable.backyard_filipino_tutorial2,
            R.drawable.backyard_filipino_tutorial3
    };
    private ImageView tutorialFilipinoImageView;
    private CardView tutorialFilipinoCardview;

    //Earn Reward
    private int currentRewardIndex = 0;
    private int[] rewardImages = {
            R.drawable.backyard_earn1,
            R.drawable.backyard_earn2,
            R.drawable.backyard_earn3,
            R.drawable.backyard_story5,
            R.drawable.backyard_story6,
            R.drawable.backyard_story7,
            R.drawable.backyard_story8
    };
    private ImageView rewardImageView;
    private CardView rewardCardview;

    //Earn Reward Filipino
    private int currentFilipinoRewardIndex = 0;
    private int[] rewardFilipinoImages = {
            R.drawable.backyard_filipino_earn1,
            R.drawable.backyard_filipino_earn2,
            R.drawable.backyard_filipino_earn3,
            R.drawable.backyard_filipino_story5,
            R.drawable.backyard_filipino_story6,
            R.drawable.backyard_filipino_story7,
            R.drawable.backyard_filipino_story8
    };
    private ImageView rewardFilipinoImageView;
    private CardView rewardFilipinoCardview;

    static {
        imageIdToNameMap.put(1, R.id.garbage1);
        imageIdToNameMap.put(2, R.id.garbage2);
        imageIdToNameMap.put(3, R.id.garbage3);
        imageIdToNameMap.put(4, R.id.garbage4);
        imageIdToNameMap.put(5, R.id.garbage17);
        imageIdToNameMap.put(6, R.id.garbage20);
        imageIdToNameMap.put(7, R.id.garbage7);
        imageIdToNameMap.put(8, R.id.garbage16);
        imageIdToNameMap.put(9, R.id.garbage9);
        imageIdToNameMap.put(10, R.id.garbage10);
        imageIdToNameMap.put(11, R.id.garbage22);
        imageIdToNameMap.put(12, R.id.garbage12);
        imageIdToNameMap.put(13, R.id.garbage13);
        imageIdToNameMap.put(14, R.id.garbage14);
        imageIdToNameMap.put(15, R.id.garbage15);
        imageIdToNameMap.put(16, R.id.garbage8);
        imageIdToNameMap.put(17, R.id.garbage5);
        imageIdToNameMap.put(18, R.id.garbage18);
        imageIdToNameMap.put(19, R.id.garbage19);
        imageIdToNameMap.put(20, R.id.garbage6);
        imageIdToNameMap.put(21, R.id.garbage21);
        imageIdToNameMap.put(22, R.id.garbage11);
        imageIdToNameMap.put(23, R.id.garbage23);
        imageIdToNameMap.put(24, R.id.garbage24);
        imageIdToNameMap.put(25, R.id.garbage25);
    }

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

        //click_blocker = findViewById(R.id.click_blocker);
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

        Hide_Navigation.enableFullscreen(Backyard_Game_Activity.this);
        Hide_Navigation.enableTrueImmersiveMode(Backyard_Game_Activity.this);
        setContentView(R.layout.activity_backyard_game);

        ConstraintLayout layout = findViewById(R.id.activity_backyard_game); // Replace with your layout ID
        selectedLevelInt = Integer.valueOf(selectedLevel);
        Log.d("DEBUG", "Selected Level: " + selectedLevelInt);
        if (selectedLevelInt == 3 || selectedLevelInt == 4) {
            changeBackground(selectedLevelInt, layout);
        }
        if (selectedLevelInt == 5 || selectedLevelInt == 6) {
            changeBackground(selectedLevelInt, layout);
        }

        cardViewEndGame = findViewById(R.id.cardview_endgame);
        cardViewEndGame.setVisibility(View.GONE);
        cardView = findViewById(R.id.cardView_ingame);
        cardView.setVisibility(View.GONE);
        cardViewTimesOut = findViewById(R.id.cardview_times_out);
        textViewTimesOut = findViewById(R.id.textview_times_out);
        timeEditText = findViewById(R.id.time);
        timeEditText.setKeyListener(null);
        timeEditText.setVisibility(View.VISIBLE);

        Button nextButton = findViewById(R.id.backyardButton_next);
        TextView text = findViewById(R.id.labelLevel);
        text.setText("Level " + selectedLevel);

//        cardQuestion = findViewById(R.id.cardView_questionnaire);


        //startCountDownTimer(TIMER_DURATION);

        // Get garbage from the database
        List<Garbage> garbageList = garbageDao.getGarbageByLocationIdAndLevelId(Location.BACKYARD, Integer.valueOf(selectedLevel));
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

        tutorialCardview = findViewById(R.id.backyard_tutorial);
        tutorialCardview.setVisibility(View.GONE);

        tutorialFilipinoCardview = findViewById(R.id.backyard_Filipinotutorial);
        tutorialFilipinoCardview.setVisibility(View.GONE);

        rewardCardview = findViewById(R.id.earnReward_cardviewBackyard);
        rewardCardview.setVisibility(View.GONE);

        rewardFilipinoCardview = findViewById(R.id.earnFilipinoReward_cardviewBackyard);
        rewardFilipinoCardview.setVisibility(View.GONE);

        // Initialize victory and failed messages
        victoryMessage = findViewById(R.id.victory_message);
        failedMessage = findViewById(R.id.gameover_message);

        ImageView starImageView = findViewById(R.id.stars_got);

        setupImageButtons();

        showBackInGameCardView();
        setNoInGameCardView();
        setYesInGameCardView();
        setupWindowInsets();

        setupPlayAgainButton();
        setupNextLevelButton();
        setupBackButton();
        cardViewTransparent();
//        applyBlurEffect();

        settings = settingsDao.getSettings(1);

        Profile myProfile = profileDao.getProfile(1);
        if(myProfile.getTutorialDoneLocation2() == 0){
            pauseTimer();
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
        TextView backCardview_text = findViewById(R.id.backyardBackCardView_message);
        backCardview_text.setText(TranslatorUtil.translate(backCardview_text.getText().toString(), languageCode));

        Button noButton = findViewById(R.id.backyardButton_no);
        noButton.setText(TranslatorUtil.translate(noButton.getText().toString(), languageCode));

        Button yesButton = findViewById(R.id.backyardButton_yes);
        yesButton.setText(TranslatorUtil.translate(yesButton.getText().toString(), languageCode));

        //Cardview GameOver
        Button buttonTryAgain = findViewById(R.id.backyardButton_try);
        buttonTryAgain.setText(TranslatorUtil.translate(buttonTryAgain.getText().toString(), languageCode));

        Button buttonNext = findViewById(R.id.backyardButton_next);
        buttonNext.setText(TranslatorUtil.translate(buttonNext.getText().toString(), languageCode));

        Button buttonBack = findViewById(R.id.backyardButton_back);
        buttonBack.setText(TranslatorUtil.translate(buttonBack.getText().toString(), languageCode));

        TextView textNextTutorial= findViewById(R.id.backyard_next_tutorial);
        textNextTutorial.setText(TranslatorUtil.translate(textNextTutorial.getText().toString(), languageCode));

        TextView textTimesUp = findViewById(R.id.textview_times_out);
        textTimesUp.setText(TranslatorUtil.translate(textTimesUp.getText().toString(), languageCode));

        TextView textQuestion = findViewById(R.id.questionText);
        textQuestion.setText(TranslatorUtil.translate(textQuestion.getText().toString(), languageCode));


        //Garbages
        ImageView paperImage = findViewById(R.id.garbage1);
        String paperDescription = TranslatorUtil.translate(getString(R.string.paper_text), languageCode);
        paperImage.setContentDescription(paperDescription);

        ImageView pizzaImage = findViewById(R.id.garbage2);
        String pizzaDescription = TranslatorUtil.translate(getString(R.string.pizza_text), languageCode);
        pizzaImage.setContentDescription(pizzaDescription);

        ImageView plasticCupImage = findViewById(R.id.garbage3);
        String plasticCupDescription = TranslatorUtil.translate(getString(R.string.platicCup_text), languageCode);
        plasticCupImage.setContentDescription(plasticCupDescription);

        ImageView fruitFeelsImage = findViewById(R.id.garbage4);
        String fruitFeelsDescription = TranslatorUtil.translate(getString(R.string.fruitFeels_text), languageCode);
        fruitFeelsImage.setContentDescription(fruitFeelsDescription);

        ImageView usedCanImage = findViewById(R.id.garbage5);
        String usedCanDescription = TranslatorUtil.translate(getString(R.string.usedCan_text), languageCode);
        usedCanImage.setContentDescription(usedCanDescription);

        ImageView paper2Image = findViewById(R.id.garbage6);
        String paper2Description = TranslatorUtil.translate(getString(R.string.paper2_text), languageCode);
        paper2Image.setContentDescription(paper2Description);

        ImageView chipsBagImage = findViewById(R.id.garbage7);
        String chipsBagDescription = TranslatorUtil.translate(getString(R.string.chipsBag_text), languageCode);
        chipsBagImage.setContentDescription(chipsBagDescription);

        ImageView brokenCarImage = findViewById(R.id.garbage8);
        String brokenCarDescription = TranslatorUtil.translate(getString(R.string.brokenCar_text), languageCode);
        brokenCarImage.setContentDescription(brokenCarDescription);

        ImageView paperRollImage = findViewById(R.id.garbage9);
        String paperRollDescription = TranslatorUtil.translate(getString(R.string.paperRoll_text), languageCode);
        paperRollImage.setContentDescription(paperRollDescription);

        ImageView bittenBurgerImage = findViewById(R.id.garbage10);
        String bittenBurgerDescription = TranslatorUtil.translate(getString(R.string.bittenBurger_text), languageCode);
        bittenBurgerImage.setContentDescription(bittenBurgerDescription);

        ImageView bittenPizzaImage = findViewById(R.id.garbage11);
        String bittenPizzaDescription = TranslatorUtil.translate(getString(R.string.bittenPizza_text), languageCode);
        bittenPizzaImage.setContentDescription(bittenPizzaDescription);

        ImageView eggShellImage = findViewById(R.id.garbage12);
        String eggShellDescription = TranslatorUtil.translate(getString(R.string.eggShell_text), languageCode);
        eggShellImage.setContentDescription(eggShellDescription);

        ImageView treeBranchImage = findViewById(R.id.garbage13);
        String treeBranchDescription = TranslatorUtil.translate(getString(R.string.treeBranch_text), languageCode);
        treeBranchImage.setContentDescription(treeBranchDescription);

        ImageView brokenClassImage = findViewById(R.id.garbage14);
        String brokenClassDescription = TranslatorUtil.translate(getString(R.string.brokenClass_text), languageCode);
        brokenClassImage.setContentDescription(brokenClassDescription);

        ImageView lightBulbImage = findViewById(R.id.garbage15);
        String lightBulbDescription = TranslatorUtil.translate(getString(R.string.lightBulb_text), languageCode);
        lightBulbImage.setContentDescription(lightBulbDescription);

        ImageView bananaImage = findViewById(R.id.garbage16);
        String bananaDescription = TranslatorUtil.translate(getString(R.string.banana_text), languageCode);
        bananaImage.setContentDescription(bananaDescription);

        ImageView usedCan2Image = findViewById(R.id.garbage17);
        String usedCan2Description = TranslatorUtil.translate(getString(R.string.usedCan2_text), languageCode);
        usedCan2Image.setContentDescription(usedCan2Description);

        ImageView ketchupBottleImage = findViewById(R.id.garbage18);
        String ketchupBottleDescription = TranslatorUtil.translate(getString(R.string.ketchupBottle_text), languageCode);
        ketchupBottleImage.setContentDescription(ketchupBottleDescription);

        ImageView boneImage = findViewById(R.id.garbage19);
        String boneDescription = TranslatorUtil.translate(getString(R.string.bone_text), languageCode);
        boneImage.setContentDescription(boneDescription);

        ImageView garbageBagImage = findViewById(R.id.garbage20);
        String garbageBagDescription = TranslatorUtil.translate(getString(R.string.garbageBag_text), languageCode);
        garbageBagImage.setContentDescription(garbageBagDescription);

        ImageView teaBagImage = findViewById(R.id.garbage21);
        String teaBagDescription = TranslatorUtil.translate(getString(R.string.teaBag_text), languageCode);
        teaBagImage.setContentDescription(teaBagDescription);

        ImageView rottenMeatImage = findViewById(R.id.garbage22);
        String rottenMeatDescription = TranslatorUtil.translate(getString(R.string.rottenMeat_text), languageCode);
        rottenMeatImage.setContentDescription(rottenMeatDescription);

        ImageView moldyBreadImage = findViewById(R.id.garbage23);
        String moldyBreadDescription = TranslatorUtil.translate(getString(R.string.moldyBread_text), languageCode);
        moldyBreadImage.setContentDescription(moldyBreadDescription);

        ImageView trashBagImage = findViewById(R.id.garbage24);
        String trashBagDescription = TranslatorUtil.translate(getString(R.string.trashBag_text), languageCode);
        trashBagImage.setContentDescription(trashBagDescription);

        ImageView candyWrapperImage = findViewById(R.id.garbage25);
        String candyWrapperDescription = TranslatorUtil.translate(getString(R.string.candyWrapper_text), languageCode);
        candyWrapperImage.setContentDescription(candyWrapperDescription);

        TextView textNextFilipinoTutorial= findViewById(R.id.next_Filipinotutorialbackyard);
        textNextFilipinoTutorial.setText(TranslatorUtil.translate(textNextFilipinoTutorial.getText().toString(), languageCode));

        TextView textNextEarnReward= findViewById(R.id.next_earnRewardBackyard);
        textNextEarnReward.setText(TranslatorUtil.translate(textNextEarnReward.getText().toString(), languageCode));

        TextView textNextFilipinoEarnReward= findViewById(R.id.nextFilipino_earnRewardBackyard);
        textNextFilipinoEarnReward.setText(TranslatorUtil.translate(textNextFilipinoEarnReward.getText().toString(), languageCode));
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed(); // Proceed with normal back action if CardView is not visible
//        cardView.setVisibility(View.VISIBLE);
//        //playButtonSound();
//        toggleMusic(false);
//        pauseTimer();
//        setGarbageClickableDisable();// Hide the CardView if it's currently visible
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        cardView.setVisibility(View.VISIBLE);
//        //playButtonSound();
//        toggleMusic(false);
//        pauseTimer();
//        setGarbageClickableDisable();
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
//        setGarbageClickableDisable();
//
//        // Your other logic here
//        Log.d("Debug", "App is stopped - likely sent to the home screen");
//    }

    private void setupTutorial() {
        tutorialCardview = findViewById(R.id.backyard_tutorial);
        tutorialImageView = findViewById(R.id.backyard_tutorial1);
        TextView tutorialText = findViewById(R.id.backyard_next_tutorial);

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
        myProfile.setTutorialDoneLocation2(1);
        profileDao.updateProfile(myProfile);
    }

    private void setupFilipinoTutorial() {
        tutorialFilipinoCardview = findViewById(R.id.backyard_Filipinotutorial);
        tutorialFilipinoImageView = findViewById(R.id.backyard_Filipinotutorial1);
        TextView tutorialFilipinoText = findViewById(R.id.next_Filipinotutorialbackyard);

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
        myProfile.setTutorialDoneLocation2(1);
        profileDao.updateProfile(myProfile);
    }

    private void earnRewardCardView() {
        rewardCardview = findViewById(R.id.earnReward_cardviewBackyard);
        rewardImageView = findViewById(R.id.earnReward_viewBackyard);
        TextView rewardText = findViewById(R.id.next_earnRewardBackyard);

        applauseSound();

        rewardCardview.setVisibility(View.VISIBLE);
        //rewardImageView.setImageResource(rewardImages[currentRewardIndex]);

        rewardText.setOnClickListener(v -> {
            currentRewardIndex++;
            if (currentRewardIndex < rewardImages.length) {
                rewardImageView.setImageResource(rewardImages[currentRewardIndex]);
            } else {
//                rewardCardview.setVisibility(View.GONE); // Hide the tutorial when the sequence is finished
                Intent intent = new Intent(Backyard_Game_Activity.this, Backyard_Location_Activity.class);
                startActivity(intent);
                finish();
            }
            playButtonSound();
        });
    }

    private void earnFilipinoRewardCardView() {
        rewardFilipinoCardview = findViewById(R.id.earnFilipinoReward_cardviewBackyard);
        rewardFilipinoImageView = findViewById(R.id.earnFilipinoReward_viewBackyard);
        TextView rewardFilipinoText = findViewById(R.id.nextFilipino_earnRewardBackyard);

        applauseSound();

        rewardFilipinoCardview.setVisibility(View.VISIBLE);
        //rewardImageView.setImageResource(rewardImages[currentRewardIndex]);

        rewardFilipinoText.setOnClickListener(v -> {
            currentFilipinoRewardIndex++;
            if (currentFilipinoRewardIndex < rewardFilipinoImages.length) {
                rewardFilipinoImageView.setImageResource(rewardFilipinoImages[currentFilipinoRewardIndex]);
            } else {
//                rewardCardview.setVisibility(View.GONE); // Hide the tutorial when the sequence is finished
                Intent intent = new Intent(Backyard_Game_Activity.this, Backyard_Location_Activity.class);
                startActivity(intent);
                finish();
            }
            playButtonSound();
        });

    }

    private void setClickable(ImageView imageView, Garbage g) {
        imageView.setOnClickListener(v -> {
            // Make cardView_questionnaire visible
            cardViewQuestionnaire = findViewById(R.id.cardView_questionnaire);
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
                // Get the content description of the clicked ImageView
                String description = (String) imageView.getContentDescription();
                imageText.setText(description); // Update the TextView with the description
            }

            // Store the correct answer and selected garbage
            correctAnswerId = g.getGarbagecategoryId();
            selectedGarbage = imageView;
        });
    }

    private void setGarbageClickableDisable() {
        List<Garbage> garbageList = garbageDao.getGarbageByLocationIdAndLevelId(Location.BACKYARD, selectedLevelInt);
        int ctr = 0;
        for (Garbage g : garbageList) {
            ctr++;
            Integer imageViewId = imageIdToNameMap.get(ctr);
            ImageView imageView = findViewById(imageViewId);
            if (imageView != null) {
                imageView.setEnabled(false);
            }
        }
    }

    private void setGarbageClickEnable() {
        List<Garbage> garbageList = garbageDao.getGarbageByLocationIdAndLevelId(Location.BACKYARD, selectedLevelInt);
        int ctr = 0;
        for (Garbage g : garbageList) {
            ctr++;
            Integer imageViewId = imageIdToNameMap.get(ctr);
            ImageView imageView = findViewById(imageViewId);
            if (imageView != null) {
               imageView.setEnabled(true);
            }
        }
    }

    private void cardViewTransparent(){
        CardView cardViewQuestionnaire = findViewById(R.id.cardView_questionnaire);

        cardViewQuestionnaire.setBackgroundColor(Color.TRANSPARENT);
        cardViewQuestionnaire.setCardBackgroundColor(Color.TRANSPARENT);
        cardViewQuestionnaire.setCardElevation(0f);
    }


    private void setupImageButtons() {
        ImageButton biodegradableButton = findViewById(R.id.biodegradable);
        ImageButton nonBiodegradableButton = findViewById(R.id.non_biodegradable);
        ImageButton recyclableButton = findViewById(R.id.recyclable);

        biodegradableButton.setOnClickListener(v -> checkAnswer(GarbageCategory.BIODEGRADABLE));
        nonBiodegradableButton.setOnClickListener(v -> checkAnswer(GarbageCategory.NON_BIODEGRADABLE));
        recyclableButton.setOnClickListener(v -> checkAnswer(GarbageCategory.RECYCLABLE));
    }

    private boolean checkAnswer(int selectedAnswerId) {
        boolean isAnswerCorrect=false;
        if (selectedAnswerId == correctAnswerId) {
            // Hide the card view if the answer is correct
            cardViewQuestionnaire = findViewById(R.id.cardView_questionnaire);
            cardViewQuestionnaire.setVisibility(View.GONE);
            dropTrashSound();
//            Toast.makeText(this, "Correct answer!", Toast.LENGTH_SHORT).show();
            isAnswerCorrect = true;
            disposedCount++;
            if(selectedGarbage != null){
                selectedGarbage.setVisibility(View.GONE);
                selectedGarbage = null;
            }
            if (disposedCount == numberOfGarbageInLevel) {
                showEndGameMessage(numberOfGarbageInLevel, selectedLevelInt);
                findViewById(R.id.click_blocker).setVisibility(View.VISIBLE);
                cardViewEndGame.setVisibility(View.VISIBLE);
            }
            enableBackButton();

        } else {
            wrongAnswerSound();
//            Toast.makeText(this, "Try again.", Toast.LENGTH_SHORT).show();

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

    private void changeBackground(int level, ConstraintLayout  layout) {
        // Change the background based on the selected level
        if (level == 3) {
            layout.setBackgroundResource(R.drawable.backyard_playarea_level); // Replace with your drawable
        } else if (level == 4) {
            layout.setBackgroundResource(R.drawable.backyard_playarea_level); // Replace with your drawable
        }else if (level == 5) {
            layout.setBackgroundResource(R.drawable.backyard_garden_level); // Replace with your drawable
        }else if (level == 6) {
            layout.setBackgroundResource(R.drawable.backyard_garden_level); // Replace with your drawable
        }
    }

//    private void applyBlurEffect() {
//        Blurry.with(this)
//                .radius(10) // Adjust the blur radius
//                .sampling(2)
//                .onto((ViewGroup) findViewById(R.id.questionCard));
//    }

    private void showBackInGameCardView() {
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            playButtonSound();
            pauseTimer();
            setGarbageClickableDisable();
            cardView.setVisibility(View.VISIBLE);
            toggleMusic(false);

        });
    }

    private void setNoInGameCardView() {
        Button buttonCvNo = findViewById(R.id.backyardButton_no);
        if (buttonCvNo != null) {
            buttonCvNo.setOnClickListener(v -> {
                playButtonSound();
                cardView.setVisibility(View.GONE);
                toggleMusic(true);// Hide the card view
                startCountDownTimer(timeLeftInMillis); // Restart the timer with the remaining time
                setGarbageClickEnable();
                // Resume the timer
            });
        }
    }

    private void setYesInGameCardView() {
        Button buttonCvYes = findViewById(R.id.backyardButton_yes);
        if (buttonCvYes != null) {
            buttonCvYes.setOnClickListener(v -> {
                playButtonSound();
                toggleMusic(false);
                Intent intent = new Intent(Backyard_Game_Activity.this, Backyard_Location_Activity.class);
                startActivity(intent);
                finish();
            });
        }
    }

    private void setupWindowInsets() {
        View mainView = findViewById(R.id.activity_backyard_game);
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
                findViewById(R.id.click_blocker).setVisibility(View.VISIBLE);
                //cardViewEndGame.setVisibility(View.VISIBLE);

                // Add a 3-second delay before displaying the CardView
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cardViewEndGame.setVisibility(View.VISIBLE);
                    }
                }, 5100); // 3000 milliseconds = 3 seconds

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
        setGarbageClickableDisable();
        disableBackButton();

        toggleMusic(false);

        //findViewById(R.id.click_blocker).setVisibility(View.VISIBLE);
//        cardViewEndGame.bringToFront();


        int starRating = calculateStarRating(disposedCount, numberOfGarbageInLevel);
        Log.d("EndGame", "Calculated star rating: " + starRating);
        ImageView starImageView = findViewById(R.id.stars_got);
        if (starImageView != null) {
            int drawableResId = getStarDrawable(starRating);
            Log.d("EndGame", "Drawable resource ID: " + drawableResId);
            starImageView.setImageResource(drawableResId);
            Log.d("EndGame", "Set drawable resource on ImageView");
        }

//        Disable all images
        for (Integer imageViewId : imageIdToNameMap.values()) {
            ImageView imageView = findViewById(imageViewId);
            if (imageView != null) {
                imageView.setEnabled(false); // Disable the garbage ImageView
            }
        }

        if (cardViewQuestionnaire != null) {
            cardViewQuestionnaire.setVisibility(View.GONE);
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
                victoryMessage.setVisibility(View.VISIBLE);
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

//            MediaPlayer x = MediaPlayer.create(this, R.raw.game_over);
//            x.start();
//            x.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    mp.release();
//                }
//            });

            SoundPool soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
            int soundId = soundPool.load(this, R.raw.applause, 1);
            soundPool.play(soundId, 1, 1, 0, 0, 1);


            //new Handler().postDelayed(() -> {
            //    applauseSound(); // Play applause sound
            //}, 500); // 500ms delay for applauseSound
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

        StarRating sr =  starRatingDao.getStarRatingByProfileIdLocationIdAndLevelId(1,Location.BACKYARD, selectedLevel);
        if(sr == null){
            double julianDayNumber = DateUtil.toJulianDayNumber(LocalDateTime.now());
            sr = new StarRating();
            sr.setProfileId(Profile.DEFAULT_PROFILE);
            sr.setLevelId(selectedLevel);
            sr.setStars(starRating);
            sr.setDateCreated(julianDayNumber);
            sr.setLocationId(Location.BACKYARD);
            starRatingDao.insertStarRating(sr);
        }else {
            int currentStar = sr.getStars();
            if(starRating > currentStar) {
                sr.setStars(starRating);
                starRatingDao.updateStarRating(sr);
            }
        }

        int starsCountInBackyardLocation = starRatingDao.getTotalStarsByProfileIdAndLocationId(1,Location.BACKYARD);
        if (timeLeftInMillis <= 0) {
            if(starsCountInBackyardLocation == StarRating.TOTAL_STARS_BACKYARD){
                //show na may na achieve sya
                Profile profile = profileDao.getProfile(1);
                int isCompleted = profile.getIsLocationCompletedBackyard();
                if(isCompleted == 0){
                    profile.setIsLocationCompletedBackyard(1);
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
            if(starsCountInBackyardLocation == StarRating.TOTAL_STARS_BACKYARD){
                Profile profile = profileDao.getProfile(1);
                int isCompleted = profile.getIsLocationCompletedBackyard();
                if(isCompleted == 0){
                    profile.setIsLocationCompletedBackyard(1);
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

    private void enableBackButton() {
        ImageButton backButton = findViewById(R.id.back_button);
        if (backButton != null) {
            backButton.setEnabled(true);
        }
    }

    private void setupPlayAgainButton() {
        Button playAgainButton = findViewById(R.id.backyardButton_try);
        if (playAgainButton != null) {
            playAgainButton.setOnClickListener(v -> {
                stopSound();
                playButtonSound();
                // Create an intent to restart the activity
                Intent intent = new Intent(Backyard_Game_Activity.this, Backyard_Game_Activity.class);
                // Optionally, pass the current level or any other necessary data
                intent.putExtra("selectedLevel", selectedLevel); // Ensure selectedLevel is defined in your class
                startActivity(intent);
                finish(); // Finish the current activity
            });
        }
    }

    private Button setupNextLevelButton() {
        Button nextButton = findViewById(R.id.backyardButton_next); // Initialize the button
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
                    intent = new Intent(Backyard_Game_Activity.this, Backyard_Game_Activity.class);
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
        Button btnbackButton = findViewById(R.id.backyardButton_back);
        if (btnbackButton != null) {
            btnbackButton.setOnClickListener(v -> {
                playButtonSound();
                stopSound();
                Intent intent = new Intent(Backyard_Game_Activity.this, Backyard_Location_Activity.class);
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