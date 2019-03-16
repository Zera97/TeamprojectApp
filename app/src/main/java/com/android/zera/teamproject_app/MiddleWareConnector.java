package com.android.zera.teamproject_app;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MiddleWareConnector extends AsyncTask<String, Integer, String> {

    //Art der Parameter,Progress,Rückgabe

    private WeakReference<Activity> parentActivity;

    public MiddleWareConnector(Activity activity) {
        parentActivity = new WeakReference<Activity>(activity);
    }

    @Override
    protected String doInBackground(String... strings) {
        //String ip = "192.168.0.12"; //eos-noctis.de
        String ip = "";
        try{
            InetAddress inet = Inet4Address.getByName("www.eos-noctis.de");
            ip = inet.getHostAddress();
        } catch (Exception e) {
            Log.e(this.getClass().toString(),"Fehler beim Erzeugen der IP-Adresse.");
        }
        int port = 31896;

        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        String response = "";
        try {
            Log.e("Asynctasc","Hallo1");
            socket = new Socket(ip, port);
            out = new PrintWriter(socket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Log.e("Asynctasc","Hallo2");
        } catch(UnknownHostException e) {
            System.out.println("Unknown host: " + ip + " " + port);
        } catch(IOException e) {
            System.out.println("No I/O");
        }

        try {
            Log.e("Asynctasc","Hallo3");
            out.write("Hallo");
            out.close();
            Log.e("Asynctasc","Hallo4");
            if(in.readLine() == null){
                System.out.println("Server: " + "NULL");
            }
            Log.e("Asynctasc","Hallo5");
        } catch (Exception e) {
            Log.e("AsyncTask", "Fehler beim Senden/Empfangen");
        }

        try {
            in.close();
            socket.close();
        } catch (Exception e) {
            Log.e("AsyncTask", "Fehler beim Schließen der Streams/des Sockets.");
        }
        Log.e("Asynctasc","Hallo6");
        System.out.println(response);
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        Activity activity = parentActivity.get();
        Button txt = (Button) activity.findViewById(R.id.btn_verbindung);
        txt.setText(result);
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
    }

}
