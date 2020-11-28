package edu.temple.project_post_it;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.temple.project_post_it.group.Group;
import edu.temple.project_post_it.post.Post;
import edu.temple.project_post_it.user.User;

public class dataBaseManagement {
    //RootNode = postit-8d9a4
    public FirebaseDatabase root;
    public DatabaseReference databaseReference;

    public dataBaseManagement() {
        root = FirebaseDatabase.getInstance();
    }


    public void dataBaseSetDirection(String reference) {
        databaseReference = root.getReference(reference);
        Log.i("Direction: ", reference);
    }


    //Mock data require debug
    public void dataBaseAddUser(String Uid) {
        databaseReference = root.getReference().child("Members/" + Uid);
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
        databaseReference = root.getReference().child("Members/" + Uid);
        databaseReference.child("user_posts/" + post.getPost_ID()).setValue(post);
        if(post.getPrivacy()) {
            databaseAddGroup(post.getGroupID());
            root.getReference("Groups/" + post.getGroupID() + "/posts").setValue(post);
        }
    }


    public void dataBaseWriteDataChild(String childs_parent_reference, String child_reference, Object object) {
        databaseReference = root.getReference().child(childs_parent_reference);
        databaseReference.child(child_reference).setValue(object);
    }


    public void dataBaseGetData(String reference) {
        databaseReference = root.getReference(reference);
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
    public void databaseRemoveData(String post_id) {
        databaseReference = root.getReference("Members/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + "user_posts/" + post_id);
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


    //Update_data:
    //Reference: directory
    //Update_object: Post
    //Key: taget node reference
    //Example usage:
    //DataBase_management.update_data("sometable/thechild", test, "-MLmtCXJtplg1g2GkpTE");

    public void databaseUpdateData(String reference, Post update_object, String key) {
        databaseReference = root.getReference(reference);
        Map<String, Object> postValues = update_object.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, postValues);
        databaseReference.updateChildren(childUpdates);
    }

    public void databaseAddGroup(String newGroup){
        databaseReference = root.getReference().child("/Groups/" + newGroup);
        Group group = new Group();
        group.setPostArrayList(new ArrayList<Post>());
        group.setUserArrayList(new ArrayList<String>());
        group.users.add(FirebaseAuth.getInstance().getUid());


        databaseReference.setValue(group);

    }
}


