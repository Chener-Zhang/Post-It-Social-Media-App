package edu.temple.project_post_it.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import edu.temple.project_post_it.CONSTANT;
import edu.temple.project_post_it.R;
import edu.temple.project_post_it.dataBaseManagement;
import edu.temple.project_post_it.post.Post;
import edu.temple.project_post_it.user_navigation;

public class PostCreationFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    EditText titleView, descriptionView;
    String title, description;
    CheckBox privacySwitch;
    CheckBox anonymousSwitch;
    boolean isPrivate;
    boolean isAnonymous;
    Button createPostButton;
    LatLng latLng;
    FirebaseUser currentUser;
    dataBaseManagement dataBaseManagement;
    Spinner groupingSelectorSpinner;
    String userGroupSelection = "Default";

    public PostCreationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Init database management
        dataBaseManagement = new dataBaseManagement();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_creation, container, false);

        //Init the xml elements
        title = "Untitled";
        description = "No Description";
        isPrivate = false;
        titleView = view.findViewById(R.id.titleEditText);
        descriptionView = view.findViewById(R.id.descriptionEditText);
        privacySwitch = view.findViewById(R.id.privacyCheckBox);
        createPostButton = view.findViewById(R.id.editPostButton);
        anonymousSwitch = view.findViewById(R.id.anonymousCheckBox);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        groupingSelectorSpinner = view.findViewById(R.id.goupingSelectorSpinner);


        //Set up spinner
        dataBaseManagement.databaseReference = dataBaseManagement.root.getReference("Members/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/groupList");
        dataBaseManagement.databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final ArrayList<String> groups = new ArrayList<String>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    groups.add(dataSnapshot.getKey());
                }
                try {
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, groups);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    groupingSelectorSpinner.setAdapter(dataAdapter);
                    groupingSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            userGroupSelection = groups.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } catch (Exception e) {
                    System.out.println(e.toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        if (user_navigation.loc != null) {
            latLng = user_navigation.loc;
        }


        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleTest = titleView.getText().toString();
                String descriptionTest = descriptionView.getText().toString();

                if (titleTest.length() > 0) {
                    title = titleTest;
                }
                if (descriptionTest.length() > 0) {
                    description = descriptionTest;
                }
                if (privacySwitch.isChecked()) {
                    isPrivate = true;
                }

                //Post field init
                String post_id = Calendar.getInstance().getTime().toString() + currentUser.getUid();
                Post post = new Post(post_id, isPrivate, 0);

                if (anonymousSwitch.isChecked()) {
                    isAnonymous = true;
                    post.setGroupID(CONSTANT.ANONYMOUS);
                } else {
                    post.setGroupID(userGroupSelection);
                    post.setCreatedBy(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                }
                post.setTitle(title);
                post.setText(description);
                post.setPrivacy(isPrivate);
                post.setAnonymous(isAnonymous);


                //Reset the boolean
                isPrivate = false;
                isAnonymous = false;

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
        });


        return view;
    }

    public void savePost(Post post) {
        //This method is where the new post will be saved to the database. This method, when called, will also return the user back to the homepage.
        this.dataBaseManagement.dataBaseSaveInMembers_Uid_UserPosts(FirebaseAuth.getInstance().getUid(), post);
        Toast.makeText(this.getContext(), getString(R.string.post_saved), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}