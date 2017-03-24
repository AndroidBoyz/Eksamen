package com.example.bjheggset.buckets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

public class MinSide extends AppCompatActivity {

    Intent i;


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
                        TextView txtnavn = (TextView)findViewById(R.id.Navn);
                     try {
                         String navn = object.getString("first_name");
                         txtnavn.setText(navn);
                     }
                     catch(JSONException e)
                     {
                         e.getMessage();
                     }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name");
        request.setParameters(parameters);
        request.executeAsync();




    }
}