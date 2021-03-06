package com.jaswanthk.traceit;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public GoogleMap mMap;
    LocationManager locationManager;
    FirebaseDatabase db;
    DatabaseReference dr,dr1;
    Marker mark;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        db= FirebaseDatabase.getInstance();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toast.makeText(getApplicationContext(),"MAP",Toast.LENGTH_SHORT).show();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final Bundle b1=getIntent().getExtras();
        dr=db.getReference().child(b1.getString("Cr"));



        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.child("Latitude").getValue(String.class);
                String value1 = dataSnapshot.child("Longitude").getValue(String.class);
                LatLng val = new LatLng(Double.parseDouble(value),Double.parseDouble(value1));
                if(mark!=null)
                {
                    mark.remove();
                }

                mark=mMap.addMarker(new MarkerOptions().position(val).title("Marker in"+b1.getString("Cr")));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(val,17));
                // Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        //String value = dataSnapshot.child("Latitude").getValue().toString();
        // Add a marker in Sydney and move the camera

    }
    public void getLocation() {
        Toast.makeText(getApplicationContext(),"Reading Location",Toast.LENGTH_SHORT).show();
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0, 0, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0, 0, (LocationListener) this);

            //Toast.makeText(getApplicationContext(),"Location Read",Toast.LENGTH_SHORT).show();
        }
        catch (SecurityException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }


    //@Override
    //protected void onStop() {
    //super.onStop();
    // finish();
    //}
}
