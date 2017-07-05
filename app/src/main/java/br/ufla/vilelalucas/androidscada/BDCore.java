package br.ufla.vilelalucas.androidscada;

/**
 * Created by Lucas on 23/01/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;

import br.ufla.vilelalucas.androidscada.Tags.PlcTag;

public class BDCore extends SQLiteOpenHelper {
    private static final String NOME_BD = "scada";
    private static final int VERSAO_BD = 30;


    public BDCore(Context ctx) {
        super(ctx, NOME_BD, null, VERSAO_BD);
    }


    @Override
    public void onCreate(SQLiteDatabase bd) {

        bd.execSQL("create table cadTag(_id integer primary key autoincrement, nome text UNIQUE, endereco text not null, rw integer not null, tipo integer not null );");
        bd.execSQL("create table plcList(_id integer primary key autoincrement, nome text UNIQUE, ip text not null, rack integer not null, slot integer not null );");
        bd.execSQL("create table cadAlarm(_id integer primary key autoincrement, mensagem text not null, equipamento text not null,tagID integer, area text not null, action text, prioridade int, valorRef real,tipo integer not null,  FOREIGN KEY(tagID) REFERENCES cadTag(_id) );");
        bd.execSQL("create table exemplo(_id integer primary key autoincrement, tag1 integer,tag2 integer, tag3 integer, tag4 integer, tag5 integer, tag6 integer, tag7 integer, tag8 integer, tag9 integer,tag10 integer, tag11 integer, tag12 integer, tag13 integer, tag14 integer, tag15 integer);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int arg1, int arg2) {
        ArrayList<PlcTag> list = new ArrayList<PlcTag>();
        try {
            String[] colunas = new String[]{"_id", "nome", "endereco", "tipo"};
            Cursor cursor = this.getReadableDatabase().query("cadTag", colunas, null, null, null, null, "nome ASC");
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    PlcTag tag = PlcTag.createTag(cursor.getString(1), cursor.getString(2), cursor.getInt(3));
                    tag.setID(cursor.getLong(0));
                    list.add(tag);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
        }

        bd.execSQL("DROP TABLE IF EXISTS cadTag;");
        bd.execSQL("DROP TABLE IF EXISTS cadAlarm;");
        bd.execSQL("DROP TABLE IF EXISTS plcList;");
        bd.execSQL("DROP TABLE IF EXISTS exemplo;");
        onCreate(bd);

        for (int i = 0; i < list.size(); i++) {
            inserir(list.get(i));
        }

    }

    public void inserir(PlcTag tag) {

        ContentValues valores = new ContentValues();
        valores.put("nome", tag.getTagName());
        valores.put("endereco", tag.getTagAddress());
        valores.put("rw", tag.getRW());
        valores.put("tipo", tag.getType());
        this.getWritableDatabase().insert("cadTag", null, valores);

    }

}
