package edu.temple.project_post_it.ui.home;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    private int mode;
    TextView titleView;
    String title;
    TextView descriptionView;
    String descritpion;
    CheckBox privacySwitch;
    boolean isPublic;
    Button createPostButton;
    Button recordButton;
    Location location;
    LatLng latLng;
    FirebaseUser currentUser;
    AudioRunner audioRunner;
    dataBaseManagement dataBaseManagement;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getInt(MODE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_audio_creation, container, false);
        dataBaseManagement = new dataBaseManagement();
        title = "Untitled";
        descritpion = "No Description";
        isPublic = true;
        titleView = view.findViewById(R.id.titleEditText);
        descriptionView = view.findViewById(R.id.descriptionEditText);
        privacySwitch = view.findViewById(R.id.privacyCheckBox);
        createPostButton = view.findViewById(R.id.createPostButton);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        audioRunner = new AudioRunner(getActivity());
        if (user_navigation.loc != null){
            latLng = user_navigation.loc;
        }

        recordButton = view.findViewById(R.id.recordButton);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(audioRunner.isRecording())){
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
                }
            }
        });




        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String audioPath = audioRunner.getCurrentAudioPath();
                if (audioPath == null) {
                    Toast.makeText(view.getContext(), "You must record something first!", Toast.LENGTH_SHORT).show();
                } else {
                    String titleTest = titleView.getText().toString();
                    String descriptionTest = (String) descriptionView.getText().toString();
                    if (titleTest.length() > 0) {
                        title = titleTest;
                    }
                    if (descriptionTest.length() > 0) {
                        descritpion = descriptionTest;
                    }
                    if (privacySwitch.isChecked()) {
                        isPublic = false;
                    }

                    String post_id = Calendar.getInstance().getTime().toString() + currentUser.getUid();
                    Post post = new AudioPost(post_id, isPublic, 2, audioPath);
                    post.setTitle(title);
                    post.setText(descritpion);
                    if (latLng != null) {
                        post.setLocation(latLng);
                    }
                    savePost(post);

                }
            }
        });



        return view;
    }

    public void savePost(Post post){
        //This method is where the new post will be saved to the database. This method, when called, will also return the user back to the homepage.
        dataBaseManagement.dataBaseSavePost(FirebaseAuth.getInstance().getUid(), post);
        Toast.makeText(this.getContext(), "Post Saved!", Toast.LENGTH_SHORT).show();

    }

}