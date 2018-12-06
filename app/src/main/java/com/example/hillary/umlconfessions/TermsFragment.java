package com.example.hillary.umlconfessions;
/* Hillary SSemakula
This is the fragment that is replaced with the main activity and it inflates the terms and services layout.

 */

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TermsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.terms_of_service_fragment, container, false); //infalte corresponding layout
    }
}
