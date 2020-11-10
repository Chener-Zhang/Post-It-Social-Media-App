package edu.temple.project_post_it.post;

import android.location.Location;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Post {
    long Post_ID;
    Location location;
    long Group_ID;
    int privacy;
    String Text;
    int type;

    public Post(long Post_ID, Location location, int Privacy, int type) {
        this.Post_ID = Post_ID;
        this.location = location;
        this.privacy = Privacy;
        this.type = type;
    }

    public long getPost_ID() {
        return Post_ID;
    }

    public void setPost_ID(long post_ID) {
        Post_ID = post_ID;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getGroup_ID() {
        return Group_ID;
    }

    public void setGroup_ID(long group_ID) {
        Group_ID = group_ID;
    }

    public int getPrivacy() {
        return privacy;
    }

    public void setPrivacy(int privacy) {
        this.privacy = privacy;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        return result;
    }

}
