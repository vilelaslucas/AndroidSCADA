package br.ufla.vilelalucas.androidscada.Tags;

import br.ufla.vilelalucas.androidscada.Moka7.S7;

/**
 * Created by Lucas on 10/01/2017.
 */

public class Char16Tag extends PlcTag {
    public Char16Tag(String address, String tagName) {
        super(address, tagName);
        super.setTagSize(2);
    }

    public static boolean checkAddress(String addr) {
        return check16bit(addr);
    }

    public String tagTostr() {

        return this.getID() + "-" + this.getTagName() + "-" + this.getAddress() + ":" + textValue();
    }

    public void setValue(byte[] value) {
        this.value = S7.GetWordAt(value, 0);
        ;
    }

    public void setWriteBuffer(int valor) {
        wbuffer = new byte[tagSize];
        S7.SetWordAt(wbuffer, 0, valor);
        bufferSt = true;
    }

    public void setType() {
        super.tagType = 2;
    }

}
