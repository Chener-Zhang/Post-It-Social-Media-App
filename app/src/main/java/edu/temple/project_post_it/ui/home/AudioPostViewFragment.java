package edu.temple.project_post_it.ui.home;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.File;
import java.io.IOException;

import edu.temple.project_post_it.MainActivity;
import edu.temple.project_post_it.R;
import edu.temple.project_post_it.dataBaseManagement;
import edu.temple.project_post_it.post.AudioPost;
import edu.temple.project_post_it.post.Post;

public class AudioPostViewFragment extends Fragment {

    private static final String POST_ID = "Post_ID";
    private String post_ID;
    private AudioPost currentPost;
    TextView titleView;
    TextView descriptionView;
    Button editPostButton;
    Button returnButton;
    Button startButton;
    Button pauseButton;
    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    edu.temple.project_post_it.dataBaseManagement dataBaseManagement;
    DatabaseReference postReference;
    Context context;
    boolean updateProgress;
    private Handler progressHandler;

    public AudioPostViewFragment() {
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
        final View view = inflater.inflate(R.layout.fragment_audio_post_view, container, false);
        dataBaseManagement = new dataBaseManagement();
        titleView = view.findViewById(R.id.titleEditText);
        descriptionView = view.findViewById(R.id.descriptionEditText);
        seekBar = view.findViewById(R.id.seekBar);
        updateProgress = false;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer.isPlaying() && fromUser == true){
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        context = this.getContext();

        postReference = FirebaseDatabase.getInstance().getReference("Members/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + "user_posts/" + post_ID);
        postReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentPost = snapshot.getValue(AudioPost.class);
                titleView.setText(currentPost.getTitle());
                descriptionView.setText(currentPost.getText());

                File file = new File(currentPost.getAudioFilePath());
                if (!(file.exists())){
                    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                    String userID =  FirebaseAuth.getInstance().getCurrentUser().getUid();
                    StorageReference usersRef = mStorageRef.child("Users/" + userID);
                    final StorageReference saveRef = usersRef.child(currentPost.getAudioFileName());
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
                Uri audioURI = Uri.fromFile(file);
                mediaPlayer = MediaPlayer.create(context, audioURI);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        startButton.setText("Start");
                    }
                });
                seekBar.setMax(mediaPlayer.getDuration() / 1000);
                progressHandler = new Handler(Looper.myLooper());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayer != null) {
                            int progress = mediaPlayer.getCurrentPosition() / 1000;
                            seekBar.setProgress(progress);
                            progressHandler.postDelayed(this, 1000);
                        }
                    }

                });
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

        seekBar = view.findViewById(R.id.seekBar);
        startButton = view.findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(mediaPlayer.isPlaying())) {
                    startButton.setText("Stop");
                    mediaPlayer.start();
                } else {
                    startButton.setText("Start");
                    mediaPlayer.stop();
                    try {
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        pauseButton = view.findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(mediaPlayer.isPlaying())) {
                    mediaPlayer.start();
                } else {
                    mediaPlayer.pause();
                }
            }
        });

        returnButton = view.findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_audioPostViewFragment_to_navigation_home);
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        });
        return view;
    }

    private void updateProgress() {
        int progress = mediaPlayer.getCurrentPosition();
        seekBar.setProgress(progress);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}