package com.example.hillary.umlconfessions.GUI;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentsHolder extends RecyclerView.ViewHolder {
    ImageView owner;
    TextView textView_for_time;
    TextView textView_for_comments;

    public CommentsHolder(View view){
        super(view);

    }
}
