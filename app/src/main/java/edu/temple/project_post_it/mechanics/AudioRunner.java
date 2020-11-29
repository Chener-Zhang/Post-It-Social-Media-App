package edu.temple.project_post_it.mechanics;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AudioRunner {
    MediaRecorder recorder;
    boolean isRecording;
    String currentAudioPath;
    String audioFileName;
    Activity activity;

    public AudioRunner(Activity activity){
        this.activity = activity;
        isRecording = false;
    }

    public File createAudioFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        audioFileName = "MP3_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File audio = File.createTempFile(
                audioFileName,
                ".mp3",
                storageDir
        );
        currentAudioPath = audio.getAbsolutePath();
        return audio;
    }

    public void startRecording() {

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(currentAudioPath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.v("ERROR", "prepare() failed");
        }

        recorder.start();
        isRecording = true;
    }

    public void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
        isRecording = true;
    }

    public String getCurrentAudioPath() {
        return currentAudioPath;
    }

    public String getAudioFileName() {
        return audioFileName;
    }

    public boolean isRecording() {
        return isRecording;
    }
}
