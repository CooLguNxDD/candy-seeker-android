package com.cmpt276.a3.Model;

import android.widget.Button;

public class ButtonStatus {
    private Button button;
    private boolean is_mines;
    private boolean clicked;

    public ButtonStatus(Button button, boolean is_mines, boolean clicked) {
        this.button = button;
        this.is_mines = is_mines;
        this.clicked = clicked;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public boolean is_mines() {
        return is_mines;
    }

    public void set_Is_mines(boolean is_mines) {
        this.is_mines = is_mines;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }
}
