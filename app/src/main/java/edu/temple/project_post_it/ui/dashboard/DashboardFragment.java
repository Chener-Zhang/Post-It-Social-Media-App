package edu.temple.project_post_it.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
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

import edu.temple.project_post_it.R;
import edu.temple.project_post_it.dataBaseManagement;
import edu.temple.project_post_it.post.Post;
import edu.temple.project_post_it.user_navigation;


public class DashboardFragment extends Fragment implements OnMapReadyCallback {
    private Marker marker;
    private MapView mapView;
    GoogleMap googleMap;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    double lat, lng;
    LatLng loc;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
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
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(user_navigation.loc, 15));
//        marker = googleMap.addMarker((new MarkerOptions()).position(user_navigation.loc));

        FirebaseDatabase.getInstance().getReference("Members/" + user.getUid() + "/user_posts")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                lat = (double) snapshot.child("location/latitude").getValue();
                                Log.i("latitude", String.valueOf(lat));
                                lng = (double) snapshot.child("location/longitude").getValue();
                                Log.i("longitude", String.valueOf(lng));
                                loc = new LatLng(lat, lng);
                                Log.i("post object", loc.toString());
                                marker = googleMap.addMarker((new MarkerOptions()).position(loc));
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