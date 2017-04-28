package com.example.bjheggset.buckets;

import  android.app.AlertDialog;
import android.app.usage.NetworkStats;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.*;

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
        String str_url="";

        switch(type) {
            //Login:
            case "login":
                str_url = "http://heggset.it/loginBuckets.php";
                try {
                    String userID = params[1];
                    String first_name = params[2];
                    String last_name = params[3];
                    String post_data = "userID=" + URLEncoder.encode(userID, "UTF-8") + "&" + URLEncoder.encode("first_name", "UTF-8") + "=" + URLEncoder.encode(first_name, "UTF-8") + "&" + URLEncoder.encode("last_name", "UTF-8") + "=" + URLEncoder.encode(last_name, "UTF-8");

                    String data = HandleURL(str_url, post_data);
                    data += "!!!login";
                    return data;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            break;

            //New item:
            case "newItem":
                str_url = "http://heggset.it/insert.php";
                try {
                    String itemValue = params[1];
                    String post_data = "itemName=" + URLEncoder.encode(itemValue, "UTF-8") + "&action=item";

                    String data = HandleURL(str_url, post_data);
                    data += "!!!newitem";
                    return data;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            break;

            //Get lists:
            case "getLists":
                str_url = "http://heggset.it/show_list.php";
                try {
                    String userID = params[1];
                    String post_data = "userID=" + URLEncoder.encode(userID, "UTF-8") + "&action=showbuckets";

                    String data = HandleURL(str_url, post_data);

                    data += "!!!showbuckets";

                    return data;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            break;

            //Get items:
            case "getItems":
                str_url = "http://heggset.it/show_list.php";
                try {
                    String bucketID = params[1];
                    String post_data = "bucketID=" + URLEncoder.encode(bucketID, "UTF-8") + "&action=showitems";

                    String data = HandleURL(str_url, post_data);
                    data += "!!!showitems";
                    return data;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            break;

            case "getUnacquired":
                str_url = "http://heggset.it/getItems.php";
                try {
                    String userID = params[1];
                    String bucketID = params[2];
                    String post_data = "userID=" + URLEncoder.encode(userID, "UTF-8") + "&bucketID=" + URLEncoder.encode(bucketID, "UTF-8") + "&action=getUnacquired";

                    String data = HandleURL(str_url, post_data);
                    data += "!!!getUnacquired";

                    return data;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            break;

            case "getAcquired":
                str_url = "http://heggset.it/getItems.php";
                try {
                    String userID = params[1];
                    String bucketID = params[2];
                    String post_data = "userID=" + URLEncoder.encode(userID, "UTF-8") + "&bucketID=" + URLEncoder.encode(bucketID, "UTF-8") + "&action=getAcquired";

                    String data = HandleURL(str_url, post_data);
                    data += "!!!getAcquired";

                    return data;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            break;

            case "saveList":
                str_url = "http://heggset.it/getItems.php";
                try {
                    String userID = params[1];
                    String bucketID = params[2];
                    String items = params[3];
                    String bucketname = params[4];
                    String post_data = "userID=" + URLEncoder.encode(userID, "UTF-8") + "&bucketID=" + URLEncoder.encode(bucketID, "UTF-8") + "&items=" + URLEncoder.encode(items, "UTF-8") + "&bucketname=" + URLEncoder.encode(bucketname, "UTF-8") + "&action=saveList";

                    String data = HandleURL(str_url, post_data);
                    data += "!!!saveList";

                    return data;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case "antallbuckets":
                str_url = "http://heggset.it/stats.php";
                try {
                    String userID = params[1];
                    String post_data = "userID=" + URLEncoder.encode(userID, "UTF-8")+"&action=antallbuckets";

                    String data = HandleURL(str_url, post_data);
                    data += "!!!antallbuckets";

                    return data;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            break;

            case "antallitems":
            str_url = "http://heggset.it/stats.php";
            try {
                String userID = params[1];
                String post_data = "userID=" + URLEncoder.encode(userID, "UTF-8")+"&action=antallitems";

                String data = HandleURL(str_url, post_data);
                data += "!!!antallitems";

                return data;
            } catch (IOException e) {
                e.printStackTrace();
            }

                break;

            case "antallaccomplished":
                str_url = "http://heggset.it/stats.php";
                try {
                    String userID = params[1];
                    String post_data = "userID=" + URLEncoder.encode(userID, "UTF-8")+"&action=antallAccomplished";

                    String data = HandleURL(str_url, post_data);
                    data += "!!!antallaccomplished";

                    return data;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case "getAccomplished":
                str_url = "http://heggset.it/getItems.php";
                try{
                    String userID = params[1];
                    String post_data = "userID=" + URLEncoder.encode(userID, "UTF-8") +"&action=getAccomplished";

                    String data = HandleURL(str_url, post_data);
                    data += "!!!getAccomplished";

                    return data;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            break;

            case "setAccomplished":
                str_url = "http://heggset.it/getItems.php";
                try{
                    String userID = params[1];
                    String itemID = params[2];
                    String post_data = "userID=" + URLEncoder.encode(userID, "UTF-8") +"&itemID=" + URLEncoder.encode(itemID, "UTF-8") +"&action=setAccomplished";

                    String data = HandleURL(str_url, post_data);
                    data += "!!!setAccomplished";

                    return data;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            break;

            default:
                break;
        } // End of switch

        return null;
    }

    private String HandleURL(String str_url, String post_data) {
        try {
            URL url = new URL(str_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            StringBuilder result = new StringBuilder();
            String line="";
            while ((line = bufferedReader.readLine())!=null) {
                result.append(line);
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

            return result.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(String result) {
        String[] resultHandler = result.split("!!!");
        String data = resultHandler[0];
        String action = resultHandler[1];

        switch (action){
            case "showbuckets":
                Intent i = new Intent(context, ListBuckets.class);
                i.putExtra("data", data);
                context.startActivity(i);
                break;
            case "login":
                alertDialog.setMessage(data);
                alertDialog.show();
                break;
            case "showitems":
                break;
            case "newitem":
                alertDialog.setMessage(data);
                alertDialog.show();
                break;
            case "getAcquired":
                Intent i2 = new Intent("Acquired");
                i2.putExtra("data", data);
                LocalBroadcastManager.getInstance(context).sendBroadcast(i2);
                break;
            case "getUnacquired":
                Intent i3 = new Intent("Unacquired");
                i3.putExtra("data", data);
                LocalBroadcastManager.getInstance(context).sendBroadcast(i3);
                break;
            case "saveList":
                alertDialog.setMessage(data);
                alertDialog.show();
                break;
            case "antallbuckets":
            Intent i4 = new Intent("antbuckets");
            i4.putExtra("antbuckets", data);
                LocalBroadcastManager.getInstance(context).sendBroadcast(i4);
                break;
            case "antallitems":
                Intent i5 = new Intent("antitems");
                i5.putExtra("antitems", data);
                LocalBroadcastManager.getInstance(context).sendBroadcast(i5);
            break;
            case "antallaccomplished":
                Intent i7 = new Intent("antaccomplished");
                i7.putExtra("antAccomplished", data);
                LocalBroadcastManager.getInstance(context).sendBroadcast(i7);
            case "getAccomplished":
                Intent i6 = new Intent("Accomplished");
                i6.putExtra("data", data);
                LocalBroadcastManager.getInstance(context).sendBroadcast(i6);
                break;
            case "setAccomplished":
                Toast toast = Toast.makeText(context, data, Toast.LENGTH_SHORT);
                toast.show();

            default:
                break;

        }


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
