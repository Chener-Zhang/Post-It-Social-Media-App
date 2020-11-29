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

    public void setPostList(ArrayList<Post> postList) {
        this.postList = postList;
    }

    public void setGroupList(ArrayList<String> groupList) {
        this.groupList = groupList;
    }

    public ArrayList<String> getGroupList() {
        return groupList;
    }

    public String getUserID() {
        return userID;
    }

    public ArrayList<Post> getPostList() {
        return postList;
    }


}
