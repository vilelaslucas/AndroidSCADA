package br.ufla.vilelalucas.androidscada.Tags;

import br.ufla.vilelalucas.androidscada.Moka7.S7;

/**
 * Created by Lucas on 10/01/2017.
 */

public class ShortIntTag extends PlcTag {
    public ShortIntTag(String address, String tagName) {
        super(address, tagName);
        super.setTagSize(1);
    }

    public static boolean checkAddress(String addr) {
        return check8bit(addr);
    }

    public String tagTostr() {

        return this.getID() + "-" + this.getTagName() + "-" + this.getAddress() + ":" + intValue();
    }

    public void setType() {
        super.tagType = 9;
    }

    public void setValue(byte[] value) {
        int aux = value[0];
        this.value = aux;
        bufferSt = true;
    }
}
