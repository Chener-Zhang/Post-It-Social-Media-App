package edu.temple.project_post_it.group;

import com.google.firebase.database.Exclude;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import edu.temple.project_post_it.CONSTANT;
import edu.temple.project_post_it.post.Post;
import edu.temple.project_post_it.user.User;
import static androidx.constraintlayout.motion.widget.Debug.getLocation;


public class Group {
    String admin;
    public ArrayList<String> users;
    public ArrayList<Post> posts;

    public Group() {

    }

    public void setUserArrayList(ArrayList<String> userArrayList) {
        users = userArrayList;
    }

    public void setPostArrayList(ArrayList<Post> postArrayList) {
        posts = postArrayList;
    }

    public void setAdmin(String user) {
        admin = user;
    }

    public ArrayList<String> getUsers(){ return users;    }

    public ArrayList<Post> getPosts(){return posts;}

    public String getAdmin(){
        return admin;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> serialize = new HashMap<>();
        serialize.put(CONSTANT.ADMIN, getAdmin());
        serialize.put(CONSTANT.USERS, getUsers());
        serialize.put(CONSTANT.POSTS, getPosts());
        return serialize;
    }
    

}
