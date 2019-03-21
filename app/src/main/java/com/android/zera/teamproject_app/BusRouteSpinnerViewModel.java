package com.android.zera.teamproject_app;

public class BusRouteSpinnerViewModel {
    public static String line = "Linie";
    public int routeNumber = -1;
    public boolean seleceted = false;
    public BusRouteSpinnerViewModel(int number, boolean selected){
        this.routeNumber = number;
        this.seleceted = selected;
    }
}
