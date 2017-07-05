package br.ufla.vilelalucas.androidscada;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import br.ufla.vilelalucas.androidscada.Tags.BinaryTag;
import br.ufla.vilelalucas.androidscada.Tags.CharTag;
import br.ufla.vilelalucas.androidscada.Tags.FloatTag;
import br.ufla.vilelalucas.androidscada.Tags.IntTag;
import br.ufla.vilelalucas.androidscada.Tags.LongIntTag;
import br.ufla.vilelalucas.androidscada.Tags.LongUIntTag;
import br.ufla.vilelalucas.androidscada.Tags.PlcTag;
import br.ufla.vilelalucas.androidscada.Tags.ShortIntTag;
import br.ufla.vilelalucas.androidscada.Tags.ShortUIntTag;
import br.ufla.vilelalucas.androidscada.Tags.UIntTag;

public class TagManagerActivity extends AppCompatActivity {

    public EditText tagNameText;
    public EditText tagAddr;
    public CheckBox readBox;
    public CheckBox writeBox;
    public Spinner spinner;
    ListView lv;
    int lvpos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_manager);

        tagNameText = (EditText) findViewById(R.id.tagNameText);
        tagAddr = (EditText) findViewById(R.id.tagAddr);
        spinner = (Spinner) findViewById(R.id.spinner);
        readBox = (CheckBox) findViewById(R.id.readBox);
        writeBox = (CheckBox) findViewById(R.id.writeBox);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tiposTags_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        lv = (ListView) findViewById(R.id.listView);
        listaTags();
        lvpos = 0;
        editMode();


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
        tagAddr.setText("");
        tagNameText.setText("");
        spinner.setSelection(0);

    }

    public void listaTags() {
        BDTag bd = new BDTag(this);
        ArrayList<PlcTag> lista = bd.buscar();
        String[] str = new String[lista.size()];
        for (int k = 0; k < lista.size(); k++) {
            str[k] = lista.get(k).tagInfo();
        }
        lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.test_list_item, str));
    }

    public PlcTag readTag() {
        try {
            String item = spinner.getSelectedItem().toString();
            String tagAdd = tagAddr.getText().toString().toUpperCase();
            String tagN = tagNameText.getText().toString().toUpperCase();
            System.out.println(tagN + "   " + item);
            PlcTag tag = PlcTag.createTag(tagN, tagAdd, item);
            tag.setRead(readBox.isChecked());
            tag.setWrite(writeBox.isChecked());
            return tag;
        } catch (Exception e) {
            return null;
        }
    }

    public void addClick(View view) {
        BDTag bd = new BDTag(this);
        if (bd.inserir(readTag())) {

            Toast t = Toast.makeText(getApplicationContext(), "Tag adicionada", Toast.LENGTH_SHORT);
            editMode();
            t.show();
        } else {
            Toast t = Toast.makeText(getApplicationContext(), "Verifique as informações da Tag", Toast.LENGTH_SHORT);
            t.show();

        }
        listaTags();
    }

    public void editClick(View view) {
        Button edit = (Button) findViewById(R.id.editButton);
        Button cancel = (Button) findViewById(R.id.cancelButton);
        BDTag bd = new BDTag(this);
        PlcTag tag = bd.buscar().get(lvpos);
        if (edit.getText().toString().equalsIgnoreCase("EDITAR")) {
            cancel.setVisibility(View.VISIBLE);
            tagAddr.setText(tag.getAddress());
            tagNameText.setText(tag.getTagName());
            int spinnerSel;
            switch (tag.getType()) {
                case 1:
                    spinnerSel = 0;
                    break;
                case 6:
                    spinnerSel = 1;
                    break;
                case 11:
                    spinnerSel = 2;
                    break;
                case 5:
                    spinnerSel = 3;
                    break;
                case 3:
                    spinnerSel = 4;
                    break;
                case 7:
                    spinnerSel = 5;
                    break;
                case 8:
                    spinnerSel = 6;
                    break;
                case 9:
                    spinnerSel = 7;
                    break;
                case 10:
                    spinnerSel = 8;
                    break;
                default:
                    spinnerSel = 9;
            }
            spinner.setSelection(spinnerSel);
            readBox.setChecked(tag.getRead());
            writeBox.setChecked(tag.getWrite());
            edit.setText("SALVAR");
        } else {
            long tagID = tag.getID();
            tag = readTag();
            tag.setID(tagID);
            if (bd.atualizar(tag)) {
                Toast t = Toast.makeText(getApplicationContext(), "Tag editada", Toast.LENGTH_SHORT);
                t.show();
                editMode();
            } else {
                Toast t = Toast.makeText(getApplicationContext(), "Verifique as informações da Tag", Toast.LENGTH_SHORT);
                t.show();

            }
            listaTags();
        }
    }

    public void cancelClick(View view) {
        editMode();
    }

    public void deleteClick(View view) {
        BDTag bd = new BDTag(this);
        PlcTag tag = bd.buscar().get(lvpos);
        bd.deletar(tag);
        System.out.println("DELETE");
        listaTags();

    }
}
