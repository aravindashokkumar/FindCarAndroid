package com.aravindproj.findcar;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;


/**
 * Created by aravindashokkumar on 26/7/15.
 */


public class CarLocationSet extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    static LatLng SG = new LatLng(1.2965,103.77548);
    static int count=0;
    LocationListener mlocListener ;
    Button locaBtn;
    Location currentLocation;



    protected static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    protected static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
    private static LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        locaBtn= (Button) findViewById(R.id.locButton);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}


        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);

                    Toast.makeText(CarLocationSet.this,"Please make sure mobile data/ Wi-Fi is turned ON.",Toast.LENGTH_SHORT).show();

                    //get gps
                }
            });
            dialog.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }


        mlocListener = new MyLocationListener();

        locaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentLocation = locationManager
                        .getLastKnownLocation(locationManager.getProviders(true).get(0));
                try
                {if(currentLocation!=null)
                {
                    getLocationValues(currentLocation);
                }}
                catch (Exception e)
                {
                    Toast.makeText(CarLocationSet.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }


            }
        });
        if(!locaBtn.isPressed())
        {
            Toast.makeText(this,"Please press the button to lock current location",Toast.LENGTH_SHORT).show();

        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded(SG);
    }

    protected void setUpMapIfNeeded(LatLng sg) {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap(sg);
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    protected void setUpMap(LatLng sing) {

       mMap.addMarker(new MarkerOptions()
               .position(sing)
               .title("Car Marker -" + (count + 1))
               .snippet("Parked Here"));
        mMap.setMyLocationEnabled(true);
        CameraPosition c = new CameraPosition.Builder()
                .target(sing)
                .zoom(18)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(c));

    }
    private void getLocationValues(Location location) {
        String message = String.format("New Location \n Longitude: %1$s \n Latitude: %2$s",
                location.getLongitude(), location.getLatitude());
            SG=new LatLng( location.getLatitude(),location.getLongitude());
        try {
            Geocoder geocoder;

            List<Address> addresses;
            geocoder = new Geocoder(CarLocationSet.this, Locale.getDefault());
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Log.v("LocationAttributes", +location.getLatitude() + ", " + location.getLongitude());

            setUpMap(SG);
            LocationDBAdapter db= new LocationDBAdapter(CarLocationSet.this);
            db.open();
            db.createLocation(location.getLatitude(), location.getLongitude(), addresses.get(0).getAddressLine(0));
            db.close();



        }
        catch(Exception e)
        {
            Log.v("LocationGet", "Unable to process geocoder/ setting up map");

        }

        Toast.makeText(CarLocationSet.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {

            Log.v("LocChanged", "location has changed");

        }

        public void onStatusChanged(String s, int i, Bundle b) {

            Toast.makeText(CarLocationSet.this, "Provider status changed",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
            Toast.makeText(CarLocationSet.this,
                    "Provider disabled by the user. GPS turned off",
                    Toast.LENGTH_LONG).show();

            NotificationManager manager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Intent n = new Intent(CarLocationSet.this, CarLocationSet.class);
            PendingIntent pending = PendingIntent.getActivity(CarLocationSet.this, 13, n, 0);
            Notification notification =
                    new Notification.Builder(CarLocationSet.this)
                            .setTicker("Important Message")
                            .setSmallIcon(R.mipmap.imap)
                            .setAutoCancel(true)
                            .setContentTitle("Reminder")
                            .setContentText("Please remember to switch on your bluetooth dongle.")
                            .setContentIntent(pending)
                            .build();
            manager.notify(13, notification);
        }

        public void onProviderEnabled(String s) {
            Toast.makeText(CarLocationSet.this,
                    "Provider enabled by the user. GPS turned on",
                    Toast.LENGTH_LONG).show();
        }

    }



}
