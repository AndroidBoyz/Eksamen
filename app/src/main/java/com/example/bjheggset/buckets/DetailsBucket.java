package com.example.bjheggset.buckets;

import android.content.BroadcastReceiver;
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
import android.widget.ListView;

import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailsBucket extends AppCompatActivity {
    String userID;
    int editId;
    Bucketlist valgt;

    List<Items> listen = new ArrayList<>();

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("data");
            fillList(data);
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
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("Items"));
        new BackgroundWorker(this).execute("getAcquired", userID, String.valueOf(editId));

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
        final ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listen);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Items item = (Items) listview.getItemAtPosition(position);

                listen.add(item);
                listen.remove(item);

                adapter.notifyDataSetChanged();
            }

        });
    }

    protected void gotoEdit (View view) {
        Intent i = new Intent(this, EditBucket.class);
        i.putExtra("selected", valgt);
        startActivity(i);
    }
}
