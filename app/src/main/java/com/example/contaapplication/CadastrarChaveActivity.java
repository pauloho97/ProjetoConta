package com.example.contaapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CadastrarChaveActivity extends AppCompatActivity {

    private EditText editTextPix; //valor digitado pelo usuário
    private Spinner spinnerPix; //opção spinner que o usuário escolhe
    private boolean isEditing = false; // Flag para evitar loops infinitos
    RepositorioPix repositorioPix; //variável do RepositórioPix


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_chave);

        editTextPix = findViewById(R.id.EditTextChavePix);
        spinnerPix = findViewById(R.id.spinnerDoPix);

        repositorioPix = new RepositorioPix(this);

        configuraçãoSpinner();
        setupTextWatcher();

        setTitle("Cadastre sua chave");
    }

    // Método para configurar o spinner
    public void configuraçãoSpinner() {
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.spinnerPix, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPix.setAdapter(arrayAdapter);
    }

    // Método para aplicar a máscara enquanto o usuário digita
    private void setupTextWatcher() {
        editTextPix.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isEditing) {
                    mascaraspinner(); // Chama o método para aplicar a máscara
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não é necessário implementar
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Não é necessário implementar
            }
        });
    }

    public void CadastrarPix(View view) {
        //valida se o usuário digitou correto
        validarValorPix();

        //envia o valor que o usuário digitou para a lista
        EnviarChavePixAoBD();
    }

    public boolean validarValorPix() {
        // Não deixa passar campo vazio
        if (editTextPix.getText().toString().isEmpty()) {
            Toast.makeText(this, "Digite algo", Toast.LENGTH_SHORT).show();

            editTextPix.setText("");
            return false;
        } else if (editTextPix.length() < 11) {
            Toast.makeText(this, "[ERRO] Preencha 11 números", Toast.LENGTH_SHORT).show();

            return false;
        }

        Toast.makeText(this, "Chave cadastrada com sucesso", Toast.LENGTH_SHORT).show();

        editTextPix.setText("");
        return true;
    }

    public void mascaraspinner() {
        if (spinnerPix.getSelectedItem() != null) {
            String input = editTextPix.getText().toString();
            String apenasNumeros = input.replaceAll("\\D", ""); // Mantém apenas números
            StringBuilder formatado = new StringBuilder();

            if (spinnerPix.getSelectedItem().equals("CPF")) {
                formatarCPF(apenasNumeros, formatado);

            } else if (spinnerPix.getSelectedItem().equals("CELULAR")) {
                formatarCelular(apenasNumeros, formatado);

            }

            // Debug: Verificar se o texto foi formatado corretamente
            Log.d("PixDebug", "Texto formatado: " + formatado.toString());


            // Apenas atualiza o texto se formatado não estiver vazio
            if (formatado.length() > 0) {
                isEditing = true; // Sinaliza que estamos editando
                editTextPix.setText(formatado.toString());
                editTextPix.setSelection(formatado.length()); // Coloca o cursor no final
                isEditing = false; // Reseta o sinalizador
            }
        }
    }

    private void formatarCPF(String apenasNumeros, StringBuilder formatado) {

        for (int i = 0; i < Math.min(apenasNumeros.length(), 11); i++) {
            if (i == 3 || i == 6) {
                formatado.append('.');
            } else if (i == 9) {
                formatado.append('-');
            }
            formatado.append(apenasNumeros.charAt(i));
        }
    }

    private void formatarCelular(String apenasNumeros, StringBuilder formatado) {


        for (int i = 0; i < Math.min(apenasNumeros.length(), 11); i++) {
            if (i == 0) {
                formatado.append("(");
            } else if (i == 2) {
                formatado.append(") ");
            } else if (i == 7) {
                formatado.append("-");
            }
            formatado.append(apenasNumeros.charAt(i));
        }
    }

    private void EnviarChavePixAoBD(){
        String chave = editTextPix.getText().toString();
        String valorChave = spinnerPix.getSelectedItem().toString();


        // Debug: Verificar o valor capturado da chave
        Log.d("PixDebug", "Chave recebida do EditText: " + chave);

        Pix pix = new Pix(null,chave,valorChave);
        repositorioPix.adicionarChavePix(pix);

        // Verificar se o banco de dados recebeu os dados corretamente
        Log.d("PixDebug", "Chave salva no banco: " + chave);

    }
}
