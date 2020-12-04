package edu.temple.project_post_it.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.temple.project_post_it.R;
import edu.temple.project_post_it.databaseManagement;
import edu.temple.project_post_it.user.User;

public class GroupFragment extends Fragment {

    databaseManagement dataBaseManagement;
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

        dataBaseManagement = new databaseManagement();
        dataBaseManagement.databaseReference = dataBaseManagement.root.getReference("Members/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        dataBaseManagement.databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> groupList;
                try {
                    User user = snapshot.getValue(User.class);
                    groupList = user.getGroupList();
                    groupAdapter = new MyGroupRecyclerViewAdapter(groupList);
                    recyclerView.setAdapter(groupAdapter);
                } catch (Exception e) {
                    System.out.println(e);
                }

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

                if (!addGroup.getText().toString().isEmpty()) {
                    dataBaseManagement.databaseAddGroup(addGroup.getText().toString());
                    groupAdapter.notifyDataSetChanged();
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