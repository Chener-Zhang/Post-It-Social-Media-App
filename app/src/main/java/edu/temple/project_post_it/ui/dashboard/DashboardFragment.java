package edu.temple.project_post_it.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        Log.i("user id", "Members/" + user.getUid() + "/user_posts");


        //Init the map
        MapsInitializer.initialize(getActivity());
        mapView = root.findViewById(R.id.mapView);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);
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
        googleMap.addMarker((new MarkerOptions()).position(user_navigation.loc).title("Current Location")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
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

                                googleMap.addMarker(new MarkerOptions().position(loc)
                                        .title(post.getPost_ID())
                                        .snippet(post.getGroupID()));
                                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(Marker marker) {
                                        Intent intent = new Intent(getContext(), postDetail.class);
                                        intent.putExtra("postID", marker.getTitle());
                                        startActivity(intent);
                                        return false;
                                    }
                                });

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i("Error", String.valueOf(error));
                    }
                });

        //Show same group post
        databaseReference = root.getReference().child("/Members/" + user.getUid() + "/groupList");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> groups = new ArrayList<String>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    groups.add(dataSnapshot.getKey());
                }

                for (String group : groups) {
                    databaseReference = root.getReference().child("/Groups/" + group + "/posts");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                final Post post = dataSnapshot.getValue(Post.class);
                                lat = post.getLocation().getLatitude();
                                lng = post.getLocation().getLongitude();
                                loc = new LatLng(lat, lng);

                                googleMap.addMarker(new MarkerOptions().position(loc)
                                        .title(post.getPost_ID())
                                        .snippet(post.getGroupID())
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(Marker marker) {
                                        Intent intent = new Intent(getContext(), postDetail.class);
                                        intent.putExtra("postID", marker.getTitle());
                                        intent.putExtra("groupId",marker.getSnippet());
                                        startActivity(intent);
                                        return false;
                                    }
                                });
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


}

