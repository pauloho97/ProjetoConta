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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class EnviarPixActivity extends AppCompatActivity {

    private EditText campoChave;
    private EditText campoValor;
    private Conta conta;
    private Spinner spinner;
    private TextView ultSaldo;

    //variáveis para captar os dados do banco
    private RepositorioPix repositorioPix;
    private RepositorioConta repositorioConta;
    private RepositorioTransacoes repositorioTransacoes;
    private boolean isEditing = false; // Flag para evitar loops infinitos


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_pix);

        conta = new Conta();

        //pegar os campos
        campoChave = findViewById(R.id.ChavePix);
        campoValor = findViewById(R.id.Valor);
        spinner = findViewById(R.id.spinnerEnviarPix);
        ultSaldo = findViewById(R.id.textViewParaSaldo);

        //obj repositorios
        repositorioPix = new RepositorioPix(this);
        repositorioConta = new RepositorioConta(this);
        repositorioTransacoes = new RepositorioTransacoes(this);

        configuraçãoSpinner();
        setupTextWatcher();

        exibirSaldo();

        setTitle("PIX");
    }

    // Método para configurar o spinner
    public void configuraçãoSpinner() {
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.spinnerEnviarPix, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    // Método para aplicar a máscara enquanto o usuário digita
    private void setupTextWatcher() {
        campoChave.addTextChangedListener(new TextWatcher() {
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

    public void enviarPix(View view) {
        // Pegar o texto dos campos
        String chavePix = campoChave.getText().toString();
        String valorPix = campoValor.getText().toString();

        if (!validarCampos(chavePix, valorPix)) {
            return;
        }

        // Verifica se o usuário possui pelo menos uma chave PIX cadastrada
        if (repositorioPix.listarChavesPix().isEmpty()) {
            Toast.makeText(this, "Erro: Você precisa cadastrar pelo menos uma chave PIX antes de enviar.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Verifica saldo e realiza o PIX
        Float saldoDisponivel = repositorioConta.obterSaldo();
        Float valor = Float.parseFloat(valorPix);

        if (valor > saldoDisponivel) {
            Toast.makeText(this, "Erro: Saldo insuficiente para realizar a transferência.",
                    Toast.LENGTH_SHORT).show();
            return;
        }


        Float novoSaldo = saldoDisponivel - valor;
        Conta conta = new Conta();
        conta.setSaldo(novoSaldo);
        repositorioConta.atualizarSaldo(conta);

        Transacoes transacoes = new Transacoes("PIX ENVIADO", valor);
        repositorioTransacoes.adicionarTransacao(transacoes);

        exibirSaldo();

        Toast.makeText(this, "PIX enviado com sucesso!", Toast.LENGTH_SHORT).show();
    }

    public boolean validarCampos(String chavePix, String valorPix) {
        if (chavePix.isEmpty()) {
            Toast.makeText(this, "Erro: Informe a Chave Pix CPF/CELULAR",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (chavePix.length() < 11) {
            Toast.makeText(this, " Erro: Chave Pix deve ter 11 números ",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (valorPix.isEmpty()) {
            Toast.makeText(this, "Erro: Informe o Valor do Pix",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            Float valor = Float.parseFloat(valorPix);
            if (valor <= 0) {
                Toast.makeText(this, "Erro: Valor deve ser maior que zero",
                        Toast.LENGTH_SHORT).show();
                return false;
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Erro: Valor informado não é válido",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true; // Se passou em todas as validações, retorna verdadeiro
    }

    public void mascaraspinner() {
        if (spinner.getSelectedItem() != null) {
            String input = campoChave.getText().toString();
            String apenasNumeros = input.replaceAll("\\D", ""); // Mantém apenas números
            StringBuilder formatado = new StringBuilder();

            if (spinner.getSelectedItem().equals("CPF")) {
                formatarCPF(apenasNumeros, formatado);

            } else if (spinner.getSelectedItem().equals("CELULAR")) {
                formatarCelular(apenasNumeros, formatado);

            }

            // Debug: Verificar se o texto foi formatado corretamente
            Log.d("PixDebug", "Texto formatado: " + formatado.toString());


            // Apenas atualiza o texto se formatado não estiver vazio
            if (formatado.length() > 0) {
                isEditing = true; // Sinaliza que estamos editando
                campoChave.setText(formatado.toString());
                campoChave.setSelection(formatado.length()); // Coloca o cursor no final
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

    // Método para exibir o saldo atual na interface
    public void exibirSaldo() {
        Float saldo = repositorioConta.obterSaldo();  // Obtém o saldo atual da conta
        conta.setSaldo(saldo); // Define o saldo da conta);
        String saldoFormatado = String.format(Locale.getDefault(), "%.2f", saldo); // Formata o saldo
        ultSaldo.setText("Saldo atual R$ " + saldoFormatado);  // Exibe o saldo na TextView*/
    }

}