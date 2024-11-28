package com.example.contaapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RepositorioConta extends SQLiteOpenHelper {

    public RepositorioConta(@Nullable Context context) {
        super(context, "conta", null, 2); // versão 2 do banco de dados
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Criação da tabela 'conta' com a coluna 'valor'
        String sqlConta = "CREATE TABLE conta (valor REAL)";
        sqLiteDatabase.execSQL(sqlConta);
        Log.i("conta", "Tabela 'conta' criada");

        // Inserir o saldo inicial (se necessário)
        String sqlInserirSaldoInicial = "INSERT INTO conta (valor) VALUES (0.0)";
        sqLiteDatabase.execSQL(sqlInserirSaldoInicial);
        Log.i("conta", "Saldo inicial inserido na tabela 'conta'");
    }

    // Adiciona saldo na tabela
    public void adicionarSaldo(Conta conta) {
        //SQLiteDatabase db = getWritableDatabase(); //Cria o BD ou modifica
        String sql = "INSERT INTO conta (valor) VALUES (" + conta.getSaldo() + ")";
        super.getWritableDatabase().execSQL(sql);
        Log.i("conta", "Saldo atualizado " + sql);
       // db.execSQL(sql);
    }

    // Obtém o saldo da tabela
    public Float obterSaldo() {
        String sql = "SELECT valor FROM conta LIMIT 1";
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);

        Float saldo = 0.0f;  // Valor padrão em caso de não encontrar dados
        if (cursor.moveToFirst()) { // Verifica se há registros no cursor
            saldo = cursor.getFloat(0);  // Obtém o valor do saldo (primeira coluna)
        }

        Log.d("Saldo Atual", "Valor recuperado do banco: " + saldo);
        cursor.close();
        return saldo;
    }

    //atualiza o saldo da conta
    public Float atualizarSaldo(Conta conta) {
        String sql = "UPDATE conta SET valor = " + conta.getSaldo();
        super.getWritableDatabase().execSQL(sql);
        Log.i("conta", "Saldo atualizado " + sql);

        return conta.getSaldo();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No upgrade, pode-se adicionar ou modificar tabelas, mas neste caso, não há mudanças de estrutura.
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE conta ADD COLUMN saldo REAL DEFAULT 0");
            Log.i("conta", "Coluna 'saldo' adicionada na tabela 'conta'");
        }
    }
}
