package br.ufla.vilelalucas.androidscada.Tags;

import br.ufla.vilelalucas.androidscada.Moka7.S7;

/**
 * Created by Lucas on 10/01/2017.
 */

public class DoubleTag extends PlcTag {
    public DoubleTag(String address, String tagName) {
        super(address, tagName);
        super.setTagSize(8);
    }

    public static boolean checkAddress(String addr) {
        return check64bit(addr);
    }

    public String tagTostr() {

        return this.getID() + "-" + this.getTagName() + "-" + this.getAddress() + ":" + getValue();
    }

    public void setType() {
        super.tagType = 4;
    }

    public void setValue(byte[] value) {
        this.value = S7.GetFloatAt(value, 0);
    }

    public void setWriteBuffer(double valor) {
        wbuffer = new byte[tagSize];
        S7.SetFloatAt(wbuffer, 0, new Double(valor).floatValue());
        bufferSt = true;
    }
}
