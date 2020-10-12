package com.cmpt276.a3.Model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
        text.setText(boardSpinner.get(position).getDescription());
        return text;
}

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView text = (TextView) super.getView(position,convertView,parent);
        text.setText(boardSpinner.get(position).getDescription());
        return text;
    }
}
