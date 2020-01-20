package com.example.panic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Main2Activity extends AppCompatActivity implements LocationListener {
    private DatabaseReference mDatabase;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference("Tracking/coord");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        try {

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            Criteria crit = new Criteria();
            crit.setAccuracy(Criteria.ACCURACY_FINE);

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        }
        catch(SecurityException e) {
            e.printStackTrace();
            Toast.makeText(this, "Not Enough Permission"+e, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        Location myLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        mDatabase.child("Lat").setValue(myLocation.getLatitude());
                        mDatabase.child("Long").setValue(myLocation.getLongitude());

                        Toast.makeText(this, " : "+myLocation.getLatitude()+" : "+myLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    }
                    catch(SecurityException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Not Enough Permission"+e, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "Not Enough Permission", Toast.LENGTH_SHORT).show();
                }
            }
            case 2: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "Not Enough Permission", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, " : "+location.getLatitude()+" : "+location.getLongitude(), Toast.LENGTH_SHORT).show();
        mDatabase.child("Lat").setValue(location.getLatitude());
        mDatabase.child("Long").setValue(location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }
}
