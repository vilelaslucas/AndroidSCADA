package br.ufla.vilelalucas.androidscada.Tags;

import br.ufla.vilelalucas.androidscada.Moka7.S7;

/**
 * Created by Lucas on 10/01/2017.
 */

public class IntTag extends PlcTag {
    public IntTag(String address, String tagName) {
        super(address, tagName);
        super.setTagSize(2);
    }

    public static boolean checkAddress(String addr) {
        return check16bit(addr);
    }

    public String tagTostr() {

        return this.getID() + "-" + this.getTagName() + "-" + this.getAddress() + ":" + intValue();
    }

    public void setValue(byte[] value) {
        this.value = S7.GetShortAt(value, 0);
        ;
    }

    public void setType() {
        super.tagType = 6;
    }

    public void setWriteBuffer(int valor) {
        wbuffer = new byte[tagSize];
        S7.SetShortAt(wbuffer, 0, valor);
        bufferSt = true;
        System.out.println("INT   " + valor);
    }

}
