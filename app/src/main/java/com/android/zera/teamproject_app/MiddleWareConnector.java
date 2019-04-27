package com.android.zera.teamproject_app;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MiddleWareConnector extends AsyncTask<String, Integer, String> {

    public interface TaskListener {
        void onFinished(String result);
    }

    private TaskListener taskListener = null;
    private Context mContext = null;

    public MiddleWareConnector(Context activity, TaskListener listener) {
        this.mContext = activity;
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
        Socket socket = null;
        System.out.println("Socket erzeug122t.");
        try {
            socket = new Socket(ip,port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            socket.setSoTimeout(8000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        System.out.println("Socket erzeugt.");
        DataOutputStream outputStream;
        PrintWriter pr;
        try {
            outputStream = new DataOutputStream(socket.getOutputStream());
            pr = new PrintWriter(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        InputStream inputStream;
        InputStreamReader isr;
        BufferedReader br;
        try {
            inputStream = socket.getInputStream();
            isr = new InputStreamReader(inputStream);
            br = new BufferedReader(isr);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        System.out.println("Streams erzeugt.");
        try {
            pr.println(msg);
            pr.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        try {
            pr.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        System.out.println("Habe an Server gesendet: " + msg);
        String fromServer = "";
        try {
            fromServer = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        System.out.println("Habe vom Server gelesen: " + fromServer);
        try {
            br.close();
            isr.close();
            pr.close();
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
