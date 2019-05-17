package com.android.zera.teamproject_app;

import android.content.Context;
import android.content.res.Resources;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MyBusMarker extends Marker{

    private String[] sonstigeParameter;
    private MapView map;
    private Context mainActivity;
    private Resources res;

    public MyBusMarker(MapView mapView, String[] sonstigeParameter, Context mainActivity, Resources res) {
        super(mapView);
        this.sonstigeParameter = sonstigeParameter;
        map = mapView;
        this.mainActivity = mainActivity;
        this.res = res;
        setMarkerSettings();
    }

    private void setMarkerSettings() {
        double longi = Double.parseDouble(sonstigeParameter[2]);
        double lat = Double.parseDouble(sonstigeParameter[1]);
        GeoPoint position = new GeoPoint(lat,longi);
        this.setPosition(position);
        this.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
       // MyInfoWindow infoWindow = new MyInfoWindow(R.layout.information_bubble, map, mainActivity,sonstigeParameter,false);
      //  this.setInfoWindow(infoWindow);
    }
}
