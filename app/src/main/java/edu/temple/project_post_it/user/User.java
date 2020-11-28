package edu.temple.project_post_it.user;

import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.temple.project_post_it.group.Group;
import edu.temple.project_post_it.post.Post;

public class User {
    public String user_id;
    public ArrayList<Post> user_posts;
    public ArrayList<String> groupList;

    public User() {

    }


    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_posts(ArrayList<Post> user_posts) {
        this.user_posts = user_posts;
    }

    public void setGroupList(ArrayList<String> groupList){ this.groupList = groupList;
    groupList.add("Default");}

    public ArrayList<String> getGroupList(){ return groupList;}

    public String getUser_id(){
        return user_id;
    }

    public ArrayList<Post> getUser_posts(){
        return user_posts;
    }

}
