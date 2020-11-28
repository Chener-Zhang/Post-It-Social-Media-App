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
    String groupName;
    public ArrayList<String> users;
    public ArrayList<Post> posts;

    public Group() {

    }
    public void setGroupName(String groupName){
        this.groupName = groupName;
    }

    public void setUsers(ArrayList<String> userArrayList) {
        users = userArrayList;
    }

    public void setPosts(ArrayList<Post> postArrayList) {
        posts = postArrayList;
    }


    public ArrayList<String> getUsers(){ return users;    }

    public ArrayList<Post> getPosts(){return posts;}

    public String getGroupName(){
        return groupName;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> serialize = new HashMap<>();
        serialize.put(CONSTANT.USERS, getUsers());
        serialize.put(CONSTANT.POSTS, getPosts());
        return serialize;
    }
    

}
