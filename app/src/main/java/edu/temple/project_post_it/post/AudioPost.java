package edu.temple.project_post_it.post;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;

public class AudioPost extends Post {
    File audioFile;
    public AudioPost(String Post_ID, boolean Privacy, int type, File audioFile) {
        super(Post_ID, Privacy, type);
        this.audioFile = audioFile;
    }
}
