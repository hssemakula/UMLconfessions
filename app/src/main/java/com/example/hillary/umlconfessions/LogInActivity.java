package com.example.hillary.umlconfessions;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LogInActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        getSupportActionBar().hide();
    }

    public void submitLogin(View view){
        //Show button animation
        ((Button)view).setBackgroundColor(R.drawable.login_btn_color_change);
        delay delayer = new delay();
        delayer.start();
        ((Button)view).setBackgroundColor(R.drawable.login_btn);
        //-------------------------------------------------------------


    }

    private class delay extends Thread{
        public void run(){


            try{
                sleep(500);
                return;

            }
            catch(InterruptedException e){
                e.printStackTrace();
            }

        }

    }
}
