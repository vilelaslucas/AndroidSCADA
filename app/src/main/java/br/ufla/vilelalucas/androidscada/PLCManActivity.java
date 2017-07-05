package br.ufla.vilelalucas.androidscada;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.ufla.vilelalucas.androidscada.BDPlc;
import br.ufla.vilelalucas.androidscada.PlcConnection;
import br.ufla.vilelalucas.androidscada.R;

public class PLCManActivity extends AppCompatActivity {
    public EditText ipText;
    public EditText rackText;
    public EditText slotText;
    public Button editar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plcman);


        ipText = (EditText) findViewById(R.id.ipText);
        slotText = (EditText) findViewById(R.id.slotText);
        rackText = (EditText) findViewById(R.id.rackText);
        editar = (Button) findViewById(R.id.button);

        setPlc();
    }

    //Atualizar os campos das informações do CLP
    public void setPlc() {
        BDPlc bdPlc = new BDPlc(this);
        PlcConnection plc = bdPlc.buscarPlc();
        if (plc != null) {
            ipText.setText(plc.getIp());
            rackText.setText("" + plc.getRack());
            slotText.setText("" + plc.getSlot());
            editar.setText("Editar");
        } else {
            ipText.setText("");
            rackText.setText("");
            slotText.setText("");
            editar.setText("Adicionar");
        }
    }

    //Metodo para editar/adicionar novo CLP
    public void editarPlcClick(View view) {
        BDPlc bdPlc = new BDPlc(this);
        PlcConnection plc = bdPlc.buscarPlc();
        if (plc == null) {
            String ip = ipText.getText().toString();
            int rack = Integer.parseInt(rackText.getText().toString());
            int slot = Integer.parseInt(slotText.getText().toString());
            bdPlc.inserir(new PlcConnection(ip, rack, slot));
            setPlc();
        } else {
            String ip = ipText.getText().toString();
            int rack = Integer.parseInt(rackText.getText().toString());
            int slot = Integer.parseInt(slotText.getText().toString());

            bdPlc.atualizar("", ip, rack, slot, plc.getId());
            setPlc();
        }
    }


}
