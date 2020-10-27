package edu.temple.project_post_it;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    }
}
