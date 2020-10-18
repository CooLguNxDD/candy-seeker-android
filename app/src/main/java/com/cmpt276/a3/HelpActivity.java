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
        //set up hyperlinks
        TextView linkText = (TextView) findViewById(R.id.link);
        TextView linkText2 = (TextView) findViewById(R.id.link2);
        TextView linkText3 = (TextView) findViewById(R.id.link3);
        TextView linkText4 = (TextView) findViewById(R.id.link4);

        linkText.setMovementMethod(LinkMovementMethod.getInstance());
        linkText2.setMovementMethod(LinkMovementMethod.getInstance());
        linkText3.setMovementMethod(LinkMovementMethod.getInstance());
        linkText4.setMovementMethod(LinkMovementMethod.getInstance());

    }
}