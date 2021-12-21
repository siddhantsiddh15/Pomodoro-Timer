package com.nichecloutpomodorotimer.pomodorotimer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {

    TextView timerTextView;
    TextView totalHoursTextView;
    TextView lastSessionTextView;
    TextView allTimeBEstTextView;
    MediaPlayer mediaPlayer;
    MediaPlayer breakSoundMusic;
    SeekBar timerSeekBAr;
    SeekBar cycleSeekBar;
    SeekBar breakSeekBar;
    Boolean counterIsActive = false;
    Button goButton;
    CountDownTimer countDownTimer;
    AdView mAdView;
    int count=0;  // keeps track of the cycleNumber
    int workingHours;
    int allTimeBest;
    SharedPreferences sharedPreferences ;
    SharedPreferences sharedPreferencesAllTimeBest;
    int lastSessionHours;
    String hoursstr = "";
    String minutesstr = ":";
    String secondsstr = ":";


    int hours;
    int minutes;
    int seconds;

    public void resetTimer() {
        timerTextView.setText("10:00");
        timerSeekBAr.setProgress(2);
        timerSeekBAr.setEnabled(true);
        cycleSeekBar.setEnabled(true);
        breakSeekBar.setEnabled(true);
        countDownTimer.cancel();
        goButton.setText("Start Again");
        counterIsActive = false;


        //breakSound music method
        breakSoundMusic = MediaPlayer.create(getApplicationContext(), R.raw.breaksoundmusic);
        breakSoundMusic.setVolume(.4f, .4f);
        breakSoundMusic.setLooping(false);
        breakSoundMusic.stop();
        breakSoundMusic.reset();
        breakSoundMusic.stop();
        breakSoundMusic.reset();



        // setting the total number of hours

        goButton.setBackgroundColor( Color.parseColor("#00504b"));

//         hours = workingHours/3600;
//         minutes = (workingHours - hours*3600)/60;
//         seconds =workingHours - hours*3600 - minutes*60;

       sharedPreferences.edit().putInt("workingHours",workingHours).apply();           //saving the working hours
//
//
//        secondsstr = Integer.toString(seconds);
//        minutesstr = Integer.toString(minutes);
//        if(minutes<=9){
//            minutesstr = "0" + Integer.toString(minutes);
//        }
//        if(seconds<= 9){
//            secondsstr = "0" + Integer.toString(seconds);
//        }
//        hoursstr = Integer.toString(hours);
//        totalHoursTextView.setText(hoursstr + ":"  + minutesstr + ":" + secondsstr);


        setTextView(workingHours, "totalHoursTextView");




        if(workingHours>allTimeBest){
            allTimeBest = workingHours;
            sharedPreferencesAllTimeBest.edit().putInt("allTimeBest", allTimeBest).apply();
//            updatingAllTimeBestTextView(allTimeBest);
            setTextView(allTimeBest, "updatingAllTimeBestTextView");
        }
//         String str = hours + ":" + minutes +":" + seconds;
//        if(minutes<=9 || seconds<=9){
//            totalHoursTextView.setText(hours + ":0" + minutes +":0" + seconds);
//            lastSessionTextView.setText("Last Session: " + hours + ":0" + minutes +":0" + seconds);
//        }else{
//            totalHoursTextView.setText(hours+ ":" + minutes + ":" + seconds);
//            lastSessionTextView.setText("Last Session: " + hours+ ":" + minutes + ":" + seconds);
//        }



    }




    public void buttonClicked(View view) {
        if (counterIsActive) {
            resetTimer();

        } else {
            counterIsActive = true;
            timerSeekBAr.setEnabled(false);
            cycleSeekBar.setEnabled(false);
            breakSeekBar.setEnabled(false);
            goButton.setText("STOP!");
            goButton.setBackgroundColor( Color.parseColor("#f32013"));


            timerSeekBarCountdown();
           // breakTimer(); cycleSeekBar.getProgress()

        }
    }

    public void createBreakSoundMusic(String number){



        if(number.equals("STOP")) {

        }
        else if(number.equals("START")){

        }
    }


    public void timerSeekBarCountdown(){
        countDownTimer = new CountDownTimer(timerSeekBAr.getProgress() * 1000 *300, 1000) {
            @Override
            public void onTick(long l) {
                //our declared method
                updateTimer((int) (l / 1000));
            }

            @Override
            public void onFinish() {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.buzzer1);
                    mediaPlayer.setVolume(.6f,.6f);
                    mediaPlayer.start();

//                breakSoundMusic = MediaPlayer.create(getApplicationContext(), R.raw.breaksoundmusic);
//                breakSoundMusic.setVolume(.4f, .4f);
//                breakSoundMusic.setLooping(true);
//                breakSoundMusic.start();


                count++;

                // call the break method
                breakTimer();

                // updating the timer to count total hours
                workingHours =   ( (timerSeekBAr.getProgress() * 1000 *300) *count)/ 1000;   //this all is in milli seconds

                sharedPreferences.edit().putInt("workingHours",workingHours).apply();

                if(workingHours>allTimeBest){
                    allTimeBest = workingHours;
                    sharedPreferencesAllTimeBest.edit().putInt("allTimeBest", allTimeBest).apply();
                    updatingAllTimeBestTextView(allTimeBest);
                }


                setTextView( workingHours, "totalHoursTextView");
//                 hours = workingHours/3600;
//                 minutes = (workingHours - hours*3600)/60;
//                 seconds =workingHours - hours*3600 - minutes*60;
//
//                secondsstr = Integer.toString(seconds);
//                minutesstr = Integer.toString(minutes);
//                if(minutes<=9){
//                    minutesstr = "0" + Integer.toString(minutes);
//                }
//                if(seconds<= 9){
//                    secondsstr = "0" + Integer.toString(seconds);
//                }
//                hoursstr = Integer.toString(hours);
//
//
//
//
//                totalHoursTextView.setText(hoursstr + ":"  + minutesstr + ":" + secondsstr);

            }
        }.start();
    }

    //break duration timer

    public void breakTimer(){
        countDownTimer = new CountDownTimer(breakSeekBar.getProgress()  *60 *1000+ 100,1000) {
            @Override
            public void onTick(long l) {
                updateTimer((int) (l / 1000));
            }

            @Override
            public void onFinish() {

                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.buzzer3);
                mediaPlayer.start();

//                breakSoundMusic.stop();
//                breakSoundMusic.reset();
                if(count>cycleSeekBar.getProgress()) {
                    resetTimer();
                    lastSessionTextView.setText("Last Session: " + hoursstr + ":"  + minutesstr + ":" + secondsstr);
                }else{
                    timerSeekBarCountdown();
                }
            }
        }.start();
    }


    public void setTextView(int workingHours, String textName){
        hours = workingHours/3600;
        minutes = (workingHours - hours*3600)/60;
        seconds =workingHours - hours*3600 - minutes*60;

        secondsstr = Integer.toString(seconds);
        minutesstr = Integer.toString(minutes);
        if(minutes<=9){
            minutesstr = "0" + Integer.toString(minutes);
        }
        if(seconds<= 9){
            secondsstr = "0" + Integer.toString(seconds);
        }
        hoursstr = Integer.toString(hours);

        if(textName.equals("totalHoursTextView")){
            totalHoursTextView.setText(hoursstr + ":"  + minutesstr + ":" + secondsstr);
        }
        if(textName.equals("lastSessionTextView")){
            lastSessionTextView.setText(" Last Session: " + hoursstr + ":"  + minutesstr + ":" + secondsstr);
        }
        if(textName.equals("allTimeBEstTextView")){
            allTimeBEstTextView.setText(" All time best: " + hoursstr + ":"  + minutesstr + ":" + secondsstr);
                }

    }



    //updating display

    public void updateTimer(int secondsLeft) {

        int minutes = secondsLeft /60;
        int seconds = secondsLeft - (minutes * 60);

        String secondString = Integer.toString(seconds);

        if (seconds <= 9) {
            timerTextView.setText(Integer.toString(minutes) + ":0" + secondString );
        } else {
            timerTextView.setText(Integer.toString(minutes) + ":" + Integer.toString(seconds));
        }
    }

    // set text View
    public void updatingAllTimeBestTextView(int lastSessionHours){
        hours = lastSessionHours/3600;
        minutes = (lastSessionHours - hours*3600)/60;
        seconds =lastSessionHours - hours*3600 - minutes*60;

        secondsstr = Integer.toString(seconds);
        minutesstr = Integer.toString(minutes);
        if(minutes<=9){
            minutesstr = "0" + Integer.toString(minutes);
        }
        if(seconds<= 9){
            secondsstr = "0" + Integer.toString(seconds);
        }
        hoursstr = Integer.toString(hours);
        allTimeBEstTextView.setText(" All time best: " + hoursstr + ":"  + minutesstr + ":" + secondsstr);
    }


    // updatingLAstSessionTextView
    public void updatingLastSessionTextView(int lastSessionHours){
        hours = lastSessionHours/3600;
        minutes = (lastSessionHours - hours*3600)/60;
        seconds =lastSessionHours - hours*3600 - minutes*60;

        secondsstr = Integer.toString(seconds);
        minutesstr = Integer.toString(minutes);
        if(minutes<=9){
            minutesstr = "0" + Integer.toString(minutes);
        }
        if(seconds<= 9){
            secondsstr = "0" + Integer.toString(seconds);
        }
        hoursstr = Integer.toString(hours);

        lastSessionTextView.setText(" Last Session: " + hoursstr + ":"  + minutesstr + ":" + secondsstr);
    }



    // on create method from here

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            //saving value of data
            lastSessionTextView = findViewById(R.id.lastSessionTextView);
            allTimeBEstTextView = findViewById(R.id.allTimeBestTextView);
          sharedPreferences = getApplicationContext().getSharedPreferences("com.nichecloutpomodorotimer.pomodorotimer", Context.MODE_PRIVATE);
          sharedPreferencesAllTimeBest= getApplicationContext().getSharedPreferences("com.nichecloutpomodorotimer.pomodorotimer", Context.MODE_PRIVATE);

          allTimeBest = sharedPreferencesAllTimeBest.getInt("allTimeBest", 0);
         lastSessionHours =  sharedPreferences.getInt("workingHours", 0);



            setTextView(allTimeBest, "allTimeBEstTextView");
            setTextView(lastSessionHours, "lastSessionTextView");

//            updatingAllTimeBestTextView(allTimeBest);
//            updatingLastSessionTextView(lastSessionHours);


            //adding adds

            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });

            mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);



            timerSeekBAr = findViewById(R.id.timerSeekBar);
            cycleSeekBar = findViewById(R.id.cycleSeekBar);
            breakSeekBar = findViewById(R.id.breakSeekBar);
            timerTextView = findViewById(R.id.timerTextView);
            totalHoursTextView = findViewById(R.id.totalHoursTextView);
            goButton = findViewById(R.id.eggButton);
            //playPause button




            //for break duration
            breakSeekBar.setMin(1);
            breakSeekBar.setMax(20);
            breakSeekBar.setProgress(5);

            breakSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                    updateTimer(progress);
                    timerTextView.setText(Integer.toString(progress)+ ":00");
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tick);
                    mediaPlayer.setVolume(.5f,.5f);

                    mediaPlayer.start();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            //for counting number of cycles
            cycleSeekBar.setMax(8);
            cycleSeekBar.setProgress(4);


            cycleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                   // Toast.makeText(MainActivity.this, Integer.toString(progress), Toast.LENGTH_SHORT).show();
                    timerTextView.setText(Integer.toString(progress));
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tick);
                    mediaPlayer.setVolume(.5f,.5f);
                    mediaPlayer.start();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            //for timerSeekBar
            timerSeekBAr.setMin(1);
            timerSeekBAr.setMax(11);
            timerSeekBAr.setProgress(5);

            timerSeekBAr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                    updateTimer(i);
                    timerTextView.setText(Integer.toString(i*5) + ":" + "00" );
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tick);
                    mediaPlayer.setVolume(.5f,.5f);
                    mediaPlayer.start();

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
    }
