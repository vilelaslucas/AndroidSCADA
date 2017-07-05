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

public class ExemploConfig extends AppCompatActivity {


    public ArrayList<Spinner> spinners;

    BDExemplo bd;
    long[] codigos;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_exemplo);
        spinners = new ArrayList<Spinner>();
        spinners.add((Spinner) findViewById(R.id.nivel_spinner));
        spinners.add((Spinner) findViewById(R.id.pmp_on_spinner));
        spinners.add((Spinner) findViewById(R.id.pmp_off_spinner));
        spinners.add((Spinner) findViewById(R.id.vlv_on_spinner));
        spinners.add((Spinner) findViewById(R.id.vlv_off_spinner));
        spinners.add((Spinner) findViewById(R.id.stt_vlv_spinner));
        spinners.add((Spinner) findViewById(R.id.stt_pmp_spinner));
        spinners.add((Spinner) findViewById(R.id.stt_bloq_pmp_spinner));
        spinners.add((Spinner) findViewById(R.id.spinnerHH));
        spinners.add((Spinner) findViewById(R.id.spinnerLL));
        spinners.add((Spinner) findViewById(R.id.spinParHH));
        spinners.add((Spinner) findViewById(R.id.spinParH));
        spinners.add((Spinner) findViewById(R.id.spinParL));
        spinners.add((Spinner) findViewById(R.id.spinParLL));
        bd = new BDExemplo(this);
        codigos = bd.buscar();


        for (int i = 0; i < spinners.size(); i++) {
            switch (i) {

                case 8:
                case 9:
                    setAlarmSpinner(spinners.get(i), codigos[i]);
                    break;
                case 0:
                case 10:
                case 11:
                case 12:
                case 13:
                    setTagSpinner(spinners.get(i), PlcTag.tInt, codigos[i]);
                    break;
                default:
                    setTagSpinner(spinners.get(i), PlcTag.tBoolean, codigos[i]);
            }
        }

    }

    public void setTagSpinner(Spinner spinner, int tagType, long codigo) {
        BDTag bd = new BDTag(this);
        ArrayList<PlcTag> listTags = bd.buscar();
        String[] tags = bd.buscarTipo(tagType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tags);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        for (int i = 0; i < tags.length; i++) {
            String text = tags[i];
            String[] texto2 = text.split(" ");
            if (codigo == Integer.parseInt(texto2[0])) {
                spinner.setSelection(i);
            }
        }

    }

    public void setAlarmSpinner(Spinner spinner, long codigo) {
        BDAlarms bd = new BDAlarms(this);
        String[] tags = bd.buscarLista();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tags);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        for (int i = 0; i < tags.length; i++) {
            String text = tags[i];
            String[] texto2 = text.split(" ");
            if (codigo == Integer.parseInt(texto2[0])) {
                spinner.setSelection(i);
            }
        }


    }

    public void setSpinnerPos() {
        try {
            BDExemplo bd = new BDExemplo(this);
            long[] codigos = bd.buscar();

            for (int i = 0; i < spinners.size(); i++) {
                String texto = spinners.get(i).getSelectedItem().toString();
                String[] texto2 = texto.split(" ");

                codigos[i] = Integer.parseInt(texto2[0]);
            }
        } catch (Exception e) {
        }
    }


    public void configClick(View view) {
        try {


            for (int i = 0; i < spinners.size(); i++) {
                String texto = spinners.get(i).getSelectedItem().toString();
                String[] texto2 = texto.split(" ");
                codigos[i] = Integer.parseInt(texto2[0]);
            }
            bd.atualizar(codigos);
        } catch (Exception e) {
        }
    }


}
