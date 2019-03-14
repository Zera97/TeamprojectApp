package com.android.zera.teamproject_app;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Button;
import java.lang.ref.WeakReference;

public class MiddleWareConnector extends AsyncTask<String,Integer,String> {

    //Art der Parameter,Progress,Rückgabe

    private WeakReference<Activity> parentActivity;

    public MiddleWareConnector(Activity activity) {
        parentActivity = new WeakReference<Activity>(activity);
    }


    @Override
    protected String doInBackground(String... strings) {

        //Hier passiert die Socket Magie.

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        Activity activity = parentActivity.get();
        Button txt = (Button) activity.findViewById(R.id.btn_verbindung);
        txt.setText(result);
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Integer... values) {}

}
