package br.ufla.vilelalucas.androidscada;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Lucas on 24/01/2017.
 */

public class BDExemplo {
    private SQLiteDatabase bd;

    public BDExemplo(Context context) {
        BDCore auxBd = new BDCore(context);
        bd = auxBd.getWritableDatabase();
        if (buscar() == null) {
            inserir();
        }
    }


    public boolean inserir() {
        try {
            ContentValues valores = new ContentValues();
            valores.put("tag1", 0);
            valores.put("tag2", 0);
            valores.put("tag3", 0);
            valores.put("tag4", 0);
            valores.put("tag5", 0);
            valores.put("tag6", 0);
            valores.put("tag7", 0);
            valores.put("tag8", 0);
            valores.put("tag9", 0);
            valores.put("tag10", 0);
            valores.put("tag11", 0);
            valores.put("tag12", 0);
            valores.put("tag13", 0);
            valores.put("tag14", 0);
            valores.put("tag15", 0);
            bd.insertOrThrow("exemplo", null, valores);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    public void atualizar(long[] code) {
        ContentValues valores = new ContentValues();
        valores.put("tag1", code[0]);
        valores.put("tag2", code[1]);
        valores.put("tag3", code[2]);
        valores.put("tag4", code[3]);
        valores.put("tag5", code[4]);
        valores.put("tag6", code[5]);
        valores.put("tag7", code[6]);
        valores.put("tag8", code[7]);
        valores.put("tag9", code[8]);
        valores.put("tag10", code[9]);
        valores.put("tag11", code[10]);
        valores.put("tag12", code[11]);
        valores.put("tag13", code[12]);
        valores.put("tag14", code[13]);
        valores.put("tag15", code[14]);
        bd.update("exemplo", valores, "_id = 1", null);
    }


    public void deletar(PlcConnection plc) {
        bd.delete("plcList", "_id = " + plc.getId(), null);
    }

    public long[] buscar() {
        String[] colunas = new String[]{"_id", "tag1", "tag2", "tag3", "tag4", "tag5", "tag6", "tag7", "tag8", "tag9", "tag10", "tag11", "tag12", "tag13", "tag14", "tag15"};

        Cursor cursor = bd.query("exemplo", colunas, "_id = 1", null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            long codigos[] = new long[15];
            for (int i = 0; i < 15; i++) {
                codigos[i] = cursor.getInt(i + 1);
            }
            return codigos;
        }

        return (null);
    }


}
