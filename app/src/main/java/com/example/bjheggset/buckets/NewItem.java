package com.example.bjheggset.buckets;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewItem extends AppCompatActivity {

    Context $me = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
    }

    public void SubmitItem(View view) {
        EditText txtItem = (EditText) findViewById(R.id.txtItem);
        String itemValue = txtItem.getText().toString();

        BackgroundWorker backgroundWorker = new BackgroundWorker($me);
        backgroundWorker.execute("newItem", itemValue);

    }


}
