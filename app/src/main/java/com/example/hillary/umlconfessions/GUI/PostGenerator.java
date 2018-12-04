package com.example.hillary.umlconfessions.GUI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.hillary.umlconfessions.R;
import com.example.hillary.umlconfessions.frameworks.Confessions;
import com.example.hillary.umlconfessions.frameworks.UserInfo;
import com.example.hillary.umlconfessions.tools.DatabaseUsage;
import com.example.hillary.umlconfessions.tools.Strings_Reference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;



public class PostGenerator extends DialogFragment implements View.OnClickListener {
    private static final int ONE = 1;
    private Confessions confessionsFramework;
    private ProgressDialog myProgressDialog;
    private Uri myUri;
    private ImageView showConfession;
    private View rootView;
    private AlertDialog alertDialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        confessionsFramework = new Confessions();
        myProgressDialog = new ProgressDialog(getContext());

        rootView = getActivity().getLayoutInflater().inflate(R.layout.create_post, null);
        //showConfession = (ImageView)rootView.findViewById(R.id.);
        rootView.findViewById(R.id.cancel).setOnClickListener(this);
        rootView.findViewById(R.id.submit).setOnClickListener(this);//the "send" imageview
        builder.setView(rootView);
        alertDialog = builder.create();
        return alertDialog;




    }

    @Override
    public void onClick(View v){ //the listener for sending confession
        switch(v.getId()){

            case R.id.submit: //the send button;
               submitConfession();
                break;

            case R.id.cancel:
                alertDialog.cancel();

                break;
            //in need of another case, will put one here
        }
    }

    private void submitConfession(){
        myProgressDialog.setMessage("Your confession is being submitted...");
        myProgressDialog.setCancelable(false);
        myProgressDialog.setIndeterminate(true);
        myProgressDialog.show();

        Toast.makeText(getActivity(),DatabaseUsage.findCurrentUserInfo().getEmail().replace(".",","), Toast.LENGTH_LONG).show();

        //Toast.makeText(getActivity(),userInfo.getActive().toString(), Toast.LENGTH_LONG).show();

        if(DatabaseUsage.findUserInfo(DatabaseUsage.findCurrentUserInfo().getEmail().replace(".",","))== null){
            Toast.makeText(getActivity(),"null",Toast.LENGTH_LONG).show();
        }

        DatabaseUsage.findUserInfo(DatabaseUsage.findCurrentUserInfo().getEmail().replace(".",",")).addListenerForSingleValueEvent(
                new ValueEventListener(){


                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot){
                        UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);

                        if(userInfo==null){

                            Toast.makeText(getActivity(), "theres the problem", Toast.LENGTH_LONG).show();
                        }




                        if(userInfo.getActive() == true){


                            Toast.makeText(getActivity(),"4", Toast.LENGTH_LONG).show();
                            final String confessionsID = DatabaseUsage.findUser_ID();
                            TextView confessDialog = (TextView) rootView.findViewById(R.id.confession_text);//layout attribute where the user types in his confession


                            String text = confessDialog.getText().toString().trim();


                            if(text.length()<1) {
                                myProgressDialog.dismiss();

                            }else {

                                if(text.length()>180) {
                                    Toast.makeText(getActivity(),"Character limit reached.  Please enter a message of 180 characters or less", Toast.LENGTH_LONG).show();

                                    myProgressDialog.dismiss();

                                }else {

                                    if (text.trim().length() > 0) {
                                        confessionsFramework.setUserInfo(userInfo);
                                        confessionsFramework.setCommentCount(0);
                                        confessionsFramework.setLikeCount(0);
                                        confessionsFramework.setTime_Of_Creation(System.currentTimeMillis());
                                        confessionsFramework.setConfessionID(confessionsID);
                                        confessionsFramework.setConfessionText(text);


                                        if (myUri != null) {
                                            appendConfessionsFeed(confessionsID);
                                        } else {
                                            appendConfessionsFeed(confessionsID);
                                        }
                                    } else {
                                        myProgressDialog.dismiss();
                                    }
                                }
                            }

                        } else {
                            myProgressDialog.dismiss();

                            Toast.makeText(getActivity(),"Your account has been suspended." +
                            "You cannot use this feature.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError){
                        Toast.makeText(getActivity(),databaseError.toString(), Toast.LENGTH_LONG).show();
                        myProgressDialog.dismiss();
                    }

                });

    }

    private void appendConfessionsFeed(String confessionsId){
        DatabaseUsage.findConfession().child(confessionsId).setValue(confessionsFramework);
        DatabaseUsage.findMyConfession().child(confessionsId).setValue(true).addOnCompleteListener(
                getActivity(), new OnCompleteListener<Void>(){
                    @Override
                    public void onComplete(@NonNull Task<Void> task){
                        myProgressDialog.dismiss();
                        dismiss();
                    }
                });

        DatabaseUsage.update(Strings_Reference.KEY_FOR_CONFESSIONS, confessionsId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ONE){
            if(resultCode == -1)
            myUri = data.getData();
            showConfession.setImageURI(myUri);

        }

    }





}
