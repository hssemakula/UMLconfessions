package com.example.hillary.umlconfessions;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
/* Hillary SSemakula
This is the fragment that is replaced with the main activity when the settings button is clicked and it inflates the settings layout.

 */


public class SettingsFragment extends Fragment implements View.OnClickListener {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = (View) inflater.inflate(R.layout.settings_fragment, container, false); //inflate settings layout
        v.findViewById(R.id.terms).setOnClickListener(this); //add click listeners to the two buttons on the settings layout
        v.findViewById(R.id.delete_account).setOnClickListener(this);
        return v;
    }


    //When the terms button is clicked replace the settings fragment with the terms fragment.
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.terms:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TermsFragment()).commit();
                break;
            case R.id.delete_account:
                // delete account
                break;
        }
    }
}

