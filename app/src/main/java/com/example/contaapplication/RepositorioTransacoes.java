package com.example.contaapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RepositorioTransacoes extends SQLiteOpenHelper {


    public RepositorioTransacoes(@Nullable Context context) {
        super(context, "transacoes", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table transacoes (id Integer not null primary key, Text tipo, Real valor)";
        sqLiteDatabase.execSQL(sql);
        Log.i("transacoes", "tabela criada com sucesso");


    }


    //insere as transações
    public void adicionarTransacao(Transacoes transacoes){
        String sql = "insert into transacoes values(null,'" + transacoes.getTipo() + "'," + transacoes.getValor() + ")";
        Log.i("transacoes","SQL insert transacoes: " + sql);
        super.getWritableDatabase().execSQL(sql);
    }

    //mostra a lista de transações
    public List<Transacoes> listarTransacoes(){
        ArrayList<Transacoes> lista = new ArrayList<Transacoes>();
        String sql = "select * from transacoes";
        Cursor cursor = getWritableDatabase().rawQuery(sql,null);
        cursor.moveToFirst();
        for(int i=0; i < cursor.getCount(); i++){

            Transacoes transacoes = new Transacoes(cursor.getString(0),cursor.getFloat(2));
            transacoes.setId(cursor.getInt(0)); // coluna 0

            lista.add(transacoes);

            cursor.moveToNext();
        }
        cursor.close();
        return lista;
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
