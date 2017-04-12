package com.example.bjheggset.buckets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

public class NewItem extends AppCompatActivity {
    private static final int CAMERA_ITEM = 1999;
    Context $me = this;

    MinSide m;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);


        ImageView itempicture = (ImageView)findViewById(R.id.imageView2);
        itempicture.setVisibility(View.GONE);


    }

    public void SubmitItem(View view) {
        EditText txtItem = (EditText) findViewById(R.id.txtItem);
        String itemValue = txtItem.getText().toString();

        BackgroundWorker backgroundWorker = new BackgroundWorker($me);
        backgroundWorker.execute("newItem", itemValue);

    }

    public void camera(View view) {
        PackageManager pm = this.getPackageManager();

        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(i, CAMERA_ITEM);
        } else {
            Toast t = Toast.makeText(this, "No camera found on this device", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        ImageView itempicture = (ImageView)findViewById(R.id.imageView2);
        itempicture.setVisibility(View.GONE);


        if (requestCode == CAMERA_ITEM && resultCode == Activity.RESULT_OK) {

            SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.sdvImage);

            ImageView btnCamera = (ImageView)findViewById(R.id.itemPicture);
            Bitmap photo = (Bitmap) data.getExtras().get("data");
           itempicture.setVisibility(View.VISIBLE);
            itempicture.setImageBitmap(photo);
            btnCamera.setVisibility(View.GONE);


        }
    }



}
