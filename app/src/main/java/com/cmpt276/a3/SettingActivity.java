package com.cmpt276.a3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.cmpt276.a3.Model.BoardAdapter;
import com.cmpt276.a3.Model.boardSpinnerClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cmpt276.a3.Model.Singleton.SAVESETTING;

public class SettingActivity extends AppCompatActivity {

    private ArrayList<boardSpinnerClass> boardSizes= new ArrayList<>();
    private ArrayList<Integer> minesNum = new ArrayList<>();
    SharedPreferences save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting2);
        save = getSharedPreferences(SAVESETTING, Context.MODE_PRIVATE);

        //- 4 rows by 6 columns
         //       - 5 rows by 10 columns
          //      - 6 rows by 15 columns
        boardSizes.add(new boardSpinnerClass("4x6", 4,6));
        boardSizes.add(new boardSpinnerClass("5x10", 5,10));
        boardSizes.add(new boardSpinnerClass("6x15", 6,15));
        setBoardSpinner();
        minesNum.add(6);
        minesNum.add(8);
        minesNum.add(10);
        minesNum.add(15);
        minesNum.add(20);
        setMinepinner();
        setOkButton();

    }

    private void setBoardSpinner(){
        Spinner boardSpinner = (Spinner)findViewById(R.id.board_spinner);
        ArrayAdapter<boardSpinnerClass> boardAdopter = new BoardAdapter(SettingActivity.this,
                android.R.layout.simple_spinner_item, boardSizes);


        boardSpinner.setAdapter(boardAdopter);
        boardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),
                        "Selected: "+boardSizes.get(i).getDescription() , Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = save.edit();
                editor.putInt("row",boardSizes.get(i).getRow());
                editor.putInt("col",boardSizes.get(i).getCol());
                editor.apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void setOkButton(){
        Button ok_button = (Button)findViewById(R.id.ok_button);
        ok_button.setBackgroundResource(R.drawable.button_ripper);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void setMinepinner(){
        Spinner boardSpinner = (Spinner)findViewById(R.id.mine_spinner);
        ArrayAdapter<Integer> Adopter = new ArrayAdapter<Integer>(SettingActivity.this,
                android.R.layout.simple_spinner_item, minesNum);

        boardSpinner.setAdapter(Adopter);
        boardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),
                        "Selected Mines: "+(""+minesNum.get(i)), Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = save.edit();
                editor.putInt("mines",minesNum.get(i));
                editor.apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}