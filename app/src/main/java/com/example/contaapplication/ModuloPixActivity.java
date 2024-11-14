package com.example.contaapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ModuloPixActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modulo_pix);

        setTitle("PIX");
    }

    public void EnviarPix(View view){
        Intent intent = new Intent(this, EnviarPixActivity.class);
        startActivity(intent);
    }

    public void cadastrarChave(View view){
        Intent intent = new Intent(this, CadastrarChaveActivity.class);
        startActivity(intent);
    }

    public void RemoverChave(View view){
        Intent intent = new Intent(this, RemoverChaveActivity.class);
        startActivity(intent);
    }

    public void ListarChave(View view){
        Intent intent = new Intent(this, ListarChaveActivity.class);
        startActivity(intent);
    }
}