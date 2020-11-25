package edu.temple.project_post_it.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.temple.project_post_it.R;

public class HomeFragment extends Fragment {

    Button textButton;
    Button photoButton;
    Button audioButton;
    RecyclerView recyclerView;
    CustomAdapter customAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //create fragments to make appropriate type of post
        textButton = root.findViewById(R.id.textButton);
        textButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_home_to_postCreationFragment, null));

        audioButton = root.findViewById(R.id.audioButton);
        audioButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_home_to_audioCreationFragment, null));

        photoButton = root.findViewById(R.id.photoButton);
        photoButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_home_to_imageCreationFragment, null));

        recyclerView = root.findViewById(R.id.recyle_view_Posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        customAdapter = new CustomAdapter();
        recyclerView.setAdapter(customAdapter);

        return root;
    }
}