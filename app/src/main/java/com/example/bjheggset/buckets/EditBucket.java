package com.example.bjheggset.buckets;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.support.v4.content.LocalBroadcastManager;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class EditBucket extends AppCompatActivity {
    BackgroundWorker backgroundWorker;
    public static List<Items> listeAlle = new ArrayList<>();
    public static List<Items> listeValgt = new ArrayList<>();
    String userID;
    int bucketID;
    AlertDialog alertDialog;

    private BroadcastReceiver acqReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("data");
            setAcquiredlist(data);
        }
    };

    private BroadcastReceiver unacqRecqiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("data");
            setUnacquiredlist(data);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bucket);

        Profile profile = Profile.getCurrentProfile();
        userID = profile.getId();
        backgroundWorker = new BackgroundWorker(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(acqReceiver, new IntentFilter("Acquired"));
        LocalBroadcastManager.getInstance(this).registerReceiver(unacqRecqiver, new IntentFilter("Unacquired"));

        Bucketlist bucketlist = (Bucketlist) getIntent().getExtras().getSerializable("selected");
        if (bucketlist != null) {
            prepEdit(bucketlist);
        } else {
            listeValgt.clear();
            getUnacquired(null);
        }

    }

    protected void fillList(List<Items> inFocus) {
        final ListView listview = (ListView)findViewById(R.id.lstItems);
        final ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, inFocus);
        listview.setAdapter(adapter);

        Button btnAcquired = (Button) findViewById(R.id.btnAcquired);
        Button btnUnacquired = (Button) findViewById(R.id.btnUnacquired);

        if (inFocus == listeAlle && listeAlle.size() > 0) { // Button 'unacquired' is selected
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    Items item = (Items) listview.getItemAtPosition(position);

                    listeValgt.add(item);
                    listeAlle.remove(item);

                    adapter.notifyDataSetChanged();
                }
            });
            btnUnacquired.setBackgroundColor(getResources().getColor(R.color.defaultSelected));
            btnAcquired.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));
        } else if (inFocus == listeValgt) { // Button 'already added' is selected
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                    Items item = (Items) listview.getItemAtPosition(position);
                    listeValgt.remove(item);

                    adapter.notifyDataSetChanged();
                }

            });
            btnAcquired.setBackgroundColor(getResources().getColor(R.color.defaultSelected));
            btnUnacquired.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));
        } else {
            fillList(inFocus); // Ingen lister har verdi fordi asynctask ikke er ferdig, kjør om igjen.
        }
    }

    public void setUnacquiredlist(String data) {
        listeAlle.clear();
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("itemID");
                String task = jsonObject.getString("items");
                Items item = new Items(id, task);
                if(!listeValgt.contains(item)) {
                    listeAlle.add(item);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fillList(listeAlle);
    }

    public void setAcquiredlist(String data) {
        listeValgt.clear();
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("itemID");
                String task = jsonObject.getString("items");
                Items item = new Items(id, task);
                listeValgt.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void Save(View view) {
        if(!checkRequirements()){
            alertDialog = new android.app.AlertDialog.Builder(this).create();
            alertDialog.setTitle("Buckets");
            alertDialog.setMessage("You have to give te list a name and add a few items");
            alertDialog.show();
            return;
        }
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
        AsyncTask bw = new BackgroundWorker(this).execute("getUnacquired", userID, String.valueOf(bucketID));
        try {
            bw.get(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    protected void getAcquired(View view){
        fillList(listeValgt);
    }

    protected void prepEdit(Bucketlist bl) {
        bucketID = bl.getId();
        new BackgroundWorker(this).execute("getAcquired", userID, String.valueOf(bucketID));

        TextView txtName = (TextView) findViewById(R.id.txtName);
        txtName.setText(bl.toString());

        getAcquired(null);

    }

    protected boolean checkRequirements(){
        TextView txtName = (TextView) findViewById(R.id.txtName);

        if (txtName.getText().toString().equals("")){ return false; }

        if (listeValgt.size() == 0) { return false; }

        return true;
    }


    // Gå tilbake til min side:
    protected void goHome(View view){
        Intent home = new Intent(this, MinSide.class);
        startActivity(home);
    }
}
