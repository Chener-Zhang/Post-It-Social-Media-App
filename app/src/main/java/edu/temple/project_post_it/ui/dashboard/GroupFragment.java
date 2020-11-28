package edu.temple.project_post_it.ui.dashboard;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.temple.project_post_it.R;
import edu.temple.project_post_it.dataBaseManagement;
import edu.temple.project_post_it.group.Group;

public class GroupFragment extends Fragment {

    dataBaseManagement dataBaseManagement;
    MyGroupRecyclerViewAdapter groupAdapter;
    RecyclerView recyclerView;
    Button addButton;
    EditText addGroup;

    public GroupFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);

        recyclerView = view.findViewById(R.id.groupListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dataBaseManagement = new dataBaseManagement();
        dataBaseManagement.databaseReference = dataBaseManagement.root.getReference("Members/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/groupList");
        dataBaseManagement.databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Init the ArrayList of post
                ArrayList<Group> groupList = new ArrayList<Group>();
                //Loop for individual post within the posts
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Group group = dataSnapshot.getValue(Group.class);
                    groupList.add(group);
                }
                //Init the custom adapter
                groupAdapter = new MyGroupRecyclerViewAdapter(groupList);
                recyclerView.setAdapter(groupAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        addButton = view.findViewById(R.id.addButton);
        addGroup = view.findViewById(R.id.addGroup);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Button Clicked", "Clicked button");
                dataBaseManagement.databaseAddGroup(addGroup.getText().toString());
                groupAdapter.notifyDataSetChanged();
            }
        });


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}