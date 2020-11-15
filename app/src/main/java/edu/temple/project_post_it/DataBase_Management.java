package edu.temple.project_post_it;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import edu.temple.project_post_it.post.Post;
import edu.temple.project_post_it.test.test;

public class DataBase_Management {
    //RootNode = postit-8d9a4
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;

    public DataBase_Management() {
        rootNode = FirebaseDatabase.getInstance();
    }


    public void set_direction(String reference) {
        databaseReference = rootNode.getReference(reference);
        Log.i("Direction: ", reference);
    }

    public void write_table(String table_name) {
        databaseReference = rootNode.getReference();
        databaseReference.child("Members").setValue("test");
        databaseReference.child("Groups").setValue("test");
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
    Write date:
    Reference: directory
    Object: post
    */

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
                test object = snapshot.getValue(test.class);
                //Access the attribute inside the object. Example: object.name, object.image_url
                Log.d("TAG", "Value is: " + object.getAge());
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
    public void update_data(String reference, test update_object, String key) {
        databaseReference = rootNode.getReference(reference);
        Map<String, Object> postValues = update_object.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, postValues);
        databaseReference.updateChildren(childUpdates);

    }
}
