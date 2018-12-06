package com.example.hillary.umlconfessions.GUI;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.app.ProgressDialog;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hillary.umlconfessions.LogInActivity;
import com.example.hillary.umlconfessions.R;
import com.example.hillary.umlconfessions.SettingsFragment;
import com.example.hillary.umlconfessions.TermsFragment;
import com.example.hillary.umlconfessions.frameworks.Comments;
import com.example.hillary.umlconfessions.frameworks.Confessions;
import com.example.hillary.umlconfessions.frameworks.UserInfo;
import com.example.hillary.umlconfessions.tools.DatabaseUsage;
import com.example.hillary.umlconfessions.tools.Strings_Reference;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.MutableData;


public class ConfessionsActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener  {

    private static final String COMMENT_FINAL = "comment";
    private Confessions confessionsFramework;
    private EditText myModifyTextView;
    private Comments commentsFramework;
    private Drawable dividerDrawable;
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



    //<string name="client_id">691289616795-c3vu5nqe4dhm8jgcqb8qhf7db9m1264c.apps.googleusercontent.com</string>

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

       //setContentView(R.layout.view_post_fragment); //subject to change



        if(savedInstanceState != null){
            commentsFramework = (Comments)savedInstanceState.getSerializable(COMMENT_FINAL);
        }

        Intent intent = getIntent();
        confessionsFramework = (Confessions) intent.getSerializableExtra(Strings_Reference.ADD_CONFESSIONS);


        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);



        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConfessionsActivitySetup()).commit();
        //auth = new FirebaseAuth.AuthStateListener() {


            /*@Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(Main____Activity.this, LogInActivity.class));
                }
            }
        };*/

       // start();
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Setup()).commit();



        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        //drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.menu);
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);
        startNav(view);




       // start();
       // startConfession();
       // startCommentArea();

    }

    private void startCommentArea(){
        RecyclerView recyclerView_For_Comments = (RecyclerView) findViewById(R.id.view_post_recycler); //recyclerview for comments from layout will go here
        recyclerView_For_Comments.setLayoutManager(new LinearLayoutManager(ConfessionsActivity.this));//replace with constraint layout?

        dividerDrawable = getResources().getDrawable(R.drawable.item_divider); //link divider xml to defined drawable divider.
        RecyclerView.ItemDecoration dividerItemDecoration = new ConfessionsActivity.DividerItemDecoration(dividerDrawable);
        recyclerView_For_Comments.addItemDecoration(dividerItemDecoration);

        FirebaseRecyclerAdapter<Comments, CommentsHolder> Adapter_For_Comments = new FirebaseRecyclerAdapter<Comments, CommentsHolder>(
                Comments.class, R.layout.comment_layout, //will find the appropriate resource
        CommentsHolder.class,
            DatabaseUsage.findComment(confessionsFramework.getConfessionID())
        ) {

            @Override
            protected void populateViewHolder(CommentsHolder commentsHolder, Comments framework, int where){
                commentsHolder.setThe_comment(framework.getComment_text());
                commentsHolder.setThe_time(DateUtils.getRelativeTimeSpanString(framework.getTime_of_creation()));
                //also will do the time here

               // Glide.with(ConfessionsActivity.this).load(framework.getUserInfo().getP()).into(commentsHolder.imageView);
            }
        };

        ((RecyclerView) recyclerView_For_Comments).setAdapter(Adapter_For_Comments);
    }

    private void startConfession(){
       // ImageView imageView = (ImageView)findViewById(R.id.);
       // TextView textView = (TextView) findViewById(R.id);
        TextView Time_of_Creation_View = (TextView) findViewById(R.id.time_passed);
       // ImageView imageView2 = (ImageView) findViewById(R.id); // not needed
       // LinearLayout confessionLikeLayout = (LinearLayout) findViewById(R.id.);
       // LinearLayout confessionCommentLayout = (LinearLayout) findViewById(R.id.);
        TextView confession_Like_Count_View = (TextView) findViewById(R.id.vote_size);
        TextView confession_Comment_Count_View = (TextView) findViewById(R.id.comment_number);
        TextView confession_Text_View = (TextView) findViewById(R.id.text_of_the_post);

        
            Time_of_Creation_View.setText(DateUtils.getRelativeTimeSpanString(confessionsFramework.getTime_Of_Creation()));

        confession_Text_View.setText(confessionsFramework.getConfessionText());

        confession_Like_Count_View.setText(String.valueOf(confessionsFramework.getLikeCount()));
        confession_Comment_Count_View.setText(String.valueOf(confessionsFramework.getCommentCount()));




    }

    private void start(){
       myModifyTextView = (EditText) findViewById(R.id.edit_text_for_comments); //comment text to edit
       findViewById(R.id.submit_button_for_comments).setOnClickListener(this); //send button

    }








       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(
                Snackbar.make(view, "You can replace this", Snackbar.LENGTH_LONG).setAction("Action", null).show();

    });*/












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
                //Intent intent=new Intent(ConfessionsActivity.this,Main____Activity.class);
                //startActivityForResult(intent, 2);
                //ConfessionsActivity.this.finish();
                // getCallingActivity()Activity().LogOff();
                //Intent intent = new Intent(this, LogInActivity.class);
                //startActivity(intent);
                //this.finish();*/

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();/*
        Fragment current_fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (current_fragment instanceof SettingsFragment) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Setup()).commit();
            showBackButon(false);
            navigationView.setCheckedItem(R.id.feed);
        }
        else if(current_fragment instanceof TermsFragment) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
        }
        //else if (drawer.isDrawerOpen(GravityCompat.START)) {
         //   drawer.closeDrawer(GravityCompat.START);
        /*}*/ else {
            super.onBackPressed();
        }
    }






    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.submit_button_for_comments: submitComment(); //id = send button
        }
    }

    private void submitComment(){
        final ProgressDialog progressDialog = new ProgressDialog(ConfessionsActivity.this);
        progressDialog.setMessage("Submitting comment");
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        commentsFramework = new Comments();
        final String user_ID = DatabaseUsage.findUser_ID();
        String text_of_comment = myModifyTextView.getText().toString();

        commentsFramework.setID_from_comment(user_ID);
        commentsFramework.setComment_text(text_of_comment);
        commentsFramework.setTime_of_creation(System.currentTimeMillis());
        DatabaseUsage.findUserInfo(DatabaseUsage.findCurrentUserInfo().getEmail().replace(".",",")).addListenerForSingleValueEvent(
                new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot){
                    UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);

                    if(userInfo.getActive() == true){
                        commentsFramework.setUserInfo(userInfo);
                        DatabaseUsage.findComment(confessionsFramework.getConfessionID()).child(user_ID).setValue(commentsFramework);

                        DatabaseUsage.findConfession().child(confessionsFramework.getConfessionID()).child(Strings_Reference.KEY_FOR_COMMENTS_NUMBER).runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData mutableData){
                                long a = (long)mutableData.getValue();
                                mutableData.setValue(a+1);
                                return Transaction.success(mutableData);
                            }

                            @Override
                            public void onComplete(DatabaseError databaseError, boolean myBoolean, DataSnapshot dataSnapshot){
                                progressDialog.dismiss();
                                DatabaseUsage.update(Strings_Reference.KEY_FOR_COMMENTS, user_ID);

                            }
                        });


                    } else {
                        progressDialog.dismiss();

                        Toast.makeText(ConfessionsActivity.this, "Your account has been suspended." +
                        "\nYou cannot use this feature.", Toast.LENGTH_LONG).show();


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError){
                    progressDialog.dismiss();
                }
                });
    }

    public static class CommentsHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView the_time;
        TextView the_comment;

        public CommentsHolder(View itemView){
            super(itemView);
            the_time = (TextView) itemView.findViewById(R.id.comment_time_passed);  //the time
            the_comment = (TextView) itemView.findViewById(R.id.comment_text_here);  //the comment

        }

        public void setThe_time(CharSequence t){the_time.setText(t);}
        public void setThe_comment(String c){the_comment.setText(c);}


    }

        @Override
        protected void onSaveInstanceState(Bundle outState){
        outState.putSerializable(COMMENT_FINAL, commentsFramework);
        super.onSaveInstanceState(outState);
    }

    /* HIllary Ssemakula: This inner class draws the lines below and above each post when the recycler view
starts to replicate the posts.

 */
    public class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private Drawable mDivider; //drawble object is passed in as a parameter

        public DividerItemDecoration(Drawable divider) {
            mDivider = divider;
        }


        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            //if parent(therefore feed) has no posts then don't draw divider

            if (parent.getChildAdapterPosition(view) == 0) {
                return;
            }

            outRect.top = mDivider.getIntrinsicHeight();
        }

        @Override
        public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
            int dividerLeft = parent.getPaddingLeft();  //get left padding
            int dividerRight = parent.getWidth() - parent.getPaddingRight(); //get right padding

            int childCount = parent.getChildCount();
            //for all posts , get the child view and draw a top divider and bottom divider
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int dividerTop = child.getBottom() + params.bottomMargin;
                int dividerBottom = dividerTop + mDivider.getIntrinsicHeight();

                mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
                mDivider.draw(canvas);
            }
        }




    }

}
