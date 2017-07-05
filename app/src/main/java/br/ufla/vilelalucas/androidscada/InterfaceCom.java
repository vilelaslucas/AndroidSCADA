package br.ufla.vilelalucas.androidscada;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.media.MediaBrowserCompat;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import br.ufla.vilelalucas.androidscada.Moka7.S7Client;
import br.ufla.vilelalucas.androidscada.Tags.PlcTag;

/**
 * Created by Lucas on 31/01/2017.
 */

public class InterfaceCom extends IntentService {
    public boolean runService;
    PlcConnection plc;
    ArrayList<PlcTag> tags;

    public InterfaceCom() {
        super("LEITURATAGS");


        runService = true;
    }


    public int onStartCommand(Intent intent, int flag, int startID) {
        tags = new BDTag(this).buscar();
        plc = new BDPlc(this).buscarPlc();
        Bundle b = intent.getExtras();
        if (b != null) {
            runService = b.getBoolean("stop");
        }
        return super.onStartCommand(intent, flag, startID);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String[] tagsStr = new String[tags.size()];
        Bundle b = intent.getExtras();

        while (runService) {
            if (!plc.isConnected()) {
                intent.putExtra("Mensagem", "Conectado");
                plc.plcConect();
            } else {
                // plc.tagRead(tags);

                long codigo = b.getLong("codTag");
                for (int i = 0; i < tags.size(); i++) {
                    plc.tagRead(tags.get(i));
                    tagsStr[i] = tags.get(i).tagTostr();
                    if (tags.get(i).getID() == codigo) {
                        tags.get(i).setWriteBuffer(500);
                        plc.tagWrite(tags.get(i));
                    }
                    // System.out.println(tagsStr[i]);
                }
                intent.putExtra("tags", tagsStr);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
