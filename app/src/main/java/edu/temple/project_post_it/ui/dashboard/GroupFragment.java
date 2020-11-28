package edu.temple.project_post_it.ui.dashboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.temple.project_post_it.R;
import edu.temple.project_post_it.dataBaseManagement;
import edu.temple.project_post_it.group.Group;
import edu.temple.project_post_it.user.User;

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
//                ArrayList<String> groupList = new ArrayList<>();
//                GenericTypeIndicator<ArrayList<String>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<String>>() {                };
//                groupList = snapshot.getValue(genericTypeIndicator);
//                groupAdapter = new MyGroupRecyclerViewAdapter(groupList);
//                recyclerView.setAdapter(groupAdapter);
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
                if(addGroup.getText() != null) {
                    dataBaseManagement.databaseAddGroup(addGroup.getText().toString());
                }
            }
        });


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}