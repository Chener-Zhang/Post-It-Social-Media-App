package edu.temple.project_post_it.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;

import edu.temple.project_post_it.DataBase_Management;
import edu.temple.project_post_it.R;
import edu.temple.project_post_it.post.Post;

public class HomeFragment extends Fragment {

    Button textButton;
    Button photoButton;
    Button audioButton;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        textButton = root.findViewById(R.id.textButton);
        textButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_home_to_postCreationFragment, null));

        audioButton = root.findViewById(R.id.audioButton);
        audioButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_home_to_audioCreationFragment, null));

        photoButton = root.findViewById(R.id.photoButton);
        photoButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_home_to_imageCreationFragment, null));

        return root;
    }

//    //Will detele later, for now just for testing
//    public void data_test() {
//        //post test----------------------->
//        Post post = new Post(0, null, 0, 0);
//        DataBase_Management dataBase_management = new DataBase_Management();
//        dataBase_management.add_post(post, FirebaseAuth.getInstance().getUid());
//        //post test----------------------->
//    }
}