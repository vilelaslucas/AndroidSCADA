package br.ufla.vilelalucas.androidscada;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import br.ufla.vilelalucas.androidscada.Tags.PlcTag;

public class ExemploActivity extends AppCompatActivity {


    public ImgObj bombaImg;
    public ImgObj valvImg;
    public EditText bombaStt;
    public EditText valvulaStt;
    public EditText nivelText;
    public CheckBox readBox;
    public CheckBox writeBox;
    public Spinner spinner;
    public PlcTag bombaStateTag;
    public PlcTag bombaBloqTag;
    public PlcTag valvulaStateTag;
    public PlcTag nivelTag;
    public PlcTag ligaBomba;
    public PlcTag desligaBomba;
    public PlcTag ligaValvula;
    public PlcTag desligaValvula;
    public PlcTag selTag;
    public ProgressBar barra;
    TextObj bombaTexto;
    TextObj valvulaTexto;
    Alarm alarmHH;
    Alarm alarmLL;
    Alarm alarmH;
    Alarm alarmL;
    boolean update;
    PlcConnection plc;
    ListView lv;
    int lvpos;
    Intent intent;
    private Thread thread;
    private boolean write;
    private BroadcastReceiver atualizarReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            nivelText.setText("" + nivelTag.intValue());
            barra.setProgress(nivelTag.intValue());


            if (valvulaStateTag.boolValue()) {
                //valvulaStt.setText("Ligada");
                valvulaTexto.update(1);
                valvImg.update(1);

            } else {
                // valvulaStt.setText("Desligada");
                valvulaTexto.update(0);
                valvImg.update(0);
            }
            if (bombaBloqTag.boolValue()) {
                //bombaStt.setText("Bloqueada");
                bombaImg.update(2);
                bombaTexto.update(2);
            } else if (bombaStateTag.boolValue()) {
                //bombaStt.setText("Ligada");
                bombaImg.update(1);
                bombaTexto.update(1);
            } else {
                //bombaStt.setText("Desligada");
                bombaImg.update(0);
                bombaTexto.update(0);
            }

            ArrayList<String> list = new ArrayList<String>();
            if (alarmHH != null) {
                if (alarmHH.getStt()) list.add(alarmHH.getString());
            }
            if (alarmLL != null) {
                if (alarmLL.getStt()) list.add(alarmLL.getString());
            }
            if (alarmL != null) {
                if (alarmL.getStt()) list.add(alarmL.getString());
            }
            if (alarmH != null) {
                if (alarmH.getStt()) list.add(alarmH.getString());
            }
            String[] str = new String[list.size()];
            for (int i = 0; i < list.size(); i++)
                str[i] = list.get(i);
            lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.test_list_item, str));

        }
    };

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exemplo);
        BDAlarms bdAlarms = new BDAlarms(this);
        lv = (ListView) findViewById(R.id.listView);
        bombaStt = (EditText) findViewById(R.id.bombaStt);
        nivelText = (EditText) findViewById(R.id.nivelText);
        valvulaStt = (EditText) findViewById(R.id.valvulaStt);
        ImageView bombaImg1 = (ImageView) findViewById(R.id.imageView3);
        ImageView valvImg1 = (ImageView) findViewById(R.id.imageView2);
        barra = (ProgressBar) findViewById(R.id.progressBar2);
        barra.setMax(300);
        BDExemplo bd = new BDExemplo(this);
        BDTag bdTag = new BDTag(this);
        long[] codigos = bd.buscar();
        nivelTag = bdTag.buscarTag(codigos[0]);
        ligaBomba = bdTag.buscarTag(codigos[1]);
        desligaBomba = bdTag.buscarTag(codigos[2]);
        ligaValvula = bdTag.buscarTag(codigos[3]);
        desligaValvula = bdTag.buscarTag(codigos[4]);
        valvulaStateTag = bdTag.buscarTag(codigos[5]);
        bombaStateTag = bdTag.buscarTag(codigos[6]);
        bombaBloqTag = bdTag.buscarTag(codigos[7]);
        alarmHH = bdAlarms.buscar(codigos[8]);
        alarmLL = bdAlarms.buscar(codigos[9]);
        try {
            int size = bdAlarms.buscar().size();
            alarmH = bdAlarms.buscar(size);
            ;
            alarmL = bdAlarms.buscar(size - 1);
        } catch (Exception e) {
            alarmH = null;
            alarmL = null;

        }


        int[] status = new int[]{R.mipmap.bomba, R.mipmap.bombaon, R.mipmap.bombaoff};
        bombaImg = new ImgObj(bombaStateTag, bombaImg1, status);
        status = new int[]{R.mipmap.valvula, R.mipmap.valvulaon};
        valvImg = new ImgObj(valvulaStateTag, valvImg1, status);

        String[] textoStt = new String[]{"Desligada", "Ligada", "Bloqueada"};
        bombaTexto = new TextObj(bombaStateTag, bombaStt, textoStt);
        textoStt = new String[]{"Desligada", "Ligada"};
        valvulaTexto = new TextObj(valvulaStateTag, valvulaStt, textoStt);

        plc = new BDPlc(this).buscarPlc();
        update = true;
        write = false;
        intent = new Intent("atualizarReceiver");
        LocalBroadcastManager.getInstance(this).registerReceiver(atualizarReceiver, new IntentFilter("atualizarReceiver"));
        atualizar();
    }

    public void parClick(View view) {
        Intent secondActivity = new Intent(this, ParametrosActivity.class);
        startActivity(secondActivity);

    }

    protected void onDestroy() {
        update = false;
        super.onDestroy();

    }

    private void atualizar() {
        thread = new Thread(new Runnable() {
            public void run() {
                try {
                    while (update) {
                        Thread.sleep(10);

                        if (plc.isConnected()) {

                            if (write) {
                                selTag.setWriteBuffer(true);
                                if (plc.tagWrite(selTag)) {
                                    System.out.println("Valor escrito com sucesso!");
                                }
                                write = false;
                            } else {
                                if (nivelTag != null) {
                                    plc.tagRead(nivelTag);
                                }
                                if (bombaStateTag != null) {
                                    plc.tagRead(bombaStateTag);

                                }
                                if (bombaBloqTag != null) {
                                    plc.tagRead(bombaBloqTag);

                                }
                                if (valvulaStateTag != null) {
                                    plc.tagRead(valvulaStateTag);

                                }
                                if (alarmHH != null) {
                                    plc.readAlarm(alarmHH);

                                }
                                if (alarmLL != null) {
                                    plc.readAlarm(alarmLL);
                                }
                                if (alarmH != null) {
                                    plc.readAlarm(alarmH);

                                }
                                if (alarmL != null) {
                                    plc.readAlarm(alarmL);
                                }

                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                            }

                        } else plc.plcConect();


                    }
                } catch (InterruptedException e) {
                }
            }
        });
        thread.start();

    }

    public void ligaBombaClick(View view) {
        if (ligaBomba != null) writeTag(ligaBomba);

    }

    public void desligaBombaClick(View view) {
        if (desligaBomba != null) writeTag(desligaBomba);

    }

    public void ligaValvulaClick(View view) {
        if (ligaValvula != null) writeTag(ligaValvula);

    }

    public void desligaValvulaClick(View view) {
        if (desligaValvula != null) writeTag(desligaValvula);

    }

    public void writeTag(PlcTag tag) {
        //Thread para comunicação
        selTag = tag;
        write = true;
    }

}
