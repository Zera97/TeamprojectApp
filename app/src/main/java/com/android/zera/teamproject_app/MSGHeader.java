package com.android.zera.teamproject_app;

public class MSGHeader {

    private int sender = -1;
    private int id = -1;
    private int codierung = -1;
    MSGData data = null;

    public MSGHeader(int sender,int id,int codierung,MSGData data){
        this.sender = sender;
        this.id = id;
        this.codierung = codierung;
        this.data = data;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodierung() {
        return codierung;
    }

    public void setCodierung(int codierung) {
        this.codierung = codierung;
    }
}
