package edu.temple.project_post_it.post;

import android.location.Location;

import java.io.File;

public class AudioPost extends Post {
    File audioFile;
    public AudioPost(long Post_ID, Location location, int Privacy, int type, File audioFile) {
        super(Post_ID, location, Privacy, type);
        this.audioFile = audioFile;
    }
}
