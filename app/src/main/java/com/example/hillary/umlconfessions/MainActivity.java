package com.example.hillary.umlconfessions;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.menu);
        navigationView.setNavigationItemSelectedListener(this);

        //For button to toggle menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.open_navigation_drawer, R.string.close_navigation_drawer);

        drawer.addDrawerListener(toggle);
        toggle.syncState();


        //open posts_fragment fragment on first run
        if(savedInstanceState == null) { //if device has just been rotated dont reload.
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new feedFragment()).commit();
            navigationView.setCheckedItem(R.id.feed);
        }




    }

    //When buttons clicked
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId()){
            case R.id.feed:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new feedFragment()).commit();
                break;


            case R.id.terms:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new termsFragment()).commit();
                break;


            case R.id.settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new settingsFragment()).commit();
                break;


            case R.id.log_out:
                Intent intent = new Intent(this, LogInActivity.class);
                startActivity(intent);
                this.finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //when back button is pressed nd menu is open, app doesn't close.
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



}
