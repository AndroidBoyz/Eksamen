package com.example.bjheggset.buckets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kosalgeek.android.photoutil.CameraPhoto;

import java.io.File;
import java.io.IOException;

import static com.example.bjheggset.buckets.R.drawable.camera;

public class NewItem extends AppCompatActivity {
    final int CAMERA_ITEM = 1999;
    private static final int IMAGE_ITEM = 1888;
    private final String TAG = this.getClass().getName();
    Context $me = this;
    ImageView ivImage, takePhoto, itemInsertImage;
    AlertDialog alertDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        //ivImage = (ImageView) findViewById(R.id.ivImage);
        //takePhoto = (ImageView) findViewById(R.id.itemPhoto);
        //itemInsertImage = (ImageView) findViewById(R.id.itemInsertImage);

/*
        takePhoto.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent("android.media.action.IMAGE_CAPTURE");

                File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));


                startActivityForResult(i, CAMERA_ITEM);

            }
        });

        itemInsertImage.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_ITEM);

            }
        });
*/
    }

    public void SubmitItem(View view) {
        if(!checkRequirements()){
            alertDialog = new android.app.AlertDialog.Builder(this).create();
            alertDialog.setTitle("Buckets");
            alertDialog.setMessage("You have to define a goal to accomplish (item name)");
            alertDialog.show();
            return;
        }
        EditText txtItem = (EditText) findViewById(R.id.txtItem);
        String itemValue = txtItem.getText().toString();

        BackgroundWorker backgroundWorker = new BackgroundWorker($me);
        backgroundWorker.execute("newItem", itemValue);

        txtItem.setText("");

    }

    public static Bitmap decodeSampledBitmapFromFile(String path,
                                                     int reqWidth, int reqHeight) { // BEST QUALITY MATCH


        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);


        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }

        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {

            inSampleSize = Math.round((float)width / (float)reqWidth);
        }


        options.inSampleSize = inSampleSize;


        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_ITEM && resultCode == Activity.RESULT_OK) {


            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
            ivImage.setImageBitmap(decodeSampledBitmapFromFile(file.getAbsolutePath(), 700, 700));
        }

            else if (requestCode == IMAGE_ITEM && resultCode == Activity.RESULT_OK) {
                super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == IMAGE_ITEM && resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                            ivImage.setImageBitmap(bitmap);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }
            }
        }

    protected boolean checkRequirements(){
        TextView txtItem = (TextView) findViewById(R.id.txtItem);
        if(txtItem.getText().toString().equals("")){ return false; }

        return true;
    }

    }










