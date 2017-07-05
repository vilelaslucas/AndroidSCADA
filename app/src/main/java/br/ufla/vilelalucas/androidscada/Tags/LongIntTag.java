package br.ufla.vilelalucas.androidscada.Tags;

import br.ufla.vilelalucas.androidscada.Moka7.S7;

/**
 * Created by Lucas on 10/01/2017.
 */

public class LongIntTag extends PlcTag {
    public LongIntTag(String address, String tagName) {
        super(address, tagName);
        super.setTagSize(4);
    }

    public static boolean checkAddress(String addr) {
        return check32bit(addr);
    }

    public String tagTostr() {

        return this.getID() + "-" + this.getTagName() + "-" + this.getAddress() + ":" + longValue();
    }

    public void setType() {
        super.tagType = 7;
    }

    public void setValue(byte[] value) {
        this.value = S7.GetDWordAt(value, 0);
    }

    public void setWriteBuffer(int valor) {
        wbuffer = new byte[tagSize];
        S7.SetDIntAt(wbuffer, 0, valor);
        bufferSt = true;
    }
}
