package com.android.zera.teamproject_app;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;


public class MyInfoWindow extends InfoWindow {

    private Context mainContext;
    private String[] params;
    private boolean stop;
    private ArrayList<BusStopData> myBusstopMarkers;;

    public MyInfoWindow(View view, MapView mapView, ArrayList<BusStopData> myBusstopMarkers,Context mainActivity, String[] stringArray, boolean stop) {
        super(view, mapView);
        this.params = stringArray;
        this.mainContext = mainActivity;
        this.stop = stop;
        this.myBusstopMarkers = myBusstopMarkers;
    }

    public void onClose() {
    }

    public void onOpen(final Object arg0) {

        TextView txtTitle = (TextView) mView.findViewWithTag("header");
        txtTitle.setText(params[0]);

        if (stop) {
            setSubtext();
        }
    }

    private void setSubtext() {
        MessageData msgObj = new MessageData("APP", 0, "3");
        int stopID = Integer.parseInt(params[3]);
        msgObj.setStopID(stopID);
        Date currentTime = Calendar.getInstance().getTime();
        msgObj.setTime((currentTime.getHours() + 2) * 60 + currentTime.getMinutes());
        msgObj.setLongitude(Double.parseDouble(params[1]));
        msgObj.setLatitude(Double.parseDouble(params[2]));
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

                Object document = Configuration.defaultConfiguration().jsonProvider().parse(result);

                JsonArray lines = JsonPath.read(document, "$.stoptimes[*].line");

                JsonArray stops = JsonPath.read(document, "$.stoptimes[*].nextstop");

                JsonArray times = JsonPath.read(document, "$.stoptimes[*].time");

                JsonArray direction = JsonPath.read(document, "$.stoptimes[*].direction");

                Gson converter = new Gson();

                Type type = new TypeToken<List<String>>() {
                }.getType();

                List<String> list_lines = converter.fromJson(lines, type);

                List<String> list_nextStop = converter.fromJson(stops, type);

                List<String> list_times = converter.fromJson(times, type);

                List<String> list_directions = converter.fromJson(direction, type);


                if (list_lines.isEmpty()) {
                    deletePlatzhalter(0);
                } else {
                    int anzahl = 0;
                    while (anzahl < list_lines.size()) {
                        TextView txtLine = (TextView) mView.findViewWithTag("linie" + anzahl);
                        TextView txtZeit = (TextView) mView.findViewWithTag("zeit" + anzahl);

                        String textLine = list_lines.get(anzahl) + " -> ";

                        for (BusStopData data: myBusstopMarkers) {
                            if(data.id == Integer.parseInt(list_nextStop.get(anzahl))){
                                textLine += data.name;
                            }
                        }

                        txtLine.setText(textLine);

                        int temp  = Integer.valueOf(list_times.get(anzahl));

                        String textTime = (temp / 60) + ":" + (temp%60);

                        txtZeit.setText(textTime);

                        anzahl++;
                    }
                    deletePlatzhalter(anzahl);
                }
            }
        });

        task.execute(message);
    }

    private void deletePlatzhalter(int untereGrenze){

        if(untereGrenze == 0){
            TextView txtLine = (TextView) mView.findViewWithTag("linie" + 0);
            TextView txtZeit = (TextView) mView.findViewWithTag("zeit" + 0);

            txtLine.setText("In nächster Zeit fährt von dieser Haltestelle kein Bus ab.");
            txtZeit.setText("");
            untereGrenze++;
        }

        for(int i = untereGrenze;i<5;i++){
            TextView txtLine = (TextView) mView.findViewWithTag("linie" + i);
            TextView txtZeit = (TextView) mView.findViewWithTag("zeit" + i);

            txtLine.setText("");
            txtZeit.setText("");
        }
    }

    private String createJSON(Object dataObj) {
        Gson gson = new Gson();
        String data = gson.toJson(dataObj);
        return data;
    }
}
