package com.example.bjheggset.buckets;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.text.DecimalFormat;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import android.content.pm.PackageManager;

import org.json.JSONException;
import org.json.JSONObject;

public class MinSide extends AppCompatActivity {
    String antBuckets;
    String antItems;
    String antAccomplished;
    TextView txtStats;

    public BroadcastReceiver bucketmotakker = new BroadcastReceiver() {


        @Override
        public void onReceive(Context context, Intent intent) {
            String antbuckets = intent.getStringExtra("antbuckets");
            antBuckets =antbuckets;
            updateStats();
        }
    };

    public BroadcastReceiver itemsmotakker = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String antitems = intent.getStringExtra("antitems");
            antItems =antitems;
            updateStats();
        }
    };

    public BroadcastReceiver accomplishedmottaker = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String antaccomplished = intent.getStringExtra("antAccomplished");
            antAccomplished = antaccomplished;
            updateStats();
        }
    };

    @Override
            public void onResume() {
        super.onResume();
        getStats();
    }



    Context $me = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_min_side);
        System.out.println(antBuckets);

        LocalBroadcastManager.getInstance(this).registerReceiver(bucketmotakker, new IntentFilter("antbuckets"));
        LocalBroadcastManager.getInstance(this).registerReceiver(itemsmotakker, new IntentFilter("antitems"));
        LocalBroadcastManager.getInstance(this).registerReceiver(accomplishedmottaker, new IntentFilter("antaccomplished"));



//Spør etter tilganger som kamera/kontakter osv der det kreves
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS, Manifest.permission.CAMERA};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }



        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {


                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {


                        getStats();


                        //Henter facebookbilde via API med userID og streamer til fresco biblioteket
                        Profile profile = Profile.getCurrentProfile();
                        BackgroundWorker backgroundWorker = new BackgroundWorker($me);
                        TextView txtnavn = (TextView) findViewById(R.id.txtNavn);


                        String profilbilde = "http://graph.facebook.com/" + profile.getId() + "/picture?type=large";

                        Uri imageUri = Uri.parse(profilbilde);
                        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.sdvImage);
                        draweeView.setImageURI(imageUri);


                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String id = Profile.getCurrentProfile().getId();

                            txtnavn.setText(first_name + " " + last_name);
                            updateStats();
                            backgroundWorker.execute("login", id, first_name, last_name);


                        } catch (JSONException e) {
                            e.getMessage();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,picture");
        request.setParameters(parameters);
        request.executeAsync();


    }

    public void ItemActivity(View view) {
        Intent i = new Intent(this, NewItem.class);
        startActivity(i);
    }

    public void getAntBuckets(){
        String userID = Profile.getCurrentProfile().getId();
        BackgroundWorker backgroundWorker = new BackgroundWorker($me);
        backgroundWorker.execute("antallbuckets", userID);

    }

    public void getAntItems() {
        String userID = Profile.getCurrentProfile().getId();
        BackgroundWorker backgroundWorker = new BackgroundWorker($me);
        backgroundWorker.execute("antallitems", userID);
    }

    public void getAntAccomplished() {
        String userID = Profile.getCurrentProfile().getId();
        BackgroundWorker backgroundWorker = new BackgroundWorker($me);
        backgroundWorker.execute("antallaccomplished", userID);
    }

    public void getBuckets(View view) {
        String userID = Profile.getCurrentProfile().getId();
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute("getLists", userID);
    }

    public void getStats(){
        getAntBuckets();
        getAntItems();
        getAntAccomplished();

        updateStats();
    }

    public void updateStats(){


        TextView txtStatsBuckets = (TextView) findViewById(R.id.txtStatsBuckets);
        TextView txtStatsItems = (TextView) findViewById(R.id.txtStatsItems);
        TextView txtStatsProgress = (TextView) findViewById(R.id.txtStatsProgress);
        TextView txtStatsAccomplished = (TextView) findViewById(R.id.txtStatsAccomplished);


        txtStatsBuckets.setText("You have  " + antBuckets + " bucketlists!");
        txtStatsItems.setText("These lists contain " + antItems + " unique items");

        if(!TextUtils.isEmpty(antAccomplished) && !TextUtils.isEmpty(antItems)) {
            double progAccomplished = Double.parseDouble(antAccomplished);
            double progItems = Double.parseDouble(antItems);
            double progress = Math.round((progAccomplished/progItems) * 100);
            txtStatsProgress.setText("Your progress: " + progress + "%");

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progbarStats);
            progressBar.setProgress((int) progress);
        }
        txtStatsAccomplished.setText("You have accomplished " + antAccomplished + " / " + antItems + " goals.");

    }



//sjekker hvilke tilganger som er gitt av bruker til app

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}
