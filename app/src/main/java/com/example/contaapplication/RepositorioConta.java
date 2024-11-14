package com.example.contaapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

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
    }

    // Adiciona saldo na tabela
    public void adicionarSaldo(Conta conta) {
        SQLiteDatabase db = getWritableDatabase(); //Cria o BD ou modifica
        String sql = "INSERT INTO conta (valor) VALUES (" + conta.getSaldo() + ")";
        Log.i("conta", "SQL insert conta: " + sql);
        db.execSQL(sql);
    }

    // Obtém o saldo da tabela
    public Float obterSaldo() {
        SQLiteDatabase db = getReadableDatabase(); //Cria o BD ou modifica
        // Recupera o saldo armazenado
        Cursor cursor = db.rawQuery("SELECT valor FROM conta LIMIT 1", null);

        // Se houver dados, retorna o saldo, caso contrário, retorna 0.0f
        return cursor.moveToFirst() ? cursor.getFloat(0) : 0.0f;
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
