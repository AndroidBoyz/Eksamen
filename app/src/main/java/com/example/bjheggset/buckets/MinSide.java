package com.example.bjheggset.buckets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MinSide extends AppCompatActivity {

    Intent i;
    Context $me = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_min_side);



        String token = getIntent().getExtras().getString("result");
        String userID = getIntent().getExtras().getString("userID");




        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {

                        Profile profile = Profile.getCurrentProfile();
                        BackgroundWorker backgroundWorker = new BackgroundWorker($me);
                        TextView txtnavn = (TextView)findViewById(R.id.Navn);
                        ((ProfilePictureView)findViewById(R.id.profilePicture)).setProfileId(
                                profile.getId());

                     try {
                         String first_name = object.getString("first_name");
                         String last_name = object.getString("last_name");
                         String id = Profile.getCurrentProfile().getId();


                         txtnavn.setText(first_name + " " +last_name);

                         backgroundWorker.execute("login", id,first_name,last_name);


                     }
                     catch(JSONException e)
                     {
                         e.getMessage();
                     }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,picture");
        request.setParameters(parameters);
        request.executeAsync();


    }

    public void ItemActivity(View view){
        Intent i = new Intent(this, NewItem.class);
        startActivity(i);
    }
}