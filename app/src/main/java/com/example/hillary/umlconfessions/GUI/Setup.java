package com.example.hillary.umlconfessions.GUI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hillary.umlconfessions.R;
import com.example.hillary.umlconfessions.frameworks.Confessions;
import com.example.hillary.umlconfessions.tools.DatabaseUsage;
import com.example.hillary.umlconfessions.tools.Strings_Reference;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;


import java.util.Timer;
import java.util.TimerTask;


public class Setup extends Fragment {
    private View rootView;
    private FirebaseRecyclerAdapter<Confessions, ConfessionsHolder> confessionsAdaper;
    private RecyclerView confessionsRecyclerView;
    private LinearLayoutManager layoutManager;

    private Drawable dividerDrawable;


    private boolean allowClick=true;
    private boolean allowClickToo=true;

    public Setup(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        rootView = inflater.inflate(R.layout.posts_fragment, container, false);
        FloatingActionButton f = (FloatingActionButton) rootView.findViewById(R.id.fab);






        f.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                PostGenerator generator = new PostGenerator();
                generator.show(getFragmentManager(), null);

        }

    });
        startSetup();


        return rootView;


    }

    private void startSetup(){
        confessionsRecyclerView = (RecyclerView) rootView.findViewById(R.id.posts_recycler);
        confessionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dividerDrawable = getResources().getDrawable(R.drawable.item_divider);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
        confessionsRecyclerView.addItemDecoration(dividerItemDecoration);


        makeAdapter();
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        confessionsRecyclerView.setLayoutManager(layoutManager);
        confessionsRecyclerView.setAdapter(confessionsAdaper);
    }

    private void makeAdapter(){

        confessionsAdaper = new FirebaseRecyclerAdapter<Confessions, ConfessionsHolder>(
                Confessions.class, R.layout.post_layout, ConfessionsHolder.class, DatabaseUsage.findConfession()){





            private int arrows;
            @Override
            protected void populateViewHolder(final ConfessionsHolder cHolder, final Confessions framework, int x){
                cHolder.setContent_Text(framework.getConfessionText());
                cHolder.setTime_Text(DateUtils.getRelativeTimeSpanString(framework.getTime_Of_Creation()));
                cHolder.setComments_Count_Text(String.valueOf(framework.getCommentCount()));
                cHolder.setLike_Count_Text(String.valueOf(framework.getLikeCount()));

               /* DatabaseUsage.findConfessionDisliked(framework.getConfessionID()).addListenerForSingleValueEvent(new ValueEventListener() {


                    @Override
                    public void onDataChange(DataSnapshot d) {


                        if (d.getValue() != null) {

                            arrows = -11;


                        } else {

                            arrows = 10;

                        }

                    }


                    @Override
                    public void onCancelled (DatabaseError databaseError){

                    }
                });*/




                cHolder.dislike_button.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view){
                        final Button theButton = cHolder.like_button;
                        final Button theButtonToo = cHolder.dislike_button;
                        theButton.setEnabled(false);
                        theButtonToo.setEnabled(false);

                        if(allowClickToo==true) {
                            allowClickToo = false;

                            // downvote(framework.getConfessionID());


                            if (allowClick == true) {
                                allowClick = false;

                                DatabaseUsage.findConfessionLiked(framework.getConfessionID()).addListenerForSingleValueEvent(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot d) {


                                        if (d.getValue() != null) {
                                            DatabaseUsage.findConfession().child(framework.getConfessionID()).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                                                @Override
                                                public Transaction.Result doTransaction(MutableData m) {
                                                    long z = (long) m.getValue();
                                                    m.setValue(z - 1);
                                                    return Transaction.success(m);

                                                }

                                                @Override
                                                public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot d) {
                                                    DatabaseUsage.findConfessionLiked(framework.getConfessionID()).setValue(null);

                                                }
                                            });

                                        } else {
                                            DatabaseUsage.findConfession().child(framework.getConfessionID()).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                                                @Override
                                                public Transaction.Result doTransaction(MutableData m) {

                                                    return Transaction.success(m);
                                                }

                                                @Override
                                                public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot dataS) {
                                                    DatabaseUsage.findConfessionLiked(framework.getConfessionID()).setValue(null);
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }

                                });


                                DatabaseUsage.findConfessionDisliked(framework.getConfessionID()).addListenerForSingleValueEvent(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot d) {


                                        if (d.getValue() != null) {
                                            DatabaseUsage.findConfession().child(framework.getConfessionID()).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
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

                                                    DatabaseUsage.findConfessionDisliked(framework.getConfessionID()).setValue(null);



                                                }
                                            });

                                        } else {
                                            DatabaseUsage.findConfession().child(framework.getConfessionID()).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                                                @Override
                                                public Transaction.Result doTransaction(MutableData m) {
                                                    long z = (long) m.getValue();
                                                    m.setValue(z - 1);
                                                    arrows=-11;
                                                    //cHolder.clicked_dislike.setVisibility(View.VISIBLE);
                                                    //cHolder.clicked_dislike.bringToFront();
                                                    return Transaction.success(m);
                                                }

                                                @Override
                                                public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot dataS) {

                                                    if(arrows==10){
                                                        Toast.makeText(getActivity(),"Error 4.", Toast.LENGTH_LONG).show();
                                                        //cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                                                        //cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black);

                                                        //cHolder.clicked_dislike.setVisibility(View.INVISIBLE);
                                                    } else {
                                                    if(arrows==-11){

/*
                                                        final Handler mHandler = new Handler();

                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run () {
                                                                mHandler.post(new Runnable() {
                                                                    @Override
                                                                    public void run () {*/

                                                                        Toast.makeText(getActivity(), "Disliked Post.", Toast.LENGTH_LONG).show();
                                                                      //  cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black);
                                                                       // cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_blue_24dp);
                                                                        //cHolder.clicked_dislike.setVisibility(View.VISIBLE);
                                                                        //cHolder.clicked_dislike.bringToFront();

/*
                                                                    }
                                                                });
                                                            }
                                                        }).start();*/

                                                    }}
                                                    arrows=0;
                                                    DatabaseUsage.findConfessionDisliked(framework.getConfessionID()).setValue(true);

                                                }
                                            });
                                        }
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

                cHolder.like_button.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        final Button theButton = cHolder.like_button;
                        final Button theButtonToo = cHolder.dislike_button;
                        theButton.setEnabled(false);
                        theButtonToo.setEnabled(false);

                        if (allowClickToo == true) {
                            allowClickToo = false;
                            //upvote(framework.getConfessionID());
                        //String s = framework.getConfessionID();
                            if(allowClick == true) {
                                allowClick = false;


                                DatabaseUsage.findConfessionDisliked(framework.getConfessionID()).addListenerForSingleValueEvent(new ValueEventListener() {


                                    @Override
                                    public void onDataChange(DataSnapshot d) {


                                        if (d.getValue() != null) {
                                            DatabaseUsage.findConfession().child(framework.getConfessionID()).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                                                @Override
                                                public Transaction.Result doTransaction(MutableData m) {
                                                    long z = (long) m.getValue();
                                                    m.setValue(z + 1);
                                                    return Transaction.success(m);

                                                }

                                                @Override
                                                public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot d) {
                                                    DatabaseUsage.findConfessionDisliked(framework.getConfessionID()).setValue(null);

                                                }
                                            });

                                        } else {
                                            DatabaseUsage.findConfession().child(framework.getConfessionID()).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                                                @Override
                                                public Transaction.Result doTransaction(MutableData m) {

                                                    return Transaction.success(m);
                                                }

                                                @Override
                                                public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot dataS) {
                                                    DatabaseUsage.findConfessionDisliked(framework.getConfessionID()).setValue(null);
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }

                                });


                                DatabaseUsage.findConfessionLiked(framework.getConfessionID()).addListenerForSingleValueEvent(new ValueEventListener() {


                                    @Override
                                    public void onDataChange(DataSnapshot d) {


                                        if (d.getValue() != null) {
                                            DatabaseUsage.findConfession().child(framework.getConfessionID()).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                                                @Override
                                                public Transaction.Result doTransaction(MutableData m) {
                                                    long z = (long) m.getValue();
                                                    m.setValue(z - 1);
                                                    arrows=0;
                                                 // cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black);
                                                    return Transaction.success(m);

                                                }

                                                @Override
                                                public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot d) {
                                                    DatabaseUsage.findConfessionLiked(framework.getConfessionID()).setValue(null);
                                                    if(arrows==0){
                                                        Toast.makeText(getActivity(),"Removed Like.", Toast.LENGTH_LONG).show();


                                                        final Handler mHandler = new Handler();

                                                        /*new Thread(new Runnable() {
                                                            @Override
                                                            public void run () {
                                                                mHandler.post(new Runnable() {
                                                                    @Override
                                                                    public void run () {
                                                                                cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black);
                                                                                cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                                                                                cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black);
                                                                                cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                                                                                cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black);
                                                                                cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                                                                    }
                                                                });
                                                            }
                                                        }).start();*/








                                                        //cHolder.clicked_dislike.setVisibility(View.INVISIBLE);
                                                    } else {
                                                        if(arrows==1){

                                                                    Toast.makeText(getActivity(), "Error 2.", Toast.LENGTH_LONG).show();
                                                                    //cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_blue);
                                                                    //cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                                                            //cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_blue);
                                                            //cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);

                                                            //cHolder.clicked_dislike.setVisibility(View.VISIBLE);
                                                            //cHolder.clicked_dislike.bringToFront();
                                                        }
                                                    }
                                                    arrows=0;



                                                }
                                            });

                                        } else {
                                            DatabaseUsage.findConfession().child(framework.getConfessionID()).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                                                @Override
                                                public Transaction.Result doTransaction(MutableData m) {
                                                    long z = (long) m.getValue();
                                                    m.setValue(z + 1);
                                                    arrows=1;
                                                  // cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_blue);
                                                    return Transaction.success(m);
                                                }

                                                @Override
                                                public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot dataS) {

                                                    if(arrows==0){
                                                        Toast.makeText(getActivity(),"Error 1.", Toast.LENGTH_LONG).show();
                                                       // cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black);
                                                       // cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);

                                                        //cHolder.clicked_dislike.setVisibility(View.INVISIBLE);
                                                    } else {
                                                    if(arrows==1){



                                                        Toast.makeText(getActivity(),"Liked Post.", Toast.LENGTH_LONG).show();

                                                        final Handler mHandler = new Handler();

                                                       /* new Thread(new Runnable() {
                                                            @Override
                                                            public void run () {
                                                                mHandler.post(new Runnable() {
                                                                    @Override
                                                                    public void run () {
                                                                cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_blue);
                                                                cHolder.dislike_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                                                                    }
                                                                });
                                                            }
                                                        }).start();*/

                                                        //cHolder.clicked_dislike.setVisibility(View.VISIBLE);
                                                        //cHolder.clicked_dislike.bringToFront();
                                                    }
                                               }

                                                    DatabaseUsage.findConfessionLiked(framework.getConfessionID()).setValue(true);
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }

                                });
                                allowClick = true;


                            }


                            /*
                            DatabaseUsage.findConfessionLiked(framework.getConfessionID()).addListenerForSingleValueEvent(new ValueEventListener() {


                                @Override
                                public void onDataChange(DataSnapshot d) {


                                    if (d.getValue() != null) {

                                        cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_blue);


                                    } /*else {

                            cHolder.like_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black);

                        }*/

                             /*   }
                                @Override
                                public void onCancelled (DatabaseError databaseError){

                                }
                            });*/



                            allowClickToo = true;

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

                cHolder.comments_layout.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        Intent i;

                            i = new Intent(getContext(), ConfessionsActivity.class);

                       i.putExtra(Strings_Reference.ADD_CONFESSIONS, framework);
                       startActivity(i);
                    }
                });

                cHolder.content_Text.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent i;

                        i = new Intent(getContext(), ConfessionsActivity.class);

                        i.putExtra(Strings_Reference.ADD_CONFESSIONS, framework);
                        startActivity(i);
                    }
                });



            }


        };
    }

    private void upvote(final String s){

        if(allowClick == true) {
            allowClick = false;


            DatabaseUsage.findConfessionDisliked(s).addListenerForSingleValueEvent(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot d) {


                    if (d.getValue() != null) {
                        DatabaseUsage.findConfession().child(s).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData m) {
                                long z = (long) m.getValue();
                                m.setValue(z + 1);
                                return Transaction.success(m);

                            }

                            @Override
                            public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot d) {
                                DatabaseUsage.findConfessionDisliked(s).setValue(null);

                            }
                        });

                    } else {
                        DatabaseUsage.findConfession().child(s).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData m) {

                                return Transaction.success(m);
                            }

                            @Override
                            public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot dataS) {
                                DatabaseUsage.findConfessionDisliked(s).setValue(null);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });


            DatabaseUsage.findConfessionLiked(s).addListenerForSingleValueEvent(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot d) {


                    if (d.getValue() != null) {
                        DatabaseUsage.findConfession().child(s).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData m) {
                                long z = (long) m.getValue();
                                m.setValue(z - 1);
                                return Transaction.success(m);

                            }

                            @Override
                            public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot d) {
                                DatabaseUsage.findConfessionLiked(s).setValue(null);

                            }
                        });

                    } else {
                        DatabaseUsage.findConfession().child(s).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData m) {
                                long z = (long) m.getValue();
                                m.setValue(z + 1);
                                return Transaction.success(m);
                            }

                            @Override
                            public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot dataS) {
                                DatabaseUsage.findConfessionLiked(s).setValue(true);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
            allowClick = true;
        }

    }

    private void downvote(final String s){
        if(allowClick == true) {
            allowClick = false;

            DatabaseUsage.findConfessionLiked(s).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot d) {


                    if (d.getValue() != null) {
                        DatabaseUsage.findConfession().child(s).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData m) {
                                long z = (long) m.getValue();
                                m.setValue(z - 1);
                                return Transaction.success(m);

                            }

                            @Override
                            public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot d) {
                                DatabaseUsage.findConfessionLiked(s).setValue(null);

                            }
                        });

                    } else {
                        DatabaseUsage.findConfession().child(s).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData m) {

                                return Transaction.success(m);
                            }

                            @Override
                            public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot dataS) {
                                DatabaseUsage.findConfessionLiked(s).setValue(null);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });


            DatabaseUsage.findConfessionDisliked(s).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot d) {


                    if (d.getValue() != null) {
                        DatabaseUsage.findConfession().child(s).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData m) {
                                long z = (long) m.getValue();
                                m.setValue(z + 1);
                                return Transaction.success(m);

                            }

                            @Override
                            public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot d) {
                                DatabaseUsage.findConfessionDisliked(s).setValue(null);

                            }
                        });

                    } else {
                        DatabaseUsage.findConfession().child(s).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData m) {
                                long z = (long) m.getValue();
                                m.setValue(z - 1);
                                return Transaction.success(m);
                            }

                            @Override
                            public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot dataS) {
                                DatabaseUsage.findConfessionDisliked(s).setValue(true);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
            allowClick = true;
        }

    }




    public static class ConfessionsHolder extends RecyclerView.ViewHolder {
        ImageView confessionsImageView;
        TextView Time_Text;
        Button like_button;
        Button dislike_button;
        //ImageView clicked_dislike;
        //ImageView clicked_like;
        LinearLayout comments_layout;
        TextView like_Count_Text;
        TextView comments_Count_Text;
        TextView content_Text;


        public ConfessionsHolder(View itemView) {
            super(itemView);
            //confessionsImageView = (ImageView) itemView.findViewById(R.id.);
            //clicked_dislike = (ImageView) itemView.findViewById(R.id.down_vote_clicked);
            //clicked_like = (ImageView) itemView.findViewById(R.id.);
            Time_Text = (TextView) itemView.findViewById(R.id.time_ago);
            like_button = (Button) itemView.findViewById(R.id.up_vote);
            dislike_button = (Button) itemView.findViewById(R.id.down_vote);
            comments_layout = (LinearLayout) itemView.findViewById(R.id.post);
            like_Count_Text = (TextView) itemView.findViewById(R.id.vote_size);
            comments_Count_Text = (TextView) itemView.findViewById(R.id.comment_number);
            content_Text = (TextView) itemView.findViewById(R.id.post_text);


        }

        public void setContent_Text(String s){
            content_Text.setText(s);

        }

        public void setComments_Count_Text(String s){
            comments_Count_Text.setText(s);

        }

        public void setLike_Count_Text(String s){
            like_Count_Text.setText(s);

        }
        public void setTime_Text(CharSequence s){
            Time_Text.setText(s);

        }
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
