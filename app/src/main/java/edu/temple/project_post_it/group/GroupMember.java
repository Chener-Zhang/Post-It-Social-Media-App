package edu.temple.project_post_it.group;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import edu.temple.project_post_it.post.Post;

public class GroupMember {
    public String member;
    public ArrayList<Post> posts;

    public GroupMember(){

    }

    public void setMember(){
        member = FirebaseAuth.getInstance().getUid();
    }

    public void setPosts(ArrayList<Post> posts){
        this.posts = posts;
    }

    public String getMember(){
        return member;
    }

    public ArrayList<Post> getPosts(){
        return posts;
    }
}
