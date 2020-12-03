package edu.temple.project_post_it.post;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class AudioPost extends Post {
    String audioFilePath;
    String audioFileName;

    public AudioPost(){

    }

    public AudioPost(String Post_ID, boolean Privacy, int type, String audioFilePath, String audioFileName) {
        super(Post_ID, Privacy, type);
        this.audioFilePath = audioFilePath;
        this.audioFileName = audioFileName;
    }

    public String getAudioFilePath() {
        return audioFilePath;
    }

    public void setAudioFilePath(String audioFilePath) {
        this.audioFilePath = audioFilePath;
    }

    public String getAudioFileName() {
        return audioFileName;
    }

    public void setAudioFileName(String audioFileName) {
        this.audioFileName = audioFileName;
    }
}
