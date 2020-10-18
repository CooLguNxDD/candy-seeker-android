package com.cmpt276.a3.Model;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.cmpt276.a3.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BoardAdapter extends ArrayAdapter<boardSpinnerClass> {
    private ArrayList<boardSpinnerClass> boardSpinner;
    private Context context;
    public BoardAdapter(@NonNull Context context,
                        int resource, ArrayList<boardSpinnerClass> boardSpinnerClass) {
        super(context, resource);
        this.context = context;
        this.boardSpinner = boardSpinnerClass;
    }
    //custom adopter using boardSpinnerClass
    // drop down menu will show descriptions
    // and return descriptions, rows, col as boardSpinnerClass

    @Override
    public int getCount() {
        return boardSpinner.size();
    }

    @Nullable
    @Override
    public boardSpinnerClass getItem(int position) {
        return boardSpinner.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView text = (TextView) super.getView(position,convertView,parent);
        text.setText(boardSpinner.get(position).getDescription()); // set up text
        text.setTextColor(Color.WHITE);
        text.setTextSize(20);
        return text;
}

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView text = (TextView) super.getView(position,convertView,parent); // set up text
        text.setText(boardSpinner.get(position).getDescription());
        text.setTextSize(20);
        return text;
    }
}
