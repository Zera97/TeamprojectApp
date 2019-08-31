package com.android.zera.teamproject_app;

/**
 * Containerklasse der Buslinien, zur Darstellung im Buslinien-DropDown.
 * @author Dirk Neumann
 * @version 1.0
 */

public class BusLineSpinnerData {

    private int number;
    private boolean selected;

    public BusLineSpinnerData(int number, boolean selected){
        this.number = number;
        this.selected = selected;
    }

    /**
     * Liefert die Nummer der Buslinie.
     * @return Nummer der Buslinie
     */
    public int getNumber(){
        return this.number;
    }

    /**
     * Liefert den Titel der Buslinie.
     * @return Titel der Buslinie
     */
    public String getTitle() {
        return number + "";
    }

    /**
     * Liefert den Wahrheitswert, ob Buslinie ausgewählt wurde.
     * @return true wenn ausgewählt, false sonst
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Setzt den Wahrheitswert, ob Buslinie ausgewählt wurde.
     * @param selected Wahrheitswert, ob Buslinie ausgewählt oder nicht.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }


}
