package com.android.zera.teamproject_app;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
                    .findViewById(R.id.edittext);
            holder.mLoad = convertView
                    .findViewById(R.id.load);
            holder.mPopup = convertView
                    .findViewById(R.id.popup);
            convertView.setTag(holder);
        } else {
            holder = (FavoritsSpinnerAdapter.ViewHolder) convertView.getTag();
        }

        //holder.mTextView.setText(listState.get(position).getTitle());
        holder.mLoad.setText(listState.get(position).getTitle());


        //region set MCheckBox
        // To check weather checked event fire from getview() or user input
        /*
        isFromView = true;
        holder.mCheckBox.setChecked(false);
        isFromView = false;
        if ((position == 0)) {
            holder.mCheckBox.setVisibility(View.GONE);
        } else {
            holder.mCheckBox.setVisibility(View.INVISIBLE);
        }
        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();
                //this.updateSelection();
                if (!isFromView) {
                    listState.get(position).setSelected(isChecked);
                    Log.e("TEST","Hallo" + getPosition + " " + listState.get(getPosition).getTitle());
                }
            }
        });
        */
        //endregion


        if ((position == 0)) {
            holder.mTextView.setText("Meine Favoriten");
            holder.mTextView.setVisibility(View.VISIBLE);
            holder.mLoad.setVisibility(View.GONE);
            holder.mPopup.setVisibility(View.GONE);
        } else {
            holder.mTextView.setText(listState.get(position).getTitle());
            holder.mTextView.setVisibility(View.VISIBLE);
            holder.mLoad.setText("X");
            holder.mLoad.setVisibility(View.VISIBLE);
            holder.mPopup.setText("o");
            holder.mPopup.setVisibility(View.VISIBLE);
        }

        //holder.mTextView.setEnabled(false);
        holder.mTextView.setFocusableInTouchMode(false);
        //holder.mTextView.setInputType(InputType.TYPE_NULL);
        holder.mTextView.setTag(position);
        holder.mLoad.setTag(position);
        holder.mPopup.setTag(position);

        holder.mLoad.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                if(position == 0)return;
                Log.e("TEST","Hallo " + position + " " + listState.get(position).getTitle());
                doClick(position);
            }
        });

        holder.mPopup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                showMenu(view);
            }
        });
        return convertView;
    }

    public void doClick(int position){
        Spinner busSpinner = ((MainActivity)this.mContext).findViewById(R.id.BusLineSpinner);
        BusLineSpinnerAdapter busAdapter = (BusLineSpinnerAdapter)busSpinner.getAdapter();
        ArrayList<BusLineSpinnerData> busLines = busAdapter.getListState();
        ArrayList<Integer> busses = this.listState.get(position).getNumbers();
        for (int i = 0; i < busLines.size();i++){
            busLines.get(i).setSelected(busses.contains(busLines.get(i).getNumber()));
        }
    }

/*
    private void updateSelection() {
        Log.e("FavoritsSpinnerAdapter", "Geschafft");
        for(FavoritsData f : this.listState){
            if(f.isSelected()){
                System.out.print(f.getTitle());
            }
        }
    }
    */
    private View lastSender = null;

    public void showMenu(View v)
    {
        View sender = getCustomView((int)v.getTag(),null,null);
        this.lastSender = sender;
        //EditText e = sender.findViewById(R.id.edittext);
        //System.out.print("HALLO I BIMS: " + e.getText());

        final int position = (int)v.getTag();

        PopupMenu popup = new PopupMenu(this.mContext,v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.option_1:
                        rename(lastSender);
                        break;
                    case R.id.option_2:
                        delete(position);
                        break;
                    default:
                        return false;
                }
                System.out.println("Hat geklappt");
                return true;
            }
        });// to implement on click event on items of menu
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.favorit_longpress_menu, popup.getMenu());
        popup.show();
    }

    private void rename(View sender){
        TextView e = sender.findViewById(R.id.edittext);
        System.out.print("HALLO I BIMS: " + e.getText());

        e.setEnabled(true);
        myAdapter.notifyDataSetChanged();
        e.setFocusable(true);
        myAdapter.notifyDataSetChanged();
        e.setFocusableInTouchMode(true);
        myAdapter.notifyDataSetChanged();
        e.setClickable(true);
        myAdapter.notifyDataSetChanged();
        e.setCursorVisible(true);
        myAdapter.notifyDataSetChanged();
        e.requestFocus();
        myAdapter.notifyDataSetChanged();
        //e.setInputType(InputType.TYPE_CLASS_TEXT);

        //e.setText(e.getText() + "+");


        int position = (int)e.getTag();//sender.findViewById(R.id.popup).getTag();
        FavoritsData f = this.listState.get(position);
        f.setTitle(e.getText().toString());
        myAdapter.notifyDataSetChanged();
    }
    private void delete(int position){
        this.listState.remove(position);
        ((MainActivity)this.mContext).saveFavoritsToFile();
        myAdapter.notifyDataSetChanged();
    }

    private class ViewHolder {
        private TextView mTextView;
        private Button mLoad;
        private Button mPopup;
    }
}