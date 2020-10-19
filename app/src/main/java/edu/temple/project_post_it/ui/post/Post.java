package edu.temple.project_post_it.ui.post;

import android.location.Location;

public class Post {
    long Post_ID;
    Location location;
    long Group_ID;
    int privacy;
    String Text;
    int type;

    public Post(long Post_ID, Location location, int Privacy, int type){
        this.Post_ID = Post_ID;
        this.location = location;
        this.privacy = Privacy;
        this.type = type;
    }
}
