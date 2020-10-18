package com.cmpt276.a3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class WelcomeActivity extends AppCompatActivity {
    private static final int time = 5000;
    private boolean screenSwitch = false;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ImageView image = (ImageView) findViewById(R.id.welcome_image);
        Animation fade_in = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        image.startAnimation(fade_in);   // fade in animation

        // runnable to next activity after 5s
        final Handler handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        };
        handler.postDelayed(runnable,time);

        //skip button
        final Button button = (Button) findViewById(R.id.skip_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable); //remove runnable
                button.setBackgroundResource(R.drawable.button_click);
                Intent intent = new Intent(WelcomeActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}