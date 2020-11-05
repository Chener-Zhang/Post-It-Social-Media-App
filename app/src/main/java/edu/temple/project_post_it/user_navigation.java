package edu.temple.project_post_it;

import android.Manifest;
import android.app.Fragment;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import edu.temple.project_post_it.ui.UserProfile.UserProfileFragment;
import edu.temple.project_post_it.ui.dashboard.DashboardFragment;
import edu.temple.project_post_it.MapService;

public class user_navigation extends AppCompatActivity implements UserProfileFragment.OnDataPass_UserProfileFragment, DashboardFragment.MapInterface {
    Intent serviceIntent;
    IntentFilter broadcastFilter;
    Location location;
    LatLng loc;


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("Location Broadcast")) {
                location = intent.getParcelableExtra("Location Key");
                loc = new LatLng(location.getLatitude(), location.getLongitude());

                }
            }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_navigation);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_userprofile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        NotificationChannel defaultChannel = new NotificationChannel("default",
                "Default",
                NotificationManager.IMPORTANCE_DEFAULT
        );

        getSystemService(NotificationManager.class).createNotificationChannel(defaultChannel);


        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        serviceIntent = new Intent(this, MapService.class);

        broadcastFilter = new IntentFilter();
        broadcastFilter.addAction("Location Broadcast");
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver, broadcastFilter);
    }

    public void back_to_sign_activity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void sign_out() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        back_to_sign_activity();
        Log.d("Reach", "user_navigation activity");
    }

    @Override
    public void setLocation(LatLng loc) {

    }
}