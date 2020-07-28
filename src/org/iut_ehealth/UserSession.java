package org.iut_ehealth;

public class UserSession {
    private static UserSession instance = null;

    private String username;
    private String usertype;

    private UserSession(){    }

    //factory method
    public static UserSession getInstance(){
        if(instance==null){
            synchronized (UserSession.class){
                instance = new UserSession();
            }
        }
        return instance;
    }
    public void setUsername(String username){
        this.username = username;
    }

    public void setUsertype(String usertype){
        this.usertype = usertype;
    }
    public String getUsername() {
        return username;
    }

    public String getUsertype() {
        return usertype;
    }

    public void cleanUserSession(){
        username = null;
        usertype = null;
    }

}
