//package edu.temple.project_post_it;
//
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class DataBase_Management {
//    FirebaseDatabase rootNode;
//    DatabaseReference databaseReference;
//
//    public DataBase_Management() {
//        rootNode = FirebaseDatabase.getInstance();
//    }
//
//    public void write_data_child(String child_table, String child_reference, Object object) {
//        databaseReference = rootNode.getReference().child(child_table);
//        databaseReference.child(child_reference).setValue(object);
//    }
//
//    public void write_data(String reference, Object object) {
//        databaseReference = rootNode.getReference(reference);
//        databaseReference.push().setValue(object);
//    }
//
//    public void get_data(String reference) {
//        databaseReference = rootNode.getReference(reference);
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String value = snapshot.getValue(String.class);
//                Log.d("TAG", "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.w("TAG", "Failed to read value.", error.toException());
//
//            }
//        });
//    }
//
//}
