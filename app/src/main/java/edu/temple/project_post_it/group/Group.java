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




}
