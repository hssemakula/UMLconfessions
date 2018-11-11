package com.example.hillary.umlconfessions.frameworks;

public class Confessions {
    private UserInfo userInfo;
    private String confessionText;
    private String url;
    private String confessionID;
    private long likeCount;
    private long commentCount;
    private long time_Of_Creation;

    public Confessions(){

    }

    public Confessions(UserInfo userInfo, String confessionText, String url, String confessionID, long likeCount, long commentCount, long time_Of_Creation) {

        this.userInfo = userInfo;
        this.confessionText = confessionText;
        this.url = url;
        this.confessionID = confessionID;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.time_Of_Creation = time_Of_Creation;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo){
        this.userInfo = userInfo;
    }

    public String getConfessionText(){
        return confessionText;
    }

    public void setConfessionText(String confessionText){
        this.confessionText = confessionText;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getConfessionID() {
        return confessionID;
    }

    public void setConfessionID(String confessionID) {
        this.confessionID = confessionID;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(){
     this.likeCount = likeCount;
    }

    public long getCommentCount(){
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    public long getTime_Of_Creation(){
        return time_Of_Creation;
    }

    public void setTime_Of_Creation(long time_Of_Creation) {
        this.time_Of_Creation = time_Of_Creation;
    }
}
