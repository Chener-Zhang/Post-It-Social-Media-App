package edu.temple.project_post_it.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.temple.project_post_it.R;
import edu.temple.project_post_it.dataBaseManagement;
import edu.temple.project_post_it.post.ImagePost;
import edu.temple.project_post_it.user_navigation;

import static android.app.Activity.RESULT_OK;

public class ImageCreationFragment extends Fragment {

    private static final String MODE = "MODE";
    TextView titleView, descriptionView;
    String title, description;
    CheckBox privacySwitch;
    boolean isPublic;
    Button createPostButton, takePhotoButton;
    LatLng latLng;
    FirebaseUser currentUser;
    Activity activity;
    Uri imageUri;
    String imageFileName;

    String currentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 713;
    edu.temple.project_post_it.dataBaseManagement dataBaseManagement;

    private StorageReference mStorageRef;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_image_creation, container, false);
        dataBaseManagement = new dataBaseManagement();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        title = "Untitled";
        description = "No Description";
        isPublic = true;
        titleView = view.findViewById(R.id.titleEditText);
        descriptionView = view.findViewById(R.id.descriptionEditText);
        privacySwitch = view.findViewById(R.id.privacyCheckBox);
        createPostButton = view.findViewById(R.id.editPostButton);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (user_navigation.loc != null) {
            latLng = user_navigation.loc;
        }
        activity = getActivity();
        takePhotoButton = view.findViewById(R.id.recordButton);


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
                if (imageUri == null) {
                    Toast.makeText(view.getContext(), "You must take a photo first!", Toast.LENGTH_SHORT).show();
                } else {

                    String titleTest = titleView.getText().toString();
                    String descriptionTest = descriptionView.getText().toString();
                    if (titleTest.length() > 0) {
                        title = titleTest;
                    }
                    if (descriptionTest.length() > 0) {
                        description = descriptionTest;
                    }
                    if (privacySwitch.isChecked()) {
                        isPublic = false;
                    }
                    String post_id = Calendar.getInstance().getTime().toString() + currentUser.getUid();
                    ImagePost post = new ImagePost(post_id, isPublic, 1, currentPhotoPath, imageFileName);
                    post.setTitle(title);
                    post.setText(description);
                    if (latLng != null) {
                        edu.temple.project_post_it.post.LatLng location = new edu.temple.project_post_it.post.LatLng();
                        location.setLatitude(latLng.latitude);
                        location.setLongitude(latLng.longitude);
                        post.setLocation(location);
                    }
                    savePost(post);
                }

            }
        });


        return view;
    }

    public void savePost(ImagePost post) {
        //This method is where the new post will be saved to the database. This method, when called, will also return the user back to the homepage.
            dataBaseManagement.dataBaseSaveInMembers_Uid_UserPosts(FirebaseAuth.getInstance().getUid(), post);
            Toast.makeText(this.getContext(), "Post Saved!", Toast.LENGTH_SHORT).show();
            final Context context = this.getContext();
            Uri file = Uri.fromFile(new File(currentPhotoPath));
            String userID =  FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference usersRef = mStorageRef.child("Users/" + userID);
            final StorageReference saveRef = usersRef.child(imageFileName);
            saveRef.putFile(file).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Error: Photo Not Saved to Firebase Storage!", Toast.LENGTH_SHORT).show();
            }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context, "Photo Saved to Firebase Storage!", Toast.LENGTH_SHORT).show();
            }
        });
        dataBaseManagement.dataBaseSaveInMembers_Uid_UserPosts(FirebaseAuth.getInstance().getUid(), post);
        Toast.makeText(this.getContext(), "Post Saved!", Toast.LENGTH_SHORT).show();
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
        imageFileName = "PNG_" + timeStamp + "_";
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
        final Context context = this.getContext();
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Toast.makeText(context, "Photo Taken!", Toast.LENGTH_SHORT).show();

        }
    }


}