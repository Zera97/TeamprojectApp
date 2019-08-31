package com.android.zera.teamproject_app;

import java.util.ArrayList;

/**
 * Containerklasse der Favoriten, zur Darstellung im Favoriten-DropDown.
 * @author Dirk Neumann
 * @version 1.0
 */

public class FavoritsData {

    private ArrayList<Integer> numbers;
    private boolean selected;
    private String title;
    public FavoritsSpinnerAdapter.ViewHolder viewHolder;

    public FavoritsData(ArrayList<Integer> numbers){
        this.setNumbers(numbers);
        //this.selected = selected;
    }

    /**
     * Liefert den Titel des Favoriten Tabs.
     * @return Titel der Favoriten Auswahl
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Setzt den Titel des Favoriten Tabs.
     * @param title Neuer Titel
     */
    public void setTitle(String title){
        this.title = title;
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

    /**
     * Wandelt eine Liste von Zahlen in einen String mit gleicher Bedeutung um und setzt den String
     * als neuen Titel des Favoriten Tabs.
     * @param numbers Liste von Zahlen
     */
    public void setNumbers(ArrayList<Integer> numbers){
        this.numbers = numbers;
        if(numbers == null){
            this.title= "Meine Favoriten";
        }
        else{
            String s = "";
            for (int i = 0; i< this.numbers.size();i++){
                s += this.numbers.get(i);
                if(i != this.numbers.size() - 1){
                    s += ", ";
                }
            }
            this.title = s;
        }
    }

    /**
     * Liefert eine Liste aller Nummern im Favoriten Tab.
     * @return Liste aller Nummern
     */
    public ArrayList<Integer> getNumbers(){
        return this.numbers;
    }

}
