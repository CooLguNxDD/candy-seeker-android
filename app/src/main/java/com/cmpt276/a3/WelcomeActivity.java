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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ImageView image = (ImageView) findViewById(R.id.welcome_image);
        Animation fade_in = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        image.startAnimation(fade_in);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        },time);

        final Button button = (Button) findViewById(R.id.skip_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setBackgroundResource(R.drawable.button_click);
                Intent intent = new Intent(WelcomeActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}