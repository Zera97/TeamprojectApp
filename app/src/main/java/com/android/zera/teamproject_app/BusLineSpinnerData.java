package com.android.zera.teamproject_app;

public class BusLineSpinnerData {

    private int number;
    private boolean selected;

    public int getNumber(){
        return this.number;
    }

    public String getTitle() {
        return number + "";
    }

    public void setTitle(int number) {
        this.number = number;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public BusLineSpinnerData(int number, boolean selected){
        this.number = number;
        this.selected = selected;
    }
}