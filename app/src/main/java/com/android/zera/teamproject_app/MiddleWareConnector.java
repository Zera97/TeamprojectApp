package com.android.zera.teamproject_app;

import android.app.Activity;
import android.os.AsyncTask;

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

    public interface TaskListener {
        void onFinished(String result);
    }

    private TaskListener taskListener = null;
    private WeakReference<Activity> parentActivity;

    public MiddleWareConnector(Activity activity, TaskListener listener) {
        parentActivity = new WeakReference<Activity>(activity);
        this.taskListener = listener;
    }


    @Override
    protected String doInBackground(String... strings) {

        String response = sendMessage(strings[0]);
        return response;
    }

    private String sendMessage(String msg){
        String ip  ="";
        InetAddress inet = null;
        try {
            inet = Inet4Address.getByName("www.eos-noctis.de");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
        ip = inet.getHostAddress();
        int port = 31896;
        Socket socket;
        try {
            socket = new Socket(ip,port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        System.out.println("Socket erzeugt.");
        DataOutputStream outputStream;
        try {
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        DataInputStream inputStream;
        try {
            inputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        System.out.println("Streams erzeugt.");
        try {
            outputStream.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        System.out.println("Habe an Server gesendet: " + msg);
        String fromServer = "";
        try {
            fromServer = inputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        System.out.println("Habe vom Server gelesen: " + fromServer);
        try {
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        System.out.println("Alles geschlossen.");

        return fromServer;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(this.taskListener != null) {
            this.taskListener.onFinished(result);
        }
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
    }

}
