package com.example.contaapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ModulosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modulos);

        setTitle("Módulos da conta");
    }

    public void Acessarconta(View view){
        Intent intent = new Intent(this, ModuloContaActivity.class);
        startActivity(intent);
    }

    public void Acessarpix(View view){
        Intent intent = new Intent(this, ModuloPixActivity.class);
        startActivity(intent);
    }


}