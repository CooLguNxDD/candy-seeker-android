package com.cmpt276.a3;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cmpt276.a3.Model.ButtonStatus;
import com.cmpt276.a3.Model.GameConfig;

import java.util.Locale;
import java.util.Random;

import static com.cmpt276.a3.Model.Singleton.SAVESETTING;

public class MainActivity extends AppCompatActivity {
    SharedPreferences settings;

    private GameConfig config;
    private ButtonStatus[][] buttons;
    private int remainMines;
    private int clickNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = getSharedPreferences(SAVESETTING, Context.MODE_PRIVATE);

        config = new GameConfig(settings.getInt("row",4),settings.getInt("col",4),settings.getInt("mines",8));
        buttons = new ButtonStatus[config.getRow()][config.getCol()];
        remainMines = config.getMines();
        clickNum = 0;

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                popupOption("You process will be lost, Are you sure you want to leave?", MainActivity.this);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        createButtons();
        RandomMines(config.getMines());
        setInfo(); //initial game
    }
    private void setTextField(){
        TextView mines = (TextView) findViewById(R.id.mines_text);
        TextView scan = (TextView) findViewById(R.id.scan_text);
        TextView best= (TextView)findViewById(R.id.best_score);
        String bestScore = ""+config.getRow()+""+config.getCol()+""+config.getMines();
        best.setText(String.format(Locale.CANADA,"Best Score: %d", settings.getInt(bestScore, 0)));
        mines.setText(String.format(Locale.CANADA,"Found %d of %d mines.", config.getMines()-remainMines, config.getMines()));
        scan.setText(String.format(Locale.CANADA,"# Scans used: %d", clickNum));
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
                button.setBackgroundResource(R.drawable.game_button_ripper);
                //not scale with text
                button.setPadding(0,0,0,0);
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
    }

    private void gridButtonClicked(int row, int col){
        //click event
        updateInfo(row,col);
        if (remainMines ==0){
            SharedPreferences.Editor editor = settings.edit();
            String bestScore = ""+config.getRow()+""+config.getCol()+""+config.getMines();
            if (settings.getInt(bestScore,0)>clickNum || settings.getInt(bestScore,0)==0){
                editor.putInt(bestScore,clickNum);
                editor.apply();
                popup("GG! You Win with best score, Scanned fields: "+ clickNum,this,true);
            }
            else{
                popup("GG! You Win, Scanned fields: "+ clickNum,this,true);
            }

        }
    }

    private void setMinesIcons(Button button){
        //set mines image and scale image by Dr.Brain Fraser:
        int buttonWidth = button.getWidth();
        int buttonHeight = button.getHeight();
        Bitmap originImage = BitmapFactory.decodeResource(getResources(), R.drawable.mines_icon);
        Bitmap scaleImage = Bitmap.createScaledBitmap(originImage,buttonWidth,buttonHeight,true);
        Resources resources = getResources();
        button.setBackground(new BitmapDrawable(resources,scaleImage));
    }
    private void lockButtonSize(){
        //lock button size by Dr.Brain Fraser:
        for(int row = 0; row < config.getRow(); row++){
            for (int col = 0; col< config.getCol(); col++){
                Button button = buttons[row][col].getButton();
                int buttonWidth = button.getWidth();
                button.setMinWidth(buttonWidth);
                button.setMaxWidth(buttonWidth);
                int buttonHeight = button.getHeight();
                button.setMaxHeight(buttonHeight);
                button.setMinHeight(buttonHeight);
            }
        }
    }
    private void RandomMines(int mines){
        //generate random mines
        Random rand = new Random();
        int unsetMines = mines;
        int nextRow = 0;
        int nexCol = 0;
        while (unsetMines >0){
            nextRow  = rand.nextInt(config.getRow());
            nexCol = rand.nextInt(config.getCol());
            if (!buttons[nextRow][nexCol].is_mines()){
                buttons[nextRow][nexCol].set_Is_mines(true);
                unsetMines-=1;
            }
        }
    }
    private void setInfo(){
        //setting the status of each button and text
        setTextField();
        for(int row = 0; row < config.getRow(); row++){
            int rowMinesNums = 0;
            for (int col = 0; col< config.getCol(); col++){
                if(buttons[row][col].is_mines()){
                    rowMinesNums += 1;
                }
            }
            for (int col = 0; col< config.getCol(); col++){
                if (buttons[row][col].isClicked()){
                    buttons[row][col].getButton().setText(""+rowMinesNums);
                }
            }
        }
    }
    private void updateInfo(int clickRow, int clickCol){
        //update the button[row][col] info:
        // if it is a mine -> set mines image
        // else display the # of mines on that row

        // each button can only be clicked once
        // lock button size on every click
        lockButtonSize();

        ButtonStatus status = buttons[clickRow][clickCol];
        if(!status.isClicked()){
            if(status.is_mines()){
                Button selectButton = status.getButton();
                setMinesIcons(selectButton);
                selectButton.setClickable(false); //cant click
                status.set_Is_mines(false);
                remainMines -=1;
                clickNum +=1;
                //not scale with text
                setInfo();
            }
            else{
                clickNum +=1;
                status.setClicked(true);
                status.getButton().setClickable(false); //cant click
                setInfo();
            }
        }
    }
    private void popup(String msg, Context context, final boolean is_finished){
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