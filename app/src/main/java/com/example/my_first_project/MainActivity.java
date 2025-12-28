package com.example.my_first_project;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.*;

public class MainActivity extends AppCompatActivity
{

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        audioManager.requestAudioFocus(
                focusChange -> {},
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
        );

        mediaPlayer = MediaPlayer.create(this, R.raw.game_background_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(0.1f, 0.1f);
        mediaPlayer.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }



    //    0-x
    //    1-0

    int activePlayer=0;
    //    0-x
    //    1-0
    //    2-null
    int gameState[]=  {2,2,2
                      ,2,2,2
                      ,2,2,2};
    int winPositions[][]=   {   {0,1,2},  {3,4,5},  {6,7,8},
                                {0,3,6},  {1,4,7},  {2,5,8},
                                {2,4,6},  {0,4,8}            };
    boolean gameactive=true;
    int count=0;

    public void oc1(View view)
    {

        ImageView img=(ImageView) view;
        int tappedImage=Integer.parseInt(img.getTag().toString())-1;

        if(gameState[tappedImage]==2 && gameactive)
        {
            playSoundEffect(R.raw.click_sound);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                vibrate();
            }

            count++;
            gameState[tappedImage]=activePlayer;
            img.setTranslationX(-1000f);
            if(activePlayer==1)
            {
                img.setImageResource(R.drawable.zero_png);

                activePlayer=0;

                TextView text_player_turn=findViewById(R.id.text_player_turn);
                text_player_turn.setText("X's   player  turn");

            }
            else
            {
                img.setImageResource(R.drawable.x_png);

                activePlayer=1;

                TextView text_player_turn=findViewById(R.id.text_player_turn);
                text_player_turn.setText("0's   player  turn");
            }

            img.animate().translationXBy(1000f).setDuration(400);

            for(int [] winPosition:winPositions)
            {
                if(gameState[winPosition[0]]==gameState[winPosition[1]] &&
                        gameState[winPosition[1]]==gameState[winPosition[2]] &&
                        gameState[winPosition[0]]!=2)
                {
                    playSoundEffect(R.raw.game_completion_sound);

                    if(gameState[winPosition[0]]==0)
                    {
                        TextView text_player_turn=findViewById(R.id.text_player_turn);
                        text_player_turn.setText("  X  WON! ");
                        TextView Reset_Text=findViewById(R.id.Reset_Text);
                        Reset_Text.setText("Tap Reset to play AGAIN ");
                    }
                    else {
                        TextView text_player_turn=findViewById(R.id.text_player_turn);
                        text_player_turn.setText("  0  WON! ");
                        TextView Reset_Text=findViewById(R.id.Reset_Text);
                        Reset_Text.setText("Tap Reset to play AGAIN ");
                    }

                    

                    if(winPosition[0]==0 && winPosition[1]==1 && winPosition[2]==2 )
                    {
                        ((ImageView) findViewById(R.id.imageView22)).setImageResource(R.drawable.line_3_png);
                    }
                    if(winPosition[0]==3 && winPosition[1]==4 && winPosition[2]==5 )
                    {
                        ((ImageView) findViewById(R.id.imageView23)).setImageResource(R.drawable.line_3_png);
                    }
                    if(winPosition[0]==6 && winPosition[1]==7 && winPosition[2]==8 )
                    {
                        ((ImageView) findViewById(R.id.imageView24)).setImageResource(R.drawable.line_3_png);
                    }
                    if(winPosition[0]==0 && winPosition[1]==3 && winPosition[2]==6 )
                    {
                        ((ImageView) findViewById(R.id.imageView12)).setImageResource(R.drawable.line_2_png);
                    }
                    if(winPosition[0]==1 && winPosition[1]==4 && winPosition[2]==7 )
                    {
                        ((ImageView) findViewById(R.id.imageView13)).setImageResource(R.drawable.line_2_png);
                    }
                    if(winPosition[0]==2 && winPosition[1]==5 && winPosition[2]==8 )
                    {
                        ((ImageView) findViewById(R.id.imageView14)).setImageResource(R.drawable.line_2_png);
                    }
                    if(winPosition[0]==0 && winPosition[1]==4 && winPosition[2]==8 )
                    {
                        ((ImageView) findViewById(R.id.imageView15)).setImageResource(R.drawable.line_4_png);
                    }
                    if(winPosition[0]==2 && winPosition[1]==4 && winPosition[2]==6 )
                    {
                        ((ImageView) findViewById(R.id.imageView16)).setImageResource(R.drawable.line_1_png);
                    }

                    gameactive=false;


                }
            }

        }
        if(count==9 && gameactive!=false)
        {
            TextView text_player_turn=findViewById(R.id.text_player_turn);
            text_player_turn.setText("Match Tie! ");
            TextView Reset_Text=findViewById(R.id.Reset_Text);
            Reset_Text.setText("Tap Reset to play AGAIN ");
        }
    }


    public void reset_button(View view) {
        if(count==0)return;

        playSoundEffect(R.raw.vanish_dound);
        activePlayer = 0;
        gameactive = true;
        TextView text_player_turn = findViewById(R.id.text_player_turn);
        text_player_turn.setText("X's player turn");

        // Reset game state
        for (int i = 0; i < 9; i++) {
            gameState[i] = 2;
        }

        // List of ImageView IDs to animate and reset
        int[] imageViews = {
                R.id.imageView4, R.id.imageView3, R.id.imageView2, R.id.imageView5, R.id.imageView6,
                R.id.imageView7, R.id.imageView8, R.id.imageView9, R.id.imageView10,
                R.id.imageView22, R.id.imageView23, R.id.imageView24,
                R.id.imageView12, R.id.imageView13, R.id.imageView14,
                R.id.imageView15, R.id.imageView16
        };



//        animate and vanish all images
        int cnt=0;
        for (int id: imageViews)
        {
            cnt++;
            if(cnt==10)break;
            ImageView img = findViewById(id);
            img.animate()
                    .translationYBy(-50f) // Move up slightly
                    .setDuration(200)
                    .withEndAction(() -> img.animate()
                            .translationYBy(50f) // Move back down
                            .alpha(0f) // Fade out
                            .setDuration(200)
                            .withEndAction(() -> {
                                img.setImageResource(0);
                                img.setTranslationY(0f); // Reset position
                                img.setAlpha(1f);
                            })
                    );
        }
//        remove lines

        cnt=0;
        for (int id: imageViews)
        {

            cnt++;
            if(cnt<10)continue;
            ((ImageView)findViewById(id)).setImageResource(0);

        }

        // Clear reset text
        TextView Reset_Text = findViewById(R.id.Reset_Text);
        Reset_Text.setText("");
        count = 0;

    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void vibrate()
    {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            // For Android O and above (API 26+)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.EFFECT_TICK));
            } else {
                // For older versions
                vibrator.vibrate(100); // vibrate for 100 milliseconds
            }
        }
    }

    private void playSoundEffect(int soundResId) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, soundResId);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.release();
        });
    }
}