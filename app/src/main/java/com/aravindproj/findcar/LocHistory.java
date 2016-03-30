package com.aravindproj.findcar;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class LocHistory extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    ListView lv;
    List<LocationVal> list;
    protected LocationDBAdapter db;
    SimpleAdapter adapter;
    private void getData()
    {
        list= new ArrayList<LocationVal>();
        list.add(new LocationVal("Vivo City" , 1.26474, 102.82217));
        list.add(new LocationVal("NUS" , 1.2947, 103.77616));
        list.add(new LocationVal("Botanic Gardens" , 1.3139, 103.81582));
    }

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    static LatLng SG = new LatLng(1.292333, 103.776815);
    double lat = SG.latitude;
    double longi= SG.longitude;

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView2))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap( lat,  longi, "ISS/ NUS");
            }
        }
    }
    private void setUpMap(double lat, double longi, String loc) {

        SG= new LatLng(lat,longi);
        mMap.addMarker(new MarkerOptions()
                .position(SG)
                .title(loc)
                .snippet(" Car marker"));
        mMap.setMyLocationEnabled(true);
        CameraPosition c = new CameraPosition.Builder()
                .target(SG)
                .zoom(18)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(c));

    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_history);
        lv=(ListView) findViewById(R.id.locHistlistView);
        //For hardcode list
//    getData();
//         adapter= new SimpleAdapter(this,list,R.layout.row2,
//                new String[]{"loc", "lat", "longi"},
//                new int[]{R.id.textHead, R.id.text1,R.id.text2});
//        lv.setAdapter(adapter);
//
//        lv.setOnItemClickListener(this);
        db = new LocationDBAdapter(this);

        db.open();
        fillData();
        lv.setOnItemClickListener(this);

    }

    private void fillData() {


                Cursor cursor = db.fetchAllLocations();
        try{
        if(!(cursor.moveToNext()))
        {
            db.createLocation(1.26474, 102.82217, "Vivo City" );
            db.createLocation( 1.2947, 103.77616,"NUS");
            db.createLocation(1.3139, 103.81582,"Botanic Gardens");
            cursor = db.fetchAllLocations();
        }}
        catch(Exception e)
        {

            e.printStackTrace();
        }
        SimpleCursorAdapter myadapter = new SimpleCursorAdapter(this,
                R.layout.row2, cursor,
                new String[] { LocationDBAdapter.KEY_LOCATION,LocationDBAdapter.KEY_LAT, LocationDBAdapter.KEY_LONG },
                new int[] { R.id.textHead, R.id.text1,R.id.text2 }, 0);
        lv.setAdapter(myadapter);
    }

    @Override
    public void onItemClick(AdapterView<?> av, View v, int position, long id) {
//  for normal hardcode list
//      LocationVal s = (LocationVal) av.getAdapter().getItem(position);
//        Toast.makeText(getApplicationContext(), s.get("loc") + " selected",
//                Toast.LENGTH_LONG).show();
//
//        setUpMap(Double.parseDouble(s.get("lat")), Double.parseDouble(s.get("longi")), s.get("loc"));

       getLocation(id);


    }

    private void getLocation(long id) {
        if (id > 0) {
            Cursor location = db.fetchLocation(id);
            String s1=location.getString(location
                    .getColumnIndexOrThrow(LocationDBAdapter.KEY_LOCATION));
            Double d1=location.getDouble(location
                    .getColumnIndexOrThrow(LocationDBAdapter.KEY_LAT));
            Double d2=location.getDouble(location
                    .getColumnIndexOrThrow(LocationDBAdapter.KEY_LONG));


            setUpMap(d1, d2, s1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loc_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id==R.id.action_title)
        {
            Intent i = new Intent(this,HomePage.class);
            startActivity(i);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
            db = null;
        }
    }
}

