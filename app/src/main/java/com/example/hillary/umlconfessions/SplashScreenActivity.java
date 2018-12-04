package com.example.hillary.umlconfessions;
/* Splash screen code */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.hillary.umlconfessions.GUI.Main____Activity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);

        setSplashScreen setSplash = new setSplashScreen();
        setSplash.start();
    }


    private class setSplashScreen extends Thread{
        public void run(){
            try{
                sleep(1000);

            }
            catch(InterruptedException e){
                e.printStackTrace();
            }

            Intent intent = new Intent(SplashScreenActivity.this, Main____Activity.class);
            startActivity(intent);
            SplashScreenActivity.this.finish();
        }

    }

}

