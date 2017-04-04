package com.example.bjheggset.buckets;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by bjheggset on 28.03.2017.
 */

public class BackgroundWorker extends AsyncTask<String,Void,String> {
    Context context;
    BackgroundWorker(Context ctx) {
        context = ctx;
    }
    AlertDialog alertDialog;


    @Override
    protected String doInBackground(String... params) {
        String type = params[0];

        if(type.equals("login")) {
            String login_url= "http://heggset.it/loginBuckets.php";
            try {
                String userID = params[1];
                String first_name = params[2];
                String last_name = params[3];


                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data = "userID="+URLEncoder.encode(userID,"UTF-8")+"&"+URLEncoder.encode("first_name","UTF-8")+"="+URLEncoder.encode(first_name,"UTF-8")+"&"+URLEncoder.encode("last_name","UTF-8")+"="+URLEncoder.encode(last_name,"UTF-8");

                //String post_data = "userID="+userID+"&first_name="+first_name+"&last_name="+last_name;

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                String result="";
                String line="";
                while ((line = bufferedReader.readLine())!=null) {
                    result+=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(type.equals("newItem")) {
            String newItem_url = "http://heggset.it/insert.php";
            try {
                String itemValue = params[1];

                URL url = new URL(newItem_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = "itemName="+URLEncoder.encode(itemValue,"UTF-8")+"&action=item";

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                String result="";
                String line="";
                while ((line = bufferedReader.readLine())!=null) {
                    result+=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(type.equals("getLists")) {
            String lists_url= "http://heggset.it/";
            try {
                String userID = params[1];

                URL url = new URL(lists_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data = "userID="+URLEncoder.encode(userID,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                String result="";
                String line="";
                while ((line = bufferedReader.readLine())!=null) {
                    result+=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
       alertDialog.setMessage(result);
        alertDialog.show();
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Buckets");
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
