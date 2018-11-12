/*package com.example.hillary.umlconfessions.GUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.app.ProgressDialog;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hillary.umlconfessions.R;
import com.example.hillary.umlconfessions.frameworks.Comments;
import com.example.hillary.umlconfessions.frameworks.Confessions;
import com.example.hillary.umlconfessions.frameworks.UserInfo;
import com.example.hillary.umlconfessions.tools.DatabaseUsage;
import com.example.hillary.umlconfessions.tools.Strings_Reference;

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
        RecyclerView recyclerView_For_Comments = (RecyclerView) findViewById(R.id.!!!!)//recyclerview for comments from layout will go here
        (RecyclerView) recyclerView_For_Comments.setLayoutManager(new LinearLayoutManager(CommentsPageActivity.this));//replace with constraint layout?

        FirebaseRecyclerAdapter<Comments, CommentsHolder> Adapter_For_Comments = new FirebaseRecylerAdapter(
                Comments.class, R.layout.(comments in the layout), //will find the appropriate resource
        CommentsHolder.class,
            DatabaseUsage.findComment(confessionsFramework.getConfessionID())) {

            @Override
            protected void populateViewHolder(CommentsHolder commentsHolder, Comments framework, int where){
                commentHolder.setComment(framework.getComment_text());
                //also will do the time here

                Glide.(CommentsPageActivity.this).load(framework.getUserInfo.getUrl()).into(commentsHolder.owner);
            }
        };

        ((RecyclerView) recyclerView_For_Comments).setAdapter(Adapter_For_Comments);
    }

    private void startConfession(){

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
        DatabaseUsage.findUserInfo(DatabaseUsage.findCurrentUserInfo.findEmail().replace(".",",")).addListenerForSingleValueEvent(
                new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot){
                    UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);

                    if(userInfo.getActive() == true){
                        commentsFramework.setUserInfo(userInfo);
                        DatabaseUsage.findComment(confessionsFramework.getConfessionID()).child(user_ID).setValue(commentsFramework);

                        DatabaseUsage.findConfession().child(confessionsFramework.getConfessionID()).child(Strings_Reference.KEY_FOR_COMMENTS_NUMBER).runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result run_Transaction(MutableData mutableData){
                                long a = (long)mutableData.getValue();
                                mutableData.setValue(a+1);
                                return Transaction.success(mutableData);
                            }

                            @Override
                            public void onComplete(DatabaseError databaseError, boolean myBoolean, DataSnapshot dataSnapshot){
                                progressDialog.dismiss();
                                DatabaseUsage.updateDatabase(STRINGS.KEY_FOR_COMMENTS, user_ID);

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

        @Override
        Protected void onSaveInstanceState(Bundle outState){
        outState.putSerializable(COMMENT_FINAL, commentsFramework);
        super.onSaveInstanceState(outState);
    }
}
*/