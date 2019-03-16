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
import java.net.InetSocketAddress;
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
        InetAddress inet = null;
        InetAddress my = null;
        String ip = "";
        try{
            Log.e("Asynctasc", "IP erzeugen");
            inet = Inet4Address.getByName("www.eos-noctis.de");
            my = InetAddress.getLocalHost();
            ip = inet.getHostAddress();
            System.out.println("ip: " + ip);
        } catch (Exception e) {
            Log.e(this.getClass().toString(),"Fehler beim Erzeugen der IP-Adresse.");
        }
        int port = 8080;
        ip = "192.168.0.12"; // nur in Fabis WLAN
        ip = "46.167.0.87"; // von nirgends

        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        String response = "";
        try {
            Log.e("Asynctasc", "Socket erzeugen");
            //socket = new Socket(inet, port);//, my,2000);
            socket = new Socket();
            System.out.println("izz da");
            socket.connect(new InetSocketAddress(ip,port),20000);
            System.out.println("izz da 2");
        }catch(UnknownHostException e) {
            System.out.println("Unknown host: " + ip + " " + port);
            return "No Socket";
        } catch(IOException e) {
            e.printStackTrace();
            return "No Socket";
        }
        try {
            Log.e("Asynctasc", "outStream erzeugen");
            out = new PrintWriter(socket.getOutputStream(),true);
        }catch(UnknownHostException e) {
            System.out.println("Unknown host: " + ip + " " + port);
            return "Fehler";
        } catch(IOException e) {
            System.out.println("No I/O");
            return "Fehler";
        }
        try {
            Log.e("Asynctasc", "inStream erzeugen");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch(UnknownHostException e) {
            System.out.println("Unknown host: " + ip + " " + port);
            return "Fehler";
        } catch(IOException e) {
            System.out.println("No I/O");
            return "Fehler";
        }

        try {
            Log.e("Asynctasc","Hallo3");
            byte b = 25;
            out.write(b);
            out.flush();
            Log.e("Asynctasc","Hallo4");
            System.out.println("Erg: " + in.readLine());
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
