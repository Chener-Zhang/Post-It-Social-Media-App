package edu.temple.project_post_it.post;


public class Post {
    // privacy: true --> private, false --> public
    boolean privacy;
    boolean anonymous;


    LatLng location;
    String Post_ID;
    String Title;
    String Text;
    String groupID = "Default";
    String createdBy;


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    int type;

    public Post() {
    }

    public Post(String Post_ID, boolean Privacy, int type) {
        this.Post_ID = Post_ID;
        this.privacy = Privacy;
        this.type = type;

    }

    public String getTitle() {
        return Title;
    }


    public void setTitle(String title) {
        Title = title;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getPost_ID() {
        return Post_ID;
    }

    public void setPost_ID(String post_ID) {
        Post_ID = post_ID;
    }

    public boolean getPrivacy() {
        return privacy;
    }

    public void setPrivacy(boolean privacy) {
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

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }
}
