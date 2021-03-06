package com.group0536.puzzlemazing.views.appinit;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.group0536.puzzlemazing.R;

/**
 * This activity displays Puzzle Mazing logo to player as they
 * enter the game.
 *
 * @author Jimmy
 * @version 1.0
 * @since 1.0
 */
public class WelcomeActivity extends AppCompatActivity {

    // Components
    VideoView vvBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setContentView(R.layout.activity_welcome);
        bindViews();
    }

    private void bindViews() {
        vvBackground = findViewById(R.id.vvBackground);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpWelcomeVideo();
        vvBackground.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseWelcomeVideo();
    }

    private void setUpWelcomeVideo() {
        String videoPath = "android.resource://" + getPackageName() + "/" +
                R.raw.welcome;
        Uri videoUri = Uri.parse(videoPath);
        vvBackground.setVideoURI(videoUri);
        vvBackground.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Intent intent = new Intent(WelcomeActivity.this, AppInitializeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void releaseWelcomeVideo() {
        vvBackground.stopPlayback();
    }
}
