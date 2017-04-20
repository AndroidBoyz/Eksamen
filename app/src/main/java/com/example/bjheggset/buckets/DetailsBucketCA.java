package com.example.bjheggset.buckets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.List;

public class DetailsBucketCA extends BaseAdapter {

    List<Items> items;
    List<Integer> accomplished;
    Context context;

    public DetailsBucketCA(){}

    public DetailsBucketCA(Context context, List<Items> items, List<Integer> accomplished) {
        this.context = context;
        this.items = items;
        this.accomplished = accomplished;


    }

    public Items getItem(int i) {
        return items.get(i);
    }

    public int getCount(){
        return items.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public boolean checkAccomplished(Items item) {
        int itemID = item.getItemID();
        if(accomplished.contains(itemID)){
            return true;
        } else {
            return false;
        }
    }

    public View getView(int position, View arg1, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.activity_details_bucket_ca, viewGroup, false);

        TextView txtItems = (TextView) row.findViewById(R.id.txtItem);
        CheckBox chkItems = (CheckBox) row.findViewById(R.id.chkItems);

        txtItems.setText(items.get(position).toString());
        chkItems.setChecked(checkAccomplished(items.get(position)));

        row.setClickable(checkAccomplished(items.get(position)));
        return row;
    }

}
