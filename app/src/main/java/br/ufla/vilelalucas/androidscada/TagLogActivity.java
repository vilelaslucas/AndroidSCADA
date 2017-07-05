package br.ufla.vilelalucas.androidscada;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.ufla.vilelalucas.androidscada.Tags.PlcTag;

public class TagLogActivity extends AppCompatActivity {
    Intent intent;
    boolean b = true, update;
    Button stopbutton;
    int lvpos;
    TextView tagText;
    TextView tagInt;
    TextView tagReal;
    Spinner spinner;
    PlcTag selTag;
    String[] str;
    PlcConnection plc;
    long tStamp;
    ListView lv;
    String mensagem = "";
    ArrayList<PlcTag> tags;
    TextView scanTime;
    Switch sw;
    boolean tLeitura;
    boolean write;
    private BroadcastReceiver tagsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            str = intent.getStringArrayExtra("tags");
            long time = intent.getLongExtra("tScan", 0);
            scanTime.setText("" + time);
            tLeitura = sw.isChecked();
            if (update && str != null) {
                lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.test_list_item, str));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_log);
        intent = new Intent(this, InterfaceCom.class);
        intent.setAction("tagsRead");
        LocalBroadcastManager.getInstance(this).registerReceiver(tagsReceiver, new IntentFilter("tagsRead"));
        lv = (ListView) findViewById(R.id.listView);
        stopbutton = (Button) findViewById(R.id.stopButton);
        tagText = (TextView) findViewById(R.id.tagText);
        scanTime = (TextView) findViewById(R.id.scanTimeView);
        tagInt = (TextView) findViewById(R.id.intText);
        tagReal = (TextView) findViewById(R.id.realText);
        spinner = (Spinner) findViewById(R.id.spinner3);
        tagInt.setVisibility(View.GONE);
        tagReal.setVisibility(View.GONE);
        spinner.setVisibility((View.GONE));
        sw = (Switch) findViewById(R.id.switch1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.verdadeiroOuFalso, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {
                        lvpos = position;
                        selectTag();
                        stop();
                    }
                }
        );

        plc = new BDPlc(this).buscarPlc();
        tags = new BDTag(this).buscar();
        System.out.println("Num tags:" + tags.size());
        str = new String[tags.size()];
        intent.putExtra("stop", true);
        scanTime.setText("0");
        //startService(intent);
        update = true;
        tLeitura = sw.isActivated();
        start();
    }

    public void readTags() {

        new Thread(new Runnable() {
            public void run() {
                try {
                    while (update) {
                        if (!plc.isConnected()) {
                            plc.plcConect();
                        } else {
                            if (write) {
                                if (plc.tagWrite(selTag)) {

                                }
                                write = false;
                            } else {
                                long tIni = System.currentTimeMillis();
                                if (tLeitura) {

                                    plc.tagRead(tags);
                                    for (int i = 0; i < tags.size(); i++) {
                                        str[i] = tags.get(i).tagTostr();
                                    }

                                } else {

                                    for (int i = 0; i < tags.size(); i++) {
                                        plc.tagRead(tags.get(i));
                                        str[i] = tags.get(i).tagTostr();
                                    }
                                }
                                intent.putExtra("tags", str);
                                intent.putExtra("tScan", (System.currentTimeMillis() - tIni));
                                System.out.println((System.currentTimeMillis() - tIni));
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                            }
                        }

                        Thread.sleep(50);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void selectTag() {
        String item = (String) lv.getItemAtPosition(lvpos);
        String[] part = item.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        selTag = new BDTag(this).buscarTag(Integer.parseInt(part[0]));
        tagText.setText(selTag.tagInfo());
        switch (selTag.getType()) {
            case PlcTag.tBoolean:
                tagInt.setVisibility(View.GONE);
                tagReal.setVisibility(View.GONE);
                spinner.setVisibility((View.VISIBLE));
                break;
            case PlcTag.tInt:
                tagInt.setVisibility(View.VISIBLE);
                tagReal.setVisibility(View.GONE);
                spinner.setVisibility((View.GONE));
                break;
            case PlcTag.tUInt:
                tagInt.setVisibility(View.VISIBLE);
                tagReal.setVisibility(View.GONE);
                spinner.setVisibility((View.GONE));
                break;
            case PlcTag.tFloat:
                tagInt.setVisibility(View.GONE);
                tagReal.setVisibility(View.VISIBLE);
                spinner.setVisibility((View.GONE));
                break;
            default:
                tagInt.setVisibility(View.GONE);
                tagReal.setVisibility(View.GONE);
                spinner.setVisibility((View.GONE));
        }


    }

    public void stopClick(View view) {
        if (update) {
            stop();
        } else {
            start();
        }
    }

    public void stop() {
        update = false;
        stopbutton.setText("START");
    }

    public void start() {
        update = true;
        stopbutton.setText("STOP");
        tLeitura = sw.isChecked();
        readTags();
    }

    protected void onDestroy() {
        update = false;
        super.onDestroy();

    }

    public void writeClick(View view) {
        if (lerValor()) {
            //Thread para comunicação
            write = true;
            start();
        } else {
            Toast t = Toast.makeText(getApplicationContext(), "Verifique o valor da TAG", Toast.LENGTH_SHORT);
            t.show();
        }

    }

    public boolean lerValor() {
        boolean result;
        if (selTag == null) {
            return false;
        } else {
            switch (selTag.getType()) {
                case PlcTag.tBoolean:
                    boolean boolTag = true;
                    String valor = spinner.getSelectedItem().toString();
                    if (valor.equalsIgnoreCase("Verdadeiro")) {
                        boolTag = true;
                    }
                    if (valor.equalsIgnoreCase("Falso")) {
                        boolTag = false;
                    }
                    selTag.setWriteBuffer(boolTag);
                    result = true;
                    break;
                case PlcTag.tInt:
                    try {
                        int valorTag = Integer.parseInt(tagInt.getText().toString());
                        selTag.setWriteBuffer(valorTag);
                        result = true;
                    } catch (Exception e) {
                        result = false;
                    }

                    break;
                case PlcTag.tUInt:
                    result = false;
                    break;
                case PlcTag.tFloat:
                    try {
                        float realTag = Float.parseFloat(tagInt.getText().toString());
                        selTag.setWriteBuffer(realTag);
                        result = true;
                    } catch (Exception e) {
                        result = false;
                    }
                    break;
                default:
                    result = false;

            }
        }
        return result;
    }

    // Create the Handler object (on the main thread by default)

// Start the initial runnable task by posting through the handler


}
