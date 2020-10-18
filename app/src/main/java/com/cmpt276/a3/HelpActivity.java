package com.cmpt276.a3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        TextView linkText = (TextView) findViewById(R.id.link);
        linkText.setMovementMethod(LinkMovementMethod.getInstance());

    }
}