package com.example.ramonmoraes.gpsfaculdade;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class ConfigActivity extends AppCompatActivity {

    private Integer precisao; //1 = grao-decimal , 2 = grau-minuto decimal , 3 = grau-minuto-segundo decimal
    private Integer velocidade; // Km/h ou Mph
    private Integer orientacao;
    private Integer tipoMapa; //1= vetorial , 2 = imagem satelite;
    private Boolean trafego;


    private RadioButton btnPrecisao1;
    private RadioButton btnPrecisao2;
    private RadioButton btnPrecisao3;

    private RadioButton btnVel1;
    private RadioButton btnVel2;

    private RadioButton btnMapType1;
    private RadioButton btnMapType2;

    private RadioButton btnOrientacao1;
    private RadioButton btnOrientacao2;
    private RadioButton btnOrientacao3;

    private Button btnTrafego;

    private Button btnSalvar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        instanceButtons();

        btnSalvar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                modifyAtributesValues();
            }
        });

    }


    public void instanceButtons(){

        this.btnPrecisao1 = (RadioButton)findViewById(R.id.btnPrecisao1);
        this.btnPrecisao2 = (RadioButton)findViewById(R.id.btnPrecisao2);
        this.btnPrecisao3 = (RadioButton)findViewById(R.id.btnPrecisao3);

        this.btnVel1 = (RadioButton)findViewById(R.id.btnVel1);
        this.btnVel2 = (RadioButton)findViewById(R.id.btnVel2);

        this.btnMapType1 = (RadioButton)findViewById(R.id.btnMapType1);
        this.btnMapType2 = (RadioButton)findViewById(R.id.btnMapType2);

        this.btnOrientacao1 = (RadioButton)findViewById(R.id.btnOrientacao1);
        this.btnOrientacao2 = (RadioButton)findViewById(R.id.btnOrientacao2);
        this.btnOrientacao3 = (RadioButton)findViewById(R.id.btnOrientacao3);

        this.btnTrafego = (Button)findViewById(R.id.btnTrafego);
        this.btnSalvar = (Button)findViewById(R.id.btnSalvar);
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
        if(btnPrecisao1.isChecked()) this.precisao=1;
        if(btnPrecisao2.isChecked()) this.precisao=2;
        if(btnPrecisao3.isChecked()) this.precisao=3;

        if(btnVel1.isChecked()) this.velocidade=1;
        if(btnVel2.isChecked()) this.velocidade=2;

        if(btnOrientacao1.isChecked()) this.orientacao=1;
        if(btnOrientacao2.isChecked()) this.orientacao=2;
        if(btnOrientacao3.isChecked()) this.orientacao=3;

        if(btnMapType1.isChecked()) this.tipoMapa=1;
        if(btnMapType2.isChecked()) this.tipoMapa=2;

        this.trafego = (btnTrafego.isActivated()) ? true : false;

        saveConfigPref();
    }

    private void loadConfigPref(){

    }

}
