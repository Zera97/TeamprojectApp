package com.android.zera.teamproject_app;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
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

    public ArrayList<FavoritsData> getListState() {
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
            holder.mTextView = convertView.findViewById(R.id.edittext);
            holder.mLoad = convertView.findViewById(R.id.load);
            holder.mRename = convertView.findViewById(R.id.rename);
            holder.mDelete = convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        } else {
            holder = (FavoritsSpinnerAdapter.ViewHolder) convertView.getTag();
        }

        if (position == 0) {
            holder.mTextView.setText("Meine Favoriten");
            holder.mTextView.setVisibility(View.VISIBLE);
            holder.mLoad.setVisibility(View.GONE);
            holder.mRename.setVisibility(View.GONE);
            holder.mDelete.setVisibility(View.GONE);
        } else {
            holder.mTextView.setText(listState.get(position).getTitle());
            holder.mTextView.setVisibility(View.VISIBLE);
            holder.mLoad.setVisibility(View.VISIBLE);
            holder.mLoad.setText("L");
            holder.mRename.setVisibility(View.VISIBLE);
            holder.mRename.setText("R");
            holder.mDelete.setVisibility(View.VISIBLE);
            holder.mDelete.setText("D");
        }
        holder.setTag(position);
        this.getListState().get(position).viewHolder = holder;

        holder.mLoad.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                if (position != 0) {
                    load(position);
                }
            }
        });

        holder.mRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRenameDialog(view);
            }
        });

        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    showDeleteDialog(v);
        }
        });
        return convertView;
    }

    public void load(int position) {
        Spinner busSpinner = ((MainActivity) this.mContext).findViewById(R.id.BusLineSpinner);
        BusLineSpinnerAdapter busAdapter = (BusLineSpinnerAdapter) busSpinner.getAdapter();
        ArrayList<BusLineSpinnerData> busLines = busAdapter.getListState();
        ArrayList<Integer> busses = this.listState.get(position).getNumbers();
        for (int i = 0; i < busLines.size(); i++) {
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

    public void showRenameDialog(View v) {
        View sender = getCustomView((int) v.getTag(), null, null);
        this.lastSender = sender;
        //EditText e = sender.findViewById(R.id.edittext);
        //System.out.print("HALLO I BIMS: " + e.getText());

        final int position = (int) v.getTag();

        final EditText edit = new EditText(this.mContext);
        AlertDialog dialog = new AlertDialog.Builder(this.mContext).create();
        dialog.setTitle("Favorit umbenennen");
        dialog.setView(edit);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = edit.getText().toString();
                getListState().get(position).setTitle(s);
                myAdapter.notifyDataSetChanged();
            }
        });
        edit.setText(this.getListState().get(position).getTitle());
        dialog.show();
    }

    public void showDeleteDialog(View v) {
        View sender = getCustomView((int) v.getTag(), null, null);
        this.lastSender = sender;
        //EditText e = sender.findViewById(R.id.edittext);
        //System.out.print("HALLO I BIMS: " + e.getText());

        final int position = (int) v.getTag();

        final EditText edit = new EditText(this.mContext);
        AlertDialog dialog = new AlertDialog.Builder(this.mContext).create();
        dialog.setTitle("Wirklich löschen?");
        dialog.setView(edit);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ja löschen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete(position);
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Nein behalten", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {            }
        });
        edit.setText(this.getListState().get(position).getTitle());
        dialog.show();
    }

    private void delete(int position) {
        this.listState.remove(position);
        for(int i = 0; i < this.getListState().size(); i++){
            FavoritsSpinnerAdapter.ViewHolder vh = this.getListState().get(i).viewHolder;
            vh.setTag(i);
        }
        ((MainActivity) this.mContext).saveFavoritsToFile();
        myAdapter.notifyDataSetChanged();
    }

    public class ViewHolder {
        private TextView mTextView;
        private Button mLoad;
        private Button mRename;
        private Button mDelete;

        public void setTag(int tag){
            this.mTextView.setTag(tag);
            this.mLoad.setTag(tag);
            this.mRename.setTag(tag);
            this.mDelete.setTag(tag);
        }
    }
}