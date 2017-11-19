package com.example.ramonmoraes.gpsfaculdade;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double latitude;
    private double longitude;

    private LocationManager locManager; // O Gerente de localização
    private LocationProvider locPro; // Provedor de localização
    private static final int REQUEST_LOCATION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locManager = (LocationManager) getSystemService(LOCATION_SERVICE);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng cellPhoneLocation = new LatLng(this.latitude, this.longitude);
        mMap.addMarker(new MarkerOptions().position(cellPhoneLocation).title("Marker on'tu"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cellPhoneLocation));
    }

    public void setCellLocation() {
            String provider;
            Location location = this.locManager.getLastKnownLocation(String provider);
            this.latitude=location.getLatitude();
            this.longitude=location.getLongitude();

    }

    public void ativaGPS() {
        try {
            this.locPro = locManager.getProvider(LocationManager.GPS_PROVIDER);

            setCellLocation();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
           ){
            // A permissão foi dada
            ativaGPS();
        } else {
            // Solicite a permissão
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);

        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if(grantResults.length == 1 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                // O usuário acabou de dar a permissão
                ativaGPS();
            }
            else {
                // O usuário não deu a permissão solicitada
                Toast.makeText(this,"Sua localização não será mostrada",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}
