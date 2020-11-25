package edu.temple.project_post_it.post;

import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;

import java.io.File;

public class ImagePost extends Post{
    String imageFilePath;
    public ImagePost(String Post_ID, boolean Privacy, int type, String imageFilePath) {
        super(Post_ID, Privacy, type);
        this.imageFilePath = imageFilePath;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }
}
