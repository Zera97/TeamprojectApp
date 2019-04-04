package com.android.zera.teamproject_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FavoritsSpinnerAdapter extends ArrayAdapter<FavoritsData> {
    private Context mContext;
    private ArrayList<FavoritsData> listState;
    private FavoritsSpinnerAdapter myAdapter;
    private boolean isFromView = false;

    public FavoritsSpinnerAdapter(Context context, int resource, List<FavoritsData> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = (ArrayList<FavoritsData>) objects;
        this.myAdapter = this;
    }

    public ArrayList<FavoritsData> getListState(){
        return this.listState;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {

        final FavoritsSpinnerAdapter.ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.fav_spinner, null);
            holder = new FavoritsSpinnerAdapter.ViewHolder();
            holder.mTextView = convertView
                    .findViewById(R.id.text);
            holder.mButton = convertView
                    .findViewById(R.id.button);
            convertView.setTag(holder);
        } else {
            holder = (FavoritsSpinnerAdapter.ViewHolder) convertView.getTag();
        }

        //holder.mTextView.setText(listState.get(position).getTitle());
        holder.mButton.setText(listState.get(position).getTitle());

        // To check weather checked event fire from getview() or user input
        //isFromView = true;
        //holder.mButton.setChecked(listState.get(position).isSelected());
        //isFromView = false;


        if ((position == 0)) {
            holder.mTextView.setText("Meine Favoriten");
            holder.mTextView.setVisibility(View.VISIBLE);
            holder.mButton.setVisibility(View.GONE);
        } else {
            holder.mTextView.setText("");
            holder.mTextView.setVisibility(View.GONE);
            holder.mButton.setVisibility(View.VISIBLE);
            holder.mButton.setText(listState.get(position).getTitle());
        }


        holder.mButton.setTag(position);

        /*holder.mButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                int getPosition = (Integer) buttonView.getTag();
                //this.updateSelection();
                if (!isFromView) {
                    //mContex
                    listState.get(position).setSelected(isChecked);
                    Log.e("TEST","Hallo" + getPosition + " " + listState.get(getPosition).getTitle());
                }
            }
        });*/
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                if(position == 0)return;
                Log.e("TEST","Hallo " + position + " " + listState.get(position).getTitle());
                doCoolStuff(position);

            }
        });
        return convertView;
    }

    public void doCoolStuff(int position){
        Spinner busSpinner = ((MainActivity)this.mContext).findViewById(R.id.BusLineSpinner);
        BusLineSpinnerAdapter busAdapter = (BusLineSpinnerAdapter)busSpinner.getAdapter();
        ArrayList<BusLineSpinnerData> busLines = busAdapter.getListState();
        ArrayList<Integer> busses = this.listState.get(position).getNumbers();
        for (int i = 0; i < busLines.size();i++){
            busLines.get(i).setSelected(busses.contains(busLines.get(i).getNumber()));
        }
    }

    private void updateSelection() {
        Log.e("FavoritsSpinnerAdapter", "Geschafft");
        for(FavoritsData f : this.listState){
            if(f.isSelected()){
                System.out.print(f.getTitle());
            }
        }
    }

    private class ViewHolder {
        private TextView mTextView;
        private Button mButton;
    }
}