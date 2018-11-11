package com.example.hillary.umlconfessions.GUI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hillary.umlconfessions.R;
import com.example.hillary.umlconfessions.frameworks.Confessions;

public class Setup extends Fragment {
    private View rootView;
    private FirebaseRecyclerAdapter<Confessions, ConfessionsHolder> confessionsAdaper;
    private RecyclerView confessionsRecyclerView;
    private LinearLayoutManager layoutManager;

    public Setup(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        rootView = inflater.inflate(R.layout., container, false);

    }
}
