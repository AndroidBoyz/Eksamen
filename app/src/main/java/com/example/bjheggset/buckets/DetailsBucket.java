package com.example.bjheggset.buckets;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailsBucket extends AppCompatActivity implements AdapterView.OnItemClickListener{
    String userID;
    int editId;
    Bucketlist valgt;

    List<Items> listen = new ArrayList<>();
    List<Integer> accomplished = new ArrayList<>();

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("data");
            fillList(data);
        }
    };

    private BroadcastReceiver Receiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("data");
            fillAccomplished(data);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_bucket);

        Bucketlist bucketlist = (Bucketlist) getIntent().getExtras().getSerializable("selected");
        valgt = bucketlist;
        editId = bucketlist.getId();

        userID = Profile.getCurrentProfile().getId();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("Acquired"));
        LocalBroadcastManager.getInstance(this).registerReceiver(Receiver2, new IntentFilter("Accomplished"));
        new BackgroundWorker(this).execute("getAccomplished", userID);
        new BackgroundWorker(this).execute("getAcquired", userID, String.valueOf(editId));

        updateInfo();
    }

    public void updateInfo(){
        TextView txtAccomplished = (TextView) findViewById(R.id.txtAccomplished);
        TextView txtTotal = (TextView) findViewById(R.id.txtTotal);

        int numAccomplished=0;
        for(int i=0; i < listen.size(); i++){
            Items item = listen.get(i);
            if (accomplished.contains(item.getItemID())){
                numAccomplished++;
            }
        }

        txtAccomplished.setText(String.valueOf(numAccomplished));
        txtTotal.setText(String.valueOf(listen.size()));
    }

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

    protected void fillList(String data) {
        listen.clear();
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("itemID");
                String task = jsonObject.getString("items");
                Items item = new Items(id, task);
                listen.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ListView listview = (ListView)findViewById(R.id.lstItems);
        //final ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listen);
        final DetailsBucketCA adapter = new DetailsBucketCA(this, listen, accomplished);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(this);
        updateInfo();
    }

    public void onItemClick(AdapterView parent, View v, int position, long id) {
        DetailsBucketCA adapter = (DetailsBucketCA) parent.getAdapter();
        Items item = adapter.getItem(position);
        String userID = Profile.getCurrentProfile().getId();


        CheckBox checkBox = (CheckBox) v.findViewById(R.id.chkItems);
        checkBox.setChecked(true);
        accomplished.add(item.getItemID());

        new BackgroundWorker(this).execute("setAccomplished", userID, String.valueOf(item.getItemID()));

        updateInfo();
    }

    protected void gotoEdit (View view) {
        Intent i = new Intent(this, EditBucket.class);
        i.putExtra("selected", valgt);
        startActivity(i);
    }
}
