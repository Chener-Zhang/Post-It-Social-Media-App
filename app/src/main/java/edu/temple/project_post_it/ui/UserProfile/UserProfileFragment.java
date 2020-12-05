package edu.temple.project_post_it.ui.UserProfile;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import edu.temple.project_post_it.R;
import edu.temple.project_post_it.dataBaseManagement;


public class UserProfileFragment extends Fragment {


    private static final int PICK_IMAGE = 100;


    //Upload Profile Image
    public Button uploadButton;
    public ImageView profileImage;
    //Setting
    public Button settingsButton;


    //Sign out
    public OnDataPass_UserProfileFragment main_activity;
    public Button sign_out_button;

    //Text view elements
    public TextView User_UID;

    //Firebase
    FirebaseUser user;
    dataBaseManagement dataBase_management;
    Uri imageUri;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        main_activity = (OnDataPass_UserProfileFragment) context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_userprofile, container, false);

        //Setup the database management
        dataBase_management = new dataBaseManagement();


        //Set the xml element
        sign_out_button = root.findViewById(R.id.logout_button);
        User_UID = root.findViewById(R.id.user_uid);
        settingsButton = root.findViewById(R.id.settings_button);
        uploadButton = root.findViewById(R.id.uploadProfile);
        profileImage = root.findViewById(R.id.profileImage);

        //Set the Uid
        set_UID();

        //Sign out button click listener
        sign_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sign out button", "click!");
                main_activity.sign_out();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                uploadImage();
            }
        });

        settingsButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_userprofile_to_settingsFragment, null));

        return root;
    }

    public void set_UID() {
        //Set the user information
        user = FirebaseAuth.getInstance().getCurrentUser();
        dataBase_management.databaseReference = dataBase_management.root.getReference("Members");
        dataBase_management.databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.hasChild(user.getUid())) {
                    dataBase_management.dataBaseAddUser(user.getUid());
                } else {
                    Log.i("USER", "ALREADY EXIST");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        User_UID.setText(user.getUid());
    }

    public void uploadImage() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode == -1) {
                System.out.println("code goes here");
                imageUri = data.getData();
                profileImage.setImageURI(imageUri);
            }
        }
    }

    public interface OnDataPass_UserProfileFragment {
        void sign_out();
    }


}
