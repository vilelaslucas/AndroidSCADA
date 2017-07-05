package br.ufla.vilelalucas.androidscada.Tags;

import br.ufla.vilelalucas.androidscada.Moka7.S7;
import br.ufla.vilelalucas.androidscada.Moka7.S7Client;

/**
 * Created by Lucas on 10/01/2017.
 */

public class LongUIntTag extends PlcTag {
    public LongUIntTag(String address, String tagName) {
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
        super.tagType = 8;
    }

    public void setValue(byte[] value) {
        long aux = S7.GetDIntAt(value, 0);
        this.value = aux;
    }

    public void setWriteBuffer(int valor) {
        wbuffer = new byte[tagSize];
        S7.SetDWordAt(wbuffer, 0, valor);
        bufferSt = true;
    }
}
