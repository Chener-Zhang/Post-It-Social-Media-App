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

public class databaseManagement {
    //RootNode = postit-8d9a4
    public FirebaseDatabase root;
    public DatabaseReference databaseReference;
    String currentUser = FirebaseAuth.getInstance().getUid();

    public databaseManagement() {
        root = FirebaseDatabase.getInstance();
    }


    //Mock data require debug
    public void dataBaseAddUser(String Uid) {
        databaseReference = root.getReference().child("Members/" + Uid);
        User user = new User();
        user.setUserID(Uid);
        user.setGroupList(new ArrayList<String>());
        user.setPostList(new ArrayList<Post>());


        databaseReference.setValue(user);
    }

    public void databaseAddGroup(final String newGroup) {
        databaseReference = root.getReference().child("/Groups/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChild(newGroup)) {
                    databaseReference = root.getReference().child("/Groups/" + newGroup);
                    Group group = new Group();
                    group.setPosts(new ArrayList<Post>());
                    group.setUsers(new ArrayList<String>());
                    group.getUsers().add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    group.getUsers().add("Do not remove");
                    group.setGroupName(newGroup);
                    databaseReference.setValue(group);
                    databaseAddGroupList(newGroup);
                    databaseReference.removeEventListener(this);

                } else if (snapshot.hasChild(newGroup)) {
                    databaseReference = root.getReference().child("/Groups/" + newGroup);
                    Group group = snapshot.child(newGroup).getValue(Group.class);
                    if (group.users.contains(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                        //display message that it failed
                        System.out.println("needs to do something");
                    else
                        group.users.add(FirebaseAuth.getInstance().getUid());
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

    public void databaseSavePost(String Uid, final Post post) {
        databaseReference = root.getReference().child("Members/" + Uid);
        databaseReference.child("user_posts/" + post.getPost_ID()).setValue(post);
        if (post.getPrivacy()) {
            databaseSaveGroupPost(post);
        }
    }

    public void databaseSaveGroupPost(final Post post) {

        if (post.isAnonymous()) {
            databaseReference = root.getReference().child("Groups").child("Anonymous").child("posts");
            databaseReference.push().setValue(post);

        } else {
            databaseReference = root.getReference().child("Groups/" + post.getGroupID() + "/posts/" + currentUser);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    databaseReference.child(post.getPost_ID()).setValue(post);
                    databaseReference.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

    }


    public void databaseRemoveData(Post post) {
        databaseReference = root.getReference("Members/" + currentUser + "/user_posts/" + post.getPost_ID());
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

        if (post.getPrivacy())
            databaseRemoveGroupData(post);
    }

    public void databaseRemoveGroupData(Post post) {
        databaseReference = root.getReference().child("/Groups/" + post.getGroupID() + "/posts/" + currentUser + "/" + post.getPost_ID());
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


    public void databaseRemoveGroup(final String groupName) {
        // Remove the Uid in the group
        databaseReference = root.getReference("Groups/" + groupName + "/users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getValue().toString().equals(FirebaseAuth.getInstance().getUid())) {
                        snapshot.child(dataSnapshot.getKey()).getRef().removeValue();
                        databaseReference.removeEventListener(this);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        // Remove the Uid in the Member group list
        databaseReference = root.getReference("Members/" + FirebaseAuth.getInstance().getUid() + "/groupList");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getValue().toString().equals(groupName)) {
                        snapshot.child(dataSnapshot.getKey()).getRef().removeValue();
                        databaseReference.removeEventListener(this);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void databaseAddGroupList(final String newGroup) {
        databaseReference = root.getReference().child("/Members/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (!((user.getGroupList()) == null)) {
                    if (!user.getGroupList().contains(newGroup)) {
                        user.groupList.add(newGroup);
                        databaseReference.setValue(user);
                        databaseReference.removeEventListener(this);
                    }
                } else {
                    databaseReference.child("groupList").child("0").setValue(newGroup);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


