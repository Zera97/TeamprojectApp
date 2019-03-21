package com.android.zera.teamproject_app;



public class CommunicationParams {
    public int commandCode = -1;
    public long arriveUntil = -1;
    public long earliestDeparture = -1;

    public Integer StartBusStop = null;
    public Integer DestinationBusStop = null;
    public Integer InformationAboutBusStop = null;
    public ArrayList<Integer> Linien = null;

    private String toJSON() {
//		StringBuilder s = new StringBuilder("");
//		s.append("{");
//		s.append("\"" + "commandCode" + "\": " + commandCode + ", ");
//		s.append("\"" + "arriveUntil" + "\": " + arriveUntil + ", ");
//		s.append("\"" + "earliestDeparture" + "\": " + earliestDeparture + ", ");
//		s.append("\"" + "StartBusStop" + "\":{\"intValue\"" + StartBusStop + "} ");
//		s.append("}");
//		return s.toString();
        return "end";
    }
}



public class MSGData {

    public int zahl;
    public String name;

    public MSGData(int zahl,String name){
        this.name = name;
        this.zahl = zahl;
    }

    @Override
    public String toString() {
        return "MSGData{" +
                "zahl=" + zahl +
                ", name='" + name + '\'' +
                '}';
    }
/*
     <Button
        android:id="@+id/btn2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_toEndOf="@id/btn_verbindung"
        android:text="HALLO"/>

    <Button
        android:id="@+id/btn3"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="TSCHAU"
        android:layout_toEndOf="@id/btn2"/>

    <Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="TSCHAU2"
        android:layout_toEndOf="@id/btn3"/>
     */
}
