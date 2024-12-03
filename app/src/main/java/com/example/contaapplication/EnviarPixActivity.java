package com.example.contaapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EnviarPixActivity extends AppCompatActivity {

    private EditText campoChave;
    private EditText campoValor;
    private RepositorioPix repositorioPix;
    private RepositorioConta repositorioConta;
    private RepositorioTransacoes repositorioTransacoes;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_pix);
        //pegar os campos
        campoChave = findViewById(R.id.ChavePix);
        campoValor = findViewById(R.id.Valor);
        //obj repositorios
        repositorioPix = new RepositorioPix(this);
        repositorioConta = new RepositorioConta(this);
        repositorioTransacoes = new RepositorioTransacoes(this);

        setTitle("PIX");
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

        Toast.makeText(this, "PIX enviado com sucesso!", Toast.LENGTH_SHORT).show();
    }

    public boolean validarCampos(String chavePix, String valorPix) {
        if (chavePix.isEmpty()) {
            Toast.makeText(this, "Erro: Informe a Chave Pix CPF/CELULAR",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (chavePix.length() != 11) {
            Toast.makeText(this, "Erro: Chave Pix não Encontrada, Deve ter 11 dígitos ",
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
}