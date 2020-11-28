package edu.temple.project_post_it.user;

import java.util.ArrayList;

import edu.temple.project_post_it.group.Group;
import edu.temple.project_post_it.post.Post;

public class User {
    public String user_id;
    public String user_groud_id;
    public ArrayList<Post> user_posts;
    public ArrayList<Group> groupList;

    public User() {
        user_posts = new ArrayList<Post>();
    }


    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public void setUser_groud_id(String user_groud_id) {
        this.user_groud_id = user_groud_id;
    }


    public void setUser_posts(ArrayList<Post> user_posts) {
        this.user_posts = user_posts;
    }

    public void setGroupList(ArrayList<Group> groupList){ this.groupList = groupList;}

    public ArrayList<Group> groupList(){ return groupList;}

}
