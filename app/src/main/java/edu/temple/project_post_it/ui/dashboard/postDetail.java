package edu.temple.project_post_it.ui.dashboard;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import edu.temple.project_post_it.R;
import edu.temple.project_post_it.dataBaseManagement;

public class postDetail extends AppCompatActivity {

    String postId;
    String groupId;
    TextView userPostDetail;
    EditText replyEditText;
    Button replyButon;
    dataBaseManagement db;

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
        userPostDetail = findViewById(R.id.userPostDetail);
        replyEditText = findViewById(R.id.replyEditText);
        replyButon = findViewById(R.id.replyButton);

        //Do with the elements
        userPostDetail.setText(postId);
        replyButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = replyEditText.getText().toString();
                db.databaseAddUserReplys(userInput, FirebaseAuth.getInstance().getUid(), postId, groupId);
            }
        });

    }
}
