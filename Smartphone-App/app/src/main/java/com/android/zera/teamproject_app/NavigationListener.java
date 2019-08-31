package com.android.zera.teamproject_app;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

/**
 * Listener zur Steuerung der Navigationsbar.
 * @author Fabian Theuerkauf
 * @version 1.0
 */

public class NavigationListener
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context context;

    public NavigationListener(Context mContext){
        this.context = mContext;
    }

    /**
     * Öffnet, in Abhängigkeit des ausgewählten Menu-Items, die jeweilige neue Activity.
     * @param item Das ausgewählte Menu-Item
     * @return true
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.map) {
            Intent activity_main = new Intent(context, MainActivity.class);
            context.startActivity(activity_main);

        } else if (id == R.id.fahrplan) {
            Intent activity_fahrpläne = new Intent(context, FahrplanActivity.class);
            context.startActivity(activity_fahrpläne);

        }  else if (id == R.id.nav_contact) {
            Intent contacts = new Intent(context, WebViewActivity.class);
            contacts.putExtra("url","https://hvb-harz.de/kontakt/");
            context.startActivity(contacts);

        } else if (id == R.id.nav_impressum) {
            Intent impressum = new Intent(context, WebViewActivity.class);
            impressum.putExtra("url","https://hvb-harz.de/impressum/");
            context.startActivity(impressum);
        }

        return true;
    }
}
