package com.example.ramonmoraes.gpsfaculdade;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static com.example.ramonmoraes.gpsfaculdade.R.id.map;

public class MainActivity extends AppCompatActivity {

    public Integer precisao; //1 = grao-decimal , 2 = grau-minuto decimal , 3 = grau-minuto-segundo decimal
    public String unidadeVelocidade; // Km/h ou Mph
    public String orientacaoMapa;
    public Integer tipoMapa; // 1= vetorial , 2 = imagem satelite;
    public Boolean informacaoTrafico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnMap=(Button)findViewById(R.id.btnMap);

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity("map");
            }
        });

        Button btnConfig=(Button)findViewById(R.id.btnConfig);
        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity("config");
            }
        });

        Button btnCredits=(Button)findViewById(R.id.btnCredits);
        btnCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity("credits");
            }
        });

        Button btnSatelite=(Button)findViewById(R.id.btnSatelite);
        btnSatelite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity("satelite");
            }
        });

    }

    private void changeActivity(String act){
        Intent intent;
        switch (act){

            case "config":
                intent = new Intent(this, ConfigActivity.class);
                startActivity(intent);
                break;
            case "map":
                 intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                break;
            case "credits":
                 intent = new Intent(this, CreditsActivity.class);
                startActivity(intent);
                break;
            case "satelite":
                 intent = new Intent(this, SateliteActivity.class);
                startActivity(intent);
                break;
            default:
                 intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
        }
    }
}
