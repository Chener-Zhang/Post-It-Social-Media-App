package edu.temple.project_post_it.user;

import java.util.ArrayList;

import edu.temple.project_post_it.post.Post;

public class User {
    public String user_id;
    public String user_groud_id;
    public int number_posts;
    public ArrayList<Post> user_posts;

    public User() {

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_groud_id() {
        return user_groud_id;
    }

    public void setUser_groud_id(String user_groud_id) {
        this.user_groud_id = user_groud_id;
    }

    public int getNumber_posts() {
        return number_posts;
    }

    public void setNumber_posts(int number_posts) {
        this.number_posts = number_posts;
    }

    public ArrayList<Post> getUser_posts() {
        return user_posts;
    }

    public void setUser_posts(ArrayList<Post> user_posts) {
        this.user_posts = user_posts;
    }

    public boolean delete_post() {
        return false;
    }

    public boolean add_post() {
        return false;
    }

}
