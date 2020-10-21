package edu.temple.project_post_it.post;

import android.graphics.Bitmap;
import android.location.Location;

import java.io.File;

public class ImagePost extends Post{
    Bitmap image;
    public ImagePost(long Post_ID, Location location, int Privacy, int type, Bitmap image) {
        super(Post_ID, location, Privacy, type);
        this.image = image;
    }
}
