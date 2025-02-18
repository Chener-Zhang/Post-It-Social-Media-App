package edu.temple.project_post_it.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import edu.temple.project_post_it.R;
import edu.temple.project_post_it.dataBaseManagement;
import edu.temple.project_post_it.mechanics.AudioRunner;
import edu.temple.project_post_it.post.AudioPost;
import edu.temple.project_post_it.post.Post;
import edu.temple.project_post_it.user_navigation;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AudioCreationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioCreationFragment extends Fragment {

    private static final String MODE = "MODE";
    EditText titleView, descriptionView;
    String title, description;
    CheckBox privacySwitch;
    boolean isPublic;
    Button createPostButton, recordButton;
    LatLng latLng;
    FirebaseUser currentUser;
    AudioRunner audioRunner;
    dataBaseManagement dataBaseManagement;

    String currentAudioPath;
    private StorageReference mStorageRef;
    private StorageReference savedAudioLocation;
    String audioFileName;

    public AudioCreationFragment() {
        // Required empty public constructor
    }
    public static PostCreationFragment newInstance(int mode) {
        PostCreationFragment fragment = new PostCreationFragment();
        Bundle args = new Bundle();
        args.putInt(MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_audio_creation, container, false);
        dataBaseManagement = new dataBaseManagement();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        title = "Untitled";
        description = "No Description";
        isPublic = true;
        titleView = view.findViewById(R.id.titleEditText);
        descriptionView = view.findViewById(R.id.descriptionEditText);
        privacySwitch = view.findViewById(R.id.privacyCheckBox);
        createPostButton = view.findViewById(R.id.editPostButton);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        audioRunner = new AudioRunner(getActivity());
        if (user_navigation.loc != null){
            latLng = user_navigation.loc;
        }

        recordButton = view.findViewById(R.id.recordButton);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 2);
                else {
                    if (!(audioRunner.isRecording())) {
                        recordButton.setText("Stop Recording");
                        try {
                            audioRunner.createAudioFile();
                            audioRunner.startRecording();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        recordButton.setText("Record");
                        audioRunner.stopRecording();
                        final Context context = getContext();
                        Toast.makeText(context, "Audio Recorded!", Toast.LENGTH_SHORT).show();
                        currentAudioPath = audioRunner.getCurrentAudioPath();
                        audioFileName = audioRunner.getAudioFileName();



                    }
                }
            }
        });




        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentAudioPath == null) {
                    Toast.makeText(view.getContext(), "You must record something first!", Toast.LENGTH_SHORT).show();
                } else {
                    String titleTest = titleView.getText().toString();
                    String descriptionTest = descriptionView.getText().toString();
                    if (titleTest.length() > 0) {
                        title = titleTest;
                    }
                    if (descriptionTest.length() > 0) {
                        description = descriptionTest;
                    }
                    if (privacySwitch.isChecked()) {
                        isPublic = false;
                    }

                    String post_id = Calendar.getInstance().getTime().toString() + currentUser.getUid();
                    Post post = new AudioPost(post_id, isPublic, 2, currentAudioPath, audioFileName);
                    post.setTitle(title);
                    post.setText(description);
                    if (latLng != null) {
                        edu.temple.project_post_it.post.LatLng location = new edu.temple.project_post_it.post.LatLng();
                        location.setLatitude(latLng.latitude);
                        location.setLongitude(latLng.longitude);
                        post.setLocation(location);
                    }
                    savePost(post);
                    titleView.getText().clear();
                    descriptionView.getText().clear();

                }
            }
        });


        return view;
    }

    public void savePost(Post post){
        //This method is where the new post will be saved to the database. This method, when called, will also return the user back to the homepage.
        dataBaseManagement.dataBaseSaveInMembers_Uid_UserPosts(FirebaseAuth.getInstance().getUid(), post);
        Toast.makeText(this.getContext(), "Post Saved!", Toast.LENGTH_SHORT).show();

        final Context context = getContext();
        Uri file = Uri.fromFile(new File(currentAudioPath));
        String userID =  FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference usersRef = mStorageRef.child("Users/" + userID);
        final StorageReference saveRef = usersRef.child(audioFileName);
        saveRef.putFile(file).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Error: Audio Not Saved to Firebase Storage!", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context, "Audio Saved to Firebase Storage!", Toast.LENGTH_SHORT).show();
                savedAudioLocation = saveRef;
            }
        });

    }

}