package edu.temple.project_post_it.ui.dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;

import edu.temple.project_post_it.R;
import edu.temple.project_post_it.user_navigation;

public class DashboardFragment extends Fragment implements OnMapReadyCallback {
    private Marker marker;
    private MapView mapView;
    Location location;
    LatLng loc;
    IntentFilter broadcastFilter;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("Location Broadcast")) {
                location = intent.getParcelableExtra("Location Key");
                loc = new LatLng(location.getLatitude(), location.getLongitude());
                Log.i("Location in Dashboard", loc.toString());

            }
        }
    };

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

//        broadcastFilter = new IntentFilter();
//        broadcastFilter.addAction("Location Broadcast");
//        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, broadcastFilter);

        if (loc != null) {
            MapsInitializer.initialize(getActivity());
            mapView = root.findViewById(R.id.mapView);
            mapView.getMapAsync(this);
            mapView.onCreate(savedInstanceState);
        }

            return root;
    }

        @Override
        public void onResume () {
            super.onResume();
            mapView.onResume();
        }

        @Override
        public void onStart () {
            super.onStart();
            mapView.onStart();
        }

        @Override
        public void onSaveInstanceState (@NonNull Bundle outState){
            super.onSaveInstanceState(outState);
            mapView.onSaveInstanceState(outState);
        }

        @Override
        public void onPause () {
            super.onPause();
            mapView.onPause();
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
        }

        @Override
        public void onStop () {
            super.onStop();
            mapView.onStart();
        }

        @Override
        public void onDestroy () {
            super.onDestroy();
            mapView.onStart();
        }

        @Override
        public void onMapReady (GoogleMap googleMap){
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 20));
            marker = googleMap.addMarker((new MarkerOptions()).position(loc));
        }

        public interface MapInterface{
        void setLocation(LatLng loc);
        }
}