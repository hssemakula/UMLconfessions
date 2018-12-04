package com.example.hillary.umlconfessions.GUI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import android.support.v4.app.Fragment;

import java.util.Timer;
import java.util.TimerTask;


public class ConfessionsActivitySetup extends Fragment  {

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
    private LinearLayoutManager layoutManager;
    private View rootView;
    private boolean allowClick=true;
    private boolean allowClickToo=true;



    //<string name="client_id">691289616795-c3vu5nqe4dhm8jgcqb8qhf7db9m1264c.apps.googleusercontent.com</string>

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        rootView = inflater.inflate(R.layout.view_post_fragment, container, false);
       //setContentView(R.layout.view_post_fragment); //subject to change



        if(savedInstanceState != null){
            commentsFramework = (Comments)savedInstanceState.getSerializable(COMMENT_FINAL);
        }

        Intent intent = getActivity().getIntent();
        confessionsFramework = (Confessions) intent.getSerializableExtra(Strings_Reference.ADD_CONFESSIONS);

/*
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);*/

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


/*
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        //drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.menu);
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);
        startNav(view);
*/



        start();
        startConfession();
        startCommentArea();


        return rootView;
    }

    private void startCommentArea(){
        RecyclerView recyclerView_For_Comments = (RecyclerView) rootView.findViewById(R.id.view_post_recycler); //recyclerview for comments from layout will go here
        recyclerView_For_Comments.setLayoutManager(new LinearLayoutManager(rootView.getContext()));//ConfessionsActivitySetup.this));//replace with constraint layout?

        dividerDrawable = getResources().getDrawable(R.drawable.item_divider);
        RecyclerView.ItemDecoration dividerItemDecoration = new ConfessionsActivitySetup.DividerItemDecoration(dividerDrawable);
        recyclerView_For_Comments.addItemDecoration(dividerItemDecoration);

        FirebaseRecyclerAdapter<Comments, CommentsHolder> Adapter_For_Comments = new FirebaseRecyclerAdapter<Comments, CommentsHolder>(
                Comments.class, R.layout.comment_layout, //will find the appropriate resource
        CommentsHolder.class,
            DatabaseUsage.findComment(confessionsFramework.getConfessionID())
        ) {
            private int arrows;
            @Override
            protected void populateViewHolder(final CommentsHolder commentsHolder, final Comments framework, int where){
                commentsHolder.setThe_comment(framework.getComment_text());
                commentsHolder.setThe_time(DateUtils.getRelativeTimeSpanString(framework.getTime_of_creation()));
                commentsHolder.setLike_Count_Text(String.valueOf(framework.getLikeCount()));

                //also will do the time here
                commentsHolder.dislike_button.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view){
                        final Button theButton = commentsHolder.like_button;
                        final Button theButtonToo = commentsHolder.dislike_button;
                        theButton.setEnabled(false);
                        theButtonToo.setEnabled(false);

                        if(allowClickToo==true) {
                            allowClickToo = false;

                            // downvote(framework.getConfessionID());


                            if (allowClick == true) {
                                allowClick = false;

                                DatabaseUsage.findConfessionLiked(framework.getID_from_comment()).addListenerForSingleValueEvent(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot d) {



                                        if (d.getValue()== null) {
                                            DatabaseUsage.findComment(confessionsFramework.getConfessionID()).child(framework.getID_from_comment()).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                                                @Override
                                                public Transaction.Result doTransaction(MutableData m) {
                                                    long z = (long) m.getValue();
                                                    m.setValue(z - 1);

                                                    if ((long) m.getValue() < (-4)) {
                                                        DatabaseUsage.findComment(confessionsFramework.getConfessionID()).child(framework.getID_from_comment()).removeValue();
                                                    }
                                                    arrows = -11;
                                                    //cHolder.clicked_dislike.setVisibility(View.VISIBLE);
                                                    //cHolder.clicked_dislike.bringToFront();
                                                    return Transaction.success(m);
                                                }

                                                @Override
                                                public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot dataS) {

                                                    if (arrows == 10) {
                                                        Toast.makeText(getActivity(), "Error 4.", Toast.LENGTH_LONG).show();
                                                        //cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                                                        //cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black);

                                                        //cHolder.clicked_dislike.setVisibility(View.INVISIBLE);
                                                    } else {
                                                        if (arrows == -11) {

/*
                                                        final Handler mHandler = new Handler();

                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run () {
                                                                mHandler.post(new Runnable() {
                                                                    @Override
                                                                    public void run () {*/

                                                            Toast.makeText(getActivity(), "Disliked Comment.", Toast.LENGTH_LONG).show();
                                                            //  cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black);
                                                            // cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_blue_24dp);
                                                            //cHolder.clicked_dislike.setVisibility(View.VISIBLE);
                                                            //cHolder.clicked_dislike.bringToFront();

/*
                                                                    }
                                                                });
                                                            }
                                                        }).start();*/

                                                        }
                                                    }
                                                    arrows = 0;
                                                    DatabaseUsage.findConfessionLiked(framework.getID_from_comment()).setValue("-1");

                                                }

                                            });


                                        } else {
                                            if (d.getValue().toString().equals("-1")) {
                                                DatabaseUsage.findComment(confessionsFramework.getConfessionID()).child(framework.getID_from_comment()).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                                                    @Override
                                                    public Transaction.Result doTransaction(MutableData m) {
                                                        long z = (long) m.getValue();
                                                        m.setValue(z + 1);
                                                        arrows = 10;
                                                        //cHolder.clicked_dislike.setVisibility(View.INVISIBLE);
                                                        return Transaction.success(m);

                                                    }

                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot d) {

                                                        if(arrows==10){
                                                            Toast.makeText(getActivity(),"Removed Dislike.", Toast.LENGTH_LONG).show();

                                                            final Handler mHandler = new Handler();

                                                       /* new Thread(new Runnable() {
                                                            @Override
                                                            public void run () {
                                                                mHandler.post(new Runnable() {
                                                                    @Override
                                                                    public void run () {
                                                                        cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                                                                        cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black);

                                                                    }
                                                                });
                                                            }
                                                        }).start();*/

                                                            //cHolder.clicked_dislike.setVisibility(View.INVISIBLE);
                                                        } else {
                                                            if(arrows==-11){
                                                                Toast.makeText(getActivity(),"Error 3.", Toast.LENGTH_LONG).show();
                                                                // cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black);
                                                                // cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_blue_24dp);
                                                                //cHolder.clicked_dislike.setVisibility(View.VISIBLE);
                                                                //cHolder.clicked_dislike.bringToFront();
                                                            }}

                                                        arrows=0;

                                                        DatabaseUsage.findConfessionLiked(framework.getID_from_comment()).setValue(null);



                                                    }
                                                });

                                            } else {
                                                if (d.getValue().toString().equals("1")){

                                                    DatabaseUsage.findComment(confessionsFramework.getConfessionID()).child(framework.getID_from_comment()).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                                                        @Override
                                                        public Transaction.Result doTransaction(MutableData m) {
                                                            long z = (long) m.getValue();
                                                            m.setValue(z - 2);

                                                            if ((long)m.getValue()<(-4)){
                                                                DatabaseUsage.findComment(confessionsFramework.getConfessionID()).child(framework.getID_from_comment()).removeValue();
                                                            }
                                                            return Transaction.success(m);

                                                        }

                                                        @Override
                                                        public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot d) {
                                                            DatabaseUsage.findConfessionLiked(framework.getID_from_comment()).setValue("-1");
                                                            Toast.makeText(getActivity(),"Disliked Comment.", Toast.LENGTH_LONG).show();
                                                        }
                                                    });



                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }

                                });


                                DatabaseUsage.findConfessionLiked(framework.getID_from_comment()).addListenerForSingleValueEvent(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot d) {



                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }

                                });
                                allowClick = true;


                            }


                            allowClickToo=true;
                        }


                           /* DatabaseUsage.findConfessionDisliked(framework.getConfessionID()).addListenerForSingleValueEvent(new ValueEventListener() {


                                @Override
                                public void onDataChange(DataSnapshot d) {


                                    if (d.getValue() != null) {

                                        cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_blue_24dp);


                                    } /*else {

                            cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);

                        }*/

                        //}
                               /* @Override
                                public void onCancelled (DatabaseError databaseError){

                             /*   }
                            });

                            allowClickToo=true;
                        }*/



                        Timer theTimer = new Timer();
                        theTimer.schedule(new TimerTask(){



                            @Override
                            public void run() {
                                getActivity().runOnUiThread(new Runnable() {


                                    @Override
                                    public void run() {

                                       /* final Handler mHandler = new Handler();

                                        new Thread(new Runnable() {
                                            @Override
                                            public void run () {
                                                mHandler.post(new Runnable() {
                                                    @Override
                                                    public void run () {*/
                                        //   Toast.makeText(getActivity(),"reached here.", Toast.LENGTH_LONG).show();
                                        // cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black);
                                        //cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_blue_24dp);
                                        //cHolder.clicked_dislike.setVisibility(View.VISIBLE);
                                        //cHolder.clicked_dislike.bringToFront();

/*
                                                    }
                                                });
                                            }
                                        }).start();*/

                                        (theButton).setEnabled(true);
                                        (theButtonToo).setEnabled(true);
                                    }
                                });
                            }
                        },430);



                    }
                });

                commentsHolder.like_button.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        final Button theButton = commentsHolder.like_button;
                        final Button theButtonToo = commentsHolder.dislike_button;
                        theButton.setEnabled(false);
                        theButtonToo.setEnabled(false);

                        if(allowClickToo==true) {
                            allowClickToo = false;

                            // downvote(framework.getConfessionID());


                            if (allowClick == true) {
                                allowClick = false;

                                DatabaseUsage.findConfessionLiked(framework.getID_from_comment()).addListenerForSingleValueEvent(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot d) {



                                        if (d.getValue()==null) {

                                            DatabaseUsage.findComment(confessionsFramework.getConfessionID()).child(framework.getID_from_comment()).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                                                @Override
                                                public Transaction.Result doTransaction(MutableData m) {
                                                    long z = (long) m.getValue();
                                                    m.setValue(z + 1);
                                                    if ((long) m.getValue() < (-4)) {
                                                        DatabaseUsage.findComment(confessionsFramework.getConfessionID()).child(framework.getID_from_comment()).removeValue();
                                                    }
                                                    arrows = 999;
                                                    //cHolder.clicked_dislike.setVisibility(View.VISIBLE);
                                                    //cHolder.clicked_dislike.bringToFront();
                                                    return Transaction.success(m);
                                                }

                                                @Override
                                                public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot dataS) {

                                                    if (arrows == 10) {
                                                        Toast.makeText(getActivity(), "Error 4.", Toast.LENGTH_LONG).show();
                                                        //cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                                                        //cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black);

                                                        //cHolder.clicked_dislike.setVisibility(View.INVISIBLE);
                                                    } else {
                                                        if (arrows == 999) {

/*
                                                        final Handler mHandler = new Handler();

                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run () {
                                                                mHandler.post(new Runnable() {
                                                                    @Override
                                                                    public void run () {*/

                                                            Toast.makeText(getActivity(), "Liked Comment.", Toast.LENGTH_LONG).show();
                                                            //  cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black);
                                                            // cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_blue_24dp);
                                                            //cHolder.clicked_dislike.setVisibility(View.VISIBLE);
                                                            //cHolder.clicked_dislike.bringToFront();

/*
                                                                    }
                                                                });
                                                            }
                                                        }).start();*/

                                                        }
                                                    }
                                                    arrows = 0;
                                                    DatabaseUsage.findConfessionLiked(framework.getID_from_comment()).setValue("1");

                                                }

                                            });


                                        } else {
                                            if (d.getValue().toString().equals("-1")) {
                                                DatabaseUsage.findComment(confessionsFramework.getConfessionID()).child(framework.getID_from_comment()).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                                                    @Override
                                                    public Transaction.Result doTransaction(MutableData m) {
                                                        long z = (long) m.getValue();
                                                        m.setValue(z + 2);
                                                        arrows = 888;
                                                        //cHolder.clicked_dislike.setVisibility(View.INVISIBLE);
                                                        return Transaction.success(m);

                                                    }

                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot d) {

                                                        if(arrows==888){
                                                            Toast.makeText(getActivity(),"Liked Comment.", Toast.LENGTH_LONG).show();

                                                            final Handler mHandler = new Handler();

                                                       /* new Thread(new Runnable() {
                                                            @Override
                                                            public void run () {
                                                                mHandler.post(new Runnable() {
                                                                    @Override
                                                                    public void run () {
                                                                        cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                                                                        cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black);

                                                                    }
                                                                });
                                                            }
                                                        }).start();*/

                                                            //cHolder.clicked_dislike.setVisibility(View.INVISIBLE);
                                                        } else {
                                                            if(arrows==-11){
                                                                Toast.makeText(getActivity(),"Error 3.", Toast.LENGTH_LONG).show();
                                                                // cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black);
                                                                // cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_blue_24dp);
                                                                //cHolder.clicked_dislike.setVisibility(View.VISIBLE);
                                                                //cHolder.clicked_dislike.bringToFront();
                                                            }}

                                                        arrows=0;

                                                        DatabaseUsage.findConfessionLiked(framework.getID_from_comment()).setValue("1");



                                                    }
                                                });

                                            } else {
                                                if (d.getValue().toString().equals("1")){

                                                    DatabaseUsage.findComment(confessionsFramework.getConfessionID()).child(framework.getID_from_comment()).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                                                        @Override
                                                        public Transaction.Result doTransaction(MutableData m) {
                                                            long z = (long) m.getValue();
                                                            m.setValue(z - 1);


                                                            if ((long)m.getValue()<(-4)){
                                                                DatabaseUsage.findComment(confessionsFramework.getConfessionID()).child(framework.getID_from_comment()).removeValue();
                                                            }
                                                            return Transaction.success(m);

                                                        }

                                                        @Override
                                                        public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot d) {
                                                            DatabaseUsage.findConfessionLiked(framework.getID_from_comment()).setValue(null);
                                                            Toast.makeText(getActivity(),"Removed Like.", Toast.LENGTH_LONG).show();
                                                        }
                                                    });


                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }

                                });


                                DatabaseUsage.findConfessionLiked(framework.getID_from_comment()).addListenerForSingleValueEvent(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot d) {



                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }

                                });
                                allowClick = true;


                            }


                            allowClickToo=true;
                        }



                        Timer theTimer = new Timer();
                        theTimer.schedule(new TimerTask(){





                            @Override
                            public void run() {
                                getActivity().runOnUiThread(new Runnable() {


                                    @Override
                                    public void run() {





                                        (theButton).setEnabled(true);
                                        (theButtonToo).setEnabled(true);
                                    }
                                });
                            }
                        },430);



                    }
                });
               // Glide.with(ConfessionsActivity.this).load(framework.getUserInfo().getP()).into(commentsHolder.imageView);
            }
        };

        ((RecyclerView) recyclerView_For_Comments).setAdapter(Adapter_For_Comments);
    }

    private void startConfession(){
       // ImageView imageView = (ImageView)findViewById(R.id.);
       // TextView textView = (TextView) findViewById(R.id);
        TextView Time_of_Creation_View = (TextView) rootView.findViewById(R.id.time_passed);
       // ImageView imageView2 = (ImageView) findViewById(R.id); // not needed
       // LinearLayout confessionLikeLayout = (LinearLayout) findViewById(R.id.);
       // LinearLayout confessionCommentLayout = (LinearLayout) findViewById(R.id.);
        TextView confession_Like_Count_View = (TextView) rootView.findViewById(R.id.vote_size);
        TextView confession_Comment_Count_View = (TextView) rootView.findViewById(R.id.comment_number);
        TextView confession_Text_View = (TextView) rootView.findViewById(R.id.text_of_the_post);

        
            Time_of_Creation_View.setText(DateUtils.getRelativeTimeSpanString(confessionsFramework.getTime_Of_Creation()));

        confession_Text_View.setText(confessionsFramework.getConfessionText());

        confession_Like_Count_View.setText(String.valueOf(confessionsFramework.getLikeCount()));
        confession_Comment_Count_View.setText(String.valueOf(confessionsFramework.getCommentCount()));




    }

    private void start(){
       myModifyTextView = (EditText) rootView.findViewById(R.id.edit_text_for_comments); //comment text to edit
       rootView.findViewById(R.id.submit_button_for_comments).setOnClickListener(new View.OnClickListener(){
               @Override
        public void onClick(View view) {
                   switch (view.getId()) {
                       case R.id.submit_button_for_comments:
                           submitComment(); //id = send button
                   }
               }
        }); //send button

       final TextView rootSize = (TextView)rootView.findViewById(R.id.vote_size);



        rootView.findViewById(R.id.down_vote).setOnClickListener(new View.OnClickListener(){
            int arrows;
            @Override
            public void onClick(View view){
                final Button theButton = rootView.findViewById(R.id.up_vote);
                final Button theButtonToo = rootView.findViewById(R.id.down_vote);
                theButton.setEnabled(false);
                theButtonToo.setEnabled(false);


                if(allowClickToo==true) {
                    allowClickToo = false;

                    // downvote(framework.getConfessionID());


                    if (allowClick == true) {
                        allowClick = false;

                        DatabaseUsage.findConfessionLiked(confessionsFramework.getConfessionID()).addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot d) {



                                if (d.getValue()== null) {
                                    DatabaseUsage.findConfession().child(confessionsFramework.getConfessionID()).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData m) {
                                            long z = (long) m.getValue();
                                            m.setValue(z - 1);
                                            rootSize.setText(String.valueOf(z-1));
                                            if ((long) m.getValue() < (-4)) {
                                                DatabaseUsage.findConfession().child(confessionsFramework.getConfessionID()).removeValue();
                                            }
                                            arrows = -11;
                                            //cHolder.clicked_dislike.setVisibility(View.VISIBLE);
                                            //cHolder.clicked_dislike.bringToFront();
                                            return Transaction.success(m);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot dataS) {

                                            if (arrows == 10) {
                                                Toast.makeText(getActivity(), "Error 4.", Toast.LENGTH_LONG).show();
                                                //cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                                                //cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black);

                                                //cHolder.clicked_dislike.setVisibility(View.INVISIBLE);
                                            } else {
                                                if (arrows == -11) {

/*
                                                        final Handler mHandler = new Handler();

                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run () {
                                                                mHandler.post(new Runnable() {
                                                                    @Override
                                                                    public void run () {*/

                                                    Toast.makeText(getActivity(), "Disliked Comment.", Toast.LENGTH_LONG).show();
                                                    //  cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black);
                                                    // cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_blue_24dp);
                                                    //cHolder.clicked_dislike.setVisibility(View.VISIBLE);
                                                    //cHolder.clicked_dislike.bringToFront();

/*
                                                                    }
                                                                });
                                                            }
                                                        }).start();*/

                                                }
                                            }
                                            arrows = 0;
                                            DatabaseUsage.findConfessionLiked(confessionsFramework.getConfessionID()).setValue("-1");

                                        }

                                    });


                                } else {
                                    if (d.getValue().toString().equals("-1")) {
                                        DatabaseUsage.findConfession().child(confessionsFramework.getConfessionID()).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                                            @Override
                                            public Transaction.Result doTransaction(MutableData m) {
                                                long z = (long) m.getValue();
                                                m.setValue(z + 1);
                                                rootSize.setText(String.valueOf(z+1));
                                                arrows = 10;
                                                //cHolder.clicked_dislike.setVisibility(View.INVISIBLE);
                                                return Transaction.success(m);

                                            }

                                            @Override
                                            public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot d) {

                                                if(arrows==10){
                                                    Toast.makeText(getActivity(),"Removed Dislike.", Toast.LENGTH_LONG).show();

                                                    final Handler mHandler = new Handler();

                                                       /* new Thread(new Runnable() {
                                                            @Override
                                                            public void run () {
                                                                mHandler.post(new Runnable() {
                                                                    @Override
                                                                    public void run () {
                                                                        cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                                                                        cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black);

                                                                    }
                                                                });
                                                            }
                                                        }).start();*/

                                                    //cHolder.clicked_dislike.setVisibility(View.INVISIBLE);
                                                } else {
                                                    if(arrows==-11){
                                                        Toast.makeText(getActivity(),"Error 3.", Toast.LENGTH_LONG).show();
                                                        // cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black);
                                                        // cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_blue_24dp);
                                                        //cHolder.clicked_dislike.setVisibility(View.VISIBLE);
                                                        //cHolder.clicked_dislike.bringToFront();
                                                    }}

                                                arrows=0;

                                                DatabaseUsage.findConfessionLiked(confessionsFramework.getConfessionID()).setValue(null);



                                            }
                                        });

                                    } else {
                                        if (d.getValue().toString().equals("1")){

                                            DatabaseUsage.findConfession().child(confessionsFramework.getConfessionID()).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                                                @Override
                                                public Transaction.Result doTransaction(MutableData m) {
                                                    long z = (long) m.getValue();
                                                    m.setValue(z - 2);
                                                    rootSize.setText(String.valueOf(z-2));

                                                    if ((long)m.getValue()<(-4)){
                                                        DatabaseUsage.findConfession().child(confessionsFramework.getConfessionID()).removeValue();
                                                    }
                                                    return Transaction.success(m);

                                                }

                                                @Override
                                                public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot d) {
                                                    DatabaseUsage.findConfessionLiked(confessionsFramework.getConfessionID()).setValue("-1");
                                                    Toast.makeText(getActivity(),"Disliked Comment.", Toast.LENGTH_LONG).show();

                                                }
                                            });



                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });


                        DatabaseUsage.findConfessionLiked(confessionsFramework.getConfessionID()).addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot d) {



                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
                        allowClick = true;


                    }


                    allowClickToo=true;
                }


                           /* DatabaseUsage.findConfessionDisliked(framework.getConfessionID()).addListenerForSingleValueEvent(new ValueEventListener() {


                                @Override
                                public void onDataChange(DataSnapshot d) {


                                    if (d.getValue() != null) {

                                        cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_blue_24dp);


                                    } /*else {

                            cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);

                        }*/

                //}
                               /* @Override
                                public void onCancelled (DatabaseError databaseError){

                             /*   }
                            });

                            allowClickToo=true;
                        }*/



                Timer theTimer = new Timer();
                theTimer.schedule(new TimerTask(){



                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {


                            @Override
                            public void run() {

                                       /* final Handler mHandler = new Handler();

                                        new Thread(new Runnable() {
                                            @Override
                                            public void run () {
                                                mHandler.post(new Runnable() {
                                                    @Override
                                                    public void run () {*/
                                //   Toast.makeText(getActivity(),"reached here.", Toast.LENGTH_LONG).show();
                                // cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black);
                                //cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_blue_24dp);
                                //cHolder.clicked_dislike.setVisibility(View.VISIBLE);
                                //cHolder.clicked_dislike.bringToFront();

/*
                                                    }
                                                });
                                            }
                                        }).start();*/

                                (theButton).setEnabled(true);
                                (theButtonToo).setEnabled(true);
                            }
                        });
                    }
                },430);



            }
        });

        rootView.findViewById(R.id.up_vote).setOnClickListener(new View.OnClickListener(){
           int arrows;
            @Override
            public void onClick(View view) {
                final Button theButton = rootView.findViewById(R.id.up_vote);
                final Button theButtonToo = rootView.findViewById(R.id.down_vote);
                theButton.setEnabled(false);
                theButtonToo.setEnabled(false);

                if(allowClickToo==true) {
                    allowClickToo = false;

                    // downvote(framework.getConfessionID());


                    if (allowClick == true) {
                        allowClick = false;

                        DatabaseUsage.findConfessionLiked(confessionsFramework.getConfessionID()).addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot d) {



                                if (d.getValue()==null) {

                                    DatabaseUsage.findConfession().child(confessionsFramework.getConfessionID()).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData m) {
                                            long z = (long) m.getValue();
                                            m.setValue(z + 1);

                                            rootSize.setText(String.valueOf(z+1));
                                            if ((long) m.getValue() < (-4)) {
                                                DatabaseUsage.findConfession().child(confessionsFramework.getConfessionID()).removeValue();
                                            }
                                            arrows = 999;
                                            //cHolder.clicked_dislike.setVisibility(View.VISIBLE);
                                            //cHolder.clicked_dislike.bringToFront();
                                            return Transaction.success(m);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot dataS) {

                                            if (arrows == 10) {
                                                Toast.makeText(getActivity(), "Error 4.", Toast.LENGTH_LONG).show();
                                                //cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                                                //cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black);

                                                //cHolder.clicked_dislike.setVisibility(View.INVISIBLE);
                                            } else {
                                                if (arrows == 999) {

/*
                                                        final Handler mHandler = new Handler();

                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run () {
                                                                mHandler.post(new Runnable() {
                                                                    @Override
                                                                    public void run () {*/

                                                    Toast.makeText(getActivity(), "Liked Comment.", Toast.LENGTH_LONG).show();

                                                    //  cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black);
                                                    // cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_blue_24dp);
                                                    //cHolder.clicked_dislike.setVisibility(View.VISIBLE);
                                                    //cHolder.clicked_dislike.bringToFront();

/*
                                                              rootSize.setText(d.getValue().toString());      }
                                                                });
                                                            }
                                                        }).start();*/

                                                }
                                            }

                                            arrows = 0;
                                            DatabaseUsage.findConfessionLiked(confessionsFramework.getConfessionID()).setValue("1");

                                        }

                                    });


                                } else {
                                    if (d.getValue().toString().equals("-1")) {
                                        DatabaseUsage.findConfession().child(confessionsFramework.getConfessionID()).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                                            @Override
                                            public Transaction.Result doTransaction(MutableData m) {
                                                long z = (long) m.getValue();
                                                m.setValue(z + 2);
                                                rootSize.setText(String.valueOf(z+2));
                                                arrows = 888;
                                                //cHolder.clicked_dislike.setVisibility(View.INVISIBLE);
                                                return Transaction.success(m);

                                            }

                                            @Override
                                            public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot d) {

                                                if(arrows==888){
                                                    Toast.makeText(getActivity(),"Liked Comment.", Toast.LENGTH_LONG).show();

                                                    final Handler mHandler = new Handler();

                                                       /* new Thread(new Runnable() {
                                                            @Override
                                                            public void run () {
                                                                mHandler.post(new Runnable() {
                                                                    @Override
                                                                    public void run () {
                                                                        cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                                                                        cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black);

                                                                    }
                                                                });
                                                            }
                                                        }).start();*/

                                                    //cHolder.clicked_dislike.setVisibility(View.INVISIBLE);
                                                } else {
                                                    if(arrows==-11){
                                                        Toast.makeText(getActivity(),"Error 3.", Toast.LENGTH_LONG).show();
                                                        // cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black);
                                                        // cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_blue_24dp);
                                                        //cHolder.clicked_dislike.setVisibility(View.VISIBLE);
                                                        //cHolder.clicked_dislike.bringToFront();
                                                    }}

                                                arrows=0;

                                                DatabaseUsage.findConfessionLiked(confessionsFramework.getConfessionID()).setValue("1");



                                            }
                                        });

                                    } else {
                                        if (d.getValue().toString().equals("1")){

                                            DatabaseUsage.findConfession().child(confessionsFramework.getConfessionID()).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                                                @Override
                                                public Transaction.Result doTransaction(MutableData m) {
                                                    long z = (long) m.getValue();
                                                    m.setValue(z - 1);
                                                    rootSize.setText(String.valueOf(z-1));

                                                    if ((long)m.getValue()<(-4)){
                                                        DatabaseUsage.findConfession().child(confessionsFramework.getConfessionID()).removeValue();
                                                    }
                                                    return Transaction.success(m);

                                                }

                                                @Override
                                                public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot d) {
                                                    DatabaseUsage.findConfessionLiked(confessionsFramework.getConfessionID()).setValue(null);
                                                    Toast.makeText(getActivity(),"Removed Like.", Toast.LENGTH_LONG).show();

                                                }
                                            });


                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });


                        DatabaseUsage.findConfessionLiked(confessionsFramework.getConfessionID()).addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot d) {



                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
                        allowClick = true;


                    }


                    allowClickToo=true;
                }



                Timer theTimer = new Timer();
                theTimer.schedule(new TimerTask(){





                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {


                            @Override
                            public void run() {





                                (theButton).setEnabled(true);
                                (theButtonToo).setEnabled(true);
                            }
                        });
                    }
                },430);



            }
        });



    }








       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(
                Snackbar.make(view, "You can replace this", Snackbar.LENGTH_LONG).setAction("Action", null).show();

    });*/










/*

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

    }*/











    private void submitComment(){
        final ProgressDialog progressDialog = new ProgressDialog(rootView.getContext());//ConfessionsActivitySetup.this);
        progressDialog.setMessage("Submitting comment");
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);

        commentsFramework = new Comments();
        final String user_ID = DatabaseUsage.findUser_ID();
        final String text_of_comment = myModifyTextView.getText().toString().trim();

        commentsFramework.setID_from_comment(user_ID);
        commentsFramework.setComment_text(text_of_comment);
        commentsFramework.setTime_of_creation(System.currentTimeMillis());
        DatabaseUsage.findUserInfo(DatabaseUsage.findCurrentUserInfo().getEmail().replace(".",",")).addListenerForSingleValueEvent(
                new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot){
                    UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);

                    if(userInfo.getActive() == true){
                        if(text_of_comment.length()<1) {
                            progressDialog.dismiss();

                        }else {

                            if (text_of_comment.length() > 180) {
                                Toast.makeText(getActivity(), "Character limit reached.  Please enter a message of 180 characters or less", Toast.LENGTH_LONG).show();

                               progressDialog.dismiss();

                            } else {
                                if (text_of_comment.trim().length() > 0){
                                    progressDialog.show();
                                commentsFramework.setUserInfo(userInfo);
                                DatabaseUsage.findComment(confessionsFramework.getConfessionID()).child(user_ID).setValue(commentsFramework);

                                DatabaseUsage.findConfession().child(confessionsFramework.getConfessionID()).child(Strings_Reference.KEY_FOR_COMMENTS_NUMBER).runTransaction(new Transaction.Handler() {
                                    @Override
                                    public Transaction.Result doTransaction(MutableData mutableData) {
                                        long a = (long) mutableData.getValue();
                                        mutableData.setValue(a + 1);
                                        return Transaction.success(mutableData);
                                    }

                                    @Override
                                    public void onComplete(DatabaseError databaseError, boolean myBoolean, DataSnapshot dataSnapshot) {
                                        progressDialog.dismiss();
                                        DatabaseUsage.update(Strings_Reference.KEY_FOR_COMMENTS, user_ID);

                                    }
                                });
                            } else{
                                    progressDialog.dismiss();
                                }
                            }
                        }

                    } else {
                        progressDialog.dismiss();

                        Toast.makeText(rootView.getContext(), "Your account has been suspended." +
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
        Button dislike_button;
        Button like_button;
        TextView like_Count_Text;

        public CommentsHolder(View itemView){
            super(itemView);
            like_button = (Button) itemView.findViewById(R.id.up_vote);
            dislike_button = (Button) itemView.findViewById(R.id.down_vote);
            the_time = (TextView) itemView.findViewById(R.id.comment_time_passed);  //the time
            the_comment = (TextView) itemView.findViewById(R.id.comment_text_here);  //the comment
            like_Count_Text = (TextView) itemView.findViewById(R.id.vote_size);

        }

        public void setThe_time(CharSequence t){the_time.setText(t);}
        public void setThe_comment(String c){the_comment.setText(c);}
        public void setLike_Count_Text(String s){ like_Count_Text.setText(s); }


    }





        @Override
        public void onSaveInstanceState(Bundle outState){
        outState.putSerializable(COMMENT_FINAL, commentsFramework);
        super.onSaveInstanceState(outState);
    }

    public class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private Drawable mDivider;

        public DividerItemDecoration(Drawable divider) {
            mDivider = divider;
        }


        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            if (parent.getChildAdapterPosition(view) == 0) {
                return;
            }

            outRect.top = mDivider.getIntrinsicHeight();
        }

        @Override
        public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
            int dividerLeft = parent.getPaddingLeft();
            int dividerRight = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
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
