package br.ufla.vilelalucas.androidscada;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import br.ufla.vilelalucas.androidscada.Tags.PlcTag;

/**
 * Created by Lucas on 24/01/2017.
 */

public class BDPlc {
    private SQLiteDatabase bd;

    public BDPlc(Context context) {
        BDCore auxBd = new BDCore(context);
        bd = auxBd.getWritableDatabase();
    }


    public boolean inserir(PlcConnection plc) {
        try {
            ContentValues valores = new ContentValues();
            valores.put("nome", plc.getPlcName());
            valores.put("ip", plc.getIp());
            valores.put("rack", plc.getRack());
            valores.put("slot", plc.getSlot());
            bd.insertOrThrow("plcList", null, valores);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    public void atualizar(PlcConnection plc) {
        ContentValues valores = new ContentValues();
        valores.put("nome", plc.getPlcName());
        valores.put("ip", plc.getIp());
        valores.put("rack", plc.getRack());
        valores.put("slot", plc.getSlot());
        bd.update("plcList", valores, "_id = ?", new String[]{"" + plc.getId()});
    }

    public void atualizar(String nome, String ip, int rack, int slot, long id) {
        ContentValues valores = new ContentValues();
        valores.put("nome", nome);
        valores.put("ip", ip);
        valores.put("rack", rack);
        valores.put("slot", slot);
        bd.update("plcList", valores, "_id = ?", new String[]{"" + id});
    }


    public void deletar(PlcConnection plc) {
        bd.delete("plcList", "_id = " + plc.getId(), null);
    }


    public ArrayList<PlcConnection> buscar() {
        ArrayList<PlcConnection> list = new ArrayList<PlcConnection>();
        String[] colunas = new String[]{"_id", "nome", "ip", "rack", "slot"};

        Cursor cursor = bd.query("plcList", colunas, null, null, null, null, "nome ASC");

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                PlcConnection plc = new PlcConnection(cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4));
                plc.setID(cursor.getLong(0));
                list.add(plc);

            } while (cursor.moveToNext());
        }

        return (list);
    }

    public PlcConnection buscarPlc() {
        String[] colunas = new String[]{"_id", "nome", "ip", "rack", "slot"};

        Cursor cursor = bd.query("plcList", colunas, "_id = 1", null, null, null, "nome ASC");

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            PlcConnection plc = new PlcConnection(cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4));
            plc.setID(cursor.getLong(0));
            return plc;
        }

        return (null);
    }


}
