package edu.temple.project_post_it.post;

import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;

import com.google.firebase.storage.StorageReference;

import java.io.File;

public class ImagePost extends Post{
    String imageFilePath;
    String imageFileName;
    public ImagePost(String Post_ID, boolean Privacy, int type, String imageFilePath, String imageFileName) {
        super(Post_ID, Privacy, type);
        this.imageFilePath = imageFilePath;
        this.imageFileName = imageFileName;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
}
