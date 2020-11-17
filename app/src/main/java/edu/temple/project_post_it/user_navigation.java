package edu.temple.project_post_it;

import android.Manifest;
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

import static edu.temple.project_post_it.CONSTANT.LOCATION_BROADCAST;
import static edu.temple.project_post_it.CONSTANT.LOCATION_KEY;

public class user_navigation extends AppCompatActivity implements UserProfileFragment.OnDataPass_UserProfileFragment {
    Intent mapserviceIntent;
    IntentFilter broadcastFilter;
    Location location;
    public static LatLng loc;


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Get the data from the MapService
            if (intent.getAction().equals(LOCATION_BROADCAST)) {
                location = intent.getParcelableExtra(LOCATION_KEY);
                loc = new LatLng(location.getLatitude(), location.getLongitude());
                Log.i("MainActivity: RECEIVED NEW LOC", loc.toString());
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


        //Create notification channel
        NotificationChannel defaultChannel = new NotificationChannel("default",
                "Default",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        getSystemService(NotificationManager.class).createNotificationChannel(defaultChannel);

        //Check user permission for the ACCESS_FINE_LOCATION
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        //Init the mapserviceIntent
        mapserviceIntent = new Intent(this, MapService.class);
        startService(mapserviceIntent);

        broadcastFilter = new IntentFilter();
        broadcastFilter.addAction(LOCATION_BROADCAST);
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
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("Map Service is stopped");
        stopService(mapserviceIntent);
    }
}