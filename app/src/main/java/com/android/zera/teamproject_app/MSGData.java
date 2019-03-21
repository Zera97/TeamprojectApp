package com.android.zera.teamproject_app;

import java.util.ArrayList;

public class MSGData {
    private int commandCode = -1;
    private long arriveUntil = -1;
    private long earliestDeparture = -1;

    private Integer StartBusStop = null;
    private Integer DestinationBusStop = null;
    private Integer InformationAboutBusStop = null;
    private ArrayList<Integer> Linien = null;



    public int getCommandCode() {
        return commandCode;
    }

    public void setCommandCode(int commandCode) {
        this.commandCode = commandCode;
    }

    public ArrayList<Integer> getLinien() {
        return Linien;
    }

    public void setLinien(ArrayList<Integer> linien) {
        Linien = linien;
    }

    public long getArriveUntil() {
        return arriveUntil;
    }

    public void setArriveUntil(long arriveUntil) {
        this.arriveUntil = arriveUntil;
    }

    public long getEarliestDeparture() {
        return earliestDeparture;
    }

    public void setEarliestDeparture(long earliestDeparture) {
        this.earliestDeparture = earliestDeparture;
    }

    public Integer getStartBusStop() {
        return StartBusStop;
    }

    public void setStartBusStop(Integer startBusStop) {
        StartBusStop = startBusStop;
    }

    public Integer getDestinationBusStop() {
        return DestinationBusStop;
    }

    public void setDestinationBusStop(Integer destinationBusStop) {
        DestinationBusStop = destinationBusStop;
    }

    public Integer getInformationAboutBusStop() {
        return InformationAboutBusStop;
    }

    public void setInformationAboutBusStop(Integer informationAboutBusStop) {
        InformationAboutBusStop = informationAboutBusStop;
    }
}
