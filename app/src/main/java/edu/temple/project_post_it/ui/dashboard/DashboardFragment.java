package edu.temple.project_post_it.ui.dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.temple.project_post_it.CONSTANT;
import edu.temple.project_post_it.R;
import edu.temple.project_post_it.post.Post;
import edu.temple.project_post_it.user_navigation;


public class DashboardFragment extends Fragment implements OnMapReadyCallback {
    private MapView mapView;
    GoogleMap googleMap;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    double lat, lng;
    LatLng loc;
    SharedPreferences preferences;
    boolean anon, group, all;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        Log.i("user id", "Members/" + user.getUid() + "/user_posts");


        //Init the map
        MapsInitializer.initialize(getActivity());
        mapView = root.findViewById(R.id.mapView);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        anon = preferences.getBoolean(getString(R.string.anon_key), false);
        group = preferences.getBoolean(getString(R.string.group_key), false);
        all = preferences.getBoolean(getString(R.string.all_key), false);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onStart();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(user_navigation.loc, 15));

        //Show User's posts
        //Different color to show current location
        googleMap.addMarker((new MarkerOptions()).position(user_navigation.loc).title("Current Location").flat(true)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        if(all) {
            showPrivatePosts();
            showGroupPosts();
            showAnonPosts();
        } else{
            showPrivatePosts();

            if(group)
                showGroupPosts();

            if(anon)
                showAnonPosts();
        }

    }


        //Show same group post
    void showAnonPosts(){
        databaseReference = root.getReference().child("/Groups/Anonymous/posts");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    final Post post = dataSnapshot.getValue(Post.class);
                    lat = post.getLocation().getLatitude();
                    lng = post.getLocation().getLongitude();
                    loc = new LatLng(lat, lng);

                    if (post.getGroupID().equals(CONSTANT.ANONYMOUS)) {
                        googleMap.addMarker(new MarkerOptions().position(loc)
                                .title(post.getPost_ID())
                                .snippet(post.getGroupID())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

                        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                if (!marker.isFlat()) {
                                    Intent intent = new Intent(getContext(), postDetail.class);
                                    intent.putExtra(CONSTANT.POST_ID, marker.getTitle());
                                    intent.putExtra(CONSTANT.GROUP_ID, marker.getSnippet());
                                    startActivity(intent);
                                }
                                return false;
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    void showGroupPosts(){
            databaseReference = root.getReference().child("/Members/" + user.getUid() + "/groupList");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<String> groups = new ArrayList<String>();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        groups.add(dataSnapshot.getKey());
                        Log.i("groups", dataSnapshot.getKey());
                    }

                    Log.i("groups", groups.toString());

                    for (String group : groups) {
                        databaseReference = root.getReference().child("/Groups/" + group + "/posts");
                        Log.i("groups", "/Groups/" + group + "/posts");
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    final Post post = dataSnapshot.getValue(Post.class);
                                    Log.i("groups", post.getText());
                                    lat = post.getLocation().getLatitude();
                                    lng = post.getLocation().getLongitude();
                                    loc = new LatLng(lat, lng);

                                    if (!post.getPrivacy() && !post.getGroupID().equals(CONSTANT.ANONYMOUS)) {
                                        googleMap.addMarker(new MarkerOptions().position(loc)
                                                .title(post.getPost_ID())
                                                .snippet(post.getGroupID())
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                                        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                            @Override
                                            public boolean onMarkerClick(Marker marker) {
                                                if (!marker.isFlat()) {
                                                    Intent intent = new Intent(getContext(), postDetail.class);
                                                    intent.putExtra(CONSTANT.POST_ID, marker.getTitle());
                                                    intent.putExtra(CONSTANT.GROUP_ID, marker.getSnippet());
                                                    startActivity(intent);
                                                }
                                                return false;
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    void showPrivatePosts(){
        FirebaseDatabase.getInstance().getReference("Members/" + user.getUid() + "/user_posts")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                final Post post = snapshot.getValue(Post.class);
                                lat = post.getLocation().getLatitude();
                                lng = post.getLocation().getLongitude();
                                loc = new LatLng(lat, lng);

                                if (post.getPrivacy() && !post.getGroupID().equals(CONSTANT.ANONYMOUS)) {
                                    googleMap.addMarker(new MarkerOptions().position(loc)
                                            .title(post.getPost_ID())
                                            .snippet(post.getGroupID())
                                            .flat(true));
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i("Error", String.valueOf(error));
                    }
                });

    }


}

