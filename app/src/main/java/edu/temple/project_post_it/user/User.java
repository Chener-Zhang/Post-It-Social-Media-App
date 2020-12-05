package edu.temple.project_post_it.user;

import java.util.ArrayList;

import edu.temple.project_post_it.post.Post;

public class User {
    public String userID;
    public ArrayList<Post> postList;
    public ArrayList<String> groupList;

    public User() {

    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


    public String getUserID() {
        return userID;
    }


}
