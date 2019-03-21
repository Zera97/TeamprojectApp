package com.android.zera.teamproject_app;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.gson.Gson;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private MapView map = null;
    private IMapController mapController = null;
    private MyLocationNewOverlay mLocationOverlay = null;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //referenz zum einfügen der Bushaltestellen

        // https://github.com/osmdroid/osmdroid/wiki/Markers,-Lines-and-Polygons
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctx = getApplicationContext();

        ActivityCompat.requestPermissions(MainActivity.this, new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION},1);

        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));


        map = (MapView) findViewById(R.id.map);
        map.setMultiTouchControls(true);
        map.setTileSource(TileSourceFactory.MAPNIK);


        mapController =  map.getController();
        mapController.setZoom(18.0);
        GeoPoint startPoint = new GeoPoint(51.826918, 10.760942);
        mapController.setCenter(startPoint);

        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx),map);
        this.mLocationOverlay.enableMyLocation();
        this.mLocationOverlay.enableFollowLocation();
        map.getOverlays().add(this.mLocationOverlay);

    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    System.out.println("Wifi");
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    System.out.println("mobile");
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public void TestVerbindung(View v) {
        //boolean b = haveNetworkConnection();
        //System.out.println(b ? "ja" : "nein");
        System.out.println(createJSON(new MSGData()));
        new MiddleWareConnector(this).execute();
    }

    private String createJSON(MSGData dataObj){
        StringBuilder sb = new StringBuilder();
        Gson gson = new Gson();
        MSGHeader headerOjb = new MSGHeader(1,1,1,dataObj);
        String header = gson.toJson(headerOjb);
        String data = gson.toJson(dataObj);
        /*
        sb.append(header.substring(0, header.length() - 1));
        sb.append(",\"Data\":");
        sb.append(data);
        sb.append("}"); */
        sb.append(header);
        //Konvertiert JSON String in Object
        //MSGData test = gson.fromJson(json,MSGData.class);
        //System.out.println("Hallo:" + json);

        return sb.toString();
    }

    public void onResume(){
        super.onResume();
        map.onResume();
    }

    public void onPause(){
        super.onPause();
        map.onPause();
    }
}
