package com.android.zera.teamproject_app;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

/**
 * Überführt die Daten des Busses in eine grafisch dargestellten Form eine Markers für OSM-Android.
 * @author Fabian Theuerkauf
 * @version 1.0
 */

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

    /**
     * Gibt dem Marker die gewünschten grafischen und logischen Eigenschaften. Setzt außerdem das
     * individuelle InfoWindow des Markers.
     */
    private void setMarkerSettings() {
        double longi = Double.parseDouble(sonstigeParameter[2]);
        double lat = Double.parseDouble(sonstigeParameter[1]);
        GeoPoint position = new GeoPoint(lat,longi);
        this.setPosition(position);
        this.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        MyInfoWindow infoWindow = new MyInfoWindow(createLayout(), map,null, mainActivity,sonstigeParameter,false);
        this.setInfoWindow(infoWindow);
    }

    /**
     * Generiert ein Dynamisches Layout für das InfoWindows des Bus Markers.
     * @return dynamisches Layout
     */
    private View createLayout(){

        LinearLayout layout = new LinearLayout(mainActivity);
        LinearLayout.LayoutParams p_outer = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setBackground(mainActivity.getResources().getDrawable(R.drawable.bonuspack_bubble));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(p_outer);

        //Ueberschrift setzen
        TextView txt_Header = new TextView(mainActivity);
        TableLayout.LayoutParams p = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        p.setMargins(0,0,0,10);
        txt_Header.setLayoutParams(p);
        txt_Header.setTextSize(20.0f);
        txt_Header.setTextColor(Color.parseColor("#000000"));
        txt_Header.setTypeface(null, Typeface.BOLD);
        txt_Header.setGravity(Gravity.CENTER);
        txt_Header.setAllCaps(true);
        txt_Header.setTag("header");

        layout.addView(txt_Header);
        return layout;
    }
}
