package com.android.zera.teamproject_app;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.Socket;

public class MiddleWareConnector extends AsyncTask<String, Integer, String> {

    //Art der Parameter,Progress,Rückgabe

    private WeakReference<Activity> parentActivity;

    public MiddleWareConnector(Activity activity) {
        parentActivity = new WeakReference<Activity>(activity);
    }


    @Override
    protected String doInBackground(String... strings) {
        String ip = "";
        int port = -1;
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        String response;
        try {


            socket = new Socket(ip, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            return e.getMessage();
        }

        try {
            out.print(new Integer(5).toString());
            response = in.readLine();
        } catch (Exception e) {
            return e.getMessage();
        }

        try {
            out.close();
            in.close();
            socket.close();
        } catch (Exception e) {
            Log.e("AsyncTask", "Fehler beim Schließen der Streams/des Sockets.");
        }


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
