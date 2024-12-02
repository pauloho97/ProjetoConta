package com.example.contaapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class RemoverChaveActivity extends AppCompatActivity {
    private EditText editTextId;
    private RepositorioPix repositorioPix;
    private Integer idInt;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_remover_pix);

        editTextId = findViewById(R.id.EditTextRemoverChavePix);
        repositorioPix = new RepositorioPix(this);

        listaDasChavesAtualizada();

        setTitle("Remova sua chave");

    }


    public void RemoverPix(View view) {

        //método para validar o valor
        validarValorPix();

        //método para remover a chave
        RemoverChavePixALista();

        //método das listas atualizadas
        listaDasChavesAtualizada();

    }

    public boolean validarValorPix() {


        // Não deixa passar campo vazio
        if (editTextId.getText().toString().isEmpty()) {
            Toast.makeText(this, "Digite algo", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            String id = editTextId.getText().toString();
            idInt = Integer.parseInt(id);

            editTextId.setText("");

            return true;
        }catch (NumberFormatException e) {
            Toast.makeText(this, "Digite apenas números", Toast.LENGTH_SHORT).show();
            return false;
        }



    }

    private void RemoverChavePixALista(){
        Pix pix = repositorioPix.buscarChavePix(idInt);
        if(pix == null){
            Toast.makeText(this, "id não encontrado", Toast.LENGTH_SHORT).show();
            return;
        }
        repositorioPix.removerChavePix(idInt);

        Toast.makeText(this, "Chave removida com sucesso", Toast.LENGTH_SHORT).show();

    }

    public void listaDasChavesAtualizada(){

        listView = findViewById(R.id.idListaDasChaves2);

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