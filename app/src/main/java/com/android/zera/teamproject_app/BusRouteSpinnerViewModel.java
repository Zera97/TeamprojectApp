package com.android.zera.teamproject_app;

//import android.databinding.BaseObservable;
//import android.databinding.Bindable;
//extends BaseObservable
public class BusRouteSpinnerViewModel{
    public static String line = "Linie";
    public int routeNumber = -1;
    public String name = "";
    private Boolean checked = false;
    //@Bindable
    public Boolean getChecked() {
        return this.checked;
    }
    public void setChecked(Boolean checked) {
        this.checked = checked;
        //notifyPropertyChanged(BR.checked);
    }

    @Override
    public String toString(){
        return this.name;
    }

    public BusRouteSpinnerViewModel(int number, boolean checked){
        this.routeNumber = number;
        this.checked = checked;
        this.name = BusRouteSpinnerViewModel.line + " " + this.routeNumber;
    }
}
