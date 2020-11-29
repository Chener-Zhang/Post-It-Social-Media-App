package edu.temple.project_post_it;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        user.setUserID(Uid);
        user.setGroupList(new ArrayList<String>());
        user.groupList.add("Default");
        user.setPostList(new ArrayList<Post>());
        //Mocking data --------------------->


        databaseReference.setValue(user);
    }

    public void dataBaseSavePost(String Uid, Post post) {
        databaseReference = root.getReference().child("Members/" + Uid);
        databaseReference.child("user_posts/" + post.getPost_ID()).setValue(post);
        if(post.getPrivacy()) {
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

    public void databaseAddGroup(final String newGroup){
        databaseReference = root.getReference().child("/Groups/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChild(newGroup)){
                    databaseReference = root.getReference().child("/Groups/" + newGroup);
                    Group group = new Group();
                    group.setPosts(new ArrayList<Post>());
                    group.setUsers(new ArrayList<String>());
                    group.getUsers().add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    group.setGroupName(newGroup);
                    databaseReference.setValue(group);
                    databaseAddGroupList(newGroup);
                    databaseReference.removeEventListener(this);

                } else if(snapshot.hasChild(newGroup)){
                    databaseReference = root.getReference().child("/Groups/" + newGroup);
                    Group group = snapshot.child(newGroup).getValue(Group.class);
                    System.out.println("This is Group " + group.users.toString());
                    if(group.users.contains(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                        //display message that it failed
                        System.out.println("needs to do something");
                    else
                        group.users.add(FirebaseAuth.getInstance().getUid());
                    System.out.println("This is Group " + group.users.toString());
                    databaseReference.setValue(group).isComplete();
                    databaseAddGroupList(newGroup);
                    databaseReference.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void databaseAddGroupList(final String newGroup){
        databaseReference = root.getReference().child("/Members/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                System.out.println("This is the snapshot" + snapshot.toString());
                System.out.println("This is the users " + user.getGroupList().toString());
                if(!user.getGroupList().contains(newGroup)) {
                    user.groupList.add(newGroup);
                    databaseReference.setValue(user);
                    databaseReference.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


