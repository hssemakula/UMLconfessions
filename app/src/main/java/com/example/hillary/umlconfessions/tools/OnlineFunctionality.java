package com.example.hillary.umlconfessions.tools;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.example.hillary.umlconfessions.R;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.ResultCallback;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OnlineFunctionality extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    protected FirebaseUser firebaseUser;
    protected GoogleApiClient googleApiClient;
    protected FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private GoogleSignInOptions googleSignInOptions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth != null){
            firebaseUser = firebaseAuth.getCurrentUser();
        }

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();


    }

    protected void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    protected void dismiss(){
        if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    protected void LogOff(){
        firebaseAuth.signOut();;


        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>(){
            @Override
            public void onResult(@NonNull Status status){

            }
        });

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult){

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        dismiss();
    }
}
