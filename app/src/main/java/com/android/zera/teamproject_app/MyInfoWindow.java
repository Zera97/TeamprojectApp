package com.android.zera.teamproject_app;

import android.content.Context;
import android.widget.TextView;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.infowindow.InfoWindow;


public class MyInfoWindow extends InfoWindow {

    private Context mainContext;
    private String[] params;

    public MyInfoWindow(int layoutResId, MapView mapView, Context mainActivity, String[] stringArray) {
        super(layoutResId, mapView);
        this.params = stringArray;
        this.mainContext = mainActivity;
    }

    public void onClose() {
    }

    public void onOpen(final Object arg0) {
        TextView txtTitle = (TextView) mView.findViewById(R.id.bubble_title);

        txtTitle.setText(params[0]);
    }

}
