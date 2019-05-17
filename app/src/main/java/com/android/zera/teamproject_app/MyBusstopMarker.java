package com.android.zera.teamproject_app;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.ViewGroup;
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

        public MyBusstopMarker(Activity activity,MapView mapView,String[] sonstigeParameter, Context mainActivity, Resources res) {
            super(mapView);
            this.sonstigeParameter = sonstigeParameter;
            map = mapView;
            this.mainActivity = mainActivity;
            this.res = res;
            setMarkerSettings(activity);
        }

        private void setMarkerSettings(Activity activity) {
            double lat = Double.parseDouble(sonstigeParameter[2]);
            double longi = Double.parseDouble(sonstigeParameter[1]);
            GeoPoint position = new GeoPoint(lat,longi);
            this.setPosition(position);
            this.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            this.setIcon(mainActivity.getResources().getDrawable(R.drawable.cute_stop));
            MyInfoWindow infoWindow = new MyInfoWindow(R.layout.information_bubble, map,activity, mainActivity,sonstigeParameter,true);
            this.setInfoWindow(infoWindow);
        }

}
