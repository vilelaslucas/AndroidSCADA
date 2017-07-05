package br.ufla.vilelalucas.androidscada;

import android.widget.ImageView;

import br.ufla.vilelalucas.androidscada.Tags.PlcTag;

/**
 * Created by vilel on 29/06/2017.
 */

public class ImgObj extends ScadaObj {
    ImageView image;
    int[] status;

    public ImgObj(PlcTag tag, ImageView image) {
        super(tag);
        this.image = image;
    }

    public ImgObj(PlcTag tag, ImageView image, int[] status) {
        super(tag);
        this.image = image;
        this.status = status;

    }

    public void update(int i) {
        image.setImageResource(status[i]);
    }

}
