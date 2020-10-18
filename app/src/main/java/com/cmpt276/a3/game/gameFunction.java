package com.cmpt276.a3.game;

import android.widget.Button;

import com.cmpt276.a3.Model.ButtonStatus;
import com.cmpt276.a3.Model.GameConfig;

import java.util.Random;

public class gameFunction {
    //game logic
    //set up random minefield
    //perform scan
    //check win condition

    public static ButtonStatus[][] RandomMines(int mines, GameConfig config, ButtonStatus[][] buttons){
        //generate random mines
        Random rand = new Random();
        int unsetMines = mines;
        int nextRow,nexCol;
        while (unsetMines > 0){
            nextRow  = rand.nextInt(config.getRow());
            nexCol = rand.nextInt(config.getCol());
            if (!buttons[nextRow][nexCol].is_mines()){  //not a mine
                buttons[nextRow][nexCol].set_Is_mines(true);
                unsetMines--; //set up a mine
            }
        }
        return buttons;
    }
    public static ButtonStatus[][] setInfo(GameConfig config, ButtonStatus[][] buttons){
        //perform search of mines
        //update the status of buttons

        for(int row = 0; row < config.getRow(); row++){  //scan every row
            for (int col = 0; col< config.getCol(); col++){ //scan every column
                int rowMinesNums = 0;
                int colMinesNums = 0;

                for(int rowCheck = 0; rowCheck< config.getRow(); rowCheck++){ // scan row
                    if(buttons[rowCheck][col].is_mines()){
                        rowMinesNums ++;
                    }
                }
                for(int colCheck = 0; colCheck< config.getCol(); colCheck++){ // scan col
                    if(buttons[row][colCheck].is_mines()){
                        colMinesNums ++;
                    }
                }
                if (buttons[row][col].isClicked()) {
                    buttons[row][col].getButton().setText(String.format("%d", colMinesNums + rowMinesNums));
                }
            }
        }
        return buttons;
    }

    public static boolean checkWin(int remainMines){
        return remainMines == 0;
    }



    // a test merge for finalv1.2 lol it is time to merge!!!!!
}
