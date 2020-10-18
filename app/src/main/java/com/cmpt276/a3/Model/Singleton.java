package com.cmpt276.a3.Model;

public class Singleton {

    private int row, col, mines;
    public static final String SAVESETTING = "savedSetting" ;

    //size of the board and # of mines

    public Singleton(int row, int col, int mines){
        this.col = col;
        this.row = row;
        this.mines = mines;
    }

    public int getMines() {
        return mines;
    }

    public void setMines(int mines) {
        this.mines = mines;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
