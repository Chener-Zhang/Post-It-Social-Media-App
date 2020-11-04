package edu.temple.project_post_it;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.temple.project_post_it.test.test;

public class DataBase_Management {
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;

    public DataBase_Management() {
        rootNode = FirebaseDatabase.getInstance();
    }


    public void write_data_child(String childs_parent_reference, String child_reference, Object object) {
        databaseReference = rootNode.getReference().child(childs_parent_reference);
        databaseReference.child(child_reference).setValue(object);
    }

    public void write_data(String reference, Object object) {
        databaseReference = rootNode.getReference(reference);
        databaseReference.push().setValue(object);
    }


    //The reference will be the table directly below postit-8d9a4;
    /*
    For example:
    reference = "Posts/post1/image"
    reference = "Members/user/name"
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


    /* remove data, the format of reference will be same as get data
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
}
