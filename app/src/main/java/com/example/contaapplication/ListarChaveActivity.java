package com.example.contaapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class ListarChaveActivity extends AppCompatActivity {
    private ArrayList<Pix> listaChaves;
    private RepositorioPix repositorioPix;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_chave);

        setTitle("CHAVES PIX");

        repositorioPix = new RepositorioPix(this);

        listView = findViewById(R.id.idListaDasChaves);

        List<Pix> listaDB = repositorioPix.listarChavesPix();

        String[] dados = new String[listaDB.size()];

        if (listaDB != null) {
            for (int i = 0; i < listaDB.size(); i++) {
                Pix pix = listaDB.get(i);
                dados[i] = pix.toString();
            }

        }

        //usando o Adapter para ligar a o vetor da lista com a parte gráfica.
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,dados);

        listView.setAdapter(adapter);

    }
}