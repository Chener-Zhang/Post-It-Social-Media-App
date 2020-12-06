package edu.temple.project_post_it;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.Calendar;

import edu.temple.project_post_it.post.ImagePost;
import edu.temple.project_post_it.post.Post;
import edu.temple.project_post_it.ui.home.PostCreationFragment;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseAndStorageTests {
    public String testUid;
    Context context;

    @Before
    public void setUp(){
        testUid = "TEST1234TEST";
    }
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("edu.temple.project_post_it", appContext.getPackageName());
    }

    // Asserts that it is possible to create and then receive text posts on the database
    @Test
    public void createAndGetTextPost(){
        final String post_id = "TextPostTest" + testUid;
        final Post post = new Post(post_id, false, 0);
        post.setTitle("JUnit Test");
        post.setText("Text Post Test for JUNIT");
        post.setAnonymous(false);
        edu.temple.project_post_it.post.LatLng latLng = new edu.temple.project_post_it.post.LatLng();
        latLng.setLatitude(39.97);
        latLng.setLongitude(-75.25);
        post.setLocation(latLng);
        FirebaseDatabase root = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = root.getReference().child("Members/" + testUid);
        DatabaseReference childCursor = databaseReference.child("user_posts/" + post.getPost_ID());
        childCursor.setValue(post);

        childCursor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post databasePost = snapshot.getValue(Post.class);
                assertEquals(databasePost.getTitle(), post.getTitle());
                assertEquals(databasePost.getText(), post.getText());
                assertEquals(databasePost.getType(), post.getType());
                assertEquals(databasePost.getLocation().getLatitude(), post.getLocation().getLatitude());
                assertEquals(databasePost.getLocation().getLongitude(), post.getLocation().getLongitude());
                assertEquals(databasePost.getPost_ID(), post.getPost_ID());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Asserts that a copy of picture on this device from storage can be downloaded from FirebaseDatabase
    @Test
    public void getImageFromStorage(){
        final File file = new File("/storage/emulated/0/Android/data/edu.temple.project_post_it/files/Pictures/TestPicture.png");
        final File fileCopy = new File("/storage/emulated/0/Android/data/edu.temple.project_post_it/files/Pictures/PNG_20201205_180011_660514658674833378.png");
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        String userID =  "BBd9N6VJc8fCHFFnpUOHly7ZUPv2";
        StorageReference usersRef = mStorageRef.child("Users/" + userID);
        final StorageReference saveRef = usersRef.child("PNG_20201205_180011_");
        saveRef.getFile(file).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                boolean success = false;
                assertEquals(true, success);
            }
        }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                boolean success = true;
                assertEquals(true, success);
                assertEquals(fileCopy, file);
            }
        });
    }


    @Test
    public void playAudioFromStorage(){
        final File file = new File("/storage/emulated/0/Android/data/edu.temple.project_post_it/files/Pictures/TestAudio.mp3");
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        String userID =  "BBd9N6VJc8fCHFFnpUOHly7ZUPv2";
        StorageReference usersRef = mStorageRef.child("Users/" + userID);
        final StorageReference saveRef = usersRef.child("MP3_20201205_180437_");
        saveRef.getFile(file).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                boolean success = false;
                assertEquals(true, success);
            }
        }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                boolean success = true;
                assertEquals(true, success);
                Uri audioUri = Uri.fromFile(file);
                MediaPlayer mediaPlayer = MediaPlayer.create(InstrumentationRegistry.getInstrumentation().getTargetContext(), audioUri);
                if (mediaPlayer == null){
                    success = false;
                }
                assertEquals(true, success);
            }
        });
    }






}