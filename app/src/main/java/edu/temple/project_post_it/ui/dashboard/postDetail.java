package edu.temple.project_post_it.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import edu.temple.project_post_it.R;
import edu.temple.project_post_it.dataBaseManagement;


import java.util.ArrayList;

public class postDetail extends AppCompatActivity {

    String postId;
    String groupId;
    TextView userPostTitle;
    TextView userPostText;
    EditText replyEditText;
    Button replyButon;
    dataBaseManagement db;
    RecyclerView recyclerView;
    PostDetailRecycleViewAdaptor postDetailRecycleViewAdaptor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postdetail
        );
        //get the Intent information
        postId = getIntent().getStringExtra("postID");
        groupId = getIntent().getStringExtra("groupId");
        //Init the database management

        db = new dataBaseManagement();

        //Init the elements
        userPostTitle = findViewById(R.id.userPostDetail);
        userPostText = findViewById(R.id.userPostTitle);
        replyEditText = findViewById(R.id.replyEditText);
        replyButon = findViewById(R.id.replyButton);

        //Implement recyclerView
        recyclerView = findViewById(R.id.replyList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Do with the elements

        db.databaseReference = db.root.getReference("Groups/" + groupId + "/posts/" + postId);
        db.databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String title = String.valueOf(snapshot.child("title").getValue());
                String text = String.valueOf(snapshot.child("text").getValue());
                userPostTitle.setText(title);
                userPostText.setText(text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        replyButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = replyEditText.getText().toString();
                db.databaseAddUserReplys(userInput, FirebaseAuth.getInstance().getUid(), postId, groupId);
            }
        });

        db.databaseReference = db.root.getReference("Groups/" + groupId + "/posts/" + postId + "/replysList");
        db.databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> replys = new ArrayList<String>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String format = dataSnapshot.getKey() + " is REPLY by " + dataSnapshot.getValue();
                    System.out.println(format);
                    replys.add(format);
                }
                postDetailRecycleViewAdaptor = new PostDetailRecycleViewAdaptor(replys);
                recyclerView.setAdapter(postDetailRecycleViewAdaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
