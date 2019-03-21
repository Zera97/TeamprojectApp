package com.android.zera.teamproject_app;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class BusRouteSpinnerViewModel extends BaseObservable {
    public static String line = "Linie";
    public int routeNumber = -1;

    private Boolean checked = false;
    @Bindable
    public Boolean getChecked() {
        return this.checked;
    }
    public void setChecked(Boolean checked) {
        this.checked = checked;
        //notifyPropertyChanged(BR.checked);
    }

    public BusRouteSpinnerViewModel(int number, boolean checked){
        this.routeNumber = number;
        this.checked = checked;
    }
}
