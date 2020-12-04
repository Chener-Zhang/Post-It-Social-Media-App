package edu.temple.project_post_it.ui.UserProfile;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import edu.temple.project_post_it.R;
import edu.temple.project_post_it.databaseManagement;

public class UserProfileFragment extends Fragment {


    //Sign out
    public OnDataPass_UserProfileFragment main_activity;
    public Button sign_out_button;

    //Text view elements
    public TextView User_UID;

    //Firebase
    FirebaseUser user;
    databaseManagement dataBase_management;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        main_activity = (OnDataPass_UserProfileFragment) context;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_userprofile, container, false);

        //Setup the database management
        dataBase_management = new databaseManagement();


        //Set the xml element
        sign_out_button = root.findViewById(R.id.logout_button);
        User_UID = root.findViewById(R.id.user_uid);
        Button settingsButton = root.findViewById(R.id.settings_button);

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

    public interface OnDataPass_UserProfileFragment {
        void sign_out();
    }
}
