package com.android.zera.teamproject_app;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.util.EnumSet;
import java.util.Set;


public class MyInfoWindow extends InfoWindow {

    private Context mainContext;
    private String[] params;
    private TextView txtSub;

    public MyInfoWindow(int layoutResId, MapView mapView, Context mainActivity, String[] stringArray) {
        super(layoutResId, mapView);
        this.params = stringArray;
        this.mainContext = mainActivity;
    }

    public void onClose() {
    }

    public void onOpen(final Object arg0) {
        TextView txtTitle = (TextView) mView.findViewById(R.id.bubble_title);
        txtTitle.setText(params[0]);

        txtSub = (TextView) mView.findViewById(R.id.bubble_subtext);
        setSubtext();
    }


    private void setSubtext() {
        MessageData msgObj = new MessageData("APP", 0, "3");
        int stopID = Integer.parseInt(params[3]);
        msgObj.setStopID(stopID);
        //msgObj.settime();
        String message = createJSON(msgObj);
        System.out.println(message);
        MiddleWareConnector task = new MiddleWareConnector(mainContext, new MiddleWareConnector.TaskListener() {
            @Override
            public void onFinished(String result) {

                System.out.println(result);

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

                txtSub.setText(result);
                }
            });

        task.execute(message);
    }

    private String createJSON(Object dataObj) {
        Gson gson = new Gson();
        String data = gson.toJson(dataObj);
        return data;
    }
}
