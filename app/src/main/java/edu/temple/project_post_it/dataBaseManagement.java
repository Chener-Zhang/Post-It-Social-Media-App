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
        user.setGroupList(new ArrayList<String>());
        user.groupList.add("Default");
        user.setPostList(new ArrayList<Post>());

        databaseReference.setValue(user);
    }

    public void dataBaseSavePost(String Uid, final Post post) {
        databaseReference = root.getReference().child("Members/" + Uid);
        databaseReference.child("user_posts/" + post.getPost_ID()).setValue(post);

        if (post.getPrivacy()) {
            databaseReference = root.getReference().child("/Groups/" + post.getGroupID());

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Group group = snapshot.getValue(Group.class);

                    if (group.getPosts().isEmpty()) {
                        group.setPosts(new ArrayList<Post>());
                    } else {
                        group.posts.add(post);
                        databaseReference.setValue(group);
                        databaseReference.removeEventListener(this);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


    public void databaseRemovePostData(String post_id) {
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

    public void databaseRemoveGroupData(final String group_id) {
        databaseReference = root.getReference("Members/" + FirebaseAuth.getInstance().getUid() + "/groupList");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String target = null;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    System.out.println(dataSnapshot.toString());
                    if (dataSnapshot.getValue().equals(group_id)) {
                        target = dataSnapshot.getKey();
                        snapshot.child(target).getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference = root.getReference("Groups/" + group_id + "/users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String target = null;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    System.out.println(dataSnapshot.toString());
                    if (dataSnapshot.getValue().toString().equals(FirebaseAuth.getInstance().getUid().toString())) {
                        target = dataSnapshot.getKey();
                        snapshot.child(target).getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void databaseAddGroup(final String newGroup) {
        databaseReference = root.getReference().child("/Groups/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChild(newGroup)) {
                    databaseReference = root.getReference().child("/Groups/" + newGroup);
                    Group group = new Group();
                    group.setPosts(new ArrayList<Post>());
                    group.setUsers(new ArrayList<String>());
                    group.getUsers().add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    group.setGroupName(newGroup);
                    databaseReference.setValue(group);
                    databaseAddGroupList(newGroup);
                    databaseReference.removeEventListener(this);

                } else if (snapshot.hasChild(newGroup)) {
                    databaseReference = root.getReference().child("/Groups/" + newGroup);
                    Group group = snapshot.child(newGroup).getValue(Group.class);
                    System.out.println(group.getGroupName());


                    if (!snapshot.child(newGroup).hasChild("users")) {
                        group.setUsers(new ArrayList<String>());
                        group.getUsers().add(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    }

                    if (group.users.contains(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        //display message that it failed
                        System.out.println("needs to do something");
                    } else {
                        group.users.add(FirebaseAuth.getInstance().getUid());
                    }
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

    public void databaseAddGroupList(final String newGroup) {
        databaseReference = root.getReference().child("/Members/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                System.out.println("This is the snapshot" + snapshot.toString());
                System.out.println("This is the users " + user.getGroupList().toString());
                if (!user.getGroupList().contains(newGroup)) {
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


