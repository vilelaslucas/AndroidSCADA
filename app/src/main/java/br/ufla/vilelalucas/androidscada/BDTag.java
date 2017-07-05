package br.ufla.vilelalucas.androidscada;

/**
 * Created by Lucas on 23/01/2017.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.nfc.Tag;
import android.os.Build;

import br.ufla.vilelalucas.androidscada.Tags.PlcTag;

public class BDTag {
    private SQLiteDatabase bd;

    public BDTag(Context context) {
        BDCore auxBd = new BDCore(context);
        bd = auxBd.getWritableDatabase();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            bd.setForeignKeyConstraintsEnabled(true);
        }
    }


    public boolean inserir(PlcTag tag) {
        try {
            ContentValues valores = new ContentValues();
            valores.put("nome", tag.getTagName());
            valores.put("endereco", tag.getTagAddress());
            valores.put("rw", tag.getRW());
            valores.put("tipo", tag.getType());
            bd.insertOrThrow("cadTag", null, valores);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    public boolean atualizar(PlcTag tag) {
        try {
            ContentValues valores = new ContentValues();
            valores.put("nome", tag.getTagName());
            valores.put("endereco", tag.getTagAddress());
            valores.put("rw", tag.getRW());
            valores.put("tipo", tag.getType());
            bd.update("cadTag", valores, "_id = ?", new String[]{"" + tag.getID()});
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    public void deletar(PlcTag tag) {
        try {
            bd.delete("cadTag", "_id = " + tag.getID(), null);
        } catch (Exception e) {

        }
    }

    public PlcTag buscarTag(long id) {
        PlcTag tag = null;
        String[] colunas2 = new String[]{"_id", "nome", "endereco", "tipo", "rw"};
        Cursor cursor2 = bd.query("cadTag", colunas2, "_id=" + "'" + id + "'", null, null, null, null);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            tag = PlcTag.createTag(cursor2.getString(1), cursor2.getString(2), cursor2.getInt(3));
            tag.setID(cursor2.getLong(0));
            tag.setRW(cursor2.getInt(4));

        }
        return tag;
    }


    public ArrayList<PlcTag> buscar() {
        ArrayList<PlcTag> list = new ArrayList<PlcTag>();
        String[] colunas = new String[]{"_id", "nome", "endereco", "tipo", "rw"};

        Cursor cursor = bd.query("cadTag", colunas, null, null, null, null, "_id ASC");

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                PlcTag tag = PlcTag.createTag(cursor.getString(1), cursor.getString(2), cursor.getInt(3));
                tag.setID(cursor.getLong(0));
                tag.setRW(cursor.getInt(4));
                list.add(tag);

            } while (cursor.moveToNext());
        }

        return (list);
    }

    public String[] buscarNomes() {
        ArrayList<PlcTag> list = new ArrayList<PlcTag>();
        String[] colunas = new String[]{"_id", "nome", "endereco", "tipo"};

        Cursor cursor = bd.query("cadTag", colunas, null, null, null, null, "nome ASC");

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                PlcTag tag = PlcTag.createTag(cursor.getString(1), cursor.getString(2), cursor.getInt(3));
                tag.setID(cursor.getLong(0));
                list.add(tag);

            } while (cursor.moveToNext());
        }
        String[] lista = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            lista[i] = list.get(i).tagInfo();
        }

        return (lista);
    }

    public String[] buscarTipo(int tipo) {
        ArrayList<PlcTag> list = new ArrayList<PlcTag>();
        String[] colunas = new String[]{"_id", "nome", "endereco", "tipo"};

        Cursor cursor = bd.query("cadTag", colunas, "tipo=" + "'" + tipo + "'", null, null, null, "nome ASC");

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                PlcTag tag = PlcTag.createTag(cursor.getString(1), cursor.getString(2), cursor.getInt(3));
                tag.setID(cursor.getLong(0));
                list.add(tag);

            } while (cursor.moveToNext());
        }
        String[] lista = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            lista[i] = list.get(i).tagInfo();
        }

        return (lista);
    }


    public ArrayList<PlcTag> buscar(long id) {
        ArrayList<PlcTag> list = new ArrayList<PlcTag>();
        String[] colunas = new String[]{"_id", "nome", "endereco", "tipo"};

        Cursor cursor = bd.query("cadTag", colunas, null, null, null, null, "nome ASC");

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                PlcTag tag = PlcTag.createTag(cursor.getString(1), cursor.getString(2), cursor.getInt(3));
                tag.setID(cursor.getLong(0));
                list.add(tag);

            } while (cursor.moveToNext());
        }

        return (list);
    }
}
