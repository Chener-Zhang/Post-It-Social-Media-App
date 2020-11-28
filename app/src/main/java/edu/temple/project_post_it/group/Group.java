package edu.temple.project_post_it.group;

import com.google.firebase.database.Exclude;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.temple.project_post_it.CONSTANT;
import edu.temple.project_post_it.post.Post;


public class Group {
    String groupName;
    public List<String> users;
    public List<Post> posts;

    public Group() {

    }
    public void setGroupName(String groupName){
        this.groupName = groupName;
    }

    public void setUsers(List<String> userArrayList) {
        users = userArrayList;
    }

    public void setPosts(List<Post> postArrayList) {
        posts = postArrayList;
    }


    public List<String> getUsers(){ return users;    }

    public List<Post> getPosts(){return posts;}

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
