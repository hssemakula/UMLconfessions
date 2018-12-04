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
import com.example.hillary.umlconfessions.frameworks.UserInfo;
import com.example.hillary.umlconfessions.tools.DatabaseUsage;
import com.example.hillary.umlconfessions.tools.OnlineFunctionality;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private boolean mToolBarNavigationListenerIsRegistered = false;
    private NavigationView navigationView;



    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.toolbar);

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
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        //drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.menu);
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


    //Back button
    private void showBackButon(boolean enable) {
        if (enable) {
            //You may not want to open the drawer on swipe from the left in this case
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            // Remove hamburger
            toggle.setDrawerIndicatorEnabled(false);
            // Show back button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
            // clicks are disabled i.e. the UP button will not work.
            // We need to add a listener, as in below, so DrawerToggle will forward
            // click events to this listener.
            if (!mToolBarNavigationListenerIsRegistered) {
                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

                mToolBarNavigationListenerIsRegistered = true;
            }

        } else {
            //You must regain the power of swipe for the drawer.
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            // Remove back button
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            // Show hamburger
            toggle.setDrawerIndicatorEnabled(true);
            // Remove the/any drawer toggle listener
            toggle.setToolbarNavigationClickListener(null);
            mToolBarNavigationListenerIsRegistered = false;
        }
    }




    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.feed:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Setup()).commit();
                showBackButon(false);
                break;


            case R.id.terms:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TermsFragment()).commit();
                break;


            case R.id.settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                showBackButon(true);
                break;


            case R.id.log_out:
                LogOff();
                Intent intent = new Intent(this, LogInActivity.class);
                startActivity(intent);
                this.finish();

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
        public void onBackPressed() {
        Fragment current_fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (current_fragment instanceof SettingsFragment) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Setup()).commit();
            showBackButon(false);
            navigationView.setCheckedItem(R.id.feed);
        }
        else if(current_fragment instanceof TermsFragment) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
        }
        else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

            //@SupressWarnings("StatementWithEmptyBody")
           // @Override
            //public boolean onNavigationItemSelected(MenuItem item){

                /*
                int id = item.getItemId();

                if(id == R.id.nav_camera){

                } else if (id == R.id.nav_gallery){

                } else if (id == R.id.nav_slideshow){

                } else if (id == R.id.nav_manage){

                } else if (id == R.id.nav_send){

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                */
              //  return true;

            //}

   // }

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
