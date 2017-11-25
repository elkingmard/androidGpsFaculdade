package com.example.ramonmoraes.gpsfaculdade;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Iterator;
import java.util.List;

import static java.lang.String.valueOf;

public class SateliteActivity extends AppCompatActivity implements LocationListener,GpsStatus.Listener{

    TableLayout table;
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
        this.satLat = (TextView) findViewById(R.id.satLat);
        this.satLong = (TextView) findViewById(R.id.satLong);
        this.satAlt = (TextView) findViewById(R.id.satAlt);
        this.table = (TableLayout) findViewById(R.id.satList);

    }

    private void addGpsListener() {
        try {
            this.gpsStatus = locManager.getGpsStatus(null);
            this.sateliteList = this.gpsStatus.getSatellites();
        } catch (SecurityException e) {
            e.printStackTrace();
            return;
        }
    }

    private void loopOverSateliteList() {

        this.cleanSatList();

        if (this.gpsStatus != null) {

            for(GpsSatellite satInfo : this.sateliteList){
                this.createSatOption(satInfo);
            }
        }
        this.createTableHead();

    }

    private void createTableHead(){

        TableRow row = new TableRow(this);
        LinearLayout linearLayout = new LinearLayout(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);

        TextView pnr = new TextView(this);
        TextView azimuth = new TextView(this);
        TextView elevation = new TextView(this);
        TextView power = new TextView(this);

        pnr.setText("PR.Number");
        azimuth.setText("Azimuth");
        elevation.setText("Elevation");
        power.setText("Power");

        this.styleTextView(power);
        this.styleTextView(azimuth);
        this.styleTextView(elevation);
        this.styleTextView(pnr);

        linearLayout.addView(pnr,0);
        linearLayout.addView(azimuth, 1);
        linearLayout.addView(elevation, 2);
        linearLayout.addView(power, 3);

        row.addView(linearLayout);
        this.table.addView(row, 0);
    }

    private void cleanSatList(){
        this.table.removeAllViews();
    }

    private void createSatOption(GpsSatellite satInfo){

        if(satInfo.getAzimuth()*satInfo.getElevation()!=0) {

            TableRow row = new TableRow(this);
            LinearLayout linearLayout = new LinearLayout(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);

            TextView pnr = new TextView(this);
            TextView azimuth = new TextView(this);
            TextView elevation = new TextView(this);
            TextView power = new TextView(this);

            pnr.setText(valueOf(satInfo.getPrn()));
            azimuth.setText(valueOf(satInfo.getAzimuth()));
            elevation.setText(valueOf(satInfo.getElevation()));
            power.setText(valueOf(satInfo.getSnr()));

            this.styleTextView(power);
            this.styleTextView(azimuth);
            this.styleTextView(elevation);
            this.styleTextView(pnr);

            linearLayout.addView(pnr,0);
            linearLayout.addView(azimuth, 1);
            linearLayout.addView(elevation, 2);
            linearLayout.addView(power, 3);

            row.addView(linearLayout);
            this.table.addView(row, 0);

        }
    }

    private void styleTextView(TextView tv){

        tv.setMinWidth(150);
        tv.setPadding(10,5,10,5);

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
            this.locManager.requestLocationUpdates(this.locPro.getName(), 3000, 1, this);
            locManager.addGpsStatusListener((GpsStatus.Listener) this);


        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }
    public void desativaGPS(){
        try {
            locManager.removeUpdates(this);
            locManager.removeGpsStatusListener((GpsStatus.Listener) this);

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.desativaGPS();
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


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        this.loopOverSateliteList();

    }

    @Override
    public void onProviderEnabled(String provider) {
//        this.addGpsListener();
//        this.loopOverSateliteList();
//        Log.d("CREATION", provider);
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onGpsStatusChanged(int event) {
        this.addGpsListener();
        this.loopOverSateliteList();
    }
}
