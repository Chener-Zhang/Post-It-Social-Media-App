package edu.temple.project_post_it.post;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;

public class AudioPost extends Post {
    String audioFilePath;

    public AudioPost(String Post_ID, boolean Privacy, int type, String audioFilePath) {
        super(Post_ID, Privacy, type);
        this.audioFilePath = audioFilePath;
    }

    public String getAudioFilePath() {
        return audioFilePath;
    }

    public void setAudioFilePath(String audioFilePath) {
        this.audioFilePath = audioFilePath;
    }
}
