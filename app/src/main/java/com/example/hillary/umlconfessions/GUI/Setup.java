package com.example.hillary.umlconfessions.GUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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



public class Setup extends Fragment {
    private View rootView;
    private FirebaseRecyclerAdapter<Confessions, ConfessionsHolder> confessionsAdaper;
    private RecyclerView confessionsRecyclerView;
    private LinearLayoutManager layoutManager;

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


            @Override
            protected void populateViewHolder(ConfessionsHolder cHolder, final Confessions framework, int x){
                cHolder.setContent_Text(framework.getConfessionText());
                cHolder.setTime_Text(DateUtils.getRelativeTimeSpanString(framework.getTime_Of_Creation()));
                cHolder.setComments_Count_Text(String.valueOf(framework.getCommentCount()));
                cHolder.setLike_Count_Text(String.valueOf(framework.getLikeCount()));


                cHolder.like_layout.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view){
                        upvote(framework.getConfessionID());
                    }
                });

                cHolder.comments_layout.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent i = new Intent(getContext(), ConfessionsActivity.class);
                        i.putExtra(Strings_Reference.ADD_CONFESSIONS, framework);
                       startActivity(i);
                    }
                });
            }


        };
    }

    private void upvote(final String s){
        DatabaseUsage.findConfessionLiked(s).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot d){
                if(d.getValue()!=null){
                    DatabaseUsage.findConfession().child(s).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler(){
                        @Override
                        public Transaction.Result doTransaction(MutableData m){
                            long z = (long) m.getValue();
                            m.setValue(z-1);
                            return Transaction.success(m);

                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot d){
                            DatabaseUsage.findConfessionLiked(s).setValue(null);

                        }
                    });

                } else {
                    DatabaseUsage.findConfession().child(s).child(Strings_Reference.KEY_FOR_LIKE_NUMBER).runTransaction(new Transaction.Handler(){
                        @Override
                        public Transaction.Result doTransaction(MutableData m){
                            long z = (long) m.getValue();
                            m.setValue(z+1);
                            return Transaction.success(m);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean myBool, DataSnapshot dataS){
                            DatabaseUsage.findConfessionLiked(s).setValue(true);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError){

            }

        });
    }



    public static class ConfessionsHolder extends RecyclerView.ViewHolder {
        ImageView confessionsImageView;
        TextView Time_Text;
        LinearLayout like_layout;
        LinearLayout comments_layout;
        TextView like_Count_Text;
        TextView comments_Count_Text;
        TextView content_Text;


        public ConfessionsHolder(View itemView) {
            super(itemView);
            //confessionsImageView = (ImageView) itemView.findViewById(R.id.);
            Time_Text = (TextView) itemView.findViewById(R.id.time_ago);
            like_layout = (LinearLayout) itemView.findViewById(R.id.vote_layout);
            comments_layout = (LinearLayout) itemView.findViewById(R.id.comment_layout);
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

}
