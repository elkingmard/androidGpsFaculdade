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
import android.util.Log;
import android.widget.TextView;
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
    private double altitude;
    private double speed;

    private TextView mapLat;
    private TextView mapLong;
    private TextView mapAlt;
    private TextView mapSpeed;

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

        mapLat = (TextView) findViewById(R.id.textLatMap);
        mapLong = (TextView) findViewById(R.id.textLongMap);
        mapAlt = (TextView) findViewById(R.id.textAltMap);
        mapSpeed = (TextView) findViewById(R.id.textSpeedMap);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng cellPhoneLocation = new LatLng(this.latitude, this.longitude);
        mMap.addMarker(new MarkerOptions().position(cellPhoneLocation).title("Você esta aqui"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cellPhoneLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));

    }

    public void setInfosOverMap(){
        this.mapLat.setText( "latitude: " + this.latitude );
        this.mapLong.setText( "longitude: " + this.longitude );
        this.mapAlt.setText( "altitude: " + this.altitude );
        this.mapSpeed.setText( "Velocidade: " + this.speed );
    }

    public void setCellLocation() {
        try {
            String provider = locManager.PASSIVE_PROVIDER;
            Location location = this.locManager.getLastKnownLocation(provider.toString());
            if(location !=null){
                this.latitude = location.getLatitude();
                this.longitude = location.getLongitude();
                this.speed = location.getSpeed();
                this.altitude = location.getAltitude();

                this.setInfosOverMap();
            }

        }catch (SecurityException e){
            e.printStackTrace();
            return;
        }
        }

    public void ativaGPS() {
        try {
            this.locPro = locManager.getProvider(LocationManager.GPS_PROVIDER);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            //&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
           ){
            // A permissão foi dada
            setCellLocation();
            ativaGPS();
        } else {
            // Solicite a permissão
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, REQUEST_LOCATION);
            //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);

        }
    }

    @Override
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
