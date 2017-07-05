package br.ufla.vilelalucas.androidscada;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import br.ufla.vilelalucas.androidscada.Tags.PlcTag;

public class ParametrosActivity extends AppCompatActivity {

    public EditText parametroHH;
    public EditText parametroH;
    public EditText parametroL;
    public EditText parametroLL;
    public Spinner spinner;
    Intent intent;
    PlcConnection plc;
    PlcTag[] tags;
    ListView lv;
    int lvpos;
    int tagVal[];
    private BroadcastReceiver atualizarRec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String parHH = intent.getStringExtra("valorHH");
            parametroHH.setText(parHH);
            String parH = intent.getStringExtra("valorH");
            parametroH.setText(parH);
            String parL = intent.getStringExtra("valorL");
            parametroL.setText(parL);
            String parLL = intent.getStringExtra("valorLL");
            parametroLL.setText(parLL);


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_par_exemplo);
        parametroHH = (EditText) findViewById(R.id.parHH);
        parametroH = (EditText) findViewById(R.id.parH);
        parametroL = (EditText) findViewById(R.id.parL);
        parametroLL = (EditText) findViewById(R.id.parLL);
        intent = new Intent("atualizarRec");
        tagVal = new int[4];
        plc = new BDPlc(this).buscarPlc();
        tags = new PlcTag[4];
        BDTag bdTag = new BDTag(this);
        BDExemplo bd = new BDExemplo(this);
        long[] codigos = bd.buscar();
        tags[0] = bdTag.buscarTag(codigos[10]);
        tags[1] = bdTag.buscarTag(codigos[11]);
        tags[2] = bdTag.buscarTag(codigos[12]);
        tags[3] = bdTag.buscarTag(codigos[13]);
        LocalBroadcastManager.getInstance(this).registerReceiver(atualizarRec, new IntentFilter("atualizarRec"));
        atualizar();
    }

    private void atualizar() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    while (!plc.isConnected()) plc.plcConect();
                    if (tags[0] != null) {
                        plc.tagRead(tags[0]);
                        intent.putExtra("valorHH", "" + tags[0].intValue());
                    }
                    if (tags[1] != null) {
                        plc.tagRead(tags[1]);
                        intent.putExtra("valorH", "" + tags[1].intValue());
                    }
                    if (tags[2] != null) {
                        plc.tagRead(tags[2]);
                        intent.putExtra("valorL", "" + tags[2].intValue());
                    }
                    if (tags[3] != null) {
                        plc.tagRead(tags[3]);
                        intent.putExtra("valorLL", "" + tags[3].intValue());
                    }

                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);


                } catch (Exception e) {
                }
            }
        }).start();

    }

    public void clickConf(View view) {
        try {
            tagVal[0] = Integer.parseInt(parametroHH.getText().toString());
            tagVal[1] = Integer.parseInt(parametroH.getText().toString());
            tagVal[2] = Integer.parseInt(parametroL.getText().toString());
            tagVal[3] = Integer.parseInt(parametroLL.getText().toString());
        } catch (Exception e) {
        }


        new Thread(new Runnable() {
            public void run() {
                try {
                    while (!plc.isConnected()) plc.plcConect();
                    if (tagVal[0] != 0) {
                        tags[0].setWriteBuffer(tagVal[0]);
                        plc.tagWrite(tags[0]);
                    }
                    if (tagVal[1] != 0) {
                        tags[1].setWriteBuffer(tagVal[1]);
                        plc.tagWrite(tags[1]);
                    }
                    if (tagVal[2] != 0) {
                        tags[2].setWriteBuffer(tagVal[2]);
                        plc.tagWrite(tags[2]);
                    }
                    if (tagVal[3] != 0) {
                        tags[3].setWriteBuffer(tagVal[3]);
                        plc.tagWrite(tags[3]);
                    }


                } catch (Exception e) {
                }
            }
        }).start();


    }
}