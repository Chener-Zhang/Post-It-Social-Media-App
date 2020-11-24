package edu.temple.project_post_it;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.temple.project_post_it.post.ImagePost;
import edu.temple.project_post_it.post.Post;
import edu.temple.project_post_it.user.User;

public class dataBaseManagement {
    //RootNode = postit-8d9a4
    public FirebaseDatabase rootNode;
    public DatabaseReference databaseReference;

    public dataBaseManagement() {
        rootNode = FirebaseDatabase.getInstance();
    }


    public void dataBaseSetDirection(String reference) {
        databaseReference = rootNode.getReference(reference);
        Log.i("Direction: ", reference);
    }


    //Mock data require debug
    public void dataBaseAddUser(String Uid) {
        databaseReference = rootNode.getReference().child("Members/" + Uid + "/Info");
        User user = new User();

        //Mocking data --------------------->
        user.setUser_id(Uid);
        user.setNumber_posts(user.getNumber_posts());
        user.setUser_groud_id("test_group_id");
        user.setUser_posts(new ArrayList<Post>());
        //Mocking data --------------------->


        databaseReference.setValue(user);
    }

    public void dataBaseSavePost(String Uid, Post post) {
        databaseReference = rootNode.getReference().child("Members/" + Uid + "/Info");
        databaseReference.child("user_posts").child(post.getPost_ID()).push().setValue(post);
    }


    public void dataBaseWriteDataChild(String childs_parent_reference, String child_reference, Object object) {
        databaseReference = rootNode.getReference().child(childs_parent_reference);
        databaseReference.child(child_reference).setValue(object);
    }


    public void dataBaseGetData(String reference) {
        databaseReference = rootNode.getReference(reference);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Class of object, exp : Post place here
                Post object = snapshot.getValue(Post.class);
                //Access the attribute inside the object. Example: object.name, object.image_url
                Log.d("TAG", "Value is: " + object);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }


    //Remove data
    //Reference: directory
    //For example:
    //reference = "Posts/post1/image"
    //reference = "Members/user/name"
    public void databaseRemoveData(String reference) {
        databaseReference = rootNode.getReference(reference);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //if (!snapshot.hasChild(user_id)) check user exist
                snapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    //Update_data:
    //Reference: directory
    //Update_object: Post
    //Key: taget node reference
    //Example usage:
    //DataBase_management.update_data("sometable/thechild", test, "-MLmtCXJtplg1g2GkpTE");

    public void databaseUpdateData(String reference, Post update_object, String key) {
        databaseReference = rootNode.getReference(reference);
        Map<String, Object> postValues = update_object.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, postValues);
        databaseReference.updateChildren(childUpdates);
    }
}


