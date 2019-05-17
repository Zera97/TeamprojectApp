package com.android.zera.teamproject_app;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;


public class MyInfoWindow extends InfoWindow {

    private Context mainContext;
    private String[] params;
    private TextView txtSub;
    private int layoutResId;
    private Activity activity;
    private boolean stop;

    public MyInfoWindow(int layoutResId, MapView mapView,Activity activity,Context mainActivity, String[] stringArray, boolean stop) {
        super(layoutResId, mapView);
        this.params = stringArray;
        this.layoutResId = layoutResId;
        this.mainContext = mainActivity;
        this.activity = activity;
        this.stop = stop;
    }

    public void onClose() {
    }

    public void onOpen(final Object arg0) {
        TextView txtTitle = (TextView) mView.findViewById(R.id.bubble_title);
        txtTitle.setText(params[0]);

        if(stop){
            setSubtext();
            //createLayout();
        }
    }
/*
    private void createLayout(){
        activity.setContentView(layoutResId);
        TableLayout layout = activity.findViewById(layoutResId);
        TableRow row = new TableRow(mainContext);
        TextView txtView = new TextView(mainContext);
        TableLayout.LayoutParams p = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        txtView.setLayoutParams(p);
        txtView.setText("HALLO");
        row.addView(txtView);
        layout.addView(row);
    }
*/

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
            /*
                Object document = Configuration.defaultConfiguration().jsonProvider().parse(result);

                JsonArray author0 = JsonPath.read(document, "$.stoptimes[*].*");

                Gson converter = new Gson();

                Type type = new TypeToken<List<String>>(){}.getType();
                List<String> list =  converter.fromJson(author0,type); */
                //txtSub.setText(result);
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
