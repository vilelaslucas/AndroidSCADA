package br.ufla.vilelalucas.androidscada;

import android.support.constraint.solver.ArrayLinkedVariables;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.ufla.vilelalucas.androidscada.Tags.PlcTag;

public class AlarmsActivity extends AppCompatActivity {
    public Spinner tagSpinner;
    public Spinner spinner;
    public EditText equipamento;
    public EditText area;
    public EditText mensagem;
    public EditText prioridade;
    public EditText instrucao;
    public ListView lv;

    EditText valorRef;
    TextView texto;
    int lvpos;
    private ArrayList<PlcTag> listTags;
    private PlcTag tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms);

        valorRef = (EditText) findViewById(R.id.valorText);
        texto = (TextView) findViewById(R.id.valorView);
        tagSpinner = (Spinner) findViewById(R.id.spinnerTag);
        spinner = (Spinner) findViewById(R.id.spinner2);
        prioridade = (EditText) findViewById(R.id.priorText);
        instrucao = (EditText) findViewById(R.id.instText);
        lv = (ListView) findViewById(R.id.listView);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.condicoesAlarms_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                if (spinner.getSelectedItemId() < 2) {
                    valorRef.setVisibility(View.INVISIBLE);
                    texto.setVisibility(View.INVISIBLE);

                } else {
                    valorRef.setVisibility(View.VISIBLE);
                    texto.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        setTagSpinner();
        equipamento = (EditText) findViewById(R.id.eqpText);
        area = (EditText) findViewById(R.id.areaText);
        mensagem = (EditText) findViewById(R.id.messageText);
        listaAlarmes();
        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {
                        lvpos = position;
                        editMode();
                    }
                }
        );
    }

    public void editMode() {
        Button edit = (Button) findViewById(R.id.editButton);
        Button cancel = (Button) findViewById(R.id.cancelButton);
        edit.setText("EDITAR");
        cancel.setVisibility(View.INVISIBLE);
        equipamento.setText("");
        area.setText("");
        mensagem.setText("");
        spinner.setSelection(0);

    }

    public void editClick(View view) {
        Button edit = (Button) findViewById(R.id.editButton);
        Button cancel = (Button) findViewById(R.id.cancelButton);
        BDAlarms bd = new BDAlarms(this);
        Alarm alarm = bd.buscar().get(lvpos);
        if (edit.getText().toString().equalsIgnoreCase("EDITAR")) {
            cancel.setVisibility(View.VISIBLE);
            equipamento.setText(alarm.getEquipamento());
            area.setText(alarm.getArea());
            mensagem.setText(alarm.getMensagem());
            valorRef.setText(alarm.getValorRef() + "");
            spinner.setSelection(alarm.getTipo());
            setTagSpinner(alarm.tag.getID());
            edit.setText("SALVAR");
        } else {
            long tagID = alarm.getID();
            alarm = readAlarme();
            alarm.setID(tagID);
            if (bd.atualizar(alarm)) {
                Toast t = Toast.makeText(getApplicationContext(), "Alarme editad0", Toast.LENGTH_SHORT);
                t.show();
                editMode();
            } else {
                Toast t = Toast.makeText(getApplicationContext(), "Verifique as informações do alarme", Toast.LENGTH_SHORT);
                t.show();

            }
            listaAlarmes();
        }
    }

    public void setTagSpinner() {
        BDTag bd = new BDTag(this);
        listTags = bd.buscar();
        String[] tags = new String[listTags.size()];
        for (int i = 0; i < listTags.size(); i++) {
            tags[i] = listTags.get(i).tagInfo();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tags);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagSpinner.setAdapter(adapter);

    }

    public void setTagSpinner(long tagID) {
        for (int i = 0; i < listTags.size(); i++) {
            if (listTags.get(i).getID() == tagID) {
                tagSpinner.setSelection(i);
            }
        }
    }

    public void cancelClick(View view) {
        editMode();
    }

    public void addClick(View view) {
        Alarm alarm;
        BDAlarms bd = new BDAlarms(this);
        alarm = readAlarme();
        bd.inserir(alarm);

        listaAlarmes();
    }

    public Alarm readAlarme() {
        PlcTag tag = null;
        int prior;
        String instru;
        try {
            tag = listTags.get((int) tagSpinner.getSelectedItemId());
        } catch (Exception e) {
            Toast t = Toast.makeText(getApplicationContext(), "Tag inválida!", Toast.LENGTH_SHORT);
            t.show();
            return null;
        }
        try {
            prior = Integer.parseInt(prioridade.getText().toString());
        } catch (Exception e) {
            Toast t = Toast.makeText(getApplicationContext(), "Verifique a prioridade!", Toast.LENGTH_SHORT);
            t.show();
            return null;

        }

        int condicao = (int) spinner.getSelectedItemId();
        if (condicao < 2) {
            return new Alarm(tag, mensagem.getText().toString().toUpperCase(), area.getText().toString().toUpperCase(), equipamento.getText().toString().toUpperCase(), instrucao.getText().toString().toUpperCase(), (int) spinner.getSelectedItemId(), prior);
        } else {
            try {
                double valor = Double.parseDouble(valorRef.getText().toString());
                return new Alarm(tag, mensagem.getText().toString().toUpperCase(), area.getText().toString().toUpperCase(), equipamento.getText().toString().toUpperCase(), instrucao.getText().toString().toUpperCase(), (int) spinner.getSelectedItemId(), valor, prior);

            } catch (Exception e) {
                Toast t = Toast.makeText(getApplicationContext(), "Verifique o valor de referência!", Toast.LENGTH_SHORT);
                t.show();
            }
        }

        return null;
    }

    public void listaAlarmes() {
        BDAlarms bd = new BDAlarms(this);
        ArrayList<Alarm> lista = bd.buscar();
        String[] str = new String[lista.size()];
        for (int k = 0; k < lista.size(); k++) {
            str[k] = lista.get(k).alarmInfo();
        }
        lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.test_list_item, str));
    }

    public void deleteClick(View view) {
        BDAlarms bd = new BDAlarms(this);
        Alarm alarm = bd.buscar().get(lvpos);
        bd.deletar(alarm);
        listaAlarmes();
    }


}
