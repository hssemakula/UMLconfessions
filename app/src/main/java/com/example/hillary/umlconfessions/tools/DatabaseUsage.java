package com.example.hillary.umlconfessions.tools;


import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseUsage {

    public static DatabaseReference findUserInfo(String email_address) {
        return FirebaseDatabase.getInstance().getReference(Strings_Reference.KEY_FOR_USERINFO).child(email_address);
    }
/*
    public static DatabaseReference findConfession() {
        return FirebaseDatabase.getInstance().getReference(Strings_Reference.KEY_FOR_CONFESSIONS);
    }

    public static DatabaseReference findLike() {
        return FirebaseDatabase.getInstance().getReference(Strings_Reference.KEY_FOR_IS_LIKED);
    }

    public static DatabaseReference findLike(String confessionID) {
    return findLike().child(findCurrentUserInfo().getEmail_adress.replace(".",",")).child(confessionID);
    }

    public static FirebaseUser findCurrentUserInfo(){
        return NULL;//outlook does not work with google so cannot use google accounts
    }

    public static String findUser_ID(){
        String path = FirebaseDatabase.getInstance.getReference.push.toString();
        return path.substring(path.lastIndexOf("/")+1);


    }

    public static void update(){

    }*/
}