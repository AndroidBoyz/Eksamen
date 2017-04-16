package com.example.bjheggset.buckets;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditBucket extends AppCompatActivity {
    BackgroundWorker backgroundWorker;
    public static List<Items> listeAlle = new ArrayList<>();
    public static List<Items> listeValgt = new ArrayList<>();
    String userID;
    int bucketID;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("data");
            setList(data);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bucket);

        Profile profile = Profile.getCurrentProfile();
        userID = profile.getId();
        backgroundWorker = new BackgroundWorker(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("Items"));



    }

    public void setList(String data) {
        listeAlle.clear();
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("itemID");
                String task = jsonObject.getString("items");
                Items item = new Items(id, task);
                listeAlle.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    protected void Save(View view) {
        EditText editText = (EditText) findViewById(R.id.txtName);
        String bucketname = editText.getText().toString();

        JSONArray jsonArray = new JSONArray();
        for(int i=0; i < listeValgt.size(); i++){
            jsonArray.put(listeValgt.get(i).getJSON());
        }
        new BackgroundWorker(this).execute("saveList",userID, String.valueOf(bucketID), jsonArray.toString(), bucketname);
        backgroundWorker.execute("getLists", userID);
    }

    protected void getUnacquired(View view){
        new BackgroundWorker(this).execute("getUnacquired", userID, String.valueOf(bucketID));
        final ListView listview = (ListView)findViewById(R.id.lstItems);
        final ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listeAlle);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Items item = (Items) listview.getItemAtPosition(position);

                listeValgt.add(item);
                listeAlle.remove(item);

                adapter.notifyDataSetChanged();
            }

        });
    }

    protected void getAcquired(View view){
        final ArrayAdapter adapter = new ArrayAdapter<Items>(this, android.R.layout.simple_list_item_1, listeValgt);
        final ListView listview = (ListView) findViewById(R.id.lstItems);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Items item = (Items) listview.getItemAtPosition(position);
                listeValgt.remove(item);
                adapter.notifyDataSetChanged();
            }

        });
    }
}
