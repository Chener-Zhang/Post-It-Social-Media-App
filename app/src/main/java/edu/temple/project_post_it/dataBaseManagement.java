package edu.temple.project_post_it;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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


    //First time login
    public void dataBaseAddUser(String Uid) {
        databaseReference = root.getReference().child("Members/" + Uid);

        User user = new User();
        user.setUserID(Uid);

        databaseReference.setValue(user);
    }


    public void dataBaseSaveInMembers_Uid_UserPosts(String Uid, final Post post) {
        databaseReference = root.getReference().child("Members/" + Uid);
        databaseReference.child("user_posts/" + post.getPost_ID()).setValue(post);
        dataBaseSaveInGroups_group_posts(post.getGroupID(), post);
        dataBaseSaveInGroup_group_users(post.getGroupID(), FirebaseAuth.getInstance().getUid());
    }

    public void dataBaseSaveInGroups_group_posts(String groupName, final Post post) {
        databaseReference = root.getReference().child("Groups/" + groupName);
        databaseReference.child("posts/" + post.getPost_ID()).setValue(post);
    }

    public void dataBaseSaveInGroup_group_users(String groupName, final String UID) {
        databaseReference = root.getReference().child("Groups/" + groupName);
        databaseReference.child("users/" + UID).setValue(UID);

    }


    public void databaseRemovePostData(String post_id) {
        databaseReference = root.getReference("Members/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + "user_posts/" + post_id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
                databaseReference.removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void databaseRemoveGroupData(final String group_id) {
        //Delete from Members

        databaseReference = root.getReference("Members/" + FirebaseAuth.getInstance().getUid() + "/groupList");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.child(group_id).getRef().removeValue();
                databaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                databaseReference.removeEventListener(this);
            }

        });

        //Delete from Groups
        databaseReference = root.getReference("Groups/" + group_id + "/users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getRef().removeValue();
                databaseReference.removeEventListener(this);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                databaseReference.removeEventListener(this);
            }
        });


    }


    public void databaseAddGroupToGroups(final String newGroup) {
        databaseReference = root.getReference().child("/Groups/");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChild(newGroup)) {
                    databaseReference.child(newGroup).child("groupName").setValue(newGroup);
                    databaseReference.child(newGroup).child("users").child(FirebaseAuth.getInstance().getUid()).setValue(FirebaseAuth.getInstance().getUid());
                } else {
                    databaseReference.child(newGroup).child("users").child(FirebaseAuth.getInstance().getUid()).setValue(FirebaseAuth.getInstance().getUid());
                }
                databaseReference = root.getReference().child("/Members/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
                databaseReference.child("groupList").child(newGroup).setValue(newGroup);
                databaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                databaseReference.removeEventListener(this);

            }
        });


    }


}


