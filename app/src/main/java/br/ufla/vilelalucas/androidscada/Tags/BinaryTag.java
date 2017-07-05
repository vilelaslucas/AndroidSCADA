package br.ufla.vilelalucas.androidscada.Tags;

import br.ufla.vilelalucas.androidscada.Moka7.S7;

/**
 * Created by Lucas on 10/01/2017.
 */

public class BinaryTag extends PlcTag {


    public BinaryTag(String address, String tagName) {
        super(address, tagName);
        super.setTagSize(1);
        super.setBitNumber();
    }

    public static boolean checkAddress(String addr) {
        return checkBool(addr);
    }

    public void setValue(byte[] value) {
        if (S7.GetBitAt(value, 0, getBitNumber())) {
            this.value = 1;
        } else {
            this.value = 0;
        }
    }

    public void setWriteBuffer(boolean valor) {
        wbuffer = new byte[tagSize];
        S7.SetBitAt(wbuffer, 0, getBitNumber(), valor);
        bufferSt = true;
    }

    public void setWriteBuffer(int valor) {
        wbuffer = new byte[tagSize];
        if (valor != 0) S7.SetBitAt(wbuffer, 0, getBitNumber(), true);
        else S7.SetBitAt(wbuffer, 0, getBitNumber(), false);
        bufferSt = true;
    }

    public String tagTostr() {
        return this.getID() + "-" + this.getTagName() + "-" + this.getAddress() + ":" + boolValue();
    }

    public void setType() {
        super.tagType = 1;
    }
}
