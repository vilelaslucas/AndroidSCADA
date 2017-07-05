package br.ufla.vilelalucas.androidscada.Tags;

import java.nio.charset.Charset;

import br.ufla.vilelalucas.androidscada.Moka7.S7;

/**
 * Created by Lucas on 10/01/2017.
 */

public class PlcTag {
    public static final int tBoolean = 1;
    public static final int tInt = 6;
    public static final int tUInt = 11;
    public static final int tFloat = 5;
    public static final int tChar = 3;
    public static final int tLongInt = 7;
    public static final int tLongUint = 8;
    public static final int tShortInt = 9;
    public static final int tShortUInt = 10;
    public static final int tDouble = 4;
    public static final int tChar16 = 2;
    String tagAddress;
    double value;
    String tagName;
    int tagSize;
    int tagType;
    int areaMem;
    int offsetAddr;
    int dbNumber;
    boolean read;
    boolean write;
    int bitNumber;//Uso exclusivo para variaveis binárias
    long tagID;
    byte[] wbuffer;
    boolean bufferSt;

    public PlcTag(String tagName, String address) {
        this.tagAddress = address;
        this.tagName = tagName;
        this.value = 0.0;
        this.tagSize = 2;
        this.setMemArea(address);
        this.read = true;
        this.write = false;
        bufferSt = false;
        this.setType();
    }

    public static boolean checkAddress(String addr) {
        return checkBool(addr) && check8bit(addr) && check16bit(addr) && check32bit(addr) && check64bit(addr);
    }

    public static boolean checkBool(String addr) {
        try {
            addr = addr.toUpperCase();
            System.out.println(addr);
            String[] part = addr.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            boolean check;
            if ((part[0].contentEquals("I") || part[0].contentEquals("Q") || part[0].contentEquals("M")) && part.length == 4 && part[2].contentEquals(".")) {
                int offset = Integer.parseInt(part[1]);
                int bit = Integer.parseInt(part[3]);
                if (offset >= 0 && bit >= 0 && bit < 8) {
                    System.out.println("Valor correto");
                    return true;
                }

            } else if (part[0].contentEquals("DB") && part[2].contentEquals(",D") && part.length == 6) {
                int dbNum = Integer.parseInt(part[1]);
                int offset = Integer.parseInt(part[3]);
                int bit = Integer.parseInt(part[5]);

                if (dbNum >= 0 && offset >= 0 && bit >= 0 && bit < 8) {
                    System.out.println("Valor correto");
                    return true;
                }
            } else

                return false;

        } catch (Exception e) {
            return false;

        }
        return false;
    }

    public static boolean check8bit(String addr) {
        try {
            addr = addr.toUpperCase();
            System.out.println(addr);
            String[] part = addr.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            boolean check;
            if ((part[0].contentEquals("IB") || part[0].contentEquals("QB") || part[0].contentEquals("MB")) && part.length == 2) {
                int offset = Integer.parseInt(part[1]);
                if (offset >= 0) {
                    System.out.println("Valor correto");
                    return true;
                }

            } else if (part[0].contentEquals("DB") && part[2].contentEquals(",DBB") && part.length == 4) {
                int dbNum = Integer.parseInt(part[1]);
                int offset = Integer.parseInt(part[3]);

                if (dbNum >= 0 && offset >= 0) {
                    System.out.println("Valor correto");
                    return true;
                }
            } else

                return false;

        } catch (Exception e) {
            return false;

        }
        return false;
    }

    public static boolean check16bit(String addr) {
        try {
            addr = addr.toUpperCase();
            System.out.println(addr);
            String[] part = addr.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            boolean check;
            if ((part[0].contentEquals("IW") || part[0].contentEquals("QW") || part[0].contentEquals("MW")) && part.length == 2) {
                int offset = Integer.parseInt(part[1]);
                if (offset >= 0) {
                    System.out.println("Valor correto");
                    return true;
                }

            } else if (part[0].contentEquals("DB") && part[2].contentEquals(",DW") && part.length == 4) {
                int dbNum = Integer.parseInt(part[1]);
                int offset = Integer.parseInt(part[3]);

                if (dbNum >= 0 && offset >= 0) {
                    System.out.println("Valor correto");
                    return true;
                }
            } else

                return false;

        } catch (Exception e) {
            return false;

        }
        return false;
    }

    public static boolean check32bit(String addr) {
        try {
            addr = addr.toUpperCase();
            System.out.println(addr);
            String[] part = addr.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            boolean check;
            if ((part[0].contentEquals("ID") || part[0].contentEquals("QD") || part[0].contentEquals("MD")) && part.length == 2) {
                int offset = Integer.parseInt(part[1]);
                if (offset >= 0) {
                    System.out.println("Valor correto");
                    return true;
                }

            } else if (part[0].contentEquals("DB") && part[2].contentEquals(",DD") && part.length == 4) {
                int dbNum = Integer.parseInt(part[1]);
                int offset = Integer.parseInt(part[3]);

                if (dbNum >= 0 && offset >= 0) {
                    System.out.println("Valor correto");
                    return true;
                }
            } else

                return false;

        } catch (Exception e) {
            return false;

        }
        return false;
    }

    public static boolean check64bit(String addr) {
        try {
            addr = addr.toUpperCase();
            System.out.println(addr);
            String[] part = addr.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            boolean check;
            if ((part[0].contentEquals("ID") || part[0].contentEquals("QD") || part[0].contentEquals("MD")) && part.length == 2) {
                int offset = Integer.parseInt(part[1]);
                if (offset >= 0) {
                    System.out.println("Valor correto");
                    return true;
                }

            } else if (part[0].contentEquals("DB") && part[2].contentEquals(",DD") && part.length == 4) {
                int dbNum = Integer.parseInt(part[1]);
                int offset = Integer.parseInt(part[3]);

                if (dbNum >= 0 && offset >= 0) {
                    System.out.println("Valor correto");
                    return true;
                }
            } else

                return false;

        } catch (Exception e) {
            return false;

        }
        return false;
    }

    public static PlcTag createTag(String nome, String address, int tipo) {
        PlcTag tag;
        switch (tipo) {
            case tBoolean:
                tag = new BinaryTag(nome, address);
                break;
            case tChar16:
                tag = new Char16Tag(nome, address);
                break;
            case tChar:
                tag = new CharTag(nome, address);
                break;
            case tDouble:
                tag = new DoubleTag(nome, address);
                break;
            case tFloat:
                tag = new FloatTag(nome, address);
                break;
            case tInt:
                tag = new IntTag(nome, address);
                break;
            case tLongInt:
                tag = new LongIntTag(nome, address);
                break;
            case tLongUint:
                tag = new LongUIntTag(nome, address);
                break;
            case tShortInt:
                tag = new ShortIntTag(nome, address);
                break;
            case tShortUInt:
                tag = new ShortUIntTag(nome, address);
                break;
            case tUInt:
                tag = new UIntTag(nome, address);
                break;
            default:
                tag = new PlcTag(nome, address);
        }
        return tag;
    }

    public static int getTipoNum(String tipo) {
        if (tipo.equalsIgnoreCase("Boolean")) {
            return tBoolean;
        } else if (tipo.equalsIgnoreCase("Int")) {
            return tInt;
        } else if (tipo.equalsIgnoreCase("UInt")) {
            return tUInt;
        } else if (tipo.equalsIgnoreCase("Float")) {
            return tFloat;
        } else if (tipo.equalsIgnoreCase("Char")) {
            return tChar;
        } else if (tipo.equalsIgnoreCase("Long Int")) {
            return tLongInt;
        } else if (tipo.equalsIgnoreCase("Long Uint")) {
            return tLongUint;
        } else if (tipo.equalsIgnoreCase("Short Int")) {
            return tShortInt;
        } else if (tipo.equalsIgnoreCase("Short UInt")) {
            return tShortUInt;
        } else
            return 0;
    }

    public static PlcTag createTag(String nome, String address, String tipo) {
        switch (getTipoNum(tipo)) {
            case tBoolean:
                if (BinaryTag.checkAddress(address))
                    return new BinaryTag(nome, address);
                break;
            case tChar16:
                if (Char16Tag.checkAddress(address))
                    return new Char16Tag(nome, address);
                break;
            case tChar:
                if (CharTag.checkAddress(address))
                    return new CharTag(nome, address);
                break;
            case tDouble:
                if (DoubleTag.checkAddress(address))
                    return new DoubleTag(nome, address);
                break;
            case tFloat:
                if (FloatTag.checkAddress(address))
                    return new FloatTag(nome, address);
                break;
            case tInt:
                if (IntTag.checkAddress(address))
                    return new IntTag(nome, address);
                break;
            case tLongInt:
                if (LongIntTag.checkAddress(address))
                    return new LongIntTag(nome, address);
                break;
            case tLongUint:
                if (LongUIntTag.checkAddress(address))
                    return new LongUIntTag(nome, address);
                break;
            case tShortInt:
                if (ShortIntTag.checkAddress(address))
                    return new ShortIntTag(nome, address);
                break;
            case tShortUInt:
                if (ShortUIntTag.checkAddress(address))
                    return new ShortUIntTag(nome, address);
                break;
            case tUInt:
                if (UIntTag.checkAddress(address))
                    return new UIntTag(nome, address);
                break;
            default:
                if (PlcTag.checkAddress(address))
                    return new PlcTag(nome, address);
        }
        return null;
    }

    public String getStrVal() {
        switch (tagType) {
            case tBoolean:
                return "" + boolValue();
            case tInt | tUInt:
                return "" + intValue();
            case tFloat:
                return "" + value;
            default:
                return "";
        }
    }

    public void setWriteBuffer(boolean valor) {
        wbuffer = new byte[tagSize];
        S7.SetBitAt(wbuffer, 0, getBitNumber(), valor);
        bufferSt = true;
    }

    public void setWriteBuffer(int valor) {
        wbuffer = new byte[tagSize];
        S7.SetWordAt(wbuffer, 0, valor);
        bufferSt = true;
    }

    public void setWriteBuffer(float valor) {
        wbuffer = new byte[tagSize];
        S7.SetFloatAt(wbuffer, 0, valor);
        bufferSt = true;
    }

    public void setWriteBuffer(byte[] valor) {
        wbuffer = valor;
        bufferSt = true;
    }

    public void setWrite() {
        wbuffer = new byte[tagSize];
        bufferSt = true;
    }

    public void cleanWB() { //Limpa buffer de escrita
        bufferSt = false;
    }

    public boolean toWrite() { //Retorna se existe um buffer para escrever
        return (bufferSt);
    }

    public String getAddress() {
        return tagAddress;
    }

    public long getID() {
        return tagID;
    }

    public void setID(long ID) {
        this.tagID = ID;
    }

    public int getType() {
        return tagType;
    }

    public void setType() {
        tagType = 0;
    }

    public boolean getRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean getWrite() {
        return write;
    }

    public void setWrite(boolean write) {
        this.write = write;
    }

    public String getTagAddress() {
        return tagAddress;
    }

    public int getTagSize() {
        return tagSize;
    }

    public void setTagSize(int tagSize) {
        this.tagSize = tagSize;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(byte[] value) {


    }

    public boolean boolValue() {
        if (textValue() != 0) {
            return true;
        } else {
            return false;
        }
    }

    public char textValue() {
        int intVal = new Double(value).intValue();
        char character = (char) intVal;
        return character;
    }

    public long longValue() {
        return new Double(value).longValue();
    }

    public int intValue() {
        return new Double(value).intValue();
    }

    public String getTagName() {
        return tagName;
    }

    public int getAreaMem() {
        return areaMem;
    }

    public int getOffsetAddr() {
        return offsetAddr;
    }

    public int getDbNumber() {
        return dbNumber;
    }

    public int getBitNumber() {
        return bitNumber;
    }

    public byte[] getWbuffer() {
        return wbuffer;
    }

    public void setBitNumber() {
        try {
            String[] part = tagAddress.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            boolean check;
            if ((part[0].contentEquals("I") || part[0].contentEquals("Q") || part[0].contentEquals("M")) && part.length == 4 && part[2].contentEquals(".")) {
                bitNumber = Integer.parseInt(part[3]);

            } else if (part[0].contentEquals("DB") && part[2].contentEquals(",D") && part.length == 6) {
                bitNumber = Integer.parseInt(part[5]);
            }
        } catch (Exception e) {
            bitNumber = 0;

        }
    }

    public int getEndAddr() {
        return getOffsetAddr() + getTagSize();
    }

    public void setMemArea(String addr) {
        addr.toUpperCase();
        String[] part = addr.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        if (addr.startsWith("I")) {
            this.areaMem = S7.S7AreaPE;
            this.offsetAddr = Integer.parseInt(part[1]);
            this.dbNumber = 0;
        }
        if (addr.startsWith("Q")) {
            this.areaMem = S7.S7AreaPA;
            this.offsetAddr = Integer.parseInt(part[1]);
            this.dbNumber = 0;
        }
        if (addr.startsWith("M")) {
            this.areaMem = S7.S7AreaMK;
            this.offsetAddr = Integer.parseInt(part[1]);
            this.dbNumber = 0;
        }
        if (addr.startsWith("DB")) {
            this.areaMem = S7.S7AreaDB;
            this.dbNumber = Integer.parseInt(part[1]);
            this.offsetAddr = Integer.parseInt(part[3]);

        }
    }

    public int getRW() {
        if (write && read) {
            return 3;
        } else if (write) {
            return 2;
        } else if (read) {
            return 1;
        } else return 0;
    }

    public void setRW(int rw) {
        switch (rw) {
            case 0:
                read = false;
                write = false;
                break;
            case 1:
                read = true;
                write = false;
                break;
            case 2:
                read = false;
                write = true;
                break;
            case 3:
                read = true;
                write = true;
                break;
            default:
                read = false;
                write = false;
                break;
        }
    }

    public String txtType() {
        String tipo = "";
        switch (this.tagType) {
            case 1:
                tipo = "Boolean";
                break;
            case 2:
                tipo = "Char16";
                break;
            case 3:
                tipo = "Char";
                break;
            case 4:
                tipo = "Double";
                break;
            case 5:
                tipo = "Float";
                break;
            case 6:
                tipo = "Int";
                break;
            case 7:
                tipo = "Long Int";
                break;
            case 8:
                tipo = "Long UInt";
                break;
            case 9:
                tipo = "Short Int";
                break;
            case 10:
                tipo = "Short UInt";
                break;
            case 11:
                tipo = "UInt";
                break;
            default:
                tipo = "PLC Tag";
        }


        return tipo;
    }

    public String tagTostr() {
        return tagName + "  -  " + tagAddress + " " + value;
    }

    public String tagInfo() {
        return tagID + " - " + tagName + "   " + tagAddress + "   " + txtType() + "   " + rwStr();
    }

    public String rwStr() {
        switch (getRW()) {
            case 1:
                return "Read";
            case 2:
                return "Write";
            case 3:
                return "Read/Write";
            default:
                return "-";
        }
    }

}
