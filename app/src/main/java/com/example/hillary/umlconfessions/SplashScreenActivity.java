package com.example.hillary.umlconfessions;
/* Splash screen code */
/* Hillary SSemakula
This the code for the splash screen activity. This is the first activity shown when the user opens the app
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.hillary.umlconfessions.GUI.Main____Activity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen); //splash screen layout is loaded here

        setSplashScreen setSplash = new setSplashScreen();
        setSplash.start();
    }

    /*a thread is started to run the splash screen for a second and then it is removed
    and using an intent the activity is changed to Main__Activity to carry out authentication.
     */
    private class setSplashScreen extends Thread {
        public void run() {
            try {
                sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(SplashScreenActivity.this, Main____Activity.class);
            startActivity(intent);
            SplashScreenActivity.this.finish();
        }

    }

}

