package com.example.bjheggset.buckets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ListBuckets extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_buckets);

        String userID = getIntent().getExtras().getString("userID");
        getBucketLists(userID);
    }

    public void getBucketLists(String userID) {
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute("getLists", userID);
    }
}
