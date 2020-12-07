package edu.temple.project_post_it.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;

import edu.temple.project_post_it.R;
import edu.temple.project_post_it.dataBaseManagement;
import edu.temple.project_post_it.post.ImagePost;


public class ImagePostViewFragment extends Fragment {

    private static final String POST_ID = "Post_ID";
    private String post_ID;
    private ImagePost currentPost;
    TextView titleView;
    TextView descriptionView;
    ImageView imageView;
    Button editPostButton;
    Button returnButton;
    edu.temple.project_post_it.dataBaseManagement dataBaseManagement;
    DatabaseReference postReference;
    Context context;

    public ImagePostViewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post_ID = getArguments().getString(POST_ID);
        }
        Log.v("Post ID Equals:", post_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_post_view, container, false);
        dataBaseManagement = new dataBaseManagement();
        titleView = view.findViewById(R.id.titleEditText);
        imageView = view.findViewById(R.id.imageView);
        descriptionView = view.findViewById(R.id.descriptionEditText);
        context = this.getContext();
        postReference = FirebaseDatabase.getInstance().getReference("Members/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + "user_posts/" + post_ID);
        postReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentPost = snapshot.getValue(ImagePost.class);
                if (currentPost != null) {
                    titleView.setText(currentPost.getTitle());
                    descriptionView.setText(currentPost.getText());

                    File file = new File(currentPost.getImageFilePath());
                    if (!(file.exists())) {
                        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        StorageReference usersRef = mStorageRef.child("Users/" + userID);
                        final StorageReference saveRef = usersRef.child(currentPost.getImageFileName());
                        saveRef.getFile(file).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.v("ERROR:", "Error getting file from storage!");
                            }
                        }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Log.v("Success:", "Audio obtained from storage!");
                            }
                        });
                    }
                    Picasso.get().load(file).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
        editPostButton = view.findViewById(R.id.editPostButton);
        editPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = titleView.getText().toString();
                String newDescription = descriptionView.getText().toString();
                postReference.child("title").setValue(newTitle);
                postReference.child("text").setValue(newDescription);
                Toast.makeText(context, "Post Edited!", Toast.LENGTH_SHORT).show();
            }
        });


        returnButton = view.findViewById(R.id.returnButton);
        returnButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_imagePostViewFragment_to_navigation_home));
        return view;
    }
}