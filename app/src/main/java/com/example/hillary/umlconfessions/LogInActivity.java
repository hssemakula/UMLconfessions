package com.example.hillary.umlconfessions;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.hillary.umlconfessions.frameworks.UserInfo;
import com.example.hillary.umlconfessions.tools.DatabaseUsage;
import com.example.hillary.umlconfessions.tools.OnlineFunctionality;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;

public class LogInActivity extends OnlineFunctionality implements View.OnClickListener{

    private static final int SIGN_IN_CODE = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        findViewById(R.id.login_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.login_button:
                showProgressDialog();
                login();
        }
    }

    private void login(){
        Intent i = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(i, SIGN_IN_CODE);
    }

    @Override
    protected void onActivityResult(int ask, int recieve, Intent i){
        super.onActivityResult(ask,recieve,i);
        if(recieve == Activity.RESULT_OK){
            if(ask == SIGN_IN_CODE){
                GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(i);
                if(googleSignInResult.isSuccess()){
                    GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();
                    Authenticate(googleSignInAccount);


                } else {
                    dismiss();
                }

            } else {
                dismiss();
            }
        } else {
            dismiss();
        }
    }

    private void Authenticate(final GoogleSignInAccount googleSignInAccount){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){
                if(task.isSuccessful()) {
                    UserInfo userInfo = new UserInfo();
                    userInfo.setActive(true);
                    String url = null;

                    userInfo.setEmail_address(googleSignInAccount.getEmail());
                    userInfo.setUserInfo(googleSignInAccount.getDisplayName());
                    userInfo.setUser_ID(firebaseAuth.getCurrentUser().getUid());

                    DatabaseUsage.findUserInfo(googleSignInAccount.getEmail().replace(".", ",")).setValue(userInfo, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            firebaseUser = firebaseAuth.getCurrentUser();
                            finish();
                        }
                    });
                } else {
                    dismiss();
                }
            }
        });
    }
}
