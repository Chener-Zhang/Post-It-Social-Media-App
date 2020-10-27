package edu.temple.project_post_it;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DataBase_Management {
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;

    DataBase_Management() {
        rootNode = FirebaseDatabase.getInstance();
    }

    public void write_data(String reference, String data) {
        databaseReference = rootNode.getReference(reference);
        databaseReference.setValue(data);
    }

    public void get_data(String reference) {
        databaseReference = rootNode.getReference(reference);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                Log.d("TAG", "Value is: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());

            }
        });
    }

}
