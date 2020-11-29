package edu.temple.project_post_it.post;

import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;

import com.google.firebase.storage.StorageReference;

import java.io.File;

public class ImagePost extends Post{
    String imageFilePath;
    StorageReference photoSaveLocation;
    public ImagePost(String Post_ID, boolean Privacy, int type, String imageFilePath, StorageReference photoSaveLocation) {
        super(Post_ID, Privacy, type);
        this.imageFilePath = imageFilePath;
        this.photoSaveLocation = photoSaveLocation;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public StorageReference getPhotoSaveLocation() {
        return photoSaveLocation;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }
}
