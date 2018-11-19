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
import com.example.hillary.umlconfessions.tools.OnlineFunctionality;
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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        confessionsFramework = new Confessions();
        myProgressDialog = new ProgressDialog(getContext());

        rootView = getActivity().getLayoutInflater().inflate(R.layout.temporary_create_post, null); //layout for creating a "confession"
        //showConfession = (ImageView)rootView.findViewById(R.id.); //an imageview in creating a confession....maybe not necessary
        rootView.findViewById(R.id.temp_submit_button).setOnClickListener(this);//the "send" imageview
        //rootView.findViewById(R.id.).serOnClickListener(this); //for selecting an image.  likely not needed
        builder.setView(rootView);
        return builder.create();
    }

    @Override
    public void onClick(View v){ //the listener for sending confession
        switch(v.getId()){
            case R.id.temp_submit_button: //the send button
                submitConfession();
                break;
            //in need of another case, will put one here
        }
    }

    private void submitConfession(){
        myProgressDialog.setMessage("Your confession is being submitted...");
        myProgressDialog.setCancelable(false);
        myProgressDialog.setIndeterminate(true);
        myProgressDialog.show();

        DatabaseUsage.findUserInfo(DatabaseUsage.findCurrentUserInfo().getEmail().replace(".",",")).addListenerForSingleValueEvent(
                new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot){
                        UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);

                        if(userInfo.getActive() == true){
                            final String confessionsID = DatabaseUsage.findUser_ID();
                            TextView confessDialog = (TextView) rootView.findViewById(R.id.temp_edit);//layout attribute where the user types in his confession


                            String text = confessDialog.getText().toString();


                            confessionsFramework.setUserInfo(userInfo);
                            confessionsFramework.setCommentCount(0);
                            confessionsFramework.setLikeCount(0);
                            confessionsFramework.setTime_Of_Creation(System.currentTimeMillis());
                            confessionsFramework.setConfessionID(confessionsID);
                            confessionsFramework.setConfessionText(text);


                            if(myUri!=null){
                                appendConfessionsFeed(confessionsID);
                            } else {
                                appendConfessionsFeed(confessionsID);
                            }

                        } else {
                            myProgressDialog.dismiss();

                            Toast.makeText(getActivity(),"Your account has been suspended." +
                            "You cannot use this feature.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError){
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
