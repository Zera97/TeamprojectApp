package com.android.zera.teamproject_app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements MapEventsReceiver {

    private MapView map = null;
    private IMapController mapController = null;
    private MyLocationNewOverlay mLocationOverlay = null;
    private Context context;
    private Resources res;
    private ArrayList<MyBusstopMarker> myBusstopMarkers;
    private Handler busHandler;
    private Runnable mHandlerTask;
    private final int INTERVAL = 10000 ;
    private ArrayList<MyBusMarker> busMarkers;
    private ArrayList<BusStopData> arrayOfBusstopDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        busMarkers = new ArrayList<>();
        myBusstopMarkers = new ArrayList<>();

        this.checkPermissions();
        this.initJSONParser();
        this.initSidebar();
        this.initBusLineSlider();
        this.initFavoritsSlider();
        this.initMap();
    }


    private void initMap(){
        map = (MapView) findViewById(R.id.map);
        map.setMultiTouchControls(true);
        map.setTileSource(TileSourceFactory.MAPNIK);


        mapController = map.getController();
        mapController.setZoom(18.0);
        GeoPoint startPoint = new GeoPoint(51.826918, 10.760942);
        mapController.setCenter(startPoint);

        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context), map);
        //this.mLocationOverlay.enableMyLocation();
        //this.mLocationOverlay.enableFollowLocation();
        map.getOverlays().add(this.mLocationOverlay);

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this);
        map.getOverlays().add(0, mapEventsOverlay);

        this.placeAllBusStops();

        busHandler = new Handler();
        mHandlerTask = new Runnable()
        {
            @Override
            public void run() {
                placeAllBuses();
                busHandler.postDelayed(mHandlerTask, INTERVAL);
            }
        };
        this.startRepeatingTask();

    }


    private void checkPermissions(){
        //TO DO:
        //https://inthecheesefactory.com/blog/things-you-need-to-know-about-android-m-permission-developer-edition/en
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
    }

    //region Network-Stuff

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void placeAllBusStops() {
        boolean b = isNetworkAvailable();
        String testJSON = createJSON(new MessageData("APP", 0, "1"));
        if (b) {
            MiddleWareConnector task = new MiddleWareConnector(this,new MiddleWareConnector.TaskListener() {
                @Override
                public void onFinished(String result) {

                    System.out.println("Result: " + result);

                    ReadContext ctx = JsonPath.parse(result);

                    JsonArray busStopData = ctx.read("$.busstops[*].name");

                    BusStopData dummy;
                    arrayOfBusstopDatas = new ArrayList<>();

                    int size = busStopData.size();
                    //System.out.println("Size: " + size);

                    for(int i = 0;i <size;i++){
                        String read = "$.busstops[" + i + "]";
                        dummy = ctx.read(read, BusStopData.class);
                        arrayOfBusstopDatas.add(dummy);
                    }

                    for(BusStopData data : arrayOfBusstopDatas){
                        System.out.println(data);
                    }

                    for(BusStopData bSD : arrayOfBusstopDatas){
                        String[] values = {bSD.name,bSD.latitude,bSD.longitude,bSD.id + ""};
                        MyBusstopMarker stop = new MyBusstopMarker(map,arrayOfBusstopDatas,values,context, res);
                        map.getOverlayManager().add(stop);
                        myBusstopMarkers.add(stop);
                        map.invalidate();
                    }
                }
            });

            task.execute(testJSON);

        } else {
            Toast.makeText(this, "Bitte für eine Internetverbindung sorgen.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    //endregion

    //region JSON

    private String createJSON(Object dataObj) {
        Gson gson = new Gson();
        String data = gson.toJson(dataObj);
        return data;
    }


    private void initJSONParser(){
        com.jayway.jsonpath.Configuration.setDefaults(new com.jayway.jsonpath.Configuration.Defaults() {

            private final JsonProvider jsonProvider = new GsonJsonProvider();
            private final MappingProvider mappingProvider = new GsonMappingProvider();

            @Override
            public JsonProvider jsonProvider() {
                return jsonProvider;
            }

            @Override
            public MappingProvider mappingProvider() {
                return mappingProvider;
            }

            @Override
            public Set<Option> options() {
                return EnumSet.noneOf(Option.class);
            }
        });
    }

    //endregion

    //region Sidebar

    private void initSidebar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationListener(this));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent mainact = new Intent(this, MainActivity.class);
            startActivity(mainact);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.impressum, menu);
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
    //endregion

    //region Sliders
    private void initBusLineSlider() {

    /*
        Spinner buslineSpinner = findViewById(R.id.BusLineSpinner);
        BusLineSpinnerAdapter myAdapter = (BusLineSpinnerAdapter)favoritSpinner.getAdapter();
        ArrayList<BusLineSpinnerData> selection = myAdapter.getListState();
        ArrayList<Integer> final_selection = new ArrayList<Integer>();
        for (int i = 0; i < selection.length; i++) {
            BusLineSpinnerData busLineDate = selection.get(i);
            if(busLineDate.isSelected()){
                final_selection.Add(new Integer(busLineDate.getNumber()));
            }
        }
     */

        final int[] select_qualification = this.getAllBusLines();
        Spinner spinner = findViewById(R.id.BusLineSpinner);
        ArrayList<BusLineSpinnerData> listVOs = new ArrayList<>();
        for (int i = 0; i < select_qualification.length; i++) {
            BusLineSpinnerData data = new BusLineSpinnerData(select_qualification[i], false);
            listVOs.add(data);
        }
        BusLineSpinnerAdapter myAdapter = new BusLineSpinnerAdapter(this, 0, listVOs);
        spinner.setAdapter(myAdapter);

        Spinner spinner2 = (Spinner)findViewById(R.id.BusLineSpinner);
        try{
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow lol = (android.widget.ListPopupWindow)popup.get(spinner2);
            lol.setHeight(600);
        }
        catch (Exception e){}
    }

    private int[] getAllBusLines() {
        int[] lines = new int[100];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = i + 200;
        }
        return lines;
    }

    private void initFavoritsSlider() {
        final ArrayList<ArrayList<Integer>> favorites = this.getAllFavoritesFromFile();
        Spinner spinner = findViewById(R.id.FavoritsSpinner);
        ArrayList<FavoritsData> listVOs = new ArrayList<>();
        for (int i = 0; i < favorites.size(); i++) {
            FavoritsData data = new FavoritsData(favorites.get(i));
            listVOs.add(data);
        }
        FavoritsSpinnerAdapter myAdapter = new FavoritsSpinnerAdapter(this, 0, listVOs);
        spinner.setAdapter(myAdapter);


        Spinner spinner2 = (Spinner)findViewById(R.id.FavoritsSpinner);
        try{
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow lol = (android.widget.ListPopupWindow)popup.get(spinner2);
            lol.setHeight(600);
        }
        catch (Exception e){}
    }




    protected void saveFavoritsToFile(){
        Spinner favoritSpinner = findViewById(R.id.FavoritsSpinner);
        FavoritsSpinnerAdapter myAdapter = (FavoritsSpinnerAdapter)favoritSpinner.getAdapter();
        ArrayList<FavoritsData> favorites = myAdapter.getListState();
        StringBuilder allFavorits = new StringBuilder();
        ArrayList<Integer> fav = null;
        for(int i = 1; i < favorites.size(); i++){
            fav = favorites.get(i).getNumbers();
            StringBuilder singleFavorit = new StringBuilder();
            System.out.print("FAV SIZE: " + fav.size());
            for (int j = 0; j < fav.size(); j++){
                singleFavorit.append(fav.get(j).intValue());
                if(j < fav.size()-1){
                    singleFavorit.append(";");
                }
            }
            System.out.print("SINGLE FAVORIT: " + singleFavorit.toString() + " ");
            allFavorits.append(singleFavorit.toString());
            if(i < favorites.size()-1){
                allFavorits.append("\n");
            }
        }
        String content = allFavorits.toString();
        System.out.println("CONTENT ON DESRTOY: \n\n" + content);
        try {
            FileOutputStream outStream = getApplicationContext().openFileOutput("wimb_favorits", this.MODE_PRIVATE);
            outStream.write(content.getBytes());
            outStream.close();
        }
        catch(Exception e){}
    }

    private ArrayList<ArrayList<Integer>> getAllFavoritesFromFile() {
        ArrayList<ArrayList<Integer>> favorites = new ArrayList<ArrayList<Integer>>();
        favorites.add(null);
        //if(true) return favorites;
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
        try {
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
            return favorites;
        }
        catch (Exception e){
            Log.e("Fehler bei Favoriten", e.getMessage());
            Toast.makeText(this, "Fehler beim Laden der Favoriten", Toast.LENGTH_SHORT).show();
            ArrayList<ArrayList<Integer>> favorites2 = new ArrayList<ArrayList<Integer>>();
            favorites2.add(null);
            return favorites2;
        }
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
            FavoritsSpinnerAdapter myAdapter = (FavoritsSpinnerAdapter)favoritSpinner.getAdapter();
            ArrayList<FavoritsData> favorites = myAdapter.getListState();
            favorites.add(newFavorit);
            this.saveFavoritsToFile();
        }

    }
    //endregion

    //region Android Status (überschriebene On-Methoden)
    @Override
    public void onResume() {
        System.out.println("HALLO HIER IN ON RESUME");
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        System.out.println("HALLO HIER IN ON PAUSE");
        super.onPause();
        map.onPause();
    }

    @Override
    public void onDestroy() {
        System.out.print("HALLO HIER ON DESTROY UND SO");
        this.saveFavoritsToFile();
        super.onDestroy();
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        for(MyBusstopMarker myMarker : myBusstopMarkers){
            if(myMarker.isInfoWindowShown() && p != myMarker.getPosition()){
                InfoWindow.closeAllInfoWindowsOn(map);
            }
        }

        for(MyBusMarker myMarker : busMarkers){
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
        System.out.println("Hallo hier in ON STOOOOOPPPPP");
        super.onStop();
    }
    //endregion

    private void startRepeatingTask()
    {
        mHandlerTask.run();
    }

    private void stopRepeatingTask()
    {
        busHandler.removeCallbacks(mHandlerTask);
    }

    private void placeAllBuses(){

        MessageData msgObj = new MessageData("APP", 0, "2");
        msgObj.setSelection(createSelection());
        String message = createJSON(msgObj);
        MiddleWareConnector task = new MiddleWareConnector(this,new MiddleWareConnector.TaskListener() {
            @Override
            public void onFinished(String result) {
                System.out.println(result);

                busMarkers = new ArrayList<>();

                Object document = com.jayway.jsonpath.Configuration.defaultConfiguration().jsonProvider().parse(result);

                JsonArray number = JsonPath.read(document, "$.bus[*].number");

                JsonArray lati = JsonPath.read(document, "$.bus[*].latitude");

                JsonArray longi = JsonPath.read(document, "$.bus[*].longitude");


                Gson converter = new Gson();

                Type type = new TypeToken<List<String>>() {}.getType();

                List<String> list_number = converter.fromJson(number, type);

                List<String> list_lati = converter.fromJson(lati, type);

                List<String> list_longi = converter.fromJson(longi, type);


                for(int i = 0;i<list_number.size();i++){
                    String[] values = {list_number.get(i),list_lati.get(i),list_longi.get(i)};
                    MyBusMarker bus = new MyBusMarker(map,values,context, res);
                    map.getOverlayManager().add(bus);
                    busMarkers.add(bus);
                    map.invalidate();
                }
            }
        });

        task.execute(message);

    }

    private ArrayList<Integer> createSelection(){
        ArrayList<Integer> selection = new ArrayList<>();

        Spinner favoritSpinner = findViewById(R.id.FavoritsSpinner);
        FavoritsSpinnerAdapter myAdapter = (FavoritsSpinnerAdapter)favoritSpinner.getAdapter();

        for(FavoritsData f : myAdapter.getListState()){
            if(!f.isSelected()){
                //System.out.println(f.getTitle());
                if(f.getTitle().contains(",")){
                    String[] temp = f.getTitle().split(",");
                    for(int i = 0;i<temp.length;i++){
                        selection.add(Integer.valueOf(temp[i].trim()));
                        //System.out.println(selection.get(i));
                    }
                }
            }
        }

        return selection;
    }
}
