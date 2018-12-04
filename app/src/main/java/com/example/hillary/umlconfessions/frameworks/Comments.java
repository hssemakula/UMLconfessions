package com.example.hillary.umlconfessions.frameworks;

import java.io.Serializable;

public class Comments implements Serializable {
    private UserInfo userInfo;
    private String ID_from_comment;
    private long time_of_creation;
    private String comment_text;
    private long likeCount;

    public Comments(){

    }

    public Comments(UserInfo userInfo, String ID_from_comment, long time_of_creation, String comment_text){
        this.userInfo = userInfo;
        this.ID_from_comment = ID_from_comment;
        this.time_of_creation = time_of_creation;
        this.comment_text = comment_text;
        this.likeCount = likeCount;
    }

    public UserInfo getUserInfo(){

        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo){this.userInfo = userInfo;}

    public String getID_from_comment(){return ID_from_comment;}

    public void setID_from_comment(String ID_from_comment){this.ID_from_comment = ID_from_comment;}

    public long getTime_of_creation() {return time_of_creation;}

    public void setTime_of_creation(long time_of_creation) {this.time_of_creation = time_of_creation;}

    public String getComment_text(){return comment_text;}

    public void setComment_text(String comment_text){this.comment_text = comment_text;}

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount){
        this.likeCount = likeCount;
    }


}
