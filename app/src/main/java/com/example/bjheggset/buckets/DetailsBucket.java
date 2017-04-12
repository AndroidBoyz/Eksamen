package com.example.bjheggset.buckets;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailsBucket extends AppCompatActivity {
AlertDialog alertDialog;
    int editId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_bucket);

        Bucketlist bucketlist = (Bucketlist) getIntent().getExtras().getSerializable("selected");
        editId = bucketlist.getId();

        //TODO: Bruk objektets ID til å finne tilhørende items
        int i = 0;

    }
}
