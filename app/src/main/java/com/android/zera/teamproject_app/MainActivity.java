package com.android.zera.teamproject_app;

import android.Manifest;
import android.content.Context;
import android.content.res.Resources;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MapEventsReceiver {


    private final String TAG = this.getClass().getSimpleName();
    private MapView map = null;
    private IMapController mapController = null;
    private MyLocationNewOverlay mLocationOverlay = null;
    private Context ctx;
    private Resources res;
    private ArrayList<Busstop> busstops;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //referenz zum einfügen der Bushaltestellen
        //https://github.com/osmdroid/osmdroid/wiki/Markers,-Lines-and-Polygons
        //https://github.com/json-path/JsonPath

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

        Log.e("Hallo", "Ich initialisiere gleich");
        this.initBusLineSlider();
        this.initFavoritsSlider();
        Log.e("Hallo", "Ich habe initialisiert");

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION}, 1);

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

        //this.setBusstops();

    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, fragment);
        fragmentTransaction.commit();

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
        String testJSON = createJSON(new TestData("app", 1, "test"));
        System.out.println(testJSON);
        if (b) {
            new MiddleWareConnector(this).execute(testJSON);
            //ReadContext ctx = JsonPath.parse(testJSON);
            //System.out.println(ctx.read("$.commandCode"));

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

    private String createJSON(Object dataObj) {
        Gson gson = new Gson();
        String data = gson.toJson(dataObj);

        return data;
    }

    @Override
    public void onResume() {
        System.out.print("HALLO HIER IN ON RESUME");
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        System.out.print("HALLO HIER IN ON PAUSE");
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

        } else if (id == R.id.nav_contact) {

        } else if (id == R.id.nav_impressum) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //endregion
    //region Sliders
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
        if (true) return favorites;
        String content = "";
        try {
            FileInputStream inStream = openFileInput("wimb_favorits");
            Scanner scanner = new Scanner(inStream);
            scanner.useDelimiter("\\z");
            content = scanner.next();
            scanner.close();
            Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
            Log.e("lol", content);
        } catch (Exception e) {
        }
        System.out.println("CONTENT: " + content);
        if (content.equals("")) {
            return favorites;
        }
        String[] favorits = content.split("\n");
        String favorit = "";
        for (int i = 0; i < favorits.length; i++) {
            favorit = favorits[i];
            System.out.println("TAG: " + favorit);
            String[] busses_str = favorit.split(";");
            ArrayList<Integer> busses = new ArrayList<Integer>();
            for (int j = 0; j < busses_str.length; j++) {
                Integer newBus = Integer.parseInt(busses_str[j]);
                if (newBus != null) {
                    busses.add(newBus);
                }
            }
            favorites.add(busses);
        }
        /*
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
        favorites.get(3).add(new Integer(209));*/
        return favorites;
    }

    public void addFavorite(View v) {
        FavoritsData newFavorit = null;
        ArrayList<Integer> busses = new ArrayList<Integer>();

        Spinner busLineSpinner = findViewById(R.id.BusLineSpinner);
        BusLineSpinnerAdapter busLineAdapter = (BusLineSpinnerAdapter) busLineSpinner.getAdapter();
        ArrayList<BusLineSpinnerData> busList = busLineAdapter.getListState();

        for (int i = 0; i < busList.size(); i++) {
            if (busList.get(i).isSelected()) {
                busses.add(busList.get(i).getNumber());
            }
        }
        if (busses.size() > 1) {
            newFavorit = new FavoritsData(busses);
            Spinner favoritSpinner = findViewById(R.id.FavoritsSpinner);
            FavoritsSpinnerAdapter myAdapter = (FavoritsSpinnerAdapter) favoritSpinner.getAdapter();
            myAdapter.getListState().add(newFavorit);
        }

    }

    //endregion
    @Override
    public void onDestroy() {
        System.out.print("HALLO HIER ON DESTROY UND SO");
        Spinner favoritSpinner = findViewById(R.id.FavoritsSpinner);
        FavoritsSpinnerAdapter myAdapter = (FavoritsSpinnerAdapter) favoritSpinner.getAdapter();
        ArrayList<FavoritsData> favorites = myAdapter.getListState();
        StringBuilder allFavorits = new StringBuilder();
        ArrayList<Integer> fav = null;
        for (int i = 1; i < favorites.size(); i++) {
            fav = favorites.get(i).getNumbers();
            StringBuilder singleFavorit = new StringBuilder();
            for (int j = 0; j < fav.size(); j++) {
                singleFavorit.append(fav.get(j).intValue());
                if (i < favorites.size() - 1) {
                    singleFavorit.append(";");
                }
            }
            System.out.print("SINGLE FAVORIT: " + singleFavorit.toString());
            allFavorits.append(singleFavorit.toString());
            if (i < favorites.size() - 1) {
                allFavorits.append("\n");
            }
        }
        String content = allFavorits.toString();
        System.out.println("CONTENT ON DESRTOY: " + content);
        try {
            FileOutputStream outStream = getApplicationContext().openFileOutput("wimb_favorits", this.MODE_PRIVATE);
            outStream.write(content.getBytes());
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void setBusstops() {
        String[] test = {"Hallo Dirk", "51.826918", "10.760942"};
        Busstop stop = new Busstop(map, test, this, res);
        map.getOverlayManager().add(stop);
        String[] test2 = {"Hallo Fabi", "51.926918", "10.960942"};
        Busstop stop2 = new Busstop(map, test2, this, res);
        map.getOverlayManager().add(stop2);
        busstops.add(stop);
        busstops.add(stop2);
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        for(Busstop myMarker : busstops){
            if(myMarker.isInfoWindowShown() && p != myMarker.getPosition()){
                InfoWindow.closeAllInfoWindowsOn(map);
            }
        }

        return true;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }

    @Override
    public void onStop() {
        System.out.print("Hallo hier in ON STOOOOOPPPPP");
        super.onStop();
    }

}
