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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.temple.project_post_it.R;
import edu.temple.project_post_it.dataBaseManagement;

public class HomeFragment extends Fragment {

    Button textButton;
    Button photoButton;
    Button audioButton;
    RecyclerView recyclerView;
    CustomAdapter customAdapter;
    dataBaseManagement dataBaseManagement;

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


        //Implement the recycleView
        recyclerView = root.findViewById(R.id.recyle_view_Posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //Implement call back function:

        dataBaseManagement = new dataBaseManagement();
        dataBaseManagement.databaseReference = dataBaseManagement.root.getReference("Members/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + "user_posts");
        dataBaseManagement.databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                ArrayList<String> list = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    list.add(dataSnapshot.child("post_ID").getValue().toString());
                }
                System.out.println(list.toString());

                customAdapter = new CustomAdapter(list);
                recyclerView.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return root;
    }
}