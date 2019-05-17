package com.android.zera.teamproject_app;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MyBusstopMarker extends Marker{

        private String[] sonstigeParameter;
        private MapView map;
        private Context mainActivity;
        private Resources res;

        public MyBusstopMarker(MapView mapView,String[] sonstigeParameter, Context mainActivity, Resources res) {
            super(mapView);
            this.sonstigeParameter = sonstigeParameter;
            map = mapView;
            this.mainActivity = mainActivity;
            this.res = res;
            setMarkerSettings();
        }

        private void setMarkerSettings() {
            double lat = Double.parseDouble(sonstigeParameter[2]);
            double longi = Double.parseDouble(sonstigeParameter[1]);
            GeoPoint position = new GeoPoint(lat,longi);
            this.setPosition(position);
            this.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            this.setIcon(mainActivity.getResources().getDrawable(R.drawable.cute_stop));
            MyInfoWindow infoWindow = new MyInfoWindow(createLayout(),R.layout.information_bubble, map, mainActivity,sonstigeParameter,true);
            this.setInfoWindow(infoWindow);
        }

    private TableLayout createLayout(){
        TableLayout layout = new TableLayout(mainActivity);
        TableRow row = new TableRow(mainActivity);
        TextView txtView = new TextView(mainActivity);
        TableLayout.LayoutParams p = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        txtView.setLayoutParams(p);
        txtView.setText("HALLO");
        txtView.setTextSize(18.0f);
        txtView.setTag("TEST");
        //row.addView(txtView);
        layout.addView(txtView);
        return layout;
    }

}
