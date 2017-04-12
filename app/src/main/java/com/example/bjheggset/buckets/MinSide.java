package com.example.bjheggset.buckets;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.common.util.ExceptionWithNoStacktrace;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.login.widget.ProfilePictureView;
import android.content.pm.PackageManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class MinSide extends AppCompatActivity {
    Intent i;
    Context $me = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_min_side);


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

                        //Henter facebookbilde via API med userID og streamer til fresco biblioteket
                        Profile profile = Profile.getCurrentProfile();
                        BackgroundWorker backgroundWorker = new BackgroundWorker($me);
                        TextView txtnavn = (TextView) findViewById(R.id.Navn);
                        Uri imageUri = Uri.parse("http://graph.facebook.com/" + profile.getId() + "/picture?type=large");
                        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.sdvImage);
                        draweeView.setImageURI(imageUri);


                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String id = Profile.getCurrentProfile().getId();


                            txtnavn.setText(first_name + " " + last_name);

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

    public void getBuckets(View view) {
        String userID = Profile.getCurrentProfile().getId();
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute("getLists", userID);
    }

    //Metode for å spørre om tilgang til kamera. Om kamera ikke finnes på enheten får bruker melding
    public void camera(View view) {

        PackageManager pm = this.getPackageManager();


        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivity(i);
        } else {
            Toast t = Toast.makeText(this, "No camera found on this device", Toast.LENGTH_SHORT);
            t.show();
        }
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

