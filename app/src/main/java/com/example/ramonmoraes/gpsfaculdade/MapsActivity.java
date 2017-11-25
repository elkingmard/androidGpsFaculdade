package com.example.ramonmoraes.gpsfaculdade;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
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

    private Integer precisaoConfig; //1 = grao-decimal , 2 = grau-minuto decimal , 3 = grau-minuto-segundo decimal
    private Integer velocidadeConfig; // 1= Km/h , 2 = Mph
    private Integer tipoMapaConfig; //1= vetorial , 2 = imagem satelite;
    private Integer orientacaoConfig;//1-nenhuma 2 - north up , 3-course up
    private Boolean trafegoConfig;//true ativado , false=desativado

    private TextView mapLat;
    private TextView mapLong;
    private TextView mapAlt;
    private TextView mapSpeed;

    private LocationManager locManager; // O Gerente de localização
    private LocationProvider locPro; // Provedor de localização
    private Location location;
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
        instanceTextView();
        readSharedPrefs();
    }

    private void instanceTextView(){

        mapLat = (TextView) findViewById(R.id.textLatMap);
        mapLong = (TextView) findViewById(R.id.textLongMap);
        mapAlt = (TextView) findViewById(R.id.textAltMap);
        mapSpeed = (TextView) findViewById(R.id.textSpeedMap);
        
    }


    private void readSharedPrefs(){
        SharedPreferences sharedPref = getSharedPreferences("gpsConfig",CONTEXT_IGNORE_SECURITY);
        this.precisaoConfig = sharedPref.getInt("precisao", 1);
        this.velocidadeConfig = sharedPref.getInt("velocidade", 1);
        this.orientacaoConfig = sharedPref.getInt("orientacao", 1);
        this.tipoMapaConfig = sharedPref.getInt("tipoMapa", 1);
        this.trafegoConfig = sharedPref.getBoolean("trafego",true);
    }

    public void setInfosOverMap(){
        this.mapLat.setText( "latitude: " + this.getModifiedCord(this.latitude) );
        this.mapLong.setText( "longitude: " + this.getModifiedCord(this.longitude) );
        this.mapAlt.setText( "altitude: " + this.altitude );
        this.mapSpeed.setText( "Velocidade: " + this.getModifiedSpeed());
    }

    private String getModifiedCord(Double cord){
        String latString = Double.toString(cord);
        String newLatString;
        switch (this.precisaoConfig){
            case 1 :
                newLatString = Location.convert(Double.parseDouble(latString), Location.FORMAT_DEGREES);
                break;
            case 2:
                newLatString = Location.convert(Double.parseDouble(latString), Location.FORMAT_MINUTES);
            break;
            case 3 :
                newLatString = Location.convert(Double.parseDouble(latString), Location.FORMAT_SECONDS);
                break;
            default:
                newLatString = Location.convert(Double.parseDouble(latString), Location.FORMAT_SECONDS);
                break;
        }
        return newLatString;
    }

    private String getModifiedSpeed(){
        String newSpeed = Double.toString(this.speed); // dado original em m/s
        String speedKMH = Double.toString(this.speed * 3.6)+"Km/hr";
        String speedMPH = Double.toString(this.speed * 2.23694)+"Mph";
        newSpeed = (this.velocidadeConfig==1) ? speedKMH : speedMPH;
        return newSpeed;
    }
    public void modifyBearing(){

        if(this.orientacaoConfig==2){
            this.location.setBearing(this.location.getBearing());
        }
        if(this.orientacaoConfig==3){
            // usar bearingTo até um ponto predestinado;
        }

    }

    public void setCellLocation() {
        try {
            String provider = locManager.PASSIVE_PROVIDER;
            //String provider = locManager.GPS_PROVIDER; testar em outro celular o meu gps é quebrado;

            this.location = this.locManager.getLastKnownLocation(provider.toString());
            if(location !=null){
                this.latitude = this.location.getLatitude();
                this.longitude = this.location.getLongitude();
                this.speed = this.location.getSpeed();
                this.altitude = this.location.getAltitude();

                this.setInfosOverMap();
                this.modifyBearing();
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

    private void modifieMapType(){
        switch (this.tipoMapaConfig){
            case 1:
                this.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case 2:
                this.mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            default:
                this.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
        }
    }

    private void modifiedTrafficMap(){
        this.mMap.setTrafficEnabled((this.trafegoConfig));
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
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng cellPhoneLocation = new LatLng(this.latitude, this.longitude);
        mMap.addMarker(new MarkerOptions().position(cellPhoneLocation).title("Você esta aqui"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cellPhoneLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));

        this.modifieMapType();
        this.modifiedTrafficMap();
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
