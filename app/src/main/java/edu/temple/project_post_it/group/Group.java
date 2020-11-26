package edu.temple.project_post_it.group;

import java.util.ArrayList;
import edu.temple.project_post_it.post.Post;
import edu.temple.project_post_it.user.User;

public class Group {
    String admin;
    ArrayList<User> users;
    ArrayList<Post> posts;

    public Group() {

    }

    public void setUserArrayList(ArrayList<User> userArrayList) {
        users = userArrayList;
    }

    public void setPostArrayList(ArrayList<Post> postArrayList) {
        posts = postArrayList;
    }
    public void setAdmin(String user) {
        admin = user;
    }

    public void grouping_post() {
    }

    public void grouping_user() {
    }
    

}
