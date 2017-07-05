package br.ufla.vilelalucas.androidscada;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewDebug;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;

import br.ufla.vilelalucas.androidscada.Moka7.S7;
import br.ufla.vilelalucas.androidscada.Moka7.S7Client;
import br.ufla.vilelalucas.androidscada.Tags.BinaryTag;
import br.ufla.vilelalucas.androidscada.Tags.Char16Tag;
import br.ufla.vilelalucas.androidscada.Tags.CharTag;
import br.ufla.vilelalucas.androidscada.Tags.FloatTag;
import br.ufla.vilelalucas.androidscada.Tags.IntTag;
import br.ufla.vilelalucas.androidscada.Tags.LongIntTag;
import br.ufla.vilelalucas.androidscada.Tags.LongUIntTag;
import br.ufla.vilelalucas.androidscada.PLCManActivity;
import br.ufla.vilelalucas.androidscada.Tags.PlcTag;
import br.ufla.vilelalucas.androidscada.Tags.ShortIntTag;
import br.ufla.vilelalucas.androidscada.Tags.ShortUIntTag;
import br.ufla.vilelalucas.androidscada.Tags.UIntTag;

public class MainActivity extends Activity {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    public EditText tagNameText;
    public EditText tagAddrText;
    public Spinner spinner;
    public String[] lvStr = new String[]{"Teste 1", "Teste 2"};
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.


    }


    public void PlcManClick(View view) {
        Intent secondActivity = new Intent(this, PLCManActivity.class);
        startActivity(secondActivity);
    }

    public void TagManClick(View view) {
        BDPlc bdPlc = new BDPlc(this);
        PlcConnection plc = bdPlc.buscarPlc();
        if (plc != null) {
            Intent secondActivity = new Intent(this, TagManagerActivity.class);
            startActivity(secondActivity);
        } else {
            Toast t = Toast.makeText(getApplicationContext(), "Adicione um CLP", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    public void taglogClick(View view) {
        BDPlc bdPlc = new BDPlc(this);
        PlcConnection plc = bdPlc.buscarPlc();
        if (plc != null) {
            Intent secondActivity = new Intent(this, TagLogActivity.class);
            startActivity(secondActivity);
        } else {
            Toast t = Toast.makeText(getApplicationContext(), "Adicione um CLP", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    public void alrManClick(View view) {
        BDPlc bdPlc = new BDPlc(this);
        PlcConnection plc = bdPlc.buscarPlc();
        if (plc != null) {
            Intent secondActivity = new Intent(this, AlarmsActivity.class);
            startActivity(secondActivity);
        } else {
            Toast t = Toast.makeText(getApplicationContext(), "Adicione um CLP", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    public void exemploClick(View view) {
        BDPlc bdPlc = new BDPlc(this);
        PlcConnection plc = bdPlc.buscarPlc();
        if (plc != null) {
            Intent secondActivity = new Intent(this, ExemploActivity.class);
            startActivity(secondActivity);
        } else {
            Toast t = Toast.makeText(getApplicationContext(), "Adicione um CLP", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    public void exemploConfigClick(View view) {
        BDPlc bdPlc = new BDPlc(this);
        PlcConnection plc = bdPlc.buscarPlc();
        if (plc != null) {
            Intent secondActivity = new Intent(this, ExemploConfig.class);
            startActivity(secondActivity);
        } else {
            Toast t = Toast.makeText(getApplicationContext(), "Adicione um CLP", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    public PlcTag createTag(String name, String addr, int tipo) {
        try {

            PlcTag tag = PlcTag.createTag(name, addr, tipo);
            tag.setRead(true);
            tag.setWrite(true);
            return tag;
        } catch (Exception e) {
            return null;
        }
    }

    public void addTagsClick(View view) {
        BDPlc bdPlc = new BDPlc(this);
        PlcConnection plc = bdPlc.buscarPlc();
        if (plc != null) {
            BDTag bd = new BDTag(this);


            bd.inserir(createTag("NIVEL", "DB10DW0", 6));
            bd.inserir(createTag("PAR_NIVEL_HH", "DB10,DW2", 6));
            bd.inserir(createTag("PAR_NIVEL_H", "DB10,DW4", 6));
            bd.inserir(createTag("PAR_NIVEL_L", "DB10,DW6", 6));
            bd.inserir(createTag("PAR_NIVEL_LL", "DB10,DW8", 6));
            bd.inserir(createTag("STT_PMP_ON", "DB10,D10.0", 1));
            bd.inserir(createTag("STT_PMP_BLOQ", "DB10,D10.1", 1));
            bd.inserir(createTag("CMD_PMP_ON", "DB10,D10.2", 1));
            bd.inserir(createTag("CMD_PMP_OFF", "DB10,D10.3", 1));
            bd.inserir(createTag("STT_VLV_ON", "DB10,D10.4", 1));
            bd.inserir(createTag("STT_VLV_OFF", "DB10,D10.5", 1));
            bd.inserir(createTag("CMD_VLV_ON", "DB10,D10.6", 1));
            bd.inserir(createTag("CMD_VLV_OFF", "DB10,D10.7", 1));
            bd.inserir(createTag("ALR_NIVEL_HH", "DB10,D11.0", 1));
            bd.inserir(createTag("ALR_NIVEL_H", "DB10,D11.1", 1));
            bd.inserir(createTag("ALR_NIVEL_L", "DB10,D11.2", 1));
            bd.inserir(createTag("ALR_NIVEL_LL", "DB10,D11.3", 1));


            //Teste de velocidade de leitura

            bd.inserir(createTag("TESTE1", "MW10", 6));
            bd.inserir(createTag("TESTE2", "MW12", 6));
            bd.inserir(createTag("TESTE3", "MW14", 6));
            bd.inserir(createTag("TESTE4", "MW16", 6));
            bd.inserir(createTag("TESTE5", "MW18", 6));
            bd.inserir(createTag("TESTE6", "MW20", 6));
            bd.inserir(createTag("TESTE7", "MW22", 6));
            bd.inserir(createTag("TESTE8", "MW24", 6));
            bd.inserir(createTag("TESTE9", "MW26", 6));
            bd.inserir(createTag("TESTE10", "MW28", 6));
            bd.inserir(createTag("TESTE11", "MW30", 6));
            bd.inserir(createTag("TESTE12", "MW32", 6));
            bd.inserir(createTag("TESTE13", "MW34", 6));
            bd.inserir(createTag("TESTE14", "MW36", 6));
            bd.inserir(createTag("TESTE15", "MW38", 6));
            bd.inserir(createTag("TESTE16", "M40.0", 1));
            bd.inserir(createTag("TESTE17", "M41.0", 1));
            bd.inserir(createTag("TESTE18", "M42.0", 1));
            bd.inserir(createTag("TESTE19", "M43.0", 1));
            bd.inserir(createTag("TESTE20", "M44.0", 1));
            bd.inserir(createTag("TESTE21", "M45.0", 1));
            bd.inserir(createTag("TESTE22", "M46.0", 1));
            bd.inserir(createTag("TESTE23", "M47.0", 1));
            bd.inserir(createTag("TESTE24", "M240.0", 1));
            bd.inserir(createTag("TESTE25", "M213.0", 1));
            bd.inserir(createTag("TESTE26", "M200.6", 1));
            bd.inserir(createTag("TESTE27", "Q140.0", 1));
            bd.inserir(createTag("TESTE28", "I40.0", 1));
            bd.inserir(createTag("TESTE29", "I80.0", 1));
            bd.inserir(createTag("TESTE30", "M49.0", 1));
            bd.inserir(createTag("TESTE31", "M49.0", 1));
            bd.inserir(createTag("TESTE32", "M49.0", 1));
            bd.inserir(createTag("TESTE33", "M49.0", 1));


            Toast t = Toast.makeText(getApplicationContext(), "Tags Adicionadas", Toast.LENGTH_SHORT);
            t.show();
        } else {
            Toast t = Toast.makeText(getApplicationContext(), "Adicione um CLP", Toast.LENGTH_SHORT);
            t.show();
        }
    }


}
