package edu.temple.project_post_it.user;

import com.google.firebase.database.Exclude;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.temple.project_post_it.CONSTANT;
import edu.temple.project_post_it.group.Group;
import edu.temple.project_post_it.post.Post;

import static edu.temple.project_post_it.CONSTANT.GROUP_ID;
import static edu.temple.project_post_it.CONSTANT.LOCATION;
import static edu.temple.project_post_it.CONSTANT.POST_ID;
import static edu.temple.project_post_it.CONSTANT.PRIVACY;
import static edu.temple.project_post_it.CONSTANT.TEXT;
import static edu.temple.project_post_it.CONSTANT.TYPE;

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

    public void setGroupList(ArrayList<String> groupList){ this.groupList = groupList;}

    public ArrayList<String> getGroupList(){ return groupList;}

    public String getUserID(){
        return userID;
    }

    public ArrayList<Post> getPostList(){
        return postList;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(CONSTANT.GROUPLIST, getGroupList());
        result.put(CONSTANT.POSTLIST, getPostList());
        result.put(CONSTANT.USERID, getUserID());
        return result;
    }

}
