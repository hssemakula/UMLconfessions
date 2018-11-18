package com.example.hillary.umlconfessions.GUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.app.ProgressDialog;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hillary.umlconfessions.R;
import com.example.hillary.umlconfessions.frameworks.Comments;
import com.example.hillary.umlconfessions.frameworks.Confessions;
import com.example.hillary.umlconfessions.frameworks.UserInfo;
import com.example.hillary.umlconfessions.tools.DatabaseUsage;
import com.example.hillary.umlconfessions.tools.Strings_Reference;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.MutableData;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;


public class CommentsPageActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String COMMENT_FINAL = "comment";
    private Confessions confessionsFramework;
    private EditText myModifyTextView;
    private Comments commentsFramework;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts_activity); //subject to change



        if(savedInstanceState != null){
            commentsFramework = (Comments)savedInstanceState.getSerializable(COMMENT_FINAL);
        }

        Intent intent = getIntent();
        confessionsFramework = (Confessions) intent.getSerializableExtra(Strings_Reference.ADD_CONFESSIONS);

        start();
        startConfession();
        startCommentArea();

    }

    private void startCommentArea(){
        RecyclerView recyclerView_For_Comments = (RecyclerView) findViewById(R.id.); //recyclerview for comments from layout will go here
        recyclerView_For_Comments.setLayoutManager(new LinearLayoutManager(CommentsPageActivity.this));//replace with constraint layout?

        FirebaseRecyclerAdapter<Comments, CommentsHolder> Adapter_For_Comments = new FirebaseRecyclerAdapter<Comments, CommentsHolder>(
                Comments.class, R.layout., //will find the appropriate resource
        CommentsHolder.class,
            DatabaseUsage.findComment(confessionsFramework.getConfessionID())
        ) {

            @Override
            protected void populateViewHolder(CommentsHolder commentsHolder, Comments framework, int where){
                commentsHolder.setThe_comment(framework.getComment_text());
                commentsHolder.setThe_time(DateUtils.getRelativeTimeSpanString(framework.getTime_of_creation()));
                //also will do the time here

                Glide.with(CommentsPageActivity.this).load(framework.getUserInfo().getP()).into(commentsHolder.imageView);
            }
        };

        ((RecyclerView) recyclerView_For_Comments).setAdapter(Adapter_For_Comments);
    }

    private void startConfession(){
        ImageView imageView = (ImageView)findViewById(R.id.);
        TextView textView = (TextView) findViewById(R.id);
        TextView Time_of_Creation_View = (TextView) findViewById(R.id);
        ImageView imageView2 = (ImageView) findViewById(R.id); // not needed
        LinearLayout confessionLikeLayout = (LinearLayout) findViewById(R.id.);
        LinearLayout confessionCommentLayout = (LinearLayout) findViewById(R.id.);
        TextView confession_Like_Count_View = (TextView) findViewById(R.id.);
        TextView confession_Comment_Count_View = (TextView) findViewById(R.id.);
        TextView confession_Text_View = (TextView) findViewById(R.id.);


        Time_of_Creation_View.setText(DateUtils.getRelativeTimeSpanString(confessionsFramework.getTime_Of_Creation()));
        confession_Text_View.setText(confessionsFramework.getConfessionText());

        confession_Like_Count_View.setText(String.valueOf(confessionsFramework.getLikeCount()));
        confession_Comment_Count_View.setText(String.valueOf(confessionsFramework.getCommentCount()));




    }

    private void start(){
        myModifyTextView = (EditText) findViewById(R.id.); //comment text to edit
        findViewById(R.id.).setOnClickListener(this); //send button

    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.: submitComment(); //id = send button
        }
    }

    private void submitComment(){
        final ProgressDialog progressDialog = new ProgressDialog(CommentsPageActivity.this);
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

                        Toast.makeText(CommentsPageActivity.this, "Your account has been suspended." +
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
            the_time = (TextView) itemView.findViewById(R.id.);  //the time
            the_comment = (TextView) itemView.findViewById(R.id.);  //the comment

        }

        public void setThe_time(CharSequence t){the_time.setText(t);}
        public void setThe_comment(String c){the_comment.setText(c);}


    }

        @Override
        protected void onSaveInstanceState(Bundle outState){
        outState.putSerializable(COMMENT_FINAL, commentsFramework);
        super.onSaveInstanceState(outState);
    }
}
