package edu.temple.project_post_it;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataBase_Management {
    FirebaseDatabase database;
    DatabaseReference myRef;

    DataBase_Management() {
        database = FirebaseDatabase.getInstance();
    }

    public void write_data(String reference, String data) {
        myRef = database.getReference(reference);
        myRef.setValue(data);
    }

    public void get_data() {

    }
}
