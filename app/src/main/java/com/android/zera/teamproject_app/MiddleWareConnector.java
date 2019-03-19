package com.android.zera.teamproject_app;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Button;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MiddleWareConnector extends AsyncTask<String, Integer, String> {

    //Art der Parameter,Progress,Rückgabe

    private final String TAG = this.getClass().getSimpleName();

    private WeakReference<Activity> parentActivity;

    public MiddleWareConnector(Activity activity) {
        parentActivity = new WeakReference<Activity>(activity);
    }


    @Override
    protected String doInBackground(String... strings) {
        System.out.println(strings[0]);

        String response = sendMessage();
        return response;
    }

    private String sendMessage(){
        String ip = "192.168.0.12";
        String ip2 = "46.167.0.87";
        String ip3  ="";
        InetAddress inet = null;
        try {
            inet = Inet4Address.getByName("www.eos-noctis.de");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
        ip3 = inet.getHostAddress();
        System.out.println("ip3: " + ip);
        int port = 8080;
        int port2 = 31896;
        Socket s;
        try {
            s = new Socket(ip3,port2);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        System.out.println("Socket erzeugt.");
        DataOutputStream out;
        try {
            out = new DataOutputStream(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        //BufferedReader in;
        DataInputStream in;
        try {
            //in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            in = new DataInputStream(s.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        System.out.println("Streams erzeugt.");
        int input = 8;
        try {
            out.writeInt(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        System.out.println("Habe Input gesendet: " + input);
        int output;
        try {
            output = in.readInt();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        System.out.println("Habe Output gelesen: " + output);
        try {
            out.close();
            in.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        System.out.println("Alles geschlossen.");
        return output + "";
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
