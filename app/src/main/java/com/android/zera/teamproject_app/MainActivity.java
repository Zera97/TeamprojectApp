package com.android.zera.teamproject_app;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

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
        implements NavigationView.OnNavigationItemSelectedListener{


    private final String TAG = this.getClass().getSimpleName();
    private MapView map = null;
    private IMapController mapController = null;
    private MyLocationNewOverlay mLocationOverlay = null;
    private Context ctx = getApplicationContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //referenz zum einfügen der Bushaltestellen
        // https://github.com/osmdroid/osmdroid/wiki/Markers,-Lines-and-Polygons

        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

        //region Sidebar
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

        //endregion

        this.initBusLineSlider();
        this.initFavoritsSlider();

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

    //region Sidebar-Methoden
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.punkt1) {

        } else if (id == R.id.punkt2) {

        } else if (id == R.id.punkt3) {

        } else if(id == R.id.nav_contact){

        }else if(id == R.id.nav_impressum){

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //endregion

    private void initBusLineSlider(){
        final int[] select_qualification = this.getAllBusLines();
        Spinner spinner = findViewById(R.id.BusLineSpinner);
        ArrayList<BusLineSpinnerData> listVOs = new ArrayList<>();
        for (int i = 0; i < select_qualification.length; i++) {
            BusLineSpinnerData data = new BusLineSpinnerData(select_qualification[i],false);
            listVOs.add(data);
        }
        BusLineSpinnerAdapter myAdapter = new BusLineSpinnerAdapter(this, 0,listVOs);
        spinner.setAdapter(myAdapter);
    }
    private int[] getAllBusLines(){
        int[] lines = new int[100];
        for(int i = 0; i < lines.length; i++){
            lines[i] = i+200;
        }
        return lines;
    }
    private void initFavoritsSlider(){
        final int[][] favorites = this.getAllFavorites();
        Spinner spinner = findViewById(R.id.FavoritsSpinner);
        ArrayList<FavoritsData> listVOs = new ArrayList<>();
        for (int i = 0; i < favorites.length; i++) {
            FavoritsData data = new FavoritsData(favorites[i]);
            listVOs.add(data);
        }
        FavoritsSpinnerAdapter myAdapter = new FavoritsSpinnerAdapter(this, 0,listVOs);
        spinner.setAdapter(myAdapter);
    }
    private int[][] getAllFavorites(){

        int[][] favorites = new int[3][3];
        favorites[0] = new int[]{201,202,203};
        favorites[1] = new int[]{204,205,206};
        favorites[2] = new int[]{207,208,209};
        return favorites;
    }



}
