package com.android.zera.teamproject_app;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

public class MyBusstopMarker extends Marker {

    private String[] sonstigeParameter;
    private MapView map;
    private Context mainActivity;
    private Resources res;
    private ArrayList<BusStopData> myBusstopMarkers;

    public MyBusstopMarker(MapView mapView,ArrayList<BusStopData> myBusstopMarkers, String[] sonstigeParameter, Context mainActivity, Resources res) {
        super(mapView);
        this.sonstigeParameter = sonstigeParameter;
        map = mapView;
        this.mainActivity = mainActivity;
        this.res = res;
        this.myBusstopMarkers = myBusstopMarkers;
        setMarkerSettings();
    }

    private void setMarkerSettings() {
        double lat = Double.parseDouble(sonstigeParameter[2]);
        double longi = Double.parseDouble(sonstigeParameter[1]);
        GeoPoint position = new GeoPoint(lat, longi);
        this.setPosition(position);
        this.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        this.setIcon(mainActivity.getResources().getDrawable(R.drawable.cute_stop));
        MyInfoWindow infoWindow = new MyInfoWindow(createLayout(), map,myBusstopMarkers,mainActivity, sonstigeParameter, true);
        this.setInfoWindow(infoWindow);
    }

    private View createLayout() {

        LinearLayout outerLayout = new LinearLayout(mainActivity);
        LinearLayout.LayoutParams p_outer = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        outerLayout.setBackground(mainActivity.getResources().getDrawable(R.drawable.bonuspack_bubble));
        outerLayout.setOrientation(LinearLayout.VERTICAL);
        outerLayout.setLayoutParams(p_outer);

        TableLayout innerLayout = new TableLayout(mainActivity);
        TableLayout.LayoutParams p_inner = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        innerLayout.setLayoutParams(p_inner);

        //Ueberschrift setzen
        TextView txt_Header = new TextView(mainActivity);
        TableLayout.LayoutParams p = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        p.setMargins(0, 0, 0, 10);
        txt_Header.setLayoutParams(p);
        txt_Header.setTextSize(20.0f);
        txt_Header.setTextColor(Color.parseColor("#000000"));
        txt_Header.setTypeface(null, Typeface.BOLD);
        txt_Header.setGravity(Gravity.CENTER);
        txt_Header.setAllCaps(true);
        txt_Header.setTag("header");

        innerLayout.addView(txt_Header);

        TableRow.LayoutParams p_row = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        p_row.setMarginEnd(10);

        //Zeilen generieren
        int grenze = 5;
        for (int i = 0; i < grenze; i++) {

            //Textview für Linien generieren
            TextView txt_Linie = new TextView(mainActivity);
            txt_Linie.setLayoutParams(p_row);
            txt_Linie.setTextSize(16.0f);
            txt_Linie.setTextColor(Color.parseColor("#000000"));
            txt_Linie.setTypeface(null, Typeface.BOLD);
            txt_Linie.setText("Platzhalter Linie");
            txt_Linie.setTag("linie" + i);

            //Textview für Abfahrtzeiten generieren
            TextView txt_Zeit = new TextView(mainActivity);
            txt_Zeit.setLayoutParams(p_row);
            txt_Zeit.setTextSize(16.0f);
            txt_Zeit.setTextColor(Color.parseColor("#000000"));
            txt_Zeit.setTypeface(null, Typeface.BOLD);
            txt_Zeit.setText("Platzhalter Zeit");
            txt_Zeit.setTag("zeit" + i);

            //zeile generieren und Layout setzen
            TableRow row = new TableRow(mainActivity);
            row.setLayoutParams(p);

            //Zeilenelemente hinzufuegen
            row.addView(txt_Linie);
            row.addView(txt_Zeit);
            innerLayout.addView(row);
        }

        outerLayout.addView(innerLayout);
        return outerLayout;
    }

}
