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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.temple.project_post_it.DataBase_Management;
import edu.temple.project_post_it.R;
import edu.temple.project_post_it.post.Post;
import edu.temple.project_post_it.user.User;

public class UserProfileFragment extends Fragment {


    //Sign out
    public OnDataPass_UserProfileFragment main_activity;
    public Button sign_out_button;

    //Text view elements
    public TextView User_UID;

    //Firebase
    FirebaseUser user;
    DataBase_Management dataBase_management;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        main_activity = (OnDataPass_UserProfileFragment) context;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_userprofile, container, false);

        //Setup the database management
        dataBase_management = new DataBase_Management();


        //Set the xml element
        sign_out_button = root.findViewById(R.id.logout_button);
        User_UID = root.findViewById(R.id.user_uid);

        set_UID();
        sign_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sign out button", "click!");
                main_activity.sign_out();
            }
        });

        return root;
    }

    public void set_UID() {
        //check if the user already exit
        dataBase_management.databaseReference = dataBase_management.rootNode.getReference().child("Members");
        dataBase_management.databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //If the user already exit
                String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if (!snapshot.hasChild(user_id)) {
                    //If the user not exit
                    User user = new User();
                    user.setUser_id(user_id);
                    user.setNumber_posts(0);
                    user.setUser_groud_id("test_group_id");
                    ArrayList<Post> test = new ArrayList<Post>();
                    test.add(null);
                    user.setUser_posts(test);
                    dataBase_management.databaseReference.child(user_id).setValue(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Set the user information
        user = FirebaseAuth.getInstance().getCurrentUser();
        User_UID.setText(user.getUid());

    }

    public interface OnDataPass_UserProfileFragment {
        public void sign_out();
    }
}
