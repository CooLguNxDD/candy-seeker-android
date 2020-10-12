package com.cmpt276.a3.Model;

public class GameConfig {

    private int row = 8;
    private int col = 4;
    private int mines = 8;

    public GameConfig(int row, int col, int mines){
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
