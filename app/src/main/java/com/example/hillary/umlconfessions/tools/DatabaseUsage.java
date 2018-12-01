package com.example.hillary.umlconfessions.tools;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;

public class DatabaseUsage {

    public static DatabaseReference findUserInfo(String email_address) {
        return FirebaseDatabase.getInstance().getReference(Strings_Reference.KEY_FOR_USERINFO).child(email_address);
    }

    public static DatabaseReference findConfession() {
        return FirebaseDatabase.getInstance().getReference(Strings_Reference.KEY_FOR_CONFESSIONS);
    }

    public static DatabaseReference findLike() {
        return FirebaseDatabase.getInstance().getReference(Strings_Reference.KEY_FOR_IS_LIKED);
    }

    public static DatabaseReference findConfessionLiked(String confessionID) {
    return findLike().child(findCurrentUserInfo().getEmail().replace(".",",")).child(confessionID);
    }

    public static DatabaseReference findDislike() {
        return FirebaseDatabase.getInstance().getReference(Strings_Reference.KEY_FOR_IS_DISLIKED);
    }

    public static DatabaseReference findConfessionDisliked(String confessionID) {
        return findDislike().child(findCurrentUserInfo().getEmail().replace(".",",")).child(confessionID);
    }

    public static FirebaseUser findCurrentUserInfo(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static String findUser_ID(){
        String path = FirebaseDatabase.getInstance().getReference().push().toString();
        return path.substring(path.lastIndexOf("/")+1);


    }

    public static DatabaseReference findMyConfession(){
        return FirebaseDatabase.getInstance().getReference(Strings_Reference.MY_CONFESSIONS).child(findCurrentUserInfo().getEmail().replace(".",","));

    }

    public static DatabaseReference findComment(String commentId){
     return FirebaseDatabase.getInstance().getReference(Strings_Reference.KEY_FOR_COMMENTS).child(commentId);
    }

    public static DatabaseReference findMyRecord(){
        return FirebaseDatabase.getInstance().getReference(Strings_Reference.USERINFO_RECORD).child(findCurrentUserInfo().getEmail().replace(".",","));

    }


    public static void update(String s, final String ID){
    findMyRecord().child(s).runTransaction(new Transaction.Handler(){

        @Override
        public Transaction.Result doTransaction(MutableData mutableData){
            ArrayList<String> records;
            if(mutableData.getValue() == null){
                records = new ArrayList<String>(1);
                records.add(ID);
            }else{
                records = (ArrayList<String>) mutableData.getValue();
                records.add(ID);
            }

            mutableData.setValue(records);
            return Transaction.success(mutableData);

        }

        @Override
        public void onComplete(DatabaseError databaseError, boolean bool, DataSnapshot dataSnapshot){

        }

        }
    );
    }
}