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

public class DataBase_Management {
    //RootNode = postit-8d9a4
    public FirebaseDatabase rootNode;
    public DatabaseReference databaseReference;

    public DataBase_Management() {
        rootNode = FirebaseDatabase.getInstance();
    }


    public void set_direction(String reference) {
        databaseReference = rootNode.getReference(reference);
        Log.i("Direction: ", reference);
    }


    //Mock data require debug
    public void add_user(String Uid) {
        databaseReference = rootNode.getReference().child("Members/" + Uid + "/Info");
        User user = new User();


        //Mocking data --------------------->
        Post post1 = new Post(123, null, 1, 1);
        Post post2 = new Post(666, null, 4, 2);
        Post post3 = new Post(897, null, 5, 3);
        Post post4 = new ImagePost(78,null,0,6,null);
        user.add_post(post1);
        user.add_post(post2);
        user.add_post(post3);
        user.add_post(post4);
        user.setUser_id(Uid);
        user.setNumber_posts(user.getNumber_posts());
        user.setUser_groud_id("test_group_id");
        //Mocking data --------------------->

        databaseReference.setValue(user);
    }

    public void add_post(Post post, String Uid) {
        databaseReference = rootNode.getReference().child("Members/" + Uid + "/Post");
        databaseReference.setValue(post);
    }


    /*
    Write_data_child:
    Child_parent_reference is the target directory
    Child_reference is the new directory
    Object wil be the post
    */
    public void write_data_child(String childs_parent_reference, String child_reference, Object object) {
        databaseReference = rootNode.getReference().child(childs_parent_reference);
        databaseReference.child(child_reference).setValue(object);
    }


    /*
    Get data
    reference: directory
    */
    public void get_data(String reference) {
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

    /*
    Remove data
    Reference: directory
    For example:
    reference = "Posts/post1/image"
    reference = "Members/user/name"
    */
    public void remove_data(String reference) {
        databaseReference = rootNode.getReference(reference);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //        if (!snapshot.hasChild(user_id)) check user exist
                snapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /*
    Update_data:
    Reference: directory
    Update_object: Post
    Key: taget node reference
    Example usage:
    DataBase_management.update_data("sometable/thechild", test, "-MLmtCXJtplg1g2GkpTE");
    */
    public void update_data(String reference, Post update_object, String key) {
        databaseReference = rootNode.getReference(reference);
        Map<String, Object> postValues = update_object.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, postValues);
        databaseReference.updateChildren(childUpdates);

    }
}


