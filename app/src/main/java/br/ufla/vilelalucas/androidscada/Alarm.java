package br.ufla.vilelalucas.androidscada;

import br.ufla.vilelalucas.androidscada.Tags.PlcTag;

/**
 * Created by Lucas on 29/01/2017.
 */

public class Alarm {
    public static final int verdadeiro = 0;
    public static final int falso = 1;
    public static final int maior = 2;
    public static final int menor = 3;
    public static final int maiorigual = 4;
    public static final int menorigual = 5;
    public PlcTag tag;
    String message;
    String area;
    String eqp;
    String action;
    double valRef;
    int prior;
    boolean shelv;
    boolean ack;
    boolean state;
    boolean alarm;
    int tipo;
    long id;
    private boolean estado;

    public Alarm(PlcTag tag, String mensagem, String area, String equipamento, int tipo, double valorRef) {
        this.tag = tag;
        this.message = mensagem;
        this.valRef = valorRef;
        this.area = area;
        this.eqp = equipamento;
        this.tipo = tipo;
    }

    public Alarm(PlcTag tag, String mensagem, String area, String equipamento, String acao, int tipo, int prioridade) {
        this.tag = tag;
        this.message = mensagem;
        this.area = area;
        this.eqp = equipamento;
        this.tipo = tipo;
        this.prior = prioridade;
        this.action = acao;
    }

    public Alarm(PlcTag tag, String mensagem, String area, String equipamento, String acao, int tipo, double valorRef, int prioridade) {
        this.tag = tag;
        this.message = mensagem;
        this.valRef = valorRef;
        this.area = area;
        this.eqp = equipamento;
        this.tipo = tipo;
        this.prior = prioridade;
        this.action = acao;
    }

    public Alarm(PlcTag tag, String mensagem, String area, String equipamento, int tipo) {
        this.tag = tag;
        this.message = mensagem;
        this.area = area;
        this.eqp = equipamento;
        this.tipo = tipo;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getPrior() {
        return prior;
    }

    public void setPrior(int prioridade) {
        prior = prioridade;
    }

    public void reconhecerAlarm() {
        ack = true;
    }

    public String getString() {
        return id + " - " + message;
    }

    public long getID() {
        return id;
    }

    public void setID(long id) {
        this.id = id;
    }

    public String getMensagem() {
        return message;
    }

    public String getArea() {
        return area;
    }

    public String getEquipamento() {
        return eqp;
    }

    public double getValorRef() {
        return valRef;
    }

    public int getTipo() {
        return tipo;
    }

    public String alarmInfo() {
        return tag.getTagName() + "   " + message + "    " + area + "    " + eqp;
    }

    public void update() {
        double valor = tag.getValue();
        switch (tipo) {
            case verdadeiro:
                if (valor != 0) alarm = true;
                else alarm = false;
                break;
            case falso:
                if (valor == 0) alarm = true;
                else alarm = false;
                break;
            case maior:
                if (valor < valRef) alarm = true;
                else alarm = false;
                break;
            case maiorigual:
                if (valor >= valRef) alarm = true;
                else alarm = false;
                break;
            case menorigual:
                if (valor <= valRef) alarm = true;
                else alarm = false;
                break;
            case menor:
                if (valor < valRef) alarm = true;
                else alarm = false;
                break;
            default:
                alarm = true;
                break;
        }
    }


    public boolean getStt() {
        return alarm;
    }


}
