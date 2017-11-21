package com.example.ramonmoraes.gpsfaculdade;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

public class SateliteActivity extends AppCompatActivity{

    ListView table;
    TextView satLat, satLong, satAlt;

    private double longitude, speed, altitude;

    private Iterable<GpsSatellite> sateliteList;
    private GpsStatus gpsStatus;


    private LocationManager locManager; // O Gerente de localização
    private LocationProvider locPro; // Provedor de localização
    private Location location;
    private static final int REQUEST_LOCATION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satelite);
        this.instanceElements();

    }

    private void instanceElements() {

        this.locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        this.addGpsListener();
        this.satLat = (TextView) findViewById(R.id.satLat);
        this.satLong = (TextView) findViewById(R.id.satLong);
        this.satAlt = (TextView) findViewById(R.id.satAlt);
        this.table = (ListView) findViewById(R.id.satList);

    }

    private void addGpsListener() {
        try {
            this.gpsStatus = locManager.getGpsStatus(null);

            this.sateliteList = this.gpsStatus.getSatellites();
            this.loopOverSateliteList();
        } catch (SecurityException e) {
            e.printStackTrace();
            return;
        }
    }

    private void loopOverSateliteList() {

        int nSat = this.gpsStatus.getMaxSatellites();
        Log.d("CREATION", String.valueOf(nSat));

        if (this.gpsStatus != null) {
            Log.d("CREATION", "GPS STATUS !=NULL");

            for(GpsSatellite satInfo : this.sateliteList){
                //Nao entra nesse for...
                double azi = satInfo.getAzimuth();
                Log.d("CREATION", String.valueOf(azi));

            }
        }

    }

    private void setTextViews() {

        this.satLat.setText("Altitude :\n" + this.location.getLatitude());
        this.satLong.setText("Longitude :\n" + this.location.getLongitude());
        this.satAlt.setText("Altitude :\n" + this.location.getAltitude());

    }

    public void getLocInfos() {

        try {
            String provider = locManager.PASSIVE_PROVIDER;
            this.location = this.locManager.getLastKnownLocation(provider.toString());
            if (this.location != null) {
                this.longitude = this.location.getLongitude();
                this.speed = this.location.getSpeed();
                this.altitude = this.location.getAltitude();
                setTextViews();
            }
        } catch (SecurityException e) {
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
                ) {
            // A permissão foi dada
            getLocInfos();
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
            if (grantResults.length == 1 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                // O usuário acabou de dar a permissão
                ativaGPS();
            } else {
                // O usuário não deu a permissão solicitada
                Toast.makeText(this, "Sua localização não será mostrada", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


}
