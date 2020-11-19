package edu.temple.project_post_it;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static edu.temple.project_post_it.CONSTANT.LOCATION_BROADCAST;
import static edu.temple.project_post_it.CONSTANT.LOCATION_KEY;

public class MapService extends Service {
    LocationManager lm;
    LocationListener ll;
    Intent locationIntent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        lm = getSystemService(LocationManager.class);
        Intent notificationIntent = new Intent(MapService.this, user_navigation.class);
        PendingIntent pi = PendingIntent.getActivity(MapService.this, 0, notificationIntent, 0);

        NotificationManager nm = getSystemService(NotificationManager.class);
        NotificationChannel channel = new NotificationChannel("MapService", "Tracing Notifications", NotificationManager.IMPORTANCE_HIGH);
        nm.createNotificationChannel(channel);

        Notification notification = new NotificationCompat.Builder(this, "MapService")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Post-It")
                .setContentText("Currently Tracing location")
                .setContentIntent(pi)
                .build();

        startForeground(1, notification);

        ll = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationIntent = new Intent(LOCATION_BROADCAST);
                locationIntent.putExtra(LOCATION_KEY, location);
                LocalBroadcastManager.getInstance(MapService.this).sendBroadcast(locationIntent);
                Log.i("Service sending Location", location.toString());
            }


            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, ll);
        }

        return START_STICKY;
    }
}
