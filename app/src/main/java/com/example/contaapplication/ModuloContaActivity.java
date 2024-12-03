package com.example.contaapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Locale;

public class ModuloContaActivity extends AppCompatActivity {

    private Conta conta;
    private Spinner opcoesSpinner;
    private ArrayAdapter transacaoAdapter;
    private EditText valorText;
    private Float valor;
    private TextView ultSaldo;

    private RepositorioTransacoes repositorioTransacoes;
    private RepositorioConta repositorioConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modulo_conta);

        setTitle("CONTA");

        conta = new Conta(); // Cria a conta aqui

        repositorioTransacoes = new RepositorioTransacoes(this);
        repositorioConta = new RepositorioConta(this);


        ultSaldo = findViewById(R.id.textViewSaldo);
        valorText = findViewById(R.id.Valor); // Inicializa o EditText

        atualizarLista();


        opcoesSpinner = findViewById(R.id.spinnerConta);
        configuraçãoSpinner();

        exibirSaldo();
    }

    // Método para configurar o spinner
    public void configuraçãoSpinner() {
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.spinnerAlternativo, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opcoesSpinner.setAdapter(arrayAdapter);
    }

    private void atualizarLista(){
        ListView listView = findViewById(R.id.ListViewConta);
        //pega a lista do BD e passa para o mobile
        List<Transacoes> listaDB = repositorioTransacoes.listarTransacoes();


        //pegando os dados pelo tamanho da lista(size)
        String [] dados = new String[listaDB.size()];

        // Verifica se há transações e adiciona todas à lista
        for (int i = 0; i < listaDB.size(); i++){
            Transacoes transacoes = listaDB.get(i);
            dados[i] = transacoes.toString();

        }

        //adapter para obter a lista a tela
        transacaoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dados);
        listView.setAdapter(transacaoAdapter);
    }

    public void EnviarValor(View view) {

        // Valida o valor de entrada do usuário
        if (!validarValor()) {
            return; // Interrompe a execução se a validação falhar
        }
        Log.d("Debug", "Valor validado: " + valor);

        //processa a transação na opcao do spinner e mostra a lista
        processarTransacao();
        Log.d("Debug", "Transação processada com sucesso.");

        // exibe o saldo
        exibirSaldo();

        //atualiza a lista
        atualizarLista();
        Log.d("Debug", "Saldo exibido.");

    }

    public boolean validarValor(){
        // Verifica se o campo de texto está vazio
        if (valorText.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.campo_vazio, Toast.LENGTH_SHORT).show();
            return false;
        }

        // Tenta converter o texto em um Float
        try {
            valor = Float.parseFloat(valorText.getText().toString().trim());

            // Verifica se o valor é positivo
            if (valor <= 0) {
                Toast.makeText(this, R.string.valor_positivo, Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.digite_apenas_n_meros, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void processarTransacao(){

        Float novoSaldo = conta.getSaldo();
        String valorFormatado;


        // Verifica a opção selecionada no Spinner
        if (opcoesSpinner.getSelectedItem().toString().equals("DEPOSITAR")) {
            novoSaldo += valor; // Atualiza o saldo
            conta.setSaldo(novoSaldo); // Define o novo saldo


            repositorioConta.atualizarSaldo(conta); // Atualiza o saldo na base de dados

            Transacoes transacoes = new Transacoes("DEPOSITO",valor);
            repositorioTransacoes.adicionarTransacao(transacoes); // Adiciona a transação de depósito

        } else if (opcoesSpinner.getSelectedItem().toString().equals("RETIRAR")) {
            if (valor > novoSaldo) {
                Toast.makeText(this, "[ERRO], valor retirado maior que o saldo", Toast.LENGTH_SHORT).show();
                return;
            } else {
                novoSaldo -= valor; // Atualiza o saldo
                conta.setSaldo(novoSaldo); // Define o novo saldo

                repositorioConta.atualizarSaldo(conta); // Atualiza o saldo na base de dados

                Transacoes transacoes = new Transacoes("RETIRADO",valor);
                repositorioTransacoes.adicionarTransacao(transacoes); // Adiciona a transação de retirada

            }
        }

        // Log para verificar as transações
        Log.d("Transacoes", "Transação adicionada: " + opcoesSpinner.getSelectedItem().toString() + " R$ " + valor);

       // Formata o saldo: 0.00
        //valorFormatado = String.format(Locale.US, "%.2f", novoSaldo);



       /* Float saldo = repositorioConta.obterSaldo();  // Obtém o saldo atual da conta
        String saldoFormatado = String.format(Locale.US, "%.2f", saldo); // Formata o saldo
        ultSaldo.setText("Saldo atual R$ " + valorFormatado);*/

        exibirSaldo();
        valorText.setText(""); // Limpa o campo
    }

    // Método para exibir o saldo atual na interface
    public void exibirSaldo() {
        Float saldo = repositorioConta.obterSaldo();  // Obtém o saldo atual da conta
        conta.setSaldo(saldo); // Define o saldo da conta);
        String saldoFormatado = String.format(Locale.getDefault(), "%.2f", saldo); // Formata o saldo
        ultSaldo.setText("Saldo atual R$ " + saldoFormatado);  // Exibe o saldo na TextView*/
    }
}
