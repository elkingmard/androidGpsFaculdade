package com.example.ramonmoraes.gpsfaculdade;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ConfigActivity extends AppCompatActivity {

    private Integer precisao; //1 = grao-decimal , 2 = grau-minuto decimal , 3 = grau-minuto-segundo decimal
    private Integer velocidade; // Km/h ou Mph
    private Integer orientacao;
    private Integer tipoMapa; //1= vetorial , 2 = imagem satelite;
    private Boolean trafego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
    }

    private void saveConfigPref(){
        SharedPreferences sharedPref = getSharedPreferences("gpsConfig",CONTEXT_IGNORE_SECURITY);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt("precisao",this.precisao);
        editor.putInt("velocidade",this.velocidade);
        editor.putInt("orientacao",this.orientacao);
        editor.putInt("tipoMapa",this.tipoMapa);
        editor.putBoolean("trafego",this.trafego);
        editor.commit();
    }

    private void modifyAtributesValues(){

    }

}
