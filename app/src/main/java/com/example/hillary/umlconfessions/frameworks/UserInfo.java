package com.example.hillary.umlconfessions.frameworks;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private String userInfo;
    private String email_address;
    private String p; //probably don't need this
    private String user_ID;
    private Boolean active;

    public UserInfo(){

    }

    public UserInfo(String UserInfo, String email_address, String p, String user_ID, Boolean active){

        this.user_ID = user_ID;
        this.email_address = email_address;
        this.p = p;
        this.user_ID = user_ID;
        this.active = active;

    }

    public String getUserInfo(){
        return userInfo;
    }

    public void setUserInfo(String userInfo){this.userInfo = userInfo;}

    public String getEmail_address(){return email_address;}

    public void setEmail_address(String email_address){this.email_address = email_address;}

    public String getP() {return p;}

    public void setP(){this.p = p;}

    public String getUser_ID(){return user_ID;}

    public void setUser_ID(String user_ID){this.user_ID = user_ID;}

    public Boolean getActive() {return active;}

    public void setActive(Boolean active){this.active = active; }


}
