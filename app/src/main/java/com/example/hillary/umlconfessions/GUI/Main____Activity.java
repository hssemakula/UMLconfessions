package com.example.hillary.umlconfessions.GUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;


import com.example.hillary.umlconfessions.LogInActivity;
import com.example.hillary.umlconfessions.SettingsFragment;
import com.example.hillary.umlconfessions.TermsFragment;
import com.example.hillary.umlconfessions.frameworks.UserInfo;
import com.example.hillary.umlconfessions.tools.DatabaseUsage;
import com.example.hillary.umlconfessions.tools.OnlineFunctionality;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import com.example.hillary.umlconfessions.R;

public class Main____Activity extends OnlineFunctionality implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView imageView;
    private ValueEventListener eventListener;
    private DatabaseReference databaseReference;
    private FirebaseAuth.AuthStateListener auth;
    private TextView nameView;
    private TextView emailView;
    private DrawerLayout drawer;


    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        auth = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(Main____Activity.this, LogInActivity.class));
                }
            }
        };

        start();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Setup()).commit();






       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(
                Snackbar.make(view, "You can replace this", Snackbar.LENGTH_LONG).setAction("Action", null).show();

    });*/




        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.menu);
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);
        startNav(view);

    }

        private void start(){
            if (firebaseUser!=null){
                databaseReference = DatabaseUsage.findUserInfo(firebaseUser.getEmail().replace(".",","));
            }
        }



        private void startNav(View v) {
            imageView = (ImageView) v.findViewById(R.id.profpic);
            nameView = (TextView) v.findViewById(R.id.name);
            emailView = (TextView) v.findViewById(R.id.email);

            eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot d) {
                    if (d.getValue() != null) {
                        UserInfo g = d.getValue(UserInfo.class);
                        nameView.setText(g.getUserInfo());
                        emailView.setText(g.getEmail_address());
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }


            };

        }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId()){
            case R.id.feed:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Setup()).commit();
                break;


            case R.id.terms:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TermsFragment()).commit();
                break;


            case R.id.settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                break;


            case R.id.log_out:
                Intent intent = new Intent(this, LogInActivity.class);
                startActivity(intent);
                this.finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
/*

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {

            getMenuInflater().inflate(R.menu.main, menu);

            return true;

        }

        public boolean onOptionsItemSelected(MenuItem menuItem){
        int id = menuItem.getItemId();

        if(id == R.id.action_settings){
            return true;
        }

        return super.onOptionsItemSelected(menuItem);
        }



        @Override
        public boolean OnOptionsCreateMenu(MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);

        }

        */



    //when back button is pressed nd menu is open, app doesn't close.
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    protected void onStart(){
       super.onStart();
       firebaseAuth.addAuthStateListener(auth);
       if(databaseReference!=null){
           databaseReference.addValueEventListener(eventListener);

       }
    }



    @Override
    protected void onStop(){
                super.onStop();
                if(auth!=null){
                    firebaseAuth.removeAuthStateListener(auth);
                }
                if(databaseReference!=null){
                    databaseReference.removeEventListener(eventListener);
                }
    }

}
