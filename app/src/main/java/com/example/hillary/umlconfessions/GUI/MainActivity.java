package com.example.hillary.umlconfessions.GUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
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

import com.example.hillary.umlconfessions.R;

public class MainActivity extends OnlineFunctionality implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView imageView;
    private ValueEventListener eventListener;
    private DatabaseReference databaseReference;
    private FirebaseAuth.AuthStateListener auth;
    private TextView nameView;
    private TextView emailView;


    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        auth = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, LogInActivity.class));
                }
            }
        };

        start();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Setup()).commit();






       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(
                Snackbar.make(view, "You can replace this", Snackbar.LENGTH_LONG).setAction("Action", null).show();

    });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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
            imageView = (ImageView) v.findViewById(R.id.);
            nameView = (TextView) v.findViewById(R.id.);
            emailView = (TextView) v.findViewById(R.id.);

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

        @Override
        public void onBackPressed() {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

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

            //@SupressWarnings("StatementWithEmptyBody")
            @Override
            public boolean onNavigationItemSelected(MenuItem item){
                int id = item.getItemId();

                if(id == R.id.nav_camera){

                } else if (id == R.id.nav_gallery){

                } else if (id == R.id.nav_slideshow){

                } else if (id == R.id.nav_manage){

                } else if (id == R.id.nav_send){

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;

            }

    }



}
