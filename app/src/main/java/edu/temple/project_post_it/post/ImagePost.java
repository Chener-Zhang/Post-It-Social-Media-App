package edu.temple.project_post_it.post;

import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;

import java.io.File;

public class ImagePost extends Post{
    Uri imageUri;
    public ImagePost(long Post_ID, Location location, int Privacy, int type, Uri imageUri) {
        super(Post_ID, location, Privacy, type);
        this.imageUri = imageUri;
    }
}
