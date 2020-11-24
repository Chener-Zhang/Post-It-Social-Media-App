package edu.temple.project_post_it.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

import edu.temple.project_post_it.R;
import edu.temple.project_post_it.dataBaseManagement;
import edu.temple.project_post_it.post.Post;
import edu.temple.project_post_it.user_navigation;

public class PostCreationFragment extends Fragment {

    private static final String MODE = "MODE";
    private int mode;
    TextView titleView;
    String title;
    TextView descriptionView;
    String descritpion;
    CheckBox privacySwitch;
    boolean isPublic;
    Button createPostButton;
    Location location;
    LatLng latLng;
    FirebaseUser currentUser;
    dataBaseManagement dataBaseManagement;

    public PostCreationFragment() {
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
        //Init database management
        dataBaseManagement = new dataBaseManagement();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_creation, container, false);
        title = "Untitled";
        descritpion = "No Description";
        isPublic = true;
        titleView = view.findViewById(R.id.titleEditText);
        descriptionView = view.findViewById(R.id.descriptionEditText);
        privacySwitch = view.findViewById(R.id.privacyCheckBox);
        createPostButton = view.findViewById(R.id.createPostButton);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (user_navigation.loc != null) {
            latLng = user_navigation.loc;
        }


        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                //Init the post class

                String post_id = Calendar.getInstance().getTime().toString() + currentUser.getUid();
                Post post = new Post(post_id, isPublic, 0);
                post.setTitle(title);
                post.setText(descritpion);
                post.setPrivacy(isPublic);

                //Set is Public back to true
                isPublic = true;

                if (latLng != null) {
                    post.setLocation(latLng);
                }
                savePost(post);

            }
        });


        return view;
    }

    public void savePost(Post post) {
        //This method is where the new post will be saved to the database. This method, when called, will also return the user back to the homepage.
        this.dataBaseManagement.dataBaseSavePost(FirebaseAuth.getInstance().getUid(), post);
    }

}