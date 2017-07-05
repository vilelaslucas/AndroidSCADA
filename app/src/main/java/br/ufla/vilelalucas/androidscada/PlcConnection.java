package br.ufla.vilelalucas.androidscada;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import br.ufla.vilelalucas.androidscada.Moka7.S7;
import br.ufla.vilelalucas.androidscada.Moka7.S7Client;
import br.ufla.vilelalucas.androidscada.Tags.PlcTag;

/**
 * Created by Lucas on 04/01/2017.
 */

public class PlcConnection {
    private S7Client Client;
    private String ip;
    private int rack;
    private int slot;
    private String plcName;
    private long id;

    public PlcConnection(String ip, int rack, int slot) {
        this.ip = ip;
        this.rack = rack;
        this.slot = slot;
        Client = new S7Client();
        plcName = "PLC1";
    }

    public PlcConnection(String nome, String ip, int rack, int slot) {
        this.ip = ip;
        this.rack = rack;
        this.slot = slot;
        Client = new S7Client();
        this.plcName = nome;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public void setID(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public String getIp() {
        return ip;
    }

    public int getRack() {
        return rack;
    }

    public int getSlot() {
        return slot;
    }

    public String getPlcName() {
        return plcName;
    }


    public boolean plcConect() {

        //connect to the plc
        Client.SetConnectionType(S7.OP);
        if (Client.ConnectTo(ip, rack, slot) == 0) {
            int res = Client.ConnectTo(ip, rack, slot);
            if (res == 0)
                return true;
            else
                return false;
        } else {
            return false;
        }
    }

    public void readArea(ArrayList<PlcTag> tags, int area) {
        //Leitura de dados em blocos
        if (!tags.isEmpty()) {
            int max = 0;
            int min = 0;
            for (int i = 0; i < tags.size(); i++) {
                int offset = tags.get(i).getOffsetAddr();
                int size = tags.get(i).getTagSize();
                if (i == 0) {
                    max = offset + size;
                    min = offset;
                } else {
                    if (max < (offset + size)) {
                        max = offset + size;
                    }
                    if (min > (offset)) {
                        min = offset;
                    }
                }
            }
            byte[] buffer = new byte[max - min];
            int res = Client.ReadArea(area, 0, min, max - min, buffer);
            if (res == 0) {
                for (int i = 0; i < tags.size(); i++) {
                    int offset = tags.get(i).getOffsetAddr() - min;
                    int size = tags.get(i).getTagSize();
                    byte[] valor = new byte[size];
                    for (int k = 0; k < size; k++) {
                        valor[k] = buffer[offset + k];
                    }
                    tags.get(i).setValue(valor);
                }
            }
        }
    }

    public boolean readAlarm(Alarm alarm) {
//Leitura de alarme
        tagRead(alarm.tag);
        alarm.update();
        return alarm.getStt();
    }


    public void readArea(ArrayList<DBList> dbs) {
        //Leitura de DBs
        for (int k = 0; k < dbs.size(); k++) {
            ArrayList<PlcTag> tags = dbs.get(k).tags;
            if (!tags.isEmpty()) {
                int max = 0;
                int min = 0;
                for (int i = 0; i < tags.size(); i++) {


                    int offset = tags.get(i).getOffsetAddr();
                    int size = tags.get(i).getTagSize();
                    if (i == 0) {
                        max = offset + size;
                        min = offset;
                    } else {
                        if (max < (offset + size)) {
                            max = offset + size;
                        }
                        if (min > (offset)) {
                            min = offset;
                        }
                    }
                }
                byte[] buffer = new byte[max - min];
                int res = Client.ReadArea(S7.S7AreaDB, dbs.get(k).dbNumber, min, max - min, buffer);
                if (res == 0) {
                    for (int i = 0; i < tags.size(); i++) {
                        int offset = tags.get(i).getOffsetAddr() - min;
                        int size = tags.get(i).getTagSize();
                        byte[] valor = new byte[size];
                        for (int j = 0; j < size; j++) {
                            valor[j] = buffer[offset + j];
                        }
                        tags.get(i).setValue(valor);
                        // System.out.println(valor[0]);

                    }
                } else {
                    // System.out.println("Erro DB"+dbs.get(k).dbNumber);
                }
            }
        }
    }

    public int dbGet(ArrayList<DBList> tagsDb, int num) {
        for (int i = 0; i < tagsDb.size(); i++) {
            if (tagsDb.get(i).dbNumber == num) {
                return i;
            }
        }
        return -1;
    }

    public boolean tagWrite(ArrayList<PlcTag> tags) {
        //Escrita de Tag
        PlcTag tag;
        byte[] buffer = new byte[2];
        S7.SetShortAt(buffer, 0, 300);
        for (int i = 0; i < tags.size(); i++) {
            tag = tags.get(i);
            if (tag.toWrite()) {
                int res = Client.WriteArea(tag.getAreaMem(), tag.getDbNumber(), tag.getOffsetAddr(), tag.getTagSize(), tag.getWbuffer());
                //  System.out.println("Write " + tag.getAddress());
                if (res == 0) {
                    tag.cleanWB();
                    // System.out.println("Write " + tag.getAddress());
                }
            }
        }
        return false;
    }


    //Leitura de uma única Tag
    public boolean tagRead(PlcTag tag) {
        byte[] buffer = new byte[tag.getTagSize()];
        int res;
        try {
            res = Client.ReadArea(tag.getAreaMem(), tag.getDbNumber(), tag.getOffsetAddr(), tag.getTagSize(), buffer);
        } catch (Exception e) {
            return false;
        }
        tag.setValue(buffer);
        if (res == 0) return true;
        else return false;
    }

    //Procedimento de leitura de Tags em massa procedimento executa a leitura de áreas maiorres de memória
    public boolean tagRead(ArrayList<PlcTag> tags) {
        //try{
        long ctime = System.currentTimeMillis();
        ArrayList<PlcTag> tagsM = new ArrayList<PlcTag>();
        ArrayList<PlcTag> tagsI = new ArrayList<PlcTag>();
        ArrayList<PlcTag> tagsQ = new ArrayList<PlcTag>();
        ArrayList<DBList> tagsDB = new ArrayList<DBList>();
        for (int i = 0; i < tags.size(); i++) {
            switch (tags.get(i).getAreaMem()) {
                case S7.S7AreaMK:
                    tagsM.add(tags.get(i));
                    break;
                case S7.S7AreaPA:
                    tagsI.add(tags.get(i));
                    break;
                case S7.S7AreaPE:
                    tagsQ.add(tags.get(i));
                    break;
                case S7.S7AreaDB: {
                    int db = tags.get(i).getDbNumber();
                    int dbIndex = dbGet(tagsDB, db);
                    if (dbIndex < 0) {
                        DBList aux = new DBList(db);
                        aux.tags.add(tags.get(i));
                        tagsDB.add(aux);
                    } else {
                        tagsDB.get(dbIndex).tags.add(tags.get(i));
                    }
                }
                break;
            }
        }
        readArea(tagsM, S7.S7AreaMK);
        readArea(tagsI, S7.S7AreaPA);
        readArea(tagsQ, S7.S7AreaPE);
        readArea(tagsDB);

        //System.out.println("TS:"+(System.currentTimeMillis()-ctime));

        // }catch (Exception e){                System.out.println("Exception");
        // }
        return false;
    }

    public boolean tagWrite(PlcTag tag) {
        int res;

        res = Client.WriteArea(tag.getAreaMem(), tag.getDbNumber(), tag.getOffsetAddr(), tag.getTagSize(), tag.getWbuffer());
        // System.out.println("Leitura:"+res);
        if (res == 0) return true;
        else return false;
    }

    public String plcInfo() {
        return id + "   " + plcName + "  " + ip + "   " + rack + "   " + "    " + slot;
    }

    public boolean isConnected() {
        return Client.Connected;
    }

    public class DBList {
        public ArrayList<PlcTag> tags;
        public int dbNumber;

        public DBList(int dbNumber) {
            this.dbNumber = dbNumber;
            tags = new ArrayList<PlcTag>();
        }
    }


}

