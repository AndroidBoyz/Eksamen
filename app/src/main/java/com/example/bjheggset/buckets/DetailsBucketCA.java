package com.example.bjheggset.buckets;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import android.support.v7.widget.CardView;

import com.facebook.Profile;
import com.facebook.common.time.SystemClock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailsBucketCA extends RecyclerView.Adapter<DetailsBucketCA.ViewHolder> {

    private List<Integer> accomplished = new ArrayList<>();
    private List<Items> mItems;
    String userID;
    Context ctx;
    Items item;
    DetailsBucketCA.ViewHolder mHolder;
    DetailsBucket mParent;



    public static class ViewHolder extends RecyclerView.ViewHolder {


        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;

        }

    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("data");
            //fillAccomplished(data);
        }
    };


    public DetailsBucketCA(List<Items> myItems) {
        mItems = myItems;
    }

    public long getItemId(int position) {
        return position;
    }
    @Override
    public DetailsBucketCA.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ctx = parent.getContext();
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent,false);
        userID = Profile.getCurrentProfile().getId();

        LocalBroadcastManager.getInstance(ctx).registerReceiver(mReceiver, new IntentFilter("Accomplished"));
        //new BackgroundWorker(ctx).execute("getAccomplished", userID);

        accomplished = DetailsBucket.getAccomplishedList();
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(DetailsBucketCA.ViewHolder holder, final int position) {
    holder.setIsRecyclable(false);



        mHolder = holder;

        userID = Profile.getCurrentProfile().getId();
        final CardView cardView = holder.cardView;
        TextView textView = (TextView)cardView.findViewById(R.id.goal_name);
        ImageView itemImg = (ImageView)cardView.findViewById(R.id.item_photo);

        mHolder.cardView.setTag(position);


        textView.setText(mItems.get(position).toString());



        cardView.setOnClickListener(new CardView.OnClickListener() {
            @Override public void onClick(View v) {
                item = mItems.get(position);
                accomplished.add(item.getItemID());
                cardView.setBackgroundColor(Color.BLUE);
                new BackgroundWorker(ctx).execute("setAccomplished", userID, String.valueOf(item.getItemID()));
                ((DetailsBucket) ctx).updateInfo();
            }
        });

        if (checkAccomplished(mItems.get(position))){
            CardView CV = (CardView) holder.cardView.findViewWithTag(position);
            CV.setBackgroundColor(Color.BLUE);
        }


        //setColors(cardView);
    }

    public int getItemCount() {
        return mItems.size();
    }

    public boolean checkAccomplished(Items item) {
        boolean result = false;
        int itemID = item.getItemID();
        for(int i =0; i < accomplished.size(); i++) {
            int same = accomplished.get(i);
            if (same == itemID){
                result = true;
            }
        }
        return result;
    }

/*
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
*/

    public void fillAccomplished(String data){
        accomplished.clear();
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int accomplishedId = jsonObject.getInt("itemID");
                accomplished.add(accomplishedId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void setColors(CardView CV){
        final CardView cardView = CV;

        for(int i=0; i < mItems.size(); i++) {
            Items item = mItems.get(i);
            if (checkAccomplished(item)) {

                CV.setBackgroundColor(Color.BLUE);

            }

        }
    }

}
