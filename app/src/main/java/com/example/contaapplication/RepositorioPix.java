package com.example.contaapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RepositorioPix extends SQLiteOpenHelper {
    public RepositorioPix(@Nullable Context context) {
        super(context, "Pix", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlPix = "CREATE TABLE pix (id Integer not null primary key, chave text, valorChave text)";
        sqLiteDatabase.execSQL(sqlPix);
        Log.i("pix", "Tabela 'pix' criada");


    }

    public void adicionarChavePix( Pix pix)
    {
        String sqlPix = "insert into pix  values (null,'" + pix.getChave() + "', '" + pix.getValorChave() + "')";
        super.getWritableDatabase().execSQL(sqlPix);
        Log.i("pix", "SQL insert pix: " + sqlPix);

    }

    public List<Pix> listarChavesPix()
    {
        ArrayList<Pix> lista = new ArrayList<Pix>();
        String sql = "select * from pix";
        Cursor cursor = getWritableDatabase().rawQuery(sql, null);
        cursor.moveToFirst();

        for(int i=0; i < cursor.getCount(); i++){
            Pix pix = new Pix();
            pix.setId(cursor.getInt(0));
            pix.setChave(cursor.getString(1));
            pix.setValorChave(cursor.getString(2));
            lista.add(pix);
            cursor.moveToNext();
        }

        cursor.close();
        return  lista;
    }

    public Pix buscarChavePix(Integer id) {
        String sql = "select * from pix where id = " + id;
        Cursor cursor = getWritableDatabase().rawQuery(sql,null);
        cursor.moveToFirst();

        Pix pix = null;

        for(int i=0; i < cursor.getCount(); i++){
            pix = new Pix();
            pix.setId(cursor.getInt(0));
            pix.setChave(cursor.getString(1));
            pix.setValorChave(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return pix;
    }


    public void removerChavePix(Integer id)
    {
        String sql = "delete from pix where id =" + id;
        getWritableDatabase().execSQL(sql);
        Log.i("Pix","SQL delete chave pix: " + sql);
    }

    public boolean existeChavePix(String chave) {
        String sql = "select COUNT(*) from pix where chave = '" + chave + "'";
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);
        boolean existe = false;

        if (cursor.moveToFirst()) {
            existe = cursor.getInt(0) > 0;
        }

        cursor.close();
        return existe;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
