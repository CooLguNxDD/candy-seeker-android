package com.cmpt276.a3.Model;

public class boardSpinnerClass {
    private String description;
    private int row;
    private int col;


    //this class is used to assist the board spinner adopter to store extra values
    //it stores:
    // description -> e.g 4x6
    // row -> e.g 5
    // col -> e.g 10


    public boardSpinnerClass(String description, int row, int col) {
        this.description = description;
        this.row = row;
        this.col = col;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
