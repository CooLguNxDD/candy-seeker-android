package com.cmpt276.a3;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cmpt276.a3.Model.ButtonStatus;
import com.cmpt276.a3.Model.Singleton;
import com.cmpt276.a3.Model.game.gameFunction;

import java.util.Locale;

import static com.cmpt276.a3.Model.Singleton.SAVESETTING;

public class MainActivity extends AppCompatActivity {
    SharedPreferences settings;

    private Singleton config;
    private ButtonStatus[][] buttons;
    private int remainMines;
    private int clickNum,totalGame;

    private String bestScore;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // get setting from SharedPreferences
        settings = getSharedPreferences(SAVESETTING, Context.MODE_PRIVATE);

        //config class will save : row, column, # of mines
        config = new Singleton(settings.getInt("row",4),settings.getInt("col",6),settings.getInt("mines",8));

        //button status class -> save Button, is a mines or not , is clicked or not
        //isClicked will not be true with a button click
        buttons = new ButtonStatus[config.getRow()][config.getCol()];

        //track mines remain
        remainMines = config.getMines();

        //track scans
        clickNum = 0;
        //add one to total game

        //total game and bestScore setup
        totalGame = settings.getInt("games",0);
        SharedPreferences.Editor editor = settings.edit();
        totalGame ++; //update total game
        editor.putInt("games" , totalGame);
        editor.apply();//save
        bestScore = ""+config.getRow()+"x"+config.getCol()+" mines "+config.getMines();

        // click return button will shows a popup window to tell player leave or not
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                popupOption("You process will be lost, Are you sure you want to leave?", MainActivity.this);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        createButtons(); //button array
        setTextField(); //setup text
        buttons = gameFunction.RandomMines(config.getMines(),config,buttons); //set random mines
        buttons = gameFunction.setInfo(config, buttons);//initial game

        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
    }
    private void setTextField(){
        //setup the textField
        TextView mines = (TextView) findViewById(R.id.mines_text);
        TextView scan = (TextView) findViewById(R.id.scan_text);
        TextView best= (TextView)findViewById(R.id.best_score);
        TextView total= (TextView)findViewById(R.id.total_game);
        best.setText(String.format(Locale.CANADA,"Mode: "+ bestScore +"   Best Score: %d", settings.getInt(bestScore, 0)));
        mines.setText(String.format(Locale.CANADA,"Found %d of %d mines.", config.getMines()-remainMines, config.getMines()));
        scan.setText(String.format(Locale.CANADA,"# Scans used: %d", clickNum));
        total.setText(String.format(Locale.CANADA,"Total Games: %d", totalGame ));
    }
    private void createButtons(){
        //set up game layout
        TableLayout table = (TableLayout) findViewById(R.id.game_table);
        for (int row = 0; row < config.getRow(); row++){
            TableRow tableRow = new TableRow(this);
            //scale table rows
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));

            table.addView(tableRow);
            //set up button
            for (int col = 0; col <  config.getCol(); col++){
                Button button = new Button(this);

                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                ));

                //button config
                button.setTextSize(20);
                button.setTextColor(Color.WHITE);
                button.setBackgroundResource(R.drawable.box_ripper);
                button.setPadding(0,0,0,0); //not scale with text

                final int currentRows = row;
                final int currentCols = col;
                //click event
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gridButtonClicked(currentRows,currentCols);
                    }
                });
                //add button to array
                tableRow.addView(button);
                buttons[row][col] = new ButtonStatus(button,false,false);
            }
        }
        lockButtonSize();
    }

    private void gridButtonClicked(int row, int col){
        //click event on grid button

        updateInfo(row,col); //update info every time player clicked a button

        if (gameFunction.checkWin(remainMines)){ //win
            SharedPreferences.Editor editor = settings.edit();
            if (settings.getInt(bestScore,0)>clickNum || settings.getInt(bestScore,0)==0){
                editor.putInt(bestScore,clickNum);
                editor.apply(); //popup window for best score
                popup("GG! You Win with best score, Scanned fields: "+ clickNum,this,true);
            }
            else{
                //popup window for best score
                popup("GG! You Win, Scanned fields: "+ clickNum,this,true);
            }
        }
    }
    private void lockButtonSize(){
        // lock button size to prevent resize
        for(int row = 0; row < config.getRow(); row++){
            for (int col = 0; col< config.getCol(); col++) {
                Button button =buttons[row][col].getButton();
                int width = button.getWidth();
                int height = button.getHeight();
                button.setWidth(width);
                button.setHeight(height);
                button.setMaxHeight(height);
                button.setMinHeight(height);
                button.setMaxWidth(width);
                button.setMinWidth(width);
            }
        }

    }

    private void setMinesIcons(Button button){
        //set candy image and scale it:
        int buttonWidth = button.getWidth();
        int buttonHeight = button.getHeight();

        button.setBackgroundResource(R.drawable.candy_icon);

        button.setHeight(buttonHeight);
        button.setWidth(buttonWidth-100); //Since i have locked the width,
        // -100 will help prevent the image being resize lol. Thanks Android.
    }

    private void updateInfo(int clickRow, int clickCol){
        //update the button[row][col] info:
        // if it is a mine -> set mines image
        // else display the # of mines on that row

        // each button can only be clicked once

        ButtonStatus status = buttons[clickRow][clickCol];
        Button selectButton = status.getButton();
        selectButton.setClickable(false); //disable button after a click

        if(!status.isClicked()){  //the button is not yet clicked
            if(status.is_mines()){  // is a mine
                setMinesIcons(selectButton); //set up
                status.set_Is_mines(false);
                remainMines -=1;
                clickNum +=1;
                vibrator.vibrate(400);

                buttons = gameFunction.setInfo(config,buttons); //update information
                setTextField();
            }
            else{
                clickNum +=1;
                vibrator.vibrate(50);
                status.setClicked(true); //already clicked
                buttons = gameFunction.setInfo(config,buttons); //update information
                setTextField();
            }
        }
    }
    private void popup(String msg, Context context, final boolean is_finished){
        //popup information when player win
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view;
        view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog, null);
        TextView text = (TextView)view.findViewById(R.id.message);
        text.setText(msg);

        builder.setCancelable(true);
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if (is_finished){
                            finish();
                        }
                    }
                });
        AlertDialog pop = builder.create();
        pop.show();
    }
    private void popupOption(String msg, Context context){
        //when player click return button
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        AlertDialog pop = builder.create();
        pop.show();
    }
}