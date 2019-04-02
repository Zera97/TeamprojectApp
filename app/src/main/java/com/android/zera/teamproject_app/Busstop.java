package com.android.zera.teamproject_app;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;

import org.mapsforge.core.model.LatLong;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class Busstop extends Marker{

        private String[] sonstigeParameter;
        private MapView map;
        private Context mainActivity;
        private Resources res;
        private Drawable dra;

        public Busstop(MapView mapView, String[] sonstigeParameter, Context mainActivity, Resources res) {
            super(mapView);
            this.sonstigeParameter = sonstigeParameter;
            map = mapView;
            this.mainActivity = mainActivity;
            this.res = res;
            setMarkerSettings();
        }

        private void setMarkerSettings() {
            double lat = Double.parseDouble(sonstigeParameter[1]);
            double longi = Double.parseDouble(sonstigeParameter[2]);
            GeoPoint position = new GeoPoint(lat,longi);
            this.setPosition(position);
            this.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
           // this.setIcon(dra = res.getDrawable(R.drawable.bus_stop_icon, null));
            MyInfoWindow infoWindow = new MyInfoWindow(R.layout.information_bubble, map, mainActivity,sonstigeParameter);
            this.setInfoWindow(infoWindow);
        }

}
