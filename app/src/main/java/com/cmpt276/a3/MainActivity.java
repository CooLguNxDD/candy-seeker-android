package com.cmpt276.a3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt276.a3.Model.ButtonStatus;
import com.cmpt276.a3.Model.GameConfig;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private GameConfig config = new GameConfig(4,8,8);
    private ButtonStatus[][] buttons = new ButtonStatus[config.getRow()][config.getCol()];
    private int remainMines = config.getMines();
    private int clickNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createButtons();
        RandomMines(config.getMines());
        setInfo(); //initial game
    }
    private void setTextField(){
        TextView mines = (TextView) findViewById(R.id.mines_text);
        TextView scan = (TextView) findViewById(R.id.scan_text);
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
                button.setBackgroundResource(R.drawable.game_button);
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
        Toast.makeText(this, "Button clicked" + col + ","+ row,
                Toast.LENGTH_SHORT).show();
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
}