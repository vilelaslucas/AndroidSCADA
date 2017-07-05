package br.ufla.vilelalucas.androidscada;

import android.widget.EditText;
import android.widget.ImageView;

import br.ufla.vilelalucas.androidscada.Tags.PlcTag;

/**
 * Created by vilel on 29/06/2017.
 */

public class TextObj extends ScadaObj {
    EditText texto;
    String[] status;

    public TextObj(PlcTag tag, EditText texto) {
        super(tag);
        this.texto = texto;
    }

    public TextObj(PlcTag tag, EditText texto, String[] status) {
        super(tag);
        this.texto = texto;
        this.status = status;

    }

    public void update(int i) {
        texto.setText(status[i]);
    }

}
