package edu.temple.project_post_it.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.temple.project_post_it.R;
import edu.temple.project_post_it.post.Post;
import edu.temple.project_post_it.user_navigation;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImageCreationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageCreationFragment extends Fragment {

    private static final String MODE = "MODE";
    private int mode;
    TextView titleView;
    String title;
    TextView descriptionView;
    String descritpion;
    CheckBox privacySwitch;
    boolean isPublic;
    Button createPostButton;
    Button takePhotoButton;
    Location location;
    LatLng latLng;
    FirebaseUser currentUser;
    Activity activity;

    Uri imageUri;

    String currentPhotoPath;
    static Uri photoURI;
    static final int REQUEST_TAKE_PHOTO = 713;

    public ImageCreationFragment() {
        // Required empty public constructor
    }
    public static ImageCreationFragment newInstance(int mode) {
        ImageCreationFragment fragment = new ImageCreationFragment();
        Bundle args = new Bundle();
        args.putInt(MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getInt(MODE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_creation, container, false);
        title = "Untitled";
        descritpion = "No Description";
        isPublic = true;
        titleView = view.findViewById(R.id.titleEditText);
        descriptionView = view.findViewById(R.id.descriptionEditText);
        privacySwitch = view.findViewById(R.id.privacyCheckBox);
        createPostButton = view.findViewById(R.id.createPostButton);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (user_navigation.loc != null){
            latLng = user_navigation.loc;
        }
        activity = getActivity();
        takePhotoButton = view.findViewById(R.id.takePictureButton);


        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    takePhoto();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleTest = titleView.getText().toString();
                String descriptionTest = (String) descriptionView.getText().toString();
                if (titleTest.length() > 0){
                    title = titleTest;
                }
                if (descriptionTest.length() > 0){
                    descritpion = descriptionTest;
                }
                if (privacySwitch.isChecked()){
                    isPublic = false;
                }
                String post_id = Calendar.getInstance().getTime().toString() + currentUser.getUid();
                Post post = new Post(post_id, isPublic, 0);
                post.setTitle(title);
                post.setText(descritpion);
                if (latLng != null){
                    post.setLocation(latLng);
                }
                savePost();

            }
        });



        return view;
    }

    public static void savePost(){
        //This method is where the new post will be saved to the database. This method, when called, will also return the user back to the homepage.

    }

    public void takePhoto() throws IOException {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = createImageFile();
        imageUri = FileProvider.getUriForFile(getContext(), "edu.temple.project_post_it.fileprovider", photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PNG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".png",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Toast.makeText(this.getContext(), "Photo Taken!", Toast.LENGTH_SHORT).show();
        }
    }


}