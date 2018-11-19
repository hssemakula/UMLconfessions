package com.example.hillary.umlconfessions;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.support.v4.app.DialogFragment;

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
        Toast.makeText(getApplicationContext(),"1.", Toast.LENGTH_LONG).show();
        switch(view.getId()){
            case R.id.login_button:
                showProgressDialog();
                login();
        }
    }

    private void login(){
        Toast.makeText(getApplicationContext(),"2.", Toast.LENGTH_LONG).show();
        Intent i = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(i, SIGN_IN_CODE);
    }

    @Override
    protected void onActivityResult(int ask, int recieve, Intent i){
        Toast.makeText(getApplicationContext(),"3.", Toast.LENGTH_LONG).show();
        super.onActivityResult(ask,recieve,i);
        if(recieve == Activity.RESULT_OK){
            Toast.makeText(getApplicationContext(),"4.", Toast.LENGTH_LONG).show();
            if(ask == SIGN_IN_CODE){
                Toast.makeText(getApplicationContext(),"5.", Toast.LENGTH_LONG).show();
                GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(i);
                if(googleSignInResult.isSuccess()){

                    Toast.makeText(getApplicationContext(),"6.", Toast.LENGTH_LONG).show();
                    GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();
                    Authenticate(googleSignInAccount);


                } else {
                    dismiss();
                }

            } else {
                dismiss();
            }
        } else {
            Toast.makeText(getApplicationContext(),""+recieve, Toast.LENGTH_LONG).show();
            dismiss();
        }
    }


    private void Authenticate(final GoogleSignInAccount googleSignInAccount){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){
                if(task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"It worked.", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(),"It didn't work.", Toast.LENGTH_LONG).show();
                    dismiss();
                }
            }
        });
    }
}
