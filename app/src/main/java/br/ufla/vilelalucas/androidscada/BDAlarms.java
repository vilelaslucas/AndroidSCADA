package br.ufla.vilelalucas.androidscada;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import java.util.ArrayList;

import br.ufla.vilelalucas.androidscada.Tags.PlcTag;

/**
 * Created by Lucas on 29/01/2017.
 */

public class BDAlarms {
    private SQLiteDatabase bd;
    private Context contexto;

    public BDAlarms(Context context) {
        contexto = context;
        BDCore auxBd = new BDCore(context);
        bd = auxBd.getWritableDatabase();


    }


    public boolean inserir(Alarm alarm) {
        try {
            ContentValues valores = new ContentValues();
            valores.put("area", alarm.getArea());
            valores.put("equipamento", alarm.getEquipamento());
            valores.put("mensagem", alarm.getMensagem());
            valores.put("tipo", alarm.getTipo());
            valores.put("valorRef", alarm.getValorRef());
            valores.put("tagID", alarm.tag.getID());
            valores.put("prioridade", alarm.getPrior());
            valores.put("action", alarm.getAction());

            bd.insertOrThrow("cadAlarm", null, valores);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    public boolean atualizar(Alarm alarm) {
        try {
            ContentValues valores = new ContentValues();
            valores.put("area", alarm.getArea());
            valores.put("equipamento", alarm.getEquipamento());
            valores.put("mensagem", alarm.getMensagem());
            valores.put("tipo", alarm.getTipo());
            valores.put("valorRef", alarm.getValorRef());
            valores.put("tagID", alarm.tag.getID());
            valores.put("prioridade", alarm.getPrior());
            valores.put("action", alarm.getAction());
            bd.update("cadAlarm", valores, "_id = ?", new String[]{"" + alarm.getID()});
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    public void deletar(Alarm alarm) {
        bd.delete("cadAlarm", "_id = " + alarm.getID(), null);
    }


    public ArrayList<Alarm> buscar() {
        ArrayList<Alarm> list = new ArrayList<Alarm>();
        String[] colunas = new String[]{"_id", "mensagem", "area", "equipamento", "tipo", "valorRef", "tagID"};
        Cursor cursor = bd.query("cadAlarm", colunas, null, null, null, null, "_id ASC");

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                BDTag bdTag = new BDTag(contexto);
                PlcTag tag = bdTag.buscarTag(cursor.getInt(6));
                Alarm alarm = new Alarm(tag, cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getDouble(5));
                alarm.setID(cursor.getLong(0));
                list.add(alarm);


            } while (cursor.moveToNext());
        }

        return (list);
    }

    public String[] buscarLista() {
        ArrayList<Alarm> list = new ArrayList<Alarm>();
        String[] lista;
        String[] colunas = new String[]{"_id", "mensagem", "area", "equipamento", "tipo", "valorRef", "tagID"};
        Cursor cursor = bd.query("cadAlarm", colunas, null, null, null, null, "_id ASC");

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                BDTag bdTag = new BDTag(contexto);
                PlcTag tag = bdTag.buscarTag(cursor.getInt(6));
                Alarm alarm = new Alarm(tag, cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getDouble(5));
                alarm.setID(cursor.getLong(0));
                list.add(alarm);


            } while (cursor.moveToNext());
        }
        lista = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            lista[i] = list.get(i).getString();
        }


        return (lista);
    }


    public Alarm buscar(long id) {
        String[] colunas = new String[]{"_id", "mensagem", "area", "equipamento", "tipo", "valorRef", "tagID", "prioridade", "action"};
        Cursor cursor = bd.query("cadAlarm", colunas, "_id=" + "'" + id + "'", null, null, null, "_id ASC");

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();


            BDTag bdTag = new BDTag(contexto);
            PlcTag tag = bdTag.buscarTag(cursor.getInt(6));
            Alarm alarm = new Alarm(tag, cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getDouble(5));
            alarm.setPrior(cursor.getInt(7));
            alarm.setAction(cursor.getString(8));
            alarm.setID(cursor.getLong(0));
            return alarm;


        }

        return null;
    }


}
