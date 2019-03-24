package com.android.zera.teamproject_app;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private final String TAG = this.getClass().getSimpleName();
    private MapView map = null;
    private IMapController mapController = null;
    private MyLocationNewOverlay mLocationOverlay = null;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //referenz zum einfügen der Bushaltestellen
        //https://github.com/osmdroid/osmdroid/wiki/Markers,-Lines-and-Polygons

        /*
            Sidebar Initialisierung
        */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //=========================================================================================

        /*
            Map Initialisierung
         */
        ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));


        map = (MapView) findViewById(R.id.map);
        map.setMultiTouchControls(true);
        map.setTileSource(TileSourceFactory.MAPNIK);


        mapController = map.getController();
        mapController.setZoom(18.0);
        GeoPoint startPoint = new GeoPoint(51.826918, 10.760942);
        mapController.setCenter(startPoint);

        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx), map);
        this.mLocationOverlay.enableMyLocation();
        this.mLocationOverlay.enableFollowLocation();
        map.getOverlays().add(this.mLocationOverlay);

        //=========================================================================================

        /*
            Slider Initialisierung
         */

        Log.e("Hallo","Ich initialisiere gleich");
        this.initBusLineSlider();
        this.initFavoritsSlider();
        Log.e("Hallo","Ich habe initialisiert");

        //=========================================================================================

        /*
            Permission erfragen
         */

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        //=========================================================================================

    }

    /*
        Verbindungs-Methoden:
            -Connection Check
            -Verbindungsaufbau
            -JSON String erzeugen
     */

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
        boolean b = haveNetworkConnection();
        System.out.println(b ? "ja" : "nein");
        System.out.println(createJSON(new MSGData()));
        if(b){
            new MiddleWareConnector(this).execute();
        } else {
            Toast.makeText(this, "Bitte für eine Internetverbindung sorgen.",
                    Toast.LENGTH_SHORT).show();
            /*
            b = haveNetworkConnection();
            while(!b){
                if(b){
                    new MiddleWareConnector(this).execute();
                    break;
                }
                b = haveNetworkConnection();
            } */
        }

    }

    private String createJSON(MSGData dataObj) {
        StringBuilder sb = new StringBuilder();
        Gson gson = new Gson();
        MSGHeader headerOjb = new MSGHeader(1, 1, 1, dataObj);
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

    /*
        OSM-Android Methoden
     */
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    public void onPause() {
        super.onPause();
        map.onPause();
    }

    /*
        Sidebar Methoden
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.impressum, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.punkt1) {

        } else if (id == R.id.punkt2) {

        } else if (id == R.id.punkt3) {

        } else if (id == R.id.nav_contact) {

        } else if (id == R.id.nav_impressum) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
        Slider Methoden
     */
    private void initBusLineSlider() {
        final int[] select_qualification = this.getAllBusLines();
        Spinner spinner = findViewById(R.id.BusLineSpinner);
        ArrayList<BusLineSpinnerData> listVOs = new ArrayList<>();
        for (int i = 0; i < select_qualification.length; i++) {
            BusLineSpinnerData data = new BusLineSpinnerData(select_qualification[i], false);
            listVOs.add(data);
        }
        BusLineSpinnerAdapter myAdapter = new BusLineSpinnerAdapter(this, 0, listVOs);
        spinner.setAdapter(myAdapter);
    }

    private int[] getAllBusLines() {
        int[] lines = new int[100];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = i + 200;
        }
        return lines;
    }

    private void initFavoritsSlider() {
        final ArrayList<ArrayList<Integer>> favorites = this.getAllFavorites();
        Spinner spinner = findViewById(R.id.FavoritsSpinner);
        ArrayList<FavoritsData> listVOs = new ArrayList<>();
        for (int i = 0; i < favorites.size(); i++) {
            FavoritsData data = new FavoritsData(favorites.get(i));
            listVOs.add(data);
        }
        FavoritsSpinnerAdapter myAdapter = new FavoritsSpinnerAdapter(this, 0, listVOs);
        spinner.setAdapter(myAdapter);
    }

    private ArrayList<ArrayList<Integer>> getAllFavorites() {
        ArrayList<ArrayList<Integer>> favorites = new ArrayList<ArrayList<Integer>>();
        favorites.add(null);
        favorites.add(new ArrayList<Integer>());
        favorites.get(1).add(new Integer(201));
        favorites.get(1).add(new Integer(202));
        favorites.get(1).add(new Integer(203));
        favorites.add(new ArrayList<Integer>());
        favorites.get(2).add(new Integer(204));
        favorites.get(2).add(new Integer(205));
        favorites.get(2).add(new Integer(206));
        favorites.get(2).add(new Integer(207));
        favorites.add(new ArrayList<Integer>());
        favorites.get(3).add(new Integer(208));
        favorites.get(3).add(new Integer(209));
        return favorites;
    }

    public void addFavorite(View v) {
        FavoritsData newFavorit;
        ArrayList<Integer> busses = new ArrayList<Integer>();

        Spinner busLineSpinner = findViewById(R.id.BusLineSpinner);
        BusLineSpinnerAdapter busLineAdapter = (BusLineSpinnerAdapter)busLineSpinner.getAdapter();
        ArrayList<BusLineSpinnerData> busList = busLineAdapter.getListState();

        for(int i = 0; i < busList.size(); i++){
            if(busList.get(i).isSelected()){
                busses.add(busList.get(i).getNumber());
            }
        }
        if(busses.size() > 1){
            newFavorit = new FavoritsData(busses);
            Spinner favoritSpinner = findViewById(R.id.FavoritsSpinner);
            FavoritsSpinnerAdapter myAdapter = (FavoritsSpinnerAdapter)favoritSpinner.getAdapter();
            myAdapter.getListState().add(newFavorit);
        }

    }

}
