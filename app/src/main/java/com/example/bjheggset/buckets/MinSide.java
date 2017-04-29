package com.example.bjheggset.buckets;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
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
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import android.content.pm.PackageManager;

import org.json.JSONException;
import org.json.JSONObject;

public class MinSide extends AppCompatActivity {
    String antBuckets;
    String antItems;
    String antAccomplished;
    TextView txtStats;
    private ShareButton shareButton;
    private ShareDialog shareDialog;

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
        prepShare();
    }



    Context $me = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_min_side);
        System.out.println(antBuckets);

        shareDialog = new ShareDialog(this);
        prepShare();

        LocalBroadcastManager.getInstance(this).registerReceiver(bucketmotakker, new IntentFilter("antbuckets"));
        LocalBroadcastManager.getInstance(this).registerReceiver(itemsmotakker, new IntentFilter("antitems"));
        LocalBroadcastManager.getInstance(this).registerReceiver(accomplishedmottaker, new IntentFilter("antaccomplished"));



//Spør etter tilganger som kamera/kontakter osv der det kreves




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
            txtStatsProgress.setText("You have completed " + progress + "% of your goals!" );

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progbarStats);
            progressBar.setProgress((int) progress);
        }
        txtStatsAccomplished.setText("You have accomplished " + antAccomplished + " / " + antItems + " goals.");

    }


    protected void prepShare() {
        double progress = 0.00;
        if(!TextUtils.isEmpty(antAccomplished) && !TextUtils.isEmpty(antItems)) {
            double progAccomplished = Double.parseDouble(antAccomplished);
            double progItems = Double.parseDouble(antItems);
            progress = Math.round((progAccomplished / progItems) * 100);
        }

        ShareLinkContent content = new ShareLinkContent.Builder().setContentTitle("Buckets")
                .setContentDescription("Hello my friends, I have just achieved " + antAccomplished + "/" + antItems + " goals on my bucketlist \n" +
                        "Adding up to " + progress + "%")
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .build();
        shareButton = (ShareButton) findViewById(R.id.fb_share_button);
        shareButton.setShareContent(content);
    }

}
