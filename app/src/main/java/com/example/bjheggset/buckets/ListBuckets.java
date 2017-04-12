package com.example.bjheggset.buckets;

import android.app.usage.NetworkStats;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListBuckets extends AppCompatActivity {
    List<Bucketlist> liste = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_buckets);

        String data = getIntent().getExtras().getString("data");
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String bucketName = jsonObject.getString("bucketName");
                long fID = jsonObject.getLong("FK_fID");
                Bucketlist bucketlist = new Bucketlist(id, bucketName, fID);
                liste.add(bucketlist);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter adapter = new ArrayAdapter<Bucketlist>(this, android.R.layout.simple_list_item_1, liste);
        final ListView listView = (ListView) findViewById(R.id.lstBuckets);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Bucketlist o = (Bucketlist) listView.getItemAtPosition(position);
                Intent i = new Intent(arg1.getContext(), DetailsBucket.class);
                i.putExtra("selected", o);
                startActivity(i);
            }

        });
    }
    protected void editBucket(View view){
        Intent i = new Intent(this, EditBucket.class);
        i.putExtra("bucketID", -1);
        startActivity(i);
    }

    protected void editBucket(View view, String bucketID) {
        Intent i = new Intent(this, EditBucket.class);
        //TODO: intent.extra = valgt bucketlist sin id
        startActivity(i);
    }
}
