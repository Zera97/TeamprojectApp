package com.android.zera.teamproject_app;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class MainActivity extends AppCompatActivity {

    private MapView map = null;
    private IMapController mapController = null;
    private MyLocationNewOverlay mLocationOverlay = null;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        boolean b = haveNetworkConnection();
        //System.out.println(b ? "ja" : "nein");

        if(true){
            new MiddleWareConnector(this).execute();
        }
        else {
            String ip = "";
            try {
                Log.e("Asynctasc", "IP erzeugen");
                InetAddress inet = Inet4Address.getByName("www.eos-noctis.de");
                ip = inet.getHostAddress();
            } catch (Exception e) {
                Log.e(this.getClass().toString(), "Fehler beim Erzeugen der IP-Adresse.");
            }
            int port = 31896;

            Socket socket = null;
            PrintWriter out = null;
            BufferedReader in = null;
            String response = "";
            try {
                Log.e("Asynctasc", "Socket erzeugen");
                socket = new Socket(ip, port);
            } catch (UnknownHostException e) {
                System.out.println("Unknown host: " + ip + " " + port);
                //return "No Socket";
            } catch (IOException e) {
                System.out.println("No I/O");
                //return "No Socket";
            }
            try {
                Log.e("Asynctasc", "outStream erzeugen");
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (UnknownHostException e) {
                System.out.println("Unknown host: " + ip + " " + port);
                //return "Fehler";
            } catch (IOException e) {
                System.out.println("No I/O");
                //return "Fehler";
            }
            try {
                Log.e("Asynctasc", "inStream erzeugen");
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (UnknownHostException e) {
                System.out.println("Unknown host: " + ip + " " + port);
                //return "Fehler";
            } catch (IOException e) {
                System.out.println("No I/O");
                //return "Fehler";
            }

            try {
                Log.e("Asynctasc", "Hallo3");
                out.write("Hallo");
                out.close();
                Log.e("Asynctasc", "Hallo4");
                if (in.readLine() == null) {
                    System.out.println("Server: " + "NULL");
                }
                Log.e("Asynctasc", "Hallo5");
            } catch (Exception e) {
                Log.e("AsyncTask", "Fehler beim Senden/Empfangen");
            }

            try {
                in.close();
                socket.close();
            } catch (Exception e) {
                Log.e("AsyncTask", "Fehler beim Schließen der Streams/des Sockets.");
            }
            Log.e("Asynctasc", "Hallo6");
            System.out.println(response);
            //return response;
        }
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
