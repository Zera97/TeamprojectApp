package com.android.zera.teamproject_app;

import android.Manifest;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Spinner;

import com.android.zera.teamproject_app.databinding.ActivityMainBinding;
import com.google.gson.Gson;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

//import com.android.zera.teamproject_app.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    Spinner spinner;
    FragmentOne f1;
    FragmentTwo f2;
    //data binding
    private ActivityMainBinding activityMainBinding;
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
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        final String[] select_qualification = {
                "Schließen", "201", "202", "203",
                "204", "205", "206", "201", "202", "203",
                "204", "205", "206", "201", "202", "203",
                "204", "205", "206", "201", "202", "203",
                "204", "205", "206"};
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayList<favSpinnerData> listVOs = new ArrayList<>();

        for (int i = 0; i < select_qualification.length; i++) {
            favSpinnerData data = new favSpinnerData();
            data.setTitle(select_qualification[i]);
            data.setSelected(false);
            listVOs.add(data);
        }
        myFavSpinnerAdapter myAdapter = new myFavSpinnerAdapter(this, 0,listVOs);


        spinner.setAdapter(myAdapter);
        BusRouteList b = new BusRouteList();
        b.routes.add(new BusRouteSpinnerViewModel(201,true));
        b.routes.add(new BusRouteSpinnerViewModel(202,false));
        b.routes.add(new BusRouteSpinnerViewModel(203,false));
        b.routes.add(new BusRouteSpinnerViewModel(204,true));
        b.routes.add(new BusRouteSpinnerViewModel(205,false));

        activityMainBinding.setBusrouteList(b);

        BusRouteSpinnerViewModel bus = new BusRouteSpinnerViewModel(201,true);
        activityMainBinding.setBusroute(bus);


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

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment,fragment);
        fragmentTransaction.commit();

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

        //Ohne klasse, aber jetzt kann man natürlich nicht o.messageID machen
        //object o = gson.fromJson(json);


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
