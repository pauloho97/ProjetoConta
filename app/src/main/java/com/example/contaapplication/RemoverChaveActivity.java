package com.example.contaapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RemoverChaveActivity extends AppCompatActivity {
    private EditText editTextPix;
    private boolean isEditing = false; // Flag para evitar loops infinitos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_remover_pix);

        editTextPix = findViewById(R.id.EditTextRemoverChavePix);

        setTitle("Remova sua chave");


    }


    public void RemoverPix(View view) {
        validarValorPix();

    }

    public boolean validarValorPix() {
        // Não deixa passar campo vazio
        if (editTextPix.getText().toString().isEmpty()) {
            Toast.makeText(this, "Digite algo", Toast.LENGTH_SHORT).show();

            editTextPix.setText("");
            return false;
        } else if (editTextPix.length() < 11) {
            Toast.makeText(this, "[ERRO] Preencha 11 números", Toast.LENGTH_SHORT).show();

            editTextPix.setText("");
            return false;
        }

        Toast.makeText(this, "Chave removida com sucesso", Toast.LENGTH_SHORT).show();

        editTextPix.setText("");
        return true;
    }

    private void RemoverChavePixALista(){

    }

}